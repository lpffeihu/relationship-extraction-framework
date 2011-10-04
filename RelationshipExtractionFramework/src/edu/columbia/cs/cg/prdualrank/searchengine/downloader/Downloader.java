package edu.columbia.cs.cg.prdualrank.searchengine.downloader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;

import edu.columbia.cs.cg.document.Document;

public class Downloader implements Runnable {

	private final int ATTEMPTS = 3;
	private final int TIME_INTERVAL = 1000;
	
	private URL url;
	private int i;
	private String[] documents;

	public Downloader(URL url, int i, String[] documentsContent) {
		this.url = url;
		this.i = i;
		this.documents = documentsContent;
	}

	@Override
	public void run() {
		
		int attempt = 0;
		
		while (attempt < ATTEMPTS){
		
			try {
				Thread.sleep(attempt*TIME_INTERVAL);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
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

				documents[i] = sb.toString();
				
				return;
				
			} catch (MalformedURLException e) {
				
				//not do anything
				
			} catch (IOException e) {
	
				//not do anything
				
			}
			
			attempt++;
		}

		documents[i] = null;

	}

}
