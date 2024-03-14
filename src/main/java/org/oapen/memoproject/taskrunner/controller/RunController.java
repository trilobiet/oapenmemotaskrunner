package org.oapen.memoproject.taskrunner.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.oapen.memoproject.taskrunner.DBService;
import org.oapen.memoproject.taskrunner.MimeTypeService;
import org.oapen.memoproject.taskrunner.TaskManager;
import org.oapen.memoproject.taskrunner.TaskResult;
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
	
	@Autowired
	private MimeTypeService mimeTypeService;
	
    @GetMapping(value = "/runtask/{id}")
    @ResponseBody
    /**
     * Run a task, and log result. When successful, only return status HTTP OK.
     * Otherwise return a JSON response describing the error.
     *   
     * @param id
     * @return
     */
    public ResponseEntity<?> runTask( @PathVariable UUID id ) {
    	
    	Optional<Task> oTask = dbService.findTaskById(id);
    	
    	ResponseEntity<?> resp = oTask.map( task -> {
			
    		TaskResult taskResult = taskManager.runTask(task);
    		
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
    		//new ResponseEntity<>(HttpStatus.NOT_FOUND)
    	);
    	
    	return resp; 
    }

    
    @GetMapping(value = "/dryruntask/{id}")
    @ResponseBody
    /**
     * Run a task, but do not log nor save results. When successful, return body as a downloadable file.
     * Otherwise return a JSON response describing the error.
     *  
     * @param id
     * @return
     */
    public ResponseEntity<?> dryRunTask( @PathVariable UUID id ) {
    	
    	Optional<Task> oTask = dbService.findTaskById(id);
    	
    	System.out.println(oTask);
    	
    	Map<String,String> msg404 = new HashMap<>();
    	msg404.put("message","Resource not found");
    	
    	ResponseEntity<?> resp = oTask.map( task -> {
			
    		TaskResult taskResult = taskManager.runTask(task);
    		
			if (taskResult.isSuccess()) { 
				
				String fileName = task.getFileName();
				String mimeType = mimeTypeService.getMimeTypeFromFileName(fileName);
				
				return ResponseEntity.ok()
					.header("Content-Type", mimeType + ";charset=utf-8")
					.header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
					.body(taskResult.getOutput());
			}
			else 
				return new ResponseEntity<>(taskResult, HttpStatus.INTERNAL_SERVER_ERROR);
    	})
    	.orElse( 
    		// return a 404	
    		new ResponseEntity<>(msg404, HttpStatus.NOT_FOUND)
    	);
    	
    	return resp; 
    }
    
}
