# gnuplot -p -e "file1='result_compilation_alternator.txt'; file1='result_compilation_alternator.txt'; title='compilation.png'" time.gnuplot

set terminal png size 1000,950
set output title

set autoscale fix
set  style fill transparent solid 0.1 noborder
set palette model RGB
set style line 1 lc rgb '#0060ad' lt 1 lw 2 pt 7 ps 1.5
set style line 2 lc rgb '#CC0000' lt 1 lw 2 pt 7 ps 1.5
set title "compilation" font ",1"
unset key

set xlabel "k"
set ylabel "secondes"

plot file1 using 1:(($2)/1000000000) with linespoints ls 1,\
     file2 using 1:(($2)/1000000000) with linespoints ls 2
