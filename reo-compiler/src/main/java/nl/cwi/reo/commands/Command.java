package nl.cwi.reo.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.util.Queue;

import nl.cwi.reo.interpret.connectors.Language;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.predicates.Conjunction;
import nl.cwi.reo.semantics.predicates.Formula;
import nl.cwi.reo.semantics.predicates.Function;
import nl.cwi.reo.semantics.predicates.MemoryVariable;
import nl.cwi.reo.semantics.predicates.PortVariable;
import nl.cwi.reo.semantics.predicates.Relation;
import nl.cwi.reo.semantics.predicates.Term;
import nl.cwi.reo.semantics.predicates.Variable;
import nl.cwi.reo.templates.Transition;
import nl.cwi.reo.templates.maude.MaudeTransition;
import nl.cwi.reo.templates.prism.PrismTransition;
import nl.cwi.reo.templates.promela.PromelaTransition;
import nl.cwi.reo.templates.treo.TreoTransition;

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
		Set<Port> N = new HashSet<>();
		for (Variable v : guard.getFreeVariables())
			if (v instanceof PortVariable)
				N.add(((PortVariable) v).getPort());
		for (Variable v : update.keySet()){
			if (v instanceof PortVariable)
				N.add(((PortVariable) v).getPort());
			for(Variable _v : update.get(v).getFreeVariables()){
				if (_v instanceof PortVariable)
					N.add(((PortVariable) _v).getPort());				
			}
			
		}
		return N;
	}

	public Transition toTransition(Language lang) {
				
		switch (lang) {
		case PRISM:
			return new PrismTransition(guard, update, constraint);
		case PROMELA:
			return new PromelaTransition(guard, update, constraint);
		case JAVA:
		case C11:
			return new Transition(guard, update, constraint);
		case MAUDE:
			return new MaudeTransition(guard, update, constraint);
		case TREO:
			return new TreoTransition(guard, update, constraint);
		case PRT:
			break;
		case TEXT:
			break;
		default:
			break;
		}
		return null;				
	}
	
	@Override
	public String toString() {
		return guard + " -> " + update + " : " + constraint;
	}
}
