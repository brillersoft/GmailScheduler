package com.app.bo;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class VendorInfo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	private long vendorId;
	
	private String vendorName;
	
	private int vendorRevenue;
	
	private int vendorMargin;
	
	private int vendorHappyPercentage;
	
	private int vendorUnhappyPercentage;

	public long getVendorId() {
		return vendorId;
	}

	public void setVendorId(long vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public int getVendorRevenue() {
		return vendorRevenue;
	}

	public void setVendorRevenue(int vendorRevenue) {
		this.vendorRevenue = vendorRevenue;
	}

	public int getVendorMargin() {
		return vendorMargin;
	}

	public void setVendorMargin(int vendorMargin) {
		this.vendorMargin = vendorMargin;
	}

	public int getVendorHappyPercentage() {
		return vendorHappyPercentage;
	}

	public void setVendorHappyPercentage(int vendorHappyPercentage) {
		this.vendorHappyPercentage = vendorHappyPercentage;
	}

	public int getVendorUnhappyPercentage() {
		return vendorUnhappyPercentage;
	}

	public void setVendorUnhappyPercentage(int vendorUnhappyPercentage) {
		this.vendorUnhappyPercentage = vendorUnhappyPercentage;
	}
	
	

}
