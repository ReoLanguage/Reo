/**
 * Generated from trans.treo by Reo 1.0.
 */

import nl.cwi.reo.runtime.*;

public class trans {

	public static void main(String[] args) {

		Port<String> $5 = new PortWaitNotify<String>();
		Port<String> $1 = new PortWaitNotify<String>();
		Port<String> $2 = new PortWaitNotify<String>();
		Port<String> $3 = new PortWaitNotify<String>();
		Port<String> $4 = new PortWaitNotify<String>();

		PortWindow1 PortWindow1 = new PortWindow1();
		$1.setProducer(PortWindow1); 
		PortWindow1.$1 = $1;
		Thread thread_PortWindow1 = new Thread(PortWindow1);
		PortWindow2 PortWindow2 = new PortWindow2();
		$2.setProducer(PortWindow2); 
		PortWindow2.$2 = $2;
		Thread thread_PortWindow2 = new Thread(PortWindow2);
		PortWindow3 PortWindow3 = new PortWindow3();
		$3.setConsumer(PortWindow3); 
		PortWindow3.$3 = $3;
		Thread thread_PortWindow3 = new Thread(PortWindow3);
		PortWindow4 PortWindow4 = new PortWindow4();
		$4.setProducer(PortWindow4); 
		PortWindow4.$4 = $4;
		Thread thread_PortWindow4 = new Thread(PortWindow4);
		PortWindow5 PortWindow5 = new PortWindow5();
		$5.setProducer(PortWindow5); 
		PortWindow5.$5 = $5;
		Thread thread_PortWindow5 = new Thread(PortWindow5);
		Protocol1 Protocol1 = new Protocol1();
		$5.setConsumer(Protocol1); 
		$1.setConsumer(Protocol1); 
		$2.setConsumer(Protocol1); 
		$3.setProducer(Protocol1); 
		$4.setConsumer(Protocol1); 
		Protocol1.$5 = $5;
		Protocol1.$1 = $1;
		Protocol1.$2 = $2;
		Protocol1.$3 = $3;
		Protocol1.$4 = $4;
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

		public volatile Port<String> $1;
		 

		public void activate() {
			synchronized (this) {
				notify();
			}
		}

		public void run() {
			Windows.producer("a", $1);
		}
	}

	private static class PortWindow2 implements Component {

		public volatile Port<String> $2;
		 

		public void activate() {
			synchronized (this) {
				notify();
			}
		}

		public void run() {
			Windows.producer("b", $2);
		}
	}

	private static class PortWindow3 implements Component {

		public volatile Port<String> $3;
		 

		public void activate() {
			synchronized (this) {
				notify();
			}
		}

		public void run() {
			Windows.consumer("c", $3);
		}
	}

	private static class PortWindow4 implements Component {

		public volatile Port<String> $4;
		 

		public void activate() {
			synchronized (this) {
				notify();
			}
		}

		public void run() {
			Windows.producer("na", $4);
		}
	}

	private static class PortWindow5 implements Component {

		public volatile Port<String> $5;
		 

		public void activate() {
			synchronized (this) {
				notify();
			}
		}

		public void run() {
			Windows.producer("nb", $5);
		}
	}

	private static class Protocol1 implements Component {

		public volatile Port<String> $5;
		public volatile Port<String> $1;
		public volatile Port<String> $2;
		public volatile Port<String> $3;
		public volatile Port<String> $4;
		 

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
		    		if ($3.hasGet() && !($5.peek() == null)&&!($2.peek() == null)&&(!($2.peek() == null) && !($5.peek() == null) && !(TransformerFunction.AddPort( $5.peek() , $2.peek() ) == null))) {
		    			$3.put(TransformerFunction.AddPort( $5.peek() , $2.peek() )); 
		    			 
		    			 
		    			$5.get();
		    			$2.get();
		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ($3.hasGet() && !($1.peek() == null)&&!($4.peek() == null)&&(!($1.peek() == null) && !($4.peek() == null) && !(TransformerFunction.AddPort( $4.peek() , $1.peek() ) == null))) {
		    			$3.put(TransformerFunction.AddPort( $4.peek() , $1.peek() )); 
		    			 
		    			 
		    			$1.get();
		    			$4.get();
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
						if ($3.hasGet() && !($5.peek() == null)&&!($2.peek() == null)&&(!($2.peek() == null) && !($5.peek() == null) && !(TransformerFunction.AddPort( $5.peek() , $2.peek() ) == null))) break;
						if ($3.hasGet() && !($1.peek() == null)&&!($4.peek() == null)&&(!($1.peek() == null) && !($4.peek() == null) && !(TransformerFunction.AddPort( $4.peek() , $1.peek() ) == null))) break;
						try { 
							wait(); 
						} catch (InterruptedException e) { }
					}	
				}
			}
		}
	}
}