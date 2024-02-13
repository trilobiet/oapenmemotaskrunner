package org.oapen.memoproject.taskrunner;

import java.time.LocalDate;
import java.util.List;

import org.oapen.memoproject.taskrunner.entities.RunLog;
import org.oapen.memoproject.taskrunner.entities.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskHandler  {
	
	@Autowired
	DBService dbService;
	
	@Autowired
	TaskRunner scriptRunner;
	
	public void run() {
		
		List<Task> tasks = getRunnableTasks();
		
		tasks.forEach(task -> writeRunLog(scriptRunner.runTask(task)));
	}
	
	
	public List<Task> getRunnableTasks() {

		return dbService.getRunnableTasks(LocalDate.now());
	}


	public void writeRunLog(RunLog rl) {

		dbService.log(rl);
	}

}
