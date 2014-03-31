package com.gmail.utexas.rmsystem;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmail.utexas.rmsystem.models.Device;
import com.google.gson.Gson;
import com.googlecode.objectify.ObjectifyService;


public class GCMHandler {
	private final static String server_key = "AIzaSyAO423ysTxCFWlNtllk-Ms2aQP5UJaBYAs";
	static Logger log = Logger.getLogger(GCMHandler.class.getName());
	static {
		ObjectifyService.register(Device.class);
	}


	public static String sendToApp(String deviceID, String appID, String msg) throws IOException{
		String resp = "";
    	log.setLevel(Level.INFO);
    	
    	String message = createGCMMessage(deviceID, appID, msg);

		URL url = new URL("https://android.googleapis.com/gcm/send");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Authorization", "key="+server_key);		
		connection.setRequestProperty("Content-Type", "application/json");
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
			BufferedReader reader = new BufferedReader(new InputStreamReader(response, charset));
			try {
				for (String line; (line = reader.readLine()) != null;) {
					log.info(line);
				}
			} finally {
				try { reader.close(); } catch (IOException logOrIgnore) {}
			}
		}

		return resp;
	}   

	public static String createGCMMessage(String deviceID, String appID, String message){
		StringBuffer sb = new StringBuffer();
		Device device = ofy().load().type(Device.class).id(deviceID).get();
		
		if(appID == null){
			ArrayList<String> appIDs = device.getAppIDs(); 
			sb.append("{\"registration_ids\":[\"");
			for(int k = 0; k < appIDs.size(); k++){			
				sb.append(appIDs.get(k));
				if(k + 1 != appIDs.size()){
					sb.append(",");
				}
			}
			
		} else {
			sb.append("{\"registration_ids\":[\"");
			sb.append(appID);
		}
		sb.append("\"], \"data\":{");
		
		
		if(!message.equals("")){
			sb.append(message+",");		
		}
		
		boolean status = device.getStatus();
		sb.append("\"activeStatus\":{\"activeFlag\":\""+status);
		sb.append("\"}}}");		
		
		log.info(sb.toString());
		
		return sb.toString();		
	}
}


