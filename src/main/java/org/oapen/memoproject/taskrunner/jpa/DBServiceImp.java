package org.oapen.memoproject.taskrunner.jpa;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.oapen.memoproject.taskrunner.DBService;
import org.oapen.memoproject.taskrunner.entities.Query;
import org.oapen.memoproject.taskrunner.entities.RunLog;
import org.oapen.memoproject.taskrunner.entities.Script;
import org.oapen.memoproject.taskrunner.entities.Task;
import org.springframework.beans.factory.annotation.Autowired;

public class DBServiceImp implements DBService {

	@Autowired
	TaskRepository taskRepository;
	
	@Autowired
	QueryRepository queryRepository;

	@Autowired
	ScriptRepository scriptRepository;

	@Autowired
	RunLogRepository runLogRepository;
	
	@Override
	public List<Task> getRunnableTasks() {
		
		List<Task> tasks = taskRepository.findAll();
		
		LocalDate today = LocalDate.now();
		LocalDate tomorrow = LocalDate.now().plusDays(1);
		
		List<Task> tasksToRun = tasks.stream()
			.filter(task -> task.getStartDate() != null)
			// startDate is today or earlier
			.filter(task -> task.getStartDate().isBefore(tomorrow))
			.filter(task ->
				// task hasn't run yet
				task.getLastRunDate() == null ||
				// task frequency has completed a cycle today, given start date
				task.getFrequency().firstAfter(task.getStartDate(), task.getLastRunDate()).isEqual(today)
			)
			.collect(Collectors.toList())
			;
		
		return tasksToRun;
	}
	
	@Override
	public void log(RunLog rl) {

		runLogRepository.save(rl);
	}

	@Override
	public Optional<Task> findTaskById(UUID id) {
		return taskRepository.findById(id);
	}

	@Override
	public Optional<Task> findTaskByName(String name) {
		
		return taskRepository.findByFileName(name);
	}

	@Override
	public Optional<Query> findQueryByName(String name) {
		
		return queryRepository.findByName(name);
	}

	@Override
	public Optional<Script> findScriptByName(String name) {
		
		return scriptRepository.findByName(name);
	}
	
}
