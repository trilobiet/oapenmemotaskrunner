package org.oapen.memoproject.taskrunner.controller;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.oapen.memoproject.taskrunner.DBService;
import org.oapen.memoproject.taskrunner.TaskManager;
import org.oapen.memoproject.taskrunner.TaskResult;
import org.oapen.memoproject.taskrunner.entities.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Allows users to call running or dry running tasks by http.
 * (Scheduled task do not use this controller.)
 * 
 */
@RestController
public class RunController {
	
	@Autowired
	private TaskManager taskManager;
	
	@Autowired
	private DBService dbService;
	
	@Autowired
	private Environment env;	
	
	private final Map<String,String> msg404 = new HashMap<>();
	private final Map<String,String> msgBusy = new HashMap<>();
	
	{
		msg404.put("message","Resource not found");
		msgBusy.put("message","Taskrunner does not accept client calls during scheduled hours");
	}
	
	private static final Logger logger = 
		LoggerFactory.getLogger(RunController.class);
	

	
    /**
     * Run a task, and log result. Save the export file to its client directory destination. 
     * When successful, only return status HTTP OK.
     * Otherwise return a JSON response describing the error.
     *   
     * @param id
     * @return
     */
    @GetMapping(value = "/runtask/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> runTask(@PathVariable UUID id) {
    	
    	return runTask(id, false);
    }

    
    /**
     * Dry run a task, but do NOT log result. Save the export file to a temp directory.
     * When successful, only return status HTTP OK.
     * Otherwise return a JSON response describing the error.
     *   
     * @param id
     * @return
     */
    @GetMapping(value = "/dryruntask/{id}")
    @Transactional(timeout = 300_000)
    @ResponseBody
    public ResponseEntity<?> dryRunTask(@PathVariable UUID id) {
    	
    	return runTask(id, true);
    }
    
    
    private ResponseEntity<?> runTask(UUID id, boolean isDry) {
    	
    	// refuse running task when scheduled taskrunner is busy
    	if (inBusyPeriod()) return new ResponseEntity<>(msgBusy, HttpStatus.SERVICE_UNAVAILABLE);
    	
    	Optional<Task> oTask = dbService.findTaskById(id);
    	
    	ResponseEntity<Object> resp = oTask.map( task -> {
    		
    		String runtype = isDry?"DRY":"";
    		
    		logger.info("====================== START USER REQUESTED {} RUN ======================", runtype);
    		logger.info("Task " + task.getFileName() + " for client " + task.getUsername());
			
    		TaskResult taskResult = taskManager.runTask(task);
    		
    		logger.info(task.getFileName() + ": " + (taskResult.isSuccess()?"OK":"FAIL"));
    		if (!taskResult.isSuccess()) logger.info("Message: " + taskResult.getMessage());
    		logger.info("======================= END USER REQUESTED {} RUN =======================", runtype);
    		
    		// Write runlog line (DB) UNLESS we are running dry
			if (!isDry) taskManager.logTaskResult(taskResult);

			if (taskResult.isSuccess()) {

				String path = task.getPath();
				if (isDry) path = "tmp/" + task.getFlattenedPath();
				
				// ResponseEntity contains info about the run, but *not* the result! 
				// (It could get too big, so it is only available through download)  
				Optional<String> p = taskManager.saveToFile(taskResult.getOutput(), path);
				if (p.isPresent()) logger.error(p.get());
				taskResult.setOutput(null); // remove data here
				return new ResponseEntity<Object>(taskResultToJson(taskResult), HttpStatus.OK);
			}
			else {
				
				return new ResponseEntity<Object>(taskResultToJson(taskResult), HttpStatus.INTERNAL_SERVER_ERROR);
			}
    	})
    	.orElse(
    		// return a 404	
    		new ResponseEntity<>(HttpStatus.NOT_FOUND)
    	);
    	
    	return resp; 
    }
    
    
    private String taskResultToJson(TaskResult taskResult) {
    	
		ObjectMapper om = new ObjectMapper();
		om.registerModule(new JavaTimeModule()).configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);;

		try {
			// System.out.println(om.writeValueAsString(taskResult));
			return om.writeValueAsString(taskResult);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "{'error':'could not serialize as Json'}"; 
		}
    	
    }
    
    /**
     * From start for n hours service is not available for client calls 
     * @return
     */
    private boolean inBusyPeriod() {
    
    	LocalTime start = LocalTime.parse(env.getProperty("taskRunner.busy.starttime")); // 00:00:00 
    	LocalTime stop = start.plusHours(Integer.parseInt(env.getProperty("taskRunner.busy.hours")));
    	LocalTime now = LocalTime.now();
    	
    	return (now.isAfter(start) && now.isBefore(stop));
    }
    
}
