package org.oapen.memoproject.taskrunner.jpa;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.oapen.memoproject.taskrunner.entities.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TaskRepositoryTest {

	@Autowired
    private TaskRepository taskRepository;
	
	@Test
    public void test() {
		
		List<Task> tasks = taskRepository.findAll();
		
		tasks.forEach(System.out::println);
		
    	assertTrue(tasks.size() > 0);
    }

	
}
