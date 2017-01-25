package nl.cwi.pr.runtime;

public abstract class Transition {

	public abstract boolean fire();
	
	protected boolean checkDataConstraint() {
		return true;
	}
}