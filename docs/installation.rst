Installation
============

UNIX
----

1. Install Java (version 1.6 or higher). You can check if the correct java version is already installed via::

	java --version

2. Download the `reo-1.0.jar <https://raw.githubusercontent.com/kasperdokter/Reo/master/bin/reo-1.0.jar>`_ via::

	cd /usr/local/lib
	wget https://raw.githubusercontent.com/kasperdokter/Reo/master/bin/reo-1.0.jar

3. Add Reo to the class path and create an alias for the compiler::

	export CLASSPATH=".:/usr/local/lib/reo.jar:$CLASSPATH"
	alias reo='java -jar /usr/local/lib/reo-1.0.jar'

.. tip:: 
	Add the export and alias commands to your startup script of your terminal (e.g., ``~/.bash_profile``).
	This way, you don't need to set the class path and the alias for every new terminal window.

3. Test the installation by running::

	reo


WINDOWS
-------

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

