import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.time.Duration;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.ThreadLocalRandom;

public class Concurrency {
	
	// Data Fields
	private static final double ONE_HUNDRED_MILLION = 100_000_000.0;
	private static final double TEN_BILLION = 10_000_000_000.0;
	private volatile double sum; // sum of values\
	// Object for thread locking
	private final Object lock = new Object();
	// Time tracking
    private long concurrentTimeElapsed = 0;
	
	public void addToTenBillionConcurrently() {
		Thread[] taskList = new Thread[100];
		double start = 1;
		double end = ONE_HUNDRED_MILLION;
		
		long startTime = System.nanoTime();
		
		for (int i = 0; i < 100; i++) {
			final double threadStart = start;
			final double threadEnd = end;
			
            taskList[i] = new Thread(() -> addToOneHundredMillion(threadStart, threadEnd));
//            String threadName = String.format("Thread%d", i);
//            taskList[i].setName(threadName);
            taskList[i].start();
            
            start += ONE_HUNDRED_MILLION;
            end += ONE_HUNDRED_MILLION;
		}
		
		 // Wait for all threads to finish
        for (Thread t : taskList) {
        	try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        
        // Convert nanoseconds to seconds
        double seconds = duration / 1_000_000_000.0;
        System.out.println("Time elapsed: " + seconds + " seconds");
        System.out.println("Total sum: " + sum);
	} // end addToTenBillionConcurrently
	
	public void addToOneHundredMillion(double start, double end) {
		double localSum = 0;
		
		// long startTime = System.nanoTime();
		for (double i = start; i <= end; i++)
        {
            localSum += i;
        }
		synchronized(lock) {
			sum += localSum;
		}
		// long endTime = System.nanoTime();
        // long duration = endTime - startTime;
        // System.out.println("Time elapsed: " + duration + " seconds");
	} // end addToOneHundredMillion
	
	public void sumToTenBillion() {
		double sum = 0;
		long startTime = System.nanoTime();
		for (double i = 1; i <= TEN_BILLION; i++) {
			sum += i;
		}
		long endTime = System.nanoTime();
        long duration = endTime - startTime;
        
        // Convert nanoseconds to seconds
        double seconds = duration / 1_000_000_000.0;
        System.out.println("Time elapsed: " + seconds + " seconds");
        System.out.println("Total sum: " + sum);
	}
	
	public static void main(String[] args) {
		Concurrency c = new Concurrency();
		//c.sumToTenBillion();
		c.addToTenBillionConcurrently();
	} // end main

} // end class
