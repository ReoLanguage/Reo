Contribute
==========

This page describes how to contribute to the Reo compiler.

Clone the repository
--------------------

Confirm with ``git --version`` that Git is installed on your system.
Clone the git repository, and change directory into the ``Reo`` folder::

	git clone https://kasperdokter@bitbucket.org/kasperdokter/Reo.git
	cd Reo
	
.. note:: 
	If you are not familiar with Git version control, please consult a 
	`tutorial on Git <https://try.github.io/>`_. 

Build the project
-----------------

This compiler project is build using Apache Maven, a software project management and comprehension tool. 

.. note:: 
	If you are not familiar with the Maven build tool, please consult a 
	`tutorial on Maven <https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html>`_.
	Maven automatically takes care of all the dependencies of this project (such as the dependency on 
	ANTLR4 for lexer and parser generation). This tool is therefore essential for smooth development 
	of this single project by multiple developers, each using his/her own operating system.

Confirm with ``mvn -v`` that Maven is installed on your system.
Otherwise (on Ubuntu), run command::

	sudo apt-get install maven

to install the latest Apache Maven.

To build the project, run::

	mvn install
	
This Maven command creates in every module (i.e., ``reo-compiler/``, etc.) a folder called ``target`` that contains a Java archive
with all compiled code of that particular module. Furthermore, it creates a self-contained Java archive ``reo-compiler/target/reoc.jar`` 
that contains the Reo compiler.

Documentation by Javadoc
------------------------

To allow people other than yourself to understand what your code is supposed to do, it is extremely important to document your code.
Since the Reo compiler is written in Java, we use `Javadoc <http://www.oracle.com/technetwork/articles/java/index-137868.html>`_ for documentation. 
Using Eclipse, you can easily generate a skeleton of the documentation using a plugin like `JAutodoc <http://jautodoc.sourceforge.net/>`_.
Once the documentation is in place, you can generate a static Javadoc website for the whole project by running::

	mvn javadoc:aggregate
	
Setting up Eclipse
------------------

If you use Eclipse as development environment, run::

	mvn eclipse:eclipse

to generate eclipse configuration files, such as ``.project`` and ``.classpath``.
Manually define the M2_REPO classpath variable in Eclipse IDE as follows:

1. Eclipse IDE, menu bar;
2. Select Window > Preferences;
3. Select Java > Build Path > Classpath Variables;
4. Click on the new button > define a new variable called ``M2_REPO`` and point it to your local Maven repository (i.e., ``~/.m2/repository`` in Linux).

Adding new semantics to the compiler
------------------------------------

There exist more than thirty different semantics for Reo [JA12]_. Although all these different kinds of semantics for Reo have 
a fully developed theory, not all of them are actually implemented in the Reo compiler. In view of the variety of Reo semantics, 
the Reo compiler has been build in a generic way that allows for easy extension to new Reo semantics.

This tutorial provides a step-by-step procedure that extends the current Reo compiler with a new type of semantics called ``MyReoSemantics``
(or ``MRS`` for short). 

1. Implement your semantics as java objects.

	a. In the ``reo-semantics`` module, add an alternative ``MRS`` to the enum ``nl.cwi.reo.semantics.SemanticsType``.
	Update the ``toString()`` method by adding a case 

	.. code-block:: java
   
		...
		case MRS: return "mrs";
		break;
		...

	.. note:: 
         
		The value of the ``toString()`` method is used by the Reo compiler when it searches component definitions in on the file system.
		

	b. In the ``reo-semantics`` module, create a new package called ``nl.cwi.reo.myreosemantics``, 
	and add a class called ``MyReoSemantics`` that implements ``nl.cwi.reo.semantics.Semantics`` as follows:

	.. code-block:: java
	
		package nl.cwi.reo.myreosemantics;
	
		import nl.cwi.reo.semantics.Semantics;

		public class MyReoSemantics implements Semantics<MyReoSemantics> { }

	If ``MyReoSemantics`` can be viewed as an extension of port automata with a particular type of labels on its transitions, then 
	we can reuse the generic automaton implementation and instantiate it using our own type of labels on the transitions.

		i. Implement the transition label by creating a class ``nl.cwi.reo.myreosemantics.MyReoSemanticsLabel`` that implements
		the ``nl.cwi.reo.automata.Label`` interface. This interface requires you to implement how composition and hiding affects 
		transition labels.
	
		ii. let the class ``MyReoSemantics`` extend the class ``nl.cwi.reo.automata.Automaton`` as follows:
	
		.. code-block:: java
	
			package nl.cwi.reo.myreosemantics;

			import nl.cwi.reo.automata.Automaton;
			import nl.cwi.reo.myreosemantics.MyReoSemanticsLabel;
			import nl.cwi.reo.semantics.Semantics;

			public class MyReoSemantics extends Automaton<MyReoSemanticsLabel> 
					implements Semantics<MyReoSemantics> { }

2. Design an ANTLR4 grammar for your semantics. For further details on ANTLR4, we refer to the manual [Parr13]_.

	a. In the folder ``reo-interpreter/src/main/antlr4/nl/cwi/reo/interpret/``, ceate a grammar file ``MRS.g4`` that contains a rule
	called ``mrs``:
	
	.. code-block:: text
	
		grammar MRS;

		import Tokens;

		mrs : //...// ;

	b. Add an alternative ``| mrs ;`` to the rule of ``atom`` in the main grammar ``Reo.g4`` of Reo.
	
3. Implement an ANTL4 listener that annotates the parse tree with our ``MyReoSemantics`` classes.

	a. In the ``reo-interpreter`` module, create a class ``nl.cwi.reo.interpret.listeners.ListenerMRS`` that extends 
	``nl.cwi.reo.interpret.listeners.Listener`` as follows:
	
	.. code-block:: java
	
		package nl.cwi.reo.interpret.listeners;

		import org.antlr.v4.runtime.tree.ParseTreeProperty;

		import nl.cwi.reo.interpret.listeners.Listener;
		import nl.cwi.reo.myreosemantics.MyReoSemantics;

		public class ListenerMRS extends Listener<MyReoSemantics> {

			private ParseTreeProperty<MyReoSemantics> myReoSemantics = 
					new ParseTreeProperty<MyReoSemantics>();

			public void exitAtom(AtomContext ctx) {
				atoms.put(ctx, automata.get(ctx.pa()));
			} 
		}
	
	b. In the root directory of this repository, run ``mvn clean install`` to let ANTLR4 generate a parser and a lexer for your new grammar.
		
	c. Go to the folder ``reo-interpreter/target/generated-sources/antr4/nl/cwi/reo/interpret`` that contains all classes generated by ANTLR4,
	and copy all (empty) methods from class ``MRSBaseListener`` to our listener class ``ListenerMRS``.
	Replace all occurrences of ``MRSParser.<rule>Context`` with ``<rule>Context`` and import ``ReoParser.<rule>Context``.
	For example:
	
	.. code-block:: java
	
		package nl.cwi.reo.interpret.listeners;
	
		import org.antlr.v4.runtime.tree.ParseTreeProperty;
	
		import nl.cwi.reo.interpret.listeners.Listener;
		import nl.cwi.reo.interpret.ReoParser.MrsContext;
		import nl.cwi.reo.myreosemantics.MyReoSemantics;
		
		public class ListenerMRS extends Listener<MyReoSemantics> {
		
			private ParseTreeProperty<MyReoSemantics> myReoSemantics = 
					new ParseTreeProperty<MyReoSemantics>();
		
			public void exitAtom(AtomContext ctx) {
				atoms.put(ctx, automata.get(ctx.pa()));
			} 

			public void enterMrs(MrsContext ctx) { }

			public void exitMrs(MrsContext ctx) { }
			
			/**
			 * All other rules go here.
			 */
		}
				
	d. Implement all other rules to eventually assign a ``MyReoSemantics`` object to the parse tree as follows:
	
	.. code-block:: java

		public void exitMrs(MrsContext ctx) { 
			//...
			myReoSemantics.put(ctx, new MyReoSemantics( ... ));
		}
		
4. Implement an interpreter for your semantics by creating a class ``nl.cwi.reo.interpret.InterpreterMRS`` with the following implementation:

.. code-block:: java

	package nl.cwi.reo.interpret;

	import java.util.List;

	import nl.cwi.reo.interpret.listeners.ListenerMRS;
	import nl.cwi.reo.myreosemantics.MyReoSemantics;
	import nl.cwi.reo.semantics.SemanticsType;

	public class InterpreterMRS extends Interpreter<MyReoSemantics> {
		/**
		 * Constructs a Reo interpreter for MyReoSemantics.
		 * @param dirs		list of directories of Reo components
		 * @param params	list of parameters passed to the main Reo component
		 */
		public InterpreterPA(List<String> dirs, List<String> params) {
			super(SemanticsType.MRS, new ListenerMRS(), dirs, params);	
		}	
	}
	
5. Edit the ``run()`` method of the compiler by using your new interpreter InterpreterMRS as follows:

.. code-block:: java

	public void run() {
		...
		Interpreter<MyReoSemantics> interpreter = new InterpreterMRS(directories, params);
		Assembly<MyReoSemantics> program = interpreter.interpret(files);
		...
	}


Future work
-----------

Since this is a young project, many features are yet to be implemented:

 - Animation
 - Autocompletion of code
 - Dynamic reconfiguration (by graph transformations)
 - Exports (to BIP, ect.)
 - Graphical editor
 - Imports (from BPEL, UML sequence sequence diagrams)
 - Model checking (via Vereofy and MCRL2)
 - Simulation (of stochastic Reo connectors)
 - Syntax highlighting
 
 
References
----------

.. [Parr13] Terence Parr. 2013. 
   The Definitive ANTLR 4 Reference (2nd ed.). Pragmatic Bookshelf. 

.. [JA12] Sung-Shik T. Q. Jongmans, Farhad Arbab: 
   Overview of Thirty Semantic Formalisms for Reo. Sci. Ann. Comp. Sci. 22(1): 201-251 (2012)
