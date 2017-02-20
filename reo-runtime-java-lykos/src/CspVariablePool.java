package nl.cwi.pr.runtime;

import java.util.HashMap;
import java.util.Map;

public class CspVariablePool {

	//
	// FIELDS
	//

	private final Map<PortImpl, CspPortVariable> portVariables = new HashMap<>();
	private final Map<MemoryCell, CspPreVariable> postVariables = new HashMap<>();
	private final Map<MemoryCell, CspPostVariable> preVariables = new HashMap<>();
	private final Map<String, CspVariable> quantifiedVariables = new HashMap<>();

	//
	// METHODS
	//

	public CspPortVariable newOrGetPortVariable(final PortImpl port) {
		if (!portVariables.containsKey(port))
			portVariables.put(port, new CspPortVariable(port));

		return portVariables.get(port);
	}

	public CspPostVariable newOrGetPostVariable(final MemoryCell cell) {
		if (!preVariables.containsKey(cell))
			preVariables.put(cell, new CspPostVariable(cell));

		return preVariables.get(cell);
	}

	public CspPreVariable newOrGetPreVariable(final MemoryCell cell) {
		if (!postVariables.containsKey(cell))
			postVariables.put(cell, new CspPreVariable(cell));

		return postVariables.get(cell);
	}

	public CspVariable newOrGetQuantifiedVariable(final String name) {
		if (!quantifiedVariables.containsKey(name))
			quantifiedVariables.put(name, new CspVariable());

		return quantifiedVariables.get(name);
	}
}
