package edu.columbia.cs.cg.prdualrank.searchengine.downloader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import edu.columbia.cs.cg.document.Document;

public class Downloader implements Runnable {

	private final int ATTEMPTS = 3;
	private final int TIME_INTERVAL = 1000;
	private final int TIME_OUT = 10000;
	
	private URL url;
	private Map<URL,String> documents;

	public Downloader(URL url, Map<URL,String> documentsContent) {
		this.url = url;
		this.documents = documentsContent;
	}

	@Override
	public void run() {
		
		

		int attempt = 0;
		
		while (attempt < ATTEMPTS){
	
			try {
				
				Thread.sleep(attempt*TIME_INTERVAL);
				
				URLConnection conn = null;
			
				conn = url.openConnection();
				 
				        // Set up a request.
				conn.setConnectTimeout(TIME_OUT);    // 10 sec
				conn.setReadTimeout(TIME_OUT);       // 10 sec
				
				conn.setRequestProperty( "User-agent", "spider" );
								
				BufferedReader br = null;
			
				conn.connect();
				
				br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
						
				StringBuilder sb = new StringBuilder();
					
				String fileLine = br.readLine();
				
				if (fileLine != null)
					sb.append(fileLine);
				
				while ((fileLine = br.readLine())!=null){
					
					sb.append("\n" + fileLine);
					
				}

				synchronized (documents) {
					
					documents.put(url, sb.toString());
					
				}
				
				System.out.print(".");
				
				return;
				
			} catch (MalformedURLException e) {
				
				//do not do anything
				
			} catch (IOException e) {
	
				//do not do anything
				
			} catch (InterruptedException e) {
				//don't do anything
			}
			
			attempt++;
		}

		documents.put(url,null);

	}

}
