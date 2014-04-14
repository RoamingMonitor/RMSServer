package com.gmail.utexas.rmsystem;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gmail.utexas.rmsystem.models.Device;
import com.googlecode.objectify.ObjectifyService;

public class TestServlet  extends HttpServlet {
	
	Logger log = Logger.getLogger(TestServlet.class.getName());
	static {
		ObjectifyService.register(Device.class);
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String str = req.getParameter("status");
		boolean status = Boolean.parseBoolean(str); 
		Device device = ofy().load().type(Device.class).id("RMShardware").get();		
		device.setBioStatus(status);
		ofy().save().entity(device).now();
		resp.sendRedirect("/");
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	log.setLevel(Level.INFO);
		BufferedReader reader = req.getReader();
		boolean status = false;
		String target = "RMShardware";
		String target1 = req.getHeader("User-Agent");
		String message = "0";
		try {			
			message = reader.readLine();
			log.info(message);			
		} finally {
			reader.close();
		}

		Device device = ofy().load().type(Device.class).id(target).get();
		
		URL url = new URL("http://"+device.getAddress());
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");
		OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
		writer.write(message);
		writer.flush();
		writer.close();
		
		InputStream response = connection.getInputStream();
		String contentType = connection.getHeaderField("Content-Type");
		String charset = null;
		for (String param : contentType.replace(" ", "").split(";")) {
			if (param.startsWith("charset=")) {
				charset = param.split("=", 2)[1];
				break;
			}
		}

		if (charset != null) {
			BufferedReader reader1 = new BufferedReader(new InputStreamReader(response, charset));
			try {
				for (String line; (line = reader1.readLine()) != null;) {
					log.info(line);
				}
			} finally {
				try { reader1.close(); } catch (IOException logOrIgnore) {}
			}
		}
	}
}
