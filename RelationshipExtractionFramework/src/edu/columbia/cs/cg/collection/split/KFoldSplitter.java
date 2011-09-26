package edu.columbia.cs.cg.collection.split;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.columbia.cs.data.Dataset;
import edu.columbia.cs.data.Writable;

public class KFoldSplitter<E extends Writable> extends Splitter<E> {

	private int numberSplits;

	public KFoldSplitter(int numberSplits){
		this.numberSplits=numberSplits;
	}

	@Override
	public void split(Dataset<E> dataset, File outputFolder) {
		List<E> listE = new ArrayList<E>();
		for(E element : dataset){
			listE.add(element);
		}
		Collections.shuffle(listE);

		List<E>[] buckets = new List[numberSplits];
		for(int i=0; i<numberSplits; i++){
			buckets[i]=new ArrayList<E>();
		}

		int currentBucket=0;
		for(E element : listE){
			buckets[currentBucket].add(element);
			currentBucket=(currentBucket+1)%numberSplits;
		}

		for(int i=0; i<numberSplits; i++){
			List<E> trainingData = new ArrayList<E>();
			List<E> testingData = new ArrayList<E>();
			testingData.addAll(buckets[i]);
			for(int j=0; j<numberSplits; j++){
				if(j!=i){
					trainingData.addAll(buckets[j]);
				}
			}

			System.out.println("Split " + i);
			System.out.println("Training docs: " + trainingData.size());
			System.out.println("Testing docs: " + testingData.size());
			writeFile(trainingData, outputFolder, "/train-"+i);
			writeFile(testingData, outputFolder, "/test-"+i);
		}

	}

	public void writeFile(List<E> data, File directory, String file){
		try {	
			// Create file 
			FileWriter fstream;

			fstream = new FileWriter(directory.getAbsolutePath()+file);

			BufferedWriter out = new BufferedWriter(fstream);
			for(E s : data){
				out.write(s.getWritableValue() + "\n");
			}
			//Close the output stream
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
	}
}
