package nl.cwi.reo.commands;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.predicates.Formula;
import nl.cwi.reo.semantics.predicates.MemoryVariable;
import nl.cwi.reo.semantics.predicates.PortVariable;
import nl.cwi.reo.semantics.predicates.Term;
import nl.cwi.reo.semantics.predicates.Variable;
import nl.cwi.reo.templates.Transition;

/**
 * A command that represents an atomic action of the system. A command consists
 * of a guard (a formula wherein each variable is an input or a current memory
 * value), an update (a map that assigns to each output port and memory update a
 * term wherein each variable is an input or a current memory value), and a
 * constraint (a generic formula that must be solved by a constraint solver at
 * runtime).
 */
public class Command {

	/** The guard. */
	private final Formula guard;

	/** The update. */
	private final Map<Variable, Term> update;

	/** The constraint. */
	private final Formula constraint;

	/**
	 * Instantiates a new command.
	 *
	 * @param guard
	 *            the guard
	 * @param constraint
	 *            the constraint
	 * @param update
	 *            the update
	 */
	public Command(Formula guard, Map<Variable, Term> update, Formula constraint) {
		this.guard = guard;
		this.constraint = constraint;
		this.update = update;
	}

	/**
	 * Gets the guard.
	 *
	 * @return the guard
	 */
	public Formula getGuard() {
		return guard;
	}

	/**
	 * @return the constraint
	 */
	public Formula getConstraint() {
		return constraint;
	}

	/**
	 * Gets the update.
	 *
	 * @return the update
	 */
	public Map<Variable, Term> getUpdate() {
		return update;
	}

	/**
	 * Gets all ports that occur in this command.
	 * 
	 * @return the ports
	 */
	public Set<Port> getPorts() {
		Set<Port> N = new HashSet<>(guard.getPorts());
		for (Variable v : update.keySet())
			if (v instanceof PortVariable)
				N.add(((PortVariable) v).getPort());
		return N;
	}

	public Transition toTransition() {
		Map<PortVariable, Term> output = new HashMap<>();
		Map<MemoryVariable, Term> memory = new HashMap<>();
		for (Map.Entry<Variable, Term> entry : update.entrySet()) {
			if (entry.getKey() instanceof PortVariable)
				output.put((PortVariable) entry.getKey(), entry.getValue());
			if (entry.getKey() instanceof MemoryVariable)
				memory.put((MemoryVariable) entry.getKey(), entry.getValue());
		}
		return new Transition(guard, output, memory, getPorts());
	}
	
	@Override
	public String toString() {
		return guard + " -> " + update + " : " + constraint;
	}
}
