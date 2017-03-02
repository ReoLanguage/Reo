Getting Started
===============

Installation
------------

UNIX
~~~~

1. Install Java (version 1.6 or higher). You can check if the correct java version is already installed via ``java --version``

2. Download the `reo.jar <https://raw.githubusercontent.com/kasperdokter/Reo/master/bin/reo.jar>`_ and put it, for example, in ``~/reo/``.

3. Create an alias for the compiler via::

	alias reo='java -jar /usr/local/lib/reo-1.0.jar'

.. tip:: add the alias command to your startup script of your terminal (e.g., ``~/.bash_profile``).

4. Test the installation by starting a new terminal and running::

	reo

WINDOWS
~~~~~~~

1. Install Java (version 1.6 or higher)

2. Download the `reo.jar <https://raw.githubusercontent.com/kasperdokter/Reo/master/bin/reo.jar>`_. 
Save to your directory for 3rd party Java libraries, say ``C:\Javalib``

3. Create short convenient commands for the Reo compiler, using batch files or doskey commands:
 - Batch files (in directory in system PATH)::

	//reo.bat
	java -jar C:\Javalib\reo-1.0.jar %*

 - Or, use doskey commands::

	doskey reo=java -jar C:\Javalib\reo-1.0.jar $*

4. Test the installation by starting a new terminal and running::

   reo

