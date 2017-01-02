package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.List;

public class ParameterList implements Evaluable<ParameterList> {
	
	private final List<Parameter> list;
	
	public ParameterList(List<Parameter> list) {
		this.list = list;
	}

	public List<Parameter> getList() {
		return list;
	}

	@Override
	public ParameterList evaluate(DefinitionList params) throws Exception {
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
}