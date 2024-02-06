package org.oapen.memoproject.taskrunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.oapen.memoproject.taskrunner.entities.Script;
import org.springframework.test.util.ReflectionTestUtils;

public class CodeGuardTest {
	
	@Test
	public void whenCodeContainsForbiddenWords_thenReturnForbiddenWords() throws IOException {
		
		File file = new File("src/test/resources/code_with_forbidden_words.txt");
		String in = Files.readString(file.toPath());
		
		List<String> found = CodeGuard.illegalInstructions(in);

		// System.out.println(found);
		assertEquals(2, found.size());
		assertThat(found.contains("exec()"));
		assertThat(found.contains("open()"));
	}
	
	@Test
	public void whenCodeContainsNoForbiddenWords_thenReturnEmptyList() throws IOException {
		
		File file = new File("src/test/resources/code_without_forbidden_words.txt");
		String in = Files.readString(file.toPath());

		List<String> found = CodeGuard.illegalInstructions(in);

		// System.out.println(found);
		assertThat(found.isEmpty());
	}

	
	@Test
	public void anotherTest() {
		
		Script script = new Script();
		Script scriptIncl = new Script();
		
		ReflectionTestUtils.setField(script, "name", "script");
		ReflectionTestUtils.setField(script, "body", ""
			+ "script header\n"
			+ "\n"
			+ "exec(whatever)    \n"
			+ "\n"
			+ "script statements\n"
			+ "\n"
			+ "some open(whatever) statement\n"
			+ "\n"
			+ "script footer"
		);		
				
		ReflectionTestUtils.setField(scriptIncl, "name", "script_included");
		ReflectionTestUtils.setField(scriptIncl, "body", ""
			+ "script header\n"
			+ "\n"
			+ "os.whatever(blahblah) \n"
			+ "\n"
			+ "eval(blahblah) \n"
			+ "\n"
			+ "script footer"
		);
		
		ScriptBundler sb = new ScriptBundler(script).includeScript(scriptIncl);
		
		Map<String, List<String>> ilins = CodeGuard.illegalInstructions(sb);
		
		// System.out.println("==================\n" + ilins);
		
		assertTrue(ilins.containsKey("script"));
		assertTrue(ilins.get("script").containsAll(new ArrayList<>(Arrays.asList("exec()","open()"))));

		assertTrue(ilins.containsKey("script_included"));
		assertTrue(ilins.get("script_included").containsAll(new ArrayList<>(Arrays.asList("os.[any method]","eval()"))));
	}
	
}
