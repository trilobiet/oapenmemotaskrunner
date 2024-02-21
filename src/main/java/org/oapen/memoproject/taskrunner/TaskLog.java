package org.oapen.memoproject.taskrunner;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class TaskLog {
	
	private UUID idTask;
	private boolean isSuccess;
	private String message;
	private LocalDateTime dateTime;
	
	public void succeed(String message) {
		
		this.isSuccess = true;
		this.message = message;
	}
	
	public void fail(String message) {
		
		this.isSuccess = false;
		this.message = message;
	}
	
}
