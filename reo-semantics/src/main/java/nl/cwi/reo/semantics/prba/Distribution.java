package nl.cwi.reo.semantics.prba;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.typetags.TypeTag;
import nl.cwi.reo.semantics.predicates.Term;
import nl.cwi.reo.semantics.predicates.Variable;
import nl.cwi.reo.util.Monitor;

/**
 * A Distribution over a set of terms.
 */
public final class Distribution implements Term {

	/**
	 * Flag for string template.
	 */
	public static final boolean distribution = true;

	/**
	 * The distribution that maps each term to a probability that is also
	 * expressed as a term.
	 */
	private final Map<Term, Term> distr;
	
	/**
	 * Free variables of this term.
	 */
	private final Set<Variable> vars;

	/**
	 * Instantiates a new distribution.
	 *
	 * @param distr
	 *            the distribution
	 */
	public Distribution(Map<Term, Term> distr) {
		this.distr = Collections.unmodifiableMap(distr);
		Set<Variable> vars = new HashSet<Variable>();
		for (Map.Entry<Term, Term> entry : distr.entrySet()) {
			vars.addAll(entry.getKey().getFreeVariables());
			vars.addAll(entry.getValue().getFreeVariables());
		}
		this.vars = Collections.unmodifiableSet(vars);
	}

	/**
	 * Gets the distribution.
	 *
	 * @return the distribution
	 */
	public Map<Term, Term> getDistribution() {
		return distr;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Term rename(Map<Port, Port> links) {
		Map<Term, Term> _distr = new HashMap<>();
		for (Map.Entry<Term, Term> entry : distr.entrySet())
			_distr.put(entry.getKey().rename(links), entry.getValue());
		return new Distribution(_distr);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Term substitute(Term t, Variable x) {
		Map<Term, Term> _distr = new HashMap<>();
		for (Map.Entry<Term, Term> entry : distr.entrySet()) {
			Term k = entry.getKey().substitute(t, x);
			Term v = entry.getValue().substitute(t, x);
			_distr.put(k, v);
		}
		return new Distribution(_distr);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Variable> getFreeVariables() {
		return vars;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public @Nullable TypeTag getTypeTag() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String s = "[";
		Iterator<Map.Entry<Term, Term>> iter = distr.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<Term, Term> entry = iter.next();
			s = s + entry.getValue() + ":" + entry.getKey();
			if (iter.hasNext())
				s += ", ";
		}
		return s + "]";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public @Nullable Term evaluate(Scope s, Monitor m) {
		Map<Term, Term> _distr = new HashMap<>();
		for (Map.Entry<Term, Term> entry : distr.entrySet()) {
			Term u = entry.getKey().evaluate(s, m);
			Term v = entry.getValue().evaluate(s, m);
			if (u == null || v == null)
				return null;
			_distr.put(u, v);
		}
		return new Distribution(_distr);
	}

	@Override
	public Term setTypeTag(TypeTag t) {
		// TODO Auto-generated method stub
		return null;
	}

}
