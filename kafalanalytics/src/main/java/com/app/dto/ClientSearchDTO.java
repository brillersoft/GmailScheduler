package com.app.dto;

import java.util.List;

import com.app.pojo.ClientSearchPojo;

public class ClientSearchDTO {
	
	List<ClientSearchPojo> clientDetails;

	public List<ClientSearchPojo> getClientDetails() {
		return clientDetails;
	}

	public void setClientDetails(List<ClientSearchPojo> clientDetails) {
		this.clientDetails = clientDetails;
	}
	
	

}
