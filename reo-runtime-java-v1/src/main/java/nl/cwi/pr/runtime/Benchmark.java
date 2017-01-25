package nl.cwi.pr.runtime;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;

public class Benchmark {

	//
	// STATIC - FIELDS
	//

	public static final AtomicLong N_GETS = new AtomicLong();
	public static final AtomicLong N_PUTS = new AtomicLong();
	public static final Semaphore SEMAPHORE = new Semaphore(0);
	
	public static volatile CyclicBarrier BARRIER; 

	public static volatile int MEASURE_TIME = 3;
	public static volatile int WARM_UP_TIME = 1;
	
	public static volatile boolean IS_ENABLED = false;
	
	public static volatile int PROD_WORK_TIME = 0;
	public static volatile int CONS_WORK_TIME = 0;
	public static volatile long SIDE_EFFECT = 0;

	//
	// STATIC - METHODS
	//

	public static void spin(final int time) {
		long deadline = System.nanoTime() + time;
		while (System.nanoTime() < deadline)
			SIDE_EFFECT = deadline;
	}
}
