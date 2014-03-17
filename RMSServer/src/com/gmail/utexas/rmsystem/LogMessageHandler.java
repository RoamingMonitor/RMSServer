package com.gmail.utexas.rmsystem;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.gmail.utexas.rmsystem.models.Device;
import com.gmail.utexas.rmsystem.models.NotificationLogMessage;
import com.gmail.utexas.rmsystem.models.Settings;
import com.google.gson.Gson;
import com.googlecode.objectify.ObjectifyService;

public class LogMessageHandler {

	public void determineMessages(String deviceID, boolean roaming, boolean sleepwalking){
		Device device = ofy().load().type(Device.class).id(deviceID).get();		
		ArrayList<String> apps = device.getAppIDs();
		
		ArrayList<NotificationLogMessage> messages = new ArrayList<NotificationLogMessage>();
		
		for(String appID: apps){
			Settings settings = ofy().load().type(Settings.class).id(appID).get();
			
			if(roaming){
				if(settings.roamingNotification){
					
				}
				if(settings.roamingAlarm){
					
				}
			}
			
			if(sleepwalking){
				if(settings.sleepwalkingNotification){
					
				}
				if(settings.sleepwalkingAlarm){
					
				}
			}				
		}
		
		
	}

	
	

	public String createLogMessage(String type){
		Date date = new Date();
		SimpleDateFormat ft = new SimpleDateFormat ("EEE, MM/dd h:mm a");
		String currentTime = ft.format(date);
		
		NotificationLogMessage message = new NotificationLogMessage(type, currentTime, "");
		Gson gson = new Gson();		
		String msg = "\"logMessage\":"+gson.toJson(message);		
		return msg;
	}
}
