package nl.cwi.reo.automata;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

import nl.cwi.reo.semantics.Port;

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
	private final List<List<Transition<L>>> outs;

	/**
	 * List of iterators of each collection
	 */
	private final List<Iterator<Transition<L>>> iters;

	/**
	 * Current combination of local transitions.
	 */
	private List<Transition<L>> tuple;

	/**
	 * Indicates whether the current tuple is a new tuple.
	 */
	private boolean isNext;
	
	/**
	 * Constructs an iterator that enumerates all global transitions 
	 * originating from the global initial state.
	 * @param automata			list of automata
	 * @throws NullPointerException if the list is null or empty, or 
	 * if the list contains a null automaton. 
	 */
	public TransitionIterator(List<Automaton<L>> automata) {
		
		if (automata == null)
			throw new NullPointerException("Undefined list of automata.");
		
		// Initialize all fields.
		this.automata = automata;
		this.outs = new ArrayList<List<Transition<L>>>();
		this.iters = new ArrayList<Iterator<Transition<L>>>();
		this.tuple = new ArrayList<Transition<L>>();
		this.isNext = false;
		
		for (Automaton<L> A : automata) {
			if (A == null)
				throw new NullPointerException("Undefined automaton in list of automata.");
			
			List<Transition<L>> outA = new ArrayList<Transition<L>>();
			outA.add(0, new Transition<L>(A.initial, A.initial, new TreeSet<Port>(), A.lbl));			
			outA.addAll(A.out.get(A.initial));			
			outs.add(outA);			
			Iterator<Transition<L>> iterA = outA.iterator();			
			iters.add(iterA);			
			tuple.add(iterA.next());	
			
		}
		
		// Skip this first tuple, and initialize the isNext flag.
		findNext();		
	}
	
	/**
	 * Checks is there exists another composable combination of local transitions.
	 */
	@Override
	public boolean hasNext() {
		findNext();
		return isNext;
	}
	
	/**
	 * Gets the next composable combination of local transitions, if possible.
	 * @return composable combination of local transitions.
	 * @throws NoSuchElementException if there is no next element.
	 */
	@Override
	public List<Transition<L>> next() {
		findNext();
		if (!isNext) throw new NoSuchElementException();
		isNext = false;
		return new ArrayList<Transition<L>>(tuple);
	}

	@Override
	public void remove() { 
		throw new UnsupportedOperationException(); 
	}
	
	/**
	 * Updates, if possible and necessary, the current combination of local 
	 * transitions to the next composable combination and sets the isNext flag 
	 * to true. If no composable combination exists, the isNext flag is set to false.
	 */
	private void findNext() {	
		
		// Nothing to do: current tuple is a new combination. 
		if (isNext) return;
		
		boolean hasNext = findNext(0);
				
		while (hasNext) {
			int i;
			
		loops:
			for (i = 0; i < outs.size() - 1; i++) {
				for (int j = i + 1; j < outs.size(); j++) {
					
					Set<Port> Nij = new HashSet<Port>(tuple.get(i).getSyncConstraint());
					Nij.retainAll(automata.get(j).iface);
					Set<Port> Nji = new HashSet<Port>(tuple.get(j).getSyncConstraint());
					Nji.retainAll(automata.get(i).iface);
					
					if (!Nij.equals(Nji)) 
						break loops;
				}
			}
			
			if (i + 1 == outs.size()) {
				isNext = true;
				return;
			} else {
				// if current tuple is not composable, increment i-th iterator, because
				// the tuple remains incomposable after incrementing any k-th iterator (k < i).
				// Note that this optimization does not skip any tuples:
				// Increment the first iterator at index j greater or equal to i, the problematic
				// index, and reset all iterators prior to j. The usual procedure would increment 
				// the first iterator that has a next value. This general procedure would continue 
				// to yield a composability conflict at index i, until all iterators before index
				// i would not have a next value. In this case, the general procedure would increment
				// the first iterator at index j starting from index i that has a next value and 
				// reset all iterators before index j. Thus, simply incrementing the first iterator 
				// at index j greater or equal to i and resetting all iterators before index j yields 
				// the same result as the general procedure.
				hasNext = findNext(i);
			}
		}
	}
	
	/**
	 * Increments, if possible, the first index j starting from index i and resets all indices before j.
	 * @param i 	lower bound of incremented index.
	 * @return true if the increment was possible, and false if there is no next tuple.
	 */
	private boolean findNext(int i) {
		
		// Find first iterator j the has a next element, starting from i.
		int j;
		for (j = i; j < iters.size(); j++)
			if (iters.get(j).hasNext()) 
				break;
		
		// if there is no next j, return false
		if (j == iters.size())
			return false;		
		
		// Reset all iterators before index.
		for (int k = 0; k < j; k++) 
			iters.set(k, outs.get(k).iterator());

		// Update the tuple at all indices up to j.
		for (int k = 0; k <= j; k++) 
			tuple.set(k, iters.get(k).next());	
		
		return true;
	}
}
