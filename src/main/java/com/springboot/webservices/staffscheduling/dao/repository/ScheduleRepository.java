package com.springboot.webservices.staffscheduling.dao.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.webservices.staffscheduling.dao.entity.Schedule;
import com.springboot.webservices.staffscheduling.dao.entity.User;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
	Boolean existsByShiftHours(double shiftHours);
	Boolean existsByWorkDate(Date workDate);
}
