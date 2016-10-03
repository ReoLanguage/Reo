package nl.cwi.reo.runtime.java;

/**
 * An input port.
 * @param <T> 		type of data
 */
public interface Input<T> {
	
	/**
	 * Retrieves a datum from this port. This method blocks until 
	 * another component offered datum at this port.
	 * @return an offered datum.
	 */
	public T get();
}
