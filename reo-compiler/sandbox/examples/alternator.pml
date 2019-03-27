/* Generated from alternator.treo by Reo 1.0.*/

#include "runtime.pml"

proctype Protocol1(port p13; port p15; port p14; port p10){
	chan m2 = [1] of {int};  
	chan m3 = [1] of {int};  
	chan m1 = [1] of {int};  
	      
	int _m2;  
	int _m3;  
	int _m1;  
	int _p13 ;
	int _p15 ;
	int _p14 ;
	int _p10 ;
	do
			:: (full(m1) && full(p14.sync)) -> atomic{m1?_m1; put(p14,_m1);}
			:: (empty(m2) && full(m3)) -> atomic{m3?_m3;m2!_m3;}
			:: (empty(m2) && empty(m3) &&  full(p13.data) && empty(m1) &&  full(p15.data) &&  full(p10.data) && full(p14.sync)) -> atomic{take(p15,_p15); put(p14,_p15);take(p13,_p13);m1!_p13;take(p10,_p10);m2!_p10;}
			:: (full(m2) && empty(m1)) -> atomic{m2?_m2;m1!_m2;}
	od

}


init {
			port p13;
			port p15;
			port p14;
			port p10

	atomic{
			run prod1(p15);	
			run prod2(p13);	
			run prod3(p10);	
			run cons(p14);	
			run Protocol1(p13,p15,p14,p10)
		
	}
}