package org.oapen.memoproject.taskrunner.jpa;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.oapen.memoproject.taskrunner.TaskFrequency;
import org.oapen.memoproject.taskrunner.entities.Task;
import org.springframework.test.util.ReflectionTestUtils;

public class TaskTest {
	
	@Test
    public void test1() {
		
		Task t1 = new Task();
		ReflectionTestUtils.setField(t1, "startDate", LocalDate.of(2023, 12, 3));
		ReflectionTestUtils.setField(t1, "frequency", TaskFrequency.Y);
		
		assertTrue( t1.isRunnableOnDate(LocalDate.of(2024,12,3)) );
    }
	
	@Test
    public void test2() {
		
		Task t2 = new Task();
		ReflectionTestUtils.setField(t2, "startDate", LocalDate.of(2024, 2, 29));
		ReflectionTestUtils.setField(t2, "frequency", TaskFrequency.Y);
		
		assertTrue( t2.isRunnableOnDate(LocalDate.of(2028, 2, 29)) );

		assertFalse( t2.isRunnableOnDate(LocalDate.of(2025, 2, 28)) );
		assertFalse( t2.isRunnableOnDate(LocalDate.of(2025, 3, 1)) );
    }

	@Test
    public void test3() {
		
		Task t3 = new Task();
		ReflectionTestUtils.setField(t3, "startDate", LocalDate.of(2024, 2, 29));
		ReflectionTestUtils.setField(t3, "frequency", TaskFrequency.M);

		assertTrue( t3.isRunnableOnDate(LocalDate.of(2024,2,29)) );
		assertTrue( t3.isRunnableOnDate(LocalDate.of(2024,3,29)) );
		assertTrue( t3.isRunnableOnDate(LocalDate.of(2024,4,29)) );
		assertTrue( t3.isRunnableOnDate(LocalDate.of(2025,1,29)) );
		assertTrue( t3.isRunnableOnDate(LocalDate.of(2025,3,29)) );
    }

	@Test
    public void test4() {
		
		Task t4 = new Task();
		ReflectionTestUtils.setField(t4, "startDate", LocalDate.of(2024, 1, 31));
		ReflectionTestUtils.setField(t4, "frequency", TaskFrequency.M);

    	assertTrue( t4.isRunnableOnDate(LocalDate.of(2024,1,31)) );
    	assertTrue( t4.isRunnableOnDate(LocalDate.of(2024,3,31)) );

    	assertFalse( t4.isRunnableOnDate(LocalDate.of(2024,2,29)) );
	}

	@Test
    public void test5() {
		
		Task t5 = new Task();
		ReflectionTestUtils.setField(t5, "startDate", LocalDate.of(2023, 12, 24));
		ReflectionTestUtils.setField(t5, "frequency", TaskFrequency.W);

		assertTrue( t5.isRunnableOnDate(LocalDate.of(2023,12,24)) );
    	assertTrue( t5.isRunnableOnDate(LocalDate.of(2023,12,31)) );
    	assertTrue( t5.isRunnableOnDate(LocalDate.of(2024,1,7)) );
    	assertTrue( t5.isRunnableOnDate(LocalDate.of(2024,2,25)) );
    	assertTrue( t5.isRunnableOnDate(LocalDate.of(2024,3,3)) );
    	
    	assertFalse( t5.isRunnableOnDate(LocalDate.of(2024,3,2)) );
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
