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
				//loop until out of current data
				if(data.size() > 0){
//					log.info("putting data into queue");
					int d = data.get(0);
					q.enq(d);
					data.remove(0);
				}
				else{
//					log.info("data empty");
					//current data empty...sleep
					try{
						Thread.sleep(3000);
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
				a.processData(q.deq());
			}
		}
	}
}
