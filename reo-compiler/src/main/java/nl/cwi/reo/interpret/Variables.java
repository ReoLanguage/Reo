package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A parameterized set of variable names, such as, for example, 
 * (a.b:int, c[-31]?, x.y.z[-1][0...1-k][5]!bool).
 */
public final class Variables implements Expression<List<String>> {

	/**
	 * Fully qualified name.
	 */
	private final String name;
	
	/**
	 * Indices.
	 */
	private final List<List<Expression<Integer>>> indices;
	
	/**
	 * Constructs a variable list.
	 * @param name		name of the node
	 */
	public Variables(String name, List<List<Expression<Integer>>> indices) {
		this.name = name;
		this.indices = indices;
	}
	
	/**
	 * Gets the concrete node interface.
	 * @param p			parameter assignment
	 * @return list of node names
	 * @throws Exception is not all required parameters are assigned.
	 */
	public List<String> evaluate(Map<String, Value> p) throws Exception {

		List<String> variables = new ArrayList<String>();
		List<String> tempvars;
		variables.add(name);
		
		for (List<Expression<Integer>> bounds : this.indices) {			
			if (bounds.size() == 1) {
				int i = bounds.get(0).evaluate(p);	
				tempvars = new ArrayList<String>();
				for (String var : variables)
					tempvars.add(var + "[" + i + "]");
				variables = tempvars;			
			} else if (bounds.size() == 2) {
				int a = bounds.get(0).evaluate(p);
				int b = bounds.get(1).evaluate(p);
				tempvars = new ArrayList<String>();
				for (int i = a; i <= b; ++i)	
					for (String var : variables)
						tempvars.add(var + "[" + i + "]");
				variables = tempvars;
			}			
		}
		
		return variables;
	}
	
	/**
	 * Gets the string representation of a parameterized set of nodes.
	 */
	@Override
	public String toString() {
		String s = name;	
		for (List<Expression<Integer>> bounds : this.indices) {			
			if (bounds.size() == 1) {
				s += "[" + bounds.get(0) + "]";
			} else if (bounds.size() == 2) {
				s += "[" + bounds.get(0)  + ".." + bounds.get(1) + "]";
			}			
		}	
		return s;
	}

	@Override
	public List<String> variables() {
		List<String> vars = new ArrayList<String>();
		for (List<Expression<Integer>> l : indices)
			for (Expression<Integer> e : l)
				vars.addAll(e.variables());
		return vars;
	}
}
