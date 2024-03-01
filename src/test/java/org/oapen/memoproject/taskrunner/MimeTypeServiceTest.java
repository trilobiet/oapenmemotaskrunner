package org.oapen.memoproject.taskrunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MimeTypeServiceTest {
	
	private	MimeTypeService mimeTypeService;
	
	@BeforeEach
	public void setup() {
		
		mimeTypeService = new MimeTypeService();
	}
	
	@Test
	public void testMimeTypeForExtension_Json() {
		
		String mt = mimeTypeService.getMimeTypeFromFileName("test.json");
		assertEquals("application/json", mt);
	}

	@Test
	public void testMimeTypeForExtension_Html() {
		
		String mt = mimeTypeService.getMimeTypeFromFileName("test.html");
		assertEquals("text/html", mt);
	}

	@Test
	public void testMimeTypeForExtension_Xml() {
		
		String mt = mimeTypeService.getMimeTypeFromFileName("test.xml");
		assertEquals("application/xml", mt);
	}

	@Test
	public void testMimeTypeForExtension_Rss() {
		
		String mt = mimeTypeService.getMimeTypeFromFileName("test.rss");
		assertEquals("application/rss+xml", mt);
	}
	
	@Test
	public void testMimeTypeForExtension_Csv() {
		
		String mt = mimeTypeService.getMimeTypeFromFileName("test.csv");
		assertEquals("text/csv", mt);
	}
	
	@Test
	public void testMimeTypeForExtension_Tsv() {
		
		String mt = mimeTypeService.getMimeTypeFromFileName("test.tsv");
		assertEquals("text/tab-separated-values", mt);
	}
	
	@Test
	public void testMimeTypeForExtension_Ris() {
		
		String mt = mimeTypeService.getMimeTypeFromFileName("test.ris");
		assertEquals("application/x-research-info-systems", mt);
	}
	
	@Test
	public void testMimeTypeForExtension_Onix() {
		
		String mt = mimeTypeService.getMimeTypeFromFileName("test.onix");
		assertEquals("application/xml", mt);
	}

	@Test
	public void testMimeTypeForExtension_Kbart() {
		
		String mt = mimeTypeService.getMimeTypeFromFileName("test.kbart");
		assertEquals("text/tab-separated-values", mt);
	}

	@Test
	public void testMimeTypeForExtension_Marc() {
		
		String mt = mimeTypeService.getMimeTypeFromFileName("test.marc");
		assertEquals("application/marc", mt);
	}
	
	
}
