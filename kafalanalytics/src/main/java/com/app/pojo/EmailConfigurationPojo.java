package com.app.pojo;

import java.util.List;
import java.util.Set;

public class EmailConfigurationPojo {
	private String adminEmail;
	private Set<String> serverType;
	public List<GmailConfig> gmailSetting;
	public List<PSTConfig> pstSetting;
	public List<OutlookConfig> outlookSetting;
	public String getAdminEmail() {
		return adminEmail;
	}
	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}
	public Set<String> getServerType() {
		return serverType;
	}
	public void setServerType(Set<String> serverType) {
		this.serverType = serverType;
	}
	public List<GmailConfig> getGmailSetting() {
		return gmailSetting;
	}
	public void setGmailSetting(List<GmailConfig> gmailSetting) {
		this.gmailSetting = gmailSetting;
	}
	public List<PSTConfig> getPstSetting() {
		return pstSetting;
	}
	public void setPstSetting(List<PSTConfig> pstSetting) {
		this.pstSetting = pstSetting;
	}
	public List<OutlookConfig> getOutlookSetting() {
		return outlookSetting;
	}
	public void setOutlookSetting(List<OutlookConfig> outlookSetting) {
		this.outlookSetting = outlookSetting;
	}
	
	
	

}
