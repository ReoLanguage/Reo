package nl.cwi.reo.semantics.rbautomaton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.predicates.Conjunction;
import nl.cwi.reo.semantics.predicates.Disjunction;
import nl.cwi.reo.semantics.predicates.Equality;
import nl.cwi.reo.semantics.predicates.Formula;
import nl.cwi.reo.semantics.predicates.Node;
import nl.cwi.reo.semantics.predicates.Term;

public class Hyperedge {
	private PortNode root;
	private HashSet<RuleNode> leaves;
	
	public Hyperedge(PortNode root, HashSet<RuleNode> leaves){
		root.addHyperedge(this);
		this.root=root;
		this.leaves=leaves;
		for(RuleNode r : leaves)
			r.addHyperedge(this);
		
	}
	
	public PortNode getRoot(){
		return root;
	}
	
	public List<RuleNode> getLeaves(){
		return new ArrayList<>(leaves);
	}

	public Rule getRule(){
		List<Formula> list = new ArrayList<Formula>();
		Map<Port,Boolean> map = new HashMap<>();
		for(RuleNode r : leaves){
			list.add(r.getRule().getFormula());
			map.putAll(r.getRule().getSync());
		}
		return new Rule(map,new Disjunction(list));
	}
	
	public Hyperedge addLeave(RuleNode r){
		//Update hashcodes :
		HashSet<RuleNode> s = new HashSet<RuleNode>(leaves);
		leaves.clear();
		leaves = s;
		
		leaves.add(r);
		r.addHyperedge(this);
		return this;
	}
	
	public Hyperedge addLeaves(List<RuleNode> r){
		//Update hashcodes :
		HashSet<RuleNode> s = new HashSet<RuleNode>(leaves);
		leaves.clear();
		leaves = s;
		
		leaves.addAll(r);
		for(RuleNode rule : r)
			rule.addHyperedge(this);
		return this;
	}
	
	public Hyperedge rmLeaves(Set<RuleNode> r){
		//Update hashcodes :
		HashSet<RuleNode> s = new HashSet<RuleNode>(leaves);
		leaves.clear();
		leaves = s;
		
		leaves.removeAll(r);
		for(RuleNode rule : r)
			rule.rmHyperedge(this);
		return this;
	}
	
	public Hyperedge rmLeave(RuleNode r){
		//Update hashcodes :
		HashSet<RuleNode> s = new HashSet<RuleNode>(leaves);
		leaves.clear();
		leaves = s;
		
		leaves.remove(r);
		r.rmHyperedge(this);
		return this;
	}
	
	public Hyperedge compose(Formula f){
		for(RuleNode r : leaves){
			r.compose(f);
		}
		return this;
	}

	public Hyperedge compose(Hyperedge h){
		if(!root.getPort().equals(h.getRoot().getPort())){
			new Exception("Those two hyperedges cannot compose because of two different roots");
		}
		Set<RuleNode> list = new HashSet<RuleNode>();
		Set<RuleNode> ruleNodes = new HashSet<RuleNode>();
		ruleNodes.addAll(h.getLeaves());
		list.addAll(leaves);
		
		if(ruleNodes.size()==1){
			//Single rule to compose
			RuleNode h_ruleNode = h.getLeaves().get(0);
			//Add rules to other hyperedges from the single rule to compose and remove the single rule
			for(Hyperedge edge : h_ruleNode.getHyperedges()){
				if(!edge.getRoot().equals(getRoot())){
					edge.rmLeaves(ruleNodes);
					edge.addLeaves(getLeaves());
				}
			}
			//Compose single rule
			for(RuleNode r : leaves){
				r.compose(h.getLeaves().get(0));
			}
		}
		else{
			
			List<RuleNode> newRules = new ArrayList<>();
			
			for(RuleNode ruleToCompose : list){
				//Compose formula and add union of hyperedges to rule
				newRules = ruleToCompose.compose(h.getLeaves());
				//Remove previous rule of "this" hyperedge
				Queue<Hyperedge> hyperedge = new LinkedList<Hyperedge>(ruleToCompose.getHyperedges());
				while(!hyperedge.isEmpty()){
					Hyperedge edge = hyperedge.poll();
					edge.rmLeave(ruleToCompose);
					edge.addLeaves(newRules);
				}
			}
			
			//Remove rules of "h" hyperedge
			for(RuleNode r : ruleNodes){
				for(Hyperedge edge : r.getHyperedges()){
					if(!edge.getRoot().equals(getRoot())){
						edge.rmLeaves(ruleNodes);
//						edge.addLeaves(newRules);
					}
				}
			}
			
//			Queue<RuleNode> h_leaves = new LinkedList<RuleNode>(h.getLeaves());
//			while(!h_leaves.isEmpty()){
//				RuleNode rule=h_leaves.poll();
//				for(RuleNode r : leaves){
//					list.add(r.compose(rule));
//				}
//				Queue<Hyperedge> listEdges = new LinkedList<>(rule.getHyperedges());
//				while(!listEdges.isEmpty()){
//					Hyperedge e = listEdges.poll();
//					if(!e.equals(h)){
//						e.addLeaves(list);
//						rule.addHyperedge(e);
//					}
//					Set<RuleNode> s = new HashSet<RuleNode>();
//					s.add(rule);
//					e.rmLeaves(s);
//				}
//			}
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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(@Nullable Object other) {
		if (other == null)
			return false;
		if (other == this)
			return true;
		if (!(other instanceof Hyperedge))
			return false;
		
		Hyperedge p = (Hyperedge) other;
		for(RuleNode r : p.getLeaves()){
			boolean b = false;
			for(RuleNode r2 : leaves){
				if(r2.equals(r)){
					b=true;
				}
			}
			if(!b)
				return false;
		}
		return root.equals(p.getRoot()) &&leaves.size()==p.getLeaves().size();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
//		int hash=0;
//		if(leaves!=null){
//			for(RuleNode r : leaves){
//				hash=hash+r.hashCode();
//			}
//		}
		return Objects.hash(this.root.getPort(),leaves);
	}
	
	public String toString(){
		String s = "";
		int i = leaves.size();
		for(RuleNode r : leaves){
			s=s+r.toString() + (i>1?" || \n ":"");
			i--;
		}
		return s;
		
	}
	
}
