package org.oapen.memoproject.taskrunner;

import java.util.List;

import org.oapen.memoproject.taskrunner.entities.Query;
import org.oapen.memoproject.taskrunner.entities.Script;

public interface DependenciesCollector {
	
	List<Query> getQueries(Script script);
	List<Script> getScripts(Script script);

}
