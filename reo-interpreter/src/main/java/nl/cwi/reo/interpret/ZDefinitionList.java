package nl.cwi.reo.interpret;

import java.util.HashMap;
import java.util.Map;

public class ZDefinitionList implements ZDefinition {

	private Map<VariableName, Expression> list;
	
	/**
	 * Constructs an empty list of definitions.
	 */
	public ZDefinitionList() {
		this.list = new HashMap<VariableName, Expression>();
	}
	
	public ZDefinitionList(ZDefinitionList defs) {
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
	
	public void putAll(ZDefinitionList defs) {
		list.putAll(defs.list);
	}
	
	public void remove(VariableName x) {
		list.remove(x);
	}

	@Override
	public ZDefinitionList evaluate(Map<VariableName, Expression> params)
			throws Exception {

		ZDefinitionList list_p = new ZDefinitionList();
		for (Map.Entry<VariableName, Expression> def : list.entrySet()) 
			list_p.put(def.getKey(), def.getValue().evaluate(params));
		
		return new ZDefinitionList(list_p);
	}

	@Override
	public String toString() {
		return "" + list;
	}
}
