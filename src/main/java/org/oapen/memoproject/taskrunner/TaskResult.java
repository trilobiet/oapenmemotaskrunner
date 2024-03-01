package org.oapen.memoproject.taskrunner;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@Builder
@ToString
public class TaskResult {
	
	private UUID idTask;
	private boolean isSuccess;
	private String message;
	private LocalDateTime dateTime;
	@ToString.Exclude
	private String output;
	
	public void succeed(String message, String output) {
		
		this.isSuccess = true;
		this.message = message;
		this.output = output;
	}
	
	public void fail(String message) {
		
		this.isSuccess = false;
		this.message = message;
	}
	
}
