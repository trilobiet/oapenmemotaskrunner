package org.oapen.memoproject.taskrunner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
public class Application {
	
	@Autowired
	private	TaskManager taskmanager;
	
	public static void main(String[] args) {
		
		SpringApplication.run(Application.class, args);
	}
	
	// second, minute, hour, day of month, month, and day of week
	@Scheduled(cron = "0 0 0 * * *")
	// @Scheduled(cron = "0 * * * * *") every minute for testing
	public void runDaily() {
		
		taskmanager.runTasks();
	}

}
