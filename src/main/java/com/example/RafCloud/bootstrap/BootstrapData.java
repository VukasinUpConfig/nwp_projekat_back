package com.example.RafCloud.bootstrap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.RafCloud.machines.Machine;
import com.example.RafCloud.machines.Status;
import com.example.RafCloud.permission.Permission;
import com.example.RafCloud.permission.PermissionRepository;
import com.example.RafCloud.user.User;
import com.example.RafCloud.user.UserRepository;

@Component
public class BootstrapData implements CommandLineRunner {
	
	@Autowired
	private PermissionRepository permissionRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public void run(String... args) throws Exception {
		String[] permissionNames = new String[] {"CAN_READ_USERS", 
				"CAN_CREATE_USERS", "CAN_UPDATE_USERS", "CAN_DELETE_USERS",
				"CAN_SEARCH_MACHINES", "CAN_START_MACHINES", "CAN_STOP_MACHINES",
				"CAN_RESTART_MACHINES", "CAN_CREATE_MACHINES", "CAN_DESTROY_MACHINES"};
		
		User user = new User();
		user.setFirstName("Harry");
		user.setLastName("Potter");
		user.setUsername("scarface");
		user.setPassword(this.passwordEncoder.encode("1234"));
		
		List<Permission> permissions = new ArrayList<>();
		for (String name : permissionNames) {
			Permission p = new Permission();
			p.setName(name);
			permissionRepository.save(p);
			user.addPermission(p);
		}
		
		Machine machine = new Machine();
		machine.setName("machine1");
		machine.setActive(true);
		machine.setStatus(Status.STOPPED);
		machine.setCreatedAt(new Date());
		machine.setOwner(user);
		
		
		Machine machine2 = new Machine();
		machine2.setName("machine2");
		machine2.setActive(true);
		machine2.setStatus(Status.RUNNING);
		machine2.setCreatedAt(DateUtils.addMonths(new Date(), -3));
		machine2.setOwner(user);
		
		Machine machine3 = new Machine();
		machine3.setName("machine3");
		machine3.setActive(true);
		machine3.setStatus(Status.RUNNING);
		machine3.setCreatedAt(DateUtils.addMonths(new Date(), -1));
		machine3.setOwner(user);
		
		user.setMachines(List.of(machine, machine2, machine3));
		userRepository.save(user);
	}

}
