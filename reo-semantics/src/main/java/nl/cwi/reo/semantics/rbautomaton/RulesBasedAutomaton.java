package nl.cwi.reo.semantics.rbautomaton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.semantics.SemanticsType;
import nl.cwi.reo.semantics.predicates.Conjunction;
import nl.cwi.reo.semantics.predicates.Existential;
import nl.cwi.reo.semantics.predicates.Formula;
import nl.cwi.reo.semantics.predicates.Node;
import nl.cwi.reo.util.Monitor;

public class RulesBasedAutomaton implements Semantics<RulesBasedAutomaton> {

	private final Set<Rule> s;

	/**
	 * Constructs an automaton, with an empty set of rules.
	 */
	public RulesBasedAutomaton() {
		this.s = new HashSet<Rule>();
	}

	/**
	 * Constructs a new automaton from a given set of rules.
	 * 
	 * @param f
	 *            formula
	 */
	public RulesBasedAutomaton(Set<Rule> s) {
		this.s = s;
	}

	/**
	 * Gets the rules of this automaton.
	 * 
	 * @return formula
	 */
	public Set<Rule> getRules() {
		Set<Rule> setRules = new HashSet<Rule>();
		Map<Port,Role> map = new HashMap<Port,Role>();
		List<Formula> listF = new ArrayList<Formula>();
		
		int i=0;
		for(Rule rule : s){
			i++;
			int j=0;
			Rule tmp = rule;
			for(Rule r : s){
				if(i!=j && rule.hasEdge(r)){
					map.putAll(r.getSync());
					listF.add(r.getFormula());
				}
			}
			setRules.add(new Rule(map,new Conjunction(listF)));
		}
		return s;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public @Nullable RulesBasedAutomaton evaluate(Scope s, Monitor m) {
		Set<Rule> setRules = new HashSet<Rule>();
		for(Rule r : this.s){
			setRules.add(r.evaluate(s,m));
		}
		return new RulesBasedAutomaton(setRules);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Port> getInterface() {
		Set<Port> p = new HashSet<Port>();
		for(Rule r : s){
			p.addAll(r.getFiringPorts());
		}
		return p;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SemanticsType getType() {
		return SemanticsType.RBA;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RulesBasedAutomaton getNode(Set<Port> node) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RulesBasedAutomaton rename(Map<Port, Port> links) {
		Set<Rule> setRules = new HashSet<Rule>();
		for(Rule r : s){
			setRules.add(r.rename(links));
		}
		return new RulesBasedAutomaton(setRules);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RulesBasedAutomaton compose(List<RulesBasedAutomaton> components) {
		Set<Rule> s = new HashSet<Rule>();
		for (RulesBasedAutomaton A : components)
			s.addAll(A.getRules());
		return new RulesBasedAutomaton(s);
	}

	public String toString(){
		String s = "[";
		for(Rule r : this.s){
			s=s+r.toString();
		}
		s=s+"]";
		return s;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public RulesBasedAutomaton restrict(Collection<? extends Port> intface) {
		Set<Rule> setRules = new HashSet<Rule>();
		for (Rule r : s){
			Formula g = r.getFormula();
			for(Port p : r.getFiringPorts())
				if (!intface.contains(p))
					g = new Existential(new Node(p), g);
			setRules.add(new Rule(r.getSync(),g));
		}
		return new RulesBasedAutomaton(setRules);
	}

}
