package nl.cwi.reo.interpret;

import java.util.HashMap;
import java.util.Map;

public class DefinitionList implements Definition {

	private Map<VariableName, Expression> list;
	
	/**
	 * Constructs an empty list of definitions.
	 */
	public DefinitionList() {
		this.list = new HashMap<VariableName, Expression>();
	}
	
	public DefinitionList(DefinitionList defs) {
		if (defs == null)
			throw new IllegalArgumentException("Argument cannot be null.");
		this.list = defs.list;
	}
	
	public Expression get(VariableName x) {
		return list.get(x);
	}
	
	public Expression getMain() {
		return list.get(new VariableName("main"));
	}
	
	public void put(VariableName x, Expression e) {
		list.put(x, e);
	}
	
	public void putAll(DefinitionList defs) {
		list.putAll(defs.list);
	}
	
	public void remove(VariableName x) {
		list.remove(x);
	}

	@Override
	public DefinitionList evaluate(DefinitionList params)
			throws Exception {

		DefinitionList list_p = new DefinitionList();
		for (Map.Entry<VariableName, Expression> def : list.entrySet()) 
			list_p.put(def.getKey(), def.getValue().evaluate(params));
		
		return new DefinitionList(list_p);
	}

}
