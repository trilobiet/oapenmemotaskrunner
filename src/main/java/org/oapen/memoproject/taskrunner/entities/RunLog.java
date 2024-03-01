package org.oapen.memoproject.taskrunner.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter @ToString 
@NoArgsConstructor
//@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded=true)
@Table(name = "runlog")
public class RunLog implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@EqualsAndHashCode.Include
	@GeneratedValue(strategy=javax.persistence.GenerationType.IDENTITY)
	private Long id;
	
	private String message;
	private boolean isSuccess;
	private LocalDateTime date;
	@Type(type="uuid-char")
	private UUID idTask;
	
	public RunLog (UUID idTask) {
		
		this.idTask = idTask;
	}

	public RunLog(boolean isSuccess, UUID idTask, String message) {
		
		this.isSuccess = isSuccess;
		this.idTask = idTask;
		this.message = message;
		this.date = LocalDateTime.now();
	}
	
}
