package edu.columbia.cs.cg.prdualrank.searchengine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.davidsoergel.conja.Function;
import com.davidsoergel.conja.Parallel;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.loaders.DocumentLoader;
import edu.columbia.cs.cg.document.loaders.impl.RawDocumentLoader;
import edu.columbia.cs.cg.document.preprocessing.Preprocessor;
import edu.columbia.cs.cg.prdualrank.searchengine.downloader.Downloader;

public abstract class WebBasedSearchEngine implements SearchEngine {

	protected static final int ATTEMPTS = 3;

	private RawDocumentLoader loader;

	public WebBasedSearchEngine(RawDocumentLoader loader){
		this.loader = loader;
	}
	
	@Override
	public List<Document> search(String query, int k_seed) {
		
		System.out.println("searching: " + query);
		
		int start = 0;
		
		List<Thread> threadPool = new ArrayList<Thread>();
		
		URL[] documentResults = new URL[k_seed];
		
		while (start < k_seed){
			
			int count = getNumberOfResultsPerThread();
			
			executeQuery(threadPool,getRunnableQueryExecutor(query,start,Math.min(count, k_seed),documentResults));
			
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
		
		List<URL> urls = new ArrayList<URL>();
		
		for (int i = 0; i < documentResults.length; i++) {
			
			if (documentResults[i] != null)
				urls.add(documentResults[i]);
			
		}
		
		System.out.println(urls.size());
		
		Map<URL,String> urlContentMap = Parallel.map(urls, new Function<URL, String>() {

			@Override
			public String apply(URL url) {
				
				System.out.print(".");
				
				int attempt = 0;
				
				while (attempt < ATTEMPTS){
				
					BufferedReader br = null;
					
					try {
						
						br = new BufferedReader(new InputStreamReader(url.openStream()));
								
						StringBuilder sb = new StringBuilder();
							
						String fileLine = br.readLine();
						
						if (fileLine != null)
							sb.append(fileLine);
						
						while ((fileLine = br.readLine())!=null){
							
							sb.append("\n" + fileLine);
							
						}

						return sb.toString();
						
					} catch (MalformedURLException e) {
						
						//not do anything
						
					} catch (IOException e) {
			
						//not do anything
						
					}
					
					attempt++;
				}

				return "";

			}

		});
		
		List<Document> ret = new ArrayList<Document>();
		
		System.out.print("\nLoading");
		
		for (URL url : urls) {
			
			String document = urlContentMap.get(url);
			
			System.out.print(".");
			
			if (!document.isEmpty()){
				
				Document d = loader.load(document);
				d.setPath(url.getHost());
				d.setFilename(url.getFile());
				ret.add(d);
			}
			
		}
		
		System.out.println("");
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
