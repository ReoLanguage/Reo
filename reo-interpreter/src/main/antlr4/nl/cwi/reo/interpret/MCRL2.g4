grammar MCRL2;

import Tokens;


mcrl2      : '#MCRL2' mcrl2_string (('[' mcrl2_param ']')? mcrl2_port)?;
mcrl2_string	:	ID;
mcrl2_port	:	'(' (ID (',' ID)*)? ';' (ID (',' ID)*)? ')';
mcrl2_param : NAT | STRING | ID ;
