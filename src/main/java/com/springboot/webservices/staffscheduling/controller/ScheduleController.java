package com.springboot.webservices.staffscheduling.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.webservices.staffscheduling.dao.entity.Role;
import com.springboot.webservices.staffscheduling.dao.entity.Schedule;
import com.springboot.webservices.staffscheduling.dao.entity.User;
import com.springboot.webservices.staffscheduling.dao.repository.RoleRepository;
import com.springboot.webservices.staffscheduling.dao.repository.ScheduleRepository;
import com.springboot.webservices.staffscheduling.dao.repository.UserRepository;
import com.springboot.webservices.staffscheduling.exception.GeneralNotAcceptableException;
import com.springboot.webservices.staffscheduling.exception.NoSchedulesException;
import com.springboot.webservices.staffscheduling.exception.ScheduleNotFoundException;
import com.springboot.webservices.staffscheduling.exception.UserNotFoundException;
import com.springboot.webservices.staffscheduling.payload.ScheduleDto;


@RestController
public class ScheduleController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ScheduleRepository scheduleRepository;

	@Autowired
	private RoleRepository roleRepository;

	//Get all schedules
	@GetMapping(path = "/schedules")
	public List<Schedule> getSchedules() {
		List<Schedule> schedules = scheduleRepository.findAll();
		if(schedules == null || schedules.size() == 0)
			throw new NoSchedulesException("No schedule found!!");

		return schedules;
	}

	//Get schedules by Id
	@GetMapping(path = "/schedules/{id}")
	public EntityModel<Schedule> getScheduleById(@PathVariable Long id) {
		Optional<Schedule> schedule = scheduleRepository.findById(id);
		if(!schedule.isPresent())
			throw new ScheduleNotFoundException("Id : "+id);

		EntityModel<Schedule> model = EntityModel.of(schedule.get());

		WebMvcLinkBuilder linkToUsers = 
				linkTo(methodOn(this.getClass()).getSchedules());

		model.add(linkToUsers.withRel("all-schedules"));

		return model;
	}

	//Create schedule
	@PostMapping("/schedules")
	public ResponseEntity<?> createSchedule(@RequestBody ScheduleDto scheduleDto, Principal userLogin){

		Optional<User> user = userRepository.findByUsername(scheduleDto.getUserName());

		Optional<Role> adminRole = roleRepository.findByName("ROLE_ADMIN");
		
		String loggedinUserName = userLogin.getName();
		Optional<User> loggedInUser = userRepository.findByEmail(loggedinUserName);
		
		if(!loggedInUser.get().getRoles().contains(adminRole.get())) {
			return new ResponseEntity<>("Staff user is not authorized to create schedule!", HttpStatus.BAD_REQUEST);
		}
		
		//if user not present
		if(!user.isPresent()){
			return new ResponseEntity<>("Username is not present!", HttpStatus.BAD_REQUEST);
		}

		//If schedule already present
		Iterator<Schedule> iterator = user.get().getSchedules().iterator();
		while(iterator.hasNext()) {
			Schedule itSchedule = iterator.next();
			boolean isSameDate = compareWorkDates(itSchedule.getWorkDate(),scheduleDto.getWorkDate());

			if(itSchedule.getShiftHours() == scheduleDto.getShiftHours() && isSameDate &&
					itSchedule.getUser().getUsername().equalsIgnoreCase(scheduleDto.getUserName())) {
				return new ResponseEntity<>("Schedule data is already present!", HttpStatus.BAD_REQUEST);
			}
		}

		// create schedule for user object
		Schedule newSchedule = new Schedule();
		newSchedule.setShiftHours(scheduleDto.getShiftHours());
		newSchedule.setWorkDate(scheduleDto.getWorkDate());
		newSchedule.setUser(user.get());
		scheduleRepository.save(newSchedule);

		return new ResponseEntity<>("Schedule added successfully for the user ->"+scheduleDto.getUserName(), HttpStatus.OK);

	}

	@GetMapping(path = "/schedules/{fromDateString}/{toDateString}")
	public List<Schedule> getSchedulesForDates(@PathVariable String fromDateString,@PathVariable String toDateString) {
		List<Schedule> schedules = scheduleRepository.findAll();
		if(schedules == null || schedules.size() == 0)
			throw new NoSchedulesException("No schedule found!!");
		Date fromDate = new Date(), toDate = new Date();
		try {
			fromDate=new SimpleDateFormat("yyyy-MM-dd").parse(fromDateString);
		} catch (ParseException e) {
			throw new GeneralNotAcceptableException("Please enter a valid from date in the format yyyy-MM-dd !");
		}  
		try {
			toDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDateString);
		} catch (ParseException e) {
			throw new GeneralNotAcceptableException("Please enter a valid to date in the format yyyy-MM-dd !");
		}  

		Long diffInMillies = Math.abs(toDate.getTime() - fromDate.getTime());
		//Restricting to one year
		if(diffInMillies > 31557600000L ) {
			throw new GeneralNotAcceptableException("Please enter from date and todate with in one year range!");
		}
		Iterator<Schedule> iterator = schedules.iterator();
		while(iterator.hasNext()) {
			Schedule itSchedule = iterator.next();
			if(itSchedule.getWorkDate().after(toDate))
				iterator.remove();
			if(itSchedule.getWorkDate().before(fromDate))
				iterator.remove();

		}
		return schedules;
	}


	@PutMapping("/schedules/edit/{scheduleid}")
	public ResponseEntity<?> editSchedule(@PathVariable Long scheduleid, @RequestBody ScheduleDto scheduleDto, Principal userLogin){
		Optional<User> userRepo = userRepository.findByUsername(scheduleDto.getUserName());
		Optional<Role> adminRole = roleRepository.findByName("ROLE_ADMIN");
		
		String loggedinUserName = userLogin.getName();
		Optional<User> loggedInUser = userRepository.findByEmail(loggedinUserName);
		
		if(!loggedInUser.get().getRoles().contains(adminRole.get())) {
			return new ResponseEntity<>("Staff user cannot edit any schedules!", HttpStatus.BAD_REQUEST);
		}
		
		if(!userRepo.isPresent())
			throw new UserNotFoundException("User not found with the username : "+scheduleDto.getUserName());
		
		
		Optional<Schedule> scheduleRepo = scheduleRepository.findById(scheduleid);
		Schedule schedule = scheduleRepo.get();
		
		if(!schedule.getUser().getUsername().equalsIgnoreCase(userRepo.get().getUsername())) {
			return new ResponseEntity<>("User in the request is different form user in the schedule!", HttpStatus.BAD_REQUEST);
		}
		
		if(schedule != null) {
			schedule.setShiftHours(scheduleDto.getShiftHours());
			schedule.setWorkDate(scheduleDto.getWorkDate());
			scheduleRepository.save(schedule);
		}else {
			return new ResponseEntity<>("No schedule available for update!", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("User schedule updated successfully", HttpStatus.OK);
	}

	private boolean compareWorkDates(Date date1, Date date2) {
		String dateString1 = date1.toString();
		String dateString2 = date2.toString();
		boolean isEqual = dateString1.startsWith(dateString2);
		return isEqual;
	}
}
