/*
!-------------------------------------------------------------------------!
!									  !
!	 N  A  S     P A R A L L E L	 B E N C H M A R K S  3.0	  !
!									  !
!			J A V A 	V E R S I O N			  !
!									  !
!                         R a n k T h r e a d                             !
!                                                                         !
!-------------------------------------------------------------------------!
!                                                                         !
!    RankThread implements thread for RankThread subroutine of IS         !    
!    benchmark.                                                           !
!									  !
!    Permission to use, copy, distribute and modify this software	  !
!    for any purpose with or without fee is hereby granted.  We 	  !
!    request, however, that all derived work reference the NAS  	  !
!    Parallel Benchmarks 3.0. This software is provided "as is" 	  !
!    without express or implied warranty.				  !
!									  !
!    Information on NPB 3.0, including the Technical Report NAS-02-008	  !
!    "Implementation of the NAS Parallel Benchmarks in Java",		  !
!    original specifications, source code, results and information	  !
!    on how to submit new results, is available at:			  !
!									  !
!	    http://www.nas.nasa.gov/Software/NPB/			  !
!									  !
!    Send comments or suggestions to  npb@nas.nasa.gov  		  !
!									  !
!	   NAS Parallel Benchmarks Group				  !
!	   NASA Ames Research Center					  !
!	   Mail Stop: T27A-1						  !
!	   Moffett Field, CA   94035-1000				  !
!									  !
!	   E-mail:  npb@nas.nasa.gov					  !
!	   Fax:     (650) 604-3957					  !
!									  !
!-------------------------------------------------------------------------!
! Translation to Java and MultiThreaded Code				  !
!	   M. Frumkin							  !
!	   M. Schultz							  !
!-------------------------------------------------------------------------!
 */
package nl.cwi.pr.runtime.examples.thesis.npb.is;

import nl.cwi.pr.runtime.api.InputPort;
import nl.cwi.pr.runtime.api.OutputPort;

public class RankThread extends ISBase {

	// ------------------------------------------------------------------------

	private InputPort doInputPort;
	private OutputPort doneOutputPort;

	private int[][] local_hists;

	public static class Init {
		IS is;
		int Id, s1, e1, s2, e2;
		int[] local_hist;
		int[][] local_hists;

		public Init(IS is, int Id, int s1, int e1, int s2, int e2,
				int[] local_hist, int[][] local_hists) {
			this.is = is;
			this.Id = Id;
			this.s1 = s1;
			this.e1 = e1;
			this.s2 = s2;
			this.e2 = e2;
			this.local_hist = local_hist;
			this.local_hists = local_hists;
		}
	}

	public static class Work {
		boolean exit = false;

		public Work() {
			this.exit = true;
		}
	}

	public RankThread(InputPort initInputPort, InputPort doInputPort,
			OutputPort doneOutputPort) {

		this.doInputPort = doInputPort;
		this.doneOutputPort = doneOutputPort;

		Init init = (Init) initInputPort.getUninterruptibly();

		RankThreadConstructor(init.is, init.Id, init.s1, init.e1, init.s2,
				init.e2);

		this.local_hist = init.local_hist;
		this.local_hists = init.local_hists;
	}

	// ------------------------------------------------------------------------

	public int id;
	protected int local_hist[];
	int start, end;
	int rstart, rend;

	// public boolean done = true;
	public static int iteration = 0;
	public int state;

	public void RankThreadConstructor(IS is, int Id, int s1, int e1, int s2,
			int e2) {
		// public RankThread(IS is, int Id, int s1, int e1, int s2, int e2) {
		Init(is);
		master = is;
		id = Id;
		start = s1;
		end = e1;
		rstart = s2;
		rend = e2;
		// local_hist = new int[MAX_KEY];
		state = 0;
		setPriority(Thread.MAX_PRIORITY);
		setDaemon(true);
	}

	void Init(IS is) {
		// initialize shared data
		num_threads = is.num_threads;
		MAX_KEY = is.MAX_KEY;

		key_array = is.key_array;
		test_index_array = is.test_index_array;
		master_hist = is.master_hist;
		partial_verify_vals = is.partial_verify_vals;
	}

	public void run() {
		Object signal = new Object();
		for (;;) {
			// synchronized (this) {
			// while (done == true) {
			// try {
			// wait();
			// synchronized (master) {
			// master.notify();
			// }
			// } catch (InterruptedException ie) {
			// }
			// }
			Object object = doInputPort.getUninterruptibly();

			if (object instanceof Work)
				return;

			switch (state) {
			case 0:
				step1();
				state = 1;
				break;
			case 1:
				step2();
				state = 0;
				break;
			}
			// synchronized (master) {
			// done = true;
			// master.notify();
			// }
			doneOutputPort.putUninterruptibly(signal);
			// }
		}
	}

	protected synchronized void step1() {
		key_array[iteration] = iteration;
		key_array[iteration + MAX_ITERATIONS] = MAX_KEY - iteration;
		for (int i = 0; i < TEST_ARRAY_SIZE; i++) {
			partial_verify_vals[i] = key_array[test_index_array[i]];
		}

		for (int i = 0; i < MAX_KEY; i++)
			local_hist[i] = 0;
		for (int i = start; i <= end; i++)
			local_hist[key_array[i]]++;
		for (int i = 0; i < MAX_KEY - 1; i++)
			local_hist[i + 1] += local_hist[i];
	}

	public void step2() {
		// Parallel calculation of the master's histogram
		for (int i = rstart; i <= rend; i++) {
			for (int j = 0; j < num_threads; j++) {
				// master_hist[i] += rankthreads[j].local_hist[i];
				master_hist[i] += local_hists[j][i];
			}
		}
	}
}
