package org.oapen.memoproject.taskrunner;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.oapen.memoproject.taskrunner.entities.Query;
import org.oapen.memoproject.taskrunner.entities.Script;

public class ScriptBundlerTest {
	
	@Test
	public void whenAddToBundle_thenBundleContainsWhatIsAdded() {

		Script mockedScript = new Script();
		Script mockedSniplet = new Script();
		Query mockedQuery = new Query();

		Set<Script> sniplets = new HashSet<>();
		sniplets.add(mockedSniplet);
		
		Set<Query> queries = new HashSet<>();
		queries.add(mockedQuery);

		ScriptBundler sb = new ScriptBundler(mockedScript).addQueries(queries).includeScripts(sniplets);
		
		assertTrue(sb.getScript().equals(mockedScript));
		assertTrue(sb.getIncludedScripts().contains(mockedSniplet));
		assertTrue(sb.getQueries().containsAll(queries));
	}

}
