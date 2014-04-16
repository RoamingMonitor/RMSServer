package com.gmail.utexas.rmsystem;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gmail.utexas.rmsystem.models.AccelerometerData;
import com.gmail.utexas.rmsystem.models.RMSUser;
import com.gmail.utexas.rmsystem.models.Settings;
import com.googlecode.objectify.ObjectifyService;

public class DatastoreServlet extends HttpServlet{

	Logger log = Logger.getLogger("Datastore Servlet");
	static {	
	    ObjectifyService.register(Settings.class);
	    ObjectifyService.register(RMSUser.class);	    
        ObjectifyService.register(AccelerometerData.class);
    }


    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	String deviceID = "RMShardware";
    	PrintWriter writer = resp.getWriter();		
		AccelerometerData dataObject = ofy().load().type(AccelerometerData.class).id(deviceID).get();
		
		RMSUser user = ofy().load().type(RMSUser.class).id(LogMessageHandler.G_APP_DEBUG).get();
		if(user.isSnooze()){			
    		Settings settings = ofy().load().type(Settings.class).id(LogMessageHandler.G_APP_DEBUG).get();
    		writer.println(settings.snooze);
			user.setSnooze(false);
	    	ofy().save().entity(user).now();
		} else {
			writer.println(0);
		}
		
		try{
			writer.println(dataObject.getData()+"");		
		} finally{
			writer.close();
		}
			
		dataObject.setData(new ArrayList<Integer>());
		ofy().save().entity(dataObject).now();
    }    
}
