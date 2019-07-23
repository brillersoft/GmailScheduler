package com.app.utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
public class AppUtils {
	
	public static boolean inProduction = false;
	
	public static String adminHomeAddress = "home admin";
	
	
	private static final Map<String, String> pinCodesAgainstState = new HashMap<String, String>();
	
	private static final String imagePattern = "(jpg|tif|bmp|jpeg|png|gif)" ;
	
	private static final String mediaPattern = "(3gp|act|aiff|aac|amr|ape|au|awb|dct|dss|dvf|flac|gsm|iklax|ivs|m4a|m4p|mmf|mp3|mpc|msv|ogg|oga|opus|ra|rm|raw|sln|tta|vox|wav|wma|wv|webm)" ;
	
	private static final String documentPattern = "(doc|dot|docx|docm|dotx|dotm|docb)";
	
	private static final String pdfDocumentPattern = "(pdf)";
	
	private static final String excelDocumentpattern = "(xls|xlt|xlm|xlsx|xlsm|xltx|xltm|xlsb|xla|xlam|xll|xlw)";
	
	private static final String powerpointDocumentPattern = "(ppt|pot|pps|pptx|pptm|potx|potm|ppam|ppsx|ppsm|sldx|sldm)";
	
	private  static final Pattern compiledImagePattern;
	
	private  static final Pattern compiledMediaPattern;
	
    private  static final Pattern compiledDocumentPattern;
    
    private  static final Pattern compiledPdfDocumentPattern;
    
    private  static final Pattern compiledExcelDocumentPattern;
    
    private  static final Pattern compiledPowerPointDocumentPattern;
    
	// Start of changes by aashish for email configuration.
    public static final String[]EMPLOYEE_HEADERS = { "Employee Name", "Employee Id", "Employee Email","Report To" };
    public static final String[] CLIENT_HEADER = { "Client Name", "Client Domain", "Client Country" };
    public static final String TOKENS_DIRECTORY_PATH = System.getProperty("user.home");
    public static final String EXTN_FOR_GMAIL_CONFIG = "p12";
    public static final String EXTN_FOR_PST_CONFIG  = "pst";
    public static final String MESSAGE_FOR_TOKEN_EXPIRED  = "Your Token is Expired , Please use forget password link on login Screen.";
    public static final String MESSAGE_FOR_PASSWORD_ALREADY_CHANGED  = "Your Password Is Alreaady Changed.";
    // end of changes by aashish for email configuration.
    
	
	
	

	static {

		pinCodesAgainstState.put("Andaman_and_Nicobar_Islands", "74");
		pinCodesAgainstState.put("Andhra_Pradesh", "50,51,52,53");
		pinCodesAgainstState.put("Arunachal_Pradesh", "79");
		pinCodesAgainstState.put("Assam", "78");
		pinCodesAgainstState.put("Bihar", "80,81,82,83,84,85");
		pinCodesAgainstState.put("Chandigarh", "14,16");
		pinCodesAgainstState.put("Chhattisgarh", "49");
		pinCodesAgainstState.put("Dadra_and_Nagar_Haveli", "40,41,42,43,44");
		pinCodesAgainstState.put("Daman_and_Diu", "36,39");
		pinCodesAgainstState.put("Delhi", "11");
		pinCodesAgainstState.put("Goa", "40");
		pinCodesAgainstState.put("Gujarat", "36,37,38,39");
		pinCodesAgainstState.put("Haryana", "12,13");
		pinCodesAgainstState.put("Himachal_Pradesh", "17");
		pinCodesAgainstState.put("Jammu_and_Kashmir", "18,19");
		pinCodesAgainstState.put("Jharkhand", "80,81,82,83,92");
		pinCodesAgainstState.put("Karnataka", "56,57,58,59");
		pinCodesAgainstState.put("Kerala", "67,68,69");
		pinCodesAgainstState.put("Lakshadweep", "36,37,38,39");
		pinCodesAgainstState.put("Madhya_Pradesh", "45,46,47,48");
		pinCodesAgainstState.put("Maharashtra", "40,41,42,43,44");
		pinCodesAgainstState.put("Manipur", "79");
		pinCodesAgainstState.put("Meghalaya", "79");
		pinCodesAgainstState.put("Mizoram", "79");
		pinCodesAgainstState.put("Nagaland", "79");
		pinCodesAgainstState.put("Orissa", "75,76,77");
		pinCodesAgainstState.put("Pondicherry", "53,60,67");
		pinCodesAgainstState.put("Punjab", "14,15,16");
		pinCodesAgainstState.put("Rajasthan", "30,31,32,33,34");
		pinCodesAgainstState.put("Sikkim", "73");
		pinCodesAgainstState.put("Tamil_Nadu", "60,61,62,63,64");
		pinCodesAgainstState.put("Telangana", "60,61,62,63,64");
		pinCodesAgainstState.put("Uttaranchal", "20,21,22,23,24,25,26,27,28");
		pinCodesAgainstState.put("Uttar_Pradesh", "20,21,22,23,24,25,26,27,28");
		pinCodesAgainstState.put("West_Bengal", "70,71,72,73,74");

	}
	
	
	public static final Map<String, String> chatCategory = new HashMap<String, String>();
	public static final Map<String, String> notificationChatCategory = new HashMap<String, String>();
	public static final Map<String, String> emdstatus = new HashMap<String, String>();
	public static final Map<String, String> serviceName = new HashMap<String, String>();
	public static final Map<String, String> getServiceName = new HashMap<String, String>();

	
	
	static {
		chatCategory.put("requestforinformation","Request for information");
		chatCategory.put("requestforreferral" ,"Request for referral");
		chatCategory.put("requestforfeedback","Request for feedback");
		chatCategory.put("generalcomments","General comments");
		chatCategory.put("referral","Referral");
		chatCategory.put("opinion","Opinion");
		chatCategory.put("topicdiscussion","Topic discussion");
		
		
		notificationChatCategory.put("servicenotification","Service Notification");
		notificationChatCategory.put("neweventnotification" ,"New Event Notification");
		notificationChatCategory.put("eventmodificationnotification","Event Modification Notification");
		notificationChatCategory.put("eventcancellationnotification","Event Cancellation Notification");
		notificationChatCategory.put("newannouncementnotification","New Announcement Notification");
		

        compiledImagePattern = Pattern.compile(imagePattern);
        compiledMediaPattern = Pattern.compile(mediaPattern);
        compiledDocumentPattern = Pattern.compile(documentPattern);
        compiledPdfDocumentPattern = Pattern.compile(pdfDocumentPattern);
        compiledExcelDocumentPattern = Pattern.compile(excelDocumentpattern);
        compiledPowerPointDocumentPattern = Pattern.compile(powerpointDocumentPattern);
        
        emdstatus.put("WAITING_FOR_ROUTING", "WAITING FOR ROUTING");
        emdstatus.put("WAITING_FOR_APPROVAL", "WAITING FOR APPROVAL");
        emdstatus.put("AS_PER_WORKFLOW", "AS PER WORKFLOW");
        emdstatus.put("AUTO_APPROVER", "AUTO APPROVER");
        emdstatus.put("ADDITIONAL_APPROVER", "ADDITIONAL APPROVER");
        emdstatus.put("WAITING_FOR ACTION", "WAITING FOR ACTION");
        emdstatus.put("APPROVED","APPROVED");
        emdstatus.put("REJECTED", "REJECTED");
        emdstatus.put("FILE_UPLOADED", "FILE UPLOADED");
        emdstatus.put("DATA_PORTING_IN_PROGRESS", "DATA PORTING IN PROGRESS");
        emdstatus.put("DATA_PORTING_FINISHED", "DATA PORTING FINISHED");
        emdstatus.put("DATA_PORTING_IN_PROGRESS_33", "DATA PORTING IN PROGRESS (33% COMPLETED)");
        emdstatus.put("DATA_PORTING_IN_PROGRESS_66", "DATA PORTING IN PROGRESS (66% COMPLETED)");
        emdstatus.put("DATA_PORTING_IN_PROGRESS_100", "DATA PORTING IN PROGRESS (100% COMPLETED)");
        emdstatus.put("DATA_PORTING_FAILED", "DATA PORTING FAILED");
        
        
        
        //serviceName.put("searchbyemployeename", "EMPLOYEE_NAME_SEARCH");
        serviceName.put("searchbyemployeeid", "EMPLOYEE_ID_SEARCH");
        //serviceName.put("searchbycustomername", "CUSTOMER_NAME_SEARCH");
        serviceName.put("searchbycustomercode", "CUSTOMER_CODE_SEARCH");
        serviceName.put("searchbysalesordernumber", "SALESORDER_NUMBER_SEARCH");
        serviceName.put("listlineitemsformilestone", "MILESTONE_SEARCH");
        serviceName.put("uploadprofilephoto", "EMPLOYEE_PROFILE_SERVICE");
        serviceName.put("initiaterddrequest", "RDD_SERVICE");
        serviceName.put("getpushnotification", "NOTIFICATION_SEND_SERVICE");
        serviceName.put("getallnotificationforuser", "NOTIFICATION_LIST_SERVICE");
        
       // getServiceName.put("EMPLOYEE_NAME_SEARCH", "EMPLOYEE SEARCH");
        getServiceName.put("EMPLOYEE_ID_SEARCH", "EMPLOYEE ID SEARCH");
        //getServiceName.put("CUSTOMER_NAME_SEARCH", "CUSTOMER SEARCH");
        getServiceName.put("CUSTOMER_CODE_SEARCH", "CUSTOMER CODE SEARCH");
        getServiceName.put("SALESORDER_NUMBER_SEARCH", "SALES ORDER NUMBER SEARCH");
        getServiceName.put("MILESTONE_SEARCH", "MILESTONE SEARCH");
        getServiceName.put("EMPLOYEE_PROFILE_SERVICE", "PROFILE SERVICE");
        getServiceName.put("RDD_SERVICE", "RDD SERVICE");
        getServiceName.put("NOTIFICATION_SEND_SERVICE", "NOTIFICATION SEND SERVICE");
        getServiceName.put("NOTIFICATION_LIST_SERVICE", "NOTIFICATION LIST SERVICE");
	}

	/*
	 * truncate time field so that day is stroing only the day and not the time
	 */
	public static Date getTodaysMinDate() {
		Calendar todaysDate = Calendar.getInstance();

		todaysDate.set(Calendar.HOUR_OF_DAY, 0);
		todaysDate.set(Calendar.MINUTE, 0);
		todaysDate.set(Calendar.SECOND, 0);
		todaysDate.set(Calendar.MILLISECOND, 0);

		return todaysDate.getTime();
	}

	public static Date getTodaysMaxDate() {
		Calendar todaysDate = Calendar.getInstance();

		todaysDate.set(Calendar.HOUR_OF_DAY, 23);
		todaysDate.set(Calendar.MINUTE, 59);
		todaysDate.set(Calendar.SECOND, 59);
		todaysDate.set(Calendar.MILLISECOND, 99);

		return todaysDate.getTime();
	}
	
	public static String getTodaysDate(){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"dd-MM-yyyy");
			
	String formattedDate = dateFormat.format(cal.getTime());
	return formattedDate;
	}
	
	public static String calculateDateTimeString(Long longDate)
	{
		Date date=new Date(longDate);
       // SimpleDateFormat df2 = new SimpleDateFormat("dd-MMMM-yy HH:mm:ss");
       // String dateText = df2.format(date);
		String dateText = new SimpleDateFormat("EEE").format(date);
        return dateText;
	}

	public static Long getTodaysActualTimeInMiliSecond()
	{
		Calendar todaysDate = Calendar.getInstance();
		return todaysDate.getTimeInMillis();
	
	}
	public static Date getTodaysActualTime() {
		Calendar todaysDate = Calendar.getInstance();
		return todaysDate.getTime();
	}

	public static Date resetTime(Date date) {
		Calendar todaysDate = Calendar.getInstance();
		todaysDate.setTime(date);

		todaysDate.set(Calendar.HOUR, 0);
		todaysDate.set(Calendar.MINUTE, 0);
		todaysDate.set(Calendar.SECOND, 0);
		todaysDate.set(Calendar.MILLISECOND, 0);

		return todaysDate.getTime();
	}

	public static Date setMaxTime(Date date) {
		Calendar todaysDate = Calendar.getInstance();
		todaysDate.setTime(date);

		todaysDate.set(Calendar.HOUR, 23);
		todaysDate.set(Calendar.MINUTE, 59);
		todaysDate.set(Calendar.SECOND, 59);
		todaysDate.set(Calendar.MILLISECOND, 99);

		return todaysDate.getTime();
	}

	public static Date addDaysToDate(Date inputDate, int intervalInDays) {
		Calendar todaysDate = Calendar.getInstance();
		todaysDate.setTime(inputDate);

		todaysDate.add(Calendar.DAY_OF_MONTH, intervalInDays);

		todaysDate.set(Calendar.HOUR, 0);
		todaysDate.set(Calendar.MINUTE, 0);
		todaysDate.set(Calendar.SECOND, 0);
		todaysDate.set(Calendar.MILLISECOND, 0);

		return todaysDate.getTime();
	}

	public static Date getNextHourBegin() {
		Calendar todaysDate = Calendar.getInstance();
		todaysDate.add(Calendar.HOUR, 1);
		todaysDate.set(Calendar.MINUTE, 0);
		todaysDate.set(Calendar.SECOND, 0);
		todaysDate.set(Calendar.MILLISECOND, 0);

		return todaysDate.getTime();
	}

	public static Date validateStringToDate(Date transformedDate,
			String inputDateStr) {

		String[] splitDate = inputDateStr.split("/");

		Calendar newCal = Calendar.getInstance();
		newCal.setTimeInMillis(transformedDate.getTime());

		if ((newCal.get(Calendar.DAY_OF_MONTH) != Integer
				.parseInt(splitDate[0]) || (newCal.get(Calendar.MONTH) != Integer
				.parseInt(splitDate[1]) - 1))) {
			/*
			 * the date is not valid so return the date = 0 / 0 / 0 ie epoch
			 * time
			 */

			newCal.setTimeInMillis(0);
			return newCal.getTime();
		}

		return transformedDate;

	}

	public static boolean validateDate(Long transformedDate) {
		boolean isValid = true;

		Calendar newCal = Calendar.getInstance();
		newCal.setTimeInMillis(transformedDate);

		Calendar epochCal = Calendar.getInstance();
		epochCal.setTimeInMillis(0);

		epochCal.setTime(AppUtils.resetTime(epochCal.getTime()));

		if (epochCal.compareTo(newCal) == 0) {
			isValid = false;
		}

		return isValid;
	}

	public static Date getEpochDate() {
		Calendar newCal = Calendar.getInstance();
		newCal.setTimeInMillis(0);
		return newCal.getTime();
	}

	public static Boolean validatePinCodeAgainstState(String pinCode, String state) {
		/*
		 * get first two character string out of the pincode
		 */
		String firstTwoDigits = pinCode.substring(0, 2);

		if (pinCodesAgainstState.containsKey(state)) {
			String[] pinCodes = pinCodesAgainstState.get(state).split(",");

			for (String elem : pinCodes) {
                        if(firstTwoDigits.equals(elem))
                        {
                        	return true;
                        }
			}
		} else {
			return false;
		}
		
		return false;

	}
	
	public String getFileType(String fileName)
	{
		String[] fileExtension = fileName.split("\\.");
		
		String fileType = fileExtension[fileExtension.length - 1];
		
		compiledImagePattern.matcher(fileType);
		
		if( compiledImagePattern.matcher(fileType).matches())
		{
			return EnumeratedTypes.FileType.IMAGE.name();
		}
		else if(compiledMediaPattern.matcher(fileType).matches())
		{
			return EnumeratedTypes.FileType.MEDIA.name();
			
		}
		else if(compiledDocumentPattern.matcher(fileType).matches())
		{
			return EnumeratedTypes.FileType.DOCUMENT.name();
		}
		
		else if(compiledPdfDocumentPattern.matcher(fileType).matches())
		{
			return EnumeratedTypes.FileType.PDF_DOCUMENT.name();
		}
		else if(compiledExcelDocumentPattern.matcher(fileType).matches())
		{
			return EnumeratedTypes.FileType.EXCEL_DOCUMENT.name();
		}
		else if(compiledPowerPointDocumentPattern.matcher(fileType).matches())
		{
			return EnumeratedTypes.FileType.POWERPOINT_DOCUMENT.name();
		}	
		
		
		return fileType;
	}
	
	
	public static String encodeValue(String inputValue)
	{
		return new String(Base64.encodeBase64(inputValue.getBytes()));
	}
	public static String calculateTimeElapsed(Long inQDate , Long actionTakenDate)
	{

		
		
		Long actualTime = AppUtils.getTodaysActualTime().getTime();
		
		long minutesDiff = (long)(actionTakenDate - inQDate)/(1000*60);
		
		/*
		 * now figure out in terms of hours  minutes and days 
		 */
		String responseString = "";
		if(minutesDiff <= 1)
		{
			responseString = "1 Min";
		}
		else if((minutesDiff) > 1 && (minutesDiff < 60))
		{
			responseString = minutesDiff + " Mins";
		}
		else if((minutesDiff) >= 60 && (minutesDiff < 1440))
		{
			responseString = (int)(minutesDiff/60) + " Hrs";
		}
		else if((minutesDiff) > 1440 && (minutesDiff <= 43200))
		{
			responseString = (int)(minutesDiff/(24*60)) + " Days";
		}
		else if((minutesDiff) > 43200 && (minutesDiff <= 518400))
		{
			
			responseString = (int)(minutesDiff/(24*60*30)) + " Months";
		}
		else 
		{
			responseString = (int)(minutesDiff/(24*60*30*12)) + " Years";
		}
		
		
		return responseString;

	
	}
	
	public static String convertLongToDate(Long longDate)
	{
		
		Date dateToBeFormatted= new Date(longDate);
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"dd-MMM-yy");
		
		String formattedDate = dateFormat.format(dateToBeFormatted);

		return formattedDate;
	}
	
	public static String generateRandomString() {
		  Random random = new Random((new Date()).getTime());
	      char[] values = {'a','b','c','d','e','f','g','h','i','j',
	               'k','l','m','n','o','p','q','r','s','t',
	               'u','v','w','x','y','z','0','1','2','3',
	               '4','5','6','7','8','9'};

	      String out = "";

	      for (int i=0;i<6;i++) {
	          int idx=random.nextInt(values.length);
	          out += values[idx];
	      }
	      return out;
	    }
	
	public static String getOriginalPassword(String userPassword)
	{
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword("316802");
		String myDecryptedText = textEncryptor.decrypt(userPassword);
		
		return myDecryptedText;
	}
	
	private static String[] arrayForPassword = {"a", "b" , "c" , "d" , "e" , 
        "1" , "2" , "3" , "4" , "5",
        "A" , "B" , "C" , "D" , "E",
        "#" , "?" , "!" ,"&" , "%" , "$",
        "6", "7" , "8" , "9",
        "f", "g" , "h" , "i" , "j" , "k" ,"l" , "m" , "n",
        "F" , "G" , "H" , "J" , "K" , "L" , "M" , "N",
        "p","q","r","s","t","u","v","w","x","y","z",
        "P","Q","R","S","T","U","V","W","X","Y","Z"};

	public static String encryptPassword(String passwords) {
		// TODO Auto-generated method stub

			StringBuffer password = new StringBuffer();
			password.append(passwords);
			
			BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
			textEncryptor.setPassword("316802");
			String myEncryptedText = textEncryptor.encrypt(password.toString());
			return myEncryptedText;
			
		
	}
}
