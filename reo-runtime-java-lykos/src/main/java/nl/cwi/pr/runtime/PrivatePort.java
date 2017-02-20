package nl.cwi.pr.runtime;

import java.util.concurrent.Semaphore;

public class PrivatePort extends PortImpl {

	//
	// FIELDS
	//

	public volatile QueueableHandler masterHandler = new QueueableHandler(
			new Semaphore(1), new QueueableHandlersQueue(1)) {
		@Override
		public boolean call() {
			return false;
		}
	};

	//
	// METHODS
	//

	public void kickMaster() {
		masterHandler.flag();
		masterHandler.callAsync();
	}
}
