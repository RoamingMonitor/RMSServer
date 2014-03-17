package com.gmail.utexas.rmsystem.models;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class User {
	@Id private String appID;
	private String deviceID;	
	
	public String getAppID(){
		return appID;
	}
	
	public String getDeviceID(){
		return deviceID;
	}

}
