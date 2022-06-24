package com.springboot.webservices.staffscheduling.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.webservices.staffscheduling.dao.entity.Role;
import com.springboot.webservices.staffscheduling.dao.entity.Schedule;
import com.springboot.webservices.staffscheduling.dao.entity.User;
import com.springboot.webservices.staffscheduling.dao.repository.RoleRepository;
import com.springboot.webservices.staffscheduling.dao.repository.ScheduleRepository;
import com.springboot.webservices.staffscheduling.dao.repository.UserRepository;
import com.springboot.webservices.staffscheduling.exception.GeneralNotAcceptableException;
import com.springboot.webservices.staffscheduling.exception.NoUsersException;
import com.springboot.webservices.staffscheduling.exception.UserNotFoundException;
import com.springboot.webservices.staffscheduling.payload.UserDto;


@RestController
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private ScheduleRepository scheduleRepository;
	
	@GetMapping(path = "/users")
	public List<User> getUsers() {
		List<User> users = userRepository.findAll();
		if(users == null || users.size() == 0)
			throw new NoUsersException("No user found!!");
		return users;
	}

	@GetMapping(path = "/users/{id}")
	public EntityModel<User> getUserbyId(@PathVariable Long id) {
		Optional<User> user = userRepository.findById(id);
		if(!user.isPresent())
			throw new UserNotFoundException("User not found with the ID :"+id);

		EntityModel<User> model = EntityModel.of(user.get());

		WebMvcLinkBuilder linkToUsers = 
				linkTo(methodOn(this.getClass()).getUsers());

		model.add(linkToUsers.withRel("all-users"));

		return model;
	}

	@DeleteMapping(path = "/users/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Long id ,Principal userLogin) {
		
		Optional<User> user = userRepository.findById(id);
		Optional<Role> adminRole = roleRepository.findByName("ROLE_ADMIN");
		
		String loggedinUserName = userLogin.getName();
		Optional<User> loggedInUser = userRepository.findByEmail(loggedinUserName);
		
		if(!loggedInUser.get().getRoles().contains(adminRole.get())) {
			return new ResponseEntity<>("Staff user cannot delete any user!", HttpStatus.BAD_REQUEST);
		}
		
		if(!user.isPresent())
			throw new UserNotFoundException("User not found with the ID : "+id);

		if(user.get().getRoles().contains(adminRole.get())) {
			return new ResponseEntity<>("Admin user cannot be deleted!", HttpStatus.BAD_REQUEST);
		}
//		Iterator<Schedule> itSchedule = user.get().getSchedules().iterator();
//		itSchedule.remove();
		userRepository.deleteById(id);
		
		return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
	}
	
	@DeleteMapping(path = "/users/delete/username/{username}")
	public ResponseEntity<?> deleteUserByUserName(@PathVariable String username ,Principal userLogin) {
		
		Optional<User> user = userRepository.findByUsername(username);
		Optional<Role> adminRole = roleRepository.findByName("ROLE_ADMIN");
		
		String loggedinUserName = userLogin.getName();
		Optional<User> loggedInUser = userRepository.findByEmail(loggedinUserName);
		
		if(!loggedInUser.get().getRoles().contains(adminRole.get())) {
			return new ResponseEntity<>("Staff user cannot delete any user!", HttpStatus.BAD_REQUEST);
		}
		
		if(!user.isPresent())
			throw new UserNotFoundException("User not found with the username : "+username);

		if(user.get().getRoles().contains(adminRole.get())) {
			return new ResponseEntity<>("Admin user cannot be deleted!", HttpStatus.BAD_REQUEST);
		}
//		Iterator<Schedule> itSchedule = user.get().getSchedules().iterator();
//		itSchedule.remove();
		userRepository.deleteById(user.get().getId());
		
		return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
	}
	
    @PostMapping("/users/edit")
    public ResponseEntity<?> editUser(@RequestBody UserDto userDto){
    	Optional<User> userRepo = userRepository.findByUsername(userDto.getUsername());
        User user = userRepo.get();
        if(user != null) {
        	user.setName(userDto.getName());
//            user.setUsername(userDto.getUsername());
//            user.setEmail(userDto.getEmail());
//            user.setPassword(user.getPassword());
//            Role roles = roleRepository.findByName("ROLE_STAFF").get();
//            user.setRoles(Collections.singleton(roles));
            userRepository.save(user);
        }else {
        	return new ResponseEntity<>("No user with user name : "+ userDto.getUsername() +" is available for update!", HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<>("User updated successfully", HttpStatus.OK);

    }
	
	    
	//Get schedules for a user
	@GetMapping(path = "/users/{id}/schedules")
	public List<Schedule> getUserSchedulesbyId(@PathVariable Long id) {
		Optional<User> user = userRepository.findById(id);
		
		if(!user.isPresent())
			throw new UserNotFoundException("User not found with the ID : "+id);

		return user.get().getSchedules();
	}
	
	
	@GetMapping(path = "/users/{id}/schedules/{fromDateString}/{toDateString}")
	public List<Schedule> getUserSchedulesForUserDates(@PathVariable Long id,@PathVariable String fromDateString,@PathVariable String toDateString) {
		Optional<User> user = userRepository.findById(id);
		
		if(!user.isPresent())
			throw new UserNotFoundException("User not found with the ID : "+id);
		Date fromDate = new Date(), toDate = new Date();
	    try {
			fromDate=new SimpleDateFormat("yyyy-MM-dd").parse(fromDateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		try {
			toDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	    
		Long diffInMillies = Math.abs(toDate.getTime() - fromDate.getTime());
		//Restricting to one year
		if(diffInMillies > 31557600000L ) {
			throw new GeneralNotAcceptableException("Please enter from date and todate with in one year range!");
		}
		Iterator<Schedule> iterator = user.get().getSchedules().iterator();
		while(iterator.hasNext()) {
			Schedule itSchedule = iterator.next();
			if(itSchedule.getWorkDate().after(toDate))
				iterator.remove();
			if(itSchedule.getWorkDate().before(fromDate))
				iterator.remove();
			
		}
		return user.get().getSchedules();
	}
	
	
	@DeleteMapping(path = "/users/{id}/schedules")
	public ResponseEntity<?> deleteSchedulesForUser(@PathVariable Long id ,Principal userLogin) {
		
		Optional<User> user = userRepository.findById(id);
		Optional<Role> adminRole = roleRepository.findByName("ROLE_ADMIN");
		
		String loggedinUserName = userLogin.getName();
		Optional<User> loggedInUser = userRepository.findByEmail(loggedinUserName);
		
		if(!loggedInUser.get().getRoles().contains(adminRole.get())) {
			return new ResponseEntity<>("Staff user cannot schedules for any user!", HttpStatus.BAD_REQUEST);
		}
		
		if(!user.isPresent())
			throw new UserNotFoundException("User not found with the ID :"+id);

		if(user.get().getRoles().contains(adminRole.get())) {
			return new ResponseEntity<>("Admin user schedules cannot be deleted!", HttpStatus.BAD_REQUEST);
		}
		
//		Iterator<Schedule> itSchedules = user.get().getSchedules().iterator();
		scheduleRepository.deleteAllInBatch(user.get().getSchedules());
		/*
		 * while(itSchedules.hasNext()) { Schedule itSchedule = itSchedules.next();
		 * scheduleRepository.delete(itSchedule); }
		 */
		
		
		return new ResponseEntity<>("Schedules deleted successfully", HttpStatus.OK);
	}
}
