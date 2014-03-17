package com.gmail.utexas.rmsystem;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gmail.utexas.rmsystem.models.Device;

public class ManualStatusServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		BufferedReader reader = req.getReader();
		boolean status = false;
		String target = "fake_device_id";    
		try {			
			target = reader.readLine();
			status = Boolean.parseBoolean(reader.readLine());			
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
