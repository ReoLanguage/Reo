package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A parameterized reference to component definition {@link nl.cwi.reo.parse.Definition}.
 */
public final class Reference implements Expression<Component> {

	/**
	 * Abstract component definition.
	 */
	public final ComponentExpression definition;
	
	/**
	 * Parameter assignment.
	 */
	public final List<ValueExpression> parameters;
	
	/**
	 * Abstract interface of the component reference
	 */
	private final Expression<List<String>> intface;
	
	/**
	 * Constructs an abstract component that instantiates a defined abstract component. 
	 * @param definition		defining component
	 * @param parameters		parameters of the referring component
	 * @param intface			interface of the referring component
	 */
	public Reference(ComponentExpression definition, List<ValueExpression> parameters, 
			Expression<List<String>> intface) {
		this.definition = definition;
		this.parameters = parameters;
		this.intface = intface;
	}
	
	/**
	 * Gets a concrete program from this parameterized component.
	 * @param p				parameter assignment
	 * @return concrete program {@link nl.cwi.reo.semantics.Program} for this parameterized component
	 * @throws Exception if not all required parameters are assigned.
	 */
	@Override
	public Component evaluate(Map<String, Value> p) throws Exception {
		
		// Find the assignment of parameters of this.component
		Map<String, Value> p1 = new HashMap<String, Value>();
		Map<String, String> defn_params = this.definition.getParameters(p);
		List<Value> paramvalues = new ArrayList<Value>();
		for (ValueExpression e : parameters)
			paramvalues.add(e.evaluate(p));
		
		if (defn_params.size() != paramvalues.size()) 
			throw new Exception("Parameters " +  defn_params +  " and " + paramvalues + " do not match.");
		
		for (int i = 0; i < defn_params.size(); i++) 
			p1.put(defn_params.get(i), paramvalues.get(i));
		
		Component C = this.definition.evaluate(p1);
		
		// Determine the interface relabeling r
		Map<String, String> r = new HashMap<String, String>();			
		Map<String, String> Pdef = this.definition.getInterface(p1);
		List<String> Pref = this.intface.evaluate(p);
		Iterator<Map.Entry<String, String>> Edef = Pdef.entrySet().iterator();
		Iterator<String> Eref = Pref.iterator();
		
		while (Edef.hasNext() && Eref.hasNext()) {
		    Map.Entry<String, String> e1 = Edef.next();
		    String e2 = Eref.next();
		    r.put(e1.getKey(), e2);
		}
		
		if (Edef.hasNext() || Eref.hasNext())
			throw new Exception("Interfaces " + Pdef + " and " + Eref + " do not match.");
		
		return C.relabel(r);
	}

	public List<String> variables() {
		List<String> vars = new ArrayList<String>();
		for (ValueExpression param : parameters) 
			vars.addAll(param.variables());
		vars.addAll(intface.variables());
		return null;
	}
}