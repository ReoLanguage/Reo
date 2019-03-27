/**
 * Generated from alternator.treo by Reo 1.0.
 */

import nl.cwi.reo.runtime.*;

public class alternator {

	public static void main(String[] args) {

		Port<String> $7 = new PortWaitNotify<String>();
		Port<String> $10 = new PortWaitNotify<String>();
		Port<String> $8 = new PortWaitNotify<String>();
		Port<String> $11 = new PortWaitNotify<String>();
		Port<String> $9 = new PortWaitNotify<String>();

		PortWindow1 PortWindow1 = new PortWindow1();
		$7.setProducer(PortWindow1); 
		PortWindow1.$7 = $7;
		Thread thread_PortWindow1 = new Thread(PortWindow1);
		PortWindow2 PortWindow2 = new PortWindow2();
		$8.setConsumer(PortWindow2); 
		PortWindow2.$8 = $8;
		Thread thread_PortWindow2 = new Thread(PortWindow2);
		PortWindow3 PortWindow3 = new PortWindow3();
		$9.setProducer(PortWindow3); 
		PortWindow3.$9 = $9;
		Thread thread_PortWindow3 = new Thread(PortWindow3);
		PortWindow4 PortWindow4 = new PortWindow4();
		$10.setProducer(PortWindow4); 
		PortWindow4.$10 = $10;
		Thread thread_PortWindow4 = new Thread(PortWindow4);
		PortWindow5 PortWindow5 = new PortWindow5();
		$11.setProducer(PortWindow5); 
		PortWindow5.$11 = $11;
		Thread thread_PortWindow5 = new Thread(PortWindow5);
		Protocol1 Protocol1 = new Protocol1();
		$7.setConsumer(Protocol1); 
		$10.setConsumer(Protocol1); 
		$11.setConsumer(Protocol1); 
		$8.setProducer(Protocol1); 
		$9.setConsumer(Protocol1); 
		Protocol1.$7 = $7;
		Protocol1.$10 = $10;
		Protocol1.$11 = $11;
		Protocol1.$8 = $8;
		Protocol1.$9 = $9;
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

		public volatile Port<String> $7;
		 

		public void activate() {
			synchronized (this) {
				notify();
			}
		}

		public void run() {
			Windows.producer("a_2", $7);
		}
	}

	private static class PortWindow2 implements Component {

		public volatile Port<String> $8;
		 

		public void activate() {
			synchronized (this) {
				notify();
			}
		}

		public void run() {
			Windows.consumer("b_1", $8);
		}
	}

	private static class PortWindow3 implements Component {

		public volatile Port<String> $9;
		 

		public void activate() {
			synchronized (this) {
				notify();
			}
		}

		public void run() {
			Windows.producer("a_1", $9);
		}
	}

	private static class PortWindow4 implements Component {

		public volatile Port<String> $10;
		 

		public void activate() {
			synchronized (this) {
				notify();
			}
		}

		public void run() {
			Windows.producer("a_4", $10);
		}
	}

	private static class PortWindow5 implements Component {

		public volatile Port<String> $11;
		 

		public void activate() {
			synchronized (this) {
				notify();
			}
		}

		public void run() {
			Windows.producer("a_3", $11);
		}
	}

	private static class Protocol1 implements Component {

		public volatile Port<String> $7;
		public volatile Port<String> $10;
		public volatile Port<String> $11;
		public volatile Port<String> $8;
		public volatile Port<String> $9;
		private String m2  = null ; 
		private String m3  = null ; 
		private String m1  = null ; 
		private String m4  = null ;  

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
					return ($8.hasGet() && (null == null && 
			           m1 == null && 
			           m2 == null && 
			           m3 == null && 
			           !($10.peek() == null) && 
			           m4 == null && 
			           !($7.peek() == null) && 
			           !($9.peek() == null) && 
			           !($11.peek() == null)));
				}
			},
			new Guard(){
				public Boolean guard(){
					return ((m1 == $8.peek() && 
			           !(m1 == null)));
				}
			},
			new Guard(){
				public Boolean guard(){
					return ((!(m2 == null) && 
			           m1 == null));
				}
			},
			new Guard(){
				public Boolean guard(){
					return ((m3 == null && 
			           !(m4 == null)));
				}
			},
			new Guard(){
				public Boolean guard(){
					return ((!(m3 == null) && 
			           m2 == null));
				}
			},		
		};

		private Command[] commands = new Command[]{
			new Command(){
				public void update(){
					$8.put($9.peek()); 
					m2 = $11.peek();
					m3 = $10.peek();
					m1 = $7.peek(); 
					 
					$10.get();
					$7.get();
					$11.get();
					$9.get();
				}
			},
			new Command(){
				public void update(){
					 
					 
					m1 = null; 

				}
			},
			new Command(){
				public void update(){
					 
					m1 = m2; 
					m2 = null;
					 

				}
			},
			new Command(){
				public void update(){
					 
					m3 = m4; 
					m4 = null;
					 

				}
			},
			new Command(){
				public void update(){
					 
					m2 = m3;
					 
					m3 = null; 

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
						if ($8.hasGet() && (null == null && 
						     m1 == null && 
						     m2 == null && 
						     m3 == null && 
						     !($10.peek() == null) && 
						     m4 == null && 
						     !($7.peek() == null) && 
						     !($9.peek() == null) && 
						     !($11.peek() == null))) break;
						if ((m1 == $8.peek() && 
						     !(m1 == null))) break;
						if ((!(m2 == null) && 
						     m1 == null)) break;
						if ((m3 == null && 
						     !(m4 == null))) break;
						if ((!(m3 == null) && 
						     m2 == null)) break;
						try { 
							wait(); 
						} catch (InterruptedException e) { }
					}	
				}
			}
		}
	}
}