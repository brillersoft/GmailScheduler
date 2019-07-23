package com.app.security;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import com.app.utilities.EnumeratedTypes;
import com.app.utilities.RamLogger;

public class LogoutAdHandler implements LogoutHandler {

	public void logout(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication) {
		// TODO Auto-generated method stub
		
		/*
		 * update the consumer to logged out 
		 * update the last logged out time
		 */
		Map<String, Object> params = new HashMap();
		
//		Neo4JExtension template = new Neo4JExtension(session);
		
		
		String pk = "";
		try{
		UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
				.getContext().getAuthentication();
		pk = String.valueOf(authObj.getUserSessionInformation().get(
				EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID));

		
		 response.addHeader("adpk", pk);
		 
		}
		catch(Exception e) {
			/*
			 * in case of exception return a message
			 */
			RamLogger.logError(e);
			e.printStackTrace();
		

		}
		
/*		String queryBegin = "MATCH (n:Agency{pk:"
				+ "\""
				+ pk
				+ "\""
				+ "})"
				+ "SET n.selfLoggedIn=" + false
				+ ",n.lastLoggedOutTime=" + AppUtils.getTodaysActualTime().getTime();

		Iterable<Map<String, Object>> mapResult = template.query(
				queryBegin, params);*/

		
	}

}

