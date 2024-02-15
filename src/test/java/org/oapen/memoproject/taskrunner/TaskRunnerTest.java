package org.oapen.memoproject.taskrunner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TaskRunnerTest {
	
	private	TaskManager manager;
	
	@BeforeEach
	public void setup() {
		
		manager = new TaskManager();
	}
	
	@Test
	public void whenScriptSetsResult_thenOutputResult() throws Exception {
		
	}

	@Test
	public void whenScriptSetsNoResult_thenThrowError() throws Exception {
		
	}

	
	@Test
	public void whenScriptHasInvalidSyntax_thenThrowError() {
		
		
	}
	
	
}
