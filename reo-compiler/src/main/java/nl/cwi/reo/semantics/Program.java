package nl.cwi.reo.semantics;

import java.util.HashSet;
import java.util.Set;

/**
 * A Reo program that consist of a set of component instances {@link nl.cwi.reo.semantics.Instance}.
 * Instances synchronize via put and get operations on shared nodes.
 */
public class Program {
	
	/**
	 * Set of component instances.
	 */
	private Set<Instance> instances;
	
	/**
	 * Constructs an empty Reo program.
	 */
	public Program() {
		this.instances = new HashSet<Instance>();
	}
	
	/**
	 * Constructor.
	 * @param instances		set of component instances
	 */
	public Program(Set<Instance> instances) {
		this.instances = instances;
	}
	
	/**
	 * Gets the component instances.
	 * @return set of component instances.
	 */
	public Set<Instance> getInstances() {
		return this.instances;
	}
	
	/**
	 * Adds the instances of a given program to the current program.
	 * @param program		Reo program
	 * @return true if the program is successfully added. 
	 */
	public boolean add(Program program) {
		return this.instances.addAll(program.instances);
	}
	
	/**
	 * Get the string representation of a Reo program.
	 */
	public String toString() {
		StringBuilder str = new StringBuilder();
		
		int i = 0;
		for (Instance inst : this.instances)
			str.append("Component Instance " + ++i + ": \n" + inst + "\n");
		 
		return str.toString();
		
	}
}
