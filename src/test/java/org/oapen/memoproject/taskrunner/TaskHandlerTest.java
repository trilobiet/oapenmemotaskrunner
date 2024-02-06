package org.oapen.memoproject.taskrunner;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.oapen.memoproject.taskrunner.entities.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TaskHandlerTest {
	
	@Autowired
    private TaskHandler taskService;
	
	@Test
    public void test() {
		
		List<Task> tasks = taskService.getRunnableTasks();
		
		tasks.forEach(task -> {
			System.out.println(	
			     task.getUsername() + "/" + task.getFileName() + " " 
			   + task.getFrequency().after(LocalDate.now()) + " "
			   + task.getLastRunDate()
			);
		});
		
		LocalDate today = LocalDate.now();
		//LocalDate yesterday = LocalDate.now().minusDays(1);
		LocalDate tomorrow = LocalDate.now().plusDays(1);
		
		List<Task> tasksToRun = tasks.stream()
			// startDate is today or earlier
			.filter(task -> task.getStartDate().isBefore(tomorrow))
			.filter(task ->
				task.getLastRunDate() == null ||
				task.getFrequency().after(task.getLastRunDate()).isBefore(today)
			)
			.collect(Collectors.toList())
			;
			
		System.out.println(tasksToRun);
			
		
    	assertTrue(tasks.size() > 0);
    }
	
	/**
	 *   |<------------- freq ------------>|
	 *   
	 *                                     | today  
	 *                                     V  
	 * --|---------------------------------|---------------------------------|---------------------------------
	 *   3 dec                             3 jan
	 * 
	 */

}
