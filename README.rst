
Lykos Reo compiler
==================

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

Getting started
---------------

1. Install Java (version 1.6 or higher). You can check if the correct java version is already installed via ``java --version``

2. Download the ``lykos-1.0.jar`` and put it, for example, in ``/usr/local/lib``.

3. Create an alias for the compiler via::

	alias reoc='java -jar /usr/local/lib/lykos-1.0.jar'

.. tip:: add the alias command to your startup script of your terminal (e.g., ``~/.bash_profile``).

4. Test the installation by starting a new terminal and running::

Compile your first protocol
---------------------------
...