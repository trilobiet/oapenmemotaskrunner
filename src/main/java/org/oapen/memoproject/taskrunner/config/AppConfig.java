package org.oapen.memoproject.taskrunner.config;

import org.oapen.memoproject.taskrunner.DBService;
import org.oapen.memoproject.taskrunner.DependenciesCollector;
import org.oapen.memoproject.taskrunner.DependenciesCollectorImp;
import org.oapen.memoproject.taskrunner.jpa.DBServiceImp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
	
	@Bean
	public DBService getDBService() {
		
		return new DBServiceImp();
	}
	
	@Bean
	public DependenciesCollector getDependenciesCollector() {
		
		return new DependenciesCollectorImp(getDBService());
	}
	

}
