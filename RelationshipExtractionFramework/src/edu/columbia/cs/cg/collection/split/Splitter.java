package edu.columbia.cs.cg.collection.split;

import java.io.File;

import edu.columbia.cs.data.Dataset;
import edu.columbia.cs.data.Writable;

public abstract class Splitter<E extends Writable> {
	public abstract void split(Dataset<E> directory, File outputFolder);
}
