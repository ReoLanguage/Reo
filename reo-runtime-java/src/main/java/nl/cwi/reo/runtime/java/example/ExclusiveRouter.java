package nl.cwi.reo.runtime.java.example;

import nl.cwi.reo.runtime.java.Component;
import nl.cwi.reo.runtime.java.Port;

public class ExclusiveRouter<T> implements Component {
	
	private volatile Port<T> a;
	private volatile Port<T> b;
	private volatile Port<T> c;

	public ExclusiveRouter(Port<T> a, Port<T> b, Port<T> c) {
		a.setConsumer(this);
		b.setProducer(this);
		c.setProducer(this);
		this.a = a;
		this.b = b;
		this.c = c;
	}
	
	public void activate() { 
//		synchronized (this) {
//			notify();	
//		} 
	}
	
	public void run() {
		while (true) {
//			synchronized (this) {
//				while (!a.canGet() || (!b.canPut() && !c.canPut())) {
//					try { wait(); } catch (InterruptedException e) { }	
//				}
//			}
			if (a.hasPut() && b.hasGet()) {
				T d_a = a.get();
				a.activateProducer();
				b.setPut(d_a);
				b.activateConsumer();
				System.out.println("fire ab");
			}
			if (a.hasPut() && c.hasGet()) {
				T d_a = a.get();
				a.activateProducer();
				c.setPut(d_a);
				c.activateConsumer();
				System.out.println("fire ac");
			}		
		}		
	}
}