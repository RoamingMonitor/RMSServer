package com.gmail.utexas.rmsystem.algorithms;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mortbay.log.Log;

import com.gmail.utexas.rmsystem.GCMHandler;
import com.gmail.utexas.rmsystem.LogMessageHandler;
import com.gmail.utexas.rmsystem.ManualStatusServlet;

public class AccelerometerAlgorithm {
	private final int MAX_LOW = 150;
	private final int MAX_HIGH = 150;
	private final int MAX_NORM = 500;
	private final int THRESH_HI = 2500;
	private final int THRESH_LOW = 2200;
	private final int REQSTEPS = 3;
	private final int TOOHIGH = 3000;
	private final int TOOLOW = 1500;
	private int lowCount, highCount, stepCount, normCount;
	private long detectionTimestamp, allowedRoamingDuration;
	private boolean prelimOn;
	private boolean detected, sentRoaming;
	private BioAlgorithm bio;
	
	Logger log = Logger.getLogger(AccelerometerAlgorithm.class.getName());

	public AccelerometerAlgorithm() {
		lowCount = 0;
		bio = new BioAlgorithm();
		prelimOn = false;
		detected = false;
		allowedRoamingDuration = getAllowedRoamingDuration();
	}
	
	//takes in element from array and handles triggering of events due to data
	public void processData(int data){
		//sleepwalking!!!
		if(stepCount >= REQSTEPS && bio.isAsleep() && !detected){
			detected = true;
			sendAlert("sleepwalking");
			//send dependent_status update to Melissa's new servlet!
		}
		//roaming!!!
		if(stepCount >= REQSTEPS && !bio.isAsleep() && !sentRoaming){
			if(!detected){
				detected = true;
				detectionTimestamp = System.currentTimeMillis();
				log.info("Roaming detected...now wait to confirm");
				//send dependent_status update to Melissa's new servlet!
			}
			else if((System.currentTimeMillis() - detectionTimestamp) >= allowedRoamingDuration){
				log.info("Sending roaming alert");
				sendAlert("roaming");
				sentRoaming = true;
			}
		}
		
		//discard vals that are too low
		if(data <= TOOLOW){
			log.info("Value too low!");
			resetData();
		}
		
		//check if low end of a step
		else if(data <= THRESH_LOW){
			//check if first low step and need to turn on biometrics
			if(lowCount == 1 && stepCount == 0){
				prelimOn = true;
				activateBio();
			}
			lowCount++;
			normCount = 0;
			//check for too many low vals in a row
			if(lowCount > MAX_LOW){
				log.info("Too many low vals in a row!");
				resetData();
			}
		}
		
		//discard vals that are too high
		else if(data >= TOOHIGH){
			log.info("Data vals too high!");
			resetData();
		}
		
		//check for high end of step (only if low end has already been detected...aka prelim is ON)
		else if(data >= THRESH_HI && prelimOn){
			highCount++;
			normCount = 0;
			//check if not too many high vals in a row
			if(highCount <= MAX_HIGH){
				if(lowCount != 0){
					stepCount++;
					log.info("Step count increased to: "+stepCount);
					lowCount = 0;
					highCount = 0;
				}
			}
			else{
				log.info("Too many high values in a row...stopping prelim detection");
				resetData();
			}
		}
		
		//data is in normal range
		else if(data < THRESH_HI && data > THRESH_LOW){
			normCount++;
			if(normCount >= MAX_NORM){
				log.info("Too many vals in norm range...stopping prelim");
				resetData();
			}
		}
	}
	public void resetData(){
		log.info("Reseting accelerometer algorithm data");
		highCount=lowCount=stepCount=normCount=0;
		prelimOn = false;
		detected = false;
		sentRoaming = false;
		deactivateBio();
	}
	public void activateBio(){
		//make new thread to start processing biometric data
		bio.activate();
	}
	public void deactivateBio(){
		//make new thread to handle turning biometrics off
		bio.deactivate();
	}
	public void sendAlert(String type){
		//type must be either: "sleepwalking" or "roaming"
		log.setLevel(Level.INFO);		
		URL url = null;
		try {
			System.out.println("Sending "+type+" alert!");
			url = new URL("http://rmsystem2014.appspot.com/alert?type="+type+"&app=g3");
			System.out.println(url.toString());
		}catch (MalformedURLException e){
			e.printStackTrace();
		}
		send(url);
	}
	public long getAllowedRoamingDuration(){
		log.setLevel(Level.INFO);		
		URL url = null;
		try {
			System.out.println("Getting allowed roaming duration setting!");
			url = new URL("http://rmsystem2014.appspot.com/settings");
		}catch (MalformedURLException e){
			e.printStackTrace();
		}
		String setting = send(url);
		long temp = Long.parseLong(setting)*60*1000; //convert to millisecs and return value
		System.out.println("Allowed roaming time in milli: " + temp);
		return temp;
	}
	public String send(URL url){
		try{
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(false);
			connection.setRequestMethod("GET");
			
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuffer response = new StringBuffer();
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}	
			return response.toString();
		}catch(IOException e){
			e.printStackTrace();
		}
		return "error";			
	}
}
