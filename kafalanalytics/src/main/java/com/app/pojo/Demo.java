package com.app.pojo;

import java.util.List;

import com.app.bo.ToneOfMail;

public class Demo {
	
	private String companyId;
	
	private ToneOfMail allMailScore;
	
	private ToneOfMail sentMailScore;
	
	private ToneOfMail receiveMailScore;
	
	private List<Demo1> client;
	
	private Long totalMailRecevied;
	
	private Long totalMailSent;
	
	private Long totalMail;

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
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

	public List<Demo1> getClient() {
		return client;
	}

	public void setClient(List<Demo1> client) {
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
	
	
	
	
	

}
