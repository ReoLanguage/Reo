#!/bin/sh

# sh print_data.sh 64 4 alternator lykos/exec_alt_lykox.txt exec_alt_lykos.txt

T=$(($1/$2))

for i in $(seq "$T"); do
  k=$((($2)*$(($i))))
  awk -v var="$k" '{ print var"\t"substr($8,2) }' ../$3/$3$k/$4 >> $5
done
