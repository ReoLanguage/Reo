package nl.cwi.reo.interpret.signatures;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nl.cwi.reo.interpret.Evaluable;
import nl.cwi.reo.interpret.arrays.Expression;
import nl.cwi.reo.interpret.variables.VariableName;

public class NodeList extends ArrayList<Node> implements Evaluable<NodeList> {
	
	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = -8325490660234832292L;

	public NodeList() { }
	
	public NodeList(List<Node> list) {
		if (list == null)
			throw new NullPointerException();
		for (Node x : list) {
			if (x == null) 
				throw new NullPointerException();
			super.add(x);
		}
	}

	@Override
	public NodeList evaluate(Map<VariableName, Expression> params) throws Exception {
		List<Node> list_p = new ArrayList<Node>();
		for (Node x : this) {
			Node x_p = x.evaluate(params);			
			List<Node> x_p_list = x_p.getList();
			if (x_p_list != null) {
				list_p.addAll(x_p_list);
			} else {
				list_p.add(x_p);
			}
		}
		return new NodeList(list_p);
	}
	
	@Override
	public String toString() {
		String s = "(";
		Iterator<Node> x = this.iterator();
		while (x.hasNext())
			s += x.next() + (x.hasNext() ? "," : "");
		return s + ")";
	}
}
