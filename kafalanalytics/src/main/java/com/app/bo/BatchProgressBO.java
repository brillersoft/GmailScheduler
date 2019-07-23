package com.app.bo;

import javax.persistence.GeneratedValue;

import javax.persistence.GenerationType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.app.pojo.AddressPojo;
import com.app.pojo.ContactPersonPojo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Document(collection="BatchProgress")
@JsonIgnoreProperties(ignoreUnknown=true)
public class BatchProgressBO {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	
	private String adminId;
	
	private Integer totalEmps;
	
	private Integer currentEmpNum;
	
	private String currentEmailId;
	
	private Integer totalMails;
	
	private Integer currentMailNum;
	
	private String typeMailbox;
	
	private String status;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public Integer getTotalEmps() {
		return totalEmps;
	}

	public void setTotalEmps(Integer totalEmps) {
		this.totalEmps = totalEmps;
	}

	public Integer getCurrentEmpNum() {
		return currentEmpNum;
	}

	public void setCurrentEmpNum(Integer currentEmpNum) {
		this.currentEmpNum = currentEmpNum;
	}

	public String getCurrentEmailId() {
		return currentEmailId;
	}

	public void setCurrentEmailId(String currentEmailId) {
		this.currentEmailId = currentEmailId;
	}

	public Integer getTotalMails() {
		return totalMails;
	}

	public void setTotalMails(Integer totalMails) {
		this.totalMails = totalMails;
	}

	public Integer getCurrentMailNum() {
		return currentMailNum;
	}

	public void setCurrentMailNum(Integer currentMailNum) {
		this.currentMailNum = currentMailNum;
	}

	public String getTypeMailbox() {
		return typeMailbox;
	}

	public void setTypeMailbox(String typeMailbox) {
		this.typeMailbox = typeMailbox;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}	
	
	

}

