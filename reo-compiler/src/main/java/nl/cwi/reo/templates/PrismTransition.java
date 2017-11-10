package nl.cwi.reo.templates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.typetags.TypeTags;
import nl.cwi.reo.interpret.values.IntegerValue;
import nl.cwi.reo.semantics.prba.Distribution;
import nl.cwi.reo.semantics.predicates.Constant;
import nl.cwi.reo.semantics.predicates.Formula;
import nl.cwi.reo.semantics.predicates.Function;
import nl.cwi.reo.semantics.predicates.MemoryVariable;
import nl.cwi.reo.semantics.predicates.PortVariable;
import nl.cwi.reo.semantics.predicates.Term;

/**
 * The Class Transition.
 */

public final class PrismTransition extends Transition{


	public Map<Map<MemoryVariable, Term>, Term> PRISMUpdate;
	/**
	 * Constructs a new transition.
	 * 
	 * @param guard
	 *            guard
	 * @param output
	 *            output provided at output ports
	 * @param memory
	 *            update of the memory cells
	 */
//	public PrismTransition(Formula guard, Map<PortVariable, Term> output, Map<MemoryVariable, Term> memory) {
//		super(guard, output, memory);
//		getPRISMUpdate();
//	}

	/**
	 * Instantiates a new transition.
	 *
	 * @param guard
	 *            the guard
	 * @param output
	 *            the output
	 * @param memory
	 *            the memory
	 * @param input
	 *            the input
	 */
	public PrismTransition(Formula guard, Map<PortVariable, Term> output, Map<MemoryVariable, Term> memory,
			Set<Port> input) {
		super(guard, output, memory, input);
		getPRISMUpdate();
	}

	/**
	 * Computes
	 * 
	 * @return
	 */
	private void getPRISMUpdate() {

		Map<Map<MemoryVariable, Term>, Term> update = new HashMap<>();

		List<Iterable<Map.Entry<Map<MemoryVariable, Term>, Term>>> iterables = new ArrayList<>();
		List<Iterator<Map.Entry<Map<MemoryVariable, Term>, Term>>> iterators = new ArrayList<>();

		// Initialize the iterables.
		for (Map.Entry<MemoryVariable, Term> entry : this.getMemory().entrySet()) {

			// Flatten the distribution terms in the memory update
			Distribution d = flattenDistribution(entry.getValue());
			Map<Map<MemoryVariable, Term>, Term> iterable = new LinkedHashMap<>();
			for (Map.Entry<Term, Term> e : d.getDistribution().entrySet()) {
				Map<MemoryVariable, Term> upd = new HashMap<>();
				upd.put(entry.getKey(), e.getKey());
				iterable.put(upd, e.getValue());
			}
			iterables.add(iterable.entrySet());
		}

		// Initialize this iterators.
		for (int i = 0; i < iterables.size(); i++)
			iterators.add(i, iterables.get(i).iterator());

		// Current tuple
		List<Map.Entry<Map<MemoryVariable, Term>, Term>> tuple = new ArrayList<>();
		for (int i = 0; i < iterators.size(); i++) 
			tuple.add(i, iterators.get(i).next());

		while (true) {
			Map<MemoryVariable, Term> distr = new HashMap<>();
			List<Term> prod = new ArrayList<>();
			for (Map.Entry<Map<MemoryVariable, Term>, Term> e : tuple) {
				distr.putAll(e.getKey());
				prod.add(e.getValue());
			}
			update.put(distr, new Function("*", prod, true, TypeTags.Decimal));
			
			int k;
			for (k = 0; k < iterators.size(); k++)
				if (iterators.get(k).hasNext())
					break;
			if (k == iterators.size())
				break;
			tuple.set(k, iterators.get(k).next());
			for (int i = 0; i < k; i++) {
				iterators.set(i, iterables.get(i).iterator());
				tuple.set(i, iterators.get(i).next());
			}
		}

		this.PRISMUpdate = update;
	}
	
	private static Distribution flattenDistribution(Term t) {
		if (t instanceof Distribution) {
			Map<Term, Term> newDistr = new HashMap<>();
			Distribution d = (Distribution) t;
			for (Map.Entry<Term, Term> entry : d.getDistribution().entrySet()) {
				Term f = flattenDistribution(entry.getKey());
				if (f instanceof Distribution) {
					for (Map.Entry<Term, Term> ef : ((Distribution) f).getDistribution().entrySet())
						newDistr.put(ef.getKey(), new Function("*", Arrays.asList(entry.getValue(), ef.getValue()),
								true, ef.getValue().getTypeTag()));
				} else {
					newDistr.put(f, entry.getValue());
				}
			}
			return new Distribution(newDistr);
		}
		Map<Term, Term> trivialDistr = new HashMap<>();
		trivialDistr.put(t, new Constant(new IntegerValue(1)));
		return new Distribution(trivialDistr);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.getGuard(), this.getOutput(), this.getMemory(), this.getInput());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.getInput() + " " + this.getGuard() + " -> " + this.getOutput() + ", " + this.getMemory();
	}
}
