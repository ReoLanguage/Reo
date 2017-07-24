package nl.cwi.reo.runtime;

// TODO: Auto-generated Javadoc
/**
 * The Interface Port.
 *
 * @param <T>
 *            the generic type
 */
public interface Port<T> extends Input<T>, Output<T> {
	
	/**
	 * Sets the producer.
	 *
	 * @param p
	 *            the new producer
	 */
	public void setProducer(Component p);

	/**
	 * Sets the consumer.
	 *
	 * @param c
	 *            the new consumer
	 */
	public void setConsumer(Component c);

	/**
	 * Sets the put.
	 *
	 * @param datum
	 *            the new put
	 */
	public void setPut(T datum);

	/**
	 * Sets the get.
	 */
	public void setGet();

	/**
	 * Checks for put.
	 *
	 * @return the t
	 */
	public T hasPut();

	/**
	 * Peek.
	 *
	 * @return the t
	 */
	public T peek();

	/**
	 * Checks for get.
	 *
	 * @return true, if successful
	 */
	public boolean hasGet();

	/**
	 * Take.
	 *
	 * @return the t
	 */
	public T take();

	/**
	 * Activate producer.
	 */
	public void activateProducer();

	/**
	 * Activate consumer.
	 */
	public void activateConsumer();
}