package nl.cwi.reo.semantics.rbautomaton;

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
import java.util.Stack;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.stringtemplate.v4.ST;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.ports.PortType;
import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.semantics.SemanticsType;
import nl.cwi.reo.semantics.predicates.Conjunction;
import nl.cwi.reo.semantics.predicates.Equality;
import nl.cwi.reo.semantics.predicates.Existential;
import nl.cwi.reo.semantics.predicates.Formula;
import nl.cwi.reo.semantics.predicates.MemCell;
import nl.cwi.reo.semantics.predicates.Node;
import nl.cwi.reo.semantics.predicates.Relation;
import nl.cwi.reo.semantics.predicates.Variable;
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
	
	@Deprecated
	public Set<Rule> getNewRules() {
		Set<Rule> setRules = new HashSet<Rule>();
		Graph g = new Graph(s).isolate();
		for (GraphNode n : g.getNodes()) {
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
		for (Rule r : this.s) {
			setRules.add(r.evaluate(s, m));
		}
		return new RulesBasedAutomaton(setRules);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Port> getInterface() {
		Set<Port> p = new HashSet<Port>();
		for (Rule r : s) {
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
			Formula transition = null;
			Map<Port, Boolean> map = new HashMap<>();
			map.put(p, true);
			for (Port x : inps) {
				if (!x.equals(p)) {
					map.put(x, false);
				}
			}
			for (Port x : outs) {
				map.put(x, true);
				Formula eq = new Equality(new Node(p), new Node(x));
				if (transition == null)
					transition = eq;
				else
					transition = new Conjunction(Arrays.asList(transition, eq));
			}
			rules.add(new Rule(map, transition));
		}

		return new RulesBasedAutomaton(rules);
	}

	public RulesBasedAutomaton getDefault(Set<Port> ports) {

		Set<Rule> rules = new HashSet<Rule>();

		for (Port p : ports) {
			Map<Port, Boolean> map = new HashMap<>();
			map.put(p, true);
			for (Port x : ports)
				if (!x.equals(p))
					map.put(x, false);
			Formula guard = new Relation("true", "true", null);
			rules.add(new Rule(map, guard));
		}

		return new RulesBasedAutomaton(rules);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RulesBasedAutomaton rename(Map<Port, Port> links) {
		Set<Rule> setRules = new HashSet<Rule>();
		for (Rule r : s) {
			setRules.add(r.rename(links));
		}
		return new RulesBasedAutomaton(setRules);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RulesBasedAutomaton compose(List<RulesBasedAutomaton> components) {

		// Rename all memory cells and put *all* components into a list.
		List<RulesBasedAutomaton> list = new ArrayList<>();

		List<RulesBasedAutomaton> oldlist = new ArrayList<>(components);
		oldlist.add(this);
		int i = 1;
		for (RulesBasedAutomaton A : oldlist) {
			Set<Rule> s = new HashSet<Rule>(this.s);
			Map<String, String> rename = new HashMap<>();
			for (Rule r : A.getRules()) {
				for (Variable v : r.getFormula().getFreeVariables()) {
					if (v instanceof MemCell) {
						String name = ((MemCell) v).getName();
						if (!rename.containsKey(name))
							rename.put(name, "m" + i++);
					}
				}
			}
			for (Rule r : A.getRules()) {
				Formula _f = r.getFormula();
				for (Map.Entry<String, String> entry : rename.entrySet()) {
					_f = _f.Substitute(new MemCell(entry.getValue(), false), new MemCell(entry.getKey(), false));
					_f = _f.Substitute(new MemCell(entry.getValue(), true), new MemCell(entry.getKey(), true));
				}
				s.add(new Rule(r.getSync(), _f));
			}
			list.add(new RulesBasedAutomaton(s));
		}

		// Compose the list of RBAs into a single list of rules.
		Set<Rule> rules = new HashSet<>();

		Set<Rule> marked = new HashSet<>(); // correct?

		for (RulesBasedAutomaton A : list) {
			for (Rule x : A.getRules()) {

				if (marked.contains(x))
					continue;

				Stack<Rule> rule = new Stack<>();
				Stack<Rule> stack = new Stack<>();

				rule.push(x);
				stack.push(null);

				Iterator<RulesBasedAutomaton> iter = list.iterator();

				loop: while (true) {

					while (iter.hasNext()) {
						RulesBasedAutomaton B = iter.next();

						for (Rule y : B.getRules())
							if (!rule.contains(y) && synchronize(compose(rule), y))
								stack.push(y);

						if (stack.peek() != null) {
							rule.push(stack.pop());
							stack.push(null);
							iter = list.iterator();
						}
					}

					rules.add(compose(rule));

					while (stack.peek() == null) {
						marked.add(rule.pop());
						stack.pop();
						if (stack.empty())
							break loop;
					}

					rule.push(stack.pop());
					stack.push(null);
					iter = list.iterator();

				}

			}
		}

		return new RulesBasedAutomaton(rules);
	}

	public RulesBasedAutomaton compose1(List<RulesBasedAutomaton> components) {

		// Rename all memory cells and put *all* components into a list.
		List<RulesBasedAutomaton> list = new ArrayList<>();

		List<RulesBasedAutomaton> oldlist = new ArrayList<>(components);
		oldlist.add(this);
		int i = 1;
		for (RulesBasedAutomaton A : oldlist) {
			Set<Rule> s = new HashSet<Rule>(this.s);
			Map<String, String> rename = new HashMap<>();
			for (Rule r : A.getRules()) {
				for (Variable v : r.getFormula().getFreeVariables()) {
					if (v instanceof MemCell) {
						String name = ((MemCell) v).getName();
						if (!rename.containsKey(name))
							rename.put(name, "m" + i++);
					}
				}
			}
			for (Rule r : A.getRules()) {
				Formula _f = r.getFormula();
				for (Map.Entry<String, String> entry : rename.entrySet()) {
					_f = _f.Substitute(new MemCell(entry.getValue(), false), new MemCell(entry.getKey(), false));
					_f = _f.Substitute(new MemCell(entry.getValue(), true), new MemCell(entry.getKey(), true));
				}
				s.add(new Rule(r.getSync(), _f));
			}
			list.add(new RulesBasedAutomaton(s));
		}

		// Compose the list of RBAs into a single list of rules.
		Set<Rule> rules = new HashSet<>();
		for(RulesBasedAutomaton A : list){
			rules.addAll(A.getRules());
		}
		Graph G = new Graph(rules);
		
//		G=G.addHyperLinks();

		G=G.isolate1();
		
		Set<Rule> s = G.getRules();
		

		// TODO Graph based approach
		
		return new RulesBasedAutomaton(s);
	}
	
	
	
	/**
	 * Determines whether there exists a port p such that both rules fire p, and
	 * whether the conjunction is satisfiable.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private static boolean synchronize(Rule x, Rule y) {
		boolean synchronize = false;
		for (Port p : x.getSync().keySet()) {
			if (x.getSync().get(p)) {
				Boolean b = y.getSync().get(p);
				if (b != null) {
					if (b.equals(true)) {
						synchronize = true;
					} else {
						return false;
					}
				}
			}
		}
		return synchronize;
	}

	/**
	 * Composes the current collection of local rules into a global rule.
	 * 
	 * @param rule
	 *            stack of local rules
	 * @return global rule
	 */
	private static Rule compose(Stack<Rule> rule) {
		List<Formula> clauses = new ArrayList<Formula>();
		Map<Port, Boolean> sync = new HashMap<>();

		for (Rule r : rule) {
			sync.putAll(r.getSync());
			clauses.add(r.getFormula());
		}

		return new Rule(sync, Conjunction.conjunction(clauses));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		ST st = new ST("<rules; separator=\"\n\">");
		st.add("rules", this.s);
		return st.render();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RulesBasedAutomaton restrict(Collection<? extends Port> intface) {
		Set<Rule> setRules = new HashSet<Rule>();
		for (Rule r : s) {
			Formula g = r.getFormula();
			for (Port p : r.getFiringPorts())
				if (!intface.contains(p))
					g = new Existential(new Node(p), g);
			setRules.add(new Rule(r.getSync(), g));
		}
		return new RulesBasedAutomaton(setRules);
	}

}
