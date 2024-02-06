package org.oapen.memoproject.taskrunner;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public enum TaskFrequency {
	
	Y(ChronoUnit.YEARS), 
	M(ChronoUnit.MONTHS),
	W(ChronoUnit.WEEKS), 
	D(ChronoUnit.DAYS);
	
	private final ChronoUnit chronoUnit;
	
	TaskFrequency(ChronoUnit chronoUnit) {
		this.chronoUnit = chronoUnit;
	}
	
	public ChronoUnit getChronoUnit() {
		return chronoUnit;
	}

	public LocalDate after(LocalDate date) {
		return date.plus(1, this.chronoUnit);
	}
	
	public LocalDate firstAfter(LocalDate startDate, LocalDate afterDate) {
		
		long unitsBetween = this.chronoUnit.between(startDate, afterDate);
		return startDate.plus(unitsBetween + 1, this.chronoUnit);
	}
	
}
