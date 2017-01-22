package nl.cwi.reo.interpret.semantics;

import nl.cwi.reo.interpret.signatures.PrioType;
import nl.cwi.reo.semantics.Semantics;

public class Port<T extends Semantics<T>> {
	
	/**
	 * Port name.
	 */
	private final String name;
	
	/**
	 * Component.
	 */
	private final Component1<T> component;
	
	/**
	 * Input/output type.
	 */
	private final IOType type;
	
	/**
	 * Priority type.
	 */
	private final PrioType prio;
	
	/**
	 * Constructs a new port
	 * @param component
	 * @param name
	 * @param type
	 */
	public Port(String name, Component1<T> component, IOType type, PrioType prio) {
		if (name == null || component == null || type == null || prio == null)
			throw new NullPointerException();
		this.name = name;
		this.component = component;
		this.type = type;
		this.prio = prio;
	}
	
	/**
	 * Constructs a new port
	 * @param component
	 * @param name
	 * @param type
	 */
	public Port(String name, Component1<T> component) {
		if (name == null || component == null)
			throw new NullPointerException();
		this.name = name;
		this.component = component;
		this.type = IOType.UNKNOWN;
		this.prio = PrioType.NONE;
	}
	
	public String getName() {
		return name;
	}
	
	public Component1<T> getComponent() {
		return component;
	}
	
	public IOType getIOType() {
		return type;
	}
	
	public PrioType getPrioType() {
		return prio;
	}
}
