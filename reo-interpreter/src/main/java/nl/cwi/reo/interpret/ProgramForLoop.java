package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
				
			// Iterate to find all concrete components. 
			boolean isProgram = true;
			Map<VariableName, Expression> defns = new HashMap<VariableName, Expression>(params);
			List<ProgramExpression> bodies = new ArrayList<ProgramExpression>();
			List<ProgramValue> progs = new ArrayList<ProgramValue>();
			for (int i = a; i <= b; i++) {
				defns.put(parameter, new IntegerValue(Integer.valueOf(i)));
				ProgramExpression e = body.evaluate(defns);
				if (e instanceof ProgramValue) {
					progs.add((ProgramValue)e);
				} else {
					isProgram = false;
				}
			}
			
			if (isProgram) {
				ProgramValue prog = new ProgramValue();
				return prog.compose(progs).remove(parameter);
			}
			
			return new ProgramBody(bodies);
		}
		
		return new ProgramForLoop(parameter, x, y, body.evaluate(params));
	}
	
	@Override
	public String toString() {
		return "for " + parameter + "=" + lower + ".." + upper + "{" + body + "}";
	}
}
