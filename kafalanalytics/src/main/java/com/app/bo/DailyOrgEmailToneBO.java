package com.app.bo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.app.pojo.EmailToneResultPojo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Document(collection="DailyOrgEmailTone")
@JsonIgnoreProperties(ignoreUnknown=true)
public class DailyOrgEmailToneBO {
	@Id
	private String id;
	
	private String orgName;
	
	private String orgFK;
	
	private String date;
	
	private Long totalMailRecevied;
	
	private Long totalMailSent;
	
	private Long totalMail;
	
	private EmailToneResultPojo orgTone;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgFK() {
		return orgFK;
	}

	public void setOrgFK(String orgFK) {
		this.orgFK = orgFK;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
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

	public EmailToneResultPojo getOrgTone() {
		return orgTone;
	}

	public void setOrgTone(EmailToneResultPojo orgTone) {
		this.orgTone = orgTone;
	}

	
	
	
	

}
