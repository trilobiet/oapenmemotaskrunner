package org.oapen.memoproject.taskrunner.jpa;

import java.util.Optional;
import java.util.UUID;

import org.oapen.memoproject.taskrunner.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

interface TaskRepository extends JpaRepository<Task, UUID> {
	
	Optional<Task> findByFileName(String name);
}
