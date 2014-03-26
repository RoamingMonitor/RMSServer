package com.gmail.utexas.rmsystem.models;

import java.util.ArrayList;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class AccelerometerData {

	@Id private String deviceID;
	private ArrayList<Integer> data = new ArrayList<Integer>();
	
		
	public AccelerometerData(){
		
	}
	

	public String getDeviceID(){
		return deviceID;
	}
	
	public void setDeviceID(String id){
		deviceID = id;
	}


	public ArrayList<Integer> getData() {
		return data;
	}


	public void setData(ArrayList<Integer> data) {
		this.data = data;
	}
	
}
