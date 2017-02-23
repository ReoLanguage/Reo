package nl.cwi.reo.interpret.instances;

import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.cwi.reo.interpret.connectors.CompositeReoComponent;
import nl.cwi.reo.interpret.connectors.Semantics;
import nl.cwi.reo.interpret.connectors.ReoComponent;
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
	private final List<ReoComponent<T>> connectors;
	
	/**
	 * A set of node unifications.
	 */
	private final Set<Set<Identifier>> unifications;
	
	/**
	 * Constructs a new set.
	 * @param connector		Reo connector
	 * @param unifications	node unifications
	 */
	public Instances(List<ReoComponent<T>> connectors, Set<Set<Identifier>> unifications) {
		this.connectors = connectors;
		this.unifications = unifications;
	}
	
	public List<ReoComponent<T>> getConnector(){
		return connectors;
	}
	public Set<Set<Identifier>> getUnifications(){
		return unifications;
	}

}
