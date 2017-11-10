[![Build Status]https://travis-ci.org/ReoLanguage/Reo.svg?branch=master)](https://travis-ci.org/ReoLanguage/Reo)

Reo
===

Protocols coordinate interaction amongst concurrently executing processes.
Most general purpose programming languages do not provide syntax for expressing these protocols explicitly.
The absence of such syntax requires programmers to implement their protocols by manually via locks and semaphores. 
Given such implicit implementation of the protocol, it is very hard, if not impossible, to reason about its correctness and efficiency of the protocol.

.. _Reo: http://reo.project.cwi.nl/reo/wiki

Reo_, an *exogenous coordination language* designed by prof. dr. F. Arbab at the Centre for Mathematics and Computer Science (​CWI) in Amsterdam, addresses this problem by providing syntax that allows explicit high-level construction of protocols.
It is much easier to develop correct protocols that are free of dead-locks, live-locks and data races.
The compiler of the coordination language is able to optimize the actual implementation of the protocol.

Installation
------------
1. Install `Java SDK 1.6+ <http://www.oracle.com/technetwork/java/javase/downloads/index.html>`_. You can check if the correct java version is installed by running ``java -version``.	

2. Download and run the `installer <https://github.com/kasperdokter/Reo/releases/download/v1.0.0/reo-installer-1.0.jar>`_.

3. Go to your installation directory and follow the step written in the README.

4. Test the installation by running ``reo``.

Documentation
-------------
The `Reo documentation <http://reo.readthedocs.io/en/latest/>`_ is available on `Read The Docs <https://readthedocs.org/>`_.
   
Contribute
----------
If you wish to contribute to the development of Reo, use the following instructions to obtain your own copy of the source code:

1. Install `Git <https://git-scm.com/downloads>`_.

2. Install `Java SDK 1.6+ <http://www.oracle.com/technetwork/java/javase/downloads/index.html>`_. You can check if the correct version is already installed via ``java -version``.

3. Install `Maven <https://maven.apache.org/download.cgi>`_.

4. Install `Eclipse <https://www.eclipse.org/downloads/>`_.

5. Change directory to eclipse workspace ``cd ../workspace``

6. Clone this repository via ``git clone https://github.com/kasperdokter/Reo.git``

7. Change directory ``cd Reo``

8. Build the project: ``mvn clean install``. 

9. Generate Eclipse configuration via ``mvn eclipse:eclipse``

10. Import project to Eclipse: ``File > Import...``, select ``General > Existing Projects into Workspace``, hit ``Next``, select root directory and point to the cloned repository, hit ``Finish``.

11. Start coding :)
