#!/bin/bash

# sh compileToLykos.sh 64 4 alternator ../alternator/comp_lykos.txt

# $1 : max value of k
# $2 : step of k
# $3 : family of connector
# $4 : file to write time of compilation

T=$(($1/$2))

#seq
for i in $(seq "$T"); do
  k=$((($2)*$(($i))))
  mkdir -p ../$3/$3$k/lykos
  clear ../$3/$3$k/lykos
  awk -v var="$k" '/N=35/ { print "\t N="var; next}1' ../$3/$3_lykos.treo > ../$3/$3$k/lykos/$3_lykos.treo
  java -jar /home/e-spin/workspace/Reo/bin/reo-1.0.jar ../$3/$3$k/lykos/$3_lykos.treo -pt -o ../$3/$3$k/lykos/
done

for i in $(seq "$T"); do
  k=$((($2)*$(($i))))
  awk -v var="$k" '{ print var"\t"$4 }' ../$3/$3$k/lykos/compilation_time_lykos.txt >> $4

done
#java -jar /home/e-spin/workspace/Reo/bin/reo-1.0.jar alternator_lykos.treo -pt -o ./  
#javac -cp "/home/e-spin/workspace/Reo/bin/reo-runtime-lykos.jar" *.java
#javac -cp "/home/e-spin/workspace/Reo/bin/reo-runtime-lykos.jar" Workers.java
