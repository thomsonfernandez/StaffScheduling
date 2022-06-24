package com.springboot.webservices.staffscheduling.dao.entity;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Entity
@Table(name = "schedules", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"workDate", "shiftHours"})
})
public class Schedule {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

//	@NotBlank(message = "Work Date is mandatory")
	private Date workDate;

//	@NotBlank(message = "Shift Hours is mandatory") 
	private Double shiftHours;

	@ManyToOne(fetch = FetchType.LAZY)
	//	@JoinColumn(name = "user_name", referencedColumnName = "username")
	private User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getWorkDate() {
		return workDate;
	}

	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}

	public Double getShiftHours() {
		return shiftHours;
	}

	public void setShiftHours(Double shiftHours) {
		this.shiftHours = shiftHours;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


}
