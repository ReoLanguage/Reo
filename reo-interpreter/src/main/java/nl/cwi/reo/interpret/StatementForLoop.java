package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A parameterized for loop of a set {link java.util.Set}&lt;{link nl.cwi.reo.parse.Component}&gt; of parameterized components.
 */
public class StatementForLoop implements Statement {

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
	public StatementForLoop(VariableName parameter, IntegerExpression lower, IntegerExpression upper, ProgramExpression body) {
		if (parameter == null || lower == null || upper == null || body == null)
			throw new IllegalArgumentException("Arguments cannot be null.");
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
	public ProgramExpression evaluate(Map<VariableName, Expression> params) throws Exception {
		
		if (params.get(parameter) != null)
			throw new Exception("Parameter " + parameter + " is already used.");
		
		IntegerExpression x = lower.evaluate(params);
		IntegerExpression y = upper.evaluate(params);
		
		if (x instanceof IntegerValue && y instanceof IntegerValue) {
			
			// Evaluate the lower and upper iteration bound.
			int a = ((IntegerValue)x).toInteger();
			int b = ((IntegerValue)y).toInteger();
			
			System.out.println("for" + a + " to " + b);
				
			// Iterate to find all concrete components. 
			boolean instancesAreValue = true;
			List<InstanceList> insts = new ArrayList<InstanceList>();
			ZDefinitionList defs = new ZDefinitionList(params);
			for (int i = a; i <= b; i++) {
				defs.put(parameter, new IntegerValue(Integer.valueOf(i)));
				ProgramExpression e = body.evaluate(defs);
				if (e instanceof Program) {
					Program B = (Program)e;
					defs.putAll(B.getDefinitions()); // Overwriting semantics of for-loop
					InstanceList inst = B.getInstance();
					System.out.println("++++++++body " + inst);
					insts.add(inst);
					if (inst instanceof InstanceList) {
						insts.add((InstanceList)inst);
					} else {
						instancesAreValue = false;
					}
				} else {
					instancesAreValue = false;
				}
			}
			
			if (instancesAreValue) {
				defs.remove(parameter);				
				return new Program(new InstanceList().compose(insts), defs);
			}
		}
		
		return new StatementForLoop(parameter, x, y, body.evaluate(params));
	}
}
