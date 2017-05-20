package nl.cwi.reo.semantics.rbautomaton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.ports.Port;

public class PortNode implements HypergraphNode{
	private List<Hyperedge> hyperedges;	
	private Port p;

	private boolean visited;

	public PortNode(Port p, List<Hyperedge> hyperedges) {
		this.p = p;
		this.hyperedges = hyperedges;
		visited = false;
	}
	
	public PortNode(Port p) {
		this.p = p;
		hyperedges=new ArrayList<Hyperedge>();
		visited = false;
	}
	
	public List<Hyperedge> getHyperedges(){
		return hyperedges;
	}
	
	public PortNode setPort(Port p){
		this.p=p;
		return this;
	}
	
	public Port getPort(){
		return p;
	}
	
	public void addHyperedge(Hyperedge h){
		hyperedges.add(h);
	}

	public void rmHyperedge(Hyperedge h){
		hyperedges.remove(h);
	}
	
	public boolean isVisited() {
		return visited;
	}

	@Override
	public void setFlag(boolean flag) {
		this.visited = flag;
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
