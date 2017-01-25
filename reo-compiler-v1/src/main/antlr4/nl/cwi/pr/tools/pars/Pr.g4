grammar Pr;

//
// PROGRAMS
//

program
	: (SPACE* include)* 
	  (SPACE* (note | integerDefinition | extralogicalDefinition | familyDefinition | workerNameDefinition | mainDefinition))*
	  (SPACE* EOF)
	;

//
// INCLUDES
//

include
	: INCLUDE SPACE* '(' SPACE* '"' location=any '"' SPACE* ')'
	;

//
// PROPERTIES
//


note
	: PROPERTY SPACE* '(' SPACE* '"' content=any? '"' SPACE* ')'
	;

//
// FAMILES
//

familyDefinition
	: familySignature SPACE* EQ SPACE* familyExpression
	  # familyDefinitionExpression
	
	| familySignature SPACE* EQ SPACE* familyPrimitive
	  # familyDefinitionPrimitive
	;

familySignature
	: familyName
	  (SPACE* familyIntegerNameList)? 
	  (SPACE* familyExtralogicalNameList)?
	  SPACE* familyPortOrArrayNameList
	;

familyName
	: IDENTIFIER
	;

familyIntegerNameList
	: '[' SPACE* integerNameList SPACE* ']'
	;

familyExtralogicalNameList
	: '<' SPACE* extralogicalNameList SPACE* '>' 
	;

familyPortOrArrayNameList
	: '(' 
	  (SPACE* input=portOrArrayNameList)?
	  SPACE* SEMICOLON 
	  (SPACE* output=portOrArrayNameList)?
	  SPACE* ')'
	;

familyExpression
	: memberSignature
	  # familyExpressionValue
	  
	| '{' SPACE* familyExpression SPACE* '}'
	  # familyExpressionScope
	
	| PROD SPACE* integerNameDeclaration SPACE* familyExpression
	  # familyExpressionProd
		
	| left=familyExpression SPACE* MULT SPACE* right=familyExpression
	  # familyExpressionMult
	
	| IF
	  SPACE* '(' SPACE* booleanExpression SPACE* ')'
	  SPACE* ifBranche=familyExpression
	  SPACE* ELSE
	  SPACE* elseBranche=familyExpression
	  # familyExpressionIf
	
	| LET
	  SPACE* integerName 
	  SPACE* EQ 
	  SPACE* integerExpression 
	  SPACE* familyExpression
	  # familyExpressionLet 
	;

familyPrimitive
	: PRIMITIVE SPACE* '(' SPACE* '"' any '"' SPACE* ')'
	;

//
// MEMBERS
//

memberSignature
	: memberName
	  (SPACE* memberIntegerExpressionList)? 
	  (SPACE* memberExtralogicalExpressionList)?
	  SPACE* memberPortOrArrayExpressionList
	;

memberName
	: IDENTIFIER
	;

memberIntegerExpressionList
	: '[' SPACE* integerExpressionList SPACE* ']'
	;

memberExtralogicalExpressionList
	: '<' SPACE* extralogicalExpressionList SPACE* '>'
	;

memberPortOrArrayExpressionList
	: '('
	  (SPACE* input=portOrArrayExpressionList)?
	  SPACE* SEMICOLON
	  (SPACE* output=portOrArrayExpressionList)? 
	  SPACE* ')'
	;

//
// INTEGERS
//

integerDefinition
	: integerName SPACE* EQ SPACE* integer
	;

integerNameList
	: integerName (SPACE* COMMA SPACE* integerName)*
	;

integerName
	: IDENTIFIER
	;

integerNameDeclaration
	: integerName SPACE* COLON SPACE* integerDomain
	;

integerDomain
	: integerExpression DOTS integerExpression
	;

integerExpressionList
	: integerExpression (SPACE* COMMA SPACE* integerExpression)*
	;

integerExpression
	: integer
	  # integerExpressionValue
	  
	| integerName
	  # integerExpressionName
	
	| SIZE SPACE* arrayOrArrayName
	  # integerExpressionSize
	  
	| '(' SPACE* integerExpression SPACE* ')'
	  # integerExpressionScope
	  
	| left=integerExpression
	  SPACE* binop=(TIMES | OBELUS | MODULO)
	  SPACE* right=integerExpression
	  # integerExpressionBinop1
	   
	| left=integerExpression
	  SPACE* binop=(PLUS | MINUS)
	  SPACE* right=integerExpression
	  # integerExpressionBinop2
	;

integer
	: NUMBER | '-' NUMBER
	;

//
// EXTRALOGICALS
//

extralogicalDefinition
	: extralogicalName SPACE* EQ SPACE* extralogical
	;

extralogicalNameList
	: extralogicalName (SPACE* COMMA SPACE* extralogicalName)*
	;

extralogicalName
	: IDENTIFIER
	;

extralogicalExpressionList
	: extralogicalExpression (SPACE* COMMA SPACE* extralogicalExpression)*
	;

extralogicalExpression
	: extralogical
	  # extralogicalExpressionValue
	  
	| extralogicalName
	  # extralogicalExpressionName
	;

extralogicalList
	: extralogical (SPACE* COMMA SPACE* extralogical)*
	;

extralogical
	: code
	;

//
// PORTS / ARRAYS
//

portOrArrayNameList
	: portOrArrayName (SPACE* COMMA SPACE* portOrArrayName)*
	;

portOrArrayName
	: IDENTIFIER
	  # portOrArrayNamePort
	
	| IDENTIFIER SPACE* '[' ']'
	  # portOrArrayNameArray
	;

portOrArrayExpressionList
	: portOrArrayExpression (SPACE* COMMA SPACE* portOrArrayExpression)*
	;
	
portOrArrayExpression
	: portExpression
	  # portOrArrayExpressionPort
	
	| arrayExpression
	  # portOrArrayExpressionArray
	;

portExpressionList
	: portExpression (SPACE* COMMA SPACE* portExpression)*
	;

portExpression
	: portOrPortName
	  # portExpressionPort
	
	| arrayOrArrayName 
	  SPACE* '['
	  SPACE* integerExpression 
	  SPACE* ']'
	  # portExpressionArray
	;

portOrPortName
	: IDENTIFIER
	;

arrayExpression
	: arrayOrArrayName 
	  SPACE* '[' 
	  SPACE* integerDomain
	  SPACE* ']'
	  # arrayExpressionImplicit
	
	| '[' portExpressionList ']'
	  # arrayExpressionExplicit
	;

arrayOrArrayName
	: IDENTIFIER
	;

//
// BOOLEANS
//

booleanExpression
	: TRUE
	  # booleanExpressionTrue
	
	| FALSE
	  # booleanExpressionFalse
	
	| SPACE* left=integerExpression SPACE* EQUALS SPACE* right=integerExpression
	  # booleanExpressionEquals
	
	| '(' SPACE* booleanExpression SPACE* ')'
	  # booleanExpressionScope
	
	| NEGATION SPACE* booleanExpression
	  # booleanExpressionNegation
	
	| left=booleanExpression 
	  SPACE* binop=(CONJUNCTION | DISJUNCTION)
	  SPACE* right=booleanExpression
	  # booleanExpressionBinop
	;

//
// MAINS
//

mainDefinition
	: MAIN 
	  (SPACE* '(' mainArgument (SPACE* COMMA SPACE* mainArgument)* ')')? 
	  SPACE* EQ 
	  SPACE* mainExpression
	;

mainArgument
	: IDENTIFIER
	;

mainExpression
	: protocolsExpression
	  # mainExpressionNoWorkers
	
	| protocolsExpression SPACE* AMONG SPACE* workersExpression
	  # mainexpressionWorkers
	;

protocolsExpression
	: protocolSignature
	  # protocolsExpressionValue
	
	| '{' SPACE* protocolsExpression SPACE* '}'
	  # protocolsExpressionScope
	  
	| left=protocolsExpression SPACE* AND SPACE* right=protocolsExpression
	  # protocolsExpressionAnd
	;

protocolSignature
	: memberSignature
	;

workerNameDefinition
	: workerNameName SPACE* EQ SPACE* workerName
	;

workerNameName
	: IDENTIFIER
	;

workersExpression
	: workerSignature
	  # workersExpressionValue
	
	| '{' SPACE* workersExpression SPACE* '}'
	  # workersExpressionScope
	
	| FORALL SPACE* integerNameDeclaration SPACE* workersExpression
	  # workersExpressionForall
	
	| left=workersExpression SPACE* AND SPACE* right=workersExpression
	  # workersExpressionAnd
	;

workerSignature
	: workerName SPACE* '(' SPACE* workerArgumentList SPACE* ')'
	;

workerName
	: anyExceptSpace
	;

workerArgumentList
	: workerArgument (SPACE* COMMA SPACE* workerArgument)*
	;

workerArgument
	: IDENTIFIER
	  # workerArgumentName
	  
	| integerExpression
	  # workerArgumentInteger
	
	| portExpression
	  # workerArgumentPort
	
	| code
	  # workerArgumentCode
	;

code
	: '\'' codeContent '\''
	;

codeContent
	:	(any | delimiterExceptSingleQuote)*
	;

//
// TOKENS II (Punctuation, keywords, and operators)
//

delimiterExceptSingleQuote
	: '(' | ')' | '[' | ']' | '{' | '}' | '<' | '>' | '"'
	;

any
	: (keyword | operator | punctuation | NUMBER | IDENTIFIER | SPACE | REST)+
	;

anyExceptSpace
	: (keyword | operator | punctuation | NUMBER | IDENTIFIER | REST)+
	;

keyword
	: AMONG | AND | CASE | DEFAULT | DEFINE | ELSE | FALSE | FORALL | IF
	| INCLUDE | LET | MAIN | MULT | PRIMITIVE | PROD | PROPERTY | SWITCH | TRUE
	;

AMONG		: 'among';
AND			: 'and';
CASE		: 'case';
DEFAULT		: 'default';
DEFINE		: 'define';
ELSE		: 'else';
FALSE		: 'false';
FORALL		: 'forall';
IF			: 'if';
INCLUDE		: 'include';
LET			: 'let';
MAIN		: 'main';
MULT		: 'mult';
PRIMITIVE	: 'primitive';
PROD		: 'prod';
PROPERTY	: 'property';
SWITCH		: 'switch';
TRUE		: 'true';

operator
	: CONJUNCTION | DISJUNCTION | EQUALS | MINUS | MODULO | NEGATION | OBELUS
	| PLUS | SIZE | TIMES
	;

CONJUNCTION	: '&&';
DISJUNCTION	: '||';
EQUALS		: '==';
MINUS		: '-';
MODULO		: '%';
NEGATION	: '!';
OBELUS		: '/';
PLUS		: '+';
SIZE		: '#';
TIMES		: '*';

punctuation
	: COLON | COMMA | DOLLAR | DOTS | EQ | SEMICOLON
	;
	
COLON		: ':';
COMMA		: ',';
DOLLAR		: '$';
DOTS		: '..';
EQ			: '=';
SEMICOLON	: ';';

//
// TOKENS I (Numbers, identifiers, space, and rest)
//

NUMBER		: ('0'..'9')+;

IDENTIFIER	: ('a'..'z' | 'A'..'Z' | '_')
			  ('a'..'z' | 'A'..'Z' | '_' | '0'..'9')*;

SPACE		: [ \n\t\r];

REST		: (.)+?;

//
// TOKENS 0
//

COMMENT		:  '//' ~('\r' | '\n')* -> skip;
 
ERROR		: . ;
