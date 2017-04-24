grammar RBA;

import Tokens;

rba      	: 	'#RBA' rba_rule* ;
rba_rule 	:	'{' (rba_port (',' rba_port)* )? '}' rba_formula;

rba_port 	: ID 										#rba_syncFire
			| '~'ID							 			#rba_syncBlock ;   

rba_formula : 'true'									#rba_true
			| 'false' 									#rba_false
			| rba_formula (',' rba_formula)+			#rba_conjunction
			| '(' rba_formula ')' 						#rba_def
			| rba_term '=' rba_term						#rba_equality
			| rba_term '!=' rba_term					#rba_inequality ;
			

rba_term	: NAT 										#rba_nat
	      	| BOOL										#rba_bool
          	| STRING									#rba_string
          	| DEC   									#rba_decimal
          	| ID '(' rba_term (',' rba_term)* ')'		#rba_function
          	| '$' ID  									#rba_memorycellIn
          	| '$' ID '\''	 							#rba_memorycellOut
          	| 'null' 									#rba_null 
          	| ID										#rba_parameter ;