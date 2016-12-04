grammar treo;

file    : ('namespace' ID ('.' ID)*)? ('include' STRING)* defn* ;
defn    : vars '=' values | values '=' vars | ID comp ;  
vars    : ID ('.' ID)* indices* ;
indices : '[' expr ']' | '[' expr '..' expr ']' ;
values  : NAT | STRING | vars | comp | list | array ;
list    : '<' '>' | '<' values (',' values)* '>' ;
array   : '[' ']' | '[' values (',' values)* ']' ;
type    : ID | '<' ID (';' type) '>' | '(' type '->' type ')';
param   : vars # paramUntyped | vars nodes # paramComp | vars ':' type # paramTyped ;
srcnode : vars? '?' type? ;
snknode : vars? '!' type? ;
mixnode : vars? type? ;
port    : srcnode | snknode ;
node    : srcnode | snknode | mixnode ;
params  : '<' '>' | '<' param (',' param)* '>' ;
ports   : '(' port (',' port)* ')' ;
nodes   : '(' ')' | '(' node (',' node)* ')' ;
intface : '(' ')' | '(' vars (',' vars)* ')' ;
inst    : comp list? intface                                 # instance
        | 'for' ID '=' expr '..' expr body                   # iteration
        | 'if' expr body (('else' expr body)* 'else' body)?  # condition ;
comp    : vars                                               # reference
        | params? ports '{' atom '}'                         # atomic
        | params? nodes body                                 # anonymous ;
body    : '{' (inst | defn)* '}' ;
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

ID      : [a-zA-Z_] [a-zA-Z0-9_]*;
NAT     : ( '0' | [1-9] [0-9]* ) ;
STRING  : '\"' .*? '\"' ;
SPACES  : [ \t\r\n]+ -> skip ;
SL_COMM : '//' .*? ('\n'|EOF) -> skip ;
ML_COMM : '/*' .*? '*/' -> skip ;
