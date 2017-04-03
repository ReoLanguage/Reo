package nl.cwi.reo.compile.components;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.predicates.Formula;
import nl.cwi.reo.semantics.predicates.MemoryCell;
import nl.cwi.reo.semantics.predicates.Node;
import nl.cwi.reo.semantics.predicates.Term;

public final class Transition {

	/**
	 * Guard
	 */
	private final Formula guard;

	/**
	 * Guard
	 */
	private final Set<Port> input;

	/**
	 * Output update
	 */
	private final Map<Node, Term> output;

	/**
	 * Memory update
	 */
	private final Map<MemoryCell, Term> memory;

	/**
	 * Constructs a new transition.
	 * 
	 * @param guard
	 *            guard
	 * @param output
	 *            output provided at output ports
	 * @param memory
	 *            update of the memory cells
	 */
	public Transition(Formula guard, Map<Node, Term> output, Map<MemoryCell, Term> memory) {
		if (guard == null)
			throw new IllegalArgumentException("No guard specified.");
		if (output == null)
			throw new IllegalArgumentException("No output values specified.");
		if (memory == null)
			throw new IllegalArgumentException("No memory update specified.");
		this.guard = guard;
		this.output = Collections.unmodifiableMap(output);
		this.memory = Collections.unmodifiableMap(memory);
		Set<Port> I = new HashSet<Port>();
		// find all *used* ports in the formula and the terms.
		this.input = I;
	}
	

	public Transition(Formula guard, Map<Node, Term> output, Map<MemoryCell, Term> memory, Set<Port> input) {
		if (guard == null)
			throw new IllegalArgumentException("No guard specified.");
		if (output == null)
			throw new IllegalArgumentException("No output values specified.");
		if (memory == null)
			throw new IllegalArgumentException("No memory update specified.");
		this.guard = guard;
		this.output = Collections.unmodifiableMap(output);
		this.memory = Collections.unmodifiableMap(memory);
		this.input = input;
	}

	/**
	 * Gets the set of guards.
	 * 
	 * @return guard
	 */
	public Formula getGuard() {
		return this.guard;
	}

	/**
	 * Gets the set of input ports that participate in this transition.
	 * 
	 * @return set of input ports
	 */
	public Set<Port> getInput() {
		return this.input;
	}

	/**
	 * Gets the values assigned to the output ports.
	 * 
	 * @return assignment of terms to output ports.
	 */
	public Map<Node, Term> getOutput() {
		return this.output;
	}

	/**
	 * Retrieves the job constraint of the current transition.
	 * 
	 * @return assignment of terms to memory cells.
	 */
	public Map<MemoryCell, Term> getMemory() {
		return this.memory;
	}

	
	/**
	 * Gets the set of ports that participate in this transition.
	 * 
	 * @return set of ports that participate in this transition
	 */
	public Set<Port> getInterface() {
		Set<Port> ports = new HashSet<Port>(input);
		for (Node x : output.keySet())
			ports.add(x.getPort());
		return ports;
	}
}
