package edu.columbia.cs.cg.sentence;

import edu.columbia.cs.cg.document.Document;

public interface SentenceSplitter {
	public Sentence[] split(Document d);
}
