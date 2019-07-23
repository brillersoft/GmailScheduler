package com.app.mongo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.app.bo.DailyOrgEmailToneBO;

@Repository
public interface DailyOrgEmailToneRepository extends MongoRepository<DailyOrgEmailToneBO, String>{

}
