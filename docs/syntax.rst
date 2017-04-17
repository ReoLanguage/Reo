Syntax of Reo
=============

.. attention:: 
	This page is still under construction and the presented language constructs may not be up-to-date.
	Sorry for the inconvenience.

A Reo program consist of a set of **components**. Each component has access to a finite set of **nodes**, called its **interface**, through which the components interacts with other components.
A component may use a node as an input port by performing get requests on that node, or a component may use a node as an output port by performing put requests on that node.

Put and get operations synchronize via the following scheme: multiple put operations on a node happen in mutual exclusion, and a single put operation synchronizes with a get operation by *all* components that use the node as input port.
This scheme completely determines the behaviour of a node, and hence, this scheme defines how a set of components composes into a larger system. This larger system may again be viewed as a component, which leads to the notion of a **composition** of components. 

Once a node has been used as a shared node in a composition of components, it is possible to **hide** this node by not exposing it in the interface of the composition. Hiding ensures that no other component can connect to this node.

In view of component composition, we distinguish two kinds of components: atomic components and composite components.

Atomic components
-----------------

The behaviour of an atomic component can be defined in 3 different ways.

1. by providing a reference to a user-defined component; or
2. by providing a definition in a particular *semantics*, such as automata; or
3. both 1 and 2.

The first option is to define the behaviour via a reference to to a user-defined component.
Consider the following piece of Java code

.. code-block:: java
	
	package com.example;

	import nl.cwi.pr.runtime.api.OutputPort;

	public class MyClass {

		public static void myProducer(OutputPort a, OutputPort b) {
			while (true) {
				for (int i = 0; i < 30000000; ++i);
				a.putUninterruptibly(i);
				b.putUninterruptibly(i);
			}
		}
	}

The static function ``myProducer`` repeatedly offers an integer at its output port.
We can refer to this static Java function by defining the following component

.. code-block:: text
   
	// producer.treo
	producer = (a!, b!) { 
		#PR identity(;a,b) | Java: "com.example.MyClass.myProducer"
	}

This Reo file consists of a single equation ``producer = (a!, b!) { ... }``. The left hand side of this equation is a variable named producer, and the right hand side of the equation is a nameless component. The nameless component consists of two parts: an interface and an implementation.

The interface ``(a!, b!)`` that defines the set of nodes used by this component. The exclamation mark (`!`) indicates that Reo component uses node `a` as output. A question mark (`?`) after a node `a` in the interface would indicate that the component uses a only as an input port. A colon (`:`) after a node `a` would indicate that the component possibly uses a both as input and output.

Arguments of the Java function are automatically linked to the nodes in the interface of the defined component.
It is, therefore, essential that the ports `a!` and `b!` of this producer component occur in the same order as in the static Java function.

The implementation ``{ ... }`` that defines the behaviour of this component. In this case, the behaviour is defined by the implementation of the Java method ``myProducer`` in class ``com.example.MyClass``. The ``Java`` tag indicates that the reference links to a Java class. Components can also be defined by methods written other general purpose languages, such as C/C++.

The second option is to define the behaviour of a component via a particular semantic model.
The following component defines a 1-place buffer in terms of the *work automaton* semantics.

.. attention:: 
	The following code is pseudo code, because the Reo compiler does not yet support work automata, constraint automata and C/C++.

.. code-block:: text

	// fifo1.wa.treo
	fifo1 = (a?,b!) {
		#WA
		q0 : initial
		q0 -> q1 : {a}, true, {}
		q1 -> q0 : {b}, true, {}
	}

.. note:: 
	The ``fifo1`` component is one of the basic components in Reo. Its has the semantics of a
	1-place buffer that accepts input from node a and offers output at node b. Components with
	exactly two nodes are called **channels**.

The third option is to define the behaviour of a component by specifying both a semantic model as well as a reference to a user-defined component. 
The following component defines the behaviour of a ``fifo1`` buffer as a C function, whose implementation can be formalized as a *constraint automaton with state memory*

.. code-block:: text
   
	// fifo1.cam.treo 
	fifo1 = (a?,b!) {
		#CASM
		q0 -> q1 : {a}, x' == d(a) 
		q1 -> q0 : {b}, d(b) == x  
	|
		C/C++:"example::Buffer"
	}

In presence of the reference to C code, the constraint automaton definition is treated by the compiler as an approximation of the implementation of the C code. This approximation can be used for all kinds of optimizations, such as scheduling or model checking. Without this approximation, the compiler assumes that any behaviour is possible, such as two subsequent put requests at port a.


Composite components
--------------------

Although atomic components are necessary for having a starting point, most components are composed out of smaller components that already exist.
A composition consists of a **set** of **instances** of existing component definitions that interact via shared nodes, together with an **interface** consisting of a collection of exposed nodes.

The following example reuses the ``fifo1`` component to define a 2-place buffer

.. code-block:: text
	
   // main.treo
   import reo.fifo1;
   
   main = (a, c) {
	fifo1(a,b)
	fifo1(b,c)
   }

The first statement of this Reo program imports a ``fifo1``. This component is then used to construct a larger main component that connects the two ``fifo1`` components in sequence.
The first component, ``fifo1(a,b)``, is a instance of the imported definition of ``fifo1`` over nodes a and b.
Similarly, the second component, ``fifo1(b,c)``, is a instance of ``fifo1`` over nodes b and c.
Note that it is not necessary to explicitly define new nodes. To define a node, it suffices to use the node is an instance of a component.

The two ``fifo1`` components share a common node b. Via this node, the two instances interact with each other.
The two instances ``fifo1(a,b)`` and ``fifo1(b,c)`` together form a set of ``{ fifo1(a,b) fifo1(b,c) }`` instances.
Together with the interface ``(a, c)``, this set defines the nameless component ``(a, c) { fifo1(a,b) fifo1(b,c) }``.
The interface ``(a, c)`` tells us that only node a and c are exposed the the environment, while node b becomes internal.

The two ``fifo1``-channels communicate via shared node a using the **broadcast** mechanism, 
that is, a *put/send operation* by a **single** component that uses node a as an *output node* 
synchronizes with a *get/receive operation* by **all** components that use node a as an *input node*.  

.. note:: 
	This broadcast communication mechanism should not be confused with broadcast communication
	as used by other models of concurrency. Usually a single send operation on a node A (also 
	called a *channel* in the literature) synchronizes with multiple, but **arbitrary** number, 
	receive operations on A.

Predicates
----------

The composition of the two ``fifo1``-channel explicitly instantiates each ``fifo1``-channel individually.
For the construction of large compositions, it becomes soon a tedious and error prone job to explicitly instantiate each component.

To counter this problem, it is possible to construct a composition by means of iteration.
The following example shows how to compose two ``fifo1`` channels in sequence link above, but now using iteration.

.. code-block:: text
	
	main = (a[0], a[2]) { fifo1(a[i], a[i+1]) | i : <0..1> }

The mathematically inclined reader recognizes the use of `set-builder notation <https://en.wikipedia.org/wiki/Set-builder_notation>`_.
The first part of the set, the **body**, in this definition consists of a single instance ``fifo1(a[i], a[i+1])`` of component ``fifo1`` over the (parametrized) nodes ``a[i]`` and ``a[i+1]``, for some value of i.
This instance is followed by a vertical line `|` that may be pronounced as 'such that'.
The last part of this set consists of the **predicate** ``i : <0..1>`` that states that i is an *element of* the list that consists of all integers from 0 to 1.

The best way to think about these predicates is to view them as `first-order formulas <https://en.wikipedia.org/wiki/First-order_logic>`_, over a number of free variables. Every assignment of values to a variable that satisfies the formula leads to one instantiation of the body of the set.

Hence, it is possible to take conjunctions and disjunctions of predicates, such as::

	i : <0..100>, i % 2 = 0 

which defined all *even* number from 0 to 100.


Parameters
----------

.. code-block:: text
	
	fifo<k>(a[0], a[1...k-1], a[k]) { fifo1(a[i],a[i+1]) |	i : <0..k-1> }

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
	
