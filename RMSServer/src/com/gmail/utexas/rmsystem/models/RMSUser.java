package com.gmail.utexas.rmsystem.models;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class RMSUser {
	@Id private String appID;
	private String deviceID;
	private boolean snoozeStatus;
	private String dependentStatus;
	public static final String ASLEEP = "Sleeping";
	public static final String ROAMING = "Roaming";
	public static final String SLEEPWALKING = "Sleepwalking";
	
	public String getAppID(){
		return appID;
	}
	
	public String getDeviceID(){
		return deviceID;
	}

	public boolean isSnooze() {
		return snoozeStatus;
	}

	public void setSnooze(boolean snoozeStatus) {
		this.snoozeStatus = snoozeStatus;
	}

	public String getDependentStatus() {
		return dependentStatus;
	}

	public void setDependentStatus(String dependentStatus) {
		this.dependentStatus = dependentStatus;
	}

}
