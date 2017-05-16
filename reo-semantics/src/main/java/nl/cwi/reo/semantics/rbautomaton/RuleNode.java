package nl.cwi.reo.semantics.rbautomaton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.predicates.Conjunction;
import nl.cwi.reo.semantics.predicates.Disjunction;
import nl.cwi.reo.semantics.predicates.Existential;
import nl.cwi.reo.semantics.predicates.Formula;
import nl.cwi.reo.semantics.predicates.Node;
import nl.cwi.reo.semantics.predicates.Variable;

public class RuleNode implements HypergraphNode{
	private Set<Hyperedge> hyperedges;
	private List<RuleNode> exclusiveRules = new ArrayList<>();
	private Rule rule;
	
	private boolean visited;

	public RuleNode(Rule r, Set<Hyperedge> hyperedge) {
		this.rule = r;
		this.hyperedges=hyperedge;
		visited = false;
	}
	
	public RuleNode(Rule r){
		this.rule = r;
		this.hyperedges = new HashSet<Hyperedge>();
		visited = false;		
	}
	
	public Rule getRule(){
		return rule;
	}
	
	
	public Set<Hyperedge> getHyperedges(){
		return hyperedges;
	}
	
	public Hyperedge getHyperedges(PortNode p){
		for(Hyperedge h : hyperedges)
			if(h.getRoot().getPort().equals(p.getPort()))
				return h;
		return null;
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

	public List<RuleNode> getExclRules(){
		return exclusiveRules;
	}
	
	public void addExclRules(RuleNode n){
		exclusiveRules.add(n);
	}
	
	@Override
	public void setFlag(boolean flag) {
		this.visited = flag;
	}
	
	public RuleNode compose(Formula f){
		boolean canSync = true;
		List<Formula> clauses = new ArrayList<Formula>();
		if(f instanceof Disjunction){
			for(Formula clause : ((Disjunction) f).getClauses()){
				for(Variable v : clause.getFreeVariables()){
					if(v instanceof Node && !rule.getSync().get((((Node)v).getPort()))){
						canSync=false;
					}
				}
				if(canSync){
					clauses.add(clause);
				}
			}
		}
		Formula formula;
		if(clauses.size()==1)
			formula = new Conjunction(Arrays.asList(rule.getFormula(),clauses.get(0)));
		else
			formula = new Conjunction(Arrays.asList(rule.getFormula(),new Disjunction(clauses)));
		rule=new Rule(rule.getSync(),formula);
		return this;
	}
	
	public RuleNode compose(RuleNode r){		
		rule.getSync().putAll(r.getRule().getSync());
		rule = new Rule(rule.getSync(),new Conjunction(Arrays.asList(rule.getFormula(),r.getRule().getFormula())));
		return this;
	}
	
	public List<RuleNode> compose(List<RuleNode> rules){	
		List<RuleNode> listNewRuleNodes = new ArrayList<>();
		for(RuleNode r : rules){
			Map<Port,Boolean> map = new HashMap<Port,Boolean>();
			map.putAll(rule.getSync());
			map.putAll(r.getRule().getSync());
			Rule newRule = new Rule(map,new Conjunction(Arrays.asList(rule.getFormula(),r.getRule().getFormula())));
			
			RuleNode newRuleNode = new RuleNode(newRule,new HashSet<>(hyperedges));
			for(Hyperedge h : r.getHyperedges()){
				if(getHyperedges(h.getRoot())==null){
					newRuleNode.getHyperedges().add(h);
					h.addLeave(newRuleNode);
				}
			}
			
			listNewRuleNodes.add(newRuleNode);
		}
		
		return listNewRuleNodes;
	}
	
	
	public RuleNode hide(PortNode p){
		rule = new Rule(rule.getSync(),(new Existential(new Node(p.getPort()),rule.getFormula())).QE());
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
		if (!(other instanceof RuleNode))
			return false;
		RuleNode rule = (RuleNode) other;
		return Objects.equals(this.rule.getFormula(),(rule.getRule().getFormula()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		Formula f = this.getRule().getFormula();
		
		return Objects.hash(f);
	}
	
	//1975929195
	public String toString(){
		String s = "("+rule.toString()+")";
//		s=s+"excl rules : (";
//		int i =0;
//		for(RuleNode r : exclusiveRules){
//			s=s+(i!=0?",":"")+r.getRule().toString()+"\n";
//			i++;
//		}
		return s;
	}
}
