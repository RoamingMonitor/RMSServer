package rmsystem2014;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TwoLockQueue {

  ReentrantLock enqLock, deqLock;
  Condition notEmpty, notFull;
  AtomicInteger size;
  Entry head;
  Entry tail;
  int capacity;
  public TwoLockQueue(int capacity) {
    this.capacity = capacity;
    this.head = new Entry();
    this.tail = head;
    this.size = new AtomicInteger();
    this.enqLock = new ReentrantLock();
    this.notFull= enqLock.newCondition();
    this.deqLock = new ReentrantLock();
    this.notEmpty= deqLock.newCondition();
  }
  public int deq() {
    int result;
    boolean mustWakeEnqueuers = false;
    deqLock.lock();
    try {
      while (size.get() == 0) {
        try {
          notEmpty.await();
        } catch (InterruptedException ex) {}
      }
      result = head.next.value;
      head = head.next;
      if(size.getAndDecrement() == capacity){
    	  mustWakeEnqueuers = true;
      }
    } finally {
      deqLock.unlock();
    }
    if(mustWakeEnqueuers){
    	enqLock.lock();
    	try{
    		notFull.signalAll();
    	}finally{
    		enqLock.unlock();
    	}
    }
	return result;
  }
  public void enq(int x) {
	boolean mustWakeDequeuers = false;
    enqLock.lock();
    //System.out.println("size is: "+size.get());
    try {
      while (size.get() == capacity) {
        try {
          notFull.await();
        } catch (InterruptedException e) {}
      }
      Entry e = new Entry(x);
      tail.next = e;
      tail = e;
      if(size.getAndIncrement() == 0){
    	  mustWakeDequeuers = true;
      }
    } finally {
      enqLock.unlock();
    }
    if(mustWakeDequeuers){
    	deqLock.lock();
    	try{
    		notEmpty.signalAll();
    	}
    	finally{
    		deqLock.unlock();
    	}
    }
  }
  public String toString(){
	  String out = "";
	  enqLock.lock();
	  deqLock.lock();
	  try{
		  Entry temp = head;
		  for(int i =0; i<size.get(); i++){
			  if(temp.next != null){
				  out+= temp.next.value+" ";
				  temp = temp.next;
			  }
		  }
	  }finally{
		  enqLock.unlock();
		  deqLock.unlock();
	  }
	  return out;
  }
  protected class Entry {
    public int value;
    public Entry next;
    public Entry(){
    	value = 0;
    	next = null;
    }
    public Entry(int x) {
      value = x;
      next = null;
    }
  }
}
