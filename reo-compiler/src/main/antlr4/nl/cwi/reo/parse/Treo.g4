grammar Treo;

file    : ('namespace' ID ('.' ID)*)? ('using' ID ('.' ID)*)* ('include' STRING)* defn* ;

// Definitions: variables and values
defn    : vars '=' values | values '=' vars | ID comp ;  
vars    : ID ('.' ID)* indices* ;
indices : '[' expr ']' | '[' expr '..' expr ']' ;
values  : NAT | STRING | vars | comp | list | array ;
array   : '[' ']' | '[' values (',' values)* ']' ;
list    : '<' '>' | '<' values (',' values)* '>' ;
intface : '(' ')' | '(' vars (',' vars)* ')' ;

// Components and instances
comp    : vars                                               # reference
        | params? ports '{' atom '}'                         # atomic
        | params? nodes body                                 # anonymous ;
inst    : comp list? intface                                 # instance
        | 'for' ID '=' expr '..' expr body                   # iteration
        | 'if' expr body (('else' expr body)* 'else' body)?  # condition ;
body    : '{' (inst | defn)* '}' ;

// Parameters, nodes and types
param   : vars # paramUntyped | vars nodes # paramComp | vars ':' type # paramTyped ;
params  : '<' '>' | '<' param (',' param)* '>' ;

// Nodes and ports
srcnode : vars? '?' type? ;
snknode : vars? '!' type? ;
mixnode : vars? type? ;
node    : srcnode | snknode | mixnode ;
nodes   : '(' ')' | '(' node (',' node)* ')' ;
port    : srcnode | snknode ;
ports   : '(' ')' | '(' port (',' port)* ')' ;

// Type tags
type    : ID | ID ('*' type) | '(' type ')' | <assoc=right> type ':' type ;

// Integer expressions
expr    : '(' expr ')'                                       # brackets
        | NAT                                                # natural
        | vars                                               # variable
        | <assoc=right> expr '^' expr                        # exponent
        | '-' expr                                           # unarymin
        | '!' expr                                           # negation
        | expr op=( '*' | '/' | '%' ) expr                   # multdivrem
        | expr op=('+' | '-') expr                           # addsub
        | expr op=('<=' | '<' | '>=' | '>') expr             # inequality
        | expr op=('==' | '!=') expr                         # disequality
        | expr '&&' expr                                     # conjunction
        | expr '||' expr                                     # disjunction ;
        
// Semantics
atom    : gpl                                                # atomGPL
        | pa                                                 # atomPA
        | cam                                                # atomCAM
        | wa                                                 # atomWA 
        | sa                                                 # atomSA ;
gpl     : '#GPL' STRING ;
pa      : '#PA' pa_tr* ;
pa_tr   : ID '*'? '->' ID ':' idset ;
idset   : '{' '}' | '{' ID (',' ID)* '}' ;
cam     : '#CAM' cam_tr* ;
cam_tr  : ID '*'? '->' ID ':' idset ',' dc ;
dc      : dt                                                 # dcTerm 
        | dc '^' dt                                          # dcPow
        | 'A' ID ':' dc                                      # dcUniversal
        | 'E' ID ':' dc                                      # dcExistential
        | 'E!' ID ':' dc                                     # dcUniqueExist
        | dt op=('*' | '/' | '%') dc                         # dcMDR
        | dt op=('+' | '-') dc                               # dcPlusMin
        | dt op=('<=' | '<' | '>=' | '>') dc                 # dcInEQ
        | dt op=('==' | '!=') dc                             # dcNotEQ
        | dt '&&' dc                                         # dcAND
        | dt '||' dc                                         # dcOR ;
dt      : '(' expr ')'                                       # dtBrackets
        | ID '(' expr (',' expr )* ')'                       # dtFunction
        | ID '\''                                            # dtNext
        | '-' dt                                             # dtUnaryMin
        | '!' dt                                             # dtNot
        | STRING                                             # dtData
        | ID                                                 # dtVariable ;
wa      : '#WA' wa_expr* ;
wa_expr : ID '*'? ':' jc                                     # wa_exprInvar
        | ID '*'? '->' ID ':' idset ',' jc ',' idset         # wa_exprTrans ;   
jc      : 'true'                                             # jcBool
        | '(' jc ')'                                         # jcBrackets
        | ID '==' NAT                                        # jcEQ
        | ID '<=' NAT                                        # jcLEQ
        | ID '>=' NAT                                        # jcGEQ
        | jc '&&' jc                                         # jcAND
        | jc '||' jc                                         # jcOR ;
sa      : '#SA' sa_tr* ;
sa_tr   : ID '*'? '->' ID ':' idset ',' sf;
sf      : ( ID ':=' pe (',' ID ':=' pe) )? ;
pe      : 'true' | 'false' | ID | pe '&&' pe | pe '||' pe ;

// Tokens
ID      : [a-zA-Z_] [a-zA-Z0-9_]*;
NAT     : ( '0' | [1-9] [0-9]* ) ;
STRING  : '\"' .*? '\"' ;
SPACES  : [ \t\r\n]+ -> skip ;
SL_COMM : '//' .*? ('\n'|EOF) -> skip ;
ML_COMM : '/*' .*? '*/' -> skip ;
