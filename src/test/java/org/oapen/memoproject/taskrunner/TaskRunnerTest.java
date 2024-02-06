package org.oapen.memoproject.taskrunner;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.oapen.memoproject.taskrunner.entities.Script;
import org.springframework.test.util.ReflectionTestUtils;

import jep.JepException;

public class TaskRunnerTest {
	
	private	TaskRunner runner;
	
	@BeforeEach
	public void setup() {
		
		runner = new TaskRunner();
	}
	
	@Test
	public void whenScriptSetsResult_thenOutputResult() throws Exception {
		
		Script script = new Script();
		String body = "result = 'test'";
		ReflectionTestUtils.setField(script, "body", body, String.class);
		ScriptBundler sb = new ScriptBundler(script);
		String scriptResult = runner.run(sb);
		assertEquals(scriptResult, "test");
	}

	@Test
	public void whenScriptSetsNoResult_thenThrowError() throws Exception {
		
		Script script = new Script();
		String body = "blabla = 'test'";
		ReflectionTestUtils.setField(script, "body", body, String.class);
		ScriptBundler sb = new ScriptBundler(script);

		JepException thrown = 
			assertThrows(JepException.class, () -> runner.run(sb), "JepException was expected");
		
		// System.out.println(thrown.getMessage());
		assertTrue(thrown.getMessage().contains("NameError"));
		
	}

	
	@Test
	public void whenScriptHasInvalidSyntax_thenThrowError() {
		
		Script script = new Script();
		String body = "blabla = +%this_is_invalid_syntax+3634!&"
				+ "result = 'hello'"
				+ "";
				
		ReflectionTestUtils.setField(script, "body", body, String.class);
		ScriptBundler sb = new ScriptBundler(script);
	
		JepException thrown = 
			assertThrows(JepException.class, () -> runner.run(sb), "JepException was expected");
		
		// System.out.println(thrown.getMessage());
		assertTrue(thrown.getMessage().contains("SyntaxError"));
		
	}
	
	
}
