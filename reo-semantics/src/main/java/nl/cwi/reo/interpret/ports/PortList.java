package nl.cwi.reo.interpret.ports;

import java.util.List;

import nl.cwi.reo.interpret.variables.Variable;

public class PortList implements Variable {

	private List<Port> node;
	
	public PortList(List<Port> node){
		this.node=node;
	}
	
	public List<Port> getNode(){
		return node;
	}
	
}
