grammar RBA;

import Tokens;

rba      : '#RbA' rba_tr* ;
rba_tr   : rba_sc ',' rba_dc ;
rba_dt	 : NAT | BOOL | STRING | DEC | ID ;
rba_sc   : '{' '}' | '{' rba_p (',' rba_p)* '}' ;
rba_p	 : ID | '~' ID ;
rba_dc   :  'true'
         | 'false'
         | '(' rba_dc ')' 
         | rba_dc '&' rba_dc ;
