package com.app.dto;

public class BatchProgressDTO {
	
	private String userId;
	
	private String adminId;
	
	private Integer totalEmps;
	
	private Integer currentEmpNum;
	
	private String currentEmailId;
	
	private Integer totalMails;
	
	private Integer currentMailNum;
	
	private String typeMailbox;

	private String errorMsg;
	
	private String status;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	

	
	
}
