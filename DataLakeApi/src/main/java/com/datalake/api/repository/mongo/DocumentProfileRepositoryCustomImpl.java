package com.datalake.api.repository.mongo;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.Fields;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.mapreduce.MapReduceOptions;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.datalake.api.model.mongo.DocumentProfileWithRepresentation;
import com.datalake.api.model.mongo.DocumentProfileWithoutRepresentation;
import com.datalake.api.model.mongo.TermFrequencyItem;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
/*import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StringType;
*/



public class DocumentProfileRepositoryCustomImpl implements DocumentProfileRepositoryCustom{

	@Autowired
	MongoTemplate mongoTemplate;
	
	
	@Override
	public Map<String, Integer> documentsNbPages(List<String> ids){
		HashMap<String, Integer> result = new HashMap<>();
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").in(ids));
		query.fields().include("_id").include("nbPages");
		List<DocumentProfileWithoutRepresentation> resultTemp=  mongoTemplate.find(query, DocumentProfileWithoutRepresentation.class);
		if(resultTemp != null) {
			for (DocumentProfileWithoutRepresentation temp : resultTemp) {
				result.put(temp.getId(), temp.getNbPages());
			}
		}
		return result;
		
	}
	
	
	@Override
	public Map<String, Integer> documentsNbPagesAll(){
		HashMap<String, Integer> result = new HashMap<>();
		Query query = new Query();
		query.fields().include("_id").include("nbPages");
		List<DocumentProfileWithoutRepresentation> resultTemp=  mongoTemplate.find(query, DocumentProfileWithoutRepresentation.class);
		if(resultTemp != null) {
			for (DocumentProfileWithoutRepresentation temp : resultTemp) {
				result.put(temp.getId(), temp.getNbPages());
			}
		}
		return result;
		
	}


	@Override
	public Map<String, Integer> documentsTopKwAll(String representation, int limit) {
		LinkedHashMap<String, Integer> result = new LinkedHashMap<String, Integer>();
		Aggregation agg = newAggregation(
			    unwind("representations"),
			    match(Criteria.where("representations.name").is(representation)),
			    unwind("representations.data"),
			    project("representations.data.term", "representations.data.freq")
			    .and("representations.data.term").as("term")
			    .and("representations.data.freq").as("freq"),
			    group("term").sum("freq").as("freq"),
			    project("freq").and("$_id").as("term").andExclude("_id"),
			    sort(Sort.Direction.DESC, "freq"),
			    limit(limit)
			);
		
		AggregationResults<TermFrequencyItem> resultTemp =  mongoTemplate.aggregate(agg, "document_profiles", TermFrequencyItem.class);
		for (TermFrequencyItem termFrequencyItem : resultTemp) {
			  result.put(termFrequencyItem.getTerm(),termFrequencyItem.getFreq());
			}
		return result;
	}


	@Override
	public Map<String, Integer> documentsTopKw(List<String> ids, String representation, int limit) {
		LinkedHashMap<String, Integer> result = new LinkedHashMap<String, Integer>();
		Aggregation agg = newAggregation(
				match(Criteria.where("_id").in(ids)),
			    unwind("representations"),
			    match(Criteria.where("representations.name").is(representation)),
			    unwind("representations.data"),
			    project("representations.data.term", "representations.data.freq")
			    .and("representations.data.term").as("term")
			    .and("representations.data.freq").as("freq"),
			    group("term").sum("freq").as("freq"),
			    project("freq").and("$_id").as("term").andExclude("_id"),
			    sort(Sort.Direction.DESC, "freq"),
			    limit(limit)
			);
		
		AggregationResults<TermFrequencyItem> resultTemp =  mongoTemplate.aggregate(agg, "document_profiles", TermFrequencyItem.class);
		for (TermFrequencyItem termFrequencyItem : resultTemp) {
			  result.put(termFrequencyItem.getTerm(),termFrequencyItem.getFreq());
			}
		return result;
	}
	
	
	
	
	@Override
	public HashMap documentTermMatrixAll(String representation, int limit) {
		LinkedHashMap<String, Integer> result = new LinkedHashMap<String, Integer>();
		Aggregation agg = newAggregation(
				//match(Criteria.where("_id").in(ids)),
			    unwind("representations"),
			    match(Criteria.where("representations.name").is(representation)),
			    unwind("representations.data"),
			    project("representations.data.term", "representations.data.freq", "_id")
			    .and("representations.data.term").as("term")
			    .and("representations.data.freq").as("freq")
			    .and("_id").as("docId")
			    //group("term").sum("freq").as("freq"),
			    //project("freq").and("$_id").as("term").andExclude("_id"),
			    //sort(Sort.Direction.DESC, "freq"),
			    // limit(limit)
			);
		
		AggregationResults<TermFrequencyItem> resultTemp =  mongoTemplate.aggregate(agg, "document_profiles", TermFrequencyItem.class);
		
		
		//Apache Spark
		/*System.setProperty("hadoop.home.dir", "C:\\Users\\Pegdwende\\Documents\\winutils");
				SparkSession spark = SparkSession.builder().appName("documentation").master("local[2]").getOrCreate();
				spark.sparkContext().setLogLevel("ERROR");
				Dataset<Row> dataFrame = spark.createDataFrame(resultTemp.getMappedResults(), TermFrequencyItem.class);
				dataFrame.printSchema();
				
				//System.out.println(dataFrame.groupBy("docId").pivot("freq").count());
				Dataset<Row> df2 = dataFrame.groupBy("docId").pivot("freq").count().toDF();
				 df2.show();
				return df2.toJSON().collectAsList();
				//return null;*/
		HashMap<String, HashMap<String, Integer>> myArray = new HashMap<String, HashMap<String, Integer>>();
		for (TermFrequencyItem docTermFrequency:resultTemp) {
			String docId = docTermFrequency.getDocId();
			if(!myArray.containsKey(docId)) {
				 myArray.put(docId, new HashMap<String, Integer>());
			}else {
				myArray.get(docId).put(docTermFrequency.getTerm(), docTermFrequency.getFreq());
			}
		}
		return myArray;
	}
	
	
	
	@Override
	public HashMap documentTermMatrix(List<String> ids, String representation, int limit) {
		LinkedHashMap<String, Integer> result = new LinkedHashMap<String, Integer>();
		Aggregation agg = newAggregation(
				match(Criteria.where("_id").in(ids)),
				unwind("representations"),
			    match(Criteria.where("representations.name").is(representation)),
			    unwind("representations.data"),
			    project("representations.data.term", "representations.data.freq", "_id")
			    .and("representations.data.term").as("term")
			    .and("representations.data.freq").as("freq")
			    .and("_id").as("docId")
			);
		
		AggregationResults<TermFrequencyItem> resultTemp =  mongoTemplate.aggregate(agg, "document_profiles", TermFrequencyItem.class);
		
		
		HashMap<String, HashMap<String, Integer>> myArray = new HashMap<String, HashMap<String, Integer>>();
		for (TermFrequencyItem docTermFrequency:resultTemp) {
			String docId = docTermFrequency.getDocId();
			if(!myArray.containsKey(docId)) {
				 myArray.put(docId, new HashMap<String, Integer>());
			}else {
				myArray.get(docId).put(docTermFrequency.getTerm(), docTermFrequency.getFreq());
			}
		}
		return myArray;
		
	}

}
