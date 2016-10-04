package nl.cwi.reo.runtime.java.example;

import nl.cwi.reo.runtime.java.Component;
import nl.cwi.reo.runtime.java.Port;

public class FIFO1<T> implements Component {

	private volatile Port<T> a;
	private volatile Port<T> b;

	private volatile T m;
	private volatile int q;
	
	public FIFO1(Port<T> a, Port<T> b) {
		a.setConsumer(this);
		b.setProducer(this);
		m = null;
		q = 0;
		this.a = a;
		this.b = b;
		activate();
	}
	
	public void activate() {
		while (true) {
			switch (q) {
			case 0:
				synchronized (this) {
					if (!a.hasGet()) a.setGet(); 
				}
				if (a.hasPut()) {
					synchronized (this) {
						if (q == 0 && a.hasPut()) {
							m = a.take();
							q = 1;
						}
					}
					a.activateProducer();
					b.activateConsumer();
					continue;
				}
			case 1:
				synchronized (this) {
					if (m != null) {
						b.setPut(m);
						m = null;
					}
				}
				if (b.hasGet() || !b.hasPut()) {
					synchronized (this) {
						if (q == 1 && (b.hasGet() || !b.hasPut()))
							q = 0;
					}
					a.activateProducer();
					continue;
				}
			}
			return;
		}
	}

	public void run() {	}
}
