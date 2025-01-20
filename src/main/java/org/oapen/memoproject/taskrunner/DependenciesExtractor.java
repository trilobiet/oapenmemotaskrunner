package org.oapen.memoproject.taskrunner;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DependenciesExtractor {

	private static final String INCLUDES_PATTERN = 
		
		"\\nfrom +sniplets +import +(.*)";

	private static final String QUERIES_PATTERN = 
			
		"\\nfrom +queries\\.?([^\\s]*) +import +(.*)";
	
	private static final String SPLITIMPORTS_PATTERN = 
			
		//"\\s*([^,]+)";
		"((^|,) *([^ ,]+))";	
	
	
	public static final Set<String> getScriptNames(String sourceCode) {
		
		Pattern pattern = Pattern.compile(INCLUDES_PATTERN);
		Matcher m = pattern.matcher(Objects.toString(sourceCode,""));
		
		Set<String> res = new LinkedHashSet<>();
		
		while(m.find()) { // subsequent calls return next find! 
	    	
			String importlist = m.group(1);
			Set<String> imports = splitImportList(importlist);
			imports.forEach(imp -> res.add(imp));
		}
		
		//System.out.println("RES " + res);
	    
		return res;
	}
	
	
	public static final Set<String> getQueryNames(String sourceCode) {
		
		Set<String> res = new LinkedHashSet<>();
		
		Pattern pattern = Pattern.compile(QUERIES_PATTERN);
		Matcher m = pattern.matcher(Objects.toString(sourceCode,""));
		
		while(m.find()) { // subsequent calls return next find!
			
			String path = m.group(1).isBlank()? "" : m.group(1) + ":";
			String importlist = m.group(2);
			
			Set<String> imports = splitImportList(importlist);
			imports.forEach(imp -> res.add(path + imp));
		}
	    
		return res;
	}

	
	public static final Set<String> splitImportList(String importList) {
		
		Set<String> res = new LinkedHashSet<>();
		
		Pattern pattern = Pattern.compile(SPLITIMPORTS_PATTERN);
		Matcher m = pattern.matcher(Objects.toString(importList,""));
		
		while(m.find()) { // subsequent calls return next find!
			
			res.add(m.group(3));
		}
		
		return res;
		
	}
	
	
	/*
	public final static void main(String... args) {
		
		String iprt = "from queries.test import lib_whatever, lib2 as pietje, lib3 as weetikveel #comment ";
		String pat = "from +queries.?([^\\s]*) +import +(.*)";

		Pattern pattern = Pattern.compile(pat);
		Matcher m = pattern.matcher(Objects.toString(iprt,""));
		
		while(m.find()) { // subsequent calls return next find!
			
			String path = m.group(1).isBlank()? "" : m.group(1) + ":";
			String includes = m.group(2);
			
			Set<String> q = splitImportList(includes);
			q.forEach(System.out::println);
			
			
			//System.out.println(m.group(1));
			//System.out.println(m.group(2));
			//System.out.println(path + query);
		}
	}
	*/
	
	
}
