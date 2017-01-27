package nl.cwi.reo.automata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import nl.cwi.pr.misc.IdObjectFactory;
import nl.cwi.pr.misc.PortFactory;
import nl.cwi.pr.misc.PortFactory.Port;
import nl.cwi.pr.misc.PortFactory.PortSet;
import nl.cwi.reo.automata.AutomatonFactory.Automaton;
import nl.cwi.reo.automata.AutomatonFactory.AutomatonSet;
import nl.cwi.reo.automata.ConstraintFactory.Constraint;
import nl.cwi.reo.automata.ConstraintFactory.ConstraintSet;
import nl.cwi.reo.automata.StateFactory.State;
import nl.cwi.reo.automata.TransitionFactory.Transition;
import nl.cwi.reo.automata.TransitionFactory.TransitionSet;

public abstract class TransitionFactory extends
		IdObjectFactory<Transition, TransitionSet, TransitionSpec> {

	//
	// METHODS - PUBLIC
	//

	@Override
	public TransitionSet newSet() {
		return new TransitionSet();
	}

	//
	// CLASSES - PUBLIC
	//

	public class Transition extends
			IdObjectFactory<Transition, TransitionSet, TransitionSpec>.IdObject {

		private Cache cache;

		//
		// CONSTRUCTORS
		//

		protected Transition(int id, TransitionSpec spec) {
			super(id, spec);
			getSpec().getSource().addTransition(this);
		}

		//
		// METHODS - PUBLIC
		//

		@Override
		public int compareTo(Transition o) {
			if (o == null)
				throw new NullPointerException();

			int source, target;
			if ((source = getSource().compareTo(o.getSource())) == 0)
				if ((target = getTarget().compareTo(o.getTarget())) == 0)
					return 0;
				else
					return target;
			else
				return source;
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

		public Constraint getConstraint() {
			return getSpec().getConstraint();
		}

		public Constraint getConstraintPlus() {

			/*
			 * Check cache
			 */

			if (cache != null && cache.getConstraintPlusResult != null)
				return cache.getConstraintPlusResult;

			/*
			 * Get
			 */

			if (!hasPredictableNeighbors())
				throw new IllegalStateException();

			ConstraintSet constraints = getConstraintFactory().newSet();
			constraints.add(getConstraint());

			for (TransitionSet s : getAgreeableTransitionsPerNeighbor()
					.values())

				for (Transition tr : s)
					constraints.add(tr.getConstraint());

			Constraint constraintPlus = getConstraintFactory().compose(
					getAutomaton(), constraints);
						
			/*
			 * Update cache and return
			 */

			if (cache != null)
				cache.getConstraintPlusResult = constraintPlus;

			return constraintPlus;
		}

		public Set<Integer> getGroupIds() {
			return getRepresentatives().keySet();
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
			for (Entry<Port, Integer> entr : getAutomaton()
					.getIndexPerPublicPort().entrySet())

				if (getPorts().contains(entr.getKey()))
					indices.put(entr.getKey(), entr.getValue());

			/*
			 * Update cache and return
			 */

			if (cache != null)
				cache.getIndexPerPublicPortResult = indices;

			return indices;
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
			properties.put("PREDICTABLE_NEIGHBORS", hasPredictableNeighbors());

			/*
			 * Update cache and return
			 */

			if (cache != null)
				cache.getPropertiesResult = properties;

			return properties;
		}

		public PortSet getPorts() {
			return getSpec().getPorts();
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

			AutomatonSet neighbors = getAutomaton().getFactory().newSet();

			for (Entry<Automaton, PortSet> entr : getAutomaton()
					.getPortsPerNeighbor().entrySet())

				if (!entr.getValue().containsNone(getPorts()))
					neighbors.add(entr.getKey());

			/*
			 * Update cache and return
			 */

			if (cache != null)
				cache.getNeighborsResult = neighbors;

			return neighbors;
		}

		public List<Integer> getPrivateGroupIds() {
			List<Integer> groupIds = new ArrayList<>(getGroupIds());
			groupIds.retainAll(getAutomaton().getPrivateGroupIds());
			return groupIds;
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

			PortSet ports = getPortFactory().takeIntersection(getPorts(),
					getAutomaton().getPrivatePorts());

			/*
			 * Update cache and return
			 */

			if (cache != null)
				cache.getPrivatePortsResult = ports;

			return ports;
		}

		public List<Integer> getPublicGroupIds() {
			List<Integer> groupIds = new ArrayList<>(getGroupIds());
			groupIds.retainAll(getAutomaton().getPublicGroupIds());
			return groupIds;
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

			PortSet ports = getPortFactory().takeIntersection(getPorts(),
					getAutomaton().getPublicPorts());

			/*
			 * Update cache and return
			 */

			if (cache != null)
				cache.getPublicPortsResult = ports;

			return ports;
		}

		public State getSource() {
			return getSpec().getSource();
		}

		public State getTarget() {
			return getSpec().getTarget();
		}

		public Map<Automaton, Transition> getPredictionPerNeighbor() {

			/*
			 * Check cache
			 */

			if (cache != null && cache.getPredictionPerNeighbor != null)
				return cache.getPredictionPerNeighbor;

			/*
			 * Get
			 */

			if (!hasPredictableNeighbors())
				throw new IllegalStateException();

			Map<Automaton, Transition> predictionPerNeighbor = new LinkedHashMap<>();
			for (Entry<Automaton, TransitionSet> entr : getAgreeableTransitionsPerNeighbor()
					.entrySet())
				predictionPerNeighbor.put(entr.getKey(), entr.getValue()
						.getSorted().get(0));

			/*
			 * Update cache and return
			 */

			if (cache != null)
				cache.getPredictionPerNeighbor = predictionPerNeighbor;

			return predictionPerNeighbor;
		}

		public Map<Integer, Port> getRepresentatives() {
			return getSpec().getRepresentatives();
		}

		public boolean hasPredictableNeighbors() {
			for (TransitionSet s : getAgreeableTransitionsPerNeighbor()
					.values())

				if (s.count() != 1)
					return false;

			return true;
		}

		public boolean isLoop() {
			return getSource() == getTarget();
		}

		public boolean isSynchronizing() {
			return getPorts().count() + getGroupIds().size() > 1;
		}

		public boolean isSilent() {
			return getPorts().isEmpty() && getGroupIds().isEmpty();
		}

		@Override
		public String toString() {
			return "<" + getSource() + ","
					+ getPorts().getSorted().toString().replaceAll(" ", "")
					+ "," + getConstraint() + "," + getTarget() + ">";
		}

		//
		// METHODS - DEFAULT
		//

		ConstraintFactory getConstraintFactory() {
			return getAutomaton().getConstraintFactory();
		}

		PortFactory getPortFactory() {
			return getAutomaton().getPortFactory();
		}

		void removePorts(PortSet ports) {
			if (ports == null)
				throw new NullPointerException();
			if (ports.getFactory() != getPorts().getFactory())
				throw new IllegalStateException();

			getPorts().removeAll(ports);
		}

		//
		// METHODS - PRIVATE
		//

		private Map<Automaton, TransitionSet> getAgreeableTransitionsPerNeighbor() {
			Map<Automaton, PortSet> portsPerNeighbor = new TreeMap<>();
			for (Entry<Automaton, PortSet> entr : getAutomaton()
					.getPortsPerNeighbor().entrySet()) {

				PortSet automatonPorts = entr.getValue();
				PortSet ports = getPorts();

				if (!automatonPorts.containsNone(ports))
					portsPerNeighbor.put(entr.getKey(), getPortFactory()
							.takeIntersection(automatonPorts, ports));
			}

			portsPerNeighbor = new LinkedHashMap<>(portsPerNeighbor);
			Map<Automaton, TransitionSet> agreeableTransitionsPerNeighbor = new LinkedHashMap<>();

			for (Entry<Automaton, PortSet> entr : portsPerNeighbor.entrySet()) {
				Automaton neighbor = entr.getKey();
				PortSet ports = entr.getValue();
				TransitionSet agreeableTransitions = neighbor
						.getTransitionFactory().newSet();

				for (Transition tr : neighbor.getTransitions())
					if (tr.getPorts().containsAll(ports))
						agreeableTransitions.add(tr);

				agreeableTransitionsPerNeighbor.put(neighbor,
						agreeableTransitions);
			}

			return agreeableTransitionsPerNeighbor;
		}

		//
		// CLASSES - PRIVATE
		//

		private class Cache {
			Constraint getConstraintPlusResult;
			Map<Port, Integer> getIndexPerPublicPortResult;
			AutomatonSet getNeighborsResult;
			Map<Automaton, Transition> getPredictionPerNeighbor;
			PortSet getPrivatePortsResult;
			Map<String, Boolean> getPropertiesResult;
			PortSet getPublicPortsResult;
		}
	}

	public class TransitionSet
			extends
			IdObjectFactory<Transition, TransitionSet, TransitionSpec>.IdObjectSet {

		public TransitionSet getSilentSubset() {
			TransitionSet subset = getFactory().newSet();
			for (Transition t : this)
				if (t.isSilent())
					subset.add(t);

			return subset;
		}

		public TransitionSet getNonsilentSubset() {
			TransitionSet subset = getFactory().newSet();
			for (Transition t : this)
				if (!t.isSilent())
					subset.add(t);

			return subset;
		}
	}
}
