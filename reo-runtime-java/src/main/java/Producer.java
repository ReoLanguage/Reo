import nl.cwi.reo.runtime.java.Component;
import nl.cwi.reo.runtime.java.Port;

@SuppressWarnings("initialization")
public class Producer implements Component {

	private volatile Port<Integer> a;
	
	public Producer(Port<Integer> a) {
		a.setProducer(this);
		this.a = a;
	}
	
	public void activate() { 
		synchronized (this) {
			notify();	
		} 
	}

	public void run() {	
		MyTestComponents.produce(a); 
	}
}
