package com.app.pojo;

public class ConsolidatedPojo {
	
	private Long totalMailRecevied;
	
	private Long totalMailSent;
	
	private Long totalMail;
	
	private EmailToneResultPojo toneWithTeam;
	
	private EmailToneResultPojo toneWithOtherEmployee;
	
	private EmailToneResultPojo toneWithClient;

	

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

	public EmailToneResultPojo getToneWithTeam() {
		return toneWithTeam;
	}

	public void setToneWithTeam(EmailToneResultPojo toneWithTeam) {
		this.toneWithTeam = toneWithTeam;
	}

	public EmailToneResultPojo getToneWithOtherEmployee() {
		return toneWithOtherEmployee;
	}

	public void setToneWithOtherEmployee(EmailToneResultPojo toneWithOtherEmployee) {
		this.toneWithOtherEmployee = toneWithOtherEmployee;
	}

	public EmailToneResultPojo getToneWithClient() {
		return toneWithClient;
	}

	public void setToneWithClient(EmailToneResultPojo toneWithClient) {
		this.toneWithClient = toneWithClient;
	}
	
	

}
