package com.app.bo;

import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.app.pojo.ConsolidatedPojo;
import com.app.pojo.FilterByCriteria;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Document(collection="EmployeeRole")
@JsonIgnoreProperties(ignoreUnknown=true)
public class EmployeeRoleBO {
	
	@GeneratedValue(strategy= GenerationType.AUTO)
	private String id;
	
	private String designation;
	
	private String department;
	
	private String teamName;
	
//	@Id
	private String reportToId;
	
	private String role;
	
	private String employeeIdFK;
	
	private String horizontal;
	
	private String vertical;
	
	private String region;
	
	private String subRegion;
	
	private String area;
	
	private String subArea;
	
	private String channel;
	
	private String subChannel;
	
	private String territory;
	
	private String subTerritory;
	
	private String status;
	
	private String fromDate;
	
	private String toDate;
	
	private Integer teamSize;
	
	private ConsolidatedPojo consolidatedTone;
	
	private List<FilterByCriteria> employeeHierarchy;
	
	private String escalated;
	
	


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<FilterByCriteria> getEmployeeHierarchy() {
		return employeeHierarchy;
	}

	public void setEmployeeHierarchy(List<FilterByCriteria> employeeHierarchy) {
		this.employeeHierarchy = employeeHierarchy;
	}

	public String getReportToId() {
		return reportToId;
	}

	public void setReportToId(String reportToId) {
		this.reportToId = reportToId;
	}

	public String getEmployeeIdFK() {
		return employeeIdFK;
	}

	public void setEmployeeIdFK(String employeeIdFK) {
		this.employeeIdFK = employeeIdFK;
	}

	public ConsolidatedPojo getConsolidatedTone() {
		return consolidatedTone;
	}

	public void setConsolidatedTone(ConsolidatedPojo consolidatedTone) {
		this.consolidatedTone = consolidatedTone;
	}

	public Integer getTeamSize() {
		return teamSize;
	}

	public void setTeamSize(Integer teamSize) {
		this.teamSize = teamSize;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getHorizontal() {
		return horizontal;
	}

	public void setHorizontal(String horizontal) {
		this.horizontal = horizontal;
	}

	public String getVertical() {
		return vertical;
	}

	public void setVertical(String vertical) {
		this.vertical = vertical;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getSubRegion() {
		return subRegion;
	}

	public void setSubRegion(String subRegion) {
		this.subRegion = subRegion;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getSubArea() {
		return subArea;
	}

	public void setSubArea(String subArea) {
		this.subArea = subArea;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getSubChannel() {
		return subChannel;
	}

	public void setSubChannel(String subChannel) {
		this.subChannel = subChannel;
	}

	public String getTerritory() {
		return territory;
	}

	public void setTerritory(String territory) {
		this.territory = territory;
	}

	public String getSubTerritory() {
		return subTerritory;
	}

	public void setSubTerritory(String subTerritory) {
		this.subTerritory = subTerritory;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getEscalated() {
		return escalated;
	}

	public void setEscalated(String escalated) {
		this.escalated = escalated;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	
	

}
