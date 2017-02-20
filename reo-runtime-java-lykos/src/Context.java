package nl.cwi.pr.runtime;

import java.util.concurrent.atomic.AtomicInteger;

public class Context {

	//
	// FIELDS
	//

	private final AtomicInteger[] integers;

	//
	// CONSTRUCTORS
	//

	public Context(final int nPorts) {
		this.integers = new AtomicInteger[(nPorts / 32) + 1];
		for (int i = 0; i < this.integers.length; i++)
			this.integers[i] = new AtomicInteger();
	}

	//
	// METHODS
	//

	public void add(final int index, final int mask) {
		AtomicInteger integer = integers[index];
		int bits = integer.get();
		while (!integer.compareAndSet(bits, bits | mask))
			bits = integer.get();
	}

	public boolean contains(final int index, final int mask) {
		return mask == (integers[index].get() & mask);
	}

	public void remove(final int index, final int mask) {
		AtomicInteger integer = integers[index];
		int current = integer.get();
		while (!integer.compareAndSet(current, current & ~mask))
			current = integer.get();
	}
}