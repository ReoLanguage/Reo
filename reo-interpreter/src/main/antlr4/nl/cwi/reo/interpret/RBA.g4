grammar RBA;

import Tokens;

rba      : 	'#RBA' rba_rule* ;
rba_rule :	'{' '(' rba_sync* ')' ',' rba_formula '}';		

rba_sync :	 ID 										#rba_syncAtom
			| ID ',' 									#rba_syncAtom
			| '!' ID 									#rba_syncTriger 
			| '!'ID ','									#rba_syncTriger 
			| '~'ID	','						 			#rba_syncBlock 
			| '~'ID							 			#rba_syncBlock ;   

rba_formula : rba_formula AND rba_formula (AND rba_formula)*		#rba_conjunction
			| '(' rba_formula ')' 									#rba_def
			| rba_term '==' rba_term								#rba_equality
			| rba_term '!=' rba_term								#rba_inequality; 

rba_term	 : 	NAT 									#rba_nat
	      	| BOOL										#rba_bool
          	| STRING									#rba_string
          	| DEC   									#rba_decimal
          	| '$q' NAT  								#rba_memorycellIn
          	| '$q' NAT '\'' 							#rba_memorycellOut
          	| 'null' 									#rba_null 
          	| ID										#rba_parameter ;

//sba_boolean : 'true' | 'false';
// rba_sync :	 rba_sync_atom 								#
// 			| rba_sync_atom rba_sync
// 			
// rba_sync_atom : ID 										#rba_syncAtom
// 			| '!'ID										#rba_syncTriger 
// 			| '~'ID						 				#rba_syncBlock;   
