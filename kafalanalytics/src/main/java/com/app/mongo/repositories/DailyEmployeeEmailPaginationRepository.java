package com.app.mongo.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.app.bo.DailyEmployeeEmailToneBO;

@Repository
public interface DailyEmployeeEmailPaginationRepository extends PagingAndSortingRepository<DailyEmployeeEmailToneBO,String >{

	
	
	@Query("{'date' : { '$gte' : ?0 },'employeeIdFK': ?1 }")
	public Page<DailyEmployeeEmailToneBO> findByDateAndEmployeeIdFK(String date,String employeeIdFK,Pageable page);
	
	@Query("{'employeeIdFK': ?0,'lineItems.type': ?1,'lineItems.fromMail': ?2}")
	public Page<DailyEmployeeEmailToneBO> findByEmployeeIdFKAndTypeAndFromMail(String employeeIdFK,String type, String fromMail, Pageable page);
	
}
