package nl.cwi.reo.semantics.rbautomaton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.predicates.Disjunction;
import nl.cwi.reo.semantics.predicates.Formula;

public class Hypergraph {

	private List<Hyperedge> hyperedges;
	
	private Set<Port> variables;
	
	private Set<RuleNode> rules;

	/**
	 * Build a tree out of a root port and a set of rules.
	 * Every rules that must fire the root port is added as child.
	 */
	public Hypergraph(Set<Rule> l) {
		hyperedges = new ArrayList<Hyperedge>();
		variables = new HashSet<Port>();
		rules = new HashSet<>();
		for(Rule r : l){	
			RuleNode rule = new RuleNode(r);
			rules.add(rule);
			for(Port v : r.getSync().keySet()){
				if(r.getSync().get(v)){
					if(!getHyperedges(v).isEmpty()){
						rule.addToHyperedge(getHyperedges(v).get(0));
					}
					else{
						Set<RuleNode> ruleNodes = new HashSet<RuleNode>();
						ruleNodes.add(rule);
						hyperedges.add(new Hyperedge(new PortNode(v),ruleNodes));
					}
					variables.add(v);
				}
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
	
	public Set<RuleNode> getRuleNode(){
		return rules;
	}
	
	public Set<Port> getVariable(){
		return variables;
	}
	
	
	/**
	 * Composition of two hypergraphs by taking the union of hyperedges and merging commune PortNode 
	 * 
	 * @return new hypergraph 
	 */
	public Hypergraph compose(Hypergraph h) {
		hyperedges.addAll(h.getHyperedges());
		variables.addAll(h.getVariable());
		rules.addAll(h.getRuleNode());		
		return this;
	}
	
	/**
	 * Distribute single hyperedges
	 * @return
	 */
	public Hypergraph distributeSingleEdge(){
				
		for(Port p : variables){
			List<Hyperedge> singleEdge = new ArrayList<>();
			List<Hyperedge> multiEdge = new ArrayList<>();
			
			for(Hyperedge h : getHyperedges(p)){
				if(h.getLeaves().size()==1)
					singleEdge.add(h);
				else
					multiEdge.add(h);
			}
			if(singleEdge.size()>1){
				Hyperedge e = singleEdge.get(0);
				singleEdge.remove(0);
				for(Hyperedge h : singleEdge){
					rules.remove(h.getLeaves().iterator().next());
					e.compose(h);
				}
				hyperedges.removeAll(singleEdge);
				singleEdge.clear();
				singleEdge.add(e);
			}
			if(!multiEdge.isEmpty()){
				Hyperedge e = multiEdge.get(0);
				for(Hyperedge h : singleEdge){
					rules.remove(h.getLeaves().iterator().next());
					e.compose(h);

				}
				if(!multiEdge.isEmpty()){
					hyperedges.removeAll(singleEdge);
				}
			}	
		}
		
		return this;
	}
	
	/**
	 * Distribute multi rules hyperedges
	 * @return
	 */
	public Hypergraph distributeMultiEdge(){
				
		for(Port p : variables){
			List<Hyperedge> multiEdge = new ArrayList<>();
		
			multiEdge.addAll(getHyperedges(p));
			Hyperedge toDistribute = multiEdge.get(0);
			multiEdge.remove(0);
			
			for(Hyperedge h : multiEdge){
				toDistribute = h.compose(toDistribute);
			}
			removeEmptyHyperedge();
		}
		rules.clear();
		
		for(Hyperedge g : hyperedges){
			rules.addAll(g.getLeaves());
		}	
		
		return this;
	}
	
	public void removeEmptyHyperedge(){
		List<Hyperedge> s = new ArrayList<>(hyperedges);
		for(Hyperedge e : s){
			if(e.getLeaves().size()==0){
				e.getRoot().rmHyperedge(e);
				hyperedges.remove(e);
			}
		}
		
	}
	
	public void removeHyperedge(List<Hyperedge> edges){
		hyperedges.removeAll(edges);
		for(Hyperedge e : edges){
			for(RuleNode r : e.getLeaves()){
				r.rmFromHyperedge(e);
			}
		}
		
	}
	
	/**
	 * Hide nodes in hypergraph and distribute assignements
	 * @param p
	 * @return
	 */
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
	
	/**
	 * Get rules for commandification
	 * @return
	 */
	public Set<Rule> getRules(){
		Set<Rule> s = new HashSet<>();		
		rules.clear();
		for(Hyperedge g : hyperedges){
			rules.addAll(g.getLeaves());
		}
		
		for(RuleNode r : rules){
			s.add(r.getRule());
		}
		
		return s;
	}

	
	public String toString(){

		String s = "";
		for(Port var : variables){
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
