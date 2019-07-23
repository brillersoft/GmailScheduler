package com.app.utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.bo.EmployeeBO;
import com.app.dto.EmployeePersonalDataDTO;
import com.app.mongo.repositories.EmployeeRepository;
import com.app.pojo.EmailPojo;


@Service
public class ExportDataToExcel {
	
	
	
	@Autowired
	private static AppUtils appUtils;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	static String filePath = null;
	
	public  String generateEmployeeList(EmployeePersonalDataDTO usrData) {
        try {
        	
        	
        	 XSSFWorkbook workbook = new XSSFWorkbook(); 
             XSSFSheet spreadsheet = workbook
             .createSheet("Employee Data");
             XSSFRow row=spreadsheet.createRow(1);
             XSSFCell cell;
             cell=row.createCell(1);
             cell.setCellValue("Serial No.");
             cell=row.createCell(2);
             cell.setCellValue("Employee Id");
             cell=row.createCell(3);
             cell.setCellValue("Employee Name");
             cell=row.createCell(4);
             cell.setCellValue("Email Id");
             cell=row.createCell(5);
             cell.setCellValue("Anger");
             cell=row.createCell(6);
             cell.setCellValue("Joy");
             cell=row.createCell(7);
             cell.setCellValue("Sadness");
             cell=row.createCell(8);
             cell.setCellValue("Fear");
             cell=row.createCell(9);
             cell.setCellValue("Tentative");
             cell=row.createCell(10);
             cell.setCellValue("Confident");
             cell=row.createCell(11);
             cell.setCellValue("Analytical");
             cell=row.createCell(12);
             cell.setCellValue("EmailCount");
             cell=row.createCell(13);
             cell.setCellValue("Progress");
               
                         
             int i=2;
             int rowCount = 1;
             
             for(EmployeePersonalDataDTO employeePersonalDataDTO : usrData.getListOfEmployee())
             {
            	 
            
            	// String sapList = StringUtils.join(sap, ",");
            	 String emailId = employeeRepository.findByEmployeeId(employeePersonalDataDTO.getEmployeeId()).getEmailId();
            	 row=spreadsheet.createRow(i);
            	 cell=row.createCell(1);
                 cell.setCellValue(rowCount);
                 if(employeePersonalDataDTO.getEmployeeId()!=null)
                 {
                	 cell = row.createCell(2);
                	 cell.setCellValue(employeePersonalDataDTO.getEmployeeId());
                 }else{
                	 cell = row.createCell(2);
                	 cell.setCellValue("");
                 }
                 if(employeePersonalDataDTO.getEmployeeName()!=null)
                 {
                	 cell = row.createCell(3);
                	 cell.setCellValue(employeePersonalDataDTO.getEmployeeName());
                 }else{
                	 cell = row.createCell(3);
                	 cell.setCellValue("");
                 }
                 if(emailId!=null)
                 {
                	 cell = row.createCell(4);
                	 cell.setCellValue(emailId);
                 }else{
                	 cell = row.createCell(4);
                	 cell.setCellValue("");
                 }
                 if(employeePersonalDataDTO.getToneOfTeamMail().getAnger()!=null)
                 {
                	 cell = row.createCell(5);
                	 cell.setCellValue(employeePersonalDataDTO.getToneOfTeamMail().getAnger());
                 }else{
                	 cell = row.createCell(5);
                	 cell.setCellValue("");
                 }
                 if(employeePersonalDataDTO.getToneOfTeamMail().getJoy()!=null)
                 {
                	 cell = row.createCell(6);
                	 cell.setCellValue(employeePersonalDataDTO.getToneOfTeamMail().getJoy());
                 }else{
                	 cell = row.createCell(6);
                	 cell.setCellValue("");
                 }
                 if(employeePersonalDataDTO.getToneOfTeamMail().getSadness()!=null)
                 {
                	 cell = row.createCell(7);
                	 cell.setCellValue(employeePersonalDataDTO.getToneOfTeamMail().getSadness());
                 }else{
                	 cell = row.createCell(7);
                	 cell.setCellValue("");
                 }
                 if(null != employeePersonalDataDTO.getToneOfTeamMail().getFear())
                 {
                	 cell = row.createCell(8);
                	 cell.setCellValue(employeePersonalDataDTO.getToneOfTeamMail().getFear());
                 }else{
                	 cell = row.createCell(8);
                	 cell.setCellValue("");
                 }
                 if(null != employeePersonalDataDTO.getToneOfTeamMail().getTentative())
                 {
                	 cell=row.createCell(9);
                	 cell.setCellValue(employeePersonalDataDTO.getToneOfTeamMail().getTentative());
                 }else{
                	 cell=row.createCell(9);
                	 cell.setCellValue("");
                 }
                
                if(null!=employeePersonalDataDTO.getToneOfTeamMail().getConfident())
                {
                	cell=row.createCell(10);
                	cell.setCellValue(employeePersonalDataDTO.getToneOfTeamMail().getConfident());
                	
                }else{
                	cell=row.createCell(10);
                	cell.setCellValue("");
                }
                
                if(null!=employeePersonalDataDTO.getToneOfTeamMail().getAnalytical())
                {
                	cell=row.createCell(11);
                	cell.setCellValue(employeePersonalDataDTO.getToneOfTeamMail().getAnalytical());
                	
                }else{
                	cell=row.createCell(11);
                	cell.setCellValue("");
                }
                
                if(null!=employeePersonalDataDTO.getNoOfMail())
                {
                	cell=row.createCell(12);
                	cell.setCellValue(employeePersonalDataDTO.getNoOfMail());
                	
                }else{
                	
                	cell=row.createCell(12);
                	cell.setCellValue("");
                }
                
               /* if(null!=employeePersonalDataDTO.getToneOfTeamMail().getConfident())
                {
                	cell=row.createCell(13);
                	cell.setCellValue(employeePersonalDataDTO.getToneOfTeamMail().getConfident());
                	
                }else{
                	cell=row.createCell(13);
                	cell.setCellValue("");
                }
                 */
                                
                
                 
                 i++;
                 rowCount ++;
                 
			 }
			 String	filePathSave =null;
             
             if(appUtils.inProduction)
     		{
            	 filePath ="/var/lib/tomcat7/conf/MasterUploadBak/UserList.xlsx";
     		}else{
     				
     		String workingDir = System.getProperty("user.dir");
//     		String workingDir = ExportDataToExcel.class.getResource("").getPath();
     		System.out.println(new File(".").getCanonicalPath());
     		System.out.println(workingDir);
//     		System.out.println(getServletContext().getRealPath("/"));
//     		filePathSave ="D:\\workspace\\kafalanalytics\\src\\main\\webapp\\images\\EmployeeList.xlsx";
//     		filePathSave = workingDir + "/EmployeeList.xlsx";
//     		filePathSave = "/opt/tomcat/KafalAnalytics";
//			 filePath = "images\\EmployeeList.xlsx";
//     		filePath = "Users\\rakshitvats\\Downloads\\Eclipse\\Eclipse.app\\Contents\\MacOS\\EmployeeList.xlsx";
//     		filePath = filePathSave;
     		filePathSave = "/opt/tomcat/webapps/HanogiAnalytics/EmployeeList.xlsx";
     		filePath = "EmployeeList.xlsx";
     		}
     			
             FileOutputStream out = new FileOutputStream(filePathSave);
             workbook.write(out);
             out.close();
                			
        }catch(Exception e)
        {
        	e.printStackTrace();
        }
		return filePath;
		
        }
	
	
	  // client dashBoard data download
	
	
	public  String generateClientList(EmployeePersonalDataDTO usrData) {
        try {
        	
        	
        	 XSSFWorkbook workbook = new XSSFWorkbook(); 
             XSSFSheet spreadsheet = workbook
             .createSheet("Client Data");
             XSSFRow row=spreadsheet.createRow(1);
             XSSFCell cell;
             cell=row.createCell(1);
             cell.setCellValue("Serial No.");
             cell=row.createCell(2);
             cell.setCellValue("Company Name");
             cell=row.createCell(3);
             cell.setCellValue("Client Name");
             cell=row.createCell(4);
             cell.setCellValue("Client EmailId");
             cell=row.createCell(5);
             cell.setCellValue("ComP. Anger");
             cell=row.createCell(6);
             cell.setCellValue("ComP. Joy");
             cell=row.createCell(7);
             cell.setCellValue("ComP. Sadness");
             cell=row.createCell(8);
             cell.setCellValue("ComP. Fear");
             cell=row.createCell(9);
             cell.setCellValue("ComP. Tentative");
             cell=row.createCell(10);
             cell.setCellValue("ComP. Confident");
             cell=row.createCell(11);
             cell.setCellValue("ComP. Analytical");
             cell=row.createCell(12);
             cell.setCellValue("ComP. AllMail");
             cell=row.createCell(13);
             cell.setCellValue("ComP. AllReceivedMail");
             cell=row.createCell(14);
             cell.setCellValue("ComP. AllSentMail");
             cell=row.createCell(15);
             cell.setCellValue("Client_Anger");
             cell=row.createCell(16);
             cell.setCellValue("Client_Joy");
             cell=row.createCell(17);
             cell.setCellValue("Client_Sadness");
             cell=row.createCell(18);
             cell.setCellValue("Client_Fear");
             cell=row.createCell(19);
             cell.setCellValue("Client_Tentative");
             cell=row.createCell(20);
             cell.setCellValue("Client_Confident");
             cell=row.createCell(21);
             cell.setCellValue("Client_Analytical");
             cell=row.createCell(22);
             cell.setCellValue("Client_AllMail");
             cell=row.createCell(23);
             cell.setCellValue("Client_AllReceivedMail");
             cell=row.createCell(24);
             cell.setCellValue("Client_AllSentMail");
             cell=row.createCell(25);
             cell.setCellValue("Progress");
               
                         
             int i=2;
             int rowCount = 1;
             
             for(EmployeePersonalDataDTO employeePersonalDataDTO : usrData.getListOfEmployee())
             {
            	 
            
            	// String sapList = StringUtils.join(sap, ",");
            	// String emailId = employeeRepository.findByEmployeeId(employeePersonalDataDTO.getEmployeeId()).getEmailId();
            	 row=spreadsheet.createRow(i);
            	 cell=row.createCell(1);
                 cell.setCellValue(rowCount);
                 if(usrData.getCompanyName()!=null)
                 {
                	 cell = row.createCell(2);
                	 cell.setCellValue(employeePersonalDataDTO.getCompanyName());
                 }else{
                	 cell = row.createCell(2);
                	 cell.setCellValue("");
                 }
                 if(employeePersonalDataDTO.getEmployeeName()!=null)
                 {
                	 cell = row.createCell(3);
                	 cell.setCellValue(employeePersonalDataDTO.getEmployeeName());
                 }else{
                	 cell = row.createCell(3);
                	 cell.setCellValue("");
                 }
                 if(employeePersonalDataDTO.getEmployeeId()!=null)
                 {
                	 cell = row.createCell(4);
                	 cell.setCellValue(employeePersonalDataDTO.getEmployeeId());
                 }else{
                	 cell = row.createCell(4);
                	 cell.setCellValue("");
                 }
                 if(usrData.getToneOfClientMail().getAnger()!=null)
                 {
                	 cell = row.createCell(5);
                	 cell.setCellValue(usrData.getToneOfClientMail().getAnger());
                 }else{
                	 cell = row.createCell(5);
                	 cell.setCellValue("");
                 }
                 if(usrData.getToneOfClientMail().getJoy()!=null)
                 {
                	 cell = row.createCell(6);
                	 cell.setCellValue(usrData.getToneOfClientMail().getJoy());
                 }else{
                	 cell = row.createCell(6);
                	 cell.setCellValue("");
                 }
                 if(usrData.getToneOfClientMail().getSadness()!=null)
                 {
                	 cell = row.createCell(7);
                	 cell.setCellValue(usrData.getToneOfClientMail().getSadness());
                 }else{
                	 cell = row.createCell(7);
                	 cell.setCellValue("");
                 }
                 if(null != usrData.getToneOfClientMail().getFear())
                 {
                	 cell = row.createCell(8);
                	 cell.setCellValue(usrData.getToneOfClientMail().getFear());
                 }else{
                	 cell = row.createCell(8);
                	 cell.setCellValue("");
                 }
                 if(null != usrData.getToneOfClientMail().getTentative())
                 {
                	 cell=row.createCell(9);
                	 cell.setCellValue(usrData.getToneOfClientMail().getTentative());
                 }else{
                	 cell=row.createCell(9);
                	 cell.setCellValue("");
                 }
                
                if(null!=usrData.getToneOfClientMail().getConfident())
                {
                	cell=row.createCell(10);
                	cell.setCellValue(usrData.getToneOfClientMail().getConfident());
                	
                }else{
                	cell=row.createCell(10);
                	cell.setCellValue("");
                }
                
                if(null!=usrData.getToneOfClientMail().getAnalytical())
                {
                	cell=row.createCell(11);
                	cell.setCellValue(usrData.getToneOfClientMail().getAnalytical());
                	
                }else{
                	cell=row.createCell(11);
                	cell.setCellValue("");
                }
                
                if(null!=usrData.getNoOfMail())
                {
                	cell=row.createCell(12);
                	cell.setCellValue(usrData.getNoOfMail());
                	
                }else{
                	cell=row.createCell(12);
                	cell.setCellValue("");
                }
                if(null!=usrData.getNoOfReceiveMail())
                {
                	cell=row.createCell(13);
                	cell.setCellValue(usrData.getNoOfReceiveMail());
                	
                }else{
                	cell=row.createCell(13);
                	cell.setCellValue("");
                }
                if(null!=usrData.getNoOfSentMail())
                {
                	cell=row.createCell(14);
                	cell.setCellValue(usrData.getNoOfSentMail());
                	
                }else{
                	cell=row.createCell(14);
                	cell.setCellValue("");
                }
                
                if(employeePersonalDataDTO.getToneOfTeamMail().getAnger()!=null)
                {
               	 cell = row.createCell(15);
               	 cell.setCellValue(employeePersonalDataDTO.getToneOfTeamMail().getAnger());
                }else{
               	 cell = row.createCell(15);
               	 cell.setCellValue("");
                }
                if(employeePersonalDataDTO.getToneOfTeamMail().getJoy()!=null)
                {
               	 cell = row.createCell(16);
               	 cell.setCellValue(employeePersonalDataDTO.getToneOfTeamMail().getJoy());
                }else{
               	 cell = row.createCell(16);
               	 cell.setCellValue("");
                }
                if(employeePersonalDataDTO.getToneOfTeamMail().getSadness()!=null)
                {
               	 cell = row.createCell(17);
               	 cell.setCellValue(employeePersonalDataDTO.getToneOfTeamMail().getSadness());
                }else{
               	 cell = row.createCell(17);
               	 cell.setCellValue("");
                }
                if(null != employeePersonalDataDTO.getToneOfTeamMail().getFear())
                {
               	 cell = row.createCell(18);
               	 cell.setCellValue(employeePersonalDataDTO.getToneOfTeamMail().getFear());
                }else{
               	 cell = row.createCell(18);
               	 cell.setCellValue("");
                }
                if(null != employeePersonalDataDTO.getToneOfTeamMail().getTentative())
                {
               	 cell=row.createCell(19);
               	 cell.setCellValue(employeePersonalDataDTO.getToneOfTeamMail().getTentative());
                }else{
               	 cell=row.createCell(19);
               	 cell.setCellValue("");
                }
               
               if(null!=employeePersonalDataDTO.getToneOfTeamMail().getConfident())
               {
               	cell=row.createCell(20);
               	cell.setCellValue(employeePersonalDataDTO.getToneOfTeamMail().getConfident());
               	
               }else{
               	cell=row.createCell(20);
               	cell.setCellValue("");
               }
               
               if(null!=employeePersonalDataDTO.getToneOfTeamMail().getAnalytical())
               {
               	cell=row.createCell(21);
               	cell.setCellValue(employeePersonalDataDTO.getToneOfTeamMail().getAnalytical());
               	
               }else{
               	cell=row.createCell(21);
               	cell.setCellValue("");
               }
               
               if(null!=employeePersonalDataDTO.getNoOfMail())
               {
               	cell=row.createCell(22);
               	cell.setCellValue(employeePersonalDataDTO.getNoOfMail());
               	
               }else{
               	cell=row.createCell(22);
               	cell.setCellValue("");
               }
               if(null!=employeePersonalDataDTO.getNoOfReceiveMail())
               {
               	cell=row.createCell(23);
               	cell.setCellValue(employeePersonalDataDTO.getNoOfReceiveMail());
               	
               }else{
               	cell=row.createCell(23);
               	cell.setCellValue("");
               }
               if(null!=employeePersonalDataDTO.getNoOfSentMail())
               {
               	cell=row.createCell(24);
               	cell.setCellValue(employeePersonalDataDTO.getNoOfSentMail());
               	
               }else{
               	cell=row.createCell(24);
               	cell.setCellValue("");
               }
                
               /* if(null!=employeePersonalDataDTO.getToneOfTeamMail().getConfident())
                {
                	cell=row.createCell(13);
                	cell.setCellValue(employeePersonalDataDTO.getToneOfTeamMail().getConfident());
                	
                }else{
                	cell=row.createCell(13);
                	cell.setCellValue("");
                }
                 */
                                
                
                 
                 i++;
                 rowCount ++;
                 
             }
			 
			 String filePathSave = null;
           
             if(appUtils.inProduction)
     		{
            	 filePath ="/var/lib/tomcat7/conf/MasterUploadBak/ClientList.xlsx";
     		}else{
     			//filePath ="C:\\Users\\Mahadev Sharma\\Downloads\\ClientList.xlsx";
     			
//     			filePathSave ="D:\\workspace\\kafalanalytics\\src\\main\\webapp\\images\\ClientList.xlsx";
//				 filePath = "images\\ClientList.xlsx";
     			filePathSave = "/opt/tomcat/webapps/HanogiAnalytics/ClientList.xlsx";
         		filePath = "ClientList.xlsx";
			     		}
     			
						 FileOutputStream out = new FileOutputStream(filePathSave);
             workbook.write(out);
             out.close();
                			
        }catch(Exception e)
        {
        	e.printStackTrace();
        }
		return filePath;
		
        }
	
      
	  // personal Scores Excel Data
	
	public  String generatePersonalDataToExcel(EmployeePersonalDataDTO usrData) {
        try {
        	
        	
        	 XSSFWorkbook workbook = new XSSFWorkbook(); 
             XSSFSheet spreadsheet = workbook
             .createSheet("Personal DashBoard Data");
             XSSFRow row=spreadsheet.createRow(1);
             XSSFCell cell;
             cell=row.createCell(1);
             cell.setCellValue("Serial No.");
             cell=row.createCell(2);
             cell.setCellValue("Employee Name");
             cell=row.createCell(3);
             cell.setCellValue("Designation");
             cell=row.createCell(4);
             cell.setCellValue("Department");
             cell=row.createCell(5);
             cell.setCellValue("Email Id");
             cell=row.createCell(6);
             cell.setCellValue("SelfTone_Anger");
             cell=row.createCell(7);
             cell.setCellValue("SelfTone_Joy");
             cell=row.createCell(8);
             cell.setCellValue("SelfTone_Sadness");
             cell=row.createCell(9);
             cell.setCellValue("SelfTone_Fear");
             cell=row.createCell(10);
             cell.setCellValue("SelfTone_Tentative");
             cell=row.createCell(11);
             cell.setCellValue("SelfTone_Confident");
             cell=row.createCell(12);
             cell.setCellValue("SelfTone_Analytical");
             cell=row.createCell(13);
             cell.setCellValue("TeamTone_Anger");
             cell=row.createCell(14);
             cell.setCellValue("TeamTone_Joy");
             cell=row.createCell(15);
             cell.setCellValue("TeamTone_Sadness");
             cell=row.createCell(16);
             cell.setCellValue("Client_Fear");
             cell=row.createCell(17);
             cell.setCellValue("TeamTone_Tentative");
             cell=row.createCell(18);
             cell.setCellValue("TeamTone_Confident");
             cell=row.createCell(19);
             cell.setCellValue("TeamTone_Analytical");
             cell=row.createCell(20);
             cell.setCellValue("Email Subject");
             cell=row.createCell(21);
             cell.setCellValue("Email Type");
             cell=row.createCell(22);
             cell.setCellValue("Sender Name");
             cell=row.createCell(23);
             cell.setCellValue("To Clients");
             cell=row.createCell(24);
             cell.setCellValue("To Employee");
             cell=row.createCell(25);
             cell.setCellValue("Cc Clients");
             cell=row.createCell(26);
             cell.setCellValue("Cc Employee");
             cell=row.createCell(27);
             cell.setCellValue("Date");
             cell=row.createCell(28);
             cell.setCellValue("Time");
             cell=row.createCell(29);
             cell.setCellValue("Anger");
             cell=row.createCell(30);
             cell.setCellValue("Joy");
             cell=row.createCell(31);
             cell.setCellValue("Sadness");
             cell=row.createCell(32);
             cell.setCellValue("Fear");
             cell=row.createCell(33);
             cell.setCellValue("Tentative");
             cell=row.createCell(34);
             cell.setCellValue("Confident");
             cell=row.createCell(35);
             cell.setCellValue("Analytical");
               
                         
             int i=2;
             int rowCount = 1;
             
             for(EmailPojo emailPojo : usrData.getListEmailAnalyser())
             {
            	 
            	 row=spreadsheet.createRow(i);
            	 cell=row.createCell(1);
                 cell.setCellValue(rowCount);
                 if(usrData.getEmployeeName()!=null)
                 {
                	 cell = row.createCell(2);
                	 cell.setCellValue(usrData.getEmployeeName());
                 }else{
                	 cell = row.createCell(2);
                	 cell.setCellValue("");
                 }
                 if(usrData.getDesignation()!=null)
                 {
                	 cell = row.createCell(3);
                	 cell.setCellValue(usrData.getDesignation());
                 }else{
                	 cell = row.createCell(3);
                	 cell.setCellValue("");
                 }
                 if(usrData.getDepartment()!=null)
                 {
                	 cell = row.createCell(4);
                	 cell.setCellValue(usrData.getDepartment());
                 }else{
                	 cell = row.createCell(4);
                	 cell.setCellValue("");
                 }
                 if(usrData.getEmailId()!=null)
                 {
                	 cell = row.createCell(5);
                	 cell.setCellValue(usrData.getEmailId());
                 }else{
                	 cell = row.createCell(5);
                	 cell.setCellValue("");
                 }
                 if(usrData.getToneOfPersonalMail().getAnger()!=null)
                 {
                	 cell = row.createCell(6);
                	 cell.setCellValue(usrData.getToneOfPersonalMail().getAnger());
                 }else{
                	 cell = row.createCell(6);
                	 cell.setCellValue("");
                 }
                 if(usrData.getToneOfPersonalMail().getJoy()!=null)
                 {
                	 cell = row.createCell(7);
                	 cell.setCellValue(usrData.getToneOfPersonalMail().getJoy());
                 }else{
                	 cell = row.createCell(7);
                	 cell.setCellValue("");
                 }
                 if(null != usrData.getToneOfPersonalMail().getSadness())
                 {
                	 cell = row.createCell(8);
                	 cell.setCellValue( usrData.getToneOfPersonalMail().getSadness());
                 }else{
                	 cell = row.createCell(8);
                	 cell.setCellValue("");
                 }
                 if(null !=  usrData.getToneOfPersonalMail().getFear())
                 {
                	 cell=row.createCell(9);
                	 cell.setCellValue( usrData.getToneOfPersonalMail().getFear());
                 }else{
                	 cell=row.createCell(9);
                	 cell.setCellValue("");
                 }
                
                if(null!= usrData.getToneOfPersonalMail().getTentative())
                {
                	cell=row.createCell(10);
                	cell.setCellValue( usrData.getToneOfPersonalMail().getTentative());
                	
                }else{
                	cell=row.createCell(10);
                	cell.setCellValue("");
                }
                
                if(null!= usrData.getToneOfPersonalMail().getConfident())
                {
                	cell=row.createCell(11);
                	cell.setCellValue( usrData.getToneOfPersonalMail().getConfident());
                	
                }else{
                	cell=row.createCell(11);
                	cell.setCellValue("");
                }
                
                if(null!=usrData.getToneOfPersonalMail().getAnalytical())
                {
                	cell=row.createCell(12);
                	cell.setCellValue(usrData.getToneOfPersonalMail().getAnalytical());
                	
                }else{
                	cell=row.createCell(12);
                	cell.setCellValue("");
                }
                
                if(usrData.getToneOfTeamMail().getAnger()!=null)
                {
               	 cell = row.createCell(13);
               	 cell.setCellValue(usrData.getToneOfTeamMail().getAnger());
                }else{
               	 cell = row.createCell(13);
               	 cell.setCellValue("");
                }
                if(usrData.getToneOfTeamMail().getJoy()!=null)
                {
               	 cell = row.createCell(14);
               	 cell.setCellValue(usrData.getToneOfTeamMail().getJoy());
                }else{
               	 cell = row.createCell(14);
               	 cell.setCellValue("");
                }
                if(usrData.getToneOfTeamMail().getSadness()!=null)
                {
               	 cell = row.createCell(15);
               	 cell.setCellValue(usrData.getToneOfTeamMail().getSadness());
                }else{
               	 cell = row.createCell(15);
               	 cell.setCellValue("");
                }
                if(null != usrData.getToneOfTeamMail().getFear())
                {
               	 cell = row.createCell(16);
               	 cell.setCellValue(usrData.getToneOfTeamMail().getFear());
                }else{
               	 cell = row.createCell(16);
               	 cell.setCellValue("");
                }
                if(null != usrData.getToneOfTeamMail().getTentative())
                {
               	 cell=row.createCell(17);
               	 cell.setCellValue(usrData.getToneOfTeamMail().getTentative());
                }else{
               	 cell=row.createCell(17);
               	 cell.setCellValue("");
                }
               
               if(null!=usrData.getToneOfTeamMail().getConfident())
               {
               	cell=row.createCell(18);
               	cell.setCellValue(usrData.getToneOfTeamMail().getConfident());
               	
               }else{
               	cell=row.createCell(18);
               	cell.setCellValue("");
               }
               
               if(null!=usrData.getToneOfTeamMail().getAnalytical())
               {
               	cell=row.createCell(19);
               	cell.setCellValue(usrData.getToneOfTeamMail().getAnalytical());
               	
               }else{
               	cell=row.createCell(19);
               	cell.setCellValue("");
               }
               
               if(null!=emailPojo.getSubject())
               {
               	cell=row.createCell(20);
               	cell.setCellValue(emailPojo.getSubject());
               	
               }else{
               	cell=row.createCell(20);
               	cell.setCellValue("");
               }
               if(null!=emailPojo.getType())
               {
               	cell=row.createCell(21);
               	cell.setCellValue(emailPojo.getType());
               	
               }else{
               	cell=row.createCell(21);
               	cell.setCellValue("");
               }
               if(null!=emailPojo.getSenderName())
               {
               	cell=row.createCell(22);
               	cell.setCellValue(emailPojo.getSenderName());
               	
               }else{
               	cell=row.createCell(22);
               	cell.setCellValue("");
               }
               if(emailPojo.getToClientNames().size()>0)
               {
               	cell=row.createCell(23);
               	cell.setCellValue(StringUtils.join(emailPojo.getToClientNames(),","));
               	
               }else{
               	cell=row.createCell(23);
               	cell.setCellValue("");
               }
               if(emailPojo.getToEmployeeNames().size()>0)
               {
               	cell=row.createCell(24);
               	cell.setCellValue(StringUtils.join(emailPojo.getToEmployeeNames(),","));
               	
               }else{
               	cell=row.createCell(24);
               	cell.setCellValue("");
               }
               if(emailPojo.getCcClientNames().size()>0)
               {
               	cell=row.createCell(25);
               	cell.setCellValue(StringUtils.join(emailPojo.getCcClientNames(),","));
               	
               }else{
               	cell=row.createCell(25);
               	cell.setCellValue("");
               }
               if(emailPojo.getCcEmployeeNames().size()>0)
               {
               	cell=row.createCell(26);
               	cell.setCellValue(StringUtils.join(emailPojo.getCcEmployeeNames(),","));
               	
               }else{
               	cell=row.createCell(26);
               	cell.setCellValue("");
               }
               if(null!=emailPojo.getDate())
               {
               	cell=row.createCell(27);
               	cell.setCellValue(emailPojo.getDate());
               	
               }else{
               	cell=row.createCell(27);
               	cell.setCellValue("");
               }
               if(null!=emailPojo.getTime())
               {
               	cell=row.createCell(28);
               	cell.setCellValue(emailPojo.getTime());
               	
               }else{
               	cell=row.createCell(28);
               	cell.setCellValue("");
               }
               if(emailPojo.getAnger()>0)
               {
               	cell=row.createCell(29);
               	cell.setCellValue(emailPojo.getAnger());
               	
               }else{
               	cell=row.createCell(29);
               	cell.setCellValue("");
               }
               if(emailPojo.getJoy()>0)
               {
               	cell=row.createCell(30);
               	cell.setCellValue(emailPojo.getJoy());
               	
               }else{
               	cell=row.createCell(30);
               	cell.setCellValue("");
               }
               if(emailPojo.getSadness()>0)
               {
               	cell=row.createCell(31);
               	cell.setCellValue(emailPojo.getSadness());
               	
               }else{
               	cell=row.createCell(31);
               	cell.setCellValue("");
               }
               if(emailPojo.getFear()>0)
               {
               	cell=row.createCell(32);
               	cell.setCellValue(emailPojo.getFear());
               	
               }else{
               	cell=row.createCell(32);
               	cell.setCellValue("");
               }
               if(emailPojo.getTentative()>0)
               {
               	cell=row.createCell(33);
               	cell.setCellValue(emailPojo.getTentative());
               	
               }else{
               	cell=row.createCell(33);
               	cell.setCellValue("");
               }
               if(emailPojo.getConfident()>0)
               {
               	cell=row.createCell(34);
               	cell.setCellValue(emailPojo.getConfident());
               	
               }else{
               	cell=row.createCell(34);
               	cell.setCellValue("");
               }
               if(emailPojo.getAnalytical()>0)
               {
               	cell=row.createCell(35);
               	cell.setCellValue(emailPojo.getAnalytical());
               	
               }else{
               	cell=row.createCell(35);
               	cell.setCellValue("");
               }
                
               /* if(null!=employeePersonalDataDTO.getToneOfTeamMail().getConfident())
                {
                	cell=row.createCell(13);
                	cell.setCellValue(employeePersonalDataDTO.getToneOfTeamMail().getConfident());
                	
                }else{
                	cell=row.createCell(13);
                	cell.setCellValue("");
                }
                 */
                                
                
                 
                 i++;
                 rowCount ++;
                 
             }
			 
			 String filePathSave = null;
           
             if(appUtils.inProduction)
     		{
            	 filePath ="/var/lib/tomcat7/conf/MasterUploadBak/PersonalList.xlsx";
     		}else{
     			//filePath ="C:\\Users\\Mahadev Sharma\\Downloads\\PersonalList.xlsx";
     			
//     			filePathSave ="D:\\workspace\\kafalanalytics\\src\\main\\webapp\\images\\PersonalList.xlsx";
//     			filePath = "images\\PersonalList.xlsx";
     			filePathSave = "/opt/tomcat/webapps/HanogiAnalytics/PersonalList.xlsx";
         		filePath = "PersonalList.xlsx";
     		}
     			
             FileOutputStream out = new FileOutputStream(filePathSave);
             workbook.write(out);
             out.close();
                			
        }catch(Exception e)
        {
        	e.printStackTrace();
        }
		return filePath;
		
        }
	
	
	
	
	
	 // Team Scores Excel Data
	
		public  String generateTeamDataToExcel(EmployeePersonalDataDTO usrData) {
	        try {
	        	
	        	 XSSFWorkbook workbook = new XSSFWorkbook(); 
	             XSSFSheet spreadsheet = workbook
	             .createSheet("Team DashBoard Data");
	             XSSFRow row=spreadsheet.createRow(1);
	             XSSFCell cell;
	             cell=row.createCell(1);
	             cell.setCellValue("Serial No.");
	             cell=row.createCell(2);
	             cell.setCellValue("Employee Name");
	             cell=row.createCell(3);
	             cell.setCellValue("Designation");
	             cell=row.createCell(4);
	             cell.setCellValue("Department");
	             cell=row.createCell(5);
	             cell.setCellValue("Email Id");
	             cell=row.createCell(6);
	             cell.setCellValue("SelfTone_Anger");
	             cell=row.createCell(7);
	             cell.setCellValue("SelfTone_Joy");
	             cell=row.createCell(8);
	             cell.setCellValue("SelfTone_Sadness");
	             cell=row.createCell(9);
	             cell.setCellValue("SelfTone_Fear");
	             cell=row.createCell(10);
	             cell.setCellValue("SelfTone_Tentative");
	             cell=row.createCell(11);
	             cell.setCellValue("SelfTone_Confident");
	             cell=row.createCell(12);
	             cell.setCellValue("SelfTone_Analytical");
	             cell=row.createCell(13);
	             cell.setCellValue("TeamTone_Anger");
	             cell=row.createCell(14);
	             cell.setCellValue("TeamTone_Joy");
	             cell=row.createCell(15);
	             cell.setCellValue("TeamTone_Sadness");
	             cell=row.createCell(16);
	             cell.setCellValue("Client_Fear");
	             cell=row.createCell(17);
	             cell.setCellValue("TeamTone_Tentative");
	             cell=row.createCell(18);
	             cell.setCellValue("TeamTone_Confident");
	             cell=row.createCell(19);
	             cell.setCellValue("TeamTone_Analytical");
	             cell=row.createCell(20);
	             cell.setCellValue("Email Subject");
	             cell=row.createCell(21);
	             cell.setCellValue("Email Type");
	             cell=row.createCell(22);
	             cell.setCellValue("Sender Name");
	             cell=row.createCell(23);
	             cell.setCellValue("To Clients");
	             cell=row.createCell(24);
	             cell.setCellValue("To Employee");
	             cell=row.createCell(25);
	             cell.setCellValue("Cc Clients");
	             cell=row.createCell(26);
	             cell.setCellValue("Cc Employee");
	             cell=row.createCell(27);
	             cell.setCellValue("Date");
	             cell=row.createCell(28);
	             cell.setCellValue("Time");
	             cell=row.createCell(29);
	             cell.setCellValue("Anger");
	             cell=row.createCell(30);
	             cell.setCellValue("Joy");
	             cell=row.createCell(31);
	             cell.setCellValue("Sadness");
	             cell=row.createCell(32);
	             cell.setCellValue("Fear");
	             cell=row.createCell(33);
	             cell.setCellValue("Tentative");
	             cell=row.createCell(34);
	             cell.setCellValue("Confident");
	             cell=row.createCell(35);
	             cell.setCellValue("Analytical");
	               
	                         
	             int i=2;
	             int rowCount = 1;
	             
	             for(EmailPojo emailPojo : usrData.getListEmailAnalyser())
	             {
	            	 
	            	 row=spreadsheet.createRow(i);
	            	 cell=row.createCell(1);
	                 cell.setCellValue(rowCount);
	                 if(usrData.getEmployeeName()!=null)
	                 {
	                	 cell = row.createCell(2);
	                	 cell.setCellValue(usrData.getEmployeeName());
	                 }else{
	                	 cell = row.createCell(2);
	                	 cell.setCellValue("");
	                 }
	                 if(usrData.getDesignation()!=null)
	                 {
	                	 cell = row.createCell(3);
	                	 cell.setCellValue(usrData.getDesignation());
	                 }else{
	                	 cell = row.createCell(3);
	                	 cell.setCellValue("");
	                 }
	                 if(usrData.getDepartment()!=null)
	                 {
	                	 cell = row.createCell(4);
	                	 cell.setCellValue(usrData.getDepartment());
	                 }else{
	                	 cell = row.createCell(4);
	                	 cell.setCellValue("");
	                 }
	                 if(usrData.getEmailId()!=null)
	                 {
	                	 cell = row.createCell(5);
	                	 cell.setCellValue(usrData.getEmailId());
	                 }else{
	                	 cell = row.createCell(5);
	                	 cell.setCellValue("");
	                 }
	                 if(usrData.getToneOfPersonalMail().getAnger()!=null)
	                 {
	                	 cell = row.createCell(6);
	                	 cell.setCellValue(usrData.getToneOfPersonalMail().getAnger());
	                 }else{
	                	 cell = row.createCell(6);
	                	 cell.setCellValue("");
	                 }
	                 if(usrData.getToneOfPersonalMail().getJoy()!=null)
	                 {
	                	 cell = row.createCell(7);
	                	 cell.setCellValue(usrData.getToneOfPersonalMail().getJoy());
	                 }else{
	                	 cell = row.createCell(7);
	                	 cell.setCellValue("");
	                 }
	                 if(null != usrData.getToneOfPersonalMail().getSadness())
	                 {
	                	 cell = row.createCell(8);
	                	 cell.setCellValue( usrData.getToneOfPersonalMail().getSadness());
	                 }else{
	                	 cell = row.createCell(8);
	                	 cell.setCellValue("");
	                 }
	                 if(null !=  usrData.getToneOfPersonalMail().getFear())
	                 {
	                	 cell=row.createCell(9);
	                	 cell.setCellValue( usrData.getToneOfPersonalMail().getFear());
	                 }else{
	                	 cell=row.createCell(9);
	                	 cell.setCellValue("");
	                 }
	                
	                if(null!= usrData.getToneOfPersonalMail().getTentative())
	                {
	                	cell=row.createCell(10);
	                	cell.setCellValue( usrData.getToneOfPersonalMail().getTentative());
	                	
	                }else{
	                	cell=row.createCell(10);
	                	cell.setCellValue("");
	                }
	                
	                if(null!= usrData.getToneOfPersonalMail().getConfident())
	                {
	                	cell=row.createCell(11);
	                	cell.setCellValue( usrData.getToneOfPersonalMail().getConfident());
	                	
	                }else{
	                	cell=row.createCell(11);
	                	cell.setCellValue("");
	                }
	                
	                if(null!=usrData.getToneOfPersonalMail().getAnalytical())
	                {
	                	cell=row.createCell(12);
	                	cell.setCellValue(usrData.getToneOfPersonalMail().getAnalytical());
	                	
	                }else{
	                	cell=row.createCell(12);
	                	cell.setCellValue("");
	                }
	                
	                if(usrData.getToneOfTeamMail().getAnger()!=null)
	                {
	               	 cell = row.createCell(13);
	               	 cell.setCellValue(usrData.getToneOfTeamMail().getAnger());
	                }else{
	               	 cell = row.createCell(13);
	               	 cell.setCellValue("");
	                }
	                if(usrData.getToneOfTeamMail().getJoy()!=null)
	                {
	               	 cell = row.createCell(14);
	               	 cell.setCellValue(usrData.getToneOfTeamMail().getJoy());
	                }else{
	               	 cell = row.createCell(14);
	               	 cell.setCellValue("");
	                }
	                if(usrData.getToneOfTeamMail().getSadness()!=null)
	                {
	               	 cell = row.createCell(15);
	               	 cell.setCellValue(usrData.getToneOfTeamMail().getSadness());
	                }else{
	               	 cell = row.createCell(15);
	               	 cell.setCellValue("");
	                }
	                if(null != usrData.getToneOfTeamMail().getFear())
	                {
	               	 cell = row.createCell(16);
	               	 cell.setCellValue(usrData.getToneOfTeamMail().getFear());
	                }else{
	               	 cell = row.createCell(16);
	               	 cell.setCellValue("");
	                }
	                if(null != usrData.getToneOfTeamMail().getTentative())
	                {
	               	 cell=row.createCell(17);
	               	 cell.setCellValue(usrData.getToneOfTeamMail().getTentative());
	                }else{
	               	 cell=row.createCell(17);
	               	 cell.setCellValue("");
	                }
	               
	               if(null!=usrData.getToneOfTeamMail().getConfident())
	               {
	               	cell=row.createCell(18);
	               	cell.setCellValue(usrData.getToneOfTeamMail().getConfident());
	               	
	               }else{
	               	cell=row.createCell(18);
	               	cell.setCellValue("");
	               }
	               
	               if(null!=usrData.getToneOfTeamMail().getAnalytical())
	               {
	               	cell=row.createCell(19);
	               	cell.setCellValue(usrData.getToneOfTeamMail().getAnalytical());
	               	
	               }else{
	               	cell=row.createCell(19);
	               	cell.setCellValue("");
	               }
	               
	               if(null!=emailPojo.getSubject())
	               {
	               	cell=row.createCell(20);
	               	cell.setCellValue(emailPojo.getSubject());
	               	
	               }else{
	               	cell=row.createCell(20);
	               	cell.setCellValue("");
	               }
	               if(null!=emailPojo.getType())
	               {
	               	cell=row.createCell(21);
	               	cell.setCellValue(emailPojo.getType());
	               	
	               }else{
	               	cell=row.createCell(21);
	               	cell.setCellValue("");
	               }
	               if(null!=emailPojo.getSenderName())
	               {
	               	cell=row.createCell(22);
	               	cell.setCellValue(emailPojo.getSenderName());
	               	
	               }else{
	               	cell=row.createCell(22);
	               	cell.setCellValue("");
	               }
	               if(emailPojo.getToClientNames().size()>0)
	               {
	               	cell=row.createCell(23);
	               	cell.setCellValue(StringUtils.join(emailPojo.getToClientNames(),","));
	               	
	               }else{
	               	cell=row.createCell(23);
	               	cell.setCellValue("");
	               }
	               if(emailPojo.getToEmployeeNames().size()>0)
	               {
	               	cell=row.createCell(24);
	               	cell.setCellValue(StringUtils.join(emailPojo.getToEmployeeNames(),","));
	               	
	               }else{
	               	cell=row.createCell(24);
	               	cell.setCellValue("");
	               }
	               if(emailPojo.getCcClientNames().size()>0)
	               {
	               	cell=row.createCell(25);
	               	cell.setCellValue(StringUtils.join(emailPojo.getCcClientNames(),","));
	               	
	               }else{
	               	cell=row.createCell(25);
	               	cell.setCellValue("");
	               }
	               if(emailPojo.getCcEmployeeNames().size()>0)
	               {
	               	cell=row.createCell(26);
	               	cell.setCellValue(StringUtils.join(emailPojo.getCcEmployeeNames(),","));
	               	
	               }else{
	               	cell=row.createCell(26);
	               	cell.setCellValue("");
	               }
	               if(null!=emailPojo.getDate())
	               {
	               	cell=row.createCell(27);
	               	cell.setCellValue(emailPojo.getDate());
	               	
	               }else{
	               	cell=row.createCell(27);
	               	cell.setCellValue("");
	               }
	               if(null!=emailPojo.getTime())
	               {
	               	cell=row.createCell(28);
	               	cell.setCellValue(emailPojo.getTime());
	               	
	               }else{
	               	cell=row.createCell(28);
	               	cell.setCellValue("");
	               }
	               if(emailPojo.getAnger()>0)
	               {
	               	cell=row.createCell(29);
	               	cell.setCellValue(emailPojo.getAnger());
	               	
	               }else{
	               	cell=row.createCell(29);
	               	cell.setCellValue("");
	               }
	               if(emailPojo.getJoy()>0)
	               {
	               	cell=row.createCell(30);
	               	cell.setCellValue(emailPojo.getJoy());
	               	
	               }else{
	               	cell=row.createCell(30);
	               	cell.setCellValue("");
	               }
	               if(emailPojo.getSadness()>0)
	               {
	               	cell=row.createCell(31);
	               	cell.setCellValue(emailPojo.getSadness());
	               	
	               }else{
	               	cell=row.createCell(31);
	               	cell.setCellValue("");
	               }
	               if(emailPojo.getFear()>0)
	               {
	               	cell=row.createCell(32);
	               	cell.setCellValue(emailPojo.getFear());
	               	
	               }else{
	               	cell=row.createCell(32);
	               	cell.setCellValue("");
	               }
	               if(emailPojo.getTentative()>0)
	               {
	               	cell=row.createCell(33);
	               	cell.setCellValue(emailPojo.getTentative());
	               	
	               }else{
	               	cell=row.createCell(33);
	               	cell.setCellValue("");
	               }
	               if(emailPojo.getConfident()>0)
	               {
	               	cell=row.createCell(34);
	               	cell.setCellValue(emailPojo.getConfident());
	               	
	               }else{
	               	cell=row.createCell(34);
	               	cell.setCellValue("");
	               }
	               if(emailPojo.getAnalytical()>0)
	               {
	               	cell=row.createCell(35);
	               	cell.setCellValue(emailPojo.getAnalytical());
	               	
	               }else{
	               	cell=row.createCell(35);
	               	cell.setCellValue("");
	               }
	                
	               /* if(null!=employeePersonalDataDTO.getToneOfTeamMail().getConfident())
	                {
	                	cell=row.createCell(13);
	                	cell.setCellValue(employeePersonalDataDTO.getToneOfTeamMail().getConfident());
	                	
	                }else{
	                	cell=row.createCell(13);
	                	cell.setCellValue("");
	                }
	                 */
	                                
	                
	                 
	                 i++;
	                 rowCount ++;
	                 
	             }
	             
				 String filePathSave = null;
	             if(appUtils.inProduction)
	     		{
	            	 filePath ="/var/lib/tomcat7/conf/MasterUploadBak/TeamList.xlsx";
	            	 
	     		}else{
	     			
	     		//	filePath ="C:\\Users\\Mahadev Sharma\\Downloads\\TeamList.xlsx";
	     			
//				 filePathSave ="D:\\workspace\\kafalanalytics\\src\\main\\webapp\\images\\TeamList.xlsx";
//				 filePath = "images\\TeamList.xlsx";
	     			filePathSave = "/opt/tomcat/webapps/HanogiAnalytics/TeamList.xlsx";
	         		filePath = "TeamList.xlsx";
				 
	     		}
	     			
	             FileOutputStream out = new FileOutputStream(filePathSave);
	             workbook.write(out);
	             out.close();
	                			
	        }catch(Exception e)
	        {
	        	e.printStackTrace();
	        }
			return filePath;
			
	        }
		
		public  String generatePersonalTeamDataToExcel(EmployeePersonalDataDTO usrData,EmployeePersonalDataDTO usrData2) {
	        try {
	        	
	        	 XSSFWorkbook workbook = new XSSFWorkbook(); 
	             XSSFSheet spreadsheet = workbook
	             .createSheet("Personal Team DashBoard Data");
	             XSSFRow row=spreadsheet.createRow(1);
	             XSSFCell cell;
	             cell=row.createCell(1);
	             cell.setCellValue("Serial No.");
	             cell=row.createCell(2);
	             cell.setCellValue("Employee Name");
	             cell=row.createCell(3);
	             cell.setCellValue("Designation");
	             cell=row.createCell(4);
	             cell.setCellValue("Department");
	             cell=row.createCell(5);
	             cell.setCellValue("Email Id");
	             cell=row.createCell(6);
	             cell.setCellValue("SelfTone_Anger");
	             cell=row.createCell(7);
	             cell.setCellValue("SelfTone_Joy");
	             cell=row.createCell(8);
	             cell.setCellValue("SelfTone_Sadness");
	             cell=row.createCell(9);
	             cell.setCellValue("SelfTone_Fear");
	             cell=row.createCell(10);
	             cell.setCellValue("SelfTone_Tentative");
	             cell=row.createCell(11);
	             cell.setCellValue("SelfTone_Confident");
	             cell=row.createCell(12);
	             cell.setCellValue("SelfTone_Analytical");
	             cell=row.createCell(13);
	             cell.setCellValue("TeamTone_Anger");
	             cell=row.createCell(14);
	             cell.setCellValue("TeamTone_Joy");
	             cell=row.createCell(15);
	             cell.setCellValue("TeamTone_Sadness");
	             cell=row.createCell(16);
	             cell.setCellValue("Client_Fear");
	             cell=row.createCell(17);
	             cell.setCellValue("TeamTone_Tentative");
	             cell=row.createCell(18);
	             cell.setCellValue("TeamTone_Confident");
	             cell=row.createCell(19);
	             cell.setCellValue("TeamTone_Analytical");
	             cell=row.createCell(20);
	             cell.setCellValue("Email Subject");
	             cell=row.createCell(21);
	             cell.setCellValue("Email Type");
	             cell=row.createCell(22);
	             cell.setCellValue("Sender Name");
	             cell=row.createCell(23);
	             cell.setCellValue("To Clients");
	             cell=row.createCell(24);
	             cell.setCellValue("To Employee");
	             cell=row.createCell(25);
	             cell.setCellValue("Cc Clients");
	             cell=row.createCell(26);
	             cell.setCellValue("Cc Employee");
	             cell=row.createCell(27);
	             cell.setCellValue("Date");
	             cell=row.createCell(28);
	             cell.setCellValue("Time");
	             cell=row.createCell(29);
	             cell.setCellValue("Anger");
	             cell=row.createCell(30);
	             cell.setCellValue("Joy");
	             cell=row.createCell(31);
	             cell.setCellValue("Sadness");
	             cell=row.createCell(32);
	             cell.setCellValue("Fear");
	             cell=row.createCell(33);
	             cell.setCellValue("Tentative");
	             cell=row.createCell(34);
	             cell.setCellValue("Confident");
	             cell=row.createCell(35);
	             cell.setCellValue("Analytical");
	               
	                         
	             int i=2;
	             int rowCount = 1;
	             
	             for(EmailPojo emailPojo : usrData.getListEmailAnalyser())
	             {
	            	 
	            	 row=spreadsheet.createRow(i);
	            	 cell=row.createCell(1);
	                 cell.setCellValue(rowCount);
	                 if(usrData.getEmployeeName()!=null)
	                 {
	                	 cell = row.createCell(2);
	                	 cell.setCellValue(usrData.getEmployeeName());
	                 }else{
	                	 cell = row.createCell(2);
	                	 cell.setCellValue("");
	                 }
	                 if(usrData.getDesignation()!=null)
	                 {
	                	 cell = row.createCell(3);
	                	 cell.setCellValue(usrData.getDesignation());
	                 }else{
	                	 cell = row.createCell(3);
	                	 cell.setCellValue("");
	                 }
	                 if(usrData.getDepartment()!=null)
	                 {
	                	 cell = row.createCell(4);
	                	 cell.setCellValue(usrData.getDepartment());
	                 }else{
	                	 cell = row.createCell(4);
	                	 cell.setCellValue("");
	                 }
	                 if(usrData.getEmailId()!=null)
	                 {
	                	 cell = row.createCell(5);
	                	 cell.setCellValue(usrData.getEmailId());
	                 }else{
	                	 cell = row.createCell(5);
	                	 cell.setCellValue("");
	                 }
	                 if(usrData.getToneOfPersonalMail().getAnger()!=null)
	                 {
	                	 cell = row.createCell(6);
	                	 cell.setCellValue(usrData.getToneOfPersonalMail().getAnger());
	                 }else{
	                	 cell = row.createCell(6);
	                	 cell.setCellValue("");
	                 }
	                 if(usrData.getToneOfPersonalMail().getJoy()!=null)
	                 {
	                	 cell = row.createCell(7);
	                	 cell.setCellValue(usrData.getToneOfPersonalMail().getJoy());
	                 }else{
	                	 cell = row.createCell(7);
	                	 cell.setCellValue("");
	                 }
	                 if(null != usrData.getToneOfPersonalMail().getSadness())
	                 {
	                	 cell = row.createCell(8);
	                	 cell.setCellValue( usrData.getToneOfPersonalMail().getSadness());
	                 }else{
	                	 cell = row.createCell(8);
	                	 cell.setCellValue("");
	                 }
	                 if(null !=  usrData.getToneOfPersonalMail().getFear())
	                 {
	                	 cell=row.createCell(9);
	                	 cell.setCellValue( usrData.getToneOfPersonalMail().getFear());
	                 }else{
	                	 cell=row.createCell(9);
	                	 cell.setCellValue("");
	                 }
	                
	                if(null!= usrData.getToneOfPersonalMail().getTentative())
	                {
	                	cell=row.createCell(10);
	                	cell.setCellValue( usrData.getToneOfPersonalMail().getTentative());
	                	
	                }else{
	                	cell=row.createCell(10);
	                	cell.setCellValue("");
	                }
	                
	                if(null!= usrData.getToneOfPersonalMail().getConfident())
	                {
	                	cell=row.createCell(11);
	                	cell.setCellValue( usrData.getToneOfPersonalMail().getConfident());
	                	
	                }else{
	                	cell=row.createCell(11);
	                	cell.setCellValue("");
	                }
	                
	                if(null!=usrData.getToneOfPersonalMail().getAnalytical())
	                {
	                	cell=row.createCell(12);
	                	cell.setCellValue(usrData.getToneOfPersonalMail().getAnalytical());
	                	
	                }else{
	                	cell=row.createCell(12);
	                	cell.setCellValue("");
	                }
	                
	                if(usrData.getToneOfTeamMail().getAnger()!=null)
	                {
	               	 cell = row.createCell(13);
	               	 cell.setCellValue(usrData.getToneOfTeamMail().getAnger());
	                }else{
	               	 cell = row.createCell(13);
	               	 cell.setCellValue("");
	                }
	                if(usrData.getToneOfTeamMail().getJoy()!=null)
	                {
	               	 cell = row.createCell(14);
	               	 cell.setCellValue(usrData.getToneOfTeamMail().getJoy());
	                }else{
	               	 cell = row.createCell(14);
	               	 cell.setCellValue("");
	                }
	                if(usrData.getToneOfTeamMail().getSadness()!=null)
	                {
	               	 cell = row.createCell(15);
	               	 cell.setCellValue(usrData.getToneOfTeamMail().getSadness());
	                }else{
	               	 cell = row.createCell(15);
	               	 cell.setCellValue("");
	                }
	                if(null != usrData.getToneOfTeamMail().getFear())
	                {
	               	 cell = row.createCell(16);
	               	 cell.setCellValue(usrData.getToneOfTeamMail().getFear());
	                }else{
	               	 cell = row.createCell(16);
	               	 cell.setCellValue("");
	                }
	                if(null != usrData.getToneOfTeamMail().getTentative())
	                {
	               	 cell=row.createCell(17);
	               	 cell.setCellValue(usrData.getToneOfTeamMail().getTentative());
	                }else{
	               	 cell=row.createCell(17);
	               	 cell.setCellValue("");
	                }
	               
	               if(null!=usrData.getToneOfTeamMail().getConfident())
	               {
	               	cell=row.createCell(18);
	               	cell.setCellValue(usrData.getToneOfTeamMail().getConfident());
	               	
	               }else{
	               	cell=row.createCell(18);
	               	cell.setCellValue("");
	               }
	               
	               if(null!=usrData.getToneOfTeamMail().getAnalytical())
	               {
	               	cell=row.createCell(19);
	               	cell.setCellValue(usrData.getToneOfTeamMail().getAnalytical());
	               	
	               }else{
	               	cell=row.createCell(19);
	               	cell.setCellValue("");
	               }
	               
	               if(null!=emailPojo.getSubject())
	               {
	               	cell=row.createCell(20);
	               	cell.setCellValue(emailPojo.getSubject());
	               	
	               }else{
	               	cell=row.createCell(20);
	               	cell.setCellValue("");
	               }
	               if(null!=emailPojo.getType())
	               {
	               	cell=row.createCell(21);
	               	cell.setCellValue(emailPojo.getType());
	               	
	               }else{
	               	cell=row.createCell(21);
	               	cell.setCellValue("");
	               }
	               if(null!=emailPojo.getSenderName())
	               {
	               	cell=row.createCell(22);
	               	cell.setCellValue(emailPojo.getSenderName());
	               	
	               }else{
	               	cell=row.createCell(22);
	               	cell.setCellValue("");
	               }
	               if(emailPojo.getToClientNames().size()>0)
	               {
	               	cell=row.createCell(23);
	               	cell.setCellValue(StringUtils.join(emailPojo.getToClientNames(),","));
	               	
	               }else{
	               	cell=row.createCell(23);
	               	cell.setCellValue("");
	               }
	               if(emailPojo.getToEmployeeNames().size()>0)
	               {
	               	cell=row.createCell(24);
	               	cell.setCellValue(StringUtils.join(emailPojo.getToEmployeeNames(),","));
	               	
	               }else{
	               	cell=row.createCell(24);
	               	cell.setCellValue("");
	               }
	               if(emailPojo.getCcClientNames().size()>0)
	               {
	               	cell=row.createCell(25);
	               	cell.setCellValue(StringUtils.join(emailPojo.getCcClientNames(),","));
	               	
	               }else{
	               	cell=row.createCell(25);
	               	cell.setCellValue("");
	               }
	               if(emailPojo.getCcEmployeeNames().size()>0)
	               {
	               	cell=row.createCell(26);
	               	cell.setCellValue(StringUtils.join(emailPojo.getCcEmployeeNames(),","));
	               	
	               }else{
	               	cell=row.createCell(26);
	               	cell.setCellValue("");
	               }
	               if(null!=emailPojo.getDate())
	               {
	               	cell=row.createCell(27);
	               	cell.setCellValue(emailPojo.getDate());
	               	
	               }else{
	               	cell=row.createCell(27);
	               	cell.setCellValue("");
	               }
	               if(null!=emailPojo.getTime())
	               {
	               	cell=row.createCell(28);
	               	cell.setCellValue(emailPojo.getTime());
	               	
	               }else{
	               	cell=row.createCell(28);
	               	cell.setCellValue("");
	               }
	               if(emailPojo.getAnger()>0)
	               {
	               	cell=row.createCell(29);
	               	cell.setCellValue(emailPojo.getAnger());
	               	
	               }else{
	               	cell=row.createCell(29);
	               	cell.setCellValue("");
	               }
	               if(emailPojo.getJoy()>0)
	               {
	               	cell=row.createCell(30);
	               	cell.setCellValue(emailPojo.getJoy());
	               	
	               }else{
	               	cell=row.createCell(30);
	               	cell.setCellValue("");
	               }
	               if(emailPojo.getSadness()>0)
	               {
	               	cell=row.createCell(31);
	               	cell.setCellValue(emailPojo.getSadness());
	               	
	               }else{
	               	cell=row.createCell(31);
	               	cell.setCellValue("");
	               }
	               if(emailPojo.getFear()>0)
	               {
	               	cell=row.createCell(32);
	               	cell.setCellValue(emailPojo.getFear());
	               	
	               }else{
	               	cell=row.createCell(32);
	               	cell.setCellValue("");
	               }
	               if(emailPojo.getTentative()>0)
	               {
	               	cell=row.createCell(33);
	               	cell.setCellValue(emailPojo.getTentative());
	               	
	               }else{
	               	cell=row.createCell(33);
	               	cell.setCellValue("");
	               }
	               if(emailPojo.getConfident()>0)
	               {
	               	cell=row.createCell(34);
	               	cell.setCellValue(emailPojo.getConfident());
	               	
	               }else{
	               	cell=row.createCell(34);
	               	cell.setCellValue("");
	               }
	               if(emailPojo.getAnalytical()>0)
	               {
	               	cell=row.createCell(35);
	               	cell.setCellValue(emailPojo.getAnalytical());
	               	
	               }else{
	               	cell=row.createCell(35);
	               	cell.setCellValue("");
	               }
	                
	               /* if(null!=employeePersonalDataDTO.getToneOfTeamMail().getConfident())
	                {
	                	cell=row.createCell(13);
	                	cell.setCellValue(employeePersonalDataDTO.getToneOfTeamMail().getConfident());
	                	
	                }else{
	                	cell=row.createCell(13);
	                	cell.setCellValue("");
	                }
	                 */
	                                
	                
	                 
	                 i++;
	                 rowCount ++;
	                 
	             }
	             
//	             for(EmailPojo emailPojo : usrData2.getListEmailAnalyser())
//	             {
//	            	 
//	            	 row=spreadsheet.createRow(i);
//	            	 cell=row.createCell(1);
//	                 cell.setCellValue(rowCount);
//	                 if(usrData.getEmployeeName()!=null)
//	                 {
//	                	 cell = row.createCell(2);
//	                	 cell.setCellValue(usrData.getEmployeeName());
//	                 }else{
//	                	 cell = row.createCell(2);
//	                	 cell.setCellValue("");
//	                 }
//	                 if(usrData.getDesignation()!=null)
//	                 {
//	                	 cell = row.createCell(3);
//	                	 cell.setCellValue(usrData.getDesignation());
//	                 }else{
//	                	 cell = row.createCell(3);
//	                	 cell.setCellValue("");
//	                 }
//	                 if(usrData.getDepartment()!=null)
//	                 {
//	                	 cell = row.createCell(4);
//	                	 cell.setCellValue(usrData.getDepartment());
//	                 }else{
//	                	 cell = row.createCell(4);
//	                	 cell.setCellValue("");
//	                 }
//	                 if(usrData.getEmailId()!=null)
//	                 {
//	                	 cell = row.createCell(5);
//	                	 cell.setCellValue(usrData.getEmailId());
//	                 }else{
//	                	 cell = row.createCell(5);
//	                	 cell.setCellValue("");
//	                 }
//	                 if(usrData.getToneOfPersonalMail().getAnger()!=null)
//	                 {
//	                	 cell = row.createCell(6);
//	                	 cell.setCellValue(usrData.getToneOfPersonalMail().getAnger());
//	                 }else{
//	                	 cell = row.createCell(6);
//	                	 cell.setCellValue("");
//	                 }
//	                 if(usrData.getToneOfPersonalMail().getJoy()!=null)
//	                 {
//	                	 cell = row.createCell(7);
//	                	 cell.setCellValue(usrData.getToneOfPersonalMail().getJoy());
//	                 }else{
//	                	 cell = row.createCell(7);
//	                	 cell.setCellValue("");
//	                 }
//	                 if(null != usrData.getToneOfPersonalMail().getSadness())
//	                 {
//	                	 cell = row.createCell(8);
//	                	 cell.setCellValue( usrData.getToneOfPersonalMail().getSadness());
//	                 }else{
//	                	 cell = row.createCell(8);
//	                	 cell.setCellValue("");
//	                 }
//	                 if(null !=  usrData.getToneOfPersonalMail().getFear())
//	                 {
//	                	 cell=row.createCell(9);
//	                	 cell.setCellValue( usrData.getToneOfPersonalMail().getFear());
//	                 }else{
//	                	 cell=row.createCell(9);
//	                	 cell.setCellValue("");
//	                 }
//	                
//	                if(null!= usrData.getToneOfPersonalMail().getTentative())
//	                {
//	                	cell=row.createCell(10);
//	                	cell.setCellValue( usrData.getToneOfPersonalMail().getTentative());
//	                	
//	                }else{
//	                	cell=row.createCell(10);
//	                	cell.setCellValue("");
//	                }
//	                
//	                if(null!= usrData.getToneOfPersonalMail().getConfident())
//	                {
//	                	cell=row.createCell(11);
//	                	cell.setCellValue( usrData.getToneOfPersonalMail().getConfident());
//	                	
//	                }else{
//	                	cell=row.createCell(11);
//	                	cell.setCellValue("");
//	                }
//	                
//	                if(null!=usrData.getToneOfPersonalMail().getAnalytical())
//	                {
//	                	cell=row.createCell(12);
//	                	cell.setCellValue(usrData.getToneOfPersonalMail().getAnalytical());
//	                	
//	                }else{
//	                	cell=row.createCell(12);
//	                	cell.setCellValue("");
//	                }
//	                
//	                if(usrData.getToneOfTeamMail().getAnger()!=null)
//	                {
//	               	 cell = row.createCell(13);
//	               	 cell.setCellValue(usrData.getToneOfTeamMail().getAnger());
//	                }else{
//	               	 cell = row.createCell(13);
//	               	 cell.setCellValue("");
//	                }
//	                if(usrData.getToneOfTeamMail().getJoy()!=null)
//	                {
//	               	 cell = row.createCell(14);
//	               	 cell.setCellValue(usrData.getToneOfTeamMail().getJoy());
//	                }else{
//	               	 cell = row.createCell(14);
//	               	 cell.setCellValue("");
//	                }
//	                if(usrData.getToneOfTeamMail().getSadness()!=null)
//	                {
//	               	 cell = row.createCell(15);
//	               	 cell.setCellValue(usrData.getToneOfTeamMail().getSadness());
//	                }else{
//	               	 cell = row.createCell(15);
//	               	 cell.setCellValue("");
//	                }
//	                if(null != usrData.getToneOfTeamMail().getFear())
//	                {
//	               	 cell = row.createCell(16);
//	               	 cell.setCellValue(usrData.getToneOfTeamMail().getFear());
//	                }else{
//	               	 cell = row.createCell(16);
//	               	 cell.setCellValue("");
//	                }
//	                if(null != usrData.getToneOfTeamMail().getTentative())
//	                {
//	               	 cell=row.createCell(17);
//	               	 cell.setCellValue(usrData.getToneOfTeamMail().getTentative());
//	                }else{
//	               	 cell=row.createCell(17);
//	               	 cell.setCellValue("");
//	                }
//	               
//	               if(null!=usrData.getToneOfTeamMail().getConfident())
//	               {
//	               	cell=row.createCell(18);
//	               	cell.setCellValue(usrData.getToneOfTeamMail().getConfident());
//	               	
//	               }else{
//	               	cell=row.createCell(18);
//	               	cell.setCellValue("");
//	               }
//	               
//	               if(null!=usrData.getToneOfTeamMail().getAnalytical())
//	               {
//	               	cell=row.createCell(19);
//	               	cell.setCellValue(usrData.getToneOfTeamMail().getAnalytical());
//	               	
//	               }else{
//	               	cell=row.createCell(19);
//	               	cell.setCellValue("");
//	               }
//	               
//	               if(null!=emailPojo.getSubject())
//	               {
//	               	cell=row.createCell(20);
//	               	cell.setCellValue(emailPojo.getSubject());
//	               	
//	               }else{
//	               	cell=row.createCell(20);
//	               	cell.setCellValue("");
//	               }
//	               if(null!=emailPojo.getType())
//	               {
//	               	cell=row.createCell(21);
//	               	cell.setCellValue(emailPojo.getType());
//	               	
//	               }else{
//	               	cell=row.createCell(21);
//	               	cell.setCellValue("");
//	               }
//	               if(null!=emailPojo.getSenderName())
//	               {
//	               	cell=row.createCell(22);
//	               	cell.setCellValue(emailPojo.getSenderName());
//	               	
//	               }else{
//	               	cell=row.createCell(22);
//	               	cell.setCellValue("");
//	               }
//	               if(emailPojo.getToClientNames().size()>0)
//	               {
//	               	cell=row.createCell(23);
//	               	cell.setCellValue(StringUtils.join(emailPojo.getToClientNames(),","));
//	               	
//	               }else{
//	               	cell=row.createCell(23);
//	               	cell.setCellValue("");
//	               }
//	               if(emailPojo.getToEmployeeNames().size()>0)
//	               {
//	               	cell=row.createCell(24);
//	               	cell.setCellValue(StringUtils.join(emailPojo.getToEmployeeNames(),","));
//	               	
//	               }else{
//	               	cell=row.createCell(24);
//	               	cell.setCellValue("");
//	               }
//	               if(emailPojo.getCcClientNames().size()>0)
//	               {
//	               	cell=row.createCell(25);
//	               	cell.setCellValue(StringUtils.join(emailPojo.getCcClientNames(),","));
//	               	
//	               }else{
//	               	cell=row.createCell(25);
//	               	cell.setCellValue("");
//	               }
//	               if(emailPojo.getCcEmployeeNames().size()>0)
//	               {
//	               	cell=row.createCell(26);
//	               	cell.setCellValue(StringUtils.join(emailPojo.getCcEmployeeNames(),","));
//	               	
//	               }else{
//	               	cell=row.createCell(26);
//	               	cell.setCellValue("");
//	               }
//	               if(null!=emailPojo.getDate())
//	               {
//	               	cell=row.createCell(27);
//	               	cell.setCellValue(emailPojo.getDate());
//	               	
//	               }else{
//	               	cell=row.createCell(27);
//	               	cell.setCellValue("");
//	               }
//	               if(null!=emailPojo.getTime())
//	               {
//	               	cell=row.createCell(28);
//	               	cell.setCellValue(emailPojo.getTime());
//	               	
//	               }else{
//	               	cell=row.createCell(28);
//	               	cell.setCellValue("");
//	               }
//	               if(emailPojo.getAnger()>0)
//	               {
//	               	cell=row.createCell(29);
//	               	cell.setCellValue(emailPojo.getAnger());
//	               	
//	               }else{
//	               	cell=row.createCell(29);
//	               	cell.setCellValue("");
//	               }
//	               if(emailPojo.getJoy()>0)
//	               {
//	               	cell=row.createCell(30);
//	               	cell.setCellValue(emailPojo.getJoy());
//	               	
//	               }else{
//	               	cell=row.createCell(30);
//	               	cell.setCellValue("");
//	               }
//	               if(emailPojo.getSadness()>0)
//	               {
//	               	cell=row.createCell(31);
//	               	cell.setCellValue(emailPojo.getSadness());
//	               	
//	               }else{
//	               	cell=row.createCell(31);
//	               	cell.setCellValue("");
//	               }
//	               if(emailPojo.getFear()>0)
//	               {
//	               	cell=row.createCell(32);
//	               	cell.setCellValue(emailPojo.getFear());
//	               	
//	               }else{
//	               	cell=row.createCell(32);
//	               	cell.setCellValue("");
//	               }
//	               if(emailPojo.getTentative()>0)
//	               {
//	               	cell=row.createCell(33);
//	               	cell.setCellValue(emailPojo.getTentative());
//	               	
//	               }else{
//	               	cell=row.createCell(33);
//	               	cell.setCellValue("");
//	               }
//	               if(emailPojo.getConfident()>0)
//	               {
//	               	cell=row.createCell(34);
//	               	cell.setCellValue(emailPojo.getConfident());
//	               	
//	               }else{
//	               	cell=row.createCell(34);
//	               	cell.setCellValue("");
//	               }
//	               if(emailPojo.getAnalytical()>0)
//	               {
//	               	cell=row.createCell(35);
//	               	cell.setCellValue(emailPojo.getAnalytical());
//	               	
//	               }else{
//	               	cell=row.createCell(35);
//	               	cell.setCellValue("");
//	               }
//	                
//	               /* if(null!=employeePersonalDataDTO.getToneOfTeamMail().getConfident())
//	                {
//	                	cell=row.createCell(13);
//	                	cell.setCellValue(employeePersonalDataDTO.getToneOfTeamMail().getConfident());
//	                	
//	                }else{
//	                	cell=row.createCell(13);
//	                	cell.setCellValue("");
//	                }
//	                 */
//	                                
//	                
//	                 
//	                 i++;
//	                 rowCount ++;
//	                 
//	             }
	             
				 String filePathSave = null;
	             if(appUtils.inProduction)
	     		{
	            	 filePath ="/var/lib/tomcat7/conf/MasterUploadBak/TeamList.xlsx";
	            	 
	     		}else{
	     			
	     		//	filePath ="C:\\Users\\Mahadev Sharma\\Downloads\\TeamList.xlsx";
	     			
//				 filePathSave ="D:\\workspace\\kafalanalytics\\src\\main\\webapp\\images\\TeamList.xlsx";
//				 filePath = "images\\TeamList.xlsx";
	     			filePathSave = "/opt/tomcat/webapps/HanogiAnalytics/PersonalTeamList.xlsx";
//	     			System.out.println(System.getProperty("xml.location"));
//	     			filePathSave = "/Users/rakshitvats/Downloads/git code/kafal/knowledgeopsfinal/kafalanalytics";
	         		filePath = "PersonalTeamList.xlsx";
				 
	     		}
	     			
	             FileOutputStream out = new FileOutputStream(filePathSave);
	             workbook.write(out);
	             out.close();
	                			
	        }catch(Exception e)
	        {
	        	e.printStackTrace();
	        }
			return filePath;
			
	        }
	
	
	
	
}
