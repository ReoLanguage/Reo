import nl.cwi.reo.components.Workers;
import nl.cwi.reo.runtime.Component;
import nl.cwi.reo.runtime.Port;

public class MyComponent implements Runnable {

	private Component[] components;
	
	public MyComponent() {
		components = new Component[2];
		components[0] = new Producer();
	}

	@Override
	public void run() {
		
	}
	
	public void schedule() {
		
	}
	
	public static void main() {
	}
	
	private static class Producer implements Component {

		public volatile Port<String> a;

		public void activate() {
			synchronized (this) {
				notify();
			}
		}

		public void run() {
			Workers.producer(a);
		}
	}
}