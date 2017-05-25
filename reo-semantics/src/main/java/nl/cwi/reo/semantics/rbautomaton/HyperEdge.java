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

public class HyperEdge {
	private PortNode root;
	private Set<RuleNode> leaves;
	
	public HyperEdge(PortNode root, Set<RuleNode> leaves){
		root.addHyperedge(this);
		this.root=root;
		this.leaves = new HashSet<RuleNode>();
		for(RuleNode r : leaves)
			r.addToHyperedge(this);
		
	}
	
	public PortNode getRoot(){
		return root;
	}
	
	public Set<RuleNode> getLeaves(){
		return leaves;
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
	
	public HyperEdge addLeave(RuleNode r){
		leaves=new HashSet<>(leaves);
		leaves.add(r);
		return this;
	}
	
	public HyperEdge addLeaves(List<RuleNode> r){
		leaves=new HashSet<>(leaves);
		leaves.addAll(r);
		return this;
	}
	
	public HyperEdge rmLeaves(Set<RuleNode> r){
		for(RuleNode rule : r)
			rule.rmFromHyperedge(this);
		return this;
	}
	
	public HyperEdge rmLeave(RuleNode r){
		leaves=new HashSet<>(leaves);
		leaves.remove(r);
		return this;
	}
	
	public HyperEdge compose(Formula f){
		leaves=new HashSet<>(leaves);
		for(RuleNode r : leaves){
			r.compose(f);
		}
		return this;
	}
	
	public HyperEdge duplicate(){
		return new HyperEdge(this.root,this.leaves);
	}

	public HyperEdge compose(HyperEdge h){ 
		if(!root.getPort().equals(h.getRoot().getPort())){
			new Exception("Those two hyperedges cannot compose because of two different roots");
		}
		Set<RuleNode> list = new HashSet<RuleNode>();
		Set<RuleNode> ruleNodes = new HashSet<RuleNode>();
		ruleNodes.addAll(h.getLeaves());
		list.addAll(leaves);
		
		if(ruleNodes.size()==1){
			//Single rule to compose
			RuleNode h_ruleNode = h.getLeaves().iterator().next();
			h_ruleNode.rmFromHyperedge(h);
			//Compose single rule
			Queue<RuleNode> rulesToCompose = new LinkedList<RuleNode>(list);

			while(!rulesToCompose.isEmpty()){
				RuleNode r = rulesToCompose.poll();
				if(r.compose(h_ruleNode)!=null)
					r.erase();
			}
			h_ruleNode.erase();
		}
		
		else{
			Queue<RuleNode> rulesToCompose = new LinkedList<RuleNode>(h.getLeaves());
			while(!rulesToCompose.isEmpty()){
				RuleNode r1 = rulesToCompose.poll();
				for(RuleNode r2 : list){
					r1.compose(r2);
				}
				r1.erase();
			}
			for(RuleNode r:list)
				r.erase();
		}
		

		return this;
	}
	
	public HyperEdge hide(PortNode p){
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
		if (!(other instanceof HyperEdge))
			return false;
		
		HyperEdge p = (HyperEdge) other;
		
		return Objects.equals(root.getPort(),p.getRoot().getPort()) && Objects.equals(leaves,p.getLeaves());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {

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
