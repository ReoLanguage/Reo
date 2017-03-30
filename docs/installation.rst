.. _installation:

Installation
============

Unix
----

1. Install Java SDK 1.6 or higher. You can check if the correct java version is already installed via::

	java --version

2. Download and unpack `reo-1.0.zip <https://raw.githubusercontent.com/kasperdokter/Reo/master/reo-1.0.zip>`_ via::

	cd /home/<username>/Reo
	wget https://raw.githubusercontent.com/kasperdokter/Reo/master/reo-1.0.zip
	unzip reo-1.0.zip

3. Add Reo to the class path and create an alias for the compiler::

	export CLASSPATH=".:/home/<username>/Reo/reo-runtime-1.0.jar:$CLASSPATH"
	alias reo='java -jar /home/<username>/Reo/reo-1.0.jar'

.. tip:: 
	Add the export and alias commands to your startup script of your terminal (e.g., ``~/.bashrc``).
	This way, you don't need to set the class path and the alias for every new terminal window.

3. Test the installation by running::

	reo


Windows
-------

1. Install Install Java SDK 1.6 or higher.

2. Download the `reo-1.0.zip <https://raw.githubusercontent.com/kasperdokter/Reo/master/reo-1.0.zip>`_, and save it in, for example, ``C:\Program Files\Reo``

3. Set the ``CLASSPATH`` environment variable to ``.;C:\Program Files\Reo\reo-runtime-1.0.jar;%CLASSPATH%`` either permenantly via::

	Control Panel » System » Advanced » Environment Variables

or only for this terminal session via::

	set CLASSPATH=".;C:\Program Files\Reo\reo-runtime-1.0.jar;%CLASSPATH%"

4. Create short convenient commands for the Reo compiler, using batch files or doskey commands:
	- Batch files (in directory in system PATH)::

		//reo.bat
		java -jar C:\Program Files\Reo\reo-1.0.jar %*

	- Or, use doskey commands::

		doskey reo=java -jar C:\Program Files\Reo\reo-1.0.jar $*

5. Test the installation by running::

	reo

