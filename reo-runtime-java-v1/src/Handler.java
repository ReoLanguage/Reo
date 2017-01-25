package nl.cwi.pr.runtime;

import java.util.concurrent.Semaphore;

public abstract class Handler {

	//
	// FIELDS
	//

	public final Semaphore semaphore;

	//
	// CONSTRUCTORS
	//

	public Handler(final Semaphore semaphore) {
		this.semaphore = semaphore;
	}

	//
	// METHODS
	//

	public abstract boolean call();

	public abstract boolean cancel();

	public boolean callSync() throws InterruptedException {
		semaphore.acquire();
		boolean result = call();
		semaphore.release();
		return result;
	}

	public boolean cancelSync() {
		semaphore.acquireUninterruptibly();
		boolean result = cancel();
		semaphore.release();
		return result;
	}

	public abstract void flag();
}
