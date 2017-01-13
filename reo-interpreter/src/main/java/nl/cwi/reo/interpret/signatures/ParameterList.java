package nl.cwi.reo.interpret.signatures;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nl.cwi.reo.interpret.Evaluable;
import nl.cwi.reo.interpret.ranges.Expression;
import nl.cwi.reo.interpret.variables.VariableName;
import nl.cwi.reo.interpret.variables.VariableNameList;

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
			if (x_p.getVariable() instanceof VariableNameList) {
				for (VariableName v : ((VariableNameList)x_p.getVariable()).getList())
					list_p.add(new Parameter(v, x_p.getType()));
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