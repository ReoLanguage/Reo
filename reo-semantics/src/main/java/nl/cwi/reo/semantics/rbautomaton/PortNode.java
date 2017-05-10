package nl.cwi.reo.semantics.rbautomaton;

import java.util.ArrayList;
import java.util.List;

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
	
	public Port getPort(){
		return p;
	}
	
	public void addHyperedge(Hyperedge h){
		hyperedges.add(h);
	}
	
	public boolean isVisited() {
		return visited;
	}

	@Override
	public void setFlag(boolean flag) {
		this.visited = flag;
	}
	
	public String toString(){
		String s = "("+p.toString()+")\n";
		return s;
	}
}
