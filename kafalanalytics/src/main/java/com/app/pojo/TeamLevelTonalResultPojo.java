package com.app.pojo;

import java.util.List;

import com.app.bo.ToneOfMail;

public class TeamLevelTonalResultPojo {
	
	private String name;
	
	private ToneOfMail allMailScore;
	
	private ToneOfMail sentMailScore;
	
	private ToneOfMail receiveMailScore;
	
	private List<EmailPojo> lineItems;
	
	private Long totalMailRecevied;
	
	private Long totalMailSent;
	
	private Long totalMail;
	
	//private List<EmailPojo> clientEmailItems;
	
	
	
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/*public List<EmailPojo> getClientEmailItems() {
		return clientEmailItems;
	}

	public void setClientEmailItems(List<EmailPojo> clientEmailItems) {
		this.clientEmailItems = clientEmailItems;
	}*/

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

	

	public List<EmailPojo> getLineItems() {
		return lineItems;
	}

	public void setLineItems(List<EmailPojo> lineItems) {
		this.lineItems = lineItems;
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
