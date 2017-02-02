package nl.cwi.reo.pr.autom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;

import nl.cwi.reo.pr.autom.AutomatonFactory.Automaton;
import nl.cwi.reo.pr.autom.AutomatonFactory.AutomatonSet;
import nl.cwi.reo.pr.autom.ConstraintFactory.Constraint;
import nl.cwi.reo.pr.autom.ConstraintFactory.ConstraintSet;
import nl.cwi.reo.pr.autom.LiteralFactory.Literal;
import nl.cwi.reo.pr.autom.LiteralFactory.LiteralSet;
import nl.cwi.reo.pr.autom.MemoryCellFactory.MemoryCell;
import nl.cwi.reo.pr.autom.MemoryCellFactory.MemoryCellSet;
import nl.cwi.reo.pr.autom.StateFactory.State;
import nl.cwi.reo.pr.autom.StateFactory.StateSet;
import nl.cwi.reo.pr.autom.TermFactory.Term;
import nl.cwi.reo.pr.autom.TermFactory.TermSet;
import nl.cwi.reo.pr.autom.TransitionFactory.Transition;
import nl.cwi.reo.pr.autom.TransitionFactory.TransitionSet;
import nl.cwi.reo.pr.misc.IdObjectFactory;
import nl.cwi.reo.pr.misc.PortFactory;
import nl.cwi.reo.pr.misc.PortFactory.Port;
import nl.cwi.reo.pr.misc.PortFactory.PortSet;
import nl.cwi.reo.pr.comp.Cancellation;
import nl.cwi.reo.pr.comp.CompilerProgressMonitor;

public abstract class AutomatonFactory extends
		IdObjectFactory<Automaton, AutomatonSet, AutomatonSpec> {

	private final ConstraintFactory constraintFactory;
	private final LiteralFactory literalFactory;
	private final MemoryCellFactory memoryCellFactory;
	private final PortFactory portFactory;
	private final TermFactory termFactory;
	private final int nStatesMax = 2048;
	private final int nTransitionsMax = (int) Math.pow(2, 11 /* 12 */);

	//
	// CONSTRUCTORS
	//

	public AutomatonFactory() {
		this.memoryCellFactory = newMemoryCellFactory();
		this.portFactory = newPortFactory();

		this.termFactory = newTermFactory(this.portFactory,
				this.memoryCellFactory);

		this.literalFactory = newLiteralFactory(termFactory);
		this.constraintFactory = newConstraintFactory(literalFactory);
	}

	//
	// METHODS - PUBLIC
	//

	@Override
	public void dispose(Automaton object) {
		super.dispose(object);
		PortUtil.removeAutomaton(object.getInputPorts(), object);
		PortUtil.removeAutomaton(object.getOutputPorts(), object);
	}

	public PortFactory getPortFactory() {
		return portFactory;
	}

	public Automaton multiply(Automaton automaton1, Automaton automaton2) {

		if (automaton1 == null)
			throw new NullPointerException();
		if (automaton2 == null)
			throw new NullPointerException();

		if (automaton1.getFactory() != this)
			throw new IllegalStateException();
		if (automaton2.getFactory() != this)
			throw new IllegalStateException();

		if (!automaton1.memoryCells.containsNone(automaton2.memoryCells))
			throw new IllegalArgumentException();

		/*
		 * Get automaton
		 */

		PortSet allPorts1 = automaton1.getAllPorts();
		PortSet allPorts2 = automaton2.getAllPorts();
		PortSet inputPorts1 = automaton1.getInputPorts();
		PortSet inputPorts2 = automaton2.getInputPorts();
		PortSet outputPorts1 = automaton1.getOutputPorts();
		PortSet outputPorts2 = automaton2.getOutputPorts();

		PortSet intersection = portFactory.takeIntersection(allPorts1,
				allPorts2);
		PortSet union = portFactory.takeUnion(
				portFactory.takeIntersection(inputPorts1, outputPorts2),
				portFactory.takeIntersection(inputPorts2, outputPorts1));

		if (!intersection.equals(union))
			throw new IllegalArgumentException();

		PortSet allPorts = portFactory.takeUnion(allPorts1, allPorts2);

		PortSet inputPorts = portFactory.takeUnion(inputPorts1, inputPorts2);
		inputPorts.removeAll(outputPorts1);
		inputPorts.removeAll(outputPorts2);

		PortSet outputPorts = portFactory.takeUnion(outputPorts1, outputPorts2);
		outputPorts.removeAll(inputPorts1);
		outputPorts.removeAll(inputPorts2);

		String description = "(x)";

		AutomatonSpec spec = new AutomatonSpec(allPorts, inputPorts,
				outputPorts, description);

		Automaton automaton = newOrGet(spec);

		PortUtil.removeAutomaton(automaton1.getAllPorts(), automaton1);
		PortUtil.removeAutomaton(automaton2.getAllPorts(), automaton2);



		/*
		 * Add memory cells
		 */

		automaton.memoryCells.addAll(automaton1.memoryCells);
		automaton.memoryCells.addAll(automaton2.memoryCells);



		/*
		 * Add states
		 */

		StateSet states1 = automaton1.getStates();
		StateSet states2 = automaton2.getStates();

		Map<State, Map<State, State>> states = new HashMap<State, Map<State, State>>();
		for (State s1 : states1) {
			states.put(s1, new HashMap<State, State>());
			for (State s2 : states2) {
				boolean isInitialState = s1.isInitial() && s2.isInitial();
				State state = automaton.addThenGetState(isInitialState);
				states.get(s1).put(s2, state);


			}
		}



		/*
		 * Add synchronizing transitions
		 */

		TransitionSet transitions1 = automaton1.getTransitions();
		TransitionSet transitions2 = automaton2.getTransitions();

		for (Transition t1 : transitions1.getSorted()) {
			PortSet ports1 = t1.getPorts();
			for (Transition t2 : transitions2) {
				PortSet ports2 = t2.getPorts();
				PortSet intersection1 = portFactory.takeIntersection(allPorts1,
						ports2);
				PortSet intersection2 = portFactory.takeIntersection(allPorts2,
						ports1);

				if (intersection1.equals(intersection2)) {

					State source, target;
					source = states.get(t1.getSource()).get(t2.getSource());
					target = states.get(t1.getTarget()).get(t2.getTarget());
					PortSet ports = portFactory.takeUnion(ports1, ports2);
					Constraint constraint = constraintFactory.compose(
							automaton, t1.getConstraint(), t2.getConstraint());

					automaton.addOrKeepTransition(source, target, ports,
							constraint);
				}


			}
		}


		/*
		 * Add independent transitions
		 */

		Constraint keepConstraint1 = constraintFactory.newOrGetKeepConstraint(
				automaton, automaton1.getMemoryCells());
		Constraint keepConstraint2 = constraintFactory.newOrGetKeepConstraint(
				automaton, automaton2.getMemoryCells());

		for (Transition t1 : transitions1) {
			PortSet ports1 = t1.getPorts();
			if (allPorts2.containsNone(ports1))
				for (State s2 : states2) {
					State source, target;
					source = states.get(t1.getSource()).get(s2);
					target = states.get(t1.getTarget()).get(s2);

					automaton.addOrKeepTransition(
							source,
							target,
							ports1,
							constraintFactory.compose(automaton,
									t1.getConstraint(), keepConstraint2));

				}
		}



		for (Transition t2 : transitions2) {
			PortSet ports2 = t2.getPorts();
			if (allPorts1.containsNone(ports2))
				for (State s1 : states1) {
					State source, target;
					source = states.get(s1).get(t2.getSource());
					target = states.get(s1).get(t2.getTarget());

					automaton.addOrKeepTransition(
							source,
							target,
							ports2,
							constraintFactory.compose(automaton,
									t2.getConstraint(), keepConstraint1));


				}
		}


		return automaton;
	}

	public Automaton multiplyAll(AutomatonSet automata,
			boolean subtractSyntactically) {

		if (automata == null)
			throw new NullPointerException();

		if (automata.isEmpty())
			throw new IllegalArgumentException();


		// System.err.println("#primitives=" + automata.count());

		/*
		 * Sort automata
		 */


		List<List<Automaton>> automataSortedByFrontier = automata
				.sortByFrontier();

		Automaton[][] array = new Automaton[automataSortedByFrontier.size()][];
		for (int i = 0; i < automataSortedByFrontier.size(); i++) {
			array[i] = automataSortedByFrontier.get(i)
					.toArray(new Automaton[0]);
			automataSortedByFrontier.set(i, null);
		}

		automataSortedByFrontier = null;



		/*
		 * Multiply automata
		 */

		automata = new AutomatonSet();
		for (Automaton[] a : array) {
			Automaton automaton = null;
			PortSet internalPorts = portFactory.newSet();

			for (int i = 0; i < a.length; i++) {


				if (automaton == null)
					automaton = a[i];
				else {
					// System.err.println(i + "--" + a[i].getDescription());

					automaton = multiply(automaton, a[i]);

					internalPorts.addAll(automaton.getInternalPorts());
					automaton.subtract(automaton.getInternalPorts(), false);
					automaton.chainSilentTransitions();
					automaton.removeUnreachableStates();

					// System.err.println("#states=" + automaton.countStates());
					// System.err.println("#transitions="
					// + automaton.countTransitions());

					dispose(a[i]);
					a[i] = null;
				}

			}

			if (subtractSyntactically)
				automaton.subtract(internalPorts, true);

			automata.add(automaton);
		}

		Automaton automaton = null;
		PortSet internalPorts = portFactory.newSet();
		int i = 1;
		for (Automaton aut : automata) {


			if (automaton == null)
				automaton = aut;
			else {
				automaton = multiply(automaton, aut);

				// internalPorts.addAll(automaton.getInternalPorts());
				// automaton.subtract(automaton.getInternalPorts(), false);
				// automaton.chainSilentTransitions(monitor);
				// automaton.removeUnreachableStates();

				dispose(aut);
			}
		}

		if (subtractSyntactically)
			automaton.subtract(internalPorts, true);

		// System.err.println(automaton);
		// System.err.println("#states=" + automaton.countStates());
		// System.err.println("#transitions=" + automaton.countTransitions());



		if (automaton.countTransitions() > nTransitionsMax) {
			dispose(automaton);
			throw new Cancellation(
					"Too many transitions in a product automaton ("
							+ automaton.countTransitions() + ", while max="
							+ nTransitionsMax + ")");
		}

		if (automaton.countStates() > nStatesMax) {
			dispose(automaton);
			throw new Cancellation("Too many states in a product automaton ("
					+ automaton.countStates() + ", while max=" + nStatesMax
					+ ")");
		}

		return automaton;
	}

	@Override
	public Automaton newOrGet(AutomatonSpec spec) {
		Automaton automaton = super.newOrGet(spec);
		PortUtil.addAutomaton(automaton.getInputPorts(), automaton);
		PortUtil.addAutomaton(automaton.getOutputPorts(), automaton);
		return automaton;
	}

	@Override
	public AutomatonSet newSet() {
		return new AutomatonSet();
	}

	//
	// METHODS - PROTECTED
	//

	protected abstract ConstraintFactory newConstraintFactory(
			LiteralFactory literalFactory);

	protected abstract LiteralFactory newLiteralFactory(TermFactory termFactory);

	protected abstract MemoryCellFactory newMemoryCellFactory();

	protected abstract PortFactory newPortFactory();

	protected abstract TermFactory newTermFactory(PortFactory portFactory,
			MemoryCellFactory memoryCellFactory);

	//
	// CLASSES - PUBLIC
	//

	public abstract class Automaton extends
			IdObjectFactory<Automaton, AutomatonSet, AutomatonSpec>.IdObject {

		private final MemoryCellSet memoryCells;
		private final StateFactory stateFactory;
		private final StateSet states;
		private final TransitionFactory transitionFactory;
		private final TransitionSet transitions;

		private Cache cache;
		private Map<Port, Integer> portToGroupId;
		private Map<Integer, PortSet> portsPerGroupId;
		private Set<Integer> publicGroupIds;
		private Set<Integer> privateGroupIds;
		private State initialState;

		//
		// CONSTRUCTORS
		//

		public Automaton(int id, AutomatonSpec spec) {
			super(id, spec);

			spec = getSpec();
			if (spec.getAllPorts().getFactory() != portFactory)
				throw new IllegalStateException();
			if (spec.getInputPorts().getFactory() != portFactory)
				throw new IllegalStateException();
			if (spec.getOutputPorts().getFactory() != portFactory)
				throw new IllegalStateException();

			this.memoryCells = memoryCellFactory.newSet();
			this.stateFactory = newStateFactory();
			this.transitionFactory = newTransitionFactory();

			this.states = stateFactory.newSet();
			this.transitions = transitionFactory.newSet();
		}

		//
		// METHODS - PUBLIC
		//

		public MemoryCell addThenGetMemoryCell() {
			MemoryCell memoryCell = memoryCellFactory
					.newOrGet(new MemoryCellSpec());

			memoryCells.add(memoryCell);
			return memoryCell;
		}

		public MemoryCell addThenGetMemoryCell(Term term) {
			if (term == null)
				throw new NullPointerException();

			MemoryCell memoryCell = memoryCellFactory
					.newOrGet(new MemoryCellSpec(term));

			memoryCells.add(memoryCell);
			return memoryCell;
		}

		public State addThenGetState(boolean setInitialState) {
			State state = stateFactory.newOrGet(new StateSpec(this,
					setInitialState, transitionFactory.newSet()));

			states.add(state);
			if (setInitialState)
				initialState = state;

			return state;
		}

		public void addOrKeepTransition(State source, State target,
				PortSet ports, Constraint constraint) {

			if (source == null)
				throw new NullPointerException();
			if (target == null)
				throw new NullPointerException();
			if (ports == null)
				throw new NullPointerException();
			if (constraint == null)
				throw new NullPointerException();

			if (source.getFactory() != stateFactory)
				throw new IllegalArgumentException();
			if (target.getFactory() != stateFactory)
				throw new IllegalArgumentException();
			if (ports.getFactory() != portFactory)
				throw new IllegalArgumentException();
			if (constraint.getFactory() != constraintFactory)
				throw new IllegalArgumentException();

			Transition transition = transitionFactory
					.newOrGet(new TransitionSpec(this, source, target, ports,
							constraint));

			transitions.add(transition);
		}

		public void addOrKeepTransition(State source, State target,
				PortSet ports, Constraint constraint,
				Map<Integer, Port> representatives) {

			if (source == null)
				throw new NullPointerException();
			if (target == null)
				throw new NullPointerException();
			if (ports == null)
				throw new NullPointerException();
			if (constraint == null)
				throw new NullPointerException();
			if (representatives == null)
				throw new NullPointerException();
			if (representatives.containsKey(null))
				throw new NullPointerException();
			if (representatives.containsValue(null))
				throw new NullPointerException();

			if (source.getFactory() != stateFactory)
				throw new IllegalArgumentException();
			if (target.getFactory() != stateFactory)
				throw new IllegalArgumentException();
			if (ports.getFactory() != portFactory)
				throw new IllegalArgumentException();
			if (constraint.getFactory() != constraintFactory)
				throw new IllegalArgumentException();
			if (representatives.isEmpty())
				throw new IllegalArgumentException();

			Transition transition = transitionFactory
					.newOrGet(new TransitionSpec(this, source, target, ports,
							constraint, representatives));

			transitions.add(transition);
		}

		public void chainSilentTransitions() {
			while (true) {


				/*
				 * Find a silent nonloop transition
				 */

				Transition silentTransition = null;
				for (Transition tr : transitions.getSilentSubset())
					if (!tr.isLoop()) {
						silentTransition = tr;
						break;
					}

				if (silentTransition == null)
					break;

				/*
				 * Chain silentTransition with its successor transitions
				 */

				for (Transition tr : silentTransition.getTarget()
						.getTransitions()) {




					State source = silentTransition.getSource();
					State target = tr.getTarget();
					PortSet ports = tr.getPorts();
					Constraint constraint = constraintFactory.chain(this,
							silentTransition.getConstraint(),
							tr.getConstraint());

					addOrKeepTransition(source, target, ports, constraint);
				}

				/*
				 * Remove silentTransition
				 */

				removeTransition(silentTransition);
			}
		}

		public void commandify(TermSet allImportVariables,
				TermSet allExportVariables) {

			if (allImportVariables == null)
				throw new NullPointerException();
			if (allExportVariables == null)
				throw new NullPointerException();

			for (Transition tr : getTransitions())
				if (tr.hasPredictableNeighbors()) {
					TermSet allImportVariablesPlus = termFactory
							.newSet(allImportVariables);
					TermSet allExportVariablesPlus = termFactory
							.newSet(allExportVariables);

					// for (Port p : getPrivateInputPorts())
					// allImportVariablesPlus.add(termFactory
					// .newOrGet(new TermSpec.PortVariableSpec(p)));
					//
					// for (Port p : getPrivateOutputPorts())
					// allExportVariablesPlus.add(termFactory
					// .newOrGet(new TermSpec.PortVariableSpec(p)));

					for (Port p : tr.getRepresentatives().values()) {

						if (getInputPorts().contains(p))
							allImportVariablesPlus
									.add(termFactory
											.newOrGet(new TermSpec.PortVariableSpec(
													p)));

						else if (getOutputPorts().contains(p))
							allExportVariablesPlus
									.add(termFactory
											.newOrGet(new TermSpec.PortVariableSpec(
													p)));
					}

					tr.getConstraintPlus().commandify(allImportVariablesPlus,
							allExportVariablesPlus);
				}

				else {
					tr.getConstraint().commandify(allImportVariables,
							allExportVariables);
				}
		}

		public int countStates() {
			return states.count();
		}

		public int countTransitions() {
			return transitions.count();
		}

		public void disableCache() {
			cache = null;
		}

		public void enableCache() {
			cache = new Cache();
		}

		public PortSet getAllPorts() {
			return getSpec().getAllPorts();
		}

		public ConstraintSet getConstraints() {

			/*
			 * Check cache
			 */

			if (cache != null && cache.getConstraintsResult != null)
				return cache.getConstraintsResult;

			/*
			 * Get
			 */

			ConstraintSet constraints = constraintFactory.newSet();
			for (Transition t : transitions) {
				constraints.add(t.getConstraint());
				if (t.hasPredictableNeighbors())
					constraints.add(t.getConstraintPlus());
			}

			/*
			 * Update cache and return
			 */

			if (cache != null)
				cache.getConstraintsResult = constraints;

			return constraints;
		}

		public String getDescription() {
			return getSpec().getDescription();
		}

		public Map<Port, Integer> getIndexPerPublicPort() {

			/*
			 * Check cache
			 */

			if (cache != null && cache.getIndexPerPublicPortResult != null)
				return cache.getIndexPerPublicPortResult;

			/*
			 * Get
			 */

			Map<Port, Integer> indices = new HashMap<>();
			int i = 0;

			for (Port p : getInputPorts().getSorted())
				if (PortUtil.isPublic(p)
						&& (portToGroupId == null || !portToGroupId
								.containsKey(p)))

					indices.put(p, i++);

			for (Port p : getOutputPorts().getSorted())
				if (PortUtil.isPublic(p)
						&& (portToGroupId == null || !portToGroupId
								.containsKey(p)))

					indices.put(p, i++);

			/*
			 * Update cache and return
			 */

			if (cache != null)
				cache.getIndexPerPublicPortResult = indices;

			return indices;
		}

		public State getInitialState() {
			return initialState;
		}

		public PortSet getInputPorts() {
			return getSpec().getInputPorts();
		}

		public PortSet getInternalPorts() {
			PortSet ports = portFactory.newSet();
			ports.addAll(getAllPorts());
			ports.removeAll(getInputPorts());
			ports.removeAll(getOutputPorts());

			return ports;
		}

		public LiteralSet getLiterals() {

			/*
			 * Check cache
			 */

			if (cache != null && cache.getLiteralsResult != null)
				return cache.getLiteralsResult;

			/*
			 * Get
			 */

			LiteralSet literals = constraintFactory.getLiteralFactory()
					.newSet();

			for (Transition t : transitions)
				literals.addAll(t.getConstraint().getLiterals());

			/*
			 * Update cache and return
			 */

			if (cache != null)
				cache.getLiteralsResult = literals;

			return literals;
		}

		public MemoryCellSet getMemoryCells() {
			return memoryCells;
		}

		public AutomatonSet getNeighbors() {

			/*
			 * Check cache
			 */

			if (cache != null && cache.getNeighborsResult != null)
				return cache.getNeighborsResult;

			/*
			 * Get
			 */

			AutomatonSet neighbors = newSet();
			for (Port p : getAllPorts())
				neighbors.addAll(PortUtil.getAutomata(p));

			neighbors.remove(this);

			/*
			 * Update cache and return
			 */

			if (cache != null)
				cache.getNeighborsResult = neighbors;

			return neighbors;
		}

		public Map<Port, AutomatonSet> getNeighborsPerInputPort() {
			Map<Port, AutomatonSet> neighborsPerInputPort = new LinkedHashMap<>();
			for (Port p : getInputPorts().getSorted())
				neighborsPerInputPort.put(p,
						takeComplement(PortUtil.getAutomata(p), this));

			return neighborsPerInputPort;
		}

		public Map<Port, AutomatonSet> getNeighborsPerOutputPort() {
			Map<Port, AutomatonSet> neighborsPerOutputPort = new LinkedHashMap<>();
			for (Port p : getOutputPorts().getSorted())
				neighborsPerOutputPort.put(p,
						takeComplement(PortUtil.getAutomata(p), this));

			return neighborsPerOutputPort;
		}

		public Map<Port, AutomatonSet> getNeighborsPerPort() {
			Map<Port, AutomatonSet> neighborsPerPort = new LinkedHashMap<>();
			for (Port p : getAllPorts().getSorted())
				neighborsPerPort.put(p,
						takeComplement(PortUtil.getAutomata(p), this));

			return neighborsPerPort;
		}

		public PortSet getOutputPorts() {
			return getSpec().getOutputPorts();
		}

		public Map<Automaton, PortSet> getPortsPerNeighbor() {

			/*
			 * Check cache
			 */

			if (cache != null && cache.getPortsPerNeighborResult != null)
				return cache.getPortsPerNeighborResult;

			/*
			 * Get
			 */

			Map<Automaton, PortSet> portsPerNeighbor = new TreeMap<>();

			PortSet ports = portFactory.newSet();
			ports.addAll(getInputPorts());
			ports.addAll(getOutputPorts());

			for (Automaton aut : getNeighbors()) {
				PortSet intersection = portFactory.takeIntersection(ports,
						aut.getAllPorts());

				portsPerNeighbor.put(aut, intersection);
				ports.removeAll(intersection);
			}

			/*
			 * Update cache and return
			 */

			if (cache != null)
				cache.getPortsPerNeighborResult = portsPerNeighbor;

			return portsPerNeighbor;
		}

		public Map<Integer, PortSet> getPortsPerGroupId() {
			return portsPerGroupId;
		}

		public Map<Port, Integer> getPortToGroupId() {
			return portToGroupId;
		}

		// public Map<Port, Integer> getInputPortToGroupId() {
		// Map<Port, Integer> inputPortToGroupId = new LinkedHashMap<>();
		//
		// Map<Port, Integer> portToGroupId = getPortToGroupId();
		// for (Port p : portToGroupId.keySet())
		// if (getInputPorts().contains(p))
		// inputPortToGroupId.put(p, portToGroupId.get(p));
		//
		// return inputPortToGroupId;
		// }
		//
		// public Map<Port, Integer> getOutputPortToGroupId() {
		// Map<Port, Integer> outputPortToGroupId = new LinkedHashMap<>();
		//
		// Map<Port, Integer> portToGroupId = getPortToGroupId();
		// for (Port p : portToGroupId.keySet())
		// if (getOutputPorts().contains(p))
		// outputPortToGroupId.put(p, portToGroupId.get(p));
		//
		// return outputPortToGroupId;
		// }

		public PortSet getPrivateInputPorts() {

			/*
			 * Check cache
			 */

			if (cache != null && cache.getPrivateInputPortsResult != null)
				return cache.getPrivateInputPortsResult;

			/*
			 * Get
			 */

			PortSet ports = portFactory.newSet();
			for (Port p : getInputPorts())
				if (PortUtil.isPrivate(p))
					ports.add(p);

			/*
			 * Update cache and return
			 */

			if (cache != null)
				cache.getPrivateInputPortsResult = ports;

			return ports;
		}

		public PortSet getPrivateOutputPorts() {

			/*
			 * Check cache
			 */

			if (cache != null && cache.getPrivateOutputPortsResult != null)
				return cache.getPrivateOutputPortsResult;

			/*
			 * Get
			 */

			PortSet ports = portFactory.newSet();
			for (Port p : getOutputPorts())
				if (PortUtil.isPrivate(p))
					ports.add(p);

			/*
			 * Update cache and return
			 */

			if (cache != null)
				cache.getPrivateOutputPortsResult = ports;

			return ports;
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

			PortSet ports = portFactory.newSet();
			ports.addAll(getPrivateInputPorts());
			ports.addAll(getPrivateOutputPorts());

			/*
			 * Update cache and return
			 */

			if (cache != null)
				cache.getPrivatePortsResult = ports;

			return ports;
		}

		public Set<Integer> getPrivateGroupIds() {
			return privateGroupIds;
		}

		public Map<String, Boolean> getProperties() {

			/*
			 * Check cache
			 */

			if (cache != null && cache.getPropertiesResult != null)
				return cache.getPropertiesResult;

			/*
			 * Get
			 */

			Map<String, Boolean> properties = new HashMap<>();

			properties.put("MASTER", isMaster());
			properties.put("SLAVE", isSlave());

			/*
			 * Update cache and return
			 */

			if (cache != null)
				cache.getPropertiesResult = properties;

			return properties;
		}

		public PortSet getPublicInputPorts() {

			/*
			 * Check cache
			 */

			if (cache != null && cache.getPublicInputPortsResult != null)
				return cache.getPublicInputPortsResult;

			/*
			 * Get
			 */

			PortSet ports = portFactory.newSet();
			for (Port p : getInputPorts())
				if (PortUtil.isPublic(p))
					ports.add(p);

			/*
			 * Update cache and return
			 */

			if (cache != null)
				cache.getPublicInputPortsResult = ports;

			return ports;
		}

		public PortSet getPublicOutputPorts() {

			/*
			 * Check cache
			 */

			if (cache != null && cache.getPublicOutputPortsResult != null)
				return cache.getPublicOutputPortsResult;

			/*
			 * Get
			 */

			PortSet ports = portFactory.newSet();
			for (Port p : getOutputPorts())
				if (PortUtil.isPublic(p))
					ports.add(p);

			/*
			 * Update cache and return
			 */

			if (cache != null)
				cache.getPublicOutputPortsResult = ports;

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

			PortSet ports = portFactory.newSet();
			ports.addAll(getPublicInputPorts());
			ports.addAll(getPublicOutputPorts());

			/*
			 * Update cache and return
			 */

			if (cache != null)
				cache.getPublicPortsResult = ports;

			return ports;
		}

		public Set<Integer> getPublicGroupIds() {
			return publicGroupIds;
		}

		public StateSet getStates() {
			return states;
		}

		public TransitionSet getTransitions() {
			return transitions;
		}

		public boolean hasPredictableNeighbors() {
			for (Transition tr : transitions)
				if (!tr.hasPredictableNeighbors())
					return false;

			return true;
		}

		public void inferQueues() {

			// TODO: Currently, queues are inferred for the whole automaton.
			// At some point, this may be extended to inference on a per-state
			// basis.

			/*
			 * Build graph and combine its edges
			 */

			Set<SynchrEdge> edges = new HashSet<>();
			for (Transition tr : getTransitions())
				edges.add(new SynchrEdge(portFactory, tr.getPorts()));

			SynchrGraph graph = new SynchrGraph(portFactory, edges);
			graph.combineEdges();

			/*
			 * Process subsets of ports (each of which represents a queue) into
			 * groups
			 */

			Map<Integer, PortSet> portsPerGroupId = new LinkedHashMap<>();
			Set<Integer> privateGroupIds = new LinkedHashSet<Integer>();
			Set<Integer> publicGroupIds = new LinkedHashSet<Integer>();
			Map<Port, Integer> portToGroupId = new HashMap<>();

			Map<Integer, Port> representatives = new LinkedHashMap<>();

			int nextGroupId = 1;

			for (SynchrEdge edg : graph)
				for (SynchrVertex v : edg) {
					if (v.getSize() == 1)
						continue;

					int groupId = nextGroupId++;
					PortSet ports = portFactory.newSet();
					portsPerGroupId.put(groupId, ports);

					for (Port p : v) {
						portToGroupId.put(p, groupId);
						ports.add(p);

						if (!representatives.containsKey(groupId))
							representatives.put(groupId, p);
					}

					// TODO: Instead of checking for visibility/direction, as
					// currently done below, it should also be possible to split
					// groups with mixed visibility/direction into subgroups
					// with consistent visibility/direction.

					/*
					 * Check if all ports in this group have the same visibility
					 */

					if (getPublicPorts().containsAll(ports))
						publicGroupIds.add(groupId);
					else if (getPrivatePorts().containsAll(ports))
						privateGroupIds.add(groupId);
					else
						return;

					/*
					 * Check if all ports in this group have the same direction
					 */

					if (!getInputPorts().containsAll(ports)
							&& !getOutputPorts().containsAll(ports))

						return;
				}

			if (nextGroupId == 1)
				return;

			/*
			 * Check if data constraints are indistinguishable
			 */

			// TODO: This can be further improved. In the current
			// implementation, transitions AB and AC are not merged into A(B*C)
			// if there is another transition XY, even though XY is independent.
			// The improvement may consist of a map from [groupIds, ports] pairs
			// to sets of transitions with indistinguishable data constraints.

			Set<Integer> groupIds = null;
			PortSet ports = null;

			for (Transition tr : getTransitions()) {
				List<PortSet> sets = new ArrayList<>();

				for (Literal l : tr.getConstraint().getLiterals())
					if (l.isEquality())
						sets.add(l.getPorts());
					else
						return;

				if (sets.isEmpty())
					return;

				PortSet equalityPorts = portFactory.newSet();
				int remainingSets = sets.size();
				equalityPorts.addAll(sets.remove(0));
				while (sets.size() < remainingSets) {
					remainingSets = sets.size();
					Iterator<PortSet> iterator = sets.iterator();
					while (iterator.hasNext()) {
						PortSet set = iterator.next();
						if (!equalityPorts.containsNone(set)) {
							equalityPorts.addAll(set);
							iterator.remove();
						}
					}
				}

				if (!sets.isEmpty())
					return;

				/*
				 * Check indistinguishability
				 */

				equalityPorts = portFactory.takeIntersection(equalityPorts,
						getAllPorts());

				if (!tr.getPorts().containsAll(equalityPorts))
					return;

				Set<Integer> trGroupIds = new HashSet<>();
				PortSet trPorts = portFactory.newSet();

				for (Port p : equalityPorts)
					if (portToGroupId.containsKey(p))
						trGroupIds.add(portToGroupId.get(p));
					else
						trPorts.add(p);

				if (groupIds == null) {
					groupIds = trGroupIds;
					ports = trPorts;
				}

				if (!groupIds.equals(trGroupIds) || !ports.equals(trPorts))
					return;
			}

			/*
			 * Set fields
			 */

			this.portsPerGroupId = portsPerGroupId;
			this.portToGroupId = portToGroupId;
			this.privateGroupIds = privateGroupIds;
			this.publicGroupIds = publicGroupIds;

			/*
			 * Update transitions
			 */

			TermSet privatePortVariables = termFactory.newSet();
			for (Port p : getPrivatePorts())
				privatePortVariables.add(termFactory
						.newOrGet(new TermSpec.PortVariableSpec(p)));

			// for (State st : states) {
			// Set<TermEdge> E = new HashSet<>();
			// for (Transition tr : st.getTransitions()) {
			// TermSet terms = termFactory.newSet();
			//
			// Constraint constraint = constraintFactory
			// .quantifySyntactically(tr.getConstraintPlus(),
			// privatePortVariables);
			//
			// for (Literal l : constraint.getLiterals())
			// terms.addAll(l.getVariables());
			//
			// E.add(new TermEdge(termFactory, terms));
			// }
			//
			// TermGraph G = new TermGraph(termFactory, E);
			// G.combineEdges();
			// }

			for (State st : states)
				for (Transition tr : st.getTransitions()) {

					PortSet trPorts = portFactory.newSet();
					List<Integer> trGroupIds = new ArrayList<>();

					for (Port p : tr.getPorts())
						if (portToGroupId.containsKey(p))
							trGroupIds.add(portToGroupId.get(p));
						else
							trPorts.add(p);

					/*
					 * Compute substitutions
					 */

					Map<Term, Term> substitutions = new HashMap<>();

					for (Port p : tr.getPorts())
						if (portToGroupId.containsKey(p))
							substitutions
									.put(termFactory
											.newOrGet(new TermSpec.PortVariableSpec(
													p)),
											termFactory
													.newOrGet(new TermSpec.PortVariableSpec(
															representatives
																	.get(portToGroupId
																			.get(p)))));

					/*
					 * Add transition
					 */

					addOrKeepTransition(
							tr.getSource(),
							tr.getTarget(),
							trPorts,
							constraintFactory.substitute(this,
									tr.getConstraint(), substitutions),
							representatives);

					/*
					 * Remove transition
					 */

					removeTransition(tr);
				}
		}

		public boolean isIndependentOf(Automaton automaton) {
			return getAllPorts().containsNone(automaton.getAllPorts());
		}

		public boolean isIndependentOf(AutomatonSet automata) {
			for (Automaton aut : automata)
				if (!isIndependentOf(aut))
					return false;

			return true;
		}

		public boolean isMaster() {
			return isSynchronizing();
		}

		public boolean isSlave() {
			return !isSynchronizing();
		}

		public boolean isSynchronizing() {
			for (Transition tr : transitions)
				if (tr.isSynchronizing())
					return true;

			return false;
		}

		public void removeTransition(Transition transition) {
			if (transition == null)
				throw new NullPointerException();
			if (transition.getFactory() != transitionFactory)
				throw new IllegalArgumentException();

			transition.getSource().removeTransition(transition);
			transitions.remove(transition);
			transitionFactory.dispose(transition);
		}

		public void removeUnreachableStates() {
			final Stack<State> stack = new Stack<State>();
			StateSet reachableStates = stateFactory.newSet();

			State initialState = getInitialState();
			stack.push(initialState);
			reachableStates.add(initialState);

			while (!stack.isEmpty())
				for (final Transition t : stack.pop().getTransitions()) {
					State target = t.getTarget();
					if (!reachableStates.contains(target)) {
						reachableStates.add(target);
						stack.push(target);
					}
				}

			StateSet unreachableStates = stateFactory.newSet();
			unreachableStates.addAll(states);
			unreachableStates.removeAll(reachableStates);

			for (State st : unreachableStates) {
				for (Transition tr : st.getTransitions())
					removeTransition(tr);

				states.remove(st);
				stateFactory.dispose(st);
			}
		}

		public void subtract(PortSet ports, boolean subtractSyntactically) {
			if (ports == null)
				throw new NullPointerException();

			if (ports.isEmpty())
				return;

			// TODO: Better not change inner spec
			getAllPorts().removeAll(ports);

			TermSet portVariables = termFactory.newSet();
			if (subtractSyntactically)
				for (Port p : ports)
					portVariables.add(termFactory
							.newOrGet(new TermSpec.PortVariableSpec(p)));

			for (Transition tr : transitions)
				if (subtractSyntactically) {
					Constraint constraint = constraintFactory
							.quantifySyntactically(this, tr.getConstraint(),
									portVariables);

					if (tr.getConstraint().equals(constraint))
						continue;

					addOrKeepTransition(tr.getSource(), tr.getTarget(),
							portFactory.takeComplement(tr.getPorts(), ports),
							constraint);

					removeTransition(tr);
				}

				else
					tr.removePorts(ports);
		}

		@Override
		public String toString() {
			String string = getTransitions().getSorted().toString();
			return getDescription() + " :: " + getInitialState() + ", "
					+ string.substring(1, string.length() - 1);
		}

		//
		// METHODS - PROTECTED
		//

		protected ConstraintFactory getConstraintFactory() {
			return constraintFactory;
		}

		protected MemoryCellFactory getMemoryCellFactory() {
			return memoryCellFactory;
		}

		protected PortFactory getPortFactory() {
			return portFactory;
		}

		protected StateFactory getStateFactory() {
			return stateFactory;
		}

		protected TransitionFactory getTransitionFactory() {
			return transitionFactory;
		}

		protected abstract StateFactory newStateFactory();

		protected abstract TransitionFactory newTransitionFactory();

		//
		// CLASSES - PRIVATE
		//

		private class Cache {
			ConstraintSet getConstraintsResult;
			Map<Port, Integer> getIndexPerPublicPortResult;
			LiteralSet getLiteralsResult;
			AutomatonSet getNeighborsResult;
			Map<Automaton, PortSet> getPortsPerNeighborResult;
			PortSet getPrivateInputPortsResult;
			PortSet getPrivateOutputPortsResult;
			PortSet getPrivatePortsResult;
			PortSet getPublicInputPortsResult;
			PortSet getPublicOutputPortsResult;
			PortSet getPublicPortsResult;
			Map<String, Boolean> getPropertiesResult;
		}
	}

	public class AutomatonSet extends
			IdObjectFactory<Automaton, AutomatonSet, AutomatonSpec>.IdObjectSet {

		//
		// METHODS - PUBLIC
		//

		public void commandifyMasters() {
			TermSet allImportVariables = termFactory.newSet();
			TermSet allExportVariables = termFactory.newSet();

			for (Port p : getPublicInputPorts())
				allImportVariables.add(termFactory
						.newOrGet(new TermSpec.PortVariableSpec(p)));

			for (MemoryCell c : getMemoryCells())
				allImportVariables.add(termFactory
						.newOrGet(new TermSpec.PreVariableSpec(c)));

			for (Port p : getPublicOutputPorts())
				allExportVariables.add(termFactory
						.newOrGet(new TermSpec.PortVariableSpec(p)));

			for (MemoryCell c : getMemoryCells())
				allExportVariables.add(termFactory
						.newOrGet(new TermSpec.PostVariableSpec(c)));

			for (Automaton aut : this)
				if (aut.isMaster())
					aut.commandify(allImportVariables, allExportVariables);
		}

		public void commandify() {
			TermSet allImportVariables = termFactory.newSet();
			TermSet allExportVariables = termFactory.newSet();

			for (Port p : getPublicInputPorts())
				allImportVariables.add(termFactory
						.newOrGet(new TermSpec.PortVariableSpec(p)));

			for (MemoryCell c : getMemoryCells())
				allImportVariables.add(termFactory
						.newOrGet(new TermSpec.PreVariableSpec(c)));

			for (Port p : getPublicOutputPorts())
				allExportVariables.add(termFactory
						.newOrGet(new TermSpec.PortVariableSpec(p)));

			for (MemoryCell c : getMemoryCells())
				allExportVariables.add(termFactory
						.newOrGet(new TermSpec.PostVariableSpec(c)));

			for (Automaton aut : this)
				aut.commandify(allImportVariables, allExportVariables);
		}

		public MemoryCellSet getMemoryCells() {
			MemoryCellSet memoryCells = memoryCellFactory.newSet();
			for (Automaton aut : this)
				memoryCells.addAll(aut.getMemoryCells());

			return memoryCells;
		}

		public PortSet getPublicInputPorts() {
			PortSet ports = portFactory.newSet();
			for (Automaton aut : this)
				ports.addAll(aut.getPublicInputPorts());

			return ports;
		}

		public PortSet getPublicOutputPorts() {
			PortSet ports = portFactory.newSet();
			for (Automaton aut : this)
				ports.addAll(aut.getPublicOutputPorts());

			return ports;
		}

		public PortSet getPublicPorts() {
			PortSet ports = portFactory.newSet();
			for (Automaton aut : this)
				ports.addAll(aut.getPublicPorts());

			return ports;
		}

		public PortSet getPrivatePorts() {
			PortSet privatePorts = portFactory.newSet();
			for (Automaton aut : this)
				privatePorts.addAll(aut.getPrivatePorts());

			return privatePorts;
		}

		public void inferQueues() {
			for (Automaton aut : this)
				aut.inferQueues();
		}

		public List<AutomatonSet> partition() {
			List<AutomatonSet> parts1 = new ArrayList<>();
			List<AutomatonSet> parts2 = new ArrayList<>();

			for (Automaton aut : this) {
				AutomatonSet part = newSet();
				part.add(aut);

				if (!aut.isSynchronizing())
					parts1.add(part);

				else {
					List<AutomatonSet> dependentParts = new ArrayList<>();
					for (AutomatonSet s : parts2)
						if (!aut.isIndependentOf(s)) {
							dependentParts.add(s);
							part.addAll(s);
						}

					parts2.removeAll(dependentParts);
					parts2.add(part);
				}
			}

			List<AutomatonSet> partition = new ArrayList<>();
			partition.addAll(parts1);
			partition.addAll(parts2);

			return partition;
		}

		public List<List<Automaton>> sortByFrontier() {
			List<List<Automaton>> sortedAutomata = new ArrayList<>();
			AutomatonSet remainingAutomata = newSet();
			remainingAutomata.addAll(this);

			while (!remainingAutomata.isEmpty()) {
				PortSet frontier = portFactory.newSet();

				AutomatonSet toVisit = newSet();
				AutomatonSet visited = newSet();
				List<Automaton> automata = new ArrayList<>();

				Automaton automaton = null;
				for (Automaton aut : remainingAutomata)
					if (automaton == null
							|| aut.getAllPorts().count() < automaton
									.getAllPorts().count())

						automaton = aut;

				toVisit.add(automaton);
				while (!toVisit.isEmpty()) {
					PortSet newFrontier = null;

					automaton = null;
					for (Automaton a : toVisit) {
						PortSet candidateFrontier = portFactory.takeDifference(
								frontier, a.getAllPorts());

						if (automaton == null)
							automaton = a;
						else
							compare: {
								if (a.countTransitions() > automaton
										.countTransitions())
									break compare;
								if (a.countTransitions() < automaton
										.countTransitions()) {
									automaton = a;
									break compare;
								}

								assert a.countTransitions() == automaton
										.countTransitions();

								if (candidateFrontier.count() > newFrontier
										.count())
									break compare;
								if (candidateFrontier.count() < newFrontier
										.count()) {
									automaton = a;
									break compare;
								}

								assert candidateFrontier.count() == newFrontier
										.count();

								if (a.countStates() > automaton.countStates())
									break compare;
								if (a.countStates() < automaton.countStates()) {
									automaton = a;
									break compare;
								}

								assert a.countStates() == automaton
										.countStates();
							}

						if (automaton == a)
							newFrontier = candidateFrontier;
					}

					frontier = newFrontier;

					automata.add(automaton);
					visited.add(automaton);
					toVisit.addAll(takeIntersection(this,
							automaton.getNeighbors()));
					toVisit.removeAll(visited);
				}

				sortedAutomata.add(automata);
				remainingAutomata.removeAll(visited);
			}

			return sortedAutomata;
		}
	}
}