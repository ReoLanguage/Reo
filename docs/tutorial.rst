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

There are four ways to define a component in Reo

First of all, we can define a component by referring to Java source code.

.. code-block:: text
   
	// producer.treo
	producer(a!String) { 
	   Java: "com.example.MyClass.myProducer";
	}


This code in ``producer.treo`` defines an atomic component called ``producer`` with a single output port ``a`` of type ``String``.
The ``Java`` tag indicates that the implementation of this component consists of a piece of Java code. 
The provided reference links to a Java class that implements the producer component as a Java method ``myProducer`` in class ``com.example.MyClass``. 

.. code-block:: java

	// MyClass.java
	package com.example;

	import nl.cwi.reo.OutputPort;

	public class MyClass {
		public static void myProducer(OutputPort<String> a) {
			a.put("Hello World!");
			return;
		}
	} 

We can compile the producer via::

	> ls
	myClass.java producer.treo
	> reo producer.treo
	> javac producer.java
	> java producer

This brings up a *port window* that allows you to take data items produced by the producer.

The current version of Reo can generate only Java code, and therefore, only Java components can be defined. 
It is only a matter of time until Reo can generate code for other languages, such as C/C++, and that components defined in these languages can be defined.

Arguments of the Java function are automatically linked to the ports in the interface of its respective atomic component.
The exclamation mark (``!``) indicates that the Reo component ``producer`` uses the node  ``a`` as its output port.
A question mark (``?``) after a node ``a`` in an interface indicates that its component uses ``a`` as an input node.
A colon (``:``) after a node ``a`` indicates that its component uses ``a`` both as input and as output 
(this is not allowed for non-atomic components).

Second, we can define a component by defining its behavior in a certain semantics.
Currently, we can express the behavior only as a *constraint automaton with memory*

.. code-block:: text
   
	// buffer.treo
	buffer(a?,b!) {
	   q0 -> q1 : {a}, x' == d(a) 
	   q1 -> q0 : {b}, d(b) == x
	}

Third, we can define a component as a Java component and a constraint automaton with memory simultaniously:

.. code-block:: text
   
	// buffer.treo
	buffer(a?,b!) {
	   Java: "com.example.MyClass.myBuffer";
	   q0 -> q1 : {a}, x' == d(a) 
	   q1 -> q0 : {b}, d(b) == x  
	}

where the Java buffer is defined in the class

.. code-block:: Java

	// MyClass.java
	package com.example;

	import nl.cwi.reo.OutputPort;

	public class MyClass {
		public static void myBuffer(InputPort<String> a, OutputPort<String> a) {
			a.put("Hello World!");
			return;
		}
	} 

In this case, the Reo compiler treats the Java code as the definition of the component, while the constraint automaton with memory is used as annotation that approximates the behavior the the Java component. Although the current version of Reo simply ignores the constraint automaton representation of the buffer component, future versions of can use the constraint automaton for tools like deadlock detection.

Sections and Imports
--------------------

In large application, it is likely that different component would get the same name. 
To be able to distinguish between the two components, we put the components in different sections.
For example, we can put the ``buffer`` component defined above in a section called MySection by adding the statement ``section mySection;`` to  the beginning of the file.

.. code-block:: text
   
	// buffer.treo 
	section mySection;

	buffer(a?,b!) {
	   Java: "com.example.MyClass.myBuffer";
	   q0 -> q1 : {a}, x' == d(a) 
	   q1 -> q0 : {b}, d(b) == x  
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

Composition
-----------


Now that we defined the buffer, we may start using it by *instantiating* our buffer.

.. code-block:: text

	// main.treo
	import buffer;
   	
	main() { }



This Reo program accomplishes the following tasks:

1. is imports the buffer component.
2. it defines a new main component.
3. it creates two new nodes x and y.
4. it creates an instance of the buffer component with a substituted by x and b substituted by y.

Reo has a standard library that defines components in a section called reo.

We may compose multiple component by placing them next to each other.
The composition is established by sharing nodes.
For example, the following code shows the composition of two ``fifo1``-channels.

.. code-block:: text
	
	{
	   fifo1(a,b) // first
	   fifo1(b,c) // second
	}

The first and second ``fifo1``-channel share the common node b.
Recall that the first ``fifo1``-channel uses node b as an output node and that
the second ``fifo1``-channel uses node b as in input channel.

The two ``fifo1``-channels communicate via shared node A using the **broadcast** mechanism, 
that is, a *put/send operation* by a **single** component that uses node A as an *output node* 
synchronizes with a *get/receive operation* by **all** components that use node A as an *input node*.  

.. note:: 
	This broadcast communication mechanism should not be confused with broadcast communication
	as used by other models of concurrency. Usually a single send operation on a node A (also 
	called a *channel* in the literature) synchronizes with multiple, but **arbitrary** number, 
	receive operations on A.

Iteration
---------

The composition of the two ``fifo1``-channel explicitly instantiates each ``fifo1``-channel individually.
In this case, may could obtain the same construction using only *one* explicit instantiation using a **predicates**

.. code-block:: text
	
	{ fifo1(a[i],a[i+1]) | i : <0..1> }

This for loop is equivalent to the composition

.. code-block:: text
	
	{ fifo1(a[0],a[1]) fifo1(a[1],a[2]) }

Abstraction
-----------

In the composition of the two ``fifo1``-channels, shared node b is still visible to the environment.
Hence, another component, say ``producer``, may synchronize with node b as follows

.. code-block:: text
	
	main() {
	   fifo1(a,b)
	   fifo1(b,c)
	   producer(b) // this component synchronizes on the 'internal' node b
	}

The data provided by the producer flows via the **second** ``fifo1``-channel from node b to node c, 
while leaving the **first** ``fifo1``-channel from node a to node b unused.
This may, or may not be the intended use of the composition of the two ``fifo1``-channels

To avoid some other component X from putting data on node b, we may hide node b from the environment
by wrapping the composition of the two ``fifo1``-channels in a new component ``fifo2`` and then instantiate 
this new component

.. code-block:: text

	fifo2(a,c) { 
	   fifo1(a,b) 
	   fifo1(b,c)
	}

	main() {
	   fifo2(a,c)
	   producer(b) // node b is different from node b used in the definition of fifo2
	}

Since we know for each component in the definition of ``fifo2`` whether a node is used as input, output or both,
there is no need to make this explicit in the interface.


Parametrization
---------------

Recall the for-loop construction that allowed us to minimize the number of explicit instantiations.
The lower and upper bounds for the iterated parameter consist of integer numbers.
In may be useful to allow variable iteration bounds

.. code-block:: text
	
	fifo<k>(a[0], a[1...k-1], a[k]) { fifo1(a[i],a[i+1]) |	i : <0..k-1 }

The variable used in the upper bound of the iteration is instantiated as a parameter in the surrounding 
component definition.

We may also use parameters in the following way

.. code-block:: text

	transformer<f>(a,b) {
	   #CASM
	   q -- {a,b}, d(b) == f(d(a)) -> q;
	}

Or, as follows

.. code-block:: text
	
	filter<R>(a,b) {
	   #CASM
	   q -- {a,b}, R(d_a) -> q;
	   q -- {a}, ~R(d_a) -> q;
	}
	
