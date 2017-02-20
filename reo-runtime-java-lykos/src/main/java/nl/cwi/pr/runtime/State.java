package nl.cwi.pr.runtime;

public abstract class State {

	public abstract void reach();

	public void reachSuccessor() {
		throw new UnsupportedOperationException();
	}
}
