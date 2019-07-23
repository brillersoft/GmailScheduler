package com.app.bo;

import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.springframework.data.mongodb.core.mapping.Document;

import com.app.pojo.Demo1;
import com.app.pojo.FilterPojo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Document(collection="Client")
@JsonIgnoreProperties(ignoreUnknown=true)
public class ClientBO {
	
	@GeneratedValue(strategy= GenerationType.AUTO)
	private String id;
	
	private String employeeIdFK;
	
	private List<FilterPojo> clients;

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

	public List<FilterPojo> getClients() {
		return clients;
	}

	public void setClients(List<FilterPojo> clients) {
		this.clients = clients;
	}
	
	
	
	
	

	
	

}
