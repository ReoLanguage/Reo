/**
 * Generated from chess.treo by Reo 1.0.
 */

import nl.cwi.reo.runtime.*;

public class chess {

	public static void main(String[] args) {

		Port<String> $20 = new PortWaitNotify<String>();
		Port<String> $13 = new PortWaitNotify<String>();
		Port<String> $15 = new PortWaitNotify<String>();
		Port<String> $16 = new PortWaitNotify<String>();
		Port<String> $17 = new PortWaitNotify<String>();
		Port<String> $18 = new PortWaitNotify<String>();

		Component1 Component1 = new Component1();
		$20.setConsumer(Component1); 
		$13.setProducer(Component1); 
		Component1.$20 = $20;
		Component1.$13 = $13;
		Thread thread_Component1 = new Thread(Component1);
		Component2 Component2 = new Component2();
		$17.setConsumer(Component2); 
		$15.setProducer(Component2); 
		Component2.$17 = $17;
		Component2.$15 = $15;
		Thread thread_Component2 = new Thread(Component2);
		Component3 Component3 = new Component3();
		$18.setConsumer(Component3); 
		$16.setConsumer(Component3); 
		Component3.$18 = $18;
		Component3.$16 = $16;
		Thread thread_Component3 = new Thread(Component3);
		Protocol1 Protocol1 = new Protocol1();
		$20.setConsumer(Protocol1); 
		$13.setConsumer(Protocol1); 
		$15.setConsumer(Protocol1); 
		$16.setProducer(Protocol1); 
		$17.setConsumer(Protocol1); 
		$18.setProducer(Protocol1); 
		Protocol1.$20 = $20;
		Protocol1.$13 = $13;
		Protocol1.$15 = $15;
		Protocol1.$16 = $16;
		Protocol1.$17 = $17;
		Protocol1.$18 = $18;
		Thread thread_Protocol1 = new Thread(Protocol1); 

		thread_Component1.start();
		thread_Component2.start();
		thread_Component3.start();
		thread_Protocol1.start();

		try {
			thread_Component1.join();
			thread_Component2.join();
			thread_Component3.join();
			thread_Protocol1.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static class Component1 implements Component {

		public volatile Port<String> $20;
		public volatile Port<String> $13;
		 

		public void activate() {
			synchronized (this) {
				notify();
			}
		}

		public void run() {
			Workers.Engine($20, $13);
		}
	}

	private static class Component2 implements Component {

		public volatile Port<String> $17;
		public volatile Port<String> $15;
		 

		public void activate() {
			synchronized (this) {
				notify();
			}
		}

		public void run() {
			Workers.Engine($17, $15);
		}
	}

	private static class Component3 implements Component {

		public volatile Port<String> $18;
		public volatile Port<String> $16;
		 

		public void activate() {
			synchronized (this) {
				notify();
			}
		}

		public void run() {
			Workers.Display($18, $16);
		}
	}

	private static class Protocol1 implements Component {

		public volatile Port<String> $20;
		public volatile Port<String> $13;
		public volatile Port<String> $15;
		public volatile Port<String> $16;
		public volatile Port<String> $17;
		public volatile Port<String> $18;
		private String m2  = null ; 
		private String m3  = null ; 
		private String m1  = "";  

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
					return ($20.hasGet() &&  $16.hasGet() && (m2 == null && 
			           !(m3 == null) && 
			           !($20.peek() == null) && 
			           !($15.peek() == null) && 
			           !(Function.parse( $15.peek() ) == null) && 
			           !(Function.concatenate( m3 ,Function.parse( $15.peek() )) == null)));
				}
			},
			new Guard(){
				public Boolean guard(){
					return ($17.hasGet() &&  $18.hasGet() && (m3 == null && 
			           !($13.peek() == null) && 
			           !(m2 == null) && 
			           !($17.peek() == null) && 
			           !(Function.parse( $13.peek() ) == null) && 
			           !(Function.concatenate( m2 ,Function.parse( $13.peek() )) == null)));
				}
			},
			new Guard(){
				public Boolean guard(){
					return ($20.hasGet() && (m2 == null && 
			           !($20.peek() == null) && 
			           !(m1 == null)));
				}
			},
			new Guard(){
				public Boolean guard(){
					return ((m1 == null));
				}
			},		
		};

		private Command[] commands = new Command[]{
			new Command(){
				public void update(){
					$20.put(Function.concatenate( m3 ,Function.parse( $15.peek() )));
					$16.put(Function.parse( $15.peek() )); 
					m2 = Function.concatenate( m3 ,Function.parse( $15.peek() ));
					 
					m3 = null; 
					$20.get();
					$15.get();
				}
			},
			new Command(){
				public void update(){
					$17.put(Function.concatenate( m2 ,Function.parse( $13.peek() )));
					$18.put(Function.parse( $13.peek() )); 
					m3 = Function.concatenate( m2 ,Function.parse( $13.peek() )); 
					m2 = null;
					 
					$13.get();
					$17.get();
				}
			},
			new Command(){
				public void update(){
					$20.put(m1); 
					m2 = m1;
					 
					m1 = null; 
					$20.get();
				}
			},
			new Command(){
				public void update(){
					 
					 
					 

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
						if ($20.hasGet() &&  $16.hasGet() && (m2 == null && 
						     !(m3 == null) && 
						     !($20.peek() == null) && 
						     !($15.peek() == null) && 
						     !(Function.parse( $15.peek() ) == null) && 
						     !(Function.concatenate( m3 ,Function.parse( $15.peek() )) == null))) break;
						if ($17.hasGet() &&  $18.hasGet() && (m3 == null && 
						     !($13.peek() == null) && 
						     !(m2 == null) && 
						     !($17.peek() == null) && 
						     !(Function.parse( $13.peek() ) == null) && 
						     !(Function.concatenate( m2 ,Function.parse( $13.peek() )) == null))) break;
						if ($20.hasGet() && (m2 == null && 
						     !($20.peek() == null) && 
						     !(m1 == null))) break;
						if ((m1 == null)) break;
						try { 
							wait(); 
						} catch (InterruptedException e) { }
					}	
				}
			}
		}
	}
}