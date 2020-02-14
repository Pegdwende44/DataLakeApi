package com.datalake.api.model.mongo;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="document_profiles") 
public class DocumentProfileWithRepresentation extends DocumentProfileWithoutRepresentation{
	
	
	
	private Collection<MongoRepresentation> representations;

	

	public Collection<MongoRepresentation> getRepresentations() {
		return representations;
	}

	public void setRepresentations(Collection<MongoRepresentation> representations) {
		this.representations = representations;
	}
	
	

}
