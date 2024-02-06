package org.oapen.memoproject.taskrunner.pythonbridge;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

import jep.Interpreter;
import jep.JepException;
import jep.SharedInterpreter;

public class RunTest {
	

	public static void main(String[] args) throws IOException {
		
		// File file = new File("src/main/resources/shmp_onix.py");
		File file = new File("src/main/resources/comic_books_rss.py");
		Path path = file.toPath();
		String script = Files.readString(path);
		
		System.out.println( runScript(script) );
	}
	
	public static String runScript(String script) throws IOException {
		
		/*	SharedInterpreter.setConfig(new JepConfig()
    	.redirectStdErr(System.err)
    	.redirectStdout(System.out)); */
		
		File fImport1 = new File("src/main/resources/memo_mysql_conn.py");
		Path path1 = fImport1.toPath();
		String sImport1 = Files.readString(path1);

		File fImport2 = new File("src/main/resources/memo_io.py");
		Path path2 = fImport2.toPath();
		String sImport2 = Files.readString(path2);
		
		

		try (Interpreter interp = new SharedInterpreter()) {
			
			interp.eval("memo_password = 'oapen_readonly_2024!'");
			interp.eval("memo_password = 'oapen_readonly_2024!'");
			interp.exec(sImport1);
			interp.exec(sImport2);
			interp.exec(script);
			
			Object result1 = interp.getValue("result"); // read a value from Python thread
			System.out.println("e" + result1);
		    return "OK " + LocalDateTime.now();
		}	
		catch (JepException e) {
			
			return "FAIL: " + e.getMessage();
		} 
		
	}
	
	

}
