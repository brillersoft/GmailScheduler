package com.app.services;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.app.bo.DailyEmployeeEmailToneBO;
import com.app.bo.DailyOrgEmailToneBO;
import com.app.bo.EmployeeBO;
import com.app.bo.OrganisationBO;
import com.app.bo.VendorInfo;
import com.app.pojo.VendorInfoPOJO;
import com.app.security.UserAuthCredentials;
import com.app.utilities.EnumeratedTypes;
import com.app.pojo.OrganisationDashboardPOJO;
import com.app.mongo.repositories.DailyEmployeeEmailToneRepository;
import com.app.mongo.repositories.DailyOrgEmailToneRepository;
import com.app.mongo.repositories.EmployeeRepository;
import com.app.mongo.repositories.CompanyRepository;
import com.app.mongo.repositories.VendorInfoRepository;
//import com.kafal.notification.KafalProductNotification.service.VendorService;
import com.app.pojo.EmailToneResultPojo;
import com.app.bo.ToneOfMail;

@Service
public class VendorService{
	
	@Resource
	private VendorInfoRepository vendorInfoRepository;
	
	@Resource
	private CompanyRepository orgRepository;
	
	@Resource
	private EmployeeRepository employeeRepository;
	
	@Resource
	private DailyOrgEmailToneRepository dailyOrgEmailToneRepository;
	
	@Resource
	private DailyEmployeeEmailToneRepository dailyEmpEmailToneRepository;
	
	VendorInfoPOJO vendorPOJO = new VendorInfoPOJO();
	OrganisationDashboardPOJO orgPOJO = new OrganisationDashboardPOJO();
	
	public VendorInfoPOJO getVendorInfo(){
		ArrayList<String> labels = new ArrayList<String>();
		ArrayList<Integer> revenues = new ArrayList<Integer>();
		ArrayList<Integer> margins = new ArrayList<Integer>();
		List<Integer> happyCounts = new ArrayList<Integer>();
		HashMap<Integer , List<Integer>> doughnut = new HashMap<Integer, List<Integer>>();
		List<VendorInfo> vendorInfo = vendorInfoRepository.findAll();
		for(int i=0;i<vendorInfo.size();i++){
			happyCounts.clear();
			labels.add(vendorInfo.get(i).getVendorName());
			revenues.add(vendorInfo.get(i).getVendorRevenue());
			margins.add(vendorInfo.get(i).getVendorMargin());
			happyCounts.add(vendorInfo.get(i).getVendorHappyPercentage());
			happyCounts.add(vendorInfo.get(i).getVendorUnhappyPercentage());
			doughnut.put(i, happyCounts);
			
		}
		
		vendorPOJO.setDataBarLabels(labels);
		vendorPOJO.setDataBarMargin(margins);
		vendorPOJO.setDataBarRevenue(revenues);
		vendorPOJO.setMap(doughnut);
		
		return vendorPOJO;
		
	}
	
	public VendorInfo saveVendorInfo(VendorInfo vendorInfo){
		return vendorInfoRepository.save(vendorInfo);
	}
	
	public OrganisationBO saveOrganisationInfo(OrganisationBO organisationInfo){
		return orgRepository.save(organisationInfo);
	}
	
	public DailyOrgEmailToneBO saveDailyOrgEmailTone(DailyOrgEmailToneBO DailyOrgEmailTone){
		return dailyOrgEmailToneRepository.save(DailyOrgEmailTone);
	}
	
	public DailyEmployeeEmailToneBO saveDailyEmployeeEmailTone(DailyEmployeeEmailToneBO DailyEmployeeEmailTone){
		return dailyEmpEmailToneRepository.save(DailyEmployeeEmailTone);
	}
	
	
	public OrganisationDashboardPOJO getOrgDashboardInfo(){
		
		UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
				.getContext().getAuthentication();
		
//		EmployeePersonalDataDTO employeePersonalDataDTO1 = new EmployeePersonalDataDTO();
		
		String emailId = (String) authObj.getUserSessionInformation().get(
				EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
		
		EmployeeBO employeeData = employeeRepository.findOne(emailId);
		
		ArrayList<String> labels = new ArrayList<String>();
		ArrayList<Integer> revenues = new ArrayList<Integer>();
		ArrayList<Integer> margins = new ArrayList<Integer>();
		List<Integer> happyCounts = new ArrayList<Integer>();
		List<DailyEmployeeEmailToneBO> orgAllMailTones = null;
		List<DailyEmployeeEmailToneBO> orgAllMailTones1 = null;
		HashMap<Integer , List<Integer>> doughnut = new HashMap<Integer, List<Integer>>();
		List<OrganisationBO> orgList = orgRepository.findAll();
		for(int i=0;i<orgList.size();i++){
			int happy = 0;
			int unhappy = 0;
			int neutral = 0;
			Double happySum = 0.0;
			Double unhappySum = 0.0;
			happyCounts = new ArrayList<Integer>();
			labels.add(orgList.get(i).getCompanyName());
			revenues.add(orgList.get(i).getCompanyRevenue());
			margins.add(orgList.get(i).getCompanyMargin());
			
			try{
				orgAllMailTones = dailyEmpEmailToneRepository.findByFromMail(".*"+orgList.get(i).getCompanyEmailDomain()+".*", employeeData.getEmployeeId());
				for(int j=0;j<orgAllMailTones.size();j++){
					
					for(int k=0;k<orgAllMailTones.get(j).getClientEmailItems().size();k++) {
						try {
//							System.out.println(orgAllMailTones.get(j).getClientEmailItems().get(k).getFromMail().split("@")[1]);
						}catch(Exception e){
							System.out.println(e);
						}
						if(orgAllMailTones.get(j).getClientEmailItems().get(k).getType().equalsIgnoreCase("sent") || !orgAllMailTones.get(j).getClientEmailItems().get(k).getFromMail().split("@")[1].equalsIgnoreCase(orgList.get(i).getCompanyEmailDomain())) {
							continue;
						}
							
						happySum = orgAllMailTones.get(j).getClientEmailItems().get(k).getJoy();
						Double devider = 0.0d;
						if(orgAllMailTones.get(j).getClientEmailItems().get(k).getAnger() != 0.0d)
							devider++;
						if(orgAllMailTones.get(j).getClientEmailItems().get(k).getFear() != 0.0d)
							devider++;
						if(orgAllMailTones.get(j).getClientEmailItems().get(k).getSadness() != 0.0d)
							devider++;
						if(devider != 0) {
							unhappySum =  ((orgAllMailTones.get(j).getClientEmailItems().get(k).getAnger())+(orgAllMailTones.get(j).getClientEmailItems().get(k).getFear())+(orgAllMailTones.get(j).getClientEmailItems().get(k).getSadness()))/devider;
						}else {
							unhappySum = 0.0d;
						}
						if(happySum>unhappySum){
							happy++;
						}else if(unhappySum > happySum){
							unhappy++;
						}else {
							neutral++;
						}
					}
					
				}
				orgAllMailTones1 = dailyEmpEmailToneRepository.findByFromMailline(".*"+orgList.get(i).getCompanyEmailDomain()+".*", employeeData.getEmployeeId());
				for(int j=0;j<orgAllMailTones1.size();j++){
				for(int k=0;k<orgAllMailTones1.get(j).getLineItems().size();k++) {
					try {
//						System.out.println(orgAllMailTones1.get(j).getLineItems().get(k).getFromMail().split("@")[1]);
					}catch(Exception e){
						System.out.println(e);
					}
					if(orgAllMailTones1.get(j).getLineItems().get(k).getType().equalsIgnoreCase("sent") || !orgAllMailTones1.get(j).getLineItems().get(k).getFromMail().split("@")[1].equalsIgnoreCase(orgList.get(i).getCompanyEmailDomain())) {
						continue;
					}
						
					happySum = orgAllMailTones1.get(j).getLineItems().get(k).getJoy();
					Double devider = 0.0d;
					if(orgAllMailTones1.get(j).getLineItems().get(k).getAnger() != 0.0d)
						devider++;
					if(orgAllMailTones1.get(j).getLineItems().get(k).getFear() != 0.0d)
						devider++;
					if(orgAllMailTones1.get(j).getLineItems().get(k).getSadness() != 0.0d)
						devider++;
					if(devider != 0) {
					unhappySum =  ((orgAllMailTones1.get(j).getLineItems().get(k).getAnger())+(orgAllMailTones1.get(j).getLineItems().get(k).getFear())+(orgAllMailTones1.get(j).getLineItems().get(k).getSadness()))/devider;
					}else {
						unhappySum = 0.0d;
					}
					if(happySum>unhappySum){
						happy++;
					}else if(unhappySum > happySum){
						unhappy++;
					}else {
						neutral++;
					}
				}
				}
			}catch(Exception e){
				System.out.println(e);
				
			}
			
			happyCounts.add(unhappy);
			happyCounts.add(happy);
			happyCounts.add(neutral);
			
			doughnut.put(i, happyCounts);
			
		}
		
		orgPOJO.setDataBarLabels(labels);
		orgPOJO.setDataBarMargin(margins);
		orgPOJO.setDataBarRevenue(revenues);
		orgPOJO.setMap(doughnut);
		
		return orgPOJO;
	}
	
	public OrganisationDashboardPOJO getOrgDashboardInfoClientWise() {
		
		UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
				.getContext().getAuthentication();
		
//		EmployeePersonalDataDTO employeePersonalDataDTO1 = new EmployeePersonalDataDTO();
		
		String emailId = (String) authObj.getUserSessionInformation().get(
				EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
		
		EmployeeBO employeeData = employeeRepository.findOne(emailId);
		
		ArrayList<String> labels = new ArrayList<String>();
		ArrayList<Integer> revenues = new ArrayList<Integer>();
		ArrayList<Integer> margins = new ArrayList<Integer>();
		List<Integer> happyCounts = new ArrayList<Integer>();
		List<DailyEmployeeEmailToneBO> orgAllMailTones = null;
		List<DailyEmployeeEmailToneBO> orgAllMailTones1 = null;
		HashMap<Integer , List<Integer>> doughnut = new HashMap<Integer, List<Integer>>();
		List<OrganisationBO> orgList = orgRepository.findAll();
		List<DailyEmployeeEmailToneBO> allClientMailOB= null;
		
		for(int i=0;i<orgList.size();i++){
			int happy = 0;
			int unhappy = 0;
			int neutral = 0;
			Double happySum = 0.0d;
			Double unhappySum = 0.0d;
			happyCounts = new ArrayList<Integer>();
			labels.add(orgList.get(i).getCompanyName());
			revenues.add(orgList.get(i).getCompanyRevenue());
			margins.add(orgList.get(i).getCompanyMargin());
			HashMap<String, ArrayList<Integer>> clientList = new HashMap<String, ArrayList<Integer>>(); 
			HashMap<String, ArrayList<LocalDateTime>> emailList = new HashMap<String, ArrayList<LocalDateTime>>(); 
			int unhappyClients = 0;
			int happyClients = 0;
			try{
				orgAllMailTones = dailyEmpEmailToneRepository.findByFromMail(".*"+orgList.get(i).getCompanyEmailDomain()+".*", employeeData.getEmployeeId());
//				List<String> emailList = new ArrayList<String>();
				for(int j=0;j<orgAllMailTones.size();j++){
					
					for(int k=0;k<orgAllMailTones.get(j).getClientEmailItems().size();k++) {
						if(orgAllMailTones.get(j).getClientEmailItems().get(k).getType().equalsIgnoreCase("sent") || !orgAllMailTones.get(j).getClientEmailItems().get(k).getFromMail().split("@")[1].equalsIgnoreCase(orgList.get(i).getCompanyEmailDomain())) {
							continue;
						}
//						String[] date = orgAllMailTones.get(j).getClientEmailItems().get(k).getDate().split("-");
//						System.out.println(date[1]);
//						System.out.println(date[2]);
//						String day = date[0];
//						String month = "01";
//						if(date[1].equals("January"))
//							month = "01";
//						else if(date[1].equals("Febuary"))
//							month = "02";
//						else if(date[1].equals("March"))
//							month = "03";
//						else if(date[1].equals("April"))
//							month = "04";
//						else if(date[1].equals("May"))
//							month = "05";
//						else if(date[1].equals("June"))
//							month = "06";
//						else if(date[1].equals("July"))
//							month = "07";
//						else if(date[1].equals("August"))
//							month = "08";
//						else if(date[1].equals("September"))
//							month = "09";
//						else if(date[1].equals("October"))
//							month = "10";
//						else if(date[1].equals("November"))
//							month = "11";
//						else if(date[1].equals("December"))
//							month = "12";
//						String year = date[2]; 
//						Date newDate = new Date();
//						newDate.setDate(Integer.parseInt(day));
//						newDate.setMonth(Integer.parseInt(month)-1);
//						
//						newDate.setYear(Integer.parseInt(year)-1900);
//						
//						
//						System.out.println(newDate);
//						String[] time = orgAllMailTones.get(j).getClientEmailItems().get(k).getTime().split(":");
//						String hh = time[0];
//						String[] mmh = time[1].split("\\s+");
//						String mm = mmh[0];
//						Date time1 = new Date();
//						time1.setHours(Integer.parseInt(hh));
//						time1.setMinutes(Integer.parseInt(mm));
//						System.out.println(time1);
//						String startingDate = new SimpleDateFormat("yyyy-MM-dd").format(newDate);
//						System.out.println(startingDate);
//						String startingTime = new SimpleDateFormat("hh:mm:ss").format(time1);
//						LocalDate datePart = LocalDate.parse(startingDate);
//					    LocalTime timePart = LocalTime.parse(startingTime);
//					    LocalDateTime dt = LocalDateTime.of(datePart, timePart);
						LocalDate datePart = LocalDate.parse(orgAllMailTones.get(j).getClientEmailItems().get(k).getDate());
					    LocalTime timePart = LocalTime.parse(orgAllMailTones.get(j).getClientEmailItems().get(k).getTime());
					    LocalDateTime dt = LocalDateTime.of(datePart, timePart);
						
						
						if(emailList.containsKey(orgAllMailTones.get(j).getClientEmailItems().get(k).getFromMail())) {
							
						    emailList.get(orgAllMailTones.get(j).getClientEmailItems().get(k).getFromMail()).add(dt);
						}else {
							ArrayList<LocalDateTime> list1 = new ArrayList<LocalDateTime>();
							list1.add(dt);
							emailList.put(orgAllMailTones.get(j).getClientEmailItems().get(k).getFromMail(), new ArrayList<LocalDateTime>(list1));
							
						}
						
						
//						if(emailList.contains(orgAllMailTones.get(j).getClientEmailItems().get(k).getFrom())) {
//							continue;
//						}else {
//							emailList.add(orgAllMailTones.get(j).getClientEmailItems().get(k).getFrom());
//							allClientMailOB = dailyEmpEmailToneRepository.findByFrom(orgAllMailTones.get(j).getClientEmailItems().get(k).getFrom());
//							for(int l=0;l<allClientMailOB.size();l++) {
//								
//							}
//						}
						
						
					}
					
					
				}
				
				orgAllMailTones1 = dailyEmpEmailToneRepository.findByFromMailline(".*"+orgList.get(i).getCompanyEmailDomain()+".*", employeeData.getEmployeeId());
				for(int j=0;j<orgAllMailTones1.size();j++){
				for(int k=0;k<orgAllMailTones1.get(j).getLineItems().size();k++) {
					if(orgAllMailTones1.get(j).getLineItems().get(k).getType().equalsIgnoreCase("sent") || !orgAllMailTones1.get(j).getLineItems().get(k).getFromMail().split("@")[1].equalsIgnoreCase(orgList.get(i).getCompanyEmailDomain())) {
						continue;
					}
//					String[] date = orgAllMailTones.get(j).getClientEmailItems().get(k).getDate().split("-");
//					System.out.println(date[1]);
//					System.out.println(date[2]);
//					String day = date[0];
//					String month = "01";
//					if(date[1].equals("January"))
//						month = "01";
//					else if(date[1].equals("Febuary"))
//						month = "02";
//					else if(date[1].equals("March"))
//						month = "03";
//					else if(date[1].equals("April"))
//						month = "04";
//					else if(date[1].equals("May"))
//						month = "05";
//					else if(date[1].equals("June"))
//						month = "06";
//					else if(date[1].equals("July"))
//						month = "07";
//					else if(date[1].equals("August"))
//						month = "08";
//					else if(date[1].equals("September"))
//						month = "09";
//					else if(date[1].equals("October"))
//						month = "10";
//					else if(date[1].equals("November"))
//						month = "11";
//					else if(date[1].equals("December"))
//						month = "12";
//					String year = date[2]; 
//					Date newDate = new Date();
//					newDate.setDate(Integer.parseInt(day));
//					newDate.setMonth(Integer.parseInt(month)-1);
//					
//					newDate.setYear(Integer.parseInt(year)-1900);
//					
//					
//					System.out.println(newDate);
//					String[] time = orgAllMailTones.get(j).getClientEmailItems().get(k).getTime().split(":");
//					String hh = time[0];
//					String[] mmh = time[1].split("\\s+");
//					String mm = mmh[0];
//					Date time1 = new Date();
//					time1.setHours(Integer.parseInt(hh));
//					time1.setMinutes(Integer.parseInt(mm));
//					System.out.println(time1);
//					String startingDate = new SimpleDateFormat("yyyy-MM-dd").format(newDate);
//					System.out.println(startingDate);
//					String startingTime = new SimpleDateFormat("hh:mm:ss").format(time1);
//					LocalDate datePart = LocalDate.parse(startingDate);
//				    LocalTime timePart = LocalTime.parse(startingTime);
//				    LocalDateTime dt = LocalDateTime.of(datePart, timePart);
					LocalDate datePart = LocalDate.parse(orgAllMailTones.get(j).getLineItems().get(k).getDate());
				    LocalTime timePart = LocalTime.parse(orgAllMailTones.get(j).getLineItems().get(k).getTime());
				    LocalDateTime dt = LocalDateTime.of(datePart, timePart);
					
					
					if(emailList.containsKey(orgAllMailTones1.get(j).getLineItems().get(k).getFromMail())) {
						
					    emailList.get(orgAllMailTones1.get(j).getLineItems().get(k).getFromMail()).add(dt);
					}else {
						ArrayList<LocalDateTime> list1 = new ArrayList<LocalDateTime>();
						list1.add(dt);
						emailList.put(orgAllMailTones1.get(j).getLineItems().get(k).getFromMail(), new ArrayList<LocalDateTime>(list1));
						
					}
					
					
//					if(emailList.contains(orgAllMailTones.get(j).getClientEmailItems().get(k).getFrom())) {
//						continue;
//					}else {
//						emailList.add(orgAllMailTones.get(j).getClientEmailItems().get(k).getFrom());
//						allClientMailOB = dailyEmpEmailToneRepository.findByFrom(orgAllMailTones.get(j).getClientEmailItems().get(k).getFrom());
//						for(int l=0;l<allClientMailOB.size();l++) {
//							
//						}
//					}
					
				}
				}
			}catch(Exception e){
				System.out.println(e);
//				System.out.println("not a num");
			}
			for(String email: emailList.keySet()) {
				Double angryCount = 0.0d;
				Double fearCount = 0.0d;
				Double sadCount = 0.0d;
				Double joyCount = 0.0d;
				for(LocalDateTime dates: emailList.get(email)) {
					String date = dates.toLocalDate().toString();
//					String month = "";
//					if(date.getMonthValue()==01)
//						month = "January";
//					else if(date.getMonthValue()==2)
//						month = "Febuary";
//					else if(date.getMonthValue()==3)
//						month = "March";
//					else if(date.getMonthValue()==4)
//						month = "April";
//					else if(date.getMonthValue()==5)
//						month = "May";
//					else if(date.getMonthValue()==6)
//						month = "June";
//					else if(date.getMonthValue()==7)
//						month = "July";
//					else if(date.getMonthValue()==8)
//						month = "August";
//					else if(date.getMonthValue()==9)
//						month = "September";
//					else if(date.getMonthValue()==10)
//						month = "October";
//					else if(date.getMonthValue()==11)
//						month = "November";
//					else if(date.getMonthValue()==12)
//						month = "December";
////					System.out.println(date);
//					String parseDate = Integer.toString(date.getDayOfMonth()) + "-" + month + "-" + Integer.toString(date.getYear());
					
					String time = dates.toLocalTime().toString();
					
//					String parseTime = Integer.toString(time.getHour()) + ":" + Integer.toString(time.getMinute());
//					System.out.println(time);
//					System.out.println(date);
//					System.out.println(email);
					List<DailyEmployeeEmailToneBO> empList=dailyEmpEmailToneRepository.findByFromMailAndDateAndTime(employeeData.getEmployeeId(), email, date, time);
					for(int j=0; j<empList.size();j++) {
						for(int k=0;k<empList.get(j).getClientEmailItems().size();k++) {
							if(empList.get(j).getClientEmailItems().get(k).getDate().equals(date) && empList.get(j).getClientEmailItems().get(k).getTime().equals(time) && empList.get(j).getClientEmailItems().get(k).getFromMail().equals(email)) {
								angryCount += empList.get(j).getClientEmailItems().get(k).getAnger();
								fearCount += empList.get(j).getClientEmailItems().get(k).getFear();
								sadCount += empList.get(j).getClientEmailItems().get(k).getSadness();
								joyCount += empList.get(j).getClientEmailItems().get(k).getJoy();
							}
						}
						
						
					}
					List<DailyEmployeeEmailToneBO> empList1=dailyEmpEmailToneRepository.findByFromMailAndDateAndTimeline(employeeData.getEmployeeId(), email, date, time);
					for(int j=0; j<empList1.size();j++) {
					for(int k=0;k<empList1.get(j).getLineItems().size();k++) {
						if(empList1.get(j).getLineItems().get(k).getDate().equals(date) && empList1.get(j).getLineItems().get(k).getTime().equals(time) && empList1.get(j).getLineItems().get(k).getFromMail().equals(email)) {
							angryCount += empList1.get(j).getLineItems().get(k).getAnger();
							fearCount += empList1.get(j).getLineItems().get(k).getFear();
							sadCount += empList1.get(j).getLineItems().get(k).getSadness();
							joyCount += empList1.get(j).getLineItems().get(k).getJoy();
						}
					}
					}
					
				}
				
				Double devider1 = 0.0d;
				if(angryCount !=0.0d)
					devider1++;
				if(fearCount !=0.0d)
					devider1++;
				if(sadCount != 0.0d)
					devider1++;
				Double sum = 0.0d;
				if(devider1 != 0) {
				sum = (angryCount+fearCount+sadCount)/devider1;
				}else {
					sum =0.0d;
				}
				if(sum>joyCount) {
					unhappy++;
				}else if(sum<joyCount) {
					happy++;
				}else {
					neutral++;
				}
			}
			
			happyCounts.add(unhappy);
			happyCounts.add(happy);
			happyCounts.add(neutral);
			
			
			
			doughnut.put(i, happyCounts);
			
		}
		
	 
		
		orgPOJO.setDataBarLabels(labels);
		orgPOJO.setDataBarMargin(margins);
		orgPOJO.setDataBarRevenue(revenues);
		orgPOJO.setMap(doughnut);
		
		return orgPOJO;
		
		
		
		
	}


}
