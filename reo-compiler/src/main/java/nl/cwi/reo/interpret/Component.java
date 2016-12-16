package nl.cwi.reo.interpret;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A program that consist of a set of component instances {@link nl.cwi.reo.semantics.Atom}.
 * Instances synchronize via put and get operations on shared nodes.
 */
public class Component {
	
	/**
	 * Set of component instances.
	 */
	private Set<Atom> atoms;
	
	/**
	 * Constructs an empty program.
	 */
	public Component() {
		this.atoms = new HashSet<Atom>();
	}
	
	/**
	 * Constructs a single atom program.
	 */
	public Component(Atom atom) {
		this.atoms = new HashSet<Atom>();
		atoms.add(atom);
	}
	
	/**
	 * Composes a set of programs into a single program.
	 * @param progs		set of component instances
	 */
	public Component(Set<Component> components) {
		this.atoms = new HashSet<Atom>();
		int i = 0;
		for (Component C : components) {
			for (Atom A : C.atoms)
				A.addSuffixToHidden("" + i++);
			this.atoms.addAll(C.getAtoms());
		}
	}
	
	/**
	 * Gets the component instances.
	 * @return set of component instances.
	 */
	public Set<Atom> getAtoms() {
		return this.atoms;
	}
	
	/**
	 * Restricts the interface of this component by hiding all non-exposed nodes.
	 * @param intface		resulting interface of this component
	 */
	public void restrict(Set<String> intface) {
		for (Atom A : atoms) 
			A.restrict(intface);
	}	
	
	/**
	 * Relabels the interface of this component.
	 * @param r		maps old node names to new node names.
	 */
	public void relabel(Map<String,String> r) {
		for (Atom A : atoms) 
			A.relabel(r);
	}
	
	/**
	 * Get the string representation of a program.
	 */
	public String toString() {
		StringBuilder str = new StringBuilder();
		
		int i = 0;
		for (Atom a : this.atoms)
			str.append("Component " + ++i + ": \n" + a + "\n");
		 
		return str.toString();
		
	}
}
