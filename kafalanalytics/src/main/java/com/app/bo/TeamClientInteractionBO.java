package com.app.bo;

import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.app.pojo.Demo;
import com.app.pojo.Demo1;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Document(collection="TeamClientInteraction")
@JsonIgnoreProperties(ignoreUnknown=true)
public class TeamClientInteractionBO {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	
	private String employeeIdFK;
	
	private String date;
	
	/*private JSONObject teamInteraction;//see the json Sturcture in DB
*/	

	private List<Demo> companyList;
	
	private List<Demo1> clients;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmployeeIdFK() {
		return employeeIdFK;
	}

	public void setEmployeeIdFK(String employeeIdFK) {
		this.employeeIdFK = employeeIdFK;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<Demo> getCompanyList() {
		return companyList;
	}

	public void setCompanyList(List<Demo> companyList) {
		this.companyList = companyList;
	}

	public List<Demo1> getClients() {
		return clients;
	}

	public void setClients(List<Demo1> clients) {
		this.clients = clients;
	}

	
	
	
	
	
	
	

}
