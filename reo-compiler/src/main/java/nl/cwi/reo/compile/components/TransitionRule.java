package nl.cwi.reo.compile.components;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.symbolicautomata.MemoryCell;
import nl.cwi.reo.semantics.symbolicautomata.Node;
import nl.cwi.reo.semantics.symbolicautomata.Term;
import nl.cwi.reo.semantics.symbolicautomata.Variable;

public final class TransitionRule {

	/**
	 * Synchronization constraint.
	 */
	private final Set<Port> N;

	/**
	 * Update (assigns value to memory cells and output ports)
	 */
	private final Map<Variable, Term> a;

	/**
	 * Constructs a new transition.
	 * 
	 * @param q1
	 *            source state
	 * @param q2
	 *            target state
	 * @param N
	 *            synchronization constraint
	 * @param a
	 *            transition label
	 */
	public TransitionRule(Set<Port> N, Map<Variable, Term> a) {
		if (N == null)
			throw new IllegalArgumentException("No synchronization constraint specified.");
		if (a == null)
			throw new IllegalArgumentException("No transition label specified.");
		this.N = Collections.unmodifiableSet(N);
		this.a = a;
	}

	/**
	 * Retrieves the synchronization constraint of the current transition.
	 * 
	 * @return synchronization constraint
	 */
	public Set<Port> getSyncConstraint() {
		return this.N;
	}

	public Set<Variable> getVariable(){
		Set<Variable> variable = new HashSet<Variable>();
		
		for(Port p : N){
			variable.add(new Node(p));
		}
		for(Term t :a.values()){
			if(t instanceof MemoryCell)
				variable.add(((MemoryCell) t));
		}
		return variable;
	}
	
	public Set<MemoryCell> getMemoryCells() {		
		Set<MemoryCell> mem = new HashSet<MemoryCell>();
		for(Term t :a.values()){
			if(t instanceof MemoryCell)
				mem.add(((MemoryCell) t));
		}
		return mem;
	}
	/**
	 * Retrieves the job constraint of the current transition.
	 * 
	 * @return job constraint
	 */
	public Map<Variable, Term> getAction() {		
		return this.a;
	}
}
