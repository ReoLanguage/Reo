package nl.cwi.reo.interpret.variables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.Token;

import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.interpret.integers.IntegerExpression;
import nl.cwi.reo.interpret.integers.IntegerValue;
import nl.cwi.reo.interpret.integers.IntegerVariable;
import nl.cwi.reo.interpret.ranges.Expression;
import nl.cwi.reo.interpret.semantics.Definitions;

/**
 * An immutable parameterized set of variable names, such as, for example, 
 * (a.b, c[-31], x.y.z[-1][0...1-k][5]).
 */
public final class VariableRange implements Variable {

	/**
	 * Fully qualified name.
	 */
	private final String name;
	
	/**
	 * Indices.
	 */
	private final List<List<IntegerExpression>> indices;
	
	/**
	 * Indicates if this range is a list.
	 */
	private final boolean isList;
	
	/**
	 * Token
	 */
	private final Token token;
	
	/**
	 * Constructs a variable list.
	 * @param name		name of the node
	 */
	public VariableRange(String name, List<List<IntegerExpression>> indices, Token token) {
		if (name == null || indices == null)
			throw new NullPointerException();
		this.name = name;
		boolean isList = false;
		List<List<IntegerExpression>> unmod_indices = new ArrayList<List<IntegerExpression>>();
		for (List<IntegerExpression> bounds : indices) {
			unmod_indices.add(Collections.unmodifiableList(bounds));
			if (bounds.size() > 1) isList = true;
		}
		this.indices = Collections.unmodifiableList(unmod_indices);	
		this.isList = isList;
		this.token = token;
	}

	@Override
	public Token getToken() {
		return token;
	}
	
	/**
	 * Finds values of all parameters in the indices, given that the size
	 * of the evaluated variable expression equals a given size.
	 * @param size		actual length of evaluated variable expression.
	 * @return a map that assigns each parameter name to an integer value, 
	 * if these values can be found, and null otherwise.
	 */
	public Definitions findParamFromSize(int size) {		
		Definitions params = new Definitions();
		for (List<IntegerExpression> bounds : indices) {
			if (bounds.size() == 1) {
				if (!(bounds.get(0) instanceof IntegerValue)) 
					return null; 
			} else if (bounds.size() == 2) {
				if (bounds.get(0) instanceof IntegerValue) {
					int lower = ((IntegerValue)bounds.get(0)).toInteger();
					if (bounds.get(1) instanceof IntegerVariable) {
						IntegerVariable k = (IntegerVariable)bounds.get(1);						
						if (k.getVariable() instanceof VariableName) {
							VariableName name = (VariableName)k.getVariable();
							params.put(name.getName(), new IntegerValue(lower + size - 1));
						} else {
							return null;
						}
					} else {
						return null;
					}
				} else if (bounds.get(1) instanceof IntegerValue) {
					int upper = ((IntegerValue)bounds.get(1)).toInteger();
					if (bounds.get(0) instanceof IntegerVariable) {
						IntegerVariable k = (IntegerVariable)bounds.get(1);						
						if (k.getVariable() instanceof VariableName) {
							VariableName name = (VariableName)k.getVariable();
							params.put(name.getName(), new IntegerValue(upper - size + 1));
						}
					} else {
						return null;
					}
				} else {
					return null;
				}
			}
		}
		return params;
	}
	
	@Override
	public Variable evaluate(Map<String, Expression> params) throws CompilationException {
		
		boolean boundsAreKnown = true;
		List<List<IntegerExpression>> indices_p = new ArrayList<List<IntegerExpression>>();

		for (List<IntegerExpression> bounds : this.indices) {
			List<IntegerExpression> bounds_p = new ArrayList<IntegerExpression>();
			for (IntegerExpression e : bounds) {
				IntegerExpression e_p = e.evaluate(params);
				if (!(e_p instanceof IntegerValue))
					boundsAreKnown = false;
				bounds_p.add(e_p);
			}		
			indices_p.add(bounds_p);
		}
		
		if (boundsAreKnown) {
			List<String> variables = new ArrayList<String>();
			List<String> tempvars;
			variables.add(name);
			
			for (List<IntegerExpression> bounds_p : indices_p) {			
				if (bounds_p.size() == 1) {
					int i = ((IntegerValue)bounds_p.get(0)).toInteger();
					tempvars = new ArrayList<String>();
					for (String var : variables)
						tempvars.add(var + "[" + i + "]");
					variables = tempvars;			
				} else if (bounds_p.size() == 2) {
					int a = ((IntegerValue)bounds_p.get(0)).toInteger();
					int b = ((IntegerValue)bounds_p.get(1)).toInteger();
					tempvars = new ArrayList<String>();
					for (int i = a; i <= b; ++i)	
						for (String var : variables)
							tempvars.add(var + "[" + i + "]");
					variables = tempvars;
				}			
			}
			
			List<VariableName> vars = new ArrayList<VariableName>();
			for (String x : variables) 
				vars.add(new VariableName(x, token));

			if (isList)
				return new VariableNameList(vars, token);
			else
				return vars.get(0);
			
		}
		
		return new VariableRange(this.name, indices_p, token);
	}
	
	@Override
	public String toString() {
		String s = name;	
		for (List<IntegerExpression> bounds : this.indices) {			
			if (bounds.size() == 1) {
				s += "[" + bounds.get(0) + "]";
			} else if (bounds.size() == 2) {
				s += "[" + bounds.get(0)  + ".." + bounds.get(1) + "]";
			}			
		}	
		return s;
	}
}
