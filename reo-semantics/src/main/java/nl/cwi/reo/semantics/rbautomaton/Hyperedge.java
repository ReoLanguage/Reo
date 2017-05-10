package nl.cwi.reo.semantics.rbautomaton;

import java.util.ArrayList;
import java.util.List;

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
	
	public Hyperedge compose(Formula f){
		for(RuleNode r : leaves){
			r.compose(f);
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
