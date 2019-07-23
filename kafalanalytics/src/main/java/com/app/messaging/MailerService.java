package com.app.messaging;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.codec.CharEncoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import com.app.bo.CronRunTime;
import com.app.bo.EmployeeBO;
import com.app.mongo.repositories.CronRunTimeRepository;
import com.app.mongo.repositories.EmployeeRepository;
import com.app.utilities.AppUtils;
import com.app.utilities.GetMailSession;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.property.complex.MessageBody;




@Service
public class MailerService {
	
	@Autowired
	CronRunTimeRepository cronRepo;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	 @Autowired
	  private MongoTemplate mongoTemplate;
	 @Autowired
	 private JavaMailSender mailSender;
	 
	 private static ResourceBundle rb = ResourceBundle.getBundle("Application");

	public void sendMessage(String emailId,String otp)
	{
		try {
			
			/*
			 * get mail session
			 */
			//CronRunTime cPass = cronRepo.findOne(1L);
			ExchangeService service = GetMailSession.initMailService("Welcome2kafal");
			EmailMessage msgs = new EmailMessage(service);
			String philipsEmailText = GetMailSession.kafalEmailTemplateText;
			EmployeeBO usrData =employeeRepository.findOne(emailId);
			if(null != usrData)
			{
				if(usrData.getEmployeeName()!=null)
				{
					philipsEmailText = philipsEmailText.replace("{{USER_NAME}}", usrData.getEmployeeName());
					philipsEmailText = philipsEmailText.replace("{{otp}}", otp);
				}
				
			}
			msgs.setSubject("Forgot Password");
	        msgs.setBody(MessageBody.getMessageBodyFromText(philipsEmailText));
	        msgs.getToRecipients().add(usrData.getEmailId());
	        msgs.send();
	        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	    	Date date = new Date();
	    	//System.out.println(dateFormat.format(date));
	    	
	    	long datetime = date.getTime();
	    	
	        CronRunTime cronRunTime = new CronRunTime();
	        cronRunTime.setOtp(otp);
	        cronRunTime.setCron_run_timecol(datetime);
	        cronRunTime.setEmployeeId(usrData.getEmployeeId());
	        mongoTemplate.save(cronRunTime);
	        
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	/*public void sendRegistrationMail(Long id)
	{
		try {
			
			
			 * get mail session
			 
			CronRunTime cPass = cronRepo.findOne(1L);
			ExchangeService service = GetMailSession.initMailService(cPass.getPassword());
			EmailMessage msgs = new EmailMessage(service);
			String philipsEmailText = GetMailSession.philipsEmailTemplateText;
			UserData usrData =usrDataRepo.findById(id);
			if(null != usrData)
			{
				if(usrData.getName()!=null)
				{
					philipsEmailText = philipsEmailText.replace("{{USER_NAME}}", usrData.getName());
				}
				if(usrData.getPassword()!=null)
				{
					//philipsEmailText = philipsEmailText.replace("{{PASSWORD}}", usrData.getPassword());
					
					philipsEmailText = philipsEmailText.replace("{{PASSWORD}}", AppUtils.getOriginalPassword(usrData.getPassword()));
				}
			}
			msgs.setSubject("Welcome To CORTA");
	        msgs.setBody(MessageBody.getMessageBodyFromText(philipsEmailText));
	        msgs.getToRecipients().add(usrData.getEmailId());
	        msgs.send();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}*/
	public void prepareAndSendforPasswordChange(String employeename,String recipient, String token) {
	   	 MimeMessagePreparator messagePreparator = mimeMessage -> {
	   		 
	   		 Map <String,String> a1= new HashMap<String,String >();
	   		 
				MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, CharEncoding.UTF_8);
				String host = rb.getString("Host");
				String port = "/";
				String context = rb.getString("Context");
			//	String page="/"+"resetpassword.jsp"+"?cred="+token;
			//	String page="/app/noauth/changepassword"+"?cred="+token;
				String page="/app/noauth/changepassword"+"?cred="+recipient;
				String Url="http://"+host+port+context+page;
				System.out.println(Url);
				messageHelper.setFrom("kumaraashish1234@gmail.com");
				messageHelper.setTo(recipient);
				messageHelper.setSubject("Please Change The password");
				messageHelper.setText("<html>\n" + 
						"<body>" + 
						"<div align=center>"+
						"<img src='cid:identifier1234'>"+  
						"</div>"+
						"<div align=center>" + 
						"<font size='3.5' >Dear "+employeename+"</font><br>"+
						"<font size='3.5' >Your Account Has been created  </font>"+
						"</div>" +
						"<div align=center>" +
						"<img src='cid:identifier1234line'>"+ 
						"</div>" +
						"<div align=center>" + 
						" <font size='3.5' >  Your user id  is :" + recipient+" </font><br>"+
						"</div>" + 
						"<div align=center>" + 
						"<a href="+Url+ "\target=\"_parent\"><button style=background-color:17bbc5;>Reset Password</button></a>"+
						"</div>" + 
						
						"<div align=center >" +
						"<img src='cid:identifier1234thickline' style='float:centre;width:950px;height:15px;'>"+ 
						"</div>" +
						"<div align=left>" + 
						" <font size='3.5' > Regards,</font> <br>" + 
						" <font size='3.5' > Hanogi Email Insights Suits.<font> <br>" + 
						
						"</div>" +
						"<div align=center>" +
						"<img src='cid:identifier1234line'>"+ 
						"</div>" +
						"<div align=center>" +
						" <font size='3.0' >In case of any issues please contact us -hello@hanogi.com <font> <br>" + 
						"</div>" +
						"</body>" + 
						"</html>", true);
				
				FileSystemResource res = new FileSystemResource(new File("/root/header.png"));
				FileSystemResource resline = new FileSystemResource(new File("/root/line.png"));
				FileSystemResource resthickline = new FileSystemResource(new File("/root/thickline.jpg"));
				messageHelper.addInline("identifier1234", res);
				messageHelper.addInline("identifier1234line", resline);
				messageHelper.addInline("identifier1234thickline", resthickline);
				
	   	    };
	   	    try {
	   	        mailSender.send(messagePreparator);
	   	    } catch (MailException e) {
	   	       
	   	     e.printStackTrace();
	   	    }
	   }
	
	
	
	
}
