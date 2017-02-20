package nl.cwi.pr.runtime;

import java.util.concurrent.Semaphore;

public class IdLock {

	//
	// FIELDS
	//

	private final Semaphore semaphore;
	private volatile int owner = -1;

	//
	// CONSTRUCTORS
	//

	public IdLock() {
		this.semaphore = new Semaphore(1);
	}

	//
	// METHODS
	//

	public boolean isLocked() {
		return semaphore.availablePermits() == 0;
	}

	public boolean isLocked(final int id) {
		return isLocked() && owner == id;
	}

	public void lock(final int id) {
		if (owner == id)
			return;

		semaphore.acquireUninterruptibly();
		owner = id;
	}

	public boolean unlock(final int id) {
		if (semaphore.availablePermits() > 0)
			return false;
		if (owner != id)
			return false;

		owner = -1;
		semaphore.release();
		return true;
	}
}
