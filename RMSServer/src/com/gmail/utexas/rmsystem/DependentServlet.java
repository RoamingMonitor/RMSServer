package com.gmail.utexas.rmsystem;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gmail.utexas.rmsystem.models.RMSUser;
import com.googlecode.objectify.ObjectifyService;

public class DependentServlet extends HttpServlet{

	static {
        ObjectifyService.register(RMSUser.class);
    }
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String status = req.getParameter("status");
		RMSUser user = ofy().load().type(RMSUser.class).id(LogMessageHandler.G_APP_DEBUG).get();
		user.setDependentStatus(status);
		ofy().save().entity(user).now();
	}
}
