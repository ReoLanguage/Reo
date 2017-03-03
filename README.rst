
Reo compiler
============

.. _Reo: http://reo.project.cwi.nl/reo/wiki

Reo_ is an *exogenous coordination language* designed by prof. dr. F. Arbab at the Centre for Mathematics and Computer Science (​CWI) in Amsterdam.
Reo allows the programmer to specify protocols that describe interaction among components in a concurrent application.

Documentation
-------------
The documentation is available on http://reo.readthedocs.io/en/latest/

What are protocols, and why do we care?
---------------------------------------

Protocols, such as mutual exclusion protocol and producer/consumer, orchestrate interaction amongst concurrently executing processes.
Unfortunately, most general purpose programming languages do not provide syntax for expressing these protocols.
This absence requires programmers to implement their protocols by manually adding locks and buffers and ensuring their correct usage. 
Even if the implementation correctly resembles the intended protocol, the implicit encoding of the protocol makes it hard, if not impossible, to reason about its correctness and efficiency.

Reo addresses this problem by providing syntax that enables explicit high-level construction of protocols.
If the protocol is specified explicitly, it becomes easier to write correct protocols that are free of dead-locks, live-locks and data races.
Moreover, the compiler of the coordination language is able to optimize the actual implementation of the protocol.

Getting started
---------------

Unix
~~~~

1. Install Java (version 1.6 or higher). You can check if the correct java version is already installed via::

	java --version

2. Download the `reo-1.0.jar <https://raw.githubusercontent.com/kasperdokter/Reo/master/bin/reo-1.0.jar>`_ via::

	cd /usr/local/lib
	sudo wget https://raw.githubusercontent.com/kasperdokter/Reo/master/bin/reo-1.0.jar

3. Add Reo to the class path and create an alias for the compiler::

	export CLASSPATH=".:/usr/local/lib/reo-1.0.jar:$CLASSPATH"
	alias reo='java -jar /usr/local/lib/reo-1.0.jar'

.. tip:: 
	Add the export and alias commands to your startup script of your terminal (e.g., ``~/.bash_profile``).
	This way, you don't need to set the class path and the alias for every new terminal window.

3. Test the installation by running::

  reo

Windows
~~~~~~~

1. Install Java (version 1.6 or higher)

2. Download the `reo-1.0.jar <https://raw.githubusercontent.com/kasperdokter/Reo/master/bin/reo-1.0.jar>`_. 
Save to your directory for 3rd party Java libraries, say ``C:\Javalib``

3. Create short convenient commands for the Reo compiler, using batch files or doskey commands:
 - Batch files (in directory in system PATH)::

	//reo.bat
	java -jar C:\Javalib\reo-1.0.jar %*

 - Or, use doskey commands::

  doskey reo=java -jar C:\Javalib\reo-1.0.jar $*

4. Test the installation by running::

  reo
   
Contribute
----------

1. Install Java (version 1.6 or higher). You can check if the correct version is already installed via ``java -version``

2. Install Maven. You can check if the correct version is already installed via ``mvn -version``

3. Install Eclipse. Available at https://www.eclipse.org/downloads/

4. Change directory to eclipse workspace ``cd ../workspace``

5. Clone this repository via ``git clone https://github.com/kasperdokter/Reo.git``

6. Change directory ``cd Reo``

7. Build the project: ``mvn install``. 

8. Run the compiler: ``java -jar reo-compiler/target/reoc.jar``

9. Generate Eclipse configuration: ``mvn eclipse:eclipse``

10. Import project to Eclipse: ``File > Import...``, select ``General > Existing Projects into Workspace``, hit ``Next``, select root directory and point to the cloned repository, hit ``Finish``.

11. Start coding
