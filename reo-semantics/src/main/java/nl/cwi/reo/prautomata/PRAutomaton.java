package nl.cwi.reo.prautomata;

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

	public String toString(){
		return null;
	}
	
	public SortedSet<Port> getInterface() {
		
		return new TreeSet<Port>(port);
	}

	public PRAutomaton getNode(SortedSet<Port> node) {
		
		return null;
	}

	@Override
	public PRAutomaton rename(Map<Port, Port> links) {
		
		return null;
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
