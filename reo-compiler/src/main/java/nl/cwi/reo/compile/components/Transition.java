package nl.cwi.reo.compile.components;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.predicates.Formula;
import nl.cwi.reo.semantics.predicates.MemCell;
import nl.cwi.reo.semantics.predicates.Node;
import nl.cwi.reo.semantics.predicates.Term;
import nl.cwi.reo.semantics.rbautomaton.RuleNode;

public final class Transition {

	/**
	 * Guard
	 */
	private final Formula guard;

	/**
	 * Set of input ports
	 */
	private final Set<Port> input;

	/**
	 * Output update
	 */
	private final Map<Node, Term> output;

	/**
	 * Memory update
	 */
	private final Map<MemCell, Term> memory;

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
	public Transition(Formula guard, Map<Node, Term> output, Map<MemCell, Term> memory) {
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
	

	public Transition(Formula guard, Map<Node, Term> output, Map<MemCell, Term> memory, Set<Port> input) {
		if (guard == null)
			throw new IllegalArgumentException("No guard specified.");
		if (output == null)
			throw new IllegalArgumentException("No output values specified.");
		if (memory == null)
			throw new IllegalArgumentException("No memory update specified.");
		this.guard = guard;
		this.output = Collections.unmodifiableMap(output);
		this.memory = Collections.unmodifiableMap(memory);
		this.input = Collections.unmodifiableSet(input);
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
	 * Retrieves the values assigned to memory cells.
	 * 
	 * @return assignment of terms to memory cells.
	 */
	public Map<MemCell, Term> getMemory() {
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
			if(!x.isVoid())
				ports.add(x.getPort());	
		
		return ports;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(@Nullable Object other) {
		if (other == null)
			return false;
		if (other == this)
			return true;
		if (!(other instanceof Transition))
			return false;
		Transition rule = (Transition) other;
		return Objects.equals(this.guard,rule.getGuard())&&
				Objects.equals(this.output,rule.getOutput())&&
				Objects.equals(this.memory,rule.getMemory())&&
				Objects.equals(this.input,rule.getInput());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.guard,this.output,this.memory,this.input);
	}
}
