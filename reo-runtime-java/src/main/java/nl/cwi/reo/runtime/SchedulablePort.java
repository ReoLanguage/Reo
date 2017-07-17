package nl.cwi.reo.runtime;

// TODO: Auto-generated Javadoc
/**
 * The Class SchedulablePort.
 *
 * @param <T>
 *            the generic type
 */
public class SchedulablePort<T> implements Port<T> {
	
	/** The prod. */
	private Component prod;
	
	/** The cons. */
	private Component cons;
	
	/** The put. */
	private volatile T put;
	
	/** The get. */
	private volatile boolean get;

	/* (non-Javadoc)
	 * @see nl.cwi.reo.runtime.Port#setProducer(nl.cwi.reo.runtime.Component)
	 */
	public void setProducer(Component p) {
		prod = p;
	}

	/* (non-Javadoc)
	 * @see nl.cwi.reo.runtime.Port#setConsumer(nl.cwi.reo.runtime.Component)
	 */
	public void setConsumer(Component c) {
		cons = c;
	}

	/* (non-Javadoc)
	 * @see nl.cwi.reo.runtime.Port#setGet()
	 */
	public void setGet() {
		get = true;
	}

	/* (non-Javadoc)
	 * @see nl.cwi.reo.runtime.Port#setPut(java.lang.Object)
	 */
	public void setPut(T datum) {
		put = datum;
	}

	/* (non-Javadoc)
	 * @see nl.cwi.reo.runtime.Port#hasGet()
	 */
	public boolean hasGet() {
		return get;
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
	 * @see nl.cwi.reo.runtime.Port#take()
	 */
	public T take() {
		T datum;
		while ((datum = put) == null) {
		}
		put = null;
		get = false;
		prod.activate();
		return datum;
	}

	/* (non-Javadoc)
	 * @see nl.cwi.reo.runtime.Port#activateProducer()
	 */
	public void activateProducer() {
		prod.activate();
	}

	/* (non-Javadoc)
	 * @see nl.cwi.reo.runtime.Port#activateConsumer()
	 */
	public void activateConsumer() {
		cons.activate();
	}

	/* (non-Javadoc)
	 * @see nl.cwi.reo.runtime.Output#put(java.lang.Object)
	 */
	public void put(T datum) {
		if (datum == null)
			throw new NullPointerException();
		// Signal the scheduler
		while (put != null) {
		}
		put = datum;
		cons.activate();
		// Wait for signal from scheduler
		while (!get && put != null) {
		}
	}

	/* (non-Javadoc)
	 * @see nl.cwi.reo.runtime.Input#get()
	 */
	public T get() {
		// Signal the scheduler
		get = true;
		prod.activate();
		// Wait for signal from scheduler
		T datum;
		while ((datum = put) == null) {
		}
		put = null;
		get = false;
		prod.activate();
		return datum;
	}

	/**
	 * Put.
	 *
	 * @param datum
	 *            the datum
	 * @param timeout
	 *            the timeout
	 * @return true, if successful
	 */
	public boolean put(T datum, long timeout) {
		if (datum == null)
			throw new NullPointerException();
		// Signal the scheduler
		long deadline = System.nanoTime() + timeout;
		while (put != null) {
		}
		put = datum;
		cons.activate();
		// Wait for signal from scheduler
		while (!get && put != null) {
			if (System.nanoTime() >= deadline) {
				put = null; // use compare and set here
				return false; // not thread safe, since datum may have been
								// taken
			}
		}
		return true;
	}

	/**
	 * Gets the.
	 *
	 * @param timeout
	 *            the timeout
	 * @return the t
	 */
	public T get(long timeout) {
		// Signal the scheduler
		long deadline = System.nanoTime() + timeout;
		get = true;
		prod.activate();
		// Wait for signal from scheduler
		T datum;
		while ((datum = put) == null) {
			if (System.nanoTime() >= deadline) {
				put = null; // use compare and set here
				return null; // not thread safe, since datum may have been taken
			}
		}
		put = null;
		get = false;
		prod.activate();
		return datum;
	}
}