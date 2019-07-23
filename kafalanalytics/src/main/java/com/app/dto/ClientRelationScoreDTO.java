package com.app.dto;

import com.app.bo.ToneOfMail;

public class ClientRelationScoreDTO {

	private String emailIdToClient;
	
	private Long totalEmail;
	
	private Long totalSentEmail;
	
	private Long totalReceivedEmail;
	
	private ToneOfMail overAllToneWithEachClient;
	
	private ToneOfMail receivedToneWithEachClient;
	
	private ToneOfMail sentToneWithEachClient;

	public String getEmailIdToClient() {
		return emailIdToClient;
	}

	public void setEmailIdToClient(String emailIdToClient) {
		this.emailIdToClient = emailIdToClient;
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

	public ToneOfMail getOverAllToneWithEachClient() {
		return overAllToneWithEachClient;
	}

	public void setOverAllToneWithEachClient(ToneOfMail overAllToneWithEachClient) {
		this.overAllToneWithEachClient = overAllToneWithEachClient;
	}

	public ToneOfMail getReceivedToneWithEachClient() {
		return receivedToneWithEachClient;
	}

	public void setReceivedToneWithEachClient(ToneOfMail receivedToneWithEachClient) {
		this.receivedToneWithEachClient = receivedToneWithEachClient;
	}

	public ToneOfMail getSentToneWithEachClient() {
		return sentToneWithEachClient;
	}

	public void setSentToneWithEachClient(ToneOfMail sentToneWithEachClient) {
		this.sentToneWithEachClient = sentToneWithEachClient;
	}
	
	
	
	
	
	
	
}
