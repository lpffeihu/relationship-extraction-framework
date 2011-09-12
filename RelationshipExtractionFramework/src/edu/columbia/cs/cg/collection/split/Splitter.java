package edu.columbia.cs.cg.collection.split;

import java.io.File;

public abstract class Splitter {
	
	public void split(File directory, File outputFolder){
		//TODO: this calls the other split method
	}
	
	public abstract void split(File[] collection, File outputFolder);
}
