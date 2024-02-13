package org.oapen.memoproject.taskrunner;

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
	
}
