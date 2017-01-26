grammar Reo;

import Tokens, PA, WA, CAM, SA;

// File structure
file    : secn? imps* ID '='? rsys EOF;
secn    : 'section' name ';';
imps    : 'import' name ';';

// Reo Systems
rsys    : var                                                    # rsys_variable
        | sign '{' atom source? '}'                              # rsys_atomic
        | sign block                                             # rsys_composite ;
atom    : pa | cam | wa | sa ;
source  : target ':' STRING ;
target  : 'Java' ; 
      //| 'C/C++' 
      //| 'URL' ;

// Blocks
block   : ID? '{' stmt* '}' ;
stmt    : expr '=' expr                                          # stmt_equation
        | var rsys                                               # stmt_compdefn
        | comp                                                   # stmt_instance
        | block                                                  # stmt_block
        | 'for' ID '=' intr '..' intr block                      # stmt_iteration
        | 'if' bool block ('else' bool block)* ('else' block)?   # stmt_condition ;
        
// Components
comp    : rsys list? iface                                       # comp_instance
        | comp ID comp                                           # comp_composition
        | comp MUL comp                                          # comp_product
        | comp ADD comp                                          # comp_sum 
        | comp SCL comp                                          # comp_semicolon ;

// Expressions
expr    : var                                                    # expr_variable
        | strg                                                   # expr_string
        | bool                                                   # expr_boolean
        | intr                                                   # expr_integer
        | rsys                                                   # expr_component
        | list                                                   # expr_list ;
list    : '<' '>' | '<' expr (',' expr)* '>' ;

// String expressions    
strg    : STRING                                                 # strg_string
        | var                                                    # strg_variable
        | strg '+' strg                                          # strg_concatenation;

// Floating point expressions
fltp    : DEC                                                    # fltp_natural
        | var                                                    # fltp_variable ;

// Boolean expressions
bool    : BOOL                                                   # bool_boolean
        | var                                                    # bool_variable
        | intr op=(LEQ | LT | GEQ | GT | EQ | NEQ) intr          # bool_relation
        | '(' bool ')'                                           # bool_brackets
        | '!' bool                                               # bool_negation
        | bool AND bool                                          # bool_conjunction
        | bool OR bool                                           # bool_disjunction ;

// Signatures
sign    : params? nodes ;
params  : '<' '>' | '<' param (',' param)* '>' ;
param   : var? ptype ;
ptype   : ':' type                                               # ptype_typetag
        | sign                                                   # ptype_signature ;
nodes   : '(' ')' | '(' node (',' node)* ')' ;
node    : var? io=(IN | OUT | MIX) type? | var ;

// Type tags for uninterpreted data
type    : ID | ID ('*' type) | '(' type ')' | <assoc=right> type ':' type ; 

// Interface instantiation
iface   : '(' ')' | '(' rnode (',' rnode)* ')' ;
rnode   : prio=(ADD | AMP)? var ;
        
// Variables (and ranges of variables)
var     : name indices* ;
name    : (ID '.')* ID ;
indices : '[' intr ']' | '[' intr '..' intr ']' ;

// Integer expressions
intr    : NAT                                                    # intr_natural
        | var                                                    # intr_variable
        | '(' intr ')'                                           # intr_brackets
        | <assoc=right> intr POW intr                            # intr_exponent
        | MIN intr                                               # intr_unarymin
        | intr op=(MUL | DIV | MOD) intr                         # intr_multdivrem
        | intr op=(ADD | MIN) intr                               # intr_addsub ;
 