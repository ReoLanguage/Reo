package nl.cwi.reo.interpret.signatures;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nl.cwi.reo.interpret.Evaluable;
import nl.cwi.reo.interpret.arrays.Expression;
import nl.cwi.reo.interpret.variables.VariableName;

public class ParameterList extends ArrayList<Parameter> implements Evaluable<ParameterList> {
	
	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 2066458682039126937L;

	public ParameterList() { }
	
	public ParameterList(List<Parameter> list) {
		if (list == null)
			throw new NullPointerException();
		for (Parameter x : list) {
			if (x == null) 
				throw new NullPointerException();
			super.add(x);
		}
	}

	@Override
	public ParameterList evaluate(Map<VariableName, Expression> params) throws Exception {
		List<Parameter> list_p = new ArrayList<Parameter>();
		for (Parameter x : this) {
			Parameter x_p = x.evaluate(params);
			List<Parameter> x_p_list = x_p.getList();
			if (x_p_list != null) {
				list_p.addAll(x_p_list);
			} else {
				list_p.add(x_p);
			}
		}
		return new ParameterList(list_p);
	}
	
	@Override
	public String toString() {
		String s = "<";
		Iterator<Parameter> x = this.iterator();
		while (x.hasNext())
			s += x.next() + (x.hasNext() ? "," : "");
		return s + ">";
	}
}