package nl.cwi.reo.automata;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import nl.cwi.pr.misc.IdObjectFactory;
import nl.cwi.pr.misc.PortFactory;
import nl.cwi.pr.misc.PortFactory.Port;
import nl.cwi.pr.misc.PortFactory.PortSet;
import nl.cwi.reo.automata.AutomatonFactory.Automaton;
import nl.cwi.reo.automata.StateFactory.State;
import nl.cwi.reo.automata.StateFactory.StateSet;
import nl.cwi.reo.automata.TransitionFactory.Transition;
import nl.cwi.reo.automata.TransitionFactory.TransitionSet;

public abstract class StateFactory extends
		IdObjectFactory<State, StateSet, StateSpec> {

	//
	// METHODS - PUBLIC
	//

	@Override
	public StateSet newSet() {
		return new StateSet();
	}

	//
	// CLASSES - PUBLIC
	//

	public class State extends
			IdObjectFactory<State, StateSet, StateSpec>.IdObject {

		private Cache cache;

		//
		// CONSTRUCTORS
		//

		protected State(int id, StateSpec spec) {
			super(id, spec);
		}

		//
		// METHODS - PUBLIC
		//

		public void disableCache() {
			cache = null;
		}

		public void enableCache() {
			cache = new Cache();
		}

		public Automaton getAutomaton() {
			return getSpec().getAutomaton();
		}

		public PortSet getPorts() {
			PortSet ports = getPortFactory().newSet();
			for (Transition tr : getTransitions())
				ports.addAll(tr.getPorts());

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

			PortSet ports = getPortFactory().newSet();
			for (Port p : getPorts())
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

		public TransitionSet getTransitions() {
			return getSpec().getTransitions();
		}

		public Map<Port, TransitionSet> getTransitionsPerPort() {

			/*
			 * Check cache
			 */

			if (cache != null && cache.getTransitionsPerPortResult != null)
				return cache.getTransitionsPerPortResult;

			/*
			 * Get
			 */

			TransitionFactory transitionFactory = getTransitionFactory();

			Map<Port, TransitionSet> transitionsPerPort = new TreeMap<>();
			for (Transition tr : getTransitions())
				for (Port p : tr.getPorts()) {
					if (!transitionsPerPort.containsKey(p))
						transitionsPerPort.put(p, transitionFactory.newSet());

					transitionsPerPort.get(p).add(tr);
				}

			for (Transition tr : getTransitions())
				for (Integer integ : tr.getGroupIds())
					for (Port p : getAutomaton().getPortsPerGroupId()
							.get(integ)) {

						if (!transitionsPerPort.containsKey(p))
							transitionsPerPort.put(p,
									transitionFactory.newSet());

						transitionsPerPort.get(p).add(tr);
					}

			for (Port p : transitionsPerPort.keySet())
				transitionsPerPort.get(p).addAll(
						getTransitions().getSilentSubset());

			transitionsPerPort = new LinkedHashMap<>(transitionsPerPort);

			/*
			 * Update cache and return
			 */

			if (cache != null)
				cache.getTransitionsPerPortResult = transitionsPerPort;

			return transitionsPerPort;
		}

		public boolean isInitial() {
			return getSpec().isInitial();
		}

		@Override
		public String toString() {
			return "q" + getId();
		}

		//
		// METHODS - DEFAULT
		//

		void addTransition(Transition transition) {
			if (transition == null)
				throw new NullPointerException();
			if (transition.getFactory() != getTransitions().getFactory())
				throw new IllegalStateException();

			getTransitions().add(transition);
		}

		PortFactory getPortFactory() {
			return getAutomaton().getPortFactory();
		}

		TransitionFactory getTransitionFactory() {
			return getAutomaton().getTransitionFactory();
		}

		void removeTransition(Transition transition) {
			if (transition == null)
				throw new NullPointerException();
			if (transition.getFactory() != getTransitions().getFactory())
				throw new IllegalStateException();

			getTransitions().remove(transition);
		}

		//
		// CLASSES - PRIVATE
		//

		private class Cache {
			PortSet getPrivatePortsResult;
			PortSet getPublicPortsResult;
			Map<Port, TransitionSet> getTransitionsPerPortResult;
		}
	}

	public class StateSet extends
			IdObjectFactory<State, StateSet, StateSpec>.IdObjectSet {
	}
}
