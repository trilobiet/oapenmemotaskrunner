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
		
		Set<String> sniplets = DependenciesExtractor.getScriptNames(in);
		
		//System.out.println(sniplets);
		
		assertEquals(2, sniplets.size());
		assertThat(sniplets.contains("mysql_connect")).isTrue();
		assertThat(sniplets.contains("test123")).isTrue();
	}

	@Test
	public void whenScriptContainsIncludedQueries_thenFind() throws IOException {
		
		Set<String> queries = DependenciesExtractor.getQueryNames(in);
		
		//System.out.println(queries);
		
		assertEquals(2, queries.size());
		assertThat(queries.contains("countries")).isTrue();
		assertThat(queries.contains("comicsgrid:comic_books_rss")).isTrue();
	}


}
