package com.gmail.utexas.rmsystem.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;


@Entity
public class Settings {
	@Id public String id;
	public int frequency, duration;
	public String start, end;
	public boolean schedule, roamingNotification, roamingAlarm, sleepwalkingNotification, sleepwalkingAlarm;
	
	public Settings(){
		
	}
	
	public Settings(String id){
		this.id = id;
	}	

}
