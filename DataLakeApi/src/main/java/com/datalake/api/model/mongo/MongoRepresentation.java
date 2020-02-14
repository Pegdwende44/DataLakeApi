package com.datalake.api.model.mongo;

import java.util.Collection;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.mongodb.BasicDBObject;

@Document(collection="document_profiles") 
public class MongoRepresentation {
	
	
	private String name;
	
	private String storageType;
	
	
	@Field(value = "data")
	private Collection<TermFrequencyItem> data;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStorageType() {
		return storageType;
	}

	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}

	public Collection<TermFrequencyItem> getData() {
		return data;
	}

	public void setData(Collection<TermFrequencyItem> data) {
		this.data = data;
	}

	
	

}
