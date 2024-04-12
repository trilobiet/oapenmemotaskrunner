package org.oapen.memoproject.taskrunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.oapen.memoproject.taskrunner.entities.Export;
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
	private ExportStore exportStore;	
	
	@Autowired
	private DependenciesCollector dpdCollector;
	
	@Autowired
	private MimeTypeService mimeTypeService;
	
	private static final Logger logger = 
			LoggerFactory.getLogger(DockerPythonRunner.class); 
	
	public void runTasks() {
		
		List<Task> tasks = taskProvider.getRunnableTasks(LocalDate.now());
		
		for (Task task: tasks) {
			
			logger.info("Starting task " + task.getFileName() + " for client " + task.getUsername()); 
			
			TaskResult taskResult = runTask(task);
		
			// For each successful task save the resulting document to the export store
			if (taskResult.isSuccess()) saveExport(task, taskResult);
			
			// Finally log
			logTaskResult(taskResult);
		}
	}
	

	public boolean saveExport(Task task, TaskResult tr) {
		
		Export exp = new Export(task, tr.getOutput());

		exp.setMimetype(mimeTypeService.getMimeTypeFromFileName(task.getFileName()));
		boolean isSaved = exportStore.save(exp);
		return isSaved;
	}
	
	
	public void logTaskResult(TaskResult tr) {
		
		taskLogger.log(tr);
	}
	
	
	public TaskResult runTask(Task task) {
		
		ScriptBundler sb = toBundle(task.getScript());
		TaskResult taskLog = TaskResult.builder().idTask(task.getId()).build();
		
		Map<String, List<String>> illegalInstructions = CodeGuard.illegalInstructions(sb);
		
		if (!illegalInstructions.isEmpty())  
		
			taskLog.fail("Illegal instructions found: " + illegalInstructions.toString());

		else {
			
			DockerPythonRunner runner = 
				new DockerPythonRunner(env.getProperty("docker.image.python"), env.getProperty("path.temp.pythonscripts"));
			
			// remove temp directories? (set false for testing)
			runner.setPurgeTempFiles(env.getProperty("path.temp.pythonscripts.purge", Boolean.class));
			
			// Now run the task...!
			Either<String, String> runResult = runner.run(sb);
			
			if (runResult.isRight()) 
				taskLog.succeed("OK", runResult.get());
			else 
				taskLog.fail(runResult.getLeft()); 
		}
		
		taskLog.setDateTime(LocalDateTime.now());
		return taskLog;
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
