package nl.cwi.reo.runtime;

public interface Component extends Runnable {
	
	/**
	 * Activates this component.
	 */
	public void activate();
}
