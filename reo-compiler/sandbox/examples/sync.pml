/* Generated from sync.rba.treo by Reo 1.0.*/

#include "runtime.pml"

proctype Protocol1(port p1; port p2){
	int _p1 ;
	int _p2 ;
	do
			:: ( full(p1.data) && full(p2.sync)) -> atomic{take(p1,_p1); put(p2,_p1);}
	od

}


init {
			port p1;
			port p2

	atomic{
			run prod(p1);	
			run cons(p2);	
			run Protocol1(p1,p2)
		
	}
}