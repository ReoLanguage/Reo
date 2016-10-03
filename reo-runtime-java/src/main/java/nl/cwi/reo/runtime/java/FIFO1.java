package nl.cwi.reo.runtime.java;

public class FIFO1<T> implements Component {

	private volatile Port<T> a;
	private volatile Port<T> b;

	private volatile boolean empty;
	
	public FIFO1(Port<T> a, Port<T> b) {
		a.setConsumer(this);
		b.setProducer(this);
		a.setGet();
		this.empty = true;
		this.a = a;
		this.b = b;
	}
	
	public void activate() {
		if (empty && a.canGet()) {
			synchronized (this) {
				if (empty && a.canGet()) {
					empty = false;
					T datum = a.get();
					b.setPut(datum);
				}
			}
			a.activateProducer();
			b.activateConsumer();
		}
		if (!empty && !b.canGet()) {
			synchronized (this) {
				if (!empty && !b.canGet()) {
					empty = true;
					a.setGet();	
				}
			}
			a.activateProducer();
		}
	}

	public void run() {	}
}
