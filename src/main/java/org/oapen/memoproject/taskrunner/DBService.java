package org.oapen.memoproject.taskrunner;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.oapen.memoproject.taskrunner.entities.Query;
import org.oapen.memoproject.taskrunner.entities.RunLog;
import org.oapen.memoproject.taskrunner.entities.Script;
import org.oapen.memoproject.taskrunner.entities.Task;

public interface DBService {

	List<Task> getRunnableTasks(LocalDate date);
	Optional<Task> findTaskById(UUID id);
	Optional<Task> findTaskByName(String name);
	Optional<Query> findQueryByName(String name);
	Optional<Script> findScriptByName(String name);
	void log(RunLog rl);
}