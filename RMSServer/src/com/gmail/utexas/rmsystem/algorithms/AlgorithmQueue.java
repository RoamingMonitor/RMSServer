package com.gmail.utexas.rmsystem.algorithms;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import org.mortbay.log.Log;

import com.gmail.utexas.rmsystem.AlgorithmsServlet;
import com.gmail.utexas.rmsystem.DatastoreHandler;
import com.gmail.utexas.rmsystem.models.AccelerometerData;
import com.gmail.utexas.rmsystem.models.Device;
import com.googlecode.objectify.ObjectifyService;

public class AlgorithmQueue {
	final static int CAPACITY = 16300;
	static TwoLockQueue q = new TwoLockQueue(CAPACITY);
	static int snooze;
	
	static Logger log = Logger.getLogger("Algorithm Queue");
	
	static {
        ObjectifyService.register(AccelerometerData.class); 
        ObjectifyService.register(Device.class);
    }
	
	
	public static class PushQueue implements Runnable{
		String filename;
		Scanner scan;
		Logger log = Logger.getLogger("Algorithms: ");
		
		public PushQueue(){	}
		
		public void run() {
			//get original dataObject
			log.info("Pushqueue running");
			String deviceID = "RMShardware";
			
			ArrayList<Integer> data = DatastoreHandler.getData();	
			
			while(true){
				if(data.size() > 0){
					//get available data and put to queue
					int d = data.get(0);
					q.enq(d);
					data.remove(0);
				}
				else{
					//current data empty...sleep
					try{
						Thread.sleep(1000);
					}catch(InterruptedException e){}
					
					data = DatastoreHandler.getData();
				}
			}
		}
	}
	
	public static class PullQueue implements Runnable{
		AccelerometerAlgorithm a = new AccelerometerAlgorithm();

		public void run() {
			while(true){
				snooze = DatastoreHandler.getSnooze();
				if(snooze > 0){
					log.info("got a snooze: " + snooze);
					DatastoreHandler.resetSnooze();
					a.snoozeAlert(snooze);
				}
				a.processData(q.deq());
			}
		}
	}
}
