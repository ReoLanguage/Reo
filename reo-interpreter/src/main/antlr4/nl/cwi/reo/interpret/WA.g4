grammar WA;

import Tokens;

wa      : '#WA' wa_expr* ;
wa_expr : ID '*'? ':' jc                                      # wa_invariant
        | ID '*'? '->' ID ':' wa_set ',' jc ',' wa_set        # wa_transition ;   
wa_set  : '{' '}' | '{' ID (',' ID)* '}' ;
jc      : BOOL                                                # wa_jc_bool
        | '(' jc ')'                                          # wa_jc_brackets
        | ID '==' NAT                                         # wa_jc_eq
        | ID '<=' NAT                                         # wa_jc_leq
        | ID '>=' NAT                                         # wa_jc_geq
        | jc '&&' jc                                          # wa_jc_and
        | jc '||' jc                                          # wa_jc_or ;