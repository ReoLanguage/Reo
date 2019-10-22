Welcome to Reo's documentation
===============================

.. _Reo: http://reo.project.cwi.nl/reo/wiki

Reo_ is an *exogenous coordination language* designed by prof. dr. F. Arbab in the late 1990's at the Centre for Mathematics and Computer Science (​CWI) in Amsterdam.  What distinguishes Reo from other contemporary formal models and languages for concurrent systems is that at its core, Reo offers an interaction-centric model of concurrency.  In contrast to process- or action-centric models of concurrency, Reo allows a programmer to directly specify protocols that describe interaction among components in a concurrent application as explicit, concrete pieces of composable software.

What are protocols, and why do we care?
---------------------------------------

A protocol specifies proper sequences of steps that a set of concurrently executing processes must follow to orchestrate their interactions in order for the system to manifest a desirable behavior, such as mutually exclusive access to a shared resource (mutual exclusion) or exchanging information according to a producer/consumer pattern.
Unfortunately, most general purpose programming languages do not provide syntax for expressing these protocols as concrete, modular pieces of software.
This shortcoming forces programmers to implement their protocols indirectly, by manually adding locks and buffers and ensuring their correct usage. 
Even if the implementation correctly manifests the intended protocol, the implicit encoding of the protocol makes it hard, if not impossible, to reason about its correctness and efficiency, scale it to engage more processes, or reuse it in some other application.

Reo addresses this problem by providing syntax that enables explicit high-level construction of protocols.
When a protocol is specified explicitly, it becomes easier to write correct protocols that are free of dead-locks, live-locks, or data races.
Moreover, a compiler is then able to optimize the actual implementation of the protocol.


Reo compiler
============

This document describes the usage of a Reo compiler that generates a multithreaded application from a set of single-threaded components and a Reo protocol specification. 


Contents of this documentation
==============================

The contents of the documentation is as follows:

.. toctree::
	:maxdepth: 2

	installation
	introduction
	tutorial
        library
	contribute


Indices and tables
==================

* :ref:`genindex`
* :ref:`modindex`
* :ref:`search`

