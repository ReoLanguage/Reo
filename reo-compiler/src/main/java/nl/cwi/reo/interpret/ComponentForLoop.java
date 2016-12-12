package nl.cwi.reo.interpret;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A parameterized for loop of a set {link java.util.Set}&lt;{link nl.cwi.reo.parse.Component}&gt; of parameterized components.
 */
public class ComponentForLoop implements Evaluable<Component> {

	/**
	 * Name of the iterated parameter.
	 */
	public String parameter;

	/**
	 * Lower bound of iteration.
	 */
	public IntegerExpression lower;

	/**
	 * Upper bound of iteration.
	 */
	public IntegerExpression upper;
	/**
	 * Iterated subprogram definition.
	 */
	public ComponentBody body;

	/**
	 * Constructs a parameterized for loop. 
	 * 
	 * @param parameter		name of the iteration parameter
	 * @param lower			expression defining the lower iteration bound
	 * @param upper			expression defining the upper iteration bound
	 * @param subprogram	iterated subprogram definition
	 */
	public ComponentForLoop(String parameter, IntegerExpression lower, IntegerExpression upper, ComponentBody body) {
		this.parameter = parameter;
		this.lower = lower;
		this.upper = upper;
		this.body = body;
	}
	
	/**
	 * Gets a {link nl.cwi.reo.ProgramInstance} for a particular parameter assignment.
	 * @param parameters		parameter assignment
	 * @return Program instance {link nl.cwi.reo.ProgramInstance} for this parameterized component
	 * @throws Exception if the provided parameters do not match the signature of this program.
	 */
	@Override
	public Component evaluate(Map<String,Value> p) throws Exception {
		
		Set<Component> subcomponents = new HashSet<Component>();
		
		// Evaluate the lower and upper iteration bound.
		int a = lower.evaluate(p);
		int b = upper.evaluate(p);	
			
		// Iterate to find all concrete components. 
		for (int i = a; i <= b; i++) {
			//TODO Check if iteration parameter is already used in the context, in which case the next statement overwrites a value.
			Map<String,Value> q = new HashMap<String,Value>(p);
			q.put(parameter, new Value(Integer.valueOf(i)));
			subcomponents.add(body.evaluate(q));
		}
		
		return new Component(subcomponents);
	}
}
