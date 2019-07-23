package com.app.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.bo.EmailConfigurationBO;
import com.app.pojo.EmailConfigurationPojo;
import com.app.pojo.GmailConfig;
import com.app.pojo.OrganizationPojo;
import com.app.pojo.OutlookConfig;
import com.app.pojo.PSTConfig;
import com.app.security.UserAuthCredentials;
import com.app.service.obj.RequestObject;
import com.app.service.obj.ResponseObject;
import com.app.services.AdminService;
import com.app.services.EmailConfigurationService;
import com.app.utilities.AppUtils;
import com.app.utilities.EnumeratedTypes;
import com.app.utilities.ExceptionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class EmailConfigurationController {
	@Autowired
	EmailConfigurationService emailConfigurationService;

	@RequestMapping(value = "/auth/configureemail", method = RequestMethod.POST)
	// @ResponseBody
	private @ResponseBody ResponseObject runImportRecordsJob(@RequestParam("file") MultipartFile file) {
		ResponseObject responseObject = new ResponseObject();

		UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder.getContext().getAuthentication();
		String pk = (String) authObj.getUserSessionInformation()
				.get(EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
		String Server_type = "Gmail";
		String dir_path = createdirectory(pk, Server_type);
		if (dir_path.equals("Directory already exist.")) {
			ExceptionResponse.generateSuccessResponse(responseObject, "Directory already exist.");
			
		} else {
			File convertedfile;
			try {
				convertedfile = ReadExcelData.fileconverter(file);
				boolean correctextn = checkextension(convertedfile, AppUtils.EXTN_FOR_GMAIL_CONFIG);
				if (correctextn) {
					String absolute_path = transferFile(convertedfile, dir_path, pk);
					EmailConfigurationPojo emailConfigurationPojo = new EmailConfigurationPojo();
					ArrayList<GmailConfig> a1 = new ArrayList<GmailConfig>();
					GmailConfig gmailConfig = new GmailConfig();
					Set<String> a2 = new HashSet<String>();
					a2.add("GMAIL");
					emailConfigurationPojo.setAdminEmail(pk);
					gmailConfig.setConfigfilelocation(absolute_path);  // Setting The absolute path.
					a1.add(gmailConfig);
					emailConfigurationPojo.setGmailSetting(a1);
					emailConfigurationPojo.setServerType(a2);
					emailConfigurationService.saveSettingGmail(emailConfigurationPojo,"file"); // Saving the setting for the
																						// Gmail.
					ExceptionResponse.generateSuccessResponse(responseObject, "success");
					
				} else {
					
					ExceptionResponse.generateSuccessResponse(responseObject, "error");
					
				}

			} catch (Exception e) {
				ExceptionResponse.generateSuccessResponse(responseObject, "error");
				e.printStackTrace();
			}

		}

		return responseObject;

	}

	// for the pst files.

	@RequestMapping(value = "/auth/configureemailpst", method = RequestMethod.POST)
	// @ResponseBody
	private @ResponseBody ResponseObject runImportRecordsJobforpst(@RequestParam("file") MultipartFile[] file) {
		ResponseObject responseObject = new ResponseObject();

		ArrayList<MultipartFile> filelist = new ArrayList<MultipartFile>(Arrays.asList(file));

		UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder.getContext().getAuthentication();
		String pk = (String) authObj.getUserSessionInformation()
				.get(EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
		String Server_type = "pst";
		String dir_path = createdirectory(pk, Server_type);
		if (dir_path.equals("Directory already exist.")) {
			ExceptionResponse.generateSuccessResponse(responseObject, "Directory already exist.");
			return responseObject;
		} else {

			try {
				List<PSTConfig> pstconfiglist = fileconverter(filelist, dir_path, pk);
				EmailConfigurationPojo emailConfigurationPojo = new EmailConfigurationPojo();
				Set<String> a1 = new HashSet<String>();
				a1.add("PST");
				emailConfigurationPojo.setServerType(a1);
				emailConfigurationPojo.setPstSetting(pstconfiglist);
				emailConfigurationPojo.setAdminEmail(pk);
				emailConfigurationService.saveSettingPST(emailConfigurationPojo); // Saving the setting for the PST
																					// // PST.
				ExceptionResponse.generateSuccessResponse(responseObject, "Total   "+pstconfiglist.size()+"  is uploded sucessfully.");
			} catch (Exception e) {
				ExceptionResponse.generateSuccessResponse(responseObject, e.getMessage());
				e.printStackTrace();
			}

		}

		return responseObject;
	}

	@RequestMapping(value = "/auth/saveemailconfvalues", method = RequestMethod.POST)
	// @ResponseBody
	private @ResponseBody ResponseObject saveemailconfvalues(@RequestBody RequestObject requestObject) {
		ObjectMapper objectMapper = new ObjectMapper();
		ResponseObject responseObject = new ResponseObject();
		try {
			String emailConfigvalues = objectMapper.treeToValue(requestObject.getRqBody(), String.class);
			String[] foam_values = emailConfigvalues.split("/", 0);
			UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder.getContext().getAuthentication();
			String pk = (String) authObj.getUserSessionInformation()
					.get(EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
			EmailConfigurationPojo emailConfigurationPojo = emailConfigurationService.getEmailConfgurationValues(pk);
			List<GmailConfig> gmailSetting = emailConfigurationPojo.getGmailSetting();
			GmailConfig gmailConfig = gmailSetting.get(0);
			GmailConfig gmailConfignew = new GmailConfig();
			gmailConfignew.setServiceAccountUser(foam_values[0]);
			gmailConfignew.setClientId(foam_values[1]);

			gmailConfignew.setConfigfilelocation(gmailConfig.getConfigfilelocation());
			List<GmailConfig> gmailSettingupdated = new ArrayList<GmailConfig>();
			gmailSettingupdated.add(gmailConfignew);

			emailConfigurationPojo.setGmailSetting(gmailSettingupdated);
			emailConfigurationService.saveSettingGmail(emailConfigurationPojo,"values");
			ExceptionResponse.generateSuccessResponse(responseObject, "Details saved sucesfully.");
		} catch (JsonProcessingException e) {
			ExceptionResponse.generateSuccessResponse(responseObject, e.getMessage());
			e.printStackTrace();
		}
		return responseObject;

	}
	
	
	// For Saving the setting for the Outlook.
	

	@RequestMapping(value = "/auth/saveemailconfvaluesoutlook", method = RequestMethod.POST)
	// @ResponseBody
	private @ResponseBody ResponseObject saveemailconfvaluesoutlook(@RequestBody RequestObject requestObject) {
		ObjectMapper objectMapper = new ObjectMapper();
		ResponseObject responseObject = new ResponseObject();
		try {
			List<OutlookConfig> a2 = new ArrayList<OutlookConfig>();
			String emailConfigvalues = objectMapper.treeToValue(requestObject.getRqBody(), String.class);
			String[] foam_values = emailConfigvalues.split("/", 0);
			UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder.getContext().getAuthentication();
			String pk = (String) authObj.getUserSessionInformation()
					.get(EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
			
			EmailConfigurationPojo emailConfigurationPojo = new EmailConfigurationPojo();
			Set<String> a1 = new HashSet<String>();
			a1.add("OUTLOOK");
			OutlookConfig outlookConfig= new OutlookConfig();
			outlookConfig.setClientId(foam_values[0]);
			outlookConfig.setUsername(foam_values[1]);
			outlookConfig.setPassword(foam_values[2]);
			a2.add(outlookConfig);
			emailConfigurationPojo.setServerType(a1);
			emailConfigurationPojo.setOutlookSetting(a2);
			emailConfigurationPojo.setAdminEmail(pk);
			emailConfigurationService.saveSettingOutlook(emailConfigurationPojo); // Saving the setting for the Outlook.
																			
			
			
			ExceptionResponse.generateSuccessResponse(responseObject, "Details saved sucesfully.");
		} catch (JsonProcessingException e) {
			ExceptionResponse.generateSuccessResponse(responseObject, e.getMessage());
			e.printStackTrace();
		}
		return responseObject;

	}
	
	
	

	public static String createdirectory(String email_id, String Server_type) {
		String dir_path = AppUtils.TOKENS_DIRECTORY_PATH + "/" + "admin_users" + "/" + email_id + "/"
				+ Server_type.toUpperCase();
		File file = new File(dir_path);
		if (!file.exists()) {
			if (file.mkdirs()) {
				System.out.println("Directory is created!");
			} else {
				System.out.println("Failed to create directory!");
				// return "Failed to create directory";
				return dir_path;
			}
			String file_path = file.getAbsolutePath();
			return file_path;
		} else {
			// return "Directory already exist.";
			return dir_path;
		}

	}

	public static String transferFile(File src, String dir_path, String pk) throws IOException {
		InputStream is = null;
		OutputStream os = null;

		File dest = new File(dir_path + "/" + pk + "_" + src.getName());
		try {
			is = new FileInputStream(src);
			os = new FileOutputStream(dest);
			// buffer size 1K
			byte[] buf = new byte[1024];
			int bytesRead;
			while ((bytesRead = is.read(buf)) > 0) {
				os.write(buf, 0, bytesRead);
			}
		} catch (Exception e) {
			System.out.println("Exception---" + e.getMessage());
			e.printStackTrace();
			return e.getMessage();

		} finally {

			is.close();
			os.close();
		}
		return dest.getAbsolutePath();
	}

	public static boolean checkextension(File file, String requiredextn) {
		System.out.println("file is " + file.getAbsolutePath());
		if (FilenameUtils.isExtension((file).getName(), requiredextn)) {
			return true;
		} else {
			return false;
		}
	}

	public static List<PSTConfig> fileconverter(List<MultipartFile> filelist, String dir_path, String pk)
			throws IOException {
		List<String> filelocationlist = new ArrayList<String>();
		List<PSTConfig> a2 = new ArrayList<PSTConfig>();
		for (MultipartFile file : filelist) {
			File convFile = new File(file.getOriginalFilename());
			convFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(convFile);
			fos.write(file.getBytes());
			fos.close();
			boolean correctextn = checkextension(convFile, AppUtils.EXTN_FOR_PST_CONFIG);
			if (correctextn) {
				String absolute_path = transferFile(convFile, dir_path, pk);
				filelocationlist.add(absolute_path);

				PSTConfig pstConfig = new PSTConfig();
				pstConfig.setConfigfilelocation(absolute_path);
				a2.add(pstConfig);
			}

		}
		return a2;
	}

}
