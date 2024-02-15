package org.oapen.memoproject.taskrunner;

import org.junit.jupiter.api.Test;
import org.oapen.memoproject.taskrunner.entities.Query;
import org.oapen.memoproject.taskrunner.entities.Script;
import org.springframework.test.util.ReflectionTestUtils;

public class DockerRunnerTest {
	
	@Test
	public void test() throws Exception {
			
		Script s = new Script();
		ReflectionTestUtils.setField(s, "body", "Main Script");
		ReflectionTestUtils.setField(s, "name", "main");

		Script i1 = new Script();
		ReflectionTestUtils.setField(i1, "body", "Included Script 1");
		ReflectionTestUtils.setField(i1, "name", "included_1");
		
		Script i2 = new Script();
		ReflectionTestUtils.setField(i2, "body", "Included Script 2");
		ReflectionTestUtils.setField(i2, "name", "included_2");
		
		Query q1 = new Query();
		ReflectionTestUtils.setField(q1, "body", "Select * from Whatever \nOrder by whatever ");
		ReflectionTestUtils.setField(q1, "name", "comic_books_rss");
		
		ScriptBundler sb = new ScriptBundler(s).includeScript(i1).includeScript(i2).addQuery(q1);
		
		new DockerRunner().run(sb);
	}
	

}
