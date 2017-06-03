#!/bin/sh

cp ../worker/$3 ../$1/$1$2/lykos/WorkersLykos_tmp.java
awk -v var="\"../$1/$1$2/lykos/$4\"" '/"result_execution.txt"/ { print var; next}1' ../$1/$1$2/lykos/WorkersLykos_tmp.java > ../$1/$1$2/lykos/WorkersLykos.java
rm ../$1/$1$2/lykos/WorkersLykos_tmp.java
