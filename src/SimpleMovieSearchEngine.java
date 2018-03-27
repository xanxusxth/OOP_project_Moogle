// Name: DUJNAPA TANUNDET
// Student ID: 6088105
// Section: 1

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SimpleMovieSearchEngine implements BaseMovieSearchEngine {
	public Map<Integer, Movie> movies;
	
	@Override
	public Map<Integer, Movie> loadMovies(String movieFilename) {
		
		// YOUR CODE GOES HERE
		try {
			BufferedReader buffread = new BufferedReader (new FileReader (movieFilename));
			movies = new HashMap<Integer, Movie>();
			String txt = ""; // for buffer to read and use this
			String regex = "([0-9]+),\"?(.*) \\(([ 0-9]+)\\)\"?,(.*)";
			Pattern chittapol = Pattern.compile(regex);
			Matcher ten;
			while ((txt = buffread.readLine()) != null) {
				
				ten = chittapol.matcher(txt);
				if(ten.matches()) {
					int mid = Integer.parseInt(ten.group(1)); //change to int from string did not reach (.)
					String title = ten.group(2);
					int year = Integer.parseInt(ten.group(3));
					String[] tags = ten.group(4).split("\\|");	//one movie has many tags
					this.movies.put(mid,new Movie(mid,title,year));
					for(String t:tags) {
						movies.get(mid).addTag(t);	
				
						/*
						 * use t instead of tags bcuz for loop original 
						 * for(int i =0; i<tags.length;i++){
						 * String t = tags[i];
						 * 
						 */
				}
		}
		}
		}
	
	catch(FileNotFoundException e){
		e.printStackTrace();
	}
	catch (NumberFormatException e) {
	
		e.printStackTrace();
	} catch (IOException e) {
		
		e.printStackTrace();
	}
			
		
		
		return movies;
	}

	@Override
	public void loadRating(String ratingFilename) {

		// YOUR CODE GOES HERE
		try {
			BufferedReader buffread = new BufferedReader (new FileReader (ratingFilename));
			String txt = ""; // for buffer to read and use this
			String regex = "([0-9]+),([0-9]+),([0-9]\\.[0-9]+),([0-9]+)";
			Pattern chittapol = Pattern.compile(regex);
			Matcher ten;
			while ((txt = buffread.readLine()) != null) {
				
				ten = chittapol.matcher(txt);
				if(ten.find()) {
					int uid = Integer.parseInt(ten.group(1)); //change to int from string did not reach (.)
					int mid = Integer.parseInt(ten.group(2));
					double rating = Double.parseDouble(ten.group(3));
					long timestamp = Long.parseLong(ten.group(4));	//one movie has many tags
					
					this.movies.get(mid).addRating(new User(uid),movies.get(mid),rating,timestamp);
					
				}
		}
		
		}
	
	catch(FileNotFoundException e){
		e.printStackTrace();
	}
	catch (NumberFormatException e) {
	
		e.printStackTrace();
	} catch (IOException e) {
		
		e.printStackTrace();
	}
			
		
		
	}

	@Override
	public void loadData(String movieFilename, String ratingFilename) {
	
		// YOUR CODE GOES HERE
		movies = loadMovies(movieFilename);
		loadRating(ratingFilename);
			
	}

	@Override
	public Map<Integer, Movie> getAllMovies() {

		// YOUR CODE GOES HERE
		
		return movies;
	}

	@Override
	public List<Movie> searchByTitle(String title, boolean exactMatch) {

		// YOUR CODE GOES HERE
		List<Movie> srcBTitle = new ArrayList<Movie>();
		for(Integer id : movies.keySet() ) 
		{
			String temporary = movies.get(id).getTitle().toLowerCase(); 
			if(temporary.contains(title)) {
				srcBTitle.add(movies.get(id));
			}
		}
		
		return srcBTitle;
	}

	@Override
	public List<Movie> searchByTag(String tag) {

		// YOUR CODE GOES HERE
		//contain คือเหมือนแค่บางส่วน แต่ถ้าเป็นequalคือต้องเป๊ะ
		List<Movie> srcBTag = new ArrayList<Movie>();
		for(Integer tagg: movies.keySet()) {
			if(movies.get(tagg).getTags().contains(tag)) {
				srcBTag.add(movies.get(tagg));
			}
			
		}
		return srcBTag;
	}

	@Override
	public List<Movie>searchByYear(int year) {

		// YOUR CODE GOES HERE
		List<Movie> srcBYear = new ArrayList<Movie>();
		for(Integer yearr: movies.keySet()) {
			if(movies.get(yearr).getYear() == year) {
				srcBYear.add(movies.get(yearr));
				
			}
		}
		
		return srcBYear;
	}

	@Override
	public List<Movie> advanceSearch(String title, String tag, int year) {

		// YOUR CODE GOES HERE
		List<Movie>  advSearch = new ArrayList<Movie>(movies.values());
		
		if(title==null && tag == null && year<0) {
			advSearch.clear();
			return advSearch;
		}
		
		
		for(Integer i : movies.keySet()) {
			Movie m = movies.get(i);
			if(title!=null && !(m.getTitle().toLowerCase().contains(title.toLowerCase()))) {
				advSearch.remove(m);
			}	
			
			if(tag!=null && !(m.getTags().contains(tag))) {
				advSearch.remove(m);
			}
			
			if(year>=0 && m.getYear()!=year) {
				advSearch.remove(m);
			}
		}
		
		
		
//		if(title == null) {
//			//tag,year
//			searchByTag(tag);
//			searchByYear(year);
//			if(advSearch.contains(tag) && advSearch == year) {
//				
//			}
//			 
//			
//			
//		}
//		else if(tag == null) {
//			//title,year
//			searchByTitle(title,false);
//			
//			
//		}
//		else if (year == -1) {
//			//title,tag
//			searchByTitle(title,false);
//			searchByTag(tag);
//			
//		}

		return advSearch;
	}
	

	@Override
	public List<Movie> sortByTitle(List<Movie> unsortedMovies, boolean asc) {

		// YOUR CODE GOES HERE
		List<Movie> sBTitle = (ArrayList<Movie>) unsortedMovies;
		sBTitle.sort(Comparator.comparing(Movie::getTitle));
		if(!asc) Collections.reverse(sBTitle); // asc = ascending
		
		
		return sBTitle;
	}

	@Override
	public List<Movie> sortByRating(List<Movie> unsortedMovies, boolean asc) {

		// YOUR CODE GOES HERE
		List<Movie> sBRating = (ArrayList<Movie>) unsortedMovies;

		sBRating.sort(Comparator.comparing(Movie::getMeanRating));
		if(!asc) Collections.reverse(sBRating); //asc = ascending 

		return sBRating;
	}

}
