package models;

import java.sql.Savepoint;

import com.avaje.ebean.Update;

public class Track {
	public Long	id;
	public String	artist;
	public String	title;
	public String	mediaType;
	public String	mediaTitle;
	public Long	mediaIndex;
	
	public static Track findById(Long id)
	{
		return new Track();
	}
	
	public void save()
	{
		
	}
	
	public void delete()
	{
		
	}
	
	public void update( Track newTrack) {
	}
}
