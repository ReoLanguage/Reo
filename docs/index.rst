Welcome to Reo's documentation!
===============================

.. _Reo: http://reo.project.cwi.nl/reo/wiki

Reo_ is an *exogenous coordination language* designed by prof. dr. F. Arbab at the Centre for Mathematics and Computer Science (​CWI) in Amsterdam.
Reo allows the programmer to specify protocols that describe interaction among components in a concurrent application.

What are protocols, and why do we care?
---------------------------------------

Protocols, such as mutual exclusion protocol and producer/consumer, orchestrate interaction amongst concurrently executing processes.
Unfortunately, most general purpose programming languages do not provide syntax for expressing these protocols.
This absence requires programmers to implement their protocols by manually adding locks and buffers and ensuring their correct usage. 
Even if the implementation correctly resembles the intended protocol, the implicit encoding of the protocol makes it hard, if not impossible, to reason about its correctness and efficiency.

Reo addresses this problem by providing syntax that enables explicit high-level construction of protocols.
If the protocol is specified explicitly, it becomes easier to write correct protocols that are free of dead-locks, live-locks and data races.
Moreover, the compiler of the coordination language is able to optimize the actual implementation of the protocol.


Reo compiler
============

This documentation describes the usage of a Reo compiler that generates a multithreaded application from single-threaded components and a Reo protocol. 


Contents of this documentation
==============================

The contents of the documentation is as follows:

.. toctree::
	:maxdepth: 2

	installation
	introduction
	tutorial
	contribute

Indices and tables
==================

* :ref:`genindex`
* :ref:`modindex`
* :ref:`search`

