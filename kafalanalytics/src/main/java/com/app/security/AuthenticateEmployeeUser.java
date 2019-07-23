package com.app.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import com.app.utilities.EnumeratedTypes;





public class AuthenticateEmployeeUser implements AuthenticationProvider{
	final static Logger logger=Logger.getLogger(AuthenticateEmployeeUser.class);
	
	
	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.authentication.AuthenticationProvider#authenticate(org.springframework.security.core.Authentication)
	 * 
	 * just to create an authentication object for future storage of user data 
	 */
	public Authentication authenticate(Authentication authentication)  throws AuthenticationException 
	{
	    System.out.println("AuthenticateLoggedInUser");
        
        List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();
        grantedAuths.add(new GrantedAuthorityImpl("USER_FULL_AUTH"));
        
        /*
         * based on the password that we receive we should populate the UserAuthCredentials object properly
         */
        
        logger.info("AuthenticateLoggedInUser");
        
        UserAuthCredentials authenticated =  new UserAuthCredentials(authentication.getName(),authentication.getCredentials().toString(), grantedAuths);
        
        
        authenticated.getUserSessionInformation().put(EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID, authentication.getName());
        
        Map<String,String> userRolesMap = new HashMap<String,String>();
        userRolesMap.put(EnumeratedTypes.UserRoleName.EMPLOYEE.name(), "");
        authenticated.getUserSessionInformation().put(EnumeratedTypes.UserSessionData.USER_ROLES,userRolesMap);
        
       return authenticated;
        
    }
 
	
    public boolean supports(Class<?> authentication)
    {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
    
   
}
