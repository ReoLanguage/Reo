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
		this.hyperedges = new HashSet<>();
		for(Hyperedge h : hyperedge)
			addToHyperedge(h);
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
	
	public void addToHyperedge(Hyperedge h){
		hyperedges.add(h);
		h.addLeave(this);
	}
	
	public void rmFromHyperedge(Hyperedge h){
		hyperedges.remove(h);
		h.rmLeave(this);
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
					if(v instanceof Node && rule.getSync().get(((Node)v).getPort())!=null && !rule.getSync().get(((Node)v).getPort())){
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
		if(!rule.canSync(r.getRule())){
			return null;
		}
			
		rule.getSync().putAll(r.getRule().getSync());
		if(rule.getFormula().equals(r.getRule().getFormula()))
			rule = new Rule(rule.getSync(),rule.getFormula());
		else
			rule = new Rule(rule.getSync(),new Conjunction(Arrays.asList(rule.getFormula(),r.getRule().getFormula())));			
		
		Set<Hyperedge> set = new HashSet<>(r.getHyperedges());
		for(Hyperedge h : set){
			addToHyperedge(h);
		}
		return this;
	}
	
	public void erase(){
		Set<Hyperedge> s = new HashSet<>(hyperedges);
		for(Hyperedge h : s){
			rmFromHyperedge(h);
		}
	}
	
	public void isolate(){
		Set<Hyperedge> s = new HashSet<>(hyperedges);
		for(Hyperedge h : s){
			h.rmLeave(this);
		}
		hyperedges=s;
	}
	
	public RuleNode duplicate(){
		return new RuleNode(this.getRule(),this.getHyperedges());
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
		return Objects.equals(this.getRule(),rule.getRule());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.getRule().hashCode());
	}
	
	public String toString(){
		String s = "("+rule.toString()+")";

		return s;
	}
}
