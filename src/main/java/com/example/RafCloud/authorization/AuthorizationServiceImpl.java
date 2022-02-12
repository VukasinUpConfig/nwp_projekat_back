package com.example.RafCloud.authorization;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.RafCloud.machines.Action;
import com.example.RafCloud.user.User;
import com.example.RafCloud.user.UserRepository;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public void canReadUsers(long actingUserId) {
		User user = findUserOrThrow(actingUserId);
		assertPermissionExists("CAN_READ_USERS", user);
	}
	
	@Override
	public void canUpdateUsers(long actingUserId) {
		User user = findUserOrThrow(actingUserId);
		assertPermissionExists("CAN_UPDATE_USERS", user);
	}

	@Override
	public void canDeleteUsers(long actingUserId) {
		User user = findUserOrThrow(actingUserId);
		assertPermissionExists("CAN_DELETE_USERS", user);
		
	}

	@Override
	public void canCreateUsers(long actingUserId) {
		User user = findUserOrThrow(actingUserId);
		assertPermissionExists("CAN_CREATE_USERS", user);
	}
	
	@Override
	public void canReadPermissions(long actingUserId, long userId) {
		if (actingUserId != userId) {
			throw new RuntimeException("Trying to get permissions for another user");
		}
	}

	@Override
	public void canCreateMachines(long actingUserId) {
		User user = findUserOrThrow(actingUserId);
		assertPermissionExists("CAN_CREATE_MACHINES", user);
	}

	@Override
	public void canDeleteMachine(long actingUserId, long machineId) {
		User user = findUserOrThrow(actingUserId);
		assertPermissionExists("CAN_DESTROY_MACHINES", user);
		
		if (!user.getMachines().stream().map(m -> m.getId()).collect(Collectors.toList()).contains(machineId)) {
			throw new RuntimeException(String.format("User with id %d cannot delete machine with id %d", actingUserId, machineId));
		}
	}

	@Override
	public void canSearchMachine(long actingUserId) {
		User user = findUserOrThrow(actingUserId);
		assertPermissionExists("CAN_SEARCH_MACHINES", user);
		
	}
	
	private User findUserOrThrow(long userId) {
		Optional<User> optional = userRepository.findById(userId);
		if (!optional.isPresent()) {
			throw new RuntimeException("Cannot find user with id: " + userId);
		}
		
		return optional.get();
	}
	
	private void assertPermissionExists(String permission, User user) {
		List<String> names = user.getPermissions().stream()
				.map(p -> p.getName())
				.collect(Collectors.toList());
		
		if (!names.contains(permission)) {
			throw new RuntimeException(
					String.format("User with id %d does not have permission '%s'", user.getId(), permission)
			);
		}
	}

	@Override
	public void canScheduleAction(long actingUserId, long machineId, Action action) {
		User user = findUserOrThrow(actingUserId);
		assertPermissionExists("CAN_" + action.toString() + "_MACHINES", user);
		
		if (!user.getMachines().stream().map(m -> m.getId()).toList().contains(machineId)) {
			throw new RuntimeException(
					String.format("USer with id %d cannot schedule action on machine %d", actingUserId, machineId)
					);
		}
	}
}
