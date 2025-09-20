package org.oapen.memoproject.taskrunner;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;

import org.junit.jupiter.api.Test;
import org.oapen.memoproject.taskrunner.entities.Query;
import org.oapen.memoproject.taskrunner.entities.Script;
import org.springframework.test.util.ReflectionTestUtils;

import io.vavr.control.Either;

public class DockerRunnerTest {
	
	private final String PYTHON_SCRIPTS_PATH = System.getProperty("user.home") + "/tmp_python/";
	private final String DOCKER_IMAGE = "oapen/ubuntu2204python310";

	/*
	 * Docker must be installed, along with image oapen/ubuntu2204python310
	 */
	
	@Test
	public void whenAddImportsBundle_andRunScript_thenImportsMustBeFound() throws Exception {
		
		String mainscript = ""
				+ "from sniplets import incl_1\n"
				+ "from sniplets import incl_2\n"
				+ "from queries import qry_1\n"
				+ "print(qry_1.query)\n";
			
		Script s = new Script();
		ReflectionTestUtils.setField(s, "body", mainscript);
		ReflectionTestUtils.setField(s, "name", "main");

		Script i1 = new Script();
		ReflectionTestUtils.setField(i1, "body", "print('Script 1')");
		ReflectionTestUtils.setField(i1, "name", "incl_1");
		
		Script i2 = new Script();
		ReflectionTestUtils.setField(i2, "body", "print('Script 2')");
		ReflectionTestUtils.setField(i2, "name", "incl_2");
		
		Query q1 = new Query();
		ReflectionTestUtils.setField(q1, "body", "Select 'I am a resultset' ");
		ReflectionTestUtils.setField(q1, "name", "qry_1");
		
		ScriptBundler sb = new ScriptBundler(s).includeScript(i1).includeScript(i2).addQuery(q1);
		
		Either<String, ByteArrayOutputStream> result = 
			new DockerPythonRunner(DOCKER_IMAGE, PYTHON_SCRIPTS_PATH).run(sb);
		
		System.out.println(result);
		
		assertTrue(result.isRight(),"Is Docker installed and running? Does image 'oapen/ubuntu2204python310' exist?");
		assertTrue(result.get().toString().contains("Script 1"));
		assertTrue(result.get().toString().contains("Script 2"));
		assertTrue(result.get().toString().contains("Select 'I am a resultset'"));
	}
	
	@Test
	public void whenScriptContainsFaultyPythonCode_thenReportSyntaxError() throws Exception {
		
		String mainscript = "I am a syntax error";
			
		Script s = new Script();
		ReflectionTestUtils.setField(s, "body", mainscript);
		ReflectionTestUtils.setField(s, "name", "main");
		
		ScriptBundler sb = new ScriptBundler(s);
		
		Either<String, ByteArrayOutputStream> result = 
			new DockerPythonRunner(DOCKER_IMAGE, PYTHON_SCRIPTS_PATH).run(sb);
		
		assertTrue(!result.isRight());
		assertTrue(result.getLeft().contains("SyntaxError"));
	}

	
	@Test
	public void whenScriptCallsImport_whenImportNotAvailable_thenReportError() throws Exception {
		
		String mainscript = "from foo import bar";
			
		Script s = new Script();
		ReflectionTestUtils.setField(s, "body", mainscript);
		ReflectionTestUtils.setField(s, "name", "main");
		
		ScriptBundler sb = new ScriptBundler(s);
		
		Either<String, ByteArrayOutputStream> result = 
			new DockerPythonRunner(DOCKER_IMAGE, PYTHON_SCRIPTS_PATH).run(sb);

		System.out.println(result);
		
		assertTrue(!result.isRight());
		assertTrue(result.getLeft().contains("ModuleNotFoundError"));
	}
	
	
}
