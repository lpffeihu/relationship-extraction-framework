package edu.columbia.cs.cg.prdualrank.index;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.saxon.query.QueryParser;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import edu.columbia.cs.cg.document.TokenizedDocument;
import edu.columbia.cs.cg.prdualrank.index.analyzer.TokenBasedAnalyzer;
import edu.columbia.cs.cg.prdualrank.index.reader.TokenBasedReader;
import edu.columbia.cs.cg.prdualrank.index.tokenizer.SpanBasedTokenizer;
import edu.columbia.cs.cg.prdualrank.searchengine.querygenerator.impl.LuceneQueryGenerator;

public class Index {

	private static final String PATH = "path";
	private static final String FILE_NAME = "filename";
	public static final String CONTENT = "content";
	
	private IndexWriter indexWriter;
	private RAMDirectory idx;
	private TokenBasedAnalyzer myAnalyzer;
	private boolean lowercase;
	private Set<String> stopWords;
	private IndexSearcher IndexSearcher;
	private Map<String, TokenizedDocument> documentMap;

	public Index(TokenBasedAnalyzer myAnalyzer,boolean lowercase, Set<String> stopWords) {

		//Index Creation

		this.lowercase = lowercase;
		
		this.stopWords = stopWords;
		
		this.myAnalyzer = myAnalyzer;
		
		idx = new RAMDirectory();

		IndexWriterConfig indexWriterConfiguration = new IndexWriterConfig(Version.LUCENE_34, myAnalyzer);

		indexWriter = null;

		try {

			indexWriter = new IndexWriter(idx,indexWriterConfiguration);

		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void addDocument(TokenizedDocument document) {
		
		try {
			indexWriter.addDocument(createDocument(document),myAnalyzer);
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private Document createDocument(TokenizedDocument document) {
		
		Document doc = new Document();
		
		doc.add(new Field(PATH,document.getPath(),Store.YES,org.apache.lucene.document.Field.Index.NO));
		
		doc.add(new Field(FILE_NAME,document.getFilename(),Store.YES,org.apache.lucene.document.Field.Index.NO));
		
		doc.add(new Field(CONTENT,myAnalyzer.getReader(document.getTokenizedSpans(),document.getTokenizedString())));
		
		save(createKey(document.getPath(),document.getFilename()),document);
		
		return doc;
		
	}

	private void save(String key, TokenizedDocument document) {
		
		getDocumentTable().put(key,document);
		
	}

	private Map<String,TokenizedDocument> getDocumentTable() {
		
		if (documentMap == null){
			documentMap = new HashMap<String, TokenizedDocument>();
		}
		return documentMap;
	}

	private String createKey(String path, String fileName) {
		return path+"-"+fileName;
	}

	public void close() {
		
		try {
			indexWriter.optimize();
			
			indexWriter.close();
			
			IndexSearcher = new IndexSearcher(idx);
			
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	public List<edu.columbia.cs.cg.document.TokenizedDocument> search(Query query, int n) {

		TopDocs result = null;
		
		try {
			result = IndexSearcher.search(query, n);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<TokenizedDocument> searchResults = new ArrayList<TokenizedDocument>();
		
		ScoreDoc[] docs = result.scoreDocs;
		
		for (int i = 0; i < docs.length; i++) {
			
			Document document;
			try {
				document = IndexSearcher.doc(docs[i].doc);
				searchResults.add(getDocumentTable().get(createKey(document.get(PATH), document.get(FILE_NAME))));
			} catch (CorruptIndexException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		}
		
		return searchResults;
		
	}

}
