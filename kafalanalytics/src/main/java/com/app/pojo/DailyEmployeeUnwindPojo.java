package com.app.pojo;

import java.util.List;

import org.springframework.data.annotation.Id;

import com.app.bo.EmployeeRoleBO;

public class DailyEmployeeUnwindPojo {
	
	private String id;
	
	private String name;
	
	private String employeeIdFK;
	
	private String reportToId;
	
	private String date;
	
	private Long totalMailRecevied;
	
	private Long totalMailSent;
	
	private Long totalMail;
	
	private EmployeeRoleBO roles;
	
	private EmailToneResultPojo selfTone;
	
	private TeamLevelTonalResultPojo teamTone;
	
	private TeamLevelTonalResultPojo otherEmployee;
	
	private TeamLevelTonalResultPojo clientTone;
	
	private EmailPojo clientEmailItems;
	
	private EmailPojo lineItems;
	
	private FilterPojo filter;
	
	
	
	
	

	

	public EmailPojo getLineItems() {
		return lineItems;
	}

	public void setLineItems(EmailPojo lineItems) {
		this.lineItems = lineItems;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmployeeIdFK() {
		return employeeIdFK;
	}

	public void setEmployeeIdFK(String employeeIdFK) {
		this.employeeIdFK = employeeIdFK;
	}

	public String getReportToId() {
		return reportToId;
	}

	public void setReportToId(String reportToId) {
		this.reportToId = reportToId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
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

	public EmployeeRoleBO getRoles() {
		return roles;
	}

	public void setRoles(EmployeeRoleBO roles) {
		this.roles = roles;
	}

	public EmailToneResultPojo getSelfTone() {
		return selfTone;
	}

	public void setSelfTone(EmailToneResultPojo selfTone) {
		this.selfTone = selfTone;
	}

	public TeamLevelTonalResultPojo getTeamTone() {
		return teamTone;
	}

	public void setTeamTone(TeamLevelTonalResultPojo teamTone) {
		this.teamTone = teamTone;
	}

	public TeamLevelTonalResultPojo getOtherEmployee() {
		return otherEmployee;
	}

	public void setOtherEmployee(TeamLevelTonalResultPojo otherEmployee) {
		this.otherEmployee = otherEmployee;
	}

	public TeamLevelTonalResultPojo getClientTone() {
		return clientTone;
	}

	public void setClientTone(TeamLevelTonalResultPojo clientTone) {
		this.clientTone = clientTone;
	}

	public EmailPojo getClientEmailItems() {
		return clientEmailItems;
	}

	public void setClientEmailItems(EmailPojo clientEmailItems) {
		this.clientEmailItems = clientEmailItems;
	}

	public FilterPojo getFilter() {
		return filter;
	}

	public void setFilter(FilterPojo filter) {
		this.filter = filter;
	}
	
	
	
	
	
	

}
