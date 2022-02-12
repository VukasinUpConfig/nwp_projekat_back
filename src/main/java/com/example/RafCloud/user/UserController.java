package com.example.RafCloud.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.RafCloud.rest.AbstractController;


@RestController
@RequestMapping("/api/users")
public class UserController extends AbstractController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<User> findUsers() {
		return userService.findAll(findActingUserId());
	}
	
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public User createUser(@RequestBody CreateUserDTO user) {
		return userService.createUser(findActingUserId(), user);
	}
	
	@DeleteMapping(value = "/{id}")
	public void deleteUser(@PathVariable("id") long id) {
		userService.deleteUser(findActingUserId(), id);
	}
	
	@PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public User updateUser(@RequestBody UpdateUserDTO user) {
		return userService.updateUser(findActingUserId(), user);
	}
	
	@GetMapping("/{id}/permissions")
	public List<String> getPermissions(@PathVariable("id") long id) {
		return userService.getUserPermissions(findActingUserId(), id);
	}
}
