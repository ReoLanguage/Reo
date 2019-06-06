/**
 * Generated from test01.treo by Reo 1.0.
 */

import nl.cwi.reo.runtime.*;

public class test01 {

	public static void main(String[] args) {

		Port<String> $181 = new PortWaitNotify<String>();
		Port<String> $229 = new PortWaitNotify<String>();
		Port<String> $231 = new PortWaitNotify<String>();
		Port<String> $197 = new PortWaitNotify<String>();
		Port<String> $233 = new PortWaitNotify<String>();

		PortWindow1 PortWindow1 = new PortWindow1();
		$231.setConsumer(PortWindow1); 
		PortWindow1.$231 = $231;
		Thread thread_PortWindow1 = new Thread(PortWindow1);
		PortWindow2 PortWindow2 = new PortWindow2();
		$233.setConsumer(PortWindow2); 
		PortWindow2.$233 = $233;
		Thread thread_PortWindow2 = new Thread(PortWindow2);
		PortWindow3 PortWindow3 = new PortWindow3();
		$197.setProducer(PortWindow3); 
		PortWindow3.$197 = $197;
		Thread thread_PortWindow3 = new Thread(PortWindow3);
		PortWindow4 PortWindow4 = new PortWindow4();
		$181.setProducer(PortWindow4); 
		PortWindow4.$181 = $181;
		Thread thread_PortWindow4 = new Thread(PortWindow4);
		PortWindow5 PortWindow5 = new PortWindow5();
		$229.setConsumer(PortWindow5); 
		PortWindow5.$229 = $229;
		Thread thread_PortWindow5 = new Thread(PortWindow5);
		Protocol1 Protocol1 = new Protocol1();
		$181.setConsumer(Protocol1); 
		$229.setProducer(Protocol1); 
		$231.setProducer(Protocol1); 
		$197.setConsumer(Protocol1); 
		$233.setProducer(Protocol1); 
		Protocol1.$181 = $181;
		Protocol1.$229 = $229;
		Protocol1.$231 = $231;
		Protocol1.$197 = $197;
		Protocol1.$233 = $233;
		Thread thread_Protocol1 = new Thread(Protocol1); 

		thread_PortWindow1.start();
		thread_PortWindow2.start();
		thread_PortWindow3.start();
		thread_PortWindow4.start();
		thread_PortWindow5.start();
		thread_Protocol1.start();

		try {
			thread_PortWindow1.join();
			thread_PortWindow2.join();
			thread_PortWindow3.join();
			thread_PortWindow4.join();
			thread_PortWindow5.join();
			thread_Protocol1.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static class PortWindow1 implements Component {

		public volatile Port<String> $231;
		 

		public void activate() {
			synchronized (this) {
				notify();
			}
		}

		public void run() {
			Windows.consumer("p1", $231);
		}
	}

	private static class PortWindow2 implements Component {

		public volatile Port<String> $233;
		 

		public void activate() {
			synchronized (this) {
				notify();
			}
		}

		public void run() {
			Windows.consumer("p2", $233);
		}
	}

	private static class PortWindow3 implements Component {

		public volatile Port<String> $197;
		 

		public void activate() {
			synchronized (this) {
				notify();
			}
		}

		public void run() {
			Windows.producer("a[1]", $197);
		}
	}

	private static class PortWindow4 implements Component {

		public volatile Port<String> $181;
		 

		public void activate() {
			synchronized (this) {
				notify();
			}
		}

		public void run() {
			Windows.producer("a[2]", $181);
		}
	}

	private static class PortWindow5 implements Component {

		public volatile Port<String> $229;
		 

		public void activate() {
			synchronized (this) {
				notify();
			}
		}

		public void run() {
			Windows.consumer("p0", $229);
		}
	}

	private static class Protocol1 implements Component {

		public volatile Port<String> $181;
		public volatile Port<String> $229;
		public volatile Port<String> $231;
		public volatile Port<String> $197;
		public volatile Port<String> $233;
		private String m2  = null ; 
		private String m3  = "*/RULE/100/*/*/q0"; 
		private String m1  = null ; 
		private String m10  = null ; 
		private String m8  = null ; 
		private String m12  = "*/RULE/100/*/*/q0"; 
		private String m11  = null ; 
		private String m9  = "*/RULE/100/*/*/q0"; 
		private String m6  = "*/RULE/100/*/*/q0"; 
		private String m7  = null ; 
		private String m4  = null ; 
		private String m5  = null ;  

		public void activate() {
			synchronized (this) {
				notify();
			}
		}

		interface Rule {
		    void fire();
		}

		private Rule[] rules = new Rule[] {
		    new Rule() { 
		    	public void fire() { 
		    		if ((!(FilterFunction.Sendout_q0(  )) && !(FilterFunction.Sendout_q2(  )) && !(FilterFunction.Sendout_q1(  )))) {
		    			 
		    			 
		    			 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ((!(m3 == null) && m1 == null && !(m11 == null) && m10 == null && !(m4 == null) && m1 == m4 && m4 == m7 && !(m8 == null) && m7 == null && !(m5 == null) && m4 == null && !(m7 == null) && m4 == m10)) {
		    			 
		    			 
		    			m11 = null;
		    			m8 = null;
		    			m5 = null;
		    			m3 = null; 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ($229.hasGet() &&  $233.hasGet() && (!(TransformerFunction.PutOut(  ) == null) && !(FilterFunction.Sendout_q0(  )) && FilterFunction.Sendout_q2(  ) && !(FilterFunction.Sendout_q2(  )) && !(FilterFunction.Sendout_q1(  )) && FilterFunction.Sendout_q0(  ))) {
		    			$229.put(TransformerFunction.PutOut(  ));
		    			$233.put(TransformerFunction.PutOut(  )); 
		    			 
		    			 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ($229.hasGet() &&  $231.hasGet() &&  $233.hasGet() && (!(TransformerFunction.PutOut(  ) == null) && !(FilterFunction.Sendout_q0(  )) && FilterFunction.Sendout_q2(  ) && FilterFunction.Sendout_q1(  ) && !(FilterFunction.Sendout_q2(  )) && !(FilterFunction.Sendout_q1(  )) && FilterFunction.Sendout_q0(  ))) {
		    			$229.put(TransformerFunction.PutOut(  ));
		    			$231.put(TransformerFunction.PutOut(  ));
		    			$233.put(TransformerFunction.PutOut(  )); 
		    			 
		    			 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ((!(m4 == null) && m5 == null)) {
		    			 
		    			m5 = m4; 
		    			m4 = null;
		    			 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ($229.hasGet() &&  $231.hasGet() && (!(FilterFunction.Sendout_q0(  )) && !(TransformerFunction.PutOut(  ) == null) && !(FilterFunction.Sendout_q2(  )) && FilterFunction.Sendout_q1(  ) && !(FilterFunction.Sendout_q1(  )) && FilterFunction.Sendout_q0(  ))) {
		    			$229.put(TransformerFunction.PutOut(  ));
		    			$231.put(TransformerFunction.PutOut(  )); 
		    			 
		    			 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ($231.hasGet() && (!(TransformerFunction.PutOut(  ) == null) && !(FilterFunction.Sendout_q0(  )) && !(FilterFunction.Sendout_q2(  )) && FilterFunction.Sendout_q1(  ) && !(FilterFunction.Sendout_q1(  )))) {
		    			$231.put(TransformerFunction.PutOut(  )); 
		    			 
		    			 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ($229.hasGet() && (!(FilterFunction.Sendout_q0(  )) && !(TransformerFunction.PutOut(  ) == null) && !(FilterFunction.Sendout_q2(  )) && !(FilterFunction.Sendout_q1(  )) && FilterFunction.Sendout_q0(  ))) {
		    			$229.put(TransformerFunction.PutOut(  )); 
		    			 
		    			 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ((!(m2 == null) && m1 == null && !(m12 == null) && m10 == null && !(m4 == null) && !(m9 == null) && m1 == m4 && m7 == null && !(m1 == null) && m4 == null && !(m6 == null) && m1 == m7 && m10 == m1 && !(m7 == null))) {
		    			 
		    			 
		    			m12 = null;
		    			m9 = null;
		    			m6 = null;
		    			m2 = null; 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ((m12 == null && !(m11 == null) && !(m9 == null) && m7 == null)) {
		    			 
		    			m12 = m11;


		    			m7 = m11; 
		    			m11 = null;
		    			m9 = null;
		    			 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ($229.hasGet() &&  $231.hasGet() &&  $233.hasGet() && (!(TransformerFunction.PutOut(  ) == null) && !(FilterFunction.Sendout_q0(  )) && FilterFunction.Sendout_q2(  ) && !(FilterFunction.Sendout_q2(  )) && FilterFunction.Sendout_q1(  ) && FilterFunction.Sendout_q0(  ) && !(FilterFunction.Sendout_q1(  )))) {
		    			$229.put(TransformerFunction.PutOut(  ));
		    			$231.put(TransformerFunction.PutOut(  ));
		    			$233.put(TransformerFunction.PutOut(  )); 
		    			 
		    			 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if (!($197.peek() == null) && (!(Pair.Merge(TransformerFunction.AddPort( 1 , $197.peek() ), m8 ) == null) && !(m11 == null) && m10 == null && !($197.peek() == null) && !(TransformerFunction.AddPort( 1 , $197.peek() ) == null) && m9 == null && !(m8 == null))) {
		    			 
		    			m9 = m8;
		    			m10 = m8; 
		    			m11 = null;
		    			m8 = null;

		    			 
		    			$197.get();
		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if (!($197.peek() == null) && (m3 == null && !(m2 == null) && !($197.peek() == null) && !(TransformerFunction.AddPort( 1 , $197.peek() ) == null) && !(Pair.Merge(TransformerFunction.AddPort( 1 , $197.peek() ), m2 ) == null) && !(m5 == null) && m4 == null)) {
		    			 
		    			m4 = m2;


		    			m3 = m2; 
		    			m5 = null;
		    			m2 = null;
		    			 
		    			$197.get();
		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ((m2 == null && !(m1 == null))) {
		    			 
		    			m2 = m1;
		    			 
		    			m1 = null; 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if (!($181.peek() == null) && (m3 == null && !(Pair.Merge(TransformerFunction.AddPort( 2 , $181.peek() ), m2 ) == null) && !(m2 == null) && !(TransformerFunction.AddPort( 2 , $181.peek() ) == null) && !($181.peek() == null) && !(m5 == null) && m4 == null)) {
		    			 
		    			m4 = m2;


		    			m3 = m2; 
		    			m5 = null;
		    			m2 = null;
		    			 
		    			$181.get();
		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ((m11 == null && !(m10 == null))) {
		    			 
		    			m11 = m10;
		    			 
		    			m10 = null; 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ((!(m3 == null) && m1 == null && m6 == null && !(m5 == null))) {
		    			 
		    			m6 = m5;


		    			m1 = m5; 
		    			m5 = null;
		    			m3 = null;
		    			 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ((!(FilterFunction.RegularMessage(  )) && !( == null) && !(FilterFunction.ControllerMessage(  )))) {
		    			 
		    			 
		    			 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if (!($197.peek() == null) && (m3 == null && !(m2 == null) && !($197.peek() == null) && !(TransformerFunction.AddPort( 1 , $197.peek() ) == null) && !(Pair.Merge(TransformerFunction.AddPort( 1 , $197.peek() ), m2 ) == null) && m4 == null && !(m6 == null))) {
		    			 
		    			m4 = m2;

		    			m3 = m2; 
		    			m6 = null;

		    			m2 = null;
		    			 
		    			$197.get();
		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ((!(m3 == null) && m1 == null && !(m12 == null) && m10 == null && !(m4 == null) && m1 == m4 && !(m8 == null) && m7 == null && !(m1 == null) && m4 == null && !(m6 == null) && m1 == m7 && m10 == m1 && !(m7 == null))) {
		    			 
		    			 
		    			m12 = null;
		    			m8 = null;
		    			m6 = null;
		    			m3 = null; 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ((!(FilterFunction.RegularMessage(  )) && FilterFunction.RegularMessage(  ) && !(TransformerFunction.Matching(  ) == null) && !(FilterFunction.ControllerMessage(  )))) {
		    			 
		    			 
		    			 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ((m12 == null && !(m11 == null) && !(m8 == null) && m7 == null)) {
		    			 
		    			m12 = m11;


		    			m7 = m11; 
		    			m11 = null;
		    			m8 = null;
		    			 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ((!(m3 == null) && m1 == null && !(m12 == null) && m10 == null && !(m4 == null) && !(m9 == null) && m1 == m4 && m7 == null && !(m1 == null) && !(m5 == null) && m4 == null && m1 == m7 && m10 == m1 && !(m7 == null))) {
		    			 
		    			 
		    			m12 = null;
		    			m9 = null;
		    			m5 = null;
		    			m3 = null; 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ($233.hasGet() && (!(TransformerFunction.PutOut(  ) == null) && !(FilterFunction.Sendout_q0(  )) && FilterFunction.Sendout_q2(  ) && !(FilterFunction.Sendout_q2(  )) && !(FilterFunction.Sendout_q1(  )))) {
		    			$233.put(TransformerFunction.PutOut(  )); 
		    			 
		    			 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ($229.hasGet() &&  $231.hasGet() &&  $233.hasGet() && (!(TransformerFunction.PutOut(  ) == null) && !(FilterFunction.Sendout_q0(  )) && FilterFunction.Sendout_q2(  ) && FilterFunction.Sendout_q1(  ) && !(FilterFunction.Sendout_q2(  )) && FilterFunction.Sendout_q0(  ) && !(FilterFunction.Sendout_q1(  )))) {
		    			$229.put(TransformerFunction.PutOut(  ));
		    			$231.put(TransformerFunction.PutOut(  ));
		    			$233.put(TransformerFunction.PutOut(  )); 
		    			 
		    			 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ((!(m3 == null) && m1 == null && !(m11 == null) && m10 == null && !(m4 == null) && m1 == m4 && m4 == m7 && !(m8 == null) && m7 == null && m4 == null && !(m6 == null) && m4 == m10)) {
		    			 
		    			 
		    			m11 = null;
		    			m8 = null;
		    			m6 = null;
		    			m3 = null; 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ((!(m3 == null) && m1 == null && !(m11 == null) && m10 == null && !(m4 == null) && !(m9 == null) && m1 == m4 && m4 == m7 && m7 == null && !(m1 == null) && m4 == null && !(m6 == null) && !(m7 == null) && m4 == m10)) {
		    			 
		    			 
		    			m11 = null;
		    			m9 = null;
		    			m6 = null;
		    			m3 = null; 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ((!(FilterFunction.RegularMessage(  )) && !(TransformerFunction.Update(  ) == null) && FilterFunction.RegularMessage(  ) && !(TransformerFunction.Matching(  ) == null) && FilterFunction.ControllerMessage(  ) && !(FilterFunction.ControllerMessage(  )))) {
		    			 
		    			 
		    			 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ($229.hasGet() &&  $233.hasGet() && (!(FilterFunction.Sendout_q0(  )) && !(TransformerFunction.PutOut(  ) == null) && FilterFunction.Sendout_q2(  ) && !(FilterFunction.Sendout_q2(  )) && !(FilterFunction.Sendout_q1(  )) && FilterFunction.Sendout_q0(  ))) {
		    			$229.put(TransformerFunction.PutOut(  ));
		    			$233.put(TransformerFunction.PutOut(  )); 
		    			 
		    			 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ((!(FilterFunction.RegularMessage(  )) && !(TransformerFunction.Update(  ) == null) && FilterFunction.ControllerMessage(  ) && !(FilterFunction.ControllerMessage(  )))) {
		    			 
		    			 
		    			 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ($229.hasGet() &&  $231.hasGet() && (!(TransformerFunction.PutOut(  ) == null) && !(FilterFunction.Sendout_q0(  )) && !(FilterFunction.Sendout_q2(  )) && FilterFunction.Sendout_q1(  ) && FilterFunction.Sendout_q0(  ) && !(FilterFunction.Sendout_q1(  )))) {
		    			$229.put(TransformerFunction.PutOut(  ));
		    			$231.put(TransformerFunction.PutOut(  )); 
		    			 
		    			 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ((!(m2 == null) && m1 == null && !(m12 == null) && m10 == null && !(m4 == null) && m1 == m4 && m4 == m7 && !(m8 == null) && m7 == null && !(m5 == null) && m4 == null && !(m7 == null) && m4 == m10)) {
		    			 
		    			 
		    			m12 = null;
		    			m8 = null;
		    			m5 = null;
		    			m2 = null; 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ((!(m2 == null) && m1 == null && !(m11 == null) && m10 == null && !(m4 == null) && !(m9 == null) && m1 == m4 && m4 == m7 && m7 == null && !(m1 == null) && m4 == null && !(m6 == null) && !(m7 == null) && m4 == m10)) {
		    			 
		    			 
		    			m11 = null;
		    			m9 = null;
		    			m6 = null;
		    			m2 = null; 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ((!(m3 == null) && m1 == null && !(m12 == null) && m10 == null && !(m4 == null) && m1 == m4 && m4 == m7 && !(m8 == null) && m7 == null && !(m5 == null) && m4 == null && !(m7 == null) && m4 == m10)) {
		    			 
		    			 
		    			m12 = null;
		    			m8 = null;
		    			m5 = null;
		    			m3 = null; 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if (!($181.peek() == null) && (!(TransformerFunction.AddPort( 2 , $181.peek() ) == null) && !(m12 == null) && m10 == null && !($181.peek() == null) && !(Pair.Merge(TransformerFunction.AddPort( 2 , $181.peek() ), m8 ) == null) && m9 == null && !(m8 == null))) {
		    			 
		    			m9 = m8;
		    			m10 = m8; 
		    			m12 = null;
		    			m8 = null;

		    			 
		    			$181.get();
		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ((!(m2 == null) && m1 == null && !(m12 == null) && m10 == null && !(m4 == null) && !(m9 == null) && m1 == m4 && m4 == m7 && m7 == null && !(m5 == null) && m4 == null && !(m7 == null) && m4 == m10)) {
		    			 
		    			 
		    			m12 = null;
		    			m9 = null;
		    			m5 = null;
		    			m2 = null; 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ($229.hasGet() && (!(TransformerFunction.PutOut(  ) == null) && !(FilterFunction.Sendout_q0(  )) && !(FilterFunction.Sendout_q2(  )) && !(FilterFunction.Sendout_q1(  )) && FilterFunction.Sendout_q0(  ))) {
		    			$229.put(TransformerFunction.PutOut(  )); 
		    			 
		    			 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ((!(FilterFunction.RegularMessage(  )) && FilterFunction.RegularMessage(  ) && !(TransformerFunction.Update(  ) == null) && !(TransformerFunction.Matching(  ) == null) && FilterFunction.ControllerMessage(  ) && !(FilterFunction.ControllerMessage(  )))) {
		    			 
		    			 
		    			 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ($231.hasGet() && (!(FilterFunction.Sendout_q0(  )) && !(TransformerFunction.PutOut(  ) == null) && !(FilterFunction.Sendout_q2(  )) && FilterFunction.Sendout_q1(  ) && !(FilterFunction.Sendout_q1(  )))) {
		    			$231.put(TransformerFunction.PutOut(  )); 
		    			 
		    			 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ($231.hasGet() &&  $233.hasGet() && (!(TransformerFunction.PutOut(  ) == null) && !(FilterFunction.Sendout_q0(  )) && FilterFunction.Sendout_q2(  ) && !(FilterFunction.Sendout_q2(  )) && FilterFunction.Sendout_q1(  ) && !(FilterFunction.Sendout_q1(  )))) {
		    			$231.put(TransformerFunction.PutOut(  ));
		    			$233.put(TransformerFunction.PutOut(  )); 
		    			 
		    			 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ((!(m2 == null) && m1 == null && !(m12 == null) && m10 == null && !(m4 == null) && m1 == m4 && m4 == m7 && !(m8 == null) && m7 == null && m4 == null && !(m6 == null) && !(m7 == null) && m4 == m10)) {
		    			 
		    			 
		    			m12 = null;
		    			m8 = null;
		    			m6 = null;
		    			m2 = null; 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if (!($181.peek() == null) && (!(TransformerFunction.AddPort( 2 , $181.peek() ) == null) && !(m11 == null) && m10 == null && !($181.peek() == null) && !(Pair.Merge(TransformerFunction.AddPort( 2 , $181.peek() ), m8 ) == null) && m9 == null && !(m8 == null))) {
		    			 
		    			m9 = m8;
		    			m10 = m8; 
		    			m11 = null;
		    			m8 = null;

		    			 
		    			$181.get();
		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if (!($181.peek() == null) && (m3 == null && !(Pair.Merge(TransformerFunction.AddPort( 2 , $181.peek() ), m2 ) == null) && !(m2 == null) && !(TransformerFunction.AddPort( 2 , $181.peek() ) == null) && !($181.peek() == null) && m4 == null && !(m6 == null))) {
		    			 
		    			m4 = m2;

		    			m3 = m2; 
		    			m6 = null;

		    			m2 = null;
		    			 
		    			$181.get();
		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ($229.hasGet() &&  $231.hasGet() && (!(TransformerFunction.PutOut(  ) == null) && !(FilterFunction.Sendout_q0(  )) && !(FilterFunction.Sendout_q2(  )) && FilterFunction.Sendout_q1(  ) && !(FilterFunction.Sendout_q1(  )) && FilterFunction.Sendout_q0(  ))) {
		    			$229.put(TransformerFunction.PutOut(  ));
		    			$231.put(TransformerFunction.PutOut(  )); 
		    			 
		    			 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ((!(m3 == null) && m1 == null && !(m11 == null) && m10 == null && !(m4 == null) && !(m9 == null) && m1 == m4 && m4 == m7 && m7 == null && !(m5 == null) && m4 == null && !(m7 == null) && m4 == m10)) {
		    			 
		    			 
		    			m11 = null;
		    			m9 = null;
		    			m5 = null;
		    			m3 = null; 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ($233.hasGet() && (!(FilterFunction.Sendout_q0(  )) && !(TransformerFunction.PutOut(  ) == null) && FilterFunction.Sendout_q2(  ) && !(FilterFunction.Sendout_q2(  )) && !(FilterFunction.Sendout_q1(  )))) {
		    			$233.put(TransformerFunction.PutOut(  )); 
		    			 
		    			 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ($231.hasGet() &&  $233.hasGet() && (!(TransformerFunction.PutOut(  ) == null) && !(FilterFunction.Sendout_q0(  )) && FilterFunction.Sendout_q2(  ) && FilterFunction.Sendout_q1(  ) && !(FilterFunction.Sendout_q2(  )) && !(FilterFunction.Sendout_q1(  )))) {
		    			$231.put(TransformerFunction.PutOut(  ));
		    			$233.put(TransformerFunction.PutOut(  )); 
		    			 
		    			 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ((!(m2 == null) && m1 == null && !(m11 == null) && m10 == null && !(m4 == null) && !(m9 == null) && m1 == m4 && m4 == m7 && m7 == null && !(m5 == null) && m4 == null && !(m7 == null) && m4 == m10)) {
		    			 
		    			 
		    			m11 = null;
		    			m9 = null;
		    			m5 = null;
		    			m2 = null; 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ((!(m2 == null) && m1 == null && !(m11 == null) && m10 == null && !(m4 == null) && m1 == m4 && m4 == m7 && !(m8 == null) && m7 == null && !(m5 == null) && m4 == null && m4 == m10)) {
		    			 
		    			 
		    			m11 = null;
		    			m8 = null;
		    			m5 = null;
		    			m2 = null; 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ((!(m3 == null) && m1 == null && !(m12 == null) && m10 == null && !(m4 == null) && !(m9 == null) && m1 == m4 && m7 == null && !(m1 == null) && m4 == null && !(m6 == null) && m1 == m7 && m10 == m1 && !(m7 == null))) {
		    			 
		    			 
		    			m12 = null;
		    			m9 = null;
		    			m6 = null;
		    			m3 = null; 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ((!(m2 == null) && m1 == null && m6 == null && !(m5 == null))) {
		    			 
		    			m6 = m5;


		    			m1 = m5; 
		    			m5 = null;
		    			m2 = null;
		    			 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ((m8 == null && !(m7 == null))) {
		    			 
		    			m8 = m7;
		    			 
		    			m7 = null; 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ($229.hasGet() &&  $231.hasGet() &&  $233.hasGet() && (!(FilterFunction.Sendout_q0(  )) && !(TransformerFunction.PutOut(  ) == null) && FilterFunction.Sendout_q2(  ) && !(FilterFunction.Sendout_q2(  )) && FilterFunction.Sendout_q1(  ) && !(FilterFunction.Sendout_q1(  )) && FilterFunction.Sendout_q0(  ))) {
		    			$229.put(TransformerFunction.PutOut(  ));
		    			$231.put(TransformerFunction.PutOut(  ));
		    			$233.put(TransformerFunction.PutOut(  )); 
		    			 
		    			 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ($229.hasGet() &&  $231.hasGet() &&  $233.hasGet() && (!(TransformerFunction.PutOut(  ) == null) && !(FilterFunction.Sendout_q0(  )) && FilterFunction.Sendout_q2(  ) && !(FilterFunction.Sendout_q2(  )) && FilterFunction.Sendout_q1(  ) && !(FilterFunction.Sendout_q1(  )) && FilterFunction.Sendout_q0(  ))) {
		    			$229.put(TransformerFunction.PutOut(  ));
		    			$231.put(TransformerFunction.PutOut(  ));
		    			$233.put(TransformerFunction.PutOut(  )); 
		    			 
		    			 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if (!($197.peek() == null) && (!(Pair.Merge(TransformerFunction.AddPort( 1 , $197.peek() ), m8 ) == null) && !(m12 == null) && m10 == null && !($197.peek() == null) && !(TransformerFunction.AddPort( 1 , $197.peek() ) == null) && m9 == null && !(m8 == null))) {
		    			 
		    			m9 = m8;
		    			m10 = m8; 
		    			m12 = null;
		    			m8 = null;

		    			 
		    			$197.get();
		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ($231.hasGet() &&  $233.hasGet() && (!(FilterFunction.Sendout_q0(  )) && !(TransformerFunction.PutOut(  ) == null) && FilterFunction.Sendout_q2(  ) && !(FilterFunction.Sendout_q2(  )) && FilterFunction.Sendout_q1(  ) && !(FilterFunction.Sendout_q1(  )))) {
		    			$231.put(TransformerFunction.PutOut(  ));
		    			$233.put(TransformerFunction.PutOut(  )); 
		    			 
		    			 

		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ((!(m2 == null) && m1 == null && !(m11 == null) && m10 == null && !(m4 == null) && m1 == m4 && m4 == m7 && !(m8 == null) && m7 == null && m4 == null && !(m6 == null) && m4 == m10)) {
		    			 
		    			 
		    			m11 = null;
		    			m8 = null;
		    			m6 = null;
		    			m2 = null; 

		    		}
		    	}
		    },
		};

		public void run() {
			int i = 0;
			int j = 0;
			int s = rules.length;

			while (true) {
				rules[(i+j) % s].fire();			

				i = (i+1) % s;
				if (i == 0)
					j = (j+1) % s;

				synchronized (this) {
					while(true) {
						if ((!(FilterFunction.Sendout_q0(  )) && !(FilterFunction.Sendout_q2(  )) && !(FilterFunction.Sendout_q1(  )))) break;
						if ((!(m3 == null) && m1 == null && !(m11 == null) && m10 == null && !(m4 == null) && m1 == m4 && m4 == m7 && !(m8 == null) && m7 == null && !(m5 == null) && m4 == null && !(m7 == null) && m4 == m10)) break;
						if ($229.hasGet() &&  $233.hasGet() && (!(TransformerFunction.PutOut(  ) == null) && !(FilterFunction.Sendout_q0(  )) && FilterFunction.Sendout_q2(  ) && !(FilterFunction.Sendout_q2(  )) && !(FilterFunction.Sendout_q1(  )) && FilterFunction.Sendout_q0(  ))) break;
						if ($229.hasGet() &&  $231.hasGet() &&  $233.hasGet() && (!(TransformerFunction.PutOut(  ) == null) && !(FilterFunction.Sendout_q0(  )) && FilterFunction.Sendout_q2(  ) && FilterFunction.Sendout_q1(  ) && !(FilterFunction.Sendout_q2(  )) && !(FilterFunction.Sendout_q1(  )) && FilterFunction.Sendout_q0(  ))) break;
						if ((!(m4 == null) && m5 == null)) break;
						if ($229.hasGet() &&  $231.hasGet() && (!(FilterFunction.Sendout_q0(  )) && !(TransformerFunction.PutOut(  ) == null) && !(FilterFunction.Sendout_q2(  )) && FilterFunction.Sendout_q1(  ) && !(FilterFunction.Sendout_q1(  )) && FilterFunction.Sendout_q0(  ))) break;
						if ($231.hasGet() && (!(TransformerFunction.PutOut(  ) == null) && !(FilterFunction.Sendout_q0(  )) && !(FilterFunction.Sendout_q2(  )) && FilterFunction.Sendout_q1(  ) && !(FilterFunction.Sendout_q1(  )))) break;
						if ($229.hasGet() && (!(FilterFunction.Sendout_q0(  )) && !(TransformerFunction.PutOut(  ) == null) && !(FilterFunction.Sendout_q2(  )) && !(FilterFunction.Sendout_q1(  )) && FilterFunction.Sendout_q0(  ))) break;
						if ((!(m2 == null) && m1 == null && !(m12 == null) && m10 == null && !(m4 == null) && !(m9 == null) && m1 == m4 && m7 == null && !(m1 == null) && m4 == null && !(m6 == null) && m1 == m7 && m10 == m1 && !(m7 == null))) break;
						if ((m12 == null && !(m11 == null) && !(m9 == null) && m7 == null)) break;
						if ($229.hasGet() &&  $231.hasGet() &&  $233.hasGet() && (!(TransformerFunction.PutOut(  ) == null) && !(FilterFunction.Sendout_q0(  )) && FilterFunction.Sendout_q2(  ) && !(FilterFunction.Sendout_q2(  )) && FilterFunction.Sendout_q1(  ) && FilterFunction.Sendout_q0(  ) && !(FilterFunction.Sendout_q1(  )))) break;
						if (!($197.peek() == null) && (!(Pair.Merge(TransformerFunction.AddPort( 1 , $197.peek() ), m8 ) == null) && !(m11 == null) && m10 == null && !($197.peek() == null) && !(TransformerFunction.AddPort( 1 , $197.peek() ) == null) && m9 == null && !(m8 == null))) break;
						if (!($197.peek() == null) && (m3 == null && !(m2 == null) && !($197.peek() == null) && !(TransformerFunction.AddPort( 1 , $197.peek() ) == null) && !(Pair.Merge(TransformerFunction.AddPort( 1 , $197.peek() ), m2 ) == null) && !(m5 == null) && m4 == null)) break;
						if ((m2 == null && !(m1 == null))) break;
						if (!($181.peek() == null) && (m3 == null && !(Pair.Merge(TransformerFunction.AddPort( 2 , $181.peek() ), m2 ) == null) && !(m2 == null) && !(TransformerFunction.AddPort( 2 , $181.peek() ) == null) && !($181.peek() == null) && !(m5 == null) && m4 == null)) break;
						if ((m11 == null && !(m10 == null))) break;
						if ((!(m3 == null) && m1 == null && m6 == null && !(m5 == null))) break;
						if ((!(FilterFunction.RegularMessage(  )) && !( == null) && !(FilterFunction.ControllerMessage(  )))) break;
						if (!($197.peek() == null) && (m3 == null && !(m2 == null) && !($197.peek() == null) && !(TransformerFunction.AddPort( 1 , $197.peek() ) == null) && !(Pair.Merge(TransformerFunction.AddPort( 1 , $197.peek() ), m2 ) == null) && m4 == null && !(m6 == null))) break;
						if ((!(m3 == null) && m1 == null && !(m12 == null) && m10 == null && !(m4 == null) && m1 == m4 && !(m8 == null) && m7 == null && !(m1 == null) && m4 == null && !(m6 == null) && m1 == m7 && m10 == m1 && !(m7 == null))) break;
						if ((!(FilterFunction.RegularMessage(  )) && FilterFunction.RegularMessage(  ) && !(TransformerFunction.Matching(  ) == null) && !(FilterFunction.ControllerMessage(  )))) break;
						if ((m12 == null && !(m11 == null) && !(m8 == null) && m7 == null)) break;
						if ((!(m3 == null) && m1 == null && !(m12 == null) && m10 == null && !(m4 == null) && !(m9 == null) && m1 == m4 && m7 == null && !(m1 == null) && !(m5 == null) && m4 == null && m1 == m7 && m10 == m1 && !(m7 == null))) break;
						if ($233.hasGet() && (!(TransformerFunction.PutOut(  ) == null) && !(FilterFunction.Sendout_q0(  )) && FilterFunction.Sendout_q2(  ) && !(FilterFunction.Sendout_q2(  )) && !(FilterFunction.Sendout_q1(  )))) break;
						if ($229.hasGet() &&  $231.hasGet() &&  $233.hasGet() && (!(TransformerFunction.PutOut(  ) == null) && !(FilterFunction.Sendout_q0(  )) && FilterFunction.Sendout_q2(  ) && FilterFunction.Sendout_q1(  ) && !(FilterFunction.Sendout_q2(  )) && FilterFunction.Sendout_q0(  ) && !(FilterFunction.Sendout_q1(  )))) break;
						if ((!(m3 == null) && m1 == null && !(m11 == null) && m10 == null && !(m4 == null) && m1 == m4 && m4 == m7 && !(m8 == null) && m7 == null && m4 == null && !(m6 == null) && m4 == m10)) break;
						if ((!(m3 == null) && m1 == null && !(m11 == null) && m10 == null && !(m4 == null) && !(m9 == null) && m1 == m4 && m4 == m7 && m7 == null && !(m1 == null) && m4 == null && !(m6 == null) && !(m7 == null) && m4 == m10)) break;
						if ((!(FilterFunction.RegularMessage(  )) && !(TransformerFunction.Update(  ) == null) && FilterFunction.RegularMessage(  ) && !(TransformerFunction.Matching(  ) == null) && FilterFunction.ControllerMessage(  ) && !(FilterFunction.ControllerMessage(  )))) break;
						if ($229.hasGet() &&  $233.hasGet() && (!(FilterFunction.Sendout_q0(  )) && !(TransformerFunction.PutOut(  ) == null) && FilterFunction.Sendout_q2(  ) && !(FilterFunction.Sendout_q2(  )) && !(FilterFunction.Sendout_q1(  )) && FilterFunction.Sendout_q0(  ))) break;
						if ((!(FilterFunction.RegularMessage(  )) && !(TransformerFunction.Update(  ) == null) && FilterFunction.ControllerMessage(  ) && !(FilterFunction.ControllerMessage(  )))) break;
						if ($229.hasGet() &&  $231.hasGet() && (!(TransformerFunction.PutOut(  ) == null) && !(FilterFunction.Sendout_q0(  )) && !(FilterFunction.Sendout_q2(  )) && FilterFunction.Sendout_q1(  ) && FilterFunction.Sendout_q0(  ) && !(FilterFunction.Sendout_q1(  )))) break;
						if ((!(m2 == null) && m1 == null && !(m12 == null) && m10 == null && !(m4 == null) && m1 == m4 && m4 == m7 && !(m8 == null) && m7 == null && !(m5 == null) && m4 == null && !(m7 == null) && m4 == m10)) break;
						if ((!(m2 == null) && m1 == null && !(m11 == null) && m10 == null && !(m4 == null) && !(m9 == null) && m1 == m4 && m4 == m7 && m7 == null && !(m1 == null) && m4 == null && !(m6 == null) && !(m7 == null) && m4 == m10)) break;
						if ((!(m3 == null) && m1 == null && !(m12 == null) && m10 == null && !(m4 == null) && m1 == m4 && m4 == m7 && !(m8 == null) && m7 == null && !(m5 == null) && m4 == null && !(m7 == null) && m4 == m10)) break;
						if (!($181.peek() == null) && (!(TransformerFunction.AddPort( 2 , $181.peek() ) == null) && !(m12 == null) && m10 == null && !($181.peek() == null) && !(Pair.Merge(TransformerFunction.AddPort( 2 , $181.peek() ), m8 ) == null) && m9 == null && !(m8 == null))) break;
						if ((!(m2 == null) && m1 == null && !(m12 == null) && m10 == null && !(m4 == null) && !(m9 == null) && m1 == m4 && m4 == m7 && m7 == null && !(m5 == null) && m4 == null && !(m7 == null) && m4 == m10)) break;
						if ($229.hasGet() && (!(TransformerFunction.PutOut(  ) == null) && !(FilterFunction.Sendout_q0(  )) && !(FilterFunction.Sendout_q2(  )) && !(FilterFunction.Sendout_q1(  )) && FilterFunction.Sendout_q0(  ))) break;
						if ((!(FilterFunction.RegularMessage(  )) && FilterFunction.RegularMessage(  ) && !(TransformerFunction.Update(  ) == null) && !(TransformerFunction.Matching(  ) == null) && FilterFunction.ControllerMessage(  ) && !(FilterFunction.ControllerMessage(  )))) break;
						if ($231.hasGet() && (!(FilterFunction.Sendout_q0(  )) && !(TransformerFunction.PutOut(  ) == null) && !(FilterFunction.Sendout_q2(  )) && FilterFunction.Sendout_q1(  ) && !(FilterFunction.Sendout_q1(  )))) break;
						if ($231.hasGet() &&  $233.hasGet() && (!(TransformerFunction.PutOut(  ) == null) && !(FilterFunction.Sendout_q0(  )) && FilterFunction.Sendout_q2(  ) && !(FilterFunction.Sendout_q2(  )) && FilterFunction.Sendout_q1(  ) && !(FilterFunction.Sendout_q1(  )))) break;
						if ((!(m2 == null) && m1 == null && !(m12 == null) && m10 == null && !(m4 == null) && m1 == m4 && m4 == m7 && !(m8 == null) && m7 == null && m4 == null && !(m6 == null) && !(m7 == null) && m4 == m10)) break;
						if (!($181.peek() == null) && (!(TransformerFunction.AddPort( 2 , $181.peek() ) == null) && !(m11 == null) && m10 == null && !($181.peek() == null) && !(Pair.Merge(TransformerFunction.AddPort( 2 , $181.peek() ), m8 ) == null) && m9 == null && !(m8 == null))) break;
						if (!($181.peek() == null) && (m3 == null && !(Pair.Merge(TransformerFunction.AddPort( 2 , $181.peek() ), m2 ) == null) && !(m2 == null) && !(TransformerFunction.AddPort( 2 , $181.peek() ) == null) && !($181.peek() == null) && m4 == null && !(m6 == null))) break;
						if ($229.hasGet() &&  $231.hasGet() && (!(TransformerFunction.PutOut(  ) == null) && !(FilterFunction.Sendout_q0(  )) && !(FilterFunction.Sendout_q2(  )) && FilterFunction.Sendout_q1(  ) && !(FilterFunction.Sendout_q1(  )) && FilterFunction.Sendout_q0(  ))) break;
						if ((!(m3 == null) && m1 == null && !(m11 == null) && m10 == null && !(m4 == null) && !(m9 == null) && m1 == m4 && m4 == m7 && m7 == null && !(m5 == null) && m4 == null && !(m7 == null) && m4 == m10)) break;
						if ($233.hasGet() && (!(FilterFunction.Sendout_q0(  )) && !(TransformerFunction.PutOut(  ) == null) && FilterFunction.Sendout_q2(  ) && !(FilterFunction.Sendout_q2(  )) && !(FilterFunction.Sendout_q1(  )))) break;
						if ($231.hasGet() &&  $233.hasGet() && (!(TransformerFunction.PutOut(  ) == null) && !(FilterFunction.Sendout_q0(  )) && FilterFunction.Sendout_q2(  ) && FilterFunction.Sendout_q1(  ) && !(FilterFunction.Sendout_q2(  )) && !(FilterFunction.Sendout_q1(  )))) break;
						if ((!(m2 == null) && m1 == null && !(m11 == null) && m10 == null && !(m4 == null) && !(m9 == null) && m1 == m4 && m4 == m7 && m7 == null && !(m5 == null) && m4 == null && !(m7 == null) && m4 == m10)) break;
						if ((!(m2 == null) && m1 == null && !(m11 == null) && m10 == null && !(m4 == null) && m1 == m4 && m4 == m7 && !(m8 == null) && m7 == null && !(m5 == null) && m4 == null && m4 == m10)) break;
						if ((!(m3 == null) && m1 == null && !(m12 == null) && m10 == null && !(m4 == null) && !(m9 == null) && m1 == m4 && m7 == null && !(m1 == null) && m4 == null && !(m6 == null) && m1 == m7 && m10 == m1 && !(m7 == null))) break;
						if ((!(m2 == null) && m1 == null && m6 == null && !(m5 == null))) break;
						if ((m8 == null && !(m7 == null))) break;
						if ($229.hasGet() &&  $231.hasGet() &&  $233.hasGet() && (!(FilterFunction.Sendout_q0(  )) && !(TransformerFunction.PutOut(  ) == null) && FilterFunction.Sendout_q2(  ) && !(FilterFunction.Sendout_q2(  )) && FilterFunction.Sendout_q1(  ) && !(FilterFunction.Sendout_q1(  )) && FilterFunction.Sendout_q0(  ))) break;
						if ($229.hasGet() &&  $231.hasGet() &&  $233.hasGet() && (!(TransformerFunction.PutOut(  ) == null) && !(FilterFunction.Sendout_q0(  )) && FilterFunction.Sendout_q2(  ) && !(FilterFunction.Sendout_q2(  )) && FilterFunction.Sendout_q1(  ) && !(FilterFunction.Sendout_q1(  )) && FilterFunction.Sendout_q0(  ))) break;
						if (!($197.peek() == null) && (!(Pair.Merge(TransformerFunction.AddPort( 1 , $197.peek() ), m8 ) == null) && !(m12 == null) && m10 == null && !($197.peek() == null) && !(TransformerFunction.AddPort( 1 , $197.peek() ) == null) && m9 == null && !(m8 == null))) break;
						if ($231.hasGet() &&  $233.hasGet() && (!(FilterFunction.Sendout_q0(  )) && !(TransformerFunction.PutOut(  ) == null) && FilterFunction.Sendout_q2(  ) && !(FilterFunction.Sendout_q2(  )) && FilterFunction.Sendout_q1(  ) && !(FilterFunction.Sendout_q1(  )))) break;
						if ((!(m2 == null) && m1 == null && !(m11 == null) && m10 == null && !(m4 == null) && m1 == m4 && m4 == m7 && !(m8 == null) && m7 == null && m4 == null && !(m6 == null) && m4 == m10)) break;
						try { 
							wait(); 
						} catch (InterruptedException e) { }
					}	
				}
			}
		}
	}
}