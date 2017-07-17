package nl.cwi.reo.semantics.seepageautomata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.List;
import java.util.Set;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.automata.Label;
import nl.cwi.reo.util.Monitor;

// TODO: Auto-generated Javadoc
/**
 * The Class SeepageFunction.
 */
public class SeepageFunction implements Label<SeepageFunction> {

	/** The equations. */
	public final List<Map<String, Set<Set<String>>>> equations;

	/**
	 * Instantiates a new seepage function.
	 */
	public SeepageFunction() {
		this.equations = new ArrayList<Map<String, Set<Set<String>>>>();
	}

	/**
	 * Instantiates a new seepage function.
	 *
	 * @param eqs
	 *            the eqs
	 */
	public SeepageFunction(List<Map<String, Set<Set<String>>>> eqs) {
		this.equations = eqs;
	}

	/**
	 * Instantiates a new seepage function.
	 *
	 * @param S
	 *            the s
	 */
	public SeepageFunction(SeepageFunction S) {
		this.equations = S.equations;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.semantics.automata.Label#compose(java.util.List)
	 */
	@Override
	public SeepageFunction compose(List<SeepageFunction> lbls) {
		List<Map<String, Set<Set<String>>>> eqs_all = new ArrayList<Map<String, Set<Set<String>>>>();
		for (SeepageFunction F : lbls)
			eqs_all.addAll(F.equations);
		return new SeepageFunction(eqs_all);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.semantics.automata.Label#restrict(java.util.Collection)
	 */
	@Override
	public SeepageFunction restrict(Collection<? extends Port> intface) {
		return new SeepageFunction(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.semantics.automata.Label#rename(java.util.Map)
	 */
	@Override
	public SeepageFunction rename(Map<Port, Port> links) {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.semantics.automata.Label#getLabel(java.util.Set)
	 */
	@Override
	public SeepageFunction getLabel(Set<Port> N) {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.semantics.automata.Label#evaluate(nl.cwi.reo.interpret.Scope,
	 * nl.cwi.reo.util.Monitor)
	 */
	@Override
	public SeepageFunction evaluate(Scope s, Monitor m) {
		return this;
	}
}
