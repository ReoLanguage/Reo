package nl.cwi.reo.runtime;

// TODO: Auto-generated Javadoc
/**
 * The Class PortRingBuffer.
 *
 * @param <T>
 *            the generic type
 */
@SuppressWarnings("initialization")
public class PortRingBuffer<T> implements Port<T> {
	
	/** The prod. */
	private Component prod;
	
	/** The cons. */
	private Component cons;
	
	/** The put. */
	private volatile T[] put;
	
	/** The head. */
	private volatile int head = 0;
	
	/** The tail. */
	private volatile int tail = 0;

	/**
	 * Instantiates a new port ring buffer.
	 *
	 * @param c
	 *            the c
	 */
	@SuppressWarnings("unchecked")
	public PortRingBuffer(int c) {
		if (c <= 0)
			throw new IllegalArgumentException();
		put = (T[]) new Object[c + 1];
	}

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
	 * @see nl.cwi.reo.runtime.Port#setPut(java.lang.Object)
	 */
	public void setPut(T datum) {
		int n = head + 1;
		if (n == put.length)
			n = 0;
		put[n] = datum;
		head = n;
	}

	/* (non-Javadoc)
	 * @see nl.cwi.reo.runtime.Port#setGet()
	 */
	public void setGet() {
	}

	/* (non-Javadoc)
	 * @see nl.cwi.reo.runtime.Port#hasPut()
	 */
	public T hasPut() {
		return head != tail ? put[head] : null;
	}

	/* (non-Javadoc)
	 * @see nl.cwi.reo.runtime.Port#peek()
	 */
	public T peek() {
		return head != tail ? put[head] : null;
	}

	/* (non-Javadoc)
	 * @see nl.cwi.reo.runtime.Port#hasGet()
	 */
	public boolean hasGet() {
		int n;
		return tail != ((n = head + 1) == put.length ? 0 : n);
	}

	/* (non-Javadoc)
	 * @see nl.cwi.reo.runtime.Port#take()
	 */
	public T take() {
		int n;
		tail = (n = tail + 1) == put.length ? 0 : n;
		return put[tail];
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
		setPut(datum);
		cons.activate();
		int n;
		while (tail == ((n = head + 1) == put.length ? 0 : n)) {
		}
	}

	/* (non-Javadoc)
	 * @see nl.cwi.reo.runtime.Input#get()
	 */
	public T get() {
		T datum = null;
		int n;
		if (tail == ((n = head + 1) == put.length ? 0 : n)) {
			datum = take();
			prod.activate();
		} else {
			while (head == tail) {
			}
			datum = take();
		}
		return datum;
	}
}