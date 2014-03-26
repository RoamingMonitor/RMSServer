package com.gmail.utexas.rmsystem.algorithms;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Logger;

import com.gmail.utexas.rmsystem.ManualStatusServlet;

public class AccelerometerAlgorithm {
	private ArrayList<Integer> buf;
	//private final int BUFSIZE = 14969;
	private final int BUFSIZE = 16300;
	private final int MAX_LOW = 150;
	private final int MAX_HIGH = 150;
	private final int MAX_NORM = 500;
	private final int THRESH_HI = 2500;
	private final int THRESH_LOW = 2200;
	private final int REQSTEPS = 5;
	private final int TOOHIGH = 3000;
	private final int TOOLOW = 1500;
	private int bufIn, bufOut, dataCount, lowCount, highCount, stepCount, normCount;
	private boolean prelimOn;
	private BioAlgorithm bio;
	
	Logger log = Logger.getLogger(AccelerometerAlgorithm.class.getName());

	public AccelerometerAlgorithm() {
		buf = new ArrayList<Integer>();
		bufIn = 0;
		bufOut = 0;
		dataCount = 0;
		lowCount = 0;
		bio = new BioAlgorithm();
		prelimOn = false;
	}
	
	//takes in element from array and handles triggering of events due to data
	public void processData(int data){
		bufOut++;	//used for debugging where triggers occur (remove when done testing)
		
		//discard vals that are too low
		if(data <= TOOLOW){
			System.out.println("Value too low!");
			resetData();
		}
		
		//check if low end of a step
		else if(data <= THRESH_LOW){
			//check if first low step and need to turn on biometrics
			if(lowCount == 1 && stepCount == 0){
				//System.out.println("Preliminary detection started!");
				prelimOn = true;
				activateBio();
			}
			lowCount++;
			normCount = 0;
			//check for too many low vals in a row
			if(lowCount > MAX_LOW){
				System.out.println("Too many low vals in a row!");
				resetData();
			}
		}
		
		//discard vals that are too high
		else if(data >= TOOHIGH){
			System.out.println("Too many high vals in a row!");
			resetData();
		}
		
		//check for high end of step (only if low end has already been detected...aka prelim is ON)
		else if(data >= THRESH_HI && prelimOn){
			highCount++;
			normCount = 0;
			//check if not too many high vals in a row
			if(highCount <= MAX_HIGH){
				if(lowCount != 0){
					stepCount++;
					System.out.println("Step count increased to: "+stepCount+" @ "+bufOut);
					lowCount = 0;
					highCount = 0;
				}
				if(stepCount >= REQSTEPS){
					sendWalkingAlert();
				}
			}
			else{
				System.out.println("Too many high values in a row...stopping prelim detection");
				resetData();
			}
		}
		
		//data is in normal range
		else if(data < THRESH_HI && data > THRESH_LOW){
			normCount++;
			if(normCount >= MAX_NORM){
				System.out.println("Too many vals in norm range @ "+bufOut+"...stopping prelim");
				resetData();
			}
		}
	}
	public void resetData(){
		System.out.println("Reseting accelerometer algorithm data");
		highCount=lowCount=stepCount=normCount=0;
		prelimOn = false;
		deactivateBio();
	}
	public void activateBio(){
		//make new thread to start processing biometric data
		//System.out.println("Turn biometrics on!");
		bio.activate();
	}
	public void deactivateBio(){
		//make new thread to handle turning biometrics off
		//System.out.println("Turn biometrics off!");
		bio.deactivate();
	}
	public void sendWalkingAlert(){
		//handle walking alert to phone app
		System.out.println("Send walking alert!");
		log.info("Send walking alert!");
	}
}
