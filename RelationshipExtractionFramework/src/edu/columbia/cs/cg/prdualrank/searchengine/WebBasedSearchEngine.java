package edu.columbia.cs.cg.prdualrank.searchengine;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.loaders.DocumentLoader;
import edu.columbia.cs.cg.document.loaders.impl.RawDocumentLoader;
import edu.columbia.cs.cg.document.preprocessing.Preprocessor;

public abstract class WebBasedSearchEngine implements SearchEngine {

	private RawDocumentLoader loader;

	public WebBasedSearchEngine(RawDocumentLoader loader){
		this.loader = loader;
	}
	
	@Override
	public List<Document> search(String query, int k_seed) {
		
		int start = 0;
		
		List<Thread> threadPool = new ArrayList<Thread>();
		
		URL[] documentResults = new URL[k_seed];
		
		while (start < k_seed){
			
			int count = getNumberOfResultsPerThread();
			
			executeQuery(threadPool,getRunnableQueryExecutor(query,start,count,documentResults));
			
			start += count;
		
		}
		
		for (Thread thread : threadPool) {
			
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		String[] documentsContent = new String[k_seed];
		
		List<Thread> downloadersPool = new ArrayList<Thread>();
		
		for (int i = 0; i < documentResults.length; i++) {
			
			Thread t = new Thread(new Downloader(documentResults[i],i,documentsContent));
			
			t.start();
			
			downloadersPool.add(t);
			
		}
		
		for (Thread thread : downloadersPool) {
			
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		List<Document> ret = new ArrayList<Document>();
		
		for (String document : documentsContent) {
			
			if (document != null)
				ret.add(loader.load(document));
			
		}
		
		return ret;
	}

	private void executeQuery(List<Thread> threadPool,Runnable runnableQueryExecutor) {
		
		Thread t = new Thread(runnableQueryExecutor);
		
		t.start();
		
		threadPool.add(t);
		
	}

	protected abstract Runnable getRunnableQueryExecutor(String query, int start, int count, URL[] documentResults);

	protected abstract int getNumberOfResultsPerThread();
	
}
