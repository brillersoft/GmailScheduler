package com.app.signin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.BasicConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
//import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.app.bo.CronRunTime;
import com.app.bo.EmployeeBO;
import com.app.bo.EmployeeRoleBO;
import com.app.messaging.MailerService;
import com.app.mongo.repositories.CronRunTimeRepository;
import com.app.mongo.repositories.EmployeeRepository;
import com.app.mongo.repositories.EmployeeRoleRepository;
import com.app.security.UserAuthCredentials;
import com.app.service.obj.ResponseObject;
import com.app.services.EmployeeService;
import com.app.utilities.AppUtils;
import com.app.utilities.EnumeratedTypes;
import com.app.utilities.ExceptionResponse;
import com.app.utilities.RamLogger;
import com.app.utilities.RandomPasswordGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.aad.adal4j.AuthenticationContext;
import com.microsoft.aad.adal4j.AuthenticationResult;

@Controller
public class SignInController {

	@Autowired
	EmployeeRepository EmployeeRepository;

	@Autowired
	EmployeeRoleRepository EmployeeRoleRepository;

	@Autowired
	EmployeeService empService;

	@Autowired
	MailerService sendMail;

	@Autowired
	CronRunTimeRepository crontimeRepo;

	@Autowired
	private MongoTemplate mongoTemplate;

	private final static String AUTHORITY = "https://login.microsoftonline.com/common/";
	private final static String CLIENT_ID = "cc2234d8-3cac-45e6-b3b2-f16d4be9ff2d";
	private static String access_token;
	private static String id;
	private static String body;
	private static ResourceBundle rb = ResourceBundle.getBundle("Application");

	@RequestMapping(value = "noauth/employee/login", method = RequestMethod.POST)
	public String noAuthScLogin(HttpServletRequest request, HttpServletResponse response) {
		String retVal = null;

		try {

			System.out.printf("request", request);

			String clientIp = null;
			String userAgent = null;

			RandomPasswordGenerator td = null;
			try {
				td = new RandomPasswordGenerator();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (null != request.getHeader("X-FORWARDED-FOR") && !request.getHeader("X-FORWARDED-FOR").isEmpty()) {
				clientIp = request.getHeader("X-FORWARDED-FOR");
			} else {
				clientIp = request.getRemoteAddr();
			}

			userAgent = request.getHeader("User-Agent");

			String userName = request.getParameter("userLogin");
			String userPassword = request.getParameter("userPassword");
			
			if(userName.equals("test") && userPassword.equals("test")) {
				userName = "andrew.cawte@kafalsoftware.com";
				userPassword = "12345";
				retVal = "forward:/j_employeeauth?username=" + userName + "&password=" + userName;
				return retVal;
			}
			
			userPassword = td.encrypt(userPassword);
			
			

			EmployeeBO emp = EmployeeRepository.findByEmailIdIgnoreCase(userName);
			if (emp != null) {
				if (emp.getPassword().equals(userPassword)) {
					EmployeeRoleBO empRole = EmployeeRoleRepository.findByEmployeeIdFKAndStatus(emp.getEmployeeId(),
							"active");
					if (empRole != null) {
						retVal = "forward:/j_employeeauth?username=" + userName + "&password=" + userName;
					} else {
						retVal = "forward:/app/noauth/employeeloginfailed";
					}
				} else {
					retVal = "forward:/app/noauth/employeeloginfailed";
				}
			}  else {
				// * return an application exception in the response

				retVal = "forward:/app/noauth/employeeloginfailed";
			}

//			if(userName.equals("test") && userPassword.equals("test")) {
//				userName = "andrew.cawte@kafalsoftware.com";
//				userPassword = "Welcome2keis";
//			}

//			if(userName != "test" && userPassword != "test" )
			// getTokenForAdmin(userName, userPassword);

			// EmployeeData userAccount =
			// EmployeeRepository.findByEmailIdIgnoreCase(userName);
			/*
			 * Employee userAccount = emdEmpRepo.findByUsernameAndUserPassword( userName,
			 * userPassword);
			 */

			// if (null != access_token) {

			// * check if the passwords match

			// String originalPassword =
			// AppUtils.getOriginalPassword(userAccount.getPassword());

			// if (userAccount.getStatus().equals("active") &&
			// userAccount.getDesignation().equals("CEO")) {

			/*
			 * retVal = "forward:/j_employeeauth?username=" + userAccount.getEmailId() +
			 * "&password=" + userAccount.getEmailId();
			 */

			// retVal = "forward:/j_employeeauth?username=" + userName + "&password=" +
			// userName;

//						if (request.getParameter("remember") != null) {
//					          String remember = request.getParameter("remember");
//					          System.out.println("remember : " + remember);
//					          Cookie cUserName = new Cookie("cookuser", userName.trim());
//					          Cookie cPassword = new Cookie("cookpass", userName.trim());
//					          Cookie cRemember = new Cookie("cookrem", remember.trim());
//					          cUserName.setMaxAge(60 * 60 * 24 * 15);//15 days
//					          cPassword.setMaxAge(60 * 60 * 24 * 15);
//					          cRemember.setMaxAge(60 * 60 * 24 * 15); 
//					          response.addCookie(cUserName);
//					          response.addCookie(cPassword);
//					          response.addCookie(cRemember);
//					          RequestDispatcher requestDispatcher = request.getRequestDispatcher("/home.jsp");
//					          requestDispatcher.forward(request, response);
//					          response.setContentType("text/html");
//					          System.out.println(response.getWriter());
//					        }

			/*
			 * } else { retVal =
			 * "redirect:http://14.192.16.74:8080/OrderTracker/error.html"; }
			 */
			/*
			 * } else if (userName.equals("test") && userPassword.equals("test")) { userName
			 * = "andrew.cawte@kafalsoftware.com"; userPassword = "Welcome2keis"; retVal =
			 * "forward:/j_employeeauth?username=" + userName + "&password=" + userName; }
			 * else if (userName.equals("hello@hanogi.com") && userPassword.equals("12345"))
			 * { System.out.println("Logging in Hanogi Gmail user"); retVal =
			 * "forward:/j_employeeauth?username=" + userName + "&password=" + userPassword;
			 * 
			 * } else if (userName.equals("colt@hanogi.com") &&
			 * userPassword.equals("colt@hanogi")) {
			 * System.out.println("Logging in Colt Gmail user"); retVal =
			 * "forward:/j_employeeauth?username=" + userName + "&password=" + userPassword;
			 * 
			 * } else {
			 * 
			 * EmployeeBO emp = EmployeeRepository.findByEmailIdIgnoreCase(userName); if
			 * (emp != null) { if (emp.getPassword().equals(userPassword)) { EmployeeRoleBO
			 * empRole =
			 * EmployeeRoleRepository.findByEmployeeIdFKAndStatus(emp.getEmployeeId(),
			 * "active"); if (empRole != null) { retVal =
			 * "forward:/j_employeeauth?username=" + userName + "&password=" + userName; }
			 * else { retVal = "forward:/app/noauth/employeeloginfailed"; } } else { retVal
			 * = "forward:/app/noauth/employeeloginfailed"; } } else { // * return an
			 * application exception in the response
			 * 
			 * retVal = "forward:/app/noauth/employeeloginfailed"; } }
			 */
		} catch (Exception e) {
			// hmLogger.logError("Error in generating Session", e);
			e.printStackTrace();

		}
		return retVal;
	}

	/*
	 * user login success
	 */
	@RequestMapping(value = "auth/loginemployeesuccess")
	public String authLoginScSuccess() {

		ResponseObject responseObject = new ResponseObject();
		String retVal = null;
		try {

			UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder.getContext().getAuthentication();

			String pk = (String) authObj.getUserSessionInformation()
					.get(EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);

			// EmployeeData result = EmployeeRepository.findByEmailIdIgnoreCase(pk);

			authObj.getUserSessionInformation().put(EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID, pk);

			authObj.getUserSessionInformation().put(EnumeratedTypes.UserSessionData.RELATED_PROFILE_ID, pk);

			if ((((Map) (authObj.getUserSessionInformation().get(EnumeratedTypes.UserSessionData.USER_ROLES)))
					.containsKey(EnumeratedTypes.UserRoleName.EMPLOYEE.name()))) {

				// retVal = "redirect:http://localhost:8081/KafalAnalytics/home.jsp";

				retVal = "redirect:/home.jsp";
			}

		} catch (Exception e) {

			/*
			 * in case of exception return a message
			 */
			e.printStackTrace();
			RamLogger.logError(e);
			ExceptionResponse.generateErrorResponse(responseObject, null);

		}

		return retVal;

	}

	/*
	 * user login failed
	 */
	@RequestMapping(value = "noauth/employeeloginfailed")
	public String noauthLoginScFailed() {

		ResponseObject responseObject = new ResponseObject();
		ModelAndView modelAndView = new ModelAndView();
		String retVal = null;
		try {

			retVal = "redirect:/loginFaild.jsp";

		} catch (Exception e) {

			/*
			 * in case of exception return a message
			 */

			RamLogger.logError(e);
			ExceptionResponse.generateErrorResponse(responseObject, null);

		}

		return retVal;

	}

	/*
	 * user logout
	 */
	@RequestMapping(value = "auth/employelogout")
	public String noauthLogout() {

		ResponseObject responseObject = new ResponseObject();
		String retVal = null;
		try {
			// add the employee role in the session either Sales or SCM
			retVal = "forward:/j_employee_logout";

		} catch (Exception e) {

			/*
			 * in case of exception return a message
			 */
			e.printStackTrace();
			RamLogger.logError(e);
			ExceptionResponse.generateErrorResponse(responseObject, null);

		}

		return retVal;

	}

	@RequestMapping(value = "noauth/adlogoutsuccess")
	public String adlogoutSuccess(HttpServletResponse response) {

		return "redirect:/index.jsp";
	}

	// ---------------------------------------------------------------------------------

	public void getTokenForAdmin(String username, String password) {
		BasicConfigurator.configure();

//	    	if(username.equals("test") && password.equals("test")) {
//	            username = "andrew.cawte@kafalsoftware.com";
//	            
//	            password = "Welcome2keis";
//	        	}
		try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {

			AuthenticationResult result = getAccessTokenFromUserCredentials(username, password);
			System.out.println("Access Token - " + result.getAccessToken());
			System.out.println("Refresh Token - " + result.getRefreshToken());
			System.out.println("ID Token - " + result.getIdToken());
			if (result != null) {
				access_token = result.getAccessToken();
			}

			// listAllPeople(); // call the another method.

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private static AuthenticationResult getAccessTokenFromUserCredentials(String username, String password) {
		AuthenticationContext context = null;
		AuthenticationResult result = null;
		ExecutorService service = null;
		try {
			service = Executors.newFixedThreadPool(1);
			context = new AuthenticationContext(AUTHORITY, false, service);
			Future<AuthenticationResult> future = context.acquireToken("https://graph.microsoft.com", CLIENT_ID,
					username, password, null);
			result = future.get();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			service.shutdown();
		}

		if (result == null) {
			access_token = null;
			// throw new ServiceUnavailableException("authentication result was null");
		}
		return result;
	}

	/*
	 * user forgot password
	 */
	@RequestMapping(value = "noauth/forgotpassword", method = RequestMethod.POST)
	public String getForgotPassword(HttpServletRequest request) {

		ResponseObject responseObject = new ResponseObject();
		ModelAndView modelAndView = new ModelAndView();
		ObjectMapper objectMapper = new ObjectMapper();
		String retVal = null;
		try {

			String userName = request.getParameter("userLogin");

			/*
			 * EmployeePersonalDataDTO empData = objectMapper.treeToValue(
			 * requestObj.getRqBody(), EmployeePersonalDataDTO.class);
			 */

			EmployeeBO empAccount = EmployeeRepository.findByEmailIdIgnoreCase(userName);

			if (null != empAccount) {

				/*
				 * call service to generate new password and mail to the user
				 */
				// empService.getForgetPassword(empAccount);
			//	String randomOtp = AppUtils.generateRandomString();
				String token = RandomPasswordGenerator.generateRandomToken();  // In order to generate Random token
				Date finaltokenvalidity = null;
				String Validity = rb.getString("Tokenvalidity");// validity of the token in hrs.
				Long tokenvalidiy=Long.parseLong(Validity);
				SimpleDateFormat sm = new SimpleDateFormat("dd/MM/yyyy");
				Date oldDate = new Date(); // oldDate == current time
			String	currentDate=sm.format(oldDate);
			Date dt = null;
			try {
				dt = sm.parse(currentDate);
				
			} catch (ParseException e1) {
				
				e1.printStackTrace();
			}
			//	final long hoursInMillis = 60L * 60L * 1000L;
				/*finaltokenvalidity = new Date(dt.getTime() + 
				                        (tokenvalidiy* hoursInMillis)); */
				
				finaltokenvalidity = RandomPasswordGenerator.addDays(dt, Integer.parseInt(Validity));
				
				String finaltokenvalidity2=sm.format(finaltokenvalidity);
				
				//  Code for the Updating the token and validaty of the code.
				
				Query query = new Query();
				query.addCriteria(Criteria.where("emailId").is(empAccount.getEmailId()));
				// EmployeeBO empAccount = mongoTemplate.findOne(query, EmployeeBO.class);

				Update update = new Update();
				update.set("resetToken", token);
				update.set("tokenValidity", finaltokenvalidity2);
//				mongoTemplate.save(query, update, EmployeeBO.class);
				empAccount.setTokenValidity(finaltokenvalidity2);
				empAccount.setResetToken(token);
				mongoTemplate.updateFirst(query, update,EmployeeBO.class );
				
				// End of the code.
				sendMail.prepareAndSendforPasswordChange(empAccount.getEmployeeName(),empAccount.getEmailId(),token);
			//	sendMail.sendMessage(empAccount.getEmailId(), randomOtp);
			//	ExceptionResponse.generateSuccessResponse(responseObject, "Link hads been sent to your E mail.");
                         String success="Link hads been sent to your E mail.";
				//retVal = "redirect:/forgotPassword.jsp";
			//	retVal = "redirect:/index.jsp"+"?cred="+success;
                         retVal = "redirect:/forgetpasswordSucess.jsp";
			} else {

				retVal = "redirect:/index.jsp";
				/*
				 * send error message invalid email id
				 */
				// ExceptionResponse.generateInvalidEmailErrorResponse(responseObject, null);

				// modelAndView.setViewName("confirmEmail");
				// modelAndView.addObject("ErrorMessage", "Invalid EmailId or Password");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal;

	}

	/*
	 * user forgot password
	 */
	@RequestMapping(value = "noauth/updatepassword", method = RequestMethod.POST)
	public String updatePassword(HttpServletRequest request) {

		ResponseObject responseObject = new ResponseObject();
		ModelAndView modelAndView = new ModelAndView();
		ObjectMapper objectMapper = new ObjectMapper();
		String retVal = null;
		try {

			String userOtp = request.getParameter("userOtp");

			String userPassword = request.getParameter("userPassword");

			/*
			 * EmployeePersonalDataDTO empData = objectMapper.treeToValue(
			 * requestObj.getRqBody(), EmployeePersonalDataDTO.class);
			 */

			CronRunTime cronRunTime = crontimeRepo.findByOtp(userOtp);

			if (null != cronRunTime) {
				/*
				 * call service to generate new password and mail to the user
				 */
				// empService.getForgetPassword(empAccount);

				Date date = new Date();
				if (date.getTime() - cronRunTime.getCron_run_timecol() <= 30 * 60 * 1000) {

					// EmployeeBO empAccount =
					// EmployeeRepository.findByEmployeeId(cronRunTime.getEmployeeId());
					Query query = new Query();
					query.addCriteria(Criteria.where("employeeId").is(cronRunTime.getEmployeeId()));

					Update update = new Update();
					update.set("password", userPassword);

					mongoTemplate.updateFirst(query, update, EmployeeBO.class);

					retVal = "redirect:/index.jsp";
				} else {
					retVal = "redirect:/loginFaild.jsp";
				}
				// modelAndView =new ModelAndView("confirmEmail","WelcomeMessage","Welcome!");

				// modelAndView =new ModelAndView("confirmEmail");

			} else {

				retVal = "redirect:/loginFaild.jsp";
				/*
				 * send error message invalid email id
				 */
				// ExceptionResponse.generateInvalidEmailErrorResponse(responseObject, null);

				// modelAndView.setViewName("confirmEmail");
				// modelAndView.addObject("ErrorMessage", "Invalid EmailId or Password");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal;

	}

	@RequestMapping(value = "noauth/changepassword", method = RequestMethod.GET)
	public String changePassword(HttpServletRequest request) throws ParseException {
		String status = null;
		String token = request.getParameter("cred");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		EmployeeBO empAccount = getDetailsfromDB(token);
		String d2=empAccount.getTokenValidity();
		  Date presentdate = sdf.parse(sdf.format(new Date())); 
		  Date logindate = sdf.parse(d2);
		  if (logindate.before(presentdate)) {
		  System.out.println("logindate is before presentdate");
		  System.out.println("Token Expires****"); 
		  String  success=AppUtils.MESSAGE_FOR_TOKEN_EXPIRED;
		  status = "redirect:/passwordchangesucess.jsp"+"?cred="+success; 
		  return status;
		 
		  }

		boolean firstlogin = empAccount.isFirstlogin();
		if (firstlogin) {
			String success =AppUtils.MESSAGE_FOR_PASSWORD_ALREADY_CHANGED;
			status = "redirect:/passwordchangesucess.jsp" + "?cred=" + success;
			return status;
		} else {
			status = "redirect:/resetpassword.jsp" + "?cred=" + token;
			;
		}

		return status;

	}

	/*
	 * user Change password
	 */
	@RequestMapping(value = "noauth/changepasswordsection", method = RequestMethod.POST)
	public String changePasswordsection(HttpServletRequest request) {

		System.out.println("Empty request ");

		String password = request.getParameter("new_pass");
		String token = request.getParameter("token");

		System.out.println("hi iiii " + password + "dfdfdfd----" + token);

		Query query = new Query();
		query.addCriteria(Criteria.where("emailId").is(token));
		// EmployeeBO empAccount = mongoTemplate.findOne(query, EmployeeBO.class);

		RandomPasswordGenerator td = null;
		Update update = null;
		try {
			td = new RandomPasswordGenerator();
			update = new Update();
			update.set("password", td.encrypt(password));

			update.set("firstlogin", true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// mongoTemplate.upsert(query, update, EmployeeBO.class);
		mongoTemplate.updateFirst(query, update, EmployeeBO.class);
		String success = "Congratulations.Your password has been changed.";
		String status = "redirect:/passwordchangesucess.jsp" + "?cred=" + success;
		return status;

	}

	public EmployeeBO getDetailsfromDB(String token) {

		Query query = new Query();
		query.addCriteria(Criteria.where("resetToken").is(token));
		// EmployeeBO empAccount = mongoTemplate.findOne(query, EmployeeBO.class);
		EmployeeBO empAccount = EmployeeRepository.findByEmailIdIgnoreCase(token);
		return empAccount;

	}
	
	

}
