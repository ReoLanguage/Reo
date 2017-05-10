package nl.cwi.reo.semantics.rbautomaton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.predicates.Disjunction;
import nl.cwi.reo.semantics.predicates.Formula;
import nl.cwi.reo.semantics.predicates.Node;
import nl.cwi.reo.semantics.predicates.Variable;

public class Hypergraph {

	private List<Hyperedge> hyperedges;

	/**
	 * Build a tree out of a root port and a set of rules.
	 * Every rules that must fire the root port is added as child.
	 */
	public Hypergraph(Set<Rule> l) {
		hyperedges = new ArrayList<Hyperedge>();
		for(Rule r : l){	
			RuleNode rule = new RuleNode(r);
			for(Port v : r.getSync().keySet())
				if(r.getSync().get(v)){
					if(!getHyperedges(v).isEmpty()){
						getHyperedges(v).get(0).addLeave(rule);
					}
					else
						hyperedges.add(new Hyperedge(new PortNode(v),Arrays.asList(rule)));
				}
		}
		
	}
	
	public List<Hyperedge> getHyperedges(Port p){
		List<Hyperedge> hyperedgeList = new ArrayList<Hyperedge>();
		for(Hyperedge h : hyperedges){
			if(h.getRoot().getPort().equals(p)){
				hyperedgeList.add(h);
			}
		}
		return hyperedgeList;
	}
	
	public List<Hyperedge> getHyperedges(){
		return hyperedges;
	}
	
	/**
	 * Composition of two hypergraphs by taking the union of hyperedges and merging commune PortNode 
	 * 
	 * @return new hypergraph 
	 */
	public Hypergraph compose(Hypergraph h) {
		hyperedges.addAll(h.getHyperedges());
		
//		Set<Port> v = new HashSet<>();
//		for(Hyperedge hyperedge : hyperedges){
//			v.add(hyperedge.getRoot().getPort());
//		}
//		
		return this;
	}
	
	public Hypergraph hide(Port p){
		Formula f = null;
		int sizeAssignement=0;
		
		for(Hyperedge h : getHyperedges(p)){
			if(h.getAssignement() instanceof Disjunction &&
					((Disjunction)h.getAssignement()).getClauses().size()>0){
				if(sizeAssignement == 0){
					sizeAssignement = ((Disjunction)h.getAssignement()).getClauses().size();
					f = ((Disjunction)h.getAssignement());
				}
				else if(((Disjunction)h.getAssignement()).getClauses().size()< sizeAssignement){
					sizeAssignement = ((Disjunction)h.getAssignement()).getClauses().size();
					f = ((Disjunction)h.getAssignement());					
				}
			}
		}
		
		for(Hyperedge h : getHyperedges(p)){
			if(f!=null)
				h.compose(f);
			h.hide(h.getRoot());
		}
		
		return this;
		
	}
	
	public Set<Rule> getRules(){
		Set<Rule> s = new HashSet<>();
		
		return s;
	}

	
	public String toString(){
		Set<Port> v = new HashSet<>();
		for(Hyperedge h : hyperedges){
			v.add(h.getRoot().getPort());
		}
		String s = "";
		for(Port var : v){
			s = s+ "Root : "+var.toString()+"\n{";
			int i=getHyperedges(var).size();
			for(Hyperedge h : getHyperedges(var)){
				s=s+h.toString()+(i>1?" && \n":"");
				i--;
			}
			s=s+"}\n \n";
		}
		return s;
	}
	
}
