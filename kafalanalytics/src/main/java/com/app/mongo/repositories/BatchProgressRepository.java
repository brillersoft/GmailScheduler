package com.app.mongo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.stereotype.Repository;

import com.app.bo.BatchProgressBO;


@Repository
public interface BatchProgressRepository extends MongoRepository<BatchProgressBO, String> {
	
	public BatchProgressBO findByAdminId(String adminId);

}
