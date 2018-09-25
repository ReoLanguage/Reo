package nl.cwi.reo.semantics;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import nl.cwi.reo.interpret.Atom;
import nl.cwi.reo.interpret.ports.Port;

/**
 * Implementation of a semantics for Reo components.
 * 
 * @param <T>
 *            semantics
 */
public interface Semantics<T> extends Atom {

	/**
	 * Gets the default semantics with a given interface. A unit has a
	 * asynchronous action for each port in its interface.
	 * 
	 * @param iface
	 *            interface of the component
	 * @return Default semantics with given interface.
	 */
	public T getDefault(Set<Port> iface);

	/**
	 * Composes a list of components into a single component.
	 * 
	 * @param components
	 *            list of components
	 * @return Composition of the list of components.
	 */
	public T compose(List<T> components);

	/**
	 * Restricts the interface of this component to a given collection of ports.
	 * 
	 * @param intface
	 *            collection of ports.
	 * @return Restriction of this component to the collection of ports.
	 */
	public T restrict(Collection<? extends Port> intface);

}