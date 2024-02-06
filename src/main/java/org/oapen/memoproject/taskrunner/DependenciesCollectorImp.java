package org.oapen.memoproject.taskrunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.oapen.memoproject.taskrunner.entities.Query;
import org.oapen.memoproject.taskrunner.entities.Script;


public class DependenciesCollectorImp implements DependenciesCollector {
	
	private final DBService dbService;

	public DependenciesCollectorImp(DBService dbService) {
		this.dbService = dbService;
	}

	@Override
	public List<Query> getQueries(Script script) {
		
		List<Query> queries = new ArrayList<>();
		Set<String> queryNames = DependenciesExtractor.getQueryNames(script.getBody());
		
		// included queries
		for (String qName : queryNames) {

			Optional<Query> incQuery = dbService.findQueryByName(qName);
			if (incQuery.isPresent()) queries.add(incQuery.get());
		}
		
		// main query
		if (script.getQuery() != null) queries.add(script.getQuery());
		
		return queries;
	}

	@Override
	public List<Script> getScripts(Script script) {
		
		Set<String> scriptNames = DependenciesExtractor.getScriptNames(script.getBody());
		List<Script> scripts = new ArrayList<>();
		
		// included scripts
		for (String sName : scriptNames) {

			Optional<Script> incScript = dbService.findScriptByName(sName);
			if (incScript.isPresent()) scripts.add(incScript.get());
		}
		
		return scripts;
	}

}
