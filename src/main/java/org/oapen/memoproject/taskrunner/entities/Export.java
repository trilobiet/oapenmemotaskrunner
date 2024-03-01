package org.oapen.memoproject.taskrunner.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter @Getter @ToString 
@Table(name = "export")
@RequiredArgsConstructor @NoArgsConstructor
public class Export implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NonNull
	@OneToOne 
	@JoinColumn(name="id_task")
	private Task task;
	
	@NonNull
	private String content;

	private String mimetype;
	
}
