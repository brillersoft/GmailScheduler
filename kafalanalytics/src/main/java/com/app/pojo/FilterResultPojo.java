package com.app.pojo;

import java.util.List;

public class FilterResultPojo {

	
	private String id;
	
	private FilterByCriteria filterName;
	
	private FilterPojo filterClients;
	
	private EmailPojo teamEmails;
	
	
	
	

	

	

	public EmailPojo getTeamEmails() {
		return teamEmails;
	}

	public void setTeamEmails(EmailPojo teamEmails) {
		this.teamEmails = teamEmails;
	}

	public FilterPojo getFilterClients() {
		return filterClients;
	}

	public void setFilterClients(FilterPojo filterClients) {
		this.filterClients = filterClients;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public FilterByCriteria getFilterName() {
		return filterName;
	}

	public void setFilterName(FilterByCriteria filterName) {
		this.filterName = filterName;
	}

	
	
	
	
	
	
}
