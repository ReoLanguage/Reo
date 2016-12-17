
main = (){ sequencer<3>(a,b,c) }

producer(a!, b!) {
  #WA 
  q0 : x <= 5
  q0 -> q1 : {}, x == 5 // produce the data
  q1 -> q0 : {a}, true	
  q1 -> q0 : {b}, true 
}

fifo1(a?, b!) {
  #WA 
  q0* -- {a}, true -> q1
  q1 -- {b}, true -> q0	
}

fifofull = (a?,b!) {
  #WA 
  q0 -- {a}, true -> q1
  q1* -- {b}, true -> q0	
}

fifo = <k> (a[0],a[k]) { 
  for i = 0 ... k-1 { 
    fifo1(a[i],a[i+1])
  }
}

sequencer = <k> (a[1..k]) {
  fifofull(x[0],x[1])
  syncdrain(a[1],x[1])
  for i = 2..k {
    fifo1(x[i-1],x[i])
    syncdrain(a[i],x[i])
  }
}