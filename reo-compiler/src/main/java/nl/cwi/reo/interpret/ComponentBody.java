package nl.cwi.reo.interpret;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A parameterized for loop of a set {link java.util.Set}&lt;{link nl.cwi.reo.parse.Component}&gt; of parameterized components.
 */
public class ComponentBody implements Evaluable<Component> {
	
	/**
	 * Components.
	 */
	public Set<Evaluable<Component>> components;
	
	/**
	 * Local definitions.
	 */
	public List<Definition> definitions;

	/**
	 * Constructs a body of components and definitions.
	 * @param components	set of component expressions
	 * @param definitions	s of definitions
	 */
	public ComponentBody(Set<Evaluable<Component>> components, List<Definition> definitions) {
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
		for (Evaluable<Component> C : components) 
			comps.add(C.evaluate(p));
		return new Component(comps);
	}
}
