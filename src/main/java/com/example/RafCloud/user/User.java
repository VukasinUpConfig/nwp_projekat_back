package com.example.RafCloud.user;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.example.RafCloud.machines.Machine;
import com.example.RafCloud.permission.Permission;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
public class User {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column
	private String firstName;
	
	@Column
	private String lastName;
	
	@Column(unique = true)
	private String username;
	
	@Column
	@JsonIgnore
	private String password;
	
	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
	private List<Machine> machines;
	
	@ManyToMany
    @JoinTable(
            name = "user_permissions",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "id")
    )
	private List<Permission> permissions = new ArrayList<>();
	
	public void addPermission(Permission p) {
		this.permissions.add(p);
		p.getUsers().add(this);
	}
}
