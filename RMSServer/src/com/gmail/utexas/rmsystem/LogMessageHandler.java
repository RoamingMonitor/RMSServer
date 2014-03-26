package com.gmail.utexas.rmsystem;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.gmail.utexas.rmsystem.models.Device;
import com.gmail.utexas.rmsystem.models.NotificationLogMessage;
import com.gmail.utexas.rmsystem.models.Settings;
import com.google.gson.Gson;
import com.googlecode.objectify.ObjectifyService;

public class LogMessageHandler {
	
	public static final String M_APP = "APA91bG8wRqLDdSZ06oPxsKsxbI8RFufD1sw_Bi2WlNcgx4kt_nK0DrlLit3zTsQEHoeEIzNPDBehuG3J8JA-ncQWMZY5KvIQK9BVJUUcvOc4vOeCtBYg8SYwbShQVsg1DhSZbOd2YcjCVW51th6vI959El52vHbIw"; 

	static {
        ObjectifyService.register(Settings.class);
    }

	public static ArrayList<NotificationLogMessage> determineMessages(String deviceID, String type){
		Device device = ofy().load().type(Device.class).id(deviceID).get();		
		ArrayList<String> apps = device.getAppIDs();
		
		ArrayList<NotificationLogMessage> messages = new ArrayList<NotificationLogMessage>();				
		for(String appID: apps){
			messages.add(determineMessage(appID, type));
		}		
		return messages;
		
	}
	
	public static NotificationLogMessage determineMessage(String appID, String type){
		Settings settings = ofy().load().type(Settings.class).id(appID).get();
		NotificationLogMessage message = new NotificationLogMessage();

		if(type.equals("sleepwalking")){
			message.setMessageTitle("Sleepwalking");				
			if(settings.sleepwalkingNotification && settings.sleepwalkingAlarm){
				message.setAlertType("both");
			} else if(settings.sleepwalkingNotification){
				message.setAlertType("notification");
			} else if(settings.sleepwalkingAlarm){
				message.setAlertType("alarm");
			}				
		} else if(type.equals("roaming")){
			message.setMessageTitle("Sleepwalking");				
			if(settings.roamingNotification && settings.roamingAlarm){
				message.setAlertType("both");
			} else if(settings.roamingNotification){
				message.setAlertType("notification");
			} else if(settings.roamingAlarm){
				message.setAlertType("alarm");
			}
		}else if(type.equals("low battery")){
			message.setMessageTitle("Low Battery");
			message.setMessageBody("RMS Device needs to be charged.");
		}else if(type.equals("headset")){
			message.setMessageTitle("Headset Detached");
			message.setMessageBody("Headset is no longer properly attached.");			
		}

		TimeZone timeZone = TimeZone.getTimeZone("Central Standard Time");
		Calendar calendar = new GregorianCalendar(timeZone);		
		SimpleDateFormat ft = new SimpleDateFormat ("EEE, MM/dd h:mm a");
		String currentTime = ft.format(calendar.getTime());		
		message.setDateAndTime(currentTime);
		
		return message;

	}
	

	public static String createMessage(String appID, String alert){		
		NotificationLogMessage message = determineMessage(appID, alert);		
		Gson gson = new Gson();
		String msg = "\"logMessage\":"+gson.toJson(message);
		return msg;
	}
}
