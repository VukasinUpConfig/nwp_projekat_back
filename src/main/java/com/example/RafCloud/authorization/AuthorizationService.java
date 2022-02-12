package com.example.RafCloud.authorization;

import com.example.RafCloud.machines.Action;

public interface AuthorizationService {

	void canReadUsers(long actingUserId);
	
	void canUpdateUsers(long actingUserId);
	
	void canDeleteUsers(long actingUserId);
	
	void canCreateUsers(long actingUserId);
	
	void canReadPermissions(long actingUserId, long userId);
	
	void canCreateMachines(long actingUserId);
	
	void canDeleteMachine(long actingUserId, long machineId);
	
	void canSearchMachine(long actingUserId);
	
	void canScheduleAction(long actingUserId, long machineId, Action action);
}
