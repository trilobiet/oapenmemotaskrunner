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
	private TaskRepository taskRepository;
	
	@Autowired
	private QueryRepository queryRepository;

	@Autowired
	private ScriptRepository scriptRepository;

	@Autowired
	private RunLogRepository runLogRepository;
	
	@Override
	public List<Task> getRunnableTasks(LocalDate date) {
		
		List<Task> tasks = taskRepository.findAll().stream()
			.filter(task -> task.isRunnableOnDate(date))
			.collect(Collectors.toList());
		
		return tasks;
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
