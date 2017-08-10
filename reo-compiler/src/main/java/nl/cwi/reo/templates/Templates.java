package nl.cwi.reo.templates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.predicates.Conjunction;
import nl.cwi.reo.semantics.predicates.Formula;
import nl.cwi.reo.semantics.predicates.Formulas;
import nl.cwi.reo.semantics.predicates.MemoryVariable;
import nl.cwi.reo.semantics.predicates.PortVariable;
import nl.cwi.reo.semantics.predicates.Term;

public class Templates {
	
	public static Transition commanditify(Formula f) {

		Formula guard;
		List<Formula> clauses = new ArrayList<Formula>();
		Map<PortVariable, Term> output = new HashMap<>();
		Map<MemoryVariable, Term> update = null;
		Set<Port> ports = null;


		if (f instanceof Conjunction) {
			
		}

		guard = Formulas.conjunction(clauses);
		return new Transition(guard, output, update, ports);
	}

}
