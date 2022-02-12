package com.example.RafCloud.machines;

import java.util.List;

public interface MachineService {
	
	Machine createMachine(CreateMachineDTO machine, long actingUserId);
	
	void deleteMachine(long actingUserId, long machineId);
	
	List<Machine> search(long actingUserId, SearchMachineDTO machine);
	
	void scheduleAction(long actingUserId, ScheduleActionDTO scheduleAction);
}
