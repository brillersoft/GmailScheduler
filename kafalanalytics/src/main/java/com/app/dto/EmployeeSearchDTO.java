package com.app.dto;

import java.util.List;

import com.app.pojo.EmployeeSearchPojo;

public class EmployeeSearchDTO {
	
	List<EmployeeSearchPojo> employeeData;

	public List<EmployeeSearchPojo> getEmployeeData() {
		return employeeData;
	}

	public void setEmployeeData(List<EmployeeSearchPojo> employeeData) {
		this.employeeData = employeeData;
	}
	
	

}
