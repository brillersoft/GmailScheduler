package com.app.bo;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.app.pojo.Demo;
import com.app.pojo.EmailPojo;
import com.app.pojo.EmailToneResultPojo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Document(collection="DailyTeamEmailTone")
@JsonIgnoreProperties(ignoreUnknown=true)
public class DailyTeamEmailToneBO {
	@Id
	private String id;
	
	private String dailyEmployeeEmailToneFK;
	
	private String teamName;
	
	private EmailToneResultPojo teamTone;
	
	private EmailToneResultPojo teamToneWithClient;
	
	private String date;
	
	private List<Demo> companyList;
	
	
	
	private List<EmailPojo> clientEmailItems;
	
	
	
	
	
	
	

	public List<Demo> getCompanyList() {
		return companyList;
	}

	public void setCompanyList(List<Demo> companyList) {
		this.companyList = companyList;
	}

	public EmailToneResultPojo getTeamToneWithClient() {
		return teamToneWithClient;
	}

	public void setTeamToneWithClient(EmailToneResultPojo teamToneWithClient) {
		this.teamToneWithClient = teamToneWithClient;
	}

	public List<EmailPojo> getClientEmailItems() {
		return clientEmailItems;
	}

	public void setClientEmailItems(List<EmailPojo> clientEmailItems) {
		this.clientEmailItems = clientEmailItems;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDailyEmployeeEmailToneFK() {
		return dailyEmployeeEmailToneFK;
	}

	public void setDailyEmployeeEmailToneFK(String dailyEmployeeEmailToneFK) {
		this.dailyEmployeeEmailToneFK = dailyEmployeeEmailToneFK;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}


	

	public EmailToneResultPojo getTeamTone() {
		return teamTone;
	}

	public void setTeamTone(EmailToneResultPojo teamTone) {
		this.teamTone = teamTone;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	
	
	
	
	

}
