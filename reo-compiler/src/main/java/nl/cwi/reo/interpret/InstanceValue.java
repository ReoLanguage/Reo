package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * A program that consist of a set of component instances {@link nl.cwi.reo.semantics.Atom}.
 * Instances synchronize via put and get operations on shared nodes.
 */
public final class InstanceValue implements InstanceExpression {
	
	/**
	 * Atomic components.
	 */
	private final Map<Semantics<?>, Map<String, NodeName>> atoms;
	
	/**
	 * Constructs an empty program.
	 */
	public InstanceValue() {
		this.atoms = new HashMap<Semantics<?>, Map<String, NodeName>>();
	}
	
	/**
	 * Constructs a single atom program.
	 */
	public InstanceValue(Semantics<?> component, Map<String, NodeName> links) {
		Map<Semantics<?>, Map<String, NodeName>> atoms = new HashMap<Semantics<?>, Map<String, NodeName>>();
		atoms.put(component, links);
		this.atoms = Collections.unmodifiableMap(atoms);		
	}
	
	/**
	 * Constructs a single atom program.
	 */
	public InstanceValue(Map<Semantics<?>, Map<String, NodeName>> atoms) {
		this.atoms = atoms;
	}
	
	/**
	 * Gets the component instances.
	 * @return set of component instances.
	 */
	public Map<Semantics<?>, Map<String, NodeName>> getAtomics() {
		return this.atoms;
	}

	@Override
	public InstanceValue evaluate(DefinitionList params) throws Exception {
		return this;
	}
	
	/**
	 * Composes a set of programs into a single program.
	 * @param progs		set of component instances
	 */
	public InstanceValue compose(List<InstanceValue> components) {
		
		Map<Semantics<?>, Map<String, NodeName>> atoms = new HashMap<Semantics<?>, Map<String, NodeName>>();
		
		List<InstanceValue> comps = new ArrayList<InstanceValue>(components);
		comps.add(0, this);
		
		// To avoid sharing hidden ports, add an suffix to them
		int i = 0;
		for (InstanceValue C : comps) {
			for (Map.Entry<Semantics<?>, Map<String, NodeName>> atom : C.atoms.entrySet()) {
				Map<String, NodeName> links = new HashMap<String, NodeName>();
				for (Map.Entry<String, NodeName> entry : atom.getValue().entrySet()) {
					NodeName n = entry.getValue();
					if (n.isHidden()) 
						links.put(entry.getKey(), n.rename(n.getName() + "." + i++)); 
					// TODO This code relies on the fact that periods are not allowed in port names.
				}	
				// TODO this code relies on the assumption that two copies of the same component are
				// different iff they are different instances.
				atoms.put(atom.getKey(), links);	
			}
		}
		
		return new InstanceValue(atoms);
	}
	
//	/**
//	 * Restricts the interface of this component by hiding all non-exposed nodes.
//	 * @param intface		resulting interface of this component
//	 */
//	public InstanceValue restrict(Set<String> intface) {
//		
//		Map<Semantics<?>, Map<String, Node>> atoms = new HashMap<Semantics<?>, Map<String, Node>>();
//		
//		for (Map.Entry<Semantics<?>, Map<String, Node>> atom : this.atoms.entrySet()) {
//			Map<String, Node> links = new HashMap<String, Node>();
//			for (Map.Entry<String, Node> entry : atom.getValue().entrySet()) {
//				Node n = entry.getValue();
//				if (!n.isHidden() && !intface.contains(n.getName()))
//					links.put(entry.getKey(), n.hide());
//			}
//			atoms.put(atom.getKey(), links);
//		}
//		
//		return new InstanceValue(atoms);
//	}	
	
	/**
	 * Relabels the interface of this component.
	 * @param iface		maps old node names to new node names.
	 */
	public InstanceValue restrictAndRename(Map<NodeName, NodeName> iface) {	

		Map<Semantics<?>, Map<String, NodeName>> atoms = new HashMap<Semantics<?>, Map<String, NodeName>>();
		
		for (Map.Entry<Semantics<?>, Map<String, NodeName>> atom : this.atoms.entrySet()) {
			Map<String, NodeName> links = new HashMap<String, NodeName>();
			for (Map.Entry<String, NodeName> link : atom.getValue().entrySet()) {
				NodeName n = iface.get(link.getValue());
				if (n == null) n = link.getValue().hide();
				links.put(link.getKey(), n);
			}
			atoms.put(atom.getKey(), links);
		}
		
		return new InstanceValue(atoms);
	}	
	
	/**
	 * Get the string representation of a program.
	 */
	public String toString() {
		StringBuilder str = new StringBuilder();
		int i = 0;
		for (Map.Entry<Semantics<?>, Map<String, NodeName>> atom : this.atoms.entrySet()) {
			str.append("Component " + ++i + ": \n");
			str.append("Nodes  : " + atom.getValue() + "\n");
			str.append("Atom   : " + atom.getKey() + "\n");	
		}
		return str.toString();
	}
}
