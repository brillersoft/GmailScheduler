package com.app.mongo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.app.bo.DailyTeamEmailToneBO;


@Repository
public interface DailyTeamEmailToneRepository extends MongoRepository<DailyTeamEmailToneBO, Long>{

}
