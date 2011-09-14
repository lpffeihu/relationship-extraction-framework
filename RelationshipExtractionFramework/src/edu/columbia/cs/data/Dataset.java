package edu.columbia.cs.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Dataset<E> implements Iterable<E>{

	private Set<E> dataset;
	
	public Dataset(){
		dataset=new HashSet<E>();
	}
	
	public Dataset(Collection<E> list){
		dataset=new HashSet<E>();
		dataset.addAll(list);
	}
	
	public Dataset(Loader<E> l, File f, boolean isListOfFiles){
		dataset=new HashSet<E>();
		if(f.isDirectory()){//Folder to the dataset
			File[] list = f.listFiles();
			dataset.addAll(l.load(Arrays.asList(list)));
		}else if(isListOfFiles){
			dataset.addAll(l.load(getFiles(f)));
		}else{
			dataset.addAll(l.load(f));
		}
	}
	
	public void add(E input){
		dataset.add(input);
	}
	
	public void addAll(Collection<E> inputs){
		dataset.addAll(inputs);
	}
	
	public void addAll(Dataset<E> inputs){
		dataset.addAll(inputs.dataset);
	}
	
	@Override
	public Iterator<E> iterator() {
		return dataset.iterator();
	}
	
	public int size(){
		return dataset.size();
	}
	
	private static List<File> getFiles(File f){
		List<File> entries = new ArrayList<File>();

		try {
			BufferedReader in = new BufferedReader(new FileReader(f));
			String input;
			while ((input = in.readLine()) != null) {
				entries.add(new File(input));
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		return entries;
	}
}
