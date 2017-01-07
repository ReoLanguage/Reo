   :github_url:

Getting Started
===============

Installation
------------

UNIX
~~~~

1. Install Java (version 1.6 or higher). You can check if the correct java version is already installed via ``java --version``

2. Download the ``lykos-1.0.jar`` and put it, for example, in ``/usr/local/lib``.

3. Create an alias for the compiler via::

	alias reoc='java -jar /usr/local/lib/lykos-1.0.jar'

.. tip:: add the alias command to your startup script of your terminal (e.g., ``~/.bash_profile``).

4. Test the installation by starting a new terminal and running::

	reoc

WINDOWS
~~~~~~~

1. Install Java (version 1.6 or higher)

2. Download ``lykos-1.0-jar-with-dependencies.jar``. 
Save to your directory for 3rd party Java libraries, say ``C:\Javalib``

3. Add ``lykos-1.0-jar-with-dependencies.jar`` to CLASSPATH, either:

 - permanently: Using System Properties dialog > Environment variables > Create or append to CLASSPATH variable
 - temporarily, at command line::

	SET CLASSPATH=.;C:\Javalib\antlr-4.5-complete.jar;%CLASSPATH%

4. Create short convenient commands for the Reo compiler, using batch files or doskey commands:
 - Batch files (in directory in system PATH)::

	//reoc.bat
	java nl.cwi.reo.Compiler %*

 - Or, use doskey commands::

	doskey reoc=java nl.cwi.reo.Compiler $*

