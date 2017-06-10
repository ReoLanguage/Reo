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


=================
Unix-like Systems and Mac :
=================

You need to set some environment variable before being able to use Reo. 
To configure reo for your current shell, execute the following commands:

        >$ export CLASSPATH=\"\$CLASSPATH:.:$BASEDIR/bin/reo-runtime-java.jar:$BASEDIR/bin/reo-runtime-java-lykos.jar\"

        >$ alias reo=\"java -jar $BASEDIR/bin/reo-1.0.jar\"

If you want to make Reo setting automatic for each Shell sessions, happen in your ~/.bashrc the following command :

        >$ source $BASEDIR/initreo.sh

Close and open a new terminal to make it effective or run

        >$ source ~/.bashrc
" >> $BASEDIR/README

