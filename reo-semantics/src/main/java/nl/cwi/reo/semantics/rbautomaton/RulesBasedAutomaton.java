package nl.cwi.reo.semantics.rbautomaton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.ports.PortType;
import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.semantics.SemanticsType;
import nl.cwi.reo.semantics.predicates.Conjunction;
import nl.cwi.reo.semantics.predicates.Equality;
import nl.cwi.reo.semantics.predicates.Existential;
import nl.cwi.reo.semantics.predicates.Formula;
import nl.cwi.reo.semantics.predicates.Function;
import nl.cwi.reo.semantics.predicates.Negation;
import nl.cwi.reo.semantics.predicates.Node;
import nl.cwi.reo.semantics.predicates.Predicate;
import nl.cwi.reo.semantics.predicates.Term;
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
		return s;
	}
	
	public Set<Rule> getNewRules() {
		Set<Rule> setRules = new HashSet<Rule>();
		Graph g = new Graph(s).isolate();
		for(GraphNode n : g.getNodes()){
			setRules.add(n.getRule());
		}
		return setRules;
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
		
		Set<Port> ports = new HashSet<Port>(node);

		Set<Port> inps = new HashSet<Port>();
		Set<Port> outs = new HashSet<Port>();

		for (Port p : ports) {
			if (p.getType() == PortType.IN) {
				inps.add(p);
			} else {
				outs.add(p);
			}
		}

		Set<Rule> rules = new HashSet<Rule>();
		/*
		 * Instantiate merger/relicator
		 */
		for (Port p : inps) {
			Formula transition = null;
			Map<Port,Role> map = null;
			for (Port x : inps) {
				if (!x.equals(p)) {
					Term t_null = new Function("constant", null, new ArrayList<Term>());
					Formula neq = new Negation(new Equality(new Node(x), t_null));
					if (transition == null)
						transition = neq;
					else
						transition = new Conjunction((Arrays.asList(transition, neq)));
				}
			}
			for (Port x : outs) {
				Formula eq = new Equality(new Node(p), new Node(x));
				if (transition == null)
					transition = eq;
				else
					transition = new Conjunction(Arrays.asList(transition, eq));
			}
			rules.add(new Rule(map,transition));
		}

		return new RulesBasedAutomaton(rules);
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
