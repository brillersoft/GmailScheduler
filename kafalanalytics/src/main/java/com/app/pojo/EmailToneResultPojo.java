package com.app.pojo;

import com.app.bo.ToneOfMail;

public class EmailToneResultPojo {
	
	private ToneOfMail allMailScore;
	
	private ToneOfMail sentMailScore;
	
	private ToneOfMail receiveMailScore;
	
	private Long totalMailRecevied;
	
	private Long totalMailSent;
	
	private Long totalMail;
	
	
	

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
