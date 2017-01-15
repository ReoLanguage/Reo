package nl.cwi.reo.workautomata;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.SortedSet;

import nl.cwi.reo.semantics.Port;

/**
 * Iterates over the Cartesian product of a non-empty list of local transitions. 
 * Returns a combination of local transitions that is composable.
 */
public class TransitionIterator implements Iterator<List<Transition>> {

	/**
	 * List of automata.
	 */
	private final List<WorkAutomaton> automata;
	
	/**
	 * List of local transitions.
	 */
	private final List<List<Transition>> localtransitions;

	/**
	 * List of iterators of each collection
	 */
	private final List<Iterator<Transition>> iterators;

	/**
	 * Current combination of local transitions.
	 */
	private List<Transition> tuple;

	/**
	 * Indicates whether the current tuple is a new tuple.
	 */
	private boolean isNext;
	
	// Just for fun: counting the actual and total number of increments
	public int actual = 0; 
	public int total = 0; 

	/**
	 * Constructor.
	 * @param automata				list of work automata
	 * @param s1					list of local states, such that s1.get(i) is a state in automata.get(i).
	 * @param localtransitions		list of sets of local transitions, such that localtransitions.get(i)
	 * consists of transitions in automata.get(i).
	 */
	public TransitionIterator(List<WorkAutomaton> automata, List<String> s1, List<List<Transition>> localtransitions) {
		
		// Initialize all fields.
		this.automata = automata;
		this.localtransitions = localtransitions;
		this.iterators = new ArrayList<Iterator<Transition>>();
		this.isNext = false;
		
		// Insert a local silent self loop transition a the start of each list of local transitions.
		for (int i = 0; i < localtransitions.size(); i++) 
			this.localtransitions.get(i).add(0, Transition.getIdlingTransition(s1.get(i)));

		// Initialize iterators.
		for (List<Transition> Tlocal : localtransitions) {
			Iterator<Transition> iterator = Tlocal.iterator();
			iterators.add(iterator);
		}
		
		// Initialize the tuple. This sets all local transitions to a silent self loop transition.
		for (int i = 0; i < localtransitions.size(); i++) 
			tuple.set(i, iterators.get(i).next());	
		
		// Skip this first tuple, and initialize the isNext flag.
		jumpToNext();
		
		// Just for fun: count total amount of increments.
		this.actual = 0;
		this.total = 1;
		for (int i = 0; i < localtransitions.size(); i++)
			total *= localtransitions.get(i).size();
		
	}
	
	/**
	 * Checks is there exists another composable combination of local transitions.
	 */
	@Override
	public boolean hasNext() {		
		return jumpToNext();
	}
	
	/**
	 * Gets the next composable combination of local transitions, if possible.
	 * @return composable combination of local transitions.
	 * @throws NoSuchElementException if there is no next element.
	 */
	@Override
	public List<Transition> next() {
		if (!isNext) 
			throw new NoSuchElementException();
		return new ArrayList<Transition>(tuple);
	}

	@Override
	public void remove() { 
		throw new UnsupportedOperationException(); 
	}
	
	/**
	 * Updates the current combination of local transitions to the next
	 * composable combination, if possible. The isNext flag is set to true,
	 * if the updated combination is composable, and false otherwise.
	 * @return true iff there exists a next composable combination.
	 */
	private boolean jumpToNext() {
		
		// get the next tuple
		boolean hasNext = increment(0);
		
		if (!hasNext) return false;
		
		boolean composable = true;
		
		do {			
			// Find the index of the first local transition that does not compose 
			// with another local transition, if it exists.
			int i;
			for (i = 0; composable && i < localtransitions.size() - 1; i++) {
				for (int j = i + 1; composable && j < localtransitions.size(); j++) {
					
					// Find the interfaces and synchronization constraints.
					SortedSet<Port> Pi = tuple.get(i).getSyncConstraint();
					SortedSet<Port> Ni = automata.get(i).getInterface();
					SortedSet<Port> Pj = tuple.get(j).getSyncConstraint();
					SortedSet<Port> Nj = automata.get(j).getInterface();
					
					// Check if composability is broken.
					if (Pi.retainAll(Nj) != Pj.retainAll(Ni)) 
						composable = false;
				}
			}
			
			if (!composable) {	
				// Increment the first iterator at index j greater or equal to i, the problematic
				// index, and reset all iterators prior to j. The usual procedure would increment 
				// the first iterator that has a next value. This general procedure would continue 
				// to yield a composability conflict at index i, until all iterators before index
				// i would not have a next value. In this case, the general procedure would increment
				// the first iterator at index j starting from index i that has a next value and 
				// reset all iterators before index j. Thus, simply incrementing the first iterator 
				// at index j greater or equal to i and resetting all iterators before index j yields 
				// the same result as the general procedure. 
				hasNext = increment(i);
			}
			
		} while (hasNext && !composable);
		
		if (composable)
			isNext = true;
		else
			isNext = false;
		
		return hasNext;
	}
	
	/**
	 * Increments, if possible, the first index j starting from index i and resets all indices before j.
	 * @param i 	lower bound of incremented index.
	 * @return true if the increment was possible, and false if there is no next tuple.
	 */
	private boolean increment(int i) {
		
		// just for fun: count the actual increments.
		this.actual += 1;
		
		// Find first iterator j the has a next element, starting from i.
		int j;
		for (j = i; j < iterators.size(); j++)
			if (iterators.get(j).hasNext()) 
				break;
		
		// if there is no next, return false
		if (i == iterators.size())
			return false;
		
		// Reset all iterators before index.
		for (int k = 0; k < j; k++) 
			iterators.set(k, localtransitions.get(k).iterator());

		// Update the tuple at all indices up to j.
		for (int k = 0; k <= j; k++) 
			tuple.set(k, iterators.get(k).next());	
		
		return true;
	}
}
