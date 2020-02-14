package com.datalake.api.model.elastic;

import java.util.List;

public class ExtendedElasticDocument extends ElasticDocument {

	private List<String> highlights;
	
	public List<String> getHighlights() {
		return highlights;
	}

	public void setHighlights(List<String> highlights) {
		this.highlights = highlights;
	}
}
