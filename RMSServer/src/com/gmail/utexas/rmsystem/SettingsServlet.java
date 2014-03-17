package com.gmail.utexas.rmsystem;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.*;

import java.util.logging.Logger;

import com.gmail.utexas.rmsystem.models.Settings;
import com.google.gson.Gson;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Ref;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class SettingsServlet extends HttpServlet{

    Logger log = Logger.getAnonymousLogger();
	static {
        ObjectifyService.register(Settings.class);
    }


    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

    	log.info(req.getQueryString());    	
    	String id = req.getParameter("id");
    	log.info(id);
    	
        resp.setContentType("text/plain");
        if(id != null){
        	Settings settings = ofy().load().type(Settings.class).id(id).get();        
        	Gson gson = new Gson();        
        	resp.getWriter().println(gson.toJson(settings));
        }        
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