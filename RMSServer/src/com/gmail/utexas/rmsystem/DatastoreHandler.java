package com.gmail.utexas.rmsystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Logger;

public class DatastoreHandler {
	
	static Logger log = Logger.getLogger("Datastore Handler");

	
	public static String makeRequest() throws IOException{
		URL url = new URL("http://rmsystem2014.appspot.com/datastore");	 
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		StringBuffer response = new StringBuffer();

		try{				
			connection.setDoOutput(false);
			connection.setRequestMethod("GET");

//			log.info("Response Code: "+connection.getResponseCode());				
			BufferedReader in = new BufferedReader(
					new InputStreamReader(connection.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
//			log.info(response.toString());
		} catch (SocketTimeoutException e){
			e.printStackTrace();
		}		

		return response.toString();
	}
	
	
	public static ArrayList<Integer> parseData(String str){
		ArrayList<Integer> data = new ArrayList<Integer>();
		System.out.println("Data: "+str);
		if(str.length() > 3){
			str = str.substring(1, str.length() - 1);			
			StringTokenizer st = new StringTokenizer(str,",");
	    	while(st.hasMoreTokens()){
	    		String num = st.nextToken().trim();
	    		int point = Integer.parseInt(num);	            		
	    		data.add(point);
	    	}
		}
    	
		return data;
	}
	
	public static ArrayList<Integer> getData() {
		String str = null;
		ArrayList<Integer> data = new ArrayList<Integer>();
		try {
			str = makeRequest();			
			data = parseData(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
}
