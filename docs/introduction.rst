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
It's possible to design the previous problem with Reo, by following two steps. On the one hand, the user should define the actors, ie in this case, what a Producer and a Consumer are. The user should also came up, on the other hand, with a suitable protocol linking those actors, and describing the desired interaction. The strong advantage of such designing is that the design of actors is completly decoupled from the design of protocol, which lead us to a new paradigm of action/interaction programming. 
A simple graphical representation of what could be a Reo protocol is given in the following picture :

.. image:: https://github.com/kasperdokter/Reo/blob/master/docs/ProdCons.jpg?raw=true

Green and Red boxes are Producer actors, and Blue is a Consumer actor. They all have ports on which they can send and/or receive data. The arrows and boxes in between model the Reo circuit, and implement the desired protocol. In this case, the arrow between Red and Green represent a *synchronised* channel, which means that Red and Green must produce data at the same time. Data produced by Green is sent to Blue, and data produced by Red is sent to a *fifo* channel. This *fifo* channel will store the data for next iteration. During the next iteration, the *fifo* channel is full, and by definition (pointer to corresponding paper that define channel), Red cannot produce. As soon as Green and Red are synchronized, Green cannot produce also. The fifo become empty by sending its data to the consumer Blue.
As defined, the protocol lets the consumer Blue receive alternatly a Green production and a Red production. The corresponding code for this program is :

Red Producer 'ProducerRed.java':

.. code-block:: java
	:linenos:
	
	
	public class Producer {	
		private Port output;
		public static void main(String[] args) {
                	Thread redProducer = new Thread("Red Producer") {
                        	public void run() {
                                	while (true) {
                                        	sleep(5000);
						producerText= "Red" ;
						put(output,producerText);
                                        	} catch (InterruptedException e) { }
					}
                                }
                        }
                };

Green Producer 'ProducerGreen.java':

.. code-block:: java
	:linenos:
	
	public class Producer {		
		private Port output;
		public static void main(String[] args) {
                	Thread redProducer = new Thread("Green Producer") {
                        	public void run() {
                                	while (true) {
                                        	sleep(5000);
						producerText= "Green" ;
						put(output,producerText);
                                        	} catch (InterruptedException e) { }
					}
                                }
                        }
                };

Blue Consumer 'Consumer.java':

.. code-block:: java
	:linenos:

	public class Consumer {
		private Port input;
		public static void main(String[] args) {
                	Thread redProducer = new Thread("Consumer") {
                        	public void run() {
                                	while (true) {
                                        	sleep(4000);
						get(input,displayText);
						print(displayText);
                                        	} catch (InterruptedException e) { }
                                	}
                        	}
			}
                };

Let's remark that the first Java program is now separated into three classes : Green Producer, Red Producer and Blue Consumer. This represents all the user will have to hard code in Java. The remaining part deals with the protocol, and is written in Reo (which then, is compiled to Java or other languages).


Reo Protocol 'main.treo' :

.. code-block:: text

	main = ProducerConsumer(a,b,c){
		alternator(a,b,c){
			syncdrain(a, b) sync(b, x) fifo(x, c) sync(a, c)
		}
		producerRed(k){
			Java: ProducerRed.java
		}
		producerGreen(k){
			Java: ProducerGreen.java
		}
		consumer(k){
			Java: Consumer.java 
		}
		
		producer<red>(a) producer<green>(b) consumer(c) alternator(a,b,c)
	}

One thing to notify from the precedent program is the presence of input and output port in the definition of a Producer and Consumer. A port is the point where external exchanges are processed according to the protocol we defined. 
	
Few little steps more before being able to run our program : generate the Main, and run it in a suitable runtime environment. You can provide your own runtime environment or use the own by default.

Compile and link to the runtime
-------------------------------

The 'reo.jar' archive is what you need to run the interpreter and the compiler. Expecting that you defined your circuit in a file with the '.treo' extension, you can now run  ::

	java -jar reo.jar test.treo -cp ./

The command will generate all the class in the direcory specified by '-cp' option.
You can now link all classes generated by the compiler to the runtime environment (where ProducerRed.java ProducerGreen.java and Consumer.java are defined) ::

	javac -cp reo-runtime-java.jar *.java

All the link are set, you can now run the Main class with the runtime environment ::

	java -cp .:reo-runtime-java.jar Main

