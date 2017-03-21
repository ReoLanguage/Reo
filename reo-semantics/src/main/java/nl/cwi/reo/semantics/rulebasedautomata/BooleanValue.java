package nl.cwi.reo.semantics.rulebasedautomata;

import java.util.HashMap;
import java.util.Map;

import nl.cwi.reo.interpret.ports.Port;

public class BooleanValue implements DataConstraint {
	
	private final boolean b;
	
	public BooleanValue(boolean b) {
		this.b = b;
	}
	
	public boolean getValue() {
		return b;
	}

	@Override
	public DataConstraint getGuard() {
		return new BooleanValue(true);
	}

	@Override
	public Map<Port, DataTerm> getAssignment() {
		return new HashMap<Port, DataTerm>();
	}

}
