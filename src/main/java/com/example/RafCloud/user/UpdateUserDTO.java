package com.example.RafCloud.user;

import lombok.Data;

@Data
public class UpdateUserDTO {
	private long id;
	private String username;
	private String firstName;
	private String lastName;
	private String[] permissions;
}
