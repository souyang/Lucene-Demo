package com.lucene.dto;

import java.util.Date;

public class ImdbDocumentDto {
  private String title;
  private int year;
  private String rated;
  private String released;
  private String runtime;
  private String genre;
  private String director;
  private String writer;
  private String actors;
  private String plot;
  private String language;
  private String country;
  private String awards;
  private int metascore;
  private double imdbRating;
  private long imdbVotes;
  private String imdbID;
  private String posterURL;
	  
  public void setTitle(String title) {
		this.title = title;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public void setRated(String rated) {
		this.rated = rated;
	}
	public void setReleased(String released) {
		this.released = released;
	}
	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public void setActors(String actors) {
		this.actors = actors;
	}
	public void setPlot(String plot) {
		this.plot = plot;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public void setAwards(String awards) {
		this.awards = awards;
	}
	public void setMetascore(int metascore) {
		this.metascore = metascore;
	}
	public void setImdbRating(double imdbRating) {
		this.imdbRating = imdbRating;
	}
	public void setImdbVotes(long imdbVotes) {
		this.imdbVotes = imdbVotes;
	}
	public void setImdbID(String imdbID) {
		this.imdbID = imdbID;
	}
	public void setPosterURL(String posterURL) {
		this.posterURL = posterURL;
	}

  public String getTitle() {
	return title;
}
public int getYear() {
	return year;
}
public String getRated() {
	return rated;
}
public String getReleased() {
	return released;
}
public String getRuntime() {
	return runtime;
}
public String getGenre() {
	return genre;
}
public String getDirector() {
	return director;
}
public String getWriter() {
	return writer;
}
public String getActors() {
	return actors;
}
public String getPlot() {
	return plot;
}
public String getLanguage() {
	return language;
}
public String getCountry() {
	return country;
}
public String getAwards() {
	return awards;
}
public int getMetascore() {
	return metascore;
}
public double getImdbRating() {
	return imdbRating;
}
public long getImdbVotes() {
	return imdbVotes;
}
public String getImdbID() {
	return imdbID;
}
public String getPosterURL() {
	return posterURL;
}

}
