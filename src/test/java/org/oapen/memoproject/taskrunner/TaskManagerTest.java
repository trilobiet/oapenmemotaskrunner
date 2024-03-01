package org.oapen.memoproject.taskrunner;

import java.net.FileNameMap;
import java.net.URLConnection;

import javax.activation.MimetypesFileTypeMap;

import org.apache.tika.Tika;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

//@SpringBootTest
public class TaskManagerTest {
	
	@Autowired
	private	TaskManager taskmanager;
	
	@BeforeEach
	public void setup() {
		
		//taskmanager = new TaskManager();
	}
	
	@Test
	public void test() {
		
		// System.out.println(taskmanager);
		
		String filename = "test.rss";
		
		FileNameMap fileNameMap = URLConnection.getFileNameMap();
	    String mimeType1 = fileNameMap.getContentTypeFor(filename);
	    String mimeType2 = URLConnection.guessContentTypeFromName(filename);
	    MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
	    String mimeType3 = fileTypeMap.getContentType(filename);
	    

	    Tika tika = new Tika();
	    String mimeType4 = tika.detect(filename);
		
	    System.out.println(mimeType1);
	    System.out.println(mimeType2);
	    System.out.println(mimeType3);
	    System.out.println(mimeType4);
		
	}
	
	
}
