
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import models.Track;
import models.TrackFactory;
import utils.CassandraClient;

public class Import 
{
	private CassandraClient client;
	private TrackFactory factory;
	
	public static void main(String[] args) 
	{
		Import importer = new Import();
		importer.init();
		importer.diagnostics();
		importer.createSchema();
		importer.load_singles();
		importer.load_albums();
	    for (Track track : importer.factory.findSome())
	    {
			System.out.printf("Track %s\n", track ); 
	    }
		importer.done();
	}

	private void init()
	{
		this.client = CassandraClient.get();
		this.client.connect("127.0.0.1");
		this.factory = new TrackFactory();
	}

	private void done()
	{
		this.client.close();
		this.client = null;
	}

	private void createSchema() 
	{
		String keyspace = "play_cassandra";

		client.getSession().execute("DROP KEYSPACE " + keyspace);
			   System.out.println("Finished dropping " + keyspace + " keyspace.");

		client.getSession().execute("CREATE KEYSPACE " + keyspace + " WITH replication " + 
			      "= {'class':'SimpleStrategy', 'replication_factor':3};");

		client.getSession().execute(
			      "CREATE TABLE " + keyspace + ".tracks (" +
			            "id UUID PRIMARY KEY," + 
			            "artist text," + 
			            "title text," + 
			            "mediaType text," + 
			            "mediaTitle text," + 
			            "mediaIndex int," + 
			            ");");
	}

	private static String unquote(String s)
	{
		if (s != null
			   && ((s.startsWith("\"") && s.endsWith("\""))
			   || (s.startsWith("'") && s.endsWith("'")))) 
		{
			  s = s.substring(1, s.length() - 1);
		}
		return s;		
	}
	
	private void load_singles()
	{
		int count = 0;
		// open singles.csv
		// 4 columns SortKey, Artist, ASide, B Side.
		try(BufferedReader br = new BufferedReader(new FileReader("singles.csv"))) {
		    for(String line; (line = br.readLine()) != null; ) {
		        String[] parts = line.split(",");
				factory.create(unquote(parts[1]), unquote(parts[2]),"single", unquote(parts[2]), 1).save();
				factory.create(unquote(parts[1]), unquote(parts[3]),"single", unquote(parts[2]), 2).save();
				count++;
		    }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		System.out.printf("Loaded %s singles\n", count ); 
	}

	private void load_albums()
	{
		
	}
	
	private void diagnostics() 
	{
		this.client.diagnostics();
	}

}
