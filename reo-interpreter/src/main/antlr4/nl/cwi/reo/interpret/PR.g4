grammar PR;

import Tokens;

pr      : '#PR' pr_string (('[' param ']')? pr_port)?;
pr_string	:	ID;
pr_port	:	'(' (ID (',' ID)*)? ';' (ID (',' ID)*)? ')';

param : (NAT | ID);
