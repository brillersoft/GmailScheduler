package com.app.utilities;

import com.app.service.obj.ResponseObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ExceptionResponse {
	
	public static void generateRDDErrorResponse(ResponseObject responseObj,String customMessage)
	{
		try{
			ObjectMapper objectMapper = new ObjectMapper();
			
			
			String responseString = null;
			
			if(null == customMessage)
			{
				responseString = "{\"exceptionBlock\" : {\"result\" : \"error\" , \"msg\" :   \"Error ..Contact KnowledgeOps Team\"}}";
			}
			else
			{
				responseString =  customMessage ;
			}
			
			JsonNode rsBodyObj = objectMapper.readTree(responseString);
			responseObj.setRsBody(rsBodyObj);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
	}
	public static void generateErrorResponse(ResponseObject responseObj,String customMessage) 
	{
		try{
		ObjectMapper objectMapper = new ObjectMapper();
		
		
		String responseString = null;
		
		if(null == customMessage)
		{
			responseString = "{\"exceptionBlock\" : {\"result\" : \"error\" , \"msg\" :   \"Error ..Contact KnowledgeOps Team\"}}";
		}
		else
		{
			responseString = "{\"exceptionBlock\" : {\"result\" : \"error\" , \"msg\" :  " + customMessage  + "}}";
		}
		
		JsonNode rsBodyObj = objectMapper.readTree(responseString);
		responseObj.setRsBody(rsBodyObj);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void generateErrorResponseResetPassword(ResponseObject responseObj,String customMessage) 
	{
		try{
		ObjectMapper objectMapper = new ObjectMapper();
		
		
		String responseString = null;
		
		if(null == customMessage)
		{
			responseString = "{\"exceptionBlock\" : {\"result\" : \"error\" , \"msg\" :   \"Error ..Contact KnowledgeOps Team\"}}";
		}
		else
		{
			responseString = "{\"exceptionBlock\" : {\"result\" : \"error\" , \"msg\" : \"Invalid Old Password\"}}";
		}
		
		JsonNode rsBodyObj = objectMapper.readTree(responseString);
		responseObj.setRsBody(rsBodyObj);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void generateErrorResponseUserExist(ResponseObject responseObj,String customMessage) 
	{
		try{
		ObjectMapper objectMapper = new ObjectMapper();
		
		
		String responseString = null;
		
		if(null == customMessage)
		{
			responseString = "{\"exceptionBlock\" : {\"result\" : \"error\" , \"msg\" :   \"error\"}}";
		}
		else
		{
			responseString = "{\"exceptionBlock\" : {\"result\" : \"error\" , \"msg\" : \"UserException\"}}";
		}
		
		JsonNode rsBodyObj = objectMapper.readTree(responseString);
		responseObj.setRsBody(rsBodyObj);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void generateLoginErrorResponse(ResponseObject responseObj,String customMessage) 
	{
		try{
		ObjectMapper objectMapper = new ObjectMapper();
		
		
		String responseString = null;
		
		responseString = "{\"result\" : \"error\" , \"msg\" :  \" Invalid EmailId or Password \"}";
		
		
		JsonNode rsBodyObj = objectMapper.readTree(responseString);
		responseObj.setRsBody(rsBodyObj);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void generateInvalidEmailErrorResponse(ResponseObject responseObj,String customMessage) 
	{
		try{
		ObjectMapper objectMapper = new ObjectMapper();
		
		
		String responseString = null;
		
		responseString = "{\"result\" : \"error\" , \"msg\" :  \" Invalid EmailId \"}";
		
		
		JsonNode rsBodyObj = objectMapper.readTree(responseString);
		responseObj.setRsBody(rsBodyObj);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void generateSuccessResponse(ResponseObject responseObj, Object data)
	{
		try{
		ObjectMapper objectMapper = new ObjectMapper();
		
		String responseString = null;
		JsonNode rsBodyObj  =  null;
		JsonNode dataString = null;
		if(null == data)
		{
			responseString = "{\"result\" : \"success\" , \"msg\" : \"success\"}";
			rsBodyObj = objectMapper.readTree(responseString);
		}
		else
		{
			
			
			dataString = objectMapper.valueToTree(data);
			responseString = "{\"result\" : \"success\" , \"msg\" :" + dataString + "}";
			//String finalJson = responseString.replaceAll("^\"|\"$","").replace("\\\"", "\"");
			rsBodyObj = objectMapper.readTree(responseString);
		}
		responseObj.setRsBody(rsBodyObj);
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		
	}
	
	public static void generateListCustomerSuccessResponse(ResponseObject responseObj, Object data)
	{
		try{
			ObjectMapper objectMapper = new ObjectMapper();
			
			String responseString = null;
			JsonNode rsBodyObj  =  null;
			JsonNode dataString = null;
			
			dataString = objectMapper.valueToTree(data);
			responseString = "{\"search\" : \"custom_customer\" , \"result\" : \"success\" , \"msg\" :" + dataString + "}";
			//String finalJson = responseString.replaceAll("^\"|\"$","").replace("\\\"", "\"");
			rsBodyObj = objectMapper.readTree(responseString);
			responseObj.setRsBody(rsBodyObj);
		}catch(Exception e)
			{
				e.printStackTrace();
			}
	}
	
	public static void generateListEmployeeSuccessResponse(ResponseObject responseObj, Object data)
	{
		try{
			ObjectMapper objectMapper = new ObjectMapper();
			
			String responseString = null;
			JsonNode rsBodyObj  =  null;
			JsonNode dataString = null;
			
			dataString = objectMapper.valueToTree(data);
			responseString = "{\"search\" : \"custom_employee\" , \"result\" : \"success\" , \"msg\" :" + dataString + "}";
			//String finalJson = responseString.replaceAll("^\"|\"$","").replace("\\\"", "\"");
			rsBodyObj = objectMapper.readTree(responseString);
			responseObj.setRsBody(rsBodyObj);
		}catch(Exception e)
			{
				e.printStackTrace();
			}
	}
	
	public static void generateDirectSuccessResponse(ResponseObject responseObj, Object data)
	{
		try{
			ObjectMapper objectMapper = new ObjectMapper();
			
			String responseString = null;
			JsonNode rsBodyObj  =  null;
			JsonNode dataString = null;
			
			dataString = objectMapper.valueToTree(data);
			responseString = "{\"search\" : \"direct\" , \"result\" : \"success\" , \"msg\" :" + dataString + "}";
			//String finalJson = responseString.replaceAll("^\"|\"$","").replace("\\\"", "\"");
			rsBodyObj = objectMapper.readTree(responseString);
			responseObj.setRsBody(rsBodyObj);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void generateCustomeEmployeeSuccessResponse(ResponseObject responseObj, Object data)
	{
		try{
			ObjectMapper objectMapper = new ObjectMapper();
			
			String responseString = null;
			JsonNode rsBodyObj  =  null;
			JsonNode dataString = null;
			
			dataString = objectMapper.valueToTree(data);
			responseString = "{\"search\" : \"custom_employee\" , \"result\" : \"success\" , \"msg\" :" + dataString + "}";
			//String finalJson = responseString.replaceAll("^\"|\"$","").replace("\\\"", "\"");
			rsBodyObj = objectMapper.readTree(responseString);
			responseObj.setRsBody(rsBodyObj);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void generateCustomCustomerSuccessResponse(ResponseObject responseObj, Object data)
	{
		try{
			ObjectMapper objectMapper = new ObjectMapper();
			
			String responseString = null;
			JsonNode rsBodyObj  =  null;
			JsonNode dataString = null;
			
			dataString = objectMapper.valueToTree(data);
			responseString = "{\"search\" : \"custom_customer\" , \"result\" : \"success\" , \"msg\" :" + dataString + "}";
			//String finalJson = responseString.replaceAll("^\"|\"$","").replace("\\\"", "\"");
			rsBodyObj = objectMapper.readTree(responseString);
			responseObj.setRsBody(rsBodyObj);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void generateMilestoneDirectSuccessResponse(ResponseObject responseObj, Object data)
	{
		try{
			ObjectMapper objectMapper = new ObjectMapper();
			
			String responseString = null;
			JsonNode rsBodyObj  =  null;
			JsonNode dataString = null;
			
			dataString = objectMapper.valueToTree(data);
			responseString = "{\"datafound\" : \"true\" , \"type\" : \"milestoneData\" ,\"search\" : \"direct\" , \"result\" : \"success\" , \"msg\" :" + dataString + "}";
			//String finalJson = responseString.replaceAll("^\"|\"$","").replace("\\\"", "\"");
			rsBodyObj = objectMapper.readTree(responseString);
			responseObj.setRsBody(rsBodyObj);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void generateMilestoneDirectSuccessResponseNoData(ResponseObject responseObj, Object data)
	{
		try{
			ObjectMapper objectMapper = new ObjectMapper();
			
			String responseString = null;
			JsonNode rsBodyObj  =  null;
			JsonNode dataString = null;
			
			dataString = objectMapper.valueToTree(data);
			responseString = "{\"datafound\" : \"false\" , \"type\" : \"milestoneData\" ,\"search\" : \"direct\" , \"result\" : \"success\" , \"msg\" :" + dataString + "}";
			//String finalJson = responseString.replaceAll("^\"|\"$","").replace("\\\"", "\"");
			rsBodyObj = objectMapper.readTree(responseString);
			responseObj.setRsBody(rsBodyObj);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void generateMasterDirectSuccessResponse(ResponseObject responseObj, Object data)
	{
		try{
			ObjectMapper objectMapper = new ObjectMapper();
			
			String responseString = null;
			JsonNode rsBodyObj  =  null;
			JsonNode dataString = null;
			
			dataString = objectMapper.valueToTree(data);
			responseString = "{\"type\" : \"masterData\" ,\"search\" : \"direct\" , \"result\" : \"success\" , \"msg\" :" + dataString + "}";
			//String finalJson = responseString.replaceAll("^\"|\"$","").replace("\\\"", "\"");
			rsBodyObj = objectMapper.readTree(responseString);
			responseObj.setRsBody(rsBodyObj);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void generateSuccessResponseUpload(ResponseObject responseObj, Object data)
	{
		try{
		ObjectMapper objectMapper = new ObjectMapper();
		
		String responseString = null;
		JsonNode rsBodyObj  =  null;
		if(null == data)
		{
			responseString = "{\"msg\" : \"SUCCESS\"}";
			rsBodyObj = objectMapper.readTree(responseString);
		}
		else
		{
			rsBodyObj = objectMapper.valueToTree(data);
		}
		responseObj.setRsBody(rsBodyObj);
		}
		catch(Exception e)
		{
			
		}
		
		
		
	}

}
