package com.example.RafCloud.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.RafCloud.authorization.AuthorizationService;
import com.example.RafCloud.permission.Permission;
import com.example.RafCloud.permission.PermissionRepository;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PermissionRepository permissionRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthorizationService authorizationService;

	@Override
	public User findUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public List<User> findAll(long actingUserId) {
		authorizationService.canReadUsers(actingUserId);
		return userRepository.findAll();
	}

	@Override
	public User createUser(long actingUserId, CreateUserDTO user) {
		authorizationService.canCreateUsers(actingUserId);
		validateUser(user);
		User userDBVO = mapCreateUserDtoToUserDbvo(user);
		return userRepository.save(userDBVO);
	}
	
	private void validateUser(CreateUserDTO user) {
		if (user.getUsername() == null) {
			throw new RuntimeException("invalid username");
		}
		
		if (user.getPassword() == null) {
			throw new RuntimeException("invalid password");
		}
		
		if (user.getFirstName() == null) {
			throw new RuntimeException("invalid first name");
		}
		
		if (user.getLastName() == null) {
			throw new RuntimeException("invalid last name");
		}
	}

	private User mapCreateUserDtoToUserDbvo(CreateUserDTO from) {
		User to = new User();
		to.setFirstName(from.getFirstName());
		to.setLastName(from.getLastName());
		to.setUsername(from.getUsername());
		to.setPassword(passwordEncoder.encode(from.getPassword()));
		List<Permission> permissions = new ArrayList<>();
		for (String name : from.getPermissions()) {
			Permission p = permissionRepository.findByName(name);
			permissions.add(p);
		}
		to.setPermissions(permissions);
		return to;
	}

	@Override
	public void deleteUser(long actingUserId, long userId) {
		authorizationService.canDeleteUsers(actingUserId);
		userRepository.deleteById(userId);
	}

	@Override
	public User updateUser(long actingUserId, UpdateUserDTO user) {
		authorizationService.canUpdateUsers(actingUserId);
		Optional<User> optional = userRepository.findById(user.getId());
		if (!optional.isPresent()) {
			throw new RuntimeException("cannot find user with id: " + user.getId());
		}
		
		User userDBVO = optional.get();
		userDBVO.setFirstName(user.getFirstName());
		userDBVO.setLastName(user.getLastName());
		userDBVO.setUsername(user.getUsername());
		List<Permission> permissions = new ArrayList<>();
		for (String name : user.getPermissions()) {
			Permission p = permissionRepository.findByName(name);
			permissions.add(p);
		}
		userDBVO.setPermissions(permissions);
		userRepository.save(userDBVO);
		return userDBVO;
	}

	@Override
	public List<String> getUserPermissions(long actingUserId, long id) {
		authorizationService.canReadUsers(actingUserId);
		List<String> permissions = new ArrayList<>();
		Optional<User> optional = userRepository.findById(id);
		if (!optional.isPresent()) {
			throw new RuntimeException("cannot find user with id: " + id);
		}
		
		User user = optional.get();
		return user.getPermissions().stream()
				.map(p -> p.getName())
				.collect(Collectors.toList());
	}
}
