package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.List;

/**
 * A parameterized set of variable names, such as, for example, 
 * (a.b, c[-31], x.y.z[-1][0...1-k][5]).
 */
public final class VariableRange implements Variable, Sequence {

	/**
	 * Fully qualified name.
	 */
	private final String name;
	
	/**
	 * Indices.
	 */
	private final List<List<IntegerExpression>> indices;
	
	/**
	 * Constructs a variable list.
	 * @param name		name of the node
	 */
	public VariableRange(String name, List<List<IntegerExpression>> indices) {
		this.name = name;
		this.indices = indices;		
	}
	
	/**
	 * Finds values of all parameters in the indices, given that the size
	 * of the evaluated variable expression equals a given size.
	 * @param size		actual length of evaluated variable expression.
	 * @return null, if values cannot be found, and a map that assigns each
	 * parameter name to an integer value otherwise.
	 */
	public DefinitionList findParamFromSize(int size) {
		
		DefinitionList params = new DefinitionList();
		
		for (List<IntegerExpression> bounds : indices) {
			switch (bounds.size()) {
			
			case 1:
				// If the index is not an integer, it can be anything, hence unknown.
				if (!(bounds.get(0) instanceof IntegerValue)) return null; 
				break;
				
			case 2:
				if (bounds.get(0) instanceof IntegerValue) {
					int lower = ((IntegerValue)bounds.get(0)).toInteger();
					
					// For now, we only allow the unknown bound to be an integer variable.
					// Of course, if we want, we could allow expressions such as k+1 or 2*k/5.
					if (bounds.get(1) instanceof IntegerVariable) {
						IntegerVariable k = (IntegerVariable)bounds.get(1);
						
						if (k.getVariable() instanceof VariableName) {
							VariableName name = (VariableName)k.getVariable();
							params.put(name, new IntegerValue(lower + size - 1));
						} else {
							return null;
						}
					} else {
						return null;
					}
				} else {
					int upper;
					
					if (bounds.get(1) instanceof IntegerValue) {
						upper = ((IntegerValue)bounds.get(1)).toInteger();
						
						// For now, we only allow the unknown bound to be an integer variable.
						// Of course, if we want, we could allow expressions such as k+1 or 2*k/5.
						if (bounds.get(0) instanceof IntegerVariable) {
							IntegerVariable k = (IntegerVariable)bounds.get(1);
							
							if (k.getVariable() instanceof VariableName) {
								VariableName name = (VariableName)k.getVariable();
								params.put(name, new IntegerValue(upper - size + 1));
							}
						} else {
							return null;
						}
					} else {
						return null;
					}
				}
				break;
			
			default:
				break;
			}
		}
		return params;
	}
	
	@Override
	public Variable evaluate(DefinitionList params) throws Exception {
		
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
			
			List<Variable> vars = new ArrayList<Variable>();
			for (String x : variables) 
				vars.add(new VariableName(x));

			return new VariableList(vars);
		}
		
		return new VariableRange(this.name, indices_p);
	}
	
//	@Override
//	public String toString() {
//		String s = name;	
//		for (List<IntegerExpression> bounds : this.indices) {			
//			if (bounds.size() == 1) {
//				s += "[" + bounds.get(0) + "]";
//			} else if (bounds.size() == 2) {
//				s += "[" + bounds.get(0)  + ".." + bounds.get(1) + "]";
//			}			
//		}	
//		return s;
//	}
}
