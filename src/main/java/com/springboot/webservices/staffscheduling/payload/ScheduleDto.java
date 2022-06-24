package com.springboot.webservices.staffscheduling.payload;

import java.sql.Date;

import lombok.Data;

@Data
public class ScheduleDto {
	private Date workDate;
	private double shiftHours;
	private String userName;

	public Date getWorkDate() {
		return workDate;
	}
	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}
	public double getShiftHours() {
		return shiftHours;
	}
	public void setShiftHours(double shiftHours) {
		this.shiftHours = shiftHours;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

}
