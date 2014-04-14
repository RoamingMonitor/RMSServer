package com.gmail.utexas.rmsystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.*;

import java.util.logging.Logger;

import com.gmail.utexas.rmsystem.models.AccelerometerData;
import com.gmail.utexas.rmsystem.models.Settings;
import com.google.gson.Gson;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Ref;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class SettingsServlet extends HttpServlet{

	Logger log = Logger.getLogger(SettingsServlet.class.getName());
	static {
        ObjectifyService.register(Settings.class);
    }


    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	log.info(req.getQueryString());    	
    	String id = req.getParameter("id");
        resp.setContentType("text/plain");
        
        Settings settings = new Settings(); 
        if(id != null){
        	settings = ofy().load().type(Settings.class).id(id).get();
        } else {
        	settings = ofy().load().type(Settings.class).id(LogMessageHandler.G_APP_DEBUG).get();
        }
    	resp.getWriter().println(settings.duration);
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
        
        Settings settings = gson.fromJson(content, Settings.class);
        ofy().save().entity(settings).now();

        resp.setContentType("text/plain");       
    	resp.getWriter().println("Settings successfully saved.");
    }
}