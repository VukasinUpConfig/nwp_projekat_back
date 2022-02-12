package com.example.RafCloud.user;

import java.util.List;

public interface UserService {
	
	User findUserByUsername(String username);

	List<User> findAll(long actingUserId);
	
	User createUser(long actingUserId, CreateUserDTO user);
	
	void deleteUser(long actingUserId, long userId);
	
	User updateUser(long actingUserId, UpdateUserDTO user);
	
	List<String> getUserPermissions(long actingUserId, long id);
}
