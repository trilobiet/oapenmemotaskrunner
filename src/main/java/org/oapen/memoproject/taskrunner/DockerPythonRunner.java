package org.oapen.memoproject.taskrunner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
	public Either<String, ByteArrayOutputStream> run(ScriptBundler scriptBundle) {

		Either<String, ByteArrayOutputStream> result;
		Optional<String> path = Optional.empty();
		int containerName = Math.round((int) (Math.random() * 1_000_000_000));
		
		try {
			
			path = Optional.of(saveBundleAsFiles(scriptBundle));

			// -tty           : allocate a pseudo-TTY (https://docs.docker.com/reference/cli/docker/container/run/#tty)
			//                  without this option output may get cut off when it is too long
			//                  (this option is the same as -t).
			// --name         : (random) name to identify the container by for later closing.				
			// --rm           : delete Docker container when ready 
			//                  (Do NOT use this: it may close the container before output is fully read.
			//                   instead close the container explicitly, by name, after closing the output stream)
			// --network=host : connect from Docker container to MySQL instance on localhost 127.0.0.1
			//                  https://stackoverflow.com/questions/24319662/from-inside-of-a-docker-container-how-do-i-connect-to-the-localhost-of-the-mach 
			// -B             : do not write Python cache files for imports
			// -v             : map a local directory to a directory IN the container 
			String cmd = 
				  "docker run --tty --name " + containerName + " --network=host -v " + path.get() + ":/root/scripts "
				+ DOCKER_IMAGE + " python3 -B /root/scripts/main.py";

			// https://www.baeldung.com/java-working-with-python
			CommandLine cmdLine = CommandLine.parse(cmd);
			
			// Prevent OOM erors by writing to temp disk file
			//FileOutputStream outputStream = new FileOutputStream("/home/acdhirr/burp.txt");
			//PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
			
			DefaultExecutor executor = DefaultExecutor.builder().get();
			executor.setStreamHandler(streamHandler);
			executor.setExitValues(null); // allow all exit values, pass Python errors to Java
			
			logger.info("Spinning up Python script in container [" + cmd + "]");

			int exitValue = executor.execute(cmdLine);
			
			if (exitValue == 0) { 
				logger.info("Size of output stream: " + outputStream.size());
				result = Either.right(outputStream);
			}	
			else {
				result = Either.left("exit value "+ exitValue + ": " + outputStream.toString());
				outputStream.close();
			}

		} catch (Exception e) {

			result = Either.left(e.getMessage());

		} finally {

			// We can clean up the temp files right away, they only contain code
			if (path.isPresent() && purgeTempFiles) cleanUpTempFiles(path.get());

			// Remove the container after x seconds so to make sure the output has been captured
			scheduleContainerCleanup(containerName, 30);

			logger.info("Script running completed");
		}

		return result;
	}
	

	private void cleanUpTempFiles(String path) {

		try {
			FileSystemUtils.deleteRecursively(Path.of(path));
		} catch (IOException e) {
			logger.warn(e.getMessage());
		}
	}

	
	private void scheduleContainerCleanup(int name, int seconds) {

		ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		
		scheduledExecutorService.schedule(
		    () -> {
		    	try {
					DefaultExecutor.builder().get().execute(CommandLine.parse("docker rm " + name));
					logger.info("Container " + name + " successfully removed");
					scheduledExecutorService.shutdown();
				} catch (IOException e) { 
					logger.warn("Could not remove container " + name);
				}
		    } ,
		    seconds, TimeUnit.SECONDS 
		); 
	}
	
	
	public String saveBundleAsFiles(ScriptBundler scriptBundle) throws IOException {

		String rootDir = createRunRoot();
		Script main = scriptBundle.getScript();
		
		// send out a warning when the script is empty
		if (main.getBody() == null) 
			throw new RuntimeException("Don't know what to run - main script contains no code...");

		// main script
		saveSourceFile(rootDir.concat("/").concat("main.py"), main.getBody());

		// included scripts
		for (Script script : scriptBundle.getIncludedScripts()) {
			saveSourceFile(rootDir.concat("/sniplets/").concat(script.getName()), script.getBody());
		}

		// included queries
		for (Query query : scriptBundle.getQueries()) {

			String body = wrapAsVariable("query", query.getBody());
			saveSourceFile(rootDir.concat("/queries/").concat(queryNameToPath(query.getName())), body);
		}

		return rootDir;
		 
	}
	

	private String queryNameToPath(String qname) {
		
		return qname.replace(":","/");
	}

	
	private void saveSourceFile(String path, String content) throws IOException {
		
		if (path != null) {

			if (!path.endsWith(".py")) path = path.concat(".py");
	
			File file = new File(path);
			file.getParentFile().mkdirs();
			byte[] bytes = Objects.toString(content,"").getBytes();
			Files.write(file.toPath(), bytes);
		}	
	}

	
	private String wrapAsVariable(String variable, String content) {
		
		if (variable != null && content != null) 
			return variable.concat(" = '''\n").concat(content).concat("\n'''");
		else 
			return ""; 
	}

	
	private String createRunRoot() throws IOException {

		int r = Math.round((int) (Math.random() * 1_000_000_000));
		File rootDir = new File(PYTHON_SCRIPTS_PATH + "/" + r);
		if (!rootDir.mkdirs()) 
			throw new IOException("Could not create temporary run directory");
		
		return rootDir.getAbsolutePath();
	}

}
