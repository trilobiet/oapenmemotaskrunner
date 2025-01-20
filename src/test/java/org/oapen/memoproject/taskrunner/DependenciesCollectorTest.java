package org.oapen.memoproject.taskrunner;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.oapen.memoproject.taskrunner.entities.Query;
import org.oapen.memoproject.taskrunner.entities.Script;
import org.springframework.test.util.ReflectionTestUtils;

public class DependenciesCollectorTest {
	
	private DBService mockDbService;
	private Script mockedScript, mockedLibraryScript1, mockedLibraryScript2, mockedLibraryScript3;
	private Query mockedQuery, mockedLibraryQuery1, mockedLibraryQuery2, mockedLibraryQuery3;
	
	private String scriptBody;
	
	@BeforeEach
	public void setup() {
		
		mockDbService = mock(DBService.class);
		mockedScript = new Script();
		mockedLibraryScript1 = new Script();
		mockedLibraryScript2 = new Script();
		mockedLibraryScript3 = new Script();
		mockedQuery = new Query();
		mockedLibraryQuery1 = new Query();
		mockedLibraryQuery2 = new Query();
		mockedLibraryQuery3 = new Query();
		
		when(mockDbService.findScriptByName("lib_script_1")).thenReturn(Optional.of(mockedLibraryScript1)); 
		when(mockDbService.findScriptByName("lib_script_2")).thenReturn(Optional.of(mockedLibraryScript2));
		when(mockDbService.findScriptByName("lib_script_3")).thenReturn(Optional.of(mockedLibraryScript3));
		when(mockDbService.findQueryByName("lib_query_1")).thenReturn(Optional.of(mockedLibraryQuery1));
		when(mockDbService.findQueryByName("lib_query_2")).thenReturn(Optional.of(mockedLibraryQuery2));
		when(mockDbService.findQueryByName("lib_query_3")).thenReturn(Optional.of(mockedLibraryQuery3));
	}
	
	@Test
	public void find_a_descriptive_name_for_this_test() {
		
		scriptBody = "# This is a sample Python script.\n"
				+ "from rfeed import *\n"
				+ "from datetime import datetime\n"
				+ "\n"
				+ "# Load required queries\n"
				+ "from queries import lib_query_1,lib_query_2 as whatever, lib_query_3\n"
				+ "\n"
				+ "# Load required library scripts\n"
				+ "from sniplets import lib_script_1,lib_script_2 as younameit\n"
				+ "from sniplets import lib_script_3\n"
				+ "\n"
				+ "today: str = datetime.today().strftime('%Y%m%d')\n"
				+ "\n"
				+ "if __name__ == '__main__':\n"
				+ "    print('hello')\n"
				+ "\n"
				+ "# See PyCharm help at https://www.jetbrains.com/help/pycharm/\n"
				+ "";
		
		String query1 = "SELECT * \nFROM table";
		String libQuery1 = "SELECT * \nFROM library_table_1";
		String libQuery2 = "SELECT * \nFROM library_table_2";
		String libQuery3 = "SELECT * \nFROM library_table_3";
		String libScript1 = "# Library Script 1 \nSome commands";
		String libScript2 = "# Library Script 2 \nSome commands";
		String libScript3 = "# Library Script 3 \nSome commands";
		
		ReflectionTestUtils.setField(mockedQuery, "name", "query_1");
		ReflectionTestUtils.setField(mockedQuery, "body", query1);
		
		ReflectionTestUtils.setField(mockedLibraryQuery1, "name", "lib_query_1");
		ReflectionTestUtils.setField(mockedLibraryQuery1, "body", libQuery1);

		ReflectionTestUtils.setField(mockedLibraryQuery2, "name", "lib_query_2");
		ReflectionTestUtils.setField(mockedLibraryQuery2, "body", libQuery2);

		ReflectionTestUtils.setField(mockedLibraryQuery3, "name", "lib_query_3");
		ReflectionTestUtils.setField(mockedLibraryQuery3, "body", libQuery3);
		
		ReflectionTestUtils.setField(mockedScript, "name", "script_1");
		ReflectionTestUtils.setField(mockedScript, "body", scriptBody);
		ReflectionTestUtils.setField(mockedScript, "query", mockedQuery);
		
		ReflectionTestUtils.setField(mockedLibraryScript1, "name", "lib_script_1");
		ReflectionTestUtils.setField(mockedLibraryScript1, "body", libScript1);
		
		ReflectionTestUtils.setField(mockedLibraryScript2, "name", "lib_script_2");
		ReflectionTestUtils.setField(mockedLibraryScript2, "body", libScript2);				
		
		ReflectionTestUtils.setField(mockedLibraryScript3, "name", "lib_script_3");
		ReflectionTestUtils.setField(mockedLibraryScript3, "body", libScript3);				

		DependenciesCollector col = new DependenciesCollectorImp(mockDbService);
		
		List<Query> queries = col.getQueries(mockedScript);
		List<Script> scripts = col.getScripts(mockedScript);
		
		assertTrue(queries.contains(mockedQuery));
		assertTrue(queries.contains(mockedLibraryQuery1));
		assertTrue(queries.contains(mockedLibraryQuery2));
		assertTrue(queries.contains(mockedLibraryQuery3));
		assertTrue(scripts.contains(mockedLibraryScript1));
		assertTrue(scripts.contains(mockedLibraryScript2));
		assertTrue(scripts.contains(mockedLibraryScript3));
	}
	
	
}
