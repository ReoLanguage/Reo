import nl.cwi.reo.runtime.java.Component;
import nl.cwi.reo.runtime.java.Port;
import nl.cwi.reo.runtime.java.PortBusyWait;
import nl.cwi.reo.runtime.java.PortRingBuffer;

@SuppressWarnings("initialization")
public class Main {

	public static void main(String[] args) {

		Port<Integer> a = new PortRingBuffer<Integer>(50);
		
		Component Producer = new Producer(a);
		Component Consumer = new Consumer(a);

		Thread thread_Producer = new Thread(Producer);
		Thread thread_Consumer = new Thread(Consumer);

		thread_Producer.start();
		thread_Consumer.start();

		try {
			thread_Producer.join();
			thread_Consumer.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}	
		
		System.exit(0);
	}
	
	public static void system1() {
		
		Port<Integer> a = new PortBusyWait<Integer>();
		Port<Integer> b0 = new PortBusyWait<Integer>();
		Port<Integer> c0 = new PortBusyWait<Integer>();
		Port<Integer> b1 = new PortBusyWait<Integer>();
		Port<Integer> c1 = new PortBusyWait<Integer>();
		Port<Integer> b2 = new PortBusyWait<Integer>();
		Port<Integer> c2 = new PortBusyWait<Integer>();
		Port<Integer> b3 = new PortBusyWait<Integer>();
		Port<Integer> c3 = new PortBusyWait<Integer>();

		Component Producer = new Producer(a);
		Component ExclRouter = new ExclusiveRouter<Integer>(a, b0, c0);
		Component Consumer1 = new Consumer(b3);
		Component Consumer2 = new Consumer(c3);
		new FIFO1<Integer>(b0, b1);
		new FIFO1<Integer>(c0, c1);
		new FIFO1<Integer>(b1, b2);
		new FIFO1<Integer>(c1, c2);
		new FIFO1<Integer>(b2, b3);
		new FIFO1<Integer>(c2, c3);

		Thread thread_Producer = new Thread(Producer);
		Thread thread_ExclRouter = new Thread(ExclRouter);
		Thread thread_Consumer1 = new Thread(Consumer1);
		Thread thread_Consumer2 = new Thread(Consumer2);
		
//		thread_ExclRouter.setDaemon(true);

		thread_Producer.start();
		thread_ExclRouter.start();
		thread_Consumer1.start();
		thread_Consumer2.start();

//		thread_Producer.setPriority(10);
//		thread_ExclRouter.setPriority(10);
//		thread_Consumer1.setPriority(1);
//		thread_Consumer2.setPriority(1);

		try {
			thread_Producer.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}		
	}
	
	public static void system2() {
		Port<Integer> a = new PortBusyWait<Integer>();
		Port<Integer> b = new PortRingBuffer<Integer>(3);
		Port<Integer> c = new PortRingBuffer<Integer>(3);

		Component Producer = new Producer(a);
		Component ExclRouter = new ExclusiveRouter<Integer>(a, b, c);
		Component Consumer1 = new Consumer(b);
		Component Consumer2 = new Consumer(c);

		Thread thread_Producer = new Thread(Producer);
		Thread thread_ExclRouter = new Thread(ExclRouter);
		Thread thread_Consumer1 = new Thread(Consumer1);
		Thread thread_Consumer2 = new Thread(Consumer2);

		thread_Producer.start();
		thread_ExclRouter.start();
		thread_Consumer1.start();
		thread_Consumer2.start();

		try {
			thread_Producer.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	public static void system3() {
//		Port<Integer> a = new PortBuffer<Integer>(100);
		PortRingBuffer<Integer> a = new PortRingBuffer<Integer>(1);
//		Port<Integer> a = new PortBusyWait<Integer>();

		Component Producer = new Producer(a);
		Component Consumer = new Consumer(a);

		Thread thread_Producer = new Thread(Producer);
		Thread thread_Consumer = new Thread(Consumer);

		thread_Producer.start();
		thread_Consumer.start();

		try {
			thread_Producer.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}
