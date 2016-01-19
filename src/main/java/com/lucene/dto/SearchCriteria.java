package com.lucene.dto;

public class SearchCriteria {
	
	private String searchField;
	private String queryString;
	private boolean isExactMatch;
	public SearchCriteria() {
		
	}
	public SearchCriteria(String currentSearchField, String currentQueryString, boolean exactMatch) {
		setSearchField(currentSearchField);
		setQueryString(currentQueryString);
		setExactMatch(exactMatch);
	}
	
	public boolean isExactMatch() {
		return isExactMatch;
	}
	public void setExactMatch(boolean isExactMatch) {
		this.isExactMatch = isExactMatch;
	}
	public String getSearchField() {
		return searchField;
	}

	public void setSearchField(String searchField) {
		this.searchField = searchField;
	}
	public String getQueryString() {
		return queryString;
	}
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((queryString == null) ? 0 : queryString.hashCode());
		result = prime * result + ((searchField == null) ? 0 : searchField.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SearchCriteria other = (SearchCriteria) obj;
		if (queryString == null) {
			if (other.queryString != null)
				return false;
		} else if (!queryString.equals(other.queryString))
			return false;
		if (searchField == null) {
			if (other.searchField != null)
				return false;
		} else if (!searchField.equals(other.searchField))
			return false;
		return true;
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		return sb.append("SearchCriteria [searchField=").
				append(searchField).append(", queryString=").append(queryString).append(", isExactMatch=")
				.append(isExactMatch).append("]").toString() ;
	}

}
