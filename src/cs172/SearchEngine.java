package cs172;
import java.io.*;
import java.util.StringTokenizer;
import java.util.Scanner;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;


class TweetInfo {
	public String date;
	public String user;
	public String text;
	public String coordinates;
	public String link;
	public String hashtags;
	public String title;
	
	TweetInfo(String dt, String coords, String use, String tex, String hash, String url, String t) {
		this.date = dt;
		this.coordinates = coords;
		this.user = use;
		this.text = tex;
		this.hashtags = hash;
		this.link = url;
		this.title = t;
	}
	
}

public class SearchEngine 
{
    public static void index (TweetInfo text) 
    {
    	IndexWriter write = null;
    	File index = new File("Index");
    	try 
    	{	
			IndexWriterConfig indexConfig = new IndexWriterConfig(Version.LUCENE_34, new StandardAnalyzer(Version.LUCENE_35));
			write = new IndexWriter(FSDirectory.open(index), indexConfig);
			Document IndexTweets = new Document();
			IndexTweets.add(new Field("date", text.date, Field.Store.YES, Field.Index.NO));
			IndexTweets.add(new Field("coordinates", text.coordinates, Field.Store.YES, Field.Index.NO));
			IndexTweets.add(new Field("user", text.user, Field.Store.YES, Field.Index.NO));
			IndexTweets.add(new Field("hashtags", text.hashtags, Field.Store.YES, Field.Index.ANALYZED));
			IndexTweets.add(new Field("text", text.text, Field.Store.YES, Field.Index.ANALYZED));
			IndexTweets.add(new Field("title", text.title, Field.Store.YES, Field.Index.ANALYZED));
			IndexTweets.add(new Field("url", text.link, Field.Store.YES, Field.Index.NO));			
			write.addDocument(IndexTweets);			
		} 
    	catch (Exception ex) 
    	{
			ex.printStackTrace();
		} 
    	finally 
    	{
			if (write != null)
				try 
				{
					write.close();
				} 
				catch (CorruptIndexException exception) 
				{
					exception.printStackTrace();
				} 
				catch (IOException exception) 
				{
					exception.printStackTrace();
				}
		}
    
    }
    
    public static String[] search (String keyword, int topk) throws CorruptIndexException, IOException 
    {
		
		IndexReader read = IndexReader.open(FSDirectory.open(new File("Index")));
		IndexSearcher search = new IndexSearcher(read);
		QueryParser key = new QueryParser(Version.LUCENE_34, "text", new StandardAnalyzer(Version.LUCENE_34));

		try 
		{
			StringTokenizer strtok = new StringTokenizer(keyword, " ~`!@#$%^&*()_-+={[}]|:;'<>,./?\"\'\\/\n\t\b\f\r");
			String keys = "";
			while(strtok.hasMoreElements()) 
			{
				String token = strtok.nextToken();
				keys = keys + "text:" + token + "^1" + "hashtags:" + token+ "^1.5" + "ptitle:" + token+"^2.0";
			}		
			Query q = key.parse(keys);
			TopDocs output = search.search(q, topk);
			String[] tweets = new String[output.scoreDocs.length];
			for (int i = 0; i < output.scoreDocs.length; ++i) 
			{
				String date = search.doc(output.scoreDocs[i].doc).getFieldable("date").stringValue();
				date = date.replace("+0000", "");
				String t = "@" + search.doc(output.scoreDocs[i].doc).getFieldable("user").stringValue();
				t = t + ": " + search.doc(output.scoreDocs[i].doc).getFieldable("text").stringValue();
				t = t + "<br/>" + date + "    Score: " +  output.scoreDocs[i].score;;
				System.out.println(search.doc(output.scoreDocs[i].doc).getFieldable("text").stringValue());
				System.out.println("score: " + output.scoreDocs[i].score);
				tweets[i] = t;
			}
			return tweets;			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
		finally 
		{
			search.close();
		}
		return null;
	}
    
    public static void main(String[] args) throws CorruptIndexException, IOException 
    {	
		if (args.length == 0) 
		{
			int count = 0;
			BufferedReader reader = null;	
        try 
        {
        	File f = new File("output.txt");
        	while (f.exists() == true) 
        	{
	            reader = new BufferedReader(new FileReader(f));
	            for (String j; (j = reader.readLine()) != null;) 
	            {
	                Scanner scan = new Scanner(j).useDelimiter("\\s*Date:\\s*|\\s*Coordinates:\\s*|\\s*User:\\s*|\\s*Text:\\s*|\\s*Hashtags:\\s*|\\s*URL:\\s*|\\s*Title:\\s*");
	                String date = scan.next();
	                String coordinates = scan.next();
	                String user = scan.next();
	                String text = scan.next();
	                String hashtags = scan.next();
	                String url = scan.next();
	                String title = "";
	                if (scan.hasNext() == true) 
	                {
	                	title = scan.next();
	                }
	                System.out.println(text);
	                TweetInfo tweepy = new TweetInfo(date, coordinates, user, text, hashtags, url, title); 
	                index(tweepy);
	            }
	            reader.close();
	            f = new File("output.txt");
            
        	}
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        finally 
        {
            try 
            {
                reader.close();
                search("hi",5);
            }
            catch (IOException e) 
            {
                e.printStackTrace();
            }

        } 
        
		}	
		
	}
    
}
   
    
    
    
    
  

