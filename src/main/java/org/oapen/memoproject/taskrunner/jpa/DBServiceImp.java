package org.oapen.memoproject.taskrunner.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.oapen.memoproject.taskrunner.DBService;
import org.oapen.memoproject.taskrunner.ExportStore;
import org.oapen.memoproject.taskrunner.entities.Export;
import org.oapen.memoproject.taskrunner.entities.Query;
import org.oapen.memoproject.taskrunner.entities.RunLog;
import org.oapen.memoproject.taskrunner.entities.Script;
import org.oapen.memoproject.taskrunner.entities.Task;
import org.springframework.beans.factory.annotation.Autowired;

public class DBServiceImp implements DBService, ExportStore {

	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private QueryRepository queryRepository;

	@Autowired
	private ScriptRepository scriptRepository;

	@Autowired
	private RunLogRepository runLogRepository;
	
	@Autowired
	private ExportRepository exportRepository;
	
	
	@Override
	public List<Task> getTasks() {
		return taskRepository.findAll();
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

	@Override
	public void log(RunLog rl) {
		runLogRepository.save(rl);
	}

	@Override
	@Transactional
	public boolean save(Export export) {
		
		// Delete previous version! 
		// We don't want to keep all history exports
		exportRepository.deleteByTask(export.getTask());
		Export newExport = exportRepository.save(export);
		
		// Some sort of check to see if things went well
		if (newExport.getId() != null) return true;
		else return false;
	}
	
}
