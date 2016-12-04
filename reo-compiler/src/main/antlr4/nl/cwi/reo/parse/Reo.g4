grammar Reo;

/**
 * Generic structure
 */

file    : incl* body EOF 
        ;
incl    : 'include' PATH
        ;
body    : (comp | defn)*
        ;
defn    : 'define' ID params? portset '{' atom '}'  # defnAtomic
        | 'define' ID params? nodeset '{' body '}'  # defnComposed
        | nodes '=' nodes                           # defnJoin
        ;
comp    : ID assign? nodeset                        # compReference 
        | 'for' ID '=' expr '...' expr '{' body '}' # compForLoop
        ;
atom    : java   # atomJava
        | c      # atomC
        | pa     # atomPA
        | cam    # atomCAM
        | wa     # atomWA
        ;
params  : '<' ID (',' ID)* '>' 
        ;
assign  : '<' value (',' value)* '>' 
        ;
value   : ID
        | INT
        | STRING
        ;
nodeset : '(' ')' 
        | '(' nodes (',' nodes)* ')'
        ;
nodes   : ID indices*
        ;
indices : '[' expr ']'
        | '[' expr '...' expr ']'
        ;
portset : '(' port (',' port)* ')'
        ;
port    : ID '?'  # portInput
        | ID '!'  # portOutput
        ;
expr    : ID             # exprParameter
        | INT            # exprInteger
        | '-' expr       # exprUnaryMin
        | expr '+' expr  # exprAddition 
        | expr '-' expr  # exprDifference
        | expr '*' expr  # exprProduct
        | expr '\' expr  # exprDivision
        | expr '%' expr  # exprRemainder
        ;

/**
 * Java
 */

java    : '#Java' FUNC
        ;

/**
 * C
 */

c       : '#C' FUNC
        ;     

/**
 * Synchronization constraints
 */

sc      : '{' '}'
        | '{' ID (',' ID)* '}'
        ;

/**
 * Data constraints
 */
 
dc      : t OP dc                              # dcInfix
        | PO t                                 # dcPrefix
        | t                                    # dcTerm
        ;
t       : STRING                               # tString
        | INT                                  # tInteger
        | ID                                   # tPortOrMem
        | ID '\''                              # tMemoryNext
        | ID '(' t (',' t )* ')'               # tFunction
        | '(' dc ')'                           # tBrackets
        ;

/**
 * Job constraints
 */
   
jc      : 'true'           # jcTrue
        | ID '==' INT      # jcEql
        | ID '<=' INT      # jcLeq
        | jc '&' jc        # jcAnd
        ;
                
/**
 * Port Automata
 */

pa      : '#PA' pa_tr* 
        ;
pa_tr   : ID '*'? '--' sc '->' ID 
        ;

/**
 * Constraint Automata with Memory
 */

cam     : '#CAM' cam_tr* 
        ;
cam_tr  : ID '*'? '--' sc ',' dc '->' ID 
        ;

/**
 * Work Automata
 */

wa      : '#WA' wa_stmt* 
        ;
wa_stmt : ID '*'? ':' jc                   # wa_stmtInvar
        | ID '*'? '--' sc ',' jc '->' ID   # wa_stmtTrans
        ;

/**
 * Tokens
 */

ID      : [a-zA-Z] [a-zA-Z0-9]* ;
OP	: ('=' | '!' | '<' | '>' | '-')+
PO	: ('-' | '~')
INT     : ( '0' | [1-9] [0-9]* ) ;
PATH    : [-.a-zA-Z0-9:/\]+;
STRING  : '\'' .*? '\'' ;
FUNC    : [a-zA-Z] [a-zA-Z0-9_-.:]* ;
SPACES  : [ \t\r\n]+ -> skip ;
SL_COMM : '//' .*? ('\n'|EOF) -> skip ;
ML_COMM : '/*' .*? '*/' -> skip ;
