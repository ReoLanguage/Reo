/**
 * Generated from main1.treo by Reo 1.0.
 */

import nl.cwi.reo.runtime.*;

public class main1 {

	public static void main(String[] args) {

		Port<String> $6 = new PortWaitNotify<String>();
		Port<String> $7 = new PortWaitNotify<String>();
		Port<String> $8 = new PortWaitNotify<String>();

		red1 red1 = new red1();
		$7.setProducer(red1); 
		red1.$7 = $7;
		Thread thread_red1 = new Thread(red1);
		green2 green2 = new green2();
		$8.setProducer(green2); 
		green2.$8 = $8;
		Thread thread_green2 = new Thread(green2);
		blue3 blue3 = new blue3();
		$6.setConsumer(blue3); 
		blue3.$6 = $6;
		Thread thread_blue3 = new Thread(blue3);
		Protocol1 Protocol1 = new Protocol1();
		$6.setProducer(Protocol1); 
		$7.setConsumer(Protocol1); 
		$8.setConsumer(Protocol1); 
		Protocol1.$6 = $6;
		Protocol1.$7 = $7;
		Protocol1.$8 = $8;
		Thread thread_Protocol1 = new Thread(Protocol1); 

		thread_red1.start();
		thread_green2.start();
		thread_blue3.start();
		thread_Protocol1.start();

		try {
			thread_red1.join();
			thread_green2.join();
			thread_blue3.join();
			thread_Protocol1.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static class red1 implements Component {

		public volatile Port<String> $7;
		 

		public void activate() {
			synchronized (this) {
				notify();
			}
		}

		public void run() {
			Producer.red($7);
		}
	}

	private static class green2 implements Component {

		public volatile Port<String> $8;
		 

		public void activate() {
			synchronized (this) {
				notify();
			}
		}

		public void run() {
			nl.cwi.reo.components.Workers2.producer($8);
		}
	}

	private static class blue3 implements Component {

		public volatile Port<String> $6;
		 

		public void activate() {
			synchronized (this) {
				notify();
			}
		}

		public void run() {
			Consumer.blue($6);
		}
	}

	private static class Protocol1 implements Component {

		public volatile Port<String> $6;
		public volatile Port<String> $7;
		public volatile Port<String> $8;
		private String m1  = null ;  

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
		    		if ($6.hasGet() && !($7.peek() == null) &&  !($8.peek() == null) && (m1 == null && !($8.peek() == null) && !($7.peek() == null))) {
		    			$6.put($7.peek()); 
		    			m1 = $8.peek(); 
		    			 
		    			$7.get();
		    			$8.get();
		    		}
		    	}
		    },
		    new Rule() { 
		    	public void fire() { 
		    		if ($6.hasGet() && !(m1 == null)) {
		    			$6.put(m1); 
		    			 
		    			m1 = null; 

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
						if ($6.hasGet() && !($7.peek() == null) &&  !($8.peek() == null) && (m1 == null && !($8.peek() == null) && !($7.peek() == null))) break;
						if ($6.hasGet() && !(m1 == null)) break;
						try { 
							wait(); 
						} catch (InterruptedException e) { }
					}	
				}
			}
		}
	}
}