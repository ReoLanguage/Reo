package nl.cwi.pr.runtime;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import nl.cwi.pr.runtime.api.InputPort;

public class InputPortImpl extends PublicPort implements InputPort {

	@Override
	public Object get() throws InterruptedException {
		buffer = null;
		status = IO.PENDING;
		handler.flag();
		return resume();
	}

	@Override
	public Object get(long timeout) throws TimeoutException {
		buffer = null;
		status = IO.PENDING;
		handler.flag();

		long deadline = System.currentTimeMillis() + timeout;
		boolean timeoutExpired = false;
		while (true)
			try {
				while (status != IO.COMPLETED && !handler.callSync())
					if (!semaphore.tryAcquire(
							deadline - System.currentTimeMillis(),
							TimeUnit.MILLISECONDS)) {

						timeoutExpired = true;
						break;
					}

				break;
			} catch (InterruptedException exception) {
			}

		if (timeoutExpired && !handler.cancelSync())
			throw new TimeoutException();

		return buffer;
	}

	@Override
	public Object getUninterruptibly() {
		while (true)
			try {
				return get();
			} catch (InterruptedException exception) {
				break;
			}

		while (true)
			try {
				return resume();
			} catch (InterruptedException exception) {
			}
	}

	@Override
	public Object resume() throws InterruptedException {
		while (true) {
			handler.callSync();
			if (status == IO.COMPLETED)
				return buffer;

			semaphore.acquire();
		}
	}
}