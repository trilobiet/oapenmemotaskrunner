package org.oapen.memoproject.taskrunner;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.oapen.memoproject.taskrunner.entities.Query;
import org.oapen.memoproject.taskrunner.entities.RunLog;
import org.oapen.memoproject.taskrunner.entities.Script;
import org.oapen.memoproject.taskrunner.entities.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskManager  {
	
	@Autowired
	DBService dbService;
	
	@Autowired
	ScriptRunner scriptRunner;
	
	@Autowired
	DependenciesCollector dpdCollector;
	
	public void runTasks() {
		
		List<Task> tasks = dbService.getRunnableTasks(LocalDate.now());
		tasks.forEach(task -> writeRunLog(runTask(task)));
	}
	
	
	public RunLog runTask(Task task) {
		
		ScriptBundler sb = toBundle(task.getScript());
		RunLog rl = new RunLog(task.getId());
		
		Map<String, List<String>> illegalInstructions = CodeGuard.illegalInstructions(sb);
		
		if (!illegalInstructions.isEmpty()) {
			
			// task fails due to illegal code instructions
			rl.fail("Illegal instructions found: " + illegalInstructions.toString());
		}
		else try {
			
			scriptRunner.run(sb);
			rl.succeed("OK");
		}
		catch (Exception e) {
			
			rl.fail(e.getMessage());
		}
		
		return rl;
	}
		
	private ScriptBundler toBundle(Script script) {
		
		List<Query> queries = dpdCollector.getQueries(script);
		List<Script> scripts = dpdCollector.getScripts(script);
		
		return new ScriptBundler(script).includeScripts(scripts).addQueries(queries);
	}


	public void writeRunLog(RunLog rl) {

		dbService.log(rl);
	}

}
