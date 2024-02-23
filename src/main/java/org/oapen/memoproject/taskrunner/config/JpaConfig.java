package org.oapen.memoproject.taskrunner.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
	basePackages = "org.oapen.memoproject.taskrunner"
)
//@PropertySource("classpath:application.properties")
public class JpaConfig {}

