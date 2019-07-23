package com.app.utilities;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import com.app.mongo.repositories.CronRunTimeRepository;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;


public class GetMailSession {
	
	@Autowired
	static
	CronRunTimeRepository cronRepo;
	
	public static String kafalEmailTemplateText;
	public static String philipsEmailNotificationTemplateText;
	public static String philipsEmdClosureTemplateText;
	public static String philipsEmdSendbackDoneTemplateText;
	public static String philipsEmdSendbackEditTemplateText;
	public static String philipsRegistrationMailTemplateText;
	
	public static ExchangeService initMailService(String password) throws Exception
	{
		ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
		//CronRunTime cPass = cronRepo.findOne(1L);
	     ExchangeCredentials credentials = new WebCredentials("alison.gray@kafalsoftware.com", password);
	     service.setCredentials(credentials);
	     //service.autodiscoverUrl("Numero@philips.com");
	     URL myURL = new URL("https://outlook.office365.com/EWS/Exchange.asmx");
	        URI uri = myURL.toURI();
		 	service.setUrl(uri);
	     EmailMessage msg = new EmailMessage(service);
	    
	     /*
	      * Templates
	      */
	     
	     String textLine = null;
	     	//InputStream in = new FileInputStream(new File("/var/lib/tomcat7/conf/EmailTemplate/email_templates.txt"));
	     ClassPathResource resource = new ClassPathResource("email_template_new.txt");
			//InputStream in = GetMailSession.class.getResourceAsStream("email_template_new.txt");
	     InputStream in = resource.getInputStream();
			BufferedReader input = new BufferedReader(new InputStreamReader(in));
			StringBuffer ftTextBuffer =  new StringBuffer();
			
			while((textLine = input.readLine()) != null)
			{
				ftTextBuffer.append(textLine);
			}
			
			kafalEmailTemplateText = ftTextBuffer.toString();
			input.close();
			
			
			/*String textLines = null;
			InputStream ins = MailSession.class.getResourceAsStream("email_issued_template.txt");
			BufferedReader inputs = new BufferedReader(new InputStreamReader(ins));
			StringBuffer ftTextBuffers =  new StringBuffer();
			
			while((textLines = inputs.readLine()) != null)
			{
				ftTextBuffers.append(textLines);
			}
			
			philipsEmailNotificationTemplateText = ftTextBuffer.toString();
			input.close();
			
			
			
			 * template for PENDING APPROVAL from REGIONAL FINANCE
			 
			
			String textLine1 = null;
			InputStream in1 = MailSession.class.getResourceAsStream("email_pending_approval_template.txt");
			BufferedReader input1 = new BufferedReader(new InputStreamReader(in1));
			StringBuffer ftTextBuffer1 =  new StringBuffer();
			
			while((textLine1 = input1.readLine()) != null)
			{
				ftTextBuffer1.append(textLine1);
			}
			
			philipsEmdClosureTemplateText = ftTextBuffer1.toString();
			input1.close();

			
			 * template for emd sendback done
			 
			String textLine2 = null;
			InputStream in2 = MailSession.class.getResourceAsStream("email_sendback_done_template.txt");
			BufferedReader input2 = new BufferedReader(new InputStreamReader(in2));
			StringBuffer ftTextBuffer2 =  new StringBuffer();
			
			while((textLine2 = input2.readLine()) != null)
			{
				ftTextBuffer2.append(textLine2);
			}
			
			philipsEmdSendbackDoneTemplateText = ftTextBuffer2.toString();
			input2.close();
			
			
			 * template for sendback edit
			 
			String textLine3 = null;
			InputStream in3 = MailSession.class.getResourceAsStream("email_sendback_edit_template.txt");
			BufferedReader input3 = new BufferedReader(new InputStreamReader(in3));
			StringBuffer ftTextBuffer3 =  new StringBuffer();
			
			while((textLine3 = input3.readLine()) != null)
			{
				ftTextBuffer3.append(textLine3);
			}
			
			philipsEmdSendbackEditTemplateText = ftTextBuffer3.toString();
			input3.close();*/
			
			 	/*msg.setSubject("Hello world!");
		        msg.setBody(MessageBody.getMessageBodyFromText(philipsEmailTemplateText));
		        msg.getToRecipients().add("nkushwaha@knowledgeops.co.in");
		        msg.send();*/
			return service;
			
	}
	 

}
