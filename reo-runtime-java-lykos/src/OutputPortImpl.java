package nl.cwi.pr.runtime;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import nl.cwi.pr.runtime.api.OutputPort;

class OutputPortImpl extends PublicPort implements OutputPort {

	@Override
	public void put(Object datum) throws InterruptedException {
		buffer = datum;
		status = IO.PENDING;
		handler.flag();
		resume();
	}

	@Override
	public void put(Object datum, long timeout) throws TimeoutException {
		buffer = datum;
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
	}

	@Override
	public void putUninterruptibly(Object datum) {
		while (true)
			try {
				put(datum);
				return;
			} catch (InterruptedException exception) {
				break;
			}

		while (true)
			try {
				resume();
				return;
			} catch (InterruptedException exception) {
			}
	}

	@Override
	public void resume() throws InterruptedException {
		while (true) {
			handler.callSync();
			if (status == IO.COMPLETED)
				return;

			semaphore.acquire();
		}
	}
}