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

public class BiometricsServlet extends HttpServlet{

	Logger log = Logger.getLogger(BiometricsServlet.class.getName());
	static {
        ObjectifyService.register(Biometrics.class);
        ObjectifyService.register(Device.class);
    }
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String type = req.getParameter("type");
		Biometrics biometrics = ofy().load().type(Biometrics.class).id("RMShardware").get();
		if(type != null){
			biometrics.setStatus(type);
			ofy().save().entity(biometrics).now();
			resp.sendRedirect("/");
		} else {
			resp.getWriter().println(biometrics.getStatus());
		}
	}

}
