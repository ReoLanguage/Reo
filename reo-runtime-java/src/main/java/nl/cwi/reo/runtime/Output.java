package nl.cwi.reo.runtime;

/**
 * An output port.
 * @param <T> 		type of data
 */
public interface Output<T> {
	
	/**
	 * Offers a datum to this port. This method until 
	 * another component takes the offered datum.
	 * @param datum		offered datum
	 */
	public void put(T datum);
}
