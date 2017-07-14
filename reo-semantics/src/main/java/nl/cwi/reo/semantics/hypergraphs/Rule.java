package nl.cwi.reo.semantics.hypergraphs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.predicates.Formula;
import nl.cwi.reo.util.Monitor;

/**
 * Rule representation of a predicate, which consists of a map that models a
 * synchronization constraint and a formula the models a data constraint.
 */
public class Rule {

	/**
	 * Synchronization constraint. A port is mapped to true, if this rule
	 * requires it to fire; to false, if the port is blocked by this rule, and
	 * to null if the port is independent of this rule.
	 */
	private Map<Port, Boolean> sync;

	/**
	 * Data constraint. Every port variable that occurs in the data constraint
	 * must fire.
	 */
	private Formula f;

	/**
	 * Constructs a new rule from a given synchronization constraint and data
	 * constraint.
	 * 
	 * @param sync
	 *            synchronization constraint
	 * @param f
	 *            data constraint
	 */
	public Rule(Map<Port, Boolean> sync, Formula f) {
		this.sync = sync;
		this.f = f;
	}

	/**
	 * Gets the synchronization constraint of this rule.
	 * 
	 * @return synchronization constraint of this rule.
	 */
	public Map<Port, Boolean> getSyncConstraint() {
		return sync;
	}

	/**
	 * Gets the data constraint of this rule.
	 * 
	 * @return data constraint of this rule.
	 */
	public Formula getDataConstraint() {
		return f;
	}

	/**
	 * Gets the set of ports that must fire by this rule.
	 * 
	 * @return set of ports that must fire by this rule.
	 */
	public Set<Port> getFiringPorts() {
		Set<Port> setPort = new HashSet<Port>();
		for (Port p : sync.keySet()) {
			if (sync.get(p)) {
				setPort.add(p);
			}
		}
		return setPort;
	}

	/**
	 * Gets the set of all ports of this rule.
	 * 
	 * @return set of all ports of this rule.
	 */
	public Set<Port> getAllPorts() {
		return new HashSet<>(sync.keySet());
	}

	/**
	 * Renames the ports in this rule.
	 * 
	 * @param links
	 *            map that assigns a new port to an old port
	 * @return new rule with each port variable renamed.
	 */
	public Rule rename(Map<Port, Port> links) {
		HashMap<Port, Boolean> map = new HashMap<>();
		for (Port p : sync.keySet()) {
			if (links.containsKey(p)) {
				map.put(links.get(p), sync.get(p));
			} else
				map.put(p, sync.get(p));
		}

		return new Rule(map, f.rename(links));
	}

	/**
	 * Checks if this rule can synchronize with another rule by comparing their
	 * synchronization constraints.
	 * 
	 * @param r
	 *            rule
	 * @return true, if this rule can synchronize with the other rule, and false
	 *         otherwise.
	 */
	public boolean canSync(Rule r) {
		for (Map.Entry<Port, Boolean> e : sync.entrySet())
			for (Map.Entry<Port, Boolean> f : r.getSyncConstraint().entrySet())
				if (e.getKey().equals(f.getKey()) && f.getValue() == !e.getValue())
					return false;
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String s = "{";
		Iterator<Map.Entry<Port, Boolean>> iter = this.sync.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<Port, Boolean> pair = iter.next();
			s += pair.getValue() ? "" : "~";
			s += pair.getKey() + (iter.hasNext() ? ", " : "");
		}
		s += "} ";
		if (f != null)
			s += f.toString();
		return s;
	}

	/**
	 * Evaluates the data constraint of this rule by evaluation each
	 * function/relation symbol with a concrete function/relation symbol.
	 * 
	 * @param s
	 *            scope
	 * @param m
	 *            monitor
	 * @return new rule with concrete functions/relations, or null if not all
	 *         function/relation symbols are defined in the scope.
	 */
	public @Nullable Rule evaluate(Scope s, Monitor m) {
		Formula g = f.evaluate(s, m);
		if (g != null)
			return new Rule(sync, g);
		return null;
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
		if (!(other instanceof Rule))
			return false;
		Rule rule = (Rule) other;
		return (Objects.equals(this.sync, rule.sync) && Objects.equals(this.f, rule.f));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.sync, this.f);

	}
}
