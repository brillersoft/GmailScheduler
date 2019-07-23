package com.app.mongo.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.app.bo.DailyEmployeeEmailToneBO;
import com.app.bo.EmployeeBO;

@Repository
public interface DailyEmployeeEmailToneRepository extends MongoRepository<DailyEmployeeEmailToneBO, String>,PagingAndSortingRepository<DailyEmployeeEmailToneBO, String>{
		
		 @Query("{'date' : { '$gte' : ?0 }}")
		public List<DailyEmployeeEmailToneBO> findByDate(String date);
		 
		 @Query(value="{'date' : { '$gte' : ?1 },'employeeIdFK': ?0, }",fields ="{'clientEmailItems' : {$elemMatch : {'type' : 'sent'}}}")
			public Page<DailyEmployeeEmailToneBO> findByEmployeeIdFKAndDate(String employeeIdFK,String date,String type,Pageable page);
	 
	 
	 	@Query(value="{'date' : { '$gte' : ?0 },'employeeIdFK': ?1 }",fields ="{'lineItems': 1}")
		public Page<DailyEmployeeEmailToneBO> findByDateAndEmployeeIdFK(String date,String employeeIdFK,Pageable page);
	 	
	 	@Query(value="{'date' : { '$gte' : ?1 },'employeeIdFK': ?0 }",fields ="{'clientEmailItems': 1}")
		public Page<DailyEmployeeEmailToneBO> findByEmployeeIdFKAndDate(String employeeIdFK,String date,Pageable page);
	 
		@Query("{'date' : { '$gte' : ?0, '$lt' : ?2 },'employeeIdFK': ?1 }")
		public List<DailyEmployeeEmailToneBO> findByDateAndEmployeeIdFK(String fdate,String employeeIdFK,String tdate);
	 

	 	@Query("{'date' : { '$gte' : ?0 },'employeeIdFK': ?1 }")
		public List<DailyEmployeeEmailToneBO> findByDateAndEmployeeIdFK(String date,String employeeIdFK);
	 	
	 	@Query(value="{'employeeIdFK' : ?0, 'date' : {'$gte' : ?3 }}", fields = "{lineItems: {$slice: [?1, ?2]}}")
	 	public Page<DailyEmployeeEmailToneBO> findByEmployeeIdFKAndDate(String employeeIdFK,int skip, int limit,String date,Pageable pageable);
	 	
	 	@Query(value="{'employeeIdFK' : ?0, 'date' : {'$gte' : ?3 }}", fields = "{clientEmailItems: {$slice: [?1, ?2]}}")
	 	public Page<DailyEmployeeEmailToneBO> findByDateAndEmployeeIdFK(String employeeIdFK,int skip, int limit,String date,Pageable pageable);

		public List<DailyEmployeeEmailToneBO> findByEmployeeIdFK(String EmployeeIdFK);
	
		@Query("{'clientEmailItems.fromEmailDomain' : ?0}")
		public List<DailyEmployeeEmailToneBO> findByFromEmailDomain(String fromEmailDomain);
		
		@Query("{'clientEmailItems.fromMail' : {$regex : ?0, $options: 'i'},'employeeIdFK' : ?1}")
		public List<DailyEmployeeEmailToneBO> findByFromMail(String fromMail, String employeeIdFK);
		
//		@Query("{'clientEmailItems.fromMail' : {$regex : ?0, $options: 'i'},'employeeIdFK' : ?1,'clientEmailItems.type : ?2'}")
//		public List<DailyEmployeeEmailToneBO> findByFromMailAndEmployeeFKAndType(String fromMail, String employeeIdFK, String type);
		
		@Query("{'lineItems.fromMail' : {$regex : ?0, $options: 'i'},'employeeIdFK' : ?1}")
		public List<DailyEmployeeEmailToneBO> findByFromMailline(String fromMail, String employeeIdFK);
	
		@Query("{'clientEmailItems.from' : ?0}")
		public List<DailyEmployeeEmailToneBO> findByFrom(String from);
	
		@Query("{'employeeIdFK' : ?0 , 'clientEmailItems.fromMail' : ?1, 'clientEmailItems.date' : ?2, 'clientEmailItems.time' : ?3}")
		public List<DailyEmployeeEmailToneBO> findByFromMailAndDateAndTime(String employeeIdFK, String fromMail,String date, String time);
		
		@Query("{'employeeIdFK' : ?0 , 'lineItems.fromMail' : ?1, 'lineItems.date' : ?2, 'lineItems.time' : ?3}")
		public List<DailyEmployeeEmailToneBO> findByFromMailAndDateAndTimeline(String employeeIdFK, String fromMail,String date, String time);
	 	
	 	/*@Query(value ="{'filter.name' : {$elemMatch : {'Anand' : { $exists : true }}},'reportToId': ?0}",fields = "{'filter.name' : {$elemMatch : {'Anand' : { $exists : true }}}}")
		public Page<DailyEmployeeEmailToneBO> findByReportToId(String employeeId);*/


}
