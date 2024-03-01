package org.oapen.memoproject.taskrunner;

import org.oapen.memoproject.taskrunner.entities.RunLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskLoggerImp implements TaskLogger {
	
	/* Tasks are saved in the database, 
	 * but maybe in the future a mail can be sent out as well.   
	 */
	
	@Autowired
	private DBService dbService; 

	@Override
	public void log(TaskResult tl) {
		
		RunLog rl = new RunLog(tl.getIdTask());
		rl.setSuccess(tl.isSuccess());
		rl.setMessage(tl.getMessage());
		rl.setDate(tl.getDateTime());
		dbService.log(rl);
	}

}
