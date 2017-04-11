/**
 * Generated from ../example/test.rba.treo by Reo 1.0.
 */
import nl.cwi.reo.runtime.java.*;

public class test {

  public static void main(String[] args) {

    Port<Object> a = new PortWaitNotify<Object>();
    Port<Object> b = new PortWaitNotify<Object>();

    Component Protocol0 = new Protocol0(a, b);
    Component Comp0 = new WindowConsumer("b",b);
    Component Comp1 = new WindowProducer("a",a);
    
 	Thread thread_Protocol0 = new Thread(Protocol0); 
 	Thread thread_Comp0 = new Thread(Comp0);
 	Thread thread_Comp1 = new Thread(Comp1);
 	
	thread_Protocol0.start();
	thread_Comp0.start();
	thread_Comp1.start();

    try {
      thread_Protocol0.join();
      thread_Comp0.join();
      thread_Comp0.join();

    } catch (InterruptedException e) {
      e.printStackTrace();
    }

  }

  private static class Protocol0 implements Component {

  	private volatile Port<Object> a;

  	private volatile Port<Object> b;

  	private Object q1;

  	public Protocol0(Port<Object> a, Port<Object> b) {
  		a.setConsumer(this);
  		b.setProducer(this);
  		this.a = a;
  		this.b = b;
  		activate();
  	}

  	public synchronized void activate() {
  		notify();
  	}

  	public void run() {
  		int k = 0;
  		while (true) {
  			k++;
  			if (((q1 == null)) && a.hasPut()) {
  				 
  				q1 = a.get(); 
  				k = 0;
  			}
  			if (((q1 == null)) && a.hasPut()) {
  				 
  				q1 = a.get(); 
  				k = 0;
  			}
  			if ((!((q1 == null))) && b.hasGet()) {
  				b.put(q1); 
  				q1 = null; 
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
