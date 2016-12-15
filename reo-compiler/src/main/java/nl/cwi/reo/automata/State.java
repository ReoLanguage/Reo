package nl.cwi.reo.automata;

import java.util.List;

public class State {
	
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
		String s = qs.get(0).name;
		for (int i = 1; i < qs.size(); i++) 
			s += "|" + qs.get(i).name;
		return new State(s);
	}
	
	/**
	 * String represenation of this state.
	 */
	public String toString() {
		return this.name;
	}
}
