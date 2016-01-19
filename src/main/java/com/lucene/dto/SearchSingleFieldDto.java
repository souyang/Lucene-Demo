package com.lucene.dto;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.store.Directory;

public class SearchSingleFieldDto {
	
	private Directory index; 
	private StandardAnalyzer analyzer; 
	private SearchCriteria searchCriteria;
	
	public Directory getIndex() {
		return index;
	}
	public StandardAnalyzer getAnalyzer() {
		return analyzer;
	}

	public void setIndex(Directory index) {
		this.index = index;
	}
	public void setAnalyzer(StandardAnalyzer analyzer) {
		this.analyzer = analyzer;
	}
	public SearchCriteria getSearchCriteria() {
		return searchCriteria;
	}
	public void setSearchCriteria(SearchCriteria searchCriteria) {
		this.searchCriteria = searchCriteria;
	}


}
