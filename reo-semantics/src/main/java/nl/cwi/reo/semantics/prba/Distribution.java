package nl.cwi.reo.semantics.prba;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.typetags.TypeTag;
import nl.cwi.reo.semantics.predicates.Term;
import nl.cwi.reo.semantics.predicates.Variable;

/**
 * A Distribution over a set of terms.
 */
public class Distribution implements Term {
	
	/**
	 * Flag for string template.
	 */
	public static final boolean distribution = true;
	
	/** The distribution. */
	private Map<Term, Double> distr;
	
	/**
	 * Instantiates a new distribution.
	 *
	 * @param distr
	 *            the distribution
	 */
	public Distribution(Map<Term, Double> distr) {
		this.distr = Collections.unmodifiableMap(distr);
	}
	
	/**
	 * Gets the distribution.
	 *
	 * @return the distribution
	 */
	public Map<Term, Double> getDistribution() {
		return distr;
	}

	/**
	 * {@inheritDoc} 
	 */
	@Override
	public boolean hasOutputPorts() {
		for (Term t : distr.keySet()) 
			if (t.hasOutputPorts())
				return true;
		return false;
	}

	/**
	 * {@inheritDoc} 
	 */
	@Override
	public Term rename(Map<Port, Port> links) {
		Map<Term, Double> _distr = new HashMap<>();
		for(Map.Entry<Term, Double> entry : distr.entrySet())
			_distr.put(entry.getKey().rename(links), entry.getValue());
		return new Distribution(_distr);
	}

	/**
	 * {@inheritDoc} 
	 */
	@Override
	public Term substitute(Term t, Variable x) {
		Map<Term, Double> _distr = new HashMap<>();
		for(Map.Entry<Term, Double> entry : distr.entrySet())
			_distr.put(entry.getKey().substitute(t, x), entry.getValue());
		return new Distribution(_distr);
	}

	/**
	 * {@inheritDoc} 
	 */
	@Override
	public Set<Variable> getFreeVariables() {
		Set<Variable> vars = new HashSet<>();
		for (Term t : distr.keySet()) 
			vars.addAll(t.getFreeVariables());
		return vars;
	}

	/**
	 * {@inheritDoc} 
	 */
	@Override
	public @Nullable TypeTag getTypeTag() {
		return null;
	}

}
