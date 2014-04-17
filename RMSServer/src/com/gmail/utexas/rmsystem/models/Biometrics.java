package com.gmail.utexas.rmsystem.models;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;


@Entity
public class Biometrics {
	public static final String ASLEEP = "asleep";
	public static final String AWAKE = "awake";
	public static final String BADINPUT = "badinput";
	public static final String FALSEPOSITIVE = "falsepositive";
	private String status;
	@Id private String deviceID;
	
	public Biometrics(){
	}
	

	public Biometrics(String device){
		deviceID = device;
	}
	
	public void setStatus(String type){
		status = type;
	}
	
	public void setAsleep(){
		status = ASLEEP;
	}
	public void setAwake(){
		status = AWAKE;
	}
	public void setBadInput(){
		status = BADINPUT;
	}
	public void setFalsePositive(){
		status = FALSEPOSITIVE;
	}
	public String getStatus(){
		return status;
	}

	public String getDeviceID(){
		return deviceID;
	}
	
	public void setDeviceID(String id){
		deviceID = id;
	}

}
