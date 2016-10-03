grammar TIGA;

strategy
 : .*? 'Strategy to win' action+ EOF
 ;

action
 : 'State:' .*?
 ;