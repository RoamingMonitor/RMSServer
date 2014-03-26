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
import com.googlecode.objectify.ObjectifyService;

public class ManualStatusServlet extends HttpServlet {

	Logger log = Logger.getLogger(ManualStatusServlet.class.getName());
	static {
		ObjectifyService.register(Device.class);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	log.setLevel(Level.INFO);
		BufferedReader reader = req.getReader();
		boolean status = false;
		String target = "fake_device_id";
		try {			
			String line = reader.readLine();
			log.info(line);
			String[] values = line.split(",");
			target = values[0];
			status = Boolean.parseBoolean(values[1]);			
		} finally {
			reader.close();
		}

		Device device = ofy().load().type(Device.class).id(target).get();
		if(device != null){
			device.setStatus(status);
			ofy().save().entity(device).now();
			// TODO: send message to device to activate or deactivate
			GCMHandler.sendToApp("", target);	    	
		} else {			
	    	resp.setStatus(400);
		}

	}
}
