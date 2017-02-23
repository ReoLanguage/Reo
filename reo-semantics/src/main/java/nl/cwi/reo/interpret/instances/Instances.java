package nl.cwi.reo.interpret.instances;

import java.util.List;
import java.util.Set;

import nl.cwi.reo.interpret.connectors.Semantics;
import nl.cwi.reo.interpret.connectors.ReoConnector;
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
	private final List<ReoConnector<T>> connectors;

	/**
	 * A set of node unifications.
	 */
	private final Set<Set<Identifier>> unifications;
	
	/**
	 * Constructs a new set.
	 * @param connector		Reo connector
	 * @param unifications	node unifications
	 */

	public Instances(List<ReoConnector<T>> connectors, Set<Set<Identifier>> unifications) {
		this.connectors = connectors;
		this.unifications = unifications;
	}
	
	public List<ReoConnector<T>> getConnector(){
		return connectors;
	}
	public Set<Set<Identifier>> getUnifications(){
		return unifications;
	}

}
