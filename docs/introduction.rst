Introduction to Reo
===================

Why do you need Reo
-------------------

Suppose we ask you to design a small program that consists of three components: two producers that output a string "Hello, " and "world!", respectively, and a consumer that **alternately** 
prints the produced strings "Hello, " and "world!", starting with "Hello, ". 

After a couple of minutes, or an hour, or maybe a day, your may come up with the following piece of Java code that starts three threads: 

.. code-block:: java
	:linenos:

	import java.util.concurrent.Semaphore;

	public class Main {
	
		private static final Semaphore greenSemaphore = new Semaphore(0);
		private static final Semaphore redSemaphore = new Semaphore(1);
		private static final Semaphore bufferSemaphore = new Semaphore(1);
		private static String buffer = null;
	
		public static void main(String[] args) {
			Thread redProducer = new Thread("Red Producer") {
				public void run() {			
					while (true) {
						for (int i = 0; i < 30000000; ++i);
						String redText = "Hello, ";
						try {
							redSemaphore.acquire();
							bufferSemaphore.acquire();
							buffer = redText;
							bufferSemaphore.release();
							greenSemaphore.release();
						} catch (InterruptedException e) { }
					}
				}
			};
		
			Thread greenProducer = new Thread("Green Producer") {
				public void run() {				
					while (true) {
						for (int i = 0; i < 50000000; ++i);
						String redText = "world! ";
						try {
							greenSemaphore.acquire();
							bufferSemaphore.acquire();
							buffer = redText;
							bufferSemaphore.release();
							redSemaphore.release();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			};
		
			Thread blueConsumer = new Thread("Blue Consumer") {
				public void run() {	
					int k = 0;
					while (k < 10) {
						for (int i = 0; i < 40000000; ++i);					
						try {
							bufferSemaphore.acquire();
							if (buffer != null) {
								System.out.print(buffer);
								buffer = null; 
								k++;
							}
							bufferSemaphore.release();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			};		
			
			redProducer.start();
			greenProducer.start();
			blueConsumer.start();
		
			try {
				blueConsumer.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

This code uses some semaphores to make sure that the Red Producer and the Green Producer alternately write their string to the buffer.

Now, you may be convinced that this Java program  of the alternating producers problem, let's take a look at the output:
 
.. code-block:: text
	Hello, world! Hello, Hello, world! Hello, Hello, Hello, Hello, Hello, 

How to design Reo protocols
---------------------------

The example above indicates how difficult it can be to write *correct* concurrent code.

