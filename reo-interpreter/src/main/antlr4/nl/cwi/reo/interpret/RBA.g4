grammar RBA;

import Tokens;

rba      : 	'#RBA' rba_tr* ;
rba_tr   : 	'[' rba_sc ',' rba_dc ']';
rba_dt	 : 	NAT 									#Rba_nat
          | BOOL									#Rba_bool
          | STRING									#Rba_string
          | DEC   									#Rba_decimal

          | 'q' NAT  								#Rba_dt_memorycellIn
          | 'q' NAT '\'' 							#Rba_dt_memorycellOut
          | ID '(' rba_tr (','rba_tr)* ')' 			#Rba_dt_function
          | 'null' 									#Rba_dt_null 
          | ID										#Rba_dt_parameter ;

rba_sc   : 	'{' rba_port (',' rba_port)* '}' ;
rba_port : 	ID										#Rba_included_port
			| '~'ID 								#Rba_excluded_port;
			
rba_dc   : 	 rba_dt									#Rba_term										
			| '(' rba_dc ')' 						#Rba_def
			| rba_dt '==' rba_dt 					#Rba_dc_equality 
			| rba_dc '&&' rba_dc					#Rba_dc_conjunction ;

// rba      : '#RbA' rba_tr* ;
// rba_tr   : rba_sc ',' rba_dc ;
// rba_dt	 : NAT | BOOL | STRING | DEC | ID ;
// rba_sc   : '{' '}' | '{' rba_p (',' rba_p)* '}' ;
// rba_p	 : ID | '~' ID ;
// rba_dc   :  'true'
//          | 'false'
//          | '(' rba_dc ')' 
//          | rba_dc '&' rba_dc ;

