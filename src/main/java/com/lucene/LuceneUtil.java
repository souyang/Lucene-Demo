package com.lucene;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import com.lucene.dto.ImdbDocumentDto;
import com.lucene.dto.SearchCriteria;
import com.lucene.dto.SearchMultipleFieldsDto;
import com.lucene.dto.SearchSingleFieldDto;

public class LuceneUtil {

	/**
	 * @param searchCriteria
	 * @return
	 */
	public static String formatQueryString(SearchCriteria searchCriteria) {
		StringBuilder sb = new StringBuilder();
		String queryString = searchCriteria.getQueryString();
		String searchField = searchCriteria.getSearchField();
		boolean isExactMatch = searchCriteria.isExactMatch();
		sb.setLength(0);  
			if (queryString.contains("|") || queryString.contains(",")) {
				boolean isStartOfQueryString = true;
				for (int i = 1; i <= queryString.length(); i++) {
					if (',' == (queryString.charAt(i - 1)) && !isStartOfQueryString) {
						sb.append(" AND ").append(searchField).append(":").
						append(appendWildCard(determineSubString(queryString.substring(i)), isExactMatch));
						
					} else if (',' == (queryString.charAt(i - 1)) && isStartOfQueryString) {
						isStartOfQueryString = false;
						sb.append(searchField).append(":").append(appendWildCard(queryString.substring(0, i - 1), isExactMatch));	
									sb.append(" AND ").append(searchField).append(":")
											.append(appendWildCard(determineSubString(queryString.substring(i)), isExactMatch));
							
					} else if ('|' == (queryString.charAt(i - 1)) && !isStartOfQueryString) {
						sb.append(" OR ").append(searchField).append(":")
								.append(appendWildCard(determineSubString(queryString.substring(i)), isExactMatch));
					} else if ('|' == (queryString.charAt(i - 1)) && isStartOfQueryString) {
						isStartOfQueryString = false;
						sb.append(searchField).append(":").append(appendWildCard(queryString.substring(0, i - 1),isExactMatch)).append(" OR ")
								.append(searchField).append(":")
								.append(appendWildCard(determineSubString(queryString.substring(i)), isExactMatch));
					}
				} 
			}
			else{sb.append(searchField).append(":").append(appendWildCard(queryString,isExactMatch));}
		return sb.toString();
	}
	
	public static String getDefaultSearchField()
	{
		return "title";
	}
	
	private static String determineSubString(String rest)
	{
		StringBuilder sb = new StringBuilder();
		int lastIndexofOr = rest.indexOf('|');
		int lastIndexOfAnd = rest.indexOf(',');
		if (lastIndexofOr == -1 && lastIndexOfAnd == -1) {
			sb.append(rest);
		}
		else if(lastIndexofOr == -1){
			sb.append(rest.substring(0,lastIndexOfAnd));
		}
		else if(lastIndexOfAnd == -1){
			sb.append(rest.substring(0,lastIndexofOr));			
		}
		else{
			sb.append(rest.substring(0,Math.min(lastIndexofOr,lastIndexOfAnd)));			
		}
		return sb.toString();
	}
	/**
	 * @param searchCriteriaList
	 * @return
	 */
	public static String formatQueryString(ArrayList<SearchCriteria> searchCriteriaList) {
		StringBuilder sb = new StringBuilder();
		 boolean isStart = true;
		  for (int j = 0; j < searchCriteriaList.size(); j++) {
			  SearchCriteria currentCriteria = searchCriteriaList.get(j);
			  if (!isStart) {
				sb.append(" AND ").append(formatQueryString(currentCriteria));
			}
			  else {
				sb.append(formatQueryString(currentCriteria));
			}
			isStart = false;
		}
		String result = sb.toString();
		sb.setLength(0);
		return result;
	}
	private static String appendWildCard(String queryString, boolean isExactMatch){
		StringBuilder sb = new StringBuilder();
		if (!isExactMatch) {
			return sb.append(queryString).append("*").toString() ;
		}
		else
			return sb.append("\"").append(queryString).append("\"").toString();
	}
	
	/**
	 * @param searchDto
	 */
	public static void validateSearchDto(SearchSingleFieldDto searchDto) {
		if (searchDto.getSearchCriteria() == null) {
			throw new IllegalArgumentException("search criteria cannot be null.");
		}
		String queryString = searchDto.getSearchCriteria().getQueryString();
		String searchField = searchDto.getSearchCriteria().getSearchField();
		if( queryString == null || "".equals(queryString))
			  throw new IllegalArgumentException("search querystring cannot be null.");
		  
	 	  if(queryString.contains("*") || queryString.startsWith(",")|| queryString.startsWith("|") ){
	 		  throw new IllegalArgumentException("invalid search query string. The search query string should not contain '*' AND should not start with '|' or ','");
	 		  }
	 	  if(searchField == null || "".equals(searchField))
	 		 throw new IllegalArgumentException("search field cannot be null.");
	 	  
	 	  if(searchDto.getAnalyzer() == null){
		 		 throw new IllegalArgumentException("search analyzer cannot be null.");	 		  
	 	  }
	 	  if(searchDto.getAnalyzer() == null){
		 		 throw new IllegalArgumentException("search index cannot be null.");	 		  
	 	  }
	}
	
	public static void validateSearchMultipleFieldsDto(SearchMultipleFieldsDto searchDto) {

	 	  if (searchDto == null || searchDto.getSearchCriteriaList() == null || searchDto.getSearchCriteriaList().size() == 0) {
	 		 throw new IllegalArgumentException("search criteria list cannot be null or search criteria list should have at least one element");
		}  
		
	 	  if ( searchDto.getSearchCriteriaList().size() > 0) {
	 		 for (int i = 0; i < searchDto.getSearchCriteriaList().size(); i++) {
				String currentQueryString = searchDto.getSearchCriteriaList().get(i).getQueryString();
				String currentSearchField = searchDto.getSearchCriteriaList().get(i).getSearchField();
	 			 if (currentSearchField == null || currentQueryString == null || "".equals(currentQueryString)|| "".equals(currentSearchField)) {
	 				 throw new IllegalArgumentException("Search criteria should contain non-empty searchField and queryString");					
				}
	 		 	  if(currentQueryString.contains("*") || currentQueryString.startsWith(",")|| currentQueryString.startsWith("|") ){
	 		 		  throw new IllegalArgumentException("Invalid search query string. The search query string should not contain '*' AND should not start with '|' or ','");
	 		 		  } 
	 			 
			}
		}
	 	  if(searchDto.getAnalyzer() == null){
		 		 throw new IllegalArgumentException("search analyzer cannot be null.");	 		  
	 	  }
	 	  if(searchDto.getAnalyzer() == null){
		 		 throw new IllegalArgumentException("search index cannot be null.");	 		  
	 	  }
	}
	
	  public static ImdbDocumentDto transferJSONToDto(JSONObject obj){
		   ImdbDocumentDto dto = new ImdbDocumentDto();
		   try {
			   dto.setTitle(obj.getString("Title"));
			   dto.setYear(obj.getInt("Year"));
			   dto.setRated(obj.getString("Rated"));
			   dto.setReleased(obj.getString("Released"));
			   dto.setRuntime(obj.getString("Runtime"));
			   dto.setGenre(obj.getString("Genre"));
			   dto.setDirector(obj.getString("Director"));
			   dto.setWriter(obj.getString("Writer"));
			   dto.setActors(obj.getString("Actors"));
			   dto.setPlot(obj.getString("Plot"));
			   dto.setLanguage(obj.getString("Language"));
			   dto.setCountry(obj.getString("Country"));
			   dto.setAwards(obj.getString("Awards"));
			   dto.setPosterURL(obj.getString("Poster"));
			   if (isInt(obj.getString("Metascore"))) {
				dto.setMetascore(Integer.parseInt(obj.getString("Metascore")));
			   }
			   else {
				   dto.setMetascore(-1);
			   }
				if (isDouble(obj.getString("imdbRating"))) {
					dto.setImdbRating(Double.parseDouble(obj.getString("imdbRating")));
				}
				else {
					dto.setImdbRating(-1);
				}
				if (isLong(obj.getString("imdbVotes"))) {
					dto.setImdbVotes(Long.parseLong(obj.getString("imdbVotes").replace(",", "")));
				}
				else {
					dto.setImdbVotes(-1);
				}
				dto.setImdbID(obj.getString("imdbID"));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   return dto;
	   }
	
	public static boolean isInt(String str)  
	{  
	  try  
	  {  
	    Integer.parseInt(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
	
	public static boolean isLong(String str)  
	{  
	  try  
	  {  
	    Long.parseLong(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
	
	public static boolean isDouble(String str)  
	{  
	  try  
	  {  
	    Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
}
