package nl.cwi.reo.parse;

import java.util.Map;
import java.util.Set;

import nl.cwi.reo.semantics.Program;

/**
 * A parameterized for loop of a set {link java.util.Set}&lt;{link nl.cwi.reo.parse.Component}&gt; of parameterized components.
 */
public class ComponentForLoop implements Component {

	/**
	 * Name of the iterated parameter.
	 */
	public String parameter;

	/**
	 * Lower bound of iteration.
	 */
	public Expression lower;

	/**
	 * Upper bound of iteration.
	 */
	public Expression upper;

	/**
	 * Set of iterated parameterized components.
	 */
	public Set<Component> components;

	/**
	 * Constructs a parameterized for loop. 
	 * 
	 * @param parameter		name of the iteration parameter
	 * @param lower			expression defining the lower iteration bound
	 * @param upper			expression defining the upper iteration bound
	 * @param components	set of iterated parameterized components
	 */
	public ComponentForLoop(String parameter, Expression lower, Expression upper, Set<Component> components) {
		this.parameter = parameter;
		this.lower = lower;
		this.upper = upper;
		this.components = components;
	}
	
	/**
	 * Gets a concrete {link nl.cwi.reo.Program} from this parameterized component.
	 * @param parameters		parameter assignment
	 * @param generator			fresh node name generator
	 * @return concrete program {link nl.cwi.reo.Program} for this parameterized component
	 * @throws Exception if not all required parameters are assigned.
	 */
	@Override
	public Program getProgram(Map<String, String> parameters, NodeGenerator generator) throws Exception {
		
		// Initialize the default program.
		Program program = new Program();
		
		// Evaluate the lower and upper iteration bound.
		int a = lower.eval(parameters);
		int b = upper.eval(parameters);	
			
		// Iterate to find all concrete components. Note that the generator
		// makes sure that all internal nodes get fresh names.
		for (int i = a; i <= b; i++) {
			parameters.put(parameter, Integer.toString(i));
			for (Component comp : this.components)
				program.add(comp.getProgram(parameters, generator));
		}
		
		return program;
	}
}
