package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class Definition implements Expression<Map<String, Value>> {

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

		List<String> varp = this.var.evaluate(p);
		Value valuep = this.value.evaluate(p);
		
		
		if (varp.size() > 1) {
			if (valuep.getType() == ValueType.LIST) {
				List<Value> list = valuep.getList();
				
				
				Iterator<String> vars = varp.iterator();
				Iterator<Value> vals = list.iterator();
				
				while (vars.hasNext() && vals.hasNext()) 
				    defs.put(vars.next(), vals.next());
				
				if (vars.hasNext() || vals.hasNext())
					throw new Exception("ERROR: List sizes of " + varp + " and " + valuep + " do not match.");
				
				
			} else {
				throw new Exception("ERROR: Value " + valuep + " is not of type list.");				
			}
		} else { 
			if (valuep.getType() != ValueType.LIST) {
				Iterator<String> iter = varp.iterator();
				if (iter.hasNext())
					defs.put(iter.next(), valuep);
			} else {
				throw new Exception("ERROR: Value " + valuep + " is cannot be of type list.");	
			}
		}
		
		return defs;
	}

	public List<String> variables() {
		List<String> vars = new ArrayList<String>();
		vars.addAll(var.variables());
		vars.addAll(value.variables());
		return null;
	}
}
