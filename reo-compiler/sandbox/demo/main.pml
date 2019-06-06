/* Generated from main.treo by Reo 1.0.*/

#include "runtime.pml"

proctype Protocol1(port p15; port p14; port p19; port p20){
	chan m2 = [1] of {int};  
	chan m3 = [1] of {int};  
	chan m1 = [1] of {int};  
	      
	int _m2;  
	int _m3;  
	int _m1;  
	int _p15 ;
	int _p14 ;
	int _p19 ;
	int _p20 ;
	do
			:: (full(m2) && empty(m1)) -> atomic{m2?_m2;m1!_m2;}
			:: (full(m1) && full(p15.sync)) -> atomic{m1?_m1; put(p15,_m1);}
			:: (empty(m2) && full(m3)) -> atomic{m3?_m3;m2!_m3;}
			:: (empty(m2) &&  full(p14.data) && empty(m3) && empty(m1) &&  full(p19.data) &&  full(p20.data) && full(p15.sync)) -> atomic{take(p14,_p14);m1!_p14;take(p20,_p20);m2!_p20;take(p19,_p19); put(p15,_p19);}
	od

}


init {
			port p15;
			port p14;
			port p19;
			port p20

	atomic{
			run prod1(p19);	
			run prod2(p14);	
			run prod3(p20);	
			run cons(p15);	
			run Protocol1(p15,p14,p19,p20)
		
	}
}