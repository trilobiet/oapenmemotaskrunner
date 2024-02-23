package org.oapen.memoproject.taskrunner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.oapen.memoproject.taskrunner.entities.Query;
import org.oapen.memoproject.taskrunner.entities.Script;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileSystemUtils;

import io.vavr.control.Either;

public final class DockerPythonRunner implements ScriptRunner {

	private final String PYTHON_SCRIPTS_PATH; // = System.getProperty("user.home") + "/tmp_python/";
	private final String DOCKER_IMAGE; // = "oapen/ubuntu2204python310";
	
	private boolean purgeTempFiles = true;
	
	private static final Logger logger = 
			LoggerFactory.getLogger(DockerPythonRunner.class); 
	
	public DockerPythonRunner(String dockerImage, String pythonScriptsPath) {

		DOCKER_IMAGE = dockerImage;
		PYTHON_SCRIPTS_PATH = pythonScriptsPath;
	}
	
	public boolean isPurgeTempFiles() {
		return purgeTempFiles;
	}

	public void setPurgeTempFiles(boolean purgeTempFiles) {
		this.purgeTempFiles = purgeTempFiles;
	}

	@Override
	public Either<String, String> run(ScriptBundler scriptBundle) {

		Either<String, String> result;
		Optional<String> path = Optional.empty();
		
		try {

			path = Optional.of(saveBundleAsFiles(scriptBundle));

			// --rm : delete Docker container when ready
			// -B   : do not write Python cache files for imports
			// -v   : map a local directory to a directory IN the container 
			String cmd = 
				  "docker run --rm " + "-v " + path.get() + ":/root/scripts "
				+ DOCKER_IMAGE + " python3 -B /root/scripts/main.py";

			System.out.println(cmd);
			
			CommandLine cmdLine = CommandLine.parse(cmd);

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);

			DefaultExecutor executor = DefaultExecutor.builder().get();
			executor.setStreamHandler(streamHandler);
			executor.setExitValues(null); // allow all exit values, pass Python errors to Java

			int exitValue = executor.execute(cmdLine);
			String output = outputStream.toString().trim();

			if (exitValue == 0)
				result = Either.right(output);
			else
				result = Either.left("exit value "+ exitValue + ": " + output);

		} catch (IOException e) {

			result = Either.left(e.getMessage());

		} finally {

			if (path.isPresent() && purgeTempFiles) cleanUp(path.get());
		}

		return result;
	}

	private void cleanUp(String path) {

		try {
			FileSystemUtils.deleteRecursively(Path.of(path));
		} catch (IOException e) {
			logger.warn(e.getMessage());
		}
	}

	public String saveBundleAsFiles(ScriptBundler scriptBundle) throws IOException {

		String rootDir = createDirectoryTree();
		Script main = scriptBundle.getScript();

		// main script
		saveSourceFile(rootDir.concat("/").concat("main.py"), main.getBody());

		// included scripts
		for (Script script : scriptBundle.getIncludedScripts()) {
			saveSourceFile(rootDir.concat("/sniplets/").concat(script.getName()), script.getBody());
		}

		// included queries
		for (Query query : scriptBundle.getQueries()) {

			String body = wrapAsVariable("query", query.getBody());
			saveSourceFile(rootDir.concat("/queries/").concat(parseQueryName(query.getName())), body);
		}

		return rootDir;
		 
	}
	
	private String parseQueryName(String qname) {
		
		return (qname.substring(qname.indexOf(":")+1)).toLowerCase();
		
	}
	
	private void saveSourceFile(String name, String content) throws IOException {

		if (!name.endsWith(".py"))
			name = name.concat(".py");

		File scriptFile = new File(name);
		byte[] bytes = content.getBytes();
		Files.write(scriptFile.toPath(), bytes);
	}

	private String wrapAsVariable(String variable, String content) {

		return variable.concat(" = '''\n").concat(content).concat("\n'''");
	}

	private String createDirectoryTree() throws IOException {

		int r = Math.round((int) (Math.random() * 1_000_000_000));
		File rootDir = new File(PYTHON_SCRIPTS_PATH + "/" + r);
		boolean success1 = rootDir.mkdirs();
		File snipletsDir = new File(rootDir.getAbsolutePath().concat("/sniplets"));
		File queriesDir = new File(rootDir.getAbsolutePath().concat("/queries"));

		boolean success2 = snipletsDir.mkdir();
		boolean success3 = queriesDir.mkdir();

		if (!(success1 && success2 && success3))
			throw new IOException("Could not create temporary Python directories");

		return rootDir.getAbsolutePath();
	}


}
