package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ParameterList implements Evaluable<ParameterList> {
	
	private final List<Parameter> list;
	
	public ParameterList() {
		this.list = Collections.unmodifiableList(new ArrayList<Parameter>());
	}
	
	public ParameterList(List<Parameter> list) {
		if (list == null)
			throw new NullPointerException();
		this.list = Collections.unmodifiableList(list);
	}

	public List<Parameter> getList() {
		return list;
	}

	@Override
	public ParameterList evaluate(Map<VariableName, Expression> params) throws Exception {
		List<Parameter> list_p = new ArrayList<Parameter>();
		for (Parameter x : list) {
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
		Iterator<Parameter> x = list.iterator();
		while (x.hasNext())
			s += x.next() + (x.hasNext() ? "," : "");
		return s + ">";
	}
}