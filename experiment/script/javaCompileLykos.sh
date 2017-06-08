#!/bin/sh
# sh javaCompileLykos.sh 4 4 alternator WorkersLykos.java exec_alt_lykos.txt

# $1 : name of the k-family of connector
# $2 : value of k
# $3 : name of the worker class
# $4 : file to write time execution

T=$(($1/$2))

for i in $(seq "$T"); do

	k=$((($2)*$(($i))))
	rm -f ../$3/$3$k/lykos/*.class
	cp ../worker/$4 ../$3/$3$k/lykos/WorkersLykos_tmp.java
	awk -v var="$k;" '/static int k=/ { print "static int k = "var; next}1' ../$3/$3$k/lykos/WorkersLykos_tmp.java > ../$3/$3$k/lykos/WorkersLykos_tmp2.java
	awk -v var="\"../$3/$3$k/lykos/$5\"" '/"result_execution.txt"/ { print var; next}1' ../$3/$3$k/lykos/WorkersLykos_tmp2.java > ../$3/$3$k/lykos/WorkersLykos.java
	rm ../$3/$3$k/lykos/WorkersLykos_tmp.java
	rm ../$3/$3$k/lykos/WorkersLykos_tmp2.java
	javac -cp "/home/e-spin/workspace/Reo/bin/reo-runtime-lykos.jar" ../$3/$3$k/lykos/*.java
	java -cp "../$3/$3$k/lykos:/home/e-spin/workspace/Reo/bin/reo-runtime-lykos.jar" Main

done


