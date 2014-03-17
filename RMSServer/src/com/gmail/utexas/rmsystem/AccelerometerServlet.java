package com.gmail.utexas.rmsystem;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gmail.utexas.rmsystem.algorithms.AccelerometerAlgorithm;

public class AccelerometerServlet extends HttpServlet{
	
	AccelerometerAlgorithm algorithm = new AccelerometerAlgorithm();

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		 BufferedReader reader = req.getReader();
	        try {
	            String line;
	            while ((line = reader.readLine()) != null) {
	                // process input
	            }
	        } finally {
	            reader.close();
	        }
	}
	

}
