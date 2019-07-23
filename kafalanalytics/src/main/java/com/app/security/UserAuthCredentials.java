package com.app.security;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import com.app.utilities.EnumeratedTypes;

public class UserAuthCredentials extends UsernamePasswordAuthenticationToken {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Map<EnumeratedTypes.UserSessionData, Object> userSessionInformation = new HashMap<EnumeratedTypes.UserSessionData, Object>();

	/*
	 * private Map<EnumeratedTypes.SocialPlatforms,
	 * UsernamePasswordAuthenticationToken> userAccessTokens = new
	 * HashMap<EnumeratedTypes.SocialPlatforms,
	 * UsernamePasswordAuthenticationToken>(); private String userAgent; private
	 * Map<EnumeratedTypes.UserRoleName,String> userRole = new
	 * HashMap<EnumeratedTypes.UserRoleName, String>();
	 * 
	 * private JSONObject userFlowDetails; private byte[] userObject ; private
	 * String resumeFileName;
	 * 
	 * private EnumeratedTypes.SocialPlatforms activeSocialPlatform; private
	 * String coverPhotoUrl;
	 */

	public UserAuthCredentials(Object principal, Object credentials) {
		super(principal, credentials);
		
		/*
		 * initilaize all maps to avoid null pointer exceptions later on
		 */
		this.userSessionInformation.put(
				EnumeratedTypes.UserSessionData.SOCIAL_ACCESS_TOKENS,
				new HashMap<EnumeratedTypes.SocialPlatforms,UsernamePasswordAuthenticationToken>());
		
		this.userSessionInformation.put(
				EnumeratedTypes.UserSessionData.USER_ROLES,
				new HashMap<String,String>());
		
	}

	public UserAuthCredentials(Object principal, Object credentials,
			Collection<? extends GrantedAuthority> authorities) {
		super(principal, credentials, authorities);
		
		/*
		 * initilaize all maps to avoid null pointer exceptions later on
		 */
		this.userSessionInformation.put(
				EnumeratedTypes.UserSessionData.SOCIAL_ACCESS_TOKENS,
				new HashMap<EnumeratedTypes.SocialPlatforms,UsernamePasswordAuthenticationToken>());
		
		this.userSessionInformation.put(
				EnumeratedTypes.UserSessionData.USER_ROLES,
				new HashMap<String,String>());
	}

	public UserAuthCredentials(String name, Object credentials,
			List<GrantedAuthority> grantedAuths,
			UserAuthCredentials inputAuthObj) {
		super(name, credentials, grantedAuths);
	
		this.userSessionInformation.put(
				EnumeratedTypes.UserSessionData.USER_AGENT,
				inputAuthObj.getUserSessionInformation().get(
						EnumeratedTypes.UserSessionData.USER_AGENT));
		
		
		if (inputAuthObj.getUserSessionInformation().get(
				EnumeratedTypes.UserSessionData.SOCIAL_ACCESS_TOKENS) == null)
		{
		this.userSessionInformation.put(
				EnumeratedTypes.UserSessionData.SOCIAL_ACCESS_TOKENS,
				new HashMap<EnumeratedTypes.SocialPlatforms,UsernamePasswordAuthenticationToken>());
		}
		else
		{
			this.userSessionInformation.put(
					EnumeratedTypes.UserSessionData.SOCIAL_ACCESS_TOKENS,
					inputAuthObj.getUserSessionInformation().get(
							EnumeratedTypes.UserSessionData.SOCIAL_ACCESS_TOKENS));
		}
		
		
		if (inputAuthObj.getUserSessionInformation().get(
				EnumeratedTypes.UserSessionData.USER_ROLES) == null)
		{
			this.userSessionInformation.put(
					EnumeratedTypes.UserSessionData.USER_ROLES,
					new HashMap<String,String>());
			
		}
		else
		{
			this.userSessionInformation.put(
					EnumeratedTypes.UserSessionData.USER_ROLES,
					inputAuthObj.getUserSessionInformation().get(
							EnumeratedTypes.UserSessionData.USER_ROLES));
		}
		
	}

	public Map<EnumeratedTypes.UserSessionData, Object> getUserSessionInformation() {
		return userSessionInformation;
	}

	public void setUserSessionInformation(
			Map<EnumeratedTypes.UserSessionData, Object> userSessionInformation) {
		this.userSessionInformation = userSessionInformation;
	}

}
