package nl.cwi.pr.runtime;

import java.util.concurrent.Semaphore;

public abstract class QueueableHandler extends Handler {

	//
	// FIELDS
	//

	public final QueueableHandlersQueue qhq;
	public volatile boolean isQueued = false;

	//
	// CONSTRUCTORS
	//

	public QueueableHandler(final Semaphore semaphore,
			final QueueableHandlersQueue qhq) {

		super(semaphore);
		this.qhq = qhq;
	}

	//
	// METHODS
	//

	public void callAsync() {
		qhq.enqueue(this);
	}

	@Override
	public boolean cancel() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void flag() {
	}
}