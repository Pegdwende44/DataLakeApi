package com.datalake.api.repository.elastic;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.datalake.api.model.elastic.ElasticDocument;

@Repository
public interface ElasticDocumentRepository extends ElasticsearchRepository<ElasticDocument, String>, ElasticDocumentRepositoryCustom {

	

}
