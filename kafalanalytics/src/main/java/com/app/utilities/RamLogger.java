package com.app.utilities;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.app.security.UserAuthCredentials;

@Component
public class RamLogger {

	private static final Logger logger = Logger.getLogger("ReferaMaid");

	public static void logInfo(String message) {
		logger.info(message);
	}

	public static void logDebug(String message) {
		logger.debug(message);
	}

	public static void logError(Object e) {

		StringWriter errors = new StringWriter();
		((Exception) e).printStackTrace(new PrintWriter(errors));

		logger.error(errors.toString());

	}

	public static void logAudit(
			EnumeratedTypes.serviceNamesForAudit serviceName,
			UserAuthCredentials userAuthCredentials, String clientIp,
			String additionalInfo) {
		
		Long currentTime =  AppUtils.getTodaysActualTime().getTime();
		
		StringBuffer auditMsg = new StringBuffer();
		
		auditMsg.append("audit");
		auditMsg.append("||");
		auditMsg.append(currentTime);
		auditMsg.append(currentTime);
		auditMsg.append("||");
		auditMsg.append(serviceName.name());
		if(null != userAuthCredentials)
		{
		auditMsg.append(userAuthCredentials.getUserSessionInformation().get(EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID).toString());
		auditMsg.append("||");
		}	
		
		if(null != clientIp && !clientIp.isEmpty())
		{
		auditMsg.append(clientIp);
		auditMsg.append("||");
		}	
		
		if(null != additionalInfo && !additionalInfo.isEmpty())
		{
		auditMsg.append(additionalInfo);
		auditMsg.append("||");
		}
		
		logger.info(auditMsg.toString());
		auditMsg = null;
	}
	
	
	public static void logAuditFailure(EnumeratedTypes.serviceNamesForAudit serviceName, UserAuthCredentials userAuthCredentials , String clientIp,String additionalInfo) {
    Long currentTime =  AppUtils.getTodaysActualTime().getTime();
		
		StringBuffer auditMsg = new StringBuffer();
		
		auditMsg.append("auditfail");
		auditMsg.append("||");
		auditMsg.append(currentTime);
		auditMsg.append(currentTime);
		auditMsg.append("||");
		auditMsg.append(serviceName.name());
		if(null != userAuthCredentials)
		{
		auditMsg.append(userAuthCredentials.getUserSessionInformation().get(EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID).toString());
		auditMsg.append("||");
		}	
		
		if(null != clientIp && !clientIp.isEmpty())
		{
		auditMsg.append(clientIp);
		auditMsg.append("||");
		}	
		
		if(null != additionalInfo && !additionalInfo.isEmpty())
		{
		auditMsg.append(additionalInfo);
		auditMsg.append("||");
		}
		
		logger.info(auditMsg.toString());
		auditMsg = null;
	}
	

	public static void logError(String message, Exception e) {
		// TODO Auto-generated method stub
		logger.error(message, e);
	}

}
