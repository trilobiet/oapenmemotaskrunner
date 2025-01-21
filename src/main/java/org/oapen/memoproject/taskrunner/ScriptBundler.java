package org.oapen.memoproject.taskrunner;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.oapen.memoproject.taskrunner.entities.Query;
import org.oapen.memoproject.taskrunner.entities.Script;

public class ScriptBundler {
	
	private final Script script;
	private final List<Script> includes = new LinkedList<>();
	private final List<Query> queries = new LinkedList<>();
	
	private String evaluatedScript;

	public ScriptBundler(Script script) {

		this.script = script;
		evaluatedScript = script.getBody();
	}
	
	public ScriptBundler includeScripts(Collection<Script> scripts) {
		
		scripts.forEach(s -> includeScript(s));
		return this;
	}

	public ScriptBundler includeScript(Script script) {
		
		includes.add(script);
		return this;
	}

	public ScriptBundler addQueries(Collection<Query> queries) {
		
		queries.forEach(q -> addQuery(q));
		return this;
	}
	
	public ScriptBundler addQuery(Query query) {
		
		queries.add(query);
		return this;
	}

	/**
	 * @return the evaluatedScript
	 */
	public String getEvaluatedScriptBody() {
		return evaluatedScript;
	}

	public Script getScript() {
		return this.script;
	}

	public List<Script> getIncludedScripts() {
		return new LinkedList<>(includes);
	}

	public List<Query> getQueries() {
		return new LinkedList<>(queries);
	}
	
}
