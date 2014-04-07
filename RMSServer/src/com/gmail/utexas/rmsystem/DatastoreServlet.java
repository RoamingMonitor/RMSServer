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
import com.gmail.utexas.rmsystem.models.Settings;
import com.googlecode.objectify.ObjectifyService;

public class DatastoreServlet extends HttpServlet{

	Logger log = Logger.getLogger("Datastore Servlet");
	static {
        ObjectifyService.register(AccelerometerData.class);
    }


    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	log.info("BOO!");
    	String deviceID = "RMShardware";
    	PrintWriter writer = resp.getWriter();		
		AccelerometerData dataObject = ofy().load().type(AccelerometerData.class).id(deviceID).get();
		try{
			writer.print(dataObject.getData()+"");		
		} finally{
			writer.close();
		}
		
		dataObject.setData(new ArrayList<Integer>());
		ofy().save().entity(dataObject).now();
    }    
}
