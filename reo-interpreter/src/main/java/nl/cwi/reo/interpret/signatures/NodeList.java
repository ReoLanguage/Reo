package nl.cwi.reo.interpret.signatures;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.interpret.variables.VariableName;
import nl.cwi.reo.interpret.variables.VariableNameList;
import nl.cwi.reo.semantics.api.Evaluable;
import nl.cwi.reo.semantics.api.Expression;

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
	public NodeList evaluate(Map<String, Expression> params) throws CompilationException {
		List<Node> list_p = new ArrayList<Node>();
		for (Node x : this) {
			Node x_p = x.evaluate(params);	
			if (x_p.getVariable() instanceof VariableNameList) {
				for (VariableName v : ((VariableNameList)x_p.getVariable()).getList())
					list_p.add(new Node(v, x_p.getNodeType(), x_p.getTypeTag()));
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
