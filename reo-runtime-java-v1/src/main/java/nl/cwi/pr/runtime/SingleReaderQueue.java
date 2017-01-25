package nl.cwi.pr.runtime;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class SingleReaderQueue {

	//
	// FIELDS
	//

	private final AtomicReferenceArray<SingleReaderQueueItem> array;
	private final AtomicInteger head = new AtomicInteger(0);
	private final int boundary;
	private final int size;
	private final Semaphore semaphore = new Semaphore(1);

	private volatile int tail = 0;
	private volatile int headForWriters = 0;

	//
	// CONSTRUCTORS
	//

	public SingleReaderQueue(final int size) {
		this.array = new AtomicReferenceArray<SingleReaderQueueItem>(size);
		this.boundary = Integer.MAX_VALUE - size;
		this.size = size;
	}

	//
	// METHODS
	//

	public boolean canPoll() {
		return tail != head.get();
	}

	public SingleReaderQueueItem peek() {
		return array.get(tail % size);
	}

	public SingleReaderQueueItem poll() {
		if (tail == Integer.MAX_VALUE)
			tail = Integer.MAX_VALUE % size;

		return array.get(tail++ % size);
	}

	public void offer(SingleReaderQueueItem item) {
		semaphore.acquireUninterruptibly();
		if (boundary < headForWriters) {
			headForWriters = headForWriters % size;

			int i = head.get();
			while (!head.compareAndSet(i, i % size))
				i = head.get();
		}

		array.set(headForWriters++ % size, item);
		semaphore.release();

		head.getAndIncrement();
	}
}
