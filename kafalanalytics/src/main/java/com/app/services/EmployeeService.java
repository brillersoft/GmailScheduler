package com.app.services;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bson.BSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AccumulatorOperators.Sum;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Term;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import com.app.bo.TeamClientInteractionBO;
import com.app.bo.ClientBO;
import com.app.bo.DailyEmployeeEmailToneBO;
import com.app.bo.DailyTeamEmailToneBO;
import com.app.bo.OrganisationBO;
import com.app.bo.ToneOfMail;
import com.app.bo.EmployeeBO;
import com.app.bo.EmployeeRelationBO;
import com.app.bo.EmployeeRoleBO;
import com.app.dto.ClientDataDTO;
import com.app.dto.EmployeePersonalDataDTO;
import com.app.messaging.MailerService;
import com.app.mongo.repositories.TeamClientInteractionRepository;
import com.app.mongo.repositories.ClientRepository;
import com.app.mongo.repositories.CompanyRepository;
import com.app.mongo.repositories.DailyEmployeeEmailPaginationRepository;
import com.app.mongo.repositories.EmployeeRepository;
import com.app.mongo.repositories.EmployeeRoleRepository;
import com.app.pojo.AddressPojo;
import com.app.pojo.AggregationPojo;
import com.app.pojo.ConsolidatedPojo;
import com.app.pojo.DailyEmployeeTeamUnwindClientPojo;
import com.app.pojo.DailyEmployeeTeamUnwindLinePojo;
import com.app.pojo.DailyEmployeeTeamUnwindPojo;
import com.app.pojo.DailyEmployeeUnwindPojo;
import com.app.pojo.EmailPojo;
import com.app.pojo.EmailToneResultPojo;
import com.app.pojo.EmployeeRolesPojo;
import com.app.pojo.EmployeeSearchPojo;
import com.app.pojo.FilterByCriteria;
import com.app.pojo.FilterPojo;
import com.app.pojo.FilterResultPojo;
import com.app.pojo.LineItemPojo;
import com.app.pojo.TeamLevelTonalResultPojo;
import com.app.mongo.repositories.DailyTeamEmailToneRepository;
import com.app.mongo.repositories.DailyEmployeeEmailToneRepository;
import com.app.security.UserAuthCredentials;
import com.app.utilities.AppUtils;
import com.app.utilities.EnumeratedTypes;
import com.app.utilities.ExportDataToExcel;
import com.app.utilities.RandomPasswordGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.JsonObject;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

@Component
public class EmployeeService {
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	DailyTeamEmailToneRepository dailyTeamEmailToneRepository;
	
	@Autowired
	DailyEmployeeEmailToneRepository dailyEmployeeEmailToneRepository;
	
	
	@Autowired
	CompanyRepository companyRepository;
	
	 @Autowired
	  private MongoTemplate mongoTemplate;
	 
	 @Autowired
	 private DailyEmployeeEmailPaginationRepository emailPaginationRepository;
	 
	 @Autowired
	 private EmployeeRoleRepository employeeRoleRepository;
	 
	 @Autowired
	 MailerService sendMail;
	 
	 @Autowired
	 TeamClientInteractionRepository teamClientInteractionRepository;
	 
	 @Autowired
	 ClientRepository clientRepository;
	 
	 int count =0;
	 
	 @Autowired
	 ExportDataToExcel exportToExcel;
	
	
	
	 
	 
	 private static ResourceBundle rb = ResourceBundle.getBundle("Application");
	
	public EmployeePersonalDataDTO getPersonalPerMailScore(EmployeePersonalDataDTO employeePersonalDataDTO) {
		
			EmployeePersonalDataDTO employeePersonalDataDTO2 = new EmployeePersonalDataDTO();
			List<EmailPojo> list = new ArrayList<EmailPojo>();
			
			
			EmployeeBO employeeBO = employeeRepository.findByEmployeeId(employeePersonalDataDTO.getEmployeeId());
			
			employeePersonalDataDTO2.setEmployeeName(employeeBO.getEmployeeName());
			employeePersonalDataDTO2.setEmailId(employeeBO.getEmailId());
			
			EmployeeRoleBO employeeRoleBO = employeeRoleRepository.findByEmployeeIdFKAndStatus(employeeBO.getEmployeeId(),"active");
			
					
					employeePersonalDataDTO2.setDesignation(employeeRoleBO.getDesignation());
					
					employeePersonalDataDTO2.setNoOfTeamMember(employeeRoleBO.getTeamSize());
						
					employeePersonalDataDTO2.setDepartment(employeeRoleBO.getDepartment());
					employeePersonalDataDTO2.setNoOfMail(employeeRoleBO.getConsolidatedTone().getTotalMail());
					
					
					 Double joy =employeeRoleBO.getConsolidatedTone().getToneWithTeam().getAllMailScore().getJoy();
					 Double tnt =employeeRoleBO.getConsolidatedTone().getToneWithTeam().getAllMailScore().getTentative();
					 Double ana =employeeRoleBO.getConsolidatedTone().getToneWithTeam().getAllMailScore().getAnalytical();
					 Double cnfdnc =employeeRoleBO.getConsolidatedTone().getToneWithTeam().getAllMailScore().getConfident();
					 Double anger =employeeRoleBO.getConsolidatedTone().getToneWithTeam().getAllMailScore().getAnger();
					 Double sad =employeeRoleBO.getConsolidatedTone().getToneWithTeam().getAllMailScore().getSadness();
					 Double fear =employeeRoleBO.getConsolidatedTone().getToneWithTeam().getAllMailScore().getFear();
					/* int joyCount = employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getJoyCount();
					 int tntCount = employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getTentativeCount();
					 int anaCount = employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getAnalyticalCount();
					 int cnfdncCount = employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getConfidentCount();
					 int angryCount = employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getAngerCount();
					 int sadCount = employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getSadnessCount();
					 int fearCount = employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getFearCount();*/
					 Double joyness = (double) Math.round(((joy+tnt+ana+cnfdnc)*100)/(joy+tnt+ana+cnfdnc+anger+sad+fear));
					 Double angry =(double) Math.round(((anger+sad+fear)*100)/(joy+tnt+ana+cnfdnc+anger+sad+fear));
					 EmployeeRelationBO employeeRelationBO = new EmployeeRelationBO();
					 employeeRelationBO.setGood(joyness);
					 employeeRelationBO.setBad(angry);
					 employeePersonalDataDTO2.setRelationWithTeam(employeeRelationBO);
					 
			
					 
					 
					 //Relation with Client
					 Double joyClnt =employeeRoleBO.getConsolidatedTone().getToneWithClient().getAllMailScore().getJoy();
					 Double anaClnt =employeeRoleBO.getConsolidatedTone().getToneWithClient().getAllMailScore().getAnalytical();
					 Double tntClnt =employeeRoleBO.getConsolidatedTone().getToneWithClient().getAllMailScore().getTentative();
					 Double confClnt =employeeRoleBO.getConsolidatedTone().getToneWithClient().getAllMailScore().getConfident();
					 Double angerClnt =employeeRoleBO.getConsolidatedTone().getToneWithClient().getAllMailScore().getAnger();
					 Double sadClnt =employeeRoleBO.getConsolidatedTone().getToneWithClient().getAllMailScore().getSadness();
					 Double fearClnt =employeeRoleBO.getConsolidatedTone().getToneWithClient().getAllMailScore().getFear();
					 
					 
					/* int joyclntCount = employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getJoyCount();
					 int angryclntCount = employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getAngerCount();
					 int tntclntCount = employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getTentativeCount();
					 int cnfclntCount = employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getConfidentCount();
					 int anaclntCount = employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getAnalyticalCount();
					 int fearclntCount = employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getFearCount();
					 int sadclntCount = employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getSadnessCount();
					 */
					 Double joynessClnt = (double) Math.round(((joyClnt+anaClnt+tntClnt+confClnt)*100)/(joyClnt+anaClnt+tntClnt+confClnt+angerClnt+sadClnt+fearClnt));
					 Double angryClnt =(double) Math.round(((angerClnt+sadClnt+fearClnt)*100)/(joyClnt+anaClnt+tntClnt+confClnt+angerClnt+sadClnt+fearClnt));
					 EmployeeRelationBO employeeRelationBO1 = new EmployeeRelationBO();
					 employeeRelationBO1.setGood(joynessClnt);
					 employeeRelationBO1.setBad(angryClnt);
					 employeePersonalDataDTO2.setRelationWithClient(employeeRelationBO1);
					 
					
					
				
				if (employeeRoleBO.getReportToId()==null) {
					
					employeePersonalDataDTO2.setReportTo("None");
					
				}else {
					employeePersonalDataDTO2.setReportTo(employeeRepository.findByEmployeeId(employeeRoleBO.getReportToId()).getEmployeeName());
				}
				
					
	
			//get data from dailyEmployeeEmailToneRepository
			
		Aggregation aggregation = Aggregation.newAggregation(
                
				Aggregation.match(Criteria.where("employeeIdFK").is(employeePersonalDataDTO.getEmployeeId()).and("date").gte("2018-04-17")),
				Aggregation.group("employeeIdFK").last("employeeIdFK").as("employeeId").sum("selfTone.allMailScore.anger").as("angerTotal").sum("selfTone.allMailScore.angerCount")
				.as("angerCount").sum("selfTone.allMailScore.joy").as("joyTotal").sum("selfTone.allMailScore.joyCount")
				.as("joyCount").sum("selfTone.allMailScore.sadness").as("sadnessTotal").sum("selfTone.allMailScore.sadnessCount")
				.as("sadnessCount").sum("selfTone.allMailScore.tentative").as("tentativeTotal").sum("selfTone.allMailScore.tentativeCount")
				.as("tentativeCount").sum("selfTone.allMailScore.analytical").as("analyticalTotal").sum("selfTone.allMailScore.analyticalCount")
				.as("analyticalCount").sum("selfTone.allMailScore.confident").as("confidentTotal").sum("selfTone.allMailScore.confidentCount")
				.as("confidentCount").sum("selfTone.allMailScore.fear").as("fearTotal").sum("selfTone.allMailScore.fearCount")
				.as("fearCount")
				.sum("teamTone.allMailScore.anger").as("angerTeamTotal").sum("teamTone.allMailScore.angerCount")
				.as("angerTeamCount").sum("teamTone.allMailScore.joy").as("joyTeamTotal").sum("teamTone.allMailScore.joyCount")
				.as("joyTeamCount").sum("teamTone.allMailScore.sadness").as("sadnessTeamTotal").sum("teamTone.allMailScore.sadnessCount")
				.as("sadnessTeamCount").sum("teamTone.allMailScore.tentative").as("tentativeTeamTotal").sum("teamTone.allMailScore.tentativeCount")
				.as("tentativeTeamCount").sum("teamTone.allMailScore.analytical").as("analyticalTeamTotal").sum("teamTone.allMailScore.analyticalCount")
				.as("analyticalTeamCount").sum("teamTone.allMailScore.confident").as("confidentTeamTotal").sum("teamTone.allMailScore.confidentCount")
				.as("confidentTeamCount").sum("teamTone.allMailScore.fear").as("fearTeamTotal").sum("teamTone.allMailScore.fearCount")
				.as("fearTeamCount")
					);
			
			AggregationResults<AggregationPojo> groupResults 
			= mongoTemplate.aggregate(aggregation, DailyEmployeeEmailToneBO.class, AggregationPojo.class);
			List<AggregationPojo> result = groupResults.getMappedResults();
			
			//Average of all personal data
			Double angerFinal = (double)Math.round(result.get(0).getAngerTotal()/result.get(0).getAngerCount());
			Double joyFinal = (double)Math.round(result.get(0).getJoyTotal()/result.get(0).getJoyCount());
			Double sadnessFinal = (double)Math.round(result.get(0).getSadnessTotal()/result.get(0).getSadnessCount());
			Double tentativeFinal = (double)Math.round(result.get(0).getTentativeTotal()/result.get(0).getTentativeCount());
			Double analyticalFinal = (double)Math.round(result.get(0).getAnalyticalTotal()/result.get(0).getAnalyticalCount());
			Double confidentFinal = (double)Math.round(result.get(0).getConfidentTotal()/result.get(0).getConfidentCount());
			Double fearFinal = (double)Math.round(result.get(0).getFearTotal()/result.get(0).getFearCount());
			

			 // percentage of each tone of personal
			 Double joyper = (double) Math.round(((joyFinal)*100)/(joyFinal+angerFinal+sadnessFinal+tentativeFinal+analyticalFinal+confidentFinal+fearFinal));
			 Double tenper = (double) Math.round(((tentativeFinal)*100)/(joyFinal+angerFinal+sadnessFinal+tentativeFinal+analyticalFinal+confidentFinal+fearFinal));
			 Double confper = (double) Math.round(((confidentFinal)*100)/(joyFinal+angerFinal+sadnessFinal+tentativeFinal+analyticalFinal+confidentFinal+fearFinal));
			 Double anaper = (double) Math.round(((analyticalFinal)*100)/(joyFinal+angerFinal+sadnessFinal+tentativeFinal+analyticalFinal+confidentFinal+fearFinal));
			 Double angper = (double) Math.round(((angerFinal)*100)/(joyFinal+angerFinal+sadnessFinal+tentativeFinal+analyticalFinal+confidentFinal+fearFinal));
			 Double sadper = (double) Math.round(((sadnessFinal)*100)/(joyFinal+angerFinal+sadnessFinal+tentativeFinal+analyticalFinal+confidentFinal+fearFinal));
			 Double fearper = (double) Math.round(((fearFinal)*100)/(joyFinal+angerFinal+sadnessFinal+tentativeFinal+analyticalFinal+confidentFinal+fearFinal));
			
			
			ToneOfMail toneOfMail2 = new ToneOfMail();
			toneOfMail2.setAnger(angper);
			toneOfMail2.setJoy(joyper);
			toneOfMail2.setSadness(sadper);
			toneOfMail2.setTentative(tenper);
			toneOfMail2.setAnalytical(anaper);
			toneOfMail2.setConfident(confper);
			toneOfMail2.setFear(fearper);
			
			employeePersonalDataDTO2.setToneOfPersonalMail(toneOfMail2);
			
			// Average of All Team Scores 
			
			Double angerFinal1 = (double)Math.round(result.get(0).getAngerTeamTotal()/result.get(0).getAngerTeamCount());
			Double joyFinal1 = (double)Math.round(result.get(0).getJoyTeamTotal()/result.get(0).getJoyTeamCount());
			Double sadnessFinal1 = (double)Math.round(result.get(0).getSadnessTeamTotal()/result.get(0).getSadnessTeamCount());
			Double tentativeFinal1 = (double)Math.round(result.get(0).getTentativeTeamTotal()/result.get(0).getTentativeTeamCount());
			Double analyticalFinal1 = (double)Math.round(result.get(0).getAnalyticalTeamTotal()/result.get(0).getAnalyticalTeamCount());
			Double confidentFinal1 = (double)Math.round(result.get(0).getConfidentTeamTotal()/result.get(0).getConfidentTeamCount());
			Double fearFinal1 = (double)Math.round(result.get(0).getFearTeamTotal()/result.get(0).getFearTeamCount());
			
			 // percentage of each tone of team
			 Double joyperT = (double) Math.round(((joyFinal1)*100)/(angerFinal1+joyFinal1+sadnessFinal1+tentativeFinal1+analyticalFinal1+confidentFinal1+fearFinal1));
			 Double tenperT = (double) Math.round(((tentativeFinal1)*100)/(angerFinal1+joyFinal1+sadnessFinal1+tentativeFinal1+analyticalFinal1+confidentFinal1+fearFinal1));
			 Double confperT = (double) Math.round(((confidentFinal1)*100)/(angerFinal1+joyFinal1+sadnessFinal1+tentativeFinal1+analyticalFinal1+confidentFinal1+fearFinal1));
			 Double anaperT = (double) Math.round(((analyticalFinal1)*100)/(angerFinal1+joyFinal1+sadnessFinal1+tentativeFinal1+analyticalFinal1+confidentFinal1+fearFinal1));
			 Double angperT = (double) Math.round(((angerFinal1)*100)/(angerFinal1+joyFinal1+sadnessFinal1+tentativeFinal1+analyticalFinal1+confidentFinal1+fearFinal1));
			 Double sadperT = (double) Math.round(((sadnessFinal1)*100)/(angerFinal1+joyFinal1+sadnessFinal1+tentativeFinal1+analyticalFinal1+confidentFinal1+fearFinal1));
			 Double fearperT = (double) Math.round(((fearFinal1)*100)/(angerFinal1+joyFinal1+sadnessFinal1+tentativeFinal1+analyticalFinal1+confidentFinal1+fearFinal1));
			
			
			ToneOfMail toneOfMail1 = new ToneOfMail();
			toneOfMail1.setAnger(angperT);
			toneOfMail1.setJoy(joyperT);
			toneOfMail1.setSadness(sadperT);
			toneOfMail1.setTentative(tenperT);
			toneOfMail1.setAnalytical(anaperT);
			toneOfMail1.setConfident(confperT);
			toneOfMail1.setFear(fearperT);
			
			employeePersonalDataDTO2.setToneOfTeamMail(toneOfMail1);
			
	
		
		//for mail Pagination
		
		Pageable pageable = new PageRequest(employeePersonalDataDTO.getPageNumber(),100,new Sort(Sort.Direction.ASC,"date"));
		
		Page<DailyEmployeeEmailToneBO> dailyEmployeeEmailToneBOs3 =
				dailyEmployeeEmailToneRepository.findByEmployeeIdFKAndDate(employeePersonalDataDTO.getEmployeeId(),"2018-04-16",pageable);
		List<DailyEmployeeEmailToneBO> dailyEmployeeEmailToneBOs = dailyEmployeeEmailToneBOs3.getContent();
		if (employeePersonalDataDTO.getIdentify().equalsIgnoreCase("all")) {
		for(DailyEmployeeEmailToneBO dailyEmployeeEmailToneBO : dailyEmployeeEmailToneBOs) {
			
			// get each email and their tone
			
						//TeamLevelTonalResultPojo clientTone = dailyEmployeeEmailToneBO.getClientTone();
						List<EmailPojo> listRcv = dailyEmployeeEmailToneBO.getClientEmailItems();
						
						for (EmailPojo emailPojo : listRcv) {
							Map<String,Double> hashMap = new HashMap<String,Double>(); 
							hashMap.put("anger", emailPojo.getAnger());
							hashMap.put("joy", emailPojo.getJoy());
							hashMap.put("sadness", emailPojo.getSadness());
							hashMap.put("tentative", emailPojo.getTentative());
							hashMap.put("analytical", emailPojo.getAnalytical());
							hashMap.put("confident", emailPojo.getConfident());
							hashMap.put("fear", emailPojo.getFear());
							
							  Map<String, Double> sortedMapDsc =	sortByComparator(hashMap, false);
							  int count =0;
							  for (Entry<String, Double> entry : sortedMapDsc.entrySet())
						        {
						            System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
						            
						            
						         
						            switch (entry.getKey()) {
						            
									case "anger":
										if (count<2) {
											emailPojo.setAnger(entry.getValue());
											count++;
											UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
													.getContext().getAuthentication();
											
//											EmployeePersonalDataDTO employeePersonalDataDTO1 = new EmployeePersonalDataDTO();
											
											String emailId = (String) authObj.getUserSessionInformation().get(
													EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
											
											EmployeeBO employeeData =employeeRepository.findOne(emailId);
											
											ClientBO clients= clientRepository.findByEmployeeIdFK(employeeData.getEmployeeId());
											if(clients != null) {
												if(emailPojo.getType().equalsIgnoreCase("Received")) {
													
													if(clients.getClients() != null) {
														for(FilterPojo client: clients.getClients()) {
															if(client.getEmailId().equalsIgnoreCase(emailPojo.getFromMail())) {
																if(client.getExecutive() != null)
																if(client.getExecutive().equalsIgnoreCase("yes")) {
																	emailPojo.setAdverse("yes");
																}
															}
														}
													}
													
												}else if(emailPojo.getType().equalsIgnoreCase("sent")){
													if(clients.getClients() != null) {
														for(FilterPojo client: clients.getClients()) {
															for(String clientEmail: emailPojo.getToClientEmails()) {
																if(client.getEmailId().equalsIgnoreCase(clientEmail)) {
																	if(client.getExecutive() != null)
																	if(client.getExecutive().equalsIgnoreCase("yes")) {
																		emailPojo.setAdverse("yes");
																	}
																}
															}
														}
													}
												}
											}
										}else {
											emailPojo.setAnger(0.0);
											count++;
										}
										
										break;
										
									case "joy":

										if (count<2) {
											emailPojo.setJoy(entry.getValue());
											count++;
										}else {
											emailPojo.setJoy(0.0);
											count++;
										}
										break;	
									case "sadness":
										if (count<2) {
											emailPojo.setSadness(entry.getValue());
											count++;
										}else {
											emailPojo.setSadness(0.0);
											count++;
										}
										break;
										
									case "tentative":
										if (count<2) {
											emailPojo.setTentative(entry.getValue());
											count++;
										}else {
											emailPojo.setTentative(0.0);
											count++;
										}
										break;
																			
									case "analytical":
										if (count<2) {
											emailPojo.setAnalytical(entry.getValue());
											count++;
										}else {
											emailPojo.setAnalytical(0.0);
											count++;
										}
										break;
										
									case "confident":
										if (count<2) {
											emailPojo.setConfident(entry.getValue());
											count++;
										}else {
											emailPojo.setConfident(0.0);
											count++;
										}
										break;
										
									case "fear":
										if (count<2) {
											emailPojo.setFear(entry.getValue());
											count++;
										}else {
											emailPojo.setFear(0.0);
											count++;
										}
										break;

									default:
										break;
										
									}
						            
						          
						        }
							  
							  System.out.println(sortedMapDsc);

							/*Stream<Map.Entry<String,Double>> sorted =
									hashMap.entrySet().stream()
								       .sorted(Entry.comparingByValue());
							
							System.out.println(sorted.toString());
							Map<String, Double> map = sorted.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
							System.out.println(map);*/
							  
							  // date formatter of email
							 /* try {
							  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");
							  Date date = simpleDateFormat.parse(emailPojo.getDate());
							  // new Format
							  SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd-MMM-YYYY");
							  simpleDateFormat2.format(date);
							  emailPojo.setDate(simpleDateFormat2.format(date));
							  }catch (Exception e) {
								// TODO: handle exception
							}*/
							  
							  list.add(emailPojo);
						}
						
						//list.addAll(listRcv);
						
						
			
		}
		
		}
		
		
		//for all received mail
		
		if (employeePersonalDataDTO.getIdentify().equalsIgnoreCase("receive")) {
			
			ProjectionOperation projectionOperation = Aggregation.project()
					.andExpression("clientEmailItems").as("teamEmails")
					.andExclude("_id");
		
		Aggregation aggregation2 = Aggregation.newAggregation(
				
				Aggregation.match(Criteria.where("employeeIdFK").is(employeePersonalDataDTO.getEmployeeId())),
				Aggregation.unwind("clientEmailItems"),
				Aggregation.match(Criteria.where("clientEmailItems.type").is("Received")),
				projectionOperation
				

				);
		
				List<FilterResultPojo> results = mongoTemplate.aggregate(aggregation2, DailyEmployeeEmailToneBO.class, FilterResultPojo.class).getMappedResults();
		
		for(FilterResultPojo basicDBObject : results) {
			
			EmailPojo emailPojo = basicDBObject.getTeamEmails();
			
			Map<String,Double> hashMap = new HashMap<String,Double>(); 
			hashMap.put("anger", emailPojo.getAnger());
			hashMap.put("joy", emailPojo.getJoy());
			hashMap.put("sadness", emailPojo.getSadness());
			hashMap.put("tentative", emailPojo.getTentative());
			hashMap.put("analytical", emailPojo.getAnalytical());
			hashMap.put("confident", emailPojo.getConfident());
			hashMap.put("fear", emailPojo.getFear());
			
			  Map<String, Double> sortedMapDsc =	sortByComparator(hashMap, false);
			  int count =0;
			  for (Entry<String, Double> entry : sortedMapDsc.entrySet())
		        {
		            System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
		            
		            
		         
		            switch (entry.getKey()) {
		            
					case "anger":
						if (count<2) {
							emailPojo.setAnger(entry.getValue());
							count++;
							UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
									.getContext().getAuthentication();
							
//							EmployeePersonalDataDTO employeePersonalDataDTO1 = new EmployeePersonalDataDTO();
							
							String emailId = (String) authObj.getUserSessionInformation().get(
									EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
							
							EmployeeBO employeeData =employeeRepository.findOne(emailId);
							
							ClientBO clients= clientRepository.findByEmployeeIdFK(employeeData.getEmployeeId());
							if(clients != null) {
								if(emailPojo.getType().equalsIgnoreCase("Received")) {
									
									if(clients.getClients() != null) {
										for(FilterPojo client: clients.getClients()) {
											if(client.getEmailId().equalsIgnoreCase(emailPojo.getFromMail())) {
												if(client.getExecutive() != null)
												if(client.getExecutive().equalsIgnoreCase("yes")) {
													emailPojo.setAdverse("yes");
												}
											}
										}
									}
									
								}else if(emailPojo.getType().equalsIgnoreCase("sent")){
									if(clients.getClients() != null) {
										for(FilterPojo client: clients.getClients()) {
											for(String clientEmail: emailPojo.getToClientEmails()) {
												if(client.getEmailId().equalsIgnoreCase(clientEmail)) {
													if(client.getExecutive() != null)
													if(client.getExecutive().equalsIgnoreCase("yes")) {
														emailPojo.setAdverse("yes");
													}
												}
											}
										}
									}
								}
							}
						}else {
							emailPojo.setAnger(0.0);
							count++;
						}
						
						break;
						
					case "joy":

						if (count<2) {
							emailPojo.setJoy(entry.getValue());
							count++;
						}else {
							emailPojo.setJoy(0.0);
							count++;
						}
						break;	
					case "sadness":
						if (count<2) {
							emailPojo.setSadness(entry.getValue());
							count++;
						}else {
							emailPojo.setSadness(0.0);
							count++;
						}
						break;
						
					case "tentative":
						if (count<2) {
							emailPojo.setTentative(entry.getValue());
							count++;
						}else {
							emailPojo.setTentative(0.0);
							count++;
						}
						break;
															
					case "analytical":
						if (count<2) {
							emailPojo.setAnalytical(entry.getValue());
							count++;
						}else {
							emailPojo.setAnalytical(0.0);
							count++;
						}
						break;
						
					case "confident":
						if (count<2) {
							emailPojo.setConfident(entry.getValue());
							count++;
						}else {
							emailPojo.setConfident(0.0);
							count++;
						}
						break;
						
					case "fear":
						if (count<2) {
							emailPojo.setFear(entry.getValue());
							count++;
						}else {
							emailPojo.setFear(0.0);
							count++;
						}
						break;

					default:
						break;
						
					}
		            
		          
		        }
			  
			  System.out.println(sortedMapDsc);
			  
			/*  try {
				  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");
				  Date date = simpleDateFormat.parse(emailPojo.getDate());
				  // new Format
				  SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd-MMM-YYYY");
				  simpleDateFormat2.format(date);
				  emailPojo.setDate(simpleDateFormat2.format(date));
				  }catch (Exception e) {
					// TODO: handle exception
				}*/

			  list.add(emailPojo);
		

			
			//list.add(listRcv);
			
		}
		
		}
		
		//for all sent mail
		
				if (employeePersonalDataDTO.getIdentify().equalsIgnoreCase("sent")) {
					
					ProjectionOperation projectionOperation = Aggregation.project()
							.andExpression("clientEmailItems").as("teamEmails")
							.andExclude("_id");
				
				
				Aggregation aggregation2 = Aggregation.newAggregation(
						
						Aggregation.match(Criteria.where("employeeIdFK").is(employeePersonalDataDTO.getEmployeeId())),
						Aggregation.unwind("clientEmailItems"),
						Aggregation.match(Criteria.where("clientEmailItems.type").is("sent")),
						projectionOperation
						
						
						);
				
						List<FilterResultPojo> results = mongoTemplate.aggregate(aggregation2, DailyEmployeeEmailToneBO.class, FilterResultPojo.class).getMappedResults();
				
				for(FilterResultPojo basicDBObject : results) {
					
					EmailPojo emailPojo = basicDBObject.getTeamEmails();
					Map<String,Double> hashMap = new HashMap<String,Double>(); 
					hashMap.put("anger", emailPojo.getAnger());
					hashMap.put("joy", emailPojo.getJoy());
					hashMap.put("sadness", emailPojo.getSadness());
					hashMap.put("tentative", emailPojo.getTentative());
					hashMap.put("analytical", emailPojo.getAnalytical());
					hashMap.put("confident", emailPojo.getConfident());
					hashMap.put("fear", emailPojo.getFear());
					
					  Map<String, Double> sortedMapDsc =	sortByComparator(hashMap, false);
					  int count =0;
					  for (Entry<String, Double> entry : sortedMapDsc.entrySet())
				        {
				            System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
				            
				            
				         
				            switch (entry.getKey()) {
				            
							case "anger":
								if (count<2) {
									emailPojo.setAnger(entry.getValue());
									count++;
									UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
											.getContext().getAuthentication();
									
//									EmployeePersonalDataDTO employeePersonalDataDTO1 = new EmployeePersonalDataDTO();
									
									String emailId = (String) authObj.getUserSessionInformation().get(
											EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
									
									EmployeeBO employeeData =employeeRepository.findOne(emailId);
									
									ClientBO clients= clientRepository.findByEmployeeIdFK(employeeData.getEmployeeId());
									if(clients != null) {
										if(emailPojo.getType().equalsIgnoreCase("Received")) {
											
											if(clients.getClients() != null) {
												for(FilterPojo client: clients.getClients()) {
													if(client.getEmailId().equalsIgnoreCase(emailPojo.getFromMail())) {
														if(client.getExecutive() != null)
														if(client.getExecutive().equalsIgnoreCase("yes")) {
															emailPojo.setAdverse("yes");
														}
													}
												}
											}
											
										}else if(emailPojo.getType().equalsIgnoreCase("sent")){
											if(clients.getClients() != null) {
												for(FilterPojo client: clients.getClients()) {
													for(String clientEmail: emailPojo.getToClientEmails()) {
														if(client.getEmailId().equalsIgnoreCase(clientEmail)) {
															if(client.getExecutive() != null)
															if(client.getExecutive().equalsIgnoreCase("yes")) {
																emailPojo.setAdverse("yes");
															}
														}
													}
												}
											}
										}
									}
								}else {
									emailPojo.setAnger(0.0);
									count++;
								}
								
								break;
								
							case "joy":

								if (count<2) {
									emailPojo.setJoy(entry.getValue());
									count++;
								}else {
									emailPojo.setJoy(0.0);
									count++;
								}
								break;	
							case "sadness":
								if (count<2) {
									emailPojo.setSadness(entry.getValue());
									count++;
								}else {
									emailPojo.setSadness(0.0);
									count++;
								}
								break;
								
							case "tentative":
								if (count<2) {
									emailPojo.setTentative(entry.getValue());
									count++;
								}else {
									emailPojo.setTentative(0.0);
									count++;
								}
								break;
																	
							case "analytical":
								if (count<2) {
									emailPojo.setAnalytical(entry.getValue());
									count++;
								}else {
									emailPojo.setAnalytical(0.0);
									count++;
								}
								break;
								
							case "confident":
								if (count<2) {
									emailPojo.setConfident(entry.getValue());
									count++;
								}else {
									emailPojo.setConfident(0.0);
									count++;
								}
								break;
								
							case "fear":
								if (count<2) {
									emailPojo.setFear(entry.getValue());
									count++;
								}else {
									emailPojo.setFear(0.0);
									count++;
								}
								break;

							default:
								break;
								
							}
				            
				          
				        }
					  
					  System.out.println(sortedMapDsc);
					  
					 /* try {
						  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");
						  Date date = simpleDateFormat.parse(emailPojo.getDate());
						  // new Format
						  SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd-MMM-YYYY");
						  simpleDateFormat2.format(date);
						  emailPojo.setDate(simpleDateFormat2.format(date));
						  }catch (Exception e) {
							// TODO: handle exception
						}*/

					  list.add(emailPojo);
					
				}
				
				}
				
		
		employeePersonalDataDTO2.setListEmailAnalyser(list);
		
		return employeePersonalDataDTO2;
		
	}
	
	
	
	// for sorting number
	
	
	 private static Map<String, Double> sortByComparator(Map<String, Double> unsortMap, final boolean order)
	    {

	        List<Entry<String, Double>> list = new LinkedList<Entry<String, Double>>(unsortMap.entrySet());

	        Map<String, Double> emotionUnsortedMap = new LinkedHashMap<String, Double>();
	        Map<String, Double> emotionlessUnsortedMap = new LinkedHashMap<String, Double>();
	        
	        for (Entry<String, Double> entry : list)
	        {
	        	if(entry.getKey().equals("anger") ||entry.getKey().equals("joy") || entry.getKey().equals("sadness") || entry.getKey().equals("fear")) {
	        		emotionUnsortedMap.put(entry.getKey(), entry.getValue());
	        	}else {
	        		emotionlessUnsortedMap.put(entry.getKey(), entry.getValue());
	        	}
	        }
	        
	        List<Entry<String, Double>> emotionlist = new LinkedList<Entry<String, Double>>(emotionUnsortedMap.entrySet());
	        List<Entry<String, Double>> emotionlesslist = new LinkedList<Entry<String, Double>>(emotionlessUnsortedMap.entrySet());
	        
	        System.err.println("Emotion Tone :- " + emotionUnsortedMap.entrySet());
	        System.err.println("Emotionless Tone :- " + emotionlessUnsortedMap.entrySet());
	        
	        // Sorting the list based on values
	        Collections.sort(emotionlist, new Comparator<Entry<String, Double>>()
	        {
	            public int compare(Entry<String, Double> o1,
	                    Entry<String, Double> o2)
	            {
	                if (order)
	                {
	                    return o1.getValue().compareTo(o2.getValue());
	                }
	                else
	                {
	                    return o2.getValue().compareTo(o1.getValue());

	                }
	            }
	        });
	        
	        
	        
	        //sorting of emotionless list
	        Collections.sort(emotionlesslist, new Comparator<Entry<String, Double>>()
	        {
	            public int compare(Entry<String, Double> o1,
	                    Entry<String, Double> o2)
	            {
	                if (order)
	                {
	                    return o1.getValue().compareTo(o2.getValue());
	                }
	                else
	                {
	                    return o2.getValue().compareTo(o1.getValue());

	                }
	            }
	        });
	        
	        

	        // Maintaining insertion order with the help of LinkedList
	        Map<String, Double> sortedMap = new LinkedHashMap<String, Double>();
//	        for (Entry<String, Double> entry : list)
//	        {
//	            sortedMap.put(entry.getKey(), entry.getValue());
//	        }
	        
	        if(emotionlist.get(0).getValue().equals(0d) && !emotionlesslist.get(0).getValue().equals(0d)) {
	        	for (Entry<String, Double> entry : emotionlesslist)
	    	        {
	    	            sortedMap.put(entry.getKey(), entry.getValue());
	    	        }
	        	for (Entry<String, Double> entry : emotionlist)
    	        {
    	            sortedMap.put(entry.getKey(), entry.getValue());
    	        }
	        }else if(!emotionlist.get(0).getValue().equals(0d) && emotionlesslist.get(0).getValue().equals(0d)) {
	        	for (Entry<String, Double> entry : emotionlist)
    	        {
    	            sortedMap.put(entry.getKey(), entry.getValue());
    	        }
	        	for (Entry<String, Double> entry : emotionlesslist)
    	        {
    	            sortedMap.put(entry.getKey(), entry.getValue());
    	        }
	        }else  {
	        	for(int i=0; i<3;i++) {
	        		if(emotionlist.get(i).getValue() > emotionlesslist.get(i).getValue()) {
	        			sortedMap.put(emotionlist.get(i).getKey(), emotionlist.get(i).getValue());
	        			sortedMap.put(emotionlesslist.get(i).getKey(), emotionlesslist.get(i).getValue());
	        		}else if(emotionlist.get(i).getValue() < emotionlesslist.get(i).getValue()) {
	        			sortedMap.put(emotionlesslist.get(i).getKey(), emotionlesslist.get(i).getValue());
	        			sortedMap.put(emotionlist.get(i).getKey(), emotionlist.get(i).getValue());
	        		}else {
	        			sortedMap.put(emotionlist.get(i).getKey(), emotionlist.get(i).getValue());
	        			sortedMap.put(emotionlesslist.get(i).getKey(), emotionlesslist.get(i).getValue());
	        		}
	        	}
	        	sortedMap.put(emotionlist.get(3).getKey(), emotionlist.get(3).getValue());
	        }
	        System.err.println("Sorted Tone"+sortedMap.entrySet());
	        return sortedMap;
	    }

	    public static void printMap(Map<String, Double> map)
	    {
	        for (Entry<String, Double> entry : map.entrySet())
	        {
	            System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
	        }
	    }
	
	
	
	public EmployeePersonalDataDTO getDashBoardData() {
		
		UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
				.getContext().getAuthentication();
		String pk = (String) authObj.getUserSessionInformation().get(
				EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
		
		/*String empId = (String) authObj.getUserSessionInformation().get(
				EnumeratedTypes.UserSessionData.RELATED_PROFILE_ID);*/
		
		EmployeePersonalDataDTO employeePersonalDataDTO = new EmployeePersonalDataDTO();
		EmployeeBO employeeData = employeeRepository.findOne(pk);
		EmployeeRoleBO employeeRolesPojo = employeeRoleRepository.findByEmployeeIdFKAndStatus(employeeData.getEmployeeId(), "active");
	
				
				 employeePersonalDataDTO.setNoOfTeamMember(employeeRolesPojo.getTeamSize());
				// employeePersonalDataDTO.setConsolidatedTone(employeeRolesPojo.getConsolidatedTone());
				/* employeePersonalDataDTO.setToneOfTeamMail(employeeRolesPojo.getConsolidatedTone().getToneWithTeam()
						 .getAllMailScore());
				 employeePersonalDataDTO.setToneOfClientMail(employeeRolesPojo.getConsolidatedTone().getToneWithClient()
						 .getAllMailScore());*/
				 employeePersonalDataDTO.setDepartment(employeeRolesPojo.getDepartment());
				 employeePersonalDataDTO.setDesignation(employeeRolesPojo.getDesignation());
				 employeePersonalDataDTO.setNoOfMail(employeeRolesPojo.getConsolidatedTone().getTotalMail());
				 //Relation with team
				 Double joy =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getJoy();
				 Double tnt =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getTentative();
				 Double ana =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getAnalytical();
				 Double cnfdnc =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getConfident();
				 Double anger =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getAnger();
				 Double sad =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getSadness();
				 Double fear =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getFear();
				/* int joyCount = employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getJoyCount();
				 int tntCount = employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getTentativeCount();
				 int anaCount = employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getAnalyticalCount();
				 int cnfdncCount = employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getConfidentCount();
				 int angryCount = employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getAngerCount();
				 int sadCount = employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getSadnessCount();
				 int fearCount = employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getFearCount();*/
				 Double joyness = (double) Math.round(((joy+tnt+ana+cnfdnc)*100)/(joy+tnt+ana+cnfdnc+anger+sad+fear));
				 Double angry =(double) Math.round(((anger+sad+fear)*100)/(joy+tnt+ana+cnfdnc+anger+sad+fear));
				 EmployeeRelationBO employeeRelationBO = new EmployeeRelationBO();
				 employeeRelationBO.setGood(joyness);
				 employeeRelationBO.setBad(angry);
				 employeePersonalDataDTO.setRelationWithTeam(employeeRelationBO);
				 
				 // percentage of each tone of employee
				 Double joyper = (double) Math.round(((joy)*100)/(joy+tnt+ana+cnfdnc+anger+sad+fear));
				 Double tenper = (double) Math.round(((tnt)*100)/(joy+tnt+ana+cnfdnc+anger+sad+fear));
				 Double confper = (double) Math.round(((cnfdnc)*100)/(joy+tnt+ana+cnfdnc+anger+sad+fear));
				 Double anaper = (double) Math.round(((ana)*100)/(joy+tnt+ana+cnfdnc+anger+sad+fear));
				 Double angper = (double) Math.round(((anger)*100)/(joy+tnt+ana+cnfdnc+anger+sad+fear));
				 Double sadper = (double) Math.round(((sad)*100)/(joy+tnt+ana+cnfdnc+anger+sad+fear));
				 Double fearper = (double) Math.round(((fear)*100)/(joy+tnt+ana+cnfdnc+anger+sad+fear));
				 ToneOfMail toneOfMail = new ToneOfMail();
				 toneOfMail.setAnalytical(anaper);
				 toneOfMail.setAnger(angper);
				 toneOfMail.setJoy(joyper);
				 toneOfMail.setTentative(tenper);
				 toneOfMail.setConfident(confper);
				 toneOfMail.setFear(fearper);
				 toneOfMail.setSadness(sadper);
				 employeePersonalDataDTO.setToneOfTeamMail(toneOfMail);
				 
				 
				 //Relation with Client
				 Double joyClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getJoy();
				 Double anaClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getAnalytical();
				 Double tntClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getTentative();
				 Double confClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getConfident();
				 Double angerClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getAnger();
				 Double sadClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getSadness();
				 Double fearClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getFear();
				 
				 
				/* int joyclntCount = employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getJoyCount();
				 int angryclntCount = employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getAngerCount();
				 int tntclntCount = employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getTentativeCount();
				 int cnfclntCount = employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getConfidentCount();
				 int anaclntCount = employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getAnalyticalCount();
				 int fearclntCount = employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getFearCount();
				 int sadclntCount = employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getSadnessCount();
				 */
				 Double joynessClnt = (double) Math.round(((joyClnt+anaClnt+tntClnt+confClnt)*100)/(joyClnt+anaClnt+tntClnt+confClnt+angerClnt+sadClnt+fearClnt));
				 Double angryClnt =(double) Math.round(((angerClnt+sadClnt+fearClnt)*100)/(joyClnt+anaClnt+tntClnt+confClnt+angerClnt+sadClnt+fearClnt));
				 EmployeeRelationBO employeeRelationBO1 = new EmployeeRelationBO();
				 employeeRelationBO1.setGood(joynessClnt);
				 employeeRelationBO1.setBad(angryClnt);
				 employeePersonalDataDTO.setRelationWithClient(employeeRelationBO1);
				 
				 // percentage of each tone of client
				 Double joyClntper = (double) Math.round(((joyClnt)*100)/(joyClnt+anaClnt+tntClnt+confClnt+angerClnt+sadClnt+fearClnt));
				 Double tntClntper = (double) Math.round(((tntClnt)*100)/(joyClnt+anaClnt+tntClnt+confClnt+angerClnt+sadClnt+fearClnt));
				 Double confClntper = (double) Math.round(((confClnt)*100)/(joyClnt+anaClnt+tntClnt+confClnt+angerClnt+sadClnt+fearClnt));
				 Double anaClntper = (double) Math.round(((anaClnt)*100)/(joyClnt+anaClnt+tntClnt+confClnt+angerClnt+sadClnt+fearClnt));
				 Double angerClntper = (double) Math.round(((angerClnt)*100)/(joyClnt+anaClnt+tntClnt+confClnt+angerClnt+sadClnt+fearClnt));
				 Double sadClntper = (double) Math.round(((sadClnt)*100)/(joyClnt+anaClnt+tntClnt+confClnt+angerClnt+sadClnt+fearClnt));
				 Double fearClntper = (double) Math.round(((fearClnt)*100)/(joyClnt+anaClnt+tntClnt+confClnt+angerClnt+sadClnt+fearClnt));
				 
				 
				 ToneOfMail toneOfMail1 = new ToneOfMail();
				 toneOfMail1.setAnalytical(anaClntper);
				 toneOfMail1.setAnger(angerClntper);
				 toneOfMail1.setJoy(joyClntper);
				 toneOfMail1.setTentative(tntClntper);
				 toneOfMail1.setConfident(confClntper);
				 toneOfMail1.setFear(fearClntper);
				 toneOfMail1.setSadness(sadClntper);
				 employeePersonalDataDTO.setToneOfClientMail(toneOfMail1);
				 
				 
				
			//}
		//}
		
		employeePersonalDataDTO.setEmployeeName(employeeData.getEmployeeName());
		employeePersonalDataDTO.setEmployeeId(employeeData.getEmployeeId());
		employeePersonalDataDTO.setEmailId(employeeData.getEmailId());
		
		if(employeeRolesPojo.getRole()==null) {
			employeePersonalDataDTO.setRole("n/a");
		}else {
			employeePersonalDataDTO.setRole(employeeRolesPojo.getRole());
		}

		if (employeeRolesPojo.getReportToId()==null) {
			
			employeePersonalDataDTO.setReportTo("None");
			
		}else {
		employeePersonalDataDTO.setReportTo(employeeRepository.findByEmployeeId(employeeRolesPojo.getReportToId()).getEmployeeName());
		}
		
	
		 
		 
		
		return employeePersonalDataDTO;
		
	}
	
	public EmployeePersonalDataDTO getDashBoardData1(EmployeePersonalDataDTO employeePersonalData) {
			
			UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
					.getContext().getAuthentication();
			String pk = (String) authObj.getUserSessionInformation().get(
					EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
			
			/*String empId = (String) authObj.getUserSessionInformation().get(
					EnumeratedTypes.UserSessionData.RELATED_PROFILE_ID);*/
			
			EmployeePersonalDataDTO employeePersonalDataDTO = new EmployeePersonalDataDTO();
			EmployeeBO employeeData = employeeRepository.findOne(pk);
			EmployeeRoleBO employeeRolesPojo = employeeRoleRepository.findByEmployeeIdFKAndStatus(employeeData.getEmployeeId(), "active");
		
					
					 employeePersonalDataDTO.setNoOfTeamMember(employeeRolesPojo.getTeamSize());
					// employeePersonalDataDTO.setConsolidatedTone(employeeRolesPojo.getConsolidatedTone());
					/* employeePersonalDataDTO.setToneOfTeamMail(employeeRolesPojo.getConsolidatedTone().getToneWithTeam()
							 .getAllMailScore());
					 employeePersonalDataDTO.setToneOfClientMail(employeeRolesPojo.getConsolidatedTone().getToneWithClient()
							 .getAllMailScore());*/
					 employeePersonalDataDTO.setDepartment(employeeRolesPojo.getDepartment());
					 employeePersonalDataDTO.setDesignation(employeeRolesPojo.getDesignation());
					 employeePersonalDataDTO.setNoOfMail(employeeRolesPojo.getConsolidatedTone().getTotalMail());
					 //Relation with team
					 Double joy =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getJoy();
					 Double tnt =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getTentative();
					 Double ana =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getAnalytical();
					 Double cnfdnc =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getConfident();
					 Double anger =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getAnger();
					 Double sad =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getSadness();
					 Double fear =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getFear();
					/* int joyCount = employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getJoyCount();
					 int tntCount = employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getTentativeCount();
					 int anaCount = employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getAnalyticalCount();
					 int cnfdncCount = employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getConfidentCount();
					 int angryCount = employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getAngerCount();
					 int sadCount = employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getSadnessCount();
					 int fearCount = employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getFearCount();*/
					 Double total = joy+tnt+ana+cnfdnc+anger+sad+fear;
					 Double joyness = 0.0d;
					 Double angry = 0.0d;
					 if(total > 0) {
					 joyness = (double) Math.round(((joy+tnt+ana+cnfdnc)*100)/total);
					 angry =(double) Math.round(((anger+sad+fear)*100)/total);
					 }
					 EmployeeRelationBO employeeRelationBO = new EmployeeRelationBO();
					 employeeRelationBO.setGood(joyness);
					 employeeRelationBO.setBad(angry);
					 employeePersonalDataDTO.setRelationWithTeam(employeeRelationBO);
					 
					 // percentage of each tone of employee
					 Double joyper = (double) Math.round(((joy)*100)/(joy+tnt+ana+cnfdnc+anger+sad+fear));
					 Double tenper = (double) Math.round(((tnt)*100)/(joy+tnt+ana+cnfdnc+anger+sad+fear));
					 Double confper = (double) Math.round(((cnfdnc)*100)/(joy+tnt+ana+cnfdnc+anger+sad+fear));
					 Double anaper = (double) Math.round(((ana)*100)/(joy+tnt+ana+cnfdnc+anger+sad+fear));
					 Double angper = (double) Math.round(((anger)*100)/(joy+tnt+ana+cnfdnc+anger+sad+fear));
					 Double sadper = (double) Math.round(((sad)*100)/(joy+tnt+ana+cnfdnc+anger+sad+fear));
					 Double fearper = (double) Math.round(((fear)*100)/(joy+tnt+ana+cnfdnc+anger+sad+fear));
					 ToneOfMail toneOfMail = new ToneOfMail();
					 toneOfMail.setAnalytical(anaper);
					 toneOfMail.setAnger(angper);
					 toneOfMail.setJoy(joyper);
					 toneOfMail.setTentative(tenper);
					 toneOfMail.setConfident(confper);
					 toneOfMail.setFear(fearper);
					 toneOfMail.setSadness(sadper);
					 employeePersonalDataDTO.setToneOfTeamMail(toneOfMail);
					 
					 
					 OrganisationBO org = companyRepository.findByCompanyName(employeePersonalData.getCompanyName());
					 
					 
					 
					 //Relation with Client
					 ProjectionOperation projectionOperation1 = Aggregation.project()
								.andExpression("clients").as("teamName")
								.andExclude("_id");
						
						Aggregation aggregation2 = Aggregation.newAggregation(
				                
								Aggregation.match(Criteria.where("employeeIdFK").is(employeeData.getEmployeeId())),
								Aggregation.unwind("$companyList"),
								Aggregation.match(Criteria.where("companyList.companyId").is(org.getId())),
								//projectionOperation1,
								Aggregation.group("employeeIdFK")
								.sum("companyList.allMailScore.anger").as("angerTeamTotal").sum("companyList.allMailScore.angerCount")
								.as("angerTeamCount").sum("companyList.allMailScore.joy").as("joyTeamTotal").sum("companyList.allMailScore.joyCount")
								.as("joyTeamCount").sum("companyList.allMailScore.sadness").as("sadnessTeamTotal").sum("companyList.allMailScore.sadnessCount")
								.as("sadnessTeamCount").sum("companyList.allMailScore.tentative").as("tentativeTeamTotal").sum("companyList.allMailScore.tentativeCount")
								.as("tentativeTeamCount").sum("companyList.allMailScore.analytical").as("analyticalTeamTotal").sum("companyList.allMailScore.analyticalCount")
								.as("analyticalTeamCount").sum("companyList.allMailScore.confident").as("confidentTeamTotal").sum("companyList.allMailScore.confidentCount")
								.as("confidentTeamCount").sum("companyList.allMailScore.fear").as("fearTeamTotal").sum("companyList.allMailScore.fearCount")
								.as("fearTeamCount")
								.sum("companyList.receiveMailScore.anger").as("angerTeamTotalrcv").sum("companyList.receiveMailScore.angerCount")
								.as("angerTeamCountrcv").sum("companyList.receiveMailScore.joy").as("joyTeamTotalrcv").sum("companyList.receiveMailScore.joyCount")
								.as("joyTeamCountrcv").sum("companyList.receiveMailScore.sadness").as("sadnessTeamTotalrcv").sum("companyList.receiveMailScore.sadnessCount")
								.as("sadnessTeamCountrcv").sum("companyList.receiveMailScore.tentative").as("tentativeTeamTotalrcv").sum("companyList.receiveMailScore.tentativeCount")
								.as("tentativeTeamCountrcv").sum("companyList.receiveMailScore.analytical").as("analyticalTeamTotalrcv").sum("companyList.receiveMailScore.analyticalCount")
								.as("analyticalTeamCountrcv").sum("companyList.receiveMailScore.confident").as("confidentTeamTotalrcv").sum("companyList.receiveMailScore.confidentCount")
								.as("confidentTeamCountrcv").sum("companyList.receiveMailScore.fear").as("fearTeamTotalrcv").sum("companyList.receiveMailScore.fearCount")
								.as("fearTeamCountrcv")
								.sum("companyList.sentMailScore.anger").as("angerTeamTotalsnt").sum("companyList.sentMailScore.angerCount")
								.as("angerTeamCountsnt").sum("companyList.sentMailScore.joy").as("joyTeamTotalsnt").sum("companyList.sentMailScore.joyCount")
								.as("joyTeamCountsnt").sum("companyList.sentMailScore.sadness").as("sadnessTeamTotalsnt").sum("companyList.sentMailScore.sadnessCount")
								.as("sadnessTeamCountsnt").sum("companyList.sentMailScore.tentative").as("tentativeTeamTotalsnt").sum("companyList.sentMailScore.tentativeCount")
								.as("tentativeTeamCountsnt").sum("companyList.sentMailScore.analytical").as("analyticalTeamTotalsnt").sum("companyList.sentMailScore.analyticalCount")
								.as("analyticalTeamCountsnt").sum("companyList.sentMailScore.confident").as("confidentTeamTotalsnt").sum("companyList.sentMailScore.confidentCount")
								.as("confidentTeamCountsnt").sum("companyList.sentMailScore.fear").as("fearTeamTotalsnt").sum("companyList.sentMailScore.fearCount")
								.as("fearTeamCountsnt")
								.sum("companyList.totalMail").as("totalTeamMail")
								.sum("companyList.totalMailRecevied").as("totalTeamMailRecevied")
								.sum("companyList.totalMailSent").as("totalTeamMailSent")
								
									);
							
							AggregationResults<AggregationPojo> groupResults 
							= mongoTemplate.aggregate(aggregation2, TeamClientInteractionBO.class, AggregationPojo.class);
							List<AggregationPojo> result = groupResults.getMappedResults();
							
						//	clientCompany Team Scores
							Double angerClnt = 0.0d;
							Double joyClnt = 0.0d;
							Double sadClnt = 0.0d;
							Double tntClnt = 0.0d;
							Double anaClnt = 0.0d;
							Double confClnt = 0.0d;
							Double fearClnt = 0.0d;
							try {
								if(result.isEmpty() != true)
								if(result.get(0).getAngerTeamCount() != 0.0d) {
									angerClnt = result.get(0).getAngerTeamTotal()/result.get(0).getAngerTeamCount(); 
								}
							}catch(Exception e) {
								System.err.println(e);
								angerClnt = 0.0d;
								System.out.println("in company all");
							}
							try {
								if(result.isEmpty() != true)
								if(result.get(0).getJoyTeamCount() !=0.0d)
									joyClnt = result.get(0).getJoyTeamTotal()/result.get(0).getJoyTeamCount();
							}catch(Exception e) {
								System.err.println(e);
								joyClnt = 0.0d;
								System.out.println("in company all");
							}
							try {
								if(result.isEmpty() != true)
								if(result.get(0).getSadnessTeamCount() != 0.0d)
									sadClnt = result.get(0).getSadnessTeamTotal()/result.get(0).getSadnessTeamCount();
							}catch(Exception e) {
								System.err.println(e);
								sadClnt = 0.0d;
								System.out.println("in company all");
							}
							try {
								if(result.isEmpty() != true)
								if(result.get(0).getTentativeTeamCount() != 0.0d)
									tntClnt = result.get(0).getTentativeTeamTotal()/result.get(0).getTentativeTeamCount();
							}catch(Exception e) {
								System.err.println(e);
								tntClnt = 0.0d;
								System.out.println("in company all");
							}
							try {
								if(result.isEmpty() != true)
								if(result.get(0).getAnalyticalTeamCount() != 0.0d)
									anaClnt = result.get(0).getAnalyticalTeamTotal()/result.get(0).getAnalyticalTeamCount();
							}catch(Exception e) {
								System.err.println(e);
								anaClnt = 0.0d;
								System.out.println("in company all");
							}
							try {
								if(result.isEmpty() != true)
								if(result.get(0).getConfidentTeamCount() != 0.0d)
									confClnt = result.get(0).getConfidentTeamTotal()/result.get(0).getConfidentTeamCount();
							}catch(Exception e) {
								System.err.println(e);
								confClnt = 0.0d;
								System.out.println("in company all");
							}
							try {
								if(result.isEmpty() != true)
								if(result.get(0).getFearTeamCount() != 0.0d)
									fearClnt = result.get(0).getFearTeamTotal()/result.get(0).getFearTeamCount();
							}catch(Exception e) {
								System.err.println(e);
								fearClnt = 0.0d;
								System.out.println("in company all");
							}
//					 Double joyClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getJoy();
//					 Double anaClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getAnalytical();
//					 Double tntClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getTentative();
//					 Double confClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getConfident();
//					 Double angerClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getAnger();
//					 Double sadClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getSadness();
//					 Double fearClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getFear();
					 
					 
					/* int joyclntCount = employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getJoyCount();
					 int angryclntCount = employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getAngerCount();
					 int tntclntCount = employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getTentativeCount();
					 int cnfclntCount = employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getConfidentCount();
					 int anaclntCount = employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getAnalyticalCount();
					 int fearclntCount = employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getFearCount();
					 int sadclntCount = employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getSadnessCount();
					 */
					 Double totalClnt = joyClnt+anaClnt+tntClnt+confClnt+angerClnt+sadClnt+fearClnt; 
					 Double joynessClnt = 0.0d;
					 Double angryClnt = 0.0d;
					 if(totalClnt > 0) {
					 joynessClnt = (double) Math.round(((joyClnt+anaClnt+tntClnt+confClnt)*100)/totalClnt);
					 angryClnt =(double) Math.round(((angerClnt+sadClnt+fearClnt)*100)/totalClnt);
					 }
					 EmployeeRelationBO employeeRelationBO1 = new EmployeeRelationBO();
					 employeeRelationBO1.setGood(joynessClnt);
					 employeeRelationBO1.setBad(angryClnt);
					 employeePersonalDataDTO.setRelationWithClient(employeeRelationBO1);
					 
					 // percentage of each tone of client
					 
					 Double joyClntper = 0.0d;
					 Double tntClntper = 0.0d;
					 Double confClntper = 0.0d;
					 Double anaClntper = 0.0d;
					 Double angerClntper = 0.0d;
					 Double sadClntper = 0.0d;
					 Double fearClntper = 0.0d;
					 
					 
					 if(totalClnt > 0) {
					 joyClntper = (double) Math.round(((joyClnt)*100)/totalClnt);
					 tntClntper = (double) Math.round(((tntClnt)*100)/totalClnt);
					 confClntper = (double) Math.round(((confClnt)*100)/totalClnt);
					 anaClntper = (double) Math.round(((anaClnt)*100)/totalClnt);
					 angerClntper = (double) Math.round(((angerClnt)*100)/totalClnt);
					 sadClntper = (double) Math.round(((sadClnt)*100)/totalClnt);
					 fearClntper = (double) Math.round(((fearClnt)*100)/totalClnt);
					 }
					 
					 ToneOfMail toneOfMail1 = new ToneOfMail();
					 toneOfMail1.setAnalytical(anaClntper);
					 toneOfMail1.setAnger(angerClntper);
					 toneOfMail1.setJoy(joyClntper);
					 toneOfMail1.setTentative(tntClntper);
					 toneOfMail1.setConfident(confClntper);
					 toneOfMail1.setFear(fearClntper);
					 toneOfMail1.setSadness(sadClntper);
					 employeePersonalDataDTO.setToneOfClientMail(toneOfMail1);
					 
					 
					
				//}
			//}
			
			employeePersonalDataDTO.setEmployeeName(employeeData.getEmployeeName());
			employeePersonalDataDTO.setEmployeeId(employeeData.getEmployeeId());
	
			if (employeeRolesPojo.getReportToId()==null) {
				
				employeePersonalDataDTO.setReportTo("None");
				
			}else {
			employeePersonalDataDTO.setReportTo(employeeRepository.findByEmployeeId(employeeRolesPojo.getReportToId()).getEmployeeName());
			}
			
		
			 
			 
			
			return employeePersonalDataDTO;
			
		}
	


	
	//list of immediate  employee Reportee
	
	public EmployeePersonalDataDTO getListOfEmployee(ClientDataDTO clientDataDTO){
		
		UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
				.getContext().getAuthentication();
		
		EmployeePersonalDataDTO employeePersonalDataDTO = new EmployeePersonalDataDTO();
		
		String emailId = (String) authObj.getUserSessionInformation().get(
				EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
		
				EmployeeBO employeeData =employeeRepository.findOne(emailId);
				
				EmployeeRoleBO employeeRoleBO = employeeRoleRepository.findByEmployeeIdFKAndStatus(employeeData.getEmployeeId(),
						"active");
				
				employeePersonalDataDTO.setDesignation(employeeRoleBO.getDesignation());
				employeePersonalDataDTO.setEmployeeName(employeeData.getEmployeeName());
				employeePersonalDataDTO.setEmployeeId(employeeData.getEmployeeId());
				/*List<EmployeeRoleBO> employeeRoleBOs = employeeRoleRepository.findByReportToIdAndFromDate(employeeData.getEmployeeId(),
						"2018-04-17");*/
			
			
			
			List<EmployeePersonalDataDTO> employeeResult = new ArrayList<EmployeePersonalDataDTO>();
			/*for(EmployeeRoleBO employeeRoleBO2 : employeeRoleBOs) {
				
				EmployeeBO employeeData1 =employeeRepository.findByEmployeeId(employeeRoleBO2.getEmployeeIdFK());
				EmployeePersonalDataDTO emp = new EmployeePersonalDataDTO();
				emp.setEmployeeId(employeeData1.getEmployeeId());
				emp.setEmployeeName(employeeData1.getEmployeeName());
				emp.setDesignation(employeeRoleBO2.getDesignation());
					
				
				employeeResult.add(emp);
				
			}
			
			employeePersonalDataDTO.setListOfEmployee(employeeResult);
			*/
			
			// new changes
			Aggregation aggregation1 = Aggregation.newAggregation(
					
					
					Aggregation.match(Criteria.where("reportToId").is(employeeData.getEmployeeId())),
					Aggregation.skip(0),
					Aggregation.limit(15),
					//sortOperation,
					Aggregation.out("temp")
					
					
						);
				
				AggregationResults<EmployeeRoleBO> groupResults1 
				= mongoTemplate.aggregate(aggregation1, EmployeeRoleBO.class, EmployeeRoleBO.class);
				List<EmployeeRoleBO> result1 = groupResults1.getMappedResults();
			
			
			Aggregation aggregation2 = Aggregation.newAggregation(
					
					Aggregation.lookup("temp", "employeeIdFK", "employeeIdFK", "employeeRoleBOs"),
					Aggregation.match(Criteria.where("employeeRoleBOs.reportToId").is(employeeData.getEmployeeId()).and("date").gte("2018-04-16")),
					//sortOperationOnName,
					//sortOperation,
					Aggregation.group("employeeIdFK").last("employeeIdFK").as("employeeIdFK").last("employeeRoleBOs").as("employeeRoleBOs").last("name").as("name")
					.sum("teamTone.allMailScore.anger").as("angerTeamTotal").sum("teamTone.allMailScore.angerCount")
					.as("angerTeamCount").sum("teamTone.allMailScore.joy").as("joyTeamTotal").sum("teamTone.allMailScore.joyCount")
					.as("joyTeamCount").sum("teamTone.allMailScore.sadness").as("sadnessTeamTotal").sum("teamTone.allMailScore.sadnessCount")
					.as("sadnessTeamCount").sum("teamTone.allMailScore.tentative").as("tentativeTeamTotal").sum("teamTone.allMailScore.tentativeCount")
					.as("tentativeTeamCount").sum("teamTone.allMailScore.analytical").as("analyticalTeamTotal").sum("teamTone.allMailScore.analyticalCount")
					.as("analyticalTeamCount").sum("teamTone.allMailScore.confident").as("confidentTeamTotal").sum("teamTone.allMailScore.confidentCount")
					.as("confidentTeamCount").sum("teamTone.allMailScore.fear").as("fearTeamTotal").sum("teamTone.allMailScore.fearCount")
					.as("fearTeamCount")
					.sum("teamTone.receiveMailScore.anger").as("angerTeamTotalrcv").sum("teamTone.receiveMailScore.angerCount")
					.as("angerTeamCountrcv").sum("teamTone.receiveMailScore.joy").as("joyTeamTotalrcv").sum("teamTone.receiveMailScore.joyCount")
					.as("joyTeamCountrcv").sum("teamTone.receiveMailScore.sadness").as("sadnessTeamTotalrcv").sum("teamTone.receiveMailScore.sadnessCount")
					.as("sadnessTeamCountrcv").sum("teamTone.receiveMailScore.tentative").as("tentativeTeamTotalrcv").sum("teamTone.receiveMailScore.tentativeCount")
					.as("tentativeTeamCountrcv").sum("teamTone.receiveMailScore.analytical").as("analyticalTeamTotalrcv").sum("teamTone.receiveMailScore.analyticalCount")
					.as("analyticalTeamCountrcv").sum("teamTone.receiveMailScore.confident").as("confidentTeamTotalrcv").sum("teamTone.receiveMailScore.confidentCount")
					.as("confidentTeamCountrcv").sum("teamTone.receiveMailScore.fear").as("fearTeamTotalrcv").sum("teamTone.receiveMailScore.fearCount")
					.as("fearTeamCountrcv")
					.sum("teamTone.sentMailScore.anger").as("angerTeamTotalsnt").sum("teamTone.sentMailScore.angerCount")
					.as("angerTeamCountsnt").sum("teamTone.sentMailScore.joy").as("joyTeamTotalsnt").sum("teamTone.sentMailScore.joyCount")
					.as("joyTeamCountsnt").sum("teamTone.sentMailScore.sadness").as("sadnessTeamTotalsnt").sum("teamTone.sentMailScore.sadnessCount")
					.as("sadnessTeamCountsnt").sum("teamTone.sentMailScore.tentative").as("tentativeTeamTotalsnt").sum("teamTone.sentMailScore.tentativeCount")
					.as("tentativeTeamCountsnt").sum("teamTone.sentMailScore.analytical").as("analyticalTeamTotalsnt").sum("teamTone.sentMailScore.analyticalCount")
					.as("analyticalTeamCountsnt").sum("teamTone.sentMailScore.confident").as("confidentTeamTotalsnt").sum("teamTone.sentMailScore.confidentCount")
					.as("confidentTeamCountsnt").sum("teamTone.sentMailScore.fear").as("fearTeamTotalsnt").sum("teamTone.sentMailScore.fearCount")
					.as("fearTeamCountsnt")
					.sum("teamTone.totalMail").as("totalTeamMail")
					.sum("teamTone.totalMailRecevied").as("totalTeamMailRecevied")
					.sum("teamTone.totalMailSent").as("totalTeamMailSent")
					
						);
				
				AggregationResults<AggregationPojo> groupResults2 
				= mongoTemplate.aggregate(aggregation2, DailyEmployeeEmailToneBO.class, AggregationPojo.class);
				List<AggregationPojo> result2 = groupResults2.getMappedResults();
				
				mongoTemplate.dropCollection("temp");
				
				long noOfMail = 0, noOfSentMail = 0, noOfReceiveMail = 0;		
				
				for(AggregationPojo aggregationPojo : result2) {
					
					EmployeePersonalDataDTO employeePersonalDataDTO1 = new EmployeePersonalDataDTO();
					
					noOfMail += aggregationPojo.getTotalTeamMail();
					noOfSentMail += aggregationPojo.getTotalTeamMailSent();
					noOfReceiveMail += aggregationPojo.getTotalTeamMailRecevied();
					
					// Average of All Self Scores 
					
					Double angerFinal = 0.0d;
					Double joyFinal = 0.0d;
					Double sadnessFinal = 0.0d;
					Double tentativeFinal = 0.0d;
					Double analyticalFinal = 0.0d;
					Double confidentFinal = 0.0d;
					Double fearFinal = 0.0d;
					
					if(aggregationPojo.getAngerTeamCount() != 0)
					angerFinal = aggregationPojo.getAngerTeamTotal()/aggregationPojo.getAngerTeamCount();
					if(aggregationPojo.getJoyTeamCount() != 0)
					joyFinal = aggregationPojo.getJoyTeamTotal()/aggregationPojo.getJoyTeamCount();
					if(aggregationPojo.getSadnessTeamCount() != 0)
					sadnessFinal = aggregationPojo.getSadnessTeamTotal()/aggregationPojo.getSadnessTeamCount();
					if(aggregationPojo.getTentativeTeamCount() != 0)
					tentativeFinal =aggregationPojo.getTentativeTeamTotal()/aggregationPojo.getTentativeTeamCount();
					if(aggregationPojo.getAnalyticalTeamCount() != 0)
					analyticalFinal =aggregationPojo.getAnalyticalTeamTotal()/aggregationPojo.getAnalyticalTeamCount();
					if(aggregationPojo.getConfidentTeamCount() != 0)
					confidentFinal = aggregationPojo.getConfidentTeamTotal()/aggregationPojo.getConfidentTeamCount();
					if(aggregationPojo.getFearTeamCount() != 0)
					fearFinal =aggregationPojo.getFearTeamTotal()/aggregationPojo.getFearTeamCount();
					
					ToneOfMail toneOfMail = new ToneOfMail();
					

					Double total = angerFinal+joyFinal+sadnessFinal+tentativeFinal+analyticalFinal+confidentFinal+fearFinal;
					Double angerP = (double)Math.round(((angerFinal)*100)/(total));
					Double joyP = (double)Math.round(((joyFinal)*100)/(total));
					Double sadnP = (double)Math.round(((sadnessFinal)*100)/(total));
					Double tenP = (double)Math.round(((tentativeFinal)*100)/(total));
					Double analP = (double)Math.round(((analyticalFinal)*100)/(total));
					Double confP = (double)Math.round(((confidentFinal)*100)/(total));
					Double fearP =  (double)Math.round(((fearFinal)*100)/(total));
					
					toneOfMail.setAnger(angerP);
					toneOfMail.setJoy(joyP);
					toneOfMail.setSadness(sadnP);
					toneOfMail.setTentative(tenP);
					toneOfMail.setAnalytical(analP);
					toneOfMail.setConfident(confP);
					toneOfMail.setFear(fearP);
					
					/*toneOfMail.setAnger(angerFinal);
					toneOfMail.setJoy(joyFinal);
					toneOfMail.setSadness(sadnessFinal);
					toneOfMail.setTentative(tentativeFinal);
					toneOfMail.setAnalytical(analyticalFinal);
					toneOfMail.setConfident(confidentFinal);
					toneOfMail.setFear(fearFinal);*/
					
					//rcv
					
					Double angerFinal1 = 0.0d;
					Double joyFinal1 = 0.0d;
					Double sadnessFinal1 = 0.0d;
					Double tentativeFinal1 = 0.0d;
					Double analyticalFinal1 = 0.0d;
					Double confidentFinal1 = 0.0d;
					Double fearFinal1 = 0.0d;
					
					if(aggregationPojo.getAngerTeamCountrcv() != 0)
					angerFinal1 = aggregationPojo.getAngerTeamTotalrcv()/aggregationPojo.getAngerTeamCountrcv();
					if(aggregationPojo.getJoyTeamCountrcv() != 0)
					joyFinal1 = aggregationPojo.getJoyTeamTotalrcv()/aggregationPojo.getJoyTeamCountrcv();
					if(aggregationPojo.getSadnessTeamCountrcv() != 0)
					sadnessFinal1 = aggregationPojo.getSadnessTeamTotalrcv()/aggregationPojo.getSadnessTeamCountrcv();
					if(aggregationPojo.getTentativeTeamCountrcv() != 0)
					tentativeFinal1 = aggregationPojo.getTentativeTeamTotalrcv()/aggregationPojo.getTentativeTeamCountrcv();
					if(aggregationPojo.getAnalyticalTeamCountrcv() != 0)
					analyticalFinal1 = aggregationPojo.getAnalyticalTeamTotalrcv()/aggregationPojo.getAnalyticalTeamCountrcv();
					if(aggregationPojo.getConfidentTeamCountrcv() != 0)
					confidentFinal1 = aggregationPojo.getConfidentTeamTotalrcv()/aggregationPojo.getConfidentTeamCountrcv();
					if(aggregationPojo.getFearTeamCountrcv() != 0)
					fearFinal1 =aggregationPojo.getFearTeamTotalrcv()/aggregationPojo.getFearTeamCountrcv();
					
					
					
					ToneOfMail toneOfMail1 = new ToneOfMail();
					
					Double totalr = angerFinal1+joyFinal1+sadnessFinal1+tentativeFinal1+analyticalFinal1+confidentFinal1+fearFinal1;
					Double angercP = (double)Math.round(((angerFinal1)*100)/(totalr));
					Double joyrP = (double)Math.round(((joyFinal1)*100)/(totalr));
					Double sadnrP = (double)Math.round(((sadnessFinal1)*100)/(totalr));
					Double tenrP = (double)Math.round(((tentativeFinal1)*100)/(totalr));
					Double analrP = (double)Math.round(((analyticalFinal1)*100)/(totalr));
					Double confrP = (double)Math.round(((confidentFinal1)*100)/(totalr));
					Double fearrP =  (double)Math.round(((fearFinal1)*100)/(totalr));
					
					toneOfMail1.setAnger(angercP);
					toneOfMail1.setJoy(joyrP);
					toneOfMail1.setSadness(sadnrP);
					toneOfMail1.setTentative(tenrP);
					toneOfMail1.setAnalytical(analrP);
					toneOfMail1.setConfident(confrP);
					toneOfMail1.setFear(fearrP);
					/*
					toneOfMail1.setAnger(angerFinal1);
					toneOfMail1.setJoy(joyFinal1);
					toneOfMail1.setSadness(sadnessFinal1);
					toneOfMail1.setTentative(tentativeFinal1);
					toneOfMail1.setAnalytical(analyticalFinal1);
					toneOfMail1.setConfident(confidentFinal1);
					toneOfMail1.setFear(fearFinal1);*/
					
					//snt
					
					Double angerFinal2 = 0.0d;
					Double joyFinal2 = 0.0d;
					Double sadnessFinal2 = 0.0d;
					Double tentativeFinal2 = 0.0d;
					Double analyticalFinal2 = 0.0d;
					Double confidentFinal2 = 0.0d;
					Double fearFinal2 = 0.0d;
					
					if(aggregationPojo.getAngerTeamCountsnt() != 0)
					angerFinal2 = aggregationPojo.getAngerTeamTotalsnt()/aggregationPojo.getAngerTeamCountsnt();
					if(aggregationPojo.getJoyTeamCountsnt() != 0)
					joyFinal2 = aggregationPojo.getJoyTeamTotalsnt()/aggregationPojo.getJoyTeamCountsnt();
					if(aggregationPojo.getSadnessTeamCountsnt() != 0)
					sadnessFinal2 = aggregationPojo.getSadnessTeamTotalsnt()/aggregationPojo.getSadnessTeamCountsnt();
					if(aggregationPojo.getTentativeTeamCountsnt() != 0)
					tentativeFinal2 =aggregationPojo.getTentativeTeamTotalsnt()/aggregationPojo.getTentativeTeamCountsnt();
					if(aggregationPojo.getAnalyticalTeamCountsnt() != 0)
					analyticalFinal2 = aggregationPojo.getAnalyticalTeamTotalsnt()/aggregationPojo.getAnalyticalTeamCountsnt();
					if(aggregationPojo.getConfidentTeamCountsnt() != 0)
					confidentFinal2 = aggregationPojo.getConfidentTeamTotalsnt()/aggregationPojo.getConfidentTeamCountsnt();
					if(aggregationPojo.getFearTeamCountsnt() != 0)
					fearFinal2 = aggregationPojo.getFearTeamTotalsnt()/aggregationPojo.getFearTeamCountsnt();
					
					ToneOfMail toneOfMail2 = new ToneOfMail();
					
					Double totalsnt = angerFinal2+joyFinal2+sadnessFinal2+tentativeFinal2+analyticalFinal2+confidentFinal2+fearFinal2;
					Double angersntP = (double)Math.round(((angerFinal2)*100)/(totalsnt));
					Double joyrsntP = (double)Math.round(((joyFinal2)*100)/(totalsnt));
					Double sadnrsntP = (double)Math.round(((sadnessFinal2)*100)/(totalsnt));
					Double tenrsntP = (double)Math.round(((tentativeFinal2)*100)/(totalsnt));
					Double analrsntP = (double)Math.round(((analyticalFinal2)*100)/(totalsnt));
					Double confrsntP = (double)Math.round(((confidentFinal2)*100)/(totalsnt));
					Double fearrsntP =  (double)Math.round(((fearFinal2)*100)/(totalsnt));
					
					toneOfMail2.setAnger(angersntP);
					toneOfMail2.setJoy(joyrsntP);
					toneOfMail2.setSadness(sadnrsntP);
					toneOfMail2.setTentative(tenrsntP);
					toneOfMail2.setAnalytical(analrsntP);
					toneOfMail2.setConfident(confrsntP);
					toneOfMail2.setFear(fearrsntP);	
					
					
					/*
					toneOfMail2.setAnger(angerFinal2);
					toneOfMail2.setJoy(joyFinal2);
					toneOfMail2.setSadness(sadnessFinal2);
					toneOfMail2.setTentative(tentativeFinal2);
					toneOfMail2.setAnalytical(analyticalFinal2);
					toneOfMail2.setConfident(confidentFinal2);
					toneOfMail2.setFear(fearFinal2);*/
					
					//EmployeeBO employeeData1 =employeeRepository.findByEmployeeId(aggregationPojo.getEmployeeId());
					employeePersonalDataDTO1.setEmployeeName(aggregationPojo.getName());
					
					employeePersonalDataDTO1.setEmployeeId(aggregationPojo.getEmployeeIdFK());
					
					employeePersonalDataDTO1.setDesignation(aggregationPojo.getEmployeeRoleBOs().get(0)
							.getDesignation());
		
					
					employeePersonalDataDTO1.setToneOfTeamMail(toneOfMail);
					employeePersonalDataDTO1.setToneOfTeamReceiveMail(toneOfMail1);
					employeePersonalDataDTO1.setToneOfTeamSentMail(toneOfMail2);
					employeePersonalDataDTO1.setNoOfMail(aggregationPojo.getTotalTeamMail());
					employeePersonalDataDTO1.setNoOfReceiveMail(aggregationPojo.getTotalTeamMailRecevied());
					employeePersonalDataDTO1.setNoOfSentMail(aggregationPojo.getTotalTeamMailSent());
				
					employeeResult.add(employeePersonalDataDTO1);
					
				}
				
			
					  
				employeePersonalDataDTO.setListOfEmployee(employeeResult);
			
			
			
			
			
			
			
			//Aggregation aggregation = null;
			
			
				
				Aggregation	 aggregation = Aggregation.newAggregation(
			                
							Aggregation.match(Criteria.where("employeeIdFK").is(employeeData.getEmployeeId()).and("date").gte("2018-04-16")),
							Aggregation.group("employeeIdFK").last("employeeIdFK").as("employeeId")
							.sum("teamTone.allMailScore.anger").as("angerTeamTotal").sum("teamTone.allMailScore.angerCount")
							.as("angerTeamCount").sum("teamTone.allMailScore.joy").as("joyTeamTotal").sum("teamTone.allMailScore.joyCount")
							.as("joyTeamCount").sum("teamTone.allMailScore.sadness").as("sadnessTeamTotal").sum("teamTone.allMailScore.sadnessCount")
							.as("sadnessTeamCount").sum("teamTone.allMailScore.tentative").as("tentativeTeamTotal").sum("teamTone.allMailScore.tentativeCount")
							.as("tentativeTeamCount").sum("teamTone.allMailScore.analytical").as("analyticalTeamTotal").sum("teamTone.allMailScore.analyticalCount")
							.as("analyticalTeamCount").sum("teamTone.allMailScore.confident").as("confidentTeamTotal").sum("teamTone.allMailScore.confidentCount")
							.as("confidentTeamCount").sum("teamTone.allMailScore.fear").as("fearTeamTotal").sum("teamTone.allMailScore.fearCount")
							.as("fearTeamCount")
							.sum("teamTone.receiveMailScore.anger").as("angerTeamTotalrcv").sum("teamTone.receiveMailScore.angerCount")
							.as("angerTeamCountrcv").sum("teamTone.receiveMailScore.joy").as("joyTeamTotalrcv").sum("teamTone.receiveMailScore.joyCount")
							.as("joyTeamCountrcv").sum("teamTone.receiveMailScore.sadness").as("sadnessTeamTotalrcv").sum("teamTone.receiveMailScore.sadnessCount")
							.as("sadnessTeamCountrcv").sum("teamTone.receiveMailScore.tentative").as("tentativeTeamTotalrcv").sum("teamTone.receiveMailScore.tentativeCount")
							.as("tentativeTeamCountrcv").sum("teamTone.receiveMailScore.analytical").as("analyticalTeamTotalrcv").sum("teamTone.receiveMailScore.analyticalCount")
							.as("analyticalTeamCountrcv").sum("teamTone.receiveMailScore.confident").as("confidentTeamTotalrcv").sum("teamTone.receiveMailScore.confidentCount")
							.as("confidentTeamCountrcv").sum("teamTone.receiveMailScore.fear").as("fearTeamTotalrcv").sum("teamTone.receiveMailScore.fearCount")
							.as("fearTeamCountrcv")
							.sum("teamTone.sentMailScore.anger").as("angerTeamTotalsnt").sum("teamTone.sentMailScore.angerCount")
							.as("angerTeamCountsnt").sum("teamTone.sentMailScore.joy").as("joyTeamTotalsnt").sum("teamTone.sentMailScore.joyCount")
							.as("joyTeamCountsnt").sum("teamTone.sentMailScore.sadness").as("sadnessTeamTotalsnt").sum("teamTone.sentMailScore.sadnessCount")
							.as("sadnessTeamCountsnt").sum("teamTone.sentMailScore.tentative").as("tentativeTeamTotalsnt").sum("teamTone.sentMailScore.tentativeCount")
							.as("tentativeTeamCountsnt").sum("teamTone.sentMailScore.analytical").as("analyticalTeamTotalsnt").sum("teamTone.sentMailScore.analyticalCount")
							.as("analyticalTeamCountsnt").sum("teamTone.sentMailScore.confident").as("confidentTeamTotalsnt").sum("teamTone.sentMailScore.confidentCount")
							.as("confidentTeamCountsnt").sum("teamTone.sentMailScore.fear").as("fearTeamTotalsnt").sum("teamTone.sentMailScore.fearCount")
							.as("fearTeamCountsnt")
							.sum("teamTone.totalMail").as("totalTeamMail")
							.sum("teamTone.totalMailRecevied").as("totalTeamMailRecevied")
							.sum("teamTone.totalMailSent").as("totalTeamMailSent")
							
							
								);
				
			
				
				AggregationResults<AggregationPojo> groupResults 
				= mongoTemplate.aggregate(aggregation, DailyEmployeeEmailToneBO.class, AggregationPojo.class);
				List<AggregationPojo> result = groupResults.getMappedResults();
				
				
			
			
		// Average of All Self Scores 
			
			Double angerFinal = 0.0d;
			Double joyFinal = 0.0d;
			Double sadnessFinal = 0.0d;
			Double tentativeFinal = 0.0d;
			Double analyticalFinal = 0.0d;
			Double confidentFinal = 0.0d;
			Double fearFinal = 0.0d;
			
			if(result.get(0).getAngerTeamCount() != 0)
			angerFinal = result.get(0).getAngerTeamTotal()/result.get(0).getAngerTeamCount();
			if(result.get(0).getJoyTeamCount() != 0)
			joyFinal = result.get(0).getJoyTeamTotal()/result.get(0).getJoyTeamCount();
			if(result.get(0).getSadnessTeamCount() != 0)
			sadnessFinal = result.get(0).getSadnessTeamTotal()/result.get(0).getSadnessTeamCount();
			if(result.get(0).getTentativeTeamCount() != 0)
			tentativeFinal = result.get(0).getTentativeTeamTotal()/result.get(0).getTentativeTeamCount();
			if(result.get(0).getAnalyticalTeamCount() != 0)
			analyticalFinal = result.get(0).getAnalyticalTeamTotal()/result.get(0).getAnalyticalTeamCount();
			if(result.get(0).getConfidentTeamCount() != 0)
			confidentFinal = result.get(0).getConfidentTeamTotal()/result.get(0).getConfidentTeamCount();
			if(result.get(0).getFearTeamCount() != 0)
			fearFinal =result.get(0).getFearTeamTotal()/result.get(0).getFearTeamCount();
			
			ToneOfMail toneOfMail = new ToneOfMail();
			
			Double total = angerFinal+joyFinal+sadnessFinal+tentativeFinal+analyticalFinal+confidentFinal+fearFinal;
			Double angerP = (double)Math.round(((angerFinal)*100)/(total));
			Double joyP = (double)Math.round(((joyFinal)*100)/(total));
			Double sadnP = (double)Math.round(((sadnessFinal)*100)/(total));
			Double tenP = (double)Math.round(((tentativeFinal)*100)/(total));
			Double analP = (double)Math.round(((analyticalFinal)*100)/(total));
			Double confP = (double)Math.round(((confidentFinal)*100)/(total));
			Double fearP =  (double)Math.round(((fearFinal)*100)/(total));
			
			toneOfMail.setAnger(angerP);
			toneOfMail.setJoy(joyP);
			toneOfMail.setSadness(sadnP);
			toneOfMail.setTentative(tenP);
			toneOfMail.setAnalytical(analP);
			toneOfMail.setConfident(confP);
			toneOfMail.setFear(fearP);
			/*
			toneOfMail.setAnger(angerFinal);
			toneOfMail.setJoy(joyFinal);
			toneOfMail.setSadness(sadnessFinal);
			toneOfMail.setTentative(tentativeFinal);
			toneOfMail.setAnalytical(analyticalFinal);
			toneOfMail.setConfident(confidentFinal);
			toneOfMail.setFear(fearFinal);*/
			
			//rcv
			
			Double angerFinal1 = 0.0d;
			Double joyFinal1 = 0.0d;
			Double sadnessFinal1 = 0.0d;
			Double tentativeFinal1 = 0.0d;
			Double analyticalFinal1 = 0.0d;
			Double confidentFinal1 = 0.0d;
			Double fearFinal1 = 0.0d;
			
			if(result.get(0).getAngerTeamCountrcv() != 0)
			angerFinal1 = result.get(0).getAngerTeamTotalrcv()/result.get(0).getAngerTeamCountrcv();
			if(result.get(0).getJoyTeamCountrcv() != 0)
			joyFinal1 = result.get(0).getJoyTeamTotalrcv()/result.get(0).getJoyTeamCountrcv();
			if(result.get(0).getSadnessTeamCountrcv() != 0)
			sadnessFinal1 = result.get(0).getSadnessTeamTotalrcv()/result.get(0).getSadnessTeamCountrcv();
			if(result.get(0).getTentativeTeamCountrcv() != 0)
			tentativeFinal1 = result.get(0).getTentativeTeamTotalrcv()/result.get(0).getTentativeTeamCountrcv();
			if(result.get(0).getAnalyticalTeamCountrcv() != 0)
			analyticalFinal1 = result.get(0).getAnalyticalTeamTotalrcv()/result.get(0).getAnalyticalTeamCountrcv();
			if(result.get(0).getConfidentTeamCountrcv() != 0)
			confidentFinal1 = result.get(0).getConfidentTeamTotalrcv()/result.get(0).getConfidentTeamCountrcv();
			if(result.get(0).getFearTeamCountrcv() != 0)
			fearFinal1 = result.get(0).getFearTeamTotalrcv()/result.get(0).getFearTeamCountrcv();
			
			ToneOfMail toneOfMail1 = new ToneOfMail();
			Double totalr = angerFinal1+joyFinal1+sadnessFinal1+tentativeFinal1+analyticalFinal1+confidentFinal1+fearFinal1;
			Double angercP = (double)Math.round(((angerFinal1)*100)/(totalr));
			Double joyrP = (double)Math.round(((joyFinal1)*100)/(totalr));
			Double sadnrP = (double)Math.round(((sadnessFinal1)*100)/(totalr));
			Double tenrP = (double)Math.round(((tentativeFinal1)*100)/(totalr));
			Double analrP = (double)Math.round(((analyticalFinal1)*100)/(totalr));
			Double confrP = (double)Math.round(((confidentFinal1)*100)/(totalr));
			Double fearrP =  (double)Math.round(((fearFinal1)*100)/(totalr));
			
			toneOfMail1.setAnger(angercP);
			toneOfMail1.setJoy(joyrP);
			toneOfMail1.setSadness(sadnrP);
			toneOfMail1.setTentative(tenrP);
			toneOfMail1.setAnalytical(analrP);
			toneOfMail1.setConfident(confrP);
			toneOfMail1.setFear(fearrP);
			
			/*toneOfMail1.setAnger(angerFinal1);
			toneOfMail1.setJoy(joyFinal1);
			toneOfMail1.setSadness(sadnessFinal1);
			toneOfMail1.setTentative(tentativeFinal1);
			toneOfMail1.setAnalytical(analyticalFinal1);
			toneOfMail1.setConfident(confidentFinal1);
			toneOfMail1.setFear(fearFinal1);*/
			
			//snt
			
			Double angerFinal2 = 0.0d;
			Double joyFinal2 = 0.0d;
			Double sadnessFinal2 = 0.0d;
			Double tentativeFinal2 = 0.0d;
			Double analyticalFinal2 = 0.0d;
			Double confidentFinal2 = 0.0d;
			Double fearFinal2 = 0.0d;
			
			if(result.get(0).getAngerTeamCountsnt() != 0)
			angerFinal2 = result.get(0).getAngerTeamTotalsnt()/result.get(0).getAngerTeamCountsnt();
			if(result.get(0).getJoyTeamCountsnt() != 0)
			joyFinal2 = result.get(0).getJoyTeamTotalsnt()/result.get(0).getJoyTeamCountsnt();
			if(result.get(0).getSadnessTeamCountsnt() != 0)
			sadnessFinal2 = result.get(0).getSadnessTeamTotalsnt()/result.get(0).getSadnessTeamCountsnt();
			if(result.get(0).getTentativeTeamCountsnt() != 0)
			tentativeFinal2 = result.get(0).getTentativeTeamTotalsnt()/result.get(0).getTentativeTeamCountsnt();
			if(result.get(0).getAnalyticalTeamCountsnt() != 0)
			analyticalFinal2 = result.get(0).getAnalyticalTeamTotalsnt()/result.get(0).getAnalyticalTeamCountsnt();
			if(result.get(0).getConfidentTeamCountsnt() != 0)
			confidentFinal2 = result.get(0).getConfidentTeamTotalsnt()/result.get(0).getConfidentTeamCountsnt();
			if(result.get(0).getFearTeamCountsnt() != 0)
			fearFinal2 = result.get(0).getFearTeamTotalsnt()/result.get(0).getFearTeamCountsnt();
			
			ToneOfMail toneOfMail2 = new ToneOfMail();
			
			Double totalsnt = angerFinal2+joyFinal2+sadnessFinal2+tentativeFinal2+analyticalFinal2+confidentFinal2+fearFinal2;
			Double angersntP = (double)Math.round(((angerFinal2)*100)/(totalsnt));
			Double joyrsntP = (double)Math.round(((joyFinal2)*100)/(totalsnt));
			Double sadnrsntP = (double)Math.round(((sadnessFinal2)*100)/(totalsnt));
			Double tenrsntP = (double)Math.round(((tentativeFinal2)*100)/(totalsnt));
			Double analrsntP = (double)Math.round(((analyticalFinal2)*100)/(totalsnt));
			Double confrsntP = (double)Math.round(((confidentFinal2)*100)/(totalsnt));
			Double fearrsntP =  (double)Math.round(((fearFinal2)*100)/(totalsnt));
			
			toneOfMail2.setAnger(angersntP);
			toneOfMail2.setJoy(joyrsntP);
			toneOfMail2.setSadness(sadnrsntP);
			toneOfMail2.setTentative(tenrsntP);
			toneOfMail2.setAnalytical(analrsntP);
			toneOfMail2.setConfident(confrsntP);
			toneOfMail2.setFear(fearrsntP);	
			
			
			
			
			/*toneOfMail2.setAnger(angerFinal2);
			toneOfMail2.setJoy(joyFinal2);
			toneOfMail2.setSadness(sadnessFinal2);
			toneOfMail2.setTentative(tentativeFinal2);
			toneOfMail2.setAnalytical(analyticalFinal2);
			toneOfMail2.setConfident(confidentFinal2);
			toneOfMail2.setFear(fearFinal2);*/
			
					
			employeePersonalDataDTO.setToneOfTeamMail(toneOfMail);
			employeePersonalDataDTO.setToneOfTeamReceiveMail(toneOfMail1);
			employeePersonalDataDTO.setToneOfTeamSentMail(toneOfMail2);
			employeePersonalDataDTO.setNoOfMail(result.get(0).getTotalTeamMail());
			employeePersonalDataDTO.setNoOfReceiveMail(result.get(0).getTotalTeamMailRecevied());
			employeePersonalDataDTO.setNoOfSentMail(result.get(0).getTotalTeamMailSent());
			
					
				
			
			
			return employeePersonalDataDTO;
		}
	
	
	//list of employeeReporteeheirachy
	
		public EmployeePersonalDataDTO getListOfEmployeeHierchy(ClientDataDTO clientDataDTO){
			EmployeePersonalDataDTO emptyData = new EmployeePersonalDataDTO();
			try {
			EmployeePersonalDataDTO empldata = new EmployeePersonalDataDTO();
			EmployeeRoleBO empldataDTO = new EmployeeRoleBO();


			 List<EmployeePersonalDataDTO> listOfEmployeeData = new ArrayList<EmployeePersonalDataDTO>();
			 List<String> empReportTo = new ArrayList<String>();
			 List<String> empReportToId = new ArrayList<String>();
			 

			 EmployeeBO employeeData =employeeRepository.findByEmployeeId(clientDataDTO.getEmployeeId());
				
				EmployeeRoleBO employeeRoleBO = employeeRoleRepository.findByEmployeeIdFKAndStatus(clientDataDTO.getEmployeeId(),
						"active");
				empldataDTO = employeeRoleBO;
				empldata.setDesignation(employeeRoleBO.getDesignation());
				empldata.setEmployeeName(employeeData.getEmployeeName());
				empldata.setEmployeeId(employeeData.getEmployeeId());
			 
			 	empReportTo.add(employeeRepository.findByEmployeeId(employeeRoleBO.getEmployeeIdFK()).getEmployeeName());
				empReportToId.add(employeeRepository.findByEmployeeId(employeeRoleBO.getEmployeeIdFK()).getEmployeeId());
				String reportToId=employeeData.getEmployeeName(); 
				
				
				try {
					while(!reportToId.equals("")) {
						reportToId = employeeRoleRepository.findByEmployeeIdFKAndStatus(empldataDTO.getReportToId(), "active").getEmployeeIdFK();
						empReportTo.add(employeeRepository.findByEmployeeId(reportToId).getEmployeeName());
						empReportToId.add(employeeRepository.findByEmployeeId(reportToId).getEmployeeId());
						empldataDTO = employeeRoleRepository.findByEmployeeIdFKAndStatus(empldataDTO.getReportToId(), "active");
					};
				}catch(Exception e) {
					
				}
				Collections.reverse(empReportTo);
				Collections.reverse(empReportToId);
				empldata.setEmpReportToHierachy(empReportTo);
				empldata.setEmpReportToId(empReportToId);
			// SortOperation sortByPopDesc = sort(new Sort(Direction.DESC, "statePop"));
			 SortOperation sortOperation = null;
		/*	// SortOperation sortOperationOnName = null;
			if (clientDataDTO.getSortDataText()!=null ) {
				 
				if (clientDataDTO.getSortOnName()!=null) {
			 sortOperation = Aggregation.sort(Sort.Direction.DESC,"employeeRoleBOs.teamSize")
					 .and(Sort.Direction.DESC,"name");
				}else {
					 sortOperation = Aggregation.sort(Sort.Direction.DESC,"employeeRoleBOs.teamSize");
				}
			}else {
				
				if (clientDataDTO.getSortOnName()!=null) {
				 sortOperation =	Aggregation.sort(Sort.Direction.DESC,"name");
				}else {
					 sortOperation = Aggregation.sort(Sort.Direction.ASC,"employeeRoleBOs.teamSize")
							 .and(Sort.Direction.ASC,"name");
				}
			}*/
			
			List<SortOperation> listSort = new ArrayList<SortOperation>();
			
			if(clientDataDTO.getSearchCriteria()!=null){
			
			if (clientDataDTO.getSearchCriteria().contains("alphabetically")) {
				
				 sortOperation =	Aggregation.sort(Sort.Direction.DESC,"name");
				 listSort.add(0, sortOperation);
				
			}
			
			if (clientDataDTO.getSearchCriteria().contains("teamSize")) {
				
				if (listSort.size()>0) {
					
					sortOperation = Aggregation.sort(Sort.Direction.DESC,"employeeRoleBOs.teamSize")
							 .and(Sort.Direction.DESC,"name");
					 listSort.add(0, sortOperation);
				}else {
					 sortOperation = Aggregation.sort(Sort.Direction.DESC,"employeeRoleBOs.teamSize");
					 listSort.add(0, sortOperation);
				}
				
			}
			
			}
				
			if (listSort.size()==0) {
				 sortOperation = Aggregation.sort(Sort.Direction.ASC,"employeeRoleBOs.teamSize")
						 .and(Sort.Direction.ASC,"name");
				// listSort.add(0, sortOperation);
			}else {
				sortOperation = listSort.get(0);
			}
			
			
			
		/*if (clientDataDTO.getSortOnName()!=null) {
				 
				 sortOperationOnName = Aggregation.sort(Sort.Direction.DESC,"name");
				}else {
					sortOperationOnName =	Aggregation.sort(Sort.Direction.ASC,"name");
				}*/
					
					//Employee Reportee
					
					Aggregation aggregation1 = Aggregation.newAggregation(
							
							
							Aggregation.match(Criteria.where("reportToId").is(clientDataDTO.getEmployeeId())),
							Aggregation.skip(0),
							Aggregation.limit(5),
							//sortOperation,
							Aggregation.out("temp")
							
							
								);
						
// 			Aggregation aggregation1 = Aggregation.newAggregation(
					
					
// 					Aggregation.match(Criteria.where("status").is("active")),
// //					Aggregation.skip(0),
// //					Aggregation.limit(5),
// 					//sortOperation,
// 					Aggregation.out("temp")
					
					
// 						);
						
						AggregationResults<EmployeeRoleBO> groupResults1 
						= mongoTemplate.aggregate(aggregation1, EmployeeRoleBO.class, EmployeeRoleBO.class);
						List<EmployeeRoleBO> result1 = groupResults1.getMappedResults();
					
					
					Aggregation aggregation2 = Aggregation.newAggregation(
							
							Aggregation.lookup("temp", "employeeIdFK", "employeeIdFK", "employeeRoleBOs"),
							Aggregation.match(Criteria.where("employeeRoleBOs.reportToId").is(clientDataDTO.getEmployeeId()).and("date").gte("2018-04-16")),
							//sortOperationOnName,
							sortOperation,
							Aggregation.group("employeeIdFK").last("employeeIdFK").as("employeeIdFK").last("employeeRoleBOs").as("employeeRoleBOs").last("name").as("name")
							.sum("teamTone.allMailScore.anger").as("angerTeamTotal").sum("teamTone.allMailScore.angerCount")
							.as("angerTeamCount").sum("teamTone.allMailScore.joy").as("joyTeamTotal").sum("teamTone.allMailScore.joyCount")
							.as("joyTeamCount").sum("teamTone.allMailScore.sadness").as("sadnessTeamTotal").sum("teamTone.allMailScore.sadnessCount")
							.as("sadnessTeamCount").sum("teamTone.allMailScore.tentative").as("tentativeTeamTotal").sum("teamTone.allMailScore.tentativeCount")
							.as("tentativeTeamCount").sum("teamTone.allMailScore.analytical").as("analyticalTeamTotal").sum("teamTone.allMailScore.analyticalCount")
							.as("analyticalTeamCount").sum("teamTone.allMailScore.confident").as("confidentTeamTotal").sum("teamTone.allMailScore.confidentCount")
							.as("confidentTeamCount").sum("teamTone.allMailScore.fear").as("fearTeamTotal").sum("teamTone.allMailScore.fearCount")
							.as("fearTeamCount")
							.sum("teamTone.receiveMailScore.anger").as("angerTeamTotalrcv").sum("teamTone.receiveMailScore.angerCount")
							.as("angerTeamCountrcv").sum("teamTone.receiveMailScore.joy").as("joyTeamTotalrcv").sum("teamTone.receiveMailScore.joyCount")
							.as("joyTeamCountrcv").sum("teamTone.receiveMailScore.sadness").as("sadnessTeamTotalrcv").sum("teamTone.receiveMailScore.sadnessCount")
							.as("sadnessTeamCountrcv").sum("teamTone.receiveMailScore.tentative").as("tentativeTeamTotalrcv").sum("teamTone.receiveMailScore.tentativeCount")
							.as("tentativeTeamCountrcv").sum("teamTone.receiveMailScore.analytical").as("analyticalTeamTotalrcv").sum("teamTone.receiveMailScore.analyticalCount")
							.as("analyticalTeamCountrcv").sum("teamTone.receiveMailScore.confident").as("confidentTeamTotalrcv").sum("teamTone.receiveMailScore.confidentCount")
							.as("confidentTeamCountrcv").sum("teamTone.receiveMailScore.fear").as("fearTeamTotalrcv").sum("teamTone.receiveMailScore.fearCount")
							.as("fearTeamCountrcv")
							.sum("teamTone.sentMailScore.anger").as("angerTeamTotalsnt").sum("teamTone.sentMailScore.angerCount")
							.as("angerTeamCountsnt").sum("teamTone.sentMailScore.joy").as("joyTeamTotalsnt").sum("teamTone.sentMailScore.joyCount")
							.as("joyTeamCountsnt").sum("teamTone.sentMailScore.sadness").as("sadnessTeamTotalsnt").sum("teamTone.sentMailScore.sadnessCount")
							.as("sadnessTeamCountsnt").sum("teamTone.sentMailScore.tentative").as("tentativeTeamTotalsnt").sum("teamTone.sentMailScore.tentativeCount")
							.as("tentativeTeamCountsnt").sum("teamTone.sentMailScore.analytical").as("analyticalTeamTotalsnt").sum("teamTone.sentMailScore.analyticalCount")
							.as("analyticalTeamCountsnt").sum("teamTone.sentMailScore.confident").as("confidentTeamTotalsnt").sum("teamTone.sentMailScore.confidentCount")
							.as("confidentTeamCountsnt").sum("teamTone.sentMailScore.fear").as("fearTeamTotalsnt").sum("teamTone.sentMailScore.fearCount")
							.as("fearTeamCountsnt")
							.sum("teamTone.totalMail").as("totalTeamMail")
							.sum("teamTone.totalMailRecevied").as("totalTeamMailRecevied")
							.sum("teamTone.totalMailSent").as("totalTeamMailSent")
							
								);
						
						AggregationResults<AggregationPojo> groupResults2 
						= mongoTemplate.aggregate(aggregation2, DailyEmployeeEmailToneBO.class, AggregationPojo.class);
						List<AggregationPojo> result2 = groupResults2.getMappedResults();
						
						mongoTemplate.dropCollection("temp");
					
						
						for(AggregationPojo aggregationPojo : result2) {
							
							EmployeePersonalDataDTO employeePersonalDataDTO = new EmployeePersonalDataDTO();
							
							// Average of All Self Scores 
							
							
							// Average of All Self Scores 
							
							Double angerFinal = 0.0d;
							Double joyFinal = 0.0d;
							Double sadnessFinal = 0.0d;
							Double tentativeFinal = 0.0d;
							Double analyticalFinal = 0.0d;
							Double confidentFinal = 0.0d;
							Double fearFinal = 0.0d;
							
							if(aggregationPojo.getAngerTeamCount() != 0)
							angerFinal = aggregationPojo.getAngerTeamTotal()/aggregationPojo.getAngerTeamCount();
							if(aggregationPojo.getJoyTeamCount() != 0)
							joyFinal = aggregationPojo.getJoyTeamTotal()/aggregationPojo.getJoyTeamCount();
							if(aggregationPojo.getSadnessTeamCount() != 0)
							sadnessFinal = aggregationPojo.getSadnessTeamTotal()/aggregationPojo.getSadnessTeamCount();
							if(aggregationPojo.getTentativeTeamCount() != 0)
							tentativeFinal =aggregationPojo.getTentativeTeamTotal()/aggregationPojo.getTentativeTeamCount();
							if(aggregationPojo.getAnalyticalTeamCount() != 0)
							analyticalFinal =aggregationPojo.getAnalyticalTeamTotal()/aggregationPojo.getAnalyticalTeamCount();
							if(aggregationPojo.getConfidentTeamCount() != 0)
							confidentFinal = aggregationPojo.getConfidentTeamTotal()/aggregationPojo.getConfidentTeamCount();
							if(aggregationPojo.getFearTeamCount() != 0)
							fearFinal =aggregationPojo.getFearTeamTotal()/aggregationPojo.getFearTeamCount();
							
							ToneOfMail toneOfMail = new ToneOfMail();
							

							Double total = angerFinal+joyFinal+sadnessFinal+tentativeFinal+analyticalFinal+confidentFinal+fearFinal;
							Double angerP = 0.0d;
							Double joyP = 0.0d;
							Double sadnP = 0.0d;
							Double tenP = 0.0d;
							Double analP = 0.0d;
							Double confP = 0.0d;
							Double fearP = 0.0d;
							
							if(total != 0) {
							angerP = (double)Math.round(((angerFinal)*100)/(total));
							joyP = (double)Math.round(((joyFinal)*100)/(total));
							sadnP = (double)Math.round(((sadnessFinal)*100)/(total));
							tenP = (double)Math.round(((tentativeFinal)*100)/(total));
							analP = (double)Math.round(((analyticalFinal)*100)/(total));
							confP = (double)Math.round(((confidentFinal)*100)/(total));
							fearP =  (double)Math.round(((fearFinal)*100)/(total));
							}
							
							toneOfMail.setAnger(angerP);
							toneOfMail.setJoy(joyP);
							toneOfMail.setSadness(sadnP);
							toneOfMail.setTentative(tenP);
							toneOfMail.setAnalytical(analP);
							toneOfMail.setConfident(confP);
							toneOfMail.setFear(fearP);
							
							/*toneOfMail.setAnger(angerFinal);
							toneOfMail.setJoy(joyFinal);
							toneOfMail.setSadness(sadnessFinal);
							toneOfMail.setTentative(tentativeFinal);
							toneOfMail.setAnalytical(analyticalFinal);
							toneOfMail.setConfident(confidentFinal);
							toneOfMail.setFear(fearFinal);*/
							
							//rcv
							
							Double angerFinal1 = 0.0d;
							Double joyFinal1 = 0.0d;
							Double sadnessFinal1 = 0.0d;
							Double tentativeFinal1 = 0.0d;
							Double analyticalFinal1 = 0.0d;
							Double confidentFinal1 = 0.0d;
							Double fearFinal1 = 0.0d;
							
							if(aggregationPojo.getAngerTeamCountrcv() != 0)
							angerFinal1 = aggregationPojo.getAngerTeamTotalrcv()/aggregationPojo.getAngerTeamCountrcv();
							if(aggregationPojo.getJoyTeamCountrcv() != 0)
							joyFinal1 = aggregationPojo.getJoyTeamTotalrcv()/aggregationPojo.getJoyTeamCountrcv();
							if(aggregationPojo.getSadnessTeamCountrcv() != 0)
							sadnessFinal1 = aggregationPojo.getSadnessTeamTotalrcv()/aggregationPojo.getSadnessTeamCountrcv();
							if(aggregationPojo.getTentativeTeamCountrcv() != 0)
							tentativeFinal1 = aggregationPojo.getTentativeTeamTotalrcv()/aggregationPojo.getTentativeTeamCountrcv();
							if(aggregationPojo.getAnalyticalTeamCountrcv() != 0)
							analyticalFinal1 = aggregationPojo.getAnalyticalTeamTotalrcv()/aggregationPojo.getAnalyticalTeamCountrcv();
							if(aggregationPojo.getConfidentTeamCountrcv() != 0)
							confidentFinal1 = aggregationPojo.getConfidentTeamTotalrcv()/aggregationPojo.getConfidentTeamCountrcv();
							if(aggregationPojo.getFearTeamCountrcv() != 0)
							fearFinal1 =aggregationPojo.getFearTeamTotalrcv()/aggregationPojo.getFearTeamCountrcv();
							
							
							
							ToneOfMail toneOfMail1 = new ToneOfMail();
							
							Double totalr = angerFinal1+joyFinal1+sadnessFinal1+tentativeFinal1+analyticalFinal1+confidentFinal1+fearFinal1;
							Double angercP = 0.0d;
							Double joyrP = 0.0d;
							Double sadnrP = 0.0d;
							Double tenrP = 0.0d;
							Double analrP = 0.0d;
							Double confrP = 0.0d;
							Double fearrP = 0.0d;
							
							if(totalr != 0) {
							angercP = (double)Math.round(((angerFinal1)*100)/(totalr));
							joyrP = (double)Math.round(((joyFinal1)*100)/(totalr));
							sadnrP = (double)Math.round(((sadnessFinal1)*100)/(totalr));
							tenrP = (double)Math.round(((tentativeFinal1)*100)/(totalr));
							analrP = (double)Math.round(((analyticalFinal1)*100)/(totalr));
							confrP = (double)Math.round(((confidentFinal1)*100)/(totalr));
							fearrP =  (double)Math.round(((fearFinal1)*100)/(totalr));
							}
							
							toneOfMail1.setAnger(angercP);
							toneOfMail1.setJoy(joyrP);
							toneOfMail1.setSadness(sadnrP);
							toneOfMail1.setTentative(tenrP);
							toneOfMail1.setAnalytical(analrP);
							toneOfMail1.setConfident(confrP);
							toneOfMail1.setFear(fearrP);
							/*
							toneOfMail1.setAnger(angerFinal1);
							toneOfMail1.setJoy(joyFinal1);
							toneOfMail1.setSadness(sadnessFinal1);
							toneOfMail1.setTentative(tentativeFinal1);
							toneOfMail1.setAnalytical(analyticalFinal1);
							toneOfMail1.setConfident(confidentFinal1);
							toneOfMail1.setFear(fearFinal1);*/
							
							//snt
							
							Double angerFinal2 = 0.0d;
							Double joyFinal2 = 0.0d;
							Double sadnessFinal2 = 0.0d;
							Double tentativeFinal2 = 0.0d;
							Double analyticalFinal2 = 0.0d;
							Double confidentFinal2 = 0.0d;
							Double fearFinal2 = 0.0d;
							
							if(aggregationPojo.getAngerTeamCountsnt() != 0)
							angerFinal2 = aggregationPojo.getAngerTeamTotalsnt()/aggregationPojo.getAngerTeamCountsnt();
							if(aggregationPojo.getJoyTeamCountsnt() != 0)
							joyFinal2 = aggregationPojo.getJoyTeamTotalsnt()/aggregationPojo.getJoyTeamCountsnt();
							if(aggregationPojo.getSadnessTeamCountsnt() != 0)
							sadnessFinal2 = aggregationPojo.getSadnessTeamTotalsnt()/aggregationPojo.getSadnessTeamCountsnt();
							if(aggregationPojo.getTentativeTeamCountsnt() != 0)
							tentativeFinal2 =aggregationPojo.getTentativeTeamTotalsnt()/aggregationPojo.getTentativeTeamCountsnt();
							if(aggregationPojo.getAnalyticalTeamCountsnt() != 0)
							analyticalFinal2 = aggregationPojo.getAnalyticalTeamTotalsnt()/aggregationPojo.getAnalyticalTeamCountsnt();
							if(aggregationPojo.getConfidentTeamCountsnt() != 0)
							confidentFinal2 = aggregationPojo.getConfidentTeamTotalsnt()/aggregationPojo.getConfidentTeamCountsnt();
							if(aggregationPojo.getFearTeamCountsnt() !=0)
							fearFinal2 = aggregationPojo.getFearTeamTotalsnt()/aggregationPojo.getFearTeamCountsnt();
							
							ToneOfMail toneOfMail2 = new ToneOfMail();
							
							Double totalsnt = angerFinal2+joyFinal2+sadnessFinal2+tentativeFinal2+analyticalFinal2+confidentFinal2+fearFinal2;
							Double angersntP = 0.0d;
							Double joyrsntP = 0.0d;
							Double sadnrsntP = 0.0d;
							Double tenrsntP = 0.0d;
							Double analrsntP = 0.0d;
							Double confrsntP = 0.0d;
							Double fearrsntP = 0.0d;
							
							if(totalsnt != 0) {
							angersntP = (double)Math.round(((angerFinal2)*100)/(totalsnt));
							joyrsntP = (double)Math.round(((joyFinal2)*100)/(totalsnt));
							sadnrsntP = (double)Math.round(((sadnessFinal2)*100)/(totalsnt));
							tenrsntP = (double)Math.round(((tentativeFinal2)*100)/(totalsnt));
							analrsntP = (double)Math.round(((analyticalFinal2)*100)/(totalsnt));
							confrsntP = (double)Math.round(((confidentFinal2)*100)/(totalsnt));
							fearrsntP =  (double)Math.round(((fearFinal2)*100)/(totalsnt));
							}
							
							toneOfMail2.setAnger(angersntP);
							toneOfMail2.setJoy(joyrsntP);
							toneOfMail2.setSadness(sadnrsntP);
							toneOfMail2.setTentative(tenrsntP);
							toneOfMail2.setAnalytical(analrsntP);
							toneOfMail2.setConfident(confrsntP);
							toneOfMail2.setFear(fearrsntP);	
							
							
						/*	
							
							
							
							Double angerFinal = (double)Math.round(aggregationPojo.getAngerTeamTotal()/aggregationPojo.getAngerTeamCount());
							Double joyFinal = (double)Math.round(aggregationPojo.getJoyTeamTotal()/aggregationPojo.getJoyTeamCount());
							Double sadnessFinal = (double)Math.round(aggregationPojo.getSadnessTeamTotal()/aggregationPojo.getSadnessTeamCount());
							Double tentativeFinal = (double)Math.round(aggregationPojo.getTentativeTeamTotal()/aggregationPojo.getTentativeTeamCount());
							Double analyticalFinal = (double)Math.round(aggregationPojo.getAnalyticalTeamTotal()/aggregationPojo.getAnalyticalTeamCount());
							Double confidentFinal = (double)Math.round(aggregationPojo.getConfidentTeamTotal()/aggregationPojo.getConfidentTeamCount());
							Double fearFinal = (double)Math.round(aggregationPojo.getFearTeamTotal()/aggregationPojo.getFearTeamCount());
							
							ToneOfMail toneOfMail = new ToneOfMail();
							toneOfMail.setAnger(angerFinal);
							toneOfMail.setJoy(joyFinal);
							toneOfMail.setSadness(sadnessFinal);
							toneOfMail.setTentative(tentativeFinal);
							toneOfMail.setAnalytical(analyticalFinal);
							toneOfMail.setConfident(confidentFinal);
							toneOfMail.setFear(fearFinal);
							
							//rcv
							
							Double angerFinal1 = (double)Math.round(aggregationPojo.getAngerTeamTotalrcv()/aggregationPojo.getAngerTeamCountrcv());
							Double joyFinal1 = (double)Math.round(aggregationPojo.getJoyTeamTotalrcv()/aggregationPojo.getJoyTeamCountrcv());
							Double sadnessFinal1 = (double)Math.round(aggregationPojo.getSadnessTeamTotalrcv()/aggregationPojo.getSadnessTeamCountrcv());
							Double tentativeFinal1 = (double)Math.round(aggregationPojo.getTentativeTeamTotalrcv()/aggregationPojo.getTentativeTeamCountrcv());
							Double analyticalFinal1 = (double)Math.round(aggregationPojo.getAnalyticalTeamTotalrcv()/aggregationPojo.getAnalyticalTeamCountrcv());
							Double confidentFinal1 = (double)Math.round(aggregationPojo.getConfidentTeamTotalrcv()/aggregationPojo.getConfidentTeamCountrcv());
							Double fearFinal1 = (double)Math.round(aggregationPojo.getFearTeamTotalrcv()/aggregationPojo.getFearTeamCountrcv());
							
							ToneOfMail toneOfMail1 = new ToneOfMail();
							toneOfMail1.setAnger(angerFinal1);
							toneOfMail1.setJoy(joyFinal1);
							toneOfMail1.setSadness(sadnessFinal1);
							toneOfMail1.setTentative(tentativeFinal1);
							toneOfMail1.setAnalytical(analyticalFinal1);
							toneOfMail1.setConfident(confidentFinal1);
							toneOfMail1.setFear(fearFinal1);
							
							//snt
							
							Double angerFinal2 = (double)Math.round(aggregationPojo.getAngerTeamTotalsnt()/aggregationPojo.getAngerTeamCountsnt());
							Double joyFinal2 = (double)Math.round(aggregationPojo.getJoyTeamTotalsnt()/aggregationPojo.getJoyTeamCountsnt());
							Double sadnessFinal2 = (double)Math.round(aggregationPojo.getSadnessTeamTotalsnt()/aggregationPojo.getSadnessTeamCountsnt());
							Double tentativeFinal2 = (double)Math.round(aggregationPojo.getTentativeTeamTotalsnt()/aggregationPojo.getTentativeTeamCountsnt());
							Double analyticalFinal2 = (double)Math.round(aggregationPojo.getAnalyticalTeamTotalsnt()/aggregationPojo.getAnalyticalTeamCountsnt());
							Double confidentFinal2 = (double)Math.round(aggregationPojo.getConfidentTeamTotalsnt()/aggregationPojo.getConfidentTeamCountsnt());
							Double fearFinal2 = (double)Math.round(aggregationPojo.getFearTeamTotalsnt()/aggregationPojo.getFearTeamCountsnt());
							
							ToneOfMail toneOfMail2 = new ToneOfMail();
							toneOfMail2.setAnger(angerFinal2);
							toneOfMail2.setJoy(joyFinal2);
							toneOfMail2.setSadness(sadnessFinal2);
							toneOfMail2.setTentative(tentativeFinal2);
							toneOfMail2.setAnalytical(analyticalFinal2);
							toneOfMail2.setConfident(confidentFinal2);
							toneOfMail2.setFear(fearFinal2);*/
							
							//EmployeeBO employeeData1 =employeeRepository.findByEmployeeId(aggregationPojo.getEmployeeId());
							employeePersonalDataDTO.setEmployeeName(aggregationPojo.getName());
							
							employeePersonalDataDTO.setEmployeeId(aggregationPojo.getEmployeeIdFK());
							
							employeePersonalDataDTO.setDesignation(aggregationPojo.getEmployeeRoleBOs().get(0)
									.getDesignation());
				
							
							employeePersonalDataDTO.setToneOfTeamMail(toneOfMail);
							employeePersonalDataDTO.setToneOfTeamReceiveMail(toneOfMail1);
							employeePersonalDataDTO.setToneOfTeamSentMail(toneOfMail2);
							employeePersonalDataDTO.setNoOfMail(aggregationPojo.getTotalTeamMail());
							employeePersonalDataDTO.setNoOfReceiveMail(aggregationPojo.getTotalTeamMailRecevied());
							employeePersonalDataDTO.setNoOfSentMail(aggregationPojo.getTotalTeamMailSent());
						
							listOfEmployeeData.add(employeePersonalDataDTO);
							
						}
						
					
							  
						empldata.setListOfEmployee(listOfEmployeeData);
						empldata.setPageNumber(clientDataDTO.getPageNumber());
				
				return empldata;
			}catch(Exception e) {
				e.printStackTrace();
			}
			return emptyData;
			}
		

		public EmployeePersonalDataDTO getListOfEmployeeHierchy1(ClientDataDTO clientDataDTO){
			EmployeePersonalDataDTO emptyData = new EmployeePersonalDataDTO();
			try {
			EmployeePersonalDataDTO empldata = new EmployeePersonalDataDTO();
			EmployeeRoleBO empldataDTO = new EmployeeRoleBO();


			 List<EmployeePersonalDataDTO> listOfEmployeeData = new ArrayList<EmployeePersonalDataDTO>();
			 List<String> empReportTo = new ArrayList<String>();
			 List<String> empReportToId = new ArrayList<String>();
			 

			 EmployeeBO employeeData =employeeRepository.findByEmployeeId(clientDataDTO.getEmployeeId());
				
				EmployeeRoleBO employeeRoleBO = employeeRoleRepository.findByEmployeeIdFKAndStatus(clientDataDTO.getEmployeeId(),
						"active");
				empldataDTO = employeeRoleBO;
				empldata.setDesignation(employeeRoleBO.getDesignation());
				empldata.setEmployeeName(employeeData.getEmployeeName());
				empldata.setEmployeeId(employeeData.getEmployeeId());
			 
			 	empReportTo.add(employeeRepository.findByEmployeeId(employeeRoleBO.getEmployeeIdFK()).getEmployeeName());
				empReportToId.add(employeeRepository.findByEmployeeId(employeeRoleBO.getEmployeeIdFK()).getEmployeeId());
				String reportToId=employeeData.getEmployeeName(); 
				
				
				try {
					while(!reportToId.equals("")) {
						reportToId = employeeRoleRepository.findByEmployeeIdFKAndStatus(empldataDTO.getReportToId(), "active").getEmployeeIdFK();
						empReportTo.add(employeeRepository.findByEmployeeId(reportToId).getEmployeeName());
						empReportToId.add(employeeRepository.findByEmployeeId(reportToId).getEmployeeId());
						empldataDTO = employeeRoleRepository.findByEmployeeIdFKAndStatus(empldataDTO.getReportToId(), "active");
					};
				}catch(Exception e) {
					
				}
				Collections.reverse(empReportTo);
				Collections.reverse(empReportToId);
				empldata.setEmpReportToHierachy(empReportTo);
				empldata.setEmpReportToId(empReportToId);
			// SortOperation sortByPopDesc = sort(new Sort(Direction.DESC, "statePop"));
			 SortOperation sortOperation = null;
		/*	// SortOperation sortOperationOnName = null;
			if (clientDataDTO.getSortDataText()!=null ) {
				 
				if (clientDataDTO.getSortOnName()!=null) {
			 sortOperation = Aggregation.sort(Sort.Direction.DESC,"employeeRoleBOs.teamSize")
					 .and(Sort.Direction.DESC,"name");
				}else {
					 sortOperation = Aggregation.sort(Sort.Direction.DESC,"employeeRoleBOs.teamSize");
				}
			}else {
				
				if (clientDataDTO.getSortOnName()!=null) {
				 sortOperation =	Aggregation.sort(Sort.Direction.DESC,"name");
				}else {
					 sortOperation = Aggregation.sort(Sort.Direction.ASC,"employeeRoleBOs.teamSize")
							 .and(Sort.Direction.ASC,"name");
				}
			}*/
			
			List<SortOperation> listSort = new ArrayList<SortOperation>();
			
			if(clientDataDTO.getSearchCriteria()!=null){
			
			if (clientDataDTO.getSearchCriteria().contains("alphabetically")) {
				
				 sortOperation =	Aggregation.sort(Sort.Direction.DESC,"name");
				 listSort.add(0, sortOperation);
				
			}
			
			if (clientDataDTO.getSearchCriteria().contains("teamSize")) {
				
				if (listSort.size()>0) {
					
					sortOperation = Aggregation.sort(Sort.Direction.DESC,"employeeRoleBOs.teamSize")
							 .and(Sort.Direction.DESC,"name");
					 listSort.add(0, sortOperation);
				}else {
					 sortOperation = Aggregation.sort(Sort.Direction.DESC,"employeeRoleBOs.teamSize");
					 listSort.add(0, sortOperation);
				}
				
			}
			
			}
				
			if (listSort.size()==0) {
				 sortOperation = Aggregation.sort(Sort.Direction.ASC,"employeeRoleBOs.teamSize")
						 .and(Sort.Direction.ASC,"name");
				// listSort.add(0, sortOperation);
			}else {
				sortOperation = listSort.get(0);
			}
			
			
			
		/*if (clientDataDTO.getSortOnName()!=null) {
				 
				 sortOperationOnName = Aggregation.sort(Sort.Direction.DESC,"name");
				}else {
					sortOperationOnName =	Aggregation.sort(Sort.Direction.ASC,"name");
				}*/
					
					//Employee Reportee
					
//					Aggregation aggregation1 = Aggregation.newAggregation(
//							
//							
//							Aggregation.match(Criteria.where("reportToId").is(clientDataDTO.getEmployeeId())),
//							Aggregation.skip(0),
//							Aggregation.limit(5),
//							//sortOperation,
//							Aggregation.out("temp")
//							
//							
//								);
						
			Aggregation aggregation1 = Aggregation.newAggregation(
					
					
 					Aggregation.match(Criteria.where("status").is("active")),
 //					Aggregation.skip(0),
 //					Aggregation.limit(5),
 					//sortOperation,
 					Aggregation.out("temp")
					
					
 						);
						
						AggregationResults<EmployeeRoleBO> groupResults1 
						= mongoTemplate.aggregate(aggregation1, EmployeeRoleBO.class, EmployeeRoleBO.class);
						List<EmployeeRoleBO> result1 = groupResults1.getMappedResults();
					
					
					Aggregation aggregation2 = Aggregation.newAggregation(
							
							Aggregation.lookup("temp", "employeeIdFK", "employeeIdFK", "employeeRoleBOs"),
//							Aggregation.match(Criteria.where("employeeRoleBOs.reportToId").is(clientDataDTO.getEmployeeId()).and("date").gte("2018-04-16")),
							Aggregation.match(Criteria.where("employeeRoleBOs.status").is("active")),
							//sortOperationOnName,
							sortOperation,
							Aggregation.group("employeeIdFK").last("employeeIdFK").as("employeeIdFK").last("employeeRoleBOs").as("employeeRoleBOs").last("name").as("name")
							.sum("teamTone.allMailScore.anger").as("angerTeamTotal").sum("teamTone.allMailScore.angerCount")
							.as("angerTeamCount").sum("teamTone.allMailScore.joy").as("joyTeamTotal").sum("teamTone.allMailScore.joyCount")
							.as("joyTeamCount").sum("teamTone.allMailScore.sadness").as("sadnessTeamTotal").sum("teamTone.allMailScore.sadnessCount")
							.as("sadnessTeamCount").sum("teamTone.allMailScore.tentative").as("tentativeTeamTotal").sum("teamTone.allMailScore.tentativeCount")
							.as("tentativeTeamCount").sum("teamTone.allMailScore.analytical").as("analyticalTeamTotal").sum("teamTone.allMailScore.analyticalCount")
							.as("analyticalTeamCount").sum("teamTone.allMailScore.confident").as("confidentTeamTotal").sum("teamTone.allMailScore.confidentCount")
							.as("confidentTeamCount").sum("teamTone.allMailScore.fear").as("fearTeamTotal").sum("teamTone.allMailScore.fearCount")
							.as("fearTeamCount")
							.sum("teamTone.receiveMailScore.anger").as("angerTeamTotalrcv").sum("teamTone.receiveMailScore.angerCount")
							.as("angerTeamCountrcv").sum("teamTone.receiveMailScore.joy").as("joyTeamTotalrcv").sum("teamTone.receiveMailScore.joyCount")
							.as("joyTeamCountrcv").sum("teamTone.receiveMailScore.sadness").as("sadnessTeamTotalrcv").sum("teamTone.receiveMailScore.sadnessCount")
							.as("sadnessTeamCountrcv").sum("teamTone.receiveMailScore.tentative").as("tentativeTeamTotalrcv").sum("teamTone.receiveMailScore.tentativeCount")
							.as("tentativeTeamCountrcv").sum("teamTone.receiveMailScore.analytical").as("analyticalTeamTotalrcv").sum("teamTone.receiveMailScore.analyticalCount")
							.as("analyticalTeamCountrcv").sum("teamTone.receiveMailScore.confident").as("confidentTeamTotalrcv").sum("teamTone.receiveMailScore.confidentCount")
							.as("confidentTeamCountrcv").sum("teamTone.receiveMailScore.fear").as("fearTeamTotalrcv").sum("teamTone.receiveMailScore.fearCount")
							.as("fearTeamCountrcv")
							.sum("teamTone.sentMailScore.anger").as("angerTeamTotalsnt").sum("teamTone.sentMailScore.angerCount")
							.as("angerTeamCountsnt").sum("teamTone.sentMailScore.joy").as("joyTeamTotalsnt").sum("teamTone.sentMailScore.joyCount")
							.as("joyTeamCountsnt").sum("teamTone.sentMailScore.sadness").as("sadnessTeamTotalsnt").sum("teamTone.sentMailScore.sadnessCount")
							.as("sadnessTeamCountsnt").sum("teamTone.sentMailScore.tentative").as("tentativeTeamTotalsnt").sum("teamTone.sentMailScore.tentativeCount")
							.as("tentativeTeamCountsnt").sum("teamTone.sentMailScore.analytical").as("analyticalTeamTotalsnt").sum("teamTone.sentMailScore.analyticalCount")
							.as("analyticalTeamCountsnt").sum("teamTone.sentMailScore.confident").as("confidentTeamTotalsnt").sum("teamTone.sentMailScore.confidentCount")
							.as("confidentTeamCountsnt").sum("teamTone.sentMailScore.fear").as("fearTeamTotalsnt").sum("teamTone.sentMailScore.fearCount")
							.as("fearTeamCountsnt")
							.sum("teamTone.totalMail").as("totalTeamMail")
							.sum("teamTone.totalMailRecevied").as("totalTeamMailRecevied")
							.sum("teamTone.totalMailSent").as("totalTeamMailSent")
							
								);
						
						AggregationResults<AggregationPojo> groupResults2 
						= mongoTemplate.aggregate(aggregation2, DailyEmployeeEmailToneBO.class, AggregationPojo.class);
						List<AggregationPojo> result2 = groupResults2.getMappedResults();
						
						mongoTemplate.dropCollection("temp");
					
						
						for(AggregationPojo aggregationPojo : result2) {
							
							EmployeePersonalDataDTO employeePersonalDataDTO = new EmployeePersonalDataDTO();
							
							// Average of All Self Scores 
							
							
							// Average of All Self Scores 
							
							Double angerFinal = 0.0d;
							Double joyFinal = 0.0d;
							Double sadnessFinal = 0.0d;
							Double tentativeFinal = 0.0d;
							Double analyticalFinal = 0.0d;
							Double confidentFinal = 0.0d;
							Double fearFinal = 0.0d;
							
							if(aggregationPojo.getAngerTeamCount() != 0)
							angerFinal = aggregationPojo.getAngerTeamTotal()/aggregationPojo.getAngerTeamCount();
							if(aggregationPojo.getJoyTeamCount() != 0)
							joyFinal = aggregationPojo.getJoyTeamTotal()/aggregationPojo.getJoyTeamCount();
							if(aggregationPojo.getSadnessTeamCount() != 0)
							sadnessFinal = aggregationPojo.getSadnessTeamTotal()/aggregationPojo.getSadnessTeamCount();
							if(aggregationPojo.getTentativeTeamCount() != 0)
							tentativeFinal =aggregationPojo.getTentativeTeamTotal()/aggregationPojo.getTentativeTeamCount();
							if(aggregationPojo.getAnalyticalTeamCount() != 0)
							analyticalFinal =aggregationPojo.getAnalyticalTeamTotal()/aggregationPojo.getAnalyticalTeamCount();
							if(aggregationPojo.getConfidentTeamCount() != 0)
							confidentFinal = aggregationPojo.getConfidentTeamTotal()/aggregationPojo.getConfidentTeamCount();
							if(aggregationPojo.getFearTeamCount() != 0)
							fearFinal =aggregationPojo.getFearTeamTotal()/aggregationPojo.getFearTeamCount();
							
							ToneOfMail toneOfMail = new ToneOfMail();
							

							Double total = angerFinal+joyFinal+sadnessFinal+tentativeFinal+analyticalFinal+confidentFinal+fearFinal;
							Double angerP = 0.0d;
							Double joyP = 0.0d;
							Double sadnP = 0.0d;
							Double tenP = 0.0d;
							Double analP = 0.0d;
							Double confP = 0.0d;
							Double fearP = 0.0d;
							
							if(total != 0) {
							angerP = (double)Math.round(((angerFinal)*100)/(total));
							joyP = (double)Math.round(((joyFinal)*100)/(total));
							sadnP = (double)Math.round(((sadnessFinal)*100)/(total));
							tenP = (double)Math.round(((tentativeFinal)*100)/(total));
							analP = (double)Math.round(((analyticalFinal)*100)/(total));
							confP = (double)Math.round(((confidentFinal)*100)/(total));
							fearP =  (double)Math.round(((fearFinal)*100)/(total));
							}
							
							toneOfMail.setAnger(angerP);
							toneOfMail.setJoy(joyP);
							toneOfMail.setSadness(sadnP);
							toneOfMail.setTentative(tenP);
							toneOfMail.setAnalytical(analP);
							toneOfMail.setConfident(confP);
							toneOfMail.setFear(fearP);
							
							/*toneOfMail.setAnger(angerFinal);
							toneOfMail.setJoy(joyFinal);
							toneOfMail.setSadness(sadnessFinal);
							toneOfMail.setTentative(tentativeFinal);
							toneOfMail.setAnalytical(analyticalFinal);
							toneOfMail.setConfident(confidentFinal);
							toneOfMail.setFear(fearFinal);*/
							
							//rcv
							
							Double angerFinal1 = 0.0d;
							Double joyFinal1 = 0.0d;
							Double sadnessFinal1 = 0.0d;
							Double tentativeFinal1 = 0.0d;
							Double analyticalFinal1 = 0.0d;
							Double confidentFinal1 = 0.0d;
							Double fearFinal1 = 0.0d;
							
							if(aggregationPojo.getAngerTeamCountrcv() != 0)
							angerFinal1 = aggregationPojo.getAngerTeamTotalrcv()/aggregationPojo.getAngerTeamCountrcv();
							if(aggregationPojo.getJoyTeamCountrcv() != 0)
							joyFinal1 = aggregationPojo.getJoyTeamTotalrcv()/aggregationPojo.getJoyTeamCountrcv();
							if(aggregationPojo.getSadnessTeamCountrcv() != 0)
							sadnessFinal1 = aggregationPojo.getSadnessTeamTotalrcv()/aggregationPojo.getSadnessTeamCountrcv();
							if(aggregationPojo.getTentativeTeamCountrcv() != 0)
							tentativeFinal1 = aggregationPojo.getTentativeTeamTotalrcv()/aggregationPojo.getTentativeTeamCountrcv();
							if(aggregationPojo.getAnalyticalTeamCountrcv() != 0)
							analyticalFinal1 = aggregationPojo.getAnalyticalTeamTotalrcv()/aggregationPojo.getAnalyticalTeamCountrcv();
							if(aggregationPojo.getConfidentTeamCountrcv() != 0)
							confidentFinal1 = aggregationPojo.getConfidentTeamTotalrcv()/aggregationPojo.getConfidentTeamCountrcv();
							if(aggregationPojo.getFearTeamCountrcv() != 0)
							fearFinal1 =aggregationPojo.getFearTeamTotalrcv()/aggregationPojo.getFearTeamCountrcv();
							
							
							
							ToneOfMail toneOfMail1 = new ToneOfMail();
							
							Double totalr = angerFinal1+joyFinal1+sadnessFinal1+tentativeFinal1+analyticalFinal1+confidentFinal1+fearFinal1;
							Double angercP = 0.0d;
							Double joyrP = 0.0d;
							Double sadnrP = 0.0d;
							Double tenrP = 0.0d;
							Double analrP = 0.0d;
							Double confrP = 0.0d;
							Double fearrP = 0.0d;
							
							if(totalr != 0) {
							angercP = (double)Math.round(((angerFinal1)*100)/(totalr));
							joyrP = (double)Math.round(((joyFinal1)*100)/(totalr));
							sadnrP = (double)Math.round(((sadnessFinal1)*100)/(totalr));
							tenrP = (double)Math.round(((tentativeFinal1)*100)/(totalr));
							analrP = (double)Math.round(((analyticalFinal1)*100)/(totalr));
							confrP = (double)Math.round(((confidentFinal1)*100)/(totalr));
							fearrP =  (double)Math.round(((fearFinal1)*100)/(totalr));
							}
							
							toneOfMail1.setAnger(angercP);
							toneOfMail1.setJoy(joyrP);
							toneOfMail1.setSadness(sadnrP);
							toneOfMail1.setTentative(tenrP);
							toneOfMail1.setAnalytical(analrP);
							toneOfMail1.setConfident(confrP);
							toneOfMail1.setFear(fearrP);
							/*
							toneOfMail1.setAnger(angerFinal1);
							toneOfMail1.setJoy(joyFinal1);
							toneOfMail1.setSadness(sadnessFinal1);
							toneOfMail1.setTentative(tentativeFinal1);
							toneOfMail1.setAnalytical(analyticalFinal1);
							toneOfMail1.setConfident(confidentFinal1);
							toneOfMail1.setFear(fearFinal1);*/
							
							//snt
							
							Double angerFinal2 = 0.0d;
							Double joyFinal2 = 0.0d;
							Double sadnessFinal2 = 0.0d;
							Double tentativeFinal2 = 0.0d;
							Double analyticalFinal2 = 0.0d;
							Double confidentFinal2 = 0.0d;
							Double fearFinal2 = 0.0d;
							
							if(aggregationPojo.getAngerTeamCountsnt() != 0)
							angerFinal2 = aggregationPojo.getAngerTeamTotalsnt()/aggregationPojo.getAngerTeamCountsnt();
							if(aggregationPojo.getJoyTeamCountsnt() != 0)
							joyFinal2 = aggregationPojo.getJoyTeamTotalsnt()/aggregationPojo.getJoyTeamCountsnt();
							if(aggregationPojo.getSadnessTeamCountsnt() != 0)
							sadnessFinal2 = aggregationPojo.getSadnessTeamTotalsnt()/aggregationPojo.getSadnessTeamCountsnt();
							if(aggregationPojo.getTentativeTeamCountsnt() != 0)
							tentativeFinal2 =aggregationPojo.getTentativeTeamTotalsnt()/aggregationPojo.getTentativeTeamCountsnt();
							if(aggregationPojo.getAnalyticalTeamCountsnt() != 0)
							analyticalFinal2 = aggregationPojo.getAnalyticalTeamTotalsnt()/aggregationPojo.getAnalyticalTeamCountsnt();
							if(aggregationPojo.getConfidentTeamCountsnt() != 0)
							confidentFinal2 = aggregationPojo.getConfidentTeamTotalsnt()/aggregationPojo.getConfidentTeamCountsnt();
							if(aggregationPojo.getFearTeamCountsnt() !=0)
							fearFinal2 = aggregationPojo.getFearTeamTotalsnt()/aggregationPojo.getFearTeamCountsnt();
							
							ToneOfMail toneOfMail2 = new ToneOfMail();
							
							Double totalsnt = angerFinal2+joyFinal2+sadnessFinal2+tentativeFinal2+analyticalFinal2+confidentFinal2+fearFinal2;
							Double angersntP = 0.0d;
							Double joyrsntP = 0.0d;
							Double sadnrsntP = 0.0d;
							Double tenrsntP = 0.0d;
							Double analrsntP = 0.0d;
							Double confrsntP = 0.0d;
							Double fearrsntP = 0.0d;
							
							if(totalsnt != 0) {
							angersntP = (double)Math.round(((angerFinal2)*100)/(totalsnt));
							joyrsntP = (double)Math.round(((joyFinal2)*100)/(totalsnt));
							sadnrsntP = (double)Math.round(((sadnessFinal2)*100)/(totalsnt));
							tenrsntP = (double)Math.round(((tentativeFinal2)*100)/(totalsnt));
							analrsntP = (double)Math.round(((analyticalFinal2)*100)/(totalsnt));
							confrsntP = (double)Math.round(((confidentFinal2)*100)/(totalsnt));
							fearrsntP =  (double)Math.round(((fearFinal2)*100)/(totalsnt));
							}
							
							toneOfMail2.setAnger(angersntP);
							toneOfMail2.setJoy(joyrsntP);
							toneOfMail2.setSadness(sadnrsntP);
							toneOfMail2.setTentative(tenrsntP);
							toneOfMail2.setAnalytical(analrsntP);
							toneOfMail2.setConfident(confrsntP);
							toneOfMail2.setFear(fearrsntP);	
							
							
						/*	
							
							
							
							Double angerFinal = (double)Math.round(aggregationPojo.getAngerTeamTotal()/aggregationPojo.getAngerTeamCount());
							Double joyFinal = (double)Math.round(aggregationPojo.getJoyTeamTotal()/aggregationPojo.getJoyTeamCount());
							Double sadnessFinal = (double)Math.round(aggregationPojo.getSadnessTeamTotal()/aggregationPojo.getSadnessTeamCount());
							Double tentativeFinal = (double)Math.round(aggregationPojo.getTentativeTeamTotal()/aggregationPojo.getTentativeTeamCount());
							Double analyticalFinal = (double)Math.round(aggregationPojo.getAnalyticalTeamTotal()/aggregationPojo.getAnalyticalTeamCount());
							Double confidentFinal = (double)Math.round(aggregationPojo.getConfidentTeamTotal()/aggregationPojo.getConfidentTeamCount());
							Double fearFinal = (double)Math.round(aggregationPojo.getFearTeamTotal()/aggregationPojo.getFearTeamCount());
							
							ToneOfMail toneOfMail = new ToneOfMail();
							toneOfMail.setAnger(angerFinal);
							toneOfMail.setJoy(joyFinal);
							toneOfMail.setSadness(sadnessFinal);
							toneOfMail.setTentative(tentativeFinal);
							toneOfMail.setAnalytical(analyticalFinal);
							toneOfMail.setConfident(confidentFinal);
							toneOfMail.setFear(fearFinal);
							
							//rcv
							
							Double angerFinal1 = (double)Math.round(aggregationPojo.getAngerTeamTotalrcv()/aggregationPojo.getAngerTeamCountrcv());
							Double joyFinal1 = (double)Math.round(aggregationPojo.getJoyTeamTotalrcv()/aggregationPojo.getJoyTeamCountrcv());
							Double sadnessFinal1 = (double)Math.round(aggregationPojo.getSadnessTeamTotalrcv()/aggregationPojo.getSadnessTeamCountrcv());
							Double tentativeFinal1 = (double)Math.round(aggregationPojo.getTentativeTeamTotalrcv()/aggregationPojo.getTentativeTeamCountrcv());
							Double analyticalFinal1 = (double)Math.round(aggregationPojo.getAnalyticalTeamTotalrcv()/aggregationPojo.getAnalyticalTeamCountrcv());
							Double confidentFinal1 = (double)Math.round(aggregationPojo.getConfidentTeamTotalrcv()/aggregationPojo.getConfidentTeamCountrcv());
							Double fearFinal1 = (double)Math.round(aggregationPojo.getFearTeamTotalrcv()/aggregationPojo.getFearTeamCountrcv());
							
							ToneOfMail toneOfMail1 = new ToneOfMail();
							toneOfMail1.setAnger(angerFinal1);
							toneOfMail1.setJoy(joyFinal1);
							toneOfMail1.setSadness(sadnessFinal1);
							toneOfMail1.setTentative(tentativeFinal1);
							toneOfMail1.setAnalytical(analyticalFinal1);
							toneOfMail1.setConfident(confidentFinal1);
							toneOfMail1.setFear(fearFinal1);
							
							//snt
							
							Double angerFinal2 = (double)Math.round(aggregationPojo.getAngerTeamTotalsnt()/aggregationPojo.getAngerTeamCountsnt());
							Double joyFinal2 = (double)Math.round(aggregationPojo.getJoyTeamTotalsnt()/aggregationPojo.getJoyTeamCountsnt());
							Double sadnessFinal2 = (double)Math.round(aggregationPojo.getSadnessTeamTotalsnt()/aggregationPojo.getSadnessTeamCountsnt());
							Double tentativeFinal2 = (double)Math.round(aggregationPojo.getTentativeTeamTotalsnt()/aggregationPojo.getTentativeTeamCountsnt());
							Double analyticalFinal2 = (double)Math.round(aggregationPojo.getAnalyticalTeamTotalsnt()/aggregationPojo.getAnalyticalTeamCountsnt());
							Double confidentFinal2 = (double)Math.round(aggregationPojo.getConfidentTeamTotalsnt()/aggregationPojo.getConfidentTeamCountsnt());
							Double fearFinal2 = (double)Math.round(aggregationPojo.getFearTeamTotalsnt()/aggregationPojo.getFearTeamCountsnt());
							
							ToneOfMail toneOfMail2 = new ToneOfMail();
							toneOfMail2.setAnger(angerFinal2);
							toneOfMail2.setJoy(joyFinal2);
							toneOfMail2.setSadness(sadnessFinal2);
							toneOfMail2.setTentative(tentativeFinal2);
							toneOfMail2.setAnalytical(analyticalFinal2);
							toneOfMail2.setConfident(confidentFinal2);
							toneOfMail2.setFear(fearFinal2);*/
							
							//EmployeeBO employeeData1 =employeeRepository.findByEmployeeId(aggregationPojo.getEmployeeId());
							employeePersonalDataDTO.setEmployeeName(aggregationPojo.getName());
							
							employeePersonalDataDTO.setEmployeeId(aggregationPojo.getEmployeeIdFK());
							
							employeePersonalDataDTO.setDesignation(aggregationPojo.getEmployeeRoleBOs().get(0)
									.getDesignation());
				
							
							employeePersonalDataDTO.setToneOfTeamMail(toneOfMail);
							employeePersonalDataDTO.setToneOfTeamReceiveMail(toneOfMail1);
							employeePersonalDataDTO.setToneOfTeamSentMail(toneOfMail2);
							employeePersonalDataDTO.setNoOfMail(aggregationPojo.getTotalTeamMail());
							employeePersonalDataDTO.setNoOfReceiveMail(aggregationPojo.getTotalTeamMailRecevied());
							employeePersonalDataDTO.setNoOfSentMail(aggregationPojo.getTotalTeamMailSent());
						
							listOfEmployeeData.add(employeePersonalDataDTO);
							
						}
						
					
							  
						empldata.setListOfEmployee(listOfEmployeeData);
						empldata.setPageNumber(clientDataDTO.getPageNumber());
				
				return empldata;
			}catch(Exception e) {
				e.printStackTrace();
			}
			return emptyData;
			}
		
		
	
		
		
	
		
		
		// get All Individuals mail sent by his team 
		
		
		public  EmployeePersonalDataDTO getEachMemberMailOfTeam(EmployeePersonalDataDTO employeePersonalDataDTO) {
			
			EmployeePersonalDataDTO employeePersonalDataDTO2 = new EmployeePersonalDataDTO();
			

			EmployeeBO employeeData = employeeRepository.findByEmployeeId(employeePersonalDataDTO.getEmployeeId());
			EmployeeRoleBO employeeRolesPojo = employeeRoleRepository.findByEmployeeIdFKAndStatus(employeePersonalDataDTO.getEmployeeId(), "active");
		
					
			employeePersonalDataDTO2.setNoOfTeamMember(employeeRolesPojo.getTeamSize());
					// employeePersonalDataDTO.setConsolidatedTone(employeeRolesPojo.getConsolidatedTone());
					/* employeePersonalDataDTO.setToneOfTeamMail(employeeRolesPojo.getConsolidatedTone().getToneWithTeam()
							 .getAllMailScore());
					 employeePersonalDataDTO.setToneOfClientMail(employeeRolesPojo.getConsolidatedTone().getToneWithClient()
							 .getAllMailScore());*/
			employeePersonalDataDTO2.setDepartment(employeeRolesPojo.getDepartment());
			employeePersonalDataDTO2.setDesignation(employeeRolesPojo.getDesignation());
			employeePersonalDataDTO2.setNoOfMail(employeeRolesPojo.getConsolidatedTone().getTotalMail());
					 //Relation with team
					 Double joy =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getJoy();
					 Double tnt =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getTentative();
					 Double ana =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getAnalytical();
					 Double cnfdnc =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getConfident();
					 Double anger =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getAnger();
					 Double sad =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getSadness();
					 Double fear =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getFear();
					
					 Double joyness = (double) Math.round(((joy+tnt+ana+cnfdnc)*100)/(joy+tnt+ana+cnfdnc+anger+sad+fear));
					 Double angry =(double) Math.round(((anger+sad+fear)*100)/(joy+tnt+ana+cnfdnc+anger+sad+fear));
					 EmployeeRelationBO employeeRelationBO = new EmployeeRelationBO();
					 employeeRelationBO.setGood(joyness);
					 employeeRelationBO.setBad(angry);
					 employeePersonalDataDTO2.setRelationWithTeam(employeeRelationBO);
					 
					
					 
					 
					 //Relation with Client
					 Double joyClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getJoy();
					 Double anaClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getAnalytical();
					 Double tntClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getTentative();
					 Double confClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getConfident();
					 Double angerClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getAnger();
					 Double sadClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getSadness();
					 Double fearClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getFear();
					 
					
					 Double joynessClnt = (double) Math.round(((joyClnt+anaClnt+tntClnt+confClnt)*100)/(joyClnt+anaClnt+tntClnt+confClnt+angerClnt+sadClnt+fearClnt));
					 Double angryClnt =(double) Math.round(((angerClnt+sadClnt+fearClnt)*100)/(joyClnt+anaClnt+tntClnt+confClnt+angerClnt+sadClnt+fearClnt));
					 EmployeeRelationBO employeeRelationBO1 = new EmployeeRelationBO();
					 employeeRelationBO1.setGood(joynessClnt);
					 employeeRelationBO1.setBad(angryClnt);
					 employeePersonalDataDTO2.setRelationWithClient(employeeRelationBO1);
					 
					
					 
					 
					
				//}
			//}
			
					 employeePersonalDataDTO2.setEmployeeName(employeeData.getEmployeeName());
					 employeePersonalDataDTO2.setEmployeeId(employeeData.getEmployeeId());
					 employeePersonalDataDTO2.setEmailId(employeeData.getEmailId());
			
			if (employeeRolesPojo.getReportToId()==null) {
				
				employeePersonalDataDTO2.setReportTo("None");
				
			}else {
				employeePersonalDataDTO2.setReportTo(employeeRepository.findByEmployeeId(employeeRolesPojo.getReportToId()).getEmployeeName());
			}
			
			List<EmailPojo> emailPojos = new ArrayList<EmailPojo>();
			
			/*Query query = new Query();
			query.limit(10);
			query.skip(0);
			query.with(new Sort(Sort.Direction.ASC, "date"));
			query.with(new Sort(Sort.Direction.DESC, "lineItems.time"));*/
			
			Pageable pageable = new PageRequest(0, 20,new Sort(new Sort.Order(Sort.Direction.ASC,"date"),new Sort.Order(Sort.Direction.DESC,"lineItems.time")));
			
			 
			 Page<DailyEmployeeEmailToneBO> dailyEmployeeEmailToneBOs = dailyEmployeeEmailToneRepository.findByDateAndEmployeeIdFK("2018-04-17", employeePersonalDataDTO.getEmployeeId(), pageable);
			
			 List<DailyEmployeeEmailToneBO> dailyEmployeeEmailToneBOs1 = dailyEmployeeEmailToneBOs.getContent();
				 

					if (employeePersonalDataDTO.getIdentify().equalsIgnoreCase("all")) {
						
					for(DailyEmployeeEmailToneBO dailyEmployeeEmailToneBO : dailyEmployeeEmailToneBOs1) {
						
						// get each email and tone of each employee
						List<EmailPojo> lineItems = dailyEmployeeEmailToneBO.getLineItems();
						// List<EmailPojo> clientItems = dailyEmployeeEmailToneBO.getClientEmailItems();

						for (EmailPojo emailPojo : lineItems) {
							try {
								emailPojo.setFrom(employeeRepository.findByEmailIdIgnoreCase(emailPojo.getFromMail()).getEmployeeName());
							}catch(Exception e) {
								emailPojo.setFrom("");
							}
							Map<String,Double> hashMap = new HashMap<String,Double>(); 
							hashMap.put("anger", emailPojo.getAnger());
							hashMap.put("joy", emailPojo.getJoy());
							hashMap.put("sadness", emailPojo.getSadness());
							hashMap.put("tentative", emailPojo.getTentative());
							hashMap.put("analytical", emailPojo.getAnalytical());
							hashMap.put("confident", emailPojo.getConfident());
							hashMap.put("fear", emailPojo.getFear());
							
							  Map<String, Double> sortedMapDsc =	sortByComparator(hashMap, false);
							  int count =0;
							  for (Entry<String, Double> entry : sortedMapDsc.entrySet())
						        {
						            System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
						            
						            
						         
						            switch (entry.getKey()) {
						            
									case "anger":
										if (count<2) {
											emailPojo.setAnger(entry.getValue());
											count++;
											UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
													.getContext().getAuthentication();
											
//											EmployeePersonalDataDTO employeePersonalDataDTO1 = new EmployeePersonalDataDTO();
											
											String emailId = (String) authObj.getUserSessionInformation().get(
													EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
											
											EmployeeBO employeeData1 =employeeRepository.findOne(emailId);
											
											ClientBO clients= clientRepository.findByEmployeeIdFK(employeeData1.getEmployeeId());
											if(clients != null) {
												if(emailPojo.getType().equalsIgnoreCase("Received")) {
													
													if(clients.getClients() != null) {
														for(FilterPojo client: clients.getClients()) {
															if(client.getEmailId().equalsIgnoreCase(emailPojo.getFromMail())) {
																if(client.getExecutive() != null)
																if(client.getExecutive().equalsIgnoreCase("yes")) {
																	emailPojo.setAdverse("yes");
																}
															}
														}
													}
													
												}else if(emailPojo.getType().equalsIgnoreCase("sent")){
													if(clients.getClients() != null) {
														for(FilterPojo client: clients.getClients()) {
															for(String clientEmail: emailPojo.getToClientEmails()) {
																if(client.getEmailId().equalsIgnoreCase(clientEmail)) {
																	if(client.getExecutive() != null)
																	if(client.getExecutive().equalsIgnoreCase("yes")) {
																		emailPojo.setAdverse("yes");
																	}
																}
															}
														}
													}
												}
											}
										}else {
											emailPojo.setAnger(0.0);
											count++;
										}
										
										break;
										
									case "joy":

										if (count<2) {
											emailPojo.setJoy(entry.getValue());
											count++;
										}else {
											emailPojo.setJoy(0.0);
											count++;
										}
										break;	
									case "sadness":
										if (count<2) {
											emailPojo.setSadness(entry.getValue());
											count++;
										}else {
											emailPojo.setSadness(0.0);
											count++;
										}
										break;
										
									case "tentative":
										if (count<2) {
											emailPojo.setTentative(entry.getValue());
											count++;
										}else {
											emailPojo.setTentative(0.0);
											count++;
										}
										break;
																			
									case "analytical":
										if (count<2) {
											emailPojo.setAnalytical(entry.getValue());
											count++;
										}else {
											emailPojo.setAnalytical(0.0);
											count++;
										}
										break;
										
									case "confident":
										if (count<2) {
											emailPojo.setConfident(entry.getValue());
											count++;
										}else {
											emailPojo.setConfident(0.0);
											count++;
										}
										break;
										
									case "fear":
										if (count<2) {
											emailPojo.setFear(entry.getValue());
											count++;
										}else {
											emailPojo.setFear(0.0);
											count++;
										}
										break;

									default:
										break;
										
									}
						            
						          
						        }
							  
							  System.out.println(sortedMapDsc);

							/*Stream<Map.Entry<String,Double>> sorted =
									hashMap.entrySet().stream()
								       .sorted(Entry.comparingByValue());
							
							System.out.println(sorted.toString());
							Map<String, Double> map = sorted.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
							System.out.println(map);*/
//							  emailPojo.setFromMail();
							  
							  try {
								  if(emailPojo.getToClientEmails().size() > 0) {
									  System.out.println("in getToclients loop");
									  String toClientEmails = emailPojo.getToClientEmails().get(0);
									  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(toClientEmails.split("@")[1]).get(0).getCompanyName());
								  }else if(emailPojo.getFromMail().length() >0 && emailPojo.getFromMail().split("@")[1] != employeeData.getEmailId().split("@")[1] ) {
									  System.out.println("in from mail loop");
									  String domain = emailPojo.getFromMail().split("@")[1];
									  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(domain).get(0).getCompanyName());
								  }else {
									  System.out.println("in else loop");
									  emailPojo.setCompanyName("");
								  }
							  }catch(Exception e) {
								  System.err.println(e);
							  }
							  
							  emailPojos.add(emailPojo);
						}
						
// 						if(clientItems!=null)
// 						for (EmailPojo emailPojo : clientItems) {
// 							try {
// 								emailPojo.setFrom(employeeRepository.findByEmailIdIgnoreCase(emailPojo.getFromMail()).getEmployeeName());
// 							}catch(Exception e) {
// 								emailPojo.setFrom("");
// 							}
// 							Map<String,Double> hashMap = new HashMap<String,Double>(); 
// 							hashMap.put("anger", emailPojo.getAnger());
// 							hashMap.put("joy", emailPojo.getJoy());
// 							hashMap.put("sadness", emailPojo.getSadness());
// 							hashMap.put("tentative", emailPojo.getTentative());
// 							hashMap.put("analytical", emailPojo.getAnalytical());
// 							hashMap.put("confident", emailPojo.getConfident());
// 							hashMap.put("fear", emailPojo.getFear());
							
// 							  Map<String, Double> sortedMapDsc =	sortByComparator(hashMap, false);
// 							  int count =0;
// 							  for (Entry<String, Double> entry : sortedMapDsc.entrySet())
// 						        {
// 						            System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
						            
						            
						         
// 						            switch (entry.getKey()) {
						            
// 									case "anger":
// 										if (count<2) {
// 											emailPojo.setAnger(entry.getValue());
// 											count++;
// 											UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
// 													.getContext().getAuthentication();
											
// //											EmployeePersonalDataDTO employeePersonalDataDTO1 = new EmployeePersonalDataDTO();
											
// 											String emailId = (String) authObj.getUserSessionInformation().get(
// 													EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
											
// 											EmployeeBO employeeData1 =employeeRepository.findOne(emailId);
											
// 											ClientBO clients= clientRepository.findByEmployeeIdFK(employeeData1.getEmployeeId());
// 											if(clients != null) {
// 												if(emailPojo.getType().equalsIgnoreCase("Received")) {
													
// 													if(clients.getClients() != null) {
// 														for(FilterPojo client: clients.getClients()) {
// 															if(client.getEmailId().equalsIgnoreCase(emailPojo.getFromMail())) {
// 																if(client.getExecutive() != null)
// 																if(client.getExecutive().equalsIgnoreCase("yes")) {
// 																	emailPojo.setAdverse("yes");
// 																}
// 															}
// 														}
// 													}
													
// 												}else if(emailPojo.getType().equalsIgnoreCase("sent")){
// 													if(clients.getClients() != null) {
// 														for(FilterPojo client: clients.getClients()) {
// 															for(String clientEmail: emailPojo.getToClientEmails()) {
// 																if(client.getEmailId().equalsIgnoreCase(clientEmail)) {
// 																	if(client.getExecutive() != null)
// 																	if(client.getExecutive().equalsIgnoreCase("yes")) {
// 																		emailPojo.setAdverse("yes");
// 																	}
// 																}
// 															}
// 														}
// 													}
// 												}
// 											}
// 										}else {
// 											emailPojo.setAnger(0.0);
// 											count++;
// 										}
										
// 										break;
										
// 									case "joy":

// 										if (count<2) {
// 											emailPojo.setJoy(entry.getValue());
// 											count++;
// 										}else {
// 											emailPojo.setJoy(0.0);
// 											count++;
// 										}
// 										break;	
// 									case "sadness":
// 										if (count<2) {
// 											emailPojo.setSadness(entry.getValue());
// 											count++;
// 										}else {
// 											emailPojo.setSadness(0.0);
// 											count++;
// 										}
// 										break;
										
// 									case "tentative":
// 										if (count<2) {
// 											emailPojo.setTentative(entry.getValue());
// 											count++;
// 										}else {
// 											emailPojo.setTentative(0.0);
// 											count++;
// 										}
// 										break;
																			
// 									case "analytical":
// 										if (count<2) {
// 											emailPojo.setAnalytical(entry.getValue());
// 											count++;
// 										}else {
// 											emailPojo.setAnalytical(0.0);
// 											count++;
// 										}
// 										break;
										
// 									case "confident":
// 										if (count<2) {
// 											emailPojo.setConfident(entry.getValue());
// 											count++;
// 										}else {
// 											emailPojo.setConfident(0.0);
// 											count++;
// 										}
// 										break;
										
// 									case "fear":
// 										if (count<2) {
// 											emailPojo.setFear(entry.getValue());
// 											count++;
// 										}else {
// 											emailPojo.setFear(0.0);
// 											count++;
// 										}
// 										break;

// 									default:
// 										break;
										
// 									}
						            
						          
// 						        }
							  
// 							  System.out.println(sortedMapDsc);

// 							/*Stream<Map.Entry<String,Double>> sorted =
// 									hashMap.entrySet().stream()
// 								       .sorted(Entry.comparingByValue());
							
// 							System.out.println(sorted.toString());
// 							Map<String, Double> map = sorted.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
// 							System.out.println(map);*/
// //							  emailPojo.setFromMail();
							  
// 							  try {
// 								  if(emailPojo.getToClientEmails().size() > 0) {
// 									  System.out.println("in getToclients loop");
// 									  String toClientEmails = emailPojo.getToClientEmails().get(0);
// 									  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(toClientEmails.split("@")[1]).get(0).getCompanyName());
// 								  }else if(emailPojo.getFromMail().length() >0 && emailPojo.getFromMail().split("@")[1] != employeeData.getEmailId().split("@")[1] ) {
// 									  System.out.println("in from mail loop");
// 									  String domain = emailPojo.getFromMail().split("@")[1];
// 									  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(domain).get(0).getCompanyName());
// 								  }else {
// 									  System.out.println("in else loop");
// 									  emailPojo.setCompanyName("");
// 								  }
// 							  }catch(Exception e) {
// 								  System.err.println(e);
// 							  }
							  
// 							  emailPojos.add(emailPojo);
// 						}
						//list.addAll(lineItems);
						
					}
					
					}
					
					
					//for all received mail
					
					if (employeePersonalDataDTO.getIdentify().equalsIgnoreCase("receive")) {
						
					
					
					Aggregation aggregation2 = Aggregation.newAggregation(
							
							Aggregation.match(Criteria.where("employeeIdFK").is(employeePersonalDataDTO.getEmployeeId())),
							Aggregation.unwind("lineItems"),
							Aggregation.match(Criteria.where("lineItems.type").is("Received"))
							
							
							);
					
					List<DailyEmployeeTeamUnwindPojo> results = mongoTemplate.aggregate(aggregation2, DailyEmployeeEmailToneBO.class, DailyEmployeeTeamUnwindPojo.class).getMappedResults();
					
					for(DailyEmployeeTeamUnwindPojo basicDBObject : results) {
						
						EmailPojo emailPojo = basicDBObject.getLineItems();
						try {
							emailPojo.setFrom(employeeRepository.findByEmailIdIgnoreCase(emailPojo.getFromMail()).getEmployeeName());
						}catch(Exception e) {
							emailPojo.setFrom("");
						}
						Map<String,Double> hashMap = new HashMap<String,Double>(); 
						hashMap.put("anger", emailPojo.getAnger());
						hashMap.put("joy", emailPojo.getJoy());
						hashMap.put("sadness", emailPojo.getSadness());
						hashMap.put("tentative", emailPojo.getTentative());
						hashMap.put("analytical", emailPojo.getAnalytical());
						hashMap.put("confident", emailPojo.getConfident());
						hashMap.put("fear", emailPojo.getFear());
						
						  Map<String, Double> sortedMapDsc =	sortByComparator(hashMap, false);
						  int count =0;
						  for (Entry<String, Double> entry : sortedMapDsc.entrySet())
					        {
					            System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
					            
					            
					         
					            switch (entry.getKey()) {
					            
								case "anger":
									if (count<2) {
										emailPojo.setAnger(entry.getValue());
										count++;
										UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
												.getContext().getAuthentication();
										
//										EmployeePersonalDataDTO employeePersonalDataDTO1 = new EmployeePersonalDataDTO();
										
										String emailId = (String) authObj.getUserSessionInformation().get(
												EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
										
										EmployeeBO employeeData1 =employeeRepository.findOne(emailId);
										
										ClientBO clients= clientRepository.findByEmployeeIdFK(employeeData1.getEmployeeId());
										if(clients != null) {
											if(emailPojo.getType().equalsIgnoreCase("Received")) {
												
												if(clients.getClients() != null) {
													for(FilterPojo client: clients.getClients()) {
														if(client.getEmailId().equalsIgnoreCase(emailPojo.getFromMail())) {
															if(client.getExecutive() != null)
															if(client.getExecutive().equalsIgnoreCase("yes")) {
																emailPojo.setAdverse("yes");
															}
														}
													}
												}
												
											}else if(emailPojo.getType().equalsIgnoreCase("sent")){
												if(clients.getClients() != null) {
													for(FilterPojo client: clients.getClients()) {
														for(String clientEmail: emailPojo.getToClientEmails()) {
															if(client.getEmailId().equalsIgnoreCase(clientEmail)) {
																if(client.getExecutive() != null)
																if(client.getExecutive().equalsIgnoreCase("yes")) {
																	emailPojo.setAdverse("yes");
																}
															}
														}
													}
												}
											}
										}
									}else {
										emailPojo.setAnger(0.0);
										count++;
									}
									
									break;
									
								case "joy":

									if (count<2) {
										emailPojo.setJoy(entry.getValue());
										count++;
									}else {
										emailPojo.setJoy(0.0);
										count++;
									}
									break;	
								case "sadness":
									if (count<2) {
										emailPojo.setSadness(entry.getValue());
										count++;
									}else {
										emailPojo.setSadness(0.0);
										count++;
									}
									break;
									
								case "tentative":
									if (count<2) {
										emailPojo.setTentative(entry.getValue());
										count++;
									}else {
										emailPojo.setTentative(0.0);
										count++;
									}
									break;
																		
								case "analytical":
									if (count<2) {
										emailPojo.setAnalytical(entry.getValue());
										count++;
									}else {
										emailPojo.setAnalytical(0.0);
										count++;
									}
									break;
									
								case "confident":
									if (count<2) {
										emailPojo.setConfident(entry.getValue());
										count++;
									}else {
										emailPojo.setConfident(0.0);
										count++;
									}
									break;
									
								case "fear":
									if (count<2) {
										emailPojo.setFear(entry.getValue());
										count++;
									}else {
										emailPojo.setFear(0.0);
										count++;
									}
									break;

								default:
									break;
									
								}
					            
					          
					        }
						  
						  System.out.println(sortedMapDsc);
						  
						  try {
							  if(emailPojo.getToClientEmails().size() > 0) {
								  System.out.println("in getToclients loop");
								  String toClientEmails = emailPojo.getToClientEmails().get(0);
								  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(toClientEmails.split("@")[1]).get(0).getCompanyName());
							  }else if(emailPojo.getFromMail().length() >0 && emailPojo.getFromMail().split("@")[1] != employeeData.getEmailId().split("@")[1] ) {
								  System.out.println("in from mail loop");
								  String domain = emailPojo.getFromMail().split("@")[1];
								  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(domain).get(0).getCompanyName());
							  }else {
								  System.out.println("in else loop");
								  emailPojo.setCompanyName("");
							  }
						  }catch(Exception e) {
							  System.err.println(e);
						  }

						  emailPojos.add(emailPojo);
					
						
						
						//list.add(listRcv);
						
					}
					
					//client receive
					
// 					Aggregation aggregation2clnt = Aggregation.newAggregation(
							
// 							Aggregation.match(Criteria.where("employeeIdFK").is(employeePersonalDataDTO.getEmployeeId())),
// 							Aggregation.unwind("clientEmailItems"),
// 							Aggregation.match(Criteria.where("clientEmailItems.type").is("Received"))
							
							
// 							);
					
// 					List<DailyEmployeeTeamUnwindPojo> resultsclnt = mongoTemplate.aggregate(aggregation2clnt, DailyEmployeeEmailToneBO.class, DailyEmployeeTeamUnwindPojo.class).getMappedResults();
					
// 					for(DailyEmployeeTeamUnwindPojo basicDBObject : resultsclnt) {
						
// 						EmailPojo emailPojo = basicDBObject.getClientEmailItems();
// 						try {
// 							emailPojo.setFrom(employeeRepository.findByEmailIdIgnoreCase(emailPojo.getFromMail()).getEmployeeName());
// 						}catch(Exception e) {
// 							emailPojo.setFrom("");
// 						}
// 						Map<String,Double> hashMap = new HashMap<String,Double>(); 
// 						hashMap.put("anger", emailPojo.getAnger());
// 						hashMap.put("joy", emailPojo.getJoy());
// 						hashMap.put("sadness", emailPojo.getSadness());
// 						hashMap.put("tentative", emailPojo.getTentative());
// 						hashMap.put("analytical", emailPojo.getAnalytical());
// 						hashMap.put("confident", emailPojo.getConfident());
// 						hashMap.put("fear", emailPojo.getFear());
						
// 						  Map<String, Double> sortedMapDsc =	sortByComparator(hashMap, false);
// 						  int count =0;
// 						  for (Entry<String, Double> entry : sortedMapDsc.entrySet())
// 					        {
// 					            System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
					            
					            
					         
// 					            switch (entry.getKey()) {
					            
// 								case "anger":
// 									if (count<2) {
// 										emailPojo.setAnger(entry.getValue());
// 										count++;
// 										UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
// 												.getContext().getAuthentication();
										
// //										EmployeePersonalDataDTO employeePersonalDataDTO1 = new EmployeePersonalDataDTO();
										
// 										String emailId = (String) authObj.getUserSessionInformation().get(
// 												EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
										
// 										EmployeeBO employeeData1 =employeeRepository.findOne(emailId);
										
// 										ClientBO clients= clientRepository.findByEmployeeIdFK(employeeData1.getEmployeeId());
// 										if(clients != null) {
// 											if(emailPojo.getType().equalsIgnoreCase("Received")) {
												
// 												if(clients.getClients() != null) {
// 													for(FilterPojo client: clients.getClients()) {
// 														if(client.getEmailId().equalsIgnoreCase(emailPojo.getFromMail())) {
// 															if(client.getExecutive() != null)
// 															if(client.getExecutive().equalsIgnoreCase("yes")) {
// 																emailPojo.setAdverse("yes");
// 															}
// 														}
// 													}
// 												}
												
// 											}else if(emailPojo.getType().equalsIgnoreCase("sent")){
// 												if(clients.getClients() != null) {
// 													for(FilterPojo client: clients.getClients()) {
// 														for(String clientEmail: emailPojo.getToClientEmails()) {
// 															if(client.getEmailId().equalsIgnoreCase(clientEmail)) {
// 																if(client.getExecutive() != null)
// 																if(client.getExecutive().equalsIgnoreCase("yes")) {
// 																	emailPojo.setAdverse("yes");
// 																}
// 															}
// 														}
// 													}
// 												}
// 											}
// 										}
// 									}else {
// 										emailPojo.setAnger(0.0);
// 										count++;
// 									}
									
// 									break;
									
// 								case "joy":

// 									if (count<2) {
// 										emailPojo.setJoy(entry.getValue());
// 										count++;
// 									}else {
// 										emailPojo.setJoy(0.0);
// 										count++;
// 									}
// 									break;	
// 								case "sadness":
// 									if (count<2) {
// 										emailPojo.setSadness(entry.getValue());
// 										count++;
// 									}else {
// 										emailPojo.setSadness(0.0);
// 										count++;
// 									}
// 									break;
									
// 								case "tentative":
// 									if (count<2) {
// 										emailPojo.setTentative(entry.getValue());
// 										count++;
// 									}else {
// 										emailPojo.setTentative(0.0);
// 										count++;
// 									}
// 									break;
																		
// 								case "analytical":
// 									if (count<2) {
// 										emailPojo.setAnalytical(entry.getValue());
// 										count++;
// 									}else {
// 										emailPojo.setAnalytical(0.0);
// 										count++;
// 									}
// 									break;
									
// 								case "confident":
// 									if (count<2) {
// 										emailPojo.setConfident(entry.getValue());
// 										count++;
// 									}else {
// 										emailPojo.setConfident(0.0);
// 										count++;
// 									}
// 									break;
									
// 								case "fear":
// 									if (count<2) {
// 										emailPojo.setFear(entry.getValue());
// 										count++;
// 									}else {
// 										emailPojo.setFear(0.0);
// 										count++;
// 									}
// 									break;

// 								default:
// 									break;
									
// 								}
					            
					          
// 					        }
						  
// 						  System.out.println(sortedMapDsc);
						  
// 						  try {
// 							  if(emailPojo.getToClientEmails().size() > 0) {
// 								  System.out.println("in getToclients loop");
// 								  String toClientEmails = emailPojo.getToClientEmails().get(0);
// 								  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(toClientEmails.split("@")[1]).get(0).getCompanyName());
// 							  }else if(emailPojo.getFromMail().length() >0 && emailPojo.getFromMail().split("@")[1] != employeeData.getEmailId().split("@")[1] ) {
// 								  System.out.println("in from mail loop");
// 								  String domain = emailPojo.getFromMail().split("@")[1];
// 								  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(domain).get(0).getCompanyName());
// 							  }else {
// 								  System.out.println("in else loop");
// 								  emailPojo.setCompanyName("");
// 							  }
// 						  }catch(Exception e) {
// 							  System.err.println(e);
// 					}
					
// 						  emailPojos.add(emailPojo);
					
						
						
// 						//list.add(listRcv);
						
// 					}
					
					}
					
					//for all sent mail
					
							if (employeePersonalDataDTO.getIdentify().equalsIgnoreCase("sent")) {
								
							
							
							Aggregation aggregation2 = Aggregation.newAggregation(
									
									Aggregation.match(Criteria.where("employeeIdFK").is(employeePersonalDataDTO.getEmployeeId())),
									Aggregation.unwind("lineItems"),
									Aggregation.match(Criteria.where("lineItems.type").is("sent"))
									
									
									);
							
							List<DailyEmployeeTeamUnwindPojo> results = mongoTemplate.aggregate(aggregation2, DailyEmployeeEmailToneBO.class, DailyEmployeeTeamUnwindPojo.class).getMappedResults();
							
							for(DailyEmployeeTeamUnwindPojo basicDBObject : results) {
								
								EmailPojo emailPojo = basicDBObject.getLineItems();
								try {
									emailPojo.setFrom(employeeRepository.findByEmailIdIgnoreCase(emailPojo.getFromMail()).getEmployeeName());
								}catch(Exception e) {
									emailPojo.setFrom("");
								}
								Map<String,Double> hashMap = new HashMap<String,Double>(); 
								hashMap.put("anger", emailPojo.getAnger());
								hashMap.put("joy", emailPojo.getJoy());
								hashMap.put("sadness", emailPojo.getSadness());
								hashMap.put("tentative", emailPojo.getTentative());
								hashMap.put("analytical", emailPojo.getAnalytical());
								hashMap.put("confident", emailPojo.getConfident());
								hashMap.put("fear", emailPojo.getFear());
								
								  Map<String, Double> sortedMapDsc =	sortByComparator(hashMap, false);
								  int count =0;
								  for (Entry<String, Double> entry : sortedMapDsc.entrySet())
							        {
							            System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
							            
							            
							         
							            switch (entry.getKey()) {
							            
										case "anger":
											if (count<2) {
												emailPojo.setAnger(entry.getValue());
												count++;
												UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
														.getContext().getAuthentication();
												
//												EmployeePersonalDataDTO employeePersonalDataDTO1 = new EmployeePersonalDataDTO();
												
												String emailId = (String) authObj.getUserSessionInformation().get(
														EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
												
												EmployeeBO employeeData1 =employeeRepository.findOne(emailId);
												
												ClientBO clients= clientRepository.findByEmployeeIdFK(employeeData1.getEmployeeId());
												if(clients != null) {
													if(emailPojo.getType().equalsIgnoreCase("Received")) {
														
														if(clients.getClients() != null) {
															for(FilterPojo client: clients.getClients()) {
																if(client.getEmailId().equalsIgnoreCase(emailPojo.getFromMail())) {
																	if(client.getExecutive() != null)
																	if(client.getExecutive().equalsIgnoreCase("yes")) {
																		emailPojo.setAdverse("yes");
																	}
																}
															}
														}
														
													}else if(emailPojo.getType().equalsIgnoreCase("sent")){
														if(clients.getClients() != null) {
															for(FilterPojo client: clients.getClients()) {
																for(String clientEmail: emailPojo.getToClientEmails()) {
																	if(client.getEmailId().equalsIgnoreCase(clientEmail)) {
																		if(client.getExecutive() != null)
																		if(client.getExecutive().equalsIgnoreCase("yes")) {
																			emailPojo.setAdverse("yes");
																		}
																	}
																}
															}
														}
													}
												}
											}else {
												emailPojo.setAnger(0.0);
												count++;
											}
											
											break;
											
										case "joy":

											if (count<2) {
												emailPojo.setJoy(entry.getValue());
												count++;
											}else {
												emailPojo.setJoy(0.0);
												count++;
											}
											break;	
										case "sadness":
											if (count<2) {
												emailPojo.setSadness(entry.getValue());
												count++;
											}else {
												emailPojo.setSadness(0.0);
												count++;
											}
											break;
											
										case "tentative":
											if (count<2) {
												emailPojo.setTentative(entry.getValue());
												count++;
											}else {
												emailPojo.setTentative(0.0);
												count++;
											}
											break;
																				
										case "analytical":
											if (count<2) {
												emailPojo.setAnalytical(entry.getValue());
												count++;
											}else {
												emailPojo.setAnalytical(0.0);
												count++;
											}
											break;
											
										case "confident":
											if (count<2) {
												emailPojo.setConfident(entry.getValue());
												count++;
											}else {
												emailPojo.setConfident(0.0);
												count++;
											}
											break;
											
										case "fear":
											if (count<2) {
												emailPojo.setFear(entry.getValue());
												count++;
											}else {
												emailPojo.setFear(0.0);
												count++;
											}
											break;

										default:
											break;
											
										}
							            
							          
							        }
								  
								  System.out.println(sortedMapDsc);
								  try {
									  if(emailPojo.getToClientEmails().size() > 0) {
										  System.out.println("in getToclients loop");
										  String toClientEmails = emailPojo.getToClientEmails().get(0);
										  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(toClientEmails.split("@")[1]).get(0).getCompanyName());
									  }else if(emailPojo.getFromMail().length() >0 && emailPojo.getFromMail().split("@")[1] != employeeData.getEmailId().split("@")[1] ) {
										  System.out.println("in from mail loop");
										  String domain = emailPojo.getFromMail().split("@")[1];
										  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(domain).get(0).getCompanyName());
									  }else {
										  System.out.println("in else loop");
										  emailPojo.setCompanyName("");
									  }
								  }catch(Exception e) {
									  System.err.println(e);
								  }
								  emailPojos.add(emailPojo);
								
								
							//	list.add(listRcv);
								
							}
							
							//clnt sent
							
// 							Aggregation aggregation2snt = Aggregation.newAggregation(
									
// 									Aggregation.match(Criteria.where("employeeIdFK").is(employeePersonalDataDTO.getEmployeeId())),
// 									Aggregation.unwind("clientEmailItems"),
// 									Aggregation.match(Criteria.where("clientEmailItems.type").is("sent"))
									
									
// 									);
							
// 							List<DailyEmployeeTeamUnwindPojo> resultssnt = mongoTemplate.aggregate(aggregation2snt, DailyEmployeeEmailToneBO.class, DailyEmployeeTeamUnwindPojo.class).getMappedResults();
							
							
// 								for(DailyEmployeeTeamUnwindPojo basicDBObject : resultssnt) {
								
// 								EmailPojo emailPojo = basicDBObject.getClientEmailItems();
// 								try {
// 									emailPojo.setFrom(employeeRepository.findByEmailIdIgnoreCase(emailPojo.getFromMail()).getEmployeeName());
// 								}catch(Exception e) {
// 									emailPojo.setFrom("");
// 							}
// 								Map<String,Double> hashMap = new HashMap<String,Double>(); 
// 								hashMap.put("anger", emailPojo.getAnger());
// 								hashMap.put("joy", emailPojo.getJoy());
// 								hashMap.put("sadness", emailPojo.getSadness());
// 								hashMap.put("tentative", emailPojo.getTentative());
// 								hashMap.put("analytical", emailPojo.getAnalytical());
// 								hashMap.put("confident", emailPojo.getConfident());
// 								hashMap.put("fear", emailPojo.getFear());
				
// 								  Map<String, Double> sortedMapDsc =	sortByComparator(hashMap, false);
// 								  int count =0;
// 								  for (Entry<String, Double> entry : sortedMapDsc.entrySet())
// 							        {
// 							            System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
							            
							            
							         
// 							            switch (entry.getKey()) {
							            
// 										case "anger":
// 											if (count<2) {
// 												emailPojo.setAnger(entry.getValue());
// 												count++;
// 												UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
// 														.getContext().getAuthentication();
												
// //												EmployeePersonalDataDTO employeePersonalDataDTO1 = new EmployeePersonalDataDTO();
												
// 												String emailId = (String) authObj.getUserSessionInformation().get(
// 														EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
												
// 												EmployeeBO employeeData1 =employeeRepository.findOne(emailId);
												
// 												ClientBO clients= clientRepository.findByEmployeeIdFK(employeeData1.getEmployeeId());
// 												if(clients != null) {
// 													if(emailPojo.getType().equalsIgnoreCase("Received")) {
														
// 														if(clients.getClients() != null) {
// 															for(FilterPojo client: clients.getClients()) {
// 																if(client.getEmailId().equalsIgnoreCase(emailPojo.getFromMail())) {
// 																	if(client.getExecutive() != null)
// 																	if(client.getExecutive().equalsIgnoreCase("yes")) {
// 																		emailPojo.setAdverse("yes");
// 																	}
// 																}
// 															}
// 														}
														
// 													}else if(emailPojo.getType().equalsIgnoreCase("sent")){
// 														if(clients.getClients() != null) {
// 															for(FilterPojo client: clients.getClients()) {
// 																for(String clientEmail: emailPojo.getToClientEmails()) {
// 																	if(client.getEmailId().equalsIgnoreCase(clientEmail)) {
// 																		if(client.getExecutive() != null)
// 																		if(client.getExecutive().equalsIgnoreCase("yes")) {
// 																			emailPojo.setAdverse("yes");
// 																		}
// 																	}
// 																}
// 															}
// 														}
// 													}
// 												}
// 											}else {
// 												emailPojo.setAnger(0.0);
// 												count++;
// 											}
											
// 											break;
											
// 										case "joy":

// 											if (count<2) {
// 												emailPojo.setJoy(entry.getValue());
// 												count++;
// 											}else {
// 												emailPojo.setJoy(0.0);
// 												count++;
// 											}
// 											break;	
// 										case "sadness":
// 											if (count<2) {
// 												emailPojo.setSadness(entry.getValue());
// 												count++;
// 											}else {
// 												emailPojo.setSadness(0.0);
// 												count++;
// 											}
// 											break;
											
// 										case "tentative":
// 											if (count<2) {
// 												emailPojo.setTentative(entry.getValue());
// 												count++;
// 											}else {
// 												emailPojo.setTentative(0.0);
// 												count++;
// 											}
// 											break;
																				
// 										case "analytical":
// 											if (count<2) {
// 												emailPojo.setAnalytical(entry.getValue());
// 												count++;
// 											}else {
// 												emailPojo.setAnalytical(0.0);
// 												count++;
// 											}
// 											break;
											
// 										case "confident":
// 											if (count<2) {
// 												emailPojo.setConfident(entry.getValue());
// 												count++;
// 											}else {
// 												emailPojo.setConfident(0.0);
// 												count++;
// 											}
// 											break;
											
// 										case "fear":
// 											if (count<2) {
// 												emailPojo.setFear(entry.getValue());
// 												count++;
// 											}else {
// 												emailPojo.setFear(0.0);
// 												count++;
// 											}
// 											break;

// 										default:
// 											break;
											
// 										}
							            
							          
// 							        }
								  
// 								  System.out.println(sortedMapDsc);
// 								  try {
// 									  if(emailPojo.getToClientEmails().size() > 0) {
// 										  System.out.println("in getToclients loop");
// 										  String toClientEmails = emailPojo.getToClientEmails().get(0);
// 										  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(toClientEmails.split("@")[1]).get(0).getCompanyName());
// 									  }else if(emailPojo.getFromMail().length() >0 && emailPojo.getFromMail().split("@")[1] != employeeData.getEmailId().split("@")[1] ) {
// 										  System.out.println("in from mail loop");
// 										  String domain = emailPojo.getFromMail().split("@")[1];
// 										  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(domain).get(0).getCompanyName());
// 									  }else {
// 										  System.out.println("in else loop");
// 										  emailPojo.setCompanyName("");
// 									  }
// 								  }catch(Exception e) {
// 									  System.err.println(e);
// 								  }
// 								  emailPojos.add(emailPojo);
								
								
// 							//	list.add(listRcv);
								
// 							}
							
 							}
				
				// emailPojos.addAll(dailyEmployeeEmailToneBO.getLineItems());
			 
			 employeePersonalDataDTO2.setListEmailAnalyser(emailPojos);
							
			 
			return employeePersonalDataDTO2;
		}
		
		
		
		public  EmployeePersonalDataDTO getClientEmails(EmployeePersonalDataDTO employeePersonalDataDTO) {
			
			UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
					.getContext().getAuthentication();
			
//			EmployeePersonalDataDTO employeePersonalDataDTO = new EmployeePersonalDataDTO();
			
			String emailId = (String) authObj.getUserSessionInformation().get(
					EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
			
			Boolean receive = false;
			Boolean sent  = false;
			
			
			EmployeePersonalDataDTO employeePersonalDataDTO2 = new EmployeePersonalDataDTO();
			

//			EmployeeBO employeeData = employeeRepository.findByEmployeeEmail(employeePersonalDataDTO.getEmployeeId());
			EmployeeBO employeeData = employeeRepository.findByEmailIdIgnoreCase(emailId);
			EmployeeRoleBO employeeRolesPojo = employeeRoleRepository.findByEmployeeIdFKAndStatus(employeeData.getEmployeeId(), "active");
			System.out.println("In client emails service");
			System.out.println(employeePersonalDataDTO.getClientEmailId());
					
			employeePersonalDataDTO2.setNoOfTeamMember(employeeRolesPojo.getTeamSize());
					// employeePersonalDataDTO.setConsolidatedTone(employeeRolesPojo.getConsolidatedTone());
					/* employeePersonalDataDTO.setToneOfTeamMail(employeeRolesPojo.getConsolidatedTone().getToneWithTeam()
							 .getAllMailScore());
					 employeePersonalDataDTO.setToneOfClientMail(employeeRolesPojo.getConsolidatedTone().getToneWithClient()
							 .getAllMailScore());*/
			employeePersonalDataDTO2.setDepartment(employeeRolesPojo.getDepartment());
			employeePersonalDataDTO2.setDesignation(employeeRolesPojo.getDesignation());
			employeePersonalDataDTO2.setNoOfMail(employeeRolesPojo.getConsolidatedTone().getTotalMail());
					 //Relation with team
					 Double joy =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getJoy();
					 Double tnt =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getTentative();
					 Double ana =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getAnalytical();
					 Double cnfdnc =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getConfident();
					 Double anger =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getAnger();
					 Double sad =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getSadness();
					 Double fear =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getFear();
					
					 Double joyness = (double) Math.round(((joy+tnt+ana+cnfdnc)*100)/(joy+tnt+ana+cnfdnc+anger+sad+fear));
					 Double angry =(double) Math.round(((anger+sad+fear)*100)/(joy+tnt+ana+cnfdnc+anger+sad+fear));
					 EmployeeRelationBO employeeRelationBO = new EmployeeRelationBO();
					 employeeRelationBO.setGood(joyness);
					 employeeRelationBO.setBad(angry);
					 employeePersonalDataDTO2.setRelationWithTeam(employeeRelationBO);
					 
					
					 
					 
					 //Relation with Client
					 Double joyClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getJoy();
					 Double anaClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getAnalytical();
					 Double tntClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getTentative();
					 Double confClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getConfident();
					 Double angerClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getAnger();
					 Double sadClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getSadness();
					 Double fearClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getFear();
					 
					
					 Double joynessClnt = (double) Math.round(((joyClnt+anaClnt+tntClnt+confClnt)*100)/(joyClnt+anaClnt+tntClnt+confClnt+angerClnt+sadClnt+fearClnt));
					 Double angryClnt =(double) Math.round(((angerClnt+sadClnt+fearClnt)*100)/(joyClnt+anaClnt+tntClnt+confClnt+angerClnt+sadClnt+fearClnt));
					 EmployeeRelationBO employeeRelationBO1 = new EmployeeRelationBO();
					 employeeRelationBO1.setGood(joynessClnt);
					 employeeRelationBO1.setBad(angryClnt);
					 employeePersonalDataDTO2.setRelationWithClient(employeeRelationBO1);
					 
					
					 
					 
					
				//}
			//}
			
					 employeePersonalDataDTO2.setEmployeeName(employeeData.getEmployeeName());
					 employeePersonalDataDTO2.setEmployeeId(employeeData.getEmployeeId());
					 employeePersonalDataDTO2.setEmailId(employeeData.getEmailId());
			
			if (employeeRolesPojo.getReportToId()==null) {
				
				employeePersonalDataDTO2.setReportTo("None");
				
			}else {
				employeePersonalDataDTO2.setReportTo(employeeRepository.findByEmployeeId(employeeRolesPojo.getReportToId()).getEmployeeName());
			}
			
			List<EmailPojo> emailPojos = new ArrayList<EmailPojo>();
			
			/*Query query = new Query();
			query.limit(10);
			query.skip(0);
			query.with(new Sort(Sort.Direction.ASC, "date"));
			query.with(new Sort(Sort.Direction.DESC, "lineItems.time"));*/
			
			Pageable pageable = new PageRequest(0, 20,new Sort(new Sort.Order(Sort.Direction.ASC,"date"),new Sort.Order(Sort.Direction.DESC,"lineItems.time")));
			
			 
			 Page<DailyEmployeeEmailToneBO> dailyEmployeeEmailToneBOs = dailyEmployeeEmailToneRepository.findByDateAndEmployeeIdFK("2018-04-17", employeePersonalDataDTO.getEmployeeId(), pageable);
			
			 List<DailyEmployeeEmailToneBO> dailyEmployeeEmailToneBOs1 = dailyEmployeeEmailToneBOs.getContent();
				 

					if (employeePersonalDataDTO.getIdentify().equalsIgnoreCase("all")) {
						System.out.println("all");
						
//					for(DailyEmployeeEmailToneBO dailyEmployeeEmailToneBO : dailyEmployeeEmailToneBOs1) {
//						
//						// get each email and tone of each employee
//						List<EmailPojo> lineItems = dailyEmployeeEmailToneBO.getLineItems();
//						
//
//						for (EmailPojo emailPojo : lineItems) {
//							try {
//								emailPojo.setFrom(employeeRepository.findByEmailIdIgnoreCase(emailPojo.getFromMail()).getEmployeeName());
//							}catch(Exception e) {
//								emailPojo.setFrom("");
//							}
//							Map<String,Double> hashMap = new HashMap<String,Double>(); 
//							hashMap.put("anger", emailPojo.getAnger());
//							hashMap.put("joy", emailPojo.getJoy());
//							hashMap.put("sadness", emailPojo.getSadness());
//							hashMap.put("tentative", emailPojo.getTentative());
//							hashMap.put("analytical", emailPojo.getAnalytical());
//							hashMap.put("confident", emailPojo.getConfident());
//							hashMap.put("fear", emailPojo.getFear());
//							
//							  Map<String, Double> sortedMapDsc =	sortByComparator(hashMap, false);
//							  int count =0;
//							  for (Entry<String, Double> entry : sortedMapDsc.entrySet())
//						        {
//						            System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
//						            
//						            
//						         
//						            switch (entry.getKey()) {
//						            
//									case "anger":
//										if (count<2) {
//											emailPojo.setAnger(entry.getValue());
//											count++;
//										}else {
//											emailPojo.setAnger(0.0);
//											count++;
//										}
//										
//										break;
//										
//									case "joy":
//
//										if (count<2) {
//											emailPojo.setJoy(entry.getValue());
//											count++;
//										}else {
//											emailPojo.setJoy(0.0);
//											count++;
//										}
//										break;	
//									case "sadness":
//										if (count<2) {
//											emailPojo.setSadness(entry.getValue());
//											count++;
//										}else {
//											emailPojo.setSadness(0.0);
//											count++;
//										}
//										break;
//										
//									case "tentative":
//										if (count<2) {
//											emailPojo.setTentative(entry.getValue());
//											count++;
//										}else {
//											emailPojo.setTentative(0.0);
//											count++;
//										}
//										break;
//																			
//									case "analytical":
//										if (count<2) {
//											emailPojo.setAnalytical(entry.getValue());
//											count++;
//										}else {
//											emailPojo.setAnalytical(0.0);
//											count++;
//										}
//										break;
//										
//									case "confident":
//										if (count<2) {
//											emailPojo.setConfident(entry.getValue());
//											count++;
//										}else {
//											emailPojo.setConfident(0.0);
//											count++;
//										}
//										break;
//										
//									case "fear":
//										if (count<2) {
//											emailPojo.setFear(entry.getValue());
//											count++;
//										}else {
//											emailPojo.setFear(0.0);
//											count++;
//										}
//										break;
//
//									default:
//										break;
//										
//									}
//						            
//						          
//						        }
//							  
//							  System.out.println(sortedMapDsc);
//
//							/*Stream<Map.Entry<String,Double>> sorted =
//									hashMap.entrySet().stream()
//								       .sorted(Entry.comparingByValue());
//							
//							System.out.println(sorted.toString());
//							Map<String, Double> map = sorted.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//							System.out.println(map);*/
////							  emailPojo.setFromMail();
//							  
//							  try {
//								  if(emailPojo.getToClientEmails().size() > 0) {
//									  System.out.println("in getToclients loop");
//									  String toClientEmails = emailPojo.getToClientEmails().get(0);
//									  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(toClientEmails.split("@")[1]).get(0).getCompanyName());
//								  }else if(emailPojo.getFromMail().length() >0 && emailPojo.getFromMail().split("@")[1] != employeeData.getEmailId().split("@")[1] ) {
//									  System.out.println("in from mail loop");
//									  String domain = emailPojo.getFromMail().split("@")[1];
//									  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(domain).get(0).getCompanyName());
//								  }else {
//									  System.out.println("in else loop");
//									  emailPojo.setCompanyName("");
//								  }
//							  }catch(Exception e) {
//								  System.err.println(e);
//							  }
//							  
//							  emailPojos.add(emailPojo);
//						}
//						
//						//list.addAll(lineItems);
//						
//					}
					
						receive = true;
						sent = true;
						
					}
					
					
					//for all received mail

					if (employeePersonalDataDTO.getIdentify().equalsIgnoreCase("receive")){
						receive = true;
					}
					
					if (employeePersonalDataDTO.getIdentify().equalsIgnoreCase("sent")) {
						sent = true;
					}
						
					
					if(receive) {
						System.out.println("receive");
					Aggregation aggregation2 = Aggregation.newAggregation(
							
							Aggregation.match(Criteria.where("employeeIdFK").is(employeeData.getEmployeeId())),
//							Aggregation.unwind("clientEmailItems"),
							Aggregation.unwind("lineItems"),
							Aggregation.match(Criteria.where("lineItems.type").is("Received")),
							Aggregation.match(Criteria.where("lineItems.fromMail").is(employeePersonalDataDTO.getClientEmailId()))
							
							
							);
					
					List<DailyEmployeeTeamUnwindLinePojo> results = mongoTemplate.aggregate(aggregation2, DailyEmployeeEmailToneBO.class, DailyEmployeeTeamUnwindLinePojo.class).getMappedResults();
					
					for(DailyEmployeeTeamUnwindLinePojo basicDBObject : results) {
						
						EmailPojo emailPojo = basicDBObject.getLineItems();
						if(emailPojo.getFromMail().equals(employeePersonalDataDTO.getClientEmailId()) && emailPojo.getType().equals("Received")) {
						try {
							emailPojo.setFrom(employeeRepository.findByEmailIdIgnoreCase(emailPojo.getFromMail()).getEmployeeName());
						}catch(Exception e) {
							emailPojo.setFrom("");
						}
						System.out.println(emailPojo.getSubject());
						Map<String,Double> hashMap = new HashMap<String,Double>(); 
						hashMap.put("anger", emailPojo.getAnger());
						hashMap.put("joy", emailPojo.getJoy());
						hashMap.put("sadness", emailPojo.getSadness());
						hashMap.put("tentative", emailPojo.getTentative());
						hashMap.put("analytical", emailPojo.getAnalytical());
						hashMap.put("confident", emailPojo.getConfident());
						hashMap.put("fear", emailPojo.getFear());
						
						  Map<String, Double> sortedMapDsc =	sortByComparator(hashMap, false);
						  int count =0;
						  for (Entry<String, Double> entry : sortedMapDsc.entrySet())
					        {
					            System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
					            
					            
					         
					            switch (entry.getKey()) {
					            
								case "anger":
									if (count<2) {
										emailPojo.setAnger(entry.getValue());
										count++;
//										UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
//												.getContext().getAuthentication();
//										
////										
//										
//										String emailId = (String) authObj.getUserSessionInformation().get(
//												EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
										
										EmployeeBO employeeData1 =employeeRepository.findOne(emailId);
										
										ClientBO clients= clientRepository.findByEmployeeIdFK(employeeData1.getEmployeeId());
										if(clients != null) {
											if(emailPojo.getType().equalsIgnoreCase("Received")) {
												
												if(clients.getClients() != null) {
													for(FilterPojo client: clients.getClients()) {
														if(client.getEmailId().equalsIgnoreCase(emailPojo.getFromMail())) {
															if(client.getExecutive() != null)
															if(client.getExecutive().equalsIgnoreCase("yes")) {
																emailPojo.setAdverse("yes");
															}
														}
													}
												}
												
											}else if(emailPojo.getType().equalsIgnoreCase("sent")){
												if(clients.getClients() != null) {
													for(FilterPojo client: clients.getClients()) {
														for(String clientEmail: emailPojo.getToClientEmails()) {
															if(client.getEmailId().equalsIgnoreCase(clientEmail)) {
																if(client.getExecutive() != null)
																if(client.getExecutive().equalsIgnoreCase("yes")) {
																	emailPojo.setAdverse("yes");
																}
															}
														}
													}
												}
											}
										}
									}else {
										emailPojo.setAnger(0.0);
										count++;
									}
									
									break;
									
								case "joy":

									if (count<2) {
										emailPojo.setJoy(entry.getValue());
										count++;
									}else {
										emailPojo.setJoy(0.0);
										count++;
									}
									break;	
								case "sadness":
									if (count<2) {
										emailPojo.setSadness(entry.getValue());
										count++;
									}else {
										emailPojo.setSadness(0.0);
										count++;
									}
									break;
									
								case "tentative":
									if (count<2) {
										emailPojo.setTentative(entry.getValue());
										count++;
									}else {
										emailPojo.setTentative(0.0);
										count++;
									}
									break;
																		
								case "analytical":
									if (count<2) {
										emailPojo.setAnalytical(entry.getValue());
										count++;
									}else {
										emailPojo.setAnalytical(0.0);
										count++;
									}
									break;
									
								case "confident":
									if (count<2) {
										emailPojo.setConfident(entry.getValue());
										count++;
									}else {
										emailPojo.setConfident(0.0);
										count++;
									}
									break;
									
								case "fear":
									if (count<2) {
										emailPojo.setFear(entry.getValue());
										count++;
									}else {
										emailPojo.setFear(0.0);
										count++;
									}
									break;

								default:
									break;
									
								}
					            
					          
					        }
						  
						  System.out.println(sortedMapDsc);
						  
						  try {
							  if(emailPojo.getToClientEmails().size() > 0) {
								  System.out.println("in getToclients loop");
								  String toClientEmails = emailPojo.getToClientEmails().get(0);
								  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(toClientEmails.split("@")[1]).get(0).getCompanyName());
							  }else if(emailPojo.getFromMail().length() >0 && emailPojo.getFromMail().split("@")[1] != employeeData.getEmailId().split("@")[1] ) {
								  System.out.println("in from mail loop");
								  String domain = emailPojo.getFromMail().split("@")[1];
								  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(domain).get(0).getCompanyName());
							  }else {
								  System.out.println("in else loop");
								  emailPojo.setCompanyName("");
							  }
						  }catch(Exception e) {
							  System.err.println(e);
						  }

						  emailPojos.add(emailPojo);
					
						}
						
						//list.add(listRcv);
						
					}
					// client items rcv
				
					Aggregation aggregation3 = Aggregation.newAggregation(
							
							Aggregation.match(Criteria.where("employeeIdFK").is(employeeData.getEmployeeId())),
//							Aggregation.unwind("lineItems"),
							Aggregation.unwind("clientEmailItems"),
							Aggregation.match(Criteria.where("clientEmailItems.type").is("Received")),
							Aggregation.match(Criteria.where("clientEmailItems.fromMail").is(employeePersonalDataDTO.getClientEmailId()))
							
							
							
							);
					List<DailyEmployeeTeamUnwindClientPojo> results1 =  new ArrayList<>() ;
					try {
					results1 = mongoTemplate.aggregate(aggregation3, DailyEmployeeEmailToneBO.class, DailyEmployeeTeamUnwindClientPojo.class).getMappedResults();
					}catch(Exception e) {
						e.printStackTrace();
					}
					
//					List<DailyEmployeeEmailToneBO> employeeMails = dailyEmployeeEmailToneRepository.findByFromMailAndEmployeeFKAndType(employeePersonalDataDTO.getClientEmailId(), employeeData.getEmployeeId(), "Received");
					
//					if(employeeMails != null)
					for(DailyEmployeeTeamUnwindClientPojo basicDBObject : results1) {
//					for(DailyEmployeeEmailToneBO basicDBObject: employeeMails) {
						
						EmailPojo emailPojo = basicDBObject.getClientEmailItems();
						
//						List<EmailPojo> emailPojo1 = basicDBObject.getClientEmailItems();
//						for(EmailPojo emailPojo : emailPojo1 ) {
//						if(emailPojo.getType().equals("Received") && emailPojo.getFromMail().equals(employeePersonalDataDTO.getClientEmailId())) {
						try {
							emailPojo.setFrom(employeeRepository.findByEmailIdIgnoreCase(emailPojo.getFromMail()).getEmployeeName());
						}catch(Exception e) {
							emailPojo.setFrom("");
						}
						System.out.println(emailPojo.getSubject());
						Map<String,Double> hashMap = new HashMap<String,Double>(); 
						hashMap.put("anger", emailPojo.getAnger());
						hashMap.put("joy", emailPojo.getJoy());
						hashMap.put("sadness", emailPojo.getSadness());
						hashMap.put("tentative", emailPojo.getTentative());
						hashMap.put("analytical", emailPojo.getAnalytical());
						hashMap.put("confident", emailPojo.getConfident());
						hashMap.put("fear", emailPojo.getFear());
						
						  Map<String, Double> sortedMapDsc =	sortByComparator(hashMap, false);
						  int count =0;
						  for (Entry<String, Double> entry : sortedMapDsc.entrySet())
					        {
					            System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
					            
					            
					         
					            switch (entry.getKey()) {
					            
								case "anger":
									if (count<2) {
										emailPojo.setAnger(entry.getValue());
										count++;
//										UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
//												.getContext().getAuthentication();
//										
////										EmployeePersonalDataDTO employeePersonalDataDTO1 = new EmployeePersonalDataDTO();
//										
//										String emailId = (String) authObj.getUserSessionInformation().get(
//												EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
										
										EmployeeBO employeeData1 =employeeRepository.findOne(emailId);
										
										ClientBO clients= clientRepository.findByEmployeeIdFK(employeeData1.getEmployeeId());
										if(clients != null) {
											if(emailPojo.getType().equalsIgnoreCase("Received")) {
												
												if(clients.getClients() != null) {
													for(FilterPojo client: clients.getClients()) {
														if(client.getEmailId().equalsIgnoreCase(emailPojo.getFromMail())) {
															if(client.getExecutive() != null)
															if(client.getExecutive().equalsIgnoreCase("yes")) {
																emailPojo.setAdverse("yes");
															}
														}
													}
												}
												
											}else if(emailPojo.getType().equalsIgnoreCase("sent")){
												if(clients.getClients() != null) {
													for(FilterPojo client: clients.getClients()) {
														for(String clientEmail: emailPojo.getToClientEmails()) {
															if(client.getEmailId().equalsIgnoreCase(clientEmail)) {
																if(client.getExecutive() != null)
																if(client.getExecutive().equalsIgnoreCase("yes")) {
																	emailPojo.setAdverse("yes");
																}
															}
														}
													}
												}
											}
										}
									}else {
										emailPojo.setAnger(0.0);
										count++;
									}
									
									break;
									
								case "joy":

									if (count<2) {
										emailPojo.setJoy(entry.getValue());
										count++;
									}else {
										emailPojo.setJoy(0.0);
										count++;
									}
									break;	
								case "sadness":
									if (count<2) {
										emailPojo.setSadness(entry.getValue());
										count++;
									}else {
										emailPojo.setSadness(0.0);
										count++;
									}
									break;
									
								case "tentative":
									if (count<2) {
										emailPojo.setTentative(entry.getValue());
										count++;
									}else {
										emailPojo.setTentative(0.0);
										count++;
									}
									break;
																		
								case "analytical":
									if (count<2) {
										emailPojo.setAnalytical(entry.getValue());
										count++;
									}else {
										emailPojo.setAnalytical(0.0);
										count++;
									}
									break;
									
								case "confident":
									if (count<2) {
										emailPojo.setConfident(entry.getValue());
										count++;
									}else {
										emailPojo.setConfident(0.0);
										count++;
									}
									break;
									
								case "fear":
									if (count<2) {
										emailPojo.setFear(entry.getValue());
										count++;
									}else {
										emailPojo.setFear(0.0);
										count++;
									}
									break;

								default:
									break;
									
								}
					            
					          
					        }
						  
						  System.out.println(sortedMapDsc);
						  
						  try {
							  if(emailPojo.getToClientEmails().size() > 0) {
								  System.out.println("in getToclients loop");
								  String toClientEmails = emailPojo.getToClientEmails().get(0);
								  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(toClientEmails.split("@")[1]).get(0).getCompanyName());
							  }else if(emailPojo.getFromMail().length() >0 && emailPojo.getFromMail().split("@")[1] != employeeData.getEmailId().split("@")[1] ) {
								  System.out.println("in from mail loop");
								  String domain = emailPojo.getFromMail().split("@")[1];
								  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(domain).get(0).getCompanyName());
							  }else {
								  System.out.println("in else loop");
								  emailPojo.setCompanyName("");
							  }
						  }catch(Exception e) {
							  System.err.println(e);
						  }

						  emailPojos.add(emailPojo);
						
						
						
						//list.add(listRcv);
						
					}
					
					
					}
					
					//for all sent mail
							
							if (sent) {
								System.out.println("sent");
							
							
							Aggregation aggregation2 = Aggregation.newAggregation(
									
									Aggregation.match(Criteria.where("employeeIdFK").is(employeeData.getEmployeeId())),
									Aggregation.unwind("lineItems"),
									Aggregation.match(Criteria.where("lineItems.type").is("sent")),
									Aggregation.match(Criteria.where("lineItems.toClientEmails").is(employeePersonalDataDTO.getClientEmailId()))
//									Aggregation.unwind("clientEmailItems")
									
									);
							
							List<DailyEmployeeTeamUnwindLinePojo> results = mongoTemplate.aggregate(aggregation2, DailyEmployeeEmailToneBO.class, DailyEmployeeTeamUnwindLinePojo.class).getMappedResults();
							
							for(DailyEmployeeTeamUnwindLinePojo basicDBObject : results) {
								
								EmailPojo emailPojo = basicDBObject.getLineItems();
								try {
									emailPojo.setFrom(employeeRepository.findByEmailIdIgnoreCase(emailPojo.getFromMail()).getEmployeeName());
								}catch(Exception e) {
									emailPojo.setFrom("");
								}
								System.out.println(emailPojo.getSubject());
								Map<String,Double> hashMap = new HashMap<String,Double>(); 
								hashMap.put("anger", emailPojo.getAnger());
								hashMap.put("joy", emailPojo.getJoy());
								hashMap.put("sadness", emailPojo.getSadness());
								hashMap.put("tentative", emailPojo.getTentative());
								hashMap.put("analytical", emailPojo.getAnalytical());
								hashMap.put("confident", emailPojo.getConfident());
								hashMap.put("fear", emailPojo.getFear());
								
								  Map<String, Double> sortedMapDsc =	sortByComparator(hashMap, false);
								  int count =0;
								  for (Entry<String, Double> entry : sortedMapDsc.entrySet())
							        {
							            System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
							            
							            
							         
							            switch (entry.getKey()) {
							            
										case "anger":
											if (count<2) {
												emailPojo.setAnger(entry.getValue());
												count++;
//												UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
//														.getContext().getAuthentication();
//												
////												EmployeePersonalDataDTO employeePersonalDataDTO1 = new EmployeePersonalDataDTO();
//												
//												String emailId = (String) authObj.getUserSessionInformation().get(
//														EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
												
												EmployeeBO employeeData1 =employeeRepository.findOne(emailId);
												
												ClientBO clients= clientRepository.findByEmployeeIdFK(employeeData1.getEmployeeId());
												if(clients != null) {
													if(emailPojo.getType().equalsIgnoreCase("Received")) {
														
														if(clients.getClients() != null) {
															for(FilterPojo client: clients.getClients()) {
																if(client.getEmailId().equalsIgnoreCase(emailPojo.getFromMail())) {
																	if(client.getExecutive() != null)
																	if(client.getExecutive().equalsIgnoreCase("yes")) {
																		emailPojo.setAdverse("yes");
																	}
																}
															}
														}
														
													}else if(emailPojo.getType().equalsIgnoreCase("sent")){
														if(clients.getClients() != null) {
															for(FilterPojo client: clients.getClients()) {
																for(String clientEmail: emailPojo.getToClientEmails()) {
																	if(client.getEmailId().equalsIgnoreCase(clientEmail)) {
																		if(client.getExecutive() != null)
																		if(client.getExecutive().equalsIgnoreCase("yes")) {
																			emailPojo.setAdverse("yes");
																		}
																	}
																}
															}
														}
													}
												}
											}else {
												emailPojo.setAnger(0.0);
												count++;
											}
											
											break;
											
										case "joy":

											if (count<2) {
												emailPojo.setJoy(entry.getValue());
												count++;
											}else {
												emailPojo.setJoy(0.0);
												count++;
											}
											break;	
										case "sadness":
											if (count<2) {
												emailPojo.setSadness(entry.getValue());
												count++;
											}else {
												emailPojo.setSadness(0.0);
												count++;
											}
											break;
											
										case "tentative":
											if (count<2) {
												emailPojo.setTentative(entry.getValue());
												count++;
											}else {
												emailPojo.setTentative(0.0);
												count++;
											}
											break;
																				
										case "analytical":
											if (count<2) {
												emailPojo.setAnalytical(entry.getValue());
												count++;
											}else {
												emailPojo.setAnalytical(0.0);
												count++;
											}
											break;
											
										case "confident":
											if (count<2) {
												emailPojo.setConfident(entry.getValue());
												count++;
											}else {
												emailPojo.setConfident(0.0);
												count++;
											}
											break;
											
										case "fear":
											if (count<2) {
												emailPojo.setFear(entry.getValue());
												count++;
											}else {
												emailPojo.setFear(0.0);
												count++;
											}
											break;

										default:
											break;
											
										}
							            
							          
							        }
								  
								  System.out.println(sortedMapDsc);
								  try {
									  if(emailPojo.getToClientEmails().size() > 0) {
										  System.out.println("in getToclients loop");
										  String toClientEmails = emailPojo.getToClientEmails().get(0);
										  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(toClientEmails.split("@")[1]).get(0).getCompanyName());
									  }else if(emailPojo.getFromMail().length() >0 && emailPojo.getFromMail().split("@")[1] != employeeData.getEmailId().split("@")[1] ) {
										  System.out.println("in from mail loop");
										  String domain = emailPojo.getFromMail().split("@")[1];
										  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(domain).get(0).getCompanyName());
									  }else {
										  System.out.println("in else loop");
										  emailPojo.setCompanyName("");
									  }
								  }catch(Exception e) {
									  System.err.println(e);
								  }
								  emailPojos.add(emailPojo);
								
								
							//	list.add(listRcv);
								
							}
							
							Aggregation aggregation3 = Aggregation.newAggregation(
									
									Aggregation.match(Criteria.where("employeeIdFK").is(employeeData.getEmployeeId())),
									Aggregation.unwind("clientEmailItems"),
									Aggregation.match(Criteria.where("clientEmailItems.type").is("sent")),
									Aggregation.match(Criteria.where("clientEmailItems.toClientEmails").is(employeePersonalDataDTO.getClientEmailId()))
//									Aggregation.unwind("lineItems")
									
									);
							
							List<DailyEmployeeTeamUnwindClientPojo> results1 = mongoTemplate.aggregate(aggregation3, DailyEmployeeEmailToneBO.class, DailyEmployeeTeamUnwindClientPojo.class).getMappedResults();
							
							for(DailyEmployeeTeamUnwindClientPojo basicDBObject : results1) {
								
								EmailPojo emailPojo = basicDBObject.getClientEmailItems();
								
								try {
									emailPojo.setFrom(employeeRepository.findByEmailIdIgnoreCase(emailPojo.getFromMail()).getEmployeeName());
								}catch(Exception e) {
									emailPojo.setFrom("");
								}
								System.out.println(emailPojo.getSubject());
								Map<String,Double> hashMap = new HashMap<String,Double>(); 
								hashMap.put("anger", emailPojo.getAnger());
								hashMap.put("joy", emailPojo.getJoy());
								hashMap.put("sadness", emailPojo.getSadness());
								hashMap.put("tentative", emailPojo.getTentative());
								hashMap.put("analytical", emailPojo.getAnalytical());
								hashMap.put("confident", emailPojo.getConfident());
								hashMap.put("fear", emailPojo.getFear());
								
								  Map<String, Double> sortedMapDsc =	sortByComparator(hashMap, false);
								  int count =0;
								  for (Entry<String, Double> entry : sortedMapDsc.entrySet())
							        {
							            System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
							            
							            
							         
							            switch (entry.getKey()) {
							            
										case "anger":
											if (count<2) {
												emailPojo.setAnger(entry.getValue());
												count++;
//												UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
//														.getContext().getAuthentication();
//												
////												EmployeePersonalDataDTO employeePersonalDataDTO1 = new EmployeePersonalDataDTO();
//												
//												String emailId = (String) authObj.getUserSessionInformation().get(
//														EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
												
												EmployeeBO employeeData1 =employeeRepository.findOne(emailId);
												
												ClientBO clients= clientRepository.findByEmployeeIdFK(employeeData1.getEmployeeId());
												if(clients != null) {
													if(emailPojo.getType().equalsIgnoreCase("Received")) {
														
														if(clients.getClients() != null) {
															for(FilterPojo client: clients.getClients()) {
																if(client.getEmailId().equalsIgnoreCase(emailPojo.getFromMail())) {
																	if(client.getExecutive() != null)
																	if(client.getExecutive().equalsIgnoreCase("yes")) {
																		emailPojo.setAdverse("yes");
																	}
																}
															}
														}
														
													}else if(emailPojo.getType().equalsIgnoreCase("sent")){
														if(clients.getClients() != null) {
															for(FilterPojo client: clients.getClients()) {
																for(String clientEmail: emailPojo.getToClientEmails()) {
																	if(client.getEmailId().equalsIgnoreCase(clientEmail)) {
																		if(client.getExecutive() != null)
																		if(client.getExecutive().equalsIgnoreCase("yes")) {
																			emailPojo.setAdverse("yes");
																		}
																	}
																}
															}
														}
													}
												}
											}else {
												emailPojo.setAnger(0.0);
												count++;
											}
											
											break;
											
										case "joy":

											if (count<2) {
												emailPojo.setJoy(entry.getValue());
												count++;
											}else {
												emailPojo.setJoy(0.0);
												count++;
											}
											break;	
										case "sadness":
											if (count<2) {
												emailPojo.setSadness(entry.getValue());
												count++;
											}else {
												emailPojo.setSadness(0.0);
												count++;
											}
											break;
											
										case "tentative":
											if (count<2) {
												emailPojo.setTentative(entry.getValue());
												count++;
											}else {
												emailPojo.setTentative(0.0);
												count++;
											}
											break;
																				
										case "analytical":
											if (count<2) {
												emailPojo.setAnalytical(entry.getValue());
												count++;
											}else {
												emailPojo.setAnalytical(0.0);
												count++;
											}
											break;
											
										case "confident":
											if (count<2) {
												emailPojo.setConfident(entry.getValue());
												count++;
											}else {
												emailPojo.setConfident(0.0);
												count++;
											}
											break;
											
										case "fear":
											if (count<2) {
												emailPojo.setFear(entry.getValue());
												count++;
											}else {
												emailPojo.setFear(0.0);
												count++;
											}
											break;

										default:
											break;
											
										}
							            
							          
							        }
								  
								  System.out.println(sortedMapDsc);
								  try {
									  if(emailPojo.getToClientEmails().size() > 0) {
										  System.out.println("in getToclients loop");
										  String toClientEmails = emailPojo.getToClientEmails().get(0);
										  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(toClientEmails.split("@")[1]).get(0).getCompanyName());
									  }else if(emailPojo.getFromMail().length() >0 && emailPojo.getFromMail().split("@")[1] != employeeData.getEmailId().split("@")[1] ) {
										  System.out.println("in from mail loop");
										  String domain = emailPojo.getFromMail().split("@")[1];
										  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(domain).get(0).getCompanyName());
									  }else {
										  System.out.println("in else loop");
										  emailPojo.setCompanyName("");
									  }
								  }catch(Exception e) {
									  System.err.println(e);
								  }
								  emailPojos.add(emailPojo);
								
								
							//	list.add(listRcv);
								
							}
							
							}
				
				// emailPojos.addAll(dailyEmployeeEmailToneBO.getLineItems());
			 
			 employeePersonalDataDTO2.setListEmailAnalyser(emailPojos);
							
			 
			return employeePersonalDataDTO2;
		}
		
public  EmployeePersonalDataDTO getOrgEmailsService(EmployeePersonalDataDTO employeePersonalDataDTO) {
			
			UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
					.getContext().getAuthentication();
			
//			EmployeePersonalDataDTO employeePersonalDataDTO = new EmployeePersonalDataDTO();
			
			String emailId = (String) authObj.getUserSessionInformation().get(
					EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
			
			Boolean receive = false;
			Boolean sent  = false;
			
			
			EmployeePersonalDataDTO employeePersonalDataDTO2 = new EmployeePersonalDataDTO();
			

//			EmployeeBO employeeData = employeeRepository.findByEmployeeEmail(employeePersonalDataDTO.getEmployeeId());
			EmployeeBO employeeData = employeeRepository.findByEmailIdIgnoreCase(emailId);
			EmployeeRoleBO employeeRolesPojo = employeeRoleRepository.findByEmployeeIdFKAndStatus(employeeData.getEmployeeId(), "active");
			System.out.println("In org emails service");
//			System.out.println(employeePersonalDataDTO.getClientEmailId());
			System.out.println(employeePersonalDataDTO.getCompanyId());
			String companyDomain = companyRepository.findById(employeePersonalDataDTO.getCompanyId()).getCompanyEmailDomain();
					
			employeePersonalDataDTO2.setNoOfTeamMember(employeeRolesPojo.getTeamSize());
					// employeePersonalDataDTO.setConsolidatedTone(employeeRolesPojo.getConsolidatedTone());
					/* employeePersonalDataDTO.setToneOfTeamMail(employeeRolesPojo.getConsolidatedTone().getToneWithTeam()
							 .getAllMailScore());
					 employeePersonalDataDTO.setToneOfClientMail(employeeRolesPojo.getConsolidatedTone().getToneWithClient()
							 .getAllMailScore());*/
			employeePersonalDataDTO2.setDepartment(employeeRolesPojo.getDepartment());
			employeePersonalDataDTO2.setDesignation(employeeRolesPojo.getDesignation());
			employeePersonalDataDTO2.setNoOfMail(employeeRolesPojo.getConsolidatedTone().getTotalMail());
					 //Relation with team
					 Double joy =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getJoy();
					 Double tnt =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getTentative();
					 Double ana =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getAnalytical();
					 Double cnfdnc =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getConfident();
					 Double anger =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getAnger();
					 Double sad =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getSadness();
					 Double fear =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getFear();
					
					 Double joyness = (double) Math.round(((joy+tnt+ana+cnfdnc)*100)/(joy+tnt+ana+cnfdnc+anger+sad+fear));
					 Double angry =(double) Math.round(((anger+sad+fear)*100)/(joy+tnt+ana+cnfdnc+anger+sad+fear));
					 EmployeeRelationBO employeeRelationBO = new EmployeeRelationBO();
					 employeeRelationBO.setGood(joyness);
					 employeeRelationBO.setBad(angry);
					 employeePersonalDataDTO2.setRelationWithTeam(employeeRelationBO);
					 
					
					 
					 
					 //Relation with Client
					 Double joyClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getJoy();
					 Double anaClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getAnalytical();
					 Double tntClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getTentative();
					 Double confClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getConfident();
					 Double angerClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getAnger();
					 Double sadClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getSadness();
					 Double fearClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getFear();
					 
					
					 Double joynessClnt = (double) Math.round(((joyClnt+anaClnt+tntClnt+confClnt)*100)/(joyClnt+anaClnt+tntClnt+confClnt+angerClnt+sadClnt+fearClnt));
					 Double angryClnt =(double) Math.round(((angerClnt+sadClnt+fearClnt)*100)/(joyClnt+anaClnt+tntClnt+confClnt+angerClnt+sadClnt+fearClnt));
					 EmployeeRelationBO employeeRelationBO1 = new EmployeeRelationBO();
					 employeeRelationBO1.setGood(joynessClnt);
					 employeeRelationBO1.setBad(angryClnt);
					 employeePersonalDataDTO2.setRelationWithClient(employeeRelationBO1);
					 
					
					 
					 
					
				//}
			//}
			
					 employeePersonalDataDTO2.setEmployeeName(employeeData.getEmployeeName());
					 employeePersonalDataDTO2.setEmployeeId(employeeData.getEmployeeId());
					 employeePersonalDataDTO2.setEmailId(employeeData.getEmailId());
			
			if (employeeRolesPojo.getReportToId()==null) {
				
				employeePersonalDataDTO2.setReportTo("None");
				
			}else {
				employeePersonalDataDTO2.setReportTo(employeeRepository.findByEmployeeId(employeeRolesPojo.getReportToId()).getEmployeeName());
			}
			
			List<EmailPojo> emailPojos = new ArrayList<EmailPojo>();
			
			/*Query query = new Query();
			query.limit(10);
			query.skip(0);
			query.with(new Sort(Sort.Direction.ASC, "date"));
			query.with(new Sort(Sort.Direction.DESC, "lineItems.time"));*/
			
			Pageable pageable = new PageRequest(0, 20,new Sort(new Sort.Order(Sort.Direction.ASC,"date"),new Sort.Order(Sort.Direction.DESC,"lineItems.time")));
			
			 
//			 Page<DailyEmployeeEmailToneBO> dailyEmployeeEmailToneBOs = dailyEmployeeEmailToneRepository.findByDateAndEmployeeIdFK("2018-04-17", employeePersonalDataDTO.getEmployeeId(), pageable);
//			
//			 List<DailyEmployeeEmailToneBO> dailyEmployeeEmailToneBOs1 = dailyEmployeeEmailToneBOs.getContent();
				 

					if (employeePersonalDataDTO.getIdentify().equalsIgnoreCase("all")) {
						System.out.println("all");
						
//					for(DailyEmployeeEmailToneBO dailyEmployeeEmailToneBO : dailyEmployeeEmailToneBOs1) {
//						
//						// get each email and tone of each employee
//						List<EmailPojo> lineItems = dailyEmployeeEmailToneBO.getLineItems();
//						
//
//						for (EmailPojo emailPojo : lineItems) {
//							try {
//								emailPojo.setFrom(employeeRepository.findByEmailIdIgnoreCase(emailPojo.getFromMail()).getEmployeeName());
//							}catch(Exception e) {
//								emailPojo.setFrom("");
//							}
//							Map<String,Double> hashMap = new HashMap<String,Double>(); 
//							hashMap.put("anger", emailPojo.getAnger());
//							hashMap.put("joy", emailPojo.getJoy());
//							hashMap.put("sadness", emailPojo.getSadness());
//							hashMap.put("tentative", emailPojo.getTentative());
//							hashMap.put("analytical", emailPojo.getAnalytical());
//							hashMap.put("confident", emailPojo.getConfident());
//							hashMap.put("fear", emailPojo.getFear());
//							
//							  Map<String, Double> sortedMapDsc =	sortByComparator(hashMap, false);
//							  int count =0;
//							  for (Entry<String, Double> entry : sortedMapDsc.entrySet())
//						        {
//						            System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
//						            
//						            
//						         
//						            switch (entry.getKey()) {
//						            
//									case "anger":
//										if (count<2) {
//											emailPojo.setAnger(entry.getValue());
//											count++;
//										}else {
//											emailPojo.setAnger(0.0);
//											count++;
//										}
//										
//										break;
//										
//									case "joy":
//
//										if (count<2) {
//											emailPojo.setJoy(entry.getValue());
//											count++;
//										}else {
//											emailPojo.setJoy(0.0);
//											count++;
//										}
//										break;	
//									case "sadness":
//										if (count<2) {
//											emailPojo.setSadness(entry.getValue());
//											count++;
//										}else {
//											emailPojo.setSadness(0.0);
//											count++;
//										}
//										break;
//										
//									case "tentative":
//										if (count<2) {
//											emailPojo.setTentative(entry.getValue());
//											count++;
//										}else {
//											emailPojo.setTentative(0.0);
//											count++;
//										}
//										break;
//																			
//									case "analytical":
//										if (count<2) {
//											emailPojo.setAnalytical(entry.getValue());
//											count++;
//										}else {
//											emailPojo.setAnalytical(0.0);
//											count++;
//										}
//										break;
//										
//									case "confident":
//										if (count<2) {
//											emailPojo.setConfident(entry.getValue());
//											count++;
//										}else {
//											emailPojo.setConfident(0.0);
//											count++;
//										}
//										break;
//										
//									case "fear":
//										if (count<2) {
//											emailPojo.setFear(entry.getValue());
//											count++;
//										}else {
//											emailPojo.setFear(0.0);
//											count++;
//										}
//										break;
//
//									default:
//										break;
//										
//									}
//						            
//						          
//						        }
//							  
//							  System.out.println(sortedMapDsc);
//
//							/*Stream<Map.Entry<String,Double>> sorted =
//									hashMap.entrySet().stream()
//								       .sorted(Entry.comparingByValue());
//							
//							System.out.println(sorted.toString());
//							Map<String, Double> map = sorted.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//							System.out.println(map);*/
////							  emailPojo.setFromMail();
//							  
//							  try {
//								  if(emailPojo.getToClientEmails().size() > 0) {
//									  System.out.println("in getToclients loop");
//									  String toClientEmails = emailPojo.getToClientEmails().get(0);
//									  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(toClientEmails.split("@")[1]).get(0).getCompanyName());
//								  }else if(emailPojo.getFromMail().length() >0 && emailPojo.getFromMail().split("@")[1] != employeeData.getEmailId().split("@")[1] ) {
//									  System.out.println("in from mail loop");
//									  String domain = emailPojo.getFromMail().split("@")[1];
//									  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(domain).get(0).getCompanyName());
//								  }else {
//									  System.out.println("in else loop");
//									  emailPojo.setCompanyName("");
//								  }
//							  }catch(Exception e) {
//								  System.err.println(e);
//							  }
//							  
//							  emailPojos.add(emailPojo);
//						}
//						
//						//list.addAll(lineItems);
//						
//					}
					
						receive = true;
						sent = true;
						
					}
					
					
					//for all received mail

					if (employeePersonalDataDTO.getIdentify().equalsIgnoreCase("receive")){
						receive = true;
					}
					
					if (employeePersonalDataDTO.getIdentify().equalsIgnoreCase("sent")) {
						sent = true;
					}
						
					
					if(receive) {
						System.out.println("receive");
					Aggregation aggregation2 = Aggregation.newAggregation(
							
							Aggregation.match(Criteria.where("employeeIdFK").is(employeeData.getEmployeeId())),
							Aggregation.unwind("lineItems"),
							Aggregation.match(Criteria.where("lineItems.type").is("Received")),
							Aggregation.match(Criteria.where("lineItems.fromMail").regex(companyDomain))
//							Aggregation.unwind("clientEmailItems")
							);
							
					List<DailyEmployeeTeamUnwindLinePojo> results = null;		
							
					try {
					 results = mongoTemplate.aggregate(aggregation2, DailyEmployeeEmailToneBO.class, DailyEmployeeTeamUnwindLinePojo.class).getMappedResults();
					}catch(Exception e) {
						
					}
					for(DailyEmployeeTeamUnwindLinePojo basicDBObject : results) {
						
						EmailPojo emailPojo = basicDBObject.getLineItems();
						try {
							emailPojo.setFrom(employeeRepository.findByEmailIdIgnoreCase(emailPojo.getFromMail()).getEmployeeName());
						}catch(Exception e) {
							emailPojo.setFrom("");
						}
						System.out.println(emailPojo.getSubject());
						Map<String,Double> hashMap = new HashMap<String,Double>(); 
						hashMap.put("anger", emailPojo.getAnger());
						hashMap.put("joy", emailPojo.getJoy());
						hashMap.put("sadness", emailPojo.getSadness());
						hashMap.put("tentative", emailPojo.getTentative());
						hashMap.put("analytical", emailPojo.getAnalytical());
						hashMap.put("confident", emailPojo.getConfident());
						hashMap.put("fear", emailPojo.getFear());
						
						  Map<String, Double> sortedMapDsc =	sortByComparator(hashMap, false);
						  int count =0;
						  for (Entry<String, Double> entry : sortedMapDsc.entrySet())
					        {
//					            System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
					            
					            
					         
					            switch (entry.getKey()) {
					            
								case "anger":
									if (count<2) {
										emailPojo.setAnger(entry.getValue());
										count++;
//										UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
//												.getContext().getAuthentication();
//										
////										EmployeePersonalDataDTO employeePersonalDataDTO1 = new EmployeePersonalDataDTO();
//										
//										String emailId = (String) authObj.getUserSessionInformation().get(
//												EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
										
										EmployeeBO employeeData1 =employeeRepository.findOne(emailId);
										
										ClientBO clients= clientRepository.findByEmployeeIdFK(employeeData1.getEmployeeId());
										if(clients != null) {
											if(emailPojo.getType().equalsIgnoreCase("Received")) {
												
												if(clients.getClients() != null) {
													for(FilterPojo client: clients.getClients()) {
														if(client.getEmailId().equalsIgnoreCase(emailPojo.getFromMail())) {
															if(client.getExecutive() != null)
															if(client.getExecutive().equalsIgnoreCase("yes")) {
																emailPojo.setAdverse("yes");
															}
														}
													}
												}
												
											}else if(emailPojo.getType().equalsIgnoreCase("sent")){
												if(clients.getClients() != null) {
													for(FilterPojo client: clients.getClients()) {
														for(String clientEmail: emailPojo.getToClientEmails()) {
															if(client.getEmailId().equalsIgnoreCase(clientEmail)) {
																if(client.getExecutive() != null)
																if(client.getExecutive().equalsIgnoreCase("yes")) {
																	emailPojo.setAdverse("yes");
																}
															}
														}
													}
												}
											}
										}
									}else {
										emailPojo.setAnger(0.0);
										count++;
									}
									
									break;
									
								case "joy":

									if (count<2) {
										emailPojo.setJoy(entry.getValue());
										count++;
									}else {
										emailPojo.setJoy(0.0);
										count++;
									}
									break;	
								case "sadness":
									if (count<2) {
										emailPojo.setSadness(entry.getValue());
										count++;
									}else {
										emailPojo.setSadness(0.0);
										count++;
									}
									break;
									
								case "tentative":
									if (count<2) {
										emailPojo.setTentative(entry.getValue());
										count++;
									}else {
										emailPojo.setTentative(0.0);
										count++;
									}
									break;
																		
								case "analytical":
									if (count<2) {
										emailPojo.setAnalytical(entry.getValue());
										count++;
									}else {
										emailPojo.setAnalytical(0.0);
										count++;
									}
									break;
									
								case "confident":
									if (count<2) {
										emailPojo.setConfident(entry.getValue());
										count++;
									}else {
										emailPojo.setConfident(0.0);
										count++;
									}
									break;
									
								case "fear":
									if (count<2) {
										emailPojo.setFear(entry.getValue());
										count++;
									}else {
										emailPojo.setFear(0.0);
										count++;
									}
									break;

								default:
									break;
									
								}
					            
					          
					        }
						  
						  System.out.println(sortedMapDsc);
						  
						  try {
							  if(emailPojo.getToClientEmails().size() > 0) {
								  System.out.println("in getToclients loop");
								  String toClientEmails = emailPojo.getToClientEmails().get(0);
								  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(toClientEmails.split("@")[1]).get(0).getCompanyName());
							  }else if(emailPojo.getFromMail().length() >0 && emailPojo.getFromMail().split("@")[1] != employeeData.getEmailId().split("@")[1] ) {
								  System.out.println("in from mail loop");
								  String domain = emailPojo.getFromMail().split("@")[1];
								  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(domain).get(0).getCompanyName());
							  }else {
								  System.out.println("in else loop");
								  emailPojo.setCompanyName("");
							  }
						  }catch(Exception e) {
							  System.err.println(e);
						  }

						  emailPojos.add(emailPojo);
					
						
						
						//list.add(listRcv);
						
					}
					// client items rcv
				
					Aggregation aggregation3 = Aggregation.newAggregation(
							
							Aggregation.match(Criteria.where("employeeIdFK").is(employeeData.getEmployeeId())),
							Aggregation.unwind("clientEmailItems"),
							Aggregation.match(Criteria.where("clientEmailItems.type").is("Received")),
							Aggregation.match(Criteria.where("clientEmailItems.fromMail").regex(companyDomain))
//							Aggregation.unwind("lineItems")
							);
							
					List<DailyEmployeeTeamUnwindClientPojo> results1 = null;		
							
					try {
					results1 = mongoTemplate.aggregate(aggregation3, DailyEmployeeEmailToneBO.class, DailyEmployeeTeamUnwindClientPojo.class).getMappedResults();
					}catch(Exception e) {
						
					}
					for(DailyEmployeeTeamUnwindClientPojo basicDBObject : results1) {
						
						EmailPojo emailPojo = basicDBObject.getClientEmailItems();
						try {
							emailPojo.setFrom(employeeRepository.findByEmailIdIgnoreCase(emailPojo.getFromMail()).getEmployeeName());
						}catch(Exception e) {
							emailPojo.setFrom("");
						}
						System.out.println(emailPojo.getSubject());
						Map<String,Double> hashMap = new HashMap<String,Double>(); 
						hashMap.put("anger", emailPojo.getAnger());
						hashMap.put("joy", emailPojo.getJoy());
						hashMap.put("sadness", emailPojo.getSadness());
						hashMap.put("tentative", emailPojo.getTentative());
						hashMap.put("analytical", emailPojo.getAnalytical());
						hashMap.put("confident", emailPojo.getConfident());
						hashMap.put("fear", emailPojo.getFear());
						
						  Map<String, Double> sortedMapDsc =	sortByComparator(hashMap, false);
						  int count =0;
						  for (Entry<String, Double> entry : sortedMapDsc.entrySet())
					        {
					            System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
					            
					            
					         
					            switch (entry.getKey()) {
					            
								case "anger":
									if (count<2) {
										emailPojo.setAnger(entry.getValue());
										count++;
//										UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
//												.getContext().getAuthentication();
//										
////										EmployeePersonalDataDTO employeePersonalDataDTO1 = new EmployeePersonalDataDTO();
//										
//										String emailId = (String) authObj.getUserSessionInformation().get(
//												EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
										
										EmployeeBO employeeData1 =employeeRepository.findOne(emailId);
										
										ClientBO clients= clientRepository.findByEmployeeIdFK(employeeData1.getEmployeeId());
										if(clients != null) {
											if(emailPojo.getType().equalsIgnoreCase("Received")) {
												
												if(clients.getClients() != null) {
													for(FilterPojo client: clients.getClients()) {
														if(client.getEmailId().equalsIgnoreCase(emailPojo.getFromMail())) {
															if(client.getExecutive() != null)
															if(client.getExecutive().equalsIgnoreCase("yes")) {
																emailPojo.setAdverse("yes");
															}
														}
													}
												}
												
											}else if(emailPojo.getType().equalsIgnoreCase("sent")){
												if(clients.getClients() != null) {
													for(FilterPojo client: clients.getClients()) {
														for(String clientEmail: emailPojo.getToClientEmails()) {
															if(client.getEmailId().equalsIgnoreCase(clientEmail)) {
																if(client.getExecutive() != null)
																if(client.getExecutive().equalsIgnoreCase("yes")) {
																	emailPojo.setAdverse("yes");
																}
															}
														}
													}
												}
											}
										}
									}else {
										emailPojo.setAnger(0.0);
										count++;
									}
									
									break;
									
								case "joy":

									if (count<2) {
										emailPojo.setJoy(entry.getValue());
										count++;
									}else {
										emailPojo.setJoy(0.0);
										count++;
									}
									break;	
								case "sadness":
									if (count<2) {
										emailPojo.setSadness(entry.getValue());
										count++;
									}else {
										emailPojo.setSadness(0.0);
										count++;
									}
									break;
									
								case "tentative":
									if (count<2) {
										emailPojo.setTentative(entry.getValue());
										count++;
									}else {
										emailPojo.setTentative(0.0);
										count++;
									}
									break;
																		
								case "analytical":
									if (count<2) {
										emailPojo.setAnalytical(entry.getValue());
										count++;
									}else {
										emailPojo.setAnalytical(0.0);
										count++;
									}
									break;
									
								case "confident":
									if (count<2) {
										emailPojo.setConfident(entry.getValue());
										count++;
									}else {
										emailPojo.setConfident(0.0);
										count++;
									}
									break;
									
								case "fear":
									if (count<2) {
										emailPojo.setFear(entry.getValue());
										count++;
									}else {
										emailPojo.setFear(0.0);
										count++;
									}
									break;

								default:
									break;
									
								}
					            
					          
					        }
						  
						  System.out.println(sortedMapDsc);
						  
						  try {
							  if(emailPojo.getToClientEmails().size() > 0) {
								  System.out.println("in getToclients loop");
								  String toClientEmails = emailPojo.getToClientEmails().get(0);
								  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(toClientEmails.split("@")[1]).get(0).getCompanyName());
							  }else if(emailPojo.getFromMail().length() >0 && emailPojo.getFromMail().split("@")[1] != employeeData.getEmailId().split("@")[1] ) {
								  System.out.println("in from mail loop");
								  String domain = emailPojo.getFromMail().split("@")[1];
								  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(domain).get(0).getCompanyName());
							  }else {
								  System.out.println("in else loop");
								  emailPojo.setCompanyName("");
							  }
						  }catch(Exception e) {
							  System.err.println(e);
						  }

						  emailPojos.add(emailPojo);
					
						
						
						//list.add(listRcv);
						
					}
					
					}
					
					//for all sent mail
							
							if (sent) {
								System.out.println("sent");
							
							
							Aggregation aggregation2 = Aggregation.newAggregation(
									
									Aggregation.match(Criteria.where("employeeIdFK").is(employeeData.getEmployeeId())),
									Aggregation.unwind("lineItems"),
									Aggregation.match(Criteria.where("lineItems.type").is("sent")),
									Aggregation.match(Criteria.where("lineItems.toClientEmails").regex(companyDomain))
//									Aggregation.unwind("clientEmailItems")
									);
									
									
									
							
							List<DailyEmployeeTeamUnwindLinePojo> results = mongoTemplate.aggregate(aggregation2, DailyEmployeeEmailToneBO.class, DailyEmployeeTeamUnwindLinePojo.class).getMappedResults();
							
							for(DailyEmployeeTeamUnwindLinePojo basicDBObject : results) {
								
								EmailPojo emailPojo = basicDBObject.getLineItems();
								try {
									emailPojo.setFrom(employeeRepository.findByEmailIdIgnoreCase(emailPojo.getFromMail()).getEmployeeName());
								}catch(Exception e) {
									emailPojo.setFrom("");
								}
								System.out.println(emailPojo.getSubject());
								Map<String,Double> hashMap = new HashMap<String,Double>(); 
								hashMap.put("anger", emailPojo.getAnger());
								hashMap.put("joy", emailPojo.getJoy());
								hashMap.put("sadness", emailPojo.getSadness());
								hashMap.put("tentative", emailPojo.getTentative());
								hashMap.put("analytical", emailPojo.getAnalytical());
								hashMap.put("confident", emailPojo.getConfident());
								hashMap.put("fear", emailPojo.getFear());
								
								  Map<String, Double> sortedMapDsc =	sortByComparator(hashMap, false);
								  int count =0;
								  for (Entry<String, Double> entry : sortedMapDsc.entrySet())
							        {
							            System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
							            
							            
							         
							            switch (entry.getKey()) {
							            
										case "anger":
											if (count<2) {
												emailPojo.setAnger(entry.getValue());
												count++;
//												UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
//														.getContext().getAuthentication();
//												
////												EmployeePersonalDataDTO employeePersonalDataDTO1 = new EmployeePersonalDataDTO();
//												
//												String emailId = (String) authObj.getUserSessionInformation().get(
//														EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
												
												EmployeeBO employeeData1 =employeeRepository.findOne(emailId);
												
												ClientBO clients= clientRepository.findByEmployeeIdFK(employeeData1.getEmployeeId());
												if(clients != null) {
													if(emailPojo.getType().equalsIgnoreCase("Received")) {
														
														if(clients.getClients() != null) {
															for(FilterPojo client: clients.getClients()) {
																if(client.getEmailId().equalsIgnoreCase(emailPojo.getFromMail())) {
																	if(client.getExecutive() != null)
																	if(client.getExecutive().equalsIgnoreCase("yes")) {
																		emailPojo.setAdverse("yes");
																	}
																}
															}
														}
														
													}else if(emailPojo.getType().equalsIgnoreCase("sent")){
														if(clients.getClients() != null) {
															for(FilterPojo client: clients.getClients()) {
																for(String clientEmail: emailPojo.getToClientEmails()) {
																	if(client.getEmailId().equalsIgnoreCase(clientEmail)) {
																		if(client.getExecutive() != null)
																		if(client.getExecutive().equalsIgnoreCase("yes")) {
																			emailPojo.setAdverse("yes");
																		}
																	}
																}
															}
														}
													}
												}
											}else {
												emailPojo.setAnger(0.0);
												count++;
											}
											
											break;
											
										case "joy":

											if (count<2) {
												emailPojo.setJoy(entry.getValue());
												count++;
											}else {
												emailPojo.setJoy(0.0);
												count++;
											}
											break;	
										case "sadness":
											if (count<2) {
												emailPojo.setSadness(entry.getValue());
												count++;
											}else {
												emailPojo.setSadness(0.0);
												count++;
											}
											break;
											
										case "tentative":
											if (count<2) {
												emailPojo.setTentative(entry.getValue());
												count++;
											}else {
												emailPojo.setTentative(0.0);
												count++;
											}
											break;
																				
										case "analytical":
											if (count<2) {
												emailPojo.setAnalytical(entry.getValue());
												count++;
											}else {
												emailPojo.setAnalytical(0.0);
												count++;
											}
											break;
											
										case "confident":
											if (count<2) {
												emailPojo.setConfident(entry.getValue());
												count++;
											}else {
												emailPojo.setConfident(0.0);
												count++;
											}
											break;
											
										case "fear":
											if (count<2) {
												emailPojo.setFear(entry.getValue());
												count++;
											}else {
												emailPojo.setFear(0.0);
												count++;
											}
											break;

										default:
											break;
											
										}
							            
							          
							        }
								  
								  System.out.println(sortedMapDsc);
								  try {
									  if(emailPojo.getToClientEmails().size() > 0) {
										  System.out.println("in getToclients loop");
										  String toClientEmails = emailPojo.getToClientEmails().get(0);
										  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(toClientEmails.split("@")[1]).get(0).getCompanyName());
									  }else if(emailPojo.getFromMail().length() >0 && emailPojo.getFromMail().split("@")[1] != employeeData.getEmailId().split("@")[1] ) {
										  System.out.println("in from mail loop");
										  String domain = emailPojo.getFromMail().split("@")[1];
										  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(domain).get(0).getCompanyName());
									  }else {
										  System.out.println("in else loop");
										  emailPojo.setCompanyName("");
									  }
								  }catch(Exception e) {
									  System.err.println(e);
								  }
								  emailPojos.add(emailPojo);
								
								
							//	list.add(listRcv);
								
							}
							
							Aggregation aggregation3 = Aggregation.newAggregation(
									
									Aggregation.match(Criteria.where("employeeIdFK").is(employeeData.getEmployeeId())),
									Aggregation.unwind("clientEmailItems"),
									Aggregation.match(Criteria.where("clientEmailItems.type").is("sent")),
									Aggregation.match(Criteria.where("clientEmailItems.toClientEmails").regex(companyDomain))
//									Aggregation.unwind("lineItems")
									);
									
									
									
							
							List<DailyEmployeeTeamUnwindClientPojo> results1 = mongoTemplate.aggregate(aggregation3, DailyEmployeeEmailToneBO.class, DailyEmployeeTeamUnwindClientPojo.class).getMappedResults();
							
							for(DailyEmployeeTeamUnwindClientPojo basicDBObject : results1) {
								
								EmailPojo emailPojo = basicDBObject.getClientEmailItems();
								
								try {
									emailPojo.setFrom(employeeRepository.findByEmailIdIgnoreCase(emailPojo.getFromMail()).getEmployeeName());
								}catch(Exception e) {
									emailPojo.setFrom("");
								}
								System.out.println(emailPojo.getSubject());
								Map<String,Double> hashMap = new HashMap<String,Double>(); 
								hashMap.put("anger", emailPojo.getAnger());
								hashMap.put("joy", emailPojo.getJoy());
								hashMap.put("sadness", emailPojo.getSadness());
								hashMap.put("tentative", emailPojo.getTentative());
								hashMap.put("analytical", emailPojo.getAnalytical());
								hashMap.put("confident", emailPojo.getConfident());
								hashMap.put("fear", emailPojo.getFear());
								
								  Map<String, Double> sortedMapDsc =	sortByComparator(hashMap, false);
								  int count =0;
								  for (Entry<String, Double> entry : sortedMapDsc.entrySet())
							        {
							            System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
							            
							            
							         
							            switch (entry.getKey()) {
							            
										case "anger":
											if (count<2) {
												emailPojo.setAnger(entry.getValue());
												count++;
//												UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
//														.getContext().getAuthentication();
//												
////												EmployeePersonalDataDTO employeePersonalDataDTO1 = new EmployeePersonalDataDTO();
//												
//												String emailId = (String) authObj.getUserSessionInformation().get(
//														EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
												
												EmployeeBO employeeData1 =employeeRepository.findOne(emailId);
												
												ClientBO clients= clientRepository.findByEmployeeIdFK(employeeData1.getEmployeeId());
												if(clients != null) {
													if(emailPojo.getType().equalsIgnoreCase("Received")) {
														
														if(clients.getClients() != null) {
															for(FilterPojo client: clients.getClients()) {
																if(client.getEmailId().equalsIgnoreCase(emailPojo.getFromMail())) {
																	if(client.getExecutive() != null)
																	if(client.getExecutive().equalsIgnoreCase("yes")) {
																		emailPojo.setAdverse("yes");
																	}
																}
															}
														}
														
													}else if(emailPojo.getType().equalsIgnoreCase("sent")){
														if(clients.getClients() != null) {
															for(FilterPojo client: clients.getClients()) {
																for(String clientEmail: emailPojo.getToClientEmails()) {
																	if(client.getEmailId().equalsIgnoreCase(clientEmail)) {
																		if(client.getExecutive() != null)
																		if(client.getExecutive().equalsIgnoreCase("yes")) {
																			emailPojo.setAdverse("yes");
																		}
																	}
																}
															}
														}
													}
												}
											}else {
												emailPojo.setAnger(0.0);
												count++;
											}
											
											break;
											
										case "joy":

											if (count<2) {
												emailPojo.setJoy(entry.getValue());
												count++;
											}else {
												emailPojo.setJoy(0.0);
												count++;
											}
											break;	
										case "sadness":
											if (count<2) {
												emailPojo.setSadness(entry.getValue());
												count++;
											}else {
												emailPojo.setSadness(0.0);
												count++;
											}
											break;
											
										case "tentative":
											if (count<2) {
												emailPojo.setTentative(entry.getValue());
												count++;
											}else {
												emailPojo.setTentative(0.0);
												count++;
											}
											break;
																				
										case "analytical":
											if (count<2) {
												emailPojo.setAnalytical(entry.getValue());
												count++;
											}else {
												emailPojo.setAnalytical(0.0);
												count++;
											}
											break;
											
										case "confident":
											if (count<2) {
												emailPojo.setConfident(entry.getValue());
												count++;
											}else {
												emailPojo.setConfident(0.0);
												count++;
											}
											break;
											
										case "fear":
											if (count<2) {
												emailPojo.setFear(entry.getValue());
												count++;
											}else {
												emailPojo.setFear(0.0);
												count++;
											}
											break;

										default:
											break;
											
										}
							            
							          
							        }
								  
								  System.out.println(sortedMapDsc);
								  try {
									  if(emailPojo.getToClientEmails().size() > 0) {
										  System.out.println("in getToclients loop");
										  String toClientEmails = emailPojo.getToClientEmails().get(0);
										  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(toClientEmails.split("@")[1]).get(0).getCompanyName());
									  }else if(emailPojo.getFromMail().length() >0 && emailPojo.getFromMail().split("@")[1] != employeeData.getEmailId().split("@")[1] ) {
										  System.out.println("in from mail loop");
										  String domain = emailPojo.getFromMail().split("@")[1];
										  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(domain).get(0).getCompanyName());
									  }else {
										  System.out.println("in else loop");
										  emailPojo.setCompanyName("");
									  }
								  }catch(Exception e) {
									  System.err.println(e);
								  }
								  emailPojos.add(emailPojo);
								
								
							//	list.add(listRcv);
								
							}
							
							}
				
				// emailPojos.addAll(dailyEmployeeEmailToneBO.getLineItems());
			 
			 employeePersonalDataDTO2.setListEmailAnalyser(emailPojos);
							
			 
			return employeePersonalDataDTO2;
		}
		
		
		
		
		
		
		//find client or Vendor company list
		
		public List<OrganisationBO> getListOfCompany() {
			
			
			List<OrganisationBO> listOfCompany=null;
			
			OrganisationBO clientBO = new OrganisationBO();
			
			listOfCompany = companyRepository.findAll();
			
			
			return listOfCompany;
		}
		
		
	
		
		
		
	// search on employee multiple  designation
		
		public EmployeePersonalDataDTO filterOnEmployeeDesignationCriteria(ClientDataDTO clientDataDTO) {
			
			EmployeePersonalDataDTO employeePersonalDataDTO1 = new EmployeePersonalDataDTO();
		    
			List<EmployeePersonalDataDTO> employeePersonalDataDTOs = new ArrayList<EmployeePersonalDataDTO>();
		    UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
					.getContext().getAuthentication();
			
			String emailId = (String) authObj.getUserSessionInformation().get(
					EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
			
			EmployeeBO employeeBO = employeeRepository.findOne(emailId);
			
			List<FilterByCriteria> filterByCriterias = new ArrayList<FilterByCriteria>();

			ProjectionOperation projectionOperation = Aggregation.project()
					.andExpression("employeeHierarchy").as("filterName")
					.andExclude("_id");

			Aggregation aggregation = Aggregation.newAggregation(
					
					
					
					Aggregation.match(Criteria.where("employeeIdFK").is(employeeBO.getEmployeeId()).and("status").is("active")),
					Aggregation.unwind("$employeeHierarchy"),
					Aggregation.match(Criteria.where("employeeHierarchy.designation").in(clientDataDTO.getSearchCriteria())),
					
					//Aggregation.project("employeeHierarchy")
					projectionOperation
					
					
					);
			
			List<FilterResultPojo> results = mongoTemplate.aggregate(aggregation, EmployeeRoleBO.class, FilterResultPojo.class).getMappedResults();
			
			for(FilterResultPojo dbObject : results) {
				
				FilterByCriteria filterByCriteria = new FilterByCriteria();
				filterByCriteria.setEmployeeName(dbObject.getFilterName().getEmployeeName());
				filterByCriteria.setEmployeeId(dbObject.getFilterName().getEmployeeId());
				filterByCriteria.setDesignation(dbObject.getFilterName().getDesignation());
				filterByCriterias.add(filterByCriteria);
				EmployeePersonalDataDTO employeePersonalDataDTO = new EmployeePersonalDataDTO();
				
				employeePersonalDataDTO.setEmployeeName(dbObject.getFilterName().getEmployeeName());
				employeePersonalDataDTO.setEmployeeId(dbObject.getFilterName().getEmployeeId());
				employeePersonalDataDTO.setDesignation(dbObject.getFilterName().getDesignation());
				
				Aggregation	 aggregation1 = Aggregation.newAggregation(
		                
						Aggregation.match(Criteria.where("employeeIdFK").is(dbObject.getFilterName().getEmployeeId()).and("date").gte("2018-04-16")),
						Aggregation.group("employeeIdFK").last("employeeIdFK").as("employeeId")
						.sum("teamTone.allMailScore.anger").as("angerTeamTotal").sum("teamTone.allMailScore.angerCount")
						.as("angerTeamCount").sum("teamTone.allMailScore.joy").as("joyTeamTotal").sum("teamTone.allMailScore.joyCount")
						.as("joyTeamCount").sum("teamTone.allMailScore.sadness").as("sadnessTeamTotal").sum("teamTone.allMailScore.sadnessCount")
						.as("sadnessTeamCount").sum("teamTone.allMailScore.tentative").as("tentativeTeamTotal").sum("teamTone.allMailScore.tentativeCount")
						.as("tentativeTeamCount").sum("teamTone.allMailScore.analytical").as("analyticalTeamTotal").sum("teamTone.allMailScore.analyticalCount")
						.as("analyticalTeamCount").sum("teamTone.allMailScore.confident").as("confidentTeamTotal").sum("teamTone.allMailScore.confidentCount")
						.as("confidentTeamCount").sum("teamTone.allMailScore.fear").as("fearTeamTotal").sum("teamTone.allMailScore.fearCount")
						.as("fearTeamCount")
						.sum("teamTone.receiveMailScore.anger").as("angerTeamTotalrcv").sum("teamTone.receiveMailScore.angerCount")
						.as("angerTeamCountrcv").sum("teamTone.receiveMailScore.joy").as("joyTeamTotalrcv").sum("teamTone.receiveMailScore.joyCount")
						.as("joyTeamCountrcv").sum("teamTone.receiveMailScore.sadness").as("sadnessTeamTotalrcv").sum("teamTone.receiveMailScore.sadnessCount")
						.as("sadnessTeamCountrcv").sum("teamTone.receiveMailScore.tentative").as("tentativeTeamTotalrcv").sum("teamTone.receiveMailScore.tentativeCount")
						.as("tentativeTeamCountrcv").sum("teamTone.receiveMailScore.analytical").as("analyticalTeamTotalrcv").sum("teamTone.receiveMailScore.analyticalCount")
						.as("analyticalTeamCountrcv").sum("teamTone.receiveMailScore.confident").as("confidentTeamTotalrcv").sum("teamTone.receiveMailScore.confidentCount")
						.as("confidentTeamCountrcv").sum("teamTone.receiveMailScore.fear").as("fearTeamTotalrcv").sum("teamTone.receiveMailScore.fearCount")
						.as("fearTeamCountrcv")
						.sum("teamTone.sentMailScore.anger").as("angerTeamTotalsnt").sum("teamTone.sentMailScore.angerCount")
						.as("angerTeamCountsnt").sum("teamTone.sentMailScore.joy").as("joyTeamTotalsnt").sum("teamTone.sentMailScore.joyCount")
						.as("joyTeamCountsnt").sum("teamTone.sentMailScore.sadness").as("sadnessTeamTotalsnt").sum("teamTone.sentMailScore.sadnessCount")
						.as("sadnessTeamCountsnt").sum("teamTone.sentMailScore.tentative").as("tentativeTeamTotalsnt").sum("teamTone.sentMailScore.tentativeCount")
						.as("tentativeTeamCountsnt").sum("teamTone.sentMailScore.analytical").as("analyticalTeamTotalsnt").sum("teamTone.sentMailScore.analyticalCount")
						.as("analyticalTeamCountsnt").sum("teamTone.sentMailScore.confident").as("confidentTeamTotalsnt").sum("teamTone.sentMailScore.confidentCount")
						.as("confidentTeamCountsnt").sum("teamTone.sentMailScore.fear").as("fearTeamTotalsnt").sum("teamTone.sentMailScore.fearCount")
						.as("fearTeamCountsnt")
						.sum("teamTone.totalMail").as("totalTeamMail")
						.sum("teamTone.totalMailRecevied").as("totalTeamMailRecevied")
						.sum("teamTone.totalMailSent").as("totalTeamMailSent")
						
						
							);
			
		
			
			AggregationResults<AggregationPojo> groupResults 
			= mongoTemplate.aggregate(aggregation1, DailyEmployeeEmailToneBO.class, AggregationPojo.class);
			List<AggregationPojo> result = groupResults.getMappedResults();
			
			
			
			
			// Average of All Self Scores 
			
						Double angerFinal = 0.0d;
						Double joyFinal = 0.0d;
						Double sadnessFinal = 0.0d;
						Double tentativeFinal = 0.0d;
						Double analyticalFinal = 0.0d;
						Double confidentFinal = 0.0d;
						Double fearFinal = 0.0d;
			
						if(result.get(0).getAngerTeamCount() != 0)
						angerFinal = result.get(0).getAngerTeamTotal()/result.get(0).getAngerTeamCount();
						if(result.get(0).getJoyTeamCount() != 0)
						joyFinal = result.get(0).getJoyTeamTotal()/result.get(0).getJoyTeamCount();
						if(result.get(0).getSadnessTeamCount() != 0)
						sadnessFinal = result.get(0).getSadnessTeamTotal()/result.get(0).getSadnessTeamCount();
						if(result.get(0).getTentativeTeamCount() != 0)
						tentativeFinal = result.get(0).getTentativeTeamTotal()/result.get(0).getTentativeTeamCount();
						if(result.get(0).getAnalyticalTeamCount() != 0)
						analyticalFinal = result.get(0).getAnalyticalTeamTotal()/result.get(0).getAnalyticalTeamCount();
						if(result.get(0).getConfidentTeamCount() != 0)
						confidentFinal = result.get(0).getConfidentTeamTotal()/result.get(0).getConfidentTeamCount();
						if(result.get(0).getFearTeamCount() != 0)
						fearFinal =result.get(0).getFearTeamTotal()/result.get(0).getFearTeamCount();
						
						ToneOfMail toneOfMail = new ToneOfMail();
						
						Double total = angerFinal+joyFinal+sadnessFinal+tentativeFinal+analyticalFinal+confidentFinal+fearFinal;
						Double angerP = (double)Math.round(((angerFinal)*100)/(total));
						Double joyP = (double)Math.round(((joyFinal)*100)/(total));
						Double sadnP = (double)Math.round(((sadnessFinal)*100)/(total));
						Double tenP = (double)Math.round(((tentativeFinal)*100)/(total));
						Double analP = (double)Math.round(((analyticalFinal)*100)/(total));
						Double confP = (double)Math.round(((confidentFinal)*100)/(total));
						Double fearP =  (double)Math.round(((fearFinal)*100)/(total));
						
						toneOfMail.setAnger(angerP);
						toneOfMail.setJoy(joyP);
						toneOfMail.setSadness(sadnP);
						toneOfMail.setTentative(tenP);
						toneOfMail.setAnalytical(analP);
						toneOfMail.setConfident(confP);
						toneOfMail.setFear(fearP);
						/*
						toneOfMail.setAnger(angerFinal);
						toneOfMail.setJoy(joyFinal);
						toneOfMail.setSadness(sadnessFinal);
						toneOfMail.setTentative(tentativeFinal);
						toneOfMail.setAnalytical(analyticalFinal);
						toneOfMail.setConfident(confidentFinal);
						toneOfMail.setFear(fearFinal);*/
						
						//rcv
						
						Double angerFinal1 = 0.0d;
						Double joyFinal1 = 0.0d;
						Double sadnessFinal1 = 0.0d;
						Double tentativeFinal1 = 0.0d;
						Double analyticalFinal1 = 0.0d;
						Double confidentFinal1 = 0.0d;
						Double fearFinal1 = 0.0d;
						
						if(result.get(0).getAngerTeamCountrcv() != 0)
						angerFinal1 = result.get(0).getAngerTeamTotalrcv()/result.get(0).getAngerTeamCountrcv();
						if(result.get(0).getJoyTeamCountrcv() != 0)
						joyFinal1 = result.get(0).getJoyTeamTotalrcv()/result.get(0).getJoyTeamCountrcv();
						if(result.get(0).getSadnessTeamCountrcv() != 0)
						sadnessFinal1 = result.get(0).getSadnessTeamTotalrcv()/result.get(0).getSadnessTeamCountrcv();
						if(result.get(0).getTentativeTeamCountrcv() != 0)
						tentativeFinal1 = result.get(0).getTentativeTeamTotalrcv()/result.get(0).getTentativeTeamCountrcv();
						if(result.get(0).getAnalyticalTeamCountrcv() != 0)
						analyticalFinal1 = result.get(0).getAnalyticalTeamTotalrcv()/result.get(0).getAnalyticalTeamCountrcv();
						if(result.get(0).getConfidentTeamCountrcv() != 0)
						confidentFinal1 = result.get(0).getConfidentTeamTotalrcv()/result.get(0).getConfidentTeamCountrcv();
						if(result.get(0).getFearTeamCountrcv() != 0)
						fearFinal1 = result.get(0).getFearTeamTotalrcv()/result.get(0).getFearTeamCountrcv();
						
						ToneOfMail toneOfMail1 = new ToneOfMail();
						Double totalr = angerFinal1+joyFinal1+sadnessFinal1+tentativeFinal1+analyticalFinal1+confidentFinal1+fearFinal1;
						Double angercP = (double)Math.round(((angerFinal1)*100)/(totalr));
						Double joyrP = (double)Math.round(((joyFinal1)*100)/(totalr));
						Double sadnrP = (double)Math.round(((sadnessFinal1)*100)/(totalr));
						Double tenrP = (double)Math.round(((tentativeFinal1)*100)/(totalr));
						Double analrP = (double)Math.round(((analyticalFinal1)*100)/(totalr));
						Double confrP = (double)Math.round(((confidentFinal1)*100)/(totalr));
						Double fearrP =  (double)Math.round(((fearFinal1)*100)/(totalr));
						
						toneOfMail1.setAnger(angercP);
						toneOfMail1.setJoy(joyrP);
						toneOfMail1.setSadness(sadnrP);
						toneOfMail1.setTentative(tenrP);
						toneOfMail1.setAnalytical(analrP);
						toneOfMail1.setConfident(confrP);
						toneOfMail1.setFear(fearrP);
						
						/*toneOfMail1.setAnger(angerFinal1);
						toneOfMail1.setJoy(joyFinal1);
						toneOfMail1.setSadness(sadnessFinal1);
						toneOfMail1.setTentative(tentativeFinal1);
						toneOfMail1.setAnalytical(analyticalFinal1);
						toneOfMail1.setConfident(confidentFinal1);
						toneOfMail1.setFear(fearFinal1);*/
						
						//snt
						
						Double angerFinal2 = 0.0d;
						Double joyFinal2 = 0.0d;
						Double sadnessFinal2 = 0.0d;
						Double tentativeFinal2 = 0.0d;
						Double analyticalFinal2 = 0.0d;
						Double confidentFinal2 = 0.0d;
						Double fearFinal2 = 0.0d;
						
						if(result.get(0).getAngerTeamCountsnt() != 0)
						angerFinal2 = result.get(0).getAngerTeamTotalsnt()/result.get(0).getAngerTeamCountsnt();
						if(result.get(0).getJoyTeamCountsnt() != 0)
						joyFinal2 = result.get(0).getJoyTeamTotalsnt()/result.get(0).getJoyTeamCountsnt();
						if(result.get(0).getSadnessTeamCountsnt() != 0)
						sadnessFinal2 = result.get(0).getSadnessTeamTotalsnt()/result.get(0).getSadnessTeamCountsnt();
						if(result.get(0).getTentativeTeamCountsnt() != 0)
						tentativeFinal2 = result.get(0).getTentativeTeamTotalsnt()/result.get(0).getTentativeTeamCountsnt();
						if(result.get(0).getAnalyticalTeamCountsnt() != 0)
						analyticalFinal2 = result.get(0).getAnalyticalTeamTotalsnt()/result.get(0).getAnalyticalTeamCountsnt();
						if(result.get(0).getConfidentTeamCountsnt() != 0)
						confidentFinal2 = result.get(0).getConfidentTeamTotalsnt()/result.get(0).getConfidentTeamCountsnt();
						if(result.get(0).getFearTeamCountsnt() != 0)
						fearFinal2 = result.get(0).getFearTeamTotalsnt()/result.get(0).getFearTeamCountsnt();
						
						ToneOfMail toneOfMail2 = new ToneOfMail();
						
						Double totalsnt = angerFinal2+joyFinal2+sadnessFinal2+tentativeFinal2+analyticalFinal2+confidentFinal2+fearFinal2;
						Double angersntP = (double)Math.round(((angerFinal2)*100)/(totalsnt));
						Double joyrsntP = (double)Math.round(((joyFinal2)*100)/(totalsnt));
						Double sadnrsntP = (double)Math.round(((sadnessFinal2)*100)/(totalsnt));
						Double tenrsntP = (double)Math.round(((tentativeFinal2)*100)/(totalsnt));
						Double analrsntP = (double)Math.round(((analyticalFinal2)*100)/(totalsnt));
						Double confrsntP = (double)Math.round(((confidentFinal2)*100)/(totalsnt));
						Double fearrsntP =  (double)Math.round(((fearFinal2)*100)/(totalsnt));
						
						toneOfMail2.setAnger(angersntP);
						toneOfMail2.setJoy(joyrsntP);
						toneOfMail2.setSadness(sadnrsntP);
						toneOfMail2.setTentative(tenrsntP);
						toneOfMail2.setAnalytical(analrsntP);
						toneOfMail2.setConfident(confrsntP);
						toneOfMail2.setFear(fearrsntP);		
			
			
			
		
		
/*	// Average of All Self Scores 
		
		Double angerFinal = (double)Math.round(result.get(0).getAngerTeamTotal()/result.get(0).getAngerTeamCount());
		Double joyFinal = (double)Math.round(result.get(0).getJoyTeamTotal()/result.get(0).getJoyTeamCount());
		Double sadnessFinal = (double)Math.round(result.get(0).getSadnessTeamTotal()/result.get(0).getSadnessTeamCount());
		Double tentativeFinal = (double)Math.round(result.get(0).getTentativeTeamTotal()/result.get(0).getTentativeTeamCount());
		Double analyticalFinal = (double)Math.round(result.get(0).getAnalyticalTeamTotal()/result.get(0).getAnalyticalTeamCount());
		Double confidentFinal = (double)Math.round(result.get(0).getConfidentTeamTotal()/result.get(0).getConfidentTeamCount());
		Double fearFinal = (double)Math.round(result.get(0).getFearTeamTotal()/result.get(0).getFearTeamCount());
		
		ToneOfMail toneOfMail = new ToneOfMail();
		toneOfMail.setAnger(angerFinal);
		toneOfMail.setJoy(joyFinal);
		toneOfMail.setSadness(sadnessFinal);
		toneOfMail.setTentative(tentativeFinal);
		toneOfMail.setAnalytical(analyticalFinal);
		toneOfMail.setConfident(confidentFinal);
		toneOfMail.setFear(fearFinal);
		
		//rcv
		
		Double angerFinal1 = (double)Math.round(result.get(0).getAngerTeamTotalrcv()/result.get(0).getAngerTeamCountrcv());
		Double joyFinal1 = (double)Math.round(result.get(0).getJoyTeamTotalrcv()/result.get(0).getJoyTeamCountrcv());
		Double sadnessFinal1 = (double)Math.round(result.get(0).getSadnessTeamTotalrcv()/result.get(0).getSadnessTeamCountrcv());
		Double tentativeFinal1 = (double)Math.round(result.get(0).getTentativeTeamTotalrcv()/result.get(0).getTentativeTeamCountrcv());
		Double analyticalFinal1 = (double)Math.round(result.get(0).getAnalyticalTeamTotalrcv()/result.get(0).getAnalyticalTeamCountrcv());
		Double confidentFinal1 = (double)Math.round(result.get(0).getConfidentTeamTotalrcv()/result.get(0).getConfidentTeamCountrcv());
		Double fearFinal1 = (double)Math.round(result.get(0).getFearTeamTotalrcv()/result.get(0).getFearTeamCountrcv());
		
		ToneOfMail toneOfMail1 = new ToneOfMail();
		toneOfMail1.setAnger(angerFinal1);
		toneOfMail1.setJoy(joyFinal1);
		toneOfMail1.setSadness(sadnessFinal1);
		toneOfMail1.setTentative(tentativeFinal1);
		toneOfMail1.setAnalytical(analyticalFinal1);
		toneOfMail1.setConfident(confidentFinal1);
		toneOfMail1.setFear(fearFinal1);
		
		//snt
		
		Double angerFinal2 = (double)Math.round(result.get(0).getAngerTeamTotalsnt()/result.get(0).getAngerTeamCountsnt());
		Double joyFinal2 = (double)Math.round(result.get(0).getJoyTeamTotalsnt()/result.get(0).getJoyTeamCountsnt());
		Double sadnessFinal2 = (double)Math.round(result.get(0).getSadnessTeamTotalsnt()/result.get(0).getSadnessTeamCountsnt());
		Double tentativeFinal2 = (double)Math.round(result.get(0).getTentativeTeamTotalsnt()/result.get(0).getTentativeTeamCountsnt());
		Double analyticalFinal2 = (double)Math.round(result.get(0).getAnalyticalTeamTotalsnt()/result.get(0).getAnalyticalTeamCountsnt());
		Double confidentFinal2 = (double)Math.round(result.get(0).getConfidentTeamTotalsnt()/result.get(0).getConfidentTeamCountsnt());
		Double fearFinal2 = (double)Math.round(result.get(0).getFearTeamTotalsnt()/result.get(0).getFearTeamCountsnt());
		
		ToneOfMail toneOfMail2 = new ToneOfMail();
		toneOfMail2.setAnger(angerFinal2);
		toneOfMail2.setJoy(joyFinal2);
		toneOfMail2.setSadness(sadnessFinal2);
		toneOfMail2.setTentative(tentativeFinal2);
		toneOfMail2.setAnalytical(analyticalFinal2);
		toneOfMail2.setConfident(confidentFinal2);
		toneOfMail2.setFear(fearFinal2);*/
		
				
		employeePersonalDataDTO.setToneOfTeamMail(toneOfMail);
		employeePersonalDataDTO.setToneOfTeamReceiveMail(toneOfMail1);
		employeePersonalDataDTO.setToneOfTeamSentMail(toneOfMail2);
		employeePersonalDataDTO.setNoOfMail(result.get(0).getTotalTeamMail());
		employeePersonalDataDTO.setNoOfReceiveMail(result.get(0).getTotalTeamMailRecevied());
		employeePersonalDataDTO.setNoOfSentMail(result.get(0).getTotalTeamMailSent());
		
		employeePersonalDataDTOs.add(employeePersonalDataDTO);
				
				
			}
			
			employeePersonalDataDTO1.setListOfEmployee(employeePersonalDataDTOs);
		    
			/*Query query = new Query();
			query.addCriteria(new Criteria().andOperator(Criteria.where("reportToId").is(employeeBO.getEmployeeId()),
					Criteria.where("roles").elemMatch(Criteria.where("designation").in(clientDataDTO.getSearchCriteria())),
					Criteria.where("roles").elemMatch(Criteria.where("status").is("active"))));
			
			
		List<EmployeeBO> employeeBOs =	mongoTemplate.find(query,EmployeeBO.class);*/
			
			
			
			
			return employeePersonalDataDTO1;
		}
		
		
		// search by employeeName
		
		
		public List<EmployeePersonalDataDTO> searchByEmployeeName(ClientDataDTO clientDataDTO) {
			
			List<EmployeePersonalDataDTO> employeePersonalDataDTO = new ArrayList<EmployeePersonalDataDTO>();
			
			
			Aggregation aggregation2 = Aggregation.newAggregation(
					
					Aggregation.match(Criteria.where("employeeIdFK").is("absc123")),
					Aggregation.unwind("clientEmailItems"),
					Aggregation.match(Criteria.where("clientEmailItems.toClientNames").in("Harley"))
					
					
					);
			
			List<DailyEmployeeUnwindPojo> results = mongoTemplate.aggregate(aggregation2, DailyEmployeeEmailToneBO.class, DailyEmployeeUnwindPojo.class).getMappedResults();
			
			
			
			ProjectionOperation projectionOperation = Aggregation.project()
					.andExpression("$companyList.client").as("teamName")
					.andExclude("_id");
			Aggregation aggregation = Aggregation.newAggregation(
                
					Aggregation.match(Criteria.where("teamName").is("abc")),
					Aggregation.unwind("$companyList"),
					Aggregation.match(Criteria.where("companyList.companyId").is("5acb16fe67a2b7664ff16f36")),
					projectionOperation,
					Aggregation.group("teamName")
					.sum("teamName.allMailScore.anger").as("angerTotal").sum("teamName.allMailScore.angerCount")
					.as("angerCount")
					
					);
			
			AggregationResults<AggregationPojo> groupResults 
			= mongoTemplate.aggregate(aggregation, DailyTeamEmailToneBO.class, AggregationPojo.class);
			List<AggregationPojo> result = groupResults.getMappedResults(); 
			
			
			
			List<EmployeeBO> employeePersonalDataDTOs = employeeRepository.findByEmployeeNameRegex(clientDataDTO.getSearchText(),
					clientDataDTO.getEmployeeId());
			
			for(EmployeeBO employeeBO : employeePersonalDataDTOs) {
				EmployeePersonalDataDTO employeePersonalDataDTO2 = new EmployeePersonalDataDTO();
				employeePersonalDataDTO2.setEmployeeName(employeeBO.getEmployeeName());
				employeePersonalDataDTO2.setEmailId(employeeBO.getEmailId());
				employeePersonalDataDTO.add(employeePersonalDataDTO2);
				
			}
			
		//	List<EmployeeBO> employeePersonalDataDTOs1 = employeeRepository.findByEmployeeNameLike("a","absc123");
			
			
			return employeePersonalDataDTO;
		}
		
		
		// search on employee name under particular Team  on employeeDashBoard
		
	public List<FilterByCriteria> filterOnSubEmployeeName(String empData,String key) {
			
		
		 UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
					.getContext().getAuthentication();
			
			String emailId = (String) authObj.getUserSessionInformation().get(
					EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
			
			EmployeeBO employeeBO = employeeRepository.findOne(emailId);
			
			List<FilterByCriteria> filterByCriterias = new ArrayList<FilterByCriteria>();
			
			//EmployeeRoleBO employeeRoleBO = employeeRoleRepository.findByEmployeeIdFKAndStatus(employeeBO.getEmployeeId(),"active");
			
			ProjectionOperation projectionOperation = Aggregation.project()
					.andExpression("employeeHierarchy").as("filterName")
					.andExclude("_id");
			
			Aggregation aggregation = null;
			
				
			
			 aggregation = Aggregation.newAggregation(
					
					
					
					Aggregation.match(Criteria.where("employeeIdFK").is(employeeBO.getEmployeeId()).and("status").is("active")),
					Aggregation.unwind("$employeeHierarchy"),
					Aggregation.match(Criteria.where("employeeHierarchy.employeeName").regex(empData,"i")),
					//Aggregation.project("employeeHierarchy")
					projectionOperation
					
					
					);
	
			
			
		
			
			List<FilterResultPojo> results = mongoTemplate.aggregate(aggregation, EmployeeRoleBO.class, FilterResultPojo.class).getMappedResults();
			
			for(FilterResultPojo dbObject : results) {
				
				FilterByCriteria filterByCriteria = new FilterByCriteria();
				filterByCriteria.setEmployeeName(dbObject.getFilterName().getEmployeeName());
				filterByCriteria.setEmployeeId(dbObject.getFilterName().getEmployeeId());
				filterByCriterias.add(filterByCriteria);
				
				
			}
					
			
			
			
			return filterByCriterias;
		}
		
		
		
		// search on employee filter
		
		public List<FilterByCriteria> filterOnEmployeeName(String empData,String key) {
			
			
		    
		    UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
					.getContext().getAuthentication();
			
			String emailId = (String) authObj.getUserSessionInformation().get(
					EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
			
			EmployeeBO employeeBO = employeeRepository.findOne(emailId);
			
			List<FilterByCriteria> filterByCriterias = new ArrayList<FilterByCriteria>();
			
			//EmployeeRoleBO employeeRoleBO = employeeRoleRepository.findByEmployeeIdFKAndStatus(employeeBO.getEmployeeId(),"active");
			
			ProjectionOperation projectionOperation = Aggregation.project()
					.andExpression("employeeHierarchy").as("filterName")
					.andExclude("_id");
			
			Aggregation aggregation = null;
			
			if (!key.isEmpty() && key.equalsIgnoreCase("employeeName")) {
				
			
			 aggregation = Aggregation.newAggregation(
					
					
					
					Aggregation.match(Criteria.where("employeeIdFK").is(employeeBO.getEmployeeId()).and("status").is("active")),
					Aggregation.unwind("$employeeHierarchy"),
					Aggregation.match(Criteria.where("employeeHierarchy.employeeName").regex(empData,"i")),
					//Aggregation.project("employeeHierarchy")
					projectionOperation
					
					
					);
			}
			
			if (!key.isEmpty() && key.equalsIgnoreCase("emailId")) {
				
				
				
				 aggregation = Aggregation.newAggregation(
						
						
						
						Aggregation.match(Criteria.where("employeeIdFK").is(employeeBO.getEmployeeId()).and("status").is("active")),
						Aggregation.unwind("$employeeHierarchy"),
						Aggregation.match(Criteria.where("employeeHierarchy.emailId").regex(empData,"i")),
						//Aggregation.project("employeeHierarchy")
						projectionOperation
						
						
						);
				}
			
			/*if (!key.isEmpty() && key.equalsIgnoreCase("designation")) {
				
				
				
				 aggregation = Aggregation.newAggregation(
						
						
						
						Aggregation.match(Criteria.where("employeeIdFK").is(employeeBO.getEmployeeId()).and("status").is("active")),
						Aggregation.unwind("$employeeHierarchy"),
						Aggregation.match(Criteria.where("employeeHierarchy.designation").regex(empData,"i")),
						
						//Aggregation.project("employeeHierarchy")
						projectionOperation
						
						
						);
				}*/
			
			List<FilterResultPojo> results = mongoTemplate.aggregate(aggregation, EmployeeRoleBO.class, FilterResultPojo.class).getMappedResults();
			
			for(FilterResultPojo dbObject : results) {
				
				FilterByCriteria filterByCriteria = new FilterByCriteria();
				filterByCriteria.setEmployeeName(dbObject.getFilterName().getEmployeeName());
				filterByCriteria.setEmployeeId(dbObject.getFilterName().getEmployeeId());
				filterByCriterias.add(filterByCriteria);
				
				
			}
					
			
		  /*  
			Query query = new Query();
			query.addCriteria(new Criteria().andOperator(Criteria.where("reportToId").is(employeeBO.getEmployeeId()),
					Criteria.where("roles").elemMatch(Criteria.where("designation").in(empData.getDesignation())),
					Criteria.where("roles").elemMatch(Criteria.where("status").is("active"))));
			
			
		List<EmployeeBO> employeeBOs =	mongoTemplate.find(query,EmployeeBO.class);*/
			
			
			return filterByCriterias;
		}
		
		
		// filter search Result of employee 
		
		
		public EmployeePersonalDataDTO getfilterEmployeeResult(ClientDataDTO clientDataDTO){
			
			EmployeePersonalDataDTO employeePersonalDataDTO1 = new EmployeePersonalDataDTO();
			
			List<EmployeePersonalDataDTO> employeePersonalDataDTOs = new ArrayList<EmployeePersonalDataDTO>();
			
			EmployeeBO employeeData = employeeRepository.findByEmployeeNameRegex1(clientDataDTO.getEmployeeName());
			
			EmployeeRoleBO employeeRoleData = employeeRoleRepository.findByEmployeeIdFKAndStatus(employeeData.getEmployeeId(), "active");
			

			Aggregation aggregation = Aggregation.newAggregation(
	                
					Aggregation.match(Criteria.where("employeeIdFK").is(employeeData.getEmployeeId()).and("date").gte("2018-04-16")),
					Aggregation.group("employeeIdFK").last("employeeIdFK").as("employeeId").last("name").as("name")
					.sum("teamTone.allMailScore.anger").as("angerTeamTotal").sum("teamTone.allMailScore.angerCount")
					.as("angerTeamCount").sum("teamTone.allMailScore.joy").as("joyTeamTotal").sum("teamTone.allMailScore.joyCount")
					.as("joyTeamCount").sum("teamTone.allMailScore.sadness").as("sadnessTeamTotal").sum("teamTone.allMailScore.sadnessCount")
					.as("sadnessTeamCount").sum("teamTone.allMailScore.tentative").as("tentativeTeamTotal").sum("teamTone.allMailScore.tentativeCount")
					.as("tentativeTeamCount").sum("teamTone.allMailScore.analytical").as("analyticalTeamTotal").sum("teamTone.allMailScore.analyticalCount")
					.as("analyticalTeamCount").sum("teamTone.allMailScore.confident").as("confidentTeamTotal").sum("teamTone.allMailScore.confidentCount")
					.as("confidentTeamCount").sum("teamTone.allMailScore.fear").as("fearTeamTotal").sum("teamTone.allMailScore.fearCount")
					.as("fearTeamCount")
					.sum("teamTone.receiveMailScore.anger").as("angerTeamTotalrcv").sum("teamTone.receiveMailScore.angerCount")
					.as("angerTeamCountrcv").sum("teamTone.receiveMailScore.joy").as("joyTeamTotalrcv").sum("teamTone.receiveMailScore.joyCount")
					.as("joyTeamCountrcv").sum("teamTone.receiveMailScore.sadness").as("sadnessTeamTotalrcv").sum("teamTone.receiveMailScore.sadnessCount")
					.as("sadnessTeamCountrcv").sum("teamTone.receiveMailScore.tentative").as("tentativeTeamTotalrcv").sum("teamTone.receiveMailScore.tentativeCount")
					.as("tentativeTeamCountrcv").sum("teamTone.receiveMailScore.analytical").as("analyticalTeamTotalrcv").sum("teamTone.receiveMailScore.analyticalCount")
					.as("analyticalTeamCountrcv").sum("teamTone.receiveMailScore.confident").as("confidentTeamTotalrcv").sum("teamTone.receiveMailScore.confidentCount")
					.as("confidentTeamCountrcv").sum("teamTone.receiveMailScore.fear").as("fearTeamTotalrcv").sum("teamTone.receiveMailScore.fearCount")
					.as("fearTeamCountrcv")
					.sum("teamTone.sentMailScore.anger").as("angerTeamTotalsnt").sum("teamTone.sentMailScore.angerCount")
					.as("angerTeamCountsnt").sum("teamTone.sentMailScore.joy").as("joyTeamTotalsnt").sum("teamTone.sentMailScore.joyCount")
					.as("joyTeamCountsnt").sum("teamTone.sentMailScore.sadness").as("sadnessTeamTotalsnt").sum("teamTone.sentMailScore.sadnessCount")
					.as("sadnessTeamCountsnt").sum("teamTone.sentMailScore.tentative").as("tentativeTeamTotalsnt").sum("teamTone.sentMailScore.tentativeCount")
					.as("tentativeTeamCountsnt").sum("teamTone.sentMailScore.analytical").as("analyticalTeamTotalsnt").sum("teamTone.sentMailScore.analyticalCount")
					.as("analyticalTeamCountsnt").sum("teamTone.sentMailScore.confident").as("confidentTeamTotalsnt").sum("teamTone.sentMailScore.confidentCount")
					.as("confidentTeamCountsnt").sum("teamTone.sentMailScore.fear").as("fearTeamTotalsnt").sum("teamTone.sentMailScore.fearCount")
					.as("fearTeamCountsnt")
					.sum("teamTone.totalMail").as("totalTeamMail")
					.sum("teamTone.totalMailRecevied").as("totalTeamMailRecevied")
					.sum("teamTone.totalMailSent").as("totalTeamMailSent")
					
						);
				
				AggregationResults<AggregationPojo> groupResults 
				= mongoTemplate.aggregate(aggregation, DailyEmployeeEmailToneBO.class, AggregationPojo.class);
				List<AggregationPojo> result = groupResults.getMappedResults();
				
				
				// Average of All Self Scores 
				
				Double angerFinal = 0.0d;
				Double joyFinal = 0.0d;
				Double sadnessFinal = 0.0d;
				Double tentativeFinal = 0.0d;
				Double analyticalFinal = 0.0d;
				Double confidentFinal = 0.0d;
				Double fearFinal = 0.0d;
				
				
				try {
				if(result.get(0).getAngerTeamCount() != 0)
				angerFinal = result.get(0).getAngerTeamTotal()/result.get(0).getAngerTeamCount();
				}catch(Exception e) {
//					e.printStackTrace();
				}
				try {
				if(result.get(0).getJoyTeamCount() != 0)
				joyFinal = result.get(0).getJoyTeamTotal()/result.get(0).getJoyTeamCount();
				}catch(Exception e) {
//					e.printStackTrace();
				}
				try {
				if(result.get(0).getSadnessTeamCount() != 0)
				sadnessFinal = result.get(0).getSadnessTeamTotal()/result.get(0).getSadnessTeamCount();
				}catch(Exception e) {
//					e.printStackTrace();
				}
				try {
				if(result.get(0).getTentativeTeamCount() != 0)
				tentativeFinal = result.get(0).getTentativeTeamTotal()/result.get(0).getTentativeTeamCount();
				}catch(Exception e) {
//					e.printStackTrace();
				}
				try {
				if(result.get(0).getAnalyticalTeamCount() != 0)
				analyticalFinal = result.get(0).getAnalyticalTeamTotal()/result.get(0).getAnalyticalTeamCount();
				}catch(Exception e) {
//					e.printStackTrace();
				}
				try {
				if(result.get(0).getConfidentTeamCount() != 0);
				confidentFinal = result.get(0).getConfidentTeamTotal()/result.get(0).getConfidentTeamCount();
				}catch(Exception e) {
//					e.printStackTrace();
				}
				try {
				if(result.get(0).getFearTeamCount() != 0)
				fearFinal =result.get(0).getFearTeamTotal()/result.get(0).getFearTeamCount();
				}catch(Exception e) {
//					e.printStackTrace();
				}
				ToneOfMail toneOfMail = new ToneOfMail();
				
				Double total = angerFinal+joyFinal+sadnessFinal+tentativeFinal+analyticalFinal+confidentFinal+fearFinal;
				Double angerP = 0.0d;
				Double joyP = 0.0d;
				Double sadnP = 0.0d;
				Double tenP = 0.0d;
				Double analP = 0.0d;
				Double confP = 0.0d;
				Double fearP = 0.0d;
				
				if(total > 0) {
				angerP = (double)Math.round(((angerFinal)*100)/(total));
				joyP = (double)Math.round(((joyFinal)*100)/(total));
				sadnP = (double)Math.round(((sadnessFinal)*100)/(total));
				tenP = (double)Math.round(((tentativeFinal)*100)/(total));
				analP = (double)Math.round(((analyticalFinal)*100)/(total));
				confP = (double)Math.round(((confidentFinal)*100)/(total));
				fearP =  (double)Math.round(((fearFinal)*100)/(total));
				}
				toneOfMail.setAnger(angerP);
				toneOfMail.setJoy(joyP);
				toneOfMail.setSadness(sadnP);
				toneOfMail.setTentative(tenP);
				toneOfMail.setAnalytical(analP);
				toneOfMail.setConfident(confP);
				toneOfMail.setFear(fearP);
				/*
				toneOfMail.setAnger(angerFinal);
				toneOfMail.setJoy(joyFinal);
				toneOfMail.setSadness(sadnessFinal);
				toneOfMail.setTentative(tentativeFinal);
				toneOfMail.setAnalytical(analyticalFinal);
				toneOfMail.setConfident(confidentFinal);
				toneOfMail.setFear(fearFinal);*/
				
				//rcv
				
				Double angerFinal1 = 0.0d;
				Double joyFinal1 = 0.0d;
				Double sadnessFinal1 = 0.0d;
				Double tentativeFinal1 = 0.0d;
				Double analyticalFinal1 = 0.0d;
				Double confidentFinal1 = 0.0d;
				Double fearFinal1 = 0.0d;
				
				try {
				if(result.get(0).getAngerTeamCountrcv() != 0)
				angerFinal1 = result.get(0).getAngerTeamTotalrcv()/result.get(0).getAngerTeamCountrcv();
				}catch(Exception e) {
//					e.printStackTrace();
				}
				try {
				if(result.get(0).getJoyTeamCountrcv() != 0)
				joyFinal1 = result.get(0).getJoyTeamTotalrcv()/result.get(0).getJoyTeamCountrcv();
				}catch(Exception e) {
//					e.printStackTrace();
				}
				try {
				if(result.get(0).getSadnessTeamCountrcv() != 0)
				sadnessFinal1 = result.get(0).getSadnessTeamTotalrcv()/result.get(0).getSadnessTeamCountrcv();
				}catch(Exception e) {
//					e.printStackTrace();
				}
				try {
				if(result.get(0).getTentativeTeamCountrcv() != 0)
				tentativeFinal1 = result.get(0).getTentativeTeamTotalrcv()/result.get(0).getTentativeTeamCountrcv();
				}catch(Exception e) {
//					e.printStackTrace();
				}
				try {
				if(result.get(0).getAnalyticalTeamCountrcv() != 0)
				analyticalFinal1 = result.get(0).getAnalyticalTeamTotalrcv()/result.get(0).getAnalyticalTeamCountrcv();
				}catch(Exception e) {
//					e.printStackTrace();
				}
				try {
				if(result.get(0).getConfidentTeamCountrcv() != 0)
				confidentFinal1 = result.get(0).getConfidentTeamTotalrcv()/result.get(0).getConfidentTeamCountrcv();
				}catch(Exception e) {
//					e.printStackTrace();
				}
				try {
				if(result.get(0).getFearTeamCountrcv() != 0)
				fearFinal1 = result.get(0).getFearTeamTotalrcv()/result.get(0).getFearTeamCountrcv();
				}catch(Exception e) {
//					e.printStackTrace();
				}
				ToneOfMail toneOfMail1 = new ToneOfMail();
				Double totalr = angerFinal1+joyFinal1+sadnessFinal1+tentativeFinal1+analyticalFinal1+confidentFinal1+fearFinal1;
				
				Double angercP = 0.0d;
				Double joyrP = 0.0d;
				Double sadnrP = 0.0d;
				Double tenrP = 0.0d;
				Double analrP = 0.0d;
				Double confrP = 0.0d;
				Double fearrP = 0.0d;
				
				
				if(totalr >0) {
				angercP = (double)Math.round(((angerFinal1)*100)/(totalr));
				joyrP = (double)Math.round(((joyFinal1)*100)/(totalr));
				sadnrP = (double)Math.round(((sadnessFinal1)*100)/(totalr));
				tenrP = (double)Math.round(((tentativeFinal1)*100)/(totalr));
				analrP = (double)Math.round(((analyticalFinal1)*100)/(totalr));
				confrP = (double)Math.round(((confidentFinal1)*100)/(totalr));
				fearrP =  (double)Math.round(((fearFinal1)*100)/(totalr));
				}
				toneOfMail1.setAnger(angercP);
				toneOfMail1.setJoy(joyrP);
				toneOfMail1.setSadness(sadnrP);
				toneOfMail1.setTentative(tenrP);
				toneOfMail1.setAnalytical(analrP);
				toneOfMail1.setConfident(confrP);
				toneOfMail1.setFear(fearrP);
				
				/*toneOfMail1.setAnger(angerFinal1);
				toneOfMail1.setJoy(joyFinal1);
				toneOfMail1.setSadness(sadnessFinal1);
				toneOfMail1.setTentative(tentativeFinal1);
				toneOfMail1.setAnalytical(analyticalFinal1);
				toneOfMail1.setConfident(confidentFinal1);
				toneOfMail1.setFear(fearFinal1);*/
				
				//snt
				
				Double angerFinal2 = 0.0d;
				Double joyFinal2 = 0.0d;
				Double sadnessFinal2 = 0.0d;
				Double tentativeFinal2 = 0.0d;
				Double analyticalFinal2 = 0.0d;
				Double confidentFinal2 = 0.0d;
				Double fearFinal2 = 0.0d;
				
				try {
				if(result.get(0).getAngerTeamCountsnt() != 0)
				angerFinal2 = result.get(0).getAngerTeamTotalsnt()/result.get(0).getAngerTeamCountsnt();
				}catch(Exception e) {
//					e.printStackTrace();
				}
				try {
				if(result.get(0).getJoyTeamCountsnt() != 0)
				joyFinal2 = result.get(0).getJoyTeamTotalsnt()/result.get(0).getJoyTeamCountsnt();
				}catch(Exception e) {
//					e.printStackTrace();
				}
				try {
				if(result.get(0).getSadnessTeamCountsnt() != 0)
				sadnessFinal2 = result.get(0).getSadnessTeamTotalsnt()/result.get(0).getSadnessTeamCountsnt();
				}catch(Exception e) {
//					e.printStackTrace();
				}
				try {
				if(result.get(0).getTentativeTeamCountsnt() != 0)
				tentativeFinal2 = result.get(0).getTentativeTeamTotalsnt()/result.get(0).getTentativeTeamCountsnt();
				}catch(Exception e) {
//					e.printStackTrace();
				}
				try {
				if(result.get(0).getAnalyticalTeamCountsnt() != 0)
				analyticalFinal2 = result.get(0).getAnalyticalTeamTotalsnt()/result.get(0).getAnalyticalTeamCountsnt();
				}catch(Exception e) {
//					e.printStackTrace();
				}
				try {
				if(result.get(0).getConfidentTeamCountsnt() != 0)
				confidentFinal2 = result.get(0).getConfidentTeamTotalsnt()/result.get(0).getConfidentTeamCountsnt();
				}catch(Exception e) {
//					e.printStackTrace();
				}
				try {
				if(result.get(0).getFearTeamCountsnt() != 0)
				fearFinal2 = result.get(0).getFearTeamTotalsnt()/result.get(0).getFearTeamCountsnt();
				}catch(Exception e) {
					e.printStackTrace();
				}
				ToneOfMail toneOfMail2 = new ToneOfMail();
				
				Double totalsnt = angerFinal2+joyFinal2+sadnessFinal2+tentativeFinal2+analyticalFinal2+confidentFinal2+fearFinal2;
				
				Double angersntP = 0.0d;
				Double joyrsntP = 0.0d;
				Double sadnrsntP = 0.0d;
				Double tenrsntP = 0.0d;
				Double analrsntP = 0.0d;
				Double confrsntP = 0.0d;
				Double fearrsntP = 0.0d;
				
				if(totalsnt > 0) {
				angersntP = (double)Math.round(((angerFinal2)*100)/(totalsnt));
				joyrsntP = (double)Math.round(((joyFinal2)*100)/(totalsnt));
				sadnrsntP = (double)Math.round(((sadnessFinal2)*100)/(totalsnt));
				tenrsntP = (double)Math.round(((tentativeFinal2)*100)/(totalsnt));
				analrsntP = (double)Math.round(((analyticalFinal2)*100)/(totalsnt));
				confrsntP = (double)Math.round(((confidentFinal2)*100)/(totalsnt));
				fearrsntP =  (double)Math.round(((fearFinal2)*100)/(totalsnt));
				}
				toneOfMail2.setAnger(angersntP);
				toneOfMail2.setJoy(joyrsntP);
				toneOfMail2.setSadness(sadnrsntP);
				toneOfMail2.setTentative(tenrsntP);
				toneOfMail2.setAnalytical(analrsntP);
				toneOfMail2.setConfident(confrsntP);
				toneOfMail2.setFear(fearrsntP);	
				
				
				
				
			/*	
				// Average of All Self Scores 
				
				Double angerFinal = (double)Math.round(result.get(0).getAngerTeamTotal()/result.get(0).getAngerTeamCount());
				Double joyFinal = (double)Math.round(result.get(0).getJoyTeamTotal()/result.get(0).getJoyTeamCount());
				Double sadnessFinal = (double)Math.round(result.get(0).getSadnessTeamTotal()/result.get(0).getSadnessTeamCount());
				Double tentativeFinal = (double)Math.round(result.get(0).getTentativeTeamTotal()/result.get(0).getTentativeTeamCount());
				Double analyticalFinal = (double)Math.round(result.get(0).getAnalyticalTeamTotal()/result.get(0).getAnalyticalTeamCount());
				Double confidentFinal = (double)Math.round(result.get(0).getConfidentTeamTotal()/result.get(0).getConfidentTeamCount());
				Double fearFinal = (double)Math.round(result.get(0).getFearTeamTotal()/result.get(0).getFearTeamCount());
				
				ToneOfMail toneOfMail = new ToneOfMail();
				toneOfMail.setAnger(angerFinal);
				toneOfMail.setJoy(joyFinal);
				toneOfMail.setSadness(sadnessFinal);
				toneOfMail.setTentative(tentativeFinal);
				toneOfMail.setAnalytical(analyticalFinal);
				toneOfMail.setConfident(confidentFinal);
				toneOfMail.setFear(fearFinal);
				
				//rcv
				
				Double angerFinal1 = (double)Math.round(result.get(0).getAngerTeamTotalrcv()/result.get(0).getAngerTeamCountrcv());
				Double joyFinal1 = (double)Math.round(result.get(0).getJoyTeamTotalrcv()/result.get(0).getJoyTeamCountrcv());
				Double sadnessFinal1 = (double)Math.round(result.get(0).getSadnessTeamTotalrcv()/result.get(0).getSadnessTeamCountrcv());
				Double tentativeFinal1 = (double)Math.round(result.get(0).getTentativeTeamTotalrcv()/result.get(0).getTentativeTeamCountrcv());
				Double analyticalFinal1 = (double)Math.round(result.get(0).getAnalyticalTeamTotalrcv()/result.get(0).getAnalyticalTeamCountrcv());
				Double confidentFinal1 = (double)Math.round(result.get(0).getConfidentTeamTotalrcv()/result.get(0).getConfidentTeamCountrcv());
				Double fearFinal1 = (double)Math.round(result.get(0).getFearTeamTotalrcv()/result.get(0).getFearTeamCountrcv());
				
				ToneOfMail toneOfMail1 = new ToneOfMail();
				toneOfMail1.setAnger(angerFinal1);
				toneOfMail1.setJoy(joyFinal1);
				toneOfMail1.setSadness(sadnessFinal1);
				toneOfMail1.setTentative(tentativeFinal1);
				toneOfMail1.setAnalytical(analyticalFinal1);
				toneOfMail1.setConfident(confidentFinal1);
				toneOfMail1.setFear(fearFinal1);
				
				//snt
				
				Double angerFinal2 = (double)Math.round(result.get(0).getAngerTeamTotalsnt()/result.get(0).getAngerTeamCountsnt());
				Double joyFinal2 = (double)Math.round(result.get(0).getJoyTeamTotalsnt()/result.get(0).getJoyTeamCountsnt());
				Double sadnessFinal2 = (double)Math.round(result.get(0).getSadnessTeamTotalsnt()/result.get(0).getSadnessTeamCountsnt());
				Double tentativeFinal2 = (double)Math.round(result.get(0).getTentativeTeamTotalsnt()/result.get(0).getTentativeTeamCountsnt());
				Double analyticalFinal2 = (double)Math.round(result.get(0).getAnalyticalTeamTotalsnt()/result.get(0).getAnalyticalTeamCountsnt());
				Double confidentFinal2 = (double)Math.round(result.get(0).getConfidentTeamTotalsnt()/result.get(0).getConfidentTeamCountsnt());
				Double fearFinal2 = (double)Math.round(result.get(0).getFearTeamTotalsnt()/result.get(0).getFearTeamCountsnt());
				
				ToneOfMail toneOfMail2 = new ToneOfMail();
				toneOfMail2.setAnger(angerFinal2);
				toneOfMail2.setJoy(joyFinal2);
				toneOfMail2.setSadness(sadnessFinal2);
				toneOfMail2.setTentative(tentativeFinal2);
				toneOfMail2.setAnalytical(analyticalFinal2);
				toneOfMail2.setConfident(confidentFinal2);
				toneOfMail2.setFear(fearFinal2);*/
				
				EmployeePersonalDataDTO employeePersonalDataDTO = new EmployeePersonalDataDTO();	
				employeePersonalDataDTO.setToneOfTeamMail(toneOfMail);
				employeePersonalDataDTO.setToneOfTeamReceiveMail(toneOfMail1);
				employeePersonalDataDTO.setToneOfTeamSentMail(toneOfMail2);
				try {
				employeePersonalDataDTO.setNoOfMail(result.get(0).getTotalTeamMail());
				}catch(Exception e) {
					employeePersonalDataDTO.setNoOfMail(0l);
				}
				try {
				employeePersonalDataDTO.setNoOfReceiveMail(result.get(0).getTotalTeamMailRecevied());
				}catch(Exception e) {
					employeePersonalDataDTO.setNoOfReceiveMail(0l);
				}
				try {
				employeePersonalDataDTO.setNoOfSentMail(result.get(0).getTotalTeamMailSent());
				}catch(Exception e) {
					employeePersonalDataDTO.setNoOfSentMail(0l);
				}
				try {
				employeePersonalDataDTO.setEmployeeName(result.get(0).getName());
				}catch(Exception e) {
					employeePersonalDataDTO.setEmployeeName("");
				}
				employeePersonalDataDTO.setEmployeeId(result.get(0).getEmployeeId());
				
				employeePersonalDataDTOs.add(employeePersonalDataDTO);
				
				EmployeeRoleBO employeeRoleBOs = employeeRoleRepository.findByEmployeeIdFKAndStatus(employeeData.getEmployeeId(), "active");
//				EmployeeBO employeeBO =	employeeRepository.findByEmployeeId(clientDataDTO.getEmployeeId());
				EmployeeBO employeeBO =	employeeRepository.findByEmployeeId(employeeRoleData.getReportToId());
//				EmployeeRoleBO employeeRoleBOs1 = employeeRoleRepository.findByEmployeeIdFKAndStatus(employeeBO.getEmployeeId(), "active");
					
				employeePersonalDataDTO1.setDesignation(employeeRoleBOs.getDesignation());
				employeePersonalDataDTO1.setEmployeeName(employeeBO.getEmployeeName());
				employeePersonalDataDTO1.setEmployeeId(employeeBO.getEmployeeId());
				employeePersonalDataDTO1.setListOfEmployee(employeePersonalDataDTOs);
				
			
			return employeePersonalDataDTO1;
		}
		
		
		
		// search on specific designation 
		
		
		public EmployeePersonalDataDTO getEmployeeSpecificResult(EmployeePersonalDataDTO empData){
			
				EmployeePersonalDataDTO employeePersonalDataDTO = new EmployeePersonalDataDTO();
			

			    UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
						.getContext().getAuthentication();
				
				String emailId = (String) authObj.getUserSessionInformation().get(
						EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
				
				EmployeeBO employeeBO = employeeRepository.findOne(emailId);
				
				List<EmployeePersonalDataDTO> list = new ArrayList<EmployeePersonalDataDTO>();
				
				//EmployeeRoleBO employeeRoleBO = employeeRoleRepository.findByEmployeeIdFKAndStatus(employeeBO.getEmployeeId(),"active");
				
				ProjectionOperation projectionOperation = Aggregation.project()
						.andExpression("employeeHierarchy").as("filterName")
						.andExclude("_id");
				
			
				Aggregation aggregation1 = Aggregation.newAggregation(
							
							
							
							Aggregation.match(Criteria.where("employeeIdFK").is(employeeBO.getEmployeeId()).and("status").is("active")),
							Aggregation.unwind("$employeeHierarchy"),
							Aggregation.match(Criteria.where("employeeHierarchy.designation").regex(empData.getDesignation(),"i")),
							
							//Aggregation.project("employeeHierarchy")
							projectionOperation
							
							
							);
				List<FilterResultPojo> results = mongoTemplate.aggregate(aggregation1, EmployeeRoleBO.class, FilterResultPojo.class).getMappedResults();

				for(FilterResultPojo dbObject : results) {

				Aggregation aggregation = Aggregation.newAggregation(
	                
					Aggregation.match(Criteria.where("employeeIdFK").is(dbObject.getFilterName().getEmployeeId()).and("date").gte("2018-04-16")),
					Aggregation.group("employeeIdFK").last("employeeIdFK").as("employeeId").last("name").as("name")
					.sum("teamTone.allMailScore.anger").as("angerTeamTotal").sum("teamTone.allMailScore.angerCount")
					.as("angerTeamCount").sum("teamTone.allMailScore.joy").as("joyTeamTotal").sum("teamTone.allMailScore.joyCount")
					.as("joyTeamCount").sum("teamTone.allMailScore.sadness").as("sadnessTeamTotal").sum("teamTone.allMailScore.sadnessCount")
					.as("sadnessTeamCount").sum("teamTone.allMailScore.tentative").as("tentativeTeamTotal").sum("teamTone.allMailScore.tentativeCount")
					.as("tentativeTeamCount").sum("teamTone.allMailScore.analytical").as("analyticalTeamTotal").sum("teamTone.allMailScore.analyticalCount")
					.as("analyticalTeamCount").sum("teamTone.allMailScore.confident").as("confidentTeamTotal").sum("teamTone.allMailScore.confidentCount")
					.as("confidentTeamCount").sum("teamTone.allMailScore.fear").as("fearTeamTotal").sum("teamTone.allMailScore.fearCount")
					.as("fearTeamCount")
					.sum("teamTone.receiveMailScore.anger").as("angerTeamTotalrcv").sum("teamTone.receiveMailScore.angerCount")
					.as("angerTeamCountrcv").sum("teamTone.receiveMailScore.joy").as("joyTeamTotalrcv").sum("teamTone.receiveMailScore.joyCount")
					.as("joyTeamCountrcv").sum("teamTone.receiveMailScore.sadness").as("sadnessTeamTotalrcv").sum("teamTone.receiveMailScore.sadnessCount")
					.as("sadnessTeamCountrcv").sum("teamTone.receiveMailScore.tentative").as("tentativeTeamTotalrcv").sum("teamTone.receiveMailScore.tentativeCount")
					.as("tentativeTeamCountrcv").sum("teamTone.receiveMailScore.analytical").as("analyticalTeamTotalrcv").sum("teamTone.receiveMailScore.analyticalCount")
					.as("analyticalTeamCountrcv").sum("teamTone.receiveMailScore.confident").as("confidentTeamTotalrcv").sum("teamTone.receiveMailScore.confidentCount")
					.as("confidentTeamCountrcv").sum("teamTone.receiveMailScore.fear").as("fearTeamTotalrcv").sum("teamTone.receiveMailScore.fearCount")
					.as("fearTeamCountrcv")
					.sum("teamTone.sentMailScore.anger").as("angerTeamTotalsnt").sum("teamTone.sentMailScore.angerCount")
					.as("angerTeamCountsnt").sum("teamTone.sentMailScore.joy").as("joyTeamTotalsnt").sum("teamTone.sentMailScore.joyCount")
					.as("joyTeamCountsnt").sum("teamTone.sentMailScore.sadness").as("sadnessTeamTotalsnt").sum("teamTone.sentMailScore.sadnessCount")
					.as("sadnessTeamCountsnt").sum("teamTone.sentMailScore.tentative").as("tentativeTeamTotalsnt").sum("teamTone.sentMailScore.tentativeCount")
					.as("tentativeTeamCountsnt").sum("teamTone.sentMailScore.analytical").as("analyticalTeamTotalsnt").sum("teamTone.sentMailScore.analyticalCount")
					.as("analyticalTeamCountsnt").sum("teamTone.sentMailScore.confident").as("confidentTeamTotalsnt").sum("teamTone.sentMailScore.confidentCount")
					.as("confidentTeamCountsnt").sum("teamTone.sentMailScore.fear").as("fearTeamTotalsnt").sum("teamTone.sentMailScore.fearCount")
					.as("fearTeamCountsnt")
					.sum("teamTone.totalMail").as("totalTeamMail")
					.sum("teamTone.totalMailRecevied").as("totalTeamMailRecevied")
					.sum("teamTone.totalMailSent").as("totalTeamMailSent")
					
						);
				
				AggregationResults<AggregationPojo> groupResults 
				= mongoTemplate.aggregate(aggregation, DailyEmployeeEmailToneBO.class, AggregationPojo.class);
				List<AggregationPojo> result = groupResults.getMappedResults();
				
				
				
				// Average of All Self Scores 
				
				Double angerFinal = result.get(0).getAngerTeamTotal()/result.get(0).getAngerTeamCount();
				Double joyFinal = result.get(0).getJoyTeamTotal()/result.get(0).getJoyTeamCount();
				Double sadnessFinal = result.get(0).getSadnessTeamTotal()/result.get(0).getSadnessTeamCount();
				Double tentativeFinal = result.get(0).getTentativeTeamTotal()/result.get(0).getTentativeTeamCount();
				Double analyticalFinal = result.get(0).getAnalyticalTeamTotal()/result.get(0).getAnalyticalTeamCount();
				Double confidentFinal = result.get(0).getConfidentTeamTotal()/result.get(0).getConfidentTeamCount();
				Double fearFinal =result.get(0).getFearTeamTotal()/result.get(0).getFearTeamCount();
				
				ToneOfMail toneOfMail = new ToneOfMail();
				
				Double total = angerFinal+joyFinal+sadnessFinal+tentativeFinal+analyticalFinal+confidentFinal+fearFinal;
				Double angerP = (double)Math.round(((angerFinal)*100)/(total));
				Double joyP = (double)Math.round(((joyFinal)*100)/(total));
				Double sadnP = (double)Math.round(((sadnessFinal)*100)/(total));
				Double tenP = (double)Math.round(((tentativeFinal)*100)/(total));
				Double analP = (double)Math.round(((analyticalFinal)*100)/(total));
				Double confP = (double)Math.round(((confidentFinal)*100)/(total));
				Double fearP =  (double)Math.round(((fearFinal)*100)/(total));
				
				toneOfMail.setAnger(angerP);
				toneOfMail.setJoy(joyP);
				toneOfMail.setSadness(sadnP);
				toneOfMail.setTentative(tenP);
				toneOfMail.setAnalytical(analP);
				toneOfMail.setConfident(confP);
				toneOfMail.setFear(fearP);
				/*
				toneOfMail.setAnger(angerFinal);
				toneOfMail.setJoy(joyFinal);
				toneOfMail.setSadness(sadnessFinal);
				toneOfMail.setTentative(tentativeFinal);
				toneOfMail.setAnalytical(analyticalFinal);
				toneOfMail.setConfident(confidentFinal);
				toneOfMail.setFear(fearFinal);*/
				
				//rcv
				
				Double angerFinal1 = result.get(0).getAngerTeamTotalrcv()/result.get(0).getAngerTeamCountrcv();
				Double joyFinal1 = result.get(0).getJoyTeamTotalrcv()/result.get(0).getJoyTeamCountrcv();
				Double sadnessFinal1 = result.get(0).getSadnessTeamTotalrcv()/result.get(0).getSadnessTeamCountrcv();
				Double tentativeFinal1 = result.get(0).getTentativeTeamTotalrcv()/result.get(0).getTentativeTeamCountrcv();
				Double analyticalFinal1 = result.get(0).getAnalyticalTeamTotalrcv()/result.get(0).getAnalyticalTeamCountrcv();
				Double confidentFinal1 = result.get(0).getConfidentTeamTotalrcv()/result.get(0).getConfidentTeamCountrcv();
				Double fearFinal1 = result.get(0).getFearTeamTotalrcv()/result.get(0).getFearTeamCountrcv();
				
				ToneOfMail toneOfMail1 = new ToneOfMail();
				Double totalr = angerFinal1+joyFinal1+sadnessFinal1+tentativeFinal1+analyticalFinal1+confidentFinal1+fearFinal1;
				Double angercP = (double)Math.round(((angerFinal1)*100)/(totalr));
				Double joyrP = (double)Math.round(((joyFinal1)*100)/(totalr));
				Double sadnrP = (double)Math.round(((sadnessFinal1)*100)/(totalr));
				Double tenrP = (double)Math.round(((tentativeFinal1)*100)/(totalr));
				Double analrP = (double)Math.round(((analyticalFinal1)*100)/(totalr));
				Double confrP = (double)Math.round(((confidentFinal1)*100)/(totalr));
				Double fearrP =  (double)Math.round(((fearFinal1)*100)/(totalr));
				
				toneOfMail1.setAnger(angercP);
				toneOfMail1.setJoy(joyrP);
				toneOfMail1.setSadness(sadnrP);
				toneOfMail1.setTentative(tenrP);
				toneOfMail1.setAnalytical(analrP);
				toneOfMail1.setConfident(confrP);
				toneOfMail1.setFear(fearrP);
				
				/*toneOfMail1.setAnger(angerFinal1);
				toneOfMail1.setJoy(joyFinal1);
				toneOfMail1.setSadness(sadnessFinal1);
				toneOfMail1.setTentative(tentativeFinal1);
				toneOfMail1.setAnalytical(analyticalFinal1);
				toneOfMail1.setConfident(confidentFinal1);
				toneOfMail1.setFear(fearFinal1);*/
				
				//snt
				
				Double angerFinal2 = result.get(0).getAngerTeamTotalsnt()/result.get(0).getAngerTeamCountsnt();
				Double joyFinal2 = result.get(0).getJoyTeamTotalsnt()/result.get(0).getJoyTeamCountsnt();
				Double sadnessFinal2 = result.get(0).getSadnessTeamTotalsnt()/result.get(0).getSadnessTeamCountsnt();
				Double tentativeFinal2 = result.get(0).getTentativeTeamTotalsnt()/result.get(0).getTentativeTeamCountsnt();
				Double analyticalFinal2 = result.get(0).getAnalyticalTeamTotalsnt()/result.get(0).getAnalyticalTeamCountsnt();
				Double confidentFinal2 = result.get(0).getConfidentTeamTotalsnt()/result.get(0).getConfidentTeamCountsnt();
				Double fearFinal2 = result.get(0).getFearTeamTotalsnt()/result.get(0).getFearTeamCountsnt();
				
				ToneOfMail toneOfMail2 = new ToneOfMail();
				
				Double totalsnt = angerFinal2+joyFinal2+sadnessFinal2+tentativeFinal2+analyticalFinal2+confidentFinal2+fearFinal2;
				Double angersntP = (double)Math.round(((angerFinal2)*100)/(totalsnt));
				Double joyrsntP = (double)Math.round(((joyFinal2)*100)/(totalsnt));
				Double sadnrsntP = (double)Math.round(((sadnessFinal2)*100)/(totalsnt));
				Double tenrsntP = (double)Math.round(((tentativeFinal2)*100)/(totalsnt));
				Double analrsntP = (double)Math.round(((analyticalFinal2)*100)/(totalsnt));
				Double confrsntP = (double)Math.round(((confidentFinal2)*100)/(totalsnt));
				Double fearrsntP =  (double)Math.round(((fearFinal2)*100)/(totalsnt));
				
				toneOfMail2.setAnger(angersntP);
				toneOfMail2.setJoy(joyrsntP);
				toneOfMail2.setSadness(sadnrsntP);
				toneOfMail2.setTentative(tenrsntP);
				toneOfMail2.setAnalytical(analrsntP);
				toneOfMail2.setConfident(confrsntP);
				toneOfMail2.setFear(fearrsntP);	
				
				
				
				/*
				// Average of All Self Scores 
				
				Double angerFinal = (double)Math.round(result.get(0).getAngerTeamTotal()/result.get(0).getAngerTeamCount());
				Double joyFinal = (double)Math.round(result.get(0).getJoyTeamTotal()/result.get(0).getJoyTeamCount());
				Double sadnessFinal = (double)Math.round(result.get(0).getSadnessTeamTotal()/result.get(0).getSadnessTeamCount());
				Double tentativeFinal = (double)Math.round(result.get(0).getTentativeTeamTotal()/result.get(0).getTentativeTeamCount());
				Double analyticalFinal = (double)Math.round(result.get(0).getAnalyticalTeamTotal()/result.get(0).getAnalyticalTeamCount());
				Double confidentFinal = (double)Math.round(result.get(0).getConfidentTeamTotal()/result.get(0).getConfidentTeamCount());
				Double fearFinal = (double)Math.round(result.get(0).getFearTeamTotal()/result.get(0).getFearTeamCount());
				
				ToneOfMail toneOfMail = new ToneOfMail();
				toneOfMail.setAnger(angerFinal);
				toneOfMail.setJoy(joyFinal);
				toneOfMail.setSadness(sadnessFinal);
				toneOfMail.setTentative(tentativeFinal);
				toneOfMail.setAnalytical(analyticalFinal);
				toneOfMail.setConfident(confidentFinal);
				toneOfMail.setFear(fearFinal);
				
				//rcv
				
				Double angerFinal1 = (double)Math.round(result.get(0).getAngerTeamTotalrcv()/result.get(0).getAngerTeamCountrcv());
				Double joyFinal1 = (double)Math.round(result.get(0).getJoyTeamTotalrcv()/result.get(0).getJoyTeamCountrcv());
				Double sadnessFinal1 = (double)Math.round(result.get(0).getSadnessTeamTotalrcv()/result.get(0).getSadnessTeamCountrcv());
				Double tentativeFinal1 = (double)Math.round(result.get(0).getTentativeTeamTotalrcv()/result.get(0).getTentativeTeamCountrcv());
				Double analyticalFinal1 = (double)Math.round(result.get(0).getAnalyticalTeamTotalrcv()/result.get(0).getAnalyticalTeamCountrcv());
				Double confidentFinal1 = (double)Math.round(result.get(0).getConfidentTeamTotalrcv()/result.get(0).getConfidentTeamCountrcv());
				Double fearFinal1 = (double)Math.round(result.get(0).getFearTeamTotalrcv()/result.get(0).getFearTeamCountrcv());
				
				ToneOfMail toneOfMail1 = new ToneOfMail();
				toneOfMail1.setAnger(angerFinal1);
				toneOfMail1.setJoy(joyFinal1);
				toneOfMail1.setSadness(sadnessFinal1);
				toneOfMail1.setTentative(tentativeFinal1);
				toneOfMail1.setAnalytical(analyticalFinal1);
				toneOfMail1.setConfident(confidentFinal1);
				toneOfMail1.setFear(fearFinal1);
				
				//snt
				
				Double angerFinal2 = (double)Math.round(result.get(0).getAngerTeamTotalsnt()/result.get(0).getAngerTeamCountsnt());
				Double joyFinal2 = (double)Math.round(result.get(0).getJoyTeamTotalsnt()/result.get(0).getJoyTeamCountsnt());
				Double sadnessFinal2 = (double)Math.round(result.get(0).getSadnessTeamTotalsnt()/result.get(0).getSadnessTeamCountsnt());
				Double tentativeFinal2 = (double)Math.round(result.get(0).getTentativeTeamTotalsnt()/result.get(0).getTentativeTeamCountsnt());
				Double analyticalFinal2 = (double)Math.round(result.get(0).getAnalyticalTeamTotalsnt()/result.get(0).getAnalyticalTeamCountsnt());
				Double confidentFinal2 = (double)Math.round(result.get(0).getConfidentTeamTotalsnt()/result.get(0).getConfidentTeamCountsnt());
				Double fearFinal2 = (double)Math.round(result.get(0).getFearTeamTotalsnt()/result.get(0).getFearTeamCountsnt());
				
				ToneOfMail toneOfMail2 = new ToneOfMail();
				toneOfMail2.setAnger(angerFinal2);
				toneOfMail2.setJoy(joyFinal2);
				toneOfMail2.setSadness(sadnessFinal2);
				toneOfMail2.setTentative(tentativeFinal2);
				toneOfMail2.setAnalytical(analyticalFinal2);
				toneOfMail2.setConfident(confidentFinal2);
				toneOfMail2.setFear(fearFinal2);*/
				
				EmployeePersonalDataDTO employeePersonalDataDTO1 = new EmployeePersonalDataDTO();
						
				employeePersonalDataDTO1.setToneOfTeamMail(toneOfMail);
				employeePersonalDataDTO1.setToneOfTeamReceiveMail(toneOfMail1);
				employeePersonalDataDTO1.setToneOfTeamSentMail(toneOfMail2);
				employeePersonalDataDTO1.setNoOfMail(result.get(0).getTotalTeamMail());
				employeePersonalDataDTO1.setNoOfReceiveMail(result.get(0).getTotalTeamMailRecevied());
				employeePersonalDataDTO1.setNoOfSentMail(result.get(0).getTotalTeamMailSent());
				
				employeePersonalDataDTO1.setEmployeeName(result.get(0).getName());
				
				employeePersonalDataDTO1.setEmployeeId(result.get(0).getEmployeeIdFK());
				
				list.add(employeePersonalDataDTO1);
				
				}
				
				employeePersonalDataDTO.setListOfEmployee(list);
				employeePersonalDataDTO.setEmployeeName(empData.getDesignation());
				
				
				
			
			return employeePersonalDataDTO;
			
		}
		
		
		
		
		//client dashboard data
		
		public EmployeePersonalDataDTO getClientDashBoard(ClientDataDTO clientDataDTO){
			
			EmployeePersonalDataDTO employeePersonalDataDTO = new EmployeePersonalDataDTO();
		

		    UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
					.getContext().getAuthentication();
			
			String emailId = (String) authObj.getUserSessionInformation().get(
					EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
			
			EmployeeBO employeeBO = employeeRepository.findOne(emailId);
			
			List<EmployeePersonalDataDTO> list = new ArrayList<EmployeePersonalDataDTO>();
			
			if(employeeBO != null)
			employeePersonalDataDTO.setEmployeeId(employeeBO.getEmployeeId());
			
			ProjectionOperation projectionOperation1 = Aggregation.project()
					.andExpression("clients").as("teamName")
					.andExclude("_id");
			
			Aggregation aggregation2 = Aggregation.newAggregation(
	                
					Aggregation.match(Criteria.where("employeeIdFK").is(employeeBO.getEmployeeId())),
					Aggregation.unwind("$companyList"),
					Aggregation.match(Criteria.where("companyList.companyId").is(clientDataDTO.getCompanyId())),
					//projectionOperation1,
					Aggregation.group("employeeIdFK")
					.sum("companyList.allMailScore.anger").as("angerTeamTotal").sum("companyList.allMailScore.angerCount")
					.as("angerTeamCount").sum("companyList.allMailScore.joy").as("joyTeamTotal").sum("companyList.allMailScore.joyCount")
					.as("joyTeamCount").sum("companyList.allMailScore.sadness").as("sadnessTeamTotal").sum("companyList.allMailScore.sadnessCount")
					.as("sadnessTeamCount").sum("companyList.allMailScore.tentative").as("tentativeTeamTotal").sum("companyList.allMailScore.tentativeCount")
					.as("tentativeTeamCount").sum("companyList.allMailScore.analytical").as("analyticalTeamTotal").sum("companyList.allMailScore.analyticalCount")
					.as("analyticalTeamCount").sum("companyList.allMailScore.confident").as("confidentTeamTotal").sum("companyList.allMailScore.confidentCount")
					.as("confidentTeamCount").sum("companyList.allMailScore.fear").as("fearTeamTotal").sum("companyList.allMailScore.fearCount")
					.as("fearTeamCount")
					.sum("companyList.receiveMailScore.anger").as("angerTeamTotalrcv").sum("companyList.receiveMailScore.angerCount")
					.as("angerTeamCountrcv").sum("companyList.receiveMailScore.joy").as("joyTeamTotalrcv").sum("companyList.receiveMailScore.joyCount")
					.as("joyTeamCountrcv").sum("companyList.receiveMailScore.sadness").as("sadnessTeamTotalrcv").sum("companyList.receiveMailScore.sadnessCount")
					.as("sadnessTeamCountrcv").sum("companyList.receiveMailScore.tentative").as("tentativeTeamTotalrcv").sum("companyList.receiveMailScore.tentativeCount")
					.as("tentativeTeamCountrcv").sum("companyList.receiveMailScore.analytical").as("analyticalTeamTotalrcv").sum("companyList.receiveMailScore.analyticalCount")
					.as("analyticalTeamCountrcv").sum("companyList.receiveMailScore.confident").as("confidentTeamTotalrcv").sum("companyList.receiveMailScore.confidentCount")
					.as("confidentTeamCountrcv").sum("companyList.receiveMailScore.fear").as("fearTeamTotalrcv").sum("companyList.receiveMailScore.fearCount")
					.as("fearTeamCountrcv")
					.sum("companyList.sentMailScore.anger").as("angerTeamTotalsnt").sum("companyList.sentMailScore.angerCount")
					.as("angerTeamCountsnt").sum("companyList.sentMailScore.joy").as("joyTeamTotalsnt").sum("companyList.sentMailScore.joyCount")
					.as("joyTeamCountsnt").sum("companyList.sentMailScore.sadness").as("sadnessTeamTotalsnt").sum("companyList.sentMailScore.sadnessCount")
					.as("sadnessTeamCountsnt").sum("companyList.sentMailScore.tentative").as("tentativeTeamTotalsnt").sum("companyList.sentMailScore.tentativeCount")
					.as("tentativeTeamCountsnt").sum("companyList.sentMailScore.analytical").as("analyticalTeamTotalsnt").sum("companyList.sentMailScore.analyticalCount")
					.as("analyticalTeamCountsnt").sum("companyList.sentMailScore.confident").as("confidentTeamTotalsnt").sum("companyList.sentMailScore.confidentCount")
					.as("confidentTeamCountsnt").sum("companyList.sentMailScore.fear").as("fearTeamTotalsnt").sum("companyList.sentMailScore.fearCount")
					.as("fearTeamCountsnt")
					.sum("companyList.totalMail").as("totalTeamMail")
					.sum("companyList.totalMailRecevied").as("totalTeamMailRecevied")
					.sum("companyList.totalMailSent").as("totalTeamMailSent")
					
						);
				
				AggregationResults<AggregationPojo> groupResults 
				= mongoTemplate.aggregate(aggregation2, TeamClientInteractionBO.class, AggregationPojo.class);
				List<AggregationPojo> result = groupResults.getMappedResults();
				
			//	clientCompany Team Scores
				Double angerFinal = 0.0d;
				Double joyFinal = 0.0d;
				Double sadnessFinal = 0.0d;
				Double tentativeFinal = 0.0d;
				Double analyticalFinal = 0.0d;
				Double confidentFinal = 0.0d;
				Double fearFinal = 0.0d;
				try {
					if(result.isEmpty() != true)
					if(result.get(0).getAngerTeamCount() != 0.0d) {
						angerFinal = result.get(0).getAngerTeamTotal()/result.get(0).getAngerTeamCount(); 
					}
				}catch(Exception e) {
					System.err.println(e);
					angerFinal = 0.0d;
//					System.out.println("in company all");
				}
				try {
					if(result.isEmpty() != true)
					if(result.get(0).getJoyTeamCount() !=0.0d)
						joyFinal = result.get(0).getJoyTeamTotal()/result.get(0).getJoyTeamCount();
				}catch(Exception e) {
					System.err.println(e);
					joyFinal = 0.0d;
//					System.out.println("in company all");
				}
				try {
					if(result.isEmpty() != true)
					if(result.get(0).getSadnessTeamCount() != 0.0d)
						sadnessFinal = result.get(0).getSadnessTeamTotal()/result.get(0).getSadnessTeamCount();
				}catch(Exception e) {
					System.err.println(e);
					sadnessFinal = 0.0d;
//					System.out.println("in company all");
				}
				try {
					if(result.isEmpty() != true)
					if(result.get(0).getTentativeTeamCount() != 0.0d)
						tentativeFinal = result.get(0).getTentativeTeamTotal()/result.get(0).getTentativeTeamCount();
				}catch(Exception e) {
					System.err.println(e);
					tentativeFinal = 0.0d;
//					System.out.println("in company all");
				}
				try {
					if(result.isEmpty() != true)
					if(result.get(0).getAnalyticalTeamCount() != 0.0d)
						analyticalFinal = result.get(0).getAnalyticalTeamTotal()/result.get(0).getAnalyticalTeamCount();
				}catch(Exception e) {
					System.err.println(e);
					analyticalFinal = 0.0d;
//					System.out.println("in company all");
				}
				try {
					if(result.isEmpty() != true)
					if(result.get(0).getConfidentTeamCount() != 0.0d)
						confidentFinal = result.get(0).getConfidentTeamTotal()/result.get(0).getConfidentTeamCount();
				}catch(Exception e) {
					System.err.println(e);
					confidentFinal = 0.0d;
//					System.out.println("in company all");
				}
				try {
					if(result.isEmpty() != true)
					if(result.get(0).getFearTeamCount() != 0.0d)
						fearFinal = result.get(0).getFearTeamTotal()/result.get(0).getFearTeamCount();
				}catch(Exception e) {
					System.err.println(e);
					fearFinal = 0.0d;
//					System.out.println("in company all");
				}
				ToneOfMail toneOfMail = new ToneOfMail();
				
				Double angerP = 0.0d;
				Double joyP = 0.0d;
				Double sadnP = 0.0d;
				Double tenP = 0.0d;
				Double analP = 0.0d;
				Double confP = 0.0d;
				Double fearP = 0.0d;
				
				
				Double total = angerFinal+joyFinal+sadnessFinal+tentativeFinal+analyticalFinal+confidentFinal+fearFinal;
				if(total.isNaN() != true ) {
					angerP = (double)Math.round(((angerFinal)*100)/(total));
					joyP = (double)Math.round(((joyFinal)*100)/(total));
					sadnP = (double)Math.round(((sadnessFinal)*100)/(total));
					tenP = (double)Math.round(((tentativeFinal)*100)/(total));
					analP = (double)Math.round(((analyticalFinal)*100)/(total));
					confP = (double)Math.round(((confidentFinal)*100)/(total));
					fearP =  (double)Math.round(((fearFinal)*100)/(total));
				}
				toneOfMail.setAnger(angerP);
				toneOfMail.setJoy(joyP);
				toneOfMail.setSadness(sadnP);
				toneOfMail.setTentative(tenP);
				toneOfMail.setAnalytical(analP);
				toneOfMail.setConfident(confP);
				toneOfMail.setFear(fearP);
				
				/*
				toneOfMail.setAnger(angerFinal);
				toneOfMail.setJoy(joyFinal);
				toneOfMail.setSadness(sadnessFinal);
				toneOfMail.setTentative(tentativeFinal);
				toneOfMail.setAnalytical(analyticalFinal);
				toneOfMail.setConfident(confidentFinal);
				toneOfMail.setFear(fearFinal);*/
				
				//rcv
				
				Double angerFinal1 = 0.0d;
				Double joyFinal1 = 0.0d;
				Double sadnessFinal1 = 0.0d;
				Double tentativeFinal1 = 0.0d;
				Double analyticalFinal1 = 0.0d;
				Double confidentFinal1 = 0.0d;
				Double fearFinal1 = 0.0d;
				
				try {
					if(result.isEmpty() != true)
					if(result.get(0).getAngerTeamCountrcv() != 0)
						angerFinal1 = result.get(0).getAngerTeamTotalrcv()/result.get(0).getAngerTeamCountrcv();
				}catch(Exception e) {
					System.err.println(e);
					angerFinal1 = 0.0d;
//					System.out.println("in company rcv");
				}
				try {
					if(result.isEmpty() != true)
					if(result.get(0).getJoyTeamCountrcv() != 0)
						joyFinal1 = result.get(0).getJoyTeamTotalrcv()/result.get(0).getJoyTeamCountrcv();
				}catch(Exception e) {
					System.err.println(e);
					joyFinal1 = 0.0d;
//					System.out.println("in company rcv");
				}
				try {
					if(result.isEmpty() != true)
					if(result.get(0).getSadnessTeamCountrcv() != 0)
						sadnessFinal1 =result.get(0).getSadnessTeamTotalrcv()/result.get(0).getSadnessTeamCountrcv();
				}catch(Exception e) {
					System.err.println(e);
					sadnessFinal1 = 0.0d;
//					System.out.println("in company rcv");
				}
				try {
					if(result.isEmpty() != true)
					if(result.get(0).getTentativeTeamCountrcv() != 0)
						tentativeFinal1 = result.get(0).getTentativeTeamTotalrcv()/result.get(0).getTentativeTeamCountrcv();
				}catch(Exception e) {
					System.err.println(e);
					tentativeFinal1 = 0.0d;
					System.out.println("in company rcv");
				}
				try {
					if(result.isEmpty() != true)
					if(result.get(0).getAnalyticalTeamCountrcv() != 0)
						analyticalFinal1 = result.get(0).getAnalyticalTeamTotalrcv()/result.get(0).getAnalyticalTeamCountrcv();
				}catch(Exception e) {
					System.err.println(e);
					analyticalFinal1 = 0.0d;
//					System.out.println("in company rcv");
				}
				try {
					if(result.isEmpty() != true)
					if(result.get(0).getConfidentTeamCountrcv() != 0)
						confidentFinal1 = result.get(0).getConfidentTeamTotalrcv()/result.get(0).getConfidentTeamCountrcv();
				}catch(Exception e) {
					System.err.println(e);
					confidentFinal1 = 0.0d;
//					System.out.println("in company rcv");
				}
				try {
					if(result.isEmpty() != true)
					if(result.get(0).getFearTeamCountrcv() != 0)
						fearFinal1 =result.get(0).getFearTeamTotalrcv()/result.get(0).getFearTeamCountrcv();
					
				}catch(Exception e) {
					System.err.println(e);
					fearFinal1 = 0.0d;
//					System.out.println("in company rcv");
				}
				
				ToneOfMail toneOfMail1 = new ToneOfMail();
				
				Double angerrP = 0.0d;
				Double joyrP = 0.0d;
				Double sadrP = 0.0d;
				Double tenrP = 0.0d;
				Double analrP = 0.0d;
				Double confrP = 0.0d;
				Double fearrP = 0.0d;
				
				Double totalr = angerFinal1+joyFinal1+sadnessFinal1+tentativeFinal1+analyticalFinal1+confidentFinal1+fearFinal1;
				if(totalr.isNaN() != true) {
					angerrP = (double)Math.round(((angerFinal1)*100)/(totalr));
					joyrP = (double)Math.round(((joyFinal1)*100)/(totalr));
					sadrP = (double)Math.round(((sadnessFinal1)*100)/(totalr));
					tenrP = (double)Math.round(((tentativeFinal1)*100)/(totalr));
					analrP = (double)Math.round(((analyticalFinal1)*100)/(totalr));
					confrP = (double)Math.round(((confidentFinal1)*100)/(totalr));
					fearrP =  (double)Math.round(((fearFinal1)*100)/(totalr));
				}
				
				toneOfMail1.setAnger(angerrP);
				toneOfMail1.setJoy(joyrP);
				toneOfMail1.setSadness(sadrP);
				toneOfMail1.setTentative(tenrP);
				toneOfMail1.setAnalytical(analrP);
				toneOfMail1.setConfident(confrP);
				toneOfMail1.setFear(fearrP);
				
				
				/*toneOfMail1.setAnger(angerFinal1);
				toneOfMail1.setJoy(joyFinal1);
				toneOfMail1.setSadness(sadnessFinal1);
				toneOfMail1.setTentative(tentativeFinal1);
				toneOfMail1.setAnalytical(analyticalFinal1);
				toneOfMail1.setConfident(confidentFinal1);
				toneOfMail1.setFear(fearFinal1);*/
				
				//snt
				
				Double angerFinal2 = 0.0d;
				Double joyFinal2 = 0.0d;
				Double sadnessFinal2 = 0.0d;
				Double tentativeFinal2 = 0.0d;
				Double analyticalFinal2 = 0.0d;
				Double confidentFinal2 = 0.0d;
				Double fearFinal2 = 0.0d;
				
				try {
					if(result.isEmpty() != true)
					if(result.get(0).getAngerTeamCountsnt() != 0)
						angerFinal2 = result.get(0).getAngerTeamTotalsnt()/result.get(0).getAngerTeamCountsnt();
				}catch(Exception e) {
					System.err.println(e);
					angerFinal2 = 0.0d;
//					System.out.println("in company sent");
				}
				try {
					if(result.isEmpty() != true)
					if(result.get(0).getAngerTeamCountsnt() != 0)
						joyFinal2 = result.get(0).getJoyTeamTotalsnt()/result.get(0).getJoyTeamCountsnt();
				}catch(Exception e) {
					System.err.println(e);
					joyFinal2 = 0.0d;
//					System.out.println("in company sent");
				}
				try {
					if(result.isEmpty() != true)
					if(result.get(0).getSadnessTeamCountsnt() != 0)
						sadnessFinal2 = result.get(0).getSadnessTeamTotalsnt()/result.get(0).getSadnessTeamCountsnt();
				}catch(Exception e) {
					System.err.println(e);
					sadnessFinal2 = 0.0d;
//					System.out.println("in company sent");
				}
				try {
					if(result.isEmpty() != true)
					if(result.get(0).getTentativeTeamCountsnt() != 0)
						tentativeFinal2 = result.get(0).getTentativeTeamTotalsnt()/result.get(0).getTentativeTeamCountsnt();
				}catch(Exception e) {
					System.err.println(e);
					tentativeFinal2 = 0.0d;
//					System.out.println("in company sent");
				}
				try {
					if(result.isEmpty() != true)
					if(result.get(0).getAnalyticalTeamCountsnt() != 0)
						analyticalFinal2 = result.get(0).getAnalyticalTeamTotalsnt()/result.get(0).getAnalyticalTeamCountsnt();
				}catch(Exception e) {
					System.err.println(e);
					analyticalFinal2 = 0.0d;
//					System.out.println("in company sent");
				}
				try {
					if(result.isEmpty() != true)
					if(result.get(0).getConfidentTeamCountsnt() != 0)
						confidentFinal2 = result.get(0).getConfidentTeamTotalsnt()/result.get(0).getConfidentTeamCountsnt();
				}catch(Exception e) {
					System.err.println(e);
					confidentFinal2 = 0.0d;
//					System.out.println("in company sent");
				}
				try {
					if(result.isEmpty() != true)
					if(result.get(0).getFearTeamCountsnt() != 0)
						fearFinal2 = result.get(0).getFearTeamTotalsnt()/result.get(0).getFearTeamCountsnt();
				}catch(Exception e) {
					System.err.println(e);
					fearFinal2 = 0.0d;
//					System.out.println("in company sent");
				}
				
				ToneOfMail toneOfMail2 = new ToneOfMail();
				
				Double angersntP = 0.0d;
				Double joysntP = 0.0d;
				Double sadsntP = 0.0d;
				Double tensntP = 0.0d;
				Double analsntP = 0.0d;
				Double confsntP = 0.0d;
				Double fearsntP = 0.0d;
				
				Double totalsnt = angerFinal2+joyFinal2+sadnessFinal2+tentativeFinal2+analyticalFinal2+confidentFinal2+fearFinal2;
				if(totalsnt.isNaN() != true) {
				angersntP = (double)Math.round(((angerFinal2)*100)/(totalsnt));
				joysntP = (double)Math.round(((joyFinal2)*100)/(totalsnt));
				sadsntP = (double)Math.round(((sadnessFinal2)*100)/(totalsnt));
				tensntP = (double)Math.round(((tentativeFinal2)*100)/(totalsnt));
				analsntP = (double)Math.round(((analyticalFinal2)*100)/(totalsnt));
				confsntP = (double)Math.round(((confidentFinal2)*100)/(totalsnt));
				fearsntP =  (double)Math.round(((fearFinal2)*100)/(totalsnt));
				}
				
				toneOfMail2.setAnger(angersntP);
				toneOfMail2.setJoy(joysntP);
				toneOfMail2.setSadness(sadsntP);
				toneOfMail2.setTentative(tensntP);
				toneOfMail2.setAnalytical(analsntP);
				toneOfMail2.setConfident(confsntP);
				toneOfMail2.setFear(fearsntP);	
				
				/*
				toneOfMail2.setAnger(angerFinal2);
				toneOfMail2.setJoy(joyFinal2);
				toneOfMail2.setSadness(sadnessFinal2);
				toneOfMail2.setTentative(tentativeFinal2);
				toneOfMail2.setAnalytical(analyticalFinal2);
				toneOfMail2.setConfident(confidentFinal2);
				toneOfMail2.setFear(fearFinal2);*/
				
				
				employeePersonalDataDTO.setToneOfClientMail(toneOfMail);
				employeePersonalDataDTO.setToneOfClientReceiveMail(toneOfMail1);
				employeePersonalDataDTO.setToneOfClientsentMail(toneOfMail2);
				if(result.isEmpty() != true)
				employeePersonalDataDTO.setNoOfMail(result.get(0).getTotalTeamMail());
				else
					employeePersonalDataDTO.setNoOfMail(0l);
				if(result.isEmpty() != true)
				employeePersonalDataDTO.setNoOfReceiveMail(result.get(0).getTotalTeamMailRecevied());
				else
					employeePersonalDataDTO.setNoOfReceiveMail(0l);
				if(result.isEmpty() != true)
				employeePersonalDataDTO.setNoOfSentMail(result.get(0).getTotalTeamMailSent());
				else
					employeePersonalDataDTO.setNoOfSentMail(0l);
			
			//EmployeeRoleBO employeeRoleBO = employeeRoleRepository.findByEmployeeIdFKAndStatus(employeeBO.getEmployeeId(),"active");
			
			ProjectionOperation projectionOperation = Aggregation.project()
					.andExpression("clients").as("filterClients")
					.andExclude("_id");
			
		
			Aggregation aggregation1 = Aggregation.newAggregation(
						
						Aggregation.match(Criteria.where("employeeIdFK").is(employeeBO.getEmployeeId())),
						Aggregation.unwind("$clients"),
						Aggregation.match(Criteria.where("clients.companyFK").is(clientDataDTO.getCompanyId())),
						
						projectionOperation
						
						
						);
			List<FilterResultPojo> results = mongoTemplate.aggregate(aggregation1, ClientBO.class, FilterResultPojo.class).getMappedResults();
			
			//new sorting changes
			
			
			
			

			for(FilterResultPojo dbObject : results) {

			Aggregation aggregation = Aggregation.newAggregation(
                
				Aggregation.match(Criteria.where("employeeIdFK").is(employeeBO.getEmployeeId())),
				Aggregation.unwind("$clients"),
				Aggregation.match(Criteria.where("clients.emailId").is(dbObject.getFilterClients().getEmailId())),
				Aggregation.group("employeeIdFK")
				.sum("clients.allMailScore.anger").as("angerTeamTotal").sum("clients.allMailScore.angerCount")
				.as("angerTeamCount").sum("clients.allMailScore.joy").as("joyTeamTotal").sum("clients.allMailScore.joyCount")
				.as("joyTeamCount").sum("clients.allMailScore.sadness").as("sadnessTeamTotal").sum("clients.allMailScore.sadnessCount")
				.as("sadnessTeamCount").sum("clients.allMailScore.tentative").as("tentativeTeamTotal").sum("clients.allMailScore.tentativeCount")
				.as("tentativeTeamCount").sum("clients.allMailScore.analytical").as("analyticalTeamTotal").sum("clients.allMailScore.analyticalCount")
				.as("analyticalTeamCount").sum("clients.allMailScore.confident").as("confidentTeamTotal").sum("clients.allMailScore.confidentCount")
				.as("confidentTeamCount").sum("clients.allMailScore.fear").as("fearTeamTotal").sum("clients.allMailScore.fearCount")
				.as("fearTeamCount")
				.sum("clients.receiveMailScore.anger").as("angerTeamTotalrcv").sum("clients.receiveMailScore.angerCount")
				.as("angerTeamCountrcv").sum("clients.receiveMailScore.joy").as("joyTeamTotalrcv").sum("clients.receiveMailScore.joyCount")
				.as("joyTeamCountrcv").sum("clients.receiveMailScore.sadness").as("sadnessTeamTotalrcv").sum("clients.receiveMailScore.sadnessCount")
				.as("sadnessTeamCountrcv").sum("clients.receiveMailScore.tentative").as("tentativeTeamTotalrcv").sum("clients.receiveMailScore.tentativeCount")
				.as("tentativeTeamCountrcv").sum("clients.receiveMailScore.analytical").as("analyticalTeamTotalrcv").sum("clients.receiveMailScore.analyticalCount")
				.as("analyticalTeamCountrcv").sum("clients.receiveMailScore.confident").as("confidentTeamTotalrcv").sum("clients.receiveMailScore.confidentCount")
				.as("confidentTeamCountrcv").sum("clients.receiveMailScore.fear").as("fearTeamTotalrcv").sum("clients.receiveMailScore.fearCount")
				.as("fearTeamCountrcv")
				.sum("clients.sentMailScore.anger").as("angerTeamTotalsnt").sum("clients.sentMailScore.angerCount")
				.as("angerTeamCountsnt").sum("clients.sentMailScore.joy").as("joyTeamTotalsnt").sum("clients.sentMailScore.joyCount")
				.as("joyTeamCountsnt").sum("clients.sentMailScore.sadness").as("sadnessTeamTotalsnt").sum("clients.sentMailScore.sadnessCount")
				.as("sadnessTeamCountsnt").sum("clients.sentMailScore.tentative").as("tentativeTeamTotalsnt").sum("clients.sentMailScore.tentativeCount")
				.as("tentativeTeamCountsnt").sum("clients.sentMailScore.analytical").as("analyticalTeamTotalsnt").sum("clients.sentMailScore.analyticalCount")
				.as("analyticalTeamCountsnt").sum("clients.sentMailScore.confident").as("confidentTeamTotalsnt").sum("clients.sentMailScore.confidentCount")
				.as("confidentTeamCountsnt").sum("clients.sentMailScore.fear").as("fearTeamTotalsnt").sum("clients.sentMailScore.fearCount")
				.as("fearTeamCountsnt")
				.sum("clients.totalMail").as("totalTeamMail")
				.sum("clients.totalMailRecevied").as("totalTeamMailRecevied")
				.sum("clients.totalMailSent").as("totalTeamMailSent")
				
					);
			
			AggregationResults<AggregationPojo> groupResults1 
			= mongoTemplate.aggregate(aggregation, TeamClientInteractionBO.class, AggregationPojo.class);
			List<AggregationPojo> result1 = groupResults1.getMappedResults();
			
			// Average of All Self Scores 
			Double angerFinalCli = 0.0d;
			Double joyFinalCli = 0.0d;
			Double sadnessFinalCli = 0.0d;
			Double tentativeFinalCli = 0.0d;
			Double analyticalFinalCli = 0.0d;
			Double confidentFinalCli = 0.0d;
			Double fearFinalCli = 0.0d;
			
			try {
				if(result1.isEmpty() != true)
				if(result1.get(0).getAngerTeamCount() != 0.0d)
					angerFinalCli = result1.get(0).getAngerTeamTotal()/result1.get(0).getAngerTeamCount();
			}catch(Exception e) {
				System.err.println(e);
				angerFinalCli = 0.0d;
//				System.out.println("in client all");
			}
			try {
				if(result1.isEmpty() != true)
				if(result1.get(0).getJoyTeamCount() != 0.0d)
					joyFinalCli = result1.get(0).getJoyTeamTotal()/result1.get(0).getJoyTeamCount();
			}catch(Exception e) {
				System.err.println(e);
				joyFinalCli = 0.0d;
//				System.out.println("in client all");
			}
			try {
				if(result1.isEmpty() != true)
				if(result1.get(0).getSadnessTeamCount() != 0.0d)
					sadnessFinalCli = result1.get(0).getSadnessTeamTotal()/result1.get(0).getSadnessTeamCount();
			}catch(Exception e) {
				System.err.println(e);
				sadnessFinalCli = 0.0d;
//				System.out.println("in client all");
			}
			try {
				if(result1.isEmpty() != true)
				if(result1.get(0).getTentativeTeamCount() != 0.0d)
					tentativeFinalCli = result1.get(0).getTentativeTeamTotal()/result1.get(0).getTentativeTeamCount();
			}catch(Exception e) {
				System.err.println(e);
				tentativeFinalCli = 0.0d;
//				System.out.println("in client all");
			}
			try {
				if(result1.isEmpty() != true)
				if(result1.get(0).getAnalyticalTeamCount() != 0.0d)
					analyticalFinalCli = result1.get(0).getAnalyticalTeamTotal()/result1.get(0).getAnalyticalTeamCount();
			}catch(Exception e) {
				System.err.println(e);
				analyticalFinalCli = 0.0d;
//				System.out.println("in client all");
			}
			try {
				if(result1.isEmpty() != true)
				if(result1.get(0).getConfidentTeamCount() != 0.0d)
					confidentFinalCli = result1.get(0).getConfidentTeamTotal()/result1.get(0).getConfidentTeamCount();
				
			}catch(Exception e) {
				System.err.println(e);
				confidentFinalCli = 0.0d;
				System.out.println("in client all");
			}
			try {
				if(result1.isEmpty() != true)
				if(result1.get(0).getFearTeamCount() != 0.0d)
					fearFinalCli = result1.get(0).getFearTeamTotal()/result1.get(0).getFearTeamCount();
			}catch(Exception e) {
				System.err.println(e);
				fearFinalCli = 0.0d;
//				System.out.println("in client all");
			}
			
			ToneOfMail toneOfMailCli = new ToneOfMail();
			
			Double angerPCli = 0.0d;
			Double joyPCli = 0.0d;
			Double sadnPCli = 0.0d;
			Double tenPCli = 0.0d;
			Double analPCli = 0.0d;
			Double confPCli = 0.0d;
			Double fearPCli = 0.0d;
			
			Double totalCli = angerFinalCli+joyFinalCli+sadnessFinalCli+tentativeFinalCli+analyticalFinalCli+confidentFinalCli+fearFinalCli;
			if(totalCli.isNaN() != true ) {
				angerPCli = (double)Math.round(((angerFinalCli)*100)/(totalCli));
				joyPCli = (double)Math.round(((joyFinalCli)*100)/(totalCli));
				sadnPCli = (double)Math.round(((sadnessFinalCli)*100)/(totalCli));
				tenPCli = (double)Math.round(((tentativeFinalCli)*100)/(totalCli));
				analPCli = (double)Math.round(((analyticalFinalCli)*100)/(totalCli));
				confPCli = (double)Math.round(((confidentFinalCli)*100)/(totalCli));
				fearPCli =  (double)Math.round(((fearFinalCli)*100)/(totalCli));
			}
			
			toneOfMailCli.setAnger(angerPCli);
			toneOfMailCli.setJoy(joyPCli);
			toneOfMailCli.setSadness(sadnPCli);
			toneOfMailCli.setTentative(tenPCli);
			toneOfMailCli.setAnalytical(analPCli);
			toneOfMailCli.setConfident(confPCli);
			toneOfMailCli.setFear(fearPCli);
			
			/*toneOfMailCli.setAnger(angerFinalCli);
			toneOfMailCli.setJoy(joyFinalCli);
			toneOfMailCli.setSadness(sadnessFinalCli);
			toneOfMailCli.setTentative(tentativeFinalCli);
			toneOfMailCli.setAnalytical(analyticalFinalCli);
			toneOfMailCli.setConfident(confidentFinalCli);
			toneOfMailCli.setFear(fearFinalCli);*/
			
			//rcv
			Double angerFinal1Cli = 0.0d;
			Double joyFinal1Cli = 0.0d;
			Double sadnessFinal1Cli = 0.0d;
			Double tentativeFinal1Cli = 0.0d;
			Double analyticalFinal1Cli = 0.0d;
			Double confidentFinal1Cli = 0.0d;
			Double fearFinal1Cli = 0.0d;
			
			try {
				if(result1.isEmpty() != true)
				if(result1.get(0).getAngerTeamCountrcv() != 0)
				angerFinal1Cli = result1.get(0).getAngerTeamTotalrcv()/result1.get(0).getAngerTeamCountrcv();
			}catch(Exception e) {
				System.out.println(e);
				angerFinal1Cli = 0.0d;
				System.out.println("in client rcv");
			}
			try {
				if(result1.isEmpty() != true)
				if(result1.get(0).getJoyTeamCountrcv() != 0)
				joyFinal1Cli = result1.get(0).getJoyTeamTotalrcv()/result1.get(0).getJoyTeamCountrcv();
			}catch(Exception e) {
				System.out.println(e);
				joyFinal1Cli = 0.0d;
				System.out.println("in client rcv");
			}
			try {
				if(result1.isEmpty() != true)
				if(result1.get(0).getSadnessTeamCountrcv() != 0)
				sadnessFinal1Cli = result1.get(0).getSadnessTeamTotalrcv()/result1.get(0).getSadnessTeamCountrcv();
			}catch(Exception e) {
				System.out.println(e);
				sadnessFinal1Cli = 0.0d;
				System.out.println("in client rcv");
			}
			try {
				if(result1.isEmpty() != true)
				if(result1.get(0).getTentativeTeamCountrcv() != 0)
				tentativeFinal1Cli = result1.get(0).getTentativeTeamTotalrcv()/result1.get(0).getTentativeTeamCountrcv();
			}catch(Exception e) {
				System.out.println(e);
				tentativeFinal1Cli = 0.0d;
				System.out.println("in client rcv");
			}
			try {
				if(result1.isEmpty() != true)
				if(result1.get(0).getAnalyticalTeamCountrcv() != 0)
				analyticalFinal1Cli = result1.get(0).getAnalyticalTeamTotalrcv()/result1.get(0).getAnalyticalTeamCountrcv();
			}catch(Exception e) {
				System.out.println(e);
				analyticalFinal1Cli = 0.0d;
				System.out.println("in client rcv");
			}
			try {
				if(result1.isEmpty() != true)
				if(result1.get(0).getConfidentTeamCountrcv() != 0)
				confidentFinal1Cli = result1.get(0).getConfidentTeamTotalrcv()/result1.get(0).getConfidentTeamCountrcv();
			}catch(Exception e) {
				System.out.println(e);
				confidentFinal1Cli = 0.0d;
				System.out.println("in client rcv");
			}
			try {
				if(result1.isEmpty() != true)
				if(result1.get(0).getFearTeamCountrcv() != 0)
				fearFinal1Cli = result1.get(0).getFearTeamTotalrcv()/result1.get(0).getFearTeamCountrcv();
			}catch(Exception e) {
				System.out.println(e);
				fearFinal1Cli = 0.0d;
				System.out.println("in client rcv");
			}
			ToneOfMail toneOfMail1Cli = new ToneOfMail();
			
			Double angerPCli1 = 0.0d;
			Double joyPCli1 = 0.0d;
			Double sadnPCli1 = 0.0d;
			Double tenPCli1 = 0.0d;
			Double analPCli1 = 0.0d;
			Double confPCli1 = 0.0d;
			Double fearPCli1 = 0.0d;
			
			Double totalCli1 = angerFinal1Cli+joyFinal1Cli+sadnessFinal1Cli+tentativeFinal1Cli+analyticalFinal1Cli+confidentFinal1Cli+fearFinal1Cli;
			if(totalCli1.isNaN() != true) {
			angerPCli1 = (double)Math.round(((angerFinal1Cli)*100)/(totalCli1));
			joyPCli1 = (double)Math.round(((joyFinal1Cli)*100)/(totalCli1));
			sadnPCli1 = (double)Math.round(((sadnessFinal1Cli)*100)/(totalCli1));
			tenPCli1 = (double)Math.round(((tentativeFinal1Cli)*100)/(totalCli1));
			analPCli1 = (double)Math.round(((analyticalFinal1Cli)*100)/(totalCli1));
			confPCli1 = (double)Math.round(((confidentFinal1Cli)*100)/(totalCli1));
			fearPCli1 =  (double)Math.round(((fearFinal1Cli)*100)/(totalCli1));
			}
			
			toneOfMail1Cli.setAnger(angerPCli1);
			toneOfMail1Cli.setJoy(joyPCli1);
			toneOfMail1Cli.setSadness(sadnPCli1);
			toneOfMail1Cli.setTentative(tenPCli1);
			toneOfMail1Cli.setAnalytical(analPCli1);
			toneOfMail1Cli.setConfident(confPCli1);
			toneOfMail1Cli.setFear(fearPCli1);
			
			//snt
			
			Double angerFinal2Cli = 0.0d;
			Double joyFinal2Cli = 0.0d;
			Double sadnessFinal2Cli = 0.0d;
			Double tentativeFinal2Cli = 0.0d;
			Double analyticalFinal2Cli = 0.0d;
			Double confidentFinal2Cli = 0.0d;
			Double fearFinal2Cli = 0.0d;
			
			try {
				if(result1.isEmpty() != true)
				if(result1.get(0).getAngerTeamCountsnt() != 0)
				angerFinal2Cli = result1.get(0).getAngerTeamTotalsnt()/result1.get(0).getAngerTeamCountsnt();
			}catch(Exception e) {
				System.out.println(e);
				angerFinal2Cli = 0.0d;
				System.out.println("in client sent");
			}
			try {
				if(result1.isEmpty() != true)
				if(result1.get(0).getJoyTeamCountsnt() != 0)
				joyFinal2Cli = result1.get(0).getJoyTeamTotalsnt()/result1.get(0).getJoyTeamCountsnt();
			}catch(Exception e) {
				System.out.println(e);
				joyFinal2Cli = 0.0d;
				System.out.println("in client sent");
			}
			try {
				if(result1.isEmpty() != true)
				if(result1.get(0).getSadnessTeamCountsnt() != 0)
				sadnessFinal2Cli = result1.get(0).getSadnessTeamTotalsnt()/result1.get(0).getSadnessTeamCountsnt();
			}catch(Exception e) {
				System.out.println(e);
				sadnessFinal2Cli = 0.0d;
				System.out.println("in client sent");
			}
			try {
				if(result1.isEmpty() != true)
				if(result1.get(0).getTentativeTeamCountsnt() != 0)
				tentativeFinal2Cli = result1.get(0).getTentativeTeamTotalsnt()/result1.get(0).getTentativeTeamCountsnt();
			}catch(Exception e) {
				System.out.println(e);
				tentativeFinal2Cli = 0.0d;
				System.out.println("in client sent");
			}
			try {
				if(result1.isEmpty() != true)
				if(result1.get(0).getAnalyticalTeamCountsnt() != 0)
				analyticalFinal2Cli = result1.get(0).getAnalyticalTeamTotalsnt()/result1.get(0).getAnalyticalTeamCountsnt();
			}catch(Exception e) {
				System.out.println(e);
				analyticalFinal2Cli = 0.0d;
				System.out.println("in client sent");
			}
			try {
				if(result1.isEmpty() != true)
				if(result1.get(0).getConfidentTeamCountsnt() != 0)
				confidentFinal2Cli = result1.get(0).getConfidentTeamTotalsnt()/result1.get(0).getConfidentTeamCountsnt();
			}catch(Exception e) {
				System.out.println(e);
				confidentFinal2Cli = 0.0d;
				System.out.println("in client sent");
			}
			try {
				if(result1.isEmpty() != true)
				if(result1.get(0).getFearTeamCountsnt() != 0)
				fearFinal2Cli = result1.get(0).getFearTeamTotalsnt()/result1.get(0).getFearTeamCountsnt();
			}catch(Exception e) {
				System.out.println(e);
				fearFinal2Cli = 0.0d;
				System.out.println("in client sent");
			}
			ToneOfMail toneOfMail2Cli = new ToneOfMail();
			
			Double angerPCli2 = 0.0d;
			Double joyPCli2 = 0.0d;
			Double sadnPCli2 = 0.0d;
			Double tenPCli2 = 0.0d;
			Double analPCli2 = 0.0d;
			Double confPCli2 = 0.0d;
			Double fearPCli2 = 0.0d;

			Double totalCli2 = angerFinal2Cli+joyFinal2Cli+sadnessFinal2Cli+tentativeFinal2Cli+analyticalFinal2Cli+confidentFinal2Cli+fearFinal2Cli;
			if(totalCli2.isNaN() != true) {
			angerPCli2 = (double)Math.round(((angerFinal2Cli)*100)/(totalCli2));
			joyPCli2 = (double)Math.round(((joyFinal2Cli)*100)/(totalCli2));
			sadnPCli2 = (double)Math.round(((sadnessFinal2Cli)*100)/(totalCli2));
			tenPCli2 = (double)Math.round(((tentativeFinal2Cli)*100)/(totalCli2));
			analPCli2 = (double)Math.round(((analyticalFinal2Cli)*100)/(totalCli2));
			confPCli2 = (double)Math.round(((confidentFinal2Cli)*100)/(totalCli2));
			fearPCli2 =  (double)Math.round(((fearFinal2Cli)*100)/(totalCli2));
			}
			
			toneOfMail2Cli.setAnger(angerPCli2);
			toneOfMail2Cli.setJoy(joyPCli2);
			toneOfMail2Cli.setSadness(sadnPCli2);
			toneOfMail2Cli.setTentative(tenPCli2);
			toneOfMail2Cli.setAnalytical(analPCli2);
			toneOfMail2Cli.setConfident(confPCli2);
			toneOfMail2Cli.setFear(fearPCli2);
			
			EmployeePersonalDataDTO employeePersonalDataDTO1 = new EmployeePersonalDataDTO();
					
			employeePersonalDataDTO1.setToneOfTeamMail(toneOfMailCli);
			employeePersonalDataDTO1.setToneOfTeamReceiveMail(toneOfMail1Cli);
			employeePersonalDataDTO1.setToneOfTeamSentMail(toneOfMail2Cli);
			if(result1.isEmpty() != true)
				employeePersonalDataDTO1.setNoOfMail(result1.get(0).getTotalTeamMail());
			else
				employeePersonalDataDTO1.setNoOfMail(0l);
			if(result1.isEmpty() != true)
			employeePersonalDataDTO1.setNoOfReceiveMail(result1.get(0).getTotalTeamMailRecevied());
			else
				employeePersonalDataDTO1.setNoOfReceiveMail(0l);
			if(result1.isEmpty() != true)
			employeePersonalDataDTO1.setNoOfSentMail(result1.get(0).getTotalTeamMailSent());
			else
				employeePersonalDataDTO1.setNoOfSentMail(0l);
			employeePersonalDataDTO1.setEmployeeName(dbObject.getFilterClients().getName());
			employeePersonalDataDTO1.setEmailId(dbObject.getFilterClients().getEmailId());
			employeePersonalDataDTO1.setExecutive(dbObject.getFilterClients().getExecutive());
			employeePersonalDataDTO1.setDesignation(dbObject.getFilterClients().getDesignation());
			
			if(employeePersonalDataDTO1.getNoOfMail() != 0l)
			list.add(employeePersonalDataDTO1);
			
			}
			
			employeePersonalDataDTO.setListOfEmployee(list);
			
			
		
		return employeePersonalDataDTO;
		
	}
		
public EmployeePersonalDataDTO getClientDashBoard2(ClientDataDTO clientDataDTO){
			
			EmployeePersonalDataDTO employeePersonalDataDTO = new EmployeePersonalDataDTO();
		

		    UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
					.getContext().getAuthentication();
			
			String emailId = (String) authObj.getUserSessionInformation().get(
					EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
			
			EmployeeBO employeeBO = employeeRepository.findOne(emailId);
			
			List<EmployeePersonalDataDTO> list = new ArrayList<EmployeePersonalDataDTO>();
			
			ProjectionOperation projectionOperation1 = Aggregation.project()
					.andExpression("clients").as("teamName")
					.andExclude("_id");
			
			Aggregation aggregation2 = Aggregation.newAggregation(
	                
					Aggregation.match(Criteria.where("employeeIdFK").is(employeeBO.getEmployeeId())),
					Aggregation.unwind("$companyList"),
					Aggregation.match(Criteria.where("companyList.companyId").is(clientDataDTO.getCompanyId())),
					//projectionOperation1,
					Aggregation.group("employeeIdFK")
					.sum("companyList.allMailScore.anger").as("angerTeamTotal").sum("companyList.allMailScore.angerCount")
					.as("angerTeamCount").sum("companyList.allMailScore.joy").as("joyTeamTotal").sum("companyList.allMailScore.joyCount")
					.as("joyTeamCount").sum("companyList.allMailScore.sadness").as("sadnessTeamTotal").sum("companyList.allMailScore.sadnessCount")
					.as("sadnessTeamCount").sum("companyList.allMailScore.tentative").as("tentativeTeamTotal").sum("companyList.allMailScore.tentativeCount")
					.as("tentativeTeamCount").sum("companyList.allMailScore.analytical").as("analyticalTeamTotal").sum("companyList.allMailScore.analyticalCount")
					.as("analyticalTeamCount").sum("companyList.allMailScore.confident").as("confidentTeamTotal").sum("companyList.allMailScore.confidentCount")
					.as("confidentTeamCount").sum("companyList.allMailScore.fear").as("fearTeamTotal").sum("companyList.allMailScore.fearCount")
					.as("fearTeamCount")
					.sum("companyList.receiveMailScore.anger").as("angerTeamTotalrcv").sum("companyList.receiveMailScore.angerCount")
					.as("angerTeamCountrcv").sum("companyList.receiveMailScore.joy").as("joyTeamTotalrcv").sum("companyList.receiveMailScore.joyCount")
					.as("joyTeamCountrcv").sum("companyList.receiveMailScore.sadness").as("sadnessTeamTotalrcv").sum("companyList.receiveMailScore.sadnessCount")
					.as("sadnessTeamCountrcv").sum("companyList.receiveMailScore.tentative").as("tentativeTeamTotalrcv").sum("companyList.receiveMailScore.tentativeCount")
					.as("tentativeTeamCountrcv").sum("companyList.receiveMailScore.analytical").as("analyticalTeamTotalrcv").sum("companyList.receiveMailScore.analyticalCount")
					.as("analyticalTeamCountrcv").sum("companyList.receiveMailScore.confident").as("confidentTeamTotalrcv").sum("companyList.receiveMailScore.confidentCount")
					.as("confidentTeamCountrcv").sum("companyList.receiveMailScore.fear").as("fearTeamTotalrcv").sum("companyList.receiveMailScore.fearCount")
					.as("fearTeamCountrcv")
					.sum("companyList.sentMailScore.anger").as("angerTeamTotalsnt").sum("companyList.sentMailScore.angerCount")
					.as("angerTeamCountsnt").sum("companyList.sentMailScore.joy").as("joyTeamTotalsnt").sum("companyList.sentMailScore.joyCount")
					.as("joyTeamCountsnt").sum("companyList.sentMailScore.sadness").as("sadnessTeamTotalsnt").sum("companyList.sentMailScore.sadnessCount")
					.as("sadnessTeamCountsnt").sum("companyList.sentMailScore.tentative").as("tentativeTeamTotalsnt").sum("companyList.sentMailScore.tentativeCount")
					.as("tentativeTeamCountsnt").sum("companyList.sentMailScore.analytical").as("analyticalTeamTotalsnt").sum("companyList.sentMailScore.analyticalCount")
					.as("analyticalTeamCountsnt").sum("companyList.sentMailScore.confident").as("confidentTeamTotalsnt").sum("companyList.sentMailScore.confidentCount")
					.as("confidentTeamCountsnt").sum("companyList.sentMailScore.fear").as("fearTeamTotalsnt").sum("companyList.sentMailScore.fearCount")
					.as("fearTeamCountsnt")
					.sum("companyList.totalMail").as("totalTeamMail")
					.sum("companyList.totalMailRecevied").as("totalTeamMailRecevied")
					.sum("companyList.totalMailSent").as("totalTeamMailSent")
					
						);
				
				AggregationResults<AggregationPojo> groupResults 
				= mongoTemplate.aggregate(aggregation2, TeamClientInteractionBO.class, AggregationPojo.class);
				List<AggregationPojo> result = groupResults.getMappedResults();
				
			//	clientCompany Team Scores
				Double angerFinal = 0.0d;
				Double joyFinal = 0.0d;
				Double sadnessFinal = 0.0d;
				Double tentativeFinal = 0.0d;
				Double analyticalFinal = 0.0d;
				Double confidentFinal = 0.0d;
				Double fearFinal = 0.0d;
				if(result.get(0).getAngerTeamCount() != 0) {
				angerFinal = result.get(0).getAngerTeamTotal()/result.get(0).getAngerTeamCount(); 
				}
				if(result.get(0).getJoyTeamCount() !=0)
				joyFinal = result.get(0).getJoyTeamTotal()/result.get(0).getJoyTeamCount();
				if(result.get(0).getSadnessTeamCount() != 0)
				sadnessFinal = result.get(0).getSadnessTeamTotal()/result.get(0).getSadnessTeamCount();
				if(result.get(0).getTentativeTeamCount() != 0)
				tentativeFinal = result.get(0).getTentativeTeamTotal()/result.get(0).getTentativeTeamCount();
				if(result.get(0).getAnalyticalTeamCount() != 0)
				analyticalFinal = result.get(0).getAnalyticalTeamTotal()/result.get(0).getAnalyticalTeamCount();
				if(result.get(0).getConfidentTeamCount() != 0)
				confidentFinal = result.get(0).getConfidentTeamTotal()/result.get(0).getConfidentTeamCount();
				if(result.get(0).getFearTeamCount() != 0)
				fearFinal = result.get(0).getFearTeamTotal()/result.get(0).getFearTeamCount();
				
				ToneOfMail toneOfMail = new ToneOfMail();
				
				Double total = angerFinal+joyFinal+sadnessFinal+tentativeFinal+analyticalFinal+confidentFinal+fearFinal;
				Double angerP = (double)Math.round(((angerFinal)*100)/(total));
				Double joyP = (double)Math.round(((joyFinal)*100)/(total));
				Double sadnP = (double)Math.round(((sadnessFinal)*100)/(total));
				Double tenP = (double)Math.round(((tentativeFinal)*100)/(total));
				Double analP = (double)Math.round(((analyticalFinal)*100)/(total));
				Double confP = (double)Math.round(((confidentFinal)*100)/(total));
				Double fearP =  (double)Math.round(((fearFinal)*100)/(total));
				
				toneOfMail.setAnger(angerP);
				toneOfMail.setJoy(joyP);
				toneOfMail.setSadness(sadnP);
				toneOfMail.setTentative(tenP);
				toneOfMail.setAnalytical(analP);
				toneOfMail.setConfident(confP);
				toneOfMail.setFear(fearP);
				
				/*
				toneOfMail.setAnger(angerFinal);
				toneOfMail.setJoy(joyFinal);
				toneOfMail.setSadness(sadnessFinal);
				toneOfMail.setTentative(tentativeFinal);
				toneOfMail.setAnalytical(analyticalFinal);
				toneOfMail.setConfident(confidentFinal);
				toneOfMail.setFear(fearFinal);*/
				
				//rcv
				
				Double angerFinal1 = 0.0d;
				Double joyFinal1 = 0.0d;
				Double sadnessFinal1 = 0.0d;
				Double tentativeFinal1 = 0.0d;
				Double analyticalFinal1 = 0.0d;
				Double confidentFinal1 = 0.0d;
				Double fearFinal1 = 0.0d;
				
				if(result.get(0).getAngerTeamCountrcv() != 0)
				angerFinal1 = result.get(0).getAngerTeamTotalrcv()/result.get(0).getAngerTeamCountrcv();
				if(result.get(0).getJoyTeamCountrcv() != 0)
				joyFinal1 = result.get(0).getJoyTeamTotalrcv()/result.get(0).getJoyTeamCountrcv();
				if(result.get(0).getSadnessTeamCountrcv() != 0)
				sadnessFinal1 =result.get(0).getSadnessTeamTotalrcv()/result.get(0).getSadnessTeamCountrcv();
				if(result.get(0).getTentativeTeamCountrcv() != 0)
				tentativeFinal1 = result.get(0).getTentativeTeamTotalrcv()/result.get(0).getTentativeTeamCountrcv();
				if(result.get(0).getAnalyticalTeamCountrcv() != 0)
				analyticalFinal1 = result.get(0).getAnalyticalTeamTotalrcv()/result.get(0).getAnalyticalTeamCountrcv();
				if(result.get(0).getConfidentTeamCountrcv() != 0)
				confidentFinal1 = result.get(0).getConfidentTeamTotalrcv()/result.get(0).getConfidentTeamCountrcv();
				if(result.get(0).getFearTeamCountrcv() != 0)
				fearFinal1 =result.get(0).getFearTeamTotalrcv()/result.get(0).getFearTeamCountrcv();
				
				ToneOfMail toneOfMail1 = new ToneOfMail();
				
				Double totalr = angerFinal1+joyFinal1+sadnessFinal1+tentativeFinal1+analyticalFinal1+confidentFinal1+fearFinal1;
				Double angercP = (double)Math.round(((angerFinal1)*100)/(totalr));
				Double joyrP = (double)Math.round(((joyFinal1)*100)/(totalr));
				Double sadnrP = (double)Math.round(((sadnessFinal1)*100)/(totalr));
				Double tenrP = (double)Math.round(((tentativeFinal1)*100)/(totalr));
				Double analrP = (double)Math.round(((analyticalFinal1)*100)/(totalr));
				Double confrP = (double)Math.round(((confidentFinal1)*100)/(totalr));
				Double fearrP =  (double)Math.round(((fearFinal1)*100)/(totalr));
				
				toneOfMail1.setAnger(angercP);
				toneOfMail1.setJoy(joyrP);
				toneOfMail1.setSadness(sadnrP);
				toneOfMail1.setTentative(tenrP);
				toneOfMail1.setAnalytical(analrP);
				toneOfMail1.setConfident(confrP);
				toneOfMail1.setFear(fearrP);
				
				
				/*toneOfMail1.setAnger(angerFinal1);
				toneOfMail1.setJoy(joyFinal1);
				toneOfMail1.setSadness(sadnessFinal1);
				toneOfMail1.setTentative(tentativeFinal1);
				toneOfMail1.setAnalytical(analyticalFinal1);
				toneOfMail1.setConfident(confidentFinal1);
				toneOfMail1.setFear(fearFinal1);*/
				
				//snt
				
				Double angerFinal2 = 0.0d;
				Double joyFinal2 = 0.0d;
				Double sadnessFinal2 = 0.0d;
				Double tentativeFinal2 = 0.0d;
				Double analyticalFinal2 = 0.0d;
				Double confidentFinal2 = 0.0d;
				Double fearFinal2 = 0.0d;
				
				if(result.get(0).getAngerTeamCountsnt() != 0)
				angerFinal2 = result.get(0).getAngerTeamTotalsnt()/result.get(0).getAngerTeamCountsnt();
				if(result.get(0).getAngerTeamCountsnt() != 0)
				joyFinal2 = result.get(0).getJoyTeamTotalsnt()/result.get(0).getJoyTeamCountsnt();
				if(result.get(0).getSadnessTeamCountsnt() != 0)
				sadnessFinal2 = result.get(0).getSadnessTeamTotalsnt()/result.get(0).getSadnessTeamCountsnt();
				if(result.get(0).getTentativeTeamCountsnt() != 0)
				tentativeFinal2 = result.get(0).getTentativeTeamTotalsnt()/result.get(0).getTentativeTeamCountsnt();
				if(result.get(0).getAnalyticalTeamCountsnt() != 0)
				analyticalFinal2 = result.get(0).getAnalyticalTeamTotalsnt()/result.get(0).getAnalyticalTeamCountsnt();
				if(result.get(0).getConfidentTeamCountsnt() != 0)
				confidentFinal2 = result.get(0).getConfidentTeamTotalsnt()/result.get(0).getConfidentTeamCountsnt();
				if(result.get(0).getFearTeamCountsnt() != 0)
				fearFinal2 = result.get(0).getFearTeamTotalsnt()/result.get(0).getFearTeamCountsnt();
				
				ToneOfMail toneOfMail2 = new ToneOfMail();
				
				Double totalsnt = angerFinal2+joyFinal2+sadnessFinal2+tentativeFinal2+analyticalFinal2+confidentFinal2+fearFinal2;
				Double angersntP = (double)Math.round(((angerFinal2)*100)/(totalsnt));
				Double joyrsntP = (double)Math.round(((joyFinal2)*100)/(totalsnt));
				Double sadnrsntP = (double)Math.round(((sadnessFinal2)*100)/(totalsnt));
				Double tenrsntP = (double)Math.round(((tentativeFinal2)*100)/(totalsnt));
				Double analrsntP = (double)Math.round(((analyticalFinal2)*100)/(totalsnt));
				Double confrsntP = (double)Math.round(((confidentFinal2)*100)/(totalsnt));
				Double fearrsntP =  (double)Math.round(((fearFinal2)*100)/(totalsnt));
				
				toneOfMail2.setAnger(angersntP);
				toneOfMail2.setJoy(joyrsntP);
				toneOfMail2.setSadness(sadnrsntP);
				toneOfMail2.setTentative(tenrsntP);
				toneOfMail2.setAnalytical(analrsntP);
				toneOfMail2.setConfident(confrsntP);
				toneOfMail2.setFear(fearrsntP);	
				
				/*
				toneOfMail2.setAnger(angerFinal2);
				toneOfMail2.setJoy(joyFinal2);
				toneOfMail2.setSadness(sadnessFinal2);
				toneOfMail2.setTentative(tentativeFinal2);
				toneOfMail2.setAnalytical(analyticalFinal2);
				toneOfMail2.setConfident(confidentFinal2);
				toneOfMail2.setFear(fearFinal2);*/
				
				
				employeePersonalDataDTO.setToneOfClientMail(toneOfMail);
				employeePersonalDataDTO.setToneOfClientReceiveMail(toneOfMail1);
				employeePersonalDataDTO.setToneOfClientsentMail(toneOfMail2);
				employeePersonalDataDTO.setNoOfMail(result.get(0).getTotalTeamMail());
				employeePersonalDataDTO.setNoOfReceiveMail(result.get(0).getTotalTeamMailRecevied());
				employeePersonalDataDTO.setNoOfSentMail(result.get(0).getTotalTeamMailSent());
			
			//EmployeeRoleBO employeeRoleBO = employeeRoleRepository.findByEmployeeIdFKAndStatus(employeeBO.getEmployeeId(),"active");
			
			ProjectionOperation projectionOperation = Aggregation.project()
					.andExpression("clients").as("filterClients")
					.andExclude("_id");
			
		
			Aggregation aggregation1 = Aggregation.newAggregation(
						
						Aggregation.match(Criteria.where("employeeIdFK").is(employeeBO.getEmployeeId())),
						Aggregation.unwind("$clients"),
						Aggregation.match(Criteria.where("clients.companyFK").is(clientDataDTO.getCompanyId())),
						
						projectionOperation
						
						
						);
			List<FilterResultPojo> results = mongoTemplate.aggregate(aggregation1, ClientBO.class, FilterResultPojo.class).getMappedResults();
			
			//new sorting changes
			
			
			
			

			for(FilterResultPojo dbObject : results) {

			Aggregation aggregation = Aggregation.newAggregation(
                
				Aggregation.match(Criteria.where("employeeIdFK").is(employeeBO.getEmployeeId())),
				Aggregation.unwind("$clients"),
				Aggregation.match(Criteria.where("clients.emailId").is(dbObject.getFilterClients().getEmailId())),
				Aggregation.group("employeeIdFK")
				.sum("clients.allMailScore.anger").as("angerTeamTotal").sum("clients.allMailScore.angerCount")
				.as("angerTeamCount").sum("clients.allMailScore.joy").as("joyTeamTotal").sum("clients.allMailScore.joyCount")
				.as("joyTeamCount").sum("clients.allMailScore.sadness").as("sadnessTeamTotal").sum("clients.allMailScore.sadnessCount")
				.as("sadnessTeamCount").sum("clients.allMailScore.tentative").as("tentativeTeamTotal").sum("clients.allMailScore.tentativeCount")
				.as("tentativeTeamCount").sum("clients.allMailScore.analytical").as("analyticalTeamTotal").sum("clients.allMailScore.analyticalCount")
				.as("analyticalTeamCount").sum("clients.allMailScore.confident").as("confidentTeamTotal").sum("clients.allMailScore.confidentCount")
				.as("confidentTeamCount").sum("clients.allMailScore.fear").as("fearTeamTotal").sum("clients.allMailScore.fearCount")
				.as("fearTeamCount")
				.sum("clients.receiveMailScore.anger").as("angerTeamTotalrcv").sum("clients.receiveMailScore.angerCount")
				.as("angerTeamCountrcv").sum("clients.receiveMailScore.joy").as("joyTeamTotalrcv").sum("clients.receiveMailScore.joyCount")
				.as("joyTeamCountrcv").sum("clients.receiveMailScore.sadness").as("sadnessTeamTotalrcv").sum("clients.receiveMailScore.sadnessCount")
				.as("sadnessTeamCountrcv").sum("clients.receiveMailScore.tentative").as("tentativeTeamTotalrcv").sum("clients.receiveMailScore.tentativeCount")
				.as("tentativeTeamCountrcv").sum("clients.receiveMailScore.analytical").as("analyticalTeamTotalrcv").sum("clients.receiveMailScore.analyticalCount")
				.as("analyticalTeamCountrcv").sum("clients.receiveMailScore.confident").as("confidentTeamTotalrcv").sum("clients.receiveMailScore.confidentCount")
				.as("confidentTeamCountrcv").sum("clients.receiveMailScore.fear").as("fearTeamTotalrcv").sum("clients.receiveMailScore.fearCount")
				.as("fearTeamCountrcv")
				.sum("clients.sentMailScore.anger").as("angerTeamTotalsnt").sum("clients.sentMailScore.angerCount")
				.as("angerTeamCountsnt").sum("clients.sentMailScore.joy").as("joyTeamTotalsnt").sum("clients.sentMailScore.joyCount")
				.as("joyTeamCountsnt").sum("clients.sentMailScore.sadness").as("sadnessTeamTotalsnt").sum("clients.sentMailScore.sadnessCount")
				.as("sadnessTeamCountsnt").sum("clients.sentMailScore.tentative").as("tentativeTeamTotalsnt").sum("clients.sentMailScore.tentativeCount")
				.as("tentativeTeamCountsnt").sum("clients.sentMailScore.analytical").as("analyticalTeamTotalsnt").sum("clients.sentMailScore.analyticalCount")
				.as("analyticalTeamCountsnt").sum("clients.sentMailScore.confident").as("confidentTeamTotalsnt").sum("clients.sentMailScore.confidentCount")
				.as("confidentTeamCountsnt").sum("clients.sentMailScore.fear").as("fearTeamTotalsnt").sum("clients.sentMailScore.fearCount")
				.as("fearTeamCountsnt")
				.sum("clients.totalMail").as("totalTeamMail")
				.sum("clients.totalMailRecevied").as("totalTeamMailRecevied")
				.sum("clients.totalMailSent").as("totalTeamMailSent")
				
					);
			
			AggregationResults<AggregationPojo> groupResults1 
			= mongoTemplate.aggregate(aggregation, TeamClientInteractionBO.class, AggregationPojo.class);
			List<AggregationPojo> result1 = groupResults1.getMappedResults();
			
			// Average of All Self Scores 
			Double angerFinalCli = 0.0d;
			Double joyFinalCli = 0.0d;
			Double sadnessFinalCli = 0.0d;
			Double tentativeFinalCli = 0.0d;
			Double analyticalFinalCli = 0.0d;
			Double confidentFinalCli = 0.0d;
			Double fearFinalCli = 0.0d;
			
			if(result1.get(0).getAngerTeamCount() != 0)
			angerFinalCli = result1.get(0).getAngerTeamTotal()/result1.get(0).getAngerTeamCount();
			if(result1.get(0).getJoyTeamCount() != 0)
			joyFinalCli = result1.get(0).getJoyTeamTotal()/result1.get(0).getJoyTeamCount();
			if(result1.get(0).getSadnessTeamCount() != 0)
			sadnessFinalCli = result1.get(0).getSadnessTeamTotal()/result1.get(0).getSadnessTeamCount();
			if(result1.get(0).getTentativeTeamCount() != 0)
			tentativeFinalCli = result1.get(0).getTentativeTeamTotal()/result1.get(0).getTentativeTeamCount();
			if(result1.get(0).getAnalyticalTeamCount() != 0)
			analyticalFinalCli = result1.get(0).getAnalyticalTeamTotal()/result1.get(0).getAnalyticalTeamCount();
			if(result1.get(0).getConfidentTeamCount() != 0)
			confidentFinalCli = result1.get(0).getConfidentTeamTotal()/result1.get(0).getConfidentTeamCount();
			if(result1.get(0).getFearTeamCount() != 0)
			fearFinalCli = result1.get(0).getFearTeamTotal()/result1.get(0).getFearTeamCount();
			
			ToneOfMail toneOfMailCli = new ToneOfMail();
			
			Double totalCli = angerFinalCli+joyFinalCli+sadnessFinalCli+tentativeFinalCli+analyticalFinalCli+confidentFinalCli+fearFinalCli;
			Double angerPCli = (double)Math.round(((angerFinalCli)*100)/(totalCli));
			Double joyPCli = (double)Math.round(((joyFinalCli)*100)/(totalCli));
			Double sadnPCli = (double)Math.round(((sadnessFinalCli)*100)/(totalCli));
			Double tenPCli = (double)Math.round(((tentativeFinalCli)*100)/(totalCli));
			Double analPCli = (double)Math.round(((analyticalFinalCli)*100)/(totalCli));
			Double confPCli = (double)Math.round(((confidentFinalCli)*100)/(totalCli));
			Double fearPCli =  (double)Math.round(((fearFinalCli)*100)/(totalCli));
			
			toneOfMailCli.setAnger(angerPCli);
			toneOfMailCli.setJoy(joyPCli);
			toneOfMailCli.setSadness(sadnPCli);
			toneOfMailCli.setTentative(tenPCli);
			toneOfMailCli.setAnalytical(analPCli);
			toneOfMailCli.setConfident(confPCli);
			toneOfMailCli.setFear(fearPCli);
			
			/*toneOfMailCli.setAnger(angerFinalCli);
			toneOfMailCli.setJoy(joyFinalCli);
			toneOfMailCli.setSadness(sadnessFinalCli);
			toneOfMailCli.setTentative(tentativeFinalCli);
			toneOfMailCli.setAnalytical(analyticalFinalCli);
			toneOfMailCli.setConfident(confidentFinalCli);
			toneOfMailCli.setFear(fearFinalCli);*/
			
			//rcv
			
			Double angerFinal1Cli = result1.get(0).getAngerTeamTotalrcv()/result1.get(0).getAngerTeamCountrcv();
			Double joyFinal1Cli = result1.get(0).getJoyTeamTotalrcv()/result1.get(0).getJoyTeamCountrcv();
			Double sadnessFinal1Cli = result1.get(0).getSadnessTeamTotalrcv()/result1.get(0).getSadnessTeamCountrcv();
			Double tentativeFinal1Cli = result1.get(0).getTentativeTeamTotalrcv()/result1.get(0).getTentativeTeamCountrcv();
			Double analyticalFinal1Cli = result1.get(0).getAnalyticalTeamTotalrcv()/result1.get(0).getAnalyticalTeamCountrcv();
			Double confidentFinal1Cli = result1.get(0).getConfidentTeamTotalrcv()/result1.get(0).getConfidentTeamCountrcv();
			Double fearFinal1Cli = result1.get(0).getFearTeamTotalrcv()/result1.get(0).getFearTeamCountrcv();
			
			ToneOfMail toneOfMail1Cli = new ToneOfMail();
			

			Double totalCli1 = angerFinal1Cli+joyFinal1Cli+sadnessFinal1Cli+tentativeFinal1Cli+analyticalFinal1Cli+confidentFinal1Cli+fearFinal1Cli;
			Double angerPCli1 = (double)Math.round(((angerFinal1Cli)*100)/(totalCli1));
			Double joyPCli1 = (double)Math.round(((joyFinal1Cli)*100)/(totalCli1));
			Double sadnPCli1 = (double)Math.round(((sadnessFinal1Cli)*100)/(totalCli1));
			Double tenPCli1 = (double)Math.round(((tentativeFinal1Cli)*100)/(totalCli1));
			Double analPCli1 = (double)Math.round(((analyticalFinal1Cli)*100)/(totalCli1));
			Double confPCli1 = (double)Math.round(((confidentFinal1Cli)*100)/(totalCli1));
			Double fearPCli1 =  (double)Math.round(((fearFinal1Cli)*100)/(totalCli1));
			
			
			toneOfMail1Cli.setAnger(angerPCli1);
			toneOfMail1Cli.setJoy(joyPCli1);
			toneOfMail1Cli.setSadness(sadnPCli1);
			toneOfMail1Cli.setTentative(tenPCli1);
			toneOfMail1Cli.setAnalytical(analPCli1);
			toneOfMail1Cli.setConfident(confPCli1);
			toneOfMail1Cli.setFear(fearPCli1);
			
			//snt
			
			Double angerFinal2Cli = result1.get(0).getAngerTeamTotalsnt()/result1.get(0).getAngerTeamCountsnt();
			Double joyFinal2Cli = result1.get(0).getJoyTeamTotalsnt()/result1.get(0).getJoyTeamCountsnt();
			Double sadnessFinal2Cli = result1.get(0).getSadnessTeamTotalsnt()/result1.get(0).getSadnessTeamCountsnt();
			Double tentativeFinal2Cli = result1.get(0).getTentativeTeamTotalsnt()/result1.get(0).getTentativeTeamCountsnt();
			Double analyticalFinal2Cli = result1.get(0).getAnalyticalTeamTotalsnt()/result1.get(0).getAnalyticalTeamCountsnt();
			Double confidentFinal2Cli = result1.get(0).getConfidentTeamTotalsnt()/result1.get(0).getConfidentTeamCountsnt();
			Double fearFinal2Cli = result1.get(0).getFearTeamTotalsnt()/result1.get(0).getFearTeamCountsnt();
			
			ToneOfMail toneOfMail2Cli = new ToneOfMail();
			

			Double totalCli2 = angerFinal2Cli+joyFinal2Cli+sadnessFinal2Cli+tentativeFinal2Cli+analyticalFinal2Cli+confidentFinal2Cli+fearFinal2Cli;
			Double angerPCli2 = (double)Math.round(((angerFinal2Cli)*100)/(totalCli2));
			Double joyPCli2 = (double)Math.round(((joyFinal2Cli)*100)/(totalCli2));
			Double sadnPCli2 = (double)Math.round(((sadnessFinal2Cli)*100)/(totalCli2));
			Double tenPCli2 = (double)Math.round(((tentativeFinal2Cli)*100)/(totalCli2));
			Double analPCli2 = (double)Math.round(((analyticalFinal2Cli)*100)/(totalCli2));
			Double confPCli2 = (double)Math.round(((confidentFinal2Cli)*100)/(totalCli2));
			Double fearPCli2 =  (double)Math.round(((fearFinal2Cli)*100)/(totalCli2));
			
			
			toneOfMail2Cli.setAnger(angerPCli2);
			toneOfMail2Cli.setJoy(joyPCli2);
			toneOfMail2Cli.setSadness(sadnPCli2);
			toneOfMail2Cli.setTentative(tenPCli2);
			toneOfMail2Cli.setAnalytical(analPCli2);
			toneOfMail2Cli.setConfident(confPCli2);
			toneOfMail2Cli.setFear(fearPCli2);
			
			EmployeePersonalDataDTO employeePersonalDataDTO1 = new EmployeePersonalDataDTO();
					
			employeePersonalDataDTO1.setToneOfTeamMail(toneOfMailCli);
			employeePersonalDataDTO1.setToneOfTeamReceiveMail(toneOfMail1Cli);
			employeePersonalDataDTO1.setToneOfTeamSentMail(toneOfMail2Cli);
			employeePersonalDataDTO1.setNoOfMail(result1.get(0).getTotalTeamMail());
			employeePersonalDataDTO1.setNoOfReceiveMail(result1.get(0).getTotalTeamMailRecevied());
			employeePersonalDataDTO1.setNoOfSentMail(result1.get(0).getTotalTeamMailSent());
			
			employeePersonalDataDTO1.setEmployeeName(dbObject.getFilterClients().getName());
			employeePersonalDataDTO1.setEmailId(dbObject.getFilterClients().getEmailId());
			
			employeePersonalDataDTO1.setDesignation(dbObject.getFilterClients().getDesignation());
			
			list.add(employeePersonalDataDTO1);
			
			}
			
			employeePersonalDataDTO.setListOfEmployee(list);
			
			
		
		return employeePersonalDataDTO;
		
	}
		
		
		
		// filters on Client Screen by Name typeahead
		
		
		
		public List<FilterByCriteria> filterOnClientName(String key,String empData) {
			
			
		    
		    UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
					.getContext().getAuthentication();
			
			String emailId = (String) authObj.getUserSessionInformation().get(
					EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
			
			EmployeeBO employeeBO = employeeRepository.findOne(emailId);
			
			List<FilterByCriteria> filterByCriterias = new ArrayList<FilterByCriteria>();
			
			//EmployeeRoleBO employeeRoleBO = employeeRoleRepository.findByEmployeeIdFKAndStatus(employeeBO.getEmployeeId(),"active");
			
			ProjectionOperation projectionOperation = Aggregation.project()
					.andExpression("clients").as("filterClients")
					.andExclude("_id");
			
			Aggregation aggregation = null;
			
			if (key.equalsIgnoreCase("clientName")) {
				
			
			
			 aggregation = Aggregation.newAggregation(
					
					
					
					Aggregation.match(Criteria.where("employeeIdFK").is(employeeBO.getEmployeeId())),
					Aggregation.unwind("$clients"),
					Aggregation.match(Criteria.where("clients.name").regex(empData,"i")),
					//Aggregation.project("employeeHierarchy")
					projectionOperation
					
					
					);
			}
			
			if (key.equalsIgnoreCase("emailId")) {
				
				
				
				 aggregation = Aggregation.newAggregation(
						
						
						
						Aggregation.match(Criteria.where("employeeIdFK").is(employeeBO.getEmployeeId())),
						Aggregation.unwind("$clients"),
						Aggregation.match(Criteria.where("clients.emailId").regex(empData,"i")),
						projectionOperation
						
						
						);
				}
			
			if (key.equalsIgnoreCase("designation")) {
				
				
				
				 aggregation = Aggregation.newAggregation(
						
						
						
						Aggregation.match(Criteria.where("employeeIdFK").is(employeeBO.getEmployeeId())),
						Aggregation.unwind("$clients"),
						Aggregation.match(Criteria.where("clients.designation").regex(empData,"i")),
						
						//Aggregation.project("employeeHierarchy")
						projectionOperation
						
						
						);
				}
			
			List<FilterResultPojo> results = mongoTemplate.aggregate(aggregation, ClientBO.class, FilterResultPojo.class).getMappedResults();
			
			for(FilterResultPojo dbObject : results) {
				
				FilterByCriteria filterByCriteria = new FilterByCriteria();
				filterByCriteria.setClientName(dbObject.getFilterClients().getName());
				filterByCriteria.setCompanyFK(dbObject.getFilterClients().getCompanyFK());
				filterByCriteria.setEmailId(dbObject.getFilterClients().getEmailId());
				filterByCriteria.setDesignation(dbObject.getFilterClients().getDesignation());
				filterByCriterias.add(filterByCriteria);
				
				
			}
					
			
		  /*  
			Query query = new Query();
			query.addCriteria(new Criteria().andOperator(Criteria.where("reportToId").is(employeeBO.getEmployeeId()),
					Criteria.where("roles").elemMatch(Criteria.where("designation").in(empData.getDesignation())),
					Criteria.where("roles").elemMatch(Criteria.where("status").is("active"))));
			
			
		List<EmployeeBO> employeeBOs =	mongoTemplate.find(query,EmployeeBO.class);*/
			
			
			return filterByCriterias;
		}
		
		
		
		// organisation name typeahead
		
		public List<OrganisationBO> typeAheadWithOrgName(String empData) {
		
			
			List<OrganisationBO> filterByCriterias = new ArrayList<OrganisationBO>();
			
			filterByCriterias =  companyRepository.findByCompanyNameRegex(empData);
			
			return filterByCriterias;
		}
		
		
		// filters on Client Screen by Name typeahead
		
		
		
				public List<FilterByCriteria> filterOnSubClientName(String key,String empData,String cmpId) {
					
					
				    
				    UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
							.getContext().getAuthentication();
					
					String emailId = (String) authObj.getUserSessionInformation().get(
							EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
					
					EmployeeBO employeeBO = employeeRepository.findOne(emailId);
					
					List<FilterByCriteria> filterByCriterias = new ArrayList<FilterByCriteria>();
					
					//EmployeeRoleBO employeeRoleBO = employeeRoleRepository.findByEmployeeIdFKAndStatus(employeeBO.getEmployeeId(),"active");
					
					ProjectionOperation projectionOperation = Aggregation.project()
							.andExpression("clients").as("filterClients")
							.andExclude("_id");
					
					Aggregation aggregation = null;
					
					if (key.equalsIgnoreCase("clientName")) {
						
					
					
					 aggregation = Aggregation.newAggregation(
							
							
							
							Aggregation.match(Criteria.where("employeeIdFK").is(employeeBO.getEmployeeId())),
							Aggregation.unwind("$clients"),
							//Aggregation.match(Criteria.where("clients.companyFK").is(cmpId).and("clients.name").regex(empData,"i")),
							//Aggregation.project("employeeHierarchy")
							Aggregation.match(Criteria.where("clients.name").regex(empData,"i")),
							projectionOperation
							
							
							);
					}
					
				
					
					
					List<FilterResultPojo> results = mongoTemplate.aggregate(aggregation, ClientBO.class, FilterResultPojo.class).getMappedResults();
					
					for(FilterResultPojo dbObject : results) {
						
						FilterByCriteria filterByCriteria = new FilterByCriteria();
						filterByCriteria.setClientName(dbObject.getFilterClients().getName());
						filterByCriteria.setCompanyFK(dbObject.getFilterClients().getCompanyFK());
						filterByCriteria.setEmailId(dbObject.getFilterClients().getEmailId());
						filterByCriteria.setDesignation(dbObject.getFilterClients().getDesignation());
						filterByCriterias.add(filterByCriteria);
						
						
					}
							
					
				  /*  
					Query query = new Query();
					query.addCriteria(new Criteria().andOperator(Criteria.where("reportToId").is(employeeBO.getEmployeeId()),
							Criteria.where("roles").elemMatch(Criteria.where("designation").in(empData.getDesignation())),
							Criteria.where("roles").elemMatch(Criteria.where("status").is("active"))));
					
					
				List<EmployeeBO> employeeBOs =	mongoTemplate.find(query,EmployeeBO.class);*/
					
					
					return filterByCriterias;
				}	
		
		
		
		
		
		
		// client search Results
		
	public EmployeePersonalDataDTO getClientSearchResult(EmployeePersonalDataDTO clientDataDTO){
			
			EmployeePersonalDataDTO employeePersonalDataDTO = new EmployeePersonalDataDTO();
		

		    UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
					.getContext().getAuthentication();
			
			String emailId = (String) authObj.getUserSessionInformation().get(
					EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
			
			EmployeeBO employeeBO = employeeRepository.findOne(emailId);
			
			try {
			Aggregation aggregation2 = Aggregation.newAggregation(
	                
					Aggregation.match(Criteria.where("employeeIdFK").is(employeeBO.getEmployeeId())),
					Aggregation.unwind("$companyList"),
					Aggregation.match(Criteria.where("companyList.companyId").is(clientDataDTO.getCompanyId())),
					//projectionOperation1,
					Aggregation.group("employeeIdFK")
					.sum("companyList.allMailScore.anger").as("angerTeamTotal").sum("companyList.allMailScore.angerCount")
					.as("angerTeamCount").sum("companyList.allMailScore.joy").as("joyTeamTotal").sum("companyList.allMailScore.joyCount")
					.as("joyTeamCount").sum("companyList.allMailScore.sadness").as("sadnessTeamTotal").sum("companyList.allMailScore.sadnessCount")
					.as("sadnessTeamCount").sum("companyList.allMailScore.tentative").as("tentativeTeamTotal").sum("companyList.allMailScore.tentativeCount")
					.as("tentativeTeamCount").sum("companyList.allMailScore.analytical").as("analyticalTeamTotal").sum("companyList.allMailScore.analyticalCount")
					.as("analyticalTeamCount").sum("companyList.allMailScore.confident").as("confidentTeamTotal").sum("companyList.allMailScore.confidentCount")
					.as("confidentTeamCount").sum("companyList.allMailScore.fear").as("fearTeamTotal").sum("companyList.allMailScore.fearCount")
					.as("fearTeamCount")
					.sum("companyList.receiveMailScore.anger").as("angerTeamTotalrcv").sum("companyList.receiveMailScore.angerCount")
					.as("angerTeamCountrcv").sum("companyList.receiveMailScore.joy").as("joyTeamTotalrcv").sum("companyList.receiveMailScore.joyCount")
					.as("joyTeamCountrcv").sum("companyList.receiveMailScore.sadness").as("sadnessTeamTotalrcv").sum("companyList.receiveMailScore.sadnessCount")
					.as("sadnessTeamCountrcv").sum("companyList.receiveMailScore.tentative").as("tentativeTeamTotalrcv").sum("companyList.receiveMailScore.tentativeCount")
					.as("tentativeTeamCountrcv").sum("companyList.receiveMailScore.analytical").as("analyticalTeamTotalrcv").sum("companyList.receiveMailScore.analyticalCount")
					.as("analyticalTeamCountrcv").sum("companyList.receiveMailScore.confident").as("confidentTeamTotalrcv").sum("companyList.receiveMailScore.confidentCount")
					.as("confidentTeamCountrcv").sum("companyList.receiveMailScore.fear").as("fearTeamTotalrcv").sum("companyList.receiveMailScore.fearCount")
					.as("fearTeamCountrcv")
					.sum("companyList.sentMailScore.anger").as("angerTeamTotalsnt").sum("companyList.sentMailScore.angerCount")
					.as("angerTeamCountsnt").sum("companyList.sentMailScore.joy").as("joyTeamTotalsnt").sum("companyList.sentMailScore.joyCount")
					.as("joyTeamCountsnt").sum("companyList.sentMailScore.sadness").as("sadnessTeamTotalsnt").sum("companyList.sentMailScore.sadnessCount")
					.as("sadnessTeamCountsnt").sum("companyList.sentMailScore.tentative").as("tentativeTeamTotalsnt").sum("companyList.sentMailScore.tentativeCount")
					.as("tentativeTeamCountsnt").sum("companyList.sentMailScore.analytical").as("analyticalTeamTotalsnt").sum("companyList.sentMailScore.analyticalCount")
					.as("analyticalTeamCountsnt").sum("companyList.sentMailScore.confident").as("confidentTeamTotalsnt").sum("companyList.sentMailScore.confidentCount")
					.as("confidentTeamCountsnt").sum("companyList.sentMailScore.fear").as("fearTeamTotalsnt").sum("companyList.sentMailScore.fearCount")
					.as("fearTeamCountsnt")
					.sum("companyList.totalMail").as("totalTeamMail")
					.sum("companyList.totalMailRecevied").as("totalTeamMailRecevied")
					.sum("companyList.totalMailSent").as("totalTeamMailSent")
					
						);
				
				AggregationResults<AggregationPojo> groupResults 
				= mongoTemplate.aggregate(aggregation2, TeamClientInteractionBO.class, AggregationPojo.class);
				List<AggregationPojo> result = groupResults.getMappedResults();
				
			//	clientCompany Team Scores
				
				Double angerFinal = 0.0d;
				Double joyFinal = 0.0d;
				Double sadnessFinal = 0.0d;
				Double tentativeFinal = 0.0d;
				Double analyticalFinal = 0.0d;
				Double confidentFinal = 0.0d;
				Double fearFinal = 0.0d;
				
				
				try {
				angerFinal = result.get(0).getAngerTeamTotal()/result.get(0).getAngerTeamCount();
				}catch(Exception e) {
					
				}
				try {
				joyFinal = result.get(0).getJoyTeamTotal()/result.get(0).getJoyTeamCount();
				}catch(Exception e) {
					
				}
				try {
				sadnessFinal = result.get(0).getSadnessTeamTotal()/result.get(0).getSadnessTeamCount();
				}catch(Exception e) {
					
				}
				try {
				tentativeFinal = result.get(0).getTentativeTeamTotal()/result.get(0).getTentativeTeamCount();
				}catch(Exception e) {
					
				}
				try {
			    analyticalFinal = result.get(0).getAnalyticalTeamTotal()/result.get(0).getAnalyticalTeamCount();
				}catch(Exception e) {
					
				}
				try {
				confidentFinal = result.get(0).getConfidentTeamTotal()/result.get(0).getConfidentTeamCount();
				}catch(Exception e) {
					
				}
				try {
				fearFinal = result.get(0).getFearTeamTotal()/result.get(0).getFearTeamCount();
				}catch(Exception e) {
					
				}
				
				ToneOfMail toneOfMail = new ToneOfMail();
				
				Double total = angerFinal+joyFinal+sadnessFinal+tentativeFinal+analyticalFinal+confidentFinal+fearFinal;
				
				Double angerP = 0.0d;
				Double joyP = 0.0d;
				Double sadnP = 0.0d;
				Double tenP = 0.0d;
				Double analP = 0.0d;
				Double confP = 0.0d;
				Double fearP = 0.0d;
				
				if(total != 0) {
				angerP = (double)Math.round(((angerFinal)*100)/(total));
				joyP = (double)Math.round(((joyFinal)*100)/(total));
				sadnP = (double)Math.round(((sadnessFinal)*100)/(total));
				tenP = (double)Math.round(((tentativeFinal)*100)/(total));
				analP = (double)Math.round(((analyticalFinal)*100)/(total));
				confP = (double)Math.round(((confidentFinal)*100)/(total));
				fearP =  (double)Math.round(((fearFinal)*100)/(total));
				}
				toneOfMail.setAnger(angerP);
				toneOfMail.setJoy(joyP);
				toneOfMail.setSadness(sadnP);
				toneOfMail.setTentative(tenP);
				toneOfMail.setAnalytical(analP);
				toneOfMail.setConfident(confP);
				toneOfMail.setFear(fearP);
				
				/*
				toneOfMail.setAnger(angerFinal);
				toneOfMail.setJoy(joyFinal);
				toneOfMail.setSadness(sadnessFinal);
				toneOfMail.setTentative(tentativeFinal);
				toneOfMail.setAnalytical(analyticalFinal);
				toneOfMail.setConfident(confidentFinal);
				toneOfMail.setFear(fearFinal);*/
				
				//rcv
				
				Double angerFinal1 = 0.0d;
				Double joyFinal1 = 0.0d;
				Double sadnessFinal1 = 0.0d;
				Double tentativeFinal1 = 0.0d;
				Double analyticalFinal1 = 0.0d;
				Double confidentFinal1 = 0.0d;
				Double fearFinal1 = 0.0d;
				
				try {
				angerFinal1 = result.get(0).getAngerTeamTotalrcv()/result.get(0).getAngerTeamCountrcv();
				}catch(Exception e) {
					
				}
				try {
				joyFinal1 = result.get(0).getJoyTeamTotalrcv()/result.get(0).getJoyTeamCountrcv();
				}catch(Exception e) {
					
				}
				try {
				sadnessFinal1 =result.get(0).getSadnessTeamTotalrcv()/result.get(0).getSadnessTeamCountrcv();
				}catch(Exception e) {
					
				}
				try {
				tentativeFinal1 = result.get(0).getTentativeTeamTotalrcv()/result.get(0).getTentativeTeamCountrcv();
				}catch(Exception e) {
					
				}
				try {
				analyticalFinal1 = result.get(0).getAnalyticalTeamTotalrcv()/result.get(0).getAnalyticalTeamCountrcv();
				}catch(Exception e) {
					
				}
				try {
				confidentFinal1 = result.get(0).getConfidentTeamTotalrcv()/result.get(0).getConfidentTeamCountrcv();
				}catch(Exception e) {
					
				}
				try {
				fearFinal1 =result.get(0).getFearTeamTotalrcv()/result.get(0).getFearTeamCountrcv();
				}catch(Exception e) {
					
				}
				
				ToneOfMail toneOfMail1 = new ToneOfMail();
				
				Double totalr = angerFinal1+joyFinal1+sadnessFinal1+tentativeFinal1+analyticalFinal1+confidentFinal1+fearFinal1;
				
				Double angercP = 0.0d;
				Double joyrP = 0.0d;
				Double sadnrP = 0.0d;
				Double tenrP = 0.0d;
				Double analrP = 0.0d;
				Double confrP = 0.0d;
				Double fearrP = 0.0d;
				
				if(totalr != 0) {
				angercP = (double)Math.round(((angerFinal1)*100)/(totalr));
				joyrP = (double)Math.round(((joyFinal1)*100)/(totalr));
				sadnrP = (double)Math.round(((sadnessFinal1)*100)/(totalr));
				tenrP = (double)Math.round(((tentativeFinal1)*100)/(totalr));
				analrP = (double)Math.round(((analyticalFinal1)*100)/(totalr));
				confrP = (double)Math.round(((confidentFinal1)*100)/(totalr));
				fearrP =  (double)Math.round(((fearFinal1)*100)/(totalr));
				}
				toneOfMail1.setAnger(angercP);
				toneOfMail1.setJoy(joyrP);
				toneOfMail1.setSadness(sadnrP);
				toneOfMail1.setTentative(tenrP);
				toneOfMail1.setAnalytical(analrP);
				toneOfMail1.setConfident(confrP);
				toneOfMail1.setFear(fearrP);
				
				
				/*toneOfMail1.setAnger(angerFinal1);
				toneOfMail1.setJoy(joyFinal1);
				toneOfMail1.setSadness(sadnessFinal1);
				toneOfMail1.setTentative(tentativeFinal1);
				toneOfMail1.setAnalytical(analyticalFinal1);
				toneOfMail1.setConfident(confidentFinal1);
				toneOfMail1.setFear(fearFinal1);*/
				
				//snt
				
				Double angerFinal2 = 0.0d;
				Double joyFinal2 = 0.0d;
				Double sadnessFinal2 = 0.0d;
				Double tentativeFinal2 = 0.0d;
				Double analyticalFinal2 = 0.0d;
				Double confidentFinal2 = 0.0d;
				Double fearFinal2 = 0.0d;
				
				try {
				angerFinal2 = result.get(0).getAngerTeamTotalsnt()/result.get(0).getAngerTeamCountsnt();
				}catch(Exception e) {
					
				}
				try {
				joyFinal2 = result.get(0).getJoyTeamTotalsnt()/result.get(0).getJoyTeamCountsnt();
				}catch(Exception e) {
					
				}
				try {
				sadnessFinal2 = result.get(0).getSadnessTeamTotalsnt()/result.get(0).getSadnessTeamCountsnt();
				}catch(Exception e) {
					
				}
				try {
				tentativeFinal2 = result.get(0).getTentativeTeamTotalsnt()/result.get(0).getTentativeTeamCountsnt();
				}catch(Exception e) {
					
				}
				try {
				analyticalFinal2 = result.get(0).getAnalyticalTeamTotalsnt()/result.get(0).getAnalyticalTeamCountsnt();
				}catch(Exception e) {
					
				}
				try {
				confidentFinal2 = result.get(0).getConfidentTeamTotalsnt()/result.get(0).getConfidentTeamCountsnt();
				}catch(Exception e) {
					
				}
				try {
				fearFinal2 = result.get(0).getFearTeamTotalsnt()/result.get(0).getFearTeamCountsnt();
				}catch(Exception e) {
					
				}
				
				ToneOfMail toneOfMail2 = new ToneOfMail();
				
				Double totalsnt = angerFinal2+joyFinal2+sadnessFinal2+tentativeFinal2+analyticalFinal2+confidentFinal2+fearFinal2;
				
				Double angersntP = 0.0d;
				Double joyrsntP = 0.0d;
				Double sadnrsntP = 0.0d;
				Double tenrsntP = 0.0d;
				Double analrsntP = 0.0d;
				Double confrsntP = 0.0d;
				Double fearrsntP = 0.0d;
				
				if(totalsnt != 0) {
				angersntP = (double)Math.round(((angerFinal2)*100)/(totalsnt));
				joyrsntP = (double)Math.round(((joyFinal2)*100)/(totalsnt));
				sadnrsntP = (double)Math.round(((sadnessFinal2)*100)/(totalsnt));
				tenrsntP = (double)Math.round(((tentativeFinal2)*100)/(totalsnt));
				analrsntP = (double)Math.round(((analyticalFinal2)*100)/(totalsnt));
				confrsntP = (double)Math.round(((confidentFinal2)*100)/(totalsnt));
				fearrsntP =  (double)Math.round(((fearFinal2)*100)/(totalsnt));
				}
				toneOfMail2.setAnger(angersntP);
				toneOfMail2.setJoy(joyrsntP);
				toneOfMail2.setSadness(sadnrsntP);
				toneOfMail2.setTentative(tenrsntP);
				toneOfMail2.setAnalytical(analrsntP);
				toneOfMail2.setConfident(confrsntP);
				toneOfMail2.setFear(fearrsntP);	
				
			/*	
				Double angerFinal = (double)Math.round(result.get(0).getAngerTeamTotal()/result.get(0).getAngerTeamCount());
				Double joyFinal = (double)Math.round(result.get(0).getJoyTeamTotal()/result.get(0).getJoyTeamCount());
				Double sadnessFinal = (double)Math.round(result.get(0).getSadnessTeamTotal()/result.get(0).getSadnessTeamCount());
				Double tentativeFinal = (double)Math.round(result.get(0).getTentativeTeamTotal()/result.get(0).getTentativeTeamCount());
				Double analyticalFinal = (double)Math.round(result.get(0).getAnalyticalTeamTotal()/result.get(0).getAnalyticalTeamCount());
				Double confidentFinal = (double)Math.round(result.get(0).getConfidentTeamTotal()/result.get(0).getConfidentTeamCount());
				Double fearFinal = (double)Math.round(result.get(0).getFearTeamTotal()/result.get(0).getFearTeamCount());
				
				ToneOfMail toneOfMail = new ToneOfMail();
				toneOfMail.setAnger(angerFinal);
				toneOfMail.setJoy(joyFinal);
				toneOfMail.setSadness(sadnessFinal);
				toneOfMail.setTentative(tentativeFinal);
				toneOfMail.setAnalytical(analyticalFinal);
				toneOfMail.setConfident(confidentFinal);
				toneOfMail.setFear(fearFinal);
				
				//rcv
				
				Double angerFinal1 = (double)Math.round(result.get(0).getAngerTeamTotalrcv()/result.get(0).getAngerTeamCountrcv());
				Double joyFinal1 = (double)Math.round(result.get(0).getJoyTeamTotalrcv()/result.get(0).getJoyTeamCountrcv());
				Double sadnessFinal1 = (double)Math.round(result.get(0).getSadnessTeamTotalrcv()/result.get(0).getSadnessTeamCountrcv());
				Double tentativeFinal1 = (double)Math.round(result.get(0).getTentativeTeamTotalrcv()/result.get(0).getTentativeTeamCountrcv());
				Double analyticalFinal1 = (double)Math.round(result.get(0).getAnalyticalTeamTotalrcv()/result.get(0).getAnalyticalTeamCountrcv());
				Double confidentFinal1 = (double)Math.round(result.get(0).getConfidentTeamTotalrcv()/result.get(0).getConfidentTeamCountrcv());
				Double fearFinal1 = (double)Math.round(result.get(0).getFearTeamTotalrcv()/result.get(0).getFearTeamCountrcv());
				
				ToneOfMail toneOfMail1 = new ToneOfMail();
				toneOfMail1.setAnger(angerFinal1);
				toneOfMail1.setJoy(joyFinal1);
				toneOfMail1.setSadness(sadnessFinal1);
				toneOfMail1.setTentative(tentativeFinal1);
				toneOfMail1.setAnalytical(analyticalFinal1);
				toneOfMail1.setConfident(confidentFinal1);
				toneOfMail1.setFear(fearFinal1);
				
				//snt
				
				Double angerFinal2 = (double)Math.round(result.get(0).getAngerTeamTotalsnt()/result.get(0).getAngerTeamCountsnt());
				Double joyFinal2 = (double)Math.round(result.get(0).getJoyTeamTotalsnt()/result.get(0).getJoyTeamCountsnt());
				Double sadnessFinal2 = (double)Math.round(result.get(0).getSadnessTeamTotalsnt()/result.get(0).getSadnessTeamCountsnt());
				Double tentativeFinal2 = (double)Math.round(result.get(0).getTentativeTeamTotalsnt()/result.get(0).getTentativeTeamCountsnt());
				Double analyticalFinal2 = (double)Math.round(result.get(0).getAnalyticalTeamTotalsnt()/result.get(0).getAnalyticalTeamCountsnt());
				Double confidentFinal2 = (double)Math.round(result.get(0).getConfidentTeamTotalsnt()/result.get(0).getConfidentTeamCountsnt());
				Double fearFinal2 = (double)Math.round(result.get(0).getFearTeamTotalsnt()/result.get(0).getFearTeamCountsnt());
				
				ToneOfMail toneOfMail2 = new ToneOfMail();
				toneOfMail2.setAnger(angerFinal2);
				toneOfMail2.setJoy(joyFinal2);
				toneOfMail2.setSadness(sadnessFinal2);
				toneOfMail2.setTentative(tentativeFinal2);
				toneOfMail2.setAnalytical(analyticalFinal2);
				toneOfMail2.setConfident(confidentFinal2);
				toneOfMail2.setFear(fearFinal2);*/
				
						
				employeePersonalDataDTO.setToneOfClientMail(toneOfMail);
				employeePersonalDataDTO.setToneOfClientReceiveMail(toneOfMail1);
				employeePersonalDataDTO.setToneOfClientsentMail(toneOfMail2);
				employeePersonalDataDTO.setNoOfMail(result.get(0).getTotalTeamMail());
				employeePersonalDataDTO.setNoOfReceiveMail(result.get(0).getTotalTeamMailRecevied());
				employeePersonalDataDTO.setNoOfSentMail(result.get(0).getTotalTeamMailSent());
			
			
			
			
			
			List<EmployeePersonalDataDTO> list = new ArrayList<EmployeePersonalDataDTO>();
			
			
			Aggregation aggregation = Aggregation.newAggregation(
                
				Aggregation.match(Criteria.where("employeeIdFK").is(employeeBO.getEmployeeId())),
				Aggregation.unwind("$clients"),
				Aggregation.match(Criteria.where("clients.emailId").is(clientDataDTO.getEmailId())),
				Aggregation.group("employeeIdFK")
				.sum("clients.allMailScore.anger").as("angerTeamTotal").sum("clients.allMailScore.angerCount")
				.as("angerTeamCount").sum("clients.allMailScore.joy").as("joyTeamTotal").sum("clients.allMailScore.joyCount")
				.as("joyTeamCount").sum("clients.allMailScore.sadness").as("sadnessTeamTotal").sum("clients.allMailScore.sadnessCount")
				.as("sadnessTeamCount").sum("clients.allMailScore.tentative").as("tentativeTeamTotal").sum("clients.allMailScore.tentativeCount")
				.as("tentativeTeamCount").sum("clients.allMailScore.analytical").as("analyticalTeamTotal").sum("clients.allMailScore.analyticalCount")
				.as("analyticalTeamCount").sum("clients.allMailScore.confident").as("confidentTeamTotal").sum("clients.allMailScore.confidentCount")
				.as("confidentTeamCount").sum("clients.allMailScore.fear").as("fearTeamTotal").sum("clients.allMailScore.fearCount")
				.as("fearTeamCount")
				.sum("clients.receiveMailScore.anger").as("angerTeamTotalrcv").sum("clients.receiveMailScore.angerCount")
				.as("angerTeamCountrcv").sum("clients.receiveMailScore.joy").as("joyTeamTotalrcv").sum("clients.receiveMailScore.joyCount")
				.as("joyTeamCountrcv").sum("clients.receiveMailScore.sadness").as("sadnessTeamTotalrcv").sum("clients.receiveMailScore.sadnessCount")
				.as("sadnessTeamCountrcv").sum("clients.receiveMailScore.tentative").as("tentativeTeamTotalrcv").sum("clients.receiveMailScore.tentativeCount")
				.as("tentativeTeamCountrcv").sum("clients.receiveMailScore.analytical").as("analyticalTeamTotalrcv").sum("clients.receiveMailScore.analyticalCount")
				.as("analyticalTeamCountrcv").sum("clients.receiveMailScore.confident").as("confidentTeamTotalrcv").sum("clients.receiveMailScore.confidentCount")
				.as("confidentTeamCountrcv").sum("clients.receiveMailScore.fear").as("fearTeamTotalrcv").sum("clients.receiveMailScore.fearCount")
				.as("fearTeamCountrcv")
				.sum("clients.sentMailScore.anger").as("angerTeamTotalsnt").sum("clients.sentMailScore.angerCount")
				.as("angerTeamCountsnt").sum("clients.sentMailScore.joy").as("joyTeamTotalsnt").sum("clients.sentMailScore.joyCount")
				.as("joyTeamCountsnt").sum("clients.sentMailScore.sadness").as("sadnessTeamTotalsnt").sum("clients.sentMailScore.sadnessCount")
				.as("sadnessTeamCountsnt").sum("clients.sentMailScore.tentative").as("tentativeTeamTotalsnt").sum("clients.sentMailScore.tentativeCount")
				.as("tentativeTeamCountsnt").sum("clients.sentMailScore.analytical").as("analyticalTeamTotalsnt").sum("clients.sentMailScore.analyticalCount")
				.as("analyticalTeamCountsnt").sum("clients.sentMailScore.confident").as("confidentTeamTotalsnt").sum("clients.sentMailScore.confidentCount")
				.as("confidentTeamCountsnt").sum("clients.sentMailScore.fear").as("fearTeamTotalsnt").sum("clients.sentMailScore.fearCount")
				.as("fearTeamCountsnt")
				.sum("clients.totalMail").as("totalTeamMail")
				.sum("clients.totalMailRecevied").as("totalTeamMailRecevied")
				.sum("clients.totalMailSent").as("totalTeamMailSent")
				
					);
			
			AggregationResults<AggregationPojo> groupResults1 
			= mongoTemplate.aggregate(aggregation, TeamClientInteractionBO.class, AggregationPojo.class);
			List<AggregationPojo> result1 = groupResults1.getMappedResults();
			
			// Average of All Self Scores 
			
			Double angerFinalCli = 0.0d;
			Double joyFinalCli = 0.0d;
			Double sadnessFinalCli = 0.0d;
			Double tentativeFinalCli = 0.0d;
			Double analyticalFinalCli = 0.0d;
			Double confidentFinalCli = 0.0d;
			Double fearFinalCli = 0.0d;
			
			try {
			angerFinalCli = result1.get(0).getAngerTeamTotal()/result1.get(0).getAngerTeamCount();
			}catch(Exception e) {
				
			}
			try {
			joyFinalCli = result1.get(0).getJoyTeamTotal()/result1.get(0).getJoyTeamCount();
			}catch(Exception e) {
				
			}
			try {
			sadnessFinalCli = result1.get(0).getSadnessTeamTotal()/result1.get(0).getSadnessTeamCount();
			}catch(Exception e) {
				
			}
			try {
			tentativeFinalCli = result1.get(0).getTentativeTeamTotal()/result1.get(0).getTentativeTeamCount();
			}catch(Exception e) {
				
			}
			try {
			analyticalFinalCli = result1.get(0).getAnalyticalTeamTotal()/result1.get(0).getAnalyticalTeamCount();
			}catch(Exception e) {
				
			}
			try {
			confidentFinalCli = result1.get(0).getConfidentTeamTotal()/result1.get(0).getConfidentTeamCount();
			}catch(Exception e) {
				
			}
			try {
			fearFinalCli = result1.get(0).getFearTeamTotal()/result1.get(0).getFearTeamCount();
			}catch(Exception e) {
				
			}
			ToneOfMail toneOfMailCli = new ToneOfMail();
			
			Double totalCli = angerFinalCli+joyFinalCli+sadnessFinalCli+tentativeFinalCli+analyticalFinalCli+confidentFinalCli+fearFinalCli;
			
			Double angerPCli = 0.0d;
			Double joyPCli = 0.0d;
			Double sadnPCli = 0.0d;
			Double tenPCli = 0.0d;
			Double analPCli = 0.0d;
			Double confPCli = 0.0d;
			Double fearPCli = 0.0d;
			
			if(totalCli != 0) {
			angerPCli = (double)Math.round(((angerFinalCli)*100)/(totalCli));
			joyPCli = (double)Math.round(((joyFinalCli)*100)/(totalCli));
			sadnPCli = (double)Math.round(((sadnessFinalCli)*100)/(totalCli));
			tenPCli = (double)Math.round(((tentativeFinalCli)*100)/(totalCli));
			analPCli = (double)Math.round(((analyticalFinalCli)*100)/(totalCli));
			confPCli = (double)Math.round(((confidentFinalCli)*100)/(totalCli));
			fearPCli =  (double)Math.round(((fearFinalCli)*100)/(totalCli));
			}
			toneOfMailCli.setAnger(angerPCli);
			toneOfMailCli.setJoy(joyPCli);
			toneOfMailCli.setSadness(sadnPCli);
			toneOfMailCli.setTentative(tenPCli);
			toneOfMailCli.setAnalytical(analPCli);
			toneOfMailCli.setConfident(confPCli);
			toneOfMailCli.setFear(fearPCli);
			
			/*toneOfMailCli.setAnger(angerFinalCli);
			toneOfMailCli.setJoy(joyFinalCli);
			toneOfMailCli.setSadness(sadnessFinalCli);
			toneOfMailCli.setTentative(tentativeFinalCli);
			toneOfMailCli.setAnalytical(analyticalFinalCli);
			toneOfMailCli.setConfident(confidentFinalCli);
			toneOfMailCli.setFear(fearFinalCli);*/
			
			//rcv
			
			Double angerFinal1Cli = 0.0d;
			Double joyFinal1Cli = 0.0d;
			Double sadnessFinal1Cli = 0.0d;
			Double tentativeFinal1Cli = 0.0d;
			Double analyticalFinal1Cli = 0.0d;
			Double confidentFinal1Cli = 0.0d;
			Double fearFinal1Cli = 0.0d;
			
			try {
			angerFinal1Cli = result1.get(0).getAngerTeamTotalrcv()/result1.get(0).getAngerTeamCountrcv();
			}catch(Exception e) {
				
			}
			try {
			joyFinal1Cli = result1.get(0).getJoyTeamTotalrcv()/result1.get(0).getJoyTeamCountrcv();
			}catch(Exception e) {
				
			}
			try {
			sadnessFinal1Cli = result1.get(0).getSadnessTeamTotalrcv()/result1.get(0).getSadnessTeamCountrcv();
			}catch(Exception e) {
				
			}
			try {
			tentativeFinal1Cli = result1.get(0).getTentativeTeamTotalrcv()/result1.get(0).getTentativeTeamCountrcv();
			}catch(Exception e) {
				
			}
			try {
			analyticalFinal1Cli = result1.get(0).getAnalyticalTeamTotalrcv()/result1.get(0).getAnalyticalTeamCountrcv();
			}catch(Exception e) {
				
			}
			try {
			confidentFinal1Cli = result1.get(0).getConfidentTeamTotalrcv()/result1.get(0).getConfidentTeamCountrcv();
			}catch(Exception e) {
				
			}
			try {
			fearFinal1Cli = result1.get(0).getFearTeamTotalrcv()/result1.get(0).getFearTeamCountrcv();
			}catch(Exception e) {
				
			}
			
			ToneOfMail toneOfMail1Cli = new ToneOfMail();
			
			Double angerPCli1 = 0.0d;
			Double joyPCli1 = 0.0d;
			Double sadnPCli1 = 0.0d;
			Double tenPCli1 = 0.0d;
			Double analPCli1 = 0.0d;
			Double confPCli1 = 0.0d;
			Double fearPCli1 = 0.0d;
			

			Double totalCli1 = angerFinal1Cli+joyFinal1Cli+sadnessFinal1Cli+tentativeFinal1Cli+analyticalFinal1Cli+confidentFinal1Cli+fearFinal1Cli;
			
			if(totalCli1 != 0) {
			angerPCli1 = (double)Math.round(((angerFinal1Cli)*100)/(totalCli1));
			joyPCli1 = (double)Math.round(((joyFinal1Cli)*100)/(totalCli1));
			sadnPCli1 = (double)Math.round(((sadnessFinal1Cli)*100)/(totalCli1));
			tenPCli1 = (double)Math.round(((tentativeFinal1Cli)*100)/(totalCli1));
			analPCli1 = (double)Math.round(((analyticalFinal1Cli)*100)/(totalCli1));
			confPCli1 = (double)Math.round(((confidentFinal1Cli)*100)/(totalCli1));
			fearPCli1 =  (double)Math.round(((fearFinal1Cli)*100)/(totalCli1));
			}
			
			toneOfMail1Cli.setAnger(angerPCli1);
			toneOfMail1Cli.setJoy(joyPCli1);
			toneOfMail1Cli.setSadness(sadnPCli1);
			toneOfMail1Cli.setTentative(tenPCli1);
			toneOfMail1Cli.setAnalytical(analPCli1);
			toneOfMail1Cli.setConfident(confPCli1);
			toneOfMail1Cli.setFear(fearPCli1);
			
			//snt
			
			Double angerFinal2Cli = 0.0d;
			Double joyFinal2Cli = 0.0d;
			Double sadnessFinal2Cli = 0.0d;
			Double tentativeFinal2Cli = 0.0d;
			Double analyticalFinal2Cli = 0.0d;
			Double confidentFinal2Cli = 0.0d;
			Double fearFinal2Cli = 0.0d;
			
			try {
			angerFinal2Cli = result1.get(0).getAngerTeamTotalsnt()/result1.get(0).getAngerTeamCountsnt();
			}catch(Exception e) {
				
			}
			try {
			joyFinal2Cli = result1.get(0).getJoyTeamTotalsnt()/result1.get(0).getJoyTeamCountsnt();
			}catch(Exception e) {
				
			}
			try {
			sadnessFinal2Cli = result1.get(0).getSadnessTeamTotalsnt()/result1.get(0).getSadnessTeamCountsnt();
			}catch(Exception e) {
				
			}
			try {
			tentativeFinal2Cli = result1.get(0).getTentativeTeamTotalsnt()/result1.get(0).getTentativeTeamCountsnt();
			}catch(Exception e) {
				
			}
			try {
			analyticalFinal2Cli = result1.get(0).getAnalyticalTeamTotalsnt()/result1.get(0).getAnalyticalTeamCountsnt();
			}catch(Exception e) {
				
			}
			try {
			confidentFinal2Cli = result1.get(0).getConfidentTeamTotalsnt()/result1.get(0).getConfidentTeamCountsnt();
			}catch(Exception e) {
				
			}
			try {
			fearFinal2Cli = result1.get(0).getFearTeamTotalsnt()/result1.get(0).getFearTeamCountsnt();
			}catch(Exception e) {
				
			}
			
			ToneOfMail toneOfMail2Cli = new ToneOfMail();
			

			Double totalCli2 = angerFinal2Cli+joyFinal2Cli+sadnessFinal2Cli+tentativeFinal2Cli+analyticalFinal2Cli+confidentFinal2Cli+fearFinal2Cli;
			
			Double angerPCli2 = 0.0d;
			Double joyPCli2 = 0.0d;
			Double sadnPCli2 = 0.0d;
			Double tenPCli2 = 0.0d;
			Double analPCli2 = 0.0d;
			Double confPCli2 = 0.0d;
			Double fearPCli2 = 0.0d;
			
			if(totalCli2 != 0) {
			angerPCli2 = (double)Math.round(((angerFinal2Cli)*100)/(totalCli2));
			joyPCli2 = (double)Math.round(((joyFinal2Cli)*100)/(totalCli2));
			sadnPCli2 = (double)Math.round(((sadnessFinal2Cli)*100)/(totalCli2));
			tenPCli2 = (double)Math.round(((tentativeFinal2Cli)*100)/(totalCli2));
			analPCli2 = (double)Math.round(((analyticalFinal2Cli)*100)/(totalCli2));
			confPCli2 = (double)Math.round(((confidentFinal2Cli)*100)/(totalCli2));
			fearPCli2 =  (double)Math.round(((fearFinal2Cli)*100)/(totalCli2));
			}
			
			toneOfMail2Cli.setAnger(angerPCli2);
			toneOfMail2Cli.setJoy(joyPCli2);
			toneOfMail2Cli.setSadness(sadnPCli2);
			toneOfMail2Cli.setTentative(tenPCli2);
			toneOfMail2Cli.setAnalytical(analPCli2);
			toneOfMail2Cli.setConfident(confPCli2);
			toneOfMail2Cli.setFear(fearPCli2);
			
			
			
		/*	
			
			Double angerFinalCli = (double)Math.round(result1.get(0).getAngerTeamTotal()/result1.get(0).getAngerTeamCount());
			Double joyFinalCli = (double)Math.round(result1.get(0).getJoyTeamTotal()/result1.get(0).getJoyTeamCount());
			Double sadnessFinalCli = (double)Math.round(result1.get(0).getSadnessTeamTotal()/result1.get(0).getSadnessTeamCount());
			Double tentativeFinalCli = (double)Math.round(result1.get(0).getTentativeTeamTotal()/result1.get(0).getTentativeTeamCount());
			Double analyticalFinalCli = (double)Math.round(result1.get(0).getAnalyticalTeamTotal()/result1.get(0).getAnalyticalTeamCount());
			Double confidentFinalCli = (double)Math.round(result1.get(0).getConfidentTeamTotal()/result1.get(0).getConfidentTeamCount());
			Double fearFinalCli = (double)Math.round(result1.get(0).getFearTeamTotal()/result1.get(0).getFearTeamCount());
			
			ToneOfMail toneOfMailCli = new ToneOfMail();
			toneOfMailCli.setAnger(angerFinalCli);
			toneOfMailCli.setJoy(joyFinalCli);
			toneOfMailCli.setSadness(sadnessFinalCli);
			toneOfMailCli.setTentative(tentativeFinalCli);
			toneOfMailCli.setAnalytical(analyticalFinalCli);
			toneOfMailCli.setConfident(confidentFinalCli);
			toneOfMailCli.setFear(fearFinalCli);
			
			//rcv
			
			Double angerFinal1Cli = (double)Math.round(result1.get(0).getAngerTeamTotalrcv()/result1.get(0).getAngerTeamCountrcv());
			Double joyFinal1Cli = (double)Math.round(result1.get(0).getJoyTeamTotalrcv()/result1.get(0).getJoyTeamCountrcv());
			Double sadnessFinal1Cli = (double)Math.round(result1.get(0).getSadnessTeamTotalrcv()/result1.get(0).getSadnessTeamCountrcv());
			Double tentativeFinal1Cli = (double)Math.round(result1.get(0).getTentativeTeamTotalrcv()/result1.get(0).getTentativeTeamCountrcv());
			Double analyticalFinal1Cli = (double)Math.round(result1.get(0).getAnalyticalTeamTotalrcv()/result1.get(0).getAnalyticalTeamCountrcv());
			Double confidentFinal1Cli = (double)Math.round(result1.get(0).getConfidentTeamTotalrcv()/result1.get(0).getConfidentTeamCountrcv());
			Double fearFinal1Cli = (double)Math.round(result1.get(0).getFearTeamTotalrcv()/result1.get(0).getFearTeamCountrcv());
			
			ToneOfMail toneOfMail1Cli = new ToneOfMail();
			toneOfMail1Cli.setAnger(angerFinal1Cli);
			toneOfMail1Cli.setJoy(joyFinal1Cli);
			toneOfMail1Cli.setSadness(sadnessFinal1Cli);
			toneOfMail1Cli.setTentative(tentativeFinal1Cli);
			toneOfMail1Cli.setAnalytical(analyticalFinal1Cli);
			toneOfMail1Cli.setConfident(confidentFinal1Cli);
			toneOfMail1Cli.setFear(fearFinal1Cli);
			
			//snt
			
			Double angerFinal2Cli = (double)Math.round(result1.get(0).getAngerTeamTotalsnt()/result1.get(0).getAngerTeamCountsnt());
			Double joyFinal2Cli = (double)Math.round(result1.get(0).getJoyTeamTotalsnt()/result1.get(0).getJoyTeamCountsnt());
			Double sadnessFinal2Cli = (double)Math.round(result1.get(0).getSadnessTeamTotalsnt()/result1.get(0).getSadnessTeamCountsnt());
			Double tentativeFinal2Cli = (double)Math.round(result1.get(0).getTentativeTeamTotalsnt()/result1.get(0).getTentativeTeamCountsnt());
			Double analyticalFinal2Cli = (double)Math.round(result1.get(0).getAnalyticalTeamTotalsnt()/result1.get(0).getAnalyticalTeamCountsnt());
			Double confidentFinal2Cli = (double)Math.round(result1.get(0).getConfidentTeamTotalsnt()/result1.get(0).getConfidentTeamCountsnt());
			Double fearFinal2Cli = (double)Math.round(result1.get(0).getFearTeamTotalsnt()/result1.get(0).getFearTeamCountsnt());
			
			ToneOfMail toneOfMail2Cli = new ToneOfMail();
			toneOfMail2Cli.setAnger(angerFinal2Cli);
			toneOfMail2Cli.setJoy(joyFinal2Cli);
			toneOfMail2Cli.setSadness(sadnessFinal2Cli);
			toneOfMail2Cli.setTentative(tentativeFinal2Cli);
			toneOfMail2Cli.setAnalytical(analyticalFinal2Cli);
			toneOfMail2Cli.setConfident(confidentFinal2Cli);
			toneOfMail2Cli.setFear(fearFinal2Cli);*/
			
			EmployeePersonalDataDTO employeePersonalDataDTO1 = new EmployeePersonalDataDTO();
					
			employeePersonalDataDTO1.setToneOfTeamMail(toneOfMailCli);
			employeePersonalDataDTO1.setToneOfTeamReceiveMail(toneOfMail1Cli);
			employeePersonalDataDTO1.setToneOfTeamSentMail(toneOfMail2Cli);
			try {
			employeePersonalDataDTO1.setNoOfMail(result1.get(0).getTotalTeamMail());
			}catch(Exception e) {
				employeePersonalDataDTO1.setNoOfMail(0l);
			}
			try {
			employeePersonalDataDTO1.setNoOfReceiveMail(result1.get(0).getTotalTeamMailRecevied());
			}catch(Exception e) {
				employeePersonalDataDTO1.setNoOfReceiveMail(0l);
			}
			
			try {
			employeePersonalDataDTO1.setNoOfSentMail(result1.get(0).getTotalTeamMailSent());
			}catch(Exception e) {
				employeePersonalDataDTO1.setNoOfSentMail(0l);
			}
			
			employeePersonalDataDTO1.setEmployeeName(clientDataDTO.getEmployeeName());
			
			employeePersonalDataDTO1.setDesignation(clientDataDTO.getDesignation());
			
			OrganisationBO org = companyRepository.findOne(clientDataDTO.getCompanyId());
			
			employeePersonalDataDTO.setCompanyName(org.getCompanyName());
			
			list.add(employeePersonalDataDTO1);
			
			
			employeePersonalDataDTO.setListOfEmployee(list);
			}catch(Exception e) {
				e.printStackTrace(); 
			}
			
		
		return employeePersonalDataDTO;          
		
	}
	
	

	
	
	
	// sorting service on EmployeeDashBoard
	
	public EmployeePersonalDataDTO getSortedEmployeeHierchy(ClientDataDTO clientDataDTO){
		
		EmployeePersonalDataDTO empldata = new EmployeePersonalDataDTO();
		
		 List<EmployeePersonalDataDTO> listOfEmployeeData = new ArrayList<EmployeePersonalDataDTO>();
		 
				
		 // loggedIn employee Details
		 
		 EmployeeRoleBO employeeRoleBO = employeeRoleRepository.findByEmployeeIdFKAndStatus(clientDataDTO.getEmployeeId(),
					"active");
		 EmployeeBO employeeData =employeeRepository.findByEmployeeId(clientDataDTO.getEmployeeId());
			
		 empldata.setDesignation(employeeRoleBO.getDesignation());
		 empldata.setEmployeeName(employeeData.getEmployeeName());
		 empldata.setEmployeeId(employeeData.getEmployeeId());
		 
			
			Aggregation	 aggregation = Aggregation.newAggregation(
		                
						Aggregation.match(Criteria.where("employeeIdFK").is(clientDataDTO.getEmployeeId()).and("date").gte("2018-04-16")),
						Aggregation.group("employeeIdFK").last("employeeIdFK").as("employeeId")
						.sum("teamTone.allMailScore.anger").as("angerTeamTotal").sum("teamTone.allMailScore.angerCount")
						.as("angerTeamCount").sum("teamTone.allMailScore.joy").as("joyTeamTotal").sum("teamTone.allMailScore.joyCount")
						.as("joyTeamCount").sum("teamTone.allMailScore.sadness").as("sadnessTeamTotal").sum("teamTone.allMailScore.sadnessCount")
						.as("sadnessTeamCount").sum("teamTone.allMailScore.tentative").as("tentativeTeamTotal").sum("teamTone.allMailScore.tentativeCount")
						.as("tentativeTeamCount").sum("teamTone.allMailScore.analytical").as("analyticalTeamTotal").sum("teamTone.allMailScore.analyticalCount")
						.as("analyticalTeamCount").sum("teamTone.allMailScore.confident").as("confidentTeamTotal").sum("teamTone.allMailScore.confidentCount")
						.as("confidentTeamCount").sum("teamTone.allMailScore.fear").as("fearTeamTotal").sum("teamTone.allMailScore.fearCount")
						.as("fearTeamCount")
						.sum("teamTone.receiveMailScore.anger").as("angerTeamTotalrcv").sum("teamTone.receiveMailScore.angerCount")
						.as("angerTeamCountrcv").sum("teamTone.receiveMailScore.joy").as("joyTeamTotalrcv").sum("teamTone.receiveMailScore.joyCount")
						.as("joyTeamCountrcv").sum("teamTone.receiveMailScore.sadness").as("sadnessTeamTotalrcv").sum("teamTone.receiveMailScore.sadnessCount")
						.as("sadnessTeamCountrcv").sum("teamTone.receiveMailScore.tentative").as("tentativeTeamTotalrcv").sum("teamTone.receiveMailScore.tentativeCount")
						.as("tentativeTeamCountrcv").sum("teamTone.receiveMailScore.analytical").as("analyticalTeamTotalrcv").sum("teamTone.receiveMailScore.analyticalCount")
						.as("analyticalTeamCountrcv").sum("teamTone.receiveMailScore.confident").as("confidentTeamTotalrcv").sum("teamTone.receiveMailScore.confidentCount")
						.as("confidentTeamCountrcv").sum("teamTone.receiveMailScore.fear").as("fearTeamTotalrcv").sum("teamTone.receiveMailScore.fearCount")
						.as("fearTeamCountrcv")
						.sum("teamTone.sentMailScore.anger").as("angerTeamTotalsnt").sum("teamTone.sentMailScore.angerCount")
						.as("angerTeamCountsnt").sum("teamTone.sentMailScore.joy").as("joyTeamTotalsnt").sum("teamTone.sentMailScore.joyCount")
						.as("joyTeamCountsnt").sum("teamTone.sentMailScore.sadness").as("sadnessTeamTotalsnt").sum("teamTone.sentMailScore.sadnessCount")
						.as("sadnessTeamCountsnt").sum("teamTone.sentMailScore.tentative").as("tentativeTeamTotalsnt").sum("teamTone.sentMailScore.tentativeCount")
						.as("tentativeTeamCountsnt").sum("teamTone.sentMailScore.analytical").as("analyticalTeamTotalsnt").sum("teamTone.sentMailScore.analyticalCount")
						.as("analyticalTeamCountsnt").sum("teamTone.sentMailScore.confident").as("confidentTeamTotalsnt").sum("teamTone.sentMailScore.confidentCount")
						.as("confidentTeamCountsnt").sum("teamTone.sentMailScore.fear").as("fearTeamTotalsnt").sum("teamTone.sentMailScore.fearCount")
						.as("fearTeamCountsnt")
						.sum("teamTone.totalMail").as("totalTeamMail")
						.sum("teamTone.totalMailRecevied").as("totalTeamMailRecevied")
						.sum("teamTone.totalMailSent").as("totalTeamMailSent")
						
						
							);
			
		
			
			AggregationResults<AggregationPojo> groupResults 
			= mongoTemplate.aggregate(aggregation, DailyEmployeeEmailToneBO.class, AggregationPojo.class);
			List<AggregationPojo> result = groupResults.getMappedResults();
			
			
		
		
	// Average of All Self Scores
			
		Double angerFinal = 0.0d;
		Double joyFinal = 0.0d;
		Double sadnessFinal = 0.0d;
		Double tentativeFinal = 0.0d;
		Double analyticalFinal = 0.0d;
		Double confidentFinal = 0.0d;
		Double fearFinal = 0.0d;
		
		if(result.get(0).getAngerTeamCount() != 0)
		angerFinal = result.get(0).getAngerTeamTotal()/result.get(0).getAngerTeamCount();
		if(result.get(0).getJoyTeamCount() != 0)
		joyFinal = result.get(0).getJoyTeamTotal()/result.get(0).getJoyTeamCount();
		if(result.get(0).getSadnessTeamCount() != 0)
		sadnessFinal = result.get(0).getSadnessTeamTotal()/result.get(0).getSadnessTeamCount();
		if(result.get(0).getTentativeTeamCount() != 0)
		tentativeFinal = result.get(0).getTentativeTeamTotal()/result.get(0).getTentativeTeamCount();
		if(result.get(0).getAnalyticalTeamCount() != 0)
		analyticalFinal = result.get(0).getAnalyticalTeamTotal()/result.get(0).getAnalyticalTeamCount();
		if(result.get(0).getConfidentTeamCount() != 0)
		confidentFinal = result.get(0).getConfidentTeamTotal()/result.get(0).getConfidentTeamCount();
		if(result.get(0).getFearTeamCount() != 0)
		fearFinal =result.get(0).getFearTeamTotal()/result.get(0).getFearTeamCount();
		
		ToneOfMail toneOfMailL = new ToneOfMail();
		
		Double totalL = angerFinal+joyFinal+sadnessFinal+tentativeFinal+analyticalFinal+confidentFinal+fearFinal;
		Double angerPL = (double)Math.round(((angerFinal)*100)/(totalL));
		Double joyPL = (double)Math.round(((joyFinal)*100)/(totalL));
		Double sadnPL = (double)Math.round(((sadnessFinal)*100)/(totalL));
		Double tenPL = (double)Math.round(((tentativeFinal)*100)/(totalL));
		Double analPL = (double)Math.round(((analyticalFinal)*100)/(totalL));
		Double confPL = (double)Math.round(((confidentFinal)*100)/(totalL));
		Double fearPL =  (double)Math.round(((fearFinal)*100)/(totalL));
		
		toneOfMailL.setAnger(angerPL);
		toneOfMailL.setJoy(joyPL);
		toneOfMailL.setSadness(sadnPL);
		toneOfMailL.setTentative(tenPL);
		toneOfMailL.setAnalytical(analPL);
		toneOfMailL.setConfident(confPL);
		toneOfMailL.setFear(fearPL);
		/*
		toneOfMail.setAnger(angerFinal);
		toneOfMail.setJoy(joyFinal);
		toneOfMail.setSadness(sadnessFinal);
		toneOfMail.setTentative(tentativeFinal);
		toneOfMail.setAnalytical(analyticalFinal);
		toneOfMail.setConfident(confidentFinal);
		toneOfMail.setFear(fearFinal);*/
		
		//rcv
		
		Double angerFinal1 = 0.0d;
		Double joyFinal1 = 0.0d;
		Double sadnessFinal1 = 0.0d;
		Double tentativeFinal1 = 0.0d;
		Double analyticalFinal1 = 0.0d;
		Double confidentFinal1 = 0.0d;
		Double fearFinal1 = 0.0d;
		
		if(result.get(0).getAngerTeamCountrcv() != 0)
		angerFinal1 = result.get(0).getAngerTeamTotalrcv()/result.get(0).getAngerTeamCountrcv();
		if(result.get(0).getJoyTeamCountrcv() != 0)
		joyFinal1 = result.get(0).getJoyTeamTotalrcv()/result.get(0).getJoyTeamCountrcv();
		if(result.get(0).getSadnessTeamCountrcv() != 0)
		sadnessFinal1 = result.get(0).getSadnessTeamTotalrcv()/result.get(0).getSadnessTeamCountrcv();
		if(result.get(0).getTentativeTeamCountrcv() != 0)
		tentativeFinal1 = result.get(0).getTentativeTeamTotalrcv()/result.get(0).getTentativeTeamCountrcv();
		if(result.get(0).getAnalyticalTeamCountrcv() != 0)
		analyticalFinal1 = result.get(0).getAnalyticalTeamTotalrcv()/result.get(0).getAnalyticalTeamCountrcv();
		if(result.get(0).getConfidentTeamCountrcv() != 0)
		confidentFinal1 = result.get(0).getConfidentTeamTotalrcv()/result.get(0).getConfidentTeamCountrcv();
		if(result.get(0).getFearTeamCountrcv() != 0)
		fearFinal1 = result.get(0).getFearTeamTotalrcv()/result.get(0).getFearTeamCountrcv();
		
		ToneOfMail toneOfMail1L = new ToneOfMail();
		Double totalrL = angerFinal1+joyFinal1+sadnessFinal1+tentativeFinal1+analyticalFinal1+confidentFinal1+fearFinal1;
		Double angercPL = (double)Math.round(((angerFinal1)*100)/(totalrL));
		Double joyrPL = (double)Math.round(((joyFinal1)*100)/(totalrL));
		Double sadnrPL = (double)Math.round(((sadnessFinal1)*100)/(totalrL));
		Double tenrPL = (double)Math.round(((tentativeFinal1)*100)/(totalrL));
		Double analrPL = (double)Math.round(((analyticalFinal1)*100)/(totalrL));
		Double confrPL = (double)Math.round(((confidentFinal1)*100)/(totalrL));
		Double fearrPL =  (double)Math.round(((fearFinal1)*100)/(totalrL));
		
		toneOfMail1L.setAnger(angercPL);
		toneOfMail1L.setJoy(joyrPL);
		toneOfMail1L.setSadness(sadnrPL);
		toneOfMail1L.setTentative(tenrPL);
		toneOfMail1L.setAnalytical(analrPL);
		toneOfMail1L.setConfident(confrPL);
		toneOfMail1L.setFear(fearrPL);
		
		/*toneOfMail1.setAnger(angerFinal1);
		toneOfMail1.setJoy(joyFinal1);
		toneOfMail1.setSadness(sadnessFinal1);
		toneOfMail1.setTentative(tentativeFinal1);
		toneOfMail1.setAnalytical(analyticalFinal1);
		toneOfMail1.setConfident(confidentFinal1);
		toneOfMail1.setFear(fearFinal1);*/
		
		//snt
		
		Double angerFinal2 = 0.0d;
		Double joyFinal2 = 0.0d;
		Double sadnessFinal2 = 0.0d;
		Double tentativeFinal2 = 0.0d;
		Double analyticalFinal2 = 0.0d;
		Double confidentFinal2 = 0.0d;
		Double fearFinal2 = 0.0d;
		
		if(result.get(0).getAngerTeamCountsnt() != 0)
		angerFinal2 = result.get(0).getAngerTeamTotalsnt()/result.get(0).getAngerTeamCountsnt();
		if(result.get(0).getJoyTeamCountsnt() != 0)
		joyFinal2 = result.get(0).getJoyTeamTotalsnt()/result.get(0).getJoyTeamCountsnt();
		if(result.get(0).getSadnessTeamCountsnt() != 0)
		sadnessFinal2 = result.get(0).getSadnessTeamTotalsnt()/result.get(0).getSadnessTeamCountsnt();
		if(result.get(0).getTentativeTeamCountsnt() != 0)
		tentativeFinal2 = result.get(0).getTentativeTeamTotalsnt()/result.get(0).getTentativeTeamCountsnt();
		if(result.get(0).getAnalyticalTeamCountsnt() != 0)
		analyticalFinal2 = result.get(0).getAnalyticalTeamTotalsnt()/result.get(0).getAnalyticalTeamCountsnt();
		if(result.get(0).getConfidentTeamCountsnt() != 0)
		confidentFinal2 = result.get(0).getConfidentTeamTotalsnt()/result.get(0).getConfidentTeamCountsnt();
		if(result.get(0).getFearTeamCountsnt() != 0)
		fearFinal2 = result.get(0).getFearTeamTotalsnt()/result.get(0).getFearTeamCountsnt();
		
		ToneOfMail toneOfMail2L = new ToneOfMail();
		
		Double totalsntL = angerFinal2+joyFinal2+sadnessFinal2+tentativeFinal2+analyticalFinal2+confidentFinal2+fearFinal2;
		Double angersntPL = (double)Math.round(((angerFinal2)*100)/(totalsntL));
		Double joyrsntPL = (double)Math.round(((joyFinal2)*100)/(totalsntL));
		Double sadnrsntPL = (double)Math.round(((sadnessFinal2)*100)/(totalsntL));
		Double tenrsntPL = (double)Math.round(((tentativeFinal2)*100)/(totalsntL));
		Double analrsntPL = (double)Math.round(((analyticalFinal2)*100)/(totalsntL));
		Double confrsntPL = (double)Math.round(((confidentFinal2)*100)/(totalsntL));
		Double fearrsntPL =  (double)Math.round(((fearFinal2)*100)/(totalsntL));
		
		toneOfMail2L.setAnger(angersntPL);
		toneOfMail2L.setJoy(joyrsntPL);
		toneOfMail2L.setSadness(sadnrsntPL);
		toneOfMail2L.setTentative(tenrsntPL);
		toneOfMail2L.setAnalytical(analrsntPL);
		toneOfMail2L.setConfident(confrsntPL);
		toneOfMail2L.setFear(fearrsntPL);	
		
		
		
		
		/*toneOfMail2.setAnger(angerFinal2);
		toneOfMail2.setJoy(joyFinal2);
		toneOfMail2.setSadness(sadnessFinal2);
		toneOfMail2.setTentative(tentativeFinal2);
		toneOfMail2.setAnalytical(analyticalFinal2);
		toneOfMail2.setConfident(confidentFinal2);
		toneOfMail2.setFear(fearFinal2);*/
		
				
		empldata.setToneOfTeamMail(toneOfMailL);
		empldata.setToneOfTeamReceiveMail(toneOfMail1L);
		empldata.setToneOfTeamSentMail(toneOfMail2L);
		empldata.setNoOfMail(result.get(0).getTotalTeamMail());
		empldata.setNoOfReceiveMail(result.get(0).getTotalTeamMailRecevied());
		empldata.setNoOfSentMail(result.get(0).getTotalTeamMailSent());
		 
				
				
				
				//sample code
				
				Aggregation aggregation1 = Aggregation.newAggregation(
						
						
						Aggregation.match(Criteria.where("reportToId").is(clientDataDTO.getEmployeeId())),
						Aggregation.skip(0),
						Aggregation.limit(5),
						Aggregation.out("temp")
						
						
							);
					
					AggregationResults<EmployeeRoleBO> groupResults1 
					= mongoTemplate.aggregate(aggregation1, EmployeeRoleBO.class, EmployeeRoleBO.class);
					List<EmployeeRoleBO> result1 = groupResults1.getMappedResults();
					List<SortOperation> listSort = new ArrayList<SortOperation>();
					SortOperation sortByTone = null;
					
					if (clientDataDTO.getSortType().equalsIgnoreCase("DSC")) {
						
			
					if(clientDataDTO.getSearchCriteria().contains("anger")) {
						
						 sortByTone = Aggregation.sort(Sort.Direction.DESC,"angerTeamTotal");
						 listSort.add(0, sortByTone);
					}
					if(clientDataDTO.getSearchCriteria().contains("joy")) {
						if (listSort.size()>0) {
							sortByTone = listSort.get(0).and(Sort.Direction.DESC,"joyTeamTotal");
							 listSort.add(0, sortByTone);
						}else {
							 sortByTone = Aggregation.sort(Sort.Direction.DESC,"joyTeamTotal");
							 listSort.add(0, sortByTone);
						}
						
					}
					if(clientDataDTO.getSearchCriteria().contains("sadness")) {
						if (listSort.size()>0) {
							sortByTone = listSort.get(0).and(Sort.Direction.DESC,"sadnessTeamTotal");
							 listSort.add(0, sortByTone);
						}else {
							 sortByTone = Aggregation.sort(Sort.Direction.DESC,"sadnessTeamTotal");
							 listSort.add(0, sortByTone);
						}
						
					}
					if(clientDataDTO.getSearchCriteria().contains("tentative")) {
						if (listSort.size()>0) {
							sortByTone = listSort.get(0).and(Sort.Direction.DESC,"tentativeTeamTotal");
							 listSort.add(0, sortByTone);
						}else {
							 sortByTone = Aggregation.sort(Sort.Direction.DESC,"tentativeTeamTotal");
							 listSort.add(0, sortByTone);
						}
						
					}
					if(clientDataDTO.getSearchCriteria().contains("analytical")) {
						if (listSort.size()>0) {
							sortByTone = listSort.get(0).and(Sort.Direction.DESC,"analyticalTeamTotal");
							 listSort.add(0, sortByTone);
						}else {
							 sortByTone = Aggregation.sort(Sort.Direction.DESC,"analyticalTeamTotal");
							 listSort.add(0, sortByTone);
						}
						
					}
					if(clientDataDTO.getSearchCriteria().contains("confident")) {
						if (listSort.size()>0) {
							sortByTone = listSort.get(0).and(Sort.Direction.DESC,"confidentTeamTotal");
							 listSort.add(0, sortByTone);
						}else {
							 sortByTone = Aggregation.sort(Sort.Direction.DESC,"confidentTeamTotal");
							 listSort.add(0, sortByTone);
						}
						
					}
					if(clientDataDTO.getSearchCriteria().contains("fear")) {
						if (listSort.size()>0) {
							sortByTone = listSort.get(0).and(Sort.Direction.DESC,"fearTeamTotal");
							 listSort.add(0, sortByTone);
						}else {
							 sortByTone = Aggregation.sort(Sort.Direction.DESC,"fearTeamTotal");
							 listSort.add(0, sortByTone);
						}
						
					}
					if(clientDataDTO.getSearchCriteria().contains("teamMail")) {
						if (listSort.size()>0) {
							sortByTone = listSort.get(0).and(Sort.Direction.DESC,"totalTeamMail");
							 listSort.add(0, sortByTone);
						}else {
							 sortByTone = Aggregation.sort(Sort.Direction.DESC,"totalTeamMail");
							 listSort.add(0, sortByTone);
						}
						
					}
					
					
					
					}
					
					/*else {
						 sortByTone = Aggregation.sort(Sort.Direction.DESC,"angerTeamTotal");
						 listSort.add(0, sortByTone);
					}*/
					
					if (clientDataDTO.getSortType().equalsIgnoreCase("ASC")) {
						
						 /*sortByTone = Aggregation.sort(Sort.Direction.DESC,"angerTeamTotal");
						 listSort.add(0, sortByTone);*/
						 
						// new changes
						 
						 if(clientDataDTO.getSearchCriteria().contains("anger")) {
								
							 sortByTone = Aggregation.sort(Sort.Direction.ASC,"angerTeamTotal");
							 listSort.add(0, sortByTone);
						}
						if(clientDataDTO.getSearchCriteria().contains("joy")) {
							if (listSort.size()>0) {
								sortByTone = listSort.get(0).and(Sort.Direction.ASC,"joyTeamTotal");
								 listSort.add(0, sortByTone);
							}else {
								 sortByTone = Aggregation.sort(Sort.Direction.ASC,"joyTeamTotal");
								 listSort.add(0, sortByTone);
							}
							
						}
						if(clientDataDTO.getSearchCriteria().contains("sadness")) {
							if (listSort.size()>0) {
								sortByTone = listSort.get(0).and(Sort.Direction.ASC,"sadnessTeamTotal");
								 listSort.add(0, sortByTone);
							}else {
								 sortByTone = Aggregation.sort(Sort.Direction.ASC,"sadnessTeamTotal");
								 listSort.add(0, sortByTone);
							}
							
						}
						if(clientDataDTO.getSearchCriteria().contains("tentative")) {
							if (listSort.size()>0) {
								sortByTone = listSort.get(0).and(Sort.Direction.ASC,"tentativeTeamTotal");
								 listSort.add(0, sortByTone);
							}else {
								 sortByTone = Aggregation.sort(Sort.Direction.ASC,"tentativeTeamTotal");
								 listSort.add(0, sortByTone);
							}
							
						}
						if(clientDataDTO.getSearchCriteria().contains("analytical")) {
							if (listSort.size()>0) {
								sortByTone = listSort.get(0).and(Sort.Direction.ASC,"analyticalTeamTotal");
								 listSort.add(0, sortByTone);
							}else {
								 sortByTone = Aggregation.sort(Sort.Direction.ASC,"analyticalTeamTotal");
								 listSort.add(0, sortByTone);
							}
							
						}
						if(clientDataDTO.getSearchCriteria().contains("confident")) {
							if (listSort.size()>0) {
								sortByTone = listSort.get(0).and(Sort.Direction.ASC,"confidentTeamTotal");
								 listSort.add(0, sortByTone);
							}else {
								 sortByTone = Aggregation.sort(Sort.Direction.ASC,"confidentTeamTotal");
								 listSort.add(0, sortByTone);
							}
							
						}
						if(clientDataDTO.getSearchCriteria().contains("fear")) {
							if (listSort.size()>0) {
								sortByTone = listSort.get(0).and(Sort.Direction.ASC,"fearTeamTotal");
								 listSort.add(0, sortByTone);
							}else {
								 sortByTone = Aggregation.sort(Sort.Direction.ASC,"fearTeamTotal");
								 listSort.add(0, sortByTone);
							}
							
						} 
						
						if(clientDataDTO.getSearchCriteria().contains("teamMail")) {
							if (listSort.size()>0) {
								sortByTone = listSort.get(0).and(Sort.Direction.ASC,"totalTeamMail");
								 listSort.add(0, sortByTone);
							}else {
								 sortByTone = Aggregation.sort(Sort.Direction.ASC,"totalTeamMail");
								 listSort.add(0, sortByTone);
							}
							
						}
						 
						 
						 
					}
					
					//get value from list
					
					sortByTone = listSort.get(0);
					
					
				
				Aggregation aggregation2 = Aggregation.newAggregation(
						
						Aggregation.lookup("temp", "employeeIdFK", "employeeIdFK", "employeeRoleBOs"),
						Aggregation.match(Criteria.where("employeeRoleBOs.reportToId").is(clientDataDTO.getEmployeeId()).and("date").gte("2018-04-16")),
						//Aggregation.sort(Sort.Direction.DESC,"name"),
						Aggregation.group("employeeIdFK").last("employeeIdFK").as("employeeId").last("employeeRoleBOs").as("employeeRoleBOs").last("name").as("name")
						.sum("teamTone.allMailScore.anger").as("angerTeamTotal").sum("teamTone.allMailScore.angerCount")
						.as("angerTeamCount").sum("teamTone.allMailScore.joy").as("joyTeamTotal").sum("teamTone.allMailScore.joyCount")
						.as("joyTeamCount").sum("teamTone.allMailScore.sadness").as("sadnessTeamTotal").sum("teamTone.allMailScore.sadnessCount")
						.as("sadnessTeamCount").sum("teamTone.allMailScore.tentative").as("tentativeTeamTotal").sum("teamTone.allMailScore.tentativeCount")
						.as("tentativeTeamCount").sum("teamTone.allMailScore.analytical").as("analyticalTeamTotal").sum("teamTone.allMailScore.analyticalCount")
						.as("analyticalTeamCount").sum("teamTone.allMailScore.confident").as("confidentTeamTotal").sum("teamTone.allMailScore.confidentCount")
						.as("confidentTeamCount").sum("teamTone.allMailScore.fear").as("fearTeamTotal").sum("teamTone.allMailScore.fearCount")
						.as("fearTeamCount")
						.sum("teamTone.receiveMailScore.anger").as("angerTeamTotalrcv").sum("teamTone.receiveMailScore.angerCount")
						.as("angerTeamCountrcv").sum("teamTone.receiveMailScore.joy").as("joyTeamTotalrcv").sum("teamTone.receiveMailScore.joyCount")
						.as("joyTeamCountrcv").sum("teamTone.receiveMailScore.sadness").as("sadnessTeamTotalrcv").sum("teamTone.receiveMailScore.sadnessCount")
						.as("sadnessTeamCountrcv").sum("teamTone.receiveMailScore.tentative").as("tentativeTeamTotalrcv").sum("teamTone.receiveMailScore.tentativeCount")
						.as("tentativeTeamCountrcv").sum("teamTone.receiveMailScore.analytical").as("analyticalTeamTotalrcv").sum("teamTone.receiveMailScore.analyticalCount")
						.as("analyticalTeamCountrcv").sum("teamTone.receiveMailScore.confident").as("confidentTeamTotalrcv").sum("teamTone.receiveMailScore.confidentCount")
						.as("confidentTeamCountrcv").sum("teamTone.receiveMailScore.fear").as("fearTeamTotalrcv").sum("teamTone.receiveMailScore.fearCount")
						.as("fearTeamCountrcv")
						.sum("teamTone.sentMailScore.anger").as("angerTeamTotalsnt").sum("teamTone.sentMailScore.angerCount")
						.as("angerTeamCountsnt").sum("teamTone.sentMailScore.joy").as("joyTeamTotalsnt").sum("teamTone.sentMailScore.joyCount")
						.as("joyTeamCountsnt").sum("teamTone.sentMailScore.sadness").as("sadnessTeamTotalsnt").sum("teamTone.sentMailScore.sadnessCount")
						.as("sadnessTeamCountsnt").sum("teamTone.sentMailScore.tentative").as("tentativeTeamTotalsnt").sum("teamTone.sentMailScore.tentativeCount")
						.as("tentativeTeamCountsnt").sum("teamTone.sentMailScore.analytical").as("analyticalTeamTotalsnt").sum("teamTone.sentMailScore.analyticalCount")
						.as("analyticalTeamCountsnt").sum("teamTone.sentMailScore.confident").as("confidentTeamTotalsnt").sum("teamTone.sentMailScore.confidentCount")
						.as("confidentTeamCountsnt").sum("teamTone.sentMailScore.fear").as("fearTeamTotalsnt").sum("teamTone.sentMailScore.fearCount")
						.as("fearTeamCountsnt")
						.sum("teamTone.totalMail").as("totalTeamMail")
						.sum("teamTone.totalMailRecevied").as("totalTeamMailRecevied")
						.sum("teamTone.totalMailSent").as("totalTeamMailSent"),
//						Aggregation.project("employeeIdFK").and("name").as("employeeName")
//						.and("employeeRoleBOs").as("role")
//						.and("totalTeamMail").as("totalTeamMail")
//						.and("totalTeamMailRecevied").as("totalTeamMailRecevied")
//						.and("totalTeamMailSent").as("totalTeamMailSent")
//						.and("angerTeamTotal").divide("angerTeamCount").as("angerTeamTotal")
//						.and("joyTeamTotal").divide("joyTeamCount").as("joyTeamTotal")
//						.and("sadnessTeamTotal").divide("sadnessTeamCount").as("sadnessTeamTotal")
//						.and("tentativeTeamTotal").divide("tentativeTeamCount").as("tentativeTeamTotal")
//						.and("analyticalTeamTotal").divide("analyticalTeamCount").as("analyticalTeamTotal")
//						.and("confidentTeamTotal").divide("confidentTeamCount").as("confidentTeamTotal")
//						.and("fearTeamTotal").divide("fearTeamCount").as("fearTeamTotal")
//						.and("angerTeamTotalsnt").divide("angerTeamCountsnt").as("angerTeamTotalsnt")
//						.and("joyTeamTotalsnt").divide("joyTeamCountsnt").as("joyTeamTotalsnt")
//						.and("sadnessTeamTotalsnt").divide("sadnessTeamCountsnt").as("sadnessTeamTotalsnt")
//						.and("tentativeTeamTotalsnt").divide("tentativeTeamCountsnt").as("tentativeTeamTotalsnt")
//						.and("analyticalTeamTotalsnt").divide("analyticalTeamCountsnt").as("analyticalTeamTotalsnt")
//						.and("confidentTeamTotalsnt").divide("confidentTeamCountsnt").as("confidentTeamTotalsnt")
//						.and("fearTeamTotalsnt").divide("fearTeamCountsnt").as("fearTeamTotalsnt")
//						.and("angerTeamTotalrcv").divide("angerTeamCountrcv").as("angerTeamTotalrcv")
//						.and("joyTeamTotalrcv").divide("joyTeamCountrcv").as("joyTeamTotalrcv")
//						.and("sadnessTeamTotalrcv").divide("sadnessTeamCountrcv").as("sadnessTeamTotalrcv")
//						.and("tentativeTeamTotalrcv").divide("tentativeTeamCountrcv").as("tentativeTeamTotalrcv")
//						.and("analyticalTeamTotalrcv").divide("analyticalTeamCountrcv").as("analyticalTeamTotalrcv")
//						.and("confidentTeamTotalrcv").divide("confidentTeamCountrcv").as("confidentTeamTotalrcv")
//						.and("fearTeamTotalrcv").divide("fearTeamCountrcv").as("fearTeamTotalrcv"),
						sortByTone
					
						
							);
					
					AggregationResults<DBObject> groupResults2 
					= mongoTemplate.aggregate(aggregation2, DailyEmployeeEmailToneBO.class, DBObject.class);
					List<DBObject> result2 = groupResults2.getMappedResults();
					
					mongoTemplate.dropCollection("temp");
					
					for(DBObject dbObject : result2) {
						
					EmployeePersonalDataDTO employeePersonalDataDTO = new EmployeePersonalDataDTO();
					// Average of All Self Scores 
					ToneOfMail toneOfMail = new ToneOfMail();
					// find percentage of all avg tone
					Double anger = ((Number)dbObject.get("angerTeamTotal")).doubleValue();
					Double joy = ((Number)dbObject.get("joyTeamTotal")).doubleValue();
					Double sadn =	((Number)dbObject.get("sadnessTeamTotal")).doubleValue();
					Double ten = ((Number)dbObject.get("tentativeTeamTotal")).doubleValue();
					Double anal = ((Number)dbObject.get("analyticalTeamTotal")).doubleValue();
					Double conf = ((Number)dbObject.get("confidentTeamTotal")).doubleValue();
					Double fear = ((Number)dbObject.get("fearTeamTotal")).doubleValue();
					
					Double total = anger+joy+sadn+ten+anal+conf+fear;
					Double angerP = (double)Math.round(((anger)*100)/(total));
					Double joyP = (double)Math.round(((joy)*100)/(total));
					Double sadnP = (double)Math.round(((sadn)*100)/(total));
					Double tenP = (double)Math.round(((ten)*100)/(total));
					Double analP = (double)Math.round(((anal)*100)/(total));
					Double confP = (double)Math.round(((conf)*100)/(total));
					Double fearP =  (double)Math.round(((fear)*100)/(total));
					
					toneOfMail.setAnger(angerP);
					toneOfMail.setJoy(joyP);
					toneOfMail.setSadness(sadnP);
					toneOfMail.setTentative(tenP);
					toneOfMail.setAnalytical(analP);
					toneOfMail.setConfident(confP);
					toneOfMail.setFear(fearP);
					
					/*toneOfMail.setAnger((double)Math.round(((Number)dbObject.get("angerTeamTotal")).doubleValue()));
					toneOfMail.setJoy((double)Math.round(((Number)dbObject.get("joyTeamTotal")).doubleValue()));
					toneOfMail.setSadness((double)Math.round(((Number)dbObject.get("sadnessTeamTotal")).doubleValue()));
					toneOfMail.setTentative((double)Math.round(((Number)dbObject.get("tentativeTeamTotal")).doubleValue()));
					toneOfMail.setAnalytical((double)Math.round(((Number)dbObject.get("analyticalTeamTotal")).doubleValue()));
					toneOfMail.setConfident((double)Math.round(((Number)dbObject.get("confidentTeamTotal")).doubleValue()));
					toneOfMail.setFear((double)Math.round(((Number)dbObject.get("fearTeamTotal")).doubleValue()));*/
					
					//rcv
					ToneOfMail toneOfMail1 = new ToneOfMail();
					
					// find percentage of rcv avg tone
					Double angerc = ((Number)dbObject.get("angerTeamTotalrcv")).doubleValue();
					Double joyr = ((Number)dbObject.get("joyTeamTotalrcv")).doubleValue();
					Double sadnr =	((Number)dbObject.get("sadnessTeamTotalrcv")).doubleValue();
					Double tenr = ((Number)dbObject.get("tentativeTeamTotalrcv")).doubleValue();
					Double analr = ((Number)dbObject.get("analyticalTeamTotalrcv")).doubleValue();
					Double confr = ((Number)dbObject.get("confidentTeamTotalrcv")).doubleValue();
					Double fearr = ((Number)dbObject.get("fearTeamTotalrcv")).doubleValue();
					
					Double totalr = angerc+joyr+sadnr+tenr+analr+confr+fearr;
					Double angercP = (double)Math.round(((angerc)*100)/(totalr));
					Double joyrP = (double)Math.round(((joyr)*100)/(totalr));
					Double sadnrP = (double)Math.round(((sadnr)*100)/(totalr));
					Double tenrP = (double)Math.round(((tenr)*100)/(totalr));
					Double analrP = (double)Math.round(((analr)*100)/(totalr));
					Double confrP = (double)Math.round(((confr)*100)/(totalr));
					Double fearrP =  (double)Math.round(((fearr)*100)/(totalr));
					
					toneOfMail1.setAnger(angercP);
					toneOfMail1.setJoy(joyrP);
					toneOfMail1.setSadness(sadnrP);
					toneOfMail1.setTentative(tenrP);
					toneOfMail1.setAnalytical(analrP);
					toneOfMail1.setConfident(confrP);
					toneOfMail1.setFear(fearrP);
					
					/*
					toneOfMail1.setAnger((double)Math.round(((Number)dbObject.get("angerTeamTotalrcv")).doubleValue()));
					toneOfMail1.setJoy((double)Math.round(((Number)dbObject.get("joyTeamTotalrcv")).doubleValue()));
					toneOfMail1.setSadness((double)Math.round(((Number)dbObject.get("sadnessTeamTotalrcv")).doubleValue()));
					toneOfMail1.setTentative((double)Math.round(((Number)dbObject.get("tentativeTeamTotalrcv")).doubleValue()));
					toneOfMail1.setAnalytical((double)Math.round(((Number)dbObject.get("analyticalTeamTotalrcv")).doubleValue()));
					toneOfMail1.setConfident((double)Math.round(((Number)dbObject.get("confidentTeamTotalrcv")).doubleValue()));
					toneOfMail1.setFear((double)Math.round(((Number)dbObject.get("fearTeamTotalrcv")).doubleValue()));
					*/
					
					//snt
					ToneOfMail toneOfMail2 = new ToneOfMail();
					
					Double angersnt = ((Number)dbObject.get("angerTeamTotalsnt")).doubleValue();
					Double joyrsnt = ((Number)dbObject.get("joyTeamTotalsnt")).doubleValue();
					Double sadnrsnt =	((Number)dbObject.get("sadnessTeamTotalsnt")).doubleValue();
					Double tenrsnt = ((Number)dbObject.get("tentativeTeamTotalsnt")).doubleValue();
					Double analrsnt = ((Number)dbObject.get("analyticalTeamTotalsnt")).doubleValue();
					Double confrsnt = ((Number)dbObject.get("confidentTeamTotalsnt")).doubleValue();
					Double fearrsnt = ((Number)dbObject.get("fearTeamTotalsnt")).doubleValue();
					
					Double totalsnt = angersnt+joyrsnt+sadnrsnt+tenrsnt+analrsnt+confrsnt+fearrsnt;
					Double angersntP = (double)Math.round(((angersnt)*100)/(totalsnt));
					Double joyrsntP = (double)Math.round(((joyrsnt)*100)/(totalsnt));
					Double sadnrsntP = (double)Math.round(((sadnrsnt)*100)/(totalsnt));
					Double tenrsntP = (double)Math.round(((tenrsnt)*100)/(totalsnt));
					Double analrsntP = (double)Math.round(((analrsnt)*100)/(totalsnt));
					Double confrsntP = (double)Math.round(((confrsnt)*100)/(totalsnt));
					Double fearrsntP =  (double)Math.round(((fearrsnt)*100)/(totalsnt));
					
					toneOfMail2.setAnger(angersntP);
					toneOfMail2.setJoy(joyrsntP);
					toneOfMail2.setSadness(sadnrsntP);
					toneOfMail2.setTentative(tenrsntP);
					toneOfMail2.setAnalytical(analrsntP);
					toneOfMail2.setConfident(confrsntP);
					toneOfMail2.setFear(fearrsntP);
					
				/*	toneOfMail2.setAnger((double)Math.round(((Number)dbObject.get("angerTeamTotalsnt")).doubleValue()));
					toneOfMail2.setJoy((double)Math.round(((Number)dbObject.get("joyTeamTotalsnt")).doubleValue()));
					toneOfMail2.setSadness((double)Math.round(((Number)dbObject.get("sadnessTeamTotalsnt")).doubleValue()));
					toneOfMail2.setTentative((double)Math.round(((Number)dbObject.get("tentativeTeamTotalsnt")).doubleValue()));
					toneOfMail2.setAnalytical((double)Math.round(((Number)dbObject.get("analyticalTeamTotalsnt")).doubleValue()));
					toneOfMail2.setConfident((double)Math.round(((Number)dbObject.get("confidentTeamTotalsnt")).doubleValue()));
					toneOfMail2.setFear((double)Math.round(((Number)dbObject.get("fearTeamTotalsnt")).doubleValue()));
					*/
//					employeePersonalDataDTO.setEmployeeName(dbObject.get("employeeName").toString());
					employeePersonalDataDTO.setEmployeeName(dbObject.get("name").toString());
					
					employeePersonalDataDTO.setEmployeeId(dbObject.get("_id").toString());
					
//					List<BSONObject> bsonObject = (List<BSONObject>)dbObject.get("role");
					List<BSONObject> bsonObject = (List<BSONObject>)dbObject.get("employeeRoleBOs");
					if (bsonObject.get(0).containsField("designation")) {
						
						bsonObject.get(0).get("designation");
						employeePersonalDataDTO.setDesignation(bsonObject.get(0).get("designation").toString());
						
						
					}
					
					
					//employeePersonalDataDTO.setDesignation(((DBObject)JSON.parse(dbObject.get("role").toString())).get("designation").toString());
		
					
					employeePersonalDataDTO.setToneOfTeamMail(toneOfMail);
					employeePersonalDataDTO.setToneOfTeamReceiveMail(toneOfMail1);
					employeePersonalDataDTO.setToneOfTeamSentMail(toneOfMail2);
					employeePersonalDataDTO.setNoOfMail(((Number)dbObject.get("totalTeamMail")).longValue());
					employeePersonalDataDTO.setNoOfReceiveMail(((Number)dbObject.get("totalTeamMailRecevied")).longValue());
					employeePersonalDataDTO.setNoOfSentMail(((Number)dbObject.get("totalTeamMailSent")).longValue());
				
					listOfEmployeeData.add(employeePersonalDataDTO);
						
					}
			
						  
					empldata.setListOfEmployee(listOfEmployeeData);
					empldata.setPageNumber(clientDataDTO.getPageNumber());
			
			return empldata;
		}
		
		
	
	//client dashboard data
	
		public EmployeePersonalDataDTO getClientDashBoard1(ClientDataDTO clientDataDTO){
				
				EmployeePersonalDataDTO employeePersonalDataDTO = new EmployeePersonalDataDTO();
			

			    UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
						.getContext().getAuthentication();
				
				String emailId = (String) authObj.getUserSessionInformation().get(
						EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
				
				EmployeeBO employeeBO = employeeRepository.findOne(emailId);
				
				List<EmployeePersonalDataDTO> list = new ArrayList<EmployeePersonalDataDTO>();
				
				ProjectionOperation projectionOperation1 = Aggregation.project()
						.andExpression("clients").as("teamName")
						.andExclude("_id");
				
				Aggregation aggregation2 = Aggregation.newAggregation(
		                
						Aggregation.match(Criteria.where("employeeIdFK").is(employeeBO.getEmployeeId())),
						Aggregation.unwind("$companyList"),
						Aggregation.match(Criteria.where("companyList.companyId").is(clientDataDTO.getCompanyId())),
						//projectionOperation1,
						Aggregation.group("employeeIdFK")
						.sum("companyList.allMailScore.anger").as("angerTeamTotal").sum("companyList.allMailScore.angerCount")
						.as("angerTeamCount").sum("companyList.allMailScore.joy").as("joyTeamTotal").sum("companyList.allMailScore.joyCount")
						.as("joyTeamCount").sum("companyList.allMailScore.sadness").as("sadnessTeamTotal").sum("companyList.allMailScore.sadnessCount")
						.as("sadnessTeamCount").sum("companyList.allMailScore.tentative").as("tentativeTeamTotal").sum("companyList.allMailScore.tentativeCount")
						.as("tentativeTeamCount").sum("companyList.allMailScore.analytical").as("analyticalTeamTotal").sum("companyList.allMailScore.analyticalCount")
						.as("analyticalTeamCount").sum("companyList.allMailScore.confident").as("confidentTeamTotal").sum("companyList.allMailScore.confidentCount")
						.as("confidentTeamCount").sum("companyList.allMailScore.fear").as("fearTeamTotal").sum("companyList.allMailScore.fearCount")
						.as("fearTeamCount")
						.sum("companyList.receiveMailScore.anger").as("angerTeamTotalrcv").sum("companyList.receiveMailScore.angerCount")
						.as("angerTeamCountrcv").sum("companyList.receiveMailScore.joy").as("joyTeamTotalrcv").sum("companyList.receiveMailScore.joyCount")
						.as("joyTeamCountrcv").sum("companyList.receiveMailScore.sadness").as("sadnessTeamTotalrcv").sum("companyList.receiveMailScore.sadnessCount")
						.as("sadnessTeamCountrcv").sum("companyList.receiveMailScore.tentative").as("tentativeTeamTotalrcv").sum("companyList.receiveMailScore.tentativeCount")
						.as("tentativeTeamCountrcv").sum("companyList.receiveMailScore.analytical").as("analyticalTeamTotalrcv").sum("companyList.receiveMailScore.analyticalCount")
						.as("analyticalTeamCountrcv").sum("companyList.receiveMailScore.confident").as("confidentTeamTotalrcv").sum("companyList.receiveMailScore.confidentCount")
						.as("confidentTeamCountrcv").sum("companyList.receiveMailScore.fear").as("fearTeamTotalrcv").sum("companyList.receiveMailScore.fearCount")
						.as("fearTeamCountrcv")
						.sum("companyList.sentMailScore.anger").as("angerTeamTotalsnt").sum("companyList.sentMailScore.angerCount")
						.as("angerTeamCountsnt").sum("companyList.sentMailScore.joy").as("joyTeamTotalsnt").sum("companyList.sentMailScore.joyCount")
						.as("joyTeamCountsnt").sum("companyList.sentMailScore.sadness").as("sadnessTeamTotalsnt").sum("companyList.sentMailScore.sadnessCount")
						.as("sadnessTeamCountsnt").sum("companyList.sentMailScore.tentative").as("tentativeTeamTotalsnt").sum("companyList.sentMailScore.tentativeCount")
						.as("tentativeTeamCountsnt").sum("companyList.sentMailScore.analytical").as("analyticalTeamTotalsnt").sum("companyList.sentMailScore.analyticalCount")
						.as("analyticalTeamCountsnt").sum("companyList.sentMailScore.confident").as("confidentTeamTotalsnt").sum("companyList.sentMailScore.confidentCount")
						.as("confidentTeamCountsnt").sum("companyList.sentMailScore.fear").as("fearTeamTotalsnt").sum("companyList.sentMailScore.fearCount")
						.as("fearTeamCountsnt")
						.sum("companyList.totalMail").as("totalTeamMail")
						.sum("companyList.totalMailRecevied").as("totalTeamMailRecevied")
						.sum("companyList.totalMailSent").as("totalTeamMailSent")
						
							);
					
					AggregationResults<AggregationPojo> groupResults 
					= mongoTemplate.aggregate(aggregation2, TeamClientInteractionBO.class, AggregationPojo.class);
					List<AggregationPojo> result = groupResults.getMappedResults();
					
				//	clientCompany Team Scores
					Double angerFinal = 0.0d;
					Double joyFinal = 0.0d;
					Double sadnessFinal = 0.0d;
					Double tentativeFinal = 0.0d;
					Double analyticalFinal = 0.0d;
					Double confidentFinal = 0.0d;
					Double fearFinal = 0.0d;
					
					if(result != null) {
					try {
					if(result.get(0).getAngerTeamCount() != 0)
					angerFinal = result.get(0).getAngerTeamTotal()/result.get(0).getAngerTeamCount();
					}catch(Exception e) {
//						e.printStackTrace();
					}
					try {
					if(result.get(0).getJoyTeamCount() != 0)
					joyFinal = result.get(0).getJoyTeamTotal()/result.get(0).getJoyTeamCount();
					}catch(Exception e) {
//						e.printStackTrace();
					}
					try {
					if(result.get(0).getSadnessTeamCount() != 0)
					sadnessFinal = result.get(0).getSadnessTeamTotal()/result.get(0).getSadnessTeamCount();
					}catch(Exception e) {
//						e.printStackTrace();
					}
					try {
					if(result.get(0).getTentativeTeamCount() != 0)
					tentativeFinal = result.get(0).getTentativeTeamTotal()/result.get(0).getTentativeTeamCount();
					}catch(Exception e) {
//						e.printStackTrace();
					}
					try {
					if(result.get(0).getAnalyticalTeamCount() != 0)
					analyticalFinal = result.get(0).getAnalyticalTeamTotal()/result.get(0).getAnalyticalTeamCount();
					}catch(Exception e) {
//						e.printStackTrace();
					}
					try {
					if(result.get(0).getConfidentTeamCount() != 0)
					confidentFinal = result.get(0).getConfidentTeamTotal()/result.get(0).getConfidentTeamCount();
					}catch(Exception e) {
//						e.printStackTrace();
					}
					try {
					if(result.get(0).getFearTeamCount() != 0)
					fearFinal = result.get(0).getFearTeamTotal()/result.get(0).getFearTeamCount();
					}catch(Exception e) {
//						e.printStackTrace();
					}
					}
					ToneOfMail toneOfMail = new ToneOfMail();
					
					Double total = angerFinal+joyFinal+sadnessFinal+tentativeFinal+analyticalFinal+confidentFinal+fearFinal;
					
					Double angerP = 0.0d;
					Double joyP = 0.0d;
					Double sadnP = 0.0d;
					Double tenP = 0.0d;
					Double analP = 0.0d;
					Double confP = 0.0d;
					Double fearP = 0.0d;
					
					if(total > 0) {
					angerP = (double)Math.round(((angerFinal)*100)/(total));
					joyP = (double)Math.round(((joyFinal)*100)/(total));
					sadnP = (double)Math.round(((sadnessFinal)*100)/(total));
					tenP = (double)Math.round(((tentativeFinal)*100)/(total));
					analP = (double)Math.round(((analyticalFinal)*100)/(total));
					confP = (double)Math.round(((confidentFinal)*100)/(total));
					fearP =  (double)Math.round(((fearFinal)*100)/(total));
					}
					toneOfMail.setAnger(angerP);
					toneOfMail.setJoy(joyP);
					toneOfMail.setSadness(sadnP);
					toneOfMail.setTentative(tenP);
					toneOfMail.setAnalytical(analP);
					toneOfMail.setConfident(confP);
					toneOfMail.setFear(fearP);
					
					/*
					toneOfMail.setAnger(angerFinal);
					toneOfMail.setJoy(joyFinal);
					toneOfMail.setSadness(sadnessFinal);
					toneOfMail.setTentative(tentativeFinal);
					toneOfMail.setAnalytical(analyticalFinal);
					toneOfMail.setConfident(confidentFinal);
					toneOfMail.setFear(fearFinal);*/
					
					//rcv
					
					Double angerFinal1 = 0.0d;
					Double joyFinal1 = 0.0d;
					Double sadnessFinal1 = 0.0d;
					Double tentativeFinal1 = 0.0d;
					Double analyticalFinal1 = 0.0d;
					Double confidentFinal1 = 0.0d;
					Double fearFinal1 = 0.0d;
					
					if(result != null) {
					try {
					if(result.get(0).getAngerTeamCountrcv() != 0 )
					angerFinal1 = result.get(0).getAngerTeamTotalrcv()/result.get(0).getAngerTeamCountrcv();
					}catch(Exception e) {
//						e.printStackTrace();
					}
					try {
					if(result.get(0).getJoyTeamCountrcv() != 0)
					joyFinal1 = result.get(0).getJoyTeamTotalrcv()/result.get(0).getJoyTeamCountrcv();
					}catch(Exception e) {
//						e.printStackTrace();
					}
					try {
					if(result.get(0).getSadnessTeamCountrcv() != 0)
					sadnessFinal1 =result.get(0).getSadnessTeamTotalrcv()/result.get(0).getSadnessTeamCountrcv();
					}catch(Exception e) {
//						e.printStackTrace();
					}
					try {
					if(result.get(0).getTentativeTeamCountrcv() != 0)
					tentativeFinal1 = result.get(0).getTentativeTeamTotalrcv()/result.get(0).getTentativeTeamCountrcv();
					}catch(Exception e) {
//						e.printStackTrace();
					}
					try {
					if(result.get(0).getAnalyticalTeamCountrcv() != 0)
					analyticalFinal1 = result.get(0).getAnalyticalTeamTotalrcv()/result.get(0).getAnalyticalTeamCountrcv();
					}catch(Exception e) {
//						e.printStackTrace();
					}
					try {
					if(result.get(0).getConfidentTeamCountrcv() != 0)
					confidentFinal1 = result.get(0).getConfidentTeamTotalrcv()/result.get(0).getConfidentTeamCountrcv();
					}catch(Exception e) {
//						e.printStackTrace();
					}
					try {
					if(result.get(0).getFearTeamCountrcv() != 0)
					fearFinal1 =result.get(0).getFearTeamTotalrcv()/result.get(0).getFearTeamCountrcv();
					}catch(Exception e) {
//						e.printStackTrace();
					}
					}
					ToneOfMail toneOfMail1 = new ToneOfMail();
					
					Double totalr = angerFinal1+joyFinal1+sadnessFinal1+tentativeFinal1+analyticalFinal1+confidentFinal1+fearFinal1;
					
					Double angercP = 0.0d;
					Double joyrP = 0.0d;
					Double sadnrP = 0.0d;
					Double tenrP = 0.0d;
					Double analrP = 0.0d;
					Double confrP = 0.0d;
					Double fearrP = 0.0d;
					
					if(totalr > 0) {
					angercP = (double)Math.round(((angerFinal1)*100)/(totalr));
					joyrP = (double)Math.round(((joyFinal1)*100)/(totalr));
					sadnrP = (double)Math.round(((sadnessFinal1)*100)/(totalr));
					tenrP = (double)Math.round(((tentativeFinal1)*100)/(totalr));
					analrP = (double)Math.round(((analyticalFinal1)*100)/(totalr));
					confrP = (double)Math.round(((confidentFinal1)*100)/(totalr));
					fearrP =  (double)Math.round(((fearFinal1)*100)/(totalr));
					}
					toneOfMail1.setAnger(angercP);
					toneOfMail1.setJoy(joyrP);
					toneOfMail1.setSadness(sadnrP);
					toneOfMail1.setTentative(tenrP);
					toneOfMail1.setAnalytical(analrP);
					toneOfMail1.setConfident(confrP);
					toneOfMail1.setFear(fearrP);
					
					
					/*toneOfMail1.setAnger(angerFinal1);
					toneOfMail1.setJoy(joyFinal1);
					toneOfMail1.setSadness(sadnessFinal1);
					toneOfMail1.setTentative(tentativeFinal1);
					toneOfMail1.setAnalytical(analyticalFinal1);
					toneOfMail1.setConfident(confidentFinal1);
					toneOfMail1.setFear(fearFinal1);*/
					
					//snt
					
					Double angerFinal2 = 0.0d;
					Double joyFinal2 = 0.0d;
					Double sadnessFinal2 = 0.0d;
					Double tentativeFinal2 = 0.0d;
					Double analyticalFinal2 = 0.0d;
					Double confidentFinal2 = 0.0d;
					Double fearFinal2 = 0.0d;
					
					if(result != null) {
					try {
					if(result.get(0).getAngerTeamCountsnt() != 0)
					angerFinal2 = result.get(0).getAngerTeamTotalsnt()/result.get(0).getAngerTeamCountsnt();
					}catch(Exception e) {
//						e.printStackTrace();
					}
					try {
					if(result.get(0).getJoyTeamCountsnt() != 0)
					joyFinal2 = result.get(0).getJoyTeamTotalsnt()/result.get(0).getJoyTeamCountsnt();
					}catch(Exception e) {
//						e.printStackTrace();
					}
					try {
					if(result.get(0).getSadnessTeamCountsnt() != 0)
					sadnessFinal2 = result.get(0).getSadnessTeamTotalsnt()/result.get(0).getSadnessTeamCountsnt();
					}catch(Exception e) {
//						e.printStackTrace();
					}
					try {
					if(result.get(0).getTentativeTeamCountsnt() != 0)
					tentativeFinal2 = result.get(0).getTentativeTeamTotalsnt()/result.get(0).getTentativeTeamCountsnt();
					}catch(Exception e) {
//						e.printStackTrace();
					}
					try {
					if(result.get(0).getAnalyticalTeamCountsnt() != 0)
					analyticalFinal2 = result.get(0).getAnalyticalTeamTotalsnt()/result.get(0).getAnalyticalTeamCountsnt();
					}catch(Exception e) {
//						e.printStackTrace();
					}
					try {
					if(result.get(0).getConfidentTeamCountsnt() != 0)
					confidentFinal2 = result.get(0).getConfidentTeamTotalsnt()/result.get(0).getConfidentTeamCountsnt();
					}catch(Exception e) {
//						e.printStackTrace();
					}
					try {
					if(result.get(0).getFearTeamCountsnt() != 0)
					fearFinal2 = result.get(0).getFearTeamTotalsnt()/result.get(0).getFearTeamCountsnt();
					}catch(Exception e) {
//						e.printStackTrace();
					}
					}
					ToneOfMail toneOfMail2 = new ToneOfMail();
					
					Double angersntP = 0.0d;
					Double joyrsntP = 0.0d;
					Double sadnrsntP = 0.0d;
					Double tenrsntP = 0.0d;
					Double analrsntP = 0.0d;
					Double confrsntP = 0.0d;
					Double fearrsntP = 0.0d;
					
					Double totalsnt = angerFinal2+joyFinal2+sadnessFinal2+tentativeFinal2+analyticalFinal2+confidentFinal2+fearFinal2;
					if(totalsnt > 0) {
					angersntP = (double)Math.round(((angerFinal2)*100)/(totalsnt));
					joyrsntP = (double)Math.round(((joyFinal2)*100)/(totalsnt));
					sadnrsntP = (double)Math.round(((sadnessFinal2)*100)/(totalsnt));
					tenrsntP = (double)Math.round(((tentativeFinal2)*100)/(totalsnt));
					analrsntP = (double)Math.round(((analyticalFinal2)*100)/(totalsnt));
					confrsntP = (double)Math.round(((confidentFinal2)*100)/(totalsnt));
					fearrsntP =  (double)Math.round(((fearFinal2)*100)/(totalsnt));
					}
					toneOfMail2.setAnger(angersntP);
					toneOfMail2.setJoy(joyrsntP);
					toneOfMail2.setSadness(sadnrsntP);
					toneOfMail2.setTentative(tenrsntP);
					toneOfMail2.setAnalytical(analrsntP);
					toneOfMail2.setConfident(confrsntP);
					toneOfMail2.setFear(fearrsntP);	
					
					
					
					
					
					
				/*	
					
					
					Double angerFinal = (double)Math.round(result.get(0).getAngerTeamTotal()/result.get(0).getAngerTeamCount());
					Double joyFinal = (double)Math.round(result.get(0).getJoyTeamTotal()/result.get(0).getJoyTeamCount());
					Double sadnessFinal = (double)Math.round(result.get(0).getSadnessTeamTotal()/result.get(0).getSadnessTeamCount());
					Double tentativeFinal = (double)Math.round(result.get(0).getTentativeTeamTotal()/result.get(0).getTentativeTeamCount());
					Double analyticalFinal = (double)Math.round(result.get(0).getAnalyticalTeamTotal()/result.get(0).getAnalyticalTeamCount());
					Double confidentFinal = (double)Math.round(result.get(0).getConfidentTeamTotal()/result.get(0).getConfidentTeamCount());
					Double fearFinal = (double)Math.round(result.get(0).getFearTeamTotal()/result.get(0).getFearTeamCount());
					
					ToneOfMail toneOfMail = new ToneOfMail();
					toneOfMail.setAnger(angerFinal);
					toneOfMail.setJoy(joyFinal);
					toneOfMail.setSadness(sadnessFinal);
					toneOfMail.setTentative(tentativeFinal);
					toneOfMail.setAnalytical(analyticalFinal);
					toneOfMail.setConfident(confidentFinal);
					toneOfMail.setFear(fearFinal);
					
					//rcv
					
					Double angerFinal1 = (double)Math.round(result.get(0).getAngerTeamTotalrcv()/result.get(0).getAngerTeamCountrcv());
					Double joyFinal1 = (double)Math.round(result.get(0).getJoyTeamTotalrcv()/result.get(0).getJoyTeamCountrcv());
					Double sadnessFinal1 = (double)Math.round(result.get(0).getSadnessTeamTotalrcv()/result.get(0).getSadnessTeamCountrcv());
					Double tentativeFinal1 = (double)Math.round(result.get(0).getTentativeTeamTotalrcv()/result.get(0).getTentativeTeamCountrcv());
					Double analyticalFinal1 = (double)Math.round(result.get(0).getAnalyticalTeamTotalrcv()/result.get(0).getAnalyticalTeamCountrcv());
					Double confidentFinal1 = (double)Math.round(result.get(0).getConfidentTeamTotalrcv()/result.get(0).getConfidentTeamCountrcv());
					Double fearFinal1 = (double)Math.round(result.get(0).getFearTeamTotalrcv()/result.get(0).getFearTeamCountrcv());
					
					ToneOfMail toneOfMail1 = new ToneOfMail();
					toneOfMail1.setAnger(angerFinal1);
					toneOfMail1.setJoy(joyFinal1);
					toneOfMail1.setSadness(sadnessFinal1);
					toneOfMail1.setTentative(tentativeFinal1);
					toneOfMail1.setAnalytical(analyticalFinal1);
					toneOfMail1.setConfident(confidentFinal1);
					toneOfMail1.setFear(fearFinal1);
					
					//snt
					
					Double angerFinal2 = (double)Math.round(result.get(0).getAngerTeamTotalsnt()/result.get(0).getAngerTeamCountsnt());
					Double joyFinal2 = (double)Math.round(result.get(0).getJoyTeamTotalsnt()/result.get(0).getJoyTeamCountsnt());
					Double sadnessFinal2 = (double)Math.round(result.get(0).getSadnessTeamTotalsnt()/result.get(0).getSadnessTeamCountsnt());
					Double tentativeFinal2 = (double)Math.round(result.get(0).getTentativeTeamTotalsnt()/result.get(0).getTentativeTeamCountsnt());
					Double analyticalFinal2 = (double)Math.round(result.get(0).getAnalyticalTeamTotalsnt()/result.get(0).getAnalyticalTeamCountsnt());
					Double confidentFinal2 = (double)Math.round(result.get(0).getConfidentTeamTotalsnt()/result.get(0).getConfidentTeamCountsnt());
					Double fearFinal2 = (double)Math.round(result.get(0).getFearTeamTotalsnt()/result.get(0).getFearTeamCountsnt());
					
					ToneOfMail toneOfMail2 = new ToneOfMail();
					toneOfMail2.setAnger(angerFinal2);
					toneOfMail2.setJoy(joyFinal2);
					toneOfMail2.setSadness(sadnessFinal2);
					toneOfMail2.setTentative(tentativeFinal2);
					toneOfMail2.setAnalytical(analyticalFinal2);
					toneOfMail2.setConfident(confidentFinal2);
					toneOfMail2.setFear(fearFinal2);*/
					
							
					employeePersonalDataDTO.setToneOfClientMail(toneOfMail);
					employeePersonalDataDTO.setToneOfClientReceiveMail(toneOfMail1);
					employeePersonalDataDTO.setToneOfClientsentMail(toneOfMail2);
					try {
					employeePersonalDataDTO.setNoOfMail(result.get(0).getTotalTeamMail());
					}catch(Exception e) {
						employeePersonalDataDTO.setNoOfMail(0l);
					}
					try {
					employeePersonalDataDTO.setNoOfReceiveMail(result.get(0).getTotalTeamMailRecevied());
					}catch(Exception e) {
						employeePersonalDataDTO.setNoOfReceiveMail(0l);
					}
					try{
					employeePersonalDataDTO.setNoOfSentMail(result.get(0).getTotalTeamMailSent());
					}catch(Exception e) {
						employeePersonalDataDTO.setNoOfSentMail(0l);
					}
					
					OrganisationBO org = companyRepository.findOne(clientDataDTO.getCompanyId());
					
					employeePersonalDataDTO.setCompanyName(org.getCompanyName());
				
				
				//new sorting changes
				
					List<SortOperation> listSort = new ArrayList<SortOperation>();
					SortOperation sortByTone = null;
				
					
					if(clientDataDTO.getSearchCriteria()!=null){
						if (clientDataDTO.getSortType().equalsIgnoreCase("DSC")) {
					if(clientDataDTO.getSearchCriteria().contains("anger")) {
						
						 sortByTone = Aggregation.sort(Sort.Direction.DESC,"angerTeamTotal");
						 listSort.add(0, sortByTone);
					}
					if(clientDataDTO.getSearchCriteria().contains("joy")) {
						if (listSort.size()>0) {
							sortByTone = listSort.get(0).and(Sort.Direction.DESC,"joyTeamTotal");
							 listSort.add(0, sortByTone);
						}else {
							 sortByTone = Aggregation.sort(Sort.Direction.DESC,"joyTeamTotal");
							 listSort.add(0, sortByTone);
						}
						
					}
					if(clientDataDTO.getSearchCriteria().contains("sadness")) {
						if (listSort.size()>0) {
							sortByTone = listSort.get(0).and(Sort.Direction.DESC,"sadnessTeamTotal");
							 listSort.add(0, sortByTone);
						}else {
							 sortByTone = Aggregation.sort(Sort.Direction.DESC,"sadnessTeamTotal");
							 listSort.add(0, sortByTone);
						}
						
					}
					if(clientDataDTO.getSearchCriteria().contains("tentative")) {
						if (listSort.size()>0) {
							sortByTone = listSort.get(0).and(Sort.Direction.DESC,"tentativeTeamTotal");
							 listSort.add(0, sortByTone);
						}else {
							 sortByTone = Aggregation.sort(Sort.Direction.DESC,"tentativeTeamTotal");
							 listSort.add(0, sortByTone);
						}
						
					}
					if(clientDataDTO.getSearchCriteria().contains("analytical")) {
						if (listSort.size()>0) {
							sortByTone = listSort.get(0).and(Sort.Direction.DESC,"analyticalTeamTotal");
							 listSort.add(0, sortByTone);
						}else {
							 sortByTone = Aggregation.sort(Sort.Direction.DESC,"analyticalTeamTotal");
							 listSort.add(0, sortByTone);
						}
						
					}
					if(clientDataDTO.getSearchCriteria().contains("confident")) {
						if (listSort.size()>0) {
							sortByTone = listSort.get(0).and(Sort.Direction.DESC,"confidentTeamTotal");
							 listSort.add(0, sortByTone);
						}else {
							 sortByTone = Aggregation.sort(Sort.Direction.DESC,"confidentTeamTotal");
							 listSort.add(0, sortByTone);
						}
						
					}
					if(clientDataDTO.getSearchCriteria().contains("fear")) {
						if (listSort.size()>0) {
							sortByTone = listSort.get(0).and(Sort.Direction.DESC,"fearTeamTotal");
							 listSort.add(0, sortByTone);
						}else {
							 sortByTone = Aggregation.sort(Sort.Direction.DESC,"fearTeamTotal");
							 listSort.add(0, sortByTone);
						}
						
					}
					if(clientDataDTO.getSearchCriteria().contains("teamMail")) {
						if (listSort.size()>0) {
							sortByTone = listSort.get(0).and(Sort.Direction.DESC,"totalTeamMail");
							 listSort.add(0, sortByTone);
						}else {
							 sortByTone = Aggregation.sort(Sort.Direction.DESC,"totalTeamMail");
							 listSort.add(0, sortByTone);
						}
						
					}
					/*if (clientDataDTO.getSearchCriteria().contains("alphabetically")) {
						
						if (listSort.size()>0) {
							sortByTone = listSort.get(0).and(Sort.Direction.DESC,"employeeName");
							 listSort.add(0, sortByTone);
						}else {
							 sortByTone = Aggregation.sort(Sort.Direction.ASC,"employeeName");
							 listSort.add(0, sortByTone);
						}
					}
					if (clientDataDTO.getSearchCriteria().contains("designation")) {
						
						if (listSort.size()>0) {
							sortByTone = listSort.get(0).and(Sort.Direction.ASC,"designation");
							 listSort.add(0, sortByTone);
						}else {
							 sortByTone = Aggregation.sort(Sort.Direction.DESC,"designation");
							 listSort.add(0, sortByTone);
						}
					}*/
					
		}
					
		}
					
					
					// asc order for client
					
					
						
						if(clientDataDTO.getSearchCriteria()!=null){
							if (clientDataDTO.getSortType().equalsIgnoreCase("ASC")) {
						if(clientDataDTO.getSearchCriteria().contains("anger")) {
							
							 sortByTone = Aggregation.sort(Sort.Direction.ASC,"angerTeamTotal");
							 listSort.add(0, sortByTone);
						}
						if(clientDataDTO.getSearchCriteria().contains("joy")) {
							if (listSort.size()>0) {
								sortByTone = listSort.get(0).and(Sort.Direction.ASC,"joyTeamTotal");
								 listSort.add(0, sortByTone);
							}else {
								 sortByTone = Aggregation.sort(Sort.Direction.ASC,"joyTeamTotal");
								 listSort.add(0, sortByTone);
							}
							
						}
						if(clientDataDTO.getSearchCriteria().contains("sadness")) {
							if (listSort.size()>0) {
								sortByTone = listSort.get(0).and(Sort.Direction.ASC,"sadnessTeamTotal");
								 listSort.add(0, sortByTone);
							}else {
								 sortByTone = Aggregation.sort(Sort.Direction.ASC,"sadnessTeamTotal");
								 listSort.add(0, sortByTone);
							}
							
						}
						if(clientDataDTO.getSearchCriteria().contains("tentative")) {
							if (listSort.size()>0) {
								sortByTone = listSort.get(0).and(Sort.Direction.ASC,"tentativeTeamTotal");
								 listSort.add(0, sortByTone);
							}else {
								 sortByTone = Aggregation.sort(Sort.Direction.ASC,"tentativeTeamTotal");
								 listSort.add(0, sortByTone);
							}
							
						}
						if(clientDataDTO.getSearchCriteria().contains("analytical")) {
							if (listSort.size()>0) {
								sortByTone = listSort.get(0).and(Sort.Direction.ASC,"analyticalTeamTotal");
								 listSort.add(0, sortByTone);
							}else {
								 sortByTone = Aggregation.sort(Sort.Direction.ASC,"analyticalTeamTotal");
								 listSort.add(0, sortByTone);
							}
							
						}
						if(clientDataDTO.getSearchCriteria().contains("confident")) {
							if (listSort.size()>0) {
								sortByTone = listSort.get(0).and(Sort.Direction.ASC,"confidentTeamTotal");
								 listSort.add(0, sortByTone);
							}else {
								 sortByTone = Aggregation.sort(Sort.Direction.ASC,"confidentTeamTotal");
								 listSort.add(0, sortByTone);
							}
							
						}
						if(clientDataDTO.getSearchCriteria().contains("fear")) {
							if (listSort.size()>0) {
								sortByTone = listSort.get(0).and(Sort.Direction.ASC,"fearTeamTotal");
								 listSort.add(0, sortByTone);
							}else {
								 sortByTone = Aggregation.sort(Sort.Direction.ASC,"fearTeamTotal");
								 listSort.add(0, sortByTone);
							}
							
						}
						
						if(clientDataDTO.getSearchCriteria().contains("teamMail")) {
							if (listSort.size()>0) {
								sortByTone = listSort.get(0).and(Sort.Direction.ASC,"totalTeamMail");
								 listSort.add(0, sortByTone);
							}else {
								 sortByTone = Aggregation.sort(Sort.Direction.ASC,"totalTeamMail");
								 listSort.add(0, sortByTone);
							}
							
						}
						
						
			}
						
			}
						if(clientDataDTO.getSearchCriteria()!=null){
					
					if (clientDataDTO.getSearchCriteria().contains("alphabetically")) {
						
						if (listSort.size()>0) {
							sortByTone = listSort.get(0).and(Sort.Direction.DESC,"employeeName");
							 listSort.add(0, sortByTone);
						}else {
							 sortByTone = Aggregation.sort(Sort.Direction.ASC,"employeeName");
							 listSort.add(0, sortByTone);
						}
					}
					if (clientDataDTO.getSearchCriteria().contains("designation")) {
						
						if (listSort.size()>0) {
							sortByTone = listSort.get(0).and(Sort.Direction.ASC,"designation");
							 listSort.add(0, sortByTone);
						}else {
							 sortByTone = Aggregation.sort(Sort.Direction.DESC,"designation");
							 listSort.add(0, sortByTone);
						}
					}
					
						
		}
					
					
					/*else {
						 sortByTone = Aggregation.sort(Sort.Direction.DESC,"angerTeamTotal");
						 listSort.add(0, sortByTone);
					}*/
					
					if (listSort.size()==0) {
						 sortByTone = Aggregation.sort(Sort.Direction.DESC,"angerTeamTotal");
						 listSort.add(0, sortByTone);
					}
					
					sortByTone = listSort.get(0);
					
					//get value from list
					
				
					
					MatchOperation matchOperation = null;
					
					if (clientDataDTO.getDesignation()!=null && clientDataDTO.getCompanyId()!=null) {
						 matchOperation = Aggregation.match(Criteria.where("clients.designation").is(clientDataDTO.getDesignation())
								 .andOperator(Criteria.where("clients.companyFK").is(clientDataDTO.getCompanyId())));
					}else {
						 matchOperation = Aggregation.match(Criteria.where("clients.companyFK").is(clientDataDTO.getCompanyId()));
					}
					
					
			
				
				Aggregation aggregation8 = Aggregation.newAggregation(
						
						Aggregation.match(Criteria.where("employeeIdFK").is(employeeBO.getEmployeeId())),
						
						Aggregation.unwind("$clients"),
						//Aggregation.match(Criteria.where("clients.companyFK").is("5acb16fe67a2b7664ff16f36")),
						matchOperation,
						Aggregation.group("clients.emailId").last("clients.emailId").as("employeeId").last("clients.name").as("name")
						.last("clients.designation").as("designation")
						.sum("clients.allMailScore.anger").as("angerTeamTotal").sum("clients.allMailScore.angerCount")
						.as("angerTeamCount").sum("clients.allMailScore.joy").as("joyTeamTotal").sum("clients.allMailScore.joyCount")
						.as("joyTeamCount").sum("clients.allMailScore.sadness").as("sadnessTeamTotal").sum("clients.allMailScore.sadnessCount")
						.as("sadnessTeamCount").sum("clients.allMailScore.tentative").as("tentativeTeamTotal").sum("clients.allMailScore.tentativeCount")
						.as("tentativeTeamCount").sum("clients.allMailScore.analytical").as("analyticalTeamTotal").sum("clients.allMailScore.analyticalCount")
						.as("analyticalTeamCount").sum("clients.allMailScore.confident").as("confidentTeamTotal").sum("clients.allMailScore.confidentCount")
						.as("confidentTeamCount").sum("clients.allMailScore.fear").as("fearTeamTotal").sum("clients.allMailScore.fearCount")
						.as("fearTeamCount")
						.sum("clients.receiveMailScore.anger").as("angerTeamTotalrcv").sum("clients.receiveMailScore.angerCount")
						.as("angerTeamCountrcv").sum("clients.receiveMailScore.joy").as("joyTeamTotalrcv").sum("clients.receiveMailScore.joyCount")
						.as("joyTeamCountrcv").sum("clients.receiveMailScore.sadness").as("sadnessTeamTotalrcv").sum("clients.receiveMailScore.sadnessCount")
						.as("sadnessTeamCountrcv").sum("clients.receiveMailScore.tentative").as("tentativeTeamTotalrcv").sum("clients.receiveMailScore.tentativeCount")
						.as("tentativeTeamCountrcv").sum("clients.receiveMailScore.analytical").as("analyticalTeamTotalrcv").sum("clients.receiveMailScore.analyticalCount")
						.as("analyticalTeamCountrcv").sum("clients.receiveMailScore.confident").as("confidentTeamTotalrcv").sum("clients.receiveMailScore.confidentCount")
						.as("confidentTeamCountrcv").sum("clients.receiveMailScore.fear").as("fearTeamTotalrcv").sum("clients.receiveMailScore.fearCount")
						.as("fearTeamCountrcv")
						.sum("clients.sentMailScore.anger").as("angerTeamTotalsnt").sum("clients.sentMailScore.angerCount")
						.as("angerTeamCountsnt").sum("clients.sentMailScore.joy").as("joyTeamTotalsnt").sum("clients.sentMailScore.joyCount")
						.as("joyTeamCountsnt").sum("clients.sentMailScore.sadness").as("sadnessTeamTotalsnt").sum("clients.sentMailScore.sadnessCount")
						.as("sadnessTeamCountsnt").sum("clients.sentMailScore.tentative").as("tentativeTeamTotalsnt").sum("clients.sentMailScore.tentativeCount")
						.as("tentativeTeamCountsnt").sum("clients.sentMailScore.analytical").as("analyticalTeamTotalsnt").sum("clients.sentMailScore.analyticalCount")
						.as("analyticalTeamCountsnt").sum("clients.sentMailScore.confident").as("confidentTeamTotalsnt").sum("clients.sentMailScore.confidentCount")
						.as("confidentTeamCountsnt").sum("clients.sentMailScore.fear").as("fearTeamTotalsnt").sum("clients.sentMailScore.fearCount")
						.as("fearTeamCountsnt")
						.sum("clients.totalMail").as("totalTeamMail")
						.sum("clients.totalMailRecevied").as("totalTeamMailRecevied")
						.sum("clients.totalMailSent").as("totalTeamMailSent"),
//						Aggregation.project("clients.emailId").and("name").as("employeeName").and("designation").as("designation")
//						.and("totalTeamMail").as("totalTeamMail")
//						.and("totalTeamMailRecevied").as("totalTeamMailRecevied")
//						.and("totalTeamMailSent").as("totalTeamMailSent")
//						.and("angerTeamTotal").divide("angerTeamCount").as("angerTeamTotal")
//						.and("joyTeamTotal").divide("joyTeamCount").as("joyTeamTotal")
//						.and("sadnessTeamTotal").divide("sadnessTeamCount").as("sadnessTeamTotal")
//						.and("tentativeTeamTotal").divide("tentativeTeamCount").as("tentativeTeamTotal")
//						.and("analyticalTeamTotal").divide("analyticalTeamCount").as("analyticalTeamTotal")
//						.and("confidentTeamTotal").divide("confidentTeamCount").as("confidentTeamTotal")
//						.and("fearTeamTotal").divide("fearTeamCount").as("fearTeamTotal")
//						.and("angerTeamTotalsnt").divide("angerTeamCountsnt").as("angerTeamTotalsnt")
//						.and("joyTeamTotalsnt").divide("joyTeamCountsnt").as("joyTeamTotalsnt")
//						.and("sadnessTeamTotalsnt").divide("sadnessTeamCountsnt").as("sadnessTeamTotalsnt")
//						.and("tentativeTeamTotalsnt").divide("tentativeTeamCountsnt").as("tentativeTeamTotalsnt")
//						.and("analyticalTeamTotalsnt").divide("analyticalTeamCountsnt").as("analyticalTeamTotalsnt")
//						.and("confidentTeamTotalsnt").divide("confidentTeamCountsnt").as("confidentTeamTotalsnt")
//						.and("fearTeamTotalsnt").divide("fearTeamCountsnt").as("fearTeamTotalsnt")
//						.and("angerTeamTotalrcv").divide("angerTeamCountrcv").as("angerTeamTotalrcv")
//						.and("joyTeamTotalrcv").divide("joyTeamCountrcv").as("joyTeamTotalrcv")
//						.and("sadnessTeamTotalrcv").divide("sadnessTeamCountrcv").as("sadnessTeamTotalrcv")
//						.and("tentativeTeamTotalrcv").divide("tentativeTeamCountrcv").as("tentativeTeamTotalrcv")
//						.and("analyticalTeamTotalrcv").divide("analyticalTeamCountrcv").as("analyticalTeamTotalrcv")
//						.and("confidentTeamTotalrcv").divide("confidentTeamCountrcv").as("confidentTeamTotalrcv")
//						.and("fearTeamTotalrcv").divide("fearTeamCountrcv").as("fearTeamTotalrcv"),
						sortByTone
						
						
						
						);
				
				AggregationResults<DBObject> groupResults8 
				= mongoTemplate.aggregate(aggregation8, TeamClientInteractionBO.class, DBObject.class);
				List<DBObject> result5 = groupResults8.getMappedResults();
				
				for(DBObject dbObject : result5) {
					
					EmployeePersonalDataDTO employeePersonalDataDTO5 = new EmployeePersonalDataDTO();
					// Average of All Self Scores 
					ToneOfMail toneOfMailAll = new ToneOfMail();
					/*toneOfMailAll.setAnger((double)Math.round(((Number)dbObject.get("angerTeamTotal")).doubleValue()));
					toneOfMailAll.setJoy((double)Math.round(((Number)dbObject.get("joyTeamTotal")).doubleValue()));
					toneOfMailAll.setSadness((double)Math.round(((Number)dbObject.get("sadnessTeamTotal")).doubleValue()));
					toneOfMailAll.setTentative((double)Math.round(((Number)dbObject.get("tentativeTeamTotal")).doubleValue()));
					toneOfMailAll.setAnalytical((double)Math.round(((Number)dbObject.get("analyticalTeamTotal")).doubleValue()));
					toneOfMailAll.setConfident((double)Math.round(((Number)dbObject.get("confidentTeamTotal")).doubleValue()));
					toneOfMailAll.setFear((double)Math.round(((Number)dbObject.get("fearTeamTotal")).doubleValue()));
					*/
					
					Double anger = ((Number)dbObject.get("angerTeamTotal")).doubleValue();
					Double joy = ((Number)dbObject.get("joyTeamTotal")).doubleValue();
					Double sadn =	((Number)dbObject.get("sadnessTeamTotal")).doubleValue();
					Double ten = ((Number)dbObject.get("tentativeTeamTotal")).doubleValue();
					Double anal = ((Number)dbObject.get("analyticalTeamTotal")).doubleValue();
					Double conf = ((Number)dbObject.get("confidentTeamTotal")).doubleValue();
					Double fear = ((Number)dbObject.get("fearTeamTotal")).doubleValue();
					
					Double angerP4 = 0.0d;
					Double joyP4 = 0.0d;
					Double sadnP4 = 0.0d;
					Double tenP4 = 0.0d;
					Double analP4 = 0.0d;
					Double confP4 = 0.0d;
					Double fearP4 = 0.0d;
					
					Double total4 = anger+joy+sadn+ten+anal+conf+fear;
					if(total4>0) {
					angerP4 = (double)Math.round(((anger)*100)/(total4));
					joyP4 = (double)Math.round(((joy)*100)/(total4));
					sadnP4 = (double)Math.round(((sadn)*100)/(total4));
					tenP4 = (double)Math.round(((ten)*100)/(total4));
					analP4 = (double)Math.round(((anal)*100)/(total4));
					confP4 = (double)Math.round(((conf)*100)/(total4));
					fearP4 =  (double)Math.round(((fear)*100)/(total4));
					}
					toneOfMailAll.setAnger(angerP4);
					toneOfMailAll.setJoy(joyP4);
					toneOfMailAll.setSadness(sadnP4);
					toneOfMailAll.setTentative(tenP4);
					toneOfMailAll.setAnalytical(analP4);
					toneOfMailAll.setConfident(confP4);
					toneOfMailAll.setFear(fearP4);
					
					
					
					//rcv
					ToneOfMail toneOfMailRcv = new ToneOfMail();
					
					
					Double angerc = ((Number)dbObject.get("angerTeamTotalrcv")).doubleValue();
					Double joyr = ((Number)dbObject.get("joyTeamTotalrcv")).doubleValue();
					Double sadnr =	((Number)dbObject.get("sadnessTeamTotalrcv")).doubleValue();
					Double tenr = ((Number)dbObject.get("tentativeTeamTotalrcv")).doubleValue();
					Double analr = ((Number)dbObject.get("analyticalTeamTotalrcv")).doubleValue();
					Double confr = ((Number)dbObject.get("confidentTeamTotalrcv")).doubleValue();
					Double fearr = ((Number)dbObject.get("fearTeamTotalrcv")).doubleValue();
					
					Double angercP5 = 0.0d;
					Double joyrP5 = 0.0d;
					Double sadnrP5 = 0.0d;
					Double tenrP5 = 0.0d;
					Double analrP5 = 0.0d;
					Double confrP5 = 0.0d;
					Double fearrP5 = 0.0d;
					
					Double totalr5 = angerc+joyr+sadnr+tenr+analr+confr+fearr;
					if(totalr5 > 0) {
					angercP5 = (double)Math.round(((angerc)*100)/(totalr5));
					joyrP5 = (double)Math.round(((joyr)*100)/(totalr5));
					sadnrP5 = (double)Math.round(((sadnr)*100)/(totalr5));
					tenrP5 = (double)Math.round(((tenr)*100)/(totalr5));
					analrP5 = (double)Math.round(((analr)*100)/(totalr5));
					confrP5 = (double)Math.round(((confr)*100)/(totalr5));
					fearrP5 =  (double)Math.round(((fearr)*100)/(totalr5));
					}
					
					toneOfMailRcv.setAnger(angercP5);
					toneOfMailRcv.setJoy(joyrP5);
					toneOfMailRcv.setSadness(sadnrP5);
					toneOfMailRcv.setTentative(tenrP5);
					toneOfMailRcv.setAnalytical(analrP5);
					toneOfMailRcv.setConfident(confrP5);
					toneOfMailRcv.setFear(fearrP5);
					
					
					
					
					//snt
					ToneOfMail toneOfMailSnt = new ToneOfMail();
					
					Double angersnt = ((Number)dbObject.get("angerTeamTotalsnt")).doubleValue();
					Double joyrsnt = ((Number)dbObject.get("joyTeamTotalsnt")).doubleValue();
					Double sadnrsnt =	((Number)dbObject.get("sadnessTeamTotalsnt")).doubleValue();
					Double tenrsnt = ((Number)dbObject.get("tentativeTeamTotalsnt")).doubleValue();
					Double analrsnt = ((Number)dbObject.get("analyticalTeamTotalsnt")).doubleValue();
					Double confrsnt = ((Number)dbObject.get("confidentTeamTotalsnt")).doubleValue();
					Double fearrsnt = ((Number)dbObject.get("fearTeamTotalsnt")).doubleValue();
					
					Double angersntP6 = 0.0d;
					Double joyrsntP6 = 0.0d;
					Double sadnrsntP6 = 0.0d;
					Double tenrsntP6 = 0.0d;
					Double analrsntP6 = 0.0d;
					Double confrsntP6 = 0.0d;
					Double fearrsntP6 = 0.0d;
					
					
					Double totalsnt6 = angersnt+joyrsnt+sadnrsnt+tenrsnt+analrsnt+confrsnt+fearrsnt;
					if(totalsnt6 > 0) {
					angersntP6 = (double)Math.round(((angersnt)*100)/(totalsnt6));
					joyrsntP6 = (double)Math.round(((joyrsnt)*100)/(totalsnt6));
					sadnrsntP6 = (double)Math.round(((sadnrsnt)*100)/(totalsnt6));
					tenrsntP6 = (double)Math.round(((tenrsnt)*100)/(totalsnt6));
					analrsntP6 = (double)Math.round(((analrsnt)*100)/(totalsnt6));
					confrsntP6 = (double)Math.round(((confrsnt)*100)/(totalsnt6));
					fearrsntP6 =  (double)Math.round(((fearrsnt)*100)/(totalsnt6));
					}
					
					toneOfMailSnt.setAnger(angersntP6);
					toneOfMailSnt.setJoy(joyrsntP6);
					toneOfMailSnt.setSadness(sadnrsntP6);
					toneOfMailSnt.setTentative(tenrsntP6);
					toneOfMailSnt.setAnalytical(analrsntP6);
					toneOfMailSnt.setConfident(confrsntP6);
					toneOfMailSnt.setFear(fearrsntP6);
					
//					employeePersonalDataDTO5.setEmployeeName(dbObject.get("employeeName").toString());
					employeePersonalDataDTO5.setEmployeeName(dbObject.get("name").toString());
					
					employeePersonalDataDTO5.setEmployeeId(dbObject.get("_id").toString());
					
					
					
//					employeePersonalDataDTO5.setDesignation(dbObject.get("designation").toString());
		
					
					employeePersonalDataDTO5.setToneOfTeamMail(toneOfMailAll);
					employeePersonalDataDTO5.setToneOfTeamReceiveMail(toneOfMailRcv);
					employeePersonalDataDTO5.setToneOfTeamSentMail(toneOfMailSnt);
					employeePersonalDataDTO5.setNoOfMail(((Number)dbObject.get("totalTeamMail")).longValue());
					if(((Number)dbObject.get("totalTeamMailRecevied")) != null)
					employeePersonalDataDTO5.setNoOfReceiveMail(((Number)dbObject.get("totalTeamMailRecevied")).longValue());
					if(((Number)dbObject.get("totalTeamMailSent")) != null)
					employeePersonalDataDTO5.setNoOfSentMail(((Number)dbObject.get("totalTeamMailSent")).longValue());
					employeePersonalDataDTO5.setEmailId(dbObject.get("employeeId").toString());
					
//					if(dbObject.get("executive") != null)
//					employeePersonalDataDTO5.setExecutive("yes");
					list.add(employeePersonalDataDTO5);
						
					}
					
					
				
			
				
				employeePersonalDataDTO.setListOfEmployee(list);
				
				
			
			return employeePersonalDataDTO;
			
		}
	
	
	
		
		// data on each sentiments tone Click
		
		public EmployeePersonalDataDTO getEmailOnToneClick(ClientDataDTO empData) {
			
			EmployeePersonalDataDTO employeePersonalDataDTO = new EmployeePersonalDataDTO();
			
			EmployeeBO employeeData = employeeRepository.findByEmployeeId(empData.getEmployeeId());
			EmployeeRoleBO employeeRolesPojo = employeeRoleRepository.findByEmployeeIdFKAndStatus(empData.getEmployeeId(), "active");
		
					
					 employeePersonalDataDTO.setNoOfTeamMember(employeeRolesPojo.getTeamSize());
					// employeePersonalDataDTO.setConsolidatedTone(employeeRolesPojo.getConsolidatedTone());
					/* employeePersonalDataDTO.setToneOfTeamMail(employeeRolesPojo.getConsolidatedTone().getToneWithTeam()
							 .getAllMailScore());
					 employeePersonalDataDTO.setToneOfClientMail(employeeRolesPojo.getConsolidatedTone().getToneWithClient()
							 .getAllMailScore());*/
					 employeePersonalDataDTO.setDepartment(employeeRolesPojo.getDepartment());
					 employeePersonalDataDTO.setDesignation(employeeRolesPojo.getDesignation());
					 employeePersonalDataDTO.setNoOfMail(employeeRolesPojo.getConsolidatedTone().getTotalMail());
					 //Relation with team
					 Double joy =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getJoy();
					 Double tnt =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getTentative();
					 Double ana =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getAnalytical();
					 Double cnfdnc =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getConfident();
					 Double anger =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getAnger();
					 Double sad =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getSadness();
					 Double fear =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getFear();
					
					 Double joyness = (double) Math.round(((joy+tnt+ana+cnfdnc)*100)/(joy+tnt+ana+cnfdnc+anger+sad+fear));
					 Double angry =(double) Math.round(((anger+sad+fear)*100)/(joy+tnt+ana+cnfdnc+anger+sad+fear));
					 EmployeeRelationBO employeeRelationBO = new EmployeeRelationBO();
					 employeeRelationBO.setGood(joyness);
					 employeeRelationBO.setBad(angry);
					 employeePersonalDataDTO.setRelationWithTeam(employeeRelationBO);
					 
					
					 
					 
					 //Relation with Client
					 Double joyClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getJoy();
					 Double anaClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getAnalytical();
					 Double tntClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getTentative();
					 Double confClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getConfident();
					 Double angerClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getAnger();
					 Double sadClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getSadness();
					 Double fearClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getFear();
					 
					
					 Double joynessClnt = (double) Math.round(((joyClnt+anaClnt+tntClnt+confClnt)*100)/(joyClnt+anaClnt+tntClnt+confClnt+angerClnt+sadClnt+fearClnt));
					 Double angryClnt =(double) Math.round(((angerClnt+sadClnt+fearClnt)*100)/(joyClnt+anaClnt+tntClnt+confClnt+angerClnt+sadClnt+fearClnt));
					 EmployeeRelationBO employeeRelationBO1 = new EmployeeRelationBO();
					 employeeRelationBO1.setGood(joynessClnt);
					 employeeRelationBO1.setBad(angryClnt);
					 employeePersonalDataDTO.setRelationWithClient(employeeRelationBO1);
					 
					
					 
					 
					
				//}
			//}
			
			employeePersonalDataDTO.setEmployeeName(employeeData.getEmployeeName());
			employeePersonalDataDTO.setEmployeeId(employeeData.getEmployeeId());
			
			if (employeeRolesPojo.getReportToId()==null) {
				
				employeePersonalDataDTO.setReportTo("None");
				
			}else {
			employeePersonalDataDTO.setReportTo(employeeRepository.findByEmployeeId(employeeRolesPojo.getReportToId()).getEmployeeName());
			}
			
			ProjectionOperation projectionOperation = Aggregation.project()
					.andExpression("lineItems").as("teamEmails")
					.andExclude("_id");
			
			Aggregation aggregation = null;
			
			List<EmailPojo> listMail = new ArrayList<EmailPojo>();
			
			String criteria = null;
			
			// make query according to user criteria
			if (empData.getSearchText().equalsIgnoreCase("anger")) {
				
				criteria = "lineItems.anger";
				
			}else if (empData.getSearchText().equalsIgnoreCase("joy")) {
				
				criteria = "lineItems.joy";
				
			}else if (empData.getSearchText().equalsIgnoreCase("sadness")) {
				
				criteria = "lineItems.sadness";
				
			}else if (empData.getSearchText().equalsIgnoreCase("tentative")) {
				
				criteria = "lineItems.tentative";
				
			}else if (empData.getSearchText().equalsIgnoreCase("analytical")) {
				
				criteria = "lineItems.analytical";
				
			}else if (empData.getSearchText().equalsIgnoreCase("confident")) {
				
				criteria = "lineItems.confident";
				
			}else if (empData.getSearchText().equalsIgnoreCase("fear")) {
				
				criteria = "lineItems.fear";
				
			}
			
			if (empData.getIdentify().equalsIgnoreCase("all")) {
				
			
				
			 aggregation = Aggregation.newAggregation(
					
					Aggregation.match(Criteria.where("employeeIdFK").is(empData.getEmployeeId())),
					Aggregation.unwind("$lineItems"),
					Aggregation.match(Criteria.where(criteria).gt(0)),
					//Aggregation.project("employeeHierarchy")
					projectionOperation
					
					
					);
			 
			}
			
			if (empData.getIdentify().equalsIgnoreCase("sent")) {
				
				
				
				 aggregation = Aggregation.newAggregation(
						
						Aggregation.match(Criteria.where("employeeIdFK").is(empData.getEmployeeId())),
						Aggregation.unwind("$lineItems"),
						Aggregation.match(Criteria.where(criteria).gt(0).andOperator(Criteria.where("lineItems.type").is("sent"))),
						//Aggregation.project("employeeHierarchy")
						projectionOperation
						
						
						);
				 
				}
			
			if (empData.getIdentify().equalsIgnoreCase("receive")) {
				
				
				
				 aggregation = Aggregation.newAggregation(
						
						Aggregation.match(Criteria.where("employeeIdFK").is(empData.getEmployeeId())),
						Aggregation.unwind("$lineItems"),
						Aggregation.match(Criteria.where(criteria).gt(0).andOperator(Criteria.where("lineItems.type").is("Received"))),
						//Aggregation.project("employeeHierarchy")
						projectionOperation
						
						
						);
				 
				}
			
			 List<FilterResultPojo> results = mongoTemplate.aggregate(aggregation, DailyEmployeeEmailToneBO.class, FilterResultPojo.class).getMappedResults();
			
			 for(FilterResultPojo filter :results){
				 
				 
				 
				 
				 EmailPojo emailPojo = filter.getTeamEmails();
				 	
					Map<String,Double> hashMap = new HashMap<String,Double>(); 
					hashMap.put("anger", emailPojo.getAnger());
					hashMap.put("joy", emailPojo.getJoy());
					hashMap.put("sadness", emailPojo.getSadness());
					hashMap.put("tentative", emailPojo.getTentative());
					hashMap.put("analytical", emailPojo.getAnalytical());
					hashMap.put("confident", emailPojo.getConfident());
					hashMap.put("fear", emailPojo.getFear());
					
					try {
						emailPojo.setFrom(employeeRepository.findByEmailIdIgnoreCase(emailPojo.getFromMail()).getEmployeeName());
					}catch(Exception e) {
						emailPojo.setFrom("");
					}
					
					try {
						  if(emailPojo.getToClientEmails().size() > 0) {
							  System.out.println("in getToclients loop");
							  String toClientEmails = emailPojo.getToClientEmails().get(0);
							  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(toClientEmails.split("@")[1]).get(0).getCompanyName());
						  }else if(emailPojo.getFromMail().length() >0 && emailPojo.getFromMail().split("@")[1] != employeeData.getEmailId().split("@")[1] ) {
							  System.out.println("in from mail loop");
							  String domain = emailPojo.getFromMail().split("@")[1];
							  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(domain).get(0).getCompanyName());
						  }else {
							  System.out.println("in else loop");
							  emailPojo.setCompanyName("");
						  }
					  }catch(Exception e) {
						  System.err.println(e);
					  }
					
					  Map<String, Double> sortedMapDsc =	sortByComparator(hashMap, false);
					  int count =0;
					  for (Entry<String, Double> entry : sortedMapDsc.entrySet())
				        {
				            System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
				            
				            
				         
				            switch (entry.getKey()) {
				            
							case "anger":
								if (count<2) {
									emailPojo.setAnger(entry.getValue());
									count++;
									UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
											.getContext().getAuthentication();
									
//									EmployeePersonalDataDTO employeePersonalDataDTO1 = new EmployeePersonalDataDTO();
									
									String emailId = (String) authObj.getUserSessionInformation().get(
											EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
									
									EmployeeBO employeeData1 =employeeRepository.findOne(emailId);
									
									ClientBO clients= clientRepository.findByEmployeeIdFK(employeeData1.getEmployeeId());
									if(clients != null) {
										if(emailPojo.getType().equalsIgnoreCase("Received")) {
											
											if(clients.getClients() != null) {
												for(FilterPojo client: clients.getClients()) {
													if(client.getEmailId().equalsIgnoreCase(emailPojo.getFromMail())) {
														if(client.getExecutive() != null)
														if(client.getExecutive().equalsIgnoreCase("yes")) {
															emailPojo.setAdverse("yes");
														}
													}
												}
											}
											
										}else if(emailPojo.getType().equalsIgnoreCase("sent")){
											if(clients.getClients() != null) {
												for(FilterPojo client: clients.getClients()) {
													for(String clientEmail: emailPojo.getToClientEmails()) {
														if(client.getEmailId().equalsIgnoreCase(clientEmail)) {
															if(client.getExecutive() != null)
															if(client.getExecutive().equalsIgnoreCase("yes")) {
																emailPojo.setAdverse("yes");
															}
														}
													}
												}
											}
										}
									}
								}else {
									emailPojo.setAnger(0.0);
									count++;
								}
								
								break;
								
							case "joy":

								if (count<2) {
									emailPojo.setJoy(entry.getValue());
									count++;
								}else {
									emailPojo.setJoy(0.0);
									count++;
								}
								break;	
							case "sadness":
								if (count<2) {
									emailPojo.setSadness(entry.getValue());
									count++;
								}else {
									emailPojo.setSadness(0.0);
									count++;
								}
								break;
								
							case "tentative":
								if (count<2) {
									emailPojo.setTentative(entry.getValue());
									count++;
								}else {
									emailPojo.setTentative(0.0);
									count++;
								}
								break;
																	
							case "analytical":
								if (count<2) {
									emailPojo.setAnalytical(entry.getValue());
									count++;
								}else {
									emailPojo.setAnalytical(0.0);
									count++;
								}
								break;
								
							case "confident":
								if (count<2) {
									emailPojo.setConfident(entry.getValue());
									count++;
								}else {
									emailPojo.setConfident(0.0);
									count++;
								}
								break;
								
							case "fear":
								if (count<2) {
									emailPojo.setFear(entry.getValue());
									count++;
								}else {
									emailPojo.setFear(0.0);
									count++;
								}
								break;

							default:
								break;
								
							}
				            
				          
				        }
					  
					  System.out.println(sortedMapDsc);
					  
					 /* try {
						  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");
						  Date date = simpleDateFormat.parse(emailPojo.getDate());
						  // new Format
						  SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd-MMM-YYYY");
						  simpleDateFormat2.format(date);
						  emailPojo.setDate(simpleDateFormat2.format(date));
						  }catch (Exception e) {
							// TODO: handle exception
						}*/

					//  list.add(emailPojo);
					  
					  if (empData.getSearchText().equalsIgnoreCase("anger")) {
							
							if (emailPojo.getAnger()>0) {
								
								listMail.add(emailPojo);
							}
							
						}else if (empData.getSearchText().equalsIgnoreCase("joy")) {
							
							if (emailPojo.getJoy()>0) {
								
								listMail.add(emailPojo);
							}
							
						}else if (empData.getSearchText().equalsIgnoreCase("sadness")) {
							
							if (emailPojo.getSadness()>0) {
								
								listMail.add(emailPojo);
							}
							
							
						}else if (empData.getSearchText().equalsIgnoreCase("tentative")) {
							
							if (emailPojo.getTentative()>0) {
								
								listMail.add(emailPojo);
							}
							
						}else if (empData.getSearchText().equalsIgnoreCase("analytical")) {
							
							if (emailPojo.getAnalytical()>0) {
								
								listMail.add(emailPojo);
							}
							
						}else if (empData.getSearchText().equalsIgnoreCase("confident")) {
							
							if (emailPojo.getConfident()>0) {
								
								listMail.add(emailPojo);
							}
							
						}else if (empData.getSearchText().equalsIgnoreCase("fear")) {
							
							if (emailPojo.getFear()>0) {
								
								listMail.add(emailPojo);
							}
							
						}
					
				// listMail.add(filter.getTeamEmails());
			 }
			
			 employeePersonalDataDTO.setListEmailAnalyser(listMail);
			 
			
			 return employeePersonalDataDTO;
			 
		}
		
		
public EmployeePersonalDataDTO getEmailOnToneClickClient(ClientDataDTO empData) {
			
//			EmployeePersonalDataDTO employeePersonalDataDTO = new EmployeePersonalDataDTO();
	
			UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
					.getContext().getAuthentication();
			
			EmployeePersonalDataDTO employeePersonalDataDTO = new EmployeePersonalDataDTO();
			
			String emailId = (String) authObj.getUserSessionInformation().get(
					EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
			
			EmployeeBO employeeData = employeeRepository.findByEmailIdIgnoreCase(emailId);
			EmployeeRoleBO employeeRolesPojo = employeeRoleRepository.findByEmployeeIdFKAndStatus(employeeData.getEmployeeId(), "active");
			Boolean receive = false;
			Boolean sent = false;
		
					
					 employeePersonalDataDTO.setNoOfTeamMember(employeeRolesPojo.getTeamSize());
					// employeePersonalDataDTO.setConsolidatedTone(employeeRolesPojo.getConsolidatedTone());
					/* employeePersonalDataDTO.setToneOfTeamMail(employeeRolesPojo.getConsolidatedTone().getToneWithTeam()
							 .getAllMailScore());
					 employeePersonalDataDTO.setToneOfClientMail(employeeRolesPojo.getConsolidatedTone().getToneWithClient()
							 .getAllMailScore());*/
					 employeePersonalDataDTO.setDepartment(employeeRolesPojo.getDepartment());
					 employeePersonalDataDTO.setDesignation(employeeRolesPojo.getDesignation());
					 employeePersonalDataDTO.setNoOfMail(employeeRolesPojo.getConsolidatedTone().getTotalMail());
					 //Relation with team
					 Double joy =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getJoy();
					 Double tnt =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getTentative();
					 Double ana =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getAnalytical();
					 Double cnfdnc =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getConfident();
					 Double anger =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getAnger();
					 Double sad =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getSadness();
					 Double fear =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getFear();
					
					 Double joyness = (double) Math.round(((joy+tnt+ana+cnfdnc)*100)/(joy+tnt+ana+cnfdnc+anger+sad+fear));
					 Double angry =(double) Math.round(((anger+sad+fear)*100)/(joy+tnt+ana+cnfdnc+anger+sad+fear));
					 EmployeeRelationBO employeeRelationBO = new EmployeeRelationBO();
					 employeeRelationBO.setGood(joyness);
					 employeeRelationBO.setBad(angry);
					 employeePersonalDataDTO.setRelationWithTeam(employeeRelationBO);
					 
					
					 
					 
					 //Relation with Client
					 Double joyClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getJoy();
					 Double anaClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getAnalytical();
					 Double tntClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getTentative();
					 Double confClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getConfident();
					 Double angerClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getAnger();
					 Double sadClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getSadness();
					 Double fearClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getFear();
					 
					
					 Double joynessClnt = (double) Math.round(((joyClnt+anaClnt+tntClnt+confClnt)*100)/(joyClnt+anaClnt+tntClnt+confClnt+angerClnt+sadClnt+fearClnt));
					 Double angryClnt =(double) Math.round(((angerClnt+sadClnt+fearClnt)*100)/(joyClnt+anaClnt+tntClnt+confClnt+angerClnt+sadClnt+fearClnt));
					 EmployeeRelationBO employeeRelationBO1 = new EmployeeRelationBO();
					 employeeRelationBO1.setGood(joynessClnt);
					 employeeRelationBO1.setBad(angryClnt);
					 employeePersonalDataDTO.setRelationWithClient(employeeRelationBO1);
					 
					
					 
					 
					
				//}
			//}
			
			employeePersonalDataDTO.setEmployeeName(employeeData.getEmployeeName());
			employeePersonalDataDTO.setEmployeeId(employeeData.getEmployeeId());
			
			if (employeeRolesPojo.getReportToId()==null) {
				
				employeePersonalDataDTO.setReportTo("None");
				
			}else {
			employeePersonalDataDTO.setReportTo(employeeRepository.findByEmployeeId(employeeRolesPojo.getReportToId()).getEmployeeName());
			}
			
			ProjectionOperation projectionOperation = Aggregation.project()
					.andExpression("lineItems").as("teamEmails")
					.andExclude("_id");
			
			ProjectionOperation projectionOperation1 = Aggregation.project()
					.andExpression("clientEmailItems").as("teamEmails")
					.andExclude("_id");
			
			Aggregation aggregation = null;
			Aggregation aggregation1 = null;
			
			List<EmailPojo> listMail = new ArrayList<EmailPojo>();
			
			String criteria = null;
			
			// make query according to user criteria
			if (empData.getSearchTextClient().equalsIgnoreCase("anger")) {
				
				criteria = "lineItems.anger";
				
			}else if (empData.getSearchTextClient().equalsIgnoreCase("joy")) {
				
				criteria = "lineItems.joy";
				
			}else if (empData.getSearchTextClient().equalsIgnoreCase("sadness")) {
				
				criteria = "lineItems.sadness";
				
			}else if (empData.getSearchTextClient().equalsIgnoreCase("tentative")) {
				
				criteria = "lineItems.tentative";
				
			}else if (empData.getSearchTextClient().equalsIgnoreCase("analytical")) {
				
				criteria = "lineItems.analytical";
				
			}else if (empData.getSearchTextClient().equalsIgnoreCase("confident")) {
				
				criteria = "lineItems.confident";
				
			}else if (empData.getSearchTextClient().equalsIgnoreCase("fear")) {
				
				criteria = "lineItems.fear";
				
			}
			
			if (empData.getIdentify().equalsIgnoreCase("all")) {
				
			
				
//			 aggregation = Aggregation.newAggregation(
//					
//					Aggregation.match(Criteria.where("employeeIdFK").is(empData.getEmployeeId())),
//					Aggregation.unwind("$lineItems"),
//					Aggregation.match(Criteria.where(criteria).gt(0)),
//					//Aggregation.project("employeeHierarchy")
//					projectionOperation
//					
//					
//					);
				
				receive = true;
				sent = true;
			 
			}
			
			if (empData.getIdentify().equalsIgnoreCase("sent")) {
				sent = true;
			}
			
			if (empData.getIdentify().equalsIgnoreCase("receive")) {
				receive = true;
			}
				
				
			if(sent) {	
				 aggregation = Aggregation.newAggregation(
						
						Aggregation.match(Criteria.where("employeeIdFK").is(employeeData.getEmployeeId())),
						Aggregation.unwind("$lineItems"),
						Aggregation.match(Criteria.where(criteria).gt(0).andOperator(Criteria.where("lineItems.type").is("sent"))),
						Aggregation.match(Criteria.where(criteria).gt(0).andOperator(Criteria.where("lineItems.toClientEmails").is(empData.getClientEmailId()))),
						//Aggregation.project("employeeHierarchy")
						projectionOperation
						
						
						);
				 
				
			
			
			
			 List<FilterResultPojo> results = mongoTemplate.aggregate(aggregation, DailyEmployeeEmailToneBO.class, FilterResultPojo.class).getMappedResults();
			
			 for(FilterResultPojo filter :results){
				 
				 
				 
				 
				 EmailPojo emailPojo = filter.getTeamEmails();
					Map<String,Double> hashMap = new HashMap<String,Double>(); 
					hashMap.put("anger", emailPojo.getAnger());
					hashMap.put("joy", emailPojo.getJoy());
					hashMap.put("sadness", emailPojo.getSadness());
					hashMap.put("tentative", emailPojo.getTentative());
					hashMap.put("analytical", emailPojo.getAnalytical());
					hashMap.put("confident", emailPojo.getConfident());
					hashMap.put("fear", emailPojo.getFear());
					
					try {
						emailPojo.setFrom(employeeRepository.findByEmailIdIgnoreCase(emailPojo.getFromMail()).getEmployeeName());
					}catch(Exception e) {
						emailPojo.setFrom("");
					}
					
					try {
						  if(emailPojo.getToClientEmails().size() > 0) {
							  System.out.println("in getToclients loop");
							  String toClientEmails = emailPojo.getToClientEmails().get(0);
							  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(toClientEmails.split("@")[1]).get(0).getCompanyName());
						  }else if(emailPojo.getFromMail().length() >0 && emailPojo.getFromMail().split("@")[1] != employeeData.getEmailId().split("@")[1] ) {
							  System.out.println("in from mail loop");
							  String domain = emailPojo.getFromMail().split("@")[1];
							  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(domain).get(0).getCompanyName());
						  }else {
							  System.out.println("in else loop");
							  emailPojo.setCompanyName("");
						  }
					  }catch(Exception e) {
						  System.err.println(e);
					  }
					
					  Map<String, Double> sortedMapDsc =	sortByComparator(hashMap, false);
					  int count =0;
					  for (Entry<String, Double> entry : sortedMapDsc.entrySet())
				        {
				            System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
				            
				            
				         
				            switch (entry.getKey()) {
				            
							case "anger":
								if (count<2) {
									emailPojo.setAnger(entry.getValue());
									count++;
//									UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
//											.getContext().getAuthentication();
									
//									EmployeePersonalDataDTO employeePersonalDataDTO1 = new EmployeePersonalDataDTO();
									
									String emailId1 = (String) authObj.getUserSessionInformation().get(
											EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
									
									EmployeeBO employeeData1 =employeeRepository.findOne(emailId1);
									
									ClientBO clients= clientRepository.findByEmployeeIdFK(employeeData1.getEmployeeId());
									if(clients != null) {
										if(emailPojo.getType().equalsIgnoreCase("Received")) {
											
											if(clients.getClients() != null) {
												for(FilterPojo client: clients.getClients()) {
													if(client.getEmailId().equalsIgnoreCase(emailPojo.getFromMail())) {
														if(client.getExecutive() != null)
														if(client.getExecutive().equalsIgnoreCase("yes")) {
															emailPojo.setAdverse("yes");
														}
													}
												}
											}
											
										}else if(emailPojo.getType().equalsIgnoreCase("sent")){
											if(clients.getClients() != null) {
												for(FilterPojo client: clients.getClients()) {
													for(String clientEmail: emailPojo.getToClientEmails()) {
														if(client.getEmailId().equalsIgnoreCase(clientEmail)) {
															if(client.getExecutive() != null)
															if(client.getExecutive().equalsIgnoreCase("yes")) {
																emailPojo.setAdverse("yes");
															}
														}
													}
												}
											}
										}
									}
								}else {
									emailPojo.setAnger(0.0);
									count++;
								}
								
								break;
								
							case "joy":

								if (count<2) {
									emailPojo.setJoy(entry.getValue());
									count++;
								}else {
									emailPojo.setJoy(0.0);
									count++;
								}
								break;	
							case "sadness":
								if (count<2) {
									emailPojo.setSadness(entry.getValue());
									count++;
								}else {
									emailPojo.setSadness(0.0);
									count++;
								}
								break;
								
							case "tentative":
								if (count<2) {
									emailPojo.setTentative(entry.getValue());
									count++;
								}else {
									emailPojo.setTentative(0.0);
									count++;
								}
								break;
																	
							case "analytical":
								if (count<2) {
									emailPojo.setAnalytical(entry.getValue());
									count++;
								}else {
									emailPojo.setAnalytical(0.0);
									count++;
								}
								break;
								
							case "confident":
								if (count<2) {
									emailPojo.setConfident(entry.getValue());
									count++;
								}else {
									emailPojo.setConfident(0.0);
									count++;
								}
								break;
								
							case "fear":
								if (count<2) {
									emailPojo.setFear(entry.getValue());
									count++;
								}else {
									emailPojo.setFear(0.0);
									count++;
								}
								break;

							default:
								break;
								
							}
				            
				          
				        }
					  
					  System.out.println(sortedMapDsc);
					  
					 /* try {
						  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");
						  Date date = simpleDateFormat.parse(emailPojo.getDate());
						  // new Format
						  SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd-MMM-YYYY");
						  simpleDateFormat2.format(date);
						  emailPojo.setDate(simpleDateFormat2.format(date));
						  }catch (Exception e) {
							// TODO: handle exception
						}*/

					//  list.add(emailPojo);
					  
					  if (empData.getSearchTextClient().equalsIgnoreCase("anger")) {
							
							if (emailPojo.getAnger()>0) {
								
								listMail.add(emailPojo);
							}
							
						}else if (empData.getSearchTextClient().equalsIgnoreCase("joy")) {
							
							if (emailPojo.getJoy()>0) {
								
								listMail.add(emailPojo);
							}
							
						}else if (empData.getSearchTextClient().equalsIgnoreCase("sadness")) {
							
							if (emailPojo.getSadness()>0) {
								
								listMail.add(emailPojo);
							}
							
							
						}else if (empData.getSearchTextClient().equalsIgnoreCase("tentative")) {
							
							if (emailPojo.getTentative()>0) {
								
								listMail.add(emailPojo);
							}
							
						}else if (empData.getSearchTextClient().equalsIgnoreCase("analytical")) {
							
							if (emailPojo.getAnalytical()>0) {
								
								listMail.add(emailPojo);
							}
							
						}else if (empData.getSearchTextClient().equalsIgnoreCase("confident")) {
							
							if (emailPojo.getConfident()>0) {
								
								listMail.add(emailPojo);
							}
							
						}else if (empData.getSearchTextClient().equalsIgnoreCase("fear")) {
							
							if (emailPojo.getFear()>0) {
								
								listMail.add(emailPojo);
							}
							
						}
					
				// listMail.add(filter.getTeamEmails());
			 }
			 
			 aggregation1 = Aggregation.newAggregation(
						
						Aggregation.match(Criteria.where("employeeIdFK").is(employeeData.getEmployeeId())),
						Aggregation.unwind("$clientEmailItems"),
						Aggregation.match(Criteria.where(criteria).gt(0).andOperator(Criteria.where("clientEmailItems.type").is("sent"))),
						Aggregation.match(Criteria.where(criteria).gt(0).andOperator(Criteria.where("clientEmailItems.toClientEmails").is(empData.getClientEmailId()))),
						//Aggregation.project("employeeHierarchy")
						projectionOperation1
						
						
						);
				 
				
			
			
			
			 List<FilterResultPojo> results1 = mongoTemplate.aggregate(aggregation1, DailyEmployeeEmailToneBO.class, FilterResultPojo.class).getMappedResults();
			
			 for(FilterResultPojo filter :results1){
				 
				 
				 
				 
				 EmailPojo emailPojo = filter.getTeamEmails();
					Map<String,Double> hashMap = new HashMap<String,Double>(); 
					hashMap.put("anger", emailPojo.getAnger());
					hashMap.put("joy", emailPojo.getJoy());
					hashMap.put("sadness", emailPojo.getSadness());
					hashMap.put("tentative", emailPojo.getTentative());
					hashMap.put("analytical", emailPojo.getAnalytical());
					hashMap.put("confident", emailPojo.getConfident());
					hashMap.put("fear", emailPojo.getFear());
					
					try {
						emailPojo.setFrom(employeeRepository.findByEmailIdIgnoreCase(emailPojo.getFromMail()).getEmployeeName());
					}catch(Exception e) {
						emailPojo.setFrom("");
					}
					
					try {
						  if(emailPojo.getToClientEmails().size() > 0) {
							  System.out.println("in getToclients loop");
							  String toClientEmails = emailPojo.getToClientEmails().get(0);
							  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(toClientEmails.split("@")[1]).get(0).getCompanyName());
						  }else if(emailPojo.getFromMail().length() >0 && emailPojo.getFromMail().split("@")[1] != employeeData.getEmailId().split("@")[1] ) {
							  System.out.println("in from mail loop");
							  String domain = emailPojo.getFromMail().split("@")[1];
							  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(domain).get(0).getCompanyName());
						  }else {
							  System.out.println("in else loop");
							  emailPojo.setCompanyName("");
						  }
					  }catch(Exception e) {
						  System.err.println(e);
					  }
					
					  Map<String, Double> sortedMapDsc =	sortByComparator(hashMap, false);
					  int count =0;
					  for (Entry<String, Double> entry : sortedMapDsc.entrySet())
				        {
				            System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
				            
				            
				         
				            switch (entry.getKey()) {
				            
							case "anger":
								if (count<2) {
									emailPojo.setAnger(entry.getValue());
									count++;
//									UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
//											.getContext().getAuthentication();
									
//									EmployeePersonalDataDTO employeePersonalDataDTO1 = new EmployeePersonalDataDTO();
									
									String emailId1 = (String) authObj.getUserSessionInformation().get(
											EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
									
									EmployeeBO employeeData1 =employeeRepository.findOne(emailId1);
									
									ClientBO clients= clientRepository.findByEmployeeIdFK(employeeData1.getEmployeeId());
									if(clients != null) {
										if(emailPojo.getType().equalsIgnoreCase("Received")) {
											
											if(clients.getClients() != null) {
												for(FilterPojo client: clients.getClients()) {
													if(client.getEmailId().equalsIgnoreCase(emailPojo.getFromMail())) {
														if(client.getExecutive() != null)
														if(client.getExecutive().equalsIgnoreCase("yes")) {
															emailPojo.setAdverse("yes");
														}
													}
												}
											}
											
										}else if(emailPojo.getType().equalsIgnoreCase("sent")){
											if(clients.getClients() != null) {
												for(FilterPojo client: clients.getClients()) {
													for(String clientEmail: emailPojo.getToClientEmails()) {
														if(client.getEmailId().equalsIgnoreCase(clientEmail)) {
															if(client.getExecutive() != null)
															if(client.getExecutive().equalsIgnoreCase("yes")) {
																emailPojo.setAdverse("yes");
															}
														}
													}
												}
											}
										}
									}
								}else {
									emailPojo.setAnger(0.0);
									count++;
								}
								
								break;
								
							case "joy":

								if (count<2) {
									emailPojo.setJoy(entry.getValue());
									count++;
								}else {
									emailPojo.setJoy(0.0);
									count++;
								}
								break;	
							case "sadness":
								if (count<2) {
									emailPojo.setSadness(entry.getValue());
									count++;
								}else {
									emailPojo.setSadness(0.0);
									count++;
								}
								break;
								
							case "tentative":
								if (count<2) {
									emailPojo.setTentative(entry.getValue());
									count++;
								}else {
									emailPojo.setTentative(0.0);
									count++;
								}
								break;
																	
							case "analytical":
								if (count<2) {
									emailPojo.setAnalytical(entry.getValue());
									count++;
								}else {
									emailPojo.setAnalytical(0.0);
									count++;
								}
								break;
								
							case "confident":
								if (count<2) {
									emailPojo.setConfident(entry.getValue());
									count++;
								}else {
									emailPojo.setConfident(0.0);
									count++;
								}
								break;
								
							case "fear":
								if (count<2) {
									emailPojo.setFear(entry.getValue());
									count++;
								}else {
									emailPojo.setFear(0.0);
									count++;
								}
								break;

							default:
								break;
								
							}
				            
				          
				        }
					  
					  System.out.println(sortedMapDsc);
					  
					 /* try {
						  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");
						  Date date = simpleDateFormat.parse(emailPojo.getDate());
						  // new Format
						  SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd-MMM-YYYY");
						  simpleDateFormat2.format(date);
						  emailPojo.setDate(simpleDateFormat2.format(date));
						  }catch (Exception e) {
							// TODO: handle exception
						}*/

					//  list.add(emailPojo);
					  
					  if (empData.getSearchTextClient().equalsIgnoreCase("anger")) {
							
							if (emailPojo.getAnger()>0) {
								
								listMail.add(emailPojo);
							}
							
						}else if (empData.getSearchTextClient().equalsIgnoreCase("joy")) {
							
							if (emailPojo.getJoy()>0) {
								
								listMail.add(emailPojo);
							}
							
						}else if (empData.getSearchTextClient().equalsIgnoreCase("sadness")) {
							
							if (emailPojo.getSadness()>0) {
								
								listMail.add(emailPojo);
							}
							
							
						}else if (empData.getSearchTextClient().equalsIgnoreCase("tentative")) {
							
							if (emailPojo.getTentative()>0) {
								
								listMail.add(emailPojo);
							}
							
						}else if (empData.getSearchTextClient().equalsIgnoreCase("analytical")) {
							
							if (emailPojo.getAnalytical()>0) {
								
								listMail.add(emailPojo);
							}
							
						}else if (empData.getSearchTextClient().equalsIgnoreCase("confident")) {
							
							if (emailPojo.getConfident()>0) {
								
								listMail.add(emailPojo);
							}
							
						}else if (empData.getSearchTextClient().equalsIgnoreCase("fear")) {
							
							if (emailPojo.getFear()>0) {
								
								listMail.add(emailPojo);
							}
							
						}
					
				// listMail.add(filter.getTeamEmails());
			 }
			 
}
			
			 
			 
			 if (receive) {
					
					
					
				 aggregation = Aggregation.newAggregation(
						
						Aggregation.match(Criteria.where("employeeIdFK").is(employeeData.getEmployeeId())),
						Aggregation.unwind("$lineItems"),
						Aggregation.match(Criteria.where(criteria).gt(0).andOperator(Criteria.where("lineItems.type").is("Received"))),
						Aggregation.match(Criteria.where(criteria).gt(0).andOperator(Criteria.where("lineItems.fromMail").is(empData.getClientEmailId()))),
						//Aggregation.project("employeeHierarchy")
						projectionOperation
						
						
						);
				 
				 List<FilterResultPojo> results = mongoTemplate.aggregate(aggregation, DailyEmployeeEmailToneBO.class, FilterResultPojo.class).getMappedResults();
					
				 for(FilterResultPojo filter :results){
					 
					 
					 
					 
					 EmailPojo emailPojo = filter.getTeamEmails();
						Map<String,Double> hashMap = new HashMap<String,Double>(); 
						hashMap.put("anger", emailPojo.getAnger());
						hashMap.put("joy", emailPojo.getJoy());
						hashMap.put("sadness", emailPojo.getSadness());
						hashMap.put("tentative", emailPojo.getTentative());
						hashMap.put("analytical", emailPojo.getAnalytical());
						hashMap.put("confident", emailPojo.getConfident());
						hashMap.put("fear", emailPojo.getFear());
						
						try {
							emailPojo.setFrom(employeeRepository.findByEmailIdIgnoreCase(emailPojo.getFromMail()).getEmployeeName());
						}catch(Exception e) {
							emailPojo.setFrom("");
						}
						
						try {
							  if(emailPojo.getToClientEmails().size() > 0) {
								  System.out.println("in getToclients loop");
								  String toClientEmails = emailPojo.getToClientEmails().get(0);
								  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(toClientEmails.split("@")[1]).get(0).getCompanyName());
							  }else if(emailPojo.getFromMail().length() >0 && emailPojo.getFromMail().split("@")[1] != employeeData.getEmailId().split("@")[1] ) {
								  System.out.println("in from mail loop");
								  String domain = emailPojo.getFromMail().split("@")[1];
								  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(domain).get(0).getCompanyName());
							  }else {
								  System.out.println("in else loop");
								  emailPojo.setCompanyName("");
							  }
						  }catch(Exception e) {
							  System.err.println(e);
						  }
						
						  Map<String, Double> sortedMapDsc =	sortByComparator(hashMap, false);
						  int count =0;
						  for (Entry<String, Double> entry : sortedMapDsc.entrySet())
					        {
					            System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
					            
					            
					         
					            switch (entry.getKey()) {
					            
								case "anger":
									if (count<2) {
										emailPojo.setAnger(entry.getValue());
										count++;
//										UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
//												.getContext().getAuthentication();
										
//										EmployeePersonalDataDTO employeePersonalDataDTO1 = new EmployeePersonalDataDTO();
										
										String emailId1 = (String) authObj.getUserSessionInformation().get(
												EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
										
										EmployeeBO employeeData1 =employeeRepository.findOne(emailId1);
										
										ClientBO clients= clientRepository.findByEmployeeIdFK(employeeData1.getEmployeeId());
										if(clients != null) {
											if(emailPojo.getType().equalsIgnoreCase("Received")) {
												
												if(clients.getClients() != null) {
													for(FilterPojo client: clients.getClients()) {
														if(client.getEmailId().equalsIgnoreCase(emailPojo.getFromMail())) {
															if(client.getExecutive() != null)
															if(client.getExecutive().equalsIgnoreCase("yes")) {
																emailPojo.setAdverse("yes");
															}
														}
													}
												}
												
											}else if(emailPojo.getType().equalsIgnoreCase("sent")){
												if(clients.getClients() != null) {
													for(FilterPojo client: clients.getClients()) {
														for(String clientEmail: emailPojo.getToClientEmails()) {
															if(client.getEmailId().equalsIgnoreCase(clientEmail)) {
																if(client.getExecutive() != null)
																if(client.getExecutive().equalsIgnoreCase("yes")) {
																	emailPojo.setAdverse("yes");
																}
															}
														}
													}
												}
											}
										}
									}else {
										emailPojo.setAnger(0.0);
										count++;
									}
									
									break;
									
								case "joy":

									if (count<2) {
										emailPojo.setJoy(entry.getValue());
										count++;
									}else {
										emailPojo.setJoy(0.0);
										count++;
									}
									break;	
								case "sadness":
									if (count<2) {
										emailPojo.setSadness(entry.getValue());
										count++;
									}else {
										emailPojo.setSadness(0.0);
										count++;
									}
									break;
									
								case "tentative":
									if (count<2) {
										emailPojo.setTentative(entry.getValue());
										count++;
									}else {
										emailPojo.setTentative(0.0);
										count++;
									}
									break;
																		
								case "analytical":
									if (count<2) {
										emailPojo.setAnalytical(entry.getValue());
										count++;
									}else {
										emailPojo.setAnalytical(0.0);
										count++;
									}
									break;
									
								case "confident":
									if (count<2) {
										emailPojo.setConfident(entry.getValue());
										count++;
									}else {
										emailPojo.setConfident(0.0);
										count++;
									}
									break;
									
								case "fear":
									if (count<2) {
										emailPojo.setFear(entry.getValue());
										count++;
									}else {
										emailPojo.setFear(0.0);
										count++;
									}
									break;

								default:
									break;
									
								}
					            
					          
					        }
						  
						  System.out.println(sortedMapDsc);
						  
						 /* try {
							  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");
							  Date date = simpleDateFormat.parse(emailPojo.getDate());
							  // new Format
							  SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd-MMM-YYYY");
							  simpleDateFormat2.format(date);
							  emailPojo.setDate(simpleDateFormat2.format(date));
							  }catch (Exception e) {
								// TODO: handle exception
							}*/

						//  list.add(emailPojo);
						  
						  if (empData.getSearchTextClient().equalsIgnoreCase("anger")) {
								
								if (emailPojo.getAnger()>0) {
									
									listMail.add(emailPojo);
								}
								
							}else if (empData.getSearchTextClient().equalsIgnoreCase("joy")) {
								
								if (emailPojo.getJoy()>0) {
									
									listMail.add(emailPojo);
								}
								
							}else if (empData.getSearchTextClient().equalsIgnoreCase("sadness")) {
								
								if (emailPojo.getSadness()>0) {
									
									listMail.add(emailPojo);
								}
								
								
							}else if (empData.getSearchTextClient().equalsIgnoreCase("tentative")) {
								
								if (emailPojo.getTentative()>0) {
									
									listMail.add(emailPojo);
								}
								
							}else if (empData.getSearchTextClient().equalsIgnoreCase("analytical")) {
								
								if (emailPojo.getAnalytical()>0) {
									
									listMail.add(emailPojo);
								}
								
							}else if (empData.getSearchTextClient().equalsIgnoreCase("confident")) {
								
								if (emailPojo.getConfident()>0) {
									
									listMail.add(emailPojo);
								}
								
							}else if (empData.getSearchTextClient().equalsIgnoreCase("fear")) {
								
								if (emailPojo.getFear()>0) {
									
									listMail.add(emailPojo);
								}
								
							}
						
					// listMail.add(filter.getTeamEmails());
				 }
				 
				 aggregation1 = Aggregation.newAggregation(
							
							Aggregation.match(Criteria.where("employeeIdFK").is(employeeData.getEmployeeId())),
							Aggregation.unwind("$clientEmailItems"),
							Aggregation.match(Criteria.where(criteria).gt(0).andOperator(Criteria.where("clientEmailItems.type").is("Received"))),
							Aggregation.match(Criteria.where(criteria).gt(0).andOperator(Criteria.where("clientEmailItems.fromMail").is(empData.getClientEmailId()))),
							//Aggregation.project("employeeHierarchy")
							projectionOperation1
							
							
							);
					 
					 List<FilterResultPojo> results1 = mongoTemplate.aggregate(aggregation1, DailyEmployeeEmailToneBO.class, FilterResultPojo.class).getMappedResults();
						
					 for(FilterResultPojo filter :results1){
						 
						 
						 
						 
						 EmailPojo emailPojo = filter.getTeamEmails();
							Map<String,Double> hashMap = new HashMap<String,Double>(); 
							hashMap.put("anger", emailPojo.getAnger());
							hashMap.put("joy", emailPojo.getJoy());
							hashMap.put("sadness", emailPojo.getSadness());
							hashMap.put("tentative", emailPojo.getTentative());
							hashMap.put("analytical", emailPojo.getAnalytical());
							hashMap.put("confident", emailPojo.getConfident());
							hashMap.put("fear", emailPojo.getFear());
							
							try {
								emailPojo.setFrom(employeeRepository.findByEmailIdIgnoreCase(emailPojo.getFromMail()).getEmployeeName());
							}catch(Exception e) {
								emailPojo.setFrom("");
							}
							
							try {
								  if(emailPojo.getToClientEmails().size() > 0) {
									  System.out.println("in getToclients loop");
									  String toClientEmails = emailPojo.getToClientEmails().get(0);
									  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(toClientEmails.split("@")[1]).get(0).getCompanyName());
								  }else if(emailPojo.getFromMail().length() >0 && emailPojo.getFromMail().split("@")[1] != employeeData.getEmailId().split("@")[1] ) {
									  System.out.println("in from mail loop");
									  String domain = emailPojo.getFromMail().split("@")[1];
									  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(domain).get(0).getCompanyName());
								  }else {
									  System.out.println("in else loop");
									  emailPojo.setCompanyName("");
								  }
							  }catch(Exception e) {
								  System.err.println(e);
							  }
							
							  Map<String, Double> sortedMapDsc =	sortByComparator(hashMap, false);
							  int count =0;
							  for (Entry<String, Double> entry : sortedMapDsc.entrySet())
						        {
						            System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
						            
						            
						         
						            switch (entry.getKey()) {
						            
									case "anger":
										if (count<2) {
											emailPojo.setAnger(entry.getValue());
											count++;
//											UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
//													.getContext().getAuthentication();
//											
//											EmployeePersonalDataDTO employeePersonalDataDTO1 = new EmployeePersonalDataDTO();
											
											String emailId1 = (String) authObj.getUserSessionInformation().get(
													EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
											
											EmployeeBO employeeData1 =employeeRepository.findOne(emailId1);
											
											ClientBO clients= clientRepository.findByEmployeeIdFK(employeeData1.getEmployeeId());
											if(clients != null) {
												if(emailPojo.getType().equalsIgnoreCase("Received")) {
													
													if(clients.getClients() != null) {
														for(FilterPojo client: clients.getClients()) {
															if(client.getEmailId().equalsIgnoreCase(emailPojo.getFromMail())) {
																if(client.getExecutive() != null)
																if(client.getExecutive().equalsIgnoreCase("yes")) {
																	emailPojo.setAdverse("yes");
																}
															}
														}
													}
													
												}else if(emailPojo.getType().equalsIgnoreCase("sent")){
													if(clients.getClients() != null) {
														for(FilterPojo client: clients.getClients()) {
															for(String clientEmail: emailPojo.getToClientEmails()) {
																if(client.getEmailId().equalsIgnoreCase(clientEmail)) {
																	if(client.getExecutive() != null)
																	if(client.getExecutive().equalsIgnoreCase("yes")) {
																		emailPojo.setAdverse("yes");
																	}
																}
															}
														}
													}
												}
											}
										}else {
											emailPojo.setAnger(0.0);
											count++;
										}
										
										break;
										
									case "joy":

										if (count<2) {
											emailPojo.setJoy(entry.getValue());
											count++;
										}else {
											emailPojo.setJoy(0.0);
											count++;
										}
										break;	
									case "sadness":
										if (count<2) {
											emailPojo.setSadness(entry.getValue());
											count++;
										}else {
											emailPojo.setSadness(0.0);
											count++;
										}
										break;
										
									case "tentative":
										if (count<2) {
											emailPojo.setTentative(entry.getValue());
											count++;
										}else {
											emailPojo.setTentative(0.0);
											count++;
										}
										break;
																			
									case "analytical":
										if (count<2) {
											emailPojo.setAnalytical(entry.getValue());
											count++;
										}else {
											emailPojo.setAnalytical(0.0);
											count++;
										}
										break;
										
									case "confident":
										if (count<2) {
											emailPojo.setConfident(entry.getValue());
											count++;
										}else {
											emailPojo.setConfident(0.0);
											count++;
										}
										break;
										
									case "fear":
										if (count<2) {
											emailPojo.setFear(entry.getValue());
											count++;
										}else {
											emailPojo.setFear(0.0);
											count++;
										}
										break;

									default:
										break;
										
									}
						            
						          
						        }
							  
							  System.out.println(sortedMapDsc);
							  
							 /* try {
								  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");
								  Date date = simpleDateFormat.parse(emailPojo.getDate());
								  // new Format
								  SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd-MMM-YYYY");
								  simpleDateFormat2.format(date);
								  emailPojo.setDate(simpleDateFormat2.format(date));
								  }catch (Exception e) {
									// TODO: handle exception
								}*/

							//  list.add(emailPojo);
							  
							  if (empData.getSearchTextClient().equalsIgnoreCase("anger")) {
									
									if (emailPojo.getAnger()>0) {
										
										listMail.add(emailPojo);
									}
									
								}else if (empData.getSearchTextClient().equalsIgnoreCase("joy")) {
									
									if (emailPojo.getJoy()>0) {
										
										listMail.add(emailPojo);
									}
									
								}else if (empData.getSearchTextClient().equalsIgnoreCase("sadness")) {
									
									if (emailPojo.getSadness()>0) {
										
										listMail.add(emailPojo);
									}
									
									
								}else if (empData.getSearchTextClient().equalsIgnoreCase("tentative")) {
									
									if (emailPojo.getTentative()>0) {
										
										listMail.add(emailPojo);
									}
									
								}else if (empData.getSearchTextClient().equalsIgnoreCase("analytical")) {
									
									if (emailPojo.getAnalytical()>0) {
										
										listMail.add(emailPojo);
									}
									
								}else if (empData.getSearchTextClient().equalsIgnoreCase("confident")) {
									
									if (emailPojo.getConfident()>0) {
										
										listMail.add(emailPojo);
									}
									
								}else if (empData.getSearchTextClient().equalsIgnoreCase("fear")) {
									
									if (emailPojo.getFear()>0) {
										
										listMail.add(emailPojo);
									}
									
								}
							
						// listMail.add(filter.getTeamEmails());
					 }
				 
				}
			 
			 employeePersonalDataDTO.setListEmailAnalyser(listMail);
			 return employeePersonalDataDTO;
			 
		}
	
public EmployeePersonalDataDTO getEmailOnToneClickOrg(ClientDataDTO empData) {
	
//	EmployeePersonalDataDTO employeePersonalDataDTO = new EmployeePersonalDataDTO();

	UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
			.getContext().getAuthentication();
	
	EmployeePersonalDataDTO employeePersonalDataDTO = new EmployeePersonalDataDTO();
	
	String emailId = (String) authObj.getUserSessionInformation().get(
			EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
	
	EmployeeBO employeeData = employeeRepository.findByEmailIdIgnoreCase(emailId);
	EmployeeRoleBO employeeRolesPojo = employeeRoleRepository.findByEmployeeIdFKAndStatus(employeeData.getEmployeeId(), "active");
	Boolean receive = false;
	Boolean sent = false;
    String companyDomain = companyRepository.findById(empData.getCompanyId()).getCompanyEmailDomain();
			
			 employeePersonalDataDTO.setNoOfTeamMember(employeeRolesPojo.getTeamSize());
			// employeePersonalDataDTO.setConsolidatedTone(employeeRolesPojo.getConsolidatedTone());
			/* employeePersonalDataDTO.setToneOfTeamMail(employeeRolesPojo.getConsolidatedTone().getToneWithTeam()
					 .getAllMailScore());
			 employeePersonalDataDTO.setToneOfClientMail(employeeRolesPojo.getConsolidatedTone().getToneWithClient()
					 .getAllMailScore());*/
			 employeePersonalDataDTO.setDepartment(employeeRolesPojo.getDepartment());
			 employeePersonalDataDTO.setDesignation(employeeRolesPojo.getDesignation());
			 employeePersonalDataDTO.setNoOfMail(employeeRolesPojo.getConsolidatedTone().getTotalMail());
			 //Relation with team
			 Double joy =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getJoy();
			 Double tnt =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getTentative();
			 Double ana =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getAnalytical();
			 Double cnfdnc =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getConfident();
			 Double anger =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getAnger();
			 Double sad =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getSadness();
			 Double fear =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getFear();
			
			 Double joyness = (double) Math.round(((joy+tnt+ana+cnfdnc)*100)/(joy+tnt+ana+cnfdnc+anger+sad+fear));
			 Double angry =(double) Math.round(((anger+sad+fear)*100)/(joy+tnt+ana+cnfdnc+anger+sad+fear));
			 EmployeeRelationBO employeeRelationBO = new EmployeeRelationBO();
			 employeeRelationBO.setGood(joyness);
			 employeeRelationBO.setBad(angry);
			 employeePersonalDataDTO.setRelationWithTeam(employeeRelationBO);
			 
			
			 
			 
			 //Relation with Client
			 Double joyClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getJoy();
			 Double anaClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getAnalytical();
			 Double tntClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getTentative();
			 Double confClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getConfident();
			 Double angerClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getAnger();
			 Double sadClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getSadness();
			 Double fearClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getFear();
			 
			
			 Double joynessClnt = (double) Math.round(((joyClnt+anaClnt+tntClnt+confClnt)*100)/(joyClnt+anaClnt+tntClnt+confClnt+angerClnt+sadClnt+fearClnt));
			 Double angryClnt =(double) Math.round(((angerClnt+sadClnt+fearClnt)*100)/(joyClnt+anaClnt+tntClnt+confClnt+angerClnt+sadClnt+fearClnt));
			 EmployeeRelationBO employeeRelationBO1 = new EmployeeRelationBO();
			 employeeRelationBO1.setGood(joynessClnt);
			 employeeRelationBO1.setBad(angryClnt);
			 employeePersonalDataDTO.setRelationWithClient(employeeRelationBO1);
			 
			
			 
			 
			
		//}
	//}
	
	employeePersonalDataDTO.setEmployeeName(employeeData.getEmployeeName());
	employeePersonalDataDTO.setEmployeeId(employeeData.getEmployeeId());
	
	if (employeeRolesPojo.getReportToId()==null) {
		
		employeePersonalDataDTO.setReportTo("None");
		
	}else {
	employeePersonalDataDTO.setReportTo(employeeRepository.findByEmployeeId(employeeRolesPojo.getReportToId()).getEmployeeName());
	}
	
	ProjectionOperation projectionOperation = Aggregation.project()
			.andExpression("lineItems").as("teamEmails")
			.andExclude("_id");
	
	ProjectionOperation projectionOperation1 = Aggregation.project()
			.andExpression("clientEmailItems").as("teamEmails")
			.andExclude("_id");
	
	Aggregation aggregation = null;
	Aggregation aggregation1 = null;
	
	List<EmailPojo> listMail = new ArrayList<EmailPojo>();
	
	String criteria = null;
	
	// make query according to user criteria
	if (empData.getSearchTextOrg().equalsIgnoreCase("anger")) {
		
		criteria = "lineItems.anger";
		
	}else if (empData.getSearchTextOrg().equalsIgnoreCase("joy")) {
		
		criteria = "lineItems.joy";
		
	}else if (empData.getSearchTextOrg().equalsIgnoreCase("sadness")) {
		
		criteria = "lineItems.sadness";
		
	}else if (empData.getSearchTextOrg().equalsIgnoreCase("tentative")) {
		
		criteria = "lineItems.tentative";
		
	}else if (empData.getSearchTextOrg().equalsIgnoreCase("analytical")) {
		
		criteria = "lineItems.analytical";
		
	}else if (empData.getSearchTextOrg().equalsIgnoreCase("confident")) {
		
		criteria = "lineItems.confident";
		
	}else if (empData.getSearchTextOrg().equalsIgnoreCase("fear")) {
		
		criteria = "lineItems.fear";
		
	}
	
	if (empData.getIdentify().equalsIgnoreCase("all")) {
		
	
		
//	 aggregation = Aggregation.newAggregation(
//			
//			Aggregation.match(Criteria.where("employeeIdFK").is(empData.getEmployeeId())),
//			Aggregation.unwind("$lineItems"),
//			Aggregation.match(Criteria.where(criteria).gt(0)),
//			//Aggregation.project("employeeHierarchy")
//			projectionOperation
//			
//			
//			);
		
		receive = true;
		sent = true;
	 
	}
	
	if (empData.getIdentify().equalsIgnoreCase("sent")) {
		sent = true;
	}
	
	if (empData.getIdentify().equalsIgnoreCase("receive")) {
		receive = true;
	}
		
		
	if(sent) {	
		 aggregation = Aggregation.newAggregation(
				
				Aggregation.match(Criteria.where("employeeIdFK").is(employeeData.getEmployeeId())),
				Aggregation.unwind("$lineItems"),
				Aggregation.match(Criteria.where(criteria).gt(0).andOperator(Criteria.where("lineItems.type").is("sent"))),
				Aggregation.match(Criteria.where(criteria).gt(0).andOperator(Criteria.where("lineItems.toClientEmails").regex(companyDomain))),
				//Aggregation.project("employeeHierarchy")
				projectionOperation
				
				
				);
		 
		
	
	
	
	 List<FilterResultPojo> results = mongoTemplate.aggregate(aggregation, DailyEmployeeEmailToneBO.class, FilterResultPojo.class).getMappedResults();
	
	 for(FilterResultPojo filter :results){
		 
		 
		 
		 
		 EmailPojo emailPojo = filter.getTeamEmails();
			Map<String,Double> hashMap = new HashMap<String,Double>(); 
			hashMap.put("anger", emailPojo.getAnger());
			hashMap.put("joy", emailPojo.getJoy());
			hashMap.put("sadness", emailPojo.getSadness());
			hashMap.put("tentative", emailPojo.getTentative());
			hashMap.put("analytical", emailPojo.getAnalytical());
			hashMap.put("confident", emailPojo.getConfident());
			hashMap.put("fear", emailPojo.getFear());
			
			try {
				emailPojo.setFrom(employeeRepository.findByEmailIdIgnoreCase(emailPojo.getFromMail()).getEmployeeName());
			}catch(Exception e) {
				emailPojo.setFrom("");
			}
			
			try {
				  if(emailPojo.getToClientEmails().size() > 0) {
					  System.out.println("in getToclients loop");
					  String toClientEmails = emailPojo.getToClientEmails().get(0);
					  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(toClientEmails.split("@")[1]).get(0).getCompanyName());
				  }else if(emailPojo.getFromMail().length() >0 && emailPojo.getFromMail().split("@")[1] != employeeData.getEmailId().split("@")[1] ) {
					  System.out.println("in from mail loop");
					  String domain = emailPojo.getFromMail().split("@")[1];
					  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(domain).get(0).getCompanyName());
				  }else {
					  System.out.println("in else loop");
					  emailPojo.setCompanyName("");
				  }
			  }catch(Exception e) {
				  System.err.println(e);
			  }
			
			  Map<String, Double> sortedMapDsc =	sortByComparator(hashMap, false);
			  int count =0;
			  for (Entry<String, Double> entry : sortedMapDsc.entrySet())
		        {
		            System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
		            
		            
		         
		            switch (entry.getKey()) {
		            
					case "anger":
						if (count<2) {
							emailPojo.setAnger(entry.getValue());
							count++;
//							UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
//									.getContext().getAuthentication();
							
//							EmployeePersonalDataDTO employeePersonalDataDTO1 = new EmployeePersonalDataDTO();
							
							String emailId1 = (String) authObj.getUserSessionInformation().get(
									EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
							
							EmployeeBO employeeData1 =employeeRepository.findOne(emailId1);
							
							ClientBO clients= clientRepository.findByEmployeeIdFK(employeeData1.getEmployeeId());
							if(clients != null) {
								if(emailPojo.getType().equalsIgnoreCase("Received")) {
									
									if(clients.getClients() != null) {
										for(FilterPojo client: clients.getClients()) {
											if(client.getEmailId().equalsIgnoreCase(emailPojo.getFromMail())) {
												if(client.getExecutive() != null)
												if(client.getExecutive().equalsIgnoreCase("yes")) {
													emailPojo.setAdverse("yes");
												}
											}
										}
									}
									
								}else if(emailPojo.getType().equalsIgnoreCase("sent")){
									if(clients.getClients() != null) {
										for(FilterPojo client: clients.getClients()) {
											for(String clientEmail: emailPojo.getToClientEmails()) {
												if(client.getEmailId().equalsIgnoreCase(clientEmail)) {
													if(client.getExecutive() != null)
													if(client.getExecutive().equalsIgnoreCase("yes")) {
														emailPojo.setAdverse("yes");
													}
												}
											}
										}
									}
								}
							}
						}else {
							emailPojo.setAnger(0.0);
							count++;
						}
						
						break;
						
					case "joy":

						if (count<2) {
							emailPojo.setJoy(entry.getValue());
							count++;
						}else {
							emailPojo.setJoy(0.0);
							count++;
						}
						break;	
					case "sadness":
						if (count<2) {
							emailPojo.setSadness(entry.getValue());
							count++;
						}else {
							emailPojo.setSadness(0.0);
							count++;
						}
						break;
						
					case "tentative":
						if (count<2) {
							emailPojo.setTentative(entry.getValue());
							count++;
						}else {
							emailPojo.setTentative(0.0);
							count++;
						}
						break;
															
					case "analytical":
						if (count<2) {
							emailPojo.setAnalytical(entry.getValue());
							count++;
						}else {
							emailPojo.setAnalytical(0.0);
							count++;
						}
						break;
						
					case "confident":
						if (count<2) {
							emailPojo.setConfident(entry.getValue());
							count++;
						}else {
							emailPojo.setConfident(0.0);
							count++;
						}
						break;
						
					case "fear":
						if (count<2) {
							emailPojo.setFear(entry.getValue());
							count++;
						}else {
							emailPojo.setFear(0.0);
							count++;
						}
						break;

					default:
						break;
						
					}
		            
		          
		        }
			  
			  System.out.println(sortedMapDsc);
			  
			 /* try {
				  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");
				  Date date = simpleDateFormat.parse(emailPojo.getDate());
				  // new Format
				  SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd-MMM-YYYY");
				  simpleDateFormat2.format(date);
				  emailPojo.setDate(simpleDateFormat2.format(date));
				  }catch (Exception e) {
					// TODO: handle exception
				}*/

			//  list.add(emailPojo);
			  
			  if (empData.getSearchTextOrg().equalsIgnoreCase("anger")) {
					
					if (emailPojo.getAnger()>0) {
						
						listMail.add(emailPojo);
					}
					
				}else if (empData.getSearchTextOrg().equalsIgnoreCase("joy")) {
					
					if (emailPojo.getJoy()>0) {
						
						listMail.add(emailPojo);
					}
					
				}else if (empData.getSearchTextOrg().equalsIgnoreCase("sadness")) {
					
					if (emailPojo.getSadness()>0) {
						
						listMail.add(emailPojo);
					}
					
					
				}else if (empData.getSearchTextOrg().equalsIgnoreCase("tentative")) {
					
					if (emailPojo.getTentative()>0) {
						
						listMail.add(emailPojo);
					}
					
				}else if (empData.getSearchTextOrg().equalsIgnoreCase("analytical")) {
					
					if (emailPojo.getAnalytical()>0) {
						
						listMail.add(emailPojo);
					}
					
				}else if (empData.getSearchTextOrg().equalsIgnoreCase("confident")) {
					
					if (emailPojo.getConfident()>0) {
						
						listMail.add(emailPojo);
					}
					
				}else if (empData.getSearchTextOrg().equalsIgnoreCase("fear")) {
					
					if (emailPojo.getFear()>0) {
						
						listMail.add(emailPojo);
					}
					
				}
			
		// listMail.add(filter.getTeamEmails());
	 }
	 
	 aggregation1 = Aggregation.newAggregation(
				
				Aggregation.match(Criteria.where("employeeIdFK").is(employeeData.getEmployeeId())),
				Aggregation.unwind("$clientEmailItems"),
				Aggregation.match(Criteria.where(criteria).gt(0).andOperator(Criteria.where("clientEmailItems.type").is("sent"))),
				Aggregation.match(Criteria.where(criteria).gt(0).andOperator(Criteria.where("clientEmailItems.toClientEmails").regex(companyDomain))),
				//Aggregation.project("employeeHierarchy")
				projectionOperation1
				
				
				);
		 
		
	
	
	
	 List<FilterResultPojo> results1 = mongoTemplate.aggregate(aggregation1, DailyEmployeeEmailToneBO.class, FilterResultPojo.class).getMappedResults();
	
	 for(FilterResultPojo filter :results1){
		 
		 
		 
		 
		 EmailPojo emailPojo = filter.getTeamEmails();
			Map<String,Double> hashMap = new HashMap<String,Double>(); 
			hashMap.put("anger", emailPojo.getAnger());
			hashMap.put("joy", emailPojo.getJoy());
			hashMap.put("sadness", emailPojo.getSadness());
			hashMap.put("tentative", emailPojo.getTentative());
			hashMap.put("analytical", emailPojo.getAnalytical());
			hashMap.put("confident", emailPojo.getConfident());
			hashMap.put("fear", emailPojo.getFear());
			
			try {
				emailPojo.setFrom(employeeRepository.findByEmailIdIgnoreCase(emailPojo.getFromMail()).getEmployeeName());
			}catch(Exception e) {
				emailPojo.setFrom("");
			}
			
			try {
				  if(emailPojo.getToClientEmails().size() > 0) {
					  System.out.println("in getToclients loop");
					  String toClientEmails = emailPojo.getToClientEmails().get(0);
					  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(toClientEmails.split("@")[1]).get(0).getCompanyName());
				  }else if(emailPojo.getFromMail().length() >0 && emailPojo.getFromMail().split("@")[1] != employeeData.getEmailId().split("@")[1] ) {
					  System.out.println("in from mail loop");
					  String domain = emailPojo.getFromMail().split("@")[1];
					  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(domain).get(0).getCompanyName());
				  }else {
					  System.out.println("in else loop");
					  emailPojo.setCompanyName("");
				  }
			  }catch(Exception e) {
				  System.err.println(e);
			  }
			
			  Map<String, Double> sortedMapDsc =	sortByComparator(hashMap, false);
			  int count =0;
			  for (Entry<String, Double> entry : sortedMapDsc.entrySet())
		        {
		            System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
		            
		            
		         
		            switch (entry.getKey()) {
		            
					case "anger":
						if (count<2) {
							emailPojo.setAnger(entry.getValue());
							count++;
//							UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
//									.getContext().getAuthentication();
							
//							EmployeePersonalDataDTO employeePersonalDataDTO1 = new EmployeePersonalDataDTO();
							
							String emailId1 = (String) authObj.getUserSessionInformation().get(
									EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
							
							EmployeeBO employeeData1 =employeeRepository.findOne(emailId1);
							
							ClientBO clients= clientRepository.findByEmployeeIdFK(employeeData1.getEmployeeId());
							if(clients != null) {
								if(emailPojo.getType().equalsIgnoreCase("Received")) {
									
									if(clients.getClients() != null) {
										for(FilterPojo client: clients.getClients()) {
											if(client.getEmailId().equalsIgnoreCase(emailPojo.getFromMail())) {
												if(client.getExecutive() != null)
												if(client.getExecutive().equalsIgnoreCase("yes")) {
													emailPojo.setAdverse("yes");
												}
											}
										}
									}
									
								}else if(emailPojo.getType().equalsIgnoreCase("sent")){
									if(clients.getClients() != null) {
										for(FilterPojo client: clients.getClients()) {
											for(String clientEmail: emailPojo.getToClientEmails()) {
												if(client.getEmailId().equalsIgnoreCase(clientEmail)) {
													if(client.getExecutive() != null)
													if(client.getExecutive().equalsIgnoreCase("yes")) {
														emailPojo.setAdverse("yes");
													}
												}
											}
										}
									}
								}
							}
						}else {
							emailPojo.setAnger(0.0);
							count++;
						}
						
						break;
						
					case "joy":

						if (count<2) {
							emailPojo.setJoy(entry.getValue());
							count++;
						}else {
							emailPojo.setJoy(0.0);
							count++;
						}
						break;	
					case "sadness":
						if (count<2) {
							emailPojo.setSadness(entry.getValue());
							count++;
						}else {
							emailPojo.setSadness(0.0);
							count++;
						}
						break;
						
					case "tentative":
						if (count<2) {
							emailPojo.setTentative(entry.getValue());
							count++;
						}else {
							emailPojo.setTentative(0.0);
							count++;
						}
						break;
															
					case "analytical":
						if (count<2) {
							emailPojo.setAnalytical(entry.getValue());
							count++;
						}else {
							emailPojo.setAnalytical(0.0);
							count++;
						}
						break;
						
					case "confident":
						if (count<2) {
							emailPojo.setConfident(entry.getValue());
							count++;
						}else {
							emailPojo.setConfident(0.0);
							count++;
						}
						break;
						
					case "fear":
						if (count<2) {
							emailPojo.setFear(entry.getValue());
							count++;
						}else {
							emailPojo.setFear(0.0);
							count++;
						}
						break;

					default:
						break;
						
					}
		            
		          
		        }
			  
			  System.out.println(sortedMapDsc);
			  
			 /* try {
				  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");
				  Date date = simpleDateFormat.parse(emailPojo.getDate());
				  // new Format
				  SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd-MMM-YYYY");
				  simpleDateFormat2.format(date);
				  emailPojo.setDate(simpleDateFormat2.format(date));
				  }catch (Exception e) {
					// TODO: handle exception
				}*/

			//  list.add(emailPojo);
			  
			  if (empData.getSearchTextOrg().equalsIgnoreCase("anger")) {
					
					if (emailPojo.getAnger()>0) {
						
						listMail.add(emailPojo);
					}
					
				}else if (empData.getSearchTextOrg().equalsIgnoreCase("joy")) {
					
					if (emailPojo.getJoy()>0) {
						
						listMail.add(emailPojo);
					}
					
				}else if (empData.getSearchTextOrg().equalsIgnoreCase("sadness")) {
					
					if (emailPojo.getSadness()>0) {
						
						listMail.add(emailPojo);
					}
					
					
				}else if (empData.getSearchTextOrg().equalsIgnoreCase("tentative")) {
					
					if (emailPojo.getTentative()>0) {
						
						listMail.add(emailPojo);
					}
					
				}else if (empData.getSearchTextOrg().equalsIgnoreCase("analytical")) {
					
					if (emailPojo.getAnalytical()>0) {
						
						listMail.add(emailPojo);
					}
					
				}else if (empData.getSearchTextOrg().equalsIgnoreCase("confident")) {
					
					if (emailPojo.getConfident()>0) {
						
						listMail.add(emailPojo);
					}
					
				}else if (empData.getSearchTextOrg().equalsIgnoreCase("fear")) {
					
					if (emailPojo.getFear()>0) {
						
						listMail.add(emailPojo);
					}
					
				}
			
		// listMail.add(filter.getTeamEmails());
	 }
	 
}
	
	 
	 
	 if (receive) {
			
			
			
		 aggregation = Aggregation.newAggregation(
				
				Aggregation.match(Criteria.where("employeeIdFK").is(employeeData.getEmployeeId())),
				Aggregation.unwind("$lineItems"),
				Aggregation.match(Criteria.where(criteria).gt(0).andOperator(Criteria.where("lineItems.type").is("Received"))),
				Aggregation.match(Criteria.where(criteria).gt(0).andOperator(Criteria.where("lineItems.fromMail").regex(companyDomain))),
				//Aggregation.project("employeeHierarchy")
				projectionOperation
				
				
				);
		 
		 List<FilterResultPojo> results = mongoTemplate.aggregate(aggregation, DailyEmployeeEmailToneBO.class, FilterResultPojo.class).getMappedResults();
			
		 for(FilterResultPojo filter :results){
			 
			 
			 
			 
			 EmailPojo emailPojo = filter.getTeamEmails();
				Map<String,Double> hashMap = new HashMap<String,Double>(); 
				hashMap.put("anger", emailPojo.getAnger());
				hashMap.put("joy", emailPojo.getJoy());
				hashMap.put("sadness", emailPojo.getSadness());
				hashMap.put("tentative", emailPojo.getTentative());
				hashMap.put("analytical", emailPojo.getAnalytical());
				hashMap.put("confident", emailPojo.getConfident());
				hashMap.put("fear", emailPojo.getFear());
				
				try {
					emailPojo.setFrom(employeeRepository.findByEmailIdIgnoreCase(emailPojo.getFromMail()).getEmployeeName());
				}catch(Exception e) {
					emailPojo.setFrom("");
				}
				
				try {
					  if(emailPojo.getToClientEmails().size() > 0) {
						  System.out.println("in getToclients loop");
						  String toClientEmails = emailPojo.getToClientEmails().get(0);
						  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(toClientEmails.split("@")[1]).get(0).getCompanyName());
					  }else if(emailPojo.getFromMail().length() >0 && emailPojo.getFromMail().split("@")[1] != employeeData.getEmailId().split("@")[1] ) {
						  System.out.println("in from mail loop");
						  String domain = emailPojo.getFromMail().split("@")[1];
						  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(domain).get(0).getCompanyName());
					  }else {
						  System.out.println("in else loop");
						  emailPojo.setCompanyName("");
					  }
				  }catch(Exception e) {
					  System.err.println(e);
				  }
				
				  Map<String, Double> sortedMapDsc =	sortByComparator(hashMap, false);
				  int count =0;
				  for (Entry<String, Double> entry : sortedMapDsc.entrySet())
			        {
			            System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
			            
			            
			         
			            switch (entry.getKey()) {
			            
						case "anger":
							if (count<2) {
								emailPojo.setAnger(entry.getValue());
								count++;
//								UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
//										.getContext().getAuthentication();
								
//								EmployeePersonalDataDTO employeePersonalDataDTO1 = new EmployeePersonalDataDTO();
								
								String emailId1 = (String) authObj.getUserSessionInformation().get(
										EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
								
								EmployeeBO employeeData1 =employeeRepository.findOne(emailId1);
								
								ClientBO clients= clientRepository.findByEmployeeIdFK(employeeData1.getEmployeeId());
								if(clients != null) {
									if(emailPojo.getType().equalsIgnoreCase("Received")) {
										
										if(clients.getClients() != null) {
											for(FilterPojo client: clients.getClients()) {
												if(client.getEmailId().equalsIgnoreCase(emailPojo.getFromMail())) {
													if(client.getExecutive() != null)
													if(client.getExecutive().equalsIgnoreCase("yes")) {
														emailPojo.setAdverse("yes");
													}
												}
											}
										}
										
									}else if(emailPojo.getType().equalsIgnoreCase("sent")){
										if(clients.getClients() != null) {
											for(FilterPojo client: clients.getClients()) {
												for(String clientEmail: emailPojo.getToClientEmails()) {
													if(client.getEmailId().equalsIgnoreCase(clientEmail)) {
														if(client.getExecutive() != null)
														if(client.getExecutive().equalsIgnoreCase("yes")) {
															emailPojo.setAdverse("yes");
														}
													}
												}
											}
										}
									}
								}
							}else {
								emailPojo.setAnger(0.0);
								count++;
							}
							
							break;
							
						case "joy":

							if (count<2) {
								emailPojo.setJoy(entry.getValue());
								count++;
							}else {
								emailPojo.setJoy(0.0);
								count++;
							}
							break;	
						case "sadness":
							if (count<2) {
								emailPojo.setSadness(entry.getValue());
								count++;
							}else {
								emailPojo.setSadness(0.0);
								count++;
							}
							break;
							
						case "tentative":
							if (count<2) {
								emailPojo.setTentative(entry.getValue());
								count++;
							}else {
								emailPojo.setTentative(0.0);
								count++;
							}
							break;
																
						case "analytical":
							if (count<2) {
								emailPojo.setAnalytical(entry.getValue());
								count++;
							}else {
								emailPojo.setAnalytical(0.0);
								count++;
							}
							break;
							
						case "confident":
							if (count<2) {
								emailPojo.setConfident(entry.getValue());
								count++;
							}else {
								emailPojo.setConfident(0.0);
								count++;
							}
							break;
							
						case "fear":
							if (count<2) {
								emailPojo.setFear(entry.getValue());
								count++;
							}else {
								emailPojo.setFear(0.0);
								count++;
							}
							break;

						default:
							break;
							
						}
			            
			          
			        }
				  
				  System.out.println(sortedMapDsc);
				  
				 /* try {
					  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");
					  Date date = simpleDateFormat.parse(emailPojo.getDate());
					  // new Format
					  SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd-MMM-YYYY");
					  simpleDateFormat2.format(date);
					  emailPojo.setDate(simpleDateFormat2.format(date));
					  }catch (Exception e) {
						// TODO: handle exception
					}*/

				//  list.add(emailPojo);
				  
				  if (empData.getSearchTextOrg().equalsIgnoreCase("anger")) {
						
						if (emailPojo.getAnger()>0) {
							
							listMail.add(emailPojo);
						}
						
					}else if (empData.getSearchTextOrg().equalsIgnoreCase("joy")) {
						
						if (emailPojo.getJoy()>0) {
							
							listMail.add(emailPojo);
						}
						
					}else if (empData.getSearchTextOrg().equalsIgnoreCase("sadness")) {
						
						if (emailPojo.getSadness()>0) {
							
							listMail.add(emailPojo);
						}
						
						
					}else if (empData.getSearchTextOrg().equalsIgnoreCase("tentative")) {
						
						if (emailPojo.getTentative()>0) {
							
							listMail.add(emailPojo);
						}
						
					}else if (empData.getSearchTextOrg().equalsIgnoreCase("analytical")) {
						
						if (emailPojo.getAnalytical()>0) {
							
							listMail.add(emailPojo);
						}
						
					}else if (empData.getSearchTextOrg().equalsIgnoreCase("confident")) {
						
						if (emailPojo.getConfident()>0) {
							
							listMail.add(emailPojo);
						}
						
					}else if (empData.getSearchTextOrg().equalsIgnoreCase("fear")) {
						
						if (emailPojo.getFear()>0) {
							
							listMail.add(emailPojo);
						}
						
					}
				
			// listMail.add(filter.getTeamEmails());
		 }
		 
		 aggregation1 = Aggregation.newAggregation(
					
					Aggregation.match(Criteria.where("employeeIdFK").is(employeeData.getEmployeeId())),
					Aggregation.unwind("$clientEmailItems"),
					Aggregation.match(Criteria.where(criteria).gt(0).andOperator(Criteria.where("clientEmailItems.type").is("Received"))),
					Aggregation.match(Criteria.where(criteria).gt(0).andOperator(Criteria.where("clientEmailItems.fromMail").regex(companyDomain))),
					//Aggregation.project("employeeHierarchy")
					projectionOperation1
					
					
					);
			 
			 List<FilterResultPojo> results1 = mongoTemplate.aggregate(aggregation1, DailyEmployeeEmailToneBO.class, FilterResultPojo.class).getMappedResults();
				
			 for(FilterResultPojo filter :results1){
				 
				 
				 
				 
				 EmailPojo emailPojo = filter.getTeamEmails();
					Map<String,Double> hashMap = new HashMap<String,Double>(); 
					hashMap.put("anger", emailPojo.getAnger());
					hashMap.put("joy", emailPojo.getJoy());
					hashMap.put("sadness", emailPojo.getSadness());
					hashMap.put("tentative", emailPojo.getTentative());
					hashMap.put("analytical", emailPojo.getAnalytical());
					hashMap.put("confident", emailPojo.getConfident());
					hashMap.put("fear", emailPojo.getFear());
					
					try {
						emailPojo.setFrom(employeeRepository.findByEmailIdIgnoreCase(emailPojo.getFromMail()).getEmployeeName());
					}catch(Exception e) {
						emailPojo.setFrom("");
					}
					
					try {
						  if(emailPojo.getToClientEmails().size() > 0) {
							  System.out.println("in getToclients loop");
							  String toClientEmails = emailPojo.getToClientEmails().get(0);
							  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(toClientEmails.split("@")[1]).get(0).getCompanyName());
						  }else if(emailPojo.getFromMail().length() >0 && emailPojo.getFromMail().split("@")[1] != employeeData.getEmailId().split("@")[1] ) {
							  System.out.println("in from mail loop");
							  String domain = emailPojo.getFromMail().split("@")[1];
							  emailPojo.setCompanyName(companyRepository.findByCompanyEmailDomain(domain).get(0).getCompanyName());
						  }else {
							  System.out.println("in else loop");
							  emailPojo.setCompanyName("");
						  }
					  }catch(Exception e) {
						  System.err.println(e);
					  }
					
					  Map<String, Double> sortedMapDsc =	sortByComparator(hashMap, false);
					  int count =0;
					  for (Entry<String, Double> entry : sortedMapDsc.entrySet())
				        {
				            System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
				            
				            
				         
				            switch (entry.getKey()) {
				            
							case "anger":
								if (count<2) {
									emailPojo.setAnger(entry.getValue());
									count++;
//									UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
//											.getContext().getAuthentication();
									
//									EmployeePersonalDataDTO employeePersonalDataDTO1 = new EmployeePersonalDataDTO();
									
									String emailId1 = (String) authObj.getUserSessionInformation().get(
											EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
									
									EmployeeBO employeeData1 =employeeRepository.findOne(emailId1);
									
									ClientBO clients= clientRepository.findByEmployeeIdFK(employeeData1.getEmployeeId());
									if(clients != null) {
										if(emailPojo.getType().equalsIgnoreCase("Received")) {
											
											if(clients.getClients() != null) {
												for(FilterPojo client: clients.getClients()) {
													if(client.getEmailId().equalsIgnoreCase(emailPojo.getFromMail())) {
														if(client.getExecutive() != null)
														if(client.getExecutive().equalsIgnoreCase("yes")) {
															emailPojo.setAdverse("yes");
														}
													}
												}
											}
											
										}else if(emailPojo.getType().equalsIgnoreCase("sent")){
											if(clients.getClients() != null) {
												for(FilterPojo client: clients.getClients()) {
													for(String clientEmail: emailPojo.getToClientEmails()) {
														if(client.getEmailId().equalsIgnoreCase(clientEmail)) {
															if(client.getExecutive() != null)
															if(client.getExecutive().equalsIgnoreCase("yes")) {
																emailPojo.setAdverse("yes");
															}
														}
													}
												}
											}
										}
									}
								}else {
									emailPojo.setAnger(0.0);
									count++;
								}
								
								break;
								
							case "joy":

								if (count<2) {
									emailPojo.setJoy(entry.getValue());
									count++;
								}else {
									emailPojo.setJoy(0.0);
									count++;
								}
								break;	
							case "sadness":
								if (count<2) {
									emailPojo.setSadness(entry.getValue());
									count++;
								}else {
									emailPojo.setSadness(0.0);
									count++;
								}
								break;
								
							case "tentative":
								if (count<2) {
									emailPojo.setTentative(entry.getValue());
									count++;
								}else {
									emailPojo.setTentative(0.0);
									count++;
								}
								break;
																	
							case "analytical":
								if (count<2) {
									emailPojo.setAnalytical(entry.getValue());
									count++;
								}else {
									emailPojo.setAnalytical(0.0);
									count++;
								}
								break;
								
							case "confident":
								if (count<2) {
									emailPojo.setConfident(entry.getValue());
									count++;
								}else {
									emailPojo.setConfident(0.0);
									count++;
								}
								break;
								
							case "fear":
								if (count<2) {
									emailPojo.setFear(entry.getValue());
									count++;
								}else {
									emailPojo.setFear(0.0);
									count++;
								}
								break;

							default:
								break;
								
							}
				            
				          
				        }
					  
					  System.out.println(sortedMapDsc);
					  
					 /* try {
						  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");
						  Date date = simpleDateFormat.parse(emailPojo.getDate());
						  // new Format
						  SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd-MMM-YYYY");
						  simpleDateFormat2.format(date);
						  emailPojo.setDate(simpleDateFormat2.format(date));
						  }catch (Exception e) {
							// TODO: handle exception
						}*/

					//  list.add(emailPojo);
					  
					  if (empData.getSearchTextOrg().equalsIgnoreCase("anger")) {
							
							if (emailPojo.getAnger()>0) {
								
								listMail.add(emailPojo);
							}
							
						}else if (empData.getSearchTextOrg().equalsIgnoreCase("joy")) {
							
							if (emailPojo.getJoy()>0) {
								
								listMail.add(emailPojo);
							}
							
						}else if (empData.getSearchTextOrg().equalsIgnoreCase("sadness")) {
							
							if (emailPojo.getSadness()>0) {
								
								listMail.add(emailPojo);
							}
							
							
						}else if (empData.getSearchTextOrg().equalsIgnoreCase("tentative")) {
							
							if (emailPojo.getTentative()>0) {
								
								listMail.add(emailPojo);
							}
							
						}else if (empData.getSearchTextOrg().equalsIgnoreCase("analytical")) {
							
							if (emailPojo.getAnalytical()>0) {
								
								listMail.add(emailPojo);
							}
							
						}else if (empData.getSearchTextOrg().equalsIgnoreCase("confident")) {
							
							if (emailPojo.getConfident()>0) {
								
								listMail.add(emailPojo);
							}
							
						}else if (empData.getSearchTextOrg().equalsIgnoreCase("fear")) {
							
							if (emailPojo.getFear()>0) {
								
								listMail.add(emailPojo);
							}
							
						}
					
				// listMail.add(filter.getTeamEmails());
			 }
		 
		}
	 
	 employeePersonalDataDTO.setListEmailAnalyser(listMail);
	 return employeePersonalDataDTO;
}
		
		// Personal Score Screen Filter code
		
		// search on basis of emailSubject
		
		
		public EmployeePersonalDataDTO filterOnMailHeading(EmployeePersonalDataDTO empData) {
			
			EmployeePersonalDataDTO employeePersonalEmployee = new EmployeePersonalDataDTO();
			
			
			List<EmailPojo> filterByCriterias = new ArrayList<EmailPojo>();
			
			 UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
						.getContext().getAuthentication();
				
				String emailId = (String) authObj.getUserSessionInformation().get(
						EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
				
				EmployeeBO employeeBO = employeeRepository.findOne(emailId);
			
			
			
			Aggregation aggregation = null;
			
			if (empData.getEmailSubject()!=null) {
				
				ProjectionOperation projectionOperation = Aggregation.project()
						.andExpression("clientEmailItems").as("teamEmails")
						.andExclude("_id");
			
			 aggregation = Aggregation.newAggregation(
					
					 Aggregation.match(Criteria.where("employeeIdFK").is(employeeBO.getEmployeeId())),
						Aggregation.unwind("clientEmailItems"),
						//Aggregation.match(Criteria.where("clientEmailItems.subject").regex(empData.getEmailSubject(),"i"))
						//Aggregation.match(Criteria.where("clientEmailItems.subject").is(empData.getEmailSubject()).andOperator(Criteria.where("clientEmailItems.type").is(empData.getEmailType())))
						Aggregation.match(Criteria.where("clientEmailItems.subject").is(empData.getEmailSubject())),
						projectionOperation
					
					);
			}
			

			List<FilterResultPojo> results = mongoTemplate.aggregate(aggregation, DailyEmployeeEmailToneBO.class, FilterResultPojo.class).getMappedResults();
			
			for(FilterResultPojo basicDBObject : results) {
				
				EmailPojo emailPojo = basicDBObject.getTeamEmails();
				
				/* try {
					  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");
					  Date date = simpleDateFormat.parse(emailPojo.getDate());
					  // new Format
					  SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd-MMM-YYYY");
					  simpleDateFormat2.format(date);
					  emailPojo.setDate(simpleDateFormat2.format(date));
					  }catch (Exception e) {
						// TODO: handle exception
					}*/
				
				filterByCriterias.add(emailPojo);
				
			}
			
			employeePersonalEmployee.setListEmailAnalyser(filterByCriterias);
			
			
			return employeePersonalEmployee;
		}
	
		
		
		// Email Subject typeAhead
	
		
		public List<EmailPojo> typeOnMailHeading(String empData,String empId) {
			
			
			List<EmailPojo> filterByCriterias = new ArrayList<EmailPojo>();
			
			 /*UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
						.getContext().getAuthentication();
				
				String emailId = (String) authObj.getUserSessionInformation().get(
						EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
				
				EmployeeBO employeeBO = employeeRepository.findOne(emailId);*/
			
			
			
			Aggregation aggregation = null;
			
			if (empData!=null) {
				
				ProjectionOperation projectionOperation = Aggregation.project()
						.andExpression("clientEmailItems").as("teamEmails")
						.andExclude("_id");
				
			 aggregation = Aggregation.newAggregation(
					
					 Aggregation.match(Criteria.where("employeeIdFK").is(empId)),
						Aggregation.unwind("clientEmailItems"),
						Aggregation.match(Criteria.where("clientEmailItems.subject").regex(empData,"i")),
						projectionOperation

					
					
					);
			}
			

			List<FilterResultPojo> results = mongoTemplate.aggregate(aggregation, DailyEmployeeEmailToneBO.class, FilterResultPojo.class).getMappedResults();
			
			for(FilterResultPojo basicDBObject : results) {
				EmailPojo emailPojo = new EmailPojo();
				emailPojo.setSubject(basicDBObject.getTeamEmails().getSubject());
				emailPojo.setType(basicDBObject.getTeamEmails().getType());
				
				//EmailPojo listRcv = basicDBObject.getClientEmailItems();
				
				filterByCriterias.add(emailPojo);
				
			}
			
			return filterByCriterias;
		}
		
		public List<EmployeeSearchPojo> typeOnReportIdSuggestion(String empData) {
			
			
			List<EmployeeSearchPojo> filterByCriterias = new ArrayList<EmployeeSearchPojo>();
			
			 UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
						.getContext().getAuthentication();
				
				String emailId = (String) authObj.getUserSessionInformation().get(
						EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
				
				EmployeeBO employeeBO = employeeRepository.findOne(emailId);
				
				if(employeeBO != null) {
					
					try {
						EmployeeRoleBO employeeRoles = employeeRoleRepository.findByEmployeeIdFKAndStatus(employeeBO.getEmployeeId(), "active");
						
						for(FilterByCriteria employees: employeeRoles.getEmployeeHierarchy()) {
							if(employees.getEmployeeId().toLowerCase().contains(empData.toLowerCase())) {
								EmployeeSearchPojo emp = new EmployeeSearchPojo();
								emp.setReportToId(employees.getEmployeeId());
								filterByCriterias.add(emp);
							}
						}
					}catch(Exception e) {
						e.printStackTrace();
					}
					
					return filterByCriterias;
				}else {
					
					return filterByCriterias;
					
				}
			
			
			
			
			

//			List<FilterResultPojo> results = mongoTemplate.aggregate(aggregation, DailyEmployeeEmailToneBO.class, FilterResultPojo.class).getMappedResults();
			
			
			
			
		}



		// searchMail on Client Name in Personal Screen
		
		public List<EmailPojo> filterOnClientNameInMail(EmployeePersonalDataDTO empData) {
			
			
			List<EmailPojo> filterByCriterias = new ArrayList<EmailPojo>();
			
			 /* UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
						.getContext().getAuthentication();
				
				String emailId = (String) authObj.getUserSessionInformation().get(
						EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);*/
				
				EmployeeBO employeeBO = employeeRepository.findOne(empData.getEmailId());
			
				ProjectionOperation projectionOperation = Aggregation.project()
						.andExpression("clientEmailItems").as("teamEmails")
						.andExclude("_id");
			
			Aggregation aggregation2 = Aggregation.newAggregation(
					
					Aggregation.match(Criteria.where("employeeIdFK").is(employeeBO.getEmployeeId())),
					Aggregation.unwind("clientEmailItems"),
					Aggregation.match(Criteria.where("clientEmailItems.toClientNames").regex(empData.getEmployeeName(),"i")),
					projectionOperation
					
					
					);
			
					List<FilterResultPojo> results = mongoTemplate.aggregate(aggregation2, DailyEmployeeEmailToneBO.class, FilterResultPojo.class).getMappedResults();
			
			
			for(FilterResultPojo basicDBObject : results) {
				
				EmailPojo emailPojo = basicDBObject.getTeamEmails();
				
				/* try {
					  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");
					  Date date = simpleDateFormat.parse(emailPojo.getDate());
					  // new Format
					  SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd-MMM-YYYY");
					  simpleDateFormat2.format(date);
					  emailPojo.setDate(simpleDateFormat2.format(date));
					  }catch (Exception e) {
						// TODO: handle exception
					}*/
				
				filterByCriterias.add(emailPojo);
				
			}
			
			return filterByCriterias;
		}
		
		
		
		
		// search mail on basis of date in personalScreen
		
		public List<EmailPojo> filterMailOnDate(EmployeePersonalDataDTO empData) {
			
			
			List<EmailPojo> filterByCriterias = new ArrayList<EmailPojo>();
			
			 /* UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
						.getContext().getAuthentication();
				
				String emailId = (String) authObj.getUserSessionInformation().get(
						EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);*/
				
						EmployeeBO employeeBO = employeeRepository.findOne(empData.getEmailId());
			
			
						ProjectionOperation projectionOperation = Aggregation.project()
						.andExpression("clientEmailItems").as("teamEmails")
						.andExclude("_id");
			
			
			Aggregation aggregation2 = Aggregation.newAggregation(
					
					Aggregation.match(Criteria.where("employeeIdFK").is(employeeBO.getEmployeeId())),
					Aggregation.unwind("clientEmailItems"),
					Aggregation.match(Criteria.where("clientEmailItems.date").gte(empData.getStartDate()).lte(empData.getEndDate())),
					projectionOperation
					
					
					);
			
					List<FilterResultPojo> results = mongoTemplate.aggregate(aggregation2, DailyEmployeeEmailToneBO.class, FilterResultPojo.class).getMappedResults();
			
			
			for(FilterResultPojo basicDBObject : results) {
				
				EmailPojo emailPojo = basicDBObject.getTeamEmails();
				/* try {
					  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");
					  Date date = simpleDateFormat.parse(emailPojo.getDate());
					  // new Format
					  SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd-MMM-YYYY");
					  simpleDateFormat2.format(date);
					  emailPojo.setDate(simpleDateFormat2.format(date));
					  }catch (Exception e) {
						// TODO: handle exception
					}*/
				
				filterByCriterias.add(emailPojo);
				
			}
			
			return filterByCriterias;
		}
		
		
		
		// search mail on basis of time in personalScreen
		
				public List<EmailPojo> filterMailOnTime(EmployeePersonalDataDTO empData) {
					
					
					List<EmailPojo> filterByCriterias = new ArrayList<EmailPojo>();
					
					 /* UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
								.getContext().getAuthentication();
						
						String emailId = (String) authObj.getUserSessionInformation().get(
								EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);*/
						
						EmployeeBO employeeBO = employeeRepository.findOne(empData.getEmailId());
						
					SortOperation	sortOperation =	Aggregation.sort(Sort.Direction.ASC,"clientEmailItems.time");
					
					ProjectionOperation projectionOperation = Aggregation.project()
							.andExpression("clientEmailItems").as("teamEmails")
							.andExclude("_id");
					
					
					Aggregation aggregation2 = Aggregation.newAggregation(
							
							Aggregation.match(Criteria.where("employeeIdFK").is(employeeBO.getEmployeeId())),
							Aggregation.unwind("clientEmailItems"),
							Aggregation.match(Criteria.where("clientEmailItems.time").gte(empData.getStartTime()).lte(empData.getEndTime())),
							sortOperation,
							projectionOperation
							
							
							);
					
							List<FilterResultPojo> results = mongoTemplate.aggregate(aggregation2, DailyEmployeeEmailToneBO.class, FilterResultPojo.class).getMappedResults();
					
					
					for(FilterResultPojo basicDBObject : results) {
						
						EmailPojo listRcv = basicDBObject.getTeamEmails();
						
						filterByCriterias.add(listRcv);
						
					}
					
					return filterByCriterias;
				}
				
		// personal Score Screen search on Sentiment tone
		
				public List<EmailPojo> filterMailOnTone(ClientDataDTO empData) {
					
					
					List<EmailPojo> filterByCriterias = new ArrayList<EmailPojo>();
					
					/* UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
								.getContext().getAuthentication();
						
						String emailId = (String) authObj.getUserSessionInformation().get(
								EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);*/
						
						EmployeeBO employeeBO = employeeRepository.findOne(empData.getEmailId());
					
						//List<MatchOperation> listOperation = new ArrayList<MatchOperation>();
						List<Criteria> criterias = new ArrayList<Criteria>();
						MatchOperation matchOperation = null ;	
						Criteria criteria = null;
						if (empData.getSearchCriteria().contains("anger")) {
							criteria =	Criteria.where("clientEmailItems.anger").gt(0);
							
							criterias.add(0,criteria);
							
						}
						if (empData.getSearchCriteria().contains("joy")) {
							
							if (criterias.size()>0) {
								criteria = criterias.get(0)
										.and("clientEmailItems.joy").gt(0);
								criterias.add(0, criteria);
							}else {
								criteria =	Criteria.where("clientEmailItems.joy").gt(0);
								criterias.add(0, criteria);
							}
						}
						if (empData.getSearchCriteria().contains("sadness")) {
							
							if (criterias.size()>0) {
								criteria = criterias.get(0)
										.and("clientEmailItems.sadness").gt(0);
								criterias.add(0, criteria);
							}else {
								criteria =	Criteria.where("clientEmailItems.sadness").gt(0);
								criterias.add(0, criteria);
							}
						}
						if (empData.getSearchCriteria().contains("fear")) {
							
							if (criterias.size()>0) {
								criteria = criterias.get(0)
										.and("clientEmailItems.fear").gt(0);
								criterias.add(0, criteria);
							}else {
								criteria =	Criteria.where("clientEmailItems.fear").gt(0);
								criterias.add(0, criteria);
							}
													
						}
						if (empData.getSearchCriteria().contains("tentative")) {
							
							if (criterias.size()>0) {
								criteria = criterias.get(0)
										.and("clientEmailItems.tentative").gt(0);
								criterias.add(0, criteria);
							}else {
								criteria =	Criteria.where("clientEmailItems.tentative").gt(0);
								criterias.add(0, criteria);
							}
						}
						if (empData.getSearchCriteria().contains("confident")) {
							
							if (criterias.size()>0) {
								criteria = criterias.get(0)
										.and("clientEmailItems.confident").gt(0);
								criterias.add(0, criteria);
							}else {
								criteria =	Criteria.where("clientEmailItems.confident").gt(0);
								criterias.add(0, criteria);
							}
						}
						if (empData.getSearchCriteria().contains("analytical")) {
							
							if (criterias.size()>0) {
								criteria = criterias.get(0)
										.and("clientEmailItems.analytical").gt(0);
								criterias.add(0, criteria);
							}else {
								criteria =	Criteria.where("clientEmailItems.analytical").gt(0);
								criterias.add(0, criteria);
							}
						}
						
						if (criterias.size()>0) {
							matchOperation = Aggregation.match(criterias.get(0));
						}
					
						ProjectionOperation projectionOperation = Aggregation.project()
								.andExpression("clientEmailItems").as("teamEmails")
								.andExclude("_id");
					
					Aggregation aggregation2 = Aggregation.newAggregation(
							
							Aggregation.match(Criteria.where("employeeIdFK").is(employeeBO.getEmployeeId())),
							Aggregation.unwind("clientEmailItems"),
							matchOperation,
							projectionOperation
							
							
							);
					
							List<FilterResultPojo> results = mongoTemplate.aggregate(aggregation2, DailyEmployeeEmailToneBO.class, FilterResultPojo.class).getMappedResults();
					
					
					for(FilterResultPojo basicDBObject : results) {
						
						EmailPojo emailPojo = basicDBObject.getTeamEmails();
						
						/* try {
							  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");
							  Date date = simpleDateFormat.parse(emailPojo.getDate());
							  // new Format
							  SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd-MMM-YYYY");
							  simpleDateFormat2.format(date);
							  emailPojo.setDate(simpleDateFormat2.format(date));
							  }catch (Exception e) {
								// TODO: handle exception
							}*/
						
						filterByCriterias.add(emailPojo);
						
					}
					
					return filterByCriterias;
				}		
				

		// sorting on Personal DashBoard Mail Screen
				
				

				public List<EmailPojo> sortMailOnPersonalScreen(ClientDataDTO empData) {
					
					
					List<EmailPojo> filterByCriterias = new ArrayList<EmailPojo>();
					
					/* UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
								.getContext().getAuthentication();
						
						String emailId = (String) authObj.getUserSessionInformation().get(
								EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);*/
						
						EmployeeBO employeeBO = employeeRepository.findOne(empData.getEmailId());
					
						List<SortOperation> criterias = new ArrayList<SortOperation>();
						SortOperation sortOperation = null;
						if (empData.getSearchCriteria().contains("anger")) {
							sortOperation =	Aggregation.sort(Sort.Direction.DESC,"clientEmailItems.anger");
							
							criterias.add(0,sortOperation);
							
						}
						if (empData.getSearchCriteria().contains("joy")) {
							
							if (criterias.size()>0) {
								sortOperation = criterias.get(0)
										.and(Sort.Direction.DESC,"clientEmailItems.joy");
								criterias.add(0, sortOperation);
							}else {
								sortOperation =	Aggregation.sort(Sort.Direction.DESC,"clientEmailItems.joy");
								criterias.add(0, sortOperation);
							}
						}
						if (empData.getSearchCriteria().contains("sadness")) {
							
							if (criterias.size()>0) {
								sortOperation = criterias.get(0)
										.and(Sort.Direction.DESC,"clientEmailItems.sadness");

								criterias.add(0, sortOperation);
							}else {
								sortOperation =Aggregation.sort(Sort.Direction.DESC,"clientEmailItems.sadness");
								criterias.add(0, sortOperation);
							}
						}
						if (empData.getSearchCriteria().contains("fear")) {
							
							if (criterias.size()>0) {
								sortOperation = criterias.get(0)
										.and(Sort.Direction.DESC,"clientEmailItems.fear");

								criterias.add(0, sortOperation);
							}else {
								sortOperation =	Aggregation.sort(Sort.Direction.DESC,"clientEmailItems.fear");
								criterias.add(0, sortOperation);
							}
													
						}
						if (empData.getSearchCriteria().contains("tentative")) {
							
							if (criterias.size()>0) {
								sortOperation = criterias.get(0)
										.and(Sort.Direction.DESC,"clientEmailItems.tentative");

								criterias.add(0, sortOperation);
							}else {
								sortOperation = Aggregation.sort(Sort.Direction.DESC,"clientEmailItems.tentative");
								criterias.add(0, sortOperation);
							}
						}
						if (empData.getSearchCriteria().contains("confident")) {
							
							if (criterias.size()>0) {
								sortOperation = criterias.get(0)
										.and(Sort.Direction.DESC,"clientEmailItems.confident");

								criterias.add(0, sortOperation);
							}else {
								sortOperation =	Aggregation.sort(Sort.Direction.DESC,"clientEmailItems.confident");
								criterias.add(0, sortOperation);
							}
						}
						if (empData.getSearchCriteria().contains("analytical")) {
							
							if (criterias.size()>0) {
								sortOperation = criterias.get(0)
										.and(Sort.Direction.DESC,"clientEmailItems.analytical");

								criterias.add(0, sortOperation);
							}else {
								sortOperation =	Aggregation.sort(Sort.Direction.DESC,"clientEmailItems.analytical");
								criterias.add(0, sortOperation);
							}
						}
						
						if (empData.getSearchCriteria().contains("subjectalphabetically")) {
							
							if (criterias.size()>0) {
								sortOperation = criterias.get(0)
										.and(Sort.Direction.DESC,"clientEmailItems.subject");

								criterias.add(0, sortOperation);
							}else {
								sortOperation =	Aggregation.sort(Sort.Direction.DESC,"clientEmailItems.subject");
								criterias.add(0, sortOperation);
							}
						}
						
						if (criterias.size()>0) {
							sortOperation = criterias.get(0);
						}else {
							sortOperation = Aggregation.sort(Sort.Direction.ASC,"clientEmailItems.subject");
						}
					
						
						ProjectionOperation projectionOperation = Aggregation.project()
								.andExpression("clientEmailItems").as("teamEmails")
								.andExclude("_id");
					
					Aggregation aggregation2 = Aggregation.newAggregation(
							
							Aggregation.match(Criteria.where("employeeIdFK").is(employeeBO.getEmployeeId())),
							Aggregation.unwind("clientEmailItems"),
							sortOperation,
							projectionOperation

							
							);
					
							List<FilterResultPojo> results = mongoTemplate.aggregate(aggregation2, DailyEmployeeEmailToneBO.class, FilterResultPojo.class).getMappedResults();
					
					
					for(FilterResultPojo basicDBObject : results) {
						
						EmailPojo emailPojo = basicDBObject.getTeamEmails();
						
						 /*try {
							  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");
							  Date date = simpleDateFormat.parse(emailPojo.getDate());
							  // new Format
							  SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd-MMM-YYYY");
							  simpleDateFormat2.format(date);
							  emailPojo.setDate(simpleDateFormat2.format(date));
							  }catch (Exception e) {
								// TODO: handle exception
							}*/
						
						filterByCriterias.add(emailPojo);
						
					}
					
					return filterByCriterias;
				}		
				
				
				
	// Team DashBoard Scrren Services
				
				
				
				// get DashBoard of Team
				
				public EmployeePersonalDataDTO getEmployeeTeamDashBoard(EmployeePersonalDataDTO employeePersonalDataDTO) {
					
					
					EmployeePersonalDataDTO employeePersonalDataDTO2 = new EmployeePersonalDataDTO();
					List<EmailPojo> list = new ArrayList<EmailPojo>();
					
					List<EmployeePersonalDataDTO> list1 = new ArrayList<EmployeePersonalDataDTO>();
					
					EmployeeBO employeeBO = employeeRepository.findByEmployeeId(employeePersonalDataDTO.getEmployeeId());
					
					employeePersonalDataDTO2.setEmployeeName(employeeBO.getEmployeeName());
					employeePersonalDataDTO2.setEmailId(employeeBO.getEmailId());
					employeePersonalDataDTO2.setEmployeeId(employeeBO.getEmployeeId());
					EmployeeRoleBO employeeRolesPojo = employeeRoleRepository.findByEmployeeIdFKAndStatus(employeePersonalDataDTO.getEmployeeId(), "active");
					
					employeePersonalDataDTO2.setDesignation(employeeRolesPojo.getDesignation());
					employeePersonalDataDTO2.setDepartment(employeeRolesPojo.getDepartment());
					if (employeeRolesPojo.getReportToId()==null) {
						
						employeePersonalDataDTO2.setReportTo("None");
					}else {
					employeePersonalDataDTO2.setReportTo(employeeRepository.findByEmployeeId(employeeRolesPojo.getReportToId()).
							getEmployeeName());
					}
					employeePersonalDataDTO2.setNoOfTeamMember(employeeRolesPojo.getTeamSize());
					 employeePersonalDataDTO2.setNoOfMail(employeeRolesPojo.getConsolidatedTone().getTotalMail());
					 
					 // sidemenu data
					 
					 Double joy =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getJoy();
					 Double tnt =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getTentative();
					 Double ana =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getAnalytical();
					 Double cnfdnc =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getConfident();
					 Double anger =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getAnger();
					 Double sad =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getSadness();
					 Double fear =employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getFear();
					/* int joyCount = employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getJoyCount();
					 int tntCount = employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getTentativeCount();
					 int anaCount = employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getAnalyticalCount();
					 int cnfdncCount = employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getConfidentCount();
					 int angryCount = employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getAngerCount();
					 int sadCount = employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getSadnessCount();
					 int fearCount = employeeRolesPojo.getConsolidatedTone().getToneWithTeam().getAllMailScore().getFearCount();*/
					 Double joyness = (double) Math.round(((joy+tnt+ana+cnfdnc)*100)/(joy+tnt+ana+cnfdnc+anger+sad+fear));
					 Double angry =(double) Math.round(((anger+sad+fear)*100)/(joy+tnt+ana+cnfdnc+anger+sad+fear));
					 EmployeeRelationBO employeeRelationBO = new EmployeeRelationBO();
					 employeeRelationBO.setGood(joyness);
					 employeeRelationBO.setBad(angry);
					 employeePersonalDataDTO2.setRelationWithTeam(employeeRelationBO);
					 
			
					 
					 
					 //Relation with Client
					 Double joyClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getJoy();
					 Double anaClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getAnalytical();
					 Double tntClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getTentative();
					 Double confClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getConfident();
					 Double angerClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getAnger();
					 Double sadClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getSadness();
					 Double fearClnt =employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getFear();
					 
					 
					/* int joyclntCount = employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getJoyCount();
					 int angryclntCount = employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getAngerCount();
					 int tntclntCount = employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getTentativeCount();
					 int cnfclntCount = employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getConfidentCount();
					 int anaclntCount = employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getAnalyticalCount();
					 int fearclntCount = employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getFearCount();
					 int sadclntCount = employeeRolesPojo.getConsolidatedTone().getToneWithClient().getAllMailScore().getSadnessCount();
					 */
					 Double joynessClnt = (double) Math.round(((joyClnt+anaClnt+tntClnt+confClnt)*100)/(joyClnt+anaClnt+tntClnt+confClnt+angerClnt+sadClnt+fearClnt));
					 Double angryClnt =(double) Math.round(((angerClnt+sadClnt+fearClnt)*100)/(joyClnt+anaClnt+tntClnt+confClnt+angerClnt+sadClnt+fearClnt));
					 EmployeeRelationBO employeeRelationBO1 = new EmployeeRelationBO();
					 employeeRelationBO1.setGood(joynessClnt);
					 employeeRelationBO1.setBad(angryClnt);
					 employeePersonalDataDTO2.setRelationWithClient(employeeRelationBO1);
					 
					 
					 
					 
					 
					 
					 // all reportee to particular Employee
					 
					 List<EmployeeRoleBO> list2 = employeeRoleRepository.findByReportToIdAndStatus(employeePersonalDataDTO.getEmployeeId(), "active");
					 for(EmployeeRoleBO employeeRoleBO : list2) {
						 
						 EmployeeBO employeeBO1 = employeeRepository.findByEmployeeId(employeeRoleBO.getEmployeeIdFK());
						 EmployeePersonalDataDTO employeePersonalDataDTO3 = new EmployeePersonalDataDTO();
						 employeePersonalDataDTO3.setEmployeeName(employeeBO1.getEmployeeName());
						 employeePersonalDataDTO3.setDesignation(employeeRoleBO.getDesignation());
						 employeePersonalDataDTO3.setEmployeeId(employeeRoleBO.getEmployeeIdFK());
						 
						 list1.add(employeePersonalDataDTO3);
					 }
					
					
					
					//get data from dailyEmployeeEmailToneRepository
			
			
				
				// Average of All Self Scores 
				
				Aggregation aggregation = Aggregation.newAggregation(
		                
					Aggregation.match(Criteria.where("employeeIdFK").is(employeePersonalDataDTO.getEmployeeId()).and("date").gte("2018-04-17")),
					Aggregation.group("employeeIdFK").last("employeeIdFK").as("employeeId").sum("selfTone.allMailScore.anger").as("angerTotal").sum("selfTone.allMailScore.angerCount")
					.as("angerCount").sum("selfTone.allMailScore.joy").as("joyTotal").sum("selfTone.allMailScore.joyCount")
					.as("joyCount").sum("selfTone.allMailScore.sadness").as("sadnessTotal").sum("selfTone.allMailScore.sadnessCount")
					.as("sadnessCount").sum("selfTone.allMailScore.tentative").as("tentativeTotal").sum("selfTone.allMailScore.tentativeCount")
					.as("tentativeCount").sum("selfTone.allMailScore.analytical").as("analyticalTotal").sum("selfTone.allMailScore.analyticalCount")
					.as("analyticalCount").sum("selfTone.allMailScore.confident").as("confidentTotal").sum("selfTone.allMailScore.confidentCount")
					.as("confidentCount").sum("selfTone.allMailScore.fear").as("fearTotal").sum("selfTone.allMailScore.fearCount")
					.as("fearCount")
					.sum("teamTone.allMailScore.anger").as("angerTeamTotal").sum("teamTone.allMailScore.angerCount")
					.as("angerTeamCount").sum("teamTone.allMailScore.joy").as("joyTeamTotal").sum("teamTone.allMailScore.joyCount")
					.as("joyTeamCount").sum("teamTone.allMailScore.sadness").as("sadnessTeamTotal").sum("teamTone.allMailScore.sadnessCount")
					.as("sadnessTeamCount").sum("teamTone.allMailScore.tentative").as("tentativeTeamTotal").sum("teamTone.allMailScore.tentativeCount")
					.as("tentativeTeamCount").sum("teamTone.allMailScore.analytical").as("analyticalTeamTotal").sum("teamTone.allMailScore.analyticalCount")
					.as("analyticalTeamCount").sum("teamTone.allMailScore.confident").as("confidentTeamTotal").sum("teamTone.allMailScore.confidentCount")
					.as("confidentTeamCount").sum("teamTone.allMailScore.fear").as("fearTeamTotal").sum("teamTone.allMailScore.fearCount")
					.as("fearTeamCount")
						);
				
				AggregationResults<AggregationPojo> groupResults 
				= mongoTemplate.aggregate(aggregation, DailyEmployeeEmailToneBO.class, AggregationPojo.class);
				List<AggregationPojo> result = groupResults.getMappedResults();
				
				
				Double angerFinal = (double)Math.round(result.get(0).getAngerTotal()/result.get(0).getAngerCount());
				Double joyFinal = (double)Math.round(result.get(0).getJoyTotal()/result.get(0).getJoyCount());
				Double sadnessFinal = (double)Math.round(result.get(0).getSadnessTotal()/result.get(0).getSadnessCount());
				Double tentativeFinal = (double)Math.round(result.get(0).getTentativeTotal()/result.get(0).getTentativeCount());
				Double analyticalFinal = (double)Math.round(result.get(0).getAnalyticalTotal()/result.get(0).getAnalyticalCount());
				Double confidentFinal = (double)Math.round(result.get(0).getConfidentTotal()/result.get(0).getConfidentCount());
				Double fearFinal = (double)Math.round(result.get(0).getFearTotal()/result.get(0).getFearCount());
				
				 // percentage of each tone of personal
				 Double joyper = (double) Math.round(((joyFinal)*100)/(joyFinal+angerFinal+sadnessFinal+tentativeFinal+analyticalFinal+confidentFinal+fearFinal));
				 Double tenper = (double) Math.round(((tentativeFinal)*100)/(joyFinal+angerFinal+sadnessFinal+tentativeFinal+analyticalFinal+confidentFinal+fearFinal));
				 Double confper = (double) Math.round(((confidentFinal)*100)/(joyFinal+angerFinal+sadnessFinal+tentativeFinal+analyticalFinal+confidentFinal+fearFinal));
				 Double anaper = (double) Math.round(((analyticalFinal)*100)/(joyFinal+angerFinal+sadnessFinal+tentativeFinal+analyticalFinal+confidentFinal+fearFinal));
				 Double angper = (double) Math.round(((angerFinal)*100)/(joyFinal+angerFinal+sadnessFinal+tentativeFinal+analyticalFinal+confidentFinal+fearFinal));
				 Double sadper = (double) Math.round(((sadnessFinal)*100)/(joyFinal+angerFinal+sadnessFinal+tentativeFinal+analyticalFinal+confidentFinal+fearFinal));
				 Double fearper = (double) Math.round(((fearFinal)*100)/(joyFinal+angerFinal+sadnessFinal+tentativeFinal+analyticalFinal+confidentFinal+fearFinal));
				
				ToneOfMail toneOfMail = new ToneOfMail();
				toneOfMail.setAnger(angper);
				toneOfMail.setJoy(joyper);
				toneOfMail.setSadness(sadper);
				toneOfMail.setTentative(tenper);
				toneOfMail.setAnalytical(anaper);
				toneOfMail.setConfident(confper);
				toneOfMail.setFear(fearper);
				
				employeePersonalDataDTO2.setToneOfPersonalMail(toneOfMail);
				
				// Average of All Team Scores 
				
				Double angerFinal1 = (double)Math.round(result.get(0).getAngerTeamTotal()/result.get(0).getAngerTeamCount());
				Double joyFinal1 = (double)Math.round(result.get(0).getJoyTeamTotal()/result.get(0).getJoyTeamCount());
				Double sadnessFinal1 = (double)Math.round(result.get(0).getSadnessTeamTotal()/result.get(0).getSadnessTeamCount());
				Double tentativeFinal1 = (double)Math.round(result.get(0).getTentativeTeamTotal()/result.get(0).getTentativeTeamCount());
				Double analyticalFinal1 = (double)Math.round(result.get(0).getAnalyticalTeamTotal()/result.get(0).getAnalyticalTeamCount());
				Double confidentFinal1 = (double)Math.round(result.get(0).getConfidentTeamTotal()/result.get(0).getConfidentTeamCount());
				Double fearFinal1 = (double)Math.round(result.get(0).getFearTeamTotal()/result.get(0).getFearTeamCount());
				
				 // percentage of each tone of team
				 Double joyperT = (double) Math.round(((joyFinal1)*100)/(angerFinal1+joyFinal1+sadnessFinal1+tentativeFinal1+analyticalFinal1+confidentFinal1+fearFinal1));
				 Double tenperT = (double) Math.round(((tentativeFinal1)*100)/(angerFinal1+joyFinal1+sadnessFinal1+tentativeFinal1+analyticalFinal1+confidentFinal1+fearFinal1));
				 Double confperT = (double) Math.round(((confidentFinal1)*100)/(angerFinal1+joyFinal1+sadnessFinal1+tentativeFinal1+analyticalFinal1+confidentFinal1+fearFinal1));
				 Double anaperT = (double) Math.round(((analyticalFinal1)*100)/(angerFinal1+joyFinal1+sadnessFinal1+tentativeFinal1+analyticalFinal1+confidentFinal1+fearFinal1));
				 Double angperT = (double) Math.round(((angerFinal1)*100)/(angerFinal1+joyFinal1+sadnessFinal1+tentativeFinal1+analyticalFinal1+confidentFinal1+fearFinal1));
				 Double sadperT = (double) Math.round(((sadnessFinal1)*100)/(angerFinal1+joyFinal1+sadnessFinal1+tentativeFinal1+analyticalFinal1+confidentFinal1+fearFinal1));
				 Double fearperT = (double) Math.round(((fearFinal1)*100)/(angerFinal1+joyFinal1+sadnessFinal1+tentativeFinal1+analyticalFinal1+confidentFinal1+fearFinal1));
				
				
				ToneOfMail toneOfMail1 = new ToneOfMail();
				toneOfMail1.setAnger(angperT);
				toneOfMail1.setJoy(joyperT);
				toneOfMail1.setSadness(sadperT);
				toneOfMail1.setTentative(tenperT);
				toneOfMail1.setAnalytical(anaperT);
				toneOfMail1.setConfident(confperT);
				toneOfMail1.setFear(fearperT);
				
				employeePersonalDataDTO2.setToneOfTeamMail(toneOfMail1);
				
				
					Pageable pageable = new PageRequest(employeePersonalDataDTO.getPageNumber(), 20);
				Page<DailyEmployeeEmailToneBO> dailyEmployeeEmailToneBOs3 =	dailyEmployeeEmailToneRepository.findByEmployeeIdFKAndDate(employeePersonalDataDTO.getEmployeeId(),0,20,"2018-04-16",pageable);	
					List<DailyEmployeeEmailToneBO> dailyEmployeeEmailToneBOs = dailyEmployeeEmailToneBOs3.getContent();
					
					if (employeePersonalDataDTO.getIdentify().equalsIgnoreCase("all")) {
					for(DailyEmployeeEmailToneBO dailyEmployeeEmailToneBO : dailyEmployeeEmailToneBOs) {
						
						// get each email and tone of each employee
						List<EmailPojo> lineItems = dailyEmployeeEmailToneBO.getLineItems();
						

						for (EmailPojo emailPojo : lineItems) {
							Map<String,Double> hashMap = new HashMap<String,Double>(); 
							hashMap.put("anger", emailPojo.getAnger());
							hashMap.put("joy", emailPojo.getJoy());
							hashMap.put("sadness", emailPojo.getSadness());
							hashMap.put("tentative", emailPojo.getTentative());
							hashMap.put("analytical", emailPojo.getAnalytical());
							hashMap.put("confident", emailPojo.getConfident());
							hashMap.put("fear", emailPojo.getFear());
							
							  Map<String, Double> sortedMapDsc =	sortByComparator(hashMap, false);
							  int count =0;
							  for (Entry<String, Double> entry : sortedMapDsc.entrySet())
						        {
						            System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
						            
						            
						         
						            switch (entry.getKey()) {
						            
									case "anger":
										if (count<2) {
											emailPojo.setAnger(entry.getValue());
											count++;
											UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
													.getContext().getAuthentication();
											
//											EmployeePersonalDataDTO employeePersonalDataDTO1 = new EmployeePersonalDataDTO();
											
											String emailId = (String) authObj.getUserSessionInformation().get(
													EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
											
											EmployeeBO employeeData =employeeRepository.findOne(emailId);
											
											ClientBO clients= clientRepository.findByEmployeeIdFK(employeeData.getEmployeeId());
											if(clients != null) {
												if(emailPojo.getType().equalsIgnoreCase("Received")) {
													
													if(clients.getClients() != null) {
														for(FilterPojo client: clients.getClients()) {
															if(client.getEmailId().equalsIgnoreCase(emailPojo.getFromMail())) {
																if(client.getExecutive() != null)
																if(client.getExecutive().equalsIgnoreCase("yes")) {
																	emailPojo.setAdverse("yes");
																}
															}
														}
													}
													
												}else if(emailPojo.getType().equalsIgnoreCase("sent")){
													if(clients.getClients() != null) {
														for(FilterPojo client: clients.getClients()) {
															for(String clientEmail: emailPojo.getToClientEmails()) {
																if(client.getEmailId().equalsIgnoreCase(clientEmail)) {
																	if(client.getExecutive() != null)
																	if(client.getExecutive().equalsIgnoreCase("yes")) {
																		emailPojo.setAdverse("yes");
																	}
																}
															}
														}
													}
												}
											}
										}else {
											emailPojo.setAnger(0.0);
											count++;
										}
										
										break;
										
									case "joy":

										if (count<2) {
											emailPojo.setJoy(entry.getValue());
											count++;
										}else {
											emailPojo.setJoy(0.0);
											count++;
										}
										break;	
									case "sadness":
										if (count<2) {
											emailPojo.setSadness(entry.getValue());
											count++;
										}else {
											emailPojo.setSadness(0.0);
											count++;
										}
										break;
										
									case "tentative":
										if (count<2) {
											emailPojo.setTentative(entry.getValue());
											count++;
										}else {
											emailPojo.setTentative(0.0);
											count++;
										}
										break;
																			
									case "analytical":
										if (count<2) {
											emailPojo.setAnalytical(entry.getValue());
											count++;
										}else {
											emailPojo.setAnalytical(0.0);
											count++;
										}
										break;
										
									case "confident":
										if (count<2) {
											emailPojo.setConfident(entry.getValue());
											count++;
										}else {
											emailPojo.setConfident(0.0);
											count++;
										}
										break;
										
									case "fear":
										if (count<2) {
											emailPojo.setFear(entry.getValue());
											count++;
										}else {
											emailPojo.setFear(0.0);
											count++;
										}
										break;

									default:
										break;
										
									}
						            
						          
						        }
							  
							  System.out.println(sortedMapDsc);

							/*Stream<Map.Entry<String,Double>> sorted =
									hashMap.entrySet().stream()
								       .sorted(Entry.comparingByValue());
							
							System.out.println(sorted.toString());
							Map<String, Double> map = sorted.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
							System.out.println(map);*/
							  
							/*  try {
								  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");
								  Date date = simpleDateFormat.parse(emailPojo.getDate());
								  // new Format
								  SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd-MMM-YYYY");
								  simpleDateFormat2.format(date);
								  emailPojo.setDate(simpleDateFormat2.format(date));
								  }catch (Exception e) {
									// TODO: handle exception
								}*/
							  
							  list.add(emailPojo);
						}
						
						//list.addAll(lineItems);
						
					}
					
					}
					
					
					//for all received mail
					
					if (employeePersonalDataDTO.getIdentify().equalsIgnoreCase("receive")) {
						
					
					
					Aggregation aggregation2 = Aggregation.newAggregation(
							
							Aggregation.match(Criteria.where("employeeIdFK").is(employeePersonalDataDTO.getEmployeeId())),
							Aggregation.unwind("lineItems"),
							Aggregation.match(Criteria.where("lineItems.type").is("Received"))
							
							
							);
					
					List<DailyEmployeeTeamUnwindPojo> results = mongoTemplate.aggregate(aggregation2, DailyEmployeeEmailToneBO.class, DailyEmployeeTeamUnwindPojo.class).getMappedResults();
					
					for(DailyEmployeeTeamUnwindPojo basicDBObject : results) {
						
						EmailPojo emailPojo = basicDBObject.getLineItems();
						
						Map<String,Double> hashMap = new HashMap<String,Double>(); 
						hashMap.put("anger", emailPojo.getAnger());
						hashMap.put("joy", emailPojo.getJoy());
						hashMap.put("sadness", emailPojo.getSadness());
						hashMap.put("tentative", emailPojo.getTentative());
						hashMap.put("analytical", emailPojo.getAnalytical());
						hashMap.put("confident", emailPojo.getConfident());
						hashMap.put("fear", emailPojo.getFear());
						
						  Map<String, Double> sortedMapDsc =	sortByComparator(hashMap, false);
						  int count =0;
						  for (Entry<String, Double> entry : sortedMapDsc.entrySet())
					        {
					            System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
					            
					            
					         
					            switch (entry.getKey()) {
					            
								case "anger":
									if (count<2) {
										emailPojo.setAnger(entry.getValue());
										count++;
										UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
												.getContext().getAuthentication();
										
//										EmployeePersonalDataDTO employeePersonalDataDTO1 = new EmployeePersonalDataDTO();
										
										String emailId = (String) authObj.getUserSessionInformation().get(
												EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
										
										EmployeeBO employeeData =employeeRepository.findOne(emailId);
										
										ClientBO clients= clientRepository.findByEmployeeIdFK(employeeData.getEmployeeId());
										if(clients != null) {
											if(emailPojo.getType().equalsIgnoreCase("Received")) {
												
												if(clients.getClients() != null) {
													for(FilterPojo client: clients.getClients()) {
														if(client.getEmailId().equalsIgnoreCase(emailPojo.getFromMail())) {
															if(client.getExecutive() != null)
															if(client.getExecutive().equalsIgnoreCase("yes")) {
																emailPojo.setAdverse("yes");
															}
														}
													}
												}
												
											}else if(emailPojo.getType().equalsIgnoreCase("sent")){
												if(clients.getClients() != null) {
													for(FilterPojo client: clients.getClients()) {
														for(String clientEmail: emailPojo.getToClientEmails()) {
															if(client.getEmailId().equalsIgnoreCase(clientEmail)) {
																if(client.getExecutive() != null)
																if(client.getExecutive().equalsIgnoreCase("yes")) {
																	emailPojo.setAdverse("yes");
																}
															}
														}
													}
												}
											}
										}
									}else {
										emailPojo.setAnger(0.0);
										count++;
									}
									
									break;
									
								case "joy":

									if (count<2) {
										emailPojo.setJoy(entry.getValue());
										count++;
									}else {
										emailPojo.setJoy(0.0);
										count++;
									}
									break;	
								case "sadness":
									if (count<2) {
										emailPojo.setSadness(entry.getValue());
										count++;
									}else {
										emailPojo.setSadness(0.0);
										count++;
									}
									break;
									
								case "tentative":
									if (count<2) {
										emailPojo.setTentative(entry.getValue());
										count++;
									}else {
										emailPojo.setTentative(0.0);
										count++;
									}
									break;
																		
								case "analytical":
									if (count<2) {
										emailPojo.setAnalytical(entry.getValue());
										count++;
									}else {
										emailPojo.setAnalytical(0.0);
										count++;
									}
									break;
									
								case "confident":
									if (count<2) {
										emailPojo.setConfident(entry.getValue());
										count++;
									}else {
										emailPojo.setConfident(0.0);
										count++;
									}
									break;
									
								case "fear":
									if (count<2) {
										emailPojo.setFear(entry.getValue());
										count++;
									}else {
										emailPojo.setFear(0.0);
										count++;
									}
									break;

								default:
									break;
									
								}
					            
					          
					        }
						  
						  System.out.println(sortedMapDsc);
						  
						  /*try {
							  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");
							  Date date = simpleDateFormat.parse(emailPojo.getDate());
							  // new Format
							  SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd-MMM-YYYY");
							  simpleDateFormat2.format(date);
							  emailPojo.setDate(simpleDateFormat2.format(date));
							  }catch (Exception e) {
								// TODO: handle exception
							}*/

						  list.add(emailPojo);
					
						
						
						//list.add(listRcv);
						
					}
					
					}
					
					//for all sent mail
					
							if (employeePersonalDataDTO.getIdentify().equalsIgnoreCase("sent")) {
								
							
							
							Aggregation aggregation2 = Aggregation.newAggregation(
									
									Aggregation.match(Criteria.where("employeeIdFK").is(employeePersonalDataDTO.getEmployeeId())),
									Aggregation.unwind("lineItems"),
									Aggregation.match(Criteria.where("lineItems.type").is("sent"))
									
									
									);
							
							List<DailyEmployeeTeamUnwindPojo> results = mongoTemplate.aggregate(aggregation2, DailyEmployeeEmailToneBO.class, DailyEmployeeTeamUnwindPojo.class).getMappedResults();
							
							for(DailyEmployeeTeamUnwindPojo basicDBObject : results) {
								
								EmailPojo emailPojo = basicDBObject.getLineItems();
								
								Map<String,Double> hashMap = new HashMap<String,Double>(); 
								hashMap.put("anger", emailPojo.getAnger());
								hashMap.put("joy", emailPojo.getJoy());
								hashMap.put("sadness", emailPojo.getSadness());
								hashMap.put("tentative", emailPojo.getTentative());
								hashMap.put("analytical", emailPojo.getAnalytical());
								hashMap.put("confident", emailPojo.getConfident());
								hashMap.put("fear", emailPojo.getFear());
								
								  Map<String, Double> sortedMapDsc =	sortByComparator(hashMap, false);
								  int count =0;
								  for (Entry<String, Double> entry : sortedMapDsc.entrySet())
							        {
							            System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
							            
							            
							         
							            switch (entry.getKey()) {
							            
										case "anger":
											if (count<2) {
												emailPojo.setAnger(entry.getValue());
												count++;
												UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
														.getContext().getAuthentication();
												
//												EmployeePersonalDataDTO employeePersonalDataDTO1 = new EmployeePersonalDataDTO();
												
												String emailId = (String) authObj.getUserSessionInformation().get(
														EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
												
												EmployeeBO employeeData =employeeRepository.findOne(emailId);
												
												ClientBO clients= clientRepository.findByEmployeeIdFK(employeeData.getEmployeeId());
												if(clients != null) {
													if(emailPojo.getType().equalsIgnoreCase("Received")) {
														
														if(clients.getClients() != null) {
															for(FilterPojo client: clients.getClients()) {
																if(client.getEmailId().equalsIgnoreCase(emailPojo.getFromMail())) {
																	if(client.getExecutive() != null)
																	if(client.getExecutive().equalsIgnoreCase("yes")) {
																		emailPojo.setAdverse("yes");
																	}
																}
															}
														}
														
													}else if(emailPojo.getType().equalsIgnoreCase("sent")){
														if(clients.getClients() != null) {
															for(FilterPojo client: clients.getClients()) {
																for(String clientEmail: emailPojo.getToClientEmails()) {
																	if(client.getEmailId().equalsIgnoreCase(clientEmail)) {
																		if(client.getExecutive() != null)
																		if(client.getExecutive().equalsIgnoreCase("yes")) {
																			emailPojo.setAdverse("yes");
																		}
																	}
																}
															}
														}
													}
												}
											}else {
												emailPojo.setAnger(0.0);
												count++;
											}
											
											break;
											
										case "joy":

											if (count<2) {
												emailPojo.setJoy(entry.getValue());
												count++;
											}else {
												emailPojo.setJoy(0.0);
												count++;
											}
											break;	
										case "sadness":
											if (count<2) {
												emailPojo.setSadness(entry.getValue());
												count++;
											}else {
												emailPojo.setSadness(0.0);
												count++;
											}
											break;
											
										case "tentative":
											if (count<2) {
												emailPojo.setTentative(entry.getValue());
												count++;
											}else {
												emailPojo.setTentative(0.0);
												count++;
											}
											break;
																				
										case "analytical":
											if (count<2) {
												emailPojo.setAnalytical(entry.getValue());
												count++;
											}else {
												emailPojo.setAnalytical(0.0);
												count++;
											}
											break;
											
										case "confident":
											if (count<2) {
												emailPojo.setConfident(entry.getValue());
												count++;
											}else {
												emailPojo.setConfident(0.0);
												count++;
											}
											break;
											
										case "fear":
											if (count<2) {
												emailPojo.setFear(entry.getValue());
												count++;
											}else {
												emailPojo.setFear(0.0);
												count++;
											}
											break;

										default:
											break;
											
										}
							            
							          
							        }
								  
								  System.out.println(sortedMapDsc);

								 /* try {
									  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");
									  Date date = simpleDateFormat.parse(emailPojo.getDate());
									  // new Format
									  SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd-MMM-YYYY");
									  simpleDateFormat2.format(date);
									  emailPojo.setDate(simpleDateFormat2.format(date));
									  }catch (Exception e) {
										// TODO: handle exception
									}*/
								  
								  list.add(emailPojo);
								
								
							//	list.add(listRcv);
								
							}
							
							}
					
					
					employeePersonalDataDTO2.setListEmailAnalyser(list);
					
					employeePersonalDataDTO2.setListOfEmployee(list1);
					
					return employeePersonalDataDTO2;
				}			
				
	
			// search name of employee under team
				
			public List<EmailPojo> searchOnTeamEmployeeName(EmployeePersonalDataDTO empId) {
					
				
					
				List<EmailPojo> filterByCriterias = new ArrayList<EmailPojo>();
					
					//EmployeeRoleBO employeeRoleBO = employeeRoleRepository.findByEmployeeIdFKAndStatus(employeeBO.getEmployeeId(),"active");
					
					ProjectionOperation projectionOperation = Aggregation.project()
							.andExpression("lineItems").as("teamEmails")
							.andExclude("_id");
					
					Aggregation aggregation = null;
					
					
					 aggregation = Aggregation.newAggregation(
							
							
							
						Aggregation.match(Criteria.where("employeeIdFK").is(empId.getEmployeeId())),
						Aggregation.unwind("lineItems"),
						Aggregation.match(Criteria.where("lineItems.toEmployeeNames").regex(empId.getEmployeeName(),"i")),
							//Aggregation.project("employeeHierarchy")
							projectionOperation
							
							
							);
					
					
					
					
					
					
							List<FilterResultPojo> results = mongoTemplate.aggregate(aggregation, DailyEmployeeEmailToneBO.class, FilterResultPojo.class).getMappedResults();
					
					for(FilterResultPojo dbObject : results) {
						
						EmailPojo emailPojo = dbObject.getTeamEmails();
						/* try {
							  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");
							  Date date = simpleDateFormat.parse(emailPojo.getDate());
							  // new Format
							  SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd-MMM-YYYY");
							  simpleDateFormat2.format(date);
							  emailPojo.setDate(simpleDateFormat2.format(date));
							  }catch (Exception e) {
								// TODO: handle exception
							}*/
						
						filterByCriterias.add(emailPojo);
						
						
					}
							
					
					
					return filterByCriterias;
				}		
				
				
		// search on Team mail Heading on TeamDashBoard
		
				// Email Subject typeAhead
				
				
				public List<EmailPojo> typeOnTeamMailHeading(String empData,String empId) {
					
					
					List<EmailPojo> filterByCriterias = new ArrayList<EmailPojo>();
					
					ProjectionOperation projectionOperation = Aggregation.project()
							.andExpression("lineItems").as("teamEmails")
							.andExclude("_id");
					
					
					
					Aggregation aggregation = null;
					
					if (empData!=null) {
						
						
					
					 aggregation = Aggregation.newAggregation(
							
							 Aggregation.match(Criteria.where("employeeIdFK").is(empId)),
								Aggregation.unwind("lineItems"),
								Aggregation.match(Criteria.where("lineItems.subject").regex(empData,"i")),
								projectionOperation
								
							
							
							);
					}
					

					List<FilterResultPojo> results = mongoTemplate.aggregate(aggregation, DailyEmployeeEmailToneBO.class, FilterResultPojo.class).getMappedResults();
					
					for(FilterResultPojo basicDBObject : results) {
						EmailPojo emailPojo = new EmailPojo();
						emailPojo.setSubject(basicDBObject.getTeamEmails().getSubject());
						emailPojo.setType(basicDBObject.getTeamEmails().getType());
						
						//EmailPojo listRcv = basicDBObject.getClientEmailItems();
						
						filterByCriterias.add(emailPojo);
						
					}
					
					return filterByCriterias;
				}

				// search on basis of emailSubject
				
				
				public List<EmailPojo> searchOnTeamMailHeading(EmployeePersonalDataDTO empData) {
					
					
					List<EmailPojo> filterByCriterias = new ArrayList<EmailPojo>();
					

					ProjectionOperation projectionOperation = Aggregation.project()
							.andExpression("lineItems").as("teamEmails")
							.andExclude("_id");
					
					Aggregation aggregation = null;
					
					if (empData.getEmailSubject()!=null) {
						
						
					
					 aggregation = Aggregation.newAggregation(
							
							 Aggregation.match(Criteria.where("employeeIdFK").is(empData.getEmployeeId())),
								Aggregation.unwind("lineItems"),
								//Aggregation.match(Criteria.where("clientEmailItems.subject").regex(empData.getEmailSubject(),"i"))
								Aggregation.match(Criteria.where("lineItems.subject").is(empData.getEmailSubject())),
							 projectionOperation
							
							);
					}
					

					List<FilterResultPojo> results = mongoTemplate.aggregate(aggregation, DailyEmployeeEmailToneBO.class, FilterResultPojo.class).getMappedResults();
					
					for(FilterResultPojo basicDBObject : results) {
						
						EmailPojo emailPojo = basicDBObject.getTeamEmails();
						
						 /*try {
							  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");
							  Date date = simpleDateFormat.parse(emailPojo.getDate());
							  // new Format
							  SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd-MMM-YYYY");
							  simpleDateFormat2.format(date);
							  emailPojo.setDate(simpleDateFormat2.format(date));
							  }catch (Exception e) {
								// TODO: handle exception
							}*/
						
						filterByCriterias.add(emailPojo);
						
					}
					
					return filterByCriterias;
				}
				
				
				
				
				// team Score Screen search on Sentiment tone
				
				public List<EmailPojo> searchMailOnTone(ClientDataDTO empData) {
					
					
					List<EmailPojo> filterByCriterias = new ArrayList<EmailPojo>();
					/*
					 UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
								.getContext().getAuthentication();
						
						String emailId = (String) authObj.getUserSessionInformation().get(
								EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
						
						EmployeeBO employeeBO = employeeRepository.findOne(emailId);*/
					
						//List<MatchOperation> listOperation = new ArrayList<MatchOperation>();
						List<Criteria> criterias = new ArrayList<Criteria>();
						MatchOperation matchOperation = null ;	
						Criteria criteria = null;
						if (empData.getSearchCriteria().contains("anger")) {
							criteria =	Criteria.where("lineItems.anger").gt(0);
							
							criterias.add(0,criteria);
							
						}
						if (empData.getSearchCriteria().contains("joy")) {
							
							if (criterias.size()>0) {
								criteria = criterias.get(0)
										.and("lineItems.joy").gt(0);
								criterias.add(0, criteria);
							}else {
								criteria =	Criteria.where("lineItems.joy").gt(0);
								criterias.add(0, criteria);
							}
						}
						if (empData.getSearchCriteria().contains("sadness")) {
							
							if (criterias.size()>0) {
								criteria = criterias.get(0)
										.and("lineItems.sadness").gt(0);
								criterias.add(0, criteria);
							}else {
								criteria =	Criteria.where("lineItems.sadness").gt(0);
								criterias.add(0, criteria);
							}
						}
						if (empData.getSearchCriteria().contains("fear")) {
							
							if (criterias.size()>0) {
								criteria = criterias.get(0)
										.and("lineItems.fear").gt(0);
								criterias.add(0, criteria);
							}else {
								criteria =	Criteria.where("lineItems.fear").gt(0);
								criterias.add(0, criteria);
							}
													
						}
						if (empData.getSearchCriteria().contains("tentative")) {
							
							if (criterias.size()>0) {
								criteria = criterias.get(0)
										.and("lineItems.tentative").gt(0);
								criterias.add(0, criteria);
							}else {
								criteria =	Criteria.where("lineItems.tentative").gt(0);
								criterias.add(0, criteria);
							}
						}
						if (empData.getSearchCriteria().contains("confident")) {
							
							if (criterias.size()>0) {
								criteria = criterias.get(0)
										.and("lineItems.confident").gt(0);
								criterias.add(0, criteria);
							}else {
								criteria =	Criteria.where("lineItems.confident").gt(0);
								criterias.add(0, criteria);
							}
						}
						if (empData.getSearchCriteria().contains("analytical")) {
							
							if (criterias.size()>0) {
								criteria = criterias.get(0)
										.and("lineItems.analytical").gt(0);
								criterias.add(0, criteria);
							}else {
								criteria =	Criteria.where("lineItems.analytical").gt(0);
								criterias.add(0, criteria);
							}
						}
						
						if (criterias.size()>0) {
							matchOperation = Aggregation.match(criterias.get(0));
						}
					
						ProjectionOperation projectionOperation = Aggregation.project()
								.andExpression("lineItems").as("teamEmails")
								.andExclude("_id");
					
					Aggregation aggregation2 = Aggregation.newAggregation(
							
						Aggregation.match(Criteria.where("employeeIdFK").is(empData.getEmployeeId())),
							Aggregation.unwind("lineItems"),
							matchOperation,
							projectionOperation
							
							
							);
					
							List<FilterResultPojo> results = mongoTemplate.aggregate(aggregation2, DailyEmployeeEmailToneBO.class, FilterResultPojo.class).getMappedResults();
					
					
							for(FilterResultPojo basicDBObject : results) {
						
								EmailPojo emailPojo = basicDBObject.getTeamEmails();
						
						/* try {
							  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");
							  Date date = simpleDateFormat.parse(emailPojo.getDate());
							  // new Format
							  SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd-MMM-YYYY");
							  simpleDateFormat2.format(date);
							  emailPojo.setDate(simpleDateFormat2.format(date));
							  }catch (Exception e) {
								// TODO: handle exception
							}*/
						
						filterByCriterias.add(emailPojo);
						
					}
					
					return filterByCriterias;
				}		
				
				
		// sorting on Team DashBoard Mail Screen
				
				

				public List<EmailPojo> sortMailOnTeamScreen(ClientDataDTO empData) {
					
					
					List<EmailPojo> filterByCriterias = new ArrayList<EmailPojo>();
					
					/* UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
								.getContext().getAuthentication();
						
						String emailId = (String) authObj.getUserSessionInformation().get(
								EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);
						
						EmployeeBO employeeBO = employeeRepository.findOne(emailId);*/
					
						List<SortOperation> criterias = new ArrayList<SortOperation>();
						SortOperation sortOperation = null;
						if (empData.getSearchCriteria().contains("anger")) {
							sortOperation =	Aggregation.sort(Sort.Direction.DESC,"lineItems.anger");
							
							criterias.add(0,sortOperation);
							
						}
						if (empData.getSearchCriteria().contains("joy")) {
							
							if (criterias.size()>0) {
								sortOperation = criterias.get(0)
										.and(Sort.Direction.DESC,"lineItems.joy");
								criterias.add(0, sortOperation);
							}else {
								sortOperation =	Aggregation.sort(Sort.Direction.DESC,"lineItems.joy");
								criterias.add(0, sortOperation);
							}
						}
						if (empData.getSearchCriteria().contains("sadness")) {
							
							if (criterias.size()>0) {
								sortOperation = criterias.get(0)
										.and(Sort.Direction.DESC,"lineItems.sadness");

								criterias.add(0, sortOperation);
							}else {
								sortOperation =Aggregation.sort(Sort.Direction.DESC,"lineItems.sadness");
								criterias.add(0, sortOperation);
							}
						}
						if (empData.getSearchCriteria().contains("fear")) {
							
							if (criterias.size()>0) {
								sortOperation = criterias.get(0)
										.and(Sort.Direction.DESC,"lineItems.fear");

								criterias.add(0, sortOperation);
							}else {
								sortOperation =	Aggregation.sort(Sort.Direction.DESC,"lineItems.fear");
								criterias.add(0, sortOperation);
							}
													
						}
						if (empData.getSearchCriteria().contains("tentative")) {
							
							if (criterias.size()>0) {
								sortOperation = criterias.get(0)
										.and(Sort.Direction.DESC,"lineItems.tentative");

								criterias.add(0, sortOperation);
							}else {
								sortOperation = Aggregation.sort(Sort.Direction.DESC,"lineItems.tentative");
								criterias.add(0, sortOperation);
							}
						}
						if (empData.getSearchCriteria().contains("confident")) {
							
							if (criterias.size()>0) {
								sortOperation = criterias.get(0)
										.and(Sort.Direction.DESC,"lineItems.confident");

								criterias.add(0, sortOperation);
							}else {
								sortOperation =	Aggregation.sort(Sort.Direction.DESC,"lineItems.confident");
								criterias.add(0, sortOperation);
							}
						}
						if (empData.getSearchCriteria().contains("analytical")) {
							
							if (criterias.size()>0) {
								sortOperation = criterias.get(0)
										.and(Sort.Direction.DESC,"lineItems.analytical");

								criterias.add(0, sortOperation);
							}else {
								sortOperation =	Aggregation.sort(Sort.Direction.DESC,"lineItems.analytical");
								criterias.add(0, sortOperation);
							}
						}
						
						if (empData.getSearchCriteria().contains("subjectalphabetically")) {
							
							if (criterias.size()>0) {
								sortOperation = criterias.get(0)
										.and(Sort.Direction.DESC,"lineItems.subject");

								criterias.add(0, sortOperation);
							}else {
								sortOperation =	Aggregation.sort(Sort.Direction.DESC,"lineItems.subject");
								criterias.add(0, sortOperation);
							}
						}
						
						if (criterias.size()>0) {
							sortOperation = criterias.get(0);
						}else {
							sortOperation = Aggregation.sort(Sort.Direction.ASC,"lineItems.subject");
						}
					
						ProjectionOperation projectionOperation = Aggregation.project()
								.andExpression("lineItems").as("teamEmails")
								.andExclude("_id");
					
					Aggregation aggregation2 = Aggregation.newAggregation(
							
						Aggregation.match(Criteria.where("employeeIdFK").is(empData.getEmployeeId())),
							Aggregation.unwind("lineItems"),
							sortOperation,
							projectionOperation
							
							
							);
					
							List<FilterResultPojo> results = mongoTemplate.aggregate(aggregation2, DailyEmployeeEmailToneBO.class, FilterResultPojo.class).getMappedResults();
					
					
							for(FilterResultPojo basicDBObject : results) {
								
								EmailPojo emailPojo = basicDBObject.getTeamEmails();
						
						 /*try {
							  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");
							  Date date = simpleDateFormat.parse(emailPojo.getDate());
							  // new Format
							  SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd-MMM-YYYY");
							  simpleDateFormat2.format(date);
							  emailPojo.setDate(simpleDateFormat2.format(date));
							  }catch (Exception e) {
								// TODO: handle exception
							}*/
						
						filterByCriterias.add(emailPojo);
						
					}
					
					return filterByCriterias;
				}
				
				
				
				
				
				// search mail on basis of date in TeamDashBoard
				
				public List<EmailPojo> filterTeamMailOnDate(EmployeePersonalDataDTO empData) {
					
					
					List<EmailPojo> filterByCriterias = new ArrayList<EmailPojo>();
					
					/* UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
								.getContext().getAuthentication();
						
						String emailId = (String) authObj.getUserSessionInformation().get(
								EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);*/
						
						EmployeeBO employeeBO = employeeRepository.findOne(empData.getEmailId());
						
						ProjectionOperation projectionOperation = Aggregation.project()
								.andExpression("lineItems").as("teamEmails")
								.andExclude("_id");
					
					
					Aggregation aggregation2 = Aggregation.newAggregation(
							
							Aggregation.match(Criteria.where("employeeIdFK").is(employeeBO.getEmployeeId())),
							Aggregation.unwind("lineItems"),
							Aggregation.match(Criteria.where("lineItems.date").gte(empData.getStartDate()).lte(empData.getEndDate())),
							projectionOperation
							
							
							);
					
					List<FilterResultPojo> results = mongoTemplate.aggregate(aggregation2, DailyEmployeeEmailToneBO.class, FilterResultPojo.class).getMappedResults();
					
					
					for(FilterResultPojo basicDBObject : results) {
						
						EmailPojo emailPojo = basicDBObject.getTeamEmails();
						/* try {
							  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");
							  Date date = simpleDateFormat.parse(emailPojo.getDate());
							  // new Format
							  SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd-MMM-YYYY");
							  simpleDateFormat2.format(date);
							  emailPojo.setDate(simpleDateFormat2.format(date));
							  }catch (Exception e) {
								// TODO: handle exception
							}*/
						
						filterByCriterias.add(emailPojo);
						
					}
					
					return filterByCriterias;
				}
				
				
				
				// search mail on basis of time in TeamDashBoard
				
						public List<EmailPojo> filterTeamMailOnTime(EmployeePersonalDataDTO empData) {
							
							
							List<EmailPojo> filterByCriterias = new ArrayList<EmailPojo>();
							
							/* UserAuthCredentials authObj = (UserAuthCredentials) SecurityContextHolder
										.getContext().getAuthentication();
								
								String emailId = (String) authObj.getUserSessionInformation().get(
										EnumeratedTypes.UserSessionData.PRIMARY_PROFILE_ID);*/
								
								EmployeeBO employeeBO = employeeRepository.findOne(empData.getEmailId());
								
							SortOperation	sortOperation =	Aggregation.sort(Sort.Direction.ASC,"lineItems.time");
							
							ProjectionOperation projectionOperation = Aggregation.project()
									.andExpression("lineItems").as("teamEmails")
									.andExclude("_id");
							
							
							Aggregation aggregation2 = Aggregation.newAggregation(
									
									Aggregation.match(Criteria.where("employeeIdFK").is(employeeBO.getEmployeeId())),
									Aggregation.unwind("lineItems"),
									Aggregation.match(Criteria.where("lineItems.time").gte(empData.getStartTime()).lte(empData.getEndTime())),
									sortOperation,
									projectionOperation
									
									
									);
							
							List<FilterResultPojo> results = mongoTemplate.aggregate(aggregation2, DailyEmployeeEmailToneBO.class, FilterResultPojo.class).getMappedResults();
							
							
							for(FilterResultPojo basicDBObject : results) {
								
								EmailPojo listRcv = basicDBObject.getTeamEmails();
								
								filterByCriterias.add(listRcv);
								
							}
							
							return filterByCriterias;
						}
				
				
			//employeeDashBoard Data download
				
				/*
				 * download employee list   getClientDashBoard1
				 */
				
				public String getDownloadPath(ClientDataDTO clientDataDTO)throws Exception
				{
					String filePath = null;
					
					EmployeePersonalDataDTO usrData = getListOfEmployeeHierchy1(clientDataDTO);
					filePath = exportToExcel.generateEmployeeList(usrData);
					return filePath;
				}
				
				/*
				 * download client data
				 * */
				
				
				public String getDownloadClientPath(ClientDataDTO clientDataDTO)throws Exception
				{
					String filePath = null;
					
					EmployeePersonalDataDTO usrData = getClientDashBoard(clientDataDTO);
					filePath = exportToExcel.generateClientList(usrData);
					return filePath;
				}
				
				
				
				
				/*
				 * download personal data
				 * */
				
				
				public String getDownloadPersonalPath(EmployeePersonalDataDTO employeePersonalDataDTO)throws Exception
				{
					String filePath = null;
					
					EmployeePersonalDataDTO usrData = getPersonalPerMailScore(employeePersonalDataDTO);
					filePath = exportToExcel.generatePersonalDataToExcel(usrData);
					return filePath;
				}
				
				/*
				 * download Team data
				 * */
				
				
				public String getDownloadTeamPath(EmployeePersonalDataDTO employeePersonalDataDTO)throws Exception
				{
					String filePath = null;
					
					EmployeePersonalDataDTO usrData = getEmployeeTeamDashBoard(employeePersonalDataDTO);
					filePath = exportToExcel.generateTeamDataToExcel(usrData);
					return filePath;
				}
				
				public String getDownloadPersonalTeamPath(EmployeePersonalDataDTO employeePersonalDataDTO)throws Exception
				{
					String filePath = null;
					
					EmployeePersonalDataDTO usrData = getEmployeeTeamDashBoard(employeePersonalDataDTO);
					EmployeePersonalDataDTO usrData2 = getPersonalPerMailScore(employeePersonalDataDTO);
					filePath = exportToExcel.generatePersonalTeamDataToExcel(usrData,usrData2);
					return filePath;
				}
				
				// changes by aashish



public String saveemp(ArrayList<EmployeePersonalDataDTO> employeeList) {
					
					if(employeeList.isEmpty())
					{
						String result="No data is to updated";
						return result;
					} 
					
					EmployeeBO employeeBO = null;
					EmployeeRoleBO employeeRoleBO = null;
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					String datestring = df.format(new Date());
					
					List <EmployeeBO>employeeBOlist = new ArrayList<EmployeeBO>();
					List <EmployeeRoleBO>employeeRoleBOlist = new ArrayList<EmployeeRoleBO>();
					
					
					List<FilterByCriteria> employeeHierarchy = new ArrayList<FilterByCriteria>();
					List<EmployeeBO> DuplicateemployeeBOlist = new ArrayList<EmployeeBO> ();
					List<EmployeeRoleBO> DuplicateemployeeRoleBOlist = new ArrayList<EmployeeRoleBO> ();
				//	String password= null;
					String token=null;
					Date finaltokenvalidity = null;
					String finaltokenvalidity2= null;
					for (EmployeePersonalDataDTO employeePersonalDataDTO : employeeList) {
				//		password= RandomPasswordGenerator.generateRandomPassword();  // In order to generate Encrypted  password
						token= RandomPasswordGenerator.generateRandomToken();  // In order to generate Random token
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
						//final long hoursInMillis = 60L * 60L * 1000L;
					/*	finaltokenvalidity = new Date(dt.getTime() + 
						                        (tokenvalidiy* hoursInMillis)); */
					finaltokenvalidity = RandomPasswordGenerator.addDays(dt, Integer.parseInt(Validity));
						finaltokenvalidity2=sm.format(finaltokenvalidity);
						
					
						employeeBO = new EmployeeBO();
						employeeRoleBO = new EmployeeRoleBO();
						
						employeeBO.setEmployeeName(employeePersonalDataDTO.getEmployeeName());
//						employeeBO.setId("123");
						employeeBO.setEmployeeId(employeePersonalDataDTO.getEmployeeId());
						employeeBO.setEmailId(employeePersonalDataDTO.getEmailId());
						employeeBO.setPassword("test1234");
						employeeBO.setFirstlogin(false);  // first login is created  sending mail.
						employeeBO.setResetToken(token);
						employeeBO.setTokenValidity(finaltokenvalidity2);
						String reporttoid = "";
						try {
						reporttoid = employeePersonalDataDTO.getReportTo();
						}catch(Exception e) {
							e.printStackTrace();
						}
						if(reporttoid != null && !reporttoid.equals("")) {
						employeeRoleBO.setReportToId(reporttoid);
						}else {
							employeeRoleBO.setReportToId(null);
						}
						employeeRoleBO.setEmployeeIdFK(employeePersonalDataDTO.getEmployeeId());
						employeeRoleBO.setEmployeeHierarchy(employeeHierarchy);
						employeeRoleBO.setFromDate(datestring);
						employeeRoleBO.setStatus("active");
						employeeRoleBO.setDepartment("");
						ConsolidatedPojo emptyPojo = new ConsolidatedPojo();
						EmailToneResultPojo emptyEmailPojo =  new EmailToneResultPojo();
						ToneOfMail emptyTone = new ToneOfMail();
						emptyTone.setAnalytical(0.0d);
						emptyTone.setJoy(0.0d);
						emptyTone.setAnger(0.0d);
						emptyTone.setTentative(0.0d);
						emptyTone.setSadness(0.0d);
						emptyTone.setFear(0.0d);
						emptyTone.setConfident(0.0d);
						emptyTone.setAnalyticalCount(0);
						emptyTone.setJoyCount(0);
						emptyTone.setAngerCount(0);
						emptyTone.setTentativeCount(0);
						emptyTone.setSadnessCount(0);
						emptyTone.setFearCount(0);
						emptyTone.setConfidentCount(0);
						emptyEmailPojo.setAllMailScore(emptyTone);
						emptyEmailPojo.setReceiveMailScore(emptyTone);
						emptyEmailPojo.setSentMailScore(emptyTone);
						emptyEmailPojo.setTotalMail(0l);
						emptyEmailPojo.setTotalMailRecevied(0l);
						emptyEmailPojo.setTotalMailSent(0l);
						emptyPojo.setTotalMail(0l);
						emptyPojo.setTotalMailSent(0l);
						emptyPojo.setTotalMailRecevied(0l);
						emptyPojo.setToneWithClient(emptyEmailPojo);
						emptyPojo.setToneWithOtherEmployee(emptyEmailPojo);
						emptyPojo.setToneWithTeam(emptyEmailPojo);
						employeeRoleBO.setConsolidatedTone(emptyPojo);
						employeeRoleBO.setArea("");
						employeeRoleBO.setChannel("");
						employeeRoleBO.setDesignation("");
						employeeRoleBO.setHorizontal("");
						employeeRoleBO.setTeamSize(0);
						employeeRoleBO.setTerritory("");
						employeeRoleBO.setVertical("");
						employeeRoleBO.setSubArea("");
						employeeRoleBO.setSubChannel("");
						employeeRoleBO.setSubRegion("");
						employeeRoleBO.setSubTerritory("");
//						employeeRoleBO.
						//fix
						//BasicDBObject whereQuery = new BasicDBObject();
						//whereQuery.put("emailId",employeePersonalDataDTO.getEmailId());
						//whereQuery.put("employeeName",employeePersonalDataDTO.getEmployeeName());
						//whereQuery.put("employeeId",employeePersonalDataDTO.getEmployeeId());
						//mongoTemplate.save(whereQuery,"Employee");
				
						//end of fix
						employeeBOlist.add(employeeBO);
						employeeRoleBOlist.add(employeeRoleBO);
						
						List<EmployeeBO> employeeBOlisttotal=employeeRepository.findAll();
						for (EmployeeBO a1 :employeeBOlisttotal) {  // already existed record
							for (EmployeeBO a2 :employeeBOlist) {           // list to be inserted
								if(a1.getEmailId().equals(a2.getEmailId()))
									DuplicateemployeeBOlist.add(a2);
							}
						}
						
						for (EmployeeBO a1 :employeeBOlisttotal) {  // already existed record
							for (EmployeeRoleBO a2 :employeeRoleBOlist) {           // list to be inserted
								if(a1.getEmployeeId().equals(a2.getEmployeeIdFK()))
									DuplicateemployeeRoleBOlist.add(a2);
							}
						}
						
					}
					employeeBOlist.removeAll(DuplicateemployeeBOlist);
					employeeRoleBOlist.removeAll(DuplicateemployeeRoleBOlist);
					
					//employeeRepository.save(employeeBOlist);
					for (EmployeeBO a1 :employeeBOlist) {           // list to be inserted
						AddressPojo emptyAddress = new AddressPojo();
						emptyAddress.setCity("");
						emptyAddress.setFirstLineOfAddress("");
						emptyAddress.setSecondLineOfAddress("");
						emptyAddress.setState("");
						emptyAddress.setZipcode("");
						BasicDBObject whereQuery = new BasicDBObject();
						whereQuery.put("emailId",a1.getEmailId());
						whereQuery.put("employeeName",a1.getEmployeeName());
						whereQuery.put("employeeId",a1.getEmployeeId());
						whereQuery.put("password", "test1234");
						whereQuery.put("employeeDetails", emptyAddress);
						whereQuery.put("firstlogin", false);
						whereQuery.put("resetToken", token);
						whereQuery.put("tokenValidity", finaltokenvalidity2);
						mongoTemplate.save(whereQuery,"Employee");
						sendMail.prepareAndSendforPasswordChange(a1.getEmployeeName(),a1.getEmailId(),token);
					}
					
					for(EmployeeRoleBO a2: employeeRoleBOlist) {
					BSONObject employeeRoleBsonObj = BasicDBObjectBuilder.start()
												.add("designation", "")
												.add("department", "")
												.add("teamName", "")
												.add("employeeIdFK", a2.getEmployeeIdFK())
												.add("horizontal", "")
												.add("vertical", "")
												.add("region", "")
												.add("subRegion", "")
												.add("area", "")
												.add("subArea","")
												.add("channel","")
												.add("subChannel", "")
												.add("territory", "")
												.add("subTerritory", "")
												.add("fromDate", a2.getFromDate())
												.add("toDate", "")
												.add("status", "active")
												.add("teamSize", 0)
												.add("consolidatedTone", a2.getConsolidatedTone()).get();
					if(a2.getReportToId() != null && !a2.getReportToId().isEmpty() && !a2.getReportToId().equals("") ) {
						employeeRoleBsonObj.put("reportToId", a2.getReportToId());
					}else {
						employeeRoleBsonObj.put("reportToId", null);
					}
					employeeRoleBsonObj.put("employeeHierarchy", a2.getEmployeeHierarchy());
					mongoTemplate.save(employeeRoleBsonObj,"EmployeeRole");
					}
//					employeeRoleRepository.save(employeeRoleBOlist);
					Boolean response  = updateemployeeHierarchy(employeeBOlist,datestring);
					
					String result= "Employee data upload is unsuccessful. Please try again";
					
					if(employeeBOlist.size() != 0 && DuplicateemployeeBOlist.size() != 0) {
						result="Records are saved successfully - "+employeeBOlist.size()+"  Duplicate records - "+DuplicateemployeeBOlist.size();
					}else if(DuplicateemployeeBOlist.size()  == employeeList.size()) {
						result = "Employee data upload is unsuccessful. Duplicate records "+DuplicateemployeeBOlist.size()+" Please try again";
					}else if(DuplicateemployeeBOlist.size() == 0) {
						result = "Employee data upload is successful";
					}
					return result;

				}
				
				
				public boolean updateemployeeHierarchy(List<EmployeeBO> employeeBOlist,String datestring) {
					for (EmployeeBO employeeBO : employeeBOlist) {
						FilterByCriteria filterByCriteria = new FilterByCriteria();
						filterByCriteria.setEmployeeName(employeeBO.getEmployeeName());
						filterByCriteria.setEmployeeId(employeeBO.getEmployeeId());
						filterByCriteria.setEmailId(employeeBO.getEmailId());
						EmployeeRoleBO employeeRoleBO = null;
						try {
						employeeRoleBO = employeeRoleRepository.findByEmployeeIdFKAndStatus(employeeBO.getEmployeeId(),
								"active");
						}catch(Exception e) {
							e.printStackTrace();
						}
						
						EmployeeRoleBO employeeRoleBOnew = null;
						try {
							String reportToId=employeeRoleBO.getReportToId();
						employeeRoleBOnew=employeeRoleRepository.findByEmployeeIdFKAndStatus(reportToId, "active");
						}catch(Exception e) {
							e.printStackTrace();
//							return false;
						}
						if(employeeRoleBOnew == null) {
							continue;
						}
						List<FilterByCriteria> employeeHierarchylist= employeeRoleBOnew.getEmployeeHierarchy();
						employeeHierarchylist.add(filterByCriteria);
						employeeRoleBOnew.setEmployeeHierarchy(employeeHierarchylist);
//						employeeRoleRepository.save(employeeRoleBOnew);
//						mongoTemplate.save(employeeRoleBOnew, "EmployeeRole");
						BSONObject employeeRoleBsonObj = BasicDBObjectBuilder.start()
								.add("designation", "")
								.add("department", "")
								.add("teamName", "")
								.add("employeeIdFK", employeeRoleBOnew.getEmployeeIdFK())
								.add("horizontal", "")
								.add("vertical", "")
								.add("region", "")
								.add("subRegion", "")
								.add("area", "")
								.add("subArea","")
								.add("channel","")
								.add("subChannel", "")
								.add("territory", "")
								.add("subTerritory", "")
								.add("fromDate", employeeRoleBOnew.getFromDate())
								.add("toDate", "")
								.add("status", "active")
								.add("teamSize", 0)
								.add("consolidatedTone", employeeRoleBOnew.getConsolidatedTone())
								.add("employeeHierarchy", employeeRoleBOnew.getEmployeeHierarchy()).get();
						
						if(employeeRoleBOnew.getReportToId() != null && !employeeRoleBOnew.getReportToId().isEmpty() ) {
							employeeRoleBsonObj.put("reportToId", employeeRoleBOnew.getReportToId());
						}else {
							employeeRoleBsonObj.put("reportToId", null);
						}
						Query q = new Query();
						q.addCriteria(Criteria.where("employeeIdFK").is(employeeRoleBOnew.getEmployeeIdFK()));
						mongoTemplate.findAndRemove(q, EmployeeRoleBO.class);
						mongoTemplate.save(employeeRoleBsonObj,"EmployeeRole");
						
						
					}
					
					
					
					return true;
					
				}
				
				
				
				

			// end of changes by aashish
				
	    			
}
