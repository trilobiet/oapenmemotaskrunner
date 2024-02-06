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
	private Script mockedScript, mockedLibraryScript1, mockedLibraryScript2;
	private Query mockedQuery, mockedLibraryQuery;
	
	private String scriptBody;
	
	@BeforeEach
	public void setup() {
		
		mockDbService = mock(DBService.class);
		mockedScript = new Script();
		mockedLibraryScript1 = new Script();
		mockedLibraryScript2 = new Script();
		mockedQuery = new Query();
		mockedLibraryQuery = new Query();
		
		when(mockDbService.findScriptByName("lib_script_1.py")).thenReturn(Optional.of(mockedLibraryScript1)); 
		when(mockDbService.findScriptByName("lib_script_2.py")).thenReturn(Optional.of(mockedLibraryScript2));
		when(mockDbService.findQueryByName("lib_query_1")).thenReturn(Optional.of(mockedLibraryQuery));
	}
	
	@Test
	public void find_a_descriptive_name_for_this_test() {
		
		scriptBody = "# This is a sample Python script.\n"
				+ "from rfeed import *\n"
				+ "from datetime import datetime\n"
				+ "\n"
				+ "# Load required library scripts\n"
				+ "#!memo_include = (lib_script_1.py, lib_script_2.py)\n"
				+ "\n"
				+ "today: str = datetime.today().strftime('%Y%m%d')\n"
				+ "\n"
				+ "baseUrl = 'https://library.oapen.org/handle/'\n"
				+ "\n"
				+ "def run_a_query():\n"
				+ "    query = $query:main\n"
				+ "\n"
				+ "    query2 = $query:lib_query_1\n"
				+ "\n"
				+ "    # connect and query\n"
				+ "    connection = MemoMySQLConn.getConnection()\n"
				+ "    cursor = connection.cursor()\n"
				+ "    cursor.execute(query)\n"
				+ "    records = cursor.fetchall()\n"
				+ "    items = []\n"
				+ "\n"
				+ "    # concatenate\n"
				+ "    for row in records:\n"
				+ "        \n"
				+ "        items.append(item)\n"
				+ "        \n"
				+ "    cursor.close()\n"
				+ "    connection.close()\n"
				+ "    \n"
				+ "    print('OK')\n"
				+ "\n"
				+ "# Press the green button in the gutter to run the script.\n"
				+ "if __name__ == '__main__':\n"
				+ "    run_a_query()\n"
				+ "\n"
				+ "# See PyCharm help at https://www.jetbrains.com/help/pycharm/\n"
				+ "";
		
		String query1 = "SELECT * \nFROM table";
		String libQuery1 = "SELECT * \nFROM library_table";
		String libScript1 = "# Library Script 1 \nSome commands";
		String libScript2 = "# Library Script 2 \nSome commands";
		
		ReflectionTestUtils.setField(mockedQuery, "name", "query_1");
		ReflectionTestUtils.setField(mockedQuery, "body", query1);
		
		ReflectionTestUtils.setField(mockedLibraryQuery, "name", "lib_query_1");
		ReflectionTestUtils.setField(mockedLibraryQuery, "body", libQuery1);

		ReflectionTestUtils.setField(mockedScript, "name", "script_1");
		ReflectionTestUtils.setField(mockedScript, "body", scriptBody);
		ReflectionTestUtils.setField(mockedScript, "query", mockedQuery);
		
		ReflectionTestUtils.setField(mockedLibraryScript1, "name", "lib_script_1.py");
		ReflectionTestUtils.setField(mockedLibraryScript1, "body", libScript1);
		
		ReflectionTestUtils.setField(mockedLibraryScript2, "name", "lib_script_2.py");
		ReflectionTestUtils.setField(mockedLibraryScript2, "body", libScript2);				
		
		DependenciesCollector col = new DependenciesCollectorImp(mockDbService);
		
		List<Query> queries = col.getQueries(mockedScript);
		List<Script> scripts = col.getScripts(mockedScript);
		
		assertTrue(queries.contains(mockedQuery));
		assertTrue(queries.contains(mockedLibraryQuery));
		assertTrue(scripts.contains(mockedLibraryScript1));
		assertTrue(scripts.contains(mockedLibraryScript2));
		
	}

}
