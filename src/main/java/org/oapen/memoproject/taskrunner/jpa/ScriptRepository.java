package org.oapen.memoproject.taskrunner.jpa;

import java.util.Optional;
import java.util.UUID;

import org.oapen.memoproject.taskrunner.entities.Script;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScriptRepository extends JpaRepository<Script, UUID> {
	
	//@Query(value = "SELECT * FROM script WHERE name = ?1", nativeQuery = true)
	Optional<Script> findByName(String name);
}
