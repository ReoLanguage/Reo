package nl.cwi.reo.templates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.predicates.Conjunction;
import nl.cwi.reo.semantics.predicates.Equality;
import nl.cwi.reo.semantics.predicates.Formula;
import nl.cwi.reo.semantics.predicates.Formulas;
import nl.cwi.reo.semantics.predicates.MemoryVariable;
import nl.cwi.reo.semantics.predicates.PortVariable;
import nl.cwi.reo.semantics.predicates.Relation;
import nl.cwi.reo.semantics.predicates.Term;
import nl.cwi.reo.semantics.predicates.Variable;

public class Templates {

	public static Transition commanditify(Formula f) {

		List<Formula> guard = new ArrayList<Formula>();
		Map<PortVariable, Term> output = new HashMap<>();
		Map<MemoryVariable, Term> update = new HashMap<>();
		Set<Port> ports = new HashSet<>();

		if (f instanceof Conjunction) {
			for (Formula L : ((Conjunction) f).getClauses()) {
				if (L instanceof Relation) {
					Relation R = (Relation) L;
					if (R.getName().equals("=")) {

						// Find the outputs and memory updates
						Set<Term> vars = new HashSet<>();
						Set<PortVariable> outs = new HashSet<>();
						Set<MemoryVariable> upds = new HashSet<>();
						for (Term t : R.getArgs()) {
							if (t instanceof PortVariable) {
								PortVariable p = (PortVariable) t;
								if (!p.isInput()) {
									outs.add(p);
									continue;
								}
							} else if (t instanceof MemoryVariable) {
								MemoryVariable m = (MemoryVariable) t;
								if (m.hasPrime()) {
									upds.add(m);
									continue;
								}
							}
							vars.add(t);
						}

						Iterator<Term> iter = vars.iterator();
						Term t0 = null;
						while (iter.hasNext()) {
							Term u = iter.next();
							
							if (t0 == null)
								t0 = u;
							else 
								guard.add(new Equality(t0, u));
							
							for (PortVariable p : outs) {
								output.put(p, t0);
							}
						}
					}
				}

			}
		}

		Formula g = Formulas.conjunction(guard);
		return new Transition(g, output, update, ports);
	}

}
