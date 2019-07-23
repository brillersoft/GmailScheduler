package com.app.dto;

import com.app.bo.ToneOfMail;

public class EmployeeRelationScoreDTO {
	
	private String emailId;
	
	private Long totalEmail;
	
	private Long totalSentEmail;
	
	private Long totalReceivedEmail;
	
	private ToneOfMail overAllToneWithEachEmployee;
	
	private ToneOfMail receivedToneWithEachEmployee;
	
	private ToneOfMail sentToneWithEachEmployee;

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Long getTotalEmail() {
		return totalEmail;
	}

	public void setTotalEmail(Long totalEmail) {
		this.totalEmail = totalEmail;
	}

	public Long getTotalSentEmail() {
		return totalSentEmail;
	}

	public void setTotalSentEmail(Long totalSentEmail) {
		this.totalSentEmail = totalSentEmail;
	}

	public Long getTotalReceivedEmail() {
		return totalReceivedEmail;
	}

	public void setTotalReceivedEmail(Long totalReceivedEmail) {
		this.totalReceivedEmail = totalReceivedEmail;
	}

	public ToneOfMail getOverAllToneWithEachEmployee() {
		return overAllToneWithEachEmployee;
	}

	public void setOverAllToneWithEachEmployee(ToneOfMail overAllToneWithEachEmployee) {
		this.overAllToneWithEachEmployee = overAllToneWithEachEmployee;
	}

	public ToneOfMail getReceivedToneWithEachEmployee() {
		return receivedToneWithEachEmployee;
	}

	public void setReceivedToneWithEachEmployee(ToneOfMail receivedToneWithEachEmployee) {
		this.receivedToneWithEachEmployee = receivedToneWithEachEmployee;
	}

	public ToneOfMail getSentToneWithEachEmployee() {
		return sentToneWithEachEmployee;
	}

	public void setSentToneWithEachEmployee(ToneOfMail sentToneWithEachEmployee) {
		this.sentToneWithEachEmployee = sentToneWithEachEmployee;
	}
	

	
	

}
