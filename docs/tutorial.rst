Tutorial
========

Components and interfaces
-------------------------

A Reo program consists of a number of *components* each of which interacts with its environment via a set of **nodes** that form its **interface**. A **node** is a means of *communication by exchange of values* between a component and other components (which collectively, form its environment).  

The interface of a component is the only thing that it shares with its environment, and communication by exchange of values means components cannot exchange references or pointers: components do not share variables, data structures, or objects.  Moreover, the nodes that form the interface of a component comprise the only means of communication between the component and its environment: a component has no means of communication or concurrency control other than blocking I/O operations that it can perform on its own nodes. 

A component is either *atomic* or *composite*.  A **composite component** consists of an expression in the language of Reo that specifies a composition of other components.  See the section on *Composite components*, below.

An **atomic component** is one whose operational details are given elsewhere in some unknown language, e.g., a piece of hardware, or a computation expressed in some conventional programming language, such as Java.  An atomic component may use each node in its interface exclusively either to acquire input, or to dispense output, but not both: the nodes in the interface of an atomic component are *unidirectional*. For clarity, we refer to unidirectional nodes as **ports**.

An atomic component may synchronize with its environment via *put* operations on its own output ports and *get* operations on its own input ports.  
A put or get operation may specify an optional time-out. 
A put or get operation blocks until either it succeeds, or its specified time-out expires. 
A put or get operation with no time-out blocks for ever, or until it succeeds.

Although specification of the internal details of an atomic component (e.g., its executable code) may be given in a language other than Reo, its existence and some information about the *externally observable behavior* of an atomic component must be specified in Reo, if this component participates in a Reo application.  On *the outside*, Reo does not distinguish between atomic and composite components; therefore, interface specification of all components share the same Reo syntax.  Whereas the body of the specification of a composite component uses Reo to define a composition (see below), the body of the specification of an atomic component uses one of a variety of concrete *sub-languages* to provide some information about the behavior of that atomic component.  The syntax of each such sub-language is completely independent of the syntax of Reo or that of other sub-languages.This seperation makes is very easy to extend the compiler to support other sub-languages for atomic component specification.

In this document, we consider only two such sub-languages: the one for components programmed in Java, and a generic one for definition of externally observable behavior. 

1. Reference to Java source code
--------------------------------

We can define an atomic component by referring to its Java source code, as:

.. code-block:: text
   
	// buffer.treo
	buffer<init:String>(a?String, b!String) {
	   #JAVA "MyClass.myBuffer"
	}


This code in buffer.treo (a Reo source code file) defines an atomic component called buffer with an input port **a** of type String, an output port **b** of type String, and a parameter ``init`` of type String.
The Java-tag indicates that the body of this component definition uses the syntax of Java-component specification sub-language. This sub-language allows a programmer to provide a link to a piece of Java code that constitutes the implementation of this component. Because the target code is Java, the type tags, String, are optional. Unspecified type tags default to Object.  The parameter block ``<init:String>`` is also optional, as we will see further below.

The Java sub-language interprets the provided reference as a link to a Java class that implements the ``buffer`` component as the Java method ``myBuffer`` in class ``MyClass``. 

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
Then, we can compile the producer via:

	> ls
	MyClass.java buffer.treo
	> reo buffer.treo -p "Hello world!"
	> javac buffer.java
	> java buffer

The ``p`` option allows us to speficy an initializing string for the buffer. Using commas in a string splits the string into multiple arguments.
The last command runs the generated application and brings up two *port windows* that allow us to put data into the buffer and take it out of the buffer.

The current version of Reo can generate only Java code, and therefore, only Java components can be defined. 
It is only a matter of time before Reo can generate code for other languages, such as C/C++, and that components defined in these languages can be used in Reo applications as well.

Arguments of the Java function are automatically linked to the ports in the interface of its respective atomic component.  Recall that ports are unidirectional nodes.  The interface of a component specifies how that component uses each node in its interface using node-type tags.
The exclamation mark (``!``) after a node name designates that node as an output port of its component.  For instance, ``b!`` in the above example indicates that the Reo component ``buffer`` uses the node  ``b`` as an output port.
A question mark (``?``) after a node name designates that node as an input node of its component.  For instance, ``a?`` in the above example indicates that 11buffer`` uses ``a`` as an input port.
A colon (``:``) after a node name ``x`` indicates that its component uses ``x`` both as input and as output. 
Because the interface of an atomic component can contain only (unidirectional) ports, colon-tag is not allowed in the interface of atomic components.

Observe that the above definition of ``buffer`` tells Reo only about this atomic component's *points of contact* with its (Reo) environment: an instance of ``buffer`` requires a string parameter for its instantiation, and has an input port and an output port through which it exchanges strings with its environment.  The ``#Java`` sub-language in the body of this definition of ``buffer`` merely provides a link to its Java source code, ``MyClass.myBuffer``, which defines the internal workings of this component.  A Java programmer can read the source code of ``MyClass.myBuffer`` in ``MyClass.java`` and surmise that this component is indeed an asynchronous buffer with capacity to hold at most one string, which is initialized with the value of its parameter ``init`` on instantiation, whose behavior consists of an infinite loop, in each iteration of which the component attempts to ``put`` the content of its buffer through its output port ``b``, and then obtain a string as its new buffer content by a ``get`` on its input port ``a``.   However, the environment of this component, specifically Reo, knows nothing about how ``buffer`` works: any possible relationship that in fact exists among its initial value, the values that ``buffer`` produces and consumes through its ports, and the relative order or timing of these actions is simply *hidden* inside the Java code and remains unknown to Reo.  

2. Definition via externally observable behavior
---------------------------------

The relations among the values that a component produces and consumes and their relative order constitutes the **externally observable behavior** of that component.  Reo supports a number of sub-languages that a programmer can use to specify the externally observable behavior of an atomic component in the body of its definition.  The *Rule-Based Automata (RBA)* specification language is one such sub-language that we consider in this document.  Intuitively, RBA is a formal language for defining the transitions of constraint automata with memory in terms of sets of rules:

.. code-block:: text
   
	// buffer.treo
	buffer<init:String>(a?String,b!String) {
	   #RBA
	   $m=init;

	   b =*  , a!=* , $m = b, $m' = a
	   b!=* , a =* , $m = b, $m' = a
	}

The buffer atomic component defined here consists of a single memory cell m, whose initial value is given by init.
If the buffer is empty ($m = * = a), it can perform an I/O operation on port a and blocks port **b** (indicated by the constraint b=* and a!=* ) and assigns the observed value at **a** to $m' which designates the value of memory cell m in the next state of the component.
If the buffer is full ($m = b != * ), it can perform an I/O operation on port **b**, block port **a** (indicated by the constraint b!=* and a=* ), assign the current value in memory cell m to port **b**, and clear the value of m ($m' = * ).

We can define a component both as a Java component and in the RBA sub-language simultaniously:

.. code-block:: text
   
	// buffer.treo
	buffer(a?,b!) {
	   #JAVA "MyClass.myBuffer"
	   #RBA
	   $m=init;
	   b =*  , a!=* , $m = b, $m' = a
	   b!=* , a =* , $m = b, $m' = a
	}

In this case, the Reo compiler treats the Java code as the definition of the component, and regards its RBA definition is used only as annotation.
Although the current version of the Reo compiler simply ignores the RBA definition, future versions of the compuiler can use the constraint automaton defined by the RBA for analysis or monitoring tools to verify or detect properties like deadlock.

3. Composite components
----------------------------

A composite component consists of a set of components, specified as an intensionally or extensionally defined set of components that may communicate with one another via shred nodes.
Consider the following simple example:

.. code-block:: text
   
	// buffer2.treo
	buffer2(a?,b!) {
	   buffer<"*">(a,x)
	   buffer<"*">(x,b)
	}

	buffer<init:String>(a?String,b!String) {
	   #RBA
	   $m=init;
	   b =*  , a!=* , $m = b, $m' = a
	   b!=* , a =* , $m = b, $m' = a
	}

This Reo program defines an atomic component, ``buffer``, and a composite component, ``buffer2``.
Since Reo is declarative, the order of the definitions of ``buffer`` and ``buffer2`` is not important.

The body of the composite component ``buffer2`` consists of a set of component instances, in this case defined in extensional format (without the separating commas) within the pair of curly brackets.  The component instances that comprise this set are two instances of another component, ``buffer``, namely, ``buffer<"*">(a,x)`` and ``buffer<"*">(x,b)``. 
Observe that the body of ``buffer2`` implicitly defines a new Reo node, ``x``, which
is local to the definition of ``buffer2``, i.e., *hidden* from outside, as it is not exposed in the interface of ``buffer2``.

The node ``x`` is shared between the two instances of the atomic component ``buffer``, where the formal parameter ports ``a`` and ``b`` of ``buffer`` get substituted, respectively, by the actual parameters ``a`` and ``x`` (of ``buffer2``) in the first instance, and by ``x`` and ``b``, respectively, in the second instance.
The signature of ``buffer`` shows that the node ``x`` serves as an output port in instance ``buffer(a,x)``, while instance ``buffer(x,b)`` treats ``x`` as an input port.  This sharing of ``x`` implies that ``x`` must be a mixed node.

A mixed node provides a **broadcast** mechanism for communication of the component instances that communicate through it.
a **single** *put/send operation* by one of the components that use a mixed node as an *output port* 
synchronizes with a *get/receive operation* by **all** components that use that mixed node as an *input port*.  In the case of our simple example, here, every *put* operation by ``buffer(a,x)`` synchronizes with a *get* operation by ``buffer(x,b)``.

.. note:: 
	This broadcast communication mechanism should not be confused with broadcast communication
	as used by other models of concurrency. Usually a single send operation on a node A (also 
	called a *channel* in the literature) synchronizes with multiple, but **arbitrary** number, 
	receive operations on A.

Predicates
~~~~~~~~~~

The definition of ``buffer2`` as a composition of two atomic buffer instances explicitly lists every one of its constituent component instances as in *extensional* specification of a set.
More expressively, Reo also allows *intensional* set speficication for definition of composite components, using **predicates**.

The following example shows a an intesional set definition where the predicate ``i : <0..1>`` simply means that the variable ``i`` can assume integer values in the (inclusive) range of 0 and 1:

.. code-block:: text
	
	{ buffer(a[i],a[i+1]) | i : <0..1> }

Although the above definition is merely a declarative definition of a set, it sometimes helps to intuitively think of what it *means* in terms of a for-loop in an imperative programming language. This for loop *unfolds* to the composition expressed above into the set:

.. code-block:: text
	
	{ fifo1(a[0],a[1]) fifo1(a[1],a[2]) }

Additionally, predicates may contain variables. All variables used in predicates must be grounded during instantiation.

.. code-block:: text
	
	{ buffer(a,b) | x=1 }
	{ buffer(a,c) | x!=1, x=2 }
	{ buffer(a,d) | x!=1, x!=2 } 

Terms
~~~~~

Besides the ordinary terms in predicates, such as 0, 1, ``n`` and ``<1..n>``, we can also have component definitions as terms.
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
