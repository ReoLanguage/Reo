grammar Reo;

import Tokens, PA, WA, CAM, SA, PR;

file      : secn? imps* ID '='? component EOF;
secn      : 'section' name ';' ;
imps      : 'import' name ';' ;

// Components
component : var                                                   # component_variable
          | sign '{' atom ('|' source)? '}'                       # component_atomic
          | sign multiset                                         # component_composite ;
atom      : pa | cam | wa | sa ;
source    : LANG ':' STRING ( '(' ID (',' ID)* ')' )? ;

// Multisets
multiset  : instance                                              # multiset_constraint
          | term? '{' multiset* ('|' formula)? '}'                # multiset_setbuilder
          | multiset '+' multiset                                 # multiset_else
          | multiset '-' multiset                                 # multiset_without ;
//          | 'for' ID '=' term '..' term multiset                  # multiset_iteration
//          | 'if' formula multiset ('else' formula multiset)* 
//          ('else' multiset)?                                      # multiset_condition ;

// Instances
instance  : component list? ports                                 # instance_atomic
          | instance term instance                                # instance_composition
          | instance '*' instance                                 # instance_product
          | instance '+' instance                                 # instance_sum	
          | instance ';' instance                                 # instance_semicolon;

// Predicates
formula   : BOOL                                                  # formula_boolean
          | var                                                   # formula_variable
          | '(' formula ')'                                       # formula_brackets
          | var component                                         # formula_componentdefn
          | 'struct' ID '{' param (',' param)* '}'                # formula_structdefn
          | ID 'in' list                                          # formula_membership
          | term op=(LEQ | LT | GEQ | GT | EQ | NEQ) term         # formula_binaryrelation
          | FORALL ID 'in' list formula                           # formula_universal
          | EXISTS ID 'in' list formula                           # formula_existential
          | '!' formula                                           # formula_negation
          | formula AND formula                                   # formula_conjunction
          | formula OR formula                                    # formula_disjunction ;

// Terms
term      : NAT                                                   # term_natural
          | BOOL                                                  # term_boolean
          | STRING                                                # term_string
          | DEC                                                   # term_decimal
          | component                                             # term_componentdefn
          | instance                                              # term_instance
          | var                                                   # term_variable
          | list                                                  # term_list
          | '(' term ')'                                          # term_brackets
          | <assoc=right> term POW term                           # term_exponent
          | MIN term                                              # term_unarymin
          | term op=(MUL | DIV | MOD | ADD | MIN) term            # term_operation ;

// Lists
list      : '<' '>' | '<' range (',' range)* '>' ;

// Signatures
sign      : params? nodes ;

// Parameters
params    : '<' '>' | '<' param (',' param)* '>' ;
param     : var? ':' type | var? sign ;

// Nodes
nodes     : '(' ')' | '(' node (',' node)* ')' ;
node      : var | var? io=(IN | OUT | MIX) type? ;

// Type tags
type      : '$'? ID ('*' type)? | '(' type ':' type ')';

// Ports
ports     : '(' ')' | '(' port (',' port)* ')' ;
port      : prio=(ADD | AND)? var ;

// Variables
var       : name ('[' range ']')* ;
name      : (ID '.')* ID ;
range     : term | term '..' term ;

