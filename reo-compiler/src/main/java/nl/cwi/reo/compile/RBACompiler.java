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
import nl.cwi.reo.semantics.predicates.Function;
import nl.cwi.reo.semantics.predicates.MemCell;
import nl.cwi.reo.semantics.predicates.Negation;
import nl.cwi.reo.semantics.predicates.Node;
import nl.cwi.reo.semantics.predicates.Relation;
import nl.cwi.reo.semantics.predicates.Term;
import nl.cwi.reo.semantics.predicates.Variable;

public class RBACompiler {

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

		// Build set of input ports and known variables (memory cell without
		// prime and input ports)
		Set<Variable> doneVariables = new HashSet<Variable>();
		Set<Port> allInputPorts = new HashSet<Port>();

		for (Variable v : g.getFreeVariables()) {
			if (v instanceof Node && ((Node) v).isInput()) {
				allInputPorts.add(((Node) v).getPort());
				doneVariables.add(v);
			}
			if (v instanceof MemCell && !((MemCell) v).hasPrime())
				doneVariables.add(v);
		}

		Map<Variable, Term> assignements = new LinkedHashMap<Variable, Term>();
		List<Formula> guards = new ArrayList<Formula>();

		int nDoneVariables = -1;
		while (nDoneVariables < doneVariables.size()) {
			nDoneVariables = doneVariables.size();
			for (Formula l : literals) {

				if (l instanceof Equality) {
					Equality equality = (Equality) l;
					Term t1 = equality.getLHS();
					Term t2 = equality.getRHS();

					if (t1 instanceof Variable && doneVariables.containsAll(t2.getFreeVariables())) {
						if (!(t2 instanceof Function
								&& (((Function) t2).getName() == "*" || ((Function) t2).getValue() == null))) {
							assignements.put((Variable) t1, t2);
							literalsToRemove.add(l);
							doneVariables.add((Variable) t1);
							continue;
						} else {
							if ((t1 instanceof MemCell && ((MemCell) t1).hasPrime())) {
								assignements.put((Variable) t1, t2);
								literalsToRemove.add(l);
								doneVariables.add((Variable) t1);
							}
							if ((t1 instanceof MemCell && !((MemCell) t1).hasPrime())) {
								guards.add(l);
								literalsToRemove.add(l);
							}
							if ((t1 instanceof Node && !((Node)t1).getPort().isInput())) {
								guards.add(l);
								literalsToRemove.add(l);
							}
							continue;
						}

					}
					if (t2 instanceof Variable && doneVariables.containsAll(t1.getFreeVariables())) {
						if (!(t1 instanceof Function
								&& (((Function) t1).getName() == "*" || ((Function) t1).getValue() == null))) {
							assignements.put((Variable) t2, t1);
							literalsToRemove.add(l);
							doneVariables.add((Variable) t2);
							continue;
						} else {
							if ((t2 instanceof MemCell && ((MemCell) t2).hasPrime())) {
								assignements.put((Variable) t2, t1);
								literalsToRemove.add(l);
								doneVariables.add((Variable) t2);
							}
							if (t2 instanceof MemCell && !((MemCell) t2).hasPrime()) {
								guards.add(l);
								literalsToRemove.add(l);
							}
							continue;
						}
					}
				}

				/*
				 * Add a guarded command
				 */

				if (l instanceof Negation && ((Negation) l).getFormula() instanceof Equality) {
					Equality equality = (Equality) ((Negation) l).getFormula();
					Term t1 = equality.getLHS();
					Term t2 = equality.getRHS();
					if (t1 instanceof Variable && doneVariables.containsAll(t2.getFreeVariables())) {
						if ((t2 instanceof Function
								&& (((Function) t2).getName() == "*" || ((Function) t2).getValue() == null))) {
							if (t1 instanceof MemCell && !((MemCell) t1).hasPrime()) {
								guards.add(l);
								literalsToRemove.add(l);
							}
//							if (t1 instanceof Node) {
//								guards.add(l);
//								literalsToRemove.add(l);
//							}
							continue;
						}
					}
					if (t2 instanceof Variable && doneVariables.containsAll(t1.getFreeVariables())) {
						if ((t1 instanceof Function
								&& (((Function) t1).getName() == "*" || ((Function) t1).getValue() == null))) {
							if (t2 instanceof MemCell && !((MemCell) t2).hasPrime()) {
								guards.add(l);
								literalsToRemove.add(l);
							}
							continue;
						}
					}
				}

				if (doneVariables.containsAll(l.getFreeVariables())) {
					guards.add(l);
					literalsToRemove.add(l);
				}
			}
			for (Formula l : literalsToRemove) {
				literals.remove(l);
			}
			literalsToRemove.clear();

		}

		Formula guard = null;
		List<Formula> list = new ArrayList<>();
		for(Formula l : guards){
			if(!(l instanceof Relation && ((Relation) l).getValue().equals("true")))
				list.add(l);
		}
		guards = list;
		switch (guards.size()) {
		case 0:
			guard = new Relation("true", "true", null);
			break;
		case 1:
			guard = guards.get(0);
			break;
		default:
			guard = new Conjunction(guards);
			break;
		}

		Map<Node, Term> output = new HashMap<Node, Term>();

		Map<MemCell, Term> memory = new LinkedHashMap<MemCell, Term>();

		assignements = sort(assignements);
		List<Variable> keys = new ArrayList<>(assignements.keySet());
		
//		Collections.reverse(keys);		
		for (Variable v : keys) {
			if (v instanceof Node) {
				output.put((Node) v, assignements.get(v));
			}
			if (v instanceof MemCell) {
				memory.put((MemCell) v, assignements.get(v));
			}
		}

		/*
		 * For all ports, in the transition, 
		 * 	- peek value except for last peek, where it gets the value.
		 * For all ports at the interface (ie protocol is not consumer and producer),
		 * 	- transitivity over put and get on same port during the same transition.
		 */
		Map<Node, Term> output_substitution = new HashMap<Node, Term>(output);
		for(Node n : output.keySet()){
			Set<Variable> s = output.get(n).getFreeVariables();
			for(Variable v : s){
				if(output.containsKey(v)){
					output_substitution.put(n, output.get(n).Substitute(output.get(v), v));
				}
				if(memory.containsKey(v) && !(memory.get(v) instanceof Function && ((Function)memory.get(v)).getName().equals("*")))
					output_substitution.put(n, output.get(n).Substitute(memory.get(v), v));
			}
		}
		
		Map<MemCell, Term> mem_substitution = new LinkedHashMap<MemCell, Term>(memory);
		for(MemCell m : memory.keySet()){
			Set<Variable> s = memory.get(m).getFreeVariables();
			for(Variable v : s){
				if(output_substitution.containsKey(v)){
					mem_substitution.put(m, memory.get(m).Substitute(output_substitution.get(v), v));
				}
				if(memory.containsKey(v) && !(memory.get(v) instanceof Function && ((Function)memory.get(v)).getName().equals("*")))
					mem_substitution.put(m, memory.get(v).Substitute(memory.get(v), v));
			}
//			if(!mem_substitution.containsKey(m))
//				if(mem_substitution.get(m)!=null)
//					mem_substitution.put(m, memory.get(m));
		}
//		mem_substitution.putAll(null_mem);
		return new Transition(guard, output_substitution, mem_substitution, allInputPorts);
	}
	
	public static Map<Variable, Term> sort(Map<Variable, Term> assignements ){
		Map<Variable, Term> assign = new LinkedHashMap<Variable,Term>();
		List<Variable> var = new ArrayList<Variable>();
		for(Variable v : assignements.keySet()){
			if(assignements.get(v) instanceof Function && ((Function)assignements.get(v)).getValue().equals("null")){
				var.add(var.size(), v);
			}
			else
				var.add(0,v);
		}
		for(Variable v : var){
			assign.put(v, assignements.get(v));
		}
		return assign;
	}

}
