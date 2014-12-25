package com.ankitpatil.movienamematcher;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.shingle.ShingleAnalyzerWrapper;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class MatchingMain {
	
	final static String INDEX_LOC="\\index\\";												// Index file location
	static String str[]= {"The Lord of the Rings: The Fellowship of the Ring", 				// Movie names to be indexed
		"The Lord of the Rings: The Two Towers",
		"The Lord of the Rings: The Return of the King",
		"The Lord of the Rings",
		"The Rings of the Lords"};
	
	public static void main(String[] args) {
		
		try {
			createIndex();																		// Index creation
			
			System.out.println(getMatch("lords ring fellowship"));								// Few test cases
			System.out.println(getMatch("lord rings towers"));
			System.out.println(getMatch("lord ring kings return"));
			System.out.println(getMatch("lord ring"));
			System.out.println(getMatch("lord of ring the"));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void createIndex() throws IOException{										// Creates indexes at specified location

		Analyzer analyzer = new ShingleAnalyzerWrapper(Version.LUCENE_41,3,3);					// Shingle Analyzer with min and max shingle size 3
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_41, analyzer);
		iwc.setOpenMode(OpenMode.CREATE);														// Always overwrite old indexes
		IndexWriter writer = new IndexWriter(FSDirectory.open(new File(INDEX_LOC)), iwc);
		
		for(int i=0;i<str.length;i++){
			Document document = new Document();
			document.add(new StringField("id",String.valueOf(i), Field.Store.YES));				// id unique identifier for movie name, stored as it is
			document.add(new TextField("name",str[i], Field.Store.YES));						// movie name: text field, divided into tokens
			
			writer.addDocument(document);
		}
		
		writer.commit();
		writer.close();
	}
	
	public static Integer getMatch(String queryString) throws IOException, ParseException{		// search index and return results
		
	    IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(INDEX_LOC)));
	    IndexSearcher searcher = new IndexSearcher(reader);
	    Analyzer analyzer = new ShingleAnalyzerWrapper(Version.LUCENE_41,3,3);
	    	    
	    QueryParser parser = new QueryParser(Version.LUCENE_41, "name", analyzer);
	    Query query = parser.parse(queryString);
	    
	    TopDocs results = searcher.search(query, 1);											// get top 1 result
	    Integer id=null;
	    
	    for ( ScoreDoc scoreDoc : results.scoreDocs ) {
	        Document doc = searcher.doc( scoreDoc.doc );
	        id=Integer.parseInt(doc.get("id"));
	        System.out.println(doc.get("name"));
	    }
	    
	    return id;
	}

}
