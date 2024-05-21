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

		logger.info("===============================");
		logger.info("Heap total memory: " + formatSize(Runtime.getRuntime().totalMemory()));
		logger.info("Heap max memory: " + formatSize(Runtime.getRuntime().maxMemory()));
		logger.info("Heap free memory: " + formatSize(Runtime.getRuntime().freeMemory()));
		logger.info("===============================");
	}
	
	
	// "0 0 0 * * *" second, minute, hour, day of month, month, and day of week
	@Scheduled(cron = "${taskrunner.cronschedule}")
	public void runDaily() {

		logger.info("Start scheduled running tasks at " + LocalDateTime.now());
		taskmanager.runTasks();
		logger.info("Finished scheduled running tasks at " + LocalDateTime.now());
	}
	
	// Readable heap size
	public static String formatSize(long v) {
		if (v < 1024) return v + " B";
		int z = (63 - Long.numberOfLeadingZeros(v)) / 10;
		return String.format("%.1f %sB", (double)v / (1L << (z*10)), " KMGTPE".charAt(z));
    }

}
