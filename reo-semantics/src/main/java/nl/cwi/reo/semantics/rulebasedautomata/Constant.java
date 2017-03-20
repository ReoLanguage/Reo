package nl.cwi.reo.semantics.rulebasedautomata;

public class Constant implements DataTerm {
	
	private final Object c;
	
	public Constant(Object c) {
		this.c = c;
	}

	public Object getValue() {
		return c;
	}

	@Override
	public boolean hadOutputs() {
		return false;
	}
}
