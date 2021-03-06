package com.app.bo;

import java.util.List;
import java.util.Set;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.springframework.data.mongodb.core.mapping.Document;

import com.app.pojo.GmailConfig;
import com.app.pojo.OutlookConfig;
import com.app.pojo.PSTConfig;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Document(collection = "EmailConfiguration")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailConfigurationBO {
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String id;
	private String adminEmail;
	private Set<String> serverType;
	public List<GmailConfig> gmailSetting;
	public List<PSTConfig> pstSetting;
	public List<OutlookConfig> outlookSetting;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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
