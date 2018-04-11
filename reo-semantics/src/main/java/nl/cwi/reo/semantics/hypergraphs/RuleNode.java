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
import nl.cwi.reo.semantics.predicates.Formula;
import nl.cwi.reo.semantics.predicates.Formulas;
import nl.cwi.reo.semantics.predicates.MemoryVariable;
import nl.cwi.reo.semantics.predicates.PortVariable;
import nl.cwi.reo.semantics.predicates.Variable;
import nl.cwi.reo.semantics.rulebasedautomata.Rule;
import nl.cwi.reo.util.Monitor;

// TODO: Auto-generated Javadoc
/**
 * Constraint hypergraph node that represent a rule.
 */
public class RuleNode {

	/**
	 * Current total number of RuleNodes. Used to provide each node with a
	 * unique identifier that avoids repeated computation of the hash value of
	 * the formula of this rule.
	 */
	static int N = 0;

	/**
	 * Unique identifier of this node.
	 */
	private int id;

	/**
	 * Set of hyperedges that have this rule in their target.
	 */
	private Set<HyperEdge> hyperedges;

	/**
	 * List of rules that are excluded (i.e., cannot fire) whenever this rule
	 * fires.
	 */
	private List<RuleNode> exclusiveRules = new ArrayList<>();

	/**
	 * Rule of this node.
	 */
	private Set<Rule> rules;

	/**
	 * Synchronous set.
	 */
	private final Map<Port, Boolean> sync;

	
	/**
	 * Constructs a new node from a given rule and a set of adjacent hyperedges.
	 * 
	 * @param r
	 *            rule of this node
	 * @param hyperedge
	 *            set of adjacent hyperedges
	 */
	public RuleNode(Set<Rule> r, Set<HyperEdge> hyperedge) {
		this.rules = r;
		this.hyperedges = new HashSet<>();
		this.sync = new HashMap<>();

		for(Rule rule : r)
			sync.putAll(rule.getSync());
		
		id = ++N;
		for (HyperEdge h : hyperedge)
			addToHyperedge(h); // TODO cannot call this.addToHyperedge, because
								// this is not fully initialized.
	}

	/**
	 * Constructs a new node from a given rule with an empty set of adjacent
	 * hyperedges.
	 * 
	 * @param r
	 *            rule of this node
	 */
	public RuleNode(Set<Rule> r) {
		this.rules = r;
		this.hyperedges = new HashSet<HyperEdge>();
		this.sync = new HashMap<>();
		for(Rule rule : r)
			sync.putAll(rule.getSync());
		
		id = ++N;
	}

	/**
	 * Gets the set of rules of this node.
	 * 
	 * @return rule of this node.
	 */
	public Set<Rule> getRules() {
		return rules;
	}

	/**
	 * Gets the rule of this node.
	 * 
	 * @return rule of this node.
	 */
	public Rule getRule() {
		List<Formula> f = new ArrayList<>();
		for(Rule r : rules)
			f.add(r.getFormula());
		return new Rule(new Conjunction(f));
	}
	
	/**
	 * Gets the rule of this node.
	 * 
	 * @return rule of this node.
	 */
	public Map<Port,Boolean> getSync() {
		return sync;
	}

	/**
	 * Gets the set of hyperedges adjacent to this rule.
	 * 
	 * @return set of adjacent hyperedges.
	 */
	public Set<HyperEdge> getHyperedges() {
		return hyperedges;
	}

	/**
	 * Gets an arbitrary hyperedges adjacent to this rule and a given port.
	 * 
	 * @param p
	 *            port
	 * @return an arbitrary hyperedge adjacent to this rule and port p, if it
	 *         exists, and null otherwise.
	 */
	@Nullable
	public HyperEdge getHyperedges(Port p) {
		for (HyperEdge h : hyperedges)
			if (h.getSource().equals(p))
				return h;
		return null;
	}

	/**
	 * Adds a hyperedge to the set of hyperedges adjacent to this rule.
	 * 
	 * @param h
	 *            hyperedge
	 */
	public void addToHyperedge(HyperEdge h) {
		hyperedges.add(h);
		h.addLeave(this);
	}

	/**
	 * Removes a hyperedge from the set of hyperedges adjacent to this rule.
	 * 
	 * @param h
	 *            hyperedge
	 */
	public void rmFromHyperedge(HyperEdge h) {
		hyperedges.remove(h);
		h.rmLeave(this);
	}

	/**
	 * Gets the list of rules that are excluded by this rule, i.e., rules that
	 * cannot fire when this rule fires.
	 * 
	 * @return list of rules that are excluded by this rule.
	 */
	public List<RuleNode> getExclRules() {
		return exclusiveRules;
	}

	/**
	 * Add a node to the list of rules excluded by this rule.
	 * 
	 * @param n
	 *            rule node
	 */
	public void addExclRules(RuleNode n) {
		exclusiveRules.add(n);
	}



	/**
	 * Joins this node with a given rule node by conjunction of the rules and
	 * union of the adjacent hyperedges.
	 * 
	 * @param r
	 *            rule node
	 * @return conjunction this node with the given rule node, if the rules of
	 *         both node can synchronize, or null otherwise.
	 */
	
	public RuleNode composeF(RuleNode r) {
		// If the two rules can not synchronize, the composition fails.
		if (!canSync(sync, r.getSync()))
			return null;

		Map<Port, Boolean> map = new HashMap<>(getSync());
		map.putAll(r.getSync());

		Set<Rule> r1;
		// If the two rules are equals, return one this.rule (idempotency)
		// otherwise, return the conjunction.
		if (rules.equals(r.getRules())) {
			if (r.getSync().equals(getSync()))
				return this;
			else
				r1 = new HashSet<>(rules);
		} else {
			r1 = new HashSet<>(rules);
			r1.addAll(r.getRules());
		}

		// Add the new rule to the hyperegde.
		Set<HyperEdge> set = new HashSet<>(hyperedges);
		for (HyperEdge h : r.getHyperedges()) {
			if (!h.getTarget().isEmpty())
				set.add(h);
		}

		return new RuleNode(r1, set);
	}

	/**
	 * Removes this node from every adjacent hyperedge, and clears the set of
	 * hyperedges of this node.
	 */
	public void erase() {
		// TODO This creation of a new HashSet can be expensive.
		Set<HyperEdge> s = new HashSet<>(hyperedges);
		for (HyperEdge h : s) {
			rmFromHyperedge(h);
		}
	}

	/**
	 * Removes this node from every adjacent hyperedge.
	 */
	public void isolate() {
		// TODO This creation of a new HashSet can be expensive.
		Set<HyperEdge> s = new HashSet<>(hyperedges);
		for (HyperEdge h : s) {
			h.rmLeave(this);
		}
		hyperedges = s;
	}

	/**
	 * Returns a new instance of a rule node with the same rule and hyperedges.
	 * 
	 * @return new instance of the this node.
	 */
	public RuleNode duplicate() {
		return new RuleNode(this.getRules(), this.getHyperedges());
	}

	/**
	 * Renames the ports in this rule node.
	 * 
	 * @param links
	 *            map that assigns a new port to old ports.
	 * @return node with rule whose port variables are renamed.
	 */
	public RuleNode rename(Map<Port, Port> links) {
		Set<Rule> _rules = new HashSet<>();
		for(Rule r : rules)
			_rules.add(r.rename(links));
		return this;
	}

	/**
	 * Renames the memory cells in the rule of this node.
	 * 
	 * @param rename
	 *            map that assigns a new name to each old name
	 * @return reference to this node.
	 */
	public RuleNode substitute(Map<String, String> rename) {
		Set<Rule> _rules = new HashSet<>();
		for (Map.Entry<String, String> entry : rename.entrySet()) {
			for(Rule r : rules){
				Rule _r = new Rule(r.getFormula().substitute(
						new MemoryVariable(entry.getValue(), false), new MemoryVariable(entry.getKey(), false)));
				_r = new Rule(_r.getFormula().substitute(
						new MemoryVariable(entry.getValue(), true), new MemoryVariable(entry.getKey(), true)));
				_rules.add(_r);
			}
			rules = new HashSet<>(_rules);
			_rules.clear();
		}
		return this;
	}

	public boolean canSync(Map<Port,Boolean> r1, Map<Port,Boolean> r2) {

		boolean hasEdge = false;
		for (Port p : r1.keySet()) {
			if (r1.get(p)) {
				if (r2.get(p)!=null && r2.get(p)) {
					hasEdge = true;
				} else if(r2.get(p)!=null && !r2.get(p)){ 
					return false;
				}
			}
			else if(r2!=null && r2.get(p)!=null &&r2.get(p))
				return false;
		}
		return hasEdge;
}
	
	
	/**
	 * Hides a port from this rule via existential quantification.
	 * 
	 * @param p
	 *            port node
	 * @return reference to this rule node.
	 */
	public RuleNode hide(Port p) {
		List<Variable> V = Arrays.asList(new PortVariable(p));
		Formula f = this.getRule().getFormula();
		rules = new HashSet<>(Arrays.asList(new Rule(Formulas.eliminate(f, V))));
		return this;
	}

	/**
	 * Evaluates the rule of this node.
	 * 
	 * @param s
	 *            scope
	 * @param m
	 *            monitor
	 */
	public void evaluate(Scope s, Monitor m) {
		Set<Rule>_rules = new HashSet<>();
		for(Rule r : rules)
			_rules.add(r.evaluate(s, m));
		rules = _rules;
	}
	
	public Set<Port> getActivePorts() {
		Set<Port> N = new HashSet<>();
		for (Rule r : rules)
			N.addAll(r.getActivePorts());
		return N;
	}

	
	public Set<Port> getPorts() {
		Set<Port> N = new HashSet<>();
		for (Rule r : rules)
			for(Variable v :r.getFormula().getFreeVariables())
				if(v instanceof PortVariable)
					N.add(((PortVariable) v).getPort());
		return N;
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
		return Objects.equals(this.id, rule.id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return rules.toString();
	}
}
