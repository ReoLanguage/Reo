Library of components
=====================

We list all components written in the "./example/slide/" folder, and shortly explain their behavior.

1. Atomic components
--------------------
We list a set of atomic components, and use a logical formula as semantic (see tutorial).

Sync
^^^^
The sync channel atomically get data from input port a, to output port b.

.. code-block:: text
   
	// sync.treo
	sync(a?String, b!String) {
	   #RBA
	   a=b
	}

Fifo1
^^^^^
The fifo1 channel behaves as a buffer of size one, where a is its input port, and b is its ouput port. The internal memory $m is hidden.

.. code-block:: text
   
	// fifo1.treo
	fifo1(a?String, b!String) {
	   #RBA
	   $m=*;
	   a!=*, $m' = a, b=*, $m=*
	   a!=*, $m' = a, b=*, $m=*
	}

Syncdrain
^^^^^^^^^
The syncdrain channel ensures that data flows atomically at port a and b, without any constraint on the data.

.. code-block:: text
   
	// syncdrain.treo
	syncdrain(a?String, b?String) {
	   #RBA
	   a!=*, b!=*
	   a=*, b=*
	}

Lossysync
^^^^^^^^^
The lossysync channel either get atomically data from the input a, to the output port b; or dropes the data from input port a.

.. code-block:: text
   
	// lossysync.treo
	lossysync(a?String, b?String) {
	   #RBA
	   a!=*, b!=*, a=b
	   a!=*
	   a=*, b=*
	}

Filter
^^^^^^
The fileter channel

.. code-block:: text

	// filter.treo
	filter<P:string>(a?, b!) {
		#RBA
		a!=*, P(a), a=b
		a!=*, b=*, !P(a)
	}


2. Connector components
-----------------------

Regulatorwwr
^^^^^^^^^^^^
The regulator write/write/read (regulatorwwr)

.. code-block:: text

	import reo.sync;
	import reo.syncdrain;

	regulatorwwr(a, b, c) {
	  sync(a, m) syncdrain(m, b) sync(m, c)
	}


Barrier
^^^^^^^
The barrier connector

.. code-block:: text
   
	// barrier.treo
	import reo.sync;
	import slides.regulatorwwr.regulatorwwr;

	barrier(a, b, c, d) {
	  regulatorwwr(a, m, c) sync(b,m) sync(m, d)
	}


Alternator
^^^^^^^^^^
The alternator connector

.. code-block:: text
   
	// alternator.treo
	import reo.fifo1;
	import reo.sync;
	import reo.syncdrain;

	alternator(a, b, c) {
	  syncdrain(a, b) sync(b, x) fifo1(x, c)
	  sync(a, c)
	}



Circulator
^^^^^^^^^^
The circulator connector

.. code-block:: text
   
	// circ1.treo
	import slides.regulatorwwr.regulatorwwr;
	import reo.syncdrain;
	import reo.sync;

	circ1(a?, b?, c?, d!) {
	 regulatorwwr(a, x, d) sync(b, x) sync(c, x)
	}


Ovflfifo
^^^^^^^^
The over flow fifo connector

.. code-block:: text
   
	import reo.lossy;
	import reo.fifo1;

	ovflfifo(a,b){
		lossy(a,m) fifo1(m,b)
	}


Regulatorwrr
^^^^^^^^^^^^
The regulatorwrr connector

.. code-block:: text

	import reo.sync;

	regulatorwrr(a, b, c) {
	  sync(a, m) sync(m, b) sync(m, c)
	}


Seqp
^^^^
The sequencer producer connector

.. code-block:: text

	import reo.fifo1;
	import reo.fifofull;
	import reo.sync;

	seqp(p[1..n]) {
	  {
	    fifo1(x[i], x[i+1])
	    sync(x[i+1], p[i])
	  | 
	    i : <2..n>
	  }
	  fifofull<"0">(x[1], x[2])
	  sync(x[2],p[1])
	  sync(x[n+1], x[1])
	}

Seqc
^^^^
The sequencer consumer connector

.. code-block:: text

	import reo.syncdrain;
	import slides.sequencer.seqp;

	seqc(p[1..n]) {
	  seqp(x[1..n]) 
	  { syncdrain(x[i], p[i]) | i : <1..n> }
	}

Shiftlossyfifo
^^^^^^^^^^^^^^
The shiftlossyfifo connector

.. code-block:: text

	import reo.sync;
	import reo.fifo1;
	import reo.fifofull;
	import reo.syncdrain;
	import slides.xrouter.xrouter;
	import reo.sync;
	import reo.syncdrain;
	import reo.lossy;

	shiftlossyfifo(in, out) {
	  sync(in,a) fifo1(a, b) fifo1(b, c) 
	  xrouter (c,d,e)
	  syncdrain(a,g) sync(d,f) sync(e,g) sync(f,out) fifofull<"0">(f,g)
	}

Variable
^^^^^^^^
The variable connector

.. code-block:: text

	import reo.sync;
	import slides.shiftlossyfifo.shiftlossyfifo;

	variable(a, b) {
	  sync(a, x) sync(x, y) shiftlossyfifo(y, z) 
	  sync(z, b) sync(z, t)  shiftlossyfifo(t, y)
	  sync(x, t)
	}

Xrouter
^^^^^^^
The xrouter connector

.. code-block:: text

	import reo.sync;
	import reo.syncdrain;
	import reo.lossy;

	xrouter(in, out[1..n]) {
	  sync(in, s) syncdrain(s, m)
	  {lossy(s, x[i]) sync(x[i], m) sync(x[i], out[i]) | i:<1..n> }
	}

3. Boundary components
----------------------
Depending on your target language, you may want to compose your protocol with specific functions in say Java, C, Promela, Maude, . . . . We list some examples of boundary components for target languages currently supported.

3.1 Java
^^^^^^^^

Producer
""""""""
A producer component with a single output port, and a Java method as semantic.

.. code-block:: text

	//producer.treo
	producer(a!String) 
	{
	  #JAVA "Producer.produce"
	}

The producer component is linked with the following class, where the datum " Hello " is produced 100 times. For more information on the runtime of ports, see runtime section.

.. code-block:: java

	import nl.cwi.reo.runtime.Input;
	import nl.cwi.reo.runtime.Output;

	public class Producer {
	
		public static void produce(Output<String> port) {
		        for (int i = 1; i < 100; i++) {
		                port.put(" Hello ");
		        }
			System.out.println("Producer finished.");
		}
	}

Consumer
""""""""
A consumer component with a single intput port, and a Java method as semantic.

.. code-block:: text

	//consumer.treo
	consumer(a!String) 
	{
	  #JAVA "Consumer.consume"
	}

The consumer component is linked with the following class, where the method get() is performed 100 times, and the datume is displayed. For more information on the runtime of ports, see runtime section.

.. code-block:: java

	import nl.cwi.reo.runtime.Input;
	import nl.cwi.reo.runtime.Output;

	public class Consumer {
	
		public static void consume(Input<String> a) {
		        for (int i = 1; i < 100; i++) {
				System.out.println(a.get());
		        }
			System.out.println("Consumer finished.");
		}
	}

EvenFilter
""""""""""
An EvenFilter component instantiates a filter (see atomic filter component) with a boolean java method called Relation.Even .

.. code-block:: text

	import reo.filter;

	filterJava(a?String,b!String){
		filter<"Relation.Even">(a,b)
	}

The Java class Relation linked to the component contains two boolean methods: Even and Odd. 

.. code-block:: java

	public class Relation {

		public static boolean Even(String datum) {
			if (datum == null) return false;
			return Integer.parseInt(datum) % 2 == 0;
		}

		public static boolean Odd(String datum) {
			if (datum == null) return false;
			return Integer.parseInt(datum) % 2 != 0;
		}

	}


3.2 Promela
^^^^^^^^^^^


