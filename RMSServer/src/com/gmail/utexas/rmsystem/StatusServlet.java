package com.gmail.utexas.rmsystem;

import static com.googlecode.objectify.ObjectifyService.ofy; 

import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gmail.utexas.rmsystem.models.Device;
import com.gmail.utexas.rmsystem.models.Settings;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.LoadType;
import com.googlecode.objectify.cmd.Query;
import com.googlecode.objectify.cmd.QueryKeys;

public class StatusServlet extends HttpServlet{

	Logger log = Logger.getAnonymousLogger();
	static {
		ObjectifyService.register(Settings.class);
		ObjectifyService.register(Device.class);
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Query<Settings> q = ofy().load().type(Settings.class);
		List<Settings> list = q.list();
		Date date = new Date();
		SimpleDateFormat ft = new SimpleDateFormat ("hh:mma");
		String currentTime = ft.format(date); 

		log.info("Current Time: "+currentTime );
		for(Settings s: list){		
			log.info("Comparing ["+currentTime+"] to ["+s.start+"]");
			if(s.start !=null && s.start.equals(currentTime)){				
				// TODO: send to device to activate
				log.info("Activate: "+s.id );
			} else if(s.end != null && s.end.equals(currentTime)){
				// TODO: send to device to deactivate
				log.info("Deactivate: "+s.id);
			}
		}	
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		BufferedReader reader = req.getReader();
		boolean status = false;
		String target = "fake_device_id";    //TODO: fix to get real device id    
		try {
			String line;
			while ((line = reader.readLine()) != null) {
				status = Boolean.parseBoolean(line);
			}
		} finally {
			reader.close();
		}

		Device device = ofy().load().type(Device.class).id(target).get();
		if(device != null){
			device.setStatus(status);
			ofy().save().entity(device).now();
			GCMHandler.sendToApp(target, null, "");
		} else {
			String ip = req.getRemoteAddr();
			device = new Device(target, status, ip);
			ofy().save().entity(device).now();
		}

	}
}
