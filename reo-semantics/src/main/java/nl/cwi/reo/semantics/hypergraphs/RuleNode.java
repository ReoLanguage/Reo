package nl.cwi.reo.semantics.hypergraphs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.predicates.Conjunction;
import nl.cwi.reo.semantics.predicates.Disjunction;
import nl.cwi.reo.semantics.predicates.Existential;
import nl.cwi.reo.semantics.predicates.Formula;
import nl.cwi.reo.semantics.predicates.MemCell;
import nl.cwi.reo.semantics.predicates.Node;
import nl.cwi.reo.semantics.predicates.Variable;
import nl.cwi.reo.util.Monitor;

public class RuleNode {
	
	static int incId = 0;
	private Set<HyperEdge> hyperedges;
	private List<RuleNode> exclusiveRules = new ArrayList<>();
	private Rule rule;
	private int id;

	public RuleNode(Rule r, Set<HyperEdge> hyperedge) {
		this.rule = r;
		this.hyperedges = new HashSet<>();
		for(HyperEdge h : hyperedge)
			addToHyperedge(h);
		incId++;
		id=incId;
	}
	
	public RuleNode(Rule r){
		this.rule = r;
		this.hyperedges = new HashSet<HyperEdge>();
		incId++;
		id=incId;
	}
	
	public Rule getRule(){
		return rule;
	}
	
	public Set<HyperEdge> getHyperedges(){
		return hyperedges;
	}
	
	public HyperEdge getHyperedges(PortNode p){
		for(HyperEdge h : hyperedges)
			if(h.getRoot().getPort().equals(p.getPort()))
				return h;
		return null;
	}
	
	public void addToHyperedge(HyperEdge h){
		hyperedges.add(h);
		h.addLeave(this);
	}
	
	public void rmFromHyperedge(HyperEdge h){
		hyperedges.remove(h);
		h.rmLeave(this);
	}
	
	public List<RuleNode> getExclRules(){
		return exclusiveRules;
	}
	
	public void addExclRules(RuleNode n){
		exclusiveRules.add(n);
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
		Map<Port,Boolean> map = new HashMap<>(rule.getSync());
		map.putAll(r.getRule().getSync());

		Rule r1;
		if(rule.getFormula().equals(r.getRule().getFormula())){
			if(r.getRule().getSync().equals(rule.getSync()))
				return this;
			else
				r1 = new Rule(map,rule.getFormula());
		}
		else{
			r1 = new Rule(map,new Conjunction(Arrays.asList(rule.getFormula(),r.getRule().getFormula())));
		}
		
		Set<HyperEdge> set = new HashSet<>(hyperedges);
		for(HyperEdge h : r.getHyperedges()){
			if(!h.getLeaves().isEmpty())
				set.add(h);
		}
		
		return new RuleNode(r1,set);
	}
	
	public void erase(){
		Set<HyperEdge> s = new HashSet<>(hyperedges);
		for(HyperEdge h : s){
			rmFromHyperedge(h);
		}
	}
	
	public void isolate(){
		Set<HyperEdge> s = new HashSet<>(hyperedges);
		for(HyperEdge h : s){
			h.rmLeave(this);
		}
		hyperedges=s;
	}
	
	public RuleNode duplicate(){
		return new RuleNode(this.getRule(),this.getHyperedges());
	}
	
	public RuleNode rename(Map<Port,Port> links){
		rule=rule.rename(links);
		return this;
	}
	
	public RuleNode substitute(Map<String,String> rename){
		for (Map.Entry<String, String> entry : rename.entrySet()) {
			rule = new Rule(rule.getSync(),rule.getFormula().Substitute(new MemCell(entry.getValue(), false), new MemCell(entry.getKey(), false)));
			rule = new Rule(rule.getSync(),rule.getFormula().Substitute(new MemCell(entry.getValue(), true), new MemCell(entry.getKey(), true)));
		}
		return this;
	}
	
	public RuleNode hide(PortNode p){
		rule = new Rule(rule.getSync(),(new Existential(new Node(p.getPort()),rule.getFormula())).QE());
		return this;
	}
	
	public RuleNode evaluate(Scope s, Monitor m) {
		rule= rule.evaluate(s, m);
		return this;
	}
	
	public int getId(){
		return id;
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
		return Objects.equals(id,rule.getId());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	
	public String toString(){
		String s = "("+rule.toString()+")";

		return s;
	}
}
