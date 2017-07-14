package nl.cwi.reo.semantics.hypergraphs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.ports.PortType;
import nl.cwi.reo.interpret.values.BooleanValue;
import nl.cwi.reo.interpret.values.DecimalValue;
import nl.cwi.reo.interpret.values.IntegerValue;
import nl.cwi.reo.interpret.values.StringValue;
import nl.cwi.reo.interpret.values.Value;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.semantics.SemanticsType;
import nl.cwi.reo.semantics.predicates.Conjunction;
import nl.cwi.reo.semantics.predicates.Equality;
import nl.cwi.reo.semantics.predicates.Existential;
import nl.cwi.reo.semantics.predicates.Formula;
import nl.cwi.reo.semantics.predicates.Function;
import nl.cwi.reo.semantics.predicates.MemoryVariable;
import nl.cwi.reo.semantics.predicates.PortVariable;
import nl.cwi.reo.semantics.predicates.Term;
import nl.cwi.reo.semantics.predicates.TruthValue;
import nl.cwi.reo.semantics.predicates.Variable;
import nl.cwi.reo.util.Monitor;

/**
 * Constraint hypergraph semantics of Reo connectors. The rule-based automaton
 * listener {@link nl.cwi.reo.interpret.ListenerRBA} uses this semantics.
 */
public class ConstraintHypergraph implements Semantics<ConstraintHypergraph> {

	/**
	 * List of hyperedges.
	 */
	private final Set<HyperEdge> hyperedges;

	/**
	 * Map that assigns an initial value to each memory cell.
	 */
	private final Map<MemoryVariable, Term> initial;

	/**
	 * Constructs an automaton, with an empty set of rules.
	 */
	public ConstraintHypergraph() {
		hyperedges = new HashSet<>();
		initial = new HashMap<>();
	}

	/**
	 * Constructs a new automaton from a given set of rules.
	 * 
	 * @param f
	 *            formula
	 */
	public ConstraintHypergraph(Set<Rule> s) {
		hyperedges = new HashSet<>();

		for (Rule r : s) {
			RuleNode rule = new RuleNode(r);
			for (Map.Entry<Port, Boolean> sc : r.getSyncConstraint().entrySet()) {
				if (sc.getValue() == true) {
					Port v = sc.getKey();
					if (!getHyperedges(v).isEmpty()) {
						rule.addToHyperedge(getHyperedges(v).get(0));
					} else {
						Set<RuleNode> ruleNodes = new HashSet<RuleNode>();
						ruleNodes.add(rule);
						hyperedges.add(new HyperEdge(new PortNode(v), ruleNodes));
					}
				}
			}

		}
		this.initial = new HashMap<>();
	}

	/**
	 * Constructs a new automaton from a given set of rules and initial values.
	 * 
	 * @param f
	 *            formula
	 */
	public ConstraintHypergraph(List<HyperEdge> h, Map<MemoryVariable, Term> initial) {
		this.hyperedges = new HashSet<HyperEdge>(h);
		this.initial = new HashMap<>(initial);
	}

	/**
	 * Constructs a new automaton from a given set of rules and initial values.
	 * 
	 * @param f
	 *            formula
	 */
	public ConstraintHypergraph(Set<Rule> s, Map<MemoryVariable, Term> initial) {
		hyperedges = new HashSet<HyperEdge>();
		for (Rule r : s) {
			RuleNode rule = new RuleNode(r);
			for (Map.Entry<Port, Boolean> sc : r.getSyncConstraint().entrySet()) {
				if (sc.getValue() == true) {
					Port v = sc.getKey();
					if (!getHyperedges(v).isEmpty()) {
						rule.addToHyperedge(getHyperedges(v).get(0));
					} else {
						Set<RuleNode> ruleNodes = new HashSet<RuleNode>();
						ruleNodes.add(rule);
						hyperedges.add(new HyperEdge(new PortNode(v), ruleNodes));
					}
				}
			}
		}
		this.initial = initial;
	}

	/**
	 * Gets all hyperedges that have a given port as their root.
	 * 
	 * @param p
	 *            port
	 * @return list of hyperedges that have the given port as their root.
	 */
	public List<HyperEdge> getHyperedges(Port p) {
		List<HyperEdge> hyperedgeList = new ArrayList<HyperEdge>();
		for (HyperEdge h : hyperedges) {
			if (h.getSource().getPort().equals(p) && !h.getTarget().isEmpty()) {
				hyperedgeList.add(h);
			}
		}
		return hyperedgeList;
	}

	/**
	 * Gets the list of hyperedges of this constraint hypergraph.
	 * 
	 * @return list of hyperedges of this constraint hypergraph.
	 */
	public Set<HyperEdge> getHyperedges() {
		return hyperedges;
	}

	/**
	 * Gets the variables of that occur in this constraint hypergraph.
	 * 
	 * @return set of all variables that occur in this constraint hypergraph.
	 */
	public Set<PortNode> getVariables() {
		Set<PortNode> s = new HashSet<>();
		for (HyperEdge h : hyperedges)
			s.add(h.getSource());
		return s;
	}

	/**
	 * Gets the set of rules of this constraint hypergraph.
	 * 
	 * @return set of rules of this constraint hypergraph.
	 */
	public Set<Rule> getRules() {
		Set<Rule> s = new HashSet<>();

		for (HyperEdge g : hyperedges) {
			for (RuleNode r : g.getTarget()) {
				s.add(r.getRule());
			}
		}

		return s;
	}

	/**
	 * Gets the set of nodes in this constraint hypergraph that represent a
	 * rule.
	 * 
	 * @return set of nodes in this constraint hypergraph that represent a rule.
	 */
	public Set<RuleNode> getRuleNodes() {
		Set<RuleNode> s = new HashSet<>();

		for (HyperEdge g : hyperedges) {
			s.addAll(g.getTarget());
		}

		return s;
	}

	/**
	 * Gets the assignment of an initial value to each memory cell.
	 * 
	 * @return map that assigns an initial value to each memory cell.
	 */
	public Map<MemoryVariable, Term> getInitials() {
		return initial;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public @Nullable ConstraintHypergraph evaluate(Scope s, Monitor m) {
		for (RuleNode r : getRuleNodes())
			r.evaluate(s, m);

		// TODO Evaluation of the initial values should be a recursive call to
		// an evaluate function in of terms.
		for (Map.Entry<MemoryVariable, Term> init : initial.entrySet()) {
			if (s.get(new Identifier(init.getValue().toString())) != null) {
				Value v = s.get(new Identifier(init.getValue().toString()));
				if (v instanceof StringValue)
					initial.put(init.getKey(),
							new Function("constant", ((StringValue) v).getValue(), new ArrayList<Term>()));
				if (v instanceof BooleanValue)
					initial.put(init.getKey(),
							new Function("constant", ((BooleanValue) v).getValue(), new ArrayList<Term>()));
				if (v instanceof IntegerValue)
					initial.put(init.getKey(),
							new Function("constant", ((IntegerValue) v).getValue(), new ArrayList<Term>()));
				if (v instanceof DecimalValue)
					initial.put(init.getKey(),
							new Function("constant", ((DecimalValue) v).getValue(), new ArrayList<Term>()));
			}
		}
		return new ConstraintHypergraph(getRules(), initial);
		// If a new constraint hypergraph is not created, all instances of the
		// same component definition will have shared rules and shared
		// hyperedges.
		// return new ConstraintHypergraph(new ArrayList<>(hyperedges),initial);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Port> getInterface() {
		Set<Port> p = new HashSet<Port>();
		for (Rule r : getRules()) {
			p.addAll(r.getFiringPorts());
		}
		return p;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SemanticsType getType() {
		return SemanticsType.CH;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConstraintHypergraph getNode(Set<Port> node) {

		Set<Port> ports = new HashSet<Port>(node);

		Set<Port> inps = new HashSet<Port>();
		Set<Port> outs = new HashSet<Port>();

		for (Port p : ports) {
			if (p.getType() == PortType.IN) {
				outs.add(new Port(p.getName(), (p.isInput() ? PortType.OUT : PortType.IN), p.getPrioType(),
						p.getTypeTag(), true));
			} else {
				inps.add(new Port(p.getName(), (p.isInput() ? PortType.OUT : PortType.IN), p.getPrioType(),
						p.getTypeTag(), false));
			}
		}

		Set<Rule> rules = new HashSet<Rule>();
		/*
		 * Instantiate merger/relicator
		 */
		for (Port p : inps) {
			Formula transition = new TruthValue(true);
			Map<Port, Boolean> map = new HashMap<>();
			map.put(p, true);
			for (Port x : inps) {
				if (!x.equals(p)) {
					map.put(x, false);
				}
			}
			for (Port x : outs) {
				map.put(x, true);
				Formula eq = new Equality(new PortVariable(p), new PortVariable(x));
				transition = new Conjunction(Arrays.asList(transition, eq));
			}
			rules.add(new Rule(map, transition));
		}

		return new ConstraintHypergraph(rules, initial);
	}

	/**
	 * Gets the default constraint hypgergraph over a set of
	 * 
	 * @param ports
	 * @return
	 */
	public ConstraintHypergraph getDefault(Set<Port> ports) {

		Set<Rule> rules = new HashSet<Rule>();

		for (Port p : ports) {
			Map<Port, Boolean> map = new HashMap<>();
			map.put(p, true);
			for (Port x : ports)
				if (!x.equals(p))
					map.put(x, false);
			Formula guard = new TruthValue(true);
			rules.add(new Rule(map, guard));
		}

		return new ConstraintHypergraph(rules);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConstraintHypergraph rename(Map<Port, Port> links) {
		for (RuleNode r : getRuleNodes()) {
			r.rename(links);
		}
		for (PortNode p : getVariables()) {
			p.rename(links);
		}
		return new ConstraintHypergraph(new ArrayList<>(hyperedges), initial);
	}

	@Override
	public ConstraintHypergraph compose(List<ConstraintHypergraph> components) {

		// Rename all memory cells and put *all* components into a list.
		List<ConstraintHypergraph> list = new ArrayList<>(components);
		List<ConstraintHypergraph> newList = new ArrayList<>(components);
		int i = 1;
		for (ConstraintHypergraph A : list) {
			Map<String, String> rename = new HashMap<>();
			for (Rule r : A.getRules()) {
				for (Variable v : r.getDataConstraint().getFreeVariables()) {
					if (v instanceof MemoryVariable) {
						String name = ((MemoryVariable) v).getName();
						if (!rename.containsKey(name))
							rename.put(name, "m" + i++);
					}
				}
			}
			for (RuleNode r : A.getRuleNodes()) {
				r.substitute(rename);
			}

			Map<MemoryVariable, Term> init = new HashMap<>(A.getInitials());
			for (Map.Entry<MemoryVariable, Term> e : init.entrySet()) {
				String newName = rename.get(e.getKey().getName());
				if (newName == null)
					newName = e.getKey().getName();
				A.getInitials().put(new MemoryVariable(newName, e.getKey().hasPrime()), e.getValue());
			}

			newList.add(new ConstraintHypergraph(new ArrayList<>(A.getHyperedges()), A.getInitials()));
		}

		// Compose the list of RBAs into a single list of rules.

		ConstraintHypergraph composedAutomaton = new ConstraintHypergraph();
		for (ConstraintHypergraph h : newList) {
			composedAutomaton.getHyperedges().addAll(h.getHyperedges());
			composedAutomaton.getInitials().putAll(h.getInitials());
		}
		composedAutomaton = composedAutomaton.distributeSingleEdge();
		composedAutomaton.distributeMultiEdge();

		return composedAutomaton;
		// return new
		// ConstraintHypergraph(composedAutomaton.getRules(),initialValue);

	}

	/**
	 * Distribute single hyperedges
	 * 
	 * @return
	 */
	public ConstraintHypergraph distributeSingleEdge() {
		Set<Port> variables = new HashSet<>();
		for (HyperEdge h : hyperedges) {
			variables.add(h.getSource().getPort());
		}

		for (Port p : variables) {
			List<HyperEdge> singleEdge = new ArrayList<>();
			List<HyperEdge> multiEdge = new ArrayList<>();

			for (HyperEdge h : getHyperedges(p)) {
				if (h.getTarget().size() == 1)
					singleEdge.add(h);
				else
					multiEdge.add(h);
			}
			if (singleEdge.size() > 1) {
				HyperEdge e = singleEdge.get(0);
				singleEdge.remove(0);
				for (HyperEdge h : singleEdge) {
					e.compose(h);
				}
				singleEdge.clear();
				singleEdge.add(e);
			}
			if (!multiEdge.isEmpty()) {
				HyperEdge e = multiEdge.get(0);
				for (HyperEdge h : singleEdge) {
					e.compose(h);

				}
				if (!multiEdge.isEmpty()) {
					hyperedges.removeAll(singleEdge);
				}
			}
		}
		return new ConstraintHypergraph(new ArrayList<>(hyperedges), initial);
	}

	/**
	 * Distribute multi rules hyperedges
	 * 
	 * @return
	 */
	public void distributeMultiEdge() {
		Set<Port> variables = new HashSet<>();
		for (HyperEdge h : hyperedges) {
			variables.add(h.getSource().getPort());
		}

		for (Port p : variables) {
			List<HyperEdge> multiEdge = new ArrayList<>();

			multiEdge.addAll(getHyperedges(p));
			if (!multiEdge.isEmpty()) {
				HyperEdge toDistribute = multiEdge.get(0);
				multiEdge.remove(0);
				boolean mult = false;
				for (HyperEdge h : multiEdge) {
					if (!h.getTarget().isEmpty()) {
						toDistribute = h.compose(toDistribute);
						mult = true;
					}
				}
				if (mult) {
					Set<HyperEdge> s = new HashSet<>(hyperedges);
					hyperedges.clear();
					hyperedges.addAll(s);
				}
			}
		}
	}

	/**
	 * Distributes all hyperedges in this hypergraph.
	 * 
	 * @return new hypergraphs wherein each variable has only a single
	 *         hyperegde.
	 */
	public ConstraintHypergraph distribute() {
		return null;
	}

	public void removeEmptyHyperedge() {
		Set<HyperEdge> s = new HashSet<>();
		Queue<HyperEdge> q = new LinkedList<>(hyperedges);
		while (!q.isEmpty()) {
			HyperEdge e = q.poll();
			if (!(e.getTarget().size() == 0))
				s.add(e);
		}
		hyperedges.clear();
		hyperedges.addAll(s);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConstraintHypergraph restrict(Collection<? extends Port> intface) {
		Set<Rule> setRules = new HashSet<Rule>();
		for (Rule r : getRules()) {
			Formula g = r.getDataConstraint();
			for (Port p : r.getFiringPorts())
				if (!intface.contains(p))
					g = new Existential(new PortVariable(p), g);
			setRules.add(new Rule(r.getSyncConstraint(), g));
		}
		return new ConstraintHypergraph(setRules, initial);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		// Set<Port> variables = new HashSet<>();
		// for (HyperEdge h : hyperedges) {
		// variables.add(h.getRoot().getPort());
		// }
		// String s = "";
		// for (Port var : variables) {
		// s = s + "Root : " + var.toString() + "\n{";
		// int i = getHyperedges(var).size();
		// for (HyperEdge h : getHyperedges(var)) {
		// s = s + h.toString() + (i > 1 ? " && \n" : "");
		// i--;
		// }
		// s = s + "}\n \n";
		// }
		String s = "";
		for (HyperEdge h : hyperedges) {
			s += h.getSource().getPort() + " -> {";
			Iterator<RuleNode> iter = h.getTarget().iterator();
			while (iter.hasNext())
				s += "\n" + iter.next() + (iter.hasNext() ? ", " : "");
			s += "\n}";
		}
		return s;
	}

}
