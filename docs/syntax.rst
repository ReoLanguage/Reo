Syntax of Reo
=============


.. attention:: 
	This page is still under construction and the presented language constructs may not be up-to-date.
	Sorry for the inconvenience.

Components and interfaces
-------------------------

Reo programs consist of *components* that interact via shared *nodes*.
Each component has access to a given set of nodes, called its *interface*.
To distinguish them from other nodes, the nodes that comprise the interface of a component are also called its *ports*.
An atomic component uses each of its ports either for input or for output, but not both.
Ports of a component comprise the only means of communication between the component and its environment: a component has 
no means of communication or concurrency control other than blocking I/O operations that it can perform on its own ports. 
A component may synchronize with its environment via *put* operations on its own output ports and *get* operations on 
its own input ports.  The put and get operations may specify an optional time-out.  A put or get opreration blocks until 
either it succeeds, or its specified time-out expires.  A put or get operation with no time-out blocks for ever, 
or until it succeeds.

Definition
----------

Before we can start using components, we need to define them. 
The following example shows how we can define a component by referring to Java source code

.. code-block:: text
   
	// producer.treo
	producer(a!) { 
		#PR identity(;a) | Java:"com.example.MyClass.myProducer"
	}

This piece of code defines an atomic component called ``producer`` with a single output port ``a``.
The ``Java`` tag indicates that the implementation of this component consists of a piece of Java code. More specifically, 
the provided reference links to a Java class: the implementation of the producer component consists of the Java
method ``myProducer`` in class ``com.example.MyClass``. 
Components can also be defined by methods written in other general purpose languages, such as C/C++

Arguments of the Java function are automatically linked to the ports in the interface of its respective atomic component.
The exclamation mark (``!``) indicates that the Reo component ``producer`` uses the node  ``a`` as its output port.
A question mark (``?``) after a node ``a`` in an interface indicates that its component uses ``a`` as an input node.
A colon (``:``) after a node ``a`` indicates that its component uses ``a`` both as input and as output 
(this is not allowed for non-atomic components).

Another possibility is to define a component via a particular semantic model, such as *constraint automaton with state memory*

.. code-block:: text
   
	// buffer.treo 
	buffer(a?,b!) {
		#CASM
			q0 -> q1 : {a}, x' == d(a) 
			q1 -> q0 : {b}, d(b) == x  
	|
		C/C++:"example::Buffer"
	}

The above code is pseudo code, because the Reo compiler does not yet support C/C++.

There are more than thirty semantics for Reo. Therefore, one may provide a second definition of the 
*same* component, using a *different* semantics. For example, the *work automaton* semantics

.. code-block:: text

   // fifo1.wa.treo
	define fifo1(a?,b!) {
      #WA
				q0 : initial
				q0 -> q1 : {a}, true, {}
				q1 -> q0 : {b}, true, {}
	}

.. note:: 
	The ``fifo1`` component is one of the basic components in Reo. Its has the semantics of a
	1-place buffer that accepts input from node a and offers output at node b. Components with
	exactly two nodes are called **channels**.

Composition
-----------

Now that we defined the ``fifo1``-channel, we may start using it by *instantiating* our ``fifo1``-channel as follows

.. code-block:: text
	
   // main.treo
   import reo.fifo1;
   
   main() {
	  fifo1(x,y)
   }

This Reo program accomplishes the following tasks:

1. is imports the Reo component called ``fifo1``.
2. it defines a new Reo component called ``main``.
3. it creates two new nodes `x` and `y`.
4. it creates an instance of the Reo component ``fifo1`` with `a` substituted by `x` and `b` substituted by `y`.

We may compose multiple component by placing them next to each other.
The composition is established by sharing nodes.
For example, the following code shows the composition of two ``fifo1``-channels.

.. code-block:: text
	:linenos:
	
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
	:linenos:
	
	{ fifo1(a[i],a[i+1]) | i : <0..1> }

This for loop is equivalent to the composition

.. code-block:: text
	:linenos:
	
	{ fifo1(a[0],a[1]) fifo1(a[1],a[2]) }

Abstraction
-----------

In the composition of the two ``fifo1``-channels, shared node b is still visible to the environment.
Hence, another component, say ``producer``, may synchronize with node b as follows

.. code-block:: text
	:linenos:
	
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
	:linenos:

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
	:linenos:
	
	fifo<k>(a[0], a[1...k-1], a[k]) { fifo1(a[i],a[i+1]) |	i : <0..k-1 }

The variable used in the upper bound of the iteration is instantiated as a parameter in the surrounding 
component definition.

We may also use parameters in the following way

.. code-block:: text
	:linenos:

	transformer<f>(a,b) {
	  #CASM
	  	q -- {a,b}, d(b) == f(d(a)) -> q;
	}

Or, as follows

.. code-block:: text
	:linenos:
	
	filter<R>(a,b) {
	  #CASM
	  q -- {a,b}, R(d_a) -> q;
	  q -- {a}, ~R(d_a) -> q;
	}
	
