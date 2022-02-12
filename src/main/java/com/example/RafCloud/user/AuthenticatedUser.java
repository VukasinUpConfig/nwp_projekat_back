package com.example.RafCloud.user;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.User;

import lombok.Data;

@Data
public class AuthenticatedUser extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final long id;
	
	public AuthenticatedUser(long id, String username, String password) {
		super(username, password, new ArrayList<>());
		this.id = id;
	}

}
