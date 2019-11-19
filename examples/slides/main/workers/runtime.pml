typedef port {
chan data = [1] of {bit};
chan trig = [1] of {bit}; 
}

inline take(a,b){
	a.trig!0 ; a.data?b
} 

inline put(a,b){
	a.data!b ; a.trig?_
} 


proctype prod(port a){
	do
		:: atomic{put(a,1)}
	od
}

proctype consFinite(port a){
	bit y;
	int i = 5;
	do
		:: i>0; atomic{take(a,y)}; i = i-1
//		:: break
	od
}

proctype cons(port a){
	bit y;
	do
		:: atomic{take(a,y)}
	od
}
