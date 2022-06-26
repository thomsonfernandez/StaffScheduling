package com.springboot.webservices.staffscheduling.controller;

import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.springboot.webservices.staffscheduling.dao.entity.Role;
import com.springboot.webservices.staffscheduling.dao.entity.User;
import com.springboot.webservices.staffscheduling.dao.repository.RoleRepository;
import com.springboot.webservices.staffscheduling.dao.repository.UserRepository;

@Component
@Order(1)
public class DemoData implements CommandLineRunner  {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;


	@Override
	public void run(String... args) throws Exception {
		Optional<Role> adminRole = roleRepository.findById(1L);
		Optional<Role> staffRole = roleRepository.findById(2L);
		if(!adminRole.isPresent()) {
			Role role = new Role();
			role.setName("ROLE_ADMIN");
			roleRepository.save(role);
		}
		if(!staffRole.isPresent()) {
			Role role2 = new Role();
			role2.setName("ROLE_STAFF");
			roleRepository.save(role2);
		}
		// create user object
		Optional<User> adminUser = userRepository.findById(1L);
		if(!adminUser.isPresent()) {
			User user = new User();
			user.setName("admin");
			user.setUsername("admin");
			user.setEmail("admin@staffschedule.com");
			user.setPassword(passwordEncoder.encode("admin"));
			Role roles = roleRepository.findByName("ROLE_ADMIN").get();
			user.setRoles(Collections.singleton(roles));
			userRepository.save(user);
		}

	}
}
