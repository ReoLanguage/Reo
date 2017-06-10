Reo Tutorial
============

Components and interfaces
-------------------------

Reo programs consist of *components* that interact via shared *nodes*.
Each component has access to a given set of nodes, called its *interface*.
To distinguish them from other nodes, the nodes that comprise the interface of a component are also called its *ports*.
An atomic component uses each of its ports either for input or for output, but not both.
Ports of a component comprise the only means of communication between the component and its environment: 
a component has no means of communication or concurrency control other than blocking I/O operations that it can perform on its own ports. 
A component may synchronize with its environment via *put* operations on its own output ports and *get* operations on its own input ports.  
The put and get operations may specify an optional time-out. 
A put or get operation blocks until either it succeeds, or its specified time-out expires. 
A put or get operation with no time-out blocks for ever, or until it succeeds.

To define a component in Reo, we refer to Java source code and/or define its behavior as a constraint automaton, or, define it as a composition of other components.

1. Reference to Java source code
--------------------------------

First of all, we can define a component by referring to Java source code.

.. code-block:: text
   
	// buffer.treo
	buffer<init:String>(a?String, b!String) {
	   Java: "MyClass.myBuffer"
	}


This code in buffer.treo defines an atomic component called buffer with an input port a of type String, an output port b of type String, and an parameter init of type String.
The Java-tag indicates that the implementation of this component consists of a piece of Java code. 
The type tags, String, are optional. On default, type tags are Object.
The parameter block ``<init:String>`` is also optional, as we will see for the second possibility to define a component.

The provided reference links to a Java class that implements the producer component as a Java method myProducer in class MyClass. 

.. code-block:: java

	// MyClass.java
	import nl.cwi.reo.runtime.Input;
	import nl.cwi.reo.runtime.Output;

	public class MyClass {	
	   public static void myBuffer(String init, Input<String> a, Output<String> b) {
	      String content = init;
	      while (true) {
	         b.put(content);
	         content = a.get();
	      }
	   }
	} 

Note that the order of the parameters and ports in ``buffer.treo`` is identical to the order of the arguments of the static function ``myBuffer``.
Furthermore, the type tags of the parameter and ports correspond to the data types of the arguments: 
``a?String`` corresponds to ``Input<String> a``, ``b!String`` corresponds to ``Output<String> b``, and ``init:String`` corresponds to ``String init``.

Ensure that the current directory ``.`` and the Reo runtime ``reo-runtime-java-1.0.jar`` are added to the Java classpath.
Then, we can compile the producer via::

	> ls
	MyClass.java buffer.treo
	> reo buffer.treo -p "Hello world!"
	> javac buffer.java
	> java buffer

The ``p`` option allows us to speficy the initial string in the buffer. Using commas in a string splits the string into multiple arguments.
The last command runs the generated application and brings up two *port windows* that allow us to put data into the buffer and take it out of the buffer.

The current version of Reo can generate only Java code, and therefore, only Java components can be defined. 
It is only a matter of time until Reo can generate code for other languages, such as C/C++, and that components defined in these languages can be defined.

Arguments of the Java function are automatically linked to the ports in the interface of its respective atomic component.
The exclamation mark (``!``) indicates that the Reo component ``producer`` uses the node  ``a`` as its output port.
A question mark (``?``) after a node ``a`` in an interface indicates that its component uses ``a`` as an input node.
A colon (``:``) after a node ``a`` indicates that its component uses ``a`` both as input and as output 
(this is not allowed for non-atomic components).

2. Definition of formal semantics
---------------------------------

Second, we can define a component by defining its behavior in a formal semantics, such as *constraint automaton with memory*:

.. code-block:: text
   
	// buffer.treo
	buffer(a?String,b!String) {
	   q0 -> q1 : {a}, x' == a 
	   q1 -> q0 : {b}, b == x
	}

This constraint automaton has two states, q0 and q1, and one memory cell, x. 
In q0, the buffer can perform an I/O operation on port a, indicated by the synchronization constraint {a}, and assign the observed value at a to the next value of memory cell x.
In q1, the buffer can perform an I/O operation on port b, indicated by the synchronization constraint {b}, and assign the current value in memory cell x to port b.

We can define a component as a Java component and a constraint automaton with memory simultaniously:

.. code-block:: text
   
	// buffer.treo
	buffer(a?,b!) {
	   Java: "MyClass.myBuffer"
	   q0 -> q1 : {a}, x' == a 
	   q1 -> q0 : {b}, b == x  
	}

In this case, the Reo compiler treats the Java code as the definition of the component, while the constraint automaton with memory is used only as annotation.
Although the current version of Reo simply ignores this annotation, future versions of can use the constraint automaton for tools like deadlock detection.

The syntax for constraint automata is completely independent of the syntax of the rest of the language.
This seperation makes is very easy to extend the current language with other types of formal semantics of components.

3. Definition as composition
----------------------------

The most expressive way to define a component in Reo is via composition.

.. code-block:: text
   
	// buffer2.treo
	buffer2(a?,b!) {
	   buffer(a,x)
	   buffer(x,b)
	}

	buffer(a?String,b!String) {
	   q0 -> q1 : {a}, x' == a 
	   q1 -> q0 : {b}, b == x
	}

This Reo program defines an atomic buffer component and a composite buffer2 component.
Since Reo is declarative, the order of the definitions of buffer and buffer2 is not important.

In the composite buffer2 component, we created implicitly a new Reo node x.
This new node is local to the definition of buffer2, as it is not exposed in the interface.

This node is shared between two instances of the atomic buffer component, with a and b substituted by respectively a and x in the first instance, and by respectively x and b in the second instance.
As seen from the signature of the atomic buffer component, instance buffer(a,x) writes to x, while instance buffer(x,b) reads from x.
The two buffer instances communicate via shared node x using the **broadcast** mechanism:
a *put/send operation* by a **single** component that uses node x as an *output node* 
synchronizes with a *get/receive operation* by **all** components that use node x as an *input node*.  

.. note:: 
	This broadcast communication mechanism should not be confused with broadcast communication
	as used by other models of concurrency. Usually a single send operation on a node A (also 
	called a *channel* in the literature) synchronizes with multiple, but **arbitrary** number, 
	receive operations on A.

Predicates
~~~~~~~~~~

The definition of buffer2 as a composition of two atomic buffer instances is explicit in the sense that every subcomponent instance is defined directly.
In this case, may can obtain the same construction using only one explicit instantiation using a **predicate**

.. code-block:: text
	
	{ buffer(a[i],a[i+1]) | i : <0..1> }

This for loop unfolds to the composition

.. code-block:: text
	
	{ fifo1(a[0],a[1]) fifo1(a[1],a[2]) }

Although predicates are already expressive enough, we add some syntactic sugar for if-then-else and for loops.
For example,

.. code-block:: text
	
	for i : <1..n> { buffer(a[i],a[i+1]) }

is equivalent to 

.. code-block:: text
	
	{ buffer(a[i],a[i+1]) | i : <1..n> }

and 

.. code-block:: text
	
	if (x=1) { buffer(a,b) } 
	else (x=2) { buffer(a,c) } 
	else { buffer(a,d) } 

is equivalent to 

.. code-block:: text
	
	{ buffer(a,b) | x=1 }
	{ buffer(a,c) | x!=1, x=2 }
	{ buffer(a,d) | x!=1, x!=2 } 

Terms
~~~~~

Besides the ordinary terms in predicates, such as 0, 1, n and <1..n>, we can also have component definitions as terms.
For example,

.. code-block:: text

	section slides.main;
	
	import reo.fifo1;
	import reo.sync;
	import reo.lossy;
	import slides.variable.variable;
	import slides.lossyfifo.lossyfifo1;
	import slides.shiftlossyfifo.shiftlossyfifo;
	
	import slides.main.red;
	import slides.main.blue;
	import slides.sequencer.seqc;
	
	main11() 
	{
	   { red(a[i]) | i : <1..n> }
	   blue(b) 
	   connector11<ileg[1..n], sync>(a[1..n], b)
	|
	   ileg[1..n] = <sync, lossy, fifo1, variable, shiftlossyfifo, lossyfifo1>
	}
	
	connector11<ileg[1..n](?, !), oleg(?, !)>(a[1..n], b) 
	{
	   seqc(x[1..n]) 
	   { ileg[i](a[i], x[i]) sync(x[i], m) | i : <1..n> }
	   oleg(m, b)
	}



Sections and Imports
--------------------

In large application, it is likely that different component would get the same name. 
To be able to distinguish between the two components, we put the components in different sections.
For example, we can put the ``buffer`` component defined above in a section called MySection by adding the statement ``section mySection;`` to the beginning of the file.

.. code-block:: text
   
	// buffer.treo 
	section mySection;

	buffer(a?,b!) {
	   Java: "MyClass.myBuffer"
	   q0 -> q1 : {a}, x' == a 
	   q1 -> q0 : {b}, b == x  
	}

In other files, we can reuse this buffer by simply importing it as follows:

.. code-block:: text
   
	// other.treo
	import mySection.buffer;
 
	other() {
		buffer(a,b)            // #1
		mySection.buffer(a,b)  // #2
	}

Option 1 is the simplest way to use an imported component, as it does not explicitly defines from which section it comes.
However, if we imported two buffer components from different sections, then Option 2 allows us to be precise on which buffer we mean.
