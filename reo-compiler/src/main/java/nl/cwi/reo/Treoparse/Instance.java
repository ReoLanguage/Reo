package nl.cwi.reo.Treoparse;

import java.util.Map;

import nl.cwi.reo.semantics.Program;

/**
 * An parameterized component in a Reo source file. An parameterized component is either a reference to a 
 * parameterized {link nl.cwi.reo.parse.Definition}, or for loop that iterates components.
 */
public interface Instance {
	
	/**
	 * Gets a concrete {link nl.cwi.reo.Program} from this parameterized component.
	 * @param parameters	parameter assignment
	 * @param generator		generator of fresh node names
	 * @return a set of component instances of type {@link nl.cwi.reo.semantics.Instance}.
	 * @throws Exception if not all required parameters are assigned.
	 */
	public Program getProgram(Map<String, String> parameters, NodeGenerator generator) throws Exception;
}
