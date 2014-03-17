package com.gmail.utexas.rmsystem;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gmail.utexas.rmsystem.models.NotificationLogMessage;
import com.google.gson.Gson;

public class AlertServlet extends HttpServlet{
	
	Logger log = Logger.getAnonymousLogger();

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {		
		GCMHandler.sendToApp(createLogMessage(), "fake_device_id");
	}
	
	
	public String createLogMessage(){
		Date date = new Date();
		SimpleDateFormat ft = new SimpleDateFormat ("EEE, MM/dd h:mm a");
		String currentTime = ft.format(date);
		
		NotificationLogMessage message = new NotificationLogMessage("Sleepwalking", currentTime, "");
		Gson gson = new Gson();		
		String msg = "\"logMessage\":"+gson.toJson(message);		
		return msg;
	}
}
