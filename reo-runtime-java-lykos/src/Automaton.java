package nl.cwi.pr.runtime;

import java.util.concurrent.Semaphore;

public abstract class Automaton extends Thread {

	//
	// FIELDS
	//

	public final Context context;
	public final Semaphore semaphore = new Semaphore(1);

	//
	// CONSTRUCTORS
	//

	public Automaton(final int nPorts) {
		this.context = new Context(nPorts);
	}

	//
	// METHODS
	//

	public void reachSuccessor() {
		throw new UnsupportedOperationException();
	}
}
