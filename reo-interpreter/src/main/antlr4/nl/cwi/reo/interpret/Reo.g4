grammar Reo;

import Tokens, PA, WA, CAM, SA;

// File structure
file    : secn? imps* ID '='? cexpr ;
secn    : 'section' name ';';
imps    : 'import' name ';';

// Component expressions
cexpr   : var                                                    # cexpr_variable
        | sign '{' atom source? '}'                              # cexpr_atomic
        | sign block                                             # cexpr_composite ;
atom    : pa | cam | wa | sa ;
source  : target ':' STRING ;
target  : 'Java' ; 
        //| 'C/C++' 
        //| 'URL' ;

// Blocks
block   : '{' stmt (',' stmt)* '}' ;
stmt    : range '=' range                                        # stmt_equation
        | var cexpr                                              # stmt_compdefn
        | comp                                                   # stmt_instance
        | block                                                  # stmt_block
        | 'for' ID '=' iexpr '..' iexpr block                    # stmt_iteration
        | 'if' bexpr block (('else' bexpr block)* 'else' block)? # stmt_condition ;
comp    : cexpr list? iface 
        | comp '+' comp
        | comp '*' comp ;

// Ranges
range   : var                                                    # range_variable
        | '(,)'                                                  # range_operator
        | expr                                                   # range_expr 
        | list                                                   # range_list ;
list    : '<' '>' | '<' range (',' range)* '>' ;
expr    : STRING                                                 # expr_string
        | bexpr                                                  # expr_boolean
        | iexpr                                                  # expr_integer
        | cexpr                                                  # expr_component ;

// Boolean expressions
bexpr   : BOOL                                                   # bexpr_boolean
        | var                                                    # bexpr_variable
        | iexpr op=(LEQ | LT | GEQ | GT | EQ | NEQ) iexpr        # bexpr_relation
        | '(' bexpr ')'                                          # bexpr_brackets
        | '!' bexpr                                              # bexpr_negation
        | bexpr AND bexpr                                        # bexpr_conjunction
        | bexpr OR bexpr                                         # bexpr_disjunction ;

// Signatures
sign    : params? nodes ;
params  : '<' '>' | '<' param (',' param)* '>' ;
param   : var? ptype | var ;
ptype   : ':' type                                               # ptype_typetag
        | sign                                                   # ptype_signature ;
nodes   : '(' ')' | '(' node (',' node)* ')' ;
node    : var? io=(IN | OUT | MIX) type? | var ;

// Type tags for uninterpreted data
type    : ID | ID ('*' type) | '(' type ')' | <assoc=right> type ':' type ; 

// Interface instantiation
iface   : '(' ')' | '(' var (',' var)* ')' ;
        
// Variables (and ranges of variables)
var     : name indices* ;
name    : (ID '.')* ID ;
indices : '[' iexpr ']' | '[' iexpr '..' iexpr ']' ;

// Integer expressions
iexpr   : NAT                                                    # iexpr_natural
        | var                                                    # iexpr_variable
        | '(' iexpr ')'                                          # iexpr_brackets
        | <assoc=right> iexpr POW iexpr                          # iexpr_exponent
        | MIN iexpr                                              # iexpr_unarymin
        | iexpr op=(MUL | DIV | MOD) iexpr                       # iexpr_multdivrem
        | iexpr op=(ADD | MIN) iexpr                             # iexpr_addsub ;
 