package com.springboot.webservices.staffscheduling.controller;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.webservices.staffscheduling.dao.entity.Role;
import com.springboot.webservices.staffscheduling.dao.entity.User;
import com.springboot.webservices.staffscheduling.dao.repository.RoleRepository;
import com.springboot.webservices.staffscheduling.dao.repository.UserRepository;
import com.springboot.webservices.staffscheduling.payload.LoginDto;
import com.springboot.webservices.staffscheduling.payload.SignUpDto;
import com.springboot.webservices.staffscheduling.util.Utils;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	
	@PostMapping("/signin")
	public ResponseEntity<String> authenticateUser(@RequestBody LoginDto loginDto){
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				loginDto.getUsernameOrEmail(), loginDto.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		return new ResponseEntity<>("User signed-in successfully!.", HttpStatus.OK);

	}


	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto){

		// add check for username exists in a DB
		if(userRepository.existsByUsername(signUpDto.getUsername())){
			return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
		}
		if(!Utils.validateEmail(signUpDto.getEmail())) {
			return new ResponseEntity<>("Please enter a valid email!", HttpStatus.BAD_REQUEST);
		}
		// add check for email exists in DB
		if(userRepository.existsByEmail(signUpDto.getEmail())){
			return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
		}

		// create user object
		User user = new User();
		user.setName(signUpDto.getName());
		user.setUsername(signUpDto.getUsername());
		user.setEmail(signUpDto.getEmail());
		user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

		Role roles = roleRepository.findByName("ROLE_STAFF").get();
		user.setRoles(Collections.singleton(roles));

		userRepository.save(user);

		return new ResponseEntity<>("User registered successfully", HttpStatus.OK);

	}

	@GetMapping("/signout")
	public ResponseEntity<String> fetchSignoutSite(HttpServletRequest request, HttpServletResponse response) {        
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}

		return new ResponseEntity<>("User signed-out successfully", HttpStatus.OK);
	}
}
