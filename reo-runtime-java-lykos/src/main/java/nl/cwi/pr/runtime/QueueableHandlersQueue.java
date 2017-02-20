package nl.cwi.pr.runtime;

import java.util.concurrent.Semaphore;

public class QueueableHandlersQueue {

	//
	// FIELDS
	//

	private final QueueableHandler[] handlers;
	private final Semaphore counter = new Semaphore(0);
	private final Semaphore mutex = new Semaphore(1);
	private final int size;
	private volatile int start = 0;
	private volatile int end = 0;

	//
	// CONSTRUCTORS
	//

	public QueueableHandlersQueue(final int size) {
		this.handlers = new QueueableHandler[size];
		this.size = size;
	}

	//
	// METHODS
	//

	public QueueableHandler dequeue() {
		counter.acquireUninterruptibly();

		mutex.acquireUninterruptibly();
		QueueableHandler handler = handlers[start];
		start = (start + 1) % size;
		handler.isQueued = false;
		mutex.release();

		return handler;
	}

	public void enqueue(final QueueableHandler handler) {
		mutex.acquireUninterruptibly();
		if (!handler.isQueued) {
			handlers[end] = handler;
			end = (end + 1) % size;
			handler.isQueued = true;

			counter.release();
		}

		mutex.release();
	}

	public boolean isEmpty() {
		return counter.availablePermits() == 0;
	}
}