package com.springboot.webservices.staffscheduling.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = {"roleId"})
public class UserRole {

	private Integer roleId;
	
	private String userPermission;//Admin, Staff
	
	public UserRole() {
	}
	public UserRole(Integer roleId, String userPermission) {
		super();
		this.roleId = roleId;
		this.userPermission = userPermission;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public String getUserPermission() {
		return userPermission;
	}
	public void setUserPermission(String userPermission) {
		this.userPermission = userPermission;
	}
	@Override
	public String toString() {
		return "UserRole [roleId=" + roleId + ", userPermission=" + userPermission + "]";
	}
}
