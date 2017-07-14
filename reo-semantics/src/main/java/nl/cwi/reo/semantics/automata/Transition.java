package nl.cwi.reo.semantics.automata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.util.Monitor;

/**
 * A transition, labeled with a generic label of type L, that is used in a {@link nl.cwi.reo.semantics.automata.Automaton}.
 * If the transition label type L is immutable, then Transition<L> is immutable too.
 * @param L 	transition label type
 */
public final class Transition<L extends Label<L>> {

	/**
	 * Source state.
	 */
	private final State q1; 
	
	/**
	 * Target state.
	 */
	private final State q2; 

	/**
	 * Synchronization constraint.
	 */
	private final SortedSet<Port> N;

	/**
	 * Transition label.
	 */
	private final L lbl;
	
	/**
	 * Constructs a new transition. 
	 * @param q1		source state
	 * @param q2		target state
	 * @param N			synchronization constraint
	 * @param lbl		transition label
	 */
	public Transition(State q1, State q2, SortedSet<Port> N, L lbl) {
		if (q1 == null)
			throw new IllegalArgumentException("No source state specified.");	
		if (q2 == null)
			throw new IllegalArgumentException("No target state specified.");
		if (N == null)
			throw new IllegalArgumentException("No synchronization constraint specified.");
		if (lbl == null)
			throw new IllegalArgumentException("No transition label specified.");
		this.q1 = q1;
		this.q2 = q2;
		this.N = Collections.unmodifiableSortedSet(N);
		this.lbl = lbl;
	}
	
	/**
	 * Retrieves the source state of the current transition.
	 * @return name of source state
	 */
	public State getSource() {
		return this.q1;
	}
	
	/**
	 * Retrieves the target state of the current transition.
	 * @return name of target state
	 */
	public State getTarget() {
		return this.q2;
	}
	
	/**
	 * Retrieves the synchronization constraint of the current transition.
	 * @return synchronization constraint
	 */
	public SortedSet<Port> getSyncConstraint() {
		return this.N;
	}
	
	/**
	 * Retrieves the job constraint of the current transition.
	 * @return job constraint
	 */
	public L getLabel() {
		return this.lbl;
	}

	/**
	 * Composes this transition with of a list of transitions.
	 * @param automata		a list of work automata
	 * @return Composed transition.
	 */
	public Transition<L> compose(List<Transition<L>> transitions) {
		List<State> sources = new ArrayList<State>();
		List<State> targets = new ArrayList<State>();
		SortedSet<Port> syncs = new TreeSet<Port>();
		List<L> labels = new ArrayList<L>();
		for (Transition<L> t : transitions) {
			sources.add(t.q1);
			targets.add(t.q2);
			syncs.addAll(t.N);
			labels.add(t.getLabel());
		}
		State q1 = this.q1.compose(sources);
		State q2 = this.q2.compose(targets);
		SortedSet<Port> N = new TreeSet<Port>(this.N);
		N.addAll(syncs);
		L lbl = this.lbl.compose(labels);
		return new Transition<L>(q1, q2, N, lbl);
	}

	/**
	 * Restricts the interface of this transition
	 * @param intface			smaller interface
	 * @returns Transition with interface intface.
	 */
	public Transition<L> restrict(Collection<? extends Port> intface) {
		SortedSet<Port> N = new TreeSet<Port>(this.N);
		N.retainAll(intface);		
		L lbl = this.lbl.restrict(intface);
		return new Transition<L>(this.q1, this.q2, N, lbl);
	}

	/**
	 * Renames entry.Key() to entry.Value() for every entry renaming map.
	 * @param links		renaming map
	 * @return renamed transition.
	 */
	public Transition<L> rename(Map<Port, Port> links) {
		SortedSet<Port> rN = new TreeSet<Port>();
		for (Port port : this.N) {
			Port newport = links.get(port);
			if (newport == null) newport = port;
			rN.add(newport); //rN.add(new Port(newport.getName()));
		}
		L lbl = this.lbl.rename(links);
		return new Transition<L>(this.q1, this.q2, rN, lbl);
	}
	
	/**
	 * Evaluates this label using specified parameters.
	 * @param params	parameters
	 * @return Evaluated label.
	 */
	public Transition<L> evaluate(Scope s, Monitor m) {
		return new Transition<L>(q1, q2, N, lbl.evaluate(s, m));
	}
	
	@Override 
	public String toString() { 
		return "(" + q1 + "," + N + "," + lbl + "," + q2 + ")";
	}
	
	@Override
	public boolean equals(@Nullable Object other) {
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof Transition<?>)) return false;
	    Transition<?> p = (Transition<?>)other;
	   	return Objects.equals(this.q1, p.q1) && Objects.equals(this.q2, p.q2) 
	   			&& Objects.equals(this.N, p.N) && Objects.equals(this.lbl, p.lbl);
	}

	@Override 
	public int hashCode() {
	    return Objects.hash(this.q1, this.q2, this.N, this.lbl);
	}
}
