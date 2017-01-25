package nl.cwi.pr.autom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import nl.cwi.pr.autom.AutomatonFactory.Automaton;
import nl.cwi.pr.autom.ConstraintFactory.Constraint;
import nl.cwi.pr.autom.ConstraintFactory.ConstraintSet;
import nl.cwi.pr.autom.LiteralFactory.Equality;
import nl.cwi.pr.autom.LiteralFactory.Literal;
import nl.cwi.pr.autom.LiteralFactory.LiteralSet;
import nl.cwi.pr.autom.MemoryCellFactory.MemoryCell;
import nl.cwi.pr.autom.MemoryCellFactory.MemoryCellSet;
import nl.cwi.pr.autom.TermFactory.Term;
import nl.cwi.pr.autom.TermFactory.TermSet;
import nl.cwi.pr.misc.IdObjectFactory;
import nl.cwi.pr.misc.PortFactory;
import nl.cwi.pr.misc.PortFactory.Port;
import nl.cwi.pr.misc.PortFactory.PortSet;

public abstract class ConstraintFactory extends
		IdObjectFactory<Constraint, ConstraintSet, ConstraintSpec> {

	private final LiteralFactory literalFactory;

	//
	// CONSTRUCTORS
	//

	public ConstraintFactory(LiteralFactory literalFactory) {
		if (literalFactory == null)
			throw new NullPointerException();

		this.literalFactory = literalFactory;
	}

	//
	// METHODS - PUBLIC
	//

	public Constraint chain(Automaton automaton, Constraint constraint1,
			Constraint constraint2) {

		if (automaton == null)
			throw new NullPointerException();
		if (constraint1 == null)
			throw new NullPointerException();
		if (constraint2 == null)
			throw new NullPointerException();

		if (constraint1.getFactory() != this)
			throw new IllegalStateException();
		if (constraint2.getFactory() != this)
			throw new IllegalStateException();

		/*
		 * Get memory cells
		 */

		Map<MemoryCell, Term> memoryCells = new HashMap<>();

		for (MemoryCell c : constraint1.getPostMemoryCells())
			if (!memoryCells.containsKey(c))
				memoryCells.put(
						c,
						getTermFactory().newOrGet(
								new TermSpec.QuantifiedVariableSpec()));

		for (MemoryCell c : constraint2.getPreMemoryCells())
			if (!memoryCells.containsKey(c))
				memoryCells.put(
						c,
						getTermFactory().newOrGet(
								new TermSpec.QuantifiedVariableSpec()));

		/*
		 * Get substitutions
		 */

		Map<Term, Term> substitutions1 = new HashMap<>();
		Map<Term, Term> substitutions2 = new HashMap<>();

		for (MemoryCell c : constraint1.getPostMemoryCells()) {
			Term oldTerm = getTermFactory().newOrGet(
					new TermSpec.PostVariableSpec(c));
			Term newTerm = memoryCells.get(c);

			substitutions1.put(oldTerm, newTerm);
		}

		for (MemoryCell c : constraint2.getPreMemoryCells()) {
			Term oldTerm = getTermFactory().newOrGet(
					new TermSpec.PreVariableSpec(c));
			Term newTerm = memoryCells.get(c);

			substitutions2.put(oldTerm, newTerm);
		}

		/*
		 * Substitute
		 */

		constraint1 = substitute(automaton, constraint1, substitutions1);
		constraint2 = substitute(automaton, constraint2, substitutions2);
		return compose(automaton, constraint1, constraint2);
	}

	public Constraint compose(Automaton automaton, Constraint constraint1,
			Constraint constraint2) {

		if (automaton == null)
			throw new NullPointerException();
		if (constraint1 == null)
			throw new NullPointerException();
		if (constraint2 == null)
			throw new NullPointerException();

		if (constraint1.getFactory() != this)
			throw new IllegalArgumentException();
		if (constraint2.getFactory() != this)
			throw new IllegalArgumentException();

		LiteralSet literals = literalFactory.newSet();
		literals.addAll(constraint1.getLiterals());
		literals.addAll(constraint2.getLiterals());

		return newOrGet(new ConstraintSpec(automaton, literals));
	}

	public Constraint compose(Automaton automaton, ConstraintSet constraints) {
		if (automaton == null)
			throw new NullPointerException();
		if (constraints == null)
			throw new NullPointerException();
		if (constraints.getFactory() != this)
			throw new IllegalArgumentException();

		LiteralSet literals = literalFactory.newSet();
		for (Constraint constr : constraints)
			literals.addAll(constr.getLiterals());

		return newOrGet(new ConstraintSpec(automaton, literals));
	}

	public LiteralFactory getLiteralFactory() {
		return literalFactory;
	}

	public MemoryCellFactory getMemoryCellFactory() {
		return literalFactory.getMemoryCellFactory();
	}

	public PortFactory getPortFactory() {
		return literalFactory.getPortFactory();
	}

	public TermFactory getTermFactory() {
		return literalFactory.getTermFactory();
	}

	public Constraint newOrGetKeepConstraint(Automaton automaton,
			MemoryCellSet memoryCells) {
		if (automaton == null)
			throw new NullPointerException();
		if (memoryCells == null)
			throw new NullPointerException();
		if (memoryCells.getFactory() != getMemoryCellFactory())
			throw new IllegalStateException();

		LiteralSet literals = literalFactory.newSet();
		for (MemoryCell c : memoryCells) {
			Term preVariable = literalFactory.getTermFactory().newOrGet(
					new TermSpec.PreVariableSpec(c));
			Term postVariable = literalFactory.getTermFactory().newOrGet(
					new TermSpec.PostVariableSpec(c));

			literals.add(literalFactory.newOrGet(new LiteralSpec.EqualitySpec(
					true, preVariable, postVariable)));
		}

		return newOrGet(new ConstraintSpec(automaton, literals));
	}

	@Override
	public ConstraintSet newSet() {
		return new ConstraintSet();
	}

	public Constraint quantifySyntactically(Automaton automaton,
			Constraint constraint, TermSet variables) {

		if (automaton == null)
			throw new NullPointerException();
		if (constraint == null)
			throw new NullPointerException();
		if (constraint.getFactory() != this)
			throw new IllegalArgumentException();

		LiteralSet literals = constraint.getLiterals();

		for (Term t : variables) {

			/*
			 * Get relevant literals
			 */

			LiteralSet tLiterals = literalFactory.newSet();
			for (Literal l : literals)
				if (l.getVariables().contains(t))
					tLiterals.add(l);

			/*
			 * Get a determinant for t
			 */

			Term determinant = null;
			for (Literal l : tLiterals)
				if (l.isEquality()) {
					Equality equality = (Equality) l;
					Term argument1 = equality.getArgument1();
					Term argument2 = equality.getArgument2();

					if (t.equals(argument1))
						determinant = argument2;
					if (t.equals(argument2))
						determinant = argument1;
					if (determinant != null)
						break;
				}

			/*
			 * Substitute the determinant for t
			 */

			if (determinant != null) {
				Map<Term, Term> substitutions = new HashMap<>();
				substitutions.put(t, determinant);

				LiteralSet newLiterals = literalFactory.takeComplement(
						literals, tLiterals);

				for (Literal l : tLiterals) {
					Literal literal = literalFactory.substitute(l,
							substitutions);

					if (!literal.isTautology())
						newLiterals.add(literal);
				}

				literals = newLiterals;
			}
		}

		return newOrGet(new ConstraintSpec(automaton, literals));
	}

	public Constraint substitute(Automaton automaton, Constraint constraint,
			Map<Term, Term> substitutions) {

		if (constraint == null)
			throw new NullPointerException();
		if (substitutions == null)
			throw new NullPointerException();
		if (substitutions.containsKey(null))
			throw new NullPointerException();
		if (substitutions.containsKey(null))
			throw new NullPointerException();

		LiteralSet literals = literalFactory.newSet();
		for (Literal l : constraint.getLiterals())
			literals.add(literalFactory.substitute(l, substitutions));

		return newOrGet(new ConstraintSpec(automaton, literals));
	}

	//
	// CLASSES - PUBLIC
	//

	public class Constraint extends
			IdObjectFactory<Constraint, ConstraintSet, ConstraintSpec>.IdObject {

		private Cache cache;

		private List<Command> commandification = new ArrayList<>();

		//
		// CONSTRUCTORS
		//

		protected Constraint(int id, ConstraintSpec spec) {
			super(id, spec);
		}

		//
		// METHODS - PUBLIC
		//

		public void commandify(TermSet allImportVariables,
				TermSet allExportVariables) {

			if (allImportVariables == null)
				throw new NullPointerException();
			if (allExportVariables == null)
				throw new NullPointerException();

			commandification.clear();

			/*
			 * Add assignment/guarded failure commands
			 */

			LiteralSet literals = getLiteralFactory().newSet();
			literals.addAll(getLiterals());

			TermSet doneVariables = getTermFactory().newSet();
			doneVariables.addAll(allImportVariables);

			int nDoneVariables = -1;
			while (nDoneVariables < doneVariables.count()) {
				nDoneVariables = doneVariables.count();

				for (Literal l : literals) {

					/*
					 * Add an assignment command
					 */

					if (l.isPositiveEquality()) {
						Equality equality = (Equality) l;
						Term argument1 = equality.getArgument1();
						Term argument2 = equality.getArgument2();

						if (argument1.isVariable()
								&& doneVariables.containsAll(argument2
										.getVariables())) {

							commandification.add(new Command.Assignment(
									argument1, argument2));

							literals.remove(l);
							doneVariables.add(argument1);
							continue;
						}

						if (argument2.isVariable()
								&& doneVariables.containsAll(argument1
										.getVariables())) {

							commandification.add(new Command.Assignment(
									argument2, argument1));

							literals.remove(l);
							doneVariables.add(argument2);
							continue;
						}
					}

					/*
					 * Add a guarded failure command
					 */

					if (doneVariables.containsAll(l.getVariables())) {
						commandification.add(new Command.GuardedFailure(l));
						literals.remove(l);
					}
				}
			}

			for (Literal l : literals)
				if (l instanceof Equality) {
					Equality equality = (Equality) l;
					if (equality.getArgument1().isQuantifiedVariable()
							&& equality.getArgument2().isQuantifiedVariable())

						literals.remove(l);
				}

			if (!literals.isEmpty()) {
				commandification.clear();
				return;
			}

			/*
			 * Prepend import commands
			 */

			for (Term t : getTermFactory().takeIntersection(allImportVariables,
					getVariables()))

				if (!t.isPostVariable())
					commandification.add(0, new Command.Import(t));

			/*
			 * Append export commands
			 */

			for (Term t : getTermFactory().takeIntersection(allExportVariables,
					getVariables()))

				if (!t.isPreVariable())
					commandification.add(new Command.Export(t));
		}

		public void disableCache() {
			cache = null;
		}

		public void enableCache() {
			cache = new Cache();
		}

		public Automaton getAutomaton() {
			return getSpec().getAutomaton();
		}

		public List<Command> getCommandification() {
			return commandification;
		}

		public LiteralSet getLiterals() {
			return getSpec().getLiterals();
		}

		public PortSet getPorts() {

			/*
			 * Check cache
			 */

			if (cache != null && cache.getPortsResult != null)
				return cache.getPortsResult;

			/*
			 * Get
			 */

			PortSet ports = getPortFactory().newSet();
			for (Literal l : getLiterals())
				ports.addAll(l.getPorts());

			/*
			 * Update cache and return
			 */

			if (cache != null)
				cache.getPortsResult = ports;

			return ports;
		}

		public MemoryCellSet getPostMemoryCells() {

			/*
			 * Check cache
			 */

			if (cache != null && cache.getPostMemoryCellsResult != null)
				return cache.getPostMemoryCellsResult;

			/*
			 * Get
			 */

			MemoryCellSet memoryCells = getMemoryCellFactory().newSet();
			for (Literal l : getLiterals())
				memoryCells.addAll(l.getPostMemoryCells());

			/*
			 * Update cache and return
			 */

			if (cache != null)
				cache.getPostMemoryCellsResult = memoryCells;

			return memoryCells;
		}

		public MemoryCellSet getPreMemoryCells() {

			/*
			 * Check cache
			 */

			if (cache != null && cache.getPreMemoryCellsResult != null)
				return cache.getPreMemoryCellsResult;

			/*
			 * Get
			 */

			MemoryCellSet memoryCells = getMemoryCellFactory().newSet();
			for (Literal l : getLiterals())
				memoryCells.addAll(l.getPreMemoryCells());

			/*
			 * Update cache and return
			 */

			if (cache != null)
				cache.getPreMemoryCellsResult = memoryCells;

			return memoryCells;
		}

		public PortSet getPrivatePorts() {

			/*
			 * Check cache
			 */

			if (cache != null && cache.getPrivatePortsResult != null)
				return cache.getPrivatePortsResult;

			/*
			 * Get
			 */

			PortSet ports = getPortFactory().newSet();
			for (Port p : getPortFactory().takeIntersection(getPorts(),
					getAutomaton().getPrivatePorts()))
				if (PortUtil.isPrivate(p))
					ports.add(p);

			/*
			 * Update cache and return
			 */

			if (cache != null)
				cache.getPrivatePortsResult = ports;

			return ports;
		}

		public PortSet getPublicPorts() {

			/*
			 * Check cache
			 */

			if (cache != null && cache.getPublicPortsResult != null)
				return cache.getPublicPortsResult;

			/*
			 * Get
			 */

			PortSet ports = getPortFactory().newSet();
			for (Port p : getPorts())
				if (PortUtil.isPublic(p))
					ports.add(p);

			/*
			 * Update cache and return
			 */

			if (cache != null)
				cache.getPublicPortsResult = ports;

			return ports;
		}

		public PortSet getQuantifiedPorts() {
			PortSet ports = getPortFactory().newSet();
			for (Port p : getPortFactory().takeComplement(getPorts(),
					getAutomaton().getAllPorts()))
				ports.add(p);

			return ports;
		}

		public Collection<Integer> getQuantifiedVariableIds() {

			/*
			 * Check cache
			 */

			if (cache != null && cache.getQuantifiedVariableIdsResult != null)
				return cache.getQuantifiedVariableIdsResult;

			/*
			 * Get
			 */

			Collection<Integer> quantifiedVariableIds = new HashSet<>();
			for (Literal l : getLiterals())
				quantifiedVariableIds.addAll(l.getQuantifiedVariableIds());

			quantifiedVariableIds = new ArrayList<>(quantifiedVariableIds);
			Collections.sort((List<Integer>) quantifiedVariableIds);

			/*
			 * Update cache and return
			 */

			if (cache != null)
				cache.getQuantifiedVariableIdsResult = quantifiedVariableIds;

			return quantifiedVariableIds;
		}

		@Override
		public String toString() {
			String string = "";
			for (Literal t : getLiterals().getSorted())
				string += "&&" + t.toString();

			return string.isEmpty() ? "true" : string.substring(2);
		}

		//
		// METHODS - PRIVATE
		//

		private TermSet getVariables() {
			TermSet variables = getTermFactory().newSet();
			for (Literal l : getLiterals())
				variables.addAll(l.getVariables());

			return variables;
		}

		//
		// CLASSES - PRIVATE
		//

		private class Cache {
			PortSet getPortsResult;
			MemoryCellSet getPostMemoryCellsResult;
			MemoryCellSet getPreMemoryCellsResult;
			PortSet getPrivatePortsResult;
			PortSet getPublicPortsResult;
			Collection<Integer> getQuantifiedVariableIdsResult;
		}
	}

	public class ConstraintSet
			extends
			IdObjectFactory<Constraint, ConstraintSet, ConstraintSpec>.IdObjectSet {
	}
}
