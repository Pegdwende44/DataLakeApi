package com.datalake.api.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.datalake.api.model.elastic.ElasticDocument;
import com.datalake.api.model.elastic.ExtendedElasticDocument;
import com.datalake.api.model.mongo.DocumentProfileWithoutRepresentation;
import com.datalake.api.repository.elastic.ElasticDocumentRepository;
import com.datalake.api.repository.mongo.DocumentProfileWithoutRepresentationRepository;

@RestController
@RequestMapping("/api/elastic")
public class ElasticController {

	
	// Logger
		public static final Logger logger = LoggerFactory.getLogger(ElasticController.class);
		
		
		// Repository
		private ElasticDocumentRepository elasticDocumentRepository;

		@Autowired
		public void setRepository(ElasticDocumentRepository elasticDocumentRepository) {
			this.elasticDocumentRepository = elasticDocumentRepository;
		}
		
		
		// Method to return a list of document properties, default all, or for a
		// specified list of identifiers
		@GetMapping("/documentsByTerms")
		public ResponseEntity<Iterable<ElasticDocument>> getDocumentsMatchingTerms(
				@RequestParam(name = "ids", required = false, defaultValue = "all") List<String> ids,
				@RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
				@RequestParam(name = "pageNo", defaultValue = "0") int pageNo,
				@RequestParam(name = "terms", required=false) List<String> posTerms,
				@RequestParam(name = "strict", required=false, defaultValue = "1") Integer strict) {

			PageRequest paging = PageRequest.of(pageNo, pageSize);// Paging definition
			
			Iterable<ElasticDocument> result;
			System.out.println(ids);
			if (ids.contains("all")) {// Return all profiles
				result = elasticDocumentRepository.findDocsByMatchingTermsAll(posTerms, paging, strict==1);
			} else {// Return from the list of ids
				
				result = elasticDocumentRepository.findDocsByMatchingTerms(ids, posTerms, paging, strict==1);
			}
			if (result != null) {// Element présent
				return new ResponseEntity<Iterable<ElasticDocument>>(result, HttpStatus.OK);
			} else { // Element absent
				System.out.println("NOT_FOUND");
				return new ResponseEntity<Iterable<ElasticDocument>>(HttpStatus.NOT_FOUND);
			}

		}
		
		
		// Method to return a list of document properties, default all, or for a
				// specified list of identifiers
				@GetMapping("/documentsHighlightsByTerms")
				public ResponseEntity<Iterable<ExtendedElasticDocument>> getDocumentsHighlightsMatchingTerms(
						@RequestParam(name = "ids", required = false, defaultValue = "all") List<String> ids,
						@RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
						@RequestParam(name = "pageNo", defaultValue = "0") int pageNo,
						@RequestParam(name = "terms", required=false) List<String> posTerms,
						@RequestParam(name = "strict", required=false, defaultValue = "1") Integer strict,
						@RequestParam(name = "fragSize", required=false, defaultValue = "0") Integer size) {

					PageRequest paging = PageRequest.of(pageNo, pageSize);// Paging definition
					System.out.println(pageNo);
					Iterable<ExtendedElasticDocument> result;
					if (ids.contains("all")) {// Return all profiles
						result = elasticDocumentRepository.findDocsHighlightsByMatchingTermsAll(posTerms, paging, strict==1, size);
					} else {// Return from the list of ids
						result = elasticDocumentRepository.findDocsHighlightsByMatchingTerms(ids, posTerms, paging, strict==1, size);
					}
					if (result != null) {// Element présent
						return new ResponseEntity<Iterable<ExtendedElasticDocument>>(result, HttpStatus.OK);
					} else { // Element absent
						System.out.println("NOT_FOUND");
						return new ResponseEntity<Iterable<ExtendedElasticDocument>>(HttpStatus.NOT_FOUND);
					}

				}
				
				
				
				
				// Method to return a list of document properties, default all, or for a
				// specified list of identifiers
				@GetMapping("/documentsUnmatchingTerms")
				public ResponseEntity<Iterable<String>> getDocumentsUnMatchingTerms(
						@RequestParam(name = "ids", required = false, defaultValue = "all") List<String> ids,
						@RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
						@RequestParam(name = "pageNo", defaultValue = "0") int pageNo,
						@RequestParam(name = "terms", required=false) List<String> negTerms,
						@RequestParam(name = "strict", required=false, defaultValue = "1") Integer strict) {

					PageRequest paging = PageRequest.of(pageNo, pageSize);// Paging definition
					
					Iterable<String> result;
					if (ids.contains("all")) {// Return all profiles
						result = elasticDocumentRepository.findDocsByUnMatchingTermsAll(negTerms, paging, strict==1);
					} else {// Return from the list of ids
						
						result = elasticDocumentRepository.findDocsByUnMatchingTerms(ids, negTerms, paging, strict==1);
					}
					if (result != null) {// Element présent
						return new ResponseEntity<Iterable<String>>(result, HttpStatus.OK);
					} else { // Element absent
						System.out.println("NOT_FOUND");
						return new ResponseEntity<Iterable<String>>(HttpStatus.NOT_FOUND);
					}

				}
}
