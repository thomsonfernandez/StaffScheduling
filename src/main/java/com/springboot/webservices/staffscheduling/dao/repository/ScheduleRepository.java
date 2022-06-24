package com.springboot.webservices.staffscheduling.dao.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.webservices.staffscheduling.dao.entity.Schedule;
import com.springboot.webservices.staffscheduling.dao.entity.User;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
	/*Optional<Schedule> findByUser(User user);
	Optional<Schedule> findByUserAndShiftHours(User user, double shiftHours);
	Optional<Schedule> findByUserAndShiftHoursAndWorkDate(User user, double shiftHours, Date workDate);*/
   /* List<Schedule> listSchedulesByUser(User user);
    List<Schedule> listSchedulesUserAndDates(User user, Date fromDate, Date toDate);
    Optional<Schedule> createSchedule(Schedule schedule);
    Optional<Schedule> editSchedule(Schedule schedule);
    Optional<Schedule> deleteSchedule(Schedule schedule);*/
//	List<Schedule> findScheduleByUser(User user);
	Boolean existsByShiftHours(double shiftHours);
	Boolean existsByWorkDate(Date workDate);
//	public List<Schedule> findAllOrderByIdAsc(); 
}
