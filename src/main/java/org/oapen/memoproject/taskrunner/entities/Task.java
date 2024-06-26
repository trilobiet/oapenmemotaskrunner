package org.oapen.memoproject.taskrunner.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Type;
import org.oapen.memoproject.taskrunner.TaskFrequency;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * See https://vladmihalcea.com/map-jpa-entity-to-view-or-sql-query-with-hibernate/
 * 
 */

@Entity
//@Setter 
@Getter 
@ToString 
@Immutable
@Subselect(
    "SELECT "
    + "	t.id, t.id_script, h.username, t.file_name, t.extension, t.is_active, t.start_date, t.frequency, l.date, l.is_success "
    + "FROM "
    + "	task t "
    + "JOIN homedir h ON t.id_homedir = h.id "
    + "LEFT JOIN runlog l ON l.id = ("  // We must only get at most 1 runlog record (multiple records may have the same timestamp)
    + "	SELECT l1.id FROM runlog AS l1 "
    + "	WHERE l1.id_task = t.id "
    + "	ORDER BY l1.date DESC "
    + "	LIMIT 1 "
    + ")"
)
public class Task implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Type(type="uuid-char")
	@EqualsAndHashCode.Include
	private UUID id;
	private String username, fileName, extension;
	
	@Enumerated(EnumType.STRING)
	private TaskFrequency frequency;
	private LocalDate startDate;
	@Column(name="date")
	private LocalDate lastRunDate;
	private Boolean isSuccess;
	private boolean isActive;
	
	@OneToOne
	@JoinColumn(name = "id_script")
	private Script script;
	
	public String getPath() {
		
		return username + "/" + fileName;
	}

	public String getFlattenedPath() {
		
		return username + "_" + fileName;
	}
	
	
}

