package com.app.mongo.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.app.bo.EmployeeBO;


@Repository
public interface EmployeeRepository extends MongoRepository<EmployeeBO, String>,PagingAndSortingRepository<EmployeeBO, String>{
	
	
	public EmployeeBO findByEmailIdIgnoreCase(String emailId);
	
	public EmployeeBO findById(Long empId);
	
	public EmployeeBO findByEmployeeId(String employeeId);
	
	/*public List<EmployeeBO> findByReportToId(String employeeId);
	
	@Query("{'': ?0,'' : {'fromDate': ?1}}")
	public List<EmployeeBO> findByReportToId(String employeeId,String date);
	
	
	//public Page<EmployeeBO> findByReportToId(String employeeId,Pageable pageable);
	
	@Query("{'roles': {$elemMatch : {'status': ?0 ,'fromDate' : { '$gte' : ?1 }}},'reportToId': ?2}")
	public Page<EmployeeBO> findByReportToId(String status,String date,String employeeId,Pageable pageable);
	
	@Query(value = "{'roles': {$elemMatch : {'status': ?0 ,'fromDate' : { '$gte' : ?1 }}},'reportToId': ?2}",fields = "{'roles': {$elemMatch : {'status': ?0 ,'fromDate' : { '$gte' : ?1 }}}}")
	public Page<EmployeeBO> findByReportToId(String status,String date,String employeeId,Pageable pageable);
	
	@Query(value ="{'roles': {$elemMatch : {'fromDate' : { '$gte' : ?0 }}},'reportToId': ?1}",fields = "{'roles': {$elemMatch : {'fromDate' : { '$gte' : ?0 }}}}")
	public Page<EmployeeBO> findByReportToId(String date,String employeeId,Pageable pageable);*/
	
	public EmployeeBO findOne(String emailId);
	
	@Query("{'employeeName': {'$regex': ?0, $options: 'i'},'reportToId': ?1}")
	public List<EmployeeBO> findByEmployeeNameRegex(String empName,String empId);
	
	@Query("{'employeeName': {'$regex': ?0, $options: 'i'}}")
	public EmployeeBO findByEmployeeNameRegex1(String empName);
	
	public List<EmployeeBO> findByEmployeeNameLike(String empName,String empId);
	
	public EmployeeBO findByResetTokenIgnoreCase(String resetToken);
	
}
