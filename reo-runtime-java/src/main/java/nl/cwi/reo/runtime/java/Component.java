package nl.cwi.reo.runtime.java;

public interface Component extends Runnable {
	
	/**
	 * Activates this component.
	 */
	public void activate();
}
