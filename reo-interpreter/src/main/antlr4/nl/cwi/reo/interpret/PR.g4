grammar PR;

import Tokens;


pr      : '#PR' pr_string (('[' pr_param ']')? pr_port)?;
pr_string	:	ID;
pr_port	:	'(' (ID (',' ID)*)? ';' (ID (',' ID)*)? ')';
pr_param : NAT | ID ;
