package org.oapen.memoproject.taskrunner.jpa;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.UUID;

import org.junit.runner.RunWith;
import org.oapen.memoproject.taskrunner.entities.RunLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RunLogRepositoryTest {
	
	@Autowired
    private RunLogRepository runlogRepository;
	
	// @Test Do not run, we use live database for testing
    public void whenSaveRunLogThenFindById() {
    	
    	UUID taskId = UUID.fromString("9708ab61-54e6-44d1-8734-f8a14fd18eff");
		RunLog rl = new RunLog(true, taskId, "Whatever");
		RunLog rSaved = runlogRepository.save(rl);
		Optional<RunLog> rl2 = runlogRepository.findById(rSaved.getId());
		
		assertTrue(rl2.isPresent());
    }

	
}
