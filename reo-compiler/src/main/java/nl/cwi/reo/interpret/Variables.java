package nl.cwi.reo.interpret;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A parameterized set of variable names, such as, for example, 
 * (a.b:int, c[-31]?, x.y.z[-1][0...1-k][5]!bool).
 */
public final class Variables implements Evaluable<Map<String, String>> {

	/**
	 * Fully qualified name.
	 */
	private final String name;

	/**
	 * Type tag.
	 */
	private final String type;
	
	/**
	 * Indices.
	 */
	private final List<List<Evaluable<Integer>>> indices;
	
	/**
	 * Constructs a variable list.
	 * @param name		name of the node
	 */
	public Variables(String name, String type, List<List<Evaluable<Integer>>> indices) {
		this.name = name;
		this.type = type;
		this.indices = indices;
	}
	
	/**
	 * Gets the concrete node interface.
	 * @param p			parameter assignment
	 * @return list of node names
	 * @throws Exception is not all required parameters are assigned.
	 */
	public Map<String,String> evaluate(Map<String, Value> p) throws Exception {

		Map<String, String> variables = new LinkedHashMap<String, String>();
		Map<String, String> tempvars;
		variables.put(name, "");
		
		for (List<Evaluable<Integer>> bounds : this.indices) {			
			if (bounds.size() == 1) {
				int i = bounds.get(0).evaluate(p);	
				tempvars = new LinkedHashMap<String, String>();
				for (Map.Entry<String, String> var : variables.entrySet())
					tempvars.put(var.getKey() + "[" + i + "]", type);
				variables = tempvars;
			} else if (bounds.size() == 2) {
				int a = bounds.get(0).evaluate(p);
				int b = bounds.get(1).evaluate(p);
				tempvars = new LinkedHashMap<String, String>();
				for (int i = a; i <= b; ++i)	
					for (Map.Entry<String, String> var : variables.entrySet())
						tempvars.put(var.getKey() + "[" + i + "]", type);
				variables = tempvars;
			}			
		}
		
		return variables;
	}
	
	/**
	 * Gets the type tag of this variable list
	 * @return type tag
	 */
	public String getTypetag() {
		return this.type;
	}
	
	/**
	 * Gets the string representation of a parameterized set of nodes.
	 */
	@Override
	public String toString() {
		String s = name;	
		for (List<Evaluable<Integer>> bounds : this.indices) {			
			if (bounds.size() == 1) {
				s += "[" + bounds.get(0) + "]";
			} else if (bounds.size() == 2) {
				s += "[" + bounds.get(0)  + "..." + bounds.get(1) + "]";
			}			
		}	
		return s;
	}
}
