grammar Reo;

/**
 * Generic structure
 */

file    : includes body EOF 
        ;
incl	: 'include' PATH
	;
body 	: (comp | defn)*
	;
defn    : 'define' ID params? portset '{' atom '}'  # defnAtomic
        | 'define' ID params? nodeset '{' body '}'  # defnComposed
	| nodes '=' nodes                           # defnJoin
        ;
comp    : ID assign? nodeset                        # compReference 
        | 'for' ID '=' expr '...' expr '{' body '}' # compForLoop
        ;
atom	: java   # atomJava
        | c      # atomC
        | pa     # atomPA
        | cam    # atomCAM
        | wa     # atomWA
        ;
params  : '<' ID (',' ID)* '>' 
        ;
assign  : '<' value (',' value)* '>' 
        ;
value	: ID
        | INT
        | STRING
        ;
nodeset : '(' ')' 
        | '(' nodes (',' nodes)* ')'
        ;
nodes	: ID indices*
	;
indices : '[' expr ']'
	| '[' expr '...' expr ']'
	;
portset	: '(' port (',' port)* ')'
	;
port	: ID '?'  # portInput
        | ID '!'  # portOutput
        ;
expr 	: ID             # exprParameter
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
 * Port Automata
 */

pa              : '#PA' pa_stmt* 
                ;
pa_stmt	        : ID '*'? '--' sync_const '->' ID 
                ;
sync_const      : '{' '}'
	        | '{' ID (',' ID)* '}'
                ;

/**
 * Constraint Automata with State Memory
 */

cam             : '#CAM' cam_stmt* 
                ;
cam_stmt        : ID '*'? '--' sync_const ',' e '->' ID 
                ;
		
		
e	: t OP e                               # cam_exInfix
	| PO t				       # cam_exInfix
	| t
	;
t 	: STRING                               # cam_termString
	| INT                                  # cam_termInteger
	| ID                                   # cam_termPortOrMem
	| ID '\''                              # cam_termMemoryNext
	| ID '(' t (',' t )* ')'               # cam_termFunc
	| '(' e ')' 
	;

/**
 * Work Automata
 */

wa              : '#WA' wa_stmt* 
                ;
wa_stmt         : ID '*'? ':' wa_jc                           # wa_stmtInvar
                | ID '*'? '--' sync_const ',' wa_jc '->' ID   # wa_stmtTrans
                ;
wa_jc           : 'true'           # wa_jcTrue
                | ID '==' INT      # wa_jcEql
                | ID '<=' INT      # wa_jcLeq
                | wa_jc '&' wa_jc  # wa_jcAnd
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
