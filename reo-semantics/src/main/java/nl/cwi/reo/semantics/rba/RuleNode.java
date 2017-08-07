package nl.cwi.reo.semantics.rba;

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
import nl.cwi.reo.semantics.predicates.MemoryVariable;
import nl.cwi.reo.semantics.predicates.PortVariable;
import nl.cwi.reo.semantics.predicates.Variable;
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
	private Rule rule;

	/**
	 * Constructs a new node from a given rule and a set of adjacent hyperedges.
	 * 
	 * @param r
	 *            rule of this node
	 * @param hyperedge
	 *            set of adjacent hyperedges
	 */
	public RuleNode(Rule r, Set<HyperEdge> hyperedge) {
		this.rule = r;
		this.hyperedges = new HashSet<>();
		for (HyperEdge h : hyperedge)
			addToHyperedge(h); // TODO cannot call this.addToHyperedge, because
								// this is not fully initialized.
		id = ++N;
	}

	/**
	 * Constructs a new node from a given rule with an empty set of adjacent
	 * hyperedges.
	 * 
	 * @param r
	 *            rule of this node
	 */
	public RuleNode(Rule r) {
		this.rule = r;
		this.hyperedges = new HashSet<HyperEdge>();
		id = ++N;
	}

	/**
	 * Gets the rule of this node.
	 * 
	 * @return rule of this node.
	 */
	public Rule getRule() {
		return rule;
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
	public HyperEdge getHyperedges(PortNode p) {
		for (HyperEdge h : hyperedges)
			if (h.getSource().getPort().equals(p.getPort()))
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
	 * Composes the rule of this node with a given formula using conjunction.
	 * 
	 * @param f
	 *            formula
	 * @return reference to this updated node.
	 */
	public RuleNode compose(Formula f) {
		boolean canSync = true;
		List<Formula> clauses = new ArrayList<Formula>();
		if (f instanceof Disjunction) {
			for (Formula clause : ((Disjunction) f).getClauses()) {
				for (Variable v : clause.getFreeVariables()) {
					if (v instanceof PortVariable && rule.getSyncConstraint().get(((PortVariable) v).getPort()) != null
							&& !rule.getSyncConstraint().get(((PortVariable) v).getPort())) {
						canSync = false;
					}
				}
				if (canSync) {
					clauses.add(clause);
				}
			}
		}
		Formula formula;
		if (clauses.size() == 1)
			formula = Conjunction.conjunction(Arrays.asList(rule.getDataConstraint(), clauses.get(0)));
		else
			formula = Conjunction.conjunction(Arrays.asList(rule.getDataConstraint(), new Disjunction(clauses)));
		rule = new Rule(rule.getSyncConstraint(), formula);
		return this;
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
	public RuleNode compose(RuleNode r) {
		if (!rule.canSync(r.getRule()))
			return null;

		Map<Port, Boolean> map = new HashMap<>(rule.getSyncConstraint());
		map.putAll(r.getRule().getSyncConstraint());

		Rule r1;
		if (rule.getDataConstraint().equals(r.getRule().getDataConstraint())) {
			if (r.getRule().getSyncConstraint().equals(rule.getSyncConstraint()))
				return this;
			else
				r1 = new Rule(map, rule.getDataConstraint());
		} else {
			r1 = new Rule(map,
					Conjunction.conjunction(Arrays.asList(rule.getDataConstraint(), r.getRule().getDataConstraint())));
		}

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
		return new RuleNode(this.getRule(), this.getHyperedges());
	}

	/**
	 * Renames the ports in this rule node.
	 * 
	 * @param links
	 *            map that assigns a new port to old ports.
	 * @return node with rule whose port variables are renamed.
	 */
	public RuleNode rename(Map<Port, Port> links) {
		rule = rule.rename(links);
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
		for (Map.Entry<String, String> entry : rename.entrySet()) {
			rule = new Rule(rule.getSyncConstraint(), rule.getDataConstraint().substitute(
					new MemoryVariable(entry.getValue(), false), new MemoryVariable(entry.getKey(), false)));
			rule = new Rule(rule.getSyncConstraint(), rule.getDataConstraint()
					.substitute(new MemoryVariable(entry.getValue(), true), new MemoryVariable(entry.getKey(), true)));
		}
		return this;
	}

	/**
	 * Hides a port from this rule via existential quantification.
	 * 
	 * @param p
	 *            port node
	 * @return reference to this rule node.
	 */
	public RuleNode hide(PortNode p) {
		rule = new Rule(rule.getSyncConstraint(),
				new Existential(new PortVariable(p.getPort()), rule.getDataConstraint()).QE());
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
		rule = rule.evaluate(s, m);
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
		return Objects.hash(this.id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return rule.toString();
	}
}
