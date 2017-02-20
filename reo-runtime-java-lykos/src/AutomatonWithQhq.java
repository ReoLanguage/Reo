package nl.cwi.pr.runtime;

public class AutomatonWithQhq extends Automaton {

	//
	// FIELDS
	//

	public final QueueableHandlersQueue qhq;

	//
	// CONSTRUCTORS
	//

	public AutomatonWithQhq(final int nPublicPorts, final int nPrivatePorts) {
		super(nPublicPorts);
		this.qhq = new QueueableHandlersQueue(nPrivatePorts);
	}

	//
	// METHODS
	//

	@Override
	public void run() {
		QueueableHandler handler;
		while (true) {
			handler = qhq.dequeue();
			super.semaphore.acquireUninterruptibly();
			handler.call();
			while (!qhq.isEmpty())
				qhq.dequeue().call();

			super.semaphore.release();
		}
	}
}
