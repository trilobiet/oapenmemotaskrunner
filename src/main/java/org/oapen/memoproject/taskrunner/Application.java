package org.oapen.memoproject.taskrunner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
public class Application {
	
	public static void main(String[] args) {
		
		SpringApplication.run(Application.class, args);
	}
	
	// second, minute, hour, day of month, month, and day of week
	@Scheduled(cron = "0 0 0 * * *")
	public void runDaily() {
		
		new TaskManager().runTasks();
	}

}
