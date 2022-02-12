package com.example.RafCloud.user;

import lombok.Data;

@Data
public class CreateUserDTO {

	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String[] permissions;
}
