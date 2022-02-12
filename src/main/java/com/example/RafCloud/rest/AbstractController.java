package com.example.RafCloud.rest;

import org.springframework.security.core.context.SecurityContextHolder;

import com.example.RafCloud.user.AuthenticatedUser;

public class AbstractController {
	
	protected long findActingUserId() {
		AuthenticatedUser authUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return authUser.getId();
	}
}
