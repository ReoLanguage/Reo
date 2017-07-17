package nl.cwi.reo.semantics.portautomata;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.automata.Label;
import nl.cwi.reo.util.Monitor;

// TODO: Auto-generated Javadoc
/**
 * The Class NullLabel.
 */
public class NullLabel implements Label<NullLabel> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.semantics.automata.Label#compose(java.util.List)
	 */
	@Override
	public NullLabel compose(List<NullLabel> lbls) {
		return new NullLabel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.semantics.automata.Label#restrict(java.util.Collection)
	 */
	@Override
	public NullLabel restrict(Collection<? extends Port> intface) {
		return new NullLabel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.semantics.automata.Label#rename(java.util.Map)
	 */
	@Override
	public NullLabel rename(Map<Port, Port> links) {
		return new NullLabel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.semantics.automata.Label#getLabel(java.util.Set)
	 */
	@Override
	public NullLabel getLabel(Set<Port> N) {
		return new NullLabel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.semantics.automata.Label#evaluate(nl.cwi.reo.interpret.Scope,
	 * nl.cwi.reo.util.Monitor)
	 */
	@Override
	public NullLabel evaluate(Scope s, Monitor m) {
		return new NullLabel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "-";
	}
}
