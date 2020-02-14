package com.datalake.api.repository.mongo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

import com.datalake.api.model.mongo.DocumentProfileWithRepresentation;
import com.datalake.api.model.mongo.DocumentProfileWithoutRepresentation;

@Repository
public interface DocumentProfileWithoutRepresentationRepository extends MongoRepository<DocumentProfileWithoutRepresentation, String>, DocumentProfileRepositoryCustom{
	
	
}
