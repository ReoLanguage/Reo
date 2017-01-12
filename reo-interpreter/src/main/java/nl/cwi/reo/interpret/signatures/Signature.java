package nl.cwi.reo.interpret.signatures;

import java.util.HashMap;
import java.util.Map;

import nl.cwi.reo.interpret.arrays.Expression;
import nl.cwi.reo.interpret.variables.VariableName;
import nl.cwi.reo.semantics.Port;

public class Signature extends HashMap<Port, Port> {
	
	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = -3243867819411595599L;
	
	private Map<VariableName, Expression> defs;
	
	public Signature(Map<VariableName, Expression> defs, Map<Port, Port> links) {
		if (defs == null || links == null)
			throw new NullPointerException();
		this.defs = defs;
		super.putAll(links);
	}
	
	public Map<VariableName, Expression> getDefinitions() {
		return this.defs;
	}

	@Override
	public String toString() {
		return "" + defs + this;
	}
}
