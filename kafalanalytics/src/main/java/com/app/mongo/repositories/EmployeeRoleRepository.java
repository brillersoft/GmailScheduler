package com.app.mongo.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.app.bo.EmployeeRoleBO;

@Repository
public interface EmployeeRoleRepository extends MongoRepository<EmployeeRoleBO, String>,PagingAndSortingRepository<EmployeeRoleBO, String>{
	
	
	
  	public EmployeeRoleBO findByEmployeeIdFKAndStatus(String empIdFk,String status);
  	
  //	public EmployeeRoleBO findByEmployeeIdFKAndfromDate(String empIdFk,String date);
  	
  	
  	public List<EmployeeRoleBO> findByReportToIdAndStatus(String reportTo,String status);
  	
//  	public EmployeeRoleBO findOneByReportToIdAndNameAndStatus(String reportTo,String status);
  	
  	public Page<EmployeeRoleBO> findByReportToIdAndStatus(String reportTo,String status,Pageable pageable);
  	
  	@Query("{'reportToId' : ?0, 'fromDate' : { '$gte' : ?1 }}")
	public List<EmployeeRoleBO> findByReportToIdAndFromDate(String reportTo,String date);
  	
  	@Query("{'reportToId' : ?0, 'fromDate' : { '$gte' : ?1 }}")
	public Page<EmployeeRoleBO> findByReportToIdAndFromDate(String reportTo,String date,Pageable pageable);

}
