package com.springboot.webservices.staffscheduling.beans;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = {"scheduleId"})
public class Schedule {
	
	private Integer scheduleId;
	
	private Date workDate;
	
	private User user;
	
	private double shiftHours;

	public Schedule() {

	}

	public Schedule(Date workDate, User user, double shiftHours) {
		this.workDate = workDate;
		this.user = user;
		this.shiftHours = shiftHours;
	}
	
	public Schedule(Integer scheduleId, Date workDate, User user, double shiftHours) {
		this.scheduleId = scheduleId;
		this.workDate = workDate;
		this.user = user;
		this.shiftHours = shiftHours;
	}

	public Integer getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}
	public Date getWorkDate() {
		return workDate;
	}
	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public double getShiftHours() {
		return shiftHours;
	}
	public void setShiftHours(double shiftHours) {
		this.shiftHours = shiftHours;
	}
	@Override
	public String toString() {
		return "Schedule [scheduleId=" + scheduleId + ", workDate=" + workDate + ", user=" + user + ", shiftHours="
				+ shiftHours + "]";
	}

}
