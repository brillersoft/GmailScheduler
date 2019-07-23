package com.app.security;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

public class LogoutAdSuccessHandler implements LogoutSuccessHandler{

	
	public void onLogoutSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		//response.sendRedirect("/agency/agency.jsp");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/app/noauth/adlogoutsuccess");
		dispatcher.forward(request, response);
		
	}

}
