grammar SA;

import Tokens;

sa      : '#SA' sa_tr* ;
sa_tr   : ID '*'? '->' ID ':' sa_sc ',' sfunc                 # sa_transition ;
sfunc   : ( ID ':=' pbexpr (',' ID ':=' pbexpr)* )?           # sa_seepagefunction ;
sa_sc   : '{' '}' | '{' ID (',' ID)* '}' ;
pbexpr  : BOOL                                                # sa_pbe_bool
        | ID                                                  # sa_pbe_variable
        | '(' pbexpr ')'                                      # sa_pbe_brackets
        | pbexpr '&' pbexpr                                   # sa_pbe_and
        | pbexpr '|' pbexpr                                   # sa_pbe_or ;