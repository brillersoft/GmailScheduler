package com.app.mongo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.app.bo.CronRunTime;

public interface CronRunTimeRepository extends MongoRepository<CronRunTime, String>{
	
	
	public CronRunTime findByOtp(String otp);

}
