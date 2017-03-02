import java.util.concurrent.atomic.AtomicInteger;

import nl.cwi.reo.runtime.java.Component;
import nl.cwi.reo.runtime.java.Port;

@SuppressWarnings("initialization")
public class FIFO1<T> implements Component {

	private volatile Port<T> a;
	private volatile Port<T> b;
	
	volatile AtomicInteger run_a = new AtomicInteger();
	volatile AtomicInteger run_b = new AtomicInteger();

	private volatile T m = null;
	private volatile int q = 0;
	
	public FIFO1(Port<T> a, Port<T> b) {
		a.setConsumer(this);
		b.setProducer(this);
		this.a = a;
		this.b = b;
		activate();
	}
	
	public void activate() {
//		System.out.println(Thread.currentThread().getName() + " " + this);

		synchronized (this) {
		execute:
			while (true) {
				switch (q) {
				case 0:
					if (!a.hasGet()) {
						a.setGet();
						run_a.incrementAndGet(); 
					}
					if (q == 0 && a.hasPut()) {
						m = a.take();
						run_a.incrementAndGet();
						q = 1;
						System.out.println("fire a");
						continue;
					}
					break execute;
				case 1:
					if (!b.hasPut()) {
						b.setPut(m);
						run_b.incrementAndGet();
					}
					if (q == 1 && !b.hasPut()) {
						q = 0;
						System.out.println("fire b");
						continue;
					}
					break execute;
				}
			}
		}
	
		if (run_a.get() > 0) { 
			a.activateProducer();
			run_a.decrementAndGet();
		}
		if (run_b.get() > 0) {
			b.activateConsumer();
			run_b.decrementAndGet();
		}
	}

	public void run() {	}
}
