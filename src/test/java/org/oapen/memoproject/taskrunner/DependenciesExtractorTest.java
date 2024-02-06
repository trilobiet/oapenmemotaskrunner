package org.oapen.memoproject.taskrunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DependenciesExtractorTest {
	
	private String in;
	
	@BeforeEach
	private void setup() throws IOException {
		
		File file = new File("src/test/resources/code_with_included_scripts.txt");
		this.in = Files.readString(file.toPath());
	}

	@Test
	public void whenScriptContainsIncludedFiles_thenFind() throws IOException {
		
		Set<String> includes = DependenciesExtractor.getScriptNames(in);
		
		assertEquals(3, includes.size());
		assertThat(includes.contains("memo_test.py")).isTrue();
	}

	@Test
	public void whenScriptContainsIncludedQueries_thenFind() throws IOException {
		
		Set<String> queries = DependenciesExtractor.getQueryNames(in);
		
		assertEquals(2, queries.size());
		assertThat(queries.contains("main")).isTrue();
		assertThat(queries.contains("test_query_2.sql")).isTrue();
		
		// System.out.println(queries);
	}


}
