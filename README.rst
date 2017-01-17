
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
