package nl.cwi.reo.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A parameterized set of node names, such as, for example, (a, a[-31], b[0...1-k]).
 */
public final class Nodes {

	/**
	 * Port name.
	 */
	private final String name;
	
	/**
	 * First expression.
	 */
	private final Expression expr1;

	/**
	 * Second expression.
	 */
	private final Expression expr2;
	
	/**
	 * Constructs a node (name).
	 * @param name		name of the node
	 */
	public Nodes(String name) {
		this.name = name;
		this.expr1 = null;
		this.expr2 = null;
	}
	
	/**
	 * Constructs a node with index (name[expr]).
	 * @param name		name of the node
	 * @param expr		index of the node
	 */
	public Nodes(String name, Expression expr) {
		this.name = name;
		this.expr1 = expr;
		this.expr2 = null;
	}
	
	/**
	 * Constructors a node range (name[lower...upper]).
	 * @param name		name of the nodes
	 * @param lower		lower bound
	 * @param upper		upper bound
	 */
	public Nodes(String name, Expression lower, Expression upper) {
		this.name = name;
		this.expr1 = lower;
		this.expr2 = upper;
	}
	
	/**
	 * Gets the concrete node interface.
	 * @param parameters		parameter assignment
	 * @return list of node names
	 * @throws Exception is not all required parameters are assigned.
	 */
	public List<String> getNodes(Map<String, String> parameters) throws Exception {
		
		List<String> nodes = new ArrayList<String>();
		
		if (expr1 == null) {
			
			// If the first expression is null, then this node is just a name.
			nodes.add(name);
			
		} else if (expr1 != null & expr2 == null) {
			
			// If the first expression is not null while the second expression is null,
			// then this node is a name with an index.
			int x = expr1.eval(parameters);				
			nodes.add(name + "[" + x + "]");
			
		} else if (expr1 != null & expr2 != null) {
			
			// If both expressions are not null, then this node is a range of indexed nodes.
			int a = expr1.eval(parameters);
			int b = expr2.eval(parameters);
			for (int i = a; i <= b; ++i)
				nodes.add(name + "[" + i + "]");
		}
		
		return nodes;
	}
	
	/**
	 * Gets the string representation of a parameterized set of nodes.
	 */
	@Override
	public String toString() {
		String s = name;		
		if (expr1 != null & expr2 == null)
			s += "[" + expr1 + "]";		
		if (expr1 != null & expr2 != null)
			s += "[" + expr1  + "..." + expr2 + "]";		
		return s;
	}
}
