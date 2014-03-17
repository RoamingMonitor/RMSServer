package com.gmail.utexas.rmsystem.algorithms;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


public class QueueTester {
	final static int CAPACITY = 8;
	TwoLockQueue que = null;
		
	public static class TestThread implements Runnable {
		final private static AtomicInteger ticket = new AtomicInteger();
		final private int id = ticket.getAndIncrement();
		final TwoLockQueue que_;
		
		TestThread(TwoLockQueue que) {
			que_ = que;
		}
		
		public void run() {
			Random ran = new Random();
			try {
				Thread.sleep(ran.nextInt(100));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
            try {
			int num = ran.nextInt(100);
			System.out.println("Thread " + id + " is adding: " + num);
			que_.enq(num);
			System.out.println("Que: " + que_.toString());
			
			try {
				Thread.sleep(ran.nextInt(100));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			num = ran.nextInt(100);
			System.out.println("Thread " + id + " is adding: " + num);
			que_.enq(num);
			System.out.println("Que: " + que_.toString());
			
			try {
				Thread.sleep(ran.nextInt(100));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			System.out.println("Thread " + id + " is removing head: " + que_.deq());
			System.out.println("Que: " + que_.toString());
			
			try {
				Thread.sleep(ran.nextInt(100));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			int tail = ran.nextInt(100);
			System.out.println("Thread " + id + " is removing head: " + que_.deq());
			System.out.println("Que: " + que_.toString());
			
			try {
				Thread.sleep(ran.nextInt(100));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			num = ran.nextInt(100);
			System.out.println("Thread " + id + " is adding: " + num);
			que_.enq(num);
			System.out.println("Que: " + que_.toString());
			
			try {
				Thread.sleep(ran.nextInt(100));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			System.out.println("Thread " + id + " is removing head: " + que_.deq());
			
			try {
				Thread.sleep(ran.nextInt(100));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			num = ran.nextInt(100);
			System.out.println("Thread " + id + " is adding: " + num);
			que_.enq(num);
			System.out.println("Que: " + que_.toString());
			System.out.println("Thread "+id+" is finished!");
            } catch (Exception e){
                e.printStackTrace();
            }
		}
	}
	
//	public static void main(String[] argc) {
//		TwoLockQueue queue = new TwoLockQueue(CAPACITY);
//		ExecutorService pool = Executors.newCachedThreadPool();
//		for (int i = 0; i < 4; ++i) pool.submit(new TestThread(queue));
//		pool.shutdown();
//	}
}
