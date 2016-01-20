package com.lucene;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import com.lucene.dto.ImdbDocumentDto;
import com.lucene.dto.SearchCriteria;
import com.lucene.dto.SearchMultipleFieldsDto;
import com.lucene.dto.SearchSingleFieldDto;

import org.json.*;
/**
 * @author simon
 *Tis is a demonstration of using Lucene for search
 *
 */
public class LuceneDemo {
	private static final String FILE_NAME_PATH = "src/main/resources/data.json";
	private static final int HITS_PER_PAGE = 10;
	private static StandardAnalyzer analyzer;
	private static Directory index;
	private static IndexWriterConfig config;
	
	private String jsonfileName;
    public LuceneDemo(String queueName) {
    	this.jsonfileName = queueName;
    }
		
	public static void main(String[] args)  throws IOException, ParseException{	
	  init();
      ArrayList<ImdbDocumentDto> documents = new ArrayList<ImdbDocumentDto>();
      LuceneDemo lucene = new LuceneDemo(FILE_NAME_PATH);
      String str =  new String(Files.readAllBytes(Paths.get(lucene.jsonfileName))); 
      lucene.transformJsonFileToImdbDto(documents, str);
	  initIndexWriter(index,config,documents);
	  
	   /* Try to find out the movie with title start with Ocean
	   * 
	   * */	  
	  String keywords = "Ocean";// Not exact Match
	  SearchSingleFieldDto searchDto = new SearchSingleFieldDto();
	  searchDto.setAnalyzer(analyzer);
	  searchDto.setIndex(index);
	  SearchCriteria searchCriteriaA = new SearchCriteria(); 
	  searchCriteriaA.setExactMatch(false);
	  searchCriteriaA.setSearchField("title");
	  searchCriteriaA.setQueryString(keywords);
	  searchDto.setSearchCriteria(searchCriteriaA);
	  lucene.searchBySingleCriteria(searchDto);
	  System.out.println();
	   // Try to find out the movie with title start with Ocean or Toy	     
	  keywords = "Ocean|Toy"; //OR condition with Exact Match
	  searchDto = new SearchSingleFieldDto();
	  searchDto.setAnalyzer(analyzer);
	  searchDto.setIndex(index);
	  SearchCriteria searchCriteriaB = new SearchCriteria(); 
	  searchCriteriaB.setExactMatch(false);
	  searchCriteriaB.setSearchField("title");
	  searchCriteriaB.setQueryString(keywords);
	  searchDto.setSearchCriteria(searchCriteriaB);
	  lucene.searchBySingleCriteria(searchDto);
	  System.out.println();
	
	   //Try to find out the file with title containing 12 or years	   
	  keywords = "12,Years"; //AND condition with Exact Match
	  searchDto = new SearchSingleFieldDto();
	  searchDto.setAnalyzer(analyzer);
	  searchDto.setIndex(index);
	  SearchCriteria searchCriteriaC = new SearchCriteria(); 
	  searchCriteriaC.setExactMatch(true);
	  searchCriteriaC.setSearchField("title");
	  searchCriteriaC.setQueryString(keywords);
	  searchDto.setSearchCriteria(searchCriteriaC);
	  lucene.searchBySingleCriteria(searchDto);

	  // Multi-Fields Search
	  // Try to find out the film with title start with "Star Wars" or "New Hope" 
	  // and Plot contains "Jedi" and "Skywalker" and Director contains George Lucas
	  
	  String keywordsForPlot = "Skywalker,Jedi"; //AND condition
	  String keywordsForTitle = "Star Wars|New Hope";//OR condition
	  String keywordsForDirector = "George Lucas";
	  SearchMultipleFieldsDto searchMultiFieldsDto = new SearchMultipleFieldsDto();
	  searchMultiFieldsDto.setAnalyzer(analyzer);
	  searchMultiFieldsDto.setIndex(index);
	  ArrayList<SearchCriteria> list = new ArrayList<SearchCriteria>();
	 list.add(new SearchCriteria("plot",keywordsForPlot,true));
	 list.add(new SearchCriteria("title",keywordsForTitle,false));
	 list.add(new SearchCriteria("director",keywordsForDirector,true));
	 searchMultiFieldsDto.setSearchCriteriaList(list); 
	 lucene.searchByMultipleCriteria(searchMultiFieldsDto);
	  
	  String keywordsForCountry = "USA|Canada|France";
	  String keywordsForGenre = "Drama";
	  String keywordsForAwards = "Won|Oscars";
	  SearchMultipleFieldsDto searchMultiFieldsDto2 = new SearchMultipleFieldsDto();
	  searchMultiFieldsDto2.setAnalyzer(analyzer);
	  searchMultiFieldsDto2.setIndex(index);
	  ArrayList<SearchCriteria> list2 = new ArrayList<SearchCriteria>();
	 list2.add(new SearchCriteria("country",keywordsForCountry,true));
	 list2.add(new SearchCriteria("genre",keywordsForGenre,true));
	 list2.add(new SearchCriteria("awards",keywordsForAwards,false));
	 searchMultiFieldsDto2.setSearchCriteriaList(list2); 
	 lucene.searchByMultipleCriteria(searchMultiFieldsDto2);
  }

	/**
	 * This method is create analyzer, index and IndexWriteConfig
	 */
	private static void init()
	{
		  analyzer = new StandardAnalyzer();
		  index = new RAMDirectory();
		  config = new IndexWriterConfig(analyzer);
	}

	private void transformJsonFileToImdbDto(ArrayList<ImdbDocumentDto> documents, String str) {
		JSONObject json = new JSONObject(str);
		  JSONArray getArray = json.getJSONArray("Search");
		  for(int i=0; i< getArray.length(); i++)
		  {
			  JSONObject object = getArray.getJSONObject(i);
			  documents.add(LuceneUtil.transferJSONToDto(object));
		  }
	}
   
   	
   public void searchBySingleCriteria(SearchSingleFieldDto searchDto)
   {
	   LuceneUtil.validateSearchDto(searchDto); 	  
 	  
 	 String queryString  = LuceneUtil.formatQueryString(searchDto.getSearchCriteria());
 	 System.out.println("queryString built is: \n" + queryString);
	   try { 
			Query query = new QueryParser(searchDto.getSearchCriteria().getSearchField(), searchDto.getAnalyzer()).parse(queryString);
			searchResult(searchDto.getIndex(), query);
	   } 
	   catch (Exception e) {
		e.printStackTrace();
	}
   }
   
   public void searchByMultipleCriteria(SearchMultipleFieldsDto searchDto)
   {
	   LuceneUtil.validateSearchMultipleFieldsDto(searchDto);
 	  	  
	  String queryString = LuceneUtil.formatQueryString(searchDto.getSearchCriteriaList());
	  System.out.println("queryString is: \n" + queryString);
	   try { 
		Query query = new QueryParser(LuceneUtil.getDefaultSearchField(), searchDto.getAnalyzer()).parse(queryString);
		searchResult(searchDto.getIndex(), query);
	   } 
	   catch (Exception e) {
		// TODO: handle exception
	}
   }

	/**
	 * Search Result with given index and query
	 * @param index
	 * @param query
	 * @return
	 * @throws IOException
	 */
	private static void searchResult(Directory index, Query query) throws IOException {
		StringBuilder sb = new StringBuilder();
		IndexReader reader = DirectoryReader.open(index);
		  IndexSearcher searcher = new IndexSearcher(reader);
		  TopDocs docs = searcher.search(query, HITS_PER_PAGE);
		  ScoreDoc[] hits = docs.scoreDocs;
		  // display results
		  System.out.println("Found " + hits.length + " hits.");
		  // display header
		  System.out.println(sb.append("Index").append("\t")
				  				.append("Title").append("\t").append("\t")
				  				.append("IMDb URL").append("\t")
				  				.toString());		  						
		  for(int i=0;i<hits.length;++i) {
		      int docId = hits[i].doc;
		      Document d = searcher.doc(docId);
		      sb.setLength(0);
		      System.out.println(sb.append((i + 1)).append( ".").append("\t").
		    		  append(d.get("title")).append("\t").
		    		  append("http://www.imdb.com/title/").
		    		  append(d.get("imdbID")).append("/").toString());
		    		  
		  }
	      // reader can only be closed when there
	      // is no need to access the documents any more.
		  reader.close();
	}
  

  /**
   *  Initialize the indexWriter 
 * @param index
 * @param config
 * @return
 * @throws IOException
 */
private static void initIndexWriter(Directory index, IndexWriterConfig config, ArrayList<ImdbDocumentDto> dtos) throws IOException {
	  IndexWriter w;
	  try {
		w = new IndexWriter(index, config);
		for(int i = 0; i < dtos.size(); i++){
			addDoc(w, dtos.get(i));
		}
		w.close();
	} catch (IOException e) {
		e.printStackTrace();
		throw e;
	} 
  }


/**
 * This method is to set the fields for the index for search purpose.
 * @param w
 * @param dto
 * @throws IOException
 */
private static void addDoc(IndexWriter w, ImdbDocumentDto dto) throws IOException {
	  Document doc = new Document();
	  doc.add(new TextField("title", dto.getTitle(), Store.YES));
	  doc.add(new IntField("year", dto.getYear() , Store.YES));
	  doc.add(new StringField("rated", dto.getRated(), Store.YES));
	  doc.add(new TextField("released", dto.getReleased(), Store.YES));
	  doc.add(new StringField("runtime", dto.getRuntime(),Store.YES)); //need further parsing
	  doc.add(new TextField("genre", dto.getGenre(), Store.YES));
	  doc.add(new TextField("director", dto.getDirector(), Store.YES));
	  doc.add(new TextField("writer", dto.getWriter(), Store.YES));
	  doc.add(new TextField("actors", dto.getActors(), Store.YES));
	  doc.add(new TextField("plot", dto.getPlot(), Store.YES));
	  doc.add(new TextField("language", dto.getLanguage(), Store.YES));	
	  doc.add(new TextField("country", dto.getCountry(), Store.YES));	
	  doc.add(new TextField("awards", dto.getAwards(), Store.YES));
	  doc.add(new IntField("metascore", dto.getMetascore(), Store.YES));
	  doc.add(new StringField("imdbID", dto.getImdbID(), Store.YES));
	  doc.add(new LongField("imdbVotes",dto.getImdbVotes(), Store.YES));
	  doc.add(new DoubleField("imdbRating", dto.getImdbRating(), Store.YES));
	  w.addDocument(doc);
	}	

}
