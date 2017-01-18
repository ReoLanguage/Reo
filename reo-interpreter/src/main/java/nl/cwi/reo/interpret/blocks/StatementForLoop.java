package nl.cwi.reo.interpret.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nl.cwi.reo.interpret.integers.IntegerExpression;
import nl.cwi.reo.interpret.integers.IntegerValue;
import nl.cwi.reo.interpret.ranges.Expression;
import nl.cwi.reo.interpret.semantics.Definitions;
import nl.cwi.reo.interpret.variables.VariableName;
import nl.cwi.reo.semantics.Semantics;

/**
 * A parameterized for loop of a set {link java.util.Set}&lt;{link nl.cwi.reo.parse.Component}&gt; of parameterized components.
 */
public class StatementForLoop<T extends Semantics<T>> implements Statement<T> {

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
	public Statement<T> body;

	/**
	 * Constructs a parameterized for loop. 
	 * 
	 * @param parameter		name of the iteration parameter
	 * @param lower			expression defining the lower iteration bound
	 * @param upper			expression defining the upper iteration bound
	 * @param subprogram	iterated subprogram definition
	 */
	public StatementForLoop(VariableName parameter, IntegerExpression lower, IntegerExpression upper, Statement<T> body) {
		if (parameter == null || lower == null || upper == null || body == null)
			throw new NullPointerException();
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
	public Statement<T> evaluate(Map<VariableName, Expression> params) throws Exception {
		
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
			Definitions defns = new Definitions(params);
			List<Statement<T>> bodies = new ArrayList<Statement<T>>();
			List<Program<T>> progs = new ArrayList<Program<T>>();
			for (int i = a; i <= b; i++) {
				defns.put(parameter, new IntegerValue(Integer.valueOf(i)));
				Statement<T> e = body.evaluate(defns);
				bodies.add(e);
				if (e instanceof Program) {
					progs.add((Program<T>)e);
				} else {
					isProgram = false;
				}
			}
			
			if (isProgram) {
				Program<T> prog = new Program<T>();
				return prog.compose(progs).remove(parameter);
			}
			
			return new Block<T>(bodies);
		}
		
		return new StatementForLoop<T>(parameter, x, y, body.evaluate(params));
	}
	
	@Override
	public String toString() {
		return "for " + parameter + "=" + lower + ".." + upper + body;
	}
}
