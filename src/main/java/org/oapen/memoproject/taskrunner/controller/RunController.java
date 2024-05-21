package org.oapen.memoproject.taskrunner.controller;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.oapen.memoproject.taskrunner.DBService;
import org.oapen.memoproject.taskrunner.MimeTypeService;
import org.oapen.memoproject.taskrunner.TaskManager;
import org.oapen.memoproject.taskrunner.TaskResult;
import org.oapen.memoproject.taskrunner.entities.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Allows clients to call running or dry running tasks by http
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
	
	@Autowired
	private MimeTypeService mimeTypeService;
	
	private final Map<String,String> msg404 = new HashMap<>();
	private final Map<String,String> msgBusy = new HashMap<>();
	
	{
		msg404.put("message","Resource not found");
		msgBusy.put("message","Taskrunner does not accept client calls during scheduled hours");
	}
	
	private static final Logger logger = 
		LoggerFactory.getLogger(RunController.class);
	
	
    @GetMapping(value = "/runtask/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    /**
     * Run a task, and log result. When successful, only return status HTTP OK.
     * Otherwise return a JSON response describing the error.
     *   
     * @param id
     * @return
     */
    public ResponseEntity<Object> runTask( @PathVariable UUID id ) {
    	
    	// refuse running task when scheduled taskrunner is busy
    	if (inBusyPeriod()) return new ResponseEntity<>(msgBusy, HttpStatus.SERVICE_UNAVAILABLE);
    	
    	Optional<Task> oTask = dbService.findTaskById(id);
    	
    	ResponseEntity<Object> resp = oTask.map( task -> {
    		
    		logger.info("Start user requested running task " + task.getFileName() + " for client " + task.getUsername());
			
    		TaskResult taskResult = taskManager.runTask(task);
    		
    		logger.info("Finished user requested running task " + task.getFileName() + ": " + (taskResult.isSuccess()?"OK":"FAIL"));
    		
    		// Write runlog line (DB)
			taskManager.logTaskResult(taskResult);

			if (taskResult.isSuccess()) {
				
				Optional<String> p = taskManager.saveToFile(taskResult.getOutput(), task.getPath());
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
    	
    	// refuse running task when scheduled taskrunner is busy
    	if (inBusyPeriod()) return new ResponseEntity<>(msgBusy, HttpStatus.SERVICE_UNAVAILABLE);
    	
    	Optional<Task> oTask = dbService.findTaskById(id);
    	
    	ResponseEntity<?> resp = oTask.map( task -> {
    		
    		logger.info("Start user requested dry running task on request " + task.getFileName() + " for client " + task.getUsername());
			
    		TaskResult taskResult = taskManager.runTask(task);
    		
    		logger.info("Finished user requested dry running task " + task.getFileName() + ": " + (taskResult.isSuccess()?"OK":"FAIL"));
    		
			if (taskResult.isSuccess()) { 
				
				String fileName = task.getFileName();
				String mimeType = mimeTypeService.getMimeTypeFromFileName(fileName);
				
				/*
				return ResponseEntity.ok()
					.header("Content-Type", mimeType + ";charset=utf-8")
					.header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
					.body(taskResult.getOutput().toString());
				*/
				
				BodyBuilder bb = ResponseEntity.ok()
					.header("Content-Type", mimeType + ";charset=utf-8")
					.header("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

				// No need to close, Spring handles this
				ByteArrayResource res = new ByteArrayResource(taskResult.getOutput().toByteArray());
				return bb.body(res);
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
    
    
    private String taskResultToJson(TaskResult taskResult) {
    	
		ObjectMapper om = new ObjectMapper();
		om.registerModule(new JavaTimeModule()).configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);;

		try {
			System.out.println(om.writeValueAsString(taskResult));
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
