package org.oapen.memoproject.taskrunner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TaskManagerTest {
	
	@Autowired
	private	TaskManager taskmanager;
	
	@BeforeEach
	public void setup() {
		
		//taskmanager = new TaskManager();
	}
	
	
	@Test
	public void test() {
		
		System.out.println(taskmanager);
		
	}
	
	
}
