package com.gmail.utexas.rmsystem;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gmail.utexas.rmsystem.models.Device;
import com.gmail.utexas.rmsystem.models.RMSUser;
import com.google.gson.Gson;
import com.googlecode.objectify.ObjectifyService;

public class RegistrationServlet extends HttpServlet{

	Logger log = Logger.getLogger(RegistrationServlet.class.getName());
	static {
		ObjectifyService.register(RMSUser.class);
		ObjectifyService.register(Device.class);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	log.setLevel(Level.INFO);
		Gson gson = new Gson();
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = req.getReader();
		try {
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} finally {
			reader.close();
		}
		String content = sb.toString();
		log.info(content);
				
		RMSUser user = gson.fromJson(content, RMSUser.class);
		RMSUser oldUser = ofy().load().type(RMSUser.class).id(user.getAppID()).get();	
		user.setDependentStatus(oldUser.getDependentStatus());
		
		ofy().save().entity(user).now();
		resp.setContentType("text/plain");
		
		if(user.getDeviceID() != null){
			Device device = ofy().load().type(Device.class).id(user.getDeviceID()).get();
			device.addAppIDs(user.getAppID());
			ofy().save().entity(device).now();
			resp.getWriter().println("User successfully registered.");
		} else {
	    	resp.getWriter().println("Invalid Device ID.");
	    	resp.setStatus(400);
		}
		
	}

}
