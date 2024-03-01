package org.oapen.memoproject.taskrunner.controller;

import java.util.Optional;
import java.util.UUID;

import org.oapen.memoproject.taskrunner.DBService;
import org.oapen.memoproject.taskrunner.TaskResult;
import org.oapen.memoproject.taskrunner.TaskManager;
import org.oapen.memoproject.taskrunner.entities.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RunController {
	
	@Autowired
	private TaskManager taskManager;
	
	@Autowired
	private DBService dbService;
	
    @GetMapping(value = "/runtask/{id}")
    @ResponseBody
    public ResponseEntity<?> runTask( @PathVariable UUID id ) {
    	
    	Optional<Task> oTask = dbService.findTaskById(id);
    	
    	ResponseEntity<TaskResult> resp = oTask.map( task -> {
			
    		TaskResult taskResult = taskManager.runTask(task);
    		
    		System.out.println(taskResult);
    		
			taskManager.logTaskResult(taskResult);

			if (taskResult.isSuccess()) {
				
				taskManager.saveExport(task, taskResult);
				return new ResponseEntity<>(taskResult, HttpStatus.OK);
			}
			else {
				
				return new ResponseEntity<>(taskResult, HttpStatus.INTERNAL_SERVER_ERROR);
			}
    	})
    	.orElse(
    		// return a 404	
    		new ResponseEntity<>(HttpStatus.NOT_FOUND)
    	);
    	
    	return resp; 
    }

}
