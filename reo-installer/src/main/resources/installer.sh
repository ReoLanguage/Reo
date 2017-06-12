#!/bin/bash
  BASEDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
echo "
#!/bin/bash

alias reo=\"java -jar $BASEDIR/bin/reo-1.0.jar\"

export CLASSPATH=\"$CLASSPATH:.:$BASEDIR/bin/reo-runtime-java.jar:$BASEDIR/bin/reo-runtime-java-lykos.jar\"
" >> $BASEDIR/initreo.sh

echo "
===================================
=                                 =
=   README for Reo installation   =
=                                 =
===================================


You need to set some environment variable before being able to use Reo. 
To configure reo for your current shell, execute the following commands:

        >$ export CLASSPATH=\"\$CLASSPATH:.:$BASEDIR/bin/reo-runtime-java.jar:$BASEDIR/bin/reo-runtime-java-lykos.jar\"

        >$ alias reo=\"java -jar $BASEDIR/bin/reo-1.0.jar\"

If you want to make Reo setting automatic for each Shell sessions, happen in your ~/.bashrc the following command :

        >$ source $BASEDIR/initreo.sh

Close and open a new terminal to make it effective or run

        >$ source ~/.bashrc

======
How to compile and run examples : 
======
Go to folder \"examples/slides/main\" and execute : 

        >$  reo FILE.treo -cp ../.. -o OUTPUT_DIR 

The option -cp indicate the classpath for reo dependencies. The option -o is the output directory.
Moove to the OUTPUT_DIR and compile the file with : 

        >$  javac OUTPUT_DIR/*.java 

Note that if you are using external java classes, you should first compile those classes before compiling the generated code. 
Run the application with : 

        >$  java FILE


" >> $BASEDIR/README

