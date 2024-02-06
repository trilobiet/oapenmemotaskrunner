package org.oapen.memoproject.taskrunner.jpa;

import java.util.Optional;
import java.util.UUID;

import org.oapen.memoproject.taskrunner.entities.Query;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QueryRepository extends JpaRepository<Query, UUID> {
	
	//@org.springframework.data.jpa.repository.Query
	//(value = "SELECT * FROM query WHERE name = ?1", nativeQuery = true)
	Optional<Query> findByName(String name);
	
} 