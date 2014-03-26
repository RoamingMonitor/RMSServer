package com.gmail.utexas.rmsystem.models;

import java.util.ArrayList;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;


@Entity
public class Device {
	@Id private String deviceID;
	private boolean status;
	private String address;
	private ArrayList<String> appIDs = new ArrayList<String>();
	
	public Device(){
		
	}
	
	public Device(String deviceID, boolean status, String address){
		this.deviceID = deviceID;
		this.status = status;		
		this.address = address;
	}
	
	public void setStatus(boolean status){
		this.status = status;		
	}
	
	public boolean getStatus(){
		return status;
	}
	
	public String getDeviceID(){
		return deviceID;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public ArrayList<String> getAppIDs() {
		return appIDs;
	}

	public void addAppIDs(String appID) {
		if(!appIDs.contains(appID)){
			appIDs.add(appID);
		}
	}

}
