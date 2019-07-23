package com.app.dto;

import java.util.ArrayList;
import java.util.List;
import com.app.bo.TeamClientInteractionBO;
import com.app.bo.ToneOfMail;

public class ClientDataDTO {
	
	private String id;
	
	private List<TeamClientInteractionBO> listOfClient;
	
	private ToneOfMail allMailScore;
	
	private ToneOfMail allSentMailScore;
	
	private ToneOfMail allReceiveMailScore;
	
	private Long totalMailCount;
	
	private Long totalSentMailCount;
	
	private Long totalReceiveMailCount;
	
	private String employeeId;
	
	private Integer pageNumber;
	
	private String fromDate;
	
	private ArrayList<String> searchCriteria;
	
	private String searchText;
	
	private String searchTextClient;
	
	private String searchTextOrg;
	
	private String sortDataText;
	
	private String sortOnName;
	
	private String companyId;
	
	private String designation;
	
	private String employeeName;
	
	private String emailId;
	
	private String sortType;
	
	private String identify;
	
	private String clientEmailId;
	
	
	
	
	
	
	
	


	public String getIdentify() {
		return identify;
	}

	public void setIdentify(String identify) {
		this.identify = identify;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getSortOnName() {
		return sortOnName;
	}

	public void setSortOnName(String sortOnName) {
		this.sortOnName = sortOnName;
	}

	public String getSortDataText() {
		return sortDataText;
	}

	public void setSortDataText(String sortDataText) {
		this.sortDataText = sortDataText;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}



	public ArrayList<String> getSearchCriteria() {
		return searchCriteria;
	}

	public void setSearchCriteria(ArrayList<String> searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<TeamClientInteractionBO> getListOfClient() {
		return listOfClient;
	}

	public void setListOfClient(List<TeamClientInteractionBO> listOfClient) {
		this.listOfClient = listOfClient;
	}

	public ToneOfMail getAllMailScore() {
		return allMailScore;
	}

	public void setAllMailScore(ToneOfMail allMailScore) {
		this.allMailScore = allMailScore;
	}

	public ToneOfMail getAllSentMailScore() {
		return allSentMailScore;
	}

	public void setAllSentMailScore(ToneOfMail allSentMailScore) {
		this.allSentMailScore = allSentMailScore;
	}

	public ToneOfMail getAllReceiveMailScore() {
		return allReceiveMailScore;
	}

	public void setAllReceiveMailScore(ToneOfMail allReceiveMailScore) {
		this.allReceiveMailScore = allReceiveMailScore;
	}

	public Long getTotalMailCount() {
		return totalMailCount;
	}

	public void setTotalMailCount(Long totalMailCount) {
		this.totalMailCount = totalMailCount;
	}

	public Long getTotalSentMailCount() {
		return totalSentMailCount;
	}

	public void setTotalSentMailCount(Long totalSentMailCount) {
		this.totalSentMailCount = totalSentMailCount;
	}

	public Long getTotalReceiveMailCount() {
		return totalReceiveMailCount;
	}

	public void setTotalReceiveMailCount(Long totalReceiveMailCount) {
		this.totalReceiveMailCount = totalReceiveMailCount;
	}

	public String getSearchTextClient() {
		return searchTextClient;
	}

	public void setSearchTextClient(String searchTextClient) {
		this.searchTextClient = searchTextClient;
	}

	public String getClientEmailId() {
		return clientEmailId;
	}

	public void setClientEmailId(String clientEmailId) {
		this.clientEmailId = clientEmailId;
	}

	public String getSearchTextOrg() {
		return searchTextOrg;
	}

	public void setSearchTextOrg(String searchTextOrg) {
		this.searchTextOrg = searchTextOrg;
	}
	
	
	
	

}
