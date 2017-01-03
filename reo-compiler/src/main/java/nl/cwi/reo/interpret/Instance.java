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
public final class Instance implements Evaluable<Instance> {
	
	/**
	 * Atomic components.
	 */
	private final Map<Semantics<?>, Map<String, Port>> atoms;
	
	/**
	 * Constructs an empty program.
	 */
	public Instance() {
		this.atoms = new HashMap<Semantics<?>, Map<String, Port>>();
	}
	
	/**
	 * Constructs a single atom program.
	 */
	public Instance(Semantics<?> component, Map<String, Port> links) {
		if (component == null || links == null)
			throw new IllegalArgumentException("Arguments cannot be null.");
		Map<Semantics<?>, Map<String, Port>> atoms = new HashMap<Semantics<?>, Map<String, Port>>();
		atoms.put(component, links);
		this.atoms = Collections.unmodifiableMap(atoms);		
	}
	
	/**
	 * Constructs a single atom program.
	 */
	public Instance(Map<Semantics<?>, Map<String, Port>> atoms) {
		this.atoms = atoms;
	}
	
	/**
	 * Gets the component instances.
	 * @return set of component instances.
	 */
	public Map<Semantics<?>, Map<String, Port>> getAtomics() {
		return this.atoms;
	}

	@Override
	public Instance evaluate(DefinitionList params) throws Exception {
		return this;
	}
	
	/**
	 * Composes a set of programs into a single program.
	 * @param progs		set of component instances
	 */
	public Instance compose(List<Instance> components) {
		
		Map<Semantics<?>, Map<String, Port>> atoms = new HashMap<Semantics<?>, Map<String, Port>>();
		
		List<Instance> comps = new ArrayList<Instance>(components);
		comps.add(0, this);
		
		// To avoid sharing hidden ports, add an suffix to them
		int i = -1;
		for (Instance C : comps) {
			i += 1;
			for (Map.Entry<Semantics<?>, Map<String, Port>> atom : C.atoms.entrySet()) {
				Map<String, Port> links = new HashMap<String, Port>();
				for (Map.Entry<String, Port> entry : atom.getValue().entrySet()) {
					Port n = entry.getValue();
					if (n.isHidden()) {
						links.put(entry.getKey(), n.addSuffix("#" + i)); 
					} else {
						links.put(entry.getKey(), n); 						
					}
				}	
				// TODO this code relies on the assumption that two copies of the same component are
				// different iff they are different Java object instances.
				atoms.put(atom.getKey(), links);	
			}
		}
		
		Instance composition = new Instance(atoms);
		
		return composition;
	}
	
	/**
	 * Relabels the interface of this component.
	 * @param iface		maps old node names to new node names.
	 */
	public Instance restrictAndRename(Map<Port, Port> iface) {	

		Map<Semantics<?>, Map<String, Port>> atoms = new HashMap<Semantics<?>, Map<String, Port>>();
		
		for (Map.Entry<Semantics<?>, Map<String, Port>> atom : this.atoms.entrySet()) {
			Map<String, Port> links = new HashMap<String, Port>();
			for (Map.Entry<String, Port> link : atom.getValue().entrySet()) {
				Port n = iface.get(link.getValue());
				if (n == null) n = link.getValue().hide();
				links.put(link.getKey(), n);
			}
			atoms.put(atom.getKey(), links);
		}
		
		return new Instance(atoms);
	}	
	
	/**
	 * Get the string representation of a program.
	 */
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (Map.Entry<Semantics<?>, Map<String, Port>> atom : this.atoms.entrySet()) {
			str.append("\n");
			str.append("Ports  : " + atom.getValue() + "\n");
			str.append("Atom   : " + atom.getKey() + "\n");	
		}
		return str.toString();
	}
}
