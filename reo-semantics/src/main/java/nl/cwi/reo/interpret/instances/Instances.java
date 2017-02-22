package nl.cwi.reo.interpret.instances;

import java.util.Map;

import nl.cwi.reo.interpret.connectors.CompositeReoConnector;
import nl.cwi.reo.interpret.connectors.Semantics;
import nl.cwi.reo.interpret.terms.Term;
import nl.cwi.reo.interpret.variables.Identifier;

/**
 * A Reo connector with a set of node unifications.
 * @param <T> Reo semantics type
 */
public final class Instances<T extends Semantics<T>> implements Term{

	/**
	 * A Reo connector.
	 */
	private final CompositeReoConnector<T> connector;
	
	/**
	 * A set of node unifications.
	 */
	private final Map<Identifier, Identifier> unifications;
	
	/**
	 * Constructs a new set.
	 * @param connector		Reo connector
	 * @param unifications	node unifications
	 */
	public Instances(CompositeReoConnector<T> connector, Map<Identifier, Identifier> unifications) {
		this.connector = connector;
		this.unifications = unifications;
	}

}
