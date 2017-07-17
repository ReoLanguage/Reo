package nl.cwi.reo.runtime;

// TODO: Auto-generated Javadoc
/**
 * The Class PortFIFO1.
 *
 * @param <T>
 *            the generic type
 */
public class PortFIFO1<T> implements Port<T> {
	
	/** The producer. */
	private Component producer;
	
	/** The consumer. */
	private Component consumer;
	
	/** The put. */
	private volatile T put;

	/* (non-Javadoc)
	 * @see nl.cwi.reo.runtime.Port#setProducer(nl.cwi.reo.runtime.Component)
	 */
	public void setProducer(Component p) {
		producer = p;
	}

	/* (non-Javadoc)
	 * @see nl.cwi.reo.runtime.Port#setConsumer(nl.cwi.reo.runtime.Component)
	 */
	public void setConsumer(Component c) {
		consumer = c;
	}

	/* (non-Javadoc)
	 * @see nl.cwi.reo.runtime.Port#setGet()
	 */
	public void setGet() {
	}

	/* (non-Javadoc)
	 * @see nl.cwi.reo.runtime.Port#setPut(java.lang.Object)
	 */
	public void setPut(T datum) {
		put = datum;
	}

	/* (non-Javadoc)
	 * @see nl.cwi.reo.runtime.Port#take()
	 */
	public T take() {
		T datum = put;
		put = null;
		return datum;
	}

	/* (non-Javadoc)
	 * @see nl.cwi.reo.runtime.Port#hasGet()
	 */
	public boolean hasGet() {
		return put == null;
	}

	/* (non-Javadoc)
	 * @see nl.cwi.reo.runtime.Port#hasPut()
	 */
	public T hasPut() {
		return put;
	}

	/* (non-Javadoc)
	 * @see nl.cwi.reo.runtime.Port#peek()
	 */
	public T peek() {
		return put;
	}

	/* (non-Javadoc)
	 * @see nl.cwi.reo.runtime.Port#activateProducer()
	 */
	public void activateProducer() {
		producer.activate();
	}

	/* (non-Javadoc)
	 * @see nl.cwi.reo.runtime.Port#activateConsumer()
	 */
	public void activateConsumer() {
		consumer.activate();
	}

	/* (non-Javadoc)
	 * @see nl.cwi.reo.runtime.Output#put(java.lang.Object)
	 */
	public void put(T datum) {
		if (datum == null)
			throw new NullPointerException();
		put = datum;
		consumer.activate();
		while (put != null) {
		}
	}

	/* (non-Javadoc)
	 * @see nl.cwi.reo.runtime.Input#get()
	 */
	public T get() {
		producer.activate();
		while (put == null) {
		}
		T datum = put;
		put = null;
		producer.activate();
		return datum;
	}
}