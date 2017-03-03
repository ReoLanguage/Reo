package nl.cwi.reo.semantics.prautomata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.ports.PortType;
import nl.cwi.reo.interpret.typetags.TypeTag;
import nl.cwi.reo.interpret.values.Value;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.interpret.variables.Parameter;
import nl.cwi.reo.interpret.variables.ParameterType;
import nl.cwi.reo.interpret.variables.VariableExpression;
import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.semantics.SemanticsType;
import nl.cwi.reo.util.Monitor;

public class PRAutomaton implements Semantics<PRAutomaton> {
		
	private String name;
	private Value variable;
	private Value value;
	private List<Port> port;
	

	public PRAutomaton() {
		this.name = "";
		this.variable = null;
		this.port = new ArrayList<Port>();
	}
	
	public PRAutomaton(String name, Value variable, Value value, List<Port> port){
		this.name=name;
		this.variable=variable;
		this.value=value;
		this.port=port;	
	}
	public PRAutomaton(String name, Value variable, List<Port> port){
		this.name=name;
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
		str.append(name +"["+ variable + "]" + "(" +getInterface()+")");
		 
		return str.toString();
	}
	
	public SortedSet<Port> getInterface() {
		
		return new TreeSet<Port>(port);
	}

	public PRAutomaton getNode(Set<Port> node) {
		
		List<Port> P = new ArrayList<Port>();

		int counterI=0;
		int counterO=0;
		for (Port p : node) {
			switch (p.getType()) {
			case IN:
				counterI++;
				P.add(new Port(p.getName(),PortType.OUT,p.getPrioType(),p.getTypeTag(), false));
				break;
			case OUT: 
				counterO++;
				P.add(new Port(p.getName(),PortType.IN,p.getPrioType(),p.getTypeTag(), false));
				break;
			default:
				break;
			}
		}

		if(counterI>counterO)
			return new PRAutomaton("Replicator",null,P);
		
		return new PRAutomaton("Merger",null,P);
	}


	@Override
	public PRAutomaton rename(Map<Port, Port> links) {
		
		List<Port> P = new ArrayList<Port>();
		
		for (Port a : this.port) {
			Port b = links.get(a);
			if (b == null) b = a;
			P.add(b);//P.add(new Port(b.getName()));
		}
		
		return new PRAutomaton(name,variable,P);
	}

	@Override
	public PRAutomaton evaluate(Scope s, Monitor m) {
//		Value v = s.get(variable);
		Value l = s.get(new Parameter(variable.toString(),new TypeTag("int")));
//		Identifier i=null;
		this.value=l;
		
		return new PRAutomaton(name,l,l,port);
	}

	public Value getValue(){
		return value;
	}
	public Value getVariable(){
		return variable;
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
