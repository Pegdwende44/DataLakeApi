package com.datalake.api.repository.mongo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.neo4j.annotation.Query;

import com.datalake.api.model.mongo.DocumentProfileWithRepresentation;
import com.datalake.api.model.mongo.DocumentProfileWithoutRepresentation;
import com.datalake.api.model.mongo.TermFrequencyItem;


public interface DocumentProfileRepositoryCustom {

	Map<String, Integer> documentsNbPagesAll();

	Map<String, Integer> documentsNbPages(List<String> ids);
	
	Map<String, Integer> documentsTopKwAll(String representation, int limit);
	
	Map<String, Integer> documentsTopKw(List<String> ids, String representation, int limit);

	HashMap documentTermMatrix(List<String> ids, String representation, int limit);

	HashMap documentTermMatrixAll(String representation, int limit);
}
