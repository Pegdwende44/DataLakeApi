package com.datalake.api.model.mongo;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="document_profiles") 
public class TermFrequencyItem implements Serializable{

	
	private String term;
	
	private int freq;

	private String docId;
	
	
	
	
	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public int getFreq() {
		return freq;
	}

	public void setFreq(int freq) {
		this.freq = freq;
	}
	
	
	
	
}
