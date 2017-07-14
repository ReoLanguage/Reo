package nl.cwi.reo.semantics.automata;

import java.util.List;
import java.util.Objects;

import org.checkerframework.checker.nullness.qual.Nullable;

public class State implements Comparable<State> {
	
	/**
	 * State name.
	 */
	private String name;
	
	/**
	 * Constructs a new state.
	 * @param q		state name.
	 */
	public State(String q) {
		this.name = q;
	}
	
	/**
	 * Gets the name of this state.
	 * @return state name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Constructs a composed global state.
	 * @param qs	list of local states.
	 * @return Composed global state.
	 */
	public State compose(List<State> qs) {
		String s = this.name;
		for (int i = 0; i < qs.size(); i++) 
			s += "|" + qs.get(i).name;
		return new State(s);
	}

	@Override
	public String toString() {
		return this.name;
	}
	
	@Override
	public boolean equals(@Nullable Object other) {
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof State)) return false;
	    State q = (State)other;
	   	return Objects.equals(this.name, q.name);
	}
	
    @Override
    public int hashCode() {
	    return Objects.hash(this.name);
    }
	
	@Override
	public int compareTo(State other) {
		return this.name.compareTo(other.name);
	}
}
