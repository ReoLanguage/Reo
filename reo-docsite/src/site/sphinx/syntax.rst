Syntax of Reo
=============

Components and interfaces
-------------------------

In Reo, programs consist of *components* that interact via shared *nodes*.
Each component has access to a given set of nodes, called an *interface*.
A component may synchronize with its environment via *puts* and *gets* on nodes in its interface.
An atomic-component uses each port either as input or as output, but not both.



Definition
----------

Before we can start using components, we need to define them. 
The following example shows how we can define a component by referring to Java source code

.. code-block:: text
	:linenos:

	define producer(a!) { 
	  #Java com.example.Producer
	}

The producer component is defined by the implementation of the Java method ``Producer`` in package ``com.example``.
The ``#Java`` tag indicates that the function is a Java function.
Arguments of the Java function are automatically linked to the nodes in the interface of the defined component.
The exclamation mark (``!``) indicates that component ``producer`` uses node a as output.
A question mark (``?``) after a node A in the interface would indicate the the component uses A as an input node.
A question mark followed by a exclamation mark (``?!``) after a node A would indicate that the component uses A
both as input and as output.

Components can also be defined by methods written other general purpose languages, such as C/C++

.. code-block:: text
	:linenos:
	
	define producer(a!) {
	  #C/C++ example::Producer
	}

Another possibility is to define a component via a particular semantic model, such as *constraint automaton with state memory*

.. code-block:: text
	:linenos:
	
	define fifo1(a?,b!) {
	  #CASM 
	  q0 -- {a}, x' == d(a) -> q1
	  q1 -- {b}, d(b) == x  -> q0
	}

By now, there are more than thirty semantics for Reo. Therefore, it is possible to provide a second definition of the 
*same* component, using a *different* semantics. For example, the *work automaton* semantics

.. code-block:: text
	:linenos:
	
	define fifo1(a?,b!) {
	  #WA 
	  q0 -- {a}, true -> q1
	  q1 -- {b}, true -> q0
	}

.. note:: 
	The ``fifo1`` component is one of the basic components in Reo. Its has the semantics of a
	1-place buffer that accepts input from node a and offers output at node b. Components with
	exactly two nodes are called **channels**.

Composition
-----------

Now that we defined the ``fifo1``-channel, we may start using it by *instantiating* our ``fifo1``-channel as follows

.. code-block:: text
	:linenos:
	
	fifo1(a,b)

This single statement accomplishes *two* tasks:

1. it instantiates the ``fifo1``-channel defined above;
2. it instantiates two new nodes a and b.

We may compose multiple component by placing them next to each other.
The composition is established by sharing nodes.
For example, the following code shows the composition of two ``fifo1``-channels.

.. code-block:: text
	:linenos:
	

	fifo1(a,b) // first
	fifo1(b,c) // second

The first and second ``fifo1``-channel share the common node b.
Recall that the first ``fifo1``-channel uses node b as an output node and that
the second ``fifo1``-channel uses node b as in input channel.

The two ``fifo1``-channels communicate via shared node A using the **broadcast** mechanism, 
that is, a *put/send operation* by a **single** component that uses node A as an *output node* 
synchronizes with a *get/receive operation* by **all** components that use node A as an *input node*.  

Note that the broadcast communication mechanism 

.. note:: 
	This broadcast communication mechanism should not be confused with broadcast communication
	as used by other models of concurrency. Usually a single send operation on a node A (also 
	called a *channel* in the literature) synchronizes with multiple, but **arbitrary** number, 
	receive operations on A.

Iteration
---------

The composition of the two ``fifo1``-channel explicitly instantiates each ``fifo1``-channel individually.
In this case, may could obtain the same construction using only *one* explicit instantiation using a **for loop**

.. code-block:: text
	:linenos:
	
	for i = 0 ... 1 {
	  fifo1(a[i],a[i+1])
	}

This for loop is equivalent to the composition

.. code-block:: text
	:linenos:
	
	fifo1(a[0],a[1])
	fifo1(a[1],a[2])

Abstraction
-----------

In the composition of the two ``fifo1``-channels, shared node b is still visible to the environment.
Hence, another component, say ``producer``, may synchronize with node b as follows

.. code-block:: text
	:linenos:
	
	fifo1(a,b)
	fifo1(b,c)
	producer(b) // this component synchronizes on the 'internal' node b

The data provided by the producer flows via the **second** ``fifo1``-channel from node b to node c, 
while leaving the **first** ``fifo1``-channel from node a to node b unused.
This may, or may not be the intended use of the composition of the two ``fifo1``-channels

To avoid some other component X from putting data on node b, we may hide node b from the environment
by wrapping the composition of the two ``fifo1``-channels in a new component ``fifo2`` and then instantiate 
this new component

.. code-block:: text
	:linenos:

	define fifo2(a,c) { 
	  fifo1(a,b) 
	  fifo1(b,c)
	}
	
	fifo2(a,c)
	producer(b) // node b is different from node b used in the definition of fifo2

Since we know for each component in the definition of ``fifo2`` whether a node is used as input, output or both,
there is no need to make this explicit in the interface.


Parametrization
---------------

Recall the for-loop construction that allowed us to minimize the number of explicit instantiations.
The lower and upper bounds for the iterated parameter consist of integer numbers.
In may be useful to allow variable iteration bounds

.. code-block:: text
	:linenos:
	
	define fifo<k>(a[0], a[1...k-1], a[k]) {
	  for i = 0 ... k-1  {
	    fifo1(a[i],a[i+1])
	  }
	}

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
	
