package com.springboot.webservices.staffscheduling.dao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.webservices.staffscheduling.dao.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findById(Integer id);
	Optional<User> findByEmail(String email);
	Optional<User> findByUsernameOrEmail(String username, String email);
	Optional<User> findByUsername(String username);
	Boolean existsByUsername(String username);
	Boolean existsByEmail(String email);
	/*Optional<User> editUser(User user);
	Optional<User> deleteUser(User user);
	List<User> listUsersBasedOnSchedules(Schedule schedule);
	List<User> listUsersBasedOnSchedulesAndDates(Schedule schedule, Date fromDate, Date toDate);*/
}
