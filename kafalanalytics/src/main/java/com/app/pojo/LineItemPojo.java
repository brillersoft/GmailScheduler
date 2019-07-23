package com.app.pojo;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;

import com.app.bo.ToneOfMail;

public class LineItemPojo {
	
	private String employeeName;
	
	@TextIndexed
	private String employeeFK;
	
	private String employeeEmailId;
	
	private Long totalMailRecevied;
	
	private Long totalMailSent;
	
	private Long totalMail;
	
	private EmployeeRolesPojo employeeRoles;
	
	private ToneOfMail allMailScore;
	
	private ToneOfMail sentMailScore;
	
	private ToneOfMail receiveMailScore;

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeeFK() {
		return employeeFK;
	}

	public void setEmployeeFK(String employeeFK) {
		this.employeeFK = employeeFK;
	}

	public String getEmployeeEmailId() {
		return employeeEmailId;
	}

	public void setEmployeeEmailId(String employeeEmailId) {
		this.employeeEmailId = employeeEmailId;
	}

	public Long getTotalMailRecevied() {
		return totalMailRecevied;
	}

	public void setTotalMailRecevied(Long totalMailRecevied) {
		this.totalMailRecevied = totalMailRecevied;
	}

	public Long getTotalMailSent() {
		return totalMailSent;
	}

	public void setTotalMailSent(Long totalMailSent) {
		this.totalMailSent = totalMailSent;
	}

	public Long getTotalMail() {
		return totalMail;
	}

	public void setTotalMail(Long totalMail) {
		this.totalMail = totalMail;
	}

	public EmployeeRolesPojo getEmployeeRoles() {
		return employeeRoles;
	}

	public void setEmployeeRoles(EmployeeRolesPojo employeeRoles) {
		this.employeeRoles = employeeRoles;
	}

	public ToneOfMail getAllMailScore() {
		return allMailScore;
	}

	public void setAllMailScore(ToneOfMail allMailScore) {
		this.allMailScore = allMailScore;
	}

	public ToneOfMail getSentMailScore() {
		return sentMailScore;
	}

	public void setSentMailScore(ToneOfMail sentMailScore) {
		this.sentMailScore = sentMailScore;
	}

	public ToneOfMail getReceiveMailScore() {
		return receiveMailScore;
	}

	public void setReceiveMailScore(ToneOfMail receiveMailScore) {
		this.receiveMailScore = receiveMailScore;
	}

	
	
	
	
	
	
	

}
