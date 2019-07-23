package com.app.utilities;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;


@Component
public class PhilipsNotificationLogger {

	
	private static final Logger logger = Logger.getLogger("SfmxNotificationLogger");
	
	public static void logInfo(String message)
	{
		logger.info(message);
    }

	public static void logDebug(String message) 
	{
			logger.debug(message);
	}

	public static void logError(Object e)
	{
		StringWriter errors = new StringWriter();
		((Exception)e).printStackTrace(new PrintWriter(errors));

		logger.error(errors.toString());

	}

	public void logError(String message, Exception e) {
		// TODO Auto-generated method stub
		logger.error(message, e);
	}


}
