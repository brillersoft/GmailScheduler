package com.app.mongo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.app.bo.EmailConfigurationBO;
@Repository
public interface EmailConfigurationRepository extends MongoRepository<EmailConfigurationBO, String>{
	public EmailConfigurationBO findByAdminEmail(String adminEmail);

}
