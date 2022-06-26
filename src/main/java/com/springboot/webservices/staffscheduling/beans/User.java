package com.springboot.webservices.staffscheduling.beans;

import java.util.Date;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class User {

	@JsonIgnore
	private Integer userId;
	
	private String name;
	
	private String phoneNumber;
	
	private String emailId;
	
	@Past
	private Date birthDate;
	
	@Size(min = 4,max = 20, message="Name should have atleast 4 characters and less than 20 characters")
	private String userName;
	
	@JsonIgnore
	private String password;
	
	private UserRole userRole;

	public User() {
	}

	public User(Integer userId, String name, String phoneNumber, String emailId, Date birthDate) {
		this.userId = userId;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.emailId = emailId;
		this.birthDate = birthDate;
	}
	
	public User(int userId, String name, String phoneNumber, String emailId, Date birthDate,  UserRole userRole) {
		this.userId = userId;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.emailId = emailId;
		this.birthDate = birthDate;
		this.userRole = userRole;
	}

	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", name=" + name + ", phoneNumber=" + phoneNumber + ", emailId=" + emailId
				+ ", birthDate=" + birthDate + ", userName=" + userName + "]";
	}

}
