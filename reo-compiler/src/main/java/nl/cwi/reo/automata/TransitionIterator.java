package nl.cwi.reo.automata;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Iterates over the Cartesian product of a non-empty list of local transitions. 
 * Returns a combination of local transitions that is composable.
 */
public class TransitionIterator<L extends Label<L>> implements Iterator<List<Transition<L>>> {

	/**
	 * List of automata.
	 */
	private final List<Automaton<L>> automata;
	
	/**
	 * List of local transitions.
	 */
	private final List<List<Transition<L>>> localtransitions;

	/**
	 * List of iterators of each collection
	 */
	private final List<Iterator<Transition<L>>> iterators;

	/**
	 * Current combination of local transitions.
	 */
	private List<Transition<L>> tuple;

	/**
	 * Indicates whether the current tuple is a new tuple.
	 */
	private boolean isNext;
	
	// Just for fun: counting the actual and total number of increments
	public int actual = 0; 
	public int total = 0; 

	/**
	 * Constructor.
	 * @param automata				list of automata
	 */
	public TransitionIterator(List<Automaton<L>> automata) {
		
		// Initialize all fields.
		this.automata = automata;
		this.localtransitions = new ArrayList<List<Transition<L>>>();
		this.iterators = new ArrayList<Iterator<Transition<L>>>();
		this.isNext = false;
		
		for (Automaton<L> A : automata) {
			
			List<Transition<L>> outA = new ArrayList<Transition<L>>(A.outgoingTransitions.get(A.initial));
			
			// Insert a local silent self loop transition a the start of each list of local transitions.
			outA.add(0, new Transition<L>(A.initial));
			
			localtransitions.add(outA);
			
			Iterator<Transition<L>> iterA = outA.iterator();
			
			iterators.add(iterA);
			
			tuple.add(iterA.next());	
			
		}
		
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
	public List<Transition<L>> next() {
		if (!isNext) 
			throw new NoSuchElementException();
		return new ArrayList<Transition<L>>(tuple);
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
					Set<String> Pi = tuple.get(i).getSyncConstraint();
					Set<String> Ni = automata.get(i).iface;
					Set<String> Pj = tuple.get(j).getSyncConstraint();
					Set<String> Nj = automata.get(j).iface;
					
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
