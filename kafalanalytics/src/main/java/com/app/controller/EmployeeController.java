package com.app.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.omg.CORBA.portable.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.app.bo.OrganisationBO;
import com.app.bo.TeamClientInteractionBO;
import com.app.bo.DailyEmployeeEmailToneBO;
import com.app.bo.EmployeeBO;
import com.app.dto.ClientDataDTO;
import com.app.dto.EmployeePersonalDataDTO;
import com.app.mongo.repositories.EmployeeRepository;
import com.app.pojo.EmailPojo;
import com.app.pojo.EmployeeSearchPojo;
import com.app.pojo.FilterByCriteria;
import com.app.pojo.FilterResultPojo;
import com.app.service.obj.RequestObject;
import com.app.service.obj.ResponseObject;
import com.app.services.EmployeeService;
import com.app.utilities.AppUtils;
import com.app.utilities.ExceptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class EmployeeController {
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	EmployeeService employeeService;
	
	
	@PostMapping("auth/displayemployeepersonalscore")
	public @ResponseBody ResponseObject displayEmployeePersonalScore(@RequestBody RequestObject requestObject) {
		
		ResponseObject responseObject = new ResponseObject();
		EmployeePersonalDataDTO emplyDTO = null;
		
		try {
		
		ObjectMapper objectMapper = new ObjectMapper();
		EmployeePersonalDataDTO employeePersonalDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
				EmployeePersonalDataDTO.class);
		
		 emplyDTO = employeeService.getPersonalPerMailScore(employeePersonalDataDTO);
		
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		
		ExceptionResponse.generateSuccessResponse(responseObject,emplyDTO); 
		
		return responseObject;
		
	}
	
	
	@PostMapping("auth/dislaydashboard")
	public @ResponseBody ResponseObject displayDashBoard(@RequestBody RequestObject requestObject) {
		
		ResponseObject responseObject = new ResponseObject();
		EmployeePersonalDataDTO emplyDTO = null;
		
		try {
		
		ObjectMapper objectMapper = new ObjectMapper();
		EmployeePersonalDataDTO employeePersonalDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
				EmployeePersonalDataDTO.class);
		
		  emplyDTO = employeeService.getDashBoardData();
		
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		
		ExceptionResponse.generateSuccessResponse(responseObject,emplyDTO); 
		
		return responseObject;
		
	}
	
	@PostMapping("auth/dislaydashboard1")
	public @ResponseBody ResponseObject displayDashBoard1(@RequestBody RequestObject requestObject) {
		
		ResponseObject responseObject = new ResponseObject();
		EmployeePersonalDataDTO emplyDTO = null;
		
		try {
		
		ObjectMapper objectMapper = new ObjectMapper();
		EmployeePersonalDataDTO employeePersonalDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
				EmployeePersonalDataDTO.class);
		
		  emplyDTO = employeeService.getDashBoardData1(employeePersonalDataDTO);
		
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		
		ExceptionResponse.generateSuccessResponse(responseObject,emplyDTO); 
		
		return responseObject;
		
	}
	
	
	@PostMapping("auth/listCompany")
	public @ResponseBody ResponseObject listCompany(@RequestBody RequestObject requestObject) {
		
		ResponseObject responseObject = new ResponseObject();
		EmployeePersonalDataDTO emplyDTO = null;
		List<OrganisationBO> listCompnyBO = null;
		
		try {
		ObjectMapper objectMapper = new ObjectMapper();
		EmployeePersonalDataDTO employeePersonalDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
				EmployeePersonalDataDTO.class);
		
		listCompnyBO = employeeService.getListOfCompany();
		
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		
		ExceptionResponse.generateSuccessResponse(responseObject,listCompnyBO); 
		
		return responseObject;
		
	}
	
	
	// client or vendor list against their company
	
	@PostMapping("auth/listCompanyClient")
	public @ResponseBody ResponseObject listCompanyClient(@RequestBody RequestObject requestObject) {
		
		ResponseObject responseObject = new ResponseObject();
		EmployeePersonalDataDTO clientFinalDataDTO = null;
		
		
		try {
		ObjectMapper objectMapper = new ObjectMapper();
		ClientDataDTO clientDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
				ClientDataDTO.class);
		
		clientFinalDataDTO = employeeService.getClientDashBoard(clientDataDTO);
		
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		
		ExceptionResponse.generateSuccessResponse(responseObject,clientFinalDataDTO); 
		System.out.println(responseObject.getRsBody());
		return responseObject;
		
	}
	
	
	// employee hierachy
	
		@PostMapping("auth/listEmployeeImidiateReportee")
		public @ResponseBody ResponseObject listEmployeeReportee(@RequestBody RequestObject requestObject) {
			
			ResponseObject responseObject = new ResponseObject();
			//List<EmployeeBO> employeeDatas = null;
			EmployeePersonalDataDTO	employeePersonalDataDTO = null;
			
			
			try {
			ObjectMapper objectMapper = new ObjectMapper();
			ClientDataDTO clientDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
					ClientDataDTO.class);
			
			employeePersonalDataDTO = employeeService.getListOfEmployee(clientDataDTO);
			
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			ExceptionResponse.generateSuccessResponse(responseObject,employeePersonalDataDTO); 
			
			return responseObject;
			
		}
		
		
		// employee ITERATION HIERACHY
		
			@PostMapping("auth/listEmployeeHierachy")
			public @ResponseBody ResponseObject listEmployeeHierachy(@RequestBody RequestObject requestObject) {
				
				ResponseObject responseObject = new ResponseObject();
				EmployeePersonalDataDTO  employeeDatas = null;
				
				
				try {
				ObjectMapper objectMapper = new ObjectMapper();
				ClientDataDTO clientDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
						ClientDataDTO.class);
				
				employeeDatas = employeeService.getListOfEmployeeHierchy(clientDataDTO);
				
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
				
				
				ExceptionResponse.generateSuccessResponse(responseObject,employeeDatas); 
				
				return responseObject;
				
			}
	

			// employee designation HIERACHY
			
				@PostMapping("auth/searchByEmployeedesignationHierachy")
				public @ResponseBody ResponseObject listSearchEmployeeHierachy(@RequestBody RequestObject requestObject) {
					
					ResponseObject responseObject = new ResponseObject();
					//List<FilterByCriteria>   employeeDatas = null;
					EmployeePersonalDataDTO employeePersonalDataDTO = null;
					
					
					
					try {
					ObjectMapper objectMapper = new ObjectMapper();
					ClientDataDTO clientDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
							ClientDataDTO.class);
					
					employeePersonalDataDTO = employeeService.filterOnEmployeeDesignationCriteria(clientDataDTO);
					
					}catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					
					
					ExceptionResponse.generateSuccessResponse(responseObject,employeePersonalDataDTO); 
					
					return responseObject;
					
				}
				
				
				@PostMapping("auth/displayemployeeteamdashboard")
				public @ResponseBody ResponseObject displayEmployeeTeamDashBoard(@RequestBody RequestObject requestObject) {
					
					ResponseObject responseObject = new ResponseObject();
					EmployeePersonalDataDTO emplyDTO = null;
					
					try {
					
					ObjectMapper objectMapper = new ObjectMapper();
					EmployeePersonalDataDTO employeePersonalDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
							EmployeePersonalDataDTO.class);
					
					 emplyDTO = employeeService.getEmployeeTeamDashBoard(employeePersonalDataDTO);
					
					}catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					
					
					ExceptionResponse.generateSuccessResponse(responseObject,emplyDTO); 
					
					return responseObject;
					
				}		
				
			
				
				// employee search ByName
				
				@RequestMapping(value="auth/searchEmployeeByName",method=RequestMethod.GET)
				public @ResponseBody ResponseObject searchEmployeeByName(HttpServletRequest request) {
					
					ResponseObject responseObject = new ResponseObject();
					List<FilterByCriteria>  filterData = null;
					
					String searchData = request.getParameter("employeeName");
					String key = request.getParameter("key");
					try {
					/*ObjectMapper objectMapper = new ObjectMapper();
					EmployeePersonalDataDTO clientDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
							EmployeePersonalDataDTO.class);*/
					
					filterData = employeeService.filterOnEmployeeName(searchData,key);
					
					}catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					
					
					ExceptionResponse.generateSuccessResponse(responseObject,filterData); 
					
					return responseObject;
					
				}
				
				
				// employee search ByName
				
				@PostMapping("auth/getEmpTeamEmail")
				public @ResponseBody ResponseObject getEmpTeamEmail(@RequestBody RequestObject requestObject) {
					
					ResponseObject responseObject = new ResponseObject();
					EmployeePersonalDataDTO  employeeDatas = null;
					
					
					try {
					ObjectMapper objectMapper = new ObjectMapper();
					EmployeePersonalDataDTO clientDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
							EmployeePersonalDataDTO.class);
					
					employeeDatas = employeeService.getEachMemberMailOfTeam(clientDataDTO);
					
					}catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					
					
					ExceptionResponse.generateSuccessResponse(responseObject,employeeDatas); 
					
					return responseObject;
					
				}
				
				//get client emails
				
				@PostMapping("auth/getClientEmails")
				public @ResponseBody ResponseObject getClientEmails(@RequestBody RequestObject requestObject) {
					
					ResponseObject responseObject = new ResponseObject();
					EmployeePersonalDataDTO  employeeDatas = null;
					
					
					try {
					ObjectMapper objectMapper = new ObjectMapper();
					EmployeePersonalDataDTO clientDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
							EmployeePersonalDataDTO.class);
					
					employeeDatas = employeeService.getClientEmails(clientDataDTO);
					
					}catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					
					
					ExceptionResponse.generateSuccessResponse(responseObject,employeeDatas); 
					
					return responseObject;
					
				}
				
				@PostMapping("auth/getOrgEmails")
				public @ResponseBody ResponseObject getOrgEmails(@RequestBody RequestObject requestObject) {
					
					ResponseObject responseObject = new ResponseObject();
					EmployeePersonalDataDTO  employeeDatas = null;
					System.out.println(requestObject.getRqBody());
					
					try {
					ObjectMapper objectMapper = new ObjectMapper();
					EmployeePersonalDataDTO clientDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
							EmployeePersonalDataDTO.class);
					
					employeeDatas = employeeService.getOrgEmailsService(clientDataDTO);
					
					}catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					
					
					ExceptionResponse.generateSuccessResponse(responseObject,employeeDatas); 
					
					return responseObject;
					
				}
				
				
				
				// results against each search
				
				@PostMapping("auth/getEmpSearchResult")
				public @ResponseBody ResponseObject getEmployeeSearchresult(@RequestBody RequestObject requestObject) {
					
					ResponseObject responseObject = new ResponseObject();
					EmployeePersonalDataDTO  employeeDatas = null;
					
					
					try {
					ObjectMapper objectMapper = new ObjectMapper();
					ClientDataDTO clientDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
							ClientDataDTO.class);
					
					employeeDatas = employeeService.getfilterEmployeeResult(clientDataDTO);
					
					}catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					
					
					ExceptionResponse.generateSuccessResponse(responseObject,employeeDatas); 
					
					return responseObject;
					
				}
				
				
				// specific designation search 
				
		
				
				@PostMapping("auth/getEmpDesigResult")
				public @ResponseBody ResponseObject getEmployeeSpecificDesinationResult(@RequestBody RequestObject requestObject) {
					
					ResponseObject responseObject = new ResponseObject();
					EmployeePersonalDataDTO  employeeDatas = null;
					
					//String designationSearch = request.getParameter("designation");
					
					
					try {
					ObjectMapper objectMapper = new ObjectMapper();
					EmployeePersonalDataDTO clientDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
							EmployeePersonalDataDTO.class);
					
					employeeDatas = employeeService.getEmployeeSpecificResult(clientDataDTO);
					
					}catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					
					
					ExceptionResponse.generateSuccessResponse(responseObject,employeeDatas); 
					
					return responseObject;
					
				}
				
				
				// searchfor demo
				
				//result on specific designation search 
				
		
				
				@PostMapping("auth/getEmpDesig")
				public @ResponseBody ResponseObject getEmployeeSpecific(@RequestBody RequestObject requestObject) {
					
					ResponseObject responseObject = new ResponseObject();
					List<EmployeePersonalDataDTO>  employeeDatas = null;
					
					
					try {
						
					ObjectMapper objectMapper = new ObjectMapper();
					ClientDataDTO clientDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
							ClientDataDTO.class);
					
					employeeDatas = employeeService.searchByEmployeeName(clientDataDTO);
					
					}catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					
					
					ExceptionResponse.generateSuccessResponse(responseObject,employeeDatas); 
					
					return responseObject;
					
				}
				
				
				// Clients Filters on name and email,designation
				//value="auth/searchEmployeeByName",method=RequestMethod.GET
				@RequestMapping(value="auth/getClientFilterResult",method=RequestMethod.GET)
				public @ResponseBody ResponseObject getClientFilterResult(HttpServletRequest request ) {
					
					ResponseObject responseObject = new ResponseObject();
					List<FilterByCriteria>  filterData  = null;
					String key = request.getParameter("key");
					String filter = request.getParameter("clientFilter");
					
					try {
					/*ObjectMapper objectMapper = new ObjectMapper();
					EmployeePersonalDataDTO clientDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
							EmployeePersonalDataDTO.class);*/
					
					filterData = employeeService.filterOnClientName(key,filter);
					
					}catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					
					
					ExceptionResponse.generateSuccessResponse(responseObject,filterData); 
					
					return responseObject;
					
				}
			
				
				// Clients Filters on Company Clientname
			
				@RequestMapping(value="auth/getCompanyClientResult",method=RequestMethod.GET)
				public @ResponseBody ResponseObject getCompanyClientResult(HttpServletRequest request ) {
					
					ResponseObject responseObject = new ResponseObject();
					List<FilterByCriteria>  filterData  = null;
					String key = request.getParameter("key");
					String filter = request.getParameter("clientName");
					String cmpId = request.getParameter("companyId");
					
					try {
					/*ObjectMapper objectMapper = new ObjectMapper();
					EmployeePersonalDataDTO clientDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
							EmployeePersonalDataDTO.class);*/
					
					filterData = employeeService.filterOnSubClientName(key,filter,cmpId);
					
					}catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					
					
					ExceptionResponse.generateSuccessResponse(responseObject,filterData); 
					
					return responseObject;
					
				}
				
				
				
				// name typeahead Filters on subEmployeename
				
				@RequestMapping(value="auth/getNameUnderSpecificEmp",method=RequestMethod.GET)
				public @ResponseBody ResponseObject getEmployeeNameUnderSpecificEmp(HttpServletRequest request ) {
					
					ResponseObject responseObject = new ResponseObject();
					List<FilterByCriteria>  filterData  = null;
					String key = request.getParameter("empId");
					String filter = request.getParameter("employeeName");
					//String cmpId = request.getParameter("companyId");
					
					try {
					/*ObjectMapper objectMapper = new ObjectMapper();
					EmployeePersonalDataDTO clientDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
							EmployeePersonalDataDTO.class);*/
					
					filterData = employeeService.filterOnSubEmployeeName(filter,key);
					
					}catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					
					
					ExceptionResponse.generateSuccessResponse(responseObject,filterData); 
					
					return responseObject;
					
				}
				
				// clients filter results
				
				@PostMapping("auth/getClientSearchData")
				public @ResponseBody ResponseObject getClientSearchData(@RequestBody RequestObject requestObject) {
					
					ResponseObject responseObject = new ResponseObject();
					EmployeePersonalDataDTO  employeeDatas = null;
					
					
					try {
					ObjectMapper objectMapper = new ObjectMapper();
					EmployeePersonalDataDTO clientDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
							EmployeePersonalDataDTO.class);
					
					employeeDatas = employeeService.getClientSearchResult(clientDataDTO);
					
					}catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					
					
					ExceptionResponse.generateSuccessResponse(responseObject,employeeDatas); 
					
					return responseObject;
					
				}
				
				
	
				//getSortedEmployeeHierchy
				
				
				@PostMapping("auth/getSortEmployee")
				public @ResponseBody ResponseObject getSortedData(@RequestBody RequestObject requestObject) {
					
					ResponseObject responseObject = new ResponseObject();
					EmployeePersonalDataDTO employeeDatas = null;
					
					
					try {
					ObjectMapper objectMapper = new ObjectMapper();
					ClientDataDTO clientDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
							ClientDataDTO.class);
					
					employeeDatas = employeeService.getSortedEmployeeHierchy(clientDataDTO);
					
					}catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					
					
					ExceptionResponse.generateSuccessResponse(responseObject,employeeDatas); 
					
					return responseObject;
					
				}
				
				
				// client data with Sorting on Tone
				
				@PostMapping("auth/listSortedClient")
				public @ResponseBody ResponseObject listSortedClient(@RequestBody RequestObject requestObject) {
					
					ResponseObject responseObject = new ResponseObject();
					EmployeePersonalDataDTO clientFinalDataDTO = null;
					
					
					try {
					ObjectMapper objectMapper = new ObjectMapper();
					ClientDataDTO clientDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
							ClientDataDTO.class);
					
					clientFinalDataDTO = employeeService.getClientDashBoard(clientDataDTO);
					
					}catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					
					
					ExceptionResponse.generateSuccessResponse(responseObject,clientFinalDataDTO); 
					
					return responseObject;
					
				}
				
				
				//list email On Tone Click getEmailOnToneClick
				
				

				@PostMapping("auth/listSortedEmailOnToneClk")
				public @ResponseBody ResponseObject listSortedEmailBasedOnTone(@RequestBody RequestObject requestObject) {
					
					ResponseObject responseObject = new ResponseObject();
					EmployeePersonalDataDTO filterResultPojos = null;
					
					
					try {
					ObjectMapper objectMapper = new ObjectMapper();
					ClientDataDTO clientDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
							ClientDataDTO.class);
					
					filterResultPojos = employeeService.getEmailOnToneClick(clientDataDTO);
					
					}catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					
					
					ExceptionResponse.generateSuccessResponse(responseObject,filterResultPojos); 
					
					return responseObject;
					
				}
				
				@PostMapping("auth/listSortedEmailOnToneClkClient")
				public @ResponseBody ResponseObject listSortedEmailBasedOnToneClient(@RequestBody RequestObject requestObject) {
					
					ResponseObject responseObject = new ResponseObject();
					EmployeePersonalDataDTO filterResultPojos = null;
					
					
					try {
					ObjectMapper objectMapper = new ObjectMapper();
					ClientDataDTO clientDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
							ClientDataDTO.class);
					
					filterResultPojos = employeeService.getEmailOnToneClickClient(clientDataDTO);
					
					}catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					
					
					ExceptionResponse.generateSuccessResponse(responseObject,filterResultPojos); 
					
					return responseObject;
					
				}
				
				@PostMapping("auth/listSortedEmailOnToneClkOrg")
				public @ResponseBody ResponseObject listSortedEmailBasedOnToneOrg(@RequestBody RequestObject requestObject) {
					
					ResponseObject responseObject = new ResponseObject();
					EmployeePersonalDataDTO filterResultPojos = null;
					
					
					try {
					ObjectMapper objectMapper = new ObjectMapper();
					ClientDataDTO clientDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
							ClientDataDTO.class);
					
					filterResultPojos = employeeService.getEmailOnToneClickOrg(clientDataDTO);
					
					}catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					
					
					ExceptionResponse.generateSuccessResponse(responseObject,filterResultPojos); 
					
					return responseObject;
					
				}
				
				
	// personal screen filter data controller
				
				
				@RequestMapping(value="auth/getEmailSubject",method=RequestMethod.GET)
				public @ResponseBody ResponseObject getPersonalEmailSubjectTypeAhead(HttpServletRequest request ) {
					
					ResponseObject responseObject = new ResponseObject();
					List<EmailPojo>  filterData  = null;
					
					String filter = request.getParameter("emailSubject");
					String empId = request.getParameter("employeeId");

					try {
					/*ObjectMapper objectMapper = new ObjectMapper();
					EmployeePersonalDataDTO clientDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
							EmployeePersonalDataDTO.class);*/
					
					filterData = employeeService.typeOnMailHeading(filter,empId);
					
					}catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					
					
					ExceptionResponse.generateSuccessResponse(responseObject,filterData); 
					
					return responseObject;
					
				}
				
				@RequestMapping(value="auth/getReportTo",method=RequestMethod.GET)
				public @ResponseBody ResponseObject getReportToSuggestion(HttpServletRequest request ) {
					
					ResponseObject responseObject = new ResponseObject();
					List<EmployeeSearchPojo>  filterData  = null;
					
					String filter = request.getParameter("reportTo");
//					String empId = request.getParameter("employeeId");

					try {
					/*ObjectMapper objectMapper = new ObjectMapper();
					EmployeePersonalDataDTO clientDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
							EmployeePersonalDataDTO.class);*/
					
					filterData = employeeService.typeOnReportIdSuggestion(filter);
					
					}catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					
					
					ExceptionResponse.generateSuccessResponse(responseObject,filterData); 
					
					return responseObject;
					
				}
				
				
			// typeAhead On OrganisationName typeAheadWithOrgName
				
				@RequestMapping(value="auth/getOrgName",method=RequestMethod.GET)
				public @ResponseBody ResponseObject getOrgName(HttpServletRequest request ) {
					
					ResponseObject responseObject = new ResponseObject();
					List<OrganisationBO>  filterData  = null;
					
					String filter = request.getParameter("companyName");
					
					try {
					/*ObjectMapper objectMapper = new ObjectMapper();
					EmployeePersonalDataDTO clientDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
							EmployeePersonalDataDTO.class);*/
					
					filterData = employeeService.typeAheadWithOrgName(filter);
					
					}catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					
					
					ExceptionResponse.generateSuccessResponse(responseObject,filterData); 
					
					return responseObject;
					
				}
				
				
		
				// filter on email Header filterOnMailHeading
				
				@PostMapping("auth/getMailFilter")
				public @ResponseBody ResponseObject getMailFilter(@RequestBody RequestObject requestObject) {
					
					ResponseObject responseObject = new ResponseObject();
					EmployeePersonalDataDTO  employeeDatas = null;
					
					
					try {
					ObjectMapper objectMapper = new ObjectMapper();
					EmployeePersonalDataDTO clientDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
							EmployeePersonalDataDTO.class);
					
					employeeDatas = employeeService.filterOnMailHeading(clientDataDTO);
					
					}catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					
					
					ExceptionResponse.generateSuccessResponse(responseObject,employeeDatas); 
					
					return responseObject;
					
				}
				
		// mailfilterByClientName filterOnClientNameInMail		
				
				
				@PostMapping("auth/getMailFilterByClientName")
				public @ResponseBody ResponseObject getMailFilterByClientName(@RequestBody RequestObject requestObject) {
					
					ResponseObject responseObject = new ResponseObject();
					List<EmailPojo>  employeeDatas = null;
					
					
					try {
					ObjectMapper objectMapper = new ObjectMapper();
					EmployeePersonalDataDTO clientDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
							EmployeePersonalDataDTO.class);
					
					employeeDatas = employeeService.filterOnClientNameInMail(clientDataDTO);
					
					}catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					
					
					ExceptionResponse.generateSuccessResponse(responseObject,employeeDatas); 
					
					return responseObject;
					
				}	
				
		// search on DateFilter in personal screen filterMailOnDate		filterTeamMailOnDate
		
				@PostMapping("auth/getMailFilterByDate")
				public @ResponseBody ResponseObject getMailFilterByDate(@RequestBody RequestObject requestObject) {
					
					ResponseObject responseObject = new ResponseObject();
					List<EmailPojo>  employeeDatas = null;
					
					
					try {
					ObjectMapper objectMapper = new ObjectMapper();
					EmployeePersonalDataDTO clientDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
							EmployeePersonalDataDTO.class);
					
					employeeDatas = employeeService.filterMailOnDate(clientDataDTO);
					
					}catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					
					
					ExceptionResponse.generateSuccessResponse(responseObject,employeeDatas); 
					
					return responseObject;
					
				}
				
				
				// search on DateFilter in personal screen filterMailOnTime		
				
				@PostMapping("auth/getMailFilterByTime")
				public @ResponseBody ResponseObject getMailFilterByTime(@RequestBody RequestObject requestObject) {
					
					ResponseObject responseObject = new ResponseObject();
					List<EmailPojo>  employeeDatas = null;
					
					
					try {
					ObjectMapper objectMapper = new ObjectMapper();
					EmployeePersonalDataDTO clientDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
							EmployeePersonalDataDTO.class);
					
					employeeDatas = employeeService.filterMailOnTime(clientDataDTO);
					
					}catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					
					
					ExceptionResponse.generateSuccessResponse(responseObject,employeeDatas); 
					
					return responseObject;
					
				}
				
		// searching ON ALL SENTIMENTS TONE IN PERSONAL SCORES filterMailOnTone
				
				@PostMapping("auth/getMailFilterByTone")
				public @ResponseBody ResponseObject getMailFilterByTone(@RequestBody RequestObject requestObject) {
					
					ResponseObject responseObject = new ResponseObject();
					List<EmailPojo>  employeeDatas = null;
					
					
					try {
					ObjectMapper objectMapper = new ObjectMapper();
					ClientDataDTO clientDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
							ClientDataDTO.class);
					
					employeeDatas = employeeService.filterMailOnTone(clientDataDTO);
					
					}catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					
					
					ExceptionResponse.generateSuccessResponse(responseObject,employeeDatas); 
					
					return responseObject;
					
				}		
		
		// sort on personal DashBoard Screen sortMailOnPersonalScreen
				
				@PostMapping("auth/sortMailByTone")
				public @ResponseBody ResponseObject sortMailByTone(@RequestBody RequestObject requestObject) {
					
					ResponseObject responseObject = new ResponseObject();
					List<EmailPojo>  employeeDatas = null;
					
					
					try {
					ObjectMapper objectMapper = new ObjectMapper();
					ClientDataDTO clientDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
							ClientDataDTO.class);
					
					employeeDatas = employeeService.sortMailOnPersonalScreen(clientDataDTO);
					
					}catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					
					
					ExceptionResponse.generateSuccessResponse(responseObject,employeeDatas); 
					
					return responseObject;
					
				}			
				
				
				
				
				// typeAhead On TeamDashBoard mailHeading
					
					@RequestMapping(value="auth/getTeamMail",method=RequestMethod.GET)
					public @ResponseBody ResponseObject getTeamMailByHeading(HttpServletRequest request ) {
						
						ResponseObject responseObject = new ResponseObject();
						List<EmailPojo>  filterData  = null;
						
						String filter = request.getParameter("employeeId");
						String mailHeader = request.getParameter("emailSubject");
						
						try {
						/*ObjectMapper objectMapper = new ObjectMapper();
						EmployeePersonalDataDTO clientDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
								EmployeePersonalDataDTO.class);*/
						
						filterData = employeeService.typeOnTeamMailHeading(mailHeader,filter);
						
						}catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						
						
						
						ExceptionResponse.generateSuccessResponse(responseObject,filterData); 
						
						return responseObject;
						
					}	
					
					// TypeAhead Results On Basis of MailHeading
					
					@PostMapping("auth/resultByMailSubject")
					public @ResponseBody ResponseObject resultByMailSubject(@RequestBody RequestObject requestObject) {
						
						ResponseObject responseObject = new ResponseObject();
						List<EmailPojo>  employeeDatas = null;
						
						
						try {
						ObjectMapper objectMapper = new ObjectMapper();
						EmployeePersonalDataDTO clientDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
								EmployeePersonalDataDTO.class);
						
						employeeDatas = employeeService.searchOnTeamMailHeading(clientDataDTO);
						
						}catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						
						
						
						ExceptionResponse.generateSuccessResponse(responseObject,employeeDatas); 
						
						return responseObject;
						
					}	
					
					
					//search on mailTone
					
					@PostMapping("auth/getMailFilterByTeamTone")
					public @ResponseBody ResponseObject getMailFilterByTeamTone(@RequestBody RequestObject requestObject) {
						
						ResponseObject responseObject = new ResponseObject();
						List<EmailPojo>  employeeDatas = null;
						
						
						try {
						ObjectMapper objectMapper = new ObjectMapper();
						ClientDataDTO clientDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
								ClientDataDTO.class);
						
						employeeDatas = employeeService.searchMailOnTone(clientDataDTO);
						
						}catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						
						
						
						ExceptionResponse.generateSuccessResponse(responseObject,employeeDatas); 
						
						return responseObject;
						
					}		
			
			// sort on Team DashBoard Screen sortMailOnPersonalScreen
					
					@PostMapping("auth/sortMailByTeamTone")
					public @ResponseBody ResponseObject sortMailByTeamTone(@RequestBody RequestObject requestObject) {
						
						ResponseObject responseObject = new ResponseObject();
						List<EmailPojo>  employeeDatas = null;
						
						
						try {
						ObjectMapper objectMapper = new ObjectMapper();
						ClientDataDTO clientDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
								ClientDataDTO.class);
						
						employeeDatas = employeeService.sortMailOnTeamScreen(clientDataDTO);
						
						}catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						
						
						
						ExceptionResponse.generateSuccessResponse(responseObject,employeeDatas); 
						
						return responseObject;
						
					}				  
					
					
					
					// employee export data to excel
					
					@PostMapping("auth/listEmployeeToExcel")
					public @ResponseBody ResponseObject listEmployeeToExcel(@RequestBody RequestObject requestObject) {
						
						ResponseObject responseObject = new ResponseObject();
						String  filePath = null;
						
						
						try {
						ObjectMapper objectMapper = new ObjectMapper();
						ClientDataDTO clientDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
								ClientDataDTO.class);
						
						filePath = employeeService.getDownloadPath(clientDataDTO);
						
						}catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						
						
						
						ExceptionResponse.generateSuccessResponse(responseObject,filePath); 
						
						return responseObject;
						
					}
						
					
			// download client data request
					
					
					@PostMapping("auth/listClientToExcel")
					public @ResponseBody ResponseObject listClientToExcel(@RequestBody RequestObject requestObject) {
						
						ResponseObject responseObject = new ResponseObject();
						String  filePath = null;
						
						
						try {
						ObjectMapper objectMapper = new ObjectMapper();
						ClientDataDTO clientDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
								ClientDataDTO.class);
						
						filePath = employeeService.getDownloadClientPath(clientDataDTO);
						
						}catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						
						
						
						ExceptionResponse.generateSuccessResponse(responseObject,filePath); 
						
						return responseObject;
						
					}
						
					
					// personal dashboard data
					
					@PostMapping("auth/listPersonalToExcel")
					public @ResponseBody ResponseObject listPersonalToExcel(@RequestBody RequestObject requestObject) {
						
						ResponseObject responseObject = new ResponseObject();
						String  filePath = null;
						
						
						try {
						ObjectMapper objectMapper = new ObjectMapper();
						EmployeePersonalDataDTO clientDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
								EmployeePersonalDataDTO.class);
						
						filePath = employeeService.getDownloadPersonalPath(clientDataDTO);
						
						}catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						
						
						
						ExceptionResponse.generateSuccessResponse(responseObject,filePath); 
						
						return responseObject;
						
					}
					
					
					// Team dashboard data
					
					@PostMapping("auth/listTeamToExcel")
					public @ResponseBody ResponseObject listTeamToExcel(@RequestBody RequestObject requestObject) {
						
						ResponseObject responseObject = new ResponseObject();
						String  filePath = null;
						
						
						try {
						ObjectMapper objectMapper = new ObjectMapper();
						EmployeePersonalDataDTO clientDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
								EmployeePersonalDataDTO.class);
						
						filePath = employeeService.getDownloadTeamPath(clientDataDTO);
						
						}catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						
						
						
						ExceptionResponse.generateSuccessResponse(responseObject,filePath); 
						
						return responseObject;
						
					}
					
					@PostMapping("auth/listPersonalTeamToExcel")
					public @ResponseBody ResponseObject listPersonalTeamToExcel(@RequestBody RequestObject requestObject) {
						
						ResponseObject responseObject = new ResponseObject();
						String  filePath = null;
						
						
						try {
						ObjectMapper objectMapper = new ObjectMapper();
						EmployeePersonalDataDTO clientDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
								EmployeePersonalDataDTO.class);
						
						filePath = employeeService.getDownloadPersonalTeamPath(clientDataDTO);
						
						}catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						
						
						
						ExceptionResponse.generateSuccessResponse(responseObject,filePath); 
						
						return responseObject;
						
					}
					
					

					
					// search Team on DateFilter in personal screen filterMailOnDate		
					
					@PostMapping("auth/getTeamMailFilterByDate")
					public @ResponseBody ResponseObject getTeamMailFilterByDate(@RequestBody RequestObject requestObject) {
						
						ResponseObject responseObject = new ResponseObject();
						List<EmailPojo>  employeeDatas = null;
						
						
						try {
						ObjectMapper objectMapper = new ObjectMapper();
						EmployeePersonalDataDTO clientDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
								EmployeePersonalDataDTO.class);
						
						employeeDatas = employeeService.filterTeamMailOnDate(clientDataDTO);
						
						}catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						
						
						
						ExceptionResponse.generateSuccessResponse(responseObject,employeeDatas); 
						
						return responseObject;
						
					}
					
					
					// search Team on DateFilter in personal screen filterMailOnTime		
					
					@PostMapping("auth/getTeamMailFilterByTime")
					public @ResponseBody ResponseObject getTeamMailFilterByTime(@RequestBody RequestObject requestObject) {
						
						ResponseObject responseObject = new ResponseObject();
						List<EmailPojo>  employeeDatas = null;
						
						
						try {
						ObjectMapper objectMapper = new ObjectMapper();
						EmployeePersonalDataDTO clientDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
								EmployeePersonalDataDTO.class);
						
						employeeDatas = employeeService.filterTeamMailOnTime(clientDataDTO);
						
						}catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						
						
						
						ExceptionResponse.generateSuccessResponse(responseObject,employeeDatas); 
						
						return responseObject;
						
					}	
					
					
					
			
			@PostMapping("auth/getTeamMailFilterByEmpName")
			public @ResponseBody ResponseObject getTeamMailFilterByEmpName(@RequestBody RequestObject requestObject) {
				
				ResponseObject responseObject = new ResponseObject();
				List<EmailPojo>  employeeDatas = null;
				
				
				try {
				ObjectMapper objectMapper = new ObjectMapper();
				EmployeePersonalDataDTO clientDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),
						EmployeePersonalDataDTO.class);
				
				employeeDatas = employeeService.searchOnTeamEmployeeName(clientDataDTO);
				
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
				
				
				ExceptionResponse.generateSuccessResponse(responseObject,employeeDatas); 
				
				return responseObject;
				
			}	
			
						// changes By aashish
						@RequestMapping(value = "/auth/saveemp", method = RequestMethod.POST)
						public @ResponseBody ResponseObject saveemp(@RequestBody RequestObject requestObject) {
			
							ResponseObject responseObject = new ResponseObject();
							try {
							ObjectMapper objectMapper = new ObjectMapper();
							ArrayList<EmployeePersonalDataDTO> employeeList= new ArrayList<EmployeePersonalDataDTO>();
							EmployeePersonalDataDTO[] employeePersonalDataDTO = objectMapper.treeToValue(requestObject.getRqBody(),EmployeePersonalDataDTO[].class);
			// Add all elements to the ArrayList from an array.
			Collections.addAll(employeeList, employeePersonalDataDTO);
			employeeList=ReadExcelData.checknullemployee(employeeList);
			String status = employeeService.saveemp(employeeList);
			ExceptionResponse.generateSuccessResponse(responseObject, status);

		}
							catch (Exception e) {
								ExceptionResponse.generateErrorResponse(responseObject, "error");
								e.printStackTrace();
							}
							return responseObject;
						}
						
						@RequestMapping(method = RequestMethod.POST, value = "/auth/savemultiplefileemp")
						//	@ResponseBody
							private @ResponseBody ResponseObject runImportRecordsJob(@RequestParam("file") MultipartFile file){	
								ResponseObject responseObject = new ResponseObject();
								try {
									String[] provider_header = AppUtils.EMPLOYEE_HEADERS;
									File convertedfile=ReadExcelData.fileconverter(file);
									ArrayList<String> a1=ReadExcelData.processfile(convertedfile,provider_header);
									ArrayList<EmployeePersonalDataDTO> employeeList=ReadExcelData.empmapper(a1,provider_header);
									employeeList=ReadExcelData.checknullemployeemultiple(employeeList);
									String status= employeeService.saveemp(employeeList);
									ExceptionResponse.generateSuccessResponse(responseObject,status); 
									
								} catch (Exception e) {
									String str = e.getMessage();
									System.out.println("Exception is--" + str);
									//ExceptionResponse.generateErrorResponse(responseObject, str);
									ExceptionResponse.generateSuccessResponse(responseObject,str); 
									e.printStackTrace();
								}
								return responseObject;
								
							}
						
						// end of changes by aashish
					
								
}
