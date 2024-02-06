package org.oapen.memoproject.taskrunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class TaskFrequencyTest {

	private final LocalDate dt2024_12_23 = LocalDate.of(2023,12,23);
	private final LocalDate dt2025_01_29 = LocalDate.of(2025,01,29);
	private final LocalDate dt2024_02_29 = LocalDate.of(2024,02,29);
	
	@Test
	public void testFirstAfterForYear () {
		
		// YEARS --------------------------------------
		assertEquals(TaskFrequency.Y.firstAfter(dt2024_12_23, LocalDate.of(2023,12,25)), LocalDate.of(2024,12,23));
		assertEquals(TaskFrequency.Y.firstAfter(dt2024_12_23, LocalDate.of(2024,01,14)), LocalDate.of(2024,12,23));
		assertEquals(TaskFrequency.Y.firstAfter(dt2024_12_23, LocalDate.of(2024,12,25)), LocalDate.of(2025,12,23));
		assertEquals(TaskFrequency.Y.firstAfter(dt2024_12_23, LocalDate.of(2024,02,29)), LocalDate.of(2024,12,23));
		assertEquals(TaskFrequency.Y.firstAfter(dt2024_02_29, LocalDate.of(2024,03,01)), LocalDate.of(2025,02,28));
	}	
		
	@Test
	public void testFirstAfterForMonth () {
		
		// MONTHS --------------------------------------
		assertEquals(TaskFrequency.M.firstAfter(dt2024_12_23, LocalDate.of(2023,12,25)), LocalDate.of(2024,01,23));
		assertEquals(TaskFrequency.M.firstAfter(dt2024_12_23, LocalDate.of(2024,03,24)), LocalDate.of(2024,04,23));
		assertEquals(TaskFrequency.M.firstAfter(dt2024_12_23, LocalDate.of(2024,12,24)), LocalDate.of(2025,01,23));
		assertEquals(TaskFrequency.M.firstAfter(dt2024_02_29, LocalDate.of(2024,03,01)), LocalDate.of(2024,03,29));
		assertEquals(TaskFrequency.M.firstAfter(dt2025_01_29, LocalDate.of(2025,01,30)), LocalDate.of(2025,02,28));
	}	
	
	@Test
	public void testFirstAfterForWeek () {

		// WEEKS --------------------------------------");
		assertEquals(TaskFrequency.W.firstAfter(dt2024_12_23, LocalDate.of(2023,12,25)), LocalDate.of(2023,12,30));
		assertEquals(TaskFrequency.W.firstAfter(dt2024_12_23, LocalDate.of(2024,01,14)), LocalDate.of(2024,01,20));
		assertEquals(TaskFrequency.W.firstAfter(dt2024_12_23, LocalDate.of(2024,12,25)), LocalDate.of(2024,12,28));
		assertEquals(TaskFrequency.W.firstAfter(dt2024_02_29, LocalDate.of(2025,03,01)), LocalDate.of(2025,03,06));
	}	
	
	@Test
	public void testFirstAfterForDay () {
		
		// DAYS --------------------------------------");
		assertEquals(TaskFrequency.D.firstAfter(dt2024_12_23, LocalDate.of(2023,12,25)), LocalDate.of(2023,12,26));
		assertEquals(TaskFrequency.D.firstAfter(dt2024_12_23, LocalDate.of(2024,12,31)), LocalDate.of(2025,01,01));
		assertEquals(TaskFrequency.D.firstAfter(dt2024_12_23, LocalDate.of(2024,02,28)), LocalDate.of(2024,02,29));
		assertEquals(TaskFrequency.D.firstAfter(dt2024_12_23, LocalDate.of(2024,02,29)), LocalDate.of(2024,03,01));
	}
	
	

}
