/* Generated from alternator.treo by Reo 1.0.*/

#include "runtime.pml"

proctype Protocol1(port p7; port p8; port p9; port p11; port p10){
	chan m2 = [1] of {Object};  
	chan m3 = [1] of {Object};  
	chan m1 = [1] of {Object};  
	chan m4 = [1] of {Object};  
	        
	Object _m2;  
	Object _m3;  
	Object _m1;  
	Object _m4;  
	String _p7 ;
	String _p8 ;
	String _p9 ;
	String _p11 ;
	String _p10 ;
	do
			:: (empty(m3) && full(m4)) -> atomic{m4?_m4;m3!_m4;}
			:: (empty(m2) && empty(m3) && empty(m1) &&  full(p7.data) &&  full(p9.data) &&  full(p10.data) && empty(m4) &&  full(p11.data) && full(p8.sync)) -> atomic{take(p11,_p11);m2!_p11;take(p10,_p10);m3!_p10;take(p7,_p7);m1!_p7;take(p9,p9); put(p8,_p9);}
			:: (full(m2) && empty(m1)) -> atomic{m2?_m2;m1!_m2;}
			:: (empty(m2) && full(m3)) -> atomic{m3?_m3;m2!_m3;}
			:: (full(m1) && full(p8.sync)) -> atomic{m1?_m1; put(p8,_m1);}
	od

}


init {
			port p7;
			port p8;
			port p9;
			port p11;
			port p10

	atomic{
			run prod(p7);	
			run cons(p8);	
			run prod(p9);	
			run prod(p10);	
			run prod(p11);	
			run Protocol1(p7,p8,p9,p11,p10)
		
	}
}