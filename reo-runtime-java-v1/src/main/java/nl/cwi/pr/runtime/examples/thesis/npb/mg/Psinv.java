/*
!-------------------------------------------------------------------------!
!									  !
!	 N  A  S     P A R A L L E L	 B E N C H M A R K S  3.0	  !
!									  !
!			J A V A 	V E R S I O N			  !
!									  !
!                               P s i n v                                 !
!                                                                         !
!-------------------------------------------------------------------------!
!                                                                         !
!    Psinv implements thread for Psinv subroutine of MG benchmark.        !
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
package nl.cwi.pr.runtime.examples.thesis.npb.mg;

import nl.cwi.pr.runtime.api.InputPort;
import nl.cwi.pr.runtime.api.OutputPort;

public class Psinv extends MGBase {

	// ------------------------------------------------------------------------

	private InputPort doInputPort;
	private OutputPort doneOutputPort;

	public static class Init {
		MG mg;
		int id;

		public Init(MG mg, int id) {
			this.mg = mg;
			this.id = id;
		}
	}

	public static class Work {
		int wstart, wend, n1, n2, n3, roff, uoff;
		boolean exit = false;

		public Work() {
			this.exit = true;
		}

		public Work(int wstart, int wend, int n1, int n2, int n3, int roff,
				int uoff) {

			this.wstart = wstart;
			this.wend = wend;
			this.n1 = n1;
			this.n2 = n2;
			this.n3 = n3;
			this.roff = roff;
			this.uoff = uoff;
		}
	}

	public Psinv(InputPort initInputPort, InputPort doInputPort,
			OutputPort doneOutputPort) {

		this.doInputPort = doInputPort;
		this.doneOutputPort = doneOutputPort;

		Init init = (Init) initInputPort.getUninterruptibly();

		PsinvConstructor(init.mg);
		this.id = init.id;
	}

	// ------------------------------------------------------------------------

	public int id;
	// public boolean done = true;

	public int n1, n2, n3;
	public int roff, uoff;

	int start, end, work;
	int state = 0;
	double r1[], r2[];

	public void PsinvConstructor(MG mg) {
		// public Psinv(MG mg) {
		Init(mg);
		r1 = new double[nm + 1];
		r2 = new double[nm + 1];
		setPriority(Thread.MAX_PRIORITY);
		setDaemon(true);
		master = mg;
	}

	void Init(MG mg) {
		// initialize shared data
		num_threads = mg.num_threads;
		r = mg.r;
		u = mg.u;
		c = mg.c;
		nm = mg.nm;
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
			Work work = (Work) doInputPort.getUninterruptibly();
			wstart = work.wstart;
			wend = work.wend;
			n1 = work.n1;
			n2 = work.n2;
			n3 = work.n3;
			roff = work.roff;
			uoff = work.uoff;

			if (work.exit)
				return;

			GetWork();
			step();
			// synchronized (master) {
			// done = true;
			// master.notify();
			// }
			doneOutputPort.putUninterruptibly(signal);
			// }
		}
	}

	public void step() {
		int i3, i2, i1;
		if (work == 0)
			return;
		for (i3 = start; i3 <= end; i3++) {
			for (i2 = 1; i2 < n2 - 1; i2++) {
				for (i1 = 0; i1 < n1; i1++) {
					r1[i1] = r[roff + i1 + n1 * (i2 - 1 + n2 * i3)]
							+ r[roff + i1 + n1 * (i2 + 1 + n2 * i3)]
							+ r[roff + i1 + n1 * (i2 + n2 * (i3 - 1))]
							+ r[roff + i1 + n1 * (i2 + n2 * (i3 + 1))];
					r2[i1] = r[roff + i1 + n1 * (i2 - 1 + n2 * (i3 - 1))]
							+ r[roff + i1 + n1 * (i2 + 1 + n2 * (i3 - 1))]
							+ r[roff + i1 + n1 * (i2 - 1 + n2 * (i3 + 1))]
							+ r[roff + i1 + n1 * (i2 + 1 + n2 * (i3 + 1))];
				}
				for (i1 = 1; i1 < n1 - 1; i1++) {
					u[uoff + i1 + n1 * (i2 + n2 * i3)] += c[0]
							* r[roff + i1 + n1 * (i2 + n2 * i3)]
							+ c[1]
							* (r[roff + i1 - 1 + n1 * (i2 + n2 * i3)]
									+ r[roff + i1 + 1 + n1 * (i2 + n2 * i3)] + r1[i1])
							+ c[2] * (r2[i1] + r1[i1 - 1] + r1[i1 + 1]);
					// c---------------------------------------------------------------------
					// c Assume c(3) = 0 (Enable line below if c(3) not= 0)
					// c---------------------------------------------------------------------
					// c > + c(3) * ( r2(i1-1) + r2(i1+1) )
					// c---------------------------------------------------------------------
				}
			}
		}

		// c---------------------------------------------------------------------
		// c exchange boundary points
		// c---------------------------------------------------------------------
		// System.out.println(id+" "+start+" "+end);
		for (i3 = start; i3 <= end; i3++)
			for (i2 = 1; i2 < n2 - 1; i2++) {
				u[uoff + n1 * (i2 + n2 * i3)] = u[uoff + n1 - 2 + n1
						* (i2 + n2 * i3)];
				u[uoff + n1 - 1 + n1 * (i2 + n2 * i3)] = u[uoff + 1 + n1
						* (i2 + n2 * i3)];
			}

		for (i3 = start; i3 <= end; i3++)
			for (i1 = 0; i1 < n1; i1++) {
				u[uoff + i1 + n1 * n2 * i3] = u[uoff + i1 + n1
						* (n2 - 2 + n2 * i3)];
				u[uoff + i1 + n1 * (n2 - 1 + n2 * i3)] = u[uoff + i1 + n1
						* (1 + n2 * i3)];
			}
	}

	public void GetWork() {
		int workpt = (wend - wstart) / num_threads;
		int remainder = wend - wstart - workpt * num_threads;
		if (workpt == 0) {
			if (id <= wend - wstart) {
				work = 1;
				start = end = wstart + id;
			} else {
				work = 0;
			}
		} else {
			if (id < remainder) {
				workpt++;
				start = wstart + workpt * id;
				end = start + workpt - 1;
				work = workpt;
			} else {
				start = wstart + remainder + workpt * id;
				end = start + workpt - 1;
				work = workpt;
			}
		}
	}
}
