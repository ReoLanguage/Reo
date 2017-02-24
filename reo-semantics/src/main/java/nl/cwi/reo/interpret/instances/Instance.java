package nl.cwi.reo.interpret.instances;

import java.util.Map;
import java.util.Objects;
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
public final class Instance<T extends Semantics<T>> implements Value {

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
	public Instance(ReoConnector<T> connector, Set<Set<Identifier>> unifications) {
		this.connector = connector;
		this.unifications = unifications;
	}
	
	/**
	 * Relabels the set of links of this subcomponent by renaming all link targets 
	 * names according to renaming map, and hiding all ports that are not renamed.
	 * @param joins		renaming map
	 * @return copy of this instance with renamed interface
	 */
	public Instance<T> reconnect(Map<Port, Port> joins) {
		return new Instance<T>(connector.reconnect(joins), unifications);
	}
	
	/**
	 * Gets the connector of this instance.
	 * @return connector of this instance
	 */
	public ReoConnector<T> getConnector(){
		return connector;
	}
	
	/**
	 * Gets the unifications in this instance.
	 * @return unifications in this instance
	 */
	public Set<Set<Identifier>> getUnifications(){
		return unifications;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return connector.toString() + unifications.toString();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object other) {
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof Instance<?>)) return false;
	    Instance<?> p = (Instance<?>)other;
	   	return Objects.equals(this.connector, p.connector) && 
	   			Objects.equals(this.unifications, p.unifications);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
	    return Objects.hash(this.connector, this.unifications);
	}

}
