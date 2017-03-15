grammar CAM;

import Tokens;

cam     : '#CAM' cam_tr* ;
cam_tr  : ID '*'? '->' ID ':' cam_sc ',' dc ;
cam_sc  : '{' '}' | '{' ID (',' ID)* '}' ;
dc      : dt                                                  # cam_dc_term 
        | dc POW dt                                           # cam_dc_exponent
        | FORALL ID ':' dc                                    # cam_dc_universal
        | EXISTS ID ':' dc                                    # cam_dc_existential
        | dt op=(MUL | DIV | MOD) dc                          # cam_dc_multdivrem
        | dt op=(ADD | MIN) dc                                # cam_dc_addsub
        | dt op=(LEQ | LT | GEQ | GT) dc                      # cam_dc_ineq
        | dt op=(EQ | NEQ) dc                                 # cam_dc_neq
        | dt AND dc                                           # cam_dc_and
        | dt OR dc                                            # cam_dc_or ;
dt      : '(' dc ')'                                          # cam_dt_brackets
        | ID '(' dc (',' dc )* ')'                            # cam_dt_function
        | ID '\''                                             # cam_dt_next
        | '-' dt                                              # cam_dt_unaryMin
        | '!' dt                                              # cam_dt_not
        | STRING                                              # cam_dt_data
        | ID                                                  # cam_dt_variable ;