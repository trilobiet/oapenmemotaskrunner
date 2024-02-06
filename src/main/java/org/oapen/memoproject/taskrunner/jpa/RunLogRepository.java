package org.oapen.memoproject.taskrunner.jpa;

import org.oapen.memoproject.taskrunner.entities.RunLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RunLogRepository extends JpaRepository<RunLog, Long> {}

