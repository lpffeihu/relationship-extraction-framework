package edu.columbia.cs.cg.document.tagger.entity.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.Segment;
import edu.columbia.cs.cg.document.tagger.entity.EntityTagger;
import edu.columbia.cs.utils.Dictionary;

public class DictionaryBasedEntityTagger extends EntityTagger {

	private Dictionary dictionary;
	private Map<String,String> regExTable;
	private int processedDocuments;

	public DictionaryBasedEntityTagger(String tag, Dictionary dictionary){
		super(tag);
		this.dictionary = dictionary;
		regExTable = new HashMap<String, String>();
		processedDocuments = 0;
	}

	@Override
	protected List<EntitySpan> findSpans(Document d) {
		
		processedDocuments++;
		
		int docId = processedDocuments;
		
		List<EntitySpan> entitySpans = new ArrayList<EntitySpan>();
		
		Set<String> keys = dictionary.getKeys();
		
		for (Segment segment : d.getPlainText()) {
			
			String content = segment.getValue();
			
			int offset = segment.getOffset();
			
			int entityKeyIndex = 1;
			
			for (String key : keys) {
				
				Pattern pattern = Pattern.compile("(\\W|^)" + "(" + getRegularExpression(key,dictionary.getAliases(key)) + ")" + "(\\W|$)");
				
				Matcher matcher = pattern.matcher(content);
				
				int occurrence = 0;
				
				while (matcher.find()) {
					
					occurrence++;
					
					int entityOffset = offset + matcher.start(2);
					
					String value = matcher.group(2);
					
					entitySpans.add(new EntitySpan(createId(docId,entityKeyIndex,occurrence), value, entityOffset, matcher.group(2).length()));
					
				}
				
			}
			
		}
		
		return entitySpans;
		
	}

	private String createId(int docId, int entityKeyIndex, int occurrence) {
		return docId + "-" + entityKeyIndex + "-" + occurrence;
	}

	private String getRegularExpression(String key,Set<String> aliases) {
		
		String regEx = regExTable.get(key);
		
		if (regEx == null){
			regEx = createEitherRegularExpression(aliases);
			regExTable.put(key,regEx);
		}
		return regEx;
	}

	private String createEitherRegularExpression(Set<String> aliases) {
		
		StringBuilder sb = new StringBuilder();
		
		for (String alias : aliases) {
			
			sb.append("|" +Pattern.quote(alias));
			
		}
		
		return sb.substring(1);
	}

}
