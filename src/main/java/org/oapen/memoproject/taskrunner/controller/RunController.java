package org.oapen.memoproject.taskrunner.controller;

import java.util.Optional;
import java.util.UUID;

import org.oapen.memoproject.taskrunner.DBService;
import org.oapen.memoproject.taskrunner.TaskLog;
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
    	
    	Optional<Task> task = dbService.findTaskById(id);
    	
    	ResponseEntity<TaskLog> resp = task.map( t -> {
			TaskLog tl = taskManager.runTask(t);
			// maybe log to DB?
			return new ResponseEntity<>(tl,HttpStatus.OK);
    	})
    	.orElse(
    		// return a 404	
    		new ResponseEntity<>(HttpStatus.NOT_FOUND)
    	);
    	
    	return resp; 
    }

}
