package com.app.mongo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.app.bo.VendorInfo;

@Repository
public interface VendorInfoRepository extends MongoRepository<VendorInfo, Long> {

}
