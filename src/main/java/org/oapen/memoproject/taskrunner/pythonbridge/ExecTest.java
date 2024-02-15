package org.oapen.memoproject.taskrunner.pythonbridge;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

public class ExecTest {
	
	public static void main(String[] args) throws IOException {
		
		String scriptsDir = "/home/acdhirr/Desktop/OAPEN2020/MEMO-project/sample-scripts/docker/hiero";
		String script = "comicsgrid:comic_books_rss.py";
		
		// TODO maybe check whether the script is created 
		
		String cmd = 
			  "docker run --rm " 
			+ "-v " + scriptsDir + ":/root/scripts " 
			+ "oapen/ubuntu2204python310 python3 /root/scripts/"
			+ script;
		
		//System.out.println(cmd);
		
		CommandLine cmdLine = CommandLine.parse(cmd);
		
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
	        
	    DefaultExecutor executor = DefaultExecutor.builder().get();
	    executor.setStreamHandler(streamHandler);
	    executor.setExitValues(null); // allow all exit values, pass Python errors to Java
	    
	    int exitValue = executor.execute(cmdLine);
	    System.out.println(outputStream.toString().trim());
	    System.out.println(exitValue);
		
	}

}
