package org.oapen.memoproject.taskrunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.oapen.memoproject.taskrunner.entities.Script;


public final class CodeGuard {

	final static private Collection<Word> illegalInstructions = 

		Arrays.asList(
			new Word("exec()", "exec *\\("),
			new Word("eval()", "eval *\\("),
			new Word("os.[any method]", "os *\\."),
			new Word("subprocess.[any method]", "subprocess *\\."),
			new Word("open()", "open *\\(")
		);
	
	public static List<String> illegalInstructions(String code) {
		
		// String noCommentsCode = removeComments(code);
		List<String> found = new ArrayList<>();
		illegalInstructions.stream().forEach( word -> {
			if (Pattern.compile(word.regex).matcher(code).find()) found.add(word.word);
		});
		
		return found;
	}
	
	public static Map<String, List<String>> illegalInstructions(ScriptBundler sb) {
		
		List<Script> scripts = sb.getIncludedScripts();
		scripts.add(sb.getScript());
		
		Map<String, List<String>> result = new HashMap<>();
		
		scripts.forEach(script -> {
			
			List<String> f = illegalInstructions(script.getBody());
			if (!f.isEmpty()) result.put(script.getName(), f);
		});
		
		return result;
	}
	
	
	// Do not use: Regexes don't work with multiline Strings or anchors in urls
	/**
	 * Remove comments before checking for forbidden words,
	 * So to allow them in comments
	 */
	/*private String removeComments(String code) {
		
		return code
			.replaceAll("\"\"\"[^\"]*\"\"\"","\r\n") // """ (multiple line) comments """
			.replaceAll("'''[^\']*'''","\r\n")       // ''' (multiple line) comments ''' 
			.replaceAll("#.*\r?\n","")           // # (single line) comments
			;
	}*/
	
}

class Word {
	
	public final String word;
	public final String regex;
	
	public Word(String word, String regex) {
		this.word = word;
		this.regex = regex;
	}
}
