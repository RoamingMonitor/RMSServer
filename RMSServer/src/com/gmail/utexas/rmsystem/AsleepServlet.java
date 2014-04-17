package com.gmail.utexas.rmsystem;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gmail.utexas.rmsystem.models.Biometrics;
import com.gmail.utexas.rmsystem.models.Device;
import com.googlecode.objectify.ObjectifyService;

public class AsleepServlet extends HttpServlet{

	Logger log = Logger.getLogger("Asleep");
	static {
        ObjectifyService.register(Biometrics.class);    
    }
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Biometrics biometrics = ofy().load().type(Biometrics.class).id("RMShardware").get();
		biometrics.setStatus(Biometrics.ASLEEP);
		ofy().save().entity(biometrics).now();
		resp.sendRedirect("/");
		
	}

}
