/**
 * Generated from ../sandbox/alternator.rba.treo by Reo 1.0.
 */

import nl.cwi.reo.runtime.*;

public class alternator {

	public static void main(String[] args) {

		Port<String> $10 = new PortWaitNotify<String>();
		Port<String> $13 = new PortWaitNotify<String>();
		Port<String> $14 = new PortWaitNotify<String>();
		Port<String> $15 = new PortWaitNotify<String>();
		Port<String> $16 = new PortWaitNotify<String>();

		producer1 producer1 = new producer1();
		$15.setProducer(producer1); 
		producer1.$15 = $15;
		Thread thread_producer1 = new Thread(producer1);
		producer2 producer2 = new producer2();
		$13.setProducer(producer2); 
		producer2.$13 = $13;
		Thread thread_producer2 = new Thread(producer2);
		producer3 producer3 = new producer3();
		$10.setProducer(producer3); 
		producer3.$10 = $10;
		Thread thread_producer3 = new Thread(producer3);
		producer4 producer4 = new producer4();
		$16.setProducer(producer4); 
		producer4.$16 = $16;
		Thread thread_producer4 = new Thread(producer4);
		consumer5 consumer5 = new consumer5();
		$14.setConsumer(consumer5); 
		consumer5.$14 = $14;
		Thread thread_consumer5 = new Thread(consumer5);
		Protocol1 Protocol1 = new Protocol1();
		$10.setConsumer(Protocol1); 
		$13.setConsumer(Protocol1); 
		$14.setProducer(Protocol1); 
		$15.setConsumer(Protocol1); 
		$16.setConsumer(Protocol1); 
		Protocol1.$10 = $10;
		Protocol1.$13 = $13;
		Protocol1.$14 = $14;
		Protocol1.$15 = $15;
		Protocol1.$16 = $16;
		Thread thread_Protocol1 = new Thread(Protocol1); 

		thread_producer1.start();
		thread_producer2.start();
		thread_producer3.start();
		thread_producer4.start();
		thread_consumer5.start();
		thread_Protocol1.start();

		try {
			thread_producer1.join();
			thread_producer2.join();
			thread_producer3.join();
			thread_producer4.join();
			thread_consumer5.join();
			thread_Protocol1.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static class producer1 implements Component {

		public volatile Port<String> $15;
		 

		public void activate() {
			synchronized (this) {
				notify();
			}
		}

		public void run() {
			nl.cwi.reo.components.Workers2.producer($15);
		}
	}

	private static class producer2 implements Component {

		public volatile Port<String> $13;
		 

		public void activate() {
			synchronized (this) {
				notify();
			}
		}

		public void run() {
			nl.cwi.reo.components.Workers2.producer($13);
		}
	}

	private static class producer3 implements Component {

		public volatile Port<String> $10;
		 

		public void activate() {
			synchronized (this) {
				notify();
			}
		}

		public void run() {
			nl.cwi.reo.components.Workers2.producer($10);
		}
	}

	private static class producer4 implements Component {

		public volatile Port<String> $16;
		 

		public void activate() {
			synchronized (this) {
				notify();
			}
		}

		public void run() {
			nl.cwi.reo.components.Workers2.producer($16);
		}
	}

	private static class consumer5 implements Component {

		public volatile Port<String> $14;
		 

		public void activate() {
			synchronized (this) {
				notify();
			}
		}

		public void run() {
			nl.cwi.reo.components.Workers2.consumer($14);
		}
	}

	private static class Protocol1 implements Component {

		public volatile Port<String> $10;
		public volatile Port<String> $13;
		public volatile Port<String> $14;
		public volatile Port<String> $15;
		public volatile Port<String> $16;
		private String m2  = null ; 
		private String m3  = null ; 
		private String m1  = null ;  

		public void activate() {
			synchronized (this) {
				notify();
			}
		}

		public void fire(Integer i){
			if(guards[i].guard())commands[i].update();;
		};

		interface Guard{
			Boolean guard();
		}

		interface Command{
			void update();
		}

		private Guard[] guards = new Guard[]{
			new Guard(){
				public Boolean guard(){
					return ((m2 == null && 
			           !(m3 == null)));
				}
			},
			new Guard(){
				public Boolean guard(){
					return ($14.hasGet() && (null == null && 
			           !($13.peek() == null) && 
			           !($15.peek() == null) && 
			           !($16.peek() == null) && 
			           m1 == null && 
			           m3 == null && 
			           !($10.peek() == null) && 
			           m2 == null));
				}
			},
			new Guard(){
				public Boolean guard(){
					return ($14.hasGet() && (!(m1 == null)));
				}
			},
			new Guard(){
				public Boolean guard(){
					return ((!(m2 == null) && 
			           m1 == null));
				}
			},		
		};

		private Command[] commands = new Command[]{
			new Command(){
				public void update(){
					 
					m2 = m3;
					 
					m3 = null; 

				}
			},
			new Command(){
				public void update(){
					$14.put($15.peek()); 
					m2 = $10.peek();
					m3 = $16.peek();
					m1 = $13.peek(); 
					 
					$10.get();
					$13.get();
					$15.get();
					$16.get();
				}
			},
			new Command(){
				public void update(){
					$14.put(m1); 
					 
					m1 = null; 

				}
			},
			new Command(){
				public void update(){
					 
					m1 = m2; 
					m2 = null;
					 

				}
			},		
		};

		public void run() {
			int i = 0;
			int j = 0;
			int s = guards.length;

			while (true) {
				fire((i+j) % s);			

				i = (i+1) % s;
				if (i == 0)
					j = (j+1) % s;

				synchronized (this) {
					while(true) {
						if ((m2 == null && 
						     !(m3 == null))) break;
						if ($14.hasGet() && (null == null && 
						     !($13.peek() == null) && 
						     !($15.peek() == null) && 
						     !($16.peek() == null) && 
						     m1 == null && 
						     m3 == null && 
						     !($10.peek() == null) && 
						     m2 == null)) break;
						if ($14.hasGet() && (!(m1 == null))) break;
						if ((!(m2 == null) && 
						     m1 == null)) break;
						try { 
							wait(); 
						} catch (InterruptedException e) { }
					}	
				}
			}
		}
	}
}