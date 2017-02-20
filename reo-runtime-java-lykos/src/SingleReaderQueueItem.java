package nl.cwi.pr.runtime;

import nl.cwi.pr.runtime.api.Port;

public class SingleReaderQueueItem {

	//
	// FIELDS
	//

	public final Port port;
	public final Automaton automaton;
	public final Runnable continuation;

	//
	// CONSTRUCTORS
	//

	public SingleReaderQueueItem(Port port, Automaton automaton,
			Runnable continuation) {

		this.port = port;
		this.automaton = automaton;
		this.continuation = continuation;
	}
}
