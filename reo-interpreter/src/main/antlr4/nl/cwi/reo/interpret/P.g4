grammar P;

import Tokens;

p      : '#P' p_form ;

p_form : '(' p_form ')'         #p_brackets     
       | 'forall' p_var p_form  #p_forall
       | 'exists' p_var p_form  #p_exists
       | '!' p_form             #p_not
       | p_form '&' p_form      #p_and
       | p_form '|' p_form      #p_or
       | ID p_args              #p_relation
       | p_term ('=' p_term)*   #p_eqs
       | p_term '!=' p_term     #p_neq
       | 'true'                 #p_true
       | 'false'                #p_false ;
       
p_args : '(' ')' | '(' p_term (',' p_term)* ')' ;

p_term : p_var                  #p_variable
       | 'null'                 #p_null 
       | NAT                    #p_natural
       | BOOL                   #p_boolean
       | STRING                 #p_string
       | DEC                    #p_decimal
       | ID p_args              #p_function ;
        
p_var  : ID                     #p_var_port
       | '$' ID                 #p_var_curr
       | '$' ID '\''            #p_var_next ;
