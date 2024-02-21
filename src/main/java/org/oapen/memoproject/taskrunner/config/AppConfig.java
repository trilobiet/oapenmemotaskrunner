package org.oapen.memoproject.taskrunner.config;

import org.oapen.memoproject.taskrunner.DBService;
import org.oapen.memoproject.taskrunner.DependenciesCollector;
import org.oapen.memoproject.taskrunner.DependenciesCollectorImp;
import org.oapen.memoproject.taskrunner.jpa.DBServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class AppConfig {
	
	@Autowired
	public Environment env;	

	@Bean
	public DBService getDBService() {
		
		return new DBServiceImp();
	}
	
	@Bean
	public DependenciesCollector getDependenciesCollector() {
		
		return new DependenciesCollectorImp(getDBService());
	}
	

}
