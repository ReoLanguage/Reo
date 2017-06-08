
#Compile $connector from $step to $max by step of $step using pr semantic. Save the time of compilation in $path/file.txt
#PR SEMANTIC :
# sh compileToLykos.sh $max $k $connector $path/file.txt
#------
#- example: 
#------
# sh compileToLykos.sh 64 4 alternator ../alternator/comp.txt
#
#RBA SEMANTIC :
# sh compile.sh $max $k $connector $path/file.txt


#Compile from Lykos generated code to Java (specify the absolute path to the runtime jar and the reo jar inside javaCompileLykos.sh or javaCompile.sh script). Specify your workers that you want to compile with from folder 
#PR SEMANTIC :
# sh javaCompileLykos.sh $max $step $connector $workers $path/file.txt
------
- example: 
------
# sh javaCompileLykos.sh 64 4 alternator Workers.java ../alternator/exec_lykos.txt
#
#RBA SEMANTIC :
# sh javaCompileLykos.sh 64 4 alternator Workers.java ../alternator/exec_lykos.txt

#Print your result by merging all file of the same name from different folder, and put in a new file with one column for k (from $step to $max) and one for specific $value
# sh print_data.sh $max $k $connector $path/file_to_concatenate.txt $path/file_output.txt

