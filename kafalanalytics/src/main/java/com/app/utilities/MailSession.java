package com.app.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


	public class MailSession {
	
	public static Transport transport;
	public static Properties props;
	public static String philipsEmailTemplateText;
	

	
	public static void initMailSession() throws Exception
	{
		props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.ssl.trust","*");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		
		
				
		String textLine = null;
		InputStream in = MailSession.class.getResourceAsStream("email_template.txt");
		BufferedReader input = new BufferedReader(new InputStreamReader(in));
		StringBuffer ftTextBuffer =  new StringBuffer();
		
		while((textLine = input.readLine()) != null)
		{
			ftTextBuffer.append(textLine);
		}
		
		philipsEmailTemplateText = ftTextBuffer.toString();
		input.close();

    }
	public static void closeSession(Transport transport) throws Exception 
	{
		PhilipsNotificationLogger.logInfo("Closing Mail Session");
		transport.close();
	}
	
	
	public static Session getSession(final String fromEmailIdIn, final String fromEmailPasswordIn)
	{
		
		javax.mail.Session mailSession = javax.mail.Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						PhilipsNotificationLogger.logInfo("Getting Mail Session");
						return new PasswordAuthentication(fromEmailIdIn, fromEmailPasswordIn);
					}
				});
		
		return mailSession;
	}
	
	public static Transport getConnection(Session mailSession,final String fromEmailIdIn, final String fromEmailPasswordIn) throws Exception
	{
		Transport transport = mailSession.getTransport("smtp");
		transport.connect("smtp.gmail.com", fromEmailIdIn, fromEmailPasswordIn);
		PhilipsNotificationLogger.logInfo("Getting transport Session");
		return transport;
	}
}