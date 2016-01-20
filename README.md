# LuceneDemo
This is a demostration of Lucene implementation to search the movie information (title and imdb page URL) using combined complex search criteria.

## Basic Concepts
Lucene is a full-text search library in Java which makes it easy to add search functionality to an application or website. It is integrated in many projects including Solr and Elastic Search.

## Underline algorithm

### Inverted index
Lucene is able to achieve fast search responses because, it searches an index called <strong>inverted index</strong> rather than search the text directly. The real world analogy is retrieving page numbers in a book related to a keyword by searching the index at the back of a book as opposed to searching the words in each page of the book. The index used by Lucene is called inverted index. It inverts a page-centric data structure (page->words) to a keyword-centric data structure (word->pages).

### Documents and Fields
An index includes one ore more documents. A document incldues one ore more fields. Ad document is a unit of index and search. A Field is a simple key-value pair. Depending on the type of key in the field, a valid Lucene Field could be TextField, StringField, IntField, DoubleField, LongField and etc.

### Indexing and Searching
Indexing involves adding Documents with one or more fields to an IndexWriter. Searching involves retrieving Documents from an existing index via an IndexSearcher with an exsiting query. The query could be created via queryParser.parse(querystring). querystring is string written following Lucene query syntax. Detail could be found in https://lucene.apache.org/core/2_9_4/queryparsersyntax.html

## usage in the program.

### "AND" condition and "OR" condition
In my program, comma stands for AND and pipe stands OR. Therefore "Skywalker,Jedi" is equivalent to keyword contains "Skywalker" AND "Jedi". "Star Wars|New Hope" is equivalent to keyword contains "Star Wars" OR "New Hope"

### Data resource
In my program the data is from http://www.omdbapi.com/

### Sample useage of code
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
	 lucene.searchByMultipleCriteria(searchMultiFieldsDto)   	 
#### Result in the console:      
queryString is:        
plot:"Skywalker" AND plot:"Jedi" AND title:Star Wars* OR title:New Hope* AND director:"George Lucas"    
Found 1 hits.
Index	Title		IMDb-URL	
1.	Star Wars: Episode IV - A New Hope	http://www.imdb.com/title/tt0076759/	
 
	 	// Single-Field Search
	 	// Try to find out the movie with title start with Ocean
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
#### Result in the console:    
queryString is:    title:Ocean*		    
Found 3 hits.      
Index	Title		IMDb-URL	
1.	Ocean's Twelve	http://www.imdb.com/title/tt0349903/  
2.	Ocean's Thirteen	http://www.imdb.com/title/tt0496806/  
3.	Ocean's Eleven	http://www.imdb.com/title/tt0240772/  

