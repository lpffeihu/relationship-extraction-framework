package edu.columbia.cs.cg.document.tagger.entity.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.aliasi.chunk.Chunk;
import com.aliasi.chunk.Chunking;
import com.aliasi.dict.DictionaryEntry;
import com.aliasi.dict.ExactDictionaryChunker;
import com.aliasi.dict.MapDictionary;
import com.aliasi.tokenizer.TokenizerFactory;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.Segment;
import edu.columbia.cs.cg.document.tagger.entity.EntityTagger;
import edu.columbia.cs.cg.document.tokenized.tokenizer.Tokenizer;
import edu.columbia.cs.utils.Dictionary;

public class DictionaryBasedEntityTagger extends EntityTagger {

	private static final double CHUNK_SCORE = 1.0;
	private Map<String,String> regExTable;
	private int processedDocuments;
	private MapDictionary<String> dictionary;
	private ExactDictionaryChunker matcher;
	private Tokenizer tokenizer;

	public DictionaryBasedEntityTagger(String tag, Dictionary dictionary, Tokenizer tokenizer){
		super(tag);
		createMatchingDictionary(dictionary);
		regExTable = new HashMap<String, String>();
		processedDocuments = 0;
		this.tokenizer = tokenizer;
	}

	private void createMatchingDictionary(Dictionary dict) {
		
		dictionary = new MapDictionary<String>();
		
		Set<String> entries = dict.getEntries();

		for (String entry : entries) {
			
			dictionary.addEntry(new DictionaryEntry<String>(entry,getTag(),CHUNK_SCORE));
			
		}
	
		matcher = new ExactDictionaryChunker(dictionary,getTokenizerFactory(),
                                     true,true);
	}

	private TokenizerFactory getTokenizerFactory() {
		
		return InstanceBasedTokenizedFactory.getInstance(tokenizer);
		
	}

	@Override
	protected List<EntitySpan> findSpans(Document d) {
		
		processedDocuments++;
		
		int docId = processedDocuments;
		
		List<EntitySpan> entitySpans = new ArrayList<EntitySpan>();
		
		int matches = 0;
		
		for (Segment segment : d.getPlainText()) {
			
			String content = segment.getValue();
			
			int offset = segment.getOffset();
			
			Chunking chunking = matcher.chunk(content);
	        
			for (Chunk chunk : chunking.chunkSet()) {
	            
				matches++;
				
	        	int start = chunk.start();
	            
	        	int end = chunk.end();
	            
	        	String value = content.substring(start,end);
	            
	            entitySpans.add(new EntitySpan(createId(processedDocuments,matches,chunk.type()), value, offset + start, value.length()));
	        
			}
					
		}
		
		return entitySpans;
		
	}

	private String createId(int processedDocument, int matches, String type) {
		
		return processedDocument + "-" + matches + "-" + type;
		
	}


}
