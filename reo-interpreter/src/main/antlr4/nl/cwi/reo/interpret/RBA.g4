grammar RBA;

import Tokens;

rba      	: 	'#RBA' rba_initial* rba_rule* ;
rba_initial :	'$' ID '=' rba_term ';' ;
rba_rule 	:	'{' (rba_port (',' rba_port)* )? '}' rba_formula;

rba_port 	: ID 										#rba_syncFire
			| '~'ID							 			#rba_syncBlock ;   

rba_formula : 'true'									#rba_true
			| 'false' 									#rba_false
			| rba_formula (',' rba_formula)+			#rba_conjunction
			| '(' rba_formula ')' 						#rba_def
          	| ID '(' rba_term (',' rba_term)* ')'		#rba_relation
          	| '!' ID '(' rba_term (',' rba_term)* ')'	#rba_notrelation
			| rba_term '=' rba_term						#rba_equality
			| rba_term '!=' rba_term					#rba_inequality ;
			

rba_term	: NAT 										#rba_nat
	      	| BOOL										#rba_bool
          	| STRING									#rba_string
          	| DEC   									#rba_decimal
          	| ID '(' rba_term (',' rba_term)* ')'		#rba_function
          	| '[' rba_term ':' rba_term (',' rba_term ':' rba_term)* ']'	#rba_distribution
          	| '$' ID  									#rba_memorycellIn
          	| '$' ID '\''	 							#rba_memorycellOut
          	| 'null' 									#rba_null 
          	| '*'										#rba_null_ctxt
          	| ID										#rba_parameter
            | MIN rba_term                                        # rba_unarymin
            | rba_term op=(MUL | DIV | MOD | ADD | MIN) rba_term  # rba_operation ;