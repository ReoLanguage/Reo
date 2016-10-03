/**
 * A non-deterministic producer that offer data at either port a or port b
 */
define producer(a!, b!) {
  #WA 
  q0 : x <= 5
  q0 -- {}, x == 5 -> q1 // produce the data
  q1 -- {a}, true -> q0	
  q1 -- {b}, true -> q0
}

define fifo1(a?, b!) {
  #WA 
  q0 -- {a}, true -> q1
  q1 -- {b}, true -> q0	
}

define fifo<k>(a[0],a[k]) { 
  for i = 0 ... k-1 { 
    fifo1(a[i],a[i+1])
  }
}

fifo<2>(x,y)
fifo<2>(y,z)
