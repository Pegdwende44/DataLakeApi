package com.datalake.api.repository.elastic;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.search.Sort;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import com.datalake.api.model.elastic.ElasticDocument;
import com.datalake.api.model.elastic.ExtendedElasticDocument;

//@Service
public class ElasticDocumentRepositoryCustomImpl implements ElasticDocumentRepositoryCustom {

	// @Autowired
	// private ElasticDocumentRepository elasticDocumentRepository;

	@Autowired
	ElasticsearchTemplate elasticTemplate;

	@Override
	public Iterable<ElasticDocument> findDocsByMatchingTerms(List<String> ids, List<String> matchingTerms,
			Pageable paging, boolean strict) {
		org.elasticsearch.index.query.QueryBuilder query1;

		if (strict) {
			query1 = org.elasticsearch.index.query.QueryBuilders.matchQuery("content", matchingTerms)
					.operator(Operator.AND);
		} else {
			query1 = org.elasticsearch.index.query.QueryBuilders.matchQuery("content", matchingTerms)
					.operator(Operator.OR);
		}

		org.elasticsearch.index.query.QueryBuilder query2 = org.elasticsearch.index.query.QueryBuilders
				.termsQuery("identifier", ids);

		// Filter on identifiers
		// BoolQueryBuilder idsFilter =
		// QueryBuilders.boolQuery().tertermsQuery("identifier", ids));

		SearchQuery searchQuery = new NativeSearchQueryBuilder().withFields("identifier")// .withIndices("document_index")
				.withFilter(query2).withQuery(query1).withSort(SortBuilders.scoreSort().order(SortOrder.DESC))
				.withPageable(paging).build();

		List<ElasticDocument> docs = elasticTemplate.query(searchQuery, new ResultsExtractor<List<ElasticDocument>>() {

			@Override
			public List<ElasticDocument> extract(SearchResponse response) {

				long totalHits = response.getHits().totalHits; // totalHits();
				List<ElasticDocument> res = new ArrayList<ElasticDocument>();

				for (SearchHit hit : response.getHits()) {
					if (hit != null) {
						ElasticDocument doc = new ElasticDocument();// Init a new doc
						doc.setId(hit.getId());// Set Id
						doc.setIdentifier((String) hit.getSourceAsMap().get("identifier"));
						doc.setScore(hit.getScore());

						res.add(doc);
					}

				}
				return res;
			}
		});

		return docs;
	}

	@Override
	public Iterable<ElasticDocument> findDocsByMatchingTermsAll(List<String> matchingTerms, Pageable paging,
			boolean strict) {
		org.elasticsearch.index.query.QueryBuilder query1;
		if (strict) {
			query1 = org.elasticsearch.index.query.QueryBuilders.matchQuery("content", matchingTerms)
					.operator(Operator.AND);
		} else {
			query1 = org.elasticsearch.index.query.QueryBuilders.matchQuery("content", matchingTerms)
					.operator(Operator.OR);
		}

		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(query1)
				.withSort(SortBuilders.scoreSort().order(SortOrder.DESC)).withPageable(paging).build();
		List<ElasticDocument> docs = elasticTemplate.query(searchQuery, new ResultsExtractor<List<ElasticDocument>>() {

			@Override
			public List<ElasticDocument> extract(SearchResponse response) {

				long totalHits = response.getHits().totalHits; // totalHits();
				List<ElasticDocument> res = new ArrayList<ElasticDocument>();

				for (SearchHit hit : response.getHits()) {
					if (hit != null) {
						ElasticDocument doc = new ElasticDocument();// Init a new doc
						doc.setId(hit.getId());// Set Id
						doc.setIdentifier((String) hit.getSourceAsMap().get("identifier"));
						doc.setScore(hit.getScore());
						res.add(doc);

					}

				}
				return res;
			}
		});

		return docs;
	}

	@Override
	public Iterable<ExtendedElasticDocument> findDocsHighlightsByMatchingTermsAll(List<String> matchingTerms,
			Pageable paging, boolean strict, int fragmentSize) {
		org.elasticsearch.index.query.QueryBuilder query1;
		if (strict) {
			query1 = org.elasticsearch.index.query.QueryBuilders.matchQuery("content", matchingTerms)
					.operator(Operator.AND);
		} else {
			query1 = org.elasticsearch.index.query.QueryBuilders.matchQuery("content", matchingTerms)
					.operator(Operator.OR);
		}

		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(query1)
				.withSort(SortBuilders.scoreSort().order(SortOrder.DESC))
				.withHighlightFields(new HighlightBuilder.Field("content").preTags("<mark>").postTags("</mark>")
						.numOfFragments(20).fragmentSize(fragmentSize))
				.withPageable(paging).build();
		List<ExtendedElasticDocument> docs = elasticTemplate.query(searchQuery,
				new ResultsExtractor<List<ExtendedElasticDocument>>() {

					@Override
					public List<ExtendedElasticDocument> extract(SearchResponse response) {

						long totalHits = response.getHits().totalHits; // totalHits();
						List<ExtendedElasticDocument> res = new ArrayList<ExtendedElasticDocument>();

						for (SearchHit hit : response.getHits()) {
							if (hit != null) {
								ExtendedElasticDocument doc = new ExtendedElasticDocument();// Init a new doc
								doc.setId(hit.getId());// Set Id
								doc.setIdentifier((String) hit.getSourceAsMap().get("identifier"));
								doc.setScore(hit.getScore());
								List<String> highlights = new ArrayList<String>();
								HighlightField highlightField = hit.getHighlightFields().get("content");
								if (highlightField != null) {
									Text[] fragments = highlightField.fragments();
									for (Text text : fragments) {
										highlights.add(text.string());
									}

								}
								doc.setHighlights(highlights);
								res.add(doc);

							}

						}
						return res;
					}
				});

		return docs;
	}

	@Override
	public Iterable<ExtendedElasticDocument> findDocsHighlightsByMatchingTerms(List<String> ids,
			List<String> matchingTerms, Pageable paging, boolean strict, int fragmentSize) {
		org.elasticsearch.index.query.QueryBuilder query1;
		if (strict) {
			query1 = org.elasticsearch.index.query.QueryBuilders.matchQuery("content", matchingTerms)
					.operator(Operator.AND);
		} else {
			query1 = org.elasticsearch.index.query.QueryBuilders.matchQuery("content", matchingTerms)
					.operator(Operator.OR);
		}
		org.elasticsearch.index.query.QueryBuilder query2 = org.elasticsearch.index.query.QueryBuilders
				.termsQuery("identifier", ids);
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(query1).withFilter(query2)
				.withSort(SortBuilders.scoreSort().order(SortOrder.DESC))
				.withHighlightFields(new HighlightBuilder.Field("content").preTags("<mark>").postTags("</mark>")
						.numOfFragments(20).fragmentSize(fragmentSize))
				.withPageable(paging).build();

		List<ExtendedElasticDocument> docs = elasticTemplate.query(searchQuery,
				new ResultsExtractor<List<ExtendedElasticDocument>>() {

					@Override
					public List<ExtendedElasticDocument> extract(SearchResponse response) {

						long totalHits = response.getHits().totalHits; // totalHits();
						List<ExtendedElasticDocument> res = new ArrayList<ExtendedElasticDocument>();

						for (SearchHit hit : response.getHits()) {
							if (hit != null) {
								ExtendedElasticDocument doc = new ExtendedElasticDocument();// Init a new doc
								doc.setId(hit.getId());// Set Id
								doc.setIdentifier((String) hit.getSourceAsMap().get("identifier"));
								doc.setScore(hit.getScore());
								List<String> highlights = new ArrayList<String>();
								HighlightField highlightField = hit.getHighlightFields().get("content");
								if (highlightField != null) {
									Text[] fragments = highlightField.fragments();
									for (Text text : fragments) {
										highlights.add(text.string());
									}

								}
								doc.setHighlights(highlights);
								res.add(doc);

							}

						}
						return res;
					}
				});

		return docs;
	}

	@Override
	public Iterable<String> findDocsByUnMatchingTermsAll(List<String> negTerms, Pageable paging, boolean strict) {
		org.elasticsearch.index.query.QueryBuilder query1;
		if (strict) {
			query1 = org.elasticsearch.index.query.QueryBuilders.matchQuery("content", negTerms)
					.operator(Operator.AND);
		} else {
			query1 = org.elasticsearch.index.query.QueryBuilders.matchQuery("content", negTerms)
					.operator(Operator.OR);
		}
		org.elasticsearch.index.query.QueryBuilder query = org.elasticsearch.index.query.QueryBuilders.boolQuery().mustNot(query1);
	
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(query)
				.withSort(SortBuilders.scoreSort().order(SortOrder.DESC)).withPageable(paging).build();
		List<String> docs = elasticTemplate.query(searchQuery, new ResultsExtractor<List<String>>() {

			@Override
			public List<String> extract(SearchResponse response) {

				long totalHits = response.getHits().totalHits; // totalHits();
				List<String> res = new ArrayList<String>();

				for (SearchHit hit : response.getHits()) {
					if (hit != null) {
					
						res.add((String) hit.getSourceAsMap().get("identifier"));

					}

				}
				return res;
			}
		});

		return docs;
	}

	@Override
	public Iterable<String> findDocsByUnMatchingTerms(List<String> ids, List<String> negTerms, Pageable paging,
			boolean strict) {
		org.elasticsearch.index.query.QueryBuilder query1;
		if (strict) {
			query1 = org.elasticsearch.index.query.QueryBuilders.matchQuery("content", negTerms)
					.operator(Operator.AND);
		} else {
			query1 = org.elasticsearch.index.query.QueryBuilders.matchQuery("content", negTerms)
					.operator(Operator.OR);
		}
		org.elasticsearch.index.query.QueryBuilder query2 = org.elasticsearch.index.query.QueryBuilders
				.termsQuery("identifier", ids);
		org.elasticsearch.index.query.QueryBuilder query = org.elasticsearch.index.query.QueryBuilders.boolQuery().mustNot(query1).filter(query2);
	
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(query)
				
				.withSort(SortBuilders.scoreSort().order(SortOrder.DESC)).withPageable(paging).build();
		List<String> docs = elasticTemplate.query(searchQuery, new ResultsExtractor<List<String>>() {

			@Override
			public List<String> extract(SearchResponse response) {

				long totalHits = response.getHits().totalHits; // totalHits();
				List<String> res = new ArrayList<String>();

				for (SearchHit hit : response.getHits()) {
					if (hit != null) {
					
						res.add((String) hit.getSourceAsMap().get("identifier"));

					}

				}
				return res;
			}
		});

		return docs;
	}

}
