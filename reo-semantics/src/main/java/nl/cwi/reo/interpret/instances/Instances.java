package nl.cwi.reo.interpret.instances;

import java.util.Map;
import java.util.Set;

import nl.cwi.reo.interpret.connectors.Semantics;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.connectors.ReoConnector;
import nl.cwi.reo.interpret.values.Value;
import nl.cwi.reo.interpret.variables.Identifier;

/**
 * A Reo connector with a set of node unifications.
 * @param <T> Reo semantics type
 */
public final class Instances<T extends Semantics<T>> implements Value {

	/**
	 * A Reo connector.
	 */
	private final ReoConnector<T> connector;

	/**
	 * A set of node unifications.
	 */
	private final Set<Set<Identifier>> unifications;
	
	/**
	 * Constructs a new set.
	 * @param connector		Reo connector
	 * @param unifications	node unifications
	 */
	public Instances(ReoConnector<T> connector, Set<Set<Identifier>> unifications) {
		this.connector = connector;
		this.unifications = unifications;
	}
	
	public Instances<T> reconnect(Map<Port, Port> joins) {
		return new Instances<T>(connector.reconnect(joins), unifications);
	}
	
	public ReoConnector<T> getConnector(){
		return connector;
	}
	
	public Set<Set<Identifier>> getUnifications(){
		return unifications;
	}

}
