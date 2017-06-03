#!/bin/bash

# $1 : max value of k
# $2 : step of k
# $3 : family of connector
# $4 : file to write time of compilation

T=$(($1/$2))

#seq
for i in $(seq "$T"); do
  k=$((($2)*$(($i))))
  mkdir -p ../$3/$3$k
  awk -v var="$k" '/N=35/ { print "\t N="var; next}1' ../$3/$3.treo > ../$3/$3$k/$3.treo
  java -jar /home/e-spin/workspace/Reo/bin/reo-1.0.jar ../$3/$3$k/$3.treo -s RBA -o ../$3/$3$k/  
done

for i in $(seq "$T"); do
  k=$((($2)*$(($i))))
  awk -v var="$k" '{ print var"\t"$4 }' ../$3/$3$k/compilation_time.txt >> ../$3/$3$k/$4

done

