package com.datalake.api.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.websocket.server.PathParam;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.datalake.api.model.mongo.DocumentProfileWithRepresentation;
import com.datalake.api.model.mongo.DocumentProfileWithoutRepresentation;
import com.datalake.api.model.mongo.TermFrequencyItem;
import com.datalake.api.repository.mongo.DocumentProfileWithRepresentationRepository;
import com.datalake.api.repository.mongo.DocumentProfileWithoutRepresentationRepository;
import com.datalake.api.repository.mongo.DocumentProfileRepositoryCustomImpl;

@RestController
@RequestMapping("/api/mongo")
public class MongoController {

	// Logger
	public static final Logger logger = LoggerFactory.getLogger(MongoController.class);

	// Repository
	private DocumentProfileWithoutRepresentationRepository documentProfileWithoutRepresentationRepository;

	@Autowired
	public void setRepository(
			DocumentProfileWithoutRepresentationRepository documentProfileWithoutRepresentationRepository) {
		this.documentProfileWithoutRepresentationRepository = documentProfileWithoutRepresentationRepository;
	}

	
	// Method to return a list of document properties, default all, or for a
	// specified list of identifiers
	@GetMapping("/documentProperties")
	public ResponseEntity<Iterable<DocumentProfileWithoutRepresentation>> getMultiDocumentProperties(
			@RequestParam(name = "ids", required = false, defaultValue = "all") List<String> ids,
			@RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
			@RequestParam(name = "pageNo", defaultValue = "1") int pageNo) {

		PageRequest paging = PageRequest.of(pageNo, pageSize);// Paging definition
		Iterable<DocumentProfileWithoutRepresentation> result;
		if (ids.contains("all")) {// Return all profiles
			result = documentProfileWithoutRepresentationRepository.findAll(paging);
		} else {// Return from the list of ids
			List<DocumentProfileWithoutRepresentation> resultTemp = 
					(List<DocumentProfileWithoutRepresentation>) documentProfileWithoutRepresentationRepository.findAllById(ids);
			result = new PageImpl<DocumentProfileWithoutRepresentation>(resultTemp, paging, resultTemp.size());
		}
		if (result != null) {// Element présent
			return new ResponseEntity<Iterable<DocumentProfileWithoutRepresentation>>(result, HttpStatus.OK);
		} else { // Element absent
			System.out.println("NOT_FOUND");
			return new ResponseEntity<Iterable<DocumentProfileWithoutRepresentation>>(HttpStatus.NOT_FOUND);
		}

	}
	
	
	
	
	
	// Method to return a specified document properties
		// specified list of identifiers
		@GetMapping("/documentProperties/{id}")
		public ResponseEntity<DocumentProfileWithoutRepresentation> getDocumentProperties(
				@PathVariable("id") String id) {
			Optional<DocumentProfileWithoutRepresentation> resultTemp;
			DocumentProfileWithoutRepresentation result;
			if(id != null) {
				resultTemp = documentProfileWithoutRepresentationRepository.findById(id);
				if (resultTemp.isPresent()) {// Check if found: found
					result = resultTemp.get();
					System.out.println("FOUND");
					return new ResponseEntity<DocumentProfileWithoutRepresentation>(result, HttpStatus.OK);
				} 
			}
			// not found or id is null
			System.out.println("NOT_FOUND");
			return new ResponseEntity<DocumentProfileWithoutRepresentation>(HttpStatus.NOT_FOUND);
		}

	
		// Method to return a list of document nbPages, default all, or for a
		// specified list of identifiers
		@GetMapping("/documentNbPages")
		public ResponseEntity<Map<String, Integer>> getMultiDocumentNbPages(
				@RequestParam(name = "ids", required = false, defaultValue = "all") 
		List<String> ids) {
			Map<String, Integer> result;
			if (ids.contains("all")) {// Return for all documents
				result = documentProfileWithoutRepresentationRepository.documentsNbPagesAll();
			} else {// Return from the list of ids
				result = documentProfileWithoutRepresentationRepository.documentsNbPages(ids);
			}
			if (result != null) {// Elements présents
				return new ResponseEntity<Map<String, Integer>>(result, HttpStatus.OK);
			} else { // Elements absents
				System.out.println("NOT_FOUND");
				return new ResponseEntity<Map<String, Integer>>(HttpStatus.NOT_FOUND);
			}

		}
		
		
		
		
		
		
		// Method to return a list of document TopKeywords
				@GetMapping("/documentTopKw")
				public ResponseEntity<Map<String, Integer>> getMultiDocumentTopKW(
						@RequestParam(name = "ids", required = false, defaultValue = "all") List<String> ids,
						@RequestParam(name = "representation", required = true) String vocabulary,
						@RequestParam(name = "limit", defaultValue = "10") int limit) {
					Map<String, Integer> result;
					if (ids.contains("all")) {// Return for all documents
						result = documentProfileWithoutRepresentationRepository.documentsTopKwAll(vocabulary, limit);
					} else {// Return from the list of ids
						result = documentProfileWithoutRepresentationRepository.documentsTopKw(ids, vocabulary, limit);
					}
					if (result != null) {// Elements présents
						System.out.println("FOUND");
						return new ResponseEntity<Map<String, Integer>>(result, HttpStatus.OK);
					} else { // Elements absents
						System.out.println("NOT_FOUND");
						return new ResponseEntity<Map<String, Integer>>(HttpStatus.NOT_FOUND);
					}

				}
				
				
				
				// Method to return a list of document TopKeywords
				@GetMapping("/documentTopKw/{id}")
				public ResponseEntity<Map<String, Integer>> getDocumentTopKW(
						@PathVariable("id") String id,
						@RequestParam(name = "representation", required = true) String vocabulary,
						@RequestParam(name = "limit", defaultValue = "10") int limit) {
					Map<String, Integer> result;
					List<String> tempId = new ArrayList<String>();
					tempId.add(id);
						result = documentProfileWithoutRepresentationRepository.documentsTopKw(tempId, vocabulary, limit);
					
					if (result != null) {// Elements présents
						System.out.println("FOUND");
						return new ResponseEntity<Map<String, Integer>>(result, HttpStatus.OK);
					} else { // Elements absents
						System.out.println("NOT_FOUND");
						return new ResponseEntity<Map<String, Integer>>(HttpStatus.NOT_FOUND);
					}

				}
				
				
				// Method to return a list of document TopKeywords
				@GetMapping("/documentTermMatrix")
				public ResponseEntity<HashMap> getDocumentTermMatrix(
						@RequestParam(name = "ids", required = false, defaultValue = "all") List<String> ids,
						@RequestParam(name = "representation", required = true) String vocabulary,
						@RequestParam(name = "limit", defaultValue = "10") int limit) {
					HashMap result;
					if (ids.contains("all")) {// Return for all documents
						result = documentProfileWithoutRepresentationRepository.documentTermMatrixAll(vocabulary, limit);
					} else {// Return from the list of ids
						result = documentProfileWithoutRepresentationRepository.documentTermMatrix(ids, vocabulary, limit);
						
					}
					if (result != null) {// Elements présents
						System.out.println("FOUND");
						return new ResponseEntity<HashMap>(result, HttpStatus.OK);
					} else { // Elements absents
						System.out.println("NOT_FOUND");
						return new ResponseEntity<HashMap>(HttpStatus.NOT_FOUND);
					}

				}
	

}
