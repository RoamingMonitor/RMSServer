package rmsystem2014;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {
	final static int CAPACITY = 16300;
	static TwoLockQueue q = new TwoLockQueue(CAPACITY);
	
	public static class PushQueue implements Runnable{
		String filename;
		Scanner scan;
		
		public PushQueue() throws FileNotFoundException{
			filename = "data/jessica_walking_paperclip1.txt";
			//filename = "data\\jessica_lying_paperclip1.txt";
			scan = new Scanner(new FileReader(filename));
		}
		@Override
		public void run() {
			while(scan.hasNextInt()){
				q.enq(scan.nextInt());
			}
		}
	}
	
	public static class PullQueue implements Runnable{
		AccelerometerAlgorithm a = new AccelerometerAlgorithm();

		@Override
		public void run() {
			while(true){
				a.processData(q.deq());
			}
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException{
		PushQueue push = new PushQueue();
		PullQueue pull = new PullQueue();
		
		ExecutorService pool = Executors.newCachedThreadPool();
		pool.submit(push);
		pool.submit(pull);
	}
}
