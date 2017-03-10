package nl.cwi.reo.semantics.imperativeautomata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.automata.Label;
import nl.cwi.reo.util.Monitor;

public final class IALabel implements Label<IALabel> {
	
	/**
	 * Data constraint.
	 */
	private final List<Assignment> commands;
	
	/**
	 * Constructs a true data constraint label.
	 */
	public IALabel() {
		this.commands = new ArrayList<Assignment>();
	}
	
	/**
	 * Constructs a data constraint label from a given formula.
	 * @param dc	data constraint formula
	 */
	public IALabel(Assignment command) {
		if (command == null)
			throw new NullPointerException();
		this.commands = Arrays.asList(command);
	}

	/**
	  * {@inheritDoc}
	  */
	@Override
	public IALabel compose(List<IALabel> lbls) {
		throw new UnsupportedOperationException();
	}

	/**
	  * {@inheritDoc}
	  */
	@Override
	public IALabel restrict(Collection<? extends Port> intface) {
		throw new UnsupportedOperationException();
	}

	/**
	  * {@inheritDoc}
	  */
	@Override
	public IALabel rename(Map<Port, Port> links) {
		throw new UnsupportedOperationException();
	}

	/**
	  * {@inheritDoc}
	  */
	@Override
	public IALabel getLabel(Set<Port> N) {
		throw new UnsupportedOperationException();
	}

	/**
	  * {@inheritDoc}
	  */
	@Override
	public IALabel evaluate(Scope s, Monitor m) {
		throw new UnsupportedOperationException();
	}

	/**
	  * {@inheritDoc}
	  */
	@Override
	public String toString() {
		return commands.toString();
	}
}
