package org.oapen.memoproject.taskrunner.jpa;

import java.util.List;

import org.oapen.memoproject.taskrunner.entities.Export;
import org.oapen.memoproject.taskrunner.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExportRepository extends JpaRepository<Export, Integer> {
	
	List<Export> deleteByTask(Task task);
} 