package nl.cwi.reo.semantics.hypergraphs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.SemanticsType;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.ports.PortType;
import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.semantics.predicates.Conjunction;
import nl.cwi.reo.semantics.predicates.Equality;
import nl.cwi.reo.semantics.predicates.Formula;
import nl.cwi.reo.semantics.predicates.Formulas;
import nl.cwi.reo.semantics.predicates.MemoryVariable;
import nl.cwi.reo.semantics.predicates.Negation;
import nl.cwi.reo.semantics.predicates.PortVariable;
import nl.cwi.reo.semantics.predicates.Term;
import nl.cwi.reo.semantics.predicates.Terms;
import nl.cwi.reo.semantics.predicates.TruthValue;
import nl.cwi.reo.semantics.predicates.Variable;
import nl.cwi.reo.semantics.rulebasedautomata.Rule;
import nl.cwi.reo.util.Monitor;

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
	public ConstraintHypergraph(Set<Set<Rule>> s, Map<MemoryVariable, Term> initial) {
		hyperedges = new HashSet<HyperEdge>();
		for (Set<Rule> rules : s) {
			for(Rule r : rules){
				RuleNode rule = new RuleNode(new HashSet<>(Arrays.asList(r)));
				if(!rule.getSync().values().contains(Boolean.TRUE)){
					Port v = new Port("hidden");
					if (!getHyperedges(v).isEmpty()) {
						rule.addToHyperedge(getHyperedges(v).get(0));
					} else {
						Set<RuleNode> ruleNodes = new HashSet<RuleNode>();
						ruleNodes.add(rule);
						hyperedges.add(new HyperEdge(v, ruleNodes));
					}
				}
				for (Map.Entry<Port, Boolean> sc : rule.getSync().entrySet()) {
					if (sc.getValue() == true) {
						Port v = sc.getKey();
						if (!getHyperedges(v).isEmpty()) {
							rule.addToHyperedge(getHyperedges(v).get(0));
						} else {
							Set<RuleNode> ruleNodes = new HashSet<RuleNode>();
							ruleNodes.add(rule);
							hyperedges.add(new HyperEdge(v, ruleNodes));
						}
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
			if (h.getSource().equals(p) && !h.getTarget().isEmpty()) {
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
	public Set<Port> getVariables() {
		Set<Port> s = new HashSet<>();
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
	 * Gets the set of rules of this constraint hypergraph.
	 * 
	 * @return set of rules of this constraint hypergraph.
	 */
	public Set<Formula> getFormulas() {
		Set<Formula> f = new HashSet<>();

		for (HyperEdge g : hyperedges) {
			for (RuleNode r : g.getTarget()) {
				f.add(r.getFormula());
			}
		}

		return f;
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

		Set<Set<Rule>> rules = new HashSet<>();
		for (RuleNode r : getRuleNodes()){
			r.evaluate(s, m);
			rules.add(r.getRules());
		}

		Map<MemoryVariable, Term> _initial = new HashMap<>();

		for (Map.Entry<MemoryVariable, Term> init : initial.entrySet()) {
			Term t = init.getValue().evaluate(s, m);
			if (t == null)
				return null;
			_initial.put(init.getKey(), t);
		}
		return new ConstraintHypergraph(rules, _initial);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Port> getInterface() {
		Set<Port> p = new HashSet<Port>();
		for (RuleNode r : getRuleNodes()) {
			p.addAll(r.getActivePorts());
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

		Set<Set<Rule>> rules = new HashSet<>();

		for (Port p : inps) {
			Set<Rule> transition = new HashSet<>();
			transition.add(new Rule(new TruthValue(true)));
			Map<Port, Boolean> map = new HashMap<>();
			map.put(p, true);
			for (Port x : inps) {
				if (!x.equals(p)) {
					transition.add(new Rule(new Equality(new PortVariable(p),Terms.Null)));
				}
			}
			for (Port x : outs) {
				transition.add(new Rule(new Equality(new PortVariable(x),Terms.NonNull)));
				transition.add(new Rule(new Equality(new PortVariable(p),new PortVariable(p))));
			}
			rules.add(transition);
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
	
	public ConstraintHypergraph(Set<Rule> s) {
		hyperedges = new HashSet<>();

		for (Rule r : s) {
			RuleNode rule = new RuleNode(new HashSet<>(Arrays.asList(r)));
			for (Map.Entry<Port, Boolean> sc : r.getSync().entrySet()) {
				if (sc.getValue() == true) {
					Port v = sc.getKey();
					if (!getHyperedges(v).isEmpty()) {
						rule.addToHyperedge(getHyperedges(v).get(0));
					} else {
						Set<RuleNode> ruleNodes = new HashSet<RuleNode>();
						ruleNodes.add(rule);
						hyperedges.add(new HyperEdge(v, ruleNodes));
					}
				}
			}

		}
		this.initial = new HashMap<>();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConstraintHypergraph rename(Map<Port, Port> links) {
		for (RuleNode r : getRuleNodes()) {
			r.rename(links);
		}
		for (Port p : getVariables()) {
			p.rename(links.get(p).getName());
		}
		return new ConstraintHypergraph(new ArrayList<>(hyperedges), initial);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConstraintHypergraph compose(List<ConstraintHypergraph> components) {

		// Rename all memory cells and put *all* components into a list.
		components = renameMemoryCells(components);

		// Compose the list of CHs into a single CH.
		ConstraintHypergraph composition = new ConstraintHypergraph();

		for (ConstraintHypergraph h : components) {
			composition.getHyperedges().addAll(h.getHyperedges());
			composition.getInitials().putAll(h.getInitials());
		}

		composition.distribute();

		return composition;
	}
	
	/**
	 * Distribute multi rules hyperedges.
	 */
	public void distribute() {
		Set<Port> variables = getVariables();

		for (Port p : variables) {
			List<HyperEdge> multiEdge = getHyperedges(p);
			
			if (!multiEdge.isEmpty()) {
				HyperEdge toDistribute = multiEdge.remove(0);
				boolean mult = false;
				for (HyperEdge h : multiEdge) {
					if (!h.getTarget().isEmpty()) {
						toDistribute.compose(h);
						mult = true;
					}
				}
				/*
				 * Remove empty hyperedges
				 */
				if (mult) {
					Set<HyperEdge> s = new HashSet<>(hyperedges);
					hyperedges.clear();
					hyperedges.addAll(s);
				}
			}
		}
	}
	/**
	 * Rename all memory cells consistently
	 * @param components
	 * @return
	 */
	public List<ConstraintHypergraph> renameMemoryCells(List<ConstraintHypergraph> components){
		List<ConstraintHypergraph> list = new ArrayList<>();

		int i = 0;
		for (ConstraintHypergraph A : components) {
			Map<String, String> rename = new HashMap<>();
			for (RuleNode rn : A.getRuleNodes()) {
				for(Rule r : rn.getRules()){
					for (Variable v : r.getFormula().getFreeVariables()) {
						if (v instanceof MemoryVariable) {
							String name = ((MemoryVariable) v).getName();
							if (!rename.containsKey(name)){
								rename.put(name, "m" + ++i);
								
							}
						}
					}
				}
			}
			Map<String, String> _rename = new HashMap<>();
			for(String name : rename.keySet()){
				if(rename.containsKey(rename.get(name))|| _rename.containsValue("m"+i)){
					while( _rename.containsValue("m"+i) || ("m"+i).equals(name) || rename.containsKey("m"+i))
						i++;
					_rename.put(name, "m"+i);
				}
				else
					_rename.put(name, rename.get(name));
			}
			
			for (RuleNode r : A.getRuleNodes())
				r.substitute(_rename);

			Map<MemoryVariable, Term> newInit = new HashMap<>();
			for (Map.Entry<MemoryVariable, Term> e : A.getInitials().entrySet()) {
				String newName = _rename.get(e.getKey().getName());
				if (newName == null)
					newName = e.getKey().getName();
				newInit.put(new MemoryVariable(newName, e.getKey().hasPrime(), e.getKey().getTypeTag()), e.getValue());
			}

			list.add(new ConstraintHypergraph(new ArrayList<>(A.getHyperedges()), newInit));
		}
		return list;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConstraintHypergraph restrict(Collection<? extends Port> intface) {
		Set<Rule> setRules = new HashSet<Rule>();
		Set<Formula> setFormula = new HashSet<>();
		
		for (RuleNode ruleNodes : getRuleNodes()) {
			Set<Formula> list = new HashSet<>();
			for(Rule _r : ruleNodes.getRules())
				list.add(_r.getFormula());
			Formula g = new Conjunction(list);
			for (Port p : ruleNodes.getPorts()) {
				if (!intface.contains(p)) {
					if(g instanceof Conjunction)
						g = Formulas.eliminate(((Conjunction) g).getClauses(), Arrays.asList(new PortVariable(p)));
					else if(g instanceof Negation)
						g = Formulas.eliminate(Arrays.asList(g), Arrays.asList(new PortVariable(p)));
				}
			}
			setFormula.add(g);
		}
		for(Formula g : setFormula)
			setRules.add(new Rule(g));
		return new ConstraintHypergraph(new HashSet<>(Arrays.asList(setRules)), initial);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		
		Set<Port> variables = new HashSet<>();
		for (HyperEdge h : hyperedges) {
			variables.add(h.getSource());
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

	}

}
