/*
!-------------------------------------------------------------------------!
!									  !
!	 N  A  S     P A R A L L E L	 B E N C H M A R K S  3.0	  !
!									  !
!			J A V A 	V E R S I O N			  !
!									  !
!                               R p r j                                   !
!                                                                         !
!-------------------------------------------------------------------------!
!                                                                         !
!    Rprj implements thread for Rprj subroutine of MG benchmark.          !
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

public class Rprj extends MGBase {

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
		int wstart, wend, m1k, m2k, m3k, m1j, m2j, m3j, roff, zoff;
		boolean exit = false;

		public Work() {
			this.exit = true;
		}

		public Work(int wstart, int wend, int m1k, int m2k, int m3k, int m1j,
				int m2j, int m3j, int roff, int zoff) {

			this.wstart = wstart;
			this.wend = wend;
			this.m1k = m1k;
			this.m2k = m2k;
			this.m3k = m3k;
			this.m1j = m1j;
			this.m2j = m2j;
			this.m3j = m3j;
			this.roff = roff;
			this.zoff = zoff;
		}
	}

	public Rprj(InputPort initInputPort, InputPort doInputPort,
			OutputPort doneOutputPort) {

		this.doInputPort = doInputPort;
		this.doneOutputPort = doneOutputPort;

		Init init = (Init) initInputPort.getUninterruptibly();

		RprjConstructor(init.mg);
		this.id = init.id;
	}

	// ------------------------------------------------------------------------

	public int id;
	// public boolean done = true;

	public int m1k, m2k, m3k;
	public int m1j, m2j, m3j;
	public int zoff, roff;

	int start, end, work;
	int state = 0;
	double x1[], y1[];

	public void RprjConstructor(MG mg) {
		// public Rprj(MG mg) {
		Init(mg);
		x1 = new double[nm + 1];
		y1 = new double[nm + 1];
		setPriority(Thread.MAX_PRIORITY);
		setDaemon(true);
		master = mg;
	}

	void Init(MG mg) {
		// initialize shared data
		num_threads = mg.num_threads;
		r = mg.r;
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
			m1k = work.m1k;
			m2k = work.m2k;
			m3k = work.m3k;
			m1j = work.m1j;
			m2j = work.m2j;
			m3j = work.m3j;
			roff = work.roff;
			zoff = work.zoff;

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
		int j3, j2, j1, i3, i2, i1, d1, d2, d3, j;
		double x2, y2;

		if (work == 0)
			return;

		if (m1k == 3)
			d1 = 2;
		else
			d1 = 1;
		if (m2k == 3)
			d2 = 2;
		else
			d2 = 1;
		if (m3k == 3)
			d3 = 2;
		else
			d3 = 1;

		for (j3 = start; j3 <= end; j3++) {
			i3 = 2 * j3 - d3 - 1;
			for (j2 = 2; j2 <= m2j - 1; j2++) {
				i2 = 2 * j2 - d2 - 1;
				for (j1 = 2; j1 <= m1j; j1++) {
					i1 = 2 * j1 - d1 - 1;
					x1[i1 - 1] = r[roff + i1 - 1 + m1k * (i2 - 1 + m2k * i3)]
							+ r[roff + i1 - 1 + m1k * (i2 + 1 + m2k * i3)]
							+ r[roff + i1 - 1 + m1k * (i2 + m2k * (i3 - 1))]
							+ r[roff + i1 - 1 + m1k * (i2 + m2k * (i3 + 1))];
					y1[i1 - 1] = r[roff + i1 - 1 + m1k
							* (i2 - 1 + m2k * (i3 - 1))]
							+ r[roff + i1 - 1 + m1k * (i2 - 1 + m2k * (i3 + 1))]
							+ r[roff + i1 - 1 + m1k * (i2 + 1 + m2k * (i3 - 1))]
							+ r[roff + i1 - 1 + m1k * (i2 + 1 + m2k * (i3 + 1))];
				}

				for (j1 = 2; j1 <= m1j - 1; j1++) {
					i1 = 2 * j1 - d1 - 1;
					y2 = r[roff + i1 + m1k * (i2 - 1 + m2k * (i3 - 1))]
							+ r[roff + i1 + m1k * (i2 - 1 + m2k * (i3 + 1))]
							+ r[roff + i1 + m1k * (i2 + 1 + m2k * (i3 - 1))]
							+ r[roff + i1 + m1k * (i2 + 1 + m2k * (i3 + 1))];
					x2 = r[roff + i1 + m1k * (i2 - 1 + m2k * i3)]
							+ r[roff + i1 + m1k * (i2 + 1 + m2k * i3)]
							+ r[roff + i1 + m1k * (i2 + m2k * (i3 - 1))]
							+ r[roff + i1 + m1k * (i2 + m2k * (i3 + 1))];
					r[zoff + j1 - 1 + m1j * (j2 - 1 + m2j * (j3 - 1))] = 0.5
							* r[roff + i1 + m1k * (i2 + m2k * i3)]
							+ 0.25
							* (r[roff + i1 - 1 + m1k * (i2 + m2k * i3)]
									+ r[roff + i1 + 1 + m1k * (i2 + m2k * i3)] + x2)
							+ 0.125 * (x1[i1 - 1] + x1[i1 + 1] + y2) + 0.0625
							* (y1[i1 - 1] + y1[i1 + 1]);
				}
			}
		}
		for (j3 = start - 1; j3 <= end - 1; j3++)
			for (j2 = 1; j2 <= m2j - 1; j2++) {
				r[zoff + m1j * (j2 + m2j * j3)] = r[zoff + m1j - 2 + m1j
						* (j2 + m2j * j3)];
				r[zoff + m1j - 1 + m1j * (j2 + m2j * j3)] = r[zoff + 1 + m1j
						* (j2 + m2j * j3)];
			}

		for (j3 = start - 1; j3 <= end - 1; j3++)
			for (j1 = 0; j1 <= m1j; j1++) {
				r[zoff + j1 + m1j * m2j * j3] = r[zoff + j1 + m1j
						* (m2j - 2 + m2j * j3)];
				r[zoff + j1 + m1j * (m2j - 1 + m2j * j3)] = r[zoff + j1 + m1j
						* (1 + m2j * j3)];
			}
	}

	void GetWork() {
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
