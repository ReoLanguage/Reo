/**
 * 
 */
package nl.cwi.reo.compile.components;

/**
 * Behavour type of an executable Reo component.
 */
public enum Behavior {
	
	/**
	 * Passive.
	 */
	PASSIVE,

	/**
	 * Proactive.
	 */
	PROACTIVE,

	/**
	 * Reactive.
	 */
	REACTIVE;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		switch (this) {
		case PASSIVE:
			return "Passive";
		case PROACTIVE:
			return "Proactive";
		case REACTIVE:
			return "Reactive";
		default:
			throw new IllegalArgumentException();
		}
	}
}
