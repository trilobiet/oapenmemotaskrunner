package org.oapen.memoproject.taskrunner;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.oapen.memoproject.taskrunner.entities.Query;
import org.oapen.memoproject.taskrunner.entities.Script;
import org.springframework.test.util.ReflectionTestUtils;

public class ScriptBundlerTest {
	
	@Test
	public void whenAddQueries_queriesBodiesMustBeRenderedInScript() {

		Script mockedScript = new Script();
		Query mockedQuery1 = new Query();
		Query mockedQuery2 = new Query();
		Query mockedQuery3 = new Query();
		
		ReflectionTestUtils.setField(mockedScript, "body", ""
			+ "script header\n"
			+ "\n"
			+ "script statements\n"
			+ "\n"
			+ "q1 = $query:query1\n"
			+ "\n"
			+ "script statements\n"
			+ "\n"
			+ "anotherQuery = $query:query2 blahblah\n"
			+ "\n"
			+ "script statements\n"
			+ "\n"
			+ "script statements\n"
			+ "\n"
			+ "blahblah q3=$query:query3\n"
			+ "\n"
			+ "script footer"
		);
		
		// Use ReflectionUtils in testing when no public setters are available
		ReflectionTestUtils.setField(mockedQuery1,"name","query1");
		ReflectionTestUtils.setField(mockedQuery1,"body","SELECT 1 FROM TEST1");
		
		ReflectionTestUtils.setField(mockedQuery2,"name","query2");
		ReflectionTestUtils.setField(mockedQuery2,"body","SELECT 2 FROM TEST2");
		
		ReflectionTestUtils.setField(mockedQuery3,"name","query3");
		ReflectionTestUtils.setField(mockedQuery3,"body","SELECT 3 FROM TEST3");

		Set<Query> queries = new HashSet<>();
		queries.add(mockedQuery1);
		queries.add(mockedQuery2);

		ScriptBundler sb = new ScriptBundler(mockedScript).addQueries(queries).addQuery(mockedQuery3);
		
		String b = sb.getEvaluatedScriptBody();
		
		assertTrue(b.contains("SELECT 1 FROM TEST1"));
		assertTrue(b.contains("SELECT 2 FROM TEST2"));
		assertTrue(b.contains("SELECT 3 FROM TEST3"));
	}

}
