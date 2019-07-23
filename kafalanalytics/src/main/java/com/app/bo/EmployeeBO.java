package com.app.bo;

import java.util.Date;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.app.pojo.AddressPojo;
import com.app.pojo.EmployeeRolesPojo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Document(collection = "Employee")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeBO {

	@GeneratedValue(strategy = GenerationType.AUTO)
	private String id;

	private String employeeName;

	private String employeeId;

	@Id
	private String emailId;

	private AddressPojo employeeDetails;

	private String password;

	private String currentYear;

	private boolean firstlogin;
	
	private String resetToken;
	
	private String tokenValidity; 

	


	public String getTokenValidity() {
		return tokenValidity;
	}

	public void setTokenValidity(String tokenValidity) {
		this.tokenValidity = tokenValidity;
	}

	public String getResetToken() {
		return resetToken;
	}

	public void setResetToken(String resetToken) {
		this.resetToken = resetToken;
	}

	public boolean isFirstlogin() {
		return firstlogin;
	}

	public void setFirstlogin(boolean firstlogin) {
		this.firstlogin = firstlogin;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public AddressPojo getEmployeeDetails() {
		return employeeDetails;
	}

	public void setEmployeeDetails(AddressPojo employeeDetails) {
		this.employeeDetails = employeeDetails;
	}

	public String getCurrentYear() {
		return currentYear;
	}

	public void setCurrentYear(String currentYear) {
		this.currentYear = currentYear;
	}

}