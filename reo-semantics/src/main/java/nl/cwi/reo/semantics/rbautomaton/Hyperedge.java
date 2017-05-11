package nl.cwi.reo.semantics.rbautomaton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import nl.cwi.reo.semantics.predicates.Conjunction;
import nl.cwi.reo.semantics.predicates.Disjunction;
import nl.cwi.reo.semantics.predicates.Equality;
import nl.cwi.reo.semantics.predicates.Formula;
import nl.cwi.reo.semantics.predicates.Node;
import nl.cwi.reo.semantics.predicates.Term;

public class Hyperedge {
	private PortNode root;
	private List<RuleNode> leaves;
	
	public Hyperedge(PortNode root, List<RuleNode> leaves){
		root.addHyperedge(this);
		this.root=root;
		for(RuleNode r : leaves)
			r.addHyperedge(this);
		this.leaves=leaves;
		
	}
	
	public PortNode getRoot(){
		return root;
	}
	
	public List<RuleNode> getLeaves(){
		return leaves;
	}
	
	public Hyperedge addLeave(RuleNode r){
		leaves.add(r);
		r.addHyperedge(this);
		return this;
	}
	
	public Hyperedge addLeaves(List<RuleNode> r){
		leaves.addAll(r);
		for(RuleNode rule : r)
			rule.addHyperedge(this);
		return this;
	}
	
	public Hyperedge rmLeaves(List<RuleNode> r){
		leaves.removeAll(r);
		for(RuleNode rule : r)
			rule.rmHyperedge(this);
		return this;
	}
	
	public Hyperedge compose(Formula f){
		for(RuleNode r : leaves){
			r.compose(f);
		}
		return this;
	}

	/*
	 * List<RuleNode> list = h.getLeaves();
	 * if(list.size==1)
	 * 		for(rules in leaves)
	 * 			rules.compose(h.getLeaves().get(0));
	 * if(list.size>1)
	 * 		
	 * for(leaves in hyperedge)
	 */
	public Hyperedge compose(Hyperedge h){
		if(!root.getPort().equals(h.getRoot().getPort())){
			new Exception("Those two hyperedges cannot compose because of two different roots");
		}
		List<RuleNode> list = new ArrayList<RuleNode>();
		List<RuleNode> ruleNodes = new ArrayList<RuleNode>();
		ruleNodes.addAll(h.getLeaves());
		
		if(ruleNodes.size()==1){
			for(RuleNode r : leaves){
				list.add(r.compose(h.getLeaves().get(0)));
			}
			Queue<Hyperedge> l = new LinkedList<>(h.getLeaves().get(0).getHyperedges());
			while(!l.isEmpty()){
				Hyperedge edge = l.poll();
				edge.rmLeaves(ruleNodes);
				edge.addLeaves(list);
			}
			rmLeaves(leaves);
			addLeaves(list);
			h.rmLeaves(ruleNodes);
		}
		else{
			Queue<RuleNode> h_leaves = new LinkedList<RuleNode>(h.getLeaves());
			while(!h_leaves.isEmpty()){
				RuleNode rule=h_leaves.poll();
				for(RuleNode r : leaves){
					list.add(r.compose(rule));
				}
				Queue<Hyperedge> listEdges = new LinkedList<>(rule.getHyperedges());
				while(!listEdges.isEmpty()){
					Hyperedge e = listEdges.poll();
					if(!e.equals(h)){
						e.addLeaves(list);
						rule.addHyperedge(e);
					}
					e.rmLeaves(Arrays.asList(rule));
				}
//			}
//			this.rmLeaves(leaves);
			}
		}

		return this;
	}
	
	public Hyperedge hide(PortNode p){
		for(RuleNode r:leaves){
			r.hide(p);
		}
		return this;
	}
	
	public Formula getAssignement(){
		List<Formula> f = new ArrayList<Formula>();
		
		for(RuleNode r : leaves){
			Formula formula = r.getRule().getFormula();
			List<Formula> literals = new ArrayList<>();
			if(formula instanceof Conjunction)
				literals = ((Conjunction) formula).getClauses();
			if(formula instanceof Equality)
				literals.add(formula);
			for(Formula l : literals){
				if (l instanceof Equality) {
					Equality equality = (Equality) l;
					Term t1 = equality.getLHS();
					Term t2 = equality.getRHS();
		
					if (t1 instanceof Node && !((Node) t1).getPort().isInput() && ((Node) t1).getPort().equals(root.getPort())){
						f.add(l);
					} 
					else if (t2 instanceof Node && !((Node) t2).getPort().isInput() && ((Node) t2).getPort().equals(root.getPort())){
						f.add(l);
					}
		
				}
			}
			
		}

		return new Disjunction(f);
	}
	
	public String toString(){
		String s = "";
		int i = leaves.size();
		for(RuleNode r : leaves){
			s=s+r.toString() + (i>1?" || ":"");
			i--;
		}
		return s;
		
	}
	
}
