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
		synchronized (this) {
			notify();	
		} 
	}
	
	public void run() {		
		while (true) {			
			if (a.hasPut() && b.hasGet()) {
				T d_a = a.get();
				b.put(d_a);
				continue;
			}
			if (a.hasPut() && c.hasGet()) {
				T d_a = a.get();
				c.put(d_a);
				continue;
			}
			synchronized (this) {
				if (!a.hasPut() || (!b.hasGet() && !c.hasGet())) 
					try { wait(); } catch (InterruptedException e) { }	
			}
		}
	}
}