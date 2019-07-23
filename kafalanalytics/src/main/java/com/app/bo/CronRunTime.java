package com.app.bo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Document(collection = "CronRunTime")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CronRunTime {
	
	
	 @Id 
	 @GeneratedValue(strategy = GenerationType.AUTO)
	 private String id;
	 
	 private Long cron_run_timecol;
	 
	 private String employeeId;
	 
	 private String password;
	 
	 private String otp;
	 
	 
	 
	 
	 
	 

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getCron_run_timecol() {
		return cron_run_timecol;
	}

	public void setCron_run_timecol(Long cron_run_timecol) {
		this.cron_run_timecol = cron_run_timecol;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
