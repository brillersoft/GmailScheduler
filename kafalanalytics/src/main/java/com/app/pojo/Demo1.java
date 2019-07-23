package com.app.pojo;

import java.util.List;

import com.app.bo.ToneOfMail;

public class Demo1 {

	
	private String name;
	
	private String emailId;
	
	private String companyFK;
	
	private String designation;
	
	private String executive;
	
	private ToneOfMail allMailScore;
	
	private ToneOfMail sentMailScore;
	
	private ToneOfMail receiveMailScore;
	
	private List<EmailPojo> client;
	
	private Long totalMailRecevied;
	
	private Long totalMailSent;
	
	private Long totalMail;
	
	
	
	
	

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getCompanyFK() {
		return companyFK;
	}

	public void setCompanyFK(String companyFK) {
		this.companyFK = companyFK;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
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

	public List<EmailPojo> getClient() {
		return client;
	}

	public void setClient(List<EmailPojo> client) {
		this.client = client;
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

	public String getExecutive() {
		return executive;
	}

	public void setExecutive(String executive) {
		this.executive = executive;
	}
	
	
	
	
}
