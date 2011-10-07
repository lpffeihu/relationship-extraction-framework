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
		
		URLConnection conn = null;
		try {
			conn = url.openConnection();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		 
		        // Set up a request.
		conn.setConnectTimeout(TIME_OUT);    // 10 sec
		conn.setReadTimeout(TIME_OUT);       // 10 sec
		
		conn.setRequestProperty( "User-agent", "spider" );

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
				
				//not do anything
				
			} catch (IOException e) {
	
				//not do anything
				
			}
			
			attempt++;
		}

		documents.put(url,null);

	}

}
