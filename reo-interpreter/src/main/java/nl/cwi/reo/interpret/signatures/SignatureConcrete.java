package nl.cwi.reo.interpret.signatures;

import java.util.HashMap;
import java.util.Map;

import nl.cwi.reo.interpret.ranges.Expression;
import nl.cwi.reo.semantics.api.Port;

public class SignatureConcrete extends HashMap<Port, Port> {
	
	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = -3243867819411595599L;
	
	private Map<String, Expression> defs;
	
	public SignatureConcrete(Map<String, Expression> defs, Map<Port, Port> links) {
		if (defs == null || links == null)
			throw new NullPointerException();
		this.defs = defs;
		super.putAll(links);
	}
	
	public Map<String, Expression> getDefinitions() {
		return this.defs;
	}

	@Override
	public String toString() {
		return "" + defs + this;
	}
}
