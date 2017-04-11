/**
 * Generated from ../example/test.rba.treo by Reo 1.0.
 */
import nl.cwi.reo.runtime.java.*;

public class test {

  public static void main(String[] args) {

    Port<Object> a = new PortBusyWait<>();
    Port<Object> b = new PortBusyWait<>();
    Port<Object> c = new PortBusyWait<>();

    Component Protocol0 = new Protocol0(a, b, c);

 	Thread thread_Protocol0 = new Thread(Protocol0); 

	thread_Protocol0.start();

    try {
      thread_Protocol0.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

  }

  private static class Protocol0 implements Component {

  	private volatile Port<Object> a;

  	private volatile Port<Object> b;

  	private volatile Port<Object> c;

  	private Object q1;

  	private Object q1;

  	public Protocol0(Port<Object> a, Port<Object> b, Port<Object> c) {
  		a.setConsumer(this);
  		b.setConsumer(this);
  		c.setConsumer(this);
  		this.a = a;
  		this.b = b;
  		this.c = c;
  		activate();
  	}

  	public synchronized void activate() {
  		notify();
  	}

  	public void run() {
  		int k = 0;
  		while (true) {
  			k++;
  			if ((!((q1 == null))) && c.hasGet() && c.hasPut()) {
  				c.put(q1); 
  				q1 = null; 
  				k = 0;
  			}
  			if (((q1 == null)) && a.hasGet() && a.hasPut() && b.hasPut() && c.hasPut()) {
  				a.put(c.get()); 
  				q1 = b.get(); 
  				k = 0;
  			}
  			if ((!((q1 == null))) && c.hasPut()) {
  				 
  				q1 = null;
  				q1 = c.get(); 
  				k = 0;
  			}
  			if (((q1 == null)) && a.hasGet() && a.hasPut() && b.hasPut() && c.hasPut()) {
  				a.put(c.get()); 
  				q1 = b.get(); 
  				k = 0;
  			}
  			if (((q1 == null)) && a.hasGet() && a.hasPut() && b.hasPut() && c.hasPut()) {
  				a.put(c.get()); 
  				q1 = b.get(); 
  				k = 0;
  			}
  			if (((q1 == null)) && a.hasGet() && a.hasPut() && b.hasPut() && c.hasPut()) {
  				a.put(c.get()); 
  				q1 = b.get(); 
  				k = 0;
  			}
  			if (k > 3) {
  				k = 0;
  				synchronized (this) {
  					try { 
  						wait(); 
  					} catch (InterruptedException e) { }
  				}	
  			}
  		}
  	}
  }


}
