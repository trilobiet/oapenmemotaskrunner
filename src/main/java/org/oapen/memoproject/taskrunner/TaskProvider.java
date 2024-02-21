package org.oapen.memoproject.taskrunner;

import java.time.LocalDate;
import java.util.List;

import org.oapen.memoproject.taskrunner.entities.Task;

public interface TaskProvider {
	
	List<Task> getRunnableTasks(LocalDate date);
}
