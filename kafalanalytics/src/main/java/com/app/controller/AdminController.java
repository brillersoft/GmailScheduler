package com.app.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;

import com.app.pojo.ClientSearchPojo;
import com.app.pojo.EmployeeSearchPojo;
import com.app.pojo.GmailConfig;
import com.app.pojo.OrganizationPojo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.dto.BatchProgressDTO;
import com.app.dto.ClientSearchDTO;
import com.app.dto.CronDTO;
import com.app.dto.EmailConfigurationDTO;
import com.app.dto.EmployeePersonalDataDTO;
import com.app.dto.EmployeeSearchDTO;
import com.app.mongo.repositories.CompanyRepository;
import com.app.service.obj.RequestObject;
import com.app.service.obj.ResponseObject;
import com.app.services.AdminService;
import com.app.services.EmployeeService;
import com.app.utilities.AppUtils;
import com.app.utilities.ExceptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController

public class AdminController {
	@Autowired
	CompanyRepository companyRepository;
	@Autowired
	AdminService adminService;
	
	@Autowired
	EmployeeService employeeService;
	// For saving data from Manually.
	@RequestMapping(value = "/auth/saveorg", method = RequestMethod.POST)
	public @ResponseBody ResponseObject saveorg(@RequestBody RequestObject requestObject) {
	
		ResponseObject responseObject = new ResponseObject();
		try {
		ObjectMapper objectMapper = new ObjectMapper();
		ArrayList<OrganizationPojo> organizationList= new ArrayList<OrganizationPojo>();
		OrganizationPojo[] organizationPojo = objectMapper.treeToValue(requestObject.getRqBody(),OrganizationPojo[].class);
		 // Add all elements to the ArrayList from an array.
        Collections.addAll(organizationList, organizationPojo);
        organizationList=ReadExcelData.checknullOrganization(organizationList);
        String  status= adminService.saveorg(organizationList);
//              String result= "";
//                if (status) {
//                	result= "101";
//                }
                ExceptionResponse.generateSuccessResponse(responseObject,status); 
		}
		catch (Exception e) {
			//responseObject.setRsBody(e.getMessage());
			ExceptionResponse.generateErrorResponse(responseObject, "500");
			e.printStackTrace();
		}
		
		
		return responseObject;
		
		
	}
	
	

	// For saving data from Excel file.
	@RequestMapping(method = RequestMethod.POST, value = "/auth/savemultiplefile")
	//@ResponseBody
	private @ResponseBody ResponseObject runImportRecordsJob(@RequestParam("file") MultipartFile file){	
		ResponseObject responseObject = new ResponseObject();
		try {
			
			ObjectMapper objectMapper = new ObjectMapper();
			String[] provider_header = AppUtils.CLIENT_HEADER;
			File convertedfile=ReadExcelData.fileconverter(file);
			ArrayList<String> a1=ReadExcelData.processfile(convertedfile,provider_header);
			ArrayList<OrganizationPojo> organizationList=ReadExcelData.orgmapper(a1,provider_header);
			 organizationList=ReadExcelData.checknullOrganizationmultiple(organizationList);
			String status = adminService.saveorg(organizationList);
			
			ExceptionResponse.generateSuccessResponse(responseObject,status); 
			
		} catch ( Exception e) {
			String str = e.getMessage();
			System.out.println("Exception is--" + str);
			//ExceptionResponse.generateErrorResponse(responseObject, str);
			ExceptionResponse.generateSuccessResponse(responseObject,str); 
			e.printStackTrace();
		}
		return responseObject;
		
	}
	
	@RequestMapping(value = "/auth/findEmp", method = RequestMethod.POST)
	public @ResponseBody ResponseObject findEmp(@RequestBody RequestObject requestObject) {
	
		ResponseObject responseObject = new ResponseObject();
		try {
		ObjectMapper objectMapper = new ObjectMapper();
		ArrayList<EmployeeSearchPojo> employee= new ArrayList<EmployeeSearchPojo>();
		EmployeeSearchPojo[] employeePojo = objectMapper.treeToValue(requestObject.getRqBody(),EmployeeSearchPojo[].class);
		 // Add all elements to the ArrayList from an array.
        Collections.addAll(employee, employeePojo);
        if(employee.get(0).getEmployeeName().contains("@")) {
        	employee.get(0).setEmailId(employee.get(0).getEmployeeName());
        	employee.get(0).setEmployeeName("");
        }
        EmployeeSearchDTO employeeData= adminService.findEmp(employee);
        
        ExceptionResponse.generateSuccessResponse(responseObject,employeeData); 
		
		}
		catch (Exception e) {
			// TODO: handle eoxception
			e.printStackTrace();
		}
		
		
		return responseObject;
		
		
	}
	
	
	@RequestMapping(value = "/auth/delEmp", method = RequestMethod.POST)
	public @ResponseBody ResponseObject delEmp(@RequestBody RequestObject requestObject) {
	
		ResponseObject responseObject = new ResponseObject();
		try {
		ObjectMapper objectMapper = new ObjectMapper();
		ArrayList<EmployeeSearchPojo> employee= new ArrayList<EmployeeSearchPojo>();
		EmployeeSearchPojo[] employeePojo = objectMapper.treeToValue(requestObject.getRqBody(),EmployeeSearchPojo[].class);
		 // Add all elements to the ArrayList from an array.
        Collections.addAll(employee, employeePojo);
//        if(client.get(0).getClientName().contains("@")) {
//        	client.get(0).setEmailId(client.get(0).getClientName());
//        	client.get(0).setClientName("");
//        }
        adminService.delEmp(employee);
        
        String success = "success";
        
        ExceptionResponse.generateSuccessResponse(responseObject,success); 
		
		}
		catch (Exception e) {
			// TODO: handle eoxception
			ExceptionResponse.generateErrorResponse(responseObject, "error");
			e.printStackTrace();
		}
		
		
		return responseObject;
		
		
	}
	
	@RequestMapping(value = "/auth/editEmp", method = RequestMethod.POST)
	public @ResponseBody ResponseObject editEmp(@RequestBody RequestObject requestObject) {
	
		ResponseObject responseObject = new ResponseObject();
		try {
		ObjectMapper objectMapper = new ObjectMapper();
		ArrayList<EmployeeSearchPojo> employee= new ArrayList<EmployeeSearchPojo>();
		EmployeeSearchPojo[] employeePojo = objectMapper.treeToValue(requestObject.getRqBody(),EmployeeSearchPojo[].class);
		 // Add all elements to the ArrayList from an array.
        Collections.addAll(employee, employeePojo);
//        if(client.get(0).getClientName().contains("@")) {
//        	client.get(0).setEmailId(client.get(0).getClientName());
//        	client.get(0).setClientName("");
//        }
//        adminService.editEmp(employee);
        
        String success = adminService.editEmp(employee);
        
        if(success.equals("success")) {
        	ExceptionResponse.generateSuccessResponse(responseObject,success); 
        }else {
        	ExceptionResponse.generateErrorResponse(responseObject, success);
        }
        
        
		
		}
		catch (Exception e) {
			// TODO: handle eoxception
			ExceptionResponse.generateErrorResponse(responseObject, "error");
			e.printStackTrace();
		}
		
		
		return responseObject;
		
		
	}
	
	
	@RequestMapping(value = "/auth/findCl", method = RequestMethod.POST)
	public @ResponseBody ResponseObject findCl(@RequestBody RequestObject requestObject) {
	
		ResponseObject responseObject = new ResponseObject();
		try {
		ObjectMapper objectMapper = new ObjectMapper();
		ArrayList<ClientSearchPojo> client= new ArrayList<ClientSearchPojo>();
		ClientSearchPojo[] clientPojo = objectMapper.treeToValue(requestObject.getRqBody(),ClientSearchPojo[].class);
		 // Add all elements to the ArrayList from an array.
        Collections.addAll(client, clientPojo);
        if(client.get(0).getClientName().contains("@")) {
        	client.get(0).setEmailId(client.get(0).getClientName());
        	client.get(0).setClientName("");
        }
        ClientSearchDTO clientData= adminService.findCl(client);
        
        ExceptionResponse.generateSuccessResponse(responseObject,clientData); 
		
		}
		catch (Exception e) {
			// TODO: handle eoxception
			e.printStackTrace();
		}
		
		
		return responseObject;
		
		
	}
	
	@RequestMapping(value = "/auth/delCl", method = RequestMethod.POST)
	public @ResponseBody ResponseObject delCl(@RequestBody RequestObject requestObject) {
	
		ResponseObject responseObject = new ResponseObject();
		try {
		ObjectMapper objectMapper = new ObjectMapper();
		ArrayList<ClientSearchPojo> client= new ArrayList<ClientSearchPojo>();
		ClientSearchPojo[] clientPojo = objectMapper.treeToValue(requestObject.getRqBody(),ClientSearchPojo[].class);
		 // Add all elements to the ArrayList from an array.
        Collections.addAll(client, clientPojo);
//        if(client.get(0).getClientName().contains("@")) {
//        	client.get(0).setEmailId(client.get(0).getClientName());
//        	client.get(0).setClientName("");
//        }
        adminService.delCl(client);
        
        String success = "success";
        
        ExceptionResponse.generateSuccessResponse(responseObject,success); 
		
		}
		catch (Exception e) {
			// TODO: handle eoxception
			ExceptionResponse.generateErrorResponse(responseObject, "error");
			e.printStackTrace();
		}
		
		
		return responseObject;
		
		
	}
	
	@RequestMapping(value = "/auth/editCl", method = RequestMethod.POST)
	public @ResponseBody ResponseObject editCl(@RequestBody RequestObject requestObject) {
	
		ResponseObject responseObject = new ResponseObject();
		try {
		ObjectMapper objectMapper = new ObjectMapper();
		ArrayList<ClientSearchPojo> client= new ArrayList<ClientSearchPojo>();
		ClientSearchPojo[] clientPojo = objectMapper.treeToValue(requestObject.getRqBody(),ClientSearchPojo[].class);
		 // Add all elements to the ArrayList from an array.
        Collections.addAll(client, clientPojo);
//        if(client.get(0).getClientName().contains("@")) {
//        	client.get(0).setEmailId(client.get(0).getClientName());
//        	client.get(0).setClientName("");
//        }
        adminService.editCl(client);
        
        String success = "success";
        
        ExceptionResponse.generateSuccessResponse(responseObject,success); 
		
		}
		catch (Exception e) {
			// TODO: handle eoxception
			ExceptionResponse.generateErrorResponse(responseObject, "error");
			e.printStackTrace();
		}
		
		
		return responseObject;
		
		
	}

	@RequestMapping(value = "/auth/savecron", method = RequestMethod.POST)
	public @ResponseBody ResponseObject savecron(@RequestBody RequestObject requestObject) {
	
		ResponseObject responseObject = new ResponseObject();
		try {
		ObjectMapper objectMapper = new ObjectMapper();
		CronDTO cronDTO = new CronDTO();
		cronDTO = objectMapper.treeToValue(requestObject.getRqBody(),CronDTO.class);
//		System.err.println(cronDTO);
		String status = adminService.setCronJob(cronDTO);
                ExceptionResponse.generateSuccessResponse(responseObject,status); 
		}
		catch (Exception e) {
			//responseObject.setRsBody(e.getMessage());
			ExceptionResponse.generateErrorResponse(responseObject, "500");
			e.printStackTrace();
		}
		
		
		return responseObject;
		
		
	}
	
	@RequestMapping(value = "/auth/runcron", method = RequestMethod.POST)
	public @ResponseBody ResponseObject runcron(@RequestBody RequestObject requestObject) {
	
		ResponseObject responseObject = new ResponseObject();
		try {
		ObjectMapper objectMapper = new ObjectMapper();
//		CronDTO cronDTO = new CronDTO();
//		cronDTO = objectMapper.treeToValue(requestObject.getRqBody(),CronDTO.class);
//		System.err.println(cronDTO);
		String status = adminService.runCronJob();
                ExceptionResponse.generateSuccessResponse(responseObject,status); 
		}
		catch (Exception e) {
			//responseObject.setRsBody(e.getMessage());
			ExceptionResponse.generateErrorResponse(responseObject, "500");
			e.printStackTrace();
		}
		
		
		return responseObject;
		
		
	}
	
	
	@RequestMapping(value = "/auth/getbatchprogress", method = RequestMethod.POST)
	public @ResponseBody ResponseObject getBatchProgressController(@RequestBody RequestObject requestObject) {
	
		ResponseObject responseObject = new ResponseObject();
		try {
		ObjectMapper objectMapper = new ObjectMapper();
		BatchProgressDTO batchProgressDTO = new BatchProgressDTO();
		batchProgressDTO = objectMapper.treeToValue(requestObject.getRqBody(),BatchProgressDTO.class);
//		System.err.println(cronDTO);
//		System.out.println(batchProgressDTO.getUserId());
		BatchProgressDTO progress = adminService.getBatchProgress(batchProgressDTO);
		if(progress.getErrorMsg().isEmpty()) {
                ExceptionResponse.generateSuccessResponse(responseObject,progress); 
		}else {
			ExceptionResponse.generateErrorResponse(responseObject, progress.getErrorMsg());
		}
		}
		catch (Exception e) {
			//responseObject.setRsBody(e.getMessage());
			ExceptionResponse.generateErrorResponse(responseObject, "500");
			e.printStackTrace();
		}
		
		
		return responseObject;
		
		
	}
	
	

	@RequestMapping(value = "/auth/stopBatchNow", method = RequestMethod.POST)
	public @ResponseBody ResponseObject stopBatchController(@RequestBody RequestObject requestObject) {
	
		ResponseObject responseObject = new ResponseObject();
		try {
		ObjectMapper objectMapper = new ObjectMapper();
		BatchProgressDTO batchProgressDTO = new BatchProgressDTO();
		batchProgressDTO = objectMapper.treeToValue(requestObject.getRqBody(),BatchProgressDTO.class);
//		System.err.println(cronDTO);
//		System.out.println(batchProgressDTO.getUserId());
		String progress = adminService.stopCronJob(batchProgressDTO);
                ExceptionResponse.generateSuccessResponse(responseObject,progress); 
//			ExceptionResponse.generateErrorResponse(responseObject, progress.getErrorMsg());
		
		}
		catch (Exception e) {
			//responseObject.setRsBody(e.getMessage());
			ExceptionResponse.generateErrorResponse(responseObject, "500");
			e.printStackTrace();
		}
		
		
		return responseObject;
		
		
	}
	
	@RequestMapping(value = "/auth/getGmailInfo", method = RequestMethod.POST)
	public @ResponseBody ResponseObject getGmailInfo(@RequestBody RequestObject requestObject) {
	
		ResponseObject responseObject = new ResponseObject();
		try {
		ObjectMapper objectMapper = new ObjectMapper();
//		ArrayList<OrganizationPojo> organizationList= new ArrayList<OrganizationPojo>();
//		CronDTO cronDTO = new CronDTO();
//		cronDTO = objectMapper.treeToValue(requestObject.getRqBody(),CronDTO.class);
//		System.err.println(cronDTO);
		GmailConfig confInfo = new GmailConfig();
		confInfo = adminService.getGmailInfo();
		 // Add all elements to the ArrayList from an array.
//        Collections.addAll(organizationList, organizationPojo);
//        String  status= adminService.saveorg(organizationList);
//              String result= "";
//                if (status) {
//                	result= "101";
//                }
                ExceptionResponse.generateSuccessResponse(responseObject,confInfo); 
		}
		catch (Exception e) {
			//responseObject.setRsBody(e.getMessage());
			ExceptionResponse.generateErrorResponse(responseObject, "500");
			e.printStackTrace();
		}
		
		
		return responseObject;
		
		
	}
	
}
