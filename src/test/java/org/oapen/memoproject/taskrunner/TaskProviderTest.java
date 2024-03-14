package org.oapen.memoproject.taskrunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.oapen.memoproject.taskrunner.entities.Task;
import org.springframework.test.util.ReflectionTestUtils;

public class TaskProviderTest {
	
	private DBService mockDBService;
	private TaskProvider taskProvider;
	private Task t1, t2, t3, t4;
	
	@BeforeEach
	public void setup() {
		
		t1 = new Task();
		ReflectionTestUtils.setField(t1, "startDate", LocalDate.of(2024, 1, 3));
		ReflectionTestUtils.setField(t1, "frequency", TaskFrequency.D);
		ReflectionTestUtils.setField(t1, "isActive", true);
		
		t2 = new Task();
		ReflectionTestUtils.setField(t2, "startDate", LocalDate.of(2024, 1, 10));
		ReflectionTestUtils.setField(t2, "frequency", TaskFrequency.W);
		ReflectionTestUtils.setField(t2, "isActive", true);

		t3 = new Task();
		ReflectionTestUtils.setField(t3, "startDate", LocalDate.of(2024, 2, 7));
		ReflectionTestUtils.setField(t3, "frequency", TaskFrequency.M);
		ReflectionTestUtils.setField(t3, "isActive", true);

		t4 = new Task();
		ReflectionTestUtils.setField(t4, "startDate", LocalDate.of(2025, 2, 5));
		ReflectionTestUtils.setField(t4, "frequency", TaskFrequency.Y);
		ReflectionTestUtils.setField(t4, "isActive", true);

		mockDBService = mock(DBService.class);
		taskProvider = new TaskProviderImp();
		ReflectionTestUtils.setField(taskProvider, "dbService", mockDBService);
		
		when(mockDBService.getTasks()).thenReturn(List.of(t1,t2,t3,t4));
	}
	
	
	@Test
    public void whenStartDate_thenTask1() {

		List<Task> runnableTasks = taskProvider.getRunnableTasks(LocalDate.of(2024,12,3));
		
		assertEquals(1, runnableTasks.size());
		assertTrue(runnableTasks.containsAll(List.of(t1)));
    }
	
	@Test
    public void whenOneWeekLater_thenTask1And2() {
		
		List<Task> runnableTasks = taskProvider.getRunnableTasks(LocalDate.of(2024, 1, 10));
		
		assertEquals(2, runnableTasks.size());
		assertTrue(runnableTasks.containsAll(List.of(t1,t2)));
    }

	@Test
    public void whenOneMonthlater_thenTask1And2And3() {
		
		List<Task> runnableTasks = taskProvider.getRunnableTasks(LocalDate.of(2024, 2, 7));
		
		assertEquals(3, runnableTasks.size());
		assertTrue(runnableTasks.containsAll(List.of(t1,t2,t3)));
    }

	@Test
    public void whenOneYearLaterSameWeekday_thenTask1And2And4_butNot3() {
		
		List<Task> runnableTasks = taskProvider.getRunnableTasks(LocalDate.of(2025, 2, 5));
		
		assertEquals(3, runnableTasks.size());
		assertTrue(runnableTasks.containsAll(List.of(t1,t2,t4)));
    }
	
	@Test
    public void when12MonthsLater_thenTask1And3() {
		
		List<Task> runnableTasks = taskProvider.getRunnableTasks(LocalDate.of(2025, 2, 7));
		
		assertEquals(2, runnableTasks.size());
		assertTrue(runnableTasks.containsAll(List.of(t1,t3)));
    }

	@Test
    public void when12MonthsAndOneDayLater_thenTask1() {
		
		List<Task> runnableTasks = taskProvider.getRunnableTasks(LocalDate.of(2025, 2, 8));
		
		assertEquals(1, runnableTasks.size());
		assertTrue(runnableTasks.containsAll(List.of(t1)));
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
