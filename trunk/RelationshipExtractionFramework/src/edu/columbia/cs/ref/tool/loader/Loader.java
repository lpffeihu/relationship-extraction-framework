package edu.columbia.cs.ref.tool.loader;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import edu.columbia.cs.ref.model.Document;

public abstract class Loader<E> {
	
	public Set<E> load(Collection<File> files){
		Set<E> documents = new HashSet<E>();
		for(File f : files){
			documents.addAll(load(f));
		}
		return documents;
	}
	
	public abstract Set<E> load(File files);
}