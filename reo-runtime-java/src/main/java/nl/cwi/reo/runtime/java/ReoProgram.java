package nl.cwi.reo.runtime.java;

public class ReoProgram {

	public static void main(String[] args) {

		Port<Integer> a = new PortBusyWait<Integer>();
		PortFIFOk<Integer> b0 = new PortFIFOk<Integer>(6);
		PortFIFOk<Integer> c0 = new PortFIFOk<Integer>(6);
//		Port<Integer> b0 = new PortBusyWait<Integer>();
//		Port<Integer> c0 = new PortBusyWait<Integer>();
//		Port<Integer> b1 = new PortBusyWait<Integer>();
//		Port<Integer> c1 = new PortBusyWait<Integer>();
//		Port<Integer> b2 = new PortBusyWait<Integer>();
//		Port<Integer> c2 = new PortBusyWait<Integer>();
//		Port<Integer> b3 = new PortBusyWait<Integer>();
//		Port<Integer> c3 = new PortBusyWait<Integer>();

		Component Producer = new Producer(a);
		Component ExclRouter = new ExclusiveRouter<Integer>(a, b0, c0);
		Component Consumer1 = new Consumer(b0);
		Component Consumer2 = new Consumer(c0);
//		new FIFO1<Integer>(b0, b1);
//		new FIFO1<Integer>(c0, c1);
//		new FIFO1<Integer>(b1, b2);
//		new FIFO1<Integer>(c1, c2);
//		new FIFO1<Integer>(b2, b3);
//		new FIFO1<Integer>(c2, c3);

		Thread thread_Producer = new Thread(Producer);
		Thread thread_ExclRouter = new Thread(ExclRouter);
		Thread thread_Consumer1 = new Thread(Consumer1);
		Thread thread_Consumer2 = new Thread(Consumer2);
		
//		thread_ExclRouter.setDaemon(true);

		thread_Producer.start();
		thread_ExclRouter.start();
		thread_Consumer1.start();
		thread_Consumer2.start();

		thread_Producer.setPriority(10);
		thread_ExclRouter.setPriority(10);
		thread_Consumer1.setPriority(1);
		thread_Consumer2.setPriority(1);

		try {
			thread_Producer.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		System.out.println(b0.empty);
		
		System.exit(0);
	}
}
