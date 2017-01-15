grammar PA;

import Tokens;

pa      : '#PA' pa_init? pa_tr* ;
pa_init : ID ':' 'initial' ;
pa_tr   : ID '->' ID ':' pa_sc ;
pa_sc   : '{' '}' | '{' ID (',' ID)* '}' ;