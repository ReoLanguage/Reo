package nl.cwi.reo.templates;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.ports.PortType;
import nl.cwi.reo.semantics.predicates.Conjunction;
import nl.cwi.reo.semantics.predicates.Equality;
import nl.cwi.reo.semantics.predicates.Formula;
import nl.cwi.reo.semantics.predicates.Function;
import nl.cwi.reo.semantics.predicates.MemoryVariable;
import nl.cwi.reo.semantics.predicates.Negation;
import nl.cwi.reo.semantics.predicates.PortVariable;
import nl.cwi.reo.semantics.predicates.Relation;
import nl.cwi.reo.semantics.predicates.Term;
import nl.cwi.reo.semantics.predicates.Variable;

// TODO: Auto-generated Javadoc
/**
 * The Class Transition.
 */
public class Transition {

	/** Guard. */
	private final Formula guard;

	/** Ports update. */
	private final Map<PortVariable, Term> output;

	/** Memory update. */
	private final Map<MemoryVariable, Term> memory;	

	/**
	 * Instantiates a new transition.
	 *
	 * @param guard
	 *            the guard
	 * @param output
	 *            the output
	 * @param memory
	 *            the memory
	 * @param input
	 *            the input
	 */
	public Transition(Formula guard, Map<Variable,Term> update, Formula constraint) {
		if (guard == null)
			throw new IllegalArgumentException("No guard specified.");
		this.guard = guard;
		this.output = new HashMap<>();
		this.memory = new HashMap<>();
		for(Variable v : update.keySet()) {
			if(v instanceof PortVariable)
				output.put((PortVariable) v, update.get(v));
			if(v instanceof MemoryVariable)
				memory.put((MemoryVariable) v, update.get(v));
		}
	}
	
	/**
	 * Gets the set of guards.
	 * 
	 * @return guard
	 */
	public Formula getGuard() {
		List<Formula> guards = new LinkedList<>();
		if(guard instanceof Conjunction){
			List<Formula> _guards = ((Conjunction) guard).getClauses();
			for(Formula _f : _guards){
				if(_f instanceof Negation && ((Negation) _f).getFormula() instanceof Relation){
					guards.add(guards.size(),_f);
				}
				else if(_f instanceof Relation){
					guards.add(guards.size(),_f);
				}
				else if(_f instanceof Negation && ((Negation) _f).getFormula() instanceof Equality){
					Formula f = ((Negation)_f).getFormula();
					if((((Equality) f).getRHS() instanceof Function || ((Equality) f).getLHS() instanceof Function) 
						|| f instanceof Relation && !guards.contains(f)){
						guards.add(guards.size(),_f);
					}
					else if(!guards.contains(_f)){
						guards.add(0,_f);
					}
				}
				else
					guards.add(0,_f);
			}
		}
		return new Conjunction(guards);
	}
	
	/**
	 * Gets the set of input port
	 */
	public Set<Port> getInput(){
		Set<Port> inputs = new HashSet<Port>();
		for (PortVariable x : output.keySet()){
			if(output.get(x) instanceof PortVariable){
				PortVariable pv = (PortVariable)output.get(x);
				inputs.add(new Port(pv.getPort().getName(),PortType.IN,pv.getPort().getPrioType(),pv.getPort().getTypeTag(),false));
			}
		}
		for (MemoryVariable x : memory.keySet()){
			if(memory.get(x) instanceof PortVariable){
				PortVariable pv = (PortVariable)memory.get(x);
				inputs.add(new Port(pv.getPort().getName(),PortType.IN,pv.getPort().getPrioType(),pv.getPort().getTypeTag(),false));
			}
		}
		for (Variable x : guard.getFreeVariables()){
			if(x instanceof PortVariable && ((PortVariable) x).isInput()){
				PortVariable pv = (PortVariable)x;
				inputs.add(new Port(pv.getPort().getName(),PortType.IN,pv.getPort().getPrioType(),pv.getPort().getTypeTag(),false));
			}
		}
		
		return inputs;
	}
	
	/**
	 * Gets the values assigned to the output ports.
	 * 
	 * @return assignment of terms to output ports.
	 */
	public Map<PortVariable, Term> getOutput() {
		return this.output;
	}

	/**
	 * Gets the values assigned to the output ports.
	 * 
	 * @return assignment of terms to output ports.
	 */
	public Map<MemoryVariable, Term> getInitial() {
		Map<MemoryVariable, Term> initial = new HashMap<>();
		for (Map.Entry<MemoryVariable, Term> upd : getMemory().entrySet()){
			initial.put(new MemoryVariable(upd.getKey().getName(),false,upd.getKey().getTypeTag()), null);
		}
		for(Variable v : getGuard().getFreeVariables()){
			if(v instanceof MemoryVariable)
				initial.put(new MemoryVariable(v.getName(),false,v.getTypeTag()), null);			
		}
		return initial;
	}
	
	/**
	 * Retrieves the values assigned to memory cells.
	 * 
	 * @return assignment of terms to memory cells.
	 */
	public Map<MemoryVariable, Term> getMemory() {
		return this.memory;
	}
	
	/**
	 * Gets the set of ports that participate in this transition.
	 * 
	 * @return set of ports that participate in this transition
	 */
	public Set<Port> getInterface() {
		
		Set<Port> ports = new HashSet<Port>();
		for(Variable v : guard.getFreeVariables())
			if(v instanceof PortVariable)
				ports.add(((PortVariable) v).getPort());
		for (PortVariable x : output.keySet()){
			ports.add(new Port(x.getPort().getName(),PortType.OUT,x.getPort().getPrioType(),x.getPort().getTypeTag(),false));
			if(output.get(x) instanceof PortVariable){
				PortVariable pv = (PortVariable)output.get(x);
				ports.add(new Port(pv.getPort().getName(),PortType.IN,pv.getPort().getPrioType(),pv.getPort().getTypeTag(),false));
			}
		}
		for (MemoryVariable x : memory.keySet()){
			if(memory.get(x) instanceof PortVariable){
				PortVariable pv = (PortVariable)memory.get(x);
				ports.add(new Port(pv.getPort().getName(),PortType.IN,pv.getPort().getPrioType(),pv.getPort().getTypeTag(),false));
			}
		}
		return ports;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(@Nullable Object other) {
		if (other == null)
			return false;
		if (other == this)
			return true;
		if (!(other instanceof Transition))
			return false;
		Transition rule = (Transition) other;
		return Objects.equals(this.guard, rule.getGuard()) && Objects.equals(this.output, rule.getOutput())
				&& Objects.equals(this.memory, rule.getMemory());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.guard, this.output, this.memory);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return guard + " -> " + output + ", " + memory;
	}
}
