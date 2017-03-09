/*
!-------------------------------------------------------------------------!
!									  !
!	 N  A  S     P A R A L L E L	 B E N C H M A R K S  3.0	  !
!									  !
!			J A V A 	V E R S I O N			  !
!									  !
!                               I n t e r p                               !
!                                                                         !
!-------------------------------------------------------------------------!
!                                                                         !
!    Interp implements thread for Interp subroutine of MG benchmark.      !
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

public class Interp extends MGBase {

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
		int wstart, wend, mm1, mm2, mm3, n1, n2, n3, zoff, uoff;
		boolean exit = false;

		public Work() {
			this.exit = true;
		}

		public Work(int wstart, int wend, int mm1, int mm2, int mm3, int n1,
				int n2, int n3, int zoff, int uoff) {

			this.wstart = wstart;
			this.wend = wend;
			this.mm1 = mm1;
			this.mm2 = mm2;
			this.mm3 = mm3;
			this.n1 = n1;
			this.n2 = n2;
			this.n3 = n3;
			this.zoff = zoff;
			this.uoff = uoff;
		}
	}

	public Interp(InputPort initInputPort, InputPort doInputPort,
			OutputPort doneOutputPort) {

		this.doInputPort = doInputPort;
		this.doneOutputPort = doneOutputPort;

		Init init = (Init) initInputPort.getUninterruptibly();

		InterpConstructor(init.mg);
		this.id = init.id;
	}

	// ------------------------------------------------------------------------

	public int id;
	// public boolean done = true;

	public int mm1, mm2, mm3;
	public int n1, n2, n3;
	public int zoff, uoff;

	int start, end, work;
	int state = 0;
	double z1[], z2[], z3[];

	public void InterpConstructor(MG mg) {
		// public Interp(MG mg) {
		Init(mg);
		int m = 535;
		z1 = new double[m];
		z2 = new double[m];
		z3 = new double[m];
		setPriority(Thread.MAX_PRIORITY);
		setDaemon(true);
		master = mg;
	}

	void Init(MG mg) {
		// initialize shared data
		num_threads = mg.num_threads;
		u = mg.u;
	}

	public void run() {
		Object signal = new Object();
		for (;;) {
			// synchronized (this) {
			// while (done == true) {
			// // This is an extra notity to compensate for lost
			// // notification of
			// // master thread when the benchmark runs under server on
			// // SUN(1.1.3)
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
			mm1 = work.mm1;
			mm2 = work.mm2;
			mm3 = work.mm3;
			n1 = work.n1;
			n2 = work.n2;
			n3 = work.n3;
			zoff = work.zoff;
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
		if (work == 0)
			return;
		int i3, i2, i1, d1, d2, d3, t1, t2, t3;
		if (n1 != 3 && n2 != 3 && n3 != 3) {
			for (i3 = start; i3 <= end; i3++) {
				for (i2 = 1; i2 <= mm2 - 1; i2++) {
					for (i1 = 1; i1 <= mm1; i1++) {
						z1[i1 - 1] = u[zoff + i1 - 1 + mm1
								* (i2 + mm2 * (i3 - 1))]
								+ u[zoff + i1 - 1 + mm1
										* (i2 - 1 + mm2 * (i3 - 1))];
						z2[i1 - 1] = u[zoff + i1 - 1 + mm1
								* (i2 - 1 + mm2 * i3)]
								+ u[zoff + i1 - 1 + mm1
										* (i2 - 1 + mm2 * (i3 - 1))];
						z3[i1 - 1] = u[zoff + i1 - 1 + mm1 * (i2 + mm2 * i3)]
								+ u[zoff + i1 - 1 + mm1 * (i2 - 1 + mm2 * i3)]
								+ z1[i1 - 1];
					}
					for (i1 = 1; i1 <= mm1 - 1; i1++) {
						u[uoff + 2 * i1 - 2 + n1
								* (2 * i2 - 2 + n2 * (2 * i3 - 2))] += u[zoff
								+ i1 - 1 + mm1 * (i2 - 1 + mm2 * (i3 - 1))];
						u[uoff + 2 * i1 - 1 + n1
								* (2 * i2 - 2 + n2 * (2 * i3 - 2))] += 0.5 * (u[zoff
								+ i1 + mm1 * (i2 - 1 + mm2 * (i3 - 1))] + u[zoff
								+ i1 - 1 + mm1 * (i2 - 1 + mm2 * (i3 - 1))]);
					}
					for (i1 = 1; i1 <= mm1 - 1; i1++) {
						u[uoff + 2 * i1 - 2 + n1
								* (2 * i2 - 1 + n2 * (2 * i3 - 2))] += 0.5 * z1[i1 - 1];
						u[uoff + 2 * i1 - 1 + n1
								* (2 * i2 - 1 + n2 * (2 * i3 - 2))] += 0.25 * (z1[i1 - 1] + z1[i1]);
					}
					for (i1 = 1; i1 <= mm1 - 1; i1++) {
						u[uoff + 2 * i1 - 2 + n1
								* (2 * i2 - 2 + n2 * (2 * i3 - 1))] += 0.5 * z2[i1 - 1];
						u[uoff + 2 * i1 - 1 + n1
								* (2 * i2 - 2 + n2 * (2 * i3 - 1))] += 0.25 * (z2[i1 - 1] + z2[i1]);
					}
					for (i1 = 1; i1 <= mm1 - 1; i1++) {
						u[uoff + 2 * i1 - 2 + n1
								* (2 * i2 - 1 + n2 * (2 * i3 - 1))] += 0.25 * z3[i1 - 1];
						u[uoff + 2 * i1 - 1 + n1
								* (2 * i2 - 1 + n2 * (2 * i3 - 1))] += 0.125 * (z3[i1 - 1] + z3[i1]);
					}
				}
			}
		} else {
			if (n1 == 3) {
				d1 = 2;
				t1 = 1;
			} else {
				d1 = 1;
				t1 = 0;
			}

			if (n2 == 3) {
				d2 = 2;
				t2 = 1;
			} else {
				d2 = 1;
				t2 = 0;
			}

			if (n3 == 3) {
				d3 = 2;
				t3 = 1;
			} else {
				d3 = 1;
				t3 = 0;
			}

			for (i3 = start; i3 <= end; i3++) {
				for (i2 = 1; i2 <= mm2 - 1; i2++) {
					for (i1 = 1; i1 <= mm1 - 1; i1++) {
						u[uoff + 2 * i1 - 1 - d1 + n1
								* (2 * i2 - 1 - d2 + n2 * (2 * i3 - 1 - d3))] += u[zoff
								+ i1 - 1 + mm1 * (i2 - 1 + mm2 * (i3 - 1))];
					}
					for (i1 = 1; i1 <= mm1 - 1; i1++) {
						u[uoff + 2 * i1 - 1 - t1 + n1
								* (2 * i2 - 1 - d2 + n2 * (2 * i3 - 1 - d3))] += 0.5 * (u[zoff
								+ i1 + mm1 * (i2 - 1 + mm2 * (i3 - 1))] + u[zoff
								+ i1 - 1 + mm1 * (i2 - 1 + mm2 * (i3 - 1))]);
					}
				}
				for (i2 = 1; i2 <= mm2 - 1; i2++) {
					for (i1 = 1; i1 <= mm1 - 1; i1++) {
						u[uoff + 2 * i1 - 1 - d1 + n1
								* (2 * i2 - 1 - t2 + n2 * (2 * i3 - 1 - d3))] += 0.5 * (u[zoff
								+ i1 - 1 + mm1 * (i2 + mm2 * (i3 - 1))] + u[zoff
								+ i1 - 1 + mm1 * (i2 - 1 + mm2 * (i3 - 1))]);
					}
					for (i1 = 1; i1 <= mm1 - 1; i1++) {
						u[uoff + 2 * i1 - 1 - t1 + n1
								* (2 * i2 - 1 - t2 + n2 * (2 * i3 - 1 - d3))] += 0.25 * (u[zoff
								+ i1 + mm1 * (i2 + mm2 * (i3 - 1))]
								+ u[zoff + i1 + mm1 * (i2 - 1 + mm2 * (i3 - 1))]
								+ u[zoff + i1 - 1 + mm1 * (i2 + mm2 * (i3 - 1))] + u[zoff
								+ i1 - 1 + mm1 * (i2 - 1 + mm2 * (i3 - 1))]);
					}
				}
			}

			for (i3 = start; i3 <= end; i3++) {
				for (i2 = 1; i2 <= mm2 - 1; i2++) {
					for (i1 = 1; i1 <= mm1 - 1; i1++) {
						u[uoff + 2 * i1 - 1 - d1 + n1
								* (2 * i2 - 1 - d2 + n2 * (2 * i3 - 1 - t3))] = 0.5 * (u[zoff
								+ i1 - 1 + mm1 * (i2 - 1 + mm2 * i3)] + u[zoff
								+ i1 - 1 + mm1 * (i2 - 1 + mm2 * (i3 - 1))]);
					}
					for (i1 = 1; i1 <= mm1 - 1; i1++) {
						u[uoff + 2 * i1 - 1 - t1 + n1
								* (2 * i2 - 1 - d2 + n2 * (2 * i3 - 1 - t3))] += 0.25 * (u[zoff
								+ i1 + mm1 * (i2 - 1 + mm2 * i3)]
								+ u[zoff + i1 - 1 + mm1 * (i2 - 1 + mm2 * i3)]
								+ u[zoff + i1 + mm1 * (i2 - 1 + mm2 * (i3 - 1))] + u[zoff
								+ i1 - 1 + mm1 * (i2 - 1 + mm2 * (i3 - 1))]);
					}
				}
				for (i2 = 1; i2 <= mm2 - 1; i2++) {
					for (i1 = 1; i1 <= mm1 - 1; i1++) {
						u[uoff + 2 * i1 - 1 - d1 + n1
								* (2 * i2 - 1 - t2 + n2 * (2 * i3 - 1 - t3))] += 0.25 * (u[zoff
								+ i1 - 1 + mm1 * (i2 + mm2 * i3)]
								+ u[zoff + i1 - 1 + mm1 * (i2 - 1 + mm2 * i3)]
								+ u[zoff + i1 - 1 + mm1 * (i2 + mm2 * (i3 - 1))] + u[zoff
								+ i1 - 1 + mm1 * (i2 - 1 + mm2 * (i3 - 1))]);
					}
					for (i1 = 1; i1 <= mm1 - 1; i1++) {
						u[uoff + 2 * i1 - 1 - t1 + n1
								* (2 * i2 - 1 - t2 + n2 * (2 * i3 - 1 - t3))] += 0.125 * (u[zoff
								+ i1 + mm1 * (i2 + mm2 * i3)]
								+ u[zoff + i1 + mm1 * (i2 - 1 + mm2 * i3)]
								+ u[zoff + i1 - 1 + mm1 * (i2 + mm2 * i3)]
								+ u[zoff + i1 - 1 + mm1 * (i2 - 1 + mm2 * i3)]
								+ u[zoff + i1 + mm1 * (i2 + mm2 * (i3 - 1))]
								+ u[zoff + i1 + mm1 * (i2 - 1 + mm2 * (i3 - 1))]
								+ u[zoff + i1 - 1 + mm1 * (i2 + mm2 * (i3 - 1))] + u[zoff
								+ i1 - 1 + mm1 * (i2 - 1 + mm2 * (i3 - 1))]);
					}
				}
			}
		}
	}

	public void step2() {
		if (work == 0)
			return;
	}

	private void GetWork() {
		int workpt = (wend - wstart) / num_threads;
		int remainder = wend - wstart - workpt * num_threads;
		if (workpt == 0) {
			if (id < wend - wstart) {
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
