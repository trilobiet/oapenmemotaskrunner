package org.oapen.memoproject.taskrunner;

import java.util.List;
import java.util.Map;

import org.oapen.memoproject.taskrunner.entities.Query;
import org.oapen.memoproject.taskrunner.entities.RunLog;
import org.oapen.memoproject.taskrunner.entities.Script;
import org.oapen.memoproject.taskrunner.entities.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jep.Interpreter;
import jep.JepException;
import jep.SharedInterpreter;

@Component
public class TaskRunner implements ScriptRunner {
	
	@Autowired
	DependenciesCollector dpdCollector;
	
	public RunLog runTask(Task task) {
		
		ScriptBundler sb = toBundle(task.getScript());
		RunLog rl = new RunLog(task.getId());
		
		Map<String, List<String>> illegalInstructions = CodeGuard.illegalInstructions(sb);
		
		if (!illegalInstructions.isEmpty()) {
			
			// task fails due to illegal code instructions
			rl.fail("Illegal instructions found: " + illegalInstructions.toString());
		}
		else try {
			
			run(sb);
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


	@Override
	public String run(ScriptBundler sb) throws Exception {
		
		try (Interpreter interp = new SharedInterpreter()) {
			
			// 1: evaluate code
			sb.getExpressions().stream().forEachOrdered(exp -> interp.eval(exp));
			sb.getIncludedScripts().stream().forEachOrdered(script -> interp.exec(script.getBody()));
			interp.exec(sb.getEvaluatedScriptBody());
			
			// 2: get result and write result to file path for this Task
			Object result1 = interp.getValue("result"); // read a value from Python thread
			
			// 3: return RunLog success
			return result1.toString();
		}	
		catch (JepException e) {

			throw (e);
		} 
	}
	
}

