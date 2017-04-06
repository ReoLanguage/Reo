grammar SBA;

import Tokens;

sba      : 	'#SBA' sba_tr* ;
sba_tr   : 	'[' sba_sc ',' sba_dc ']';
sba_dt	 : 	NAT 									#Sba_nat
          | sba_boolean									#Sba_bool
          | STRING									#Sba_string
          | DEC   									#Sba_decimal
          | '$q' NAT  								#Sba_dt_memorycellIn
          | '$q' NAT '\'' 							#Sba_dt_memorycellOut
//         | ID '(' sba_dt (','sba_dt)* ')' 			#sba_dt_function
          | 'null' 									#Sba_dt_null 
          | ID										#Sba_dt_parameter ;

sba_sc   : 	'{' sba_port (',' sba_port)* '}' ;
sba_port : 	ID										#Sba_included_port
			| '~'ID 								#Sba_excluded_port;
			
sba_dc   : 	 sba_dt									#Sba_term										
			| '(' sba_dc ')' 						#Sba_def
			| sba_dt '==' sba_dt 					#Sba_dc_equality
			| sba_dt '!=' sba_dt 					#Sba_dc_inequality 
			| sba_dc '&&' sba_dc					#Sba_dc_conjunction ;

sba_boolean : 'true' | 'false';

// sba      : '#sba' sba_tr* ;
// sba_tr   : sba_sc ',' sba_dc ;
// sba_dt	 : NAT | BOOL | STRING | DEC | ID ;
// sba_sc   : '{' '}' | '{' sba_p (',' sba_p)* '}' ;
// sba_p	 : ID | '~' ID ;
// sba_dc   :  'true'
//          | 'false'
//          | '(' sba_dc ')' 
//          | sba_dc '&' sba_dc ;
