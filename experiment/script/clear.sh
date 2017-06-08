#!/bin/bash

# $1 is the max value of k for experiment; $2 is the iteration value.

T=$(($1/$2))

#seq
for i in $(seq "$T"); do
  k=$((($2)*$(($i))))
  
  rm -r $3/$3$k
done
