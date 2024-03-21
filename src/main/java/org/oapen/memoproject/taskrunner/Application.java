package org.oapen.memoproject.taskrunner;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@PropertySource("classpath:application.properties")
public class Application {
	
	@Autowired
	private	TaskManager taskmanager;
	
	private static final Logger logger = 
			LoggerFactory.getLogger(DockerPythonRunner.class); 
	
	public static void main(String[] args) {
		
		SpringApplication.run(Application.class, args);
	}
	
	// "0 0 0 * * *" second, minute, hour, day of month, month, and day of week
	@Scheduled(cron = "${taskrunner.cronschedule}")
	public void runDaily() {

		logger.info("Starting runTasks at " + LocalDateTime.now());
		taskmanager.runTasks();
		logger.info("Finished runTasks at " + LocalDateTime.now());
	}

}
