package rmsystem2014;

public class Biometrics {
	public static final String ASLEEP = "ASLEEP";
	public static final String AWAKE = "AWAKE";
	public static final String BADINPUT = "BAD INPUT";
	public static final String FALSEPOSITIVE = "FALSE POSITIVE";
	private String status;
	
	public Biometrics(){
		status = AWAKE;
	}
	public void setAsleep(){
		status = ASLEEP;
	}
	public void setAwake(){
		status = AWAKE;
	}
	public void setBadInput(){
		status = BADINPUT;
	}
	public void setFalsePositive(){
		status = FALSEPOSITIVE;
	}
	public String getStatus(){
		return status;
	}
}
