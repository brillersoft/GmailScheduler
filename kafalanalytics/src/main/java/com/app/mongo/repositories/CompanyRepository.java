package com.app.mongo.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


import com.app.bo.OrganisationBO;

@Repository
public interface CompanyRepository extends MongoRepository<OrganisationBO, String>{
	
	
	@Query("{'companyName': {'$regex': ?0, $options: 'i'}}")
	public List<OrganisationBO> findByCompanyNameRegex(String cmpName);
	
	@Query("{'companyName': {'$regex': ?0, $options: 'i'}}")
	public OrganisationBO findByCompanyName(String cmpName);
	
	public List<OrganisationBO> findByCompanyEmailDomain(String companyEmailDomain);

	
	public OrganisationBO findById(String id);
}
