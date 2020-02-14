package com.datalake.api.repository.elastic;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.datalake.api.model.elastic.ElasticDocument;
import com.datalake.api.model.elastic.ExtendedElasticDocument;

public interface ElasticDocumentRepositoryCustom {





	Iterable<ElasticDocument> findDocsByMatchingTerms(List<String> ids, List<String> matchingTerms, Pageable paging,
			boolean strict);

	Iterable<ElasticDocument> findDocsByMatchingTermsAll(List<String> matchingTerms, Pageable paging, boolean strict);

	
	
	Iterable<ExtendedElasticDocument> findDocsHighlightsByMatchingTermsAll(List<String> matchingTerms, Pageable paging,
			boolean strict, int fragmentSize);

	Iterable<ExtendedElasticDocument> findDocsHighlightsByMatchingTerms(List<String> ids, List<String> matchingTerms,
			Pageable paging, boolean strict, int fragmentSize);
	
	Iterable<String> findDocsByUnMatchingTermsAll(List<String> negTerms, Pageable paging, boolean strict);
	
	Iterable<String> findDocsByUnMatchingTerms(List<String> ids, List<String> negTerms, Pageable paging, boolean strict);
	
}
