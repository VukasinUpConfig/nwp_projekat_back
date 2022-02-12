package com.example.RafCloud.machines;

import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledActionProcessingServiceImpl implements ScheduledActionProcessingService {
	
	@Autowired
	private ScheduledActionRepository scheduledActionRepository;
	
	@Autowired
	private MachineRepository machineRepository;

	@Autowired
	private ScheduledActionErrorRepository scheduledActionErrorRepository;
	
	@Async
	@Scheduled(fixedRate = 5000)
	@Override
	public void process() {
		List<ScheduledAction> schedules = scheduledActionRepository.findAll();
		Date now = new Date();
		for (ScheduledAction schedule: schedules) {
			if (schedule.getStatus() == ScheduledActionStatus.DONE) {
				continue;
			}
			
			if (now.getTime() > schedule.getProcessingDate().getTime()) {
				this.processActionInGracePeriod(schedule);
			}
		}
	}
	
	@Async
	public void processActionInGracePeriod(ScheduledAction schedule) {
		System.out.println(Thread.currentThread().getId() + ": process Action in Grace Period");
		schedule.setStatus(ScheduledActionStatus.DONE);
		scheduledActionRepository.save(schedule);
		switch (schedule.getAction()) {
			case START: 
				handleStartAction(schedule);
				break;
			case STOP:
				handleStopAction(schedule);
				break;
			case RESTART:
				handleRestartAction(schedule);
				break;
		}
	}
	
	public void handleStartAction(ScheduledAction schedule) {
		Machine machine = schedule.getMachine();
		if (machine.getStatus() != Status.STOPPED) {
			persistErrorMessage("Machine can only be started if it's in STOPPED state", schedule);
		}
		Random r = new Random();
		int sleep = 10000 + r.nextInt(5000);
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		machine.setStatus(Status.RUNNING);
		try {
			machineRepository.save(machine);
		} catch(ObjectOptimisticLockingFailureException e) {
			persistErrorMessage("Optimistic locking failure exception", schedule);
		}
	}
	
	public void handleStopAction(ScheduledAction schedule) {
		Machine machine = schedule.getMachine();
		if (machine.getStatus() != Status.RUNNING) {
			persistErrorMessage("Machine can only be stopped if it's in RUNNING state", schedule);
		}
		
		Random r = new Random();
		int sleep = 10000 + r.nextInt(5000);
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		machine.setStatus(Status.STOPPED);
		try {
			machineRepository.save(machine);
		} catch(ObjectOptimisticLockingFailureException e) {
			persistErrorMessage("Optimistic locking failure exception", schedule);
		}
	}
	
	public void handleRestartAction(ScheduledAction schedule) {
		Machine machine = schedule.getMachine();
		if (machine.getStatus() != Status.RUNNING) {
			persistErrorMessage("Machine can only be restarted if it's in RUNNING state", schedule);
		}
		
		Random r = new Random();
		int sleep = (10000 + r.nextInt(5000))/2;
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		machine.setStatus(Status.STOPPED);
		try {
			machine = machineRepository.save(machine);
		} catch(ObjectOptimisticLockingFailureException e) {
			persistErrorMessage("Optimistic locking failure exception", schedule);
		}
		
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		machine.setStatus(Status.RUNNING);
		try {
			machineRepository.save(machine);
		} catch(ObjectOptimisticLockingFailureException e) {
			persistErrorMessage("Optimistic locking failure exception", schedule);
		}
	}
	
	public void persistErrorMessage(String message, ScheduledAction schedule) {
		ScheduledActionError error = new ScheduledActionError();
		error.setMessage(message);
		error.setScheduledAction(schedule);
		scheduledActionErrorRepository.save(error);
	}

}
