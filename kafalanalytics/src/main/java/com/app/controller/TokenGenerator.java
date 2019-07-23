package com.app.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.app.utilities.AppUtils;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

public class TokenGenerator {

	// GMAIl API
	private static final String APPLICATION_NAME = "Hanogi App";
	private static final com.google.api.client.json.JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	public static void main(String args[]) throws Exception {
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		
	/*	UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
				.getContext().getAuthentication();
		String pk = (String) authObj.getUserSessionInformation().get(
				EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);  */
		
		String email_id = "andrew.cawte@kafalsoftware.com";
		String file_path = createdirectory(email_id);
		if (file_path.equals("Directory already exist.")) {
			System.out.println("Tokens exists.");
		} else {
			File src= new File("/home/kafalsoft/Employeedemo.xlsx");
			//transferFile(src, file_path);
			System.out.println("Tokens created.");
			
		}

	}


	public static String createdirectory(String email_id) {
		String dir_path = AppUtils.TOKENS_DIRECTORY_PATH + "/"+"admin_users"+"/"+email_id+ "/" +"Gmail";
		File file = new File(dir_path);
		System.out.println("file_path---" + dir_path);
		if (!file.exists()) {
			if (file.mkdirs()) {
				System.out.println("Directory is created!");
			} else {
				System.out.println("Failed to create directory!");
				return "Failed to create directory";
			}
			String file_path = file.getAbsolutePath();
			System.out.println("file_path---" + file_path);
			return file_path;
		} else {
			return "Directory already exist.";
		}

	}
	
	public static void transferFile(File src, String dir_path) throws IOException {
		InputStream is = null;
		OutputStream os = null;
		File dest = new File(dir_path+"/"+"andrew.cawte@kafalsoftware"+"_gmail"+".p12");
		try {
			is = new FileInputStream(src);
			os = new FileOutputStream(dest);
			// buffer size 1K
			byte[] buf = new byte[1024];
			int bytesRead;
			while ((bytesRead = is.read(buf)) > 0) {
				os.write(buf, 0, bytesRead);
			}
		}
			catch(Exception e ) {
				System.out.println("Exception---" + e.getMessage());
				e.printStackTrace();
			}
		 finally {

			is.close();
			os.close();
		}
	}

}
