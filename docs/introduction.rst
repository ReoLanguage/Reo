Introduction to Reo
===================

A simple concurrent program
---------------------------

Writing correct concurrent programs is far more difficult than writing correct sequential programs.
Let us start with very simple program that repeatedly prints "Hello, " and "world!" in alternating order.
We split this program into three different processes: two producers that output a string "Hello, " and "world!", respectively, 
and a consumer that **alternately** prints the produced strings "Hello, " and "world!", starting of course with "Hello, ". 

If you are asked to write a small program that implements the above informal specification, you may come up with the following Java code:

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

The main method in the above Java code instantiates three different Java `threads <https://docs.oracle.com/javase/tutorial/essential/concurrency/runthread.html>`_, namely a Red and Green producer and a Blue consumer.
These threads communicate with each other via a shared buffer that is protected by a `semaphore <https://en.wikipedia.org/wiki/Semaphore_(programming)>`_.
If a thread wants to operate on the buffer, it tries acquire a token from the semaphore that protects the buffer.
Once acquired, the process can write to the buffer without being disturbed by any other process.
Finally, the process releases the token, which allows other processes to operate on the buffer.

The same stategy is used to alternate the writes to the buffer. Each producer has its own semaphore. 
If a producer wants to write to a buffer, it first tries to acquire a token from its semaphore.
After writing to the buffer, the producer hands over the token to the other producer.

Analysis
--------

Let us now analyze the Java implementation by answering a few simple questions.

1. Where is the "Hello, " string computed?

On line 15.

2. Where is the text printed?

On line 53.

For the next question, however, it is not possible to point at a single line of code:

3. Where is the protocol?

		a. What determines which producers goes first? 

		This is determined by the initial value of the semaphores on lines 5 and 6, together with the usage of the semaphores on lines 17, 21, 33, and 37.

		b. What takes care of buffer protection? 

		This program establishes buffer protection by the code on lines 18, 20, 34, 36, 51, and 57.

The reason why this third question is much more difficult to answer is because the protocol is **implicit**.

For such a simple program, you may argue that the fact that the protocol is implicit is not big deal.
However, if you really think this, then you may be surprised by the output:

.. code-block:: text

	Hello, world! Hello, Hello, world! Hello, Hello, Hello, Hello, Hello, 

There is a bug! Can you spot the error?

Reo protocols
-------------

The Reo language offers a solution by providing a domain specific language that allow you to declare your protocol explicitly.
The following diagram shows an example of such an explicit protocol:

.. image:: https://github.com/kasperdokter/Reo/blob/master/docs/ProdCons.jpg?raw=true

Every process is represented as a box together with a set of ports that define the interface of each process. 
These boxes, called components, are connected via a network of channels and nodes, which constitutes the protocol.
The components now interact with each other by offering messages to the protocol. 
The protocol, then, synchonizes components and exchanges the messages.

The channel between Red and Green is a *syncdrain* channel that accepts data from both its input ends simultaneously, and then it looses the data.
The channel between Red and Blue is a *sync* channel that atomically takes data from its input end and offers this data to its output end.
The other incoming channel connected to Blue is a *fifo* channel that stores a single data item that it receives at its input end. 
After the buffer became full, it offers this data to its output end. 
Suppose Red wants to output some data. Then, Red issues a *put request* at its port. 
As soon as Green has also issued a *put request*, and Blue issued a *get request*, the protocol synchronously accepts the data produced by Red and Green, offers Greens data to Blue, and stores Reds data in a buffer. 
Upon the next get request by Blue, Blue receives the data from the buffer, after which the protocol returned to its initial configuration.
Therefore, this protocol implements the informal specification that prescribes alternation.

Although we may think of such a protocol as `message passing <http://mpi-forum.org/>`_, the code that is generated by the compiler 
is (depending on the target) based on shared memory. 

It's possible to design the previous problem with Reo, by following two steps. 
On the one hand, the user should define the actors, ie in this case, what a Producer and a Consumer are. 
The user should also came up, on the other hand, with a suitable protocol linking those actors, and describing the desired interaction. 
The strong advantage of such designing is that the design of actors is completly decoupled from the design of protocol, which lead us to a new paradigm of action/interaction programming. 

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














 


