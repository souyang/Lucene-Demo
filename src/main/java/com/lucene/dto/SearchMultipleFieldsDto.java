package com.lucene.dto;

import java.util.ArrayList;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.store.Directory;

public class SearchMultipleFieldsDto {
	private Directory index; 
	private StandardAnalyzer analyzer; 
	private ArrayList<SearchCriteria> searchCriteriaList; 
	
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
	public ArrayList<SearchCriteria> getSearchCriteriaList() {
		return searchCriteriaList;
	}
	public void setSearchCriteriaList(ArrayList<SearchCriteria> searchCriteriaList) {
		this.searchCriteriaList = searchCriteriaList;
	}

}
