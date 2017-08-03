package nl.cwi.reo.compile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.cwi.reo.compile.components.Transition;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.predicates.Conjunction;
import nl.cwi.reo.semantics.predicates.Equality;
import nl.cwi.reo.semantics.predicates.Formula;
import nl.cwi.reo.semantics.predicates.MemoryVariable;
import nl.cwi.reo.semantics.predicates.Negation;
import nl.cwi.reo.semantics.predicates.NullValue;
import nl.cwi.reo.semantics.predicates.PortVariable;
import nl.cwi.reo.semantics.predicates.Relation;
import nl.cwi.reo.semantics.predicates.Term;
import nl.cwi.reo.semantics.predicates.TruthValue;
import nl.cwi.reo.semantics.predicates.Variable;

// TODO: Auto-generated Javadoc
/**
 * The Class RBACompiler.
 */
public class RBACompiler {

	/**
	 * Commandifies the formula.
	 *
	 * @param g
	 *            the formula
	 * @return the transition
	 */
	public static Transition commandify(Formula g) {

		List<Formula> literals = new ArrayList<Formula>();
		List<Formula> literalsToRemove = new ArrayList<Formula>();

		if (g instanceof Conjunction)
			literals = ((Conjunction) g).getClauses();
		if (g instanceof Equality)
			literals.add(g);

		// Remove from literals all equalities as x?=x!
		for (Formula l : literals) {
			if (l instanceof Equality) {
				Equality equality = (Equality) l;
				Term t1 = equality.getLHS();
				Term t2 = equality.getRHS();
				if (t1.equals(t2)) {
					literalsToRemove.add(l);
				}
			}
		}
		for (Formula l : literalsToRemove) {
			literals.remove(l);
		}
		literalsToRemove.clear();

		// The set of variables in the formula whose value is known.
		Set<Variable> knownVariables = new HashSet<Variable>();

		Set<Port> allInputPorts = new HashSet<Port>();

		// Add all input port variables and current memory cell variables to the
		// set of known variables.
		for (Variable v : g.getFreeVariables()) {
			if (v instanceof PortVariable && ((PortVariable) v).isInput()) {
				allInputPorts.add(((PortVariable) v).getPort());
				knownVariables.add(v);
			}
			if (v instanceof MemoryVariable && !((MemoryVariable) v).hasPrime())
				knownVariables.add(v);
		}

		Map<Variable, Term> assignements = new LinkedHashMap<>();
		List<Formula> guards = new ArrayList<Formula>();

		int nDoneVariables = -1;
		while (nDoneVariables < knownVariables.size()) {
			nDoneVariables = knownVariables.size();
			for (Formula l : literals) {
				if (l instanceof Equality) {

					// If the literal is an equality, then we may learn the
					// value of a different variable.
					Equality equality = (Equality) l;
					Term t1 = equality.getLHS();
					Term t2 = equality.getRHS();

					if (t1 instanceof Variable && knownVariables.containsAll(t2.getFreeVariables())) {
						if (t2 instanceof NullValue) {
							if ((t1 instanceof MemoryVariable && ((MemoryVariable) t1).hasPrime())) {
								assignements.put((Variable) t1, t2);
								literalsToRemove.add(l);
								knownVariables.add((Variable) t1);
							}
							if ((t1 instanceof MemoryVariable && !((MemoryVariable) t1).hasPrime())) {
								guards.add(l);
								literalsToRemove.add(l);
							}
							if ((t1 instanceof PortVariable)) {
								if (allInputPorts.contains(((PortVariable) t1).getPort()))
									allInputPorts.remove(((PortVariable) t1).getPort());
								guards.add(l);
								literalsToRemove.add(l);
							}
							continue;
						} else {
							assignements.put((Variable) t1, t2);
							literalsToRemove.add(l);
							knownVariables.add((Variable) t1);
							continue;
						}

					} else if (t2 instanceof Variable && knownVariables.containsAll(t1.getFreeVariables())) {
						if (t1 instanceof NullValue) {
							if ((t2 instanceof MemoryVariable && ((MemoryVariable) t2).hasPrime())) {
								assignements.put((Variable) t2, t1);
								literalsToRemove.add(l);
								knownVariables.add((Variable) t2);
							}
							if (t2 instanceof MemoryVariable && !((MemoryVariable) t2).hasPrime()) {
								guards.add(l);
								literalsToRemove.add(l);
							}
							continue;
						} else {
							assignements.put((Variable) t2, t1);
							literalsToRemove.add(l);
							knownVariables.add((Variable) t2);
							continue;
						}
					}
				} else if (l instanceof Negation && ((Negation) l).getFormula() instanceof Equality) {
					Equality equality = (Equality) ((Negation) l).getFormula();
					Term t1 = equality.getLHS();
					Term t2 = equality.getRHS();
					if (t1 instanceof Variable && knownVariables.containsAll(t2.getFreeVariables())) {
						if (t2 instanceof NullValue) {
							if (t1 instanceof MemoryVariable && !((MemoryVariable) t1).hasPrime()) {
								guards.add(l);
								literalsToRemove.add(l);
							}
							continue;
						}
					}
					if (t2 instanceof Variable && knownVariables.containsAll(t1.getFreeVariables())) {
						if (t1 instanceof NullValue) {
							if (t2 instanceof MemoryVariable && !((MemoryVariable) t2).hasPrime()) {
								guards.add(l);
								literalsToRemove.add(l);
							}
							continue;
						}
					}
				}

				if (knownVariables.containsAll(l.getFreeVariables())) {
					guards.add(l);
					literalsToRemove.add(l);
				}
			}

			for (Formula l : literalsToRemove)
				literals.remove(l);
			literalsToRemove.clear();

		}

		Formula guard = null;
		List<Formula> list = new ArrayList<>();
		for (Formula l : guards) {
			if (!(l instanceof Relation && ((Relation) l).getValue().equals("true")))
				list.add(l);
		}
		guards = list;
		switch (guards.size()) {
		case 0:
			guard = new TruthValue(true);
			break;
		case 1:
			guard = guards.get(0);
			break;
		default:
			guard = Conjunction.conjunction(guards);
			break;
		}

		Map<PortVariable, Term> output = new HashMap<PortVariable, Term>();

		Map<MemoryVariable, Term> memory = new LinkedHashMap<MemoryVariable, Term>();

		assignements = sort(assignements);
		List<Variable> keys = new ArrayList<>(assignements.keySet());

		// Collections.reverse(keys);
		for (Variable v : keys) {
			if (v instanceof PortVariable) {
				output.put((PortVariable) v, assignements.get(v));
			}
			if (v instanceof MemoryVariable) {
				memory.put((MemoryVariable) v, assignements.get(v));
			}
		}

		/*
		 * For all ports, in the transition, - peek value except for last peek,
		 * where it gets the value. For all ports at the interface (ie protocol
		 * is not consumer and producer), - transitivity over put and get on
		 * same port during the same transition.
		 */
		Map<PortVariable, Term> output_substitution = new HashMap<PortVariable, Term>(output);
		for (PortVariable n : output.keySet()) {
			Set<Variable> s = output.get(n).getFreeVariables();
			for (Variable v : s) {
				if (output.containsKey(v))
					output_substitution.put(n, output.get(n).substitute(output.get(v), v));
				if (memory.containsKey(v) && !(memory.get(v) instanceof NullValue))
					output_substitution.put(n, output.get(n).substitute(memory.get(v), v));
			}
		}

		Map<MemoryVariable, Term> mem_substitution = new LinkedHashMap<MemoryVariable, Term>(memory);
		for (MemoryVariable m : memory.keySet()) {
			Set<Variable> s = memory.get(m).getFreeVariables();
			for (Variable v : s) {
				if (output_substitution.containsKey(v))
					mem_substitution.put(m, memory.get(m).substitute(output_substitution.get(v), v));
				if (memory.containsKey(v) && !(memory.get(v) instanceof NullValue))
					mem_substitution.put(m, memory.get(v).substitute(memory.get(v), v));
			}
		}
		return new Transition(guard, output_substitution, mem_substitution, allInputPorts);
	}

	/**
	 * Sort.
	 *
	 * @param assignements
	 *            the assignements
	 * @return the map
	 */
	public static Map<Variable, Term> sort(Map<Variable, Term> assignements) {
		Map<Variable, Term> assign = new LinkedHashMap<Variable, Term>();
		List<Variable> var = new ArrayList<Variable>();
		for (Variable v : assignements.keySet()) {
			if (assignements.get(v) instanceof NullValue) {
				var.add(var.size(), v);
			} else
				var.add(0, v);
		}
		for (Variable v : var) {
			assign.put(v, assignements.get(v));
		}
		return assign;
	}

}
