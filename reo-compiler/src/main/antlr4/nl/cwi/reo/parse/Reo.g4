grammar Reo;

/**
 * Generic structure
 */

file 	: (comp | defn)* EOF 
		;
defn	: 'define' ID params? portset '{' atom '}'	 # defnAtomic
		| 'define' ID params? nodeset '{' comp* '}'  # defnComposed
		;
comp	: ID assign? nodeset						 # compReference 
		| 'for' ID '=' expr '...' expr '{' comp* '}' # compForLoop
		;
atom	: java										 # atomJava
		| c											 # atomC
		| pa										 # atomPA
		| casm										 # atomCASM
		| wa										 # atomWA
		;
params 	: '<' ID (',' ID)* '>' 
		;
assign 	: '<' value (',' value)* '>' 
		;
value	: ID
		| INT
		| STRING
		;
nodeset	: '(' ')' 
		| '(' nodes (',' nodes)* ')'
		;
nodes	: ID 										 # nodesName
		| ID '[' expr ']' 							 # nodesIndex
		| ID '[' expr '...' expr ']'				 # nodesRange
		;
portset	: '(' port (',' port)* ')'
		;
port	: ID '?'									 # portInput
		| ID '!'									 # portOutput
		;
expr 	: ID										 # exprParameter
		| INT 										 # exprInteger
		| INT ID 									 # exprScalar
		| '-' expr									 # exprUnaryMin
		| expr '+' expr 							 # exprAddition 
		| expr '-' expr 							 # exprDifference
		;

/**
 * Java
 */

java		: '#Java' FUNC
			;

/**
 * C
 */

c			: '#C' FUNC
			;
			
/**
 * Port Automata
 */

pa			: '#PA' pa_stmt* 
			;
pa_stmt		: ID '--' sync_const '->' ID 
			;
sync_const	: '{' '}'
			| '{' ID (',' ID)* '}'
			;

/**
 * Constraint Automata with State Memory
 */

casm		: '#CASM' casm_stmt* 
			;
casm_stmt 	: ID '--' sync_const ',' casm_dc '->' ID 
			;
casm_dc		: 'true'								 # casm_dcTrue
			| casm_term '==' casm_term				 # casm_dcEql
			;
casm_term	: STRING								 # casm_termData
			| 'd(' ID ')'							 # casm_termPort
			| ID									 # casm_termMemoryCurr
			| ID '\''								 # casm_termMemoryNext
			;

/**
 * Work Automata
 */

wa		: '#WA' wa_stmt* 
		;
wa_stmt : ID ':' wa_jc 								 # wa_stmtInvar
		| ID '--' sync_const ',' wa_jc '->' ID 		 # wa_stmtTrans
		;
wa_jc	: 'true' 									 # wa_jcTrue
		| ID '==' INT 								 # wa_jcEql
		| ID '<=' INT 								 # wa_jcLeq
		| wa_jc '&' wa_jc							 # wa_jcAnd
		;

/**
 * Tokens
 */
 
ID 		: [a-zA-Z] [a-zA-Z0-9]* ;
INT 	: ( '0' | [1-9] [0-9]* ) ;
STRING	: '\'' .*? '\'' ;
FUNC 	: [a-zA-Z] [a-zA-Z0-9_-.:]* ;
SPACES 	: [ \t\r\n]+ -> skip ;
SL_COMM : '//' .*? ('\n'|EOF) -> skip ;
ML_COMM : '/*' .*? '*/' -> skip ;