grammar RBA;

import Tokens;

rba      : '#RBA' rba_tr* ;
rba_tr   : '(' rba_at (',' rba_at )* ')' '->' '(' rba_dt (',' rba_dt)* ')' ':' rba_sc ',' rba_dc ;
rba_at	 : NAT | ID ;
rba_dt	 : rba_at ;
rba_sc   : '{' ID (',' ID)* '}' ;
rba_dc   :  ID
			| '(' rba_dc ')' 
			| rba_dc '&&' rba_dc 
			| rba_dc '||' rba_dc 
			| '!' rba_dc ;
