package org.oapen.memoproject.taskrunner;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.oapen.memoproject.taskrunner.entities.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskProviderImp implements TaskProvider {
	
	@Autowired
	private DBService dbService;

	@Override
	public List<Task> getRunnableTasks(LocalDate date) {
		
		//System.out.println(dbService.getTasks());
		
		List<Task> tasks = dbService.getTasks().stream()
			.filter(task -> task.isActive())
			.filter(task -> !task.getStartDate().isAfter(date))
			.filter(task -> isRunnableOnDate(task,date))
			.collect(Collectors.toList());
		
		return tasks;
	}

	private boolean isRunnableOnDate(Task task, LocalDate today) {
		
		// lastRunDate plays no role: we would never call this function before lastRunDate!
		
		if (task.getStartDate() == null) return false;
		else {
			long unitsBetween = task.getFrequency().getChronoUnit().between(task.getStartDate(), today);
			LocalDate dt = task.getStartDate().plus(unitsBetween, task.getFrequency().getChronoUnit());
			
			//System.out.println("units " + unitsBetween);
			//System.out.println(dt + " - " + today);
			
			return unitsBetween >= 0 && dt.equals(today);
		}	
	}

	
	
	
}
