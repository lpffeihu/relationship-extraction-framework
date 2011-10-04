package edu.columbia.cs.cg.document.tagger.entity.impl;

import java.io.File;
import java.util.List;
import java.util.Set;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.Segment;
import edu.columbia.cs.cg.document.tagger.entity.EntityTagger;
import edu.columbia.cs.utils.Dictionary;

public class DictionaryBasedEntityTagger extends EntityTagger {

	private Dictionary dictionary;

	public DictionaryBasedEntityTagger(String tag, Dictionary dictionary){
		super(tag);
		this.dictionary = dictionary;
	}

	@Override
	protected List<EntitySpan> findSpans(Document d) {
		
		Set<String> entries = dictionary.getEntries();
		
		for (Segment segment : d.getPlainText()) {
			
			String content = segment.getValue();
			
			int offset = segment.getOffset();
			
			for (String string : entries) {
				
				
				
			}
			
		}
		
	}

}
