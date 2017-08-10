package nl.cwi.reo.semantics.rulebasedautomata;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.stringtemplate.v4.ST;

import nl.cwi.reo.interpret.Atom;
import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.SemanticsType;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.ports.PortType;
import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.semantics.predicates.Equality;
import nl.cwi.reo.semantics.predicates.Formula;
import nl.cwi.reo.semantics.predicates.Formulas;
import nl.cwi.reo.semantics.predicates.MemoryVariable;
import nl.cwi.reo.semantics.predicates.PortVariable;
import nl.cwi.reo.semantics.predicates.Term;
import nl.cwi.reo.semantics.predicates.TruthValue;
import nl.cwi.reo.util.Monitor;

/**
 * A Rule-based Automaton.
 */
public final class RuleBasedAutomaton implements Semantics<RuleBasedAutomaton> {

	/** The set of sets rules. */
	private final Set<Set<Rule>> rules;

	/** The initial value of memory. */
	private final Map<MemoryVariable, Term> initial;

	/**
	 * Instantiates an empty rule based automaton.
	 */
	public RuleBasedAutomaton() {
		this.rules = new HashSet<>();
		this.initial = new HashMap<>();
	}

	/**
	 * Instantiates a new rule-based automaton.
	 *
	 * @param rules
	 *            the set of set of rules
	 * @param initial
	 *            the initial value of memory
	 */
	public RuleBasedAutomaton(Set<Set<Rule>> rules, Map<MemoryVariable, Term> initial) {
		Set<Set<Rule>> _rules = new HashSet<>();
		for (Set<Rule> part : rules)
			_rules.add(Collections.unmodifiableSet(part));
		this.rules = Collections.unmodifiableSet(_rules);
		this.initial = Collections.unmodifiableMap(initial);
	}

	/**
	 * Gets the initial.
	 *
	 * @return the initial
	 */
	public Map<MemoryVariable, Term> getInitial() {
		return initial;
	}

	/**
	 * Gets the rules.
	 *
	 * @return the rules
	 */
	public Set<Set<Rule>> getRules() {
		return rules;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.Atom#getType()
	 */
	@Override
	public SemanticsType getType() {
		return SemanticsType.RBA;
	}

	/**
	 * Gets the interface.
	 *
	 * @return the interface
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.Atom#getInterface()
	 */
	@Override
	public Set<Port> getInterface() {
		Set<Port> intface = new HashSet<>();
		for (Set<Rule> part : rules)
			for (Rule t : part)
				intface.addAll(t.getInterface());
		return intface;
	}

	/**
	 * Rename.
	 *
	 * @param links
	 *            the links
	 * @return the atom
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.Atom#rename(java.util.Map)
	 */
	@Override
	public Atom rename(Map<Port, Port> links) {
		Set<Set<Rule>> _rules = new HashSet<>();
		for (Set<Rule> part : rules) {
			Set<Rule> _part = new HashSet<>();
			for (Rule t : part)
				_part.add(t.rename(links));
			_rules.add(_part);
		}
		return new RuleBasedAutomaton(_rules, initial);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.Atom#getNode(java.util.Set)
	 */
	@Override
	public Atom getNode(Set<Port> node) {

		// TODO It seems not necessary to flip the port type here (or in
		// insertNodes() method).
		Set<Port> inps = new HashSet<>();
		Set<Port> outs = new HashSet<>();
		for (Port p : node) {
			if (p.isInput()) {
				outs.add(new Port(p.getName(), PortType.OUT, p.getPrioType(), p.getTypeTag(), true));
			} else {
				inps.add(new Port(p.getName(), PortType.IN, p.getPrioType(), p.getTypeTag(), true));
			}
		}

		Set<Rule> part = new HashSet<>();
		for (Port p : inps) {
			Formula f = new TruthValue(true);
			Map<Port, Boolean> map = new HashMap<>();

			map.put(p, true);
			for (Port x : inps)
				if (!x.equals(p))
					map.put(x, false);

			for (Port x : outs) {
				map.put(x, true);
				Formula eq = new Equality(new PortVariable(p), new PortVariable(x));
				f = Formulas.conjunction(Arrays.asList(f, eq));
			}

			part.add(new Rule(map, f));
		}

		Set<Set<Rule>> _rules = new HashSet<>();
		_rules.add(part);

		return new RuleBasedAutomaton(_rules, new HashMap<>());
	}

	/**
	 * Evaluate.
	 *
	 * @param s
	 *            the s
	 * @param m
	 *            the m
	 * @return the atom
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.Expression#evaluate(nl.cwi.reo.interpret.Scope,
	 * nl.cwi.reo.util.Monitor)
	 */
	@Override
	public @Nullable Atom evaluate(Scope s, Monitor m) {
		Set<Set<Rule>> _rules = new HashSet<>();
		for (Set<Rule> part : rules) {
			Set<Rule> _part = new HashSet<>();
			for (Rule t : part)
				_part.add(t.evaluate(s, m));
			_rules.add(_part);
		}
		return new RuleBasedAutomaton(_rules, initial);
	}

	/**
	 * Gets the default.
	 *
	 * @param iface
	 *            the iface
	 * @return the default
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.semantics.Semantics#getDefault(java.util.Set)
	 */
	@Override
	public RuleBasedAutomaton getDefault(Set<Port> iface) {
		Set<Rule> part = new HashSet<>();
		for (Port p : iface) {
			Formula f = new TruthValue(true);
			Map<Port, Boolean> map = new HashMap<>();
			map.put(p, true);
			for (Port x : iface)
				if (!x.equals(p))
					map.put(x, false);
			part.add(new Rule(map, f));
		}
		Set<Set<Rule>> _rules = new HashSet<>();
		_rules.add(part);
		return new RuleBasedAutomaton(_rules, new HashMap<>());
	}

	/**
	 * Compose.
	 *
	 * @param components
	 *            the components
	 * @return the rule based automaton
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.semantics.Semantics#compose(java.util.List)
	 */
	@Override
	public RuleBasedAutomaton compose(List<RuleBasedAutomaton> components) {

		List<RuleBasedAutomaton> list = new ArrayList<>();

		int i = 1;

		Map<String, String> rename = new HashMap<>();
		for (MemoryVariable m : getMemoryCells())
			if (!rename.containsKey(m.getName()))
				rename.putIfAbsent(m.getName(), "m" + i++);
		list.add(renameMemory(rename));

		for (RuleBasedAutomaton A : components) {
			Map<String, String> renameA = new HashMap<>();
			for (MemoryVariable m : A.getMemoryCells())
				if (!renameA.containsKey(m.getName()))
					renameA.put(m.getName(), "m" + i++);
			list.add(A.renameMemory(renameA));
		}

		Set<Set<Rule>> _rules = new HashSet<>();
		Map<MemoryVariable, Term> _initial = new HashMap<>();

		_rules.addAll(rules);
		for (RuleBasedAutomaton A : list) {
			_rules.addAll(A.getRules());
			_initial.putAll(A.getInitial());
		}

		return new RuleBasedAutomaton(_rules, _initial);
	}

	/**
	 * Rename memory.
	 *
	 * @param rename
	 *            the rename
	 * @return the rule based automaton
	 */
	private RuleBasedAutomaton renameMemory(Map<String, String> rename) {
		Map<MemoryVariable, MemoryVariable> substitution = new HashMap<>();
		for (MemoryVariable m : getMemoryCells()) {
			String name = rename.get(m.getName());
			if (name == null)
				name = m.getName();
			substitution.put(m, new MemoryVariable(name, m.hasPrime(), m.getTypeTag()));
		}

		Set<Set<Rule>> _rules = new HashSet<>();
		for (Set<Rule> part : rules) {
			Set<Rule> _part = new HashSet<>();
			for (Rule t : part)
				_part.add(t.renameMemory(substitution));
			_rules.add(_part);
		}

		Map<MemoryVariable, Term> _initial = new HashMap<>();
		for (Map.Entry<MemoryVariable, Term> init : initial.entrySet()) {
			String _m = rename.get(init.getKey().getName());
			MemoryVariable _q = init.getKey();
			if (_m != null)
				_q = new MemoryVariable(_m, true, init.getKey().getTypeTag());
			_initial.put(_q, init.getValue());
		}
		return new RuleBasedAutomaton(_rules, _initial);
	}

	/**
	 * Gets the memory cells of this rule-based automaton.
	 *
	 * @return the set of memory cells
	 */
	private Set<MemoryVariable> getMemoryCells() {
		Set<MemoryVariable> memorycells = new HashSet<>();
		for (Set<Rule> part : rules)
			for (Rule t : part)
				memorycells.addAll(t.getMemoryCells());
		return memorycells;
	}

	/**
	 * Restrict.
	 *
	 * @param intface
	 *            the intface
	 * @return the rule based automaton
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.semantics.Semantics#restrict(java.util.Collection)
	 */
	@Override
	public RuleBasedAutomaton restrict(Collection<? extends Port> intface) {

		long t0 = System.nanoTime();

		RuleBasedAutomaton A = distribute();

		System.out.println("Distribute() : " + ((double) (System.nanoTime() - t0)) / 1E9);

		Set<Set<Rule>> _rules = new HashSet<>();
		for (Set<Rule> part : A.getRules()) {
			Set<Rule> _part = new HashSet<>();
			for (Rule t : part) 
				_part.add(t.restrict(intface));
			_rules.add(_part);
		}
		return new RuleBasedAutomaton(_rules, initial);
	}

	/**
	 * Computes an equivalent rule-based automaton whose set of rules contains
	 * only a single set of transitions.
	 *
	 * @return an equivalent rule-based automaton whose set of rules contains
	 *         only a single set of transitions
	 */
	private RuleBasedAutomaton distribute() {

		Map<Port, List<List<Rule>>> H = toHypergraph();

		Set<Set<Rule>> closed = new HashSet<>();

		// History of alternative extensions of the partial global transition
		Deque<Rule> stack = new ArrayDeque<>();

		// Partial global transition
		Deque<Rule> partial = new ArrayDeque<>();

		// Rules that are explored
		Set<Rule> explored = new HashSet<>();

		final Rule seperator = new Rule(new HashMap<>(), new TruthValue(false));

		for (Set<Rule> part : rules) {
			for (Rule base : part) {

				if (explored.contains(base))
					continue;

				stack.offer(seperator);
				partial.add(base);

				// Ports that possibly extend the current partial transition
				Set<Port> unExplored = base.getActivePorts();

				long t0 = System.nanoTime();

				while (!partial.isEmpty()) {

					// Find all transitions that synchronize
					boolean isClosed = true;
					List<List<Rule>> edges;
					Set<Port> activePorts = new HashSet<>();
					for (Port p : unExplored) {
						if ((edges = H.get(p)) != null) {
							for (List<Rule> rules : edges) {
								if (Collections.disjoint(rules, partial)) {
									isClosed = false;
									explored.addAll(rules);

									// Append the alternatives to the stack and
									// extend the partial transition.
									Iterator<Rule> iter = rules.iterator();
									while (iter.hasNext()) {
										Rule x = iter.next();
										if (iter.hasNext()) {
											stack.push(x);
										} else {
											stack.push(seperator);
											partial.push(x);
											activePorts.addAll(x.getActivePorts());
										}
									}

								}
							}
						}
					}

					// Update the unexplored ports.
					activePorts.removeAll(unExplored);
					unExplored = activePorts;

					if (isClosed) {
						closed.add(new HashSet<>(partial));

						// Backtrack to the last branching point.
						while (!stack.isEmpty()) {
							Rule x = stack.pop();
							if (x == seperator) {
								partial.pop();
							} else {
								stack.push(seperator);
								partial.push(x);
							}
						}
					}
				}
				double d = ((double) (System.nanoTime() - t0)) / 1E9;
				if (d > 1)
					System.out.println("find extensions : " + d);
			}
		}

		// Compose the global transitions into rules
		Set<Rule> rules = new HashSet<>();
		for (Set<Rule> C : closed) {
			Map<Port, Boolean> sync = new HashMap<>();
			List<Formula> f = new ArrayList<>();
			for (Rule r : C) {
				sync.putAll(r.getSync());
				f.add(r.getFormula());
			}
			rules.add(new Rule(sync, Formulas.conjunction(f)));
		}

		return new RuleBasedAutomaton(new HashSet<>(Arrays.asList(rules)), initial);

	}

	/**
	 * Constructs a hypergraph from this rule-based automaton.
	 *
	 * @return the hypergraph
	 */
	private Map<Port, List<List<Rule>>> toHypergraph() {
		Map<Port, List<List<Rule>>> H = new LinkedHashMap<>();

		for (Set<Rule> part : rules) {

			// Find the hypergraph of each part.
			Map<Port, List<Rule>> Hp = new HashMap<>();
			for (Rule t : part) {
				for (Port p : t.getActivePorts()) {
					List<Rule> disjuncts = Hp.get(p);
					if (disjuncts == null) {
						Hp.put(p, new ArrayList<>(Arrays.asList(t)));
					} else {
						disjuncts.add(t);
					}
				}
			}

			// Compose the local hypergraph with the global hypergraph.
			for (Map.Entry<Port, List<Rule>> edge : Hp.entrySet()) {
				List<List<Rule>> conjuncts = H.get(edge.getKey());
				if (conjuncts == null) {
					H.put(edge.getKey(), new ArrayList<>(Arrays.asList(edge.getValue())));
				} else {
					conjuncts.add(edge.getValue());
				}
			}

		}

		return H;
	}

	public Set<Rule> getAllRules() {
		Set<Rule> flatten = new HashSet<>();
		for (Set<Rule> part : rules)
			flatten.addAll(part);
		return flatten;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		ST st = new ST("{ <initial.keys:{k | <k.name> = <rules.(k)>}>\n  <rules; separator=\"\n\">\n}");
		st.add("initial", initial);
		st.add("rules", rules);
		return st.render();
	}

}