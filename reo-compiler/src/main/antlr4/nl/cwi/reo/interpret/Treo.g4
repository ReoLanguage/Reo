grammar Treo;

file    : ('namespace' ID ('.' ID)*)? ('using' ID ('.' ID)*)* ('include' STRING)* defn* ;

// Definitions
defn    : value '=' value                                    # defn_equation
        | var comp                                           # defn_definition ;  
value   : ref | STRING | bexp | iexp | comp | list;
list    : '<' '>' | '<' value (',' value)* '>' ;

// Components
comp    : ref                                                 # comp_variable
        | params? ports '{' atom '}'                          # comp_atomic
        | params? nodes body                                  # comp_composite ;
inst    : comp list? intface                                  # inst_reference
        | 'for' ID '=' iexp '..' iexp body                    # inst_iteration
        | 'if' bexp body (('else' bexp body)* 'else' body)?   # inst_condition ;
body    : '{' (inst | defn)* '}' ;

// Boolean expressions
bexp    : BOOL                                                # bexp_boolean
        | var                                                 # bexp_variable
        | iexp op=('<=' | '<' | '>=' | '>'| '==' | '!=') iexp # bexp_relation
        | '(' bexp ')'                                        # bexp_brackets
        | '!' bexp                                            # bexp_negation
        | bexp '&&' bexp                                      # bexp_conjunction
        | bexp '||' bexp                                      # bexp_disjunction ;

// Signatures
params  : '<' '>' | '<' param (',' param)* '>' ;
param   : var? ':' type | var params nodes | var ;
nodes   : '(' ')' | '(' node (',' node)* ')' ;
node    : srcnode | snknode | mixnode ;
ports   : '(' ')' | '(' port (',' port)* ')' ;
port    : srcnode | snknode ;
srcnode : var? '?' type? ;
snknode : var? '!' type? ;
mixnode : var? ':' type | var;

// Type tags for uninterpreted data
type    : ID | ID ('*' type) | '(' type ')' | <assoc=right> type ':' type ; 

// Interface instantiation
intface : '(' ')' | '(' var (',' var)* ')' ;
        
// Variable lists
ref     : (ID '.')* var ;
var     : ID indices* ;
indices : '[' iexp ']' | '[' iexp '..' iexp ']' ;

// Integer expressions
iexp    : NAT                                                 # iexp_natural
        | var                                                 # iexp_variable
        | '(' iexp ')'                                        # iexp_brackets
        | <assoc=right> iexp '^' iexp                         # iexp_exponent
        | '-' iexp                                            # iexp_unarymin
        | iexp op=( '*' | '/' | '%' ) iexp                    # iexp_multdivrem
        | iexp op=('+' | '-') iexp                            # iexp_addsub ;

// Semantics
atom    : gpl                                                 # atom_sourcecode
        | pa                                                  # atom_portautomata
        | cam                                                 # atom_constraintautomata
        | wa                                                  # atom_workautomata 
        | sa                                                  # atom_seepageautomata ;
gpl     : '#GPL' STRING ;
pa      : '#PA' pa_tr* ;
pa_tr   : ID '*'? '->' ID ':' idset ;
idset   : '{' '}' | '{' ID (',' ID)* '}' ;
cam     : '#CAM' cam_tr* ;
cam_tr  : ID '*'? '->' ID ':' idset ',' dc ;
dc      : dt                                                  # cam_dc_term 
        | dc '^' dt                                           # cam_dc_exponent
        | 'A' ID ':' dc                                       # cam_dc_universal
        | 'E' ID ':' dc                                       # cam_dc_existential
        | 'E!' ID ':' dc                                      # cam_dc_uniqueexists
        | dt op=('*' | '/' | '%') dc                          # cam_dc_multdivrem
        | dt op=('+' | '-') dc                                # cam_dc_addsub
        | dt op=('<=' | '<' | '>=' | '>') dc                  # cam_dc_ineq
        | dt op=('==' | '!=') dc                              # cam_dc_neq
        | dt '&&' dc                                          # cam_dc_and
        | dt '||' dc                                          # cam_dc_or ;
dt      : '(' dc ')'                                          # cam_dt_brackets
        | ID '(' dc (',' dc )* ')'                            # cam_dt_function
        | ID '\''                                             # cam_dt_next
        | '-' dt                                              # cam_dt_unaryMin
        | '!' dt                                              # cam_dt_not
        | STRING                                              # cam_dt_data
        | ID                                                  # cam_dt_variable ;
wa      : '#WA' wa_expr*   ;
wa_expr : ID '*'? ':' jc                                      # wa_invariant
        | ID '*'? '->' ID ':' idset ',' jc ',' idset          # wa_transition ;   
jc      : 'true'                                              # wa_jc_bool
        | '(' jc ')'                                          # wa_jc_brackets
        | ID '==' NAT                                         # wa_jc_eq
        | ID '<=' NAT                                         # wa_jc_leq
        | ID '>=' NAT                                         # wa_jc_geq
        | jc '&&' jc                                          # wa_jc_and
        | jc '||' jc                                          # wa_jc_or ;
sa      : '#SA' sa_tr* ;
sa_tr   : ID '*'? '->' ID ':' idset ',' sfunc                 # sa_transition ;
sfunc   : ( ID ':=' pbexp (',' ID ':=' pbexp)* )?             # sa_seepagefunction ;
pbexp   : BOOL                                                # sa_pbe_bool
        | ID                                                  # sa_pbe_variable
        | pbexp '&&' pbexp                                    # sa_pbe_and
        | pbexp '||' pbexp                                    # sa_pbe_or ;

// Tokens
NAT     : ('0'|[1-9][0-9]*) ;
BOOL    : 'true'|'false' ;
ID      : [a-zA-Z_][a-zA-Z0-9_]*;
STRING  : '\"' .*? '\"' ;
SPACES  : [ \t\r\n]+ -> skip ;
SL_COMM : '//' .*? ('\n'|EOF) -> skip ;
ML_COMM : '/*' .*? '*/' -> skip ;