package nl.cwi.reo.semantics.rbautomaton;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.checkerframework.checker.nullness.qual.Nullable;
import nl.cwi.reo.interpret.ports.Port;

public class PortNode {
	private Port p;

	public PortNode(Port p, List<HyperEdge> hyperedges) {
		this.p = p;
	}
	
	public PortNode(Port p) {
		this.p = p;
	}
		
	public PortNode setPort(Port p){
		this.p=p;
		return this;
	}
	
	public Port getPort(){
		return p;
	}
	
	public PortNode rename(Map<Port,Port> links){
		p=p.rename(links.get(p).getName());
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(@Nullable Object other) {
		if (other == null)
			return false;
		if (other == this)
			return true;
		if (!(other instanceof PortNode))
			return false;
		PortNode port = (PortNode) other;
		return this.p.equals(port.getPort());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.p);
	}
	
	public String toString(){
		String s = "("+p.toString()+")\n";
		return s;
	}
}
