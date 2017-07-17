package nl.cwi.reo.runtime;

// TODO: Auto-generated Javadoc
/**
 * The Class PortWaitNotify.
 *
 * @param <T>
 *            the generic type
 */
@SuppressWarnings("initialization")
public class PortWaitNotify<T> implements Port<T> {
	
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
	 * @see nl.cwi.reo.runtime.Port#take()
	 */
	public T take() {
		T datum = put;
		put = null;
		get = false;
		return datum;
	}

	/* (non-Javadoc)
	 * @see nl.cwi.reo.runtime.Port#hasGet()
	 */
	public boolean hasGet() {
		return get && put == null;
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
		while (put != null) {
		}
		put = datum;
		cons.activate();
		if (!get && put != null)
			synchronized (prod) {
				while (!get && put != null)
					try {
						prod.wait();
					} catch (InterruptedException e) {
					}
			}
	}

	/* (non-Javadoc)
	 * @see nl.cwi.reo.runtime.Input#get()
	 */
	public T get() {
		get = true;
		prod.activate();
		if (put == null)
			synchronized (cons) {
				while (put == null)
					try {
						cons.wait();
					} catch (InterruptedException e) {
					}
			}
		T datum = put;
		put = null;
		get = false;
		prod.activate();
		return datum;
	}
}