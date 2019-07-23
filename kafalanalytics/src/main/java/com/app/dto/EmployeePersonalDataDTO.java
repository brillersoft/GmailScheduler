package com.app.dto;

import java.util.List;

import com.app.bo.TeamClientInteractionBO;
import com.app.bo.EmployeeBO;
import com.app.bo.EmployeeRelationBO;
import com.app.bo.ToneOfMail;
import com.app.pojo.ConsolidatedPojo;
import com.app.pojo.EmailPojo;
import com.app.pojo.EmailToneResultPojo;

public class EmployeePersonalDataDTO {
	
	private String id;
	
	private String employeeId;
	
	private String employeeName;
	
	private List<EmployeePersonalDataDTO> listOfEmployee;
	
	private List<EmailPojo> listEmailAnalyser;
	
	private String reportTo;
	
	private String department;
	
	private String designation;
	
	private String emailId;
	
	private Long noOfMail;
	
	private Long noOfSentMail;
	
	private Long noOfReceiveMail;
	
	private ToneOfMail toneOfTeamReceiveMail;
	
	private ToneOfMail toneOfTeamSentMail;
	
	private Integer noOfTeamMember;
	
	private ToneOfMail toneOfPersonalMail;
	
	private ToneOfMail toneOfTeamMail;
	
	private ToneOfMail toneOfClientMail;
	
	private ToneOfMail toneOfClientsentMail;
	
	private ToneOfMail toneOfClientReceiveMail;
	
	private EmployeeRelationBO relationWithTeam;
	
	private EmployeeRelationBO relationWithClient;
	
	//private ConsolidatedPojo consolidatedTone;
	
	private EmailToneResultPojo teamToneOfEachEmployee;
	
	private Integer pageNumber;
	
	private Integer skip;
	
	private Integer limit;
	
	private String identify;
	
	private String companyId;
	
	private String companyName;
	
	private String emailSubject;
	
	private String emailType;
	
	private String startDate;
	
	private String endDate;
	
	private String startTime;
	
	private String endTime;
	
	private String role;
	
	private List<String> empReportToHierachy;
	
	private List<String> empReportToId;
	
	private String clientEmailId;
	
	private String searchTextClient;
	
	private String searchTextOrg;
	
	private String executive;
	
	

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getEmailType() {
		return emailType;
	}

	public void setEmailType(String emailType) {
		this.emailType = emailType;
	}

	public String getEmailSubject() {
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getIdentify() {
		return identify;
	}

	public void setIdentify(String identify) {
		this.identify = identify;
	}

	public ToneOfMail getToneOfClientsentMail() {
		return toneOfClientsentMail;
	}

	public void setToneOfClientsentMail(ToneOfMail toneOfClientsentMail) {
		this.toneOfClientsentMail = toneOfClientsentMail;
	}

	public ToneOfMail getToneOfClientReceiveMail() {
		return toneOfClientReceiveMail;
	}

	public void setToneOfClientReceiveMail(ToneOfMail toneOfClientReceiveMail) {
		this.toneOfClientReceiveMail = toneOfClientReceiveMail;
	}

	public Integer getSkip() {
		return skip;
	}

	public void setSkip(Integer skip) {
		this.skip = skip;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public List<EmailPojo> getListEmailAnalyser() {
		return listEmailAnalyser;
	}

	public void setListEmailAnalyser(List<EmailPojo> listEmailAnalyser) {
		this.listEmailAnalyser = listEmailAnalyser;
	}

	public EmailToneResultPojo getTeamToneOfEachEmployee() {
		return teamToneOfEachEmployee;
	}

	public void setTeamToneOfEachEmployee(EmailToneResultPojo teamToneOfEachEmployee) {
		this.teamToneOfEachEmployee = teamToneOfEachEmployee;
	}

	public String getId() {
		return id;
	}

	public Long getNoOfSentMail() {
		return noOfSentMail;
	}

	public void setNoOfSentMail(Long noOfSentMail) {
		this.noOfSentMail = noOfSentMail;
	}

	public Long getNoOfReceiveMail() {
		return noOfReceiveMail;
	}

	public void setNoOfReceiveMail(Long noOfReceiveMail) {
		this.noOfReceiveMail = noOfReceiveMail;
	}

	public ToneOfMail getToneOfTeamReceiveMail() {
		return toneOfTeamReceiveMail;
	}

	public void setToneOfTeamReceiveMail(ToneOfMail toneOfTeamReceiveMail) {
		this.toneOfTeamReceiveMail = toneOfTeamReceiveMail;
	}

	public ToneOfMail getToneOfTeamSentMail() {
		return toneOfTeamSentMail;
	}

	public void setToneOfTeamSentMail(ToneOfMail toneOfTeamSentMail) {
		this.toneOfTeamSentMail = toneOfTeamSentMail;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public ToneOfMail getToneOfClientMail() {
		return toneOfClientMail;
	}

	public void setToneOfClientMail(ToneOfMail toneOfClientMail) {
		this.toneOfClientMail = toneOfClientMail;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}


	public String getReportTo() {
		return reportTo;
	}

	public void setReportTo(String reportTo) {
		this.reportTo = reportTo;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public Long getNoOfMail() {
		return noOfMail;
	}

	public void setNoOfMail(Long noOfMail) {
		this.noOfMail = noOfMail;
	}

	public Integer getNoOfTeamMember() {
		return noOfTeamMember;
	}

	public void setNoOfTeamMember(Integer noOfTeamMember) {
		this.noOfTeamMember = noOfTeamMember;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public ToneOfMail getToneOfPersonalMail() {
		return toneOfPersonalMail;
	}

	public void setToneOfPersonalMail(ToneOfMail toneOfPersonalMail) {
		this.toneOfPersonalMail = toneOfPersonalMail;
	}

	public ToneOfMail getToneOfTeamMail() {
		return toneOfTeamMail;
	}

	public void setToneOfTeamMail(ToneOfMail toneOfTeamMail) {
		this.toneOfTeamMail = toneOfTeamMail;
	}

	public EmployeeRelationBO getRelationWithTeam() {
		return relationWithTeam;
	}

	public void setRelationWithTeam(EmployeeRelationBO relationWithTeam) {
		this.relationWithTeam = relationWithTeam;
	}

	public EmployeeRelationBO getRelationWithClient() {
		return relationWithClient;
	}

	public void setRelationWithClient(EmployeeRelationBO relationWithClient) {
		this.relationWithClient = relationWithClient;
	}

	public List<EmployeePersonalDataDTO> getListOfEmployee() {
		return listOfEmployee;
	}

	public void setListOfEmployee(List<EmployeePersonalDataDTO> listOfEmployee) {
		this.listOfEmployee = listOfEmployee;
	}
	
	public List<String> getEmpReportToHierachy() {
		return empReportToHierachy;
	}

	public void setEmpReportToHierachy(List<String> empReportToHierachy) {
		this.empReportToHierachy = empReportToHierachy;
	}

	public List<String> getEmpReportToId() {
		return empReportToId;
	}

	public void setEmpReportToId(List<String> empReportToId) {
		this.empReportToId = empReportToId;
	}

	public String getClientEmailId() {
		return clientEmailId;
	}

	public void setClientEmailId(String clientEmailId) {
		this.clientEmailId = clientEmailId;
	}

	public String getSearchTextClient() {
		return searchTextClient;
	}

	public void setSearchTextClient(String searchTextClient) {
		this.searchTextClient = searchTextClient;
	}

	public String getSearchTextOrg() {
		return searchTextOrg;
	}

	public void setSearchTextOrg(String searchTextOrg) {
		this.searchTextOrg = searchTextOrg;
	}

	public String getExecutive() {
		return executive;
	}

	public void setExecutive(String executive) {
		this.executive = executive;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	
	
	



}
