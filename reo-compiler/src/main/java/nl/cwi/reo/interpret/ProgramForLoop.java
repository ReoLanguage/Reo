package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.List;

/**
 * A parameterized for loop of a set {link java.util.Set}&lt;{link nl.cwi.reo.parse.Component}&gt; of parameterized components.
 */
public class ProgramForLoop implements ProgramExpression {

	/**
	 * Name of the iterated parameter.
	 */
	public VariableName parameter;

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
	public ProgramExpression body;

	/**
	 * Constructs a parameterized for loop. 
	 * 
	 * @param parameter		name of the iteration parameter
	 * @param lower			expression defining the lower iteration bound
	 * @param upper			expression defining the upper iteration bound
	 * @param subprogram	iterated subprogram definition
	 */
	public ProgramForLoop(VariableName parameter, IntegerExpression lower, IntegerExpression upper, ProgramExpression body) {
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
	public ProgramExpression evaluate(DefinitionList params) throws Exception {
		
		if (params.get(parameter) != null)
			throw new Exception("Parameter " + parameter + " is already used.");
		
		IntegerExpression x = lower.evaluate(params);
		IntegerExpression y = upper.evaluate(params);
		
		if (x instanceof IntegerValue && y instanceof IntegerValue) {
			
			// Evaluate the lower and upper iteration bound.
			int a = ((IntegerValue)x).toInteger();
			int b = ((IntegerValue)y).toInteger();	
				
			// Iterate to find all concrete components. 
			boolean instancesAreValue = true;
			List<Instance> insts = new ArrayList<Instance>();
			DefinitionList defs = new DefinitionList(params);
			for (int i = a; i <= b; i++) {
				defs.put(parameter, new IntegerValue(Integer.valueOf(i)));
				ProgramExpression e = body.evaluate(defs);
				if (e instanceof ProgramValue) {
					ProgramValue B = (ProgramValue)e;
					defs.putAll(B.getDefinitions()); // Overwriting semantics of for-loop
					Instance inst = B.getInstances();
					insts.add(inst);
					if (inst instanceof Instance) {
						insts.add((Instance)inst);
					} else {
						instancesAreValue = false;
					}
				} else {
					instancesAreValue = false;
				}
			}
			
			if (instancesAreValue) {
				defs.remove(parameter);				
				return new ProgramValue(new Instance().compose(insts), defs);
			}
		}
		
		return new ProgramForLoop(parameter, x, y, body.evaluate(params));
	}
}
