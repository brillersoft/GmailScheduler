package com.app.mongo.repositories;

import java.util.List;

import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import com.app.bo.TeamClientInteractionBO;

@Repository
public interface TeamClientInteractionRepository extends MongoRepository<TeamClientInteractionBO, String>{
	
	/*public List<TeamClientInteractionBO> findByOrganisationFK(String id);
	//public List<DailyClientEmailToneBO>	find({lineItems: {$elemMatch: {employeeFK:empId}}});
	
	@Query("{employeeFK: { $regex: ?0 } })")
	public List<DailyClientEmailToneBO> findByLineItems(String employeeFK);
	
	@Query("{ 'name' : ?0 }")
	public TeamClientInteractionBO findByName(String name);
	
	
	public List<TeamClientInteractionBO> findAllBy(TextCriteria employeeFK);*/
	
	/*@Query(value="{'?0' : { $exists : true },'employeeIdFK' : ?1}",fields= "{'?2' : 1, _id : 0}")
	public TeamClientInteractionBO findByEmployeeIdFK(String id,String empId,String id1);*/
	
	@Query(value="{'?0' : { $exists : true },'employeeIdFK' : ?1, 'date' : {'$gte' : ?3}}",fields= "{'?2' : 1, _id : 0}")
	public List<TeamClientInteractionBO> findByEmployeeIdFKAndDate(String id,String empId,String id1,String date);
	
	//public List<DailyClientEmailToneBO> findByOrganisationFKAndEmployeeFK(String orgFk,String empFK);

}
