package nl.cwi.reo.templates.treo;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.predicates.Conjunction;
import nl.cwi.reo.semantics.predicates.Constant;
import nl.cwi.reo.semantics.predicates.Equality;
import nl.cwi.reo.semantics.predicates.Formula;
import nl.cwi.reo.semantics.predicates.Function;
import nl.cwi.reo.semantics.predicates.MemoryVariable;
import nl.cwi.reo.semantics.predicates.Negation;
import nl.cwi.reo.semantics.predicates.NonNullValue;
import nl.cwi.reo.semantics.predicates.NullValue;
import nl.cwi.reo.semantics.predicates.PortVariable;
import nl.cwi.reo.semantics.predicates.Relation;
import nl.cwi.reo.semantics.predicates.Term;
import nl.cwi.reo.semantics.predicates.Variable;
import nl.cwi.reo.templates.Transition;

/**
 * The Class Transition.
 */

public final class TreoTransition extends Transition{

	
	/** Guard. */
	private final Formula renamedGuard;

	/** Ports update. */
	private final Map<PortVariable, Term> renamedOutput;

	/** Memory update. */
	private final Map<MemoryVariable, Term> renamedMemory;	
	
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
	public TreoTransition(Formula guard, Map<PortVariable, Term> output, Map<MemoryVariable, Term> memory) {
		super(guard,output,memory);
		Map<Port,Port> map = new HashMap<>();
		for(Variable v : guard.getFreeVariables()){
			if(v instanceof PortVariable){
				Port p = ((PortVariable) v).getPort();
				map.put(p, p.rename("p"+p.getName().substring(1)));
			}
		}
		
		renamedGuard=guard.rename(map);
		Map<PortVariable,Term> setPort = new HashMap<>();
		for(PortVariable p :  getOutput().keySet())
			setPort.put(new PortVariable(p.getPort().rename("p"+p.getName().substring(1))),getOutput().get(p).rename(map));
		renamedOutput = setPort;
		Map<MemoryVariable,Term> setMem = new HashMap<>();
		for(MemoryVariable p :  getMemory().keySet()){
			setMem.put(p,getMemory().get(p).rename(map));
		}
		renamedMemory = setMem;

	}
	

	public Map<PortVariable,Term> getRenamedOutput() {
		return renamedOutput;
	}
	
	public Map<MemoryVariable,Term> getRenamedMemory() {
		return renamedMemory;
	}
	
	public Formula getRenamedGuard(){
		return renamedGuard;
	}
	
//	public Map<PortVariable,Term> getRenamedMemory() {
//		Map<PortVariable,Term> set = new HashMap<>();
//		for(MemoryVariable p :  getMemory().keySet())
//			set.put(new MemoryVariable(p.rename("p"+p.getName().substring(1))),getOutput().get(p));
//		return set;
//	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.getGuard(), this.getOutput(), this.getMemory());
	}

}
