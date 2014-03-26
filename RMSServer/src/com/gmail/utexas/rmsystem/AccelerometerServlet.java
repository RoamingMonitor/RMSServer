package com.gmail.utexas.rmsystem;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gmail.utexas.rmsystem.algorithms.AccelerometerAlgorithm;
import com.gmail.utexas.rmsystem.models.AccelerometerData;
import com.googlecode.objectify.ObjectifyService;

public class AccelerometerServlet extends HttpServlet{
	
	static {
        ObjectifyService.register(AccelerometerData.class);
    }
	Logger log = Logger.getLogger(AccelerometerServlet.class.getName());
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		log.info("Accelerometer data!");
		String deviceID = req.getHeader("User-Agent");
		AccelerometerData dataObject = ofy().load().type(AccelerometerData.class).id(deviceID).get();
		if(dataObject == null){
			dataObject = new AccelerometerData();
			dataObject.setDeviceID(deviceID);
		}
		ArrayList<Integer> buffer = dataObject.getData();
				
		 BufferedReader reader = req.getReader();		 
	        try {
	            String line;
	            while ((line = reader.readLine()) != null) {
	            	log.setLevel(Level.INFO);
	            	log.info(line);
	            	log.info("line length  " +line.length());
	            	
	                // process input
	            	StringTokenizer st = new StringTokenizer(line,",");
	            	while(st.hasMoreTokens()){
	            		String num = st.nextToken().trim();
	            		int point = Integer.parseInt(num) - 1000;	            		
	            		buffer.add(point);
	            	}
	            	
	            	dataObject.setData(buffer);
	            	ofy().save().entity(dataObject).now();
	            }
	        } finally {
	            reader.close();
	        }
	}
	

}
