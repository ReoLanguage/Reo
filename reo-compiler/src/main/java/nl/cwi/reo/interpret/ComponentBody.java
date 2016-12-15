package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A parameterized for loop of a set {link java.util.Set}&lt;{link nl.cwi.reo.parse.Component}&gt; of parameterized components.
 */
public class ComponentBody implements Expression<Component> {
	
	/**
	 * Components.
	 */
	public Set<ComponentExpression> components;
	
	/**
	 * Local definitions.
	 */
	public List<Definition> definitions;

	/**
	 * Constructs a body of components and definitions.
	 * @param components	set of component expressions
	 * @param definitions	s of definitions
	 */
	public ComponentBody(Set<ComponentExpression> components, List<Definition> definitions) {
		this.components = components;
		this.definitions = definitions;
	}
	
	/**
	 * Evaluates this body for a particular parameter assignment.
	 * @param p			parameter assignment
	 * @return Component for this body.
	 * @throws Exception if not all required parameters are provided.
	 */
	public Component evaluate(Map<String, Value> p) throws Exception {	
		Set<Component> comps = new HashSet<Component>();
		for (ComponentExpression C : components) 
			comps.add(C.evaluate(p));
		return new Component(comps);
	}
	
	/**
	 * Gets all variables in order of occurrence. 
	 * @return list of all variables in order of occurrence.
	 */
	public List<String> variables() {
		List<String> vars = new ArrayList<String>();
		for (ComponentExpression C : components) 
			vars.addAll(C.variables());
		for (Definition D : definitions) 
			vars.addAll(D.variables());
		return vars;
	}
}
