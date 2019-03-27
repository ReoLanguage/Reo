/* Generated from alternator3.treo by Reo 1.0.*/

#include "runtime.pml"

proctype Protocol1(port p5; port p6; port p7; port p8){
	chan m2 = [1] of {int};  
	chan m3 = [1] of {int};  
	chan m1 = [1] of {int};  
	      
	int _m2;  
	int _m3;  
	int _m1;  
	int _p5 ;
	int _p6 ;
	int _p7 ;
	int _p8 ;
	do
			:: (empty(m2) && empty(m3) &&  full(p5.data) && empty(m1) &&  full(p7.data) &&  full(p8.data) && full(p6.sync)) -> atomic{take(p7,_p7); put(p6,_p7);take(p5,_p5);m1!_p5;take(p8,_p8);m2!_p8;}
			:: (full(m2) && empty(m1)) -> atomic{m2?_m2;m1!_m2;}
			:: (empty(m2) && full(m3)) -> atomic{m3?_m3;m2!_m3;}
			:: (full(m1) && full(p6.sync)) -> atomic{m1?_m1; put(p6,_m1);}
	od

}


init {
			port p5;
			port p6;
			port p7;
			port p8

	atomic{
			run prod(p5);	
			run cons(p6);	
			run prod(p7);	
			run prod(p8);	
			run Protocol1(p5,p6,p7,p8)
		
	}
}