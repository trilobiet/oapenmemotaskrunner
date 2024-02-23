package org.oapen.memoproject.taskrunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.oapen.memoproject.taskrunner.entities.Query;
import org.oapen.memoproject.taskrunner.entities.Script;
import org.oapen.memoproject.taskrunner.entities.Task;
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
	
	
	public void runTasks() {
		
		System.out.println("running at " + LocalDateTime.now());
		
		// TODO for each task save the resulting document to the clients directory
		
		
		//List<Task> tasks = taskProvider.getRunnableTasks(LocalDate.now());
		//tasks.forEach(task -> taskLogger.log(runTask(task)));
	}
	
	public TaskLog runTask(Task task) {
		
		ScriptBundler sb = toBundle(task.getScript());
		TaskLog taskLog = TaskLog.builder().idTask(task.getId()).build();
		
		Map<String, List<String>> illegalInstructions = CodeGuard.illegalInstructions(sb);
		
		if (!illegalInstructions.isEmpty())  
		
			taskLog.fail("Illegal instructions found: " + illegalInstructions.toString());

		else {
			
			DockerPythonRunner runner = 
				new DockerPythonRunner(env.getProperty("docker.image.python"), env.getProperty("path.temp.pythonscripts"));
			
			runner.setPurgeTempFiles(env.getProperty("path.temp.pythonscripts.purge",Boolean.class));
			
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
