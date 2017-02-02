package nl.cwi.reo.prautomata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import nl.cwi.reo.semantics.api.Port;
import nl.cwi.reo.semantics.api.Semantics;
import nl.cwi.reo.semantics.api.SemanticsType;

public class PRAutomaton implements Semantics<PRAutomaton> {
		
	private String name;
	private String variable;
	private Integer value; 
	private List<Port> port;
	

	public PRAutomaton(String name, String variable, Integer value, List<Port> port){
		this.name=name;
		this.value=value;
		this.variable=variable;
		this.port=port;
		
	}
	
	public SemanticsType getType() {
		return SemanticsType.PA;
	}
		
	public String getName(){
		return name;
	}

	
	public Port getDest(){
		return port.get(1);
	}

	public String toString(){
		StringBuilder str = new StringBuilder();
	//	str.append(name + "["+ variable + "]("+getInterface()+")");
		str.append(name + "("+getInterface()+")");
		 
		return str.toString();
	}
	
	public SortedSet<Port> getInterface() {
		
		return new TreeSet<Port>(port);
	}

	public PRAutomaton getNode(SortedSet<Port> node) {
		
		List<Port> P = new ArrayList<Port>();

		SortedSet<Port> ins = new TreeSet<Port>();
		SortedSet<Port> outs = new TreeSet<Port>();
		for (Port p : node) {
			P.add(p);
			switch (p.getType()) {
			case IN:
				outs.add(p);
				break;
			case OUT: 
				ins.add(p);
				break;
			default:
				break;
			}
		}

		return new PRAutomaton(name,variable,value,P);
	}

	@Override
	public PRAutomaton rename(Map<Port, Port> links) {
		
		List<Port> P = new ArrayList<Port>();
		
		for (Port a : this.port) {
			Port b = links.get(a);
			if (b == null) b = a;
			P.add(b);//P.add(new Port(b.getName()));
		}
		
		return new PRAutomaton(name,variable,value,P);
	}

	@Override
	public PRAutomaton evaluate(Map<String, String> params) {
		String s = params.get(variable);
		Integer newValue=null;
		
		try {
			newValue =Integer.parseInt(s);
		}
		catch(NumberFormatException e){
			
		}
		
		return new PRAutomaton(name,variable,newValue,port);
	}

	@Override
	public PRAutomaton compose(List<PRAutomaton> automata) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PRAutomaton restrict(Collection<? extends Port> intface) {
		// TODO Auto-generated method stub
		return null;
	}

}
