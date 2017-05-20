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
	private List<RuleNode> leaves;
	
	public Hyperedge(PortNode root, List<RuleNode> leaves){
		root.addHyperedge(this);
		this.root=root;
		this.leaves = new ArrayList<RuleNode>();
		for(RuleNode r : leaves)
			r.addToHyperedge(this);
		
	}
	
	public PortNode getRoot(){
		return root;
	}
	
	public List<RuleNode> getLeaves(){
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
	
	public Hyperedge addLeave(RuleNode r){
		leaves.add(r);
		return this;
	}
	
	public Hyperedge addLeaves(List<RuleNode> r){
		leaves.addAll(r);
		return this;
	}
	
	public Hyperedge rmLeaves(Set<RuleNode> r){
		for(RuleNode rule : r)
			rule.rmFromHyperedge(this);
		return this;
	}
	
	public Hyperedge rmLeave(RuleNode r){
		leaves.remove(r);
		return this;
	}
	
	public Hyperedge compose(Formula f){
		for(RuleNode r : leaves){
			r.compose(f);
		}
		return this;
	}
	
	public Hyperedge duplicate(){
		return new Hyperedge(this.root,this.leaves);
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
			RuleNode toCompose = new RuleNode(h_ruleNode.getRule(),h_ruleNode.getHyperedges());
			toCompose.rmFromHyperedge(h);
			//Compose single rule
			for(RuleNode r : list){
				if(r.compose(toCompose)==null){
					r.erase();
					for(RuleNode rule : leaves){
						if(rule.equals(r)){
							for(Port p : toCompose.getRule().getSync().keySet())
								if(toCompose.getRule().getSync().get(p))
									rule.getRule().getSync().put(p, false);
						}
					}
				}
			}
			toCompose.erase();
			h_ruleNode.erase();
		}
		else{
			
			//List of rules to compose
			Queue<RuleNode> rulesToCompose = new LinkedList<RuleNode>(h.getLeaves());
			while(!rulesToCompose.isEmpty()){
				RuleNode h_ruleNode = rulesToCompose.poll();
				//Duplicate rule
				RuleNode toCompose = new RuleNode(h_ruleNode.getRule(),h_ruleNode.getHyperedges());
				toCompose.rmFromHyperedge(h);
				//Compose single rule
				for(RuleNode r : list){
					
					if(!r.getRule().equals(toCompose.getRule())){
						RuleNode r2= new RuleNode(new Rule(new HashMap<Port,Boolean>(r.getRule().getSync()),r.getRule().getFormula()),r.getHyperedges());
						if(r2.compose(toCompose)==null){
							r2.erase();
							for(RuleNode rule : leaves){
								if(rule.equals(r)){
									for(Port p : toCompose.getRule().getSync().keySet())
										if(toCompose.getRule().getSync().get(p))
											rule.getRule().getSync().put(p, false);
								}
							}
						}
					}
					else{
						RuleNode r2=new RuleNode(new Rule(new HashMap<Port,Boolean>(r.getRule().getSync()),r.getRule().getFormula()),r.getHyperedges());
						r2.rmFromHyperedge(h);
						r2.rmFromHyperedge(this);
						r2.compose(toCompose);
					}					
				}
				toCompose.erase();
				h_ruleNode.erase();
			}
			for(RuleNode r:list)
				r.erase();
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
		int hashcode =0;
		for(RuleNode r : leaves)
			hashcode=0+r.hashCode();
		return Objects.hash(this.root.getPort(),hashcode);
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
