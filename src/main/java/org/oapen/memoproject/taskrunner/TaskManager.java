package org.oapen.memoproject.taskrunner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.oapen.memoproject.taskrunner.entities.Query;
import org.oapen.memoproject.taskrunner.entities.Script;
import org.oapen.memoproject.taskrunner.entities.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import io.vavr.control.Either;

@Component
public class TaskManager  {
	
	@Autowired
	private TaskProvider taskProvider;
	
	@Autowired
	private TaskLogger taskLogger;
	
	@Autowired
	private Environment env;	
	
	@Autowired
	private DependenciesCollector dpdCollector;
	
	private static final Logger logger = 
			LoggerFactory.getLogger(DockerPythonRunner.class); 
	
	
	public void runTasks() {
		
		List<Task> tasks = taskProvider.getRunnableTasks(LocalDate.now());
		
		for (Task task: tasks) {
			
    		logger.info("====================== START SCHEDULED RUN ======================");
    		logger.info("Task " + task.getFileName() + " for client " + task.getUsername());
			
			TaskResult taskResult = runTask(task);
			
    		logger.info(task.getFileName() + ": " + (taskResult.isSuccess()?"OK":"FAIL"));
    		if (!taskResult.isSuccess()) logger.info("Message: " + taskResult.getMessage());
    		logger.info("======================= END SCHEDULED RUN =======================");
			
			Optional<String> saveError = saveToFile(taskResult.getOutput(), task.getPath());
			
			if (!saveError.isEmpty()) taskResult.fail(saveError.get());
		
			// Finally log
			logTaskResult(taskResult);
		}
	}
	
	
	public void logTaskResult(TaskResult tr) {
		
		taskLogger.log(tr);
	}
	
	
	public TaskResult runTask(Task task) {
		
		ScriptBundler sb = toBundle(task.getScript());
		TaskResult taskResult = TaskResult.builder().idTask(task.getId()).build();
		
		Map<String, List<String>> illegalInstructions = CodeGuard.illegalInstructions(sb);
		
		if (!illegalInstructions.isEmpty())  
		
			taskResult.fail("Illegal instructions found: " + illegalInstructions.toString());

		else {
			
			DockerPythonRunner runner = 
				new DockerPythonRunner(env.getProperty("docker.image.python"), env.getProperty("path.temp.pythonscripts"));
			
			// remove temp directories? (set false for testing)
			runner.setPurgeTempFiles(env.getProperty("path.temp.pythonscripts.purge", Boolean.class));
			
			// Now run the task...!
			Either<String, ByteArrayOutputStream> output = runner.run(sb);
			
			if (output.isRight()) 
				taskResult.succeed("OK",output.get());
			else 
				taskResult.fail(output.getLeft()); 
		}
		
		taskResult.setDateTime(LocalDateTime.now());
		return taskResult;
	}
	
	
	// On success return empty Optional, on failure an error message
	public Optional<String> saveToFile(ByteArrayOutputStream stream, String path) {
		
		String fullPath = env.getProperty("path.exports") + path;

		File file = new File(fullPath);
		file.getParentFile().mkdirs();
		
		try(OutputStream outputStream = new FileOutputStream(fullPath)) {
			
		    stream.writeTo(outputStream);
		    logger.info("Saved file (" + stream.size() + " bytes) " + fullPath);
		    return Optional.empty();
		    
		} catch (IOException e) {
			
			logger.error("Could not save file " + fullPath);
			return Optional.of(e.getMessage());
		}
	}

	
	private ScriptBundler toBundle(Script script) {
		
		List<Query> queries = dpdCollector.getQueries(script);
		List<Script> scripts = dpdCollector.getScripts(script);
		
		return new ScriptBundler(script).includeScripts(scripts).addQueries(queries);
	}


	@Override
	public String toString() {
		return "TaskManager [taskProvider=" + taskProvider + ", taskLogger=" + taskLogger + "]";
	}

}
