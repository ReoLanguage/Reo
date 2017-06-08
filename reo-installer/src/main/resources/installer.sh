#!/bin/bash

  BASEDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"


if grep -q "alias reo="  ~/.bashrc
then 
   echo "Alias already exist";
else
  echo "alias reo='java -jar $BASEDIR/bin/reo-1.0.jar'" >> ~/.bashrc 
fi

if grep -q ".reosource"  ~/.bashrc
then
   echo "ClassPath already set";
else
  echo ". $BASEDIR/.reosource" >> ~/.bashrc
fi
