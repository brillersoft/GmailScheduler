package com.app.utilities;

public class EnumeratedTypes {

	public enum SocialPlatforms {
		FACEBOOK, NATIVE
	}

	public enum FileType {
	
		DOCUMENT , MEDIA , IMAGE, PDF_DOCUMENT, EXCEL_DOCUMENT, POWERPOINT_DOCUMENT, DEFAULT
	}
	
	
	
	public enum UserRoleName {
		REGISTERED ,PAID , SERVICEPROVIDER,SERVICECONSUMER,AGENCY, ADMIN ,EMPLOYEE
	}

	public enum EmdStatus {
		WAITING_FOR_APPROVAL , APPROVED , WAITING_FOR_ROUTING , REJECTED ,AUTO_APPROVER , ADDITIONAL_APPROVER , AS_PER_WORKFLOW
	}
	
	public enum UserLoggedInStatus {
		LOGGEDOUT, LOGGEDIN
	}

	
	public enum ResponseStatus {
		SUCCESS, ERROR
	}



	public enum ValidationErrorMessages {
		ENTER_VALUE, INCORRECT_FORMAT, INVALID_VALUE, AGE_IS_LESS_THEN_16_YEARS, DATE_GREATER_THAN_TODAYS_DATE, UPLOAD_RESUME, RANGE_OF_EXPERIENCE_REQUIRED_INCORRECT,THIS_ID_IS_ALREADY_REGISTERED,ENTER_MINIMUM_NUMBER_OF_CHARACTERS,
		ENTER_CONTACT_NUMBER_AND_NAME_OR_ENTER_ID_PROOF,
		ENTER_CONTACT_NUMBER_OR_ENTER_ID_PROOF,
		MAXIMUM_255_CHARACTERS_ALLOWED, PASSWORD_MISMATCH_TRY_AGAIN,ENTER_VALUE_FOR_ATLEAST_ONE_CRITERIA, NEW_PASSWORD_AND_RETYPE_NEWPASSWORD_MISMATCH, OLD_PASSWORD_AND_TYPE_PASSWORD_MISMATCH, THIS_MESSAGE_GROUP_NAME_IS_ALREADY_CREATED
	}


	public enum UserSessionData {
		 POSTS_SEARCHED_FOR_TEXT ,USER_PROFILE, USER_AGENT, SOCIAL_ACCESS_TOKENS, USER_ROLES, ACTIVE_SOCIAL_PLATFORM, SP_PRIMARY_PROFILE_ID,PRIMARY_PROFILE_ID,CLIENT_IP, RELATED_PROFILE_ID
	}

	public enum ExceptionType {
		VALIDATION_EXCEPTION, BUSINESS_EXCEPTION, TECHNICAL_EXCEPTION
	}

	public enum UserProfileStatus {
		ACTIVE, PENDING_CLOSURE, CLOSED,ONHOLD
	}

	
	
	
	public enum BusinessExceptions {
		Maximum_limit_of_chats_initiated_by_you_has_been_reached__Please_close_some_previously_initiated_chats,
		Maximum_limit_of_chats_blocked_by_you_has_been_reached__Please_unblock_some_previously_blocked_chats,
		Maximum_limit_of_chats_pinned_by_you_has_been_reached__Please_unpin_some_previously_unpinned_chats
	}
	
	
	public enum Gender {
		M,F
	}
	
	
	public enum State {
		    Andaman_and_Nicobar_Islands,
			Andhra_Pradesh,
			Arunachal_Pradesh,
			Assam,
			Bihar,
			Chandigarh,
			Chhattisgarh,
			Dadra_and_Nagar_Haveli,
			Daman_and_Diu,
			Delhi,
			Goa,
			Gujarat,
			Haryana,
			Himachal_Pradesh,
			Jammu_and_Kashmir,
			Jharkhand,
			Karnataka,
			Kerala,
			Lakshadweep,
			Madhya_Pradesh,
			Maharashtra,
			Manipur,
			Meghalaya,
			Mizoram,
			Nagaland,
			Orissa,
			Pondicherry,
			Punjab,
			Rajasthan,
			Sikkim,
			Tamil_Nadu,
			Telangana,
			Tripura,
			Uttaranchal,
			Uttar_Pradesh,
			West_Bengal
	}
	
	public enum skills {
		maid,cook,driver,nanny,gardener,officeboy
	}
	
	public enum idProofType
	{
		adhaar,pancard,drivinglicense,passport,voterid
	}
	
	public enum messageTypes {
		SP_UPDATE_PROFILE,
		SC_UPDATE_PROFILE,
		RESET_PASSWORD
	}
	public enum Priority
	{
		Low, Moderate, High, Neutral
	}
	
	public enum workDuration {
		PARTIME,FULLTIME,ANY
	}
	
	public enum serviceNamesForAudit {
		SC_SIGNUP , SC_REGISTER,SC_UPDATE,SC_SELFPHOTO,SC_SPFEEDBACK,SC_SPREFERRAL,SC_SEARCHSP,
		SP_REGISTER , SP_AUTHORIZE,SP_SEARCHFORREVIEWORREFERRAL,SP_POLICEVERIFICATION,SP_IDPROOF,
		SP_SELFPHOTO,SP_UPDATE, AG_SIGNUP, AG_AUTHORIZE, AG_UPDATE, AGOWNER_IDPROOF, AG_ID, AG_SELFPHOTO, AG_CONTENT, AG_ABOUTUS, AG_EVENT, AG_RESOURCE, AG_DELETESP, AG_DETAILOFSP, AG_SEARCHSP, AG_NOTIFICATION, AG_SEARCH
	}
	
	public static enum chatCategory {
		requestforinformation,requestforreferral,requestforfeedback,generalcomments,referral,opinion,topicdiscussion
	}
	
	
	public static enum agencyChatCategory {
		servicenotification , neweventnotification, eventmodificationnotification,eventcancellationnotification,newannouncementnotification
	}
	public  enum contentType {
		header,aboutUs,resources,blogs,events
	}
	
	public  enum searchKey {
		All,Any
	}
	
	public enum attributeName{
		EMD_AMOUNT , COLLECTABLE_OR_NON_COLLECTABLE , DD_DATE , DD_FDR_NUMBER , FORM_OF_EMD , SAP_POSTING_DATE , BUSINESS_GROUP , CUSTOMER_TYPE , DELIVERY_PERIOD ,
		EMD_REFUND_DUE_DATE , EMD_REQUESTER_REGION , EMD_REQUIRED_DATE , EMD_REQUIRED_FROM , EMD_UP_TIME , LATE_DELIVERY_CHARGES ,
		ISSUANCE_DATE_OF_FDR_OR_BG , MATURITY_DATE_OF_FDR_BG , SECURITY_DEPOSIT , SECURITY_DEPOSIT_DUE_DATE , TENDER_ISSUING_AUTHORITY ,
		TNDER_ISSUING_STATE , CFO_APPROVAL_REQUIRED , TENDER_ORDER_OPEN_DATE , TENDER_ORDER_CLOSE_DATE , TENDER_VALUE
	
	}
	
	public enum CollectableOrnot{
		
		collectable , non_collectable
	}
	
	public enum FormOfEmd{
		
		Bank_Guarantee , Cheque ,Citi_Bank_Demand_Draft , Fixed_Deposit_Receipt , Online_Transfer,SBI_Demand_Draft

	}
	
	public enum BusinessGroup{
		 
		IS , PCMS , ULS , HHS , Customer_Service
	}
	
	public enum EmdUpTime{
		
		LESS_THAN_95_PERCENT , GREATER_THAN_95_PERCENT
	}
	
	public enum emdRequiredFrom{
		
		Citi_Bank , Nationalized_Bank_SBI
	}
}
