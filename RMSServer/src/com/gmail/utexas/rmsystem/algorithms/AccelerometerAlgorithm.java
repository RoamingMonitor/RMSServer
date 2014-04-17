package com.gmail.utexas.rmsystem.algorithms;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmail.utexas.rmsystem.models.Biometrics;
import com.gmail.utexas.rmsystem.models.RMSUser;

public class AccelerometerAlgorithm {
	private final int MAX_LOW = 50;
	private final int MAX_HIGH = 50;
	private final int THRESH_HI = 2500;
	private final int THRESH_LOW = 2200;
	private final int REQSTEPS = 3;
	private int lowCount, highCount, stepCount, normCount;
	private long detectionTimestamp, snoozeTimestamp, allowedRoamingDuration, snoozeDuration;
	private boolean prelimOn;
	private boolean detected, sentRoaming, snoozed;
	
	Logger log = Logger.getLogger(AccelerometerAlgorithm.class.getName());

	public AccelerometerAlgorithm() {
		lowCount = 0;
		prelimOn = false;
		detected = false;
		sentRoaming = false;
		snoozed = false;
		allowedRoamingDuration = getAllowedRoamingDuration();
	}
	
	//takes in element from array and handles triggering of events due to data
	public void processData(int data){
		//sleepwalking!!!
		if(stepCount >= REQSTEPS && !detected && getBioStatus().equals(Biometrics.ASLEEP)){
			detected = true;
			setDependentStatus(RMSUser.SLEEPWALKING);
			sendAlert("sleepwalking");
		}
		//roaming!!!
		else if(stepCount >= REQSTEPS && !sentRoaming && !snoozed && getBioStatus().equals(Biometrics.AWAKE) ){
			if(!detected){
				detected = true;
				detectionTimestamp = System.currentTimeMillis();
				log.info("Roaming detected...now wait allowed duration");
				setDependentStatus(RMSUser.ROAMING);
				allowedRoamingDuration = getAllowedRoamingDuration();
			}
			else if((System.currentTimeMillis() - detectionTimestamp) >= allowedRoamingDuration){
				//log.info("Sending roaming alert");
				setDependentStatus(RMSUser.ROAMING);
				sendAlert("roaming");
				sentRoaming = true;
			}
		}
		//check on alert snoozing
		if(snoozed && ((System.currentTimeMillis() - snoozeTimestamp) >= snoozeDuration)){
			snoozed = false;
			sentRoaming = false;
			if(stepCount >= REQSTEPS && getBioStatus().equals(Biometrics.ASLEEP)){
				//log.info("Sending roaming alert");
				sendAlert("sleepwalking");
			}
		}
		//check if low end of a step
		if(data <= THRESH_LOW){
			//check if first low step and need to turn on biometrics
			lowCount++;
			normCount = 0;
			if(lowCount == 1 && stepCount == 0){
				prelimOn = true;
				setBioStatus("on");
			}
			//check for too many low vals in a row
			if(lowCount > MAX_LOW){
				log.info("Too many low vals in a row!");
				resetData();
			}
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
				log.info("Too many high values in a row!");
				resetData();
			}
		}
		//data is in normal range
		else if(data < THRESH_HI && data > THRESH_LOW){
			normCount++;
		}
	}
	
	public void resetData(){
		log.info("Reseting accelerometer algorithm data");
		highCount=lowCount=stepCount=normCount=0;
		prelimOn = false;
		detected = false;		
		sentRoaming = false;
		setBioStatus("off");
		setDependentStatus(RMSUser.SLEEPING);
	}
	
	public void snoozeAlert(long dur){
		if(!snoozed){
			snoozed = true;
			snoozeTimestamp = System.currentTimeMillis();
			log.info("Alert has been snoozed");
			snoozeDuration = dur*60*1000; //converted duration into millisecs
		}
	}
	
	public void sendAlert(String type){
		//type must be either: "sleepwalking" or "roaming"
		if(!(type.equals("sleepwalking") || type.equals("roaming"))) return;
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
	
	public String getBioStatus(){
		log.setLevel(Level.INFO);		
		URL url = null;
		try {
			url = new URL("http://rmsystem2014.appspot.com/biometrics");
		}catch (MalformedURLException e){
			e.printStackTrace();
		}
		String status = send(url);
		//System.out.println("Retrieved biometrics status of: " + status);
		return status;
	}
	
	public void setBioStatus(String s){
		//s must either be "on" or "off"
		String temp = "";
		if(s.equals("on")){
			temp = "true";
		}
		else if(s.equals("off")){
			temp = "false";
		}
		else return;
		log.setLevel(Level.INFO);		
		URL url = null;
		try {
			System.out.println("Setting biometrics status " + s);
			url = new URL("http://rmsystem2014.appspot.com/test?status="+temp);
		}catch (MalformedURLException e){
			e.printStackTrace();
		}
		send(url);
	}
	
	public void setDependentStatus(String status){
		log.setLevel(Level.INFO);
		URL url = null;
		try {
			System.out.println("Setting dependent's status to " + status);
			url = new URL("http://rmsystem2014.appspot.com/dependent_status?status="+status);
		}catch (MalformedURLException e){
			e.printStackTrace();
		}
		send(url);
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
