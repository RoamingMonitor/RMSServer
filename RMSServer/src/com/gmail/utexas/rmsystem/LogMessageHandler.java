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
	public static final String G_APP_SIM = "APA91bGFtWn-03SJe5mbhOMbZbByVQbrQmpTWUanlsx_buFyyjEDG4l5dGoIs1Kn1YQhk8J3W5QOlUBGZ-azrNGNZ1iLrSvlTYT5TKs7jHK0f9Jl3-FJEhe_yVUZHN5Dj1Xak4Ed1sPOtdNrAiIXtSIxuwNxIoosYw";
	public static final String G_APP = "APA91bHpoSB1wA82iKzY7pK0GlgSyPIWLcJGNFXy-ZZZHEDVi1p9d9NhreHVXrtpzoY11O18Kb5sknK4DD9dy_pbmhJoF_2vyT_Ep58yxkHVSSCSNxA_aD54Erpcb0mPT9rBRRdt5R9ch7PRi9mK8df8dqCX0_XJqQ";
	public static final String G_APP_DEBUG = "APA91bG45xD5SyPGM1UecqKEoh0cP_WN2JVRFvGCYBGWkrqRQCqVHMdWLwiP5-2Rh9fCHqGK5uKRnT_1v_zZZKwf_shGyjMyC70vjmLVsJnUC4FFDUXGPyZmJEJNHzaToiklZn2Q5fMd8MzmSkLNumLA-cF7V7gpFw";

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
			message.setMessageBody("Sleepwalking detected!");
			
			if(settings.sleepwalkingNotification && settings.sleepwalkingAlarm){
				message.setAlertType("both");				
			} else if(settings.sleepwalkingNotification){
				message.setAlertType("notification");
			} else if(settings.sleepwalkingAlarm){
				message.setAlertType("alarm");
			} else {
				message.setAlertType("none");
			}
			
		} else if(type.equals("roaming")){
			message.setMessageTitle("Roaming");
			message.setMessageBody("Roaming detected!");
			
			if(settings.roamingNotification && settings.roamingAlarm){
				message.setAlertType("both");			
			} else if(settings.roamingNotification){
				message.setAlertType("notification");			
			} else if(settings.roamingAlarm){
				message.setAlertType("alarm");		
			} else {
				message.setAlertType("none");
			}
			
		}else if(type.equals("lowbattery")){
			message.setAlertType("notification");
			message.setMessageTitle("Low Battery");
			message.setMessageBody("RMS Device needs to be charged.");
		}else if(type.equals("headset")){
			message.setAlertType("notification");
			message.setMessageTitle("Headset Detached");			
			message.setMessageBody("Headset is no longer properly attached.");			
		}

		TimeZone timeZone = TimeZone.getTimeZone("Central Standard Time");
		Calendar calendar = new GregorianCalendar(timeZone);		
		SimpleDateFormat ft = new SimpleDateFormat ("EEE, MM/dd h:mm a");
		ft.setTimeZone(TimeZone.getTimeZone("CST"));
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
	
	public String createFakeLogMessage(){
		Date date = new Date();
		SimpleDateFormat ft = new SimpleDateFormat ("EEE, MM/dd h:mm a");
		ft.setTimeZone(TimeZone.getTimeZone("CST"));
		String currentTime = ft.format(date);
		
		NotificationLogMessage message = new NotificationLogMessage("Sleepwalking", currentTime, "", "alarm");
		Gson gson = new Gson();		
		String msg = "\"logMessage\":"+gson.toJson(message);		
		return msg;
	}
	
}
