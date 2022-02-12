package com.example.RafCloud.machines;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.RafCloud.authorization.AuthorizationService;
import com.example.RafCloud.user.User;
import com.example.RafCloud.user.UserRepository;
import com.example.RafCloud.utils.DateUtils;

@Service
public class MachineServiceImpl implements MachineService {
	
	private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private MachineRepository machineRepository;
	
	@Autowired
	private AuthorizationService authorizationService;
	
	@Autowired
	private ScheduledActionRepository scheduledActionRepository;

	@Override
	public Machine createMachine(CreateMachineDTO createMachine, long actingUserId) {
		authorizationService.canCreateMachines(actingUserId);
		User user = userRepository.findById(actingUserId).get();
		Machine machine = new Machine();
		machine.setActive(true);
		machine.setCreatedAt(new Date());
		machine.setStatus(Status.STOPPED);
		machine.setOwner(user);
		machine.setName(createMachine.getName());
		machineRepository.save(machine);
		return machine;
	}

	@Override
	public void deleteMachine(long actingUserId, long machineId) {
		authorizationService.canDeleteMachine(actingUserId, machineId);
		Machine machine = machineRepository.findById(machineId).get();
		if (!machine.getStatus().equals(Status.STOPPED)) {
			throw new RuntimeException("Only stopped machine can be destroyed");
		}
		
		machine.setActive(false);
		machineRepository.save(machine);
	}

	@Override
	public List<Machine> search(long actingUserId, SearchMachineDTO machine) {
		authorizationService.canSearchMachine(actingUserId);
		String name = StringUtils.trim(machine.getName());
		Status status = EnumUtils.getEnum(Status.class, machine.getStatus());
		Date from = null, to = null;
		try {
			if (StringUtils.trimToNull(machine.getDateFrom()) != null) {
				from = DateUtils.parseStringAsDate(machine.getDateFrom(), DATE_FORMAT);
			}
			
			if (StringUtils.trimToNull(machine.getDateTo()) != null) {
				to = DateUtils.parseStringAsDate(machine.getDateTo(), DATE_FORMAT);
			}
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException("Date parsing has gone wrong");
		}
		
		return machineRepository.search(name, status, from, to).stream()
				.filter(m -> m.getOwner().getId() == actingUserId)
				.toList();
	}

	@Override
	public void scheduleAction(long actingUserId, ScheduleActionDTO scheduleAction) {
		Action action = EnumUtils.getEnum(Action.class, scheduleAction.getAction());
		if (action == null) {
			throw new RuntimeException("Invalid scheduling action");
		}
		
		authorizationService.canScheduleAction(actingUserId, scheduleAction.getMachineId(), action);
		
		String dateString = StringUtils.trimToNull(scheduleAction.getDateTime());
		Date date = new Date();
		if (dateString != null) {
			try {
				date = DateUtils.parseStringAsDate(dateString, DATETIME_FORMAT);
			} catch (ParseException e) {
				e.printStackTrace();
				throw new RuntimeException("Date parsing has gone wrong");
			}
		}
		
		ScheduledAction schedule = new ScheduledAction();
		schedule.setProcessingDate(date);
		schedule.setStatus(ScheduledActionStatus.PENDING);
		schedule.setAction(action);
		Machine machine = machineRepository.findById(scheduleAction.getMachineId()).get();
		schedule.setMachine(machine);
		
		scheduledActionRepository.save(schedule);
	}

}
