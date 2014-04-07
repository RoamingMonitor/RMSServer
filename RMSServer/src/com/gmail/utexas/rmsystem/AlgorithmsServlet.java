package com.gmail.utexas.rmsystem;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gmail.utexas.rmsystem.algorithms.AccelerometerAlgorithm;
import com.gmail.utexas.rmsystem.algorithms.AlgorithmQueue.PullQueue;
import com.gmail.utexas.rmsystem.algorithms.AlgorithmQueue.PushQueue;
import com.gmail.utexas.rmsystem.models.AccelerometerData;
import com.gmail.utexas.rmsystem.models.Device;
import com.google.appengine.api.ThreadManager;
import com.googlecode.objectify.ObjectifyService;

public class AlgorithmsServlet extends HttpServlet{

	static {
        ObjectifyService.register(AccelerometerData.class);
        ObjectifyService.register(Device.class);
    }

	Logger log = Logger.getLogger(AlgorithmsServlet.class.getName());
	String deviceID = "RMShardware";
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {			
		PrintWriter writer = resp.getWriter();		
		AccelerometerData dataObject = ofy().load().type(AccelerometerData.class).id(deviceID).get();
		try{
			writer.print(dataObject.getData()+"");
		} finally{
			writer.close();
		}
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {		
		log.setLevel(Level.INFO);
		log.info("Starting algorithms!");
		
		PushQueue push = new PushQueue();
		PullQueue pull = new PullQueue();
		
		
		Thread pushThread = ThreadManager.createBackgroundThread(push);
		Thread pullThread = ThreadManager.createBackgroundThread(pull);
		
		pushThread.start();
		pullThread.start();
		log.info("Launching algorithm threads!");			
	}
	
	


}
