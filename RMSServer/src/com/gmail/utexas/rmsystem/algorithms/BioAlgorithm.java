package com.gmail.utexas.rmsystem.algorithms;

import java.util.ArrayList;

public class BioAlgorithm {
	private boolean sleep;
	private ArrayList<Integer> bioBuf;
	private final int BUFSIZE = 512;
	private int bufIn, bufOut, dataCount;
	
	public BioAlgorithm() {
		sleep = true;
		bioBuf = new ArrayList<Integer>();
		bufIn = 0;
		bufOut = 0; 
		dataCount = 0;
	}
	public boolean isAsleep(){
		//returns true if in sleep mode, false otherwise
		return sleep;
	}
	public void activate(){
		//set sleep to false and reinitialize all values
		bioBuf.clear();
		bufIn=bufOut=dataCount=0;
		sleep = false;
	}
	public void deactivate(){
		sleep = true;
	}

}
