/**
 * Generated from variabletest.treo by Reo 1.0.
 */

import nl.cwi.reo.runtime.*;

public class variabletest {

	public static void main(String[] args) {

		Port<String> $1 = new PortWaitNotify<String>();
		Port<String> $2 = new PortWaitNotify<String>();

		PortWindow1 PortWindow1 = new PortWindow1();
		$1.setProducer(PortWindow1); 
		PortWindow1.$1 = $1;
		Thread thread_PortWindow1 = new Thread(PortWindow1);
		PortWindow2 PortWindow2 = new PortWindow2();
		$2.setConsumer(PortWindow2); 
		PortWindow2.$2 = $2;
		Thread thread_PortWindow2 = new Thread(PortWindow2);
		Protocol1 Protocol1 = new Protocol1();
		$1.setConsumer(Protocol1); 
		$2.setProducer(Protocol1); 
		Protocol1.$1 = $1;
		Protocol1.$2 = $2;
		Thread thread_Protocol1 = new Thread(Protocol1); 

		thread_PortWindow1.start();
		thread_PortWindow2.start();
		thread_Protocol1.start();

		try {
			thread_PortWindow1.join();
			thread_PortWindow2.join();
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
			Windows.producer("in", $1);
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
			Windows.consumer("out", $2);
		}
	}

	private static class Protocol1 implements Component {

		public volatile Port<String> $1;
		public volatile Port<String> $2;
		 

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
		    		if ($2.hasGet() && !($1.peek() == null) && !($1.peek() == null)) {
		    			$2.put($1.peek()); 
		    			 
		    			 
		    			$1.get();
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
						if ($2.hasGet() && !($1.peek() == null) && !($1.peek() == null)) break;
						try { 
							wait(); 
						} catch (InterruptedException e) { }
					}	
				}
			}
		}
	}
}