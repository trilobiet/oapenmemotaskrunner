package org.oapen.memoproject.taskrunner.jpa;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.oapen.memoproject.taskrunner.DBService;
import org.oapen.memoproject.taskrunner.TaskFrequency;
import org.oapen.memoproject.taskrunner.entities.Task;
import org.springframework.test.util.ReflectionTestUtils;

public class DBServiceImpTest {
	
    private DBService dbService;
    private TaskRepository taskRepository;
    private Task t1, t2, t3, t4;
	
	@BeforeEach
	public void setup() {
		
		t1 = new Task();
		ReflectionTestUtils.setField(t1, "startDate", LocalDate.of(2024, 1, 1));
		ReflectionTestUtils.setField(t1, "frequency", TaskFrequency.Y);
		
		t2 = new Task();
		ReflectionTestUtils.setField(t2, "startDate", LocalDate.of(2024, 2, 1));
		ReflectionTestUtils.setField(t2, "frequency", TaskFrequency.M);

		t3 = new Task();
		ReflectionTestUtils.setField(t3, "startDate", LocalDate.of(2024, 3, 1));
		ReflectionTestUtils.setField(t3, "frequency", TaskFrequency.W);

		t4 = new Task();
		ReflectionTestUtils.setField(t4, "startDate", LocalDate.of(2024, 4, 1));
		ReflectionTestUtils.setField(t4, "frequency", TaskFrequency.D);

		taskRepository = mock(TaskRepository.class);
		when(taskRepository.findAll()).thenReturn(List.of(t1,t2,t3,t4));
		dbService = new DBServiceImp();
		ReflectionTestUtils.setField(dbService, "taskRepository", taskRepository, TaskRepository.class);
		
	}
	
	@Test
    public void test1() {
		
		List<Task> tasks = dbService.getRunnableTasks(LocalDate.of(2025, 1, 1));
		System.out.println(tasks.size());
	
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
