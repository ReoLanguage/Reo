package nl.cwi.reo.pr.autom;

import java.util.Collection;
import java.util.List;

import nl.cwi.reo.pr.autom.AutomatonFactory.Automaton;
import nl.cwi.reo.pr.autom.ConstraintFactory.Constraint;
import nl.cwi.reo.pr.autom.LiteralFactory.Literal;
import nl.cwi.reo.pr.autom.LiteralFactory.LiteralSet;
import nl.cwi.reo.pr.autom.LiteralSpec.EqualitySpec;
import nl.cwi.reo.pr.autom.LiteralSpec.RelationSpec;
import nl.cwi.reo.pr.autom.MemoryCellFactory.MemoryCell;
import nl.cwi.reo.pr.autom.StateFactory.State;
import nl.cwi.reo.pr.autom.TermFactory.Term;
import nl.cwi.reo.pr.autom.TermSpec.DatumSpec;
import nl.cwi.reo.pr.autom.TermSpec.FunctionSpec;
import nl.cwi.reo.pr.autom.TermSpec.PortVariableSpec;
import nl.cwi.reo.pr.autom.TermSpec.PostVariableSpec;
import nl.cwi.reo.pr.autom.TermSpec.PreVariableSpec;
import nl.cwi.reo.pr.misc.PortFactory;
import nl.cwi.reo.pr.misc.PortFactory.Port;
import nl.cwi.reo.pr.misc.PortFactory.PortSet;

public class UserDefinedAutomaton {
	private final Automaton automaton;
	private final Extralogical[] extralogicals;
	private final boolean ignoreData;
	private final Port[] inputPorts;
	private final Port[] outputPorts;

	//
	// CONSTRUCTORS
	//

	public UserDefinedAutomaton(AutomatonFactory automatonFactory,
			PortFactory portFactory, Collection<Port> inputPorts,
			Collection<Port> outputPorts,
			Collection<Extralogical> extralogicals, String description,
			boolean ignoreData) {

		if (automatonFactory == null)
			throw new NullPointerException();
		if (portFactory == null)
			throw new NullPointerException();
		if (inputPorts == null)
			throw new NullPointerException();
		if (outputPorts == null)
			throw new NullPointerException();
		if (extralogicals == null)
			throw new NullPointerException();
		if (description == null)
			throw new NullPointerException();
		if (inputPorts.contains(null))
			throw new NullPointerException();
		if (outputPorts.contains(null))
			throw new NullPointerException();
		if (extralogicals.contains(null))
			throw new NullPointerException();

		this.automaton = automatonFactory.newOrGet(new AutomatonSpec(
				portFactory.takeUnion(portFactory.newSet(inputPorts),
						portFactory.newSet(outputPorts)), portFactory
						.newSet(inputPorts), portFactory.newSet(outputPorts),
				description));

		this.extralogicals = new Extralogical[extralogicals.size()];
		this.ignoreData = ignoreData;
		this.inputPorts = new Port[inputPorts.size()];
		this.outputPorts = new Port[outputPorts.size()];

		extralogicals.toArray(this.extralogicals);
		inputPorts.toArray(this.inputPorts);
		outputPorts.toArray(this.outputPorts);
	}

	//
	// METHODS - PUBLIC
	//

	public MemoryCell addThenGetMemoryCell() {
		return automaton.addThenGetMemoryCell();
	}

	public MemoryCell addThenGetMemoryCell(Term term) {
		return automaton.addThenGetMemoryCell(term);
	}

	public State addThenGetState(boolean setInitialState) {
		return automaton.addThenGetState(setInitialState);
	}

	public void addOrKeepTransition(State source, State target,
			List<Port> ports, Constraint constraint) {

		if (source == null)
			throw new NullPointerException();
		if (target == null)
			throw new NullPointerException();
		if (ports == null)
			throw new NullPointerException();
		if (ports.contains(null))
			throw new NullPointerException();
		if (constraint == null)
			throw new NullPointerException();

		if (source.getFactory() != automaton.getStateFactory())
			throw new IllegalArgumentException();
		if (target.getFactory() != automaton.getStateFactory())
			throw new IllegalArgumentException();
		if (constraint.getFactory() != automaton.getConstraintFactory())
			throw new IllegalArgumentException();

		for (Port p : ports)
			if (p.getFactory() != automaton.getPortFactory())
				throw new IllegalStateException();

		PortSet set = automaton.getPortFactory().newSet();
		set.addAll(ports);
		automaton.addOrKeepTransition(source, target, set, constraint);
	}

	public int countExtralogicals() {
		return extralogicals.length;
	}

	public int countInputPorts() {
		return inputPorts.length;
	}

	public int countOutputPorts() {
		return outputPorts.length;
	}

	public Extralogical[] getExtralogicals() {
		return extralogicals;
	}

	public Port[] getInputPorts() {
		return inputPorts;
	}

	public Port[] getOutputPorts() {
		return outputPorts;
	}

	public Constraint newConstraint(List<Literal> literals) {
		if (literals == null)
			throw new NullPointerException();
		if (literals.contains(null))
			throw new NullPointerException();

		LiteralFactory literalFactory = automaton.getConstraintFactory()
				.getLiteralFactory();

		for (Literal l : literals)
			if (l.getFactory() != literalFactory)
				throw new IllegalStateException();

		LiteralSet set = literalFactory.newSet();
		if (!ignoreData)
			set.addAll(literals);

		return automaton.getConstraintFactory().newOrGet(
				new ConstraintSpec(automaton, set));
	}

	public Literal newLiteral(boolean isPositive, Extralogical relation,
			List<Term> arguments) {

		if (relation == null)
			throw new NullPointerException();
		if (arguments == null)
			throw new NullPointerException();
		if (arguments.contains(null))
			throw new NullPointerException();

		return automaton.getConstraintFactory().getLiteralFactory()
				.newOrGet(new RelationSpec(isPositive, relation, arguments));
	}

	public Literal newLiteral(boolean isPositive, Term argument1, Term argument2) {
		if (argument1 == null)
			throw new NullPointerException();
		if (argument2 == null)
			throw new NullPointerException();

		return automaton.getConstraintFactory().getLiteralFactory()
				.newOrGet(new EqualitySpec(isPositive, argument1, argument2));
	}

	public Term newTerm(Extralogical datum) {
		if (datum == null)
			throw new NullPointerException();

		return automaton.getConstraintFactory().getLiteralFactory()
				.getTermFactory().newOrGet(new DatumSpec(datum));
	}

	public Term newTerm(Extralogical function, List<Term> arguments) {
		if (function == null)
			throw new NullPointerException();
		if (arguments == null)
			throw new NullPointerException();
		if (arguments.contains(null))
			throw new NullPointerException();

		TermFactory termFactory = automaton.getConstraintFactory()
				.getLiteralFactory().getTermFactory();

		for (Term t : arguments)
			if (t.getFactory() != termFactory)
				throw new IllegalStateException();

		return termFactory.newOrGet(new FunctionSpec(function, arguments));
	}

	public Term newTerm(MemoryCell memoryCell, boolean isPost) {
		if (memoryCell == null)
			throw new NullPointerException();

		if (memoryCell.getFactory() != automaton.getMemoryCellFactory())
			throw new IllegalStateException();

		TermFactory termFactory = automaton.getConstraintFactory()
				.getLiteralFactory().getTermFactory();

		return isPost ? termFactory.newOrGet(new PostVariableSpec(memoryCell))
				: termFactory.newOrGet(new PreVariableSpec(memoryCell));
	}

	public Term newTerm(Port port) {
		if (port == null)
			throw new NullPointerException();

		if (port.getFactory() != automaton.getPortFactory())
			throw new IllegalStateException();

		return automaton.getConstraintFactory().getLiteralFactory()
				.getTermFactory().newOrGet(new PortVariableSpec(port));
	}

	//
	// METHODS - DEFAULT
	//

	public Automaton getAutomaton() {
		return automaton;
	}
}
