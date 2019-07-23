package com.app.mongo.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.app.bo.ClientBO;
import com.app.bo.EmployeeBO;

@Repository
public interface ClientRepository extends MongoRepository<ClientBO, String>{
	
	
//	public List<ClientBO> findByEmployeeIdFKAndOrganisationFK(String empId,String orgFk);
	
	public ClientBO findByEmployeeIdFK(String employeeIdFK);

}
