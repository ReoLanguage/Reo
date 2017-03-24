grammar Reo;

import Tokens, PA, WA, CAM, SA, PR, RBA;

// Reo File
file      : secn? imps* defn* EOF;
secn      : 'section' name ';' ;
imps      : 'import' name ';' ;
defn      : ID '='? component ;

// Components
component : var                                                   # component_variable
          | sign '{' atom ('|' source)? '}'                       # component_atomic
          | sign multiset                                         # component_composite ;
atom      : pa | cam | wa | sa | pr | rba ;
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
//          | instance term instance                                # instance_composition
          | instance '*' instance                                 # instance_product
          | instance '+' instance                                 # instance_sum	
          | instance ';' instance                                 # instance_semicolon;

// Statements
formula   : BOOL                                                  # formula_boolean
          | '(' formula ')'                                       # formula_brackets
          | var component                                         # formula_componentdefn
          | 'struct' ID '{' param (',' param)* '}'                # formula_structdefn
          | ID ':' list                                           # formula_membership
          | term op=(LEQ | LT | GEQ | GT | EQ | NEQ) term         # formula_binaryrelation
          | var                                                   # formula_variable
          | FORALL ID ':' list formula                            # formula_universal
          | EXISTS ID ':' list formula                            # formula_existential
          | '!' formula                                           # formula_negation
          | formula (AND | ',') formula                           # formula_conjunction
          | formula OR formula                                    # formula_disjunction
          | formula IMPLIES formula                               # formula_implication ;

// Terms
term      : NAT                                                   # term_natural
          | BOOL                                                  # term_boolean
          | STRING                                                # term_string
          | DEC                                                   # term_decimal
          | instance                                              # term_instance
          | var                                                   # term_variable
          | component                                             # term_componentdefn
          | list                                                  # term_list
          | tuple                                                 # term_tuple
          | func                                                  # term_function
          | '(' term ')'                                          # term_brackets
          | <assoc=right> term tuple                              # term_application
          | <assoc=right> term POW term                           # term_exponent
          | MIN term                                              # term_unarymin
          | term LIST term                                        # term_range
          | term op=(MUL | DIV | MOD | ADD | MIN) term            # term_operation ;

// Functions

func      : '{' ('[' term ',' term ']')* '}' ;

// Tuples
tuple     : '[' ']' | '[' term (',' term)* ']' ;

// Lists
list      : '<' '>' | '<' term (',' term)* '>' ;

// Signatures
sign      : params? nodes ;

// Parameters
params    : '<' '>' | '<' param (',' param)* '>' ;
param     : var? ':' type | var? sign ;

// Nodes
nodes     : '(' ')' | '(' node (',' node)* ')' ;
node      : var | var? io=(IN | OUT | MIX) type? ;

// Type tags
type      : '$'? ID | '(' type ')' | type '*' type | <assoc=right> type '^' type;

// Ports
ports     : '(' ')' | '(' port (',' port)* ')' ;
port      : prio=(ADD | AND)? var ;

// Variables
var       : name ('[' term ']')* ;
name      : (ID '.')* ID ;

