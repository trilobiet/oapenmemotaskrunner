package org.oapen.memoproject.taskrunner.jpa;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.oapen.memoproject.taskrunner.entities.Script;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ScriptRepositoryTest {

	@Autowired
    private ScriptRepository scriptRepository;
	
	@Test
    public void test() {
		
		Optional<Script> s1 = scriptRepository.findByName("687t9fougjhgr65e8");
    	assertTrue(s1.isEmpty());
	
    	//Optional<Script> s2 = scriptRepository.findByName("memo_mysql_conn.py");
    	//assertTrue(s2.isPresent());
	}
	
}
