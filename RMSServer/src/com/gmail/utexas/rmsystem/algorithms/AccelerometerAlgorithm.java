package com.gmail.utexas.rmsystem.algorithms;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmail.utexas.rmsystem.GCMHandler;
import com.gmail.utexas.rmsystem.LogMessageHandler;
import com.gmail.utexas.rmsystem.ManualStatusServlet;

public class AccelerometerAlgorithm {
	private ArrayList<Integer> buf;
	//private final int BUFSIZE = 14969;
	private final int BUFSIZE = 16300;
	private final int MAX_LOW = 150;
	private final int MAX_HIGH = 150;
	private final int MAX_NORM = 500;
	private final int THRESH_HI = 2500;
	private final int THRESH_LOW = 2200;
	private final int REQSTEPS = 3;
	private final int TOOHIGH = 3000;
	private final int TOOLOW = 1500;
	private int bufIn, bufOut, dataCount, lowCount, highCount, stepCount, normCount;
	private boolean prelimOn;
	private boolean detection_sent;
	private BioAlgorithm bio;
	
	Logger log = Logger.getLogger(AccelerometerAlgorithm.class.getName());

	public AccelerometerAlgorithm() {
		buf = new ArrayList<Integer>();
		bufIn = 0;
		bufOut = 0;
		dataCount = 0;
		lowCount = 0;
		bio = new BioAlgorithm();
		prelimOn = false;
		detection_sent = false;
	}
	
	//takes in element from array and handles triggering of events due to data
	public void processData(int data){
		bufOut++;	//used for debugging where triggers occur (remove when done testing)
		if(detection_sent && timeDiff >= 5){
			sendWalkingAlert();
		}
		
		//discard vals that are too low
		if(data <= TOOLOW){
			//System.out.println("Value too low!");
			log.info("Value too low!");
			resetData();
		}
		
		//check if low end of a step
		else if(data <= THRESH_LOW){
			//check if first low step and need to turn on biometrics
			if(lowCount == 1 && stepCount == 0){
				//System.out.println("Preliminary detection started!");
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
			log.info("Too many high vals in a row!");
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
					log.info("Step count increased to: "+stepCount+" @ "+bufOut);
					lowCount = 0;
					highCount = 0;
				}
				if(stepCount >= REQSTEPS && !detection_sent){
					sendWalkingAlert();
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
				log.info("Too many vals in norm range @ "+bufOut+"...stopping prelim");
				resetData();
			}
		}
	}
	public void resetData(){
		log.info("Reseting accelerometer algorithm data");
		highCount=lowCount=stepCount=normCount=0;
		prelimOn = false;
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
	public void sendWalkingAlert(){
		//handle walking alert to phone app
		log.setLevel(Level.INFO);		

		try {
			URL url = null;
			if(bio.isAsleep()){				
				System.out.println("Sending sleepwalking alert!");
				url = new URL("http://rmsystem2014.appspot.com/alert?type=sleepwalking&app=g3");
			} else {
				System.out.println("Sending roaming alert!");
				url = new URL("http://rmsystem2014.appspot.com/alert?type=roaming&app=g3");
			}
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(false);
			connection.setRequestMethod("GET");
			
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuffer response = new StringBuffer();
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}						
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
