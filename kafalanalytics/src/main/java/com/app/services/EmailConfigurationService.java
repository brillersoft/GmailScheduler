package com.app.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.app.bo.EmailConfigurationBO;
import com.app.mongo.repositories.EmailConfigurationRepository;
import com.app.pojo.EmailConfigurationPojo;
import com.app.pojo.GmailConfig;
import com.app.pojo.OutlookConfig;
import com.app.pojo.PSTConfig;

@Component
public class EmailConfigurationService {

	@Autowired
	EmailConfigurationRepository emailConfigurationRepository;
	@Autowired
	private MongoTemplate mongoTemplate;

// To save the Gmail Settings  on click of browse button.
	public String saveSettingGmail(EmailConfigurationPojo emailConfigurationPojo, String mode) {
		GmailConfig gmailConfig = null;
		Set<String> serverTypes = emailConfigurationPojo.getServerType();
		EmailConfigurationBO emailConfigurationBO = new EmailConfigurationBO();
		emailConfigurationBO.setAdminEmail(emailConfigurationPojo.getAdminEmail());
		emailConfigurationBO.setServerType(serverTypes);
		emailConfigurationBO.setGmailSetting(emailConfigurationPojo.getGmailSetting());
		emailConfigurationBO.setPstSetting(null);
		emailConfigurationBO.setOutlookSetting(null);
		EmailConfigurationBO emailConfigurationBOnew = emailConfigurationRepository
				.findByAdminEmail(emailConfigurationPojo.getAdminEmail());
		if (emailConfigurationBOnew == null) {
			emailConfigurationRepository.save(emailConfigurationBO);
		} else {
			Set<String> a1 = emailConfigurationBOnew.getServerType();
			if (a1 == null) {
				a1 = new HashSet<String>();
			}
			List<GmailConfig> a2 = emailConfigurationBOnew.getGmailSetting();
			if (a2 == null && mode.equals("file") ) {
				gmailConfig = new GmailConfig();
				gmailConfig.setConfigfilelocation(
						emailConfigurationPojo.getGmailSetting().get(0).getConfigfilelocation());
			} else {
				
					gmailConfig = a2.get(0);
					gmailConfig.setServiceAccountUser(
							emailConfigurationPojo.getGmailSetting().get(0).getServiceAccountUser());
					gmailConfig.setClientId(emailConfigurationPojo.getGmailSetting().get(0).getClientId());
				

			}

			List<GmailConfig> a3 = new ArrayList<GmailConfig>();
			a3.add(gmailConfig);
			a1.addAll(serverTypes);

			emailConfigurationBOnew.setServerType(a1);
			emailConfigurationBOnew.setGmailSetting(a3);
			emailConfigurationRepository.save(emailConfigurationBOnew);
		}

		return "sucess";

	}

	// To save the Pst Settings

	public String saveSettingPST(EmailConfigurationPojo emailConfigurationPojo) {
		Set<String> serverTypes = emailConfigurationPojo.getServerType();
		EmailConfigurationBO emailConfigurationBO = new EmailConfigurationBO();
		emailConfigurationBO.setAdminEmail(emailConfigurationPojo.getAdminEmail());
		emailConfigurationBO.setServerType(serverTypes);
		emailConfigurationBO.setGmailSetting(null);
		emailConfigurationBO.setPstSetting(emailConfigurationPojo.getPstSetting());
		emailConfigurationBO.setOutlookSetting(null);
		EmailConfigurationBO emailConfigurationBOnew = emailConfigurationRepository
				.findByAdminEmail(emailConfigurationPojo.getAdminEmail());
		if (emailConfigurationBOnew == null) {
			emailConfigurationRepository.save(emailConfigurationBO);
		} else {
			Set<String> a1 = emailConfigurationBOnew.getServerType();
			if (a1 == null) {
				a1 = new HashSet<String>();
			}
			List<PSTConfig> a2 = emailConfigurationBOnew.getPstSetting();
			if (a2 == null) {
				a2 = new ArrayList<PSTConfig>();
			}

			a1.addAll(serverTypes);
			a2.addAll(emailConfigurationPojo.getPstSetting());
			emailConfigurationBOnew.setServerType(a1);
			emailConfigurationBOnew.setPstSetting(a2);
			emailConfigurationRepository.save(emailConfigurationBOnew);

		}

		return "sucess";

	}

	public EmailConfigurationPojo getEmailConfgurationValues(String admin_email) {

		EmailConfigurationBO emailConfigurationBO = emailConfigurationRepository.findByAdminEmail(admin_email);
		if (emailConfigurationBO == null) {
			EmailConfigurationPojo emailConfigurationPojo = null;
			return emailConfigurationPojo;
		}
		EmailConfigurationPojo emailConfigurationPojo = new EmailConfigurationPojo();
		emailConfigurationPojo.setAdminEmail(emailConfigurationBO.getAdminEmail());

		emailConfigurationPojo.setServerType(emailConfigurationBO.getServerType());

		emailConfigurationPojo.setGmailSetting(emailConfigurationBO.getGmailSetting());
		emailConfigurationPojo.setOutlookSetting(emailConfigurationBO.getOutlookSetting());
		emailConfigurationPojo.setPstSetting(emailConfigurationBO.getPstSetting());
		return emailConfigurationPojo;
	}

	public String saveSettingOutlook(EmailConfigurationPojo emailConfigurationPojo) {

		Set<String> serverTypes = emailConfigurationPojo.getServerType();
		EmailConfigurationBO emailConfigurationBO = new EmailConfigurationBO();
		emailConfigurationBO.setAdminEmail(emailConfigurationPojo.getAdminEmail());
		emailConfigurationBO.setServerType(serverTypes);
		emailConfigurationBO.setGmailSetting(null);
		emailConfigurationBO.setPstSetting(null);
		emailConfigurationBO.setOutlookSetting(emailConfigurationPojo.getOutlookSetting());
		EmailConfigurationBO emailConfigurationBOnew = emailConfigurationRepository
				.findByAdminEmail(emailConfigurationPojo.getAdminEmail());
		if (emailConfigurationBOnew == null) {
			emailConfigurationRepository.save(emailConfigurationBO);
		} else {
			Set<String> a1 = emailConfigurationBOnew.getServerType();
			if (a1 == null) {
				a1 = new HashSet<String>();
			}
			List<OutlookConfig> a2 = emailConfigurationBOnew.getOutlookSetting();
			if (a2 == null) {
				a2 = new ArrayList<OutlookConfig>();
			}

			a1.addAll(serverTypes);
			a2.addAll(emailConfigurationPojo.getOutlookSetting());
			emailConfigurationBOnew.setServerType(a1);
			emailConfigurationBOnew.setOutlookSetting(a2);
			emailConfigurationRepository.save(emailConfigurationBOnew);

		}

		return "sucess";

	}

}
