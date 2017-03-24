grammar RBA;

import Tokens;

rba      : 	'#RBA' rba_tr* ;
rba_tr   : 	rba_sc ',' rba_dc ;
rba_dt	 : 	NAT 									#Rba_nat
          | BOOL									#Rba_bool
          | STRING									#Rba_string
          | DEC   									#Rba_decimal
          | ID										#Rba_dt_parameter
          | '\\' ID  								#Rba_dt_memorycellIn
          | '\\' ID '\'' 							#Rba_dt_memorycellOut
          | ID '(' rba_tr (','rba_tr)* ')' 			#Rba_dt_function
          | 'null' ;
rba_sc   : 	'{' rba_port (',' rba_port)* '}' ;
rba_port : 	ID										#Rba_included_port
			| '~'ID 								#Rba_excluded_port;
			
rba_dc   : 	 ID										#Rba_term										
			| '(' rba_dc ')' 						#Rba_def
			| ID '==' ID 							#Rba_dc_equality 
			| rba_dc '&&' rba_dc					#Rba_dc_conjunction ;

