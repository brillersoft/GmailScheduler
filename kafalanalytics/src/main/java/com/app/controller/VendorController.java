package com.app.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.bo.DailyEmployeeEmailToneBO;
import com.app.bo.DailyOrgEmailToneBO;
import com.app.bo.EmployeeBO;
import com.app.bo.OrganisationBO;
import com.app.bo.VendorInfo;
import com.app.pojo.VendorInfoPOJO;
import com.app.pojo.OrganisationDashboardPOJO;
import com.app.mongo.repositories.EmployeeRepository;
//import com.kafal.notification.KafalProductNotification.repository.VendorInfoRepository;
import com.app.services.VendorService;

@RestController
@RequestMapping(value = "/")
public class VendorController {
	

	

	
	
	
	private final Logger LOG = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private VendorService vendorService;
	
	
	
//	@Resource
//	private VendorInfoRepository vendorInfoRepository;
//	
//	public VendorController(VendorInfoRepository vendorInfoRepository) {
//		this.vendorInfoRepository = vendorInfoRepository;
//	}
	
	@RequestMapping(value = "/vendordata", method = RequestMethod.GET)
	public VendorInfoPOJO getVendorInfo(){
		
		return vendorService.getVendorInfo();
		
	}

	@RequestMapping(value = "/createvendor", method = RequestMethod.POST)
	public VendorInfo addNewEmployee(@RequestBody VendorInfo vendorInfo) {
		LOG.info("Saving user.");
		return vendorService.saveVendorInfo(vendorInfo);
	}
	
//	@CrossOrigin(origins = "http://127.0.0.1:8080")
//	@RequestMapping(value = "/orgdata", method = RequestMethod.GET)
	@PostMapping("/orgdata")
	public OrganisationDashboardPOJO getOrganisationInfo(){
		
		return vendorService.getOrgDashboardInfo();
		
	}
	
//	@CrossOrigin(origins = "http://127.0.0.1:8080")
//	@RequestMapping(value = "/orgdataclient", method = RequestMethod.GET)
	@PostMapping("orgdataclient")
	public OrganisationDashboardPOJO getOrganisationInfoClientWise(){
		
		return vendorService.getOrgDashboardInfoClientWise();
		
	}
	
	@RequestMapping(value = "/createorg", method = RequestMethod.POST)
	public OrganisationBO addNewOrganisation(@RequestBody OrganisationBO organizationInfo) {
		LOG.info("Saving user.");
		return vendorService.saveOrganisationInfo(organizationInfo);
	}
	
	@RequestMapping(value = "/createorgemailtone", method = RequestMethod.POST)
	public DailyOrgEmailToneBO addNewDailyOrgEmail(@RequestBody DailyOrgEmailToneBO DailyOrgEmailTone) {
		LOG.info("Saving user.");
		return vendorService.saveDailyOrgEmailTone(DailyOrgEmailTone);
	}
	
	@RequestMapping(value = "/createempemailtone", method = RequestMethod.POST)
	public DailyEmployeeEmailToneBO addNewDailyEmpEmail(@RequestBody DailyEmployeeEmailToneBO DailyEmployeeEmailTone) {
		LOG.info("Saving user.");
		return vendorService.saveDailyEmployeeEmailTone(DailyEmployeeEmailTone);
	}
	
	
	

}
