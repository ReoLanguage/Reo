package nl.cwi.reo.interpret;

import java.util.HashMap;
import java.util.Map;

public class BodyDefinitionList implements BodyDefinition {

	private Map<VariableName, Expression> list;
	
	/**
	 * Constructs an empty list of definitions.
	 */
	public BodyDefinitionList() {
		this.list = new HashMap<VariableName, Expression>();
	}
	
	public BodyDefinitionList(BodyDefinitionList defs) {
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
	
	public void putAll(BodyDefinitionList defs) {
		list.putAll(defs.list);
	}
	
	public void remove(VariableName x) {
		list.remove(x);
	}

	@Override
	public BodyDefinitionList evaluate(Map<VariableName, Expression> params)
			throws Exception {

		BodyDefinitionList list_p = new BodyDefinitionList();
		for (Map.Entry<VariableName, Expression> def : list.entrySet()) 
			list_p.put(def.getKey(), def.getValue().evaluate(params));
		
		return new BodyDefinitionList(list_p);
	}

	@Override
	public String toString() {
		return "" + list;
	}
}
