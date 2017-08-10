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
import nl.cwi.reo.semantics.predicates.Variable;
import nl.cwi.reo.util.Monitor;

// TODO: Auto-generated Javadoc
/**
 * Constraint hypergraph semantics of Reo connectors.
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
	 * @param s
	 *            the s
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
	 * @param h
	 *            the h
	 * @param initial
	 *            the initial
	 */
	public ConstraintHypergraph(List<HyperEdge> h, Map<MemoryVariable, Term> initial) {
		this.hyperedges = new HashSet<HyperEdge>(h);
		this.initial = new HashMap<>(initial);
	}

	/**
	 * Constructs a new automaton from a given set of rules and initial values.
	 *
	 * @param s
	 *            the s
	 * @param initial
	 *            the initial
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
	@Nullable
	public ConstraintHypergraph evaluate(Scope s, Monitor m) {
<<<<<<< HEAD:reo-semantics/src/main/java/nl/cwi/reo/semantics/rba/ConstraintHypergraph.java

=======
>>>>>>> 21ae1367aaf067a9316ae7c996398eea5fd0656d:reo-semantics/src/main/java/nl/cwi/reo/semantics/hypergraphs/ConstraintHypergraph.java
		for (RuleNode r : getRuleNodes())
			r.evaluate(s, m);

		Map<MemoryVariable, Term> _initial = new HashMap<>();

		for (Map.Entry<MemoryVariable, Term> init : initial.entrySet()) {
			Term t = init.getValue().evaluate(s, m);
			if (t == null)
				return null;
			_initial.put(init.getKey(), t);
		}
<<<<<<< HEAD:reo-semantics/src/main/java/nl/cwi/reo/semantics/rba/ConstraintHypergraph.java
		
		
=======

>>>>>>> 21ae1367aaf067a9316ae7c996398eea5fd0656d:reo-semantics/src/main/java/nl/cwi/reo/semantics/hypergraphs/ConstraintHypergraph.java
		return new ConstraintHypergraph(getRules(), _initial);
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

		Set<Port> inps = new HashSet<Port>();
		Set<Port> outs = new HashSet<Port>();

		for (Port p : node) {
			if (p.getType() == PortType.IN) {
				outs.add(new Port(p.getName(), (p.isInput() ? PortType.OUT : PortType.IN), p.getPrioType(),
						p.getTypeTag(), true));
			} else {
				inps.add(new Port(p.getName(), (p.isInput() ? PortType.OUT : PortType.IN), p.getPrioType(),
						p.getTypeTag(), false));
			}
		}

		Set<Rule> rules = new HashSet<Rule>();

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
				transition = Formulas.conjunction(Arrays.asList(transition, eq));
			}
			rules.add(new Rule(map, transition));
		}

		return new ConstraintHypergraph(rules, initial);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConstraintHypergraph getDefault(Set<Port> iface) {

		Set<Rule> rules = new HashSet<Rule>();

		for (Port p : iface) {
			Map<Port, Boolean> map = new HashMap<>();
			map.put(p, true);
			for (Port x : iface)
				if (!x.equals(p))
					map.put(x, false);
			rules.add(new Rule(map, new TruthValue(true)));
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConstraintHypergraph compose(List<ConstraintHypergraph> components) {

		// Rename all memory cells and put *all* components into a list.
		List<ConstraintHypergraph> list = new ArrayList<>();

		int i = 1;
		for (ConstraintHypergraph A : components) {
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
			for (RuleNode r : A.getRuleNodes())
				r.substitute(rename);

			Map<MemoryVariable, Term> newInit = new HashMap<>();
			for (Map.Entry<MemoryVariable, Term> e : A.getInitials().entrySet()) {
				String newName = rename.get(e.getKey().getName());
				if (newName == null)
					newName = e.getKey().getName();
				newInit.put(new MemoryVariable(newName, e.getKey().hasPrime()), e.getValue());
			}

			list.add(new ConstraintHypergraph(new ArrayList<>(A.getHyperedges()), newInit));
		}

		// Compose the list of CHs into a single CH.
		ConstraintHypergraph composition = new ConstraintHypergraph();

		for (ConstraintHypergraph h : list) {
			composition.getHyperedges().addAll(h.getHyperedges());
			composition.getInitials().putAll(h.getInitials());
		}

		composition = composition.distributeSingleEdge();
		composition.distributeMultiEdge();

		return composition;

	}

	/**
	 * Distribute single hyperedges.
	 *
	 * @return the constraint hypergraph
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
	 * Distribute multi rules hyperedges.
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
//						toDistribute = h.compose(toDistribute);
						toDistribute.compose(h);
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
	 * Removes the empty hyperedge.
	 */
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
			for (Port p : r.getFiringPorts()) {
				if (!intface.contains(p)) {
					List<Variable> V = Arrays.asList(new PortVariable(p));
					g = Formulas.eliminate(g, V);
				}
			}
			setRules.add(new Rule(r.getSyncConstraint(), g));
		}
		return new ConstraintHypergraph(setRules, initial);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		
		Set<Port> variables = new HashSet<>();
		for (HyperEdge h : hyperedges) {
			variables.add(h.getSource().getPort());
		}
		String s = "";
		for (Port var : variables) {
			s = s + "Root : " + var.toString() + "\n{";
			int i = getHyperedges(var).size();
			for (HyperEdge h : getHyperedges(var)) {
				s = s + h.toString() + (i > 1 ? " && \n" : "");
				i--;
			}
			s = s + "}\n \n";
		}
		return s;
//		
//		String s = "";
//		Iterator<HyperEdge> iter = hyperedges.iterator();
//		while (iter.hasNext())
//			s += iter.next() + (iter.hasNext() ? "\n" : "");
//		return s;
	}

}
