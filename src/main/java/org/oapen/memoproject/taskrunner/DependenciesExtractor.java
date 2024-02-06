package org.oapen.memoproject.taskrunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DependenciesExtractor {

	/* look for lines:
	 * 
	 * #!memo_include = (memo_mysql_conn.py, memo_io.py)
	 * 
	 */
	private static final String INCLUDES_PATTERN = 
		
		"#!memo_include *= *\\(([^)]*)\\)";

	private static final String QUERIES_PATTERN = 
			
		"\\$query:([^\\s]*)";
	
	
	public static final Set<String> getScriptNames(String sourceCode) {
		
		Pattern pattern = Pattern.compile(INCLUDES_PATTERN);
		Matcher m = pattern.matcher(sourceCode);
		
		Set<String> res = new HashSet<>();
		
		while(m.find()) { // subsequent calls return next find! 
	    	
			String group = m.group(1);
			res.addAll(Arrays.asList(group.split(", *")));
		}
	    
		return res;
	}
	
	
	public static final Set<String> getQueryNames(String sourceCode) {
		
		Pattern pattern = Pattern.compile(QUERIES_PATTERN);
		Matcher m = pattern.matcher(sourceCode);
		
		Set<String> res = new HashSet<>();
		
		while(m.find()) { // subsequent calls return next find! 

			res.add(m.group(1));
		}
	    
		return res;
	}

}
