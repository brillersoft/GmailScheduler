package com.app.service.obj;

import com.fasterxml.jackson.databind.JsonNode;

public class RequestObject {
	
	JsonNode rqBody;

	public JsonNode getRqBody() {
		return rqBody;
	}

	public void setRqBody(JsonNode rqBody) {
		this.rqBody = rqBody;
	}
	
	

}
