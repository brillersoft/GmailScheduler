package com.app.bo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.app.pojo.AddressPojo;
import com.app.pojo.ContactPersonPojo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Document(collection="Organisation")
@JsonIgnoreProperties(ignoreUnknown=true)
public class OrganisationBO {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	
	private String companyName;
	
	private AddressPojo companyLocation;
	
	private String companyEmailId;
	
	private String companyEmailDomain;
	
	private String type;
	
	private ContactPersonPojo contactPersonDetails;
	
	private int companyMargin;
	
	private int companyRevenue;
	


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public AddressPojo getCompanyLocation() {
		return companyLocation;
	}

	public void setCompanyLocation(AddressPojo companyLocation) {
		this.companyLocation = companyLocation;
	}

	public String getCompanyEmailId() {
		return companyEmailId;
	}

	public void setCompanyEmailId(String companyEmailId) {
		this.companyEmailId = companyEmailId;
	}

	public String getCompanyEmailDomain() {
		return companyEmailDomain;
	}

	public void setCompanyEmailDomain(String companyEmailDomain) {
		this.companyEmailDomain = companyEmailDomain;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ContactPersonPojo getContactPersonDetails() {
		return contactPersonDetails;
	}

	public void setContactPersonDetails(ContactPersonPojo contactPersonDetails) {
		this.contactPersonDetails = contactPersonDetails;
	}

	public int getCompanyMargin() {
		return companyMargin;
	}

	public void setCompanyMargin(int companyMargin) {
		this.companyMargin = companyMargin;
	}

	public int getCompanyRevenue() {
		return companyRevenue;
	}

	public void setCompanyRevenue(int companyRevenue) {
		this.companyRevenue = companyRevenue;
	}
	
	
	
	
	

	

}
