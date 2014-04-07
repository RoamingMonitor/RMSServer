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
	private boolean bioStatus;
	
	public Device(){
		
	}
	
	public Device(String deviceID, boolean status, String address){
		this.deviceID = deviceID;
		this.status = status;		
		this.address = address;	
	}
	
	public Device(String deviceID){
		this.deviceID = deviceID;
	}
	
	public void setStatus(boolean status){
		this.status = status;		
	}
	
	public boolean getStatus(){
		return status;
	}
	
	public void setDeviceID(String id){
		deviceID = id;
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

	public boolean getBioStatus() {
		return bioStatus;
	}

	public void setBioStatus(boolean bioStatus) {
		this.bioStatus = bioStatus;
	}

}
