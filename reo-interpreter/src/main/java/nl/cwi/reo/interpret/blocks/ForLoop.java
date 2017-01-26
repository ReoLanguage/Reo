package nl.cwi.reo.interpret.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.interpret.integers.IntegerExpression;
import nl.cwi.reo.interpret.integers.IntegerValue;
import nl.cwi.reo.interpret.semantics.Definitions;
import nl.cwi.reo.interpret.variables.VariableName;
import nl.cwi.reo.semantics.api.Expression;
import nl.cwi.reo.semantics.api.Semantics;

/**
 * A parameterized for loop of a set {link java.util.Set}&lt;{link nl.cwi.reo.parse.Component}&gt; of parameterized components.
 */
public class ForLoop<T extends Semantics<T>> implements ReoBlock<T> {

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
	public ReoBlock<T> reoBlock;

	/**
	 * Constructs a parameterized for loop. 
	 * 
	 * @param parameter		name of the iteration parameter
	 * @param lower			expression defining the lower iteration bound
	 * @param upper			expression defining the upper iteration bound
	 * @param subprogram	iterated subprogram definition
	 */
	public ForLoop(VariableName parameter, IntegerExpression lower, IntegerExpression upper, ReoBlock<T> body) {
		if (parameter == null || lower == null || upper == null || body == null)
			throw new NullPointerException();
		this.parameter = parameter;
		this.lower = lower;
		this.upper = upper;
		this.reoBlock = body;
	}
	
	/**
	 * Gets a {link nl.cwi.reo.ProgramInstance} for a particular parameter assignment.
	 * @param parameters		parameter assignment
	 * @return Program instance {link nl.cwi.reo.ProgramInstance} for this parameterized component
	 * @throws Exception if the provided parameters do not match the signature of this program.
	 */
	@Override
	public ReoBlock<T> evaluate(Map<String, Expression> params) throws CompilationException {
		
		if (params.get(parameter) != null)
			throw new CompilationException(parameter.getToken(), "Parameter " + parameter + " is already used.");
		
		IntegerExpression x = lower.evaluate(params);
		IntegerExpression y = upper.evaluate(params);
		
		if (x instanceof IntegerValue && y instanceof IntegerValue) {
			
			// Evaluate the lower and upper iteration bound.
			int a = ((IntegerValue)x).toInteger();
			int b = ((IntegerValue)y).toInteger();
				
			// Iterate to find all concrete components. 
			boolean isProgram = true;
			Definitions defns = new Definitions(params);
			List<ReoBlock<T>> bodies = new ArrayList<ReoBlock<T>>();
			List<Assembly<T>> progs = new ArrayList<Assembly<T>>();
			for (int i = a; i <= b; i++) {
				defns.put(parameter.getName(), new IntegerValue(Integer.valueOf(i)));
				ReoBlock<T> e = reoBlock.evaluate(defns);
				bodies.add(e);
				if (e instanceof Assembly) {
					progs.add((Assembly<T>)e);
				} else {
					isProgram = false;
				}
			}
			
			if (isProgram) {
				Assembly<T> prog = new Assembly<T>();
				return prog.compose(progs).remove(parameter);
			}
			
			return new Body<T>(bodies);
		}
		
		return new ForLoop<T>(parameter, x, y, reoBlock.evaluate(params));
	}
	
	@Override
	public String toString() {
		return "for " + parameter + "=" + lower + ".." + upper + reoBlock;
	}
}
