package com.gmail.utexas.rmsystem;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gmail.utexas.rmsystem.models.Biometrics;
import com.gmail.utexas.rmsystem.models.Device;
import com.googlecode.objectify.ObjectifyService;

public class BiometricsInputServlet extends HttpServlet{

	Logger log = Logger.getLogger(BiometricsInputServlet.class.getName());
	static {
        ObjectifyService.register(Biometrics.class);
        ObjectifyService.register(Device.class);
    }
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//		String type = req.getParameter("type");		
//		if(type != null){
//			String currentStatus = biometrics.getStatus();
//			if(currentStatus.equals(Biometrics.AWAKE)){
//				biometrics.setStatus(Biometrics.ASLEEP);
//			} else if (currentStatus.equals(Biometrics.ASLEEP)) {
//				biometrics.setStatus(Biometrics.AWAKE);
//			} else {
//				biometrics.setStatus(type);
//			}
//			
//			ofy().save().entity(biometrics).now();
//			resp.sendRedirect("/");
//		} else {			
//		}
		log.setLevel(Level.INFO);		
		Biometrics biometrics = ofy().load().type(Biometrics.class).id("RMShardware").get();
		String bio = biometrics.getStatus(); 
		log.info(bio);
		resp.getWriter().println(bio);
	}

}
