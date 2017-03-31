package nl.cwi.reo.semantics.symbolicautomata;

import java.util.Map;
import java.util.Objects;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.ports.PortType;

public class Node implements Variable {
	
	private final Port p;
	
	public Node(Port p) {
		this.p = p;
	}

	public Port getPort() {
		return p;
	}

	@Override
	public boolean hadOutputs() {
		return p.getType() == PortType.OUT;
	}

	public String getName(){
		return p.getName();
	}
	
	@Override
	public Term rename(Map<Port, Port> links) {
		Port b = links.get(p);
		if (b != null)
			return new Node(b);
		return this;
	}
	public String toString(){
		return p.getName();
	}
	
	public boolean equals(Object o){
		if(o instanceof Node){
			return ((Node) o).getPort().equals(p);
		}
		return false;
	}
	@Override
	public int hashCode() {
		return Objects.hash(this.p);
	}

	public boolean isInput() {
		return p.isInput();
	}
}
