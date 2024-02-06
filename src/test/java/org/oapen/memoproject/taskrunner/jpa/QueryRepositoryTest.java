package org.oapen.memoproject.taskrunner.jpa;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.oapen.memoproject.taskrunner.entities.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class QueryRepositoryTest {

	@Autowired
    private QueryRepository queryRepository;
	
	@Test
    public void test() {
		
		Optional<Query> q1 = queryRepository.findByName("687t9fougjhgr65e8");
    	assertTrue(q1.isEmpty());
	
    	//Optional<Query> q2 = queryRepository.findByName("qFullTextSearch1");
    	//assertTrue(q2.isPresent());

	}

	
}
