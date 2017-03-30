
Reo compiler
============

Protocols coordinate interaction amongst concurrently executing processes.
Most general purpose programming languages do not provide syntax for expressing these protocols explicitly.
The absence of such syntax requires programmers to implement their protocols by manually via locks and semaphores. 
Given such implicit implementation of the protocol, it is very hard, if not impossible, to reason about its correctness and efficiency of the protocol.

.. _Reo: http://reo.project.cwi.nl/reo/wiki

Reo_, an *exogenous coordination language* designed by prof. dr. F. Arbab at the Centre for Mathematics and Computer Science (​CWI) in Amsterdam, addresses this problem by providing syntax that allows explicit high-level construction of protocols.
It is much easier to develop correct protocols that are free of dead-locks, live-locks and data races.
The compiler of the coordination language is able to optimize the actual implementation of the protocol.

Documentation
-------------
The documentation is available on http://reo.readthedocs.io/en/latest/
   
Contribute
----------

1. Install Java SDk (version 1.6 or higher). You can check if the correct version is already installed via ``java -version``

2. Install Maven. You can check if the correct version is already installed via ``mvn -version``

3. Install Eclipse. Available at https://www.eclipse.org/downloads/

4. Change directory to eclipse workspace ``cd ../workspace``

5. Clone this repository via ``git clone https://github.com/kasperdokter/Reo.git``

6. Change directory ``cd Reo``

7. Build the project: ``mvn clean install``. 

8. Generate Eclipse configuration: ``mvn eclipse:eclipse``

9. Import project to Eclipse: ``File > Import...``, select ``General > Existing Projects into Workspace``, hit ``Next``, select root directory and point to the cloned repository, hit ``Finish``.

10. Start coding
