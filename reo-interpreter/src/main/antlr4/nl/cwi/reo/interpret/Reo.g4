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
inst    : cexpr list? iface                                      # inst_instance
        | inst '*' inst                                          # inst_product
        | inst '+' inst                                          # inst_sum ;

// Ranges
range   : var                                                    # range_variable
        | '(,)'                                                  # range_comma
        | '(*)'                                                  # range_product
        | '(+)'                                                  # range_sum
        | expr                                                   # range_expr 
        | list                                                   # range_list ;
list    : '<' '>' | '<' range (',' range)* '>' ;
expr    : str                                                    # expr_string
        | bool                                                   # expr_boolean
        | int                                                    # expr_integer
        | comp                                                   # expr_component ;

// String expressions    
string  : STRING                                                 # string_string
        | var                                                    # string_variable
        | string '+' string                                      # string_concatenation;

// Boolean expressions
bool    : BOOL                                                   # bool_boolean
        | var                                                    # bool_variable
        | int op=(LEQ | LT | GEQ | GT | EQ | NEQ) int            # bool_relation
        | '(' bool ')'                                           # bool_brackets
        | '!' bool                                               # bool_negation
        | bool AND bool                                          # bool_conjunction
        | bool OR bool                                           # bool_disjunction ;

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
indices : '[' int ']' | '[' int '..' int ']' ;

// Integer expressions
int     : NAT                                                    # iexpr_natural
        | var                                                    # iexpr_variable
        | '(' int ')'                                            # iexpr_brackets
        | <assoc=right> int POW int                              # iexpr_exponent
        | MIN int                                                # iexpr_unarymin
        | int op=(MUL | DIV | MOD) int                           # iexpr_multdivrem
        | int op=(ADD | MIN) int                                 # iexpr_addsub ;
 