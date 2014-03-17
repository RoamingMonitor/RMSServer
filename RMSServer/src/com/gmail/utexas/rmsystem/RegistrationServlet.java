package com.gmail.utexas.rmsystem;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gmail.utexas.rmsystem.models.Device;
import com.gmail.utexas.rmsystem.models.User;
import com.google.gson.Gson;
import com.googlecode.objectify.ObjectifyService;

public class RegistrationServlet extends HttpServlet{

	Logger log = Logger.getAnonymousLogger();
	static {
		ObjectifyService.register(User.class);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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

		User user = gson.fromJson(content, User.class);
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


	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// For debugging purposes
		String msg = "hi"; 
		GCMHandler.sendToApp(msg, "APA91bG8wRqLDdSZ06oPxsKsxbI8RFufD1sw_Bi2WlNcgx4kt_nK0DrlLit3zTsQEHoeEIzNPDBehuG3J8JA-ncQWMZY5KvIQK9BVJUUcvOc4vOeCtBYg8SYwbShQVsg1DhSZbOd2YcjCVW51th6vI959El52vHbIw");
	}
	


}
