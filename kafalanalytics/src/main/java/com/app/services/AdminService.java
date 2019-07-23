package com.app.services;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.Collections;
import java.util.regex.Pattern;

import org.bson.BSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.context.SecurityContextHolder;

import com.app.bo.BatchProgressBO;
import com.app.bo.ClientBO;
import com.app.bo.EmailConfigurationBO;
import com.app.bo.EmployeeBO;
import com.app.bo.EmployeeRoleBO;
import com.app.bo.OrganisationBO;
import com.app.dto.BatchProgressDTO;
import com.app.dto.ClientSearchDTO;
import com.app.dto.CronDTO;
import com.app.dto.EmailConfigurationDTO;
import com.app.dto.EmployeeSearchDTO;
import com.app.mongo.repositories.BatchProgressRepository;
import com.app.mongo.repositories.ClientRepository;
import com.app.mongo.repositories.CompanyRepository;
import com.app.mongo.repositories.EmailConfigurationRepository;
import com.app.mongo.repositories.EmployeeRepository;
import com.app.mongo.repositories.EmployeeRoleRepository;
import com.app.pojo.AddressPojo;
import com.app.pojo.ClientSearchPojo;
import com.app.pojo.ContactPersonPojo;
import com.app.pojo.EmployeeSearchPojo;
import com.app.pojo.FilterByCriteria;
import com.app.pojo.FilterPojo;
import com.app.pojo.GmailConfig;
import com.app.pojo.OrganizationPojo;
import com.app.security.UserAuthCredentials;
import com.app.utilities.EnumeratedTypes;
import com.app.utilities.StreamGobbler;
import com.mongodb.BasicDBObjectBuilder;

@Component
public class AdminService {
	@Autowired
	CompanyRepository companyRepository;

	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	EmailConfigurationRepository emailConfigurationRepository;
	
	@Autowired
	EmployeeRoleRepository employeeRoleRepository;
	
	@Autowired
	ClientRepository clientRepository;
	
	@Autowired
	BatchProgressRepository batchProgressRepository;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	public String saveorg(ArrayList<OrganizationPojo> organizationList) {
		
		if(organizationList.isEmpty())
		{
			String result="Empty File, Please try again.";
			return result;
		}

		OrganisationBO organisationBO = null;
		List<OrganisationBO> organisationBOlist = new ArrayList<OrganisationBO> ();
		List<OrganisationBO> DuplicateorganisationBOlist = new ArrayList<OrganisationBO> ();
		ContactPersonPojo contactPersonPojo=  new ContactPersonPojo();
		AddressPojo addressPojo= new AddressPojo();
		for (OrganizationPojo organizationPojo :organizationList) {
			organisationBO = new OrganisationBO();
			organisationBO.setCompanyName(organizationPojo.getOrganizationName());
			organisationBO.setCompanyEmailDomain(organizationPojo.getDomainName());
			organisationBO.setContactPersonDetails(contactPersonPojo);
			organisationBO.setCompanyLocation(addressPojo);
			organisationBO.setType("Client");
			organisationBOlist.add(organisationBO);
		}
		List<OrganisationBO> organisationBOlisttotal=companyRepository.findAll();
		
		for (OrganisationBO a1 :organisationBOlisttotal) {  // already existed record
			for (OrganisationBO a2 :organisationBOlist) {           // list to be inserted
				try {
				if(a1.getCompanyEmailDomain().equals(a2.getCompanyEmailDomain()))
					DuplicateorganisationBOlist.add(a2);
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		organisationBOlist.removeAll(DuplicateorganisationBOlist);
		for(OrganisationBO ob: organisationBOlist) {
			companyRepository.save(ob);
		}
//		String result="Saved:"+organisationBOlist.size()+"  Duplicates:"+DuplicateorganisationBOlist.size();
		
		String result= "Client data upload is unsuccessful. Please try again";
		
		if(organisationBOlist.size() != 0 && DuplicateorganisationBOlist.size() != 0) {
			result="Records are saved successfully - "+organisationBOlist.size()+"  Duplicate records - "+DuplicateorganisationBOlist.size();
		}else if(DuplicateorganisationBOlist.size()  == organizationList.size()) {
			result = "Client data upload is unsuccessful. Duplicate records "+DuplicateorganisationBOlist.size()+" Please try again";
		}else if(DuplicateorganisationBOlist.size() == 0) {
			result = "Client data upload is successful";
		}
		
		
				return result;

	}

		
	public EmployeeSearchDTO findEmp(ArrayList<EmployeeSearchPojo> employee) {
		
		EmployeeSearchDTO employeeData = new EmployeeSearchDTO();
		
		
		
		Query query = new Query();
		
		if(!employee.get(0).getArea().equals(""))
			query.addCriteria(Criteria.where("area").regex(Pattern.compile(employee.get(0).getArea(), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)));
		if(!employee.get(0).getChannel().equals("") )
			query.addCriteria(Criteria.where("channel").regex(Pattern.compile(employee.get(0).getChannel(), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)));
		if(!employee.get(0).getDepartment().equals(""))
			query.addCriteria(Criteria.where("department").regex(Pattern.compile(employee.get(0).getDepartment(), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)));
		if(!employee.get(0).getDesignation().equals(""))
			query.addCriteria(Criteria.where("designation").regex(Pattern.compile(employee.get(0).getDesignation(), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)));
		if(!employee.get(0).getFromDate().equals(""))
			query.addCriteria(Criteria.where("fromDate").is(employee.get(0).getFromDate()));
		if(!employee.get(0).getHorizontal().equals(""))
			query.addCriteria(Criteria.where("horizontal").is(employee.get(0).getHorizontal()));
		if(!employee.get(0).getRegion().equals(""))
			query.addCriteria(Criteria.where("region").is(employee.get(0).getRegion()));
		if(!employee.get(0).getReportToId().equals(""))
			query.addCriteria(Criteria.where("reportToId").is(employee.get(0).getReportToId()));
		if(!employee.get(0).getStatus().equals(""))
			query.addCriteria(Criteria.where("status").is(employee.get(0).getStatus()));
		if(!employee.get(0).getTerritory().equals(""))
			query.addCriteria(Criteria.where("territory").regex(Pattern.compile(employee.get(0).getTerritory(), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)));
		if(!employee.get(0).getToDate().equals(""))
			query.addCriteria(Criteria.where("toDate").is(employee.get(0).getToDate()));
		if(!employee.get(0).getVertical().equals(""))
			query.addCriteria(Criteria.where("vertical").is(employee.get(0).getVertical()));
		
		Query query2 = new Query();
		
		if(!employee.get(0).getEmployeeName().equals(""))
			query2.addCriteria(Criteria.where("employeeName").regex(Pattern.compile(employee.get(0).getEmployeeName(), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)));
		if(!employee.get(0).getEmailId().equals(""))
			query2.addCriteria(Criteria.where("emailId").is(employee.get(0).getEmailId()));
			
		
//		LookupOperation lookupOp = LookupOperation.newLookup()
//				.from("Employee")
//				.localField("employeeIdFK")
//				.foreignField("employeeId")
//				.as("EmployeeId");
		
		
//		Aggregation aggregation = Aggregation.newAggregation(
//				Aggregation.lookup("Employee", "employeeIdFK", "employeeId", "employeeId"),
//				Aggregation.match(Criteria.where("employeeName").is(employee)));
//		
//		AggregationResults<EmployeeSearchPojo> employees =  mongoTemplate.aggregate(aggregation, EmployeeRoleBO.class, EmployeeSearchPojo.class);
		
		List<EmployeeRoleBO> employeeRole = mongoTemplate.find(query, EmployeeRoleBO.class);
		
		List<EmployeeBO> employeeBOs =  mongoTemplate.find(query2, EmployeeBO.class);
		
		if( employeeBOs == null && employeeRole != null) {
			List<EmployeeSearchPojo> empSearch = new ArrayList<EmployeeSearchPojo>();
			BeanUtils.copyProperties(employeeRole, empSearch);
			employeeData.setEmployeeData(empSearch);
		}else if(employeeBOs != null && employeeRole == null) {
			List<EmployeeSearchPojo> empSearch = new ArrayList<EmployeeSearchPojo>();
			BeanUtils.copyProperties(employeeBOs, empSearch);
			employeeData.setEmployeeData(empSearch);
		}else {
			List<EmployeeSearchPojo> empSearch = new ArrayList<EmployeeSearchPojo>();
			for(EmployeeRoleBO empRole: employeeRole) {
				for(EmployeeBO emp: employeeBOs) {
					if(empRole.getEmployeeIdFK().equalsIgnoreCase(emp.getEmployeeId())) {
						EmployeeSearchPojo empCp = new EmployeeSearchPojo(); 
						BeanUtils.copyProperties(empRole, empCp);
						if(empCp.getReportToId() != null) {
							EmployeeBO reportTo = employeeRepository.findByEmployeeId(empCp.getReportToId());
							if(reportTo != null)
								empCp.setReportToName(reportTo.getEmployeeName());
						}
						empCp.setEmployeeId(empRole.getEmployeeIdFK());
						empCp.setEmailId(emp.getEmailId());
						empCp.setEmployeeName(emp.getEmployeeName());
						empCp.setAreaToggle(false);
						empCp.setDesignationToggle(false);
						empCp.setReportToIdToggle(false);
						empCp.setStatusToggle(false);
						empCp.setChecked(false);
						empCp.setEditchecked(false);
						empSearch.add(empCp);
					}
				}
			}
			employeeData.setEmployeeData(empSearch);
			
			
		}
		
		return employeeData;
		
	}
	
	public void delEmp(ArrayList<EmployeeSearchPojo> employeeList) {
		
		for(EmployeeSearchPojo employee: employeeList) {
			Query query = new Query();
			query.addCriteria(Criteria.where("employeeIdFK").is(employee.getEmployeeId()));
			mongoTemplate.findAllAndRemove(query, EmployeeRoleBO.class);
			
			Query query2 = new Query();
			query2.addCriteria(Criteria.where("employeeId").is(employee.getEmployeeId()));
			mongoTemplate.findAndRemove(query2, EmployeeBO.class);
					
		}
		
		
		
	}
	
	public boolean saveEmpRoleBSON(EmployeeRoleBO employeeRoleBO) {
		try {
			
			BSONObject employeeRoleBsonObj = BasicDBObjectBuilder.start()
					.add("designation", employeeRoleBO.getDesignation())
					.add("department", employeeRoleBO.getDepartment())
					.add("teamName", employeeRoleBO.getTeamName())
					.add("employeeIdFK", employeeRoleBO.getEmployeeIdFK())
					.add("horizontal", employeeRoleBO.getHorizontal())
					.add("vertical", employeeRoleBO.getVertical())
					.add("region", employeeRoleBO.getRegion())
					.add("subRegion", employeeRoleBO.getSubRegion())
					.add("area", employeeRoleBO.getArea())
					.add("subArea",employeeRoleBO.getSubArea())
					.add("channel",employeeRoleBO.getChannel())
					.add("subChannel", employeeRoleBO.getSubChannel())
					.add("territory", employeeRoleBO.getTerritory())
					.add("subTerritory", employeeRoleBO.getSubTerritory())
					.add("fromDate", employeeRoleBO.getFromDate())
					.add("toDate", employeeRoleBO.getToDate())
					.add("status", employeeRoleBO.getStatus())
					.add("teamSize", employeeRoleBO.getTeamSize())
					.add("consolidatedTone", employeeRoleBO.getConsolidatedTone())
					.add("employeeHierarchy", employeeRoleBO.getEmployeeHierarchy()).get();
			
			if(employeeRoleBO.getReportToId() != null && !employeeRoleBO.getReportToId().isEmpty() ) {
				employeeRoleBsonObj.put("reportToId", employeeRoleBO.getReportToId());
			}else {
				employeeRoleBsonObj.put("reportToId", null);
			}
			Query q = new Query();
			q.addCriteria(Criteria.where("employeeIdFK").is(employeeRoleBO.getEmployeeIdFK()));
			mongoTemplate.findAndRemove(q, EmployeeRoleBO.class);
			mongoTemplate.save(employeeRoleBsonObj,"EmployeeRole");
			return true;
			
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public String editEmp(ArrayList<EmployeeSearchPojo> employeeList) {
			
			for(EmployeeSearchPojo employee: employeeList) {
				Query query = new Query();
				query.addCriteria(Criteria.where("employeeIdFK").is(employee.getEmployeeId()));
				List<EmployeeRoleBO> qEmployeesRole = mongoTemplate.find(query, EmployeeRoleBO.class);
				
				if(qEmployeesRole != null) {
					for(EmployeeRoleBO qEmployeeRole : qEmployeesRole) {
						EmployeeBO empBO = employeeRepository.findByEmployeeId(qEmployeeRole.getEmployeeIdFK()); 
						qEmployeeRole.setArea(employee.getArea());
						qEmployeeRole.setDesignation(employee.getDesignation());
						qEmployeeRole.setStatus(employee.getStatus());
						EmployeeBO reporteeEmp = null;
						
						if(employee.getReportToId() != null) {
						
							
						if(employee.getReportToId().equals("")) {
							qEmployeeRole.setReportToId("");
						}else {
							try {
							reporteeEmp = employeeRepository.findByEmployeeId(employee.getReportToId());
							}catch(Exception e) {
								return "No Employee with employeeId: "+employee.getReportToId();
							}
							if(reporteeEmp == null) {
								return "No Employee with employeeId: "+employee.getReportToId();
							}
						}
						if(!employee.getReportToId().equals(qEmployeeRole.getReportToId())) {
							EmployeeRoleBO bossOb = employeeRoleRepository.findByEmployeeIdFKAndStatus(qEmployeeRole.getReportToId(), "active");
							
//							employeeRoleRepository.findByReportToIdAndStatus(reportTo, status, pageable)
							if(bossOb != null) {
								FilterByCriteria empHierarchyOb = new FilterByCriteria();
								empHierarchyOb.setEmployeeName(empBO.getEmployeeName());
								empHierarchyOb.setEmailId(empBO.getEmailId());
								empHierarchyOb.setEmployeeId(empBO.getEmployeeId());
								List<FilterByCriteria> emptyHierList = new ArrayList<FilterByCriteria>();
								for(FilterByCriteria empH : bossOb.getEmployeeHierarchy()) {
									if(empH.getEmailId().equals(empHierarchyOb.getEmailId())) {
										continue;
									}else {
										emptyHierList.add(empH);
									}
								}
								bossOb.setEmployeeHierarchy(emptyHierList);
								saveEmpRoleBSON(bossOb); //updates emp hierarchy of previous boss
								
								EmployeeRoleBO newbossOb = employeeRoleRepository.findByEmployeeIdFKAndStatus(employee.getReportToId(), "active");
								if(newbossOb != null) {
									newbossOb.getEmployeeHierarchy().add(empHierarchyOb);
									saveEmpRoleBSON(newbossOb);
								}
							}
						}
						qEmployeeRole.setReportToId(employee.getReportToId());
						}
						
						
//						
//						BSONObject employeeRoleBsonObj = BasicDBObjectBuilder.start()
//								.add("designation", qEmployeeRole.getDesignation())
//								.add("department", qEmployeeRole.getDepartment())
//								.add("teamName", qEmployeeRole.getTeamName())
//								.add("employeeIdFK", qEmployeeRole.getEmployeeIdFK())
//								.add("horizontal", qEmployeeRole.getHorizontal())
//								.add("vertical", qEmployeeRole.getVertical())
//								.add("region", qEmployeeRole.getRegion())
//								.add("subRegion", qEmployeeRole.getSubRegion())
//								.add("area", qEmployeeRole.getArea())
//								.add("subArea",qEmployeeRole.getSubArea())
//								.add("channel",qEmployeeRole.getChannel())
//								.add("subChannel", qEmployeeRole.getSubChannel())
//								.add("territory", qEmployeeRole.getTerritory())
//								.add("subTerritory", qEmployeeRole.getSubTerritory())
//								.add("fromDate", qEmployeeRole.getFromDate())
//								.add("toDate", qEmployeeRole.getToDate())
//								.add("status", qEmployeeRole.getStatus())
//								.add("teamSize", qEmployeeRole.getTeamSize())
//								.add("consolidatedTone", qEmployeeRole.getConsolidatedTone())
//								.add("employeeHierarchy", qEmployeeRole.getEmployeeHierarchy()).get();
//						
//						if(qEmployeeRole.getReportToId() != null && !qEmployeeRole.getReportToId().isEmpty() ) {
//							employeeRoleBsonObj.put("reportToId", qEmployeeRole.getReportToId());
//						}else {
//							employeeRoleBsonObj.put("reportToId", null);
//						}
//						Query q = new Query();
//						q.addCriteria(Criteria.where("employeeIdFK").is(qEmployeeRole.getEmployeeIdFK()));
//						mongoTemplate.findAndRemove(q, EmployeeRoleBO.class);
//						mongoTemplate.save(employeeRoleBsonObj,"EmployeeRole");
						saveEmpRoleBSON(qEmployeeRole);
//						employeeRoleRepository.save(qEmployeeRole);
						return "success";
					}
				}else {
					return "Employee not found";
				}
//				Query query2 = new Query();
//				query2.addCriteria(Criteria.where("employeeId").is(employee.getEmployeeId()));
//				mongoTemplate.findAndRemove(query2, EmployeeBO.class);
						
			}
			
			return "Updation of records failed. Please try again.";
			
		}
	
	public ClientSearchPojo addCl(FilterPojo cp) {
		ClientSearchPojo cpTo = new ClientSearchPojo();
		cpTo.setClientName(cp.getName());
		cpTo.setCompanyFK(cp.getCompanyFK());
		cpTo.setDesignation(cp.getDesignation());
		cpTo.setEmailId(cp.getEmailId());
		cpTo.setExecutive(cp.getExecutive());
		cpTo.setLocation(cp.getLocation());
		cpTo.setOrganization(cp.getOrganization());
		cpTo.setChecked(false);
		cpTo.setEditchecked(false);
		cpTo.setClientNameToggle(false);
		cpTo.setDesignationToggle(false);
		cpTo.setExecutiveToggle(false);
		cpTo.setOrganizationToggle(false);
		cpTo.setLocationToggle(false);
		return cpTo;
	}
	
public ClientSearchDTO findCl(ArrayList<ClientSearchPojo> client) {
	
		UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
				.getContext().getAuthentication();
		String pk = (String) authObj.getUserSessionInformation().get(
				EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
		
		EmployeeBO employee = employeeRepository.findOne(pk);
		
		ClientSearchDTO clientData = new ClientSearchDTO();
		
		Boolean name = false;
		Boolean designation = false;
		Boolean emailId = false;
		Boolean executive = false;
		Boolean location = false;
		Boolean organizationB = false;
		Boolean account = false;
		
		int queryCount = 0;
		
		Query query = new Query();
		
		query.addCriteria(Criteria.where("employeeIdFK").is(employee.getEmployeeId()));
		
		if(!client.get(0).getClientName().equals("")) {
			query.addCriteria(Criteria.where("clients.name").regex(Pattern.compile(client.get(0).getClientName(), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)));
			name = true;
			queryCount++;
		}
		if(!client.get(0).getDesignation().equals("")) {
			query.addCriteria(Criteria.where("clients.designation").regex(Pattern.compile(client.get(0).getDesignation(), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)));
			designation = true;
			queryCount++;
		}
		if(!client.get(0).getEmailId().equals("")) {
			query.addCriteria(Criteria.where("clients.emailId").is(client.get(0).getEmailId()));
			emailId = true;
			queryCount++;
		}
		if(!client.get(0).getExecutive().equals("")) {
			query.addCriteria(Criteria.where("clients.executive").is(client.get(0).getExecutive()));
			executive = true;
			queryCount++;
		}
		if(!client.get(0).getLocation().equals("")) {
			query.addCriteria(Criteria.where("clients.location").regex(Pattern.compile(client.get(0).getLocation(), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)));
			location = true;
			queryCount++;
		}
		if(!client.get(0).getOrganization().equals("")) {
			query.addCriteria(Criteria.where("clients.organization").regex(Pattern.compile(client.get(0).getOrganization(), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)));
			organizationB = true;
			queryCount++;
		}
		
		Query query2 = new Query();
		
		List<OrganisationBO> organizationDetails = null;
		
		if(!client.get(0).getAccount().equals("")) {
//			query2.addCriteria(Criteria.where("companyName").is(client.get(0).getAccount()));
			organizationDetails = companyRepository.findByCompanyNameRegex(client.get(0).getAccount());
		
			if(organizationDetails.size() != 0) {
			query.addCriteria(Criteria.where("clients.companyFK").is(organizationDetails.get(0).getId()));
			account = true;
			queryCount++;
			}else {
				return clientData;
			}
			
		}
			
//		query.fields().include("clients.$");
		
		List<ClientBO> clients = mongoTemplate.find(query, ClientBO.class);
		
		List<ClientSearchPojo> clSearch = new ArrayList<ClientSearchPojo>();
		
//		int queryCountCmp = 0;
		
		for(FilterPojo cls: clients.get(0).getClients()) {
			
			int queryCountCmp = 0;
			
			if(name) {
				try {
				if(cls.getName().toLowerCase().contains(client.get(0).getClientName().toLowerCase()))
					queryCountCmp++;
				}catch(Exception e) {
					
				}
			}
			if(designation) {
				try {
				if(cls.getDesignation().toLowerCase().contains(client.get(0).getDesignation().toLowerCase()))
					queryCountCmp++;
				}catch(Exception e) {
					
				}
			}
			if(emailId) {
				try {
				if(cls.getEmailId().toLowerCase().contains(client.get(0).getEmailId().toLowerCase()))
					queryCountCmp++;
				}catch(Exception e) {
									
								}
			}
			if(executive) {
				try {
				if(cls.getExecutive().equals(client.get(0).getExecutive()))
					queryCountCmp++;
				}catch(Exception e) {
									
								}
			}
			if(location) {
				try {
				if(cls.getLocation().toLowerCase().contains(client.get(0).getLocation().toLowerCase()))
					queryCountCmp++;
				}catch(Exception e) {
									
								}
			}
			if(organizationB) {
				try {
				if(cls.getOrganization().toLowerCase().contains(client.get(0).getOrganization().toLowerCase()))
					queryCountCmp++;
				}catch(Exception e) {
									
								}
			}
			if(account) {
				if(cls.getCompanyFK().equals(organizationDetails.get(0).getId()))
					queryCountCmp++;
			}
			
			
			if(queryCount == queryCountCmp) {
				
				clSearch.add(addCl(cls));
			}
			
		}
		
		
		
		
//		List<OrganisationBO> organization =  mongoTemplate.find(query2, OrganisationBO.class);
		
		
		

		
//		BeanUtils.copyProperties(clSearch, clients.get(0).getClients());
		
		if(organizationDetails != null) {
			
			for(int i=0;i<clSearch.size();i++) {
				clSearch.get(i).setAccount(organizationDetails.get(0).getCompanyName());
			}
		}else {
			for(int i=0;i<clSearch.size();i++) {
				
				try {
					clSearch.get(i).setAccount(companyRepository.findOne(clSearch.get(i).getCompanyFK()).getCompanyName());
				}catch(Exception e) {
					
				}
				
			}
		}
		
		clientData.setClientDetails(clSearch);
		

		
		return clientData;
		
	}

public void delCl(ArrayList<ClientSearchPojo> client) {
	
	UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
			.getContext().getAuthentication();
	String pk = (String) authObj.getUserSessionInformation().get(
			EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
	
	EmployeeBO employee = employeeRepository.findOne(pk);
	
	for(ClientSearchPojo cl: client) {
		Query query = new Query();
		query.addCriteria(Criteria.where("clients.emailId").is(cl.getEmailId()));
		List<ClientBO> qClient = mongoTemplate.find(query, ClientBO.class);
		
		if(qClient != null)
		for(ClientBO employeeDocument: qClient) {
			List<FilterPojo> tmpClient = new ArrayList<FilterPojo>();
			for(FilterPojo clientDocument: employeeDocument.getClients()) {
				
				if(clientDocument.getEmailId().equals(cl.getEmailId())) {
					
				}else {
					tmpClient.add(clientDocument);
				}
			}
			employeeDocument.setClients(tmpClient);
			
			clientRepository.save(employeeDocument);
			
		}
	}
	
//	ClientSearchDTO tmp = new ClientSearc
	
}

public void editCl(ArrayList<ClientSearchPojo> client) {
	
	UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
			.getContext().getAuthentication();
	String pk = (String) authObj.getUserSessionInformation().get(
			EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
	
	EmployeeBO employee = employeeRepository.findOne(pk);
	
	for(ClientSearchPojo cl: client) {
		Query query = new Query();
		query.addCriteria(Criteria.where("clients.emailId").is(cl.getEmailId()));
		List<ClientBO> qClient = mongoTemplate.find(query, ClientBO.class);
		
		if(qClient != null)
		for(ClientBO employeeDocument: qClient) {
			List<FilterPojo> tmpClient = new ArrayList<FilterPojo>();
			for(FilterPojo clientDocument: employeeDocument.getClients()) {
				
				if(clientDocument.getEmailId().equals(cl.getEmailId())) {
					
					clientDocument.setDesignation(cl.getDesignation());
					clientDocument.setExecutive(cl.getExecutive());
					clientDocument.setLocation(cl.getLocation());
					clientDocument.setName(cl.getClientName());
					clientDocument.setOrganization(cl.getOrganization());
					tmpClient.add(clientDocument);
					
				}else {
					tmpClient.add(clientDocument);
				}
			}
			employeeDocument.setClients(tmpClient);
			
			clientRepository.save(employeeDocument);
			
		}
	}
	
//	ClientSearchDTO tmp = new ClientSearc
	
}
	
	
public String setCronJob(CronDTO cronDTO) {
	
	String fileName = "cron.txt";
	
	try {
		
		UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
				.getContext().getAuthentication();
		String pk = (String) authObj.getUserSessionInformation().get(
				EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
		
		String homeDirectory = System.getProperty("user.home");
		String username = System.getProperty("user.name");
		FileWriter fileWriter = new FileWriter(fileName);
		PrintWriter printWriter = new PrintWriter(fileWriter);
//		printWriter.println("pkill -u rakshitvats cron");
		String min = cronDTO.getTime().split(":")[1];
		String hour = cronDTO.getTime().split(":")[0];
		
		if(cronDTO.getDay() == null) {
			printWriter.println(min+" "+hour+" * * * java -jar "+homeDirectory+"/kafalbatch.jar");
		}else {
			printWriter.println(min+" "+hour+" * * "+cronDTO.getDay()+" java -jar "+homeDirectory+"/kafalbatch.jar "+pk);
		}
		printWriter.close();
		fileWriter.close();
		//create a File linked to the same file using the name of this one;
        File f = new File(fileName);
        //Print absolute path
        System.out.println(f.getAbsolutePath());
        boolean isWindows = System.getProperty("os.name")
        		  .toLowerCase().startsWith("windows");
        
        
        
        System.out.println(homeDirectory);
        System.out.println(username);
        
        Process process;
        Process process2;
        if (isWindows) {
            process = Runtime.getRuntime()
              .exec(String.format("cmd.exe /c dir %s", homeDirectory));
        } else {
        	process2 = Runtime.getRuntime()
                    .exec(String.format("pkill -u "+username+" cron"));
        	process = Runtime.getRuntime()
              .exec(String.format("crontab "+ f.getAbsolutePath()));
        }
        StreamGobbler streamGobbler = 
          new StreamGobbler(process.getInputStream(), System.out::println);
        Executors.newSingleThreadExecutor().submit(streamGobbler);
        int exitCode = process.waitFor();
        assert exitCode == 0;
        
        return "101";
        
	}catch(Exception e) {
		e.printStackTrace();
		return "102";
	}
	
	
}

public String runCronJob() {
	
	try {
		UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
				.getContext().getAuthentication();
		String pk = (String) authObj.getUserSessionInformation().get(
				EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
		
		EmployeeBO employee = employeeRepository.findOne(pk);
		
		String homeDirectory = System.getProperty("user.home");
		String username = System.getProperty("user.name");
		
		
		boolean isWindows = System.getProperty("os.name")
      		  .toLowerCase().startsWith("windows");
      
      
      
	      System.out.println(homeDirectory);
	      System.out.println(username);
	      
	      Process process;
	      Process process2;
	      if (isWindows) {
	          process = Runtime.getRuntime()
	            .exec(String.format("cmd.exe /c dir %s", homeDirectory));
	      } else {
//	      	process2 = Runtime.getRuntime()
//	                  .exec(String.format("pkill -u "+username+" cron"));
	      	process = Runtime.getRuntime()
	            .exec(String.format("java -jar "+homeDirectory+"/kafalbatch.jar "+pk));
	      }
	      StreamGobbler streamGobbler = 
	        new StreamGobbler(process.getInputStream(), System.out::println);
	      Executors.newSingleThreadExecutor().submit(streamGobbler);
//	      int exitCode = process.waitFor();
//	      assert exitCode == 0;
	      
	      return "101";
		
		
	}catch(Exception e) {
		e.printStackTrace();
		return "500";
	}
	
	
	
	
}

public String stopCronJob(BatchProgressDTO batchProgress) {
	
	try {
		UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
				.getContext().getAuthentication();
		String pk = (String) authObj.getUserSessionInformation().get(
				EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
		
		EmployeeBO employee = employeeRepository.findOne(pk);
		
		EmployeeBO employeeBO = employeeRepository.findByEmployeeId(batchProgress.getUserId());
		
		if(employeeBO != null) {
		
		String homeDirectory = System.getProperty("user.home");
		String username = System.getProperty("user.name");
		
		
		boolean isWindows = System.getProperty("os.name")
      		  .toLowerCase().startsWith("windows");
      
      
      
	      System.out.println(homeDirectory);
	      System.out.println(username);
	      
	      Process process;
	      Process process2;
	      if (isWindows) {
	          process = Runtime.getRuntime()
	            .exec(String.format("cmd.exe /c dir %s", homeDirectory));
	      } else {
//	      	process2 = Runtime.getRuntime()
//	                  .exec(String.format("pkill -u "+username+" cron"));
	      	process = Runtime.getRuntime()
	            .exec(String.format("pkill -f 'java -jar '"+homeDirectory+"'/kafalbatch.jar'"));
	      }
	      StreamGobbler streamGobbler = 
	        new StreamGobbler(process.getInputStream(), System.out::println);
	      Executors.newSingleThreadExecutor().submit(streamGobbler);
	      int exitCode = process.waitFor();
	      assert exitCode == 0;
	      
	      BatchProgressBO bpBO =  batchProgressRepository.findByAdminId(employeeBO.getEmailId());
	      
	      if(bpBO != null) {
	    	  bpBO.setStatus("inactive");
	    	  batchProgressRepository.save(bpBO);
	    	  return "101";
	      }else {
	    	  return "batch status not found";
	      }
	      
		
	      
	      
		}else {
			return "id not found";
		}
		
		
	}catch(Exception e) {
		e.printStackTrace();
		return "500";
	}
	
	
	
	
}

public BatchProgressDTO getBatchProgress(BatchProgressDTO userBatchProgress) {
	
	UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
			.getContext().getAuthentication();
	String pk = (String) authObj.getUserSessionInformation().get(
			EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
	
	EmployeeBO emp = employeeRepository.findByEmployeeId(userBatchProgress.getUserId());
	
	String response = "";
	
//	EmployeeRoleBO empRole = employeeRoleRepository.findByEmployeeIdFKAndStatus(userBatchProgress.getUserId(), "");
	
	BatchProgressDTO progressDTO = new BatchProgressDTO();
	
	try {
	
		
		
		if(emp != null) {
			
			
			if(emp.getEmailId() != null) {
				if(emp.getEmailId().equals(pk)) {
					BatchProgressBO progressBO = new BatchProgressBO();
					progressBO = batchProgressRepository.findByAdminId(emp.getEmailId());
					if(progressBO != null) {
						BeanUtils.copyProperties(progressBO, progressDTO);
					}else {
						response="no batch info found";
					}
				}else {
					response = "invalid request";
				}
			}else {
				response = "email not found";
			}
			
		}else {
			response = "employee not found or inactive";
		}
		
		progressDTO.setErrorMsg(response);
		return progressDTO;
	}catch(Exception e) {
		e.printStackTrace();
		return progressDTO;
	}
	
	
}
	
public GmailConfig getGmailInfo() {
	
	UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
			.getContext().getAuthentication();
	String pk = (String) authObj.getUserSessionInformation().get(
			EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
	
	EmployeeBO employee = employeeRepository.findOne(pk);
	
	GmailConfig gmailConfig = new GmailConfig();
	
	EmailConfigurationBO userServerInfo = null;
	
	try {
		userServerInfo = emailConfigurationRepository.findByAdminEmail(pk);
	}catch(Exception e) {
		e.printStackTrace();
	}
	
	if(userServerInfo != null) {
		
		try {
		
		gmailConfig.setClientId(userServerInfo.getGmailSetting().get(0).getClientId());
		gmailConfig.setServiceAccountUser(userServerInfo.getGmailSetting().get(0).getServiceAccountUser());
		if(userServerInfo.getGmailSetting().get(0).getConfigfilelocation().length() > 0) {
			gmailConfig.setFileAvaialable(true);
		}else {
			gmailConfig.setFileAvaialable(false);
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	return gmailConfig;
	
	
	
}
	

}
