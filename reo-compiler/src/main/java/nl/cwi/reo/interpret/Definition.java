package nl.cwi.reo.interpret;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class Definition implements Evaluable<Map<String, Value>> {

	/**
	 * Variable list.
	 */
	private final Variables var;

	/**
	 * Abstract value.
	 */
	private final ValueExpression value;

	/**
	 * Constructs an abstract definition
	 * @param var
	 * @param value
	 */
	public Definition(Variables var, ValueExpression value) {
		this.var = var;
		this.value = value;
	}
	
	public Map<String, Value> evaluate(Map<String, Value> p) throws Exception {
		
		Map<String, Value> defs = new HashMap<String, Value>();

		Map<String, String> varp = this.var.evaluate(p);
		Value valuep = this.value.evaluate(p);
		
		
		if (varp.size() > 1) {
			if (valuep.getType() == ValueType.LIST) {
				List<Value> list = valuep.getList();
				
				
				Iterator<Map.Entry<String, String>> vars = varp.entrySet().iterator();
				Iterator<Value> vals = list.iterator();
				
				while (vars.hasNext() && vals.hasNext()) {
				    Map.Entry<String, String> var = vars.next();
				    Value val = vals.next();
				    defs.put(var.getKey(), val);
				}
				
				if (vars.hasNext() || vals.hasNext())
					throw new Exception("ERROR: List sizes of " + varp + " and " + valuep + " do not match.");
				
				
			} else {
				throw new Exception("ERROR: Value " + valuep + " is not of type list.");				
			}
		} else { 
			if (valuep.getType() != ValueType.LIST) {
				Iterator<String> iter = varp.keySet().iterator();
				if (iter.hasNext())
					defs.put(iter.next(), valuep);
			} else {
				throw new Exception("ERROR: Value " + valuep + " is cannot be of type list.");	
			}
		}
		
		return defs;
	}
}
