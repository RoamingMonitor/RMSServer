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
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	String type = req.getParameter("type");
    	String app= req.getParameter("app");

    	String appID = LogMessageHandler.M_APP;
    	if(app.equals("g1")){
    		appID = LogMessageHandler.G_APP;
    	} else if (app.equals("m1")){
    		appID = LogMessageHandler.M_APP;
    	} else if (app.equals("g2")){
    		appID = LogMessageHandler.G_APP_SIM;
    	} else if (app.equals("g3")){
    		appID = LogMessageHandler.G_APP_DEBUG;
    	}
    		    	
    	String deviceID = "RMShardware";
    	
    	String message = LogMessageHandler.createMessage(appID, type);
		GCMHandler.sendToApp(deviceID, appID, message);		
		resp.sendRedirect("/");
	}	
	
}
