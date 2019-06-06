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
	
	//Renaming map
	private Map<Port,Port> m = new HashMap<>();
	
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
		for(Port p : getInterface())
			if(p.getName().substring(0, 1).contains("$")) {
				m.put(p, p.rename("p"+p.getName().substring(1,p.getName().length())));
			}
	}
	
	@Override
	public Map<PortVariable, Term> getOutput() {
		Map<PortVariable, Term> map = super.getOutput();
		Map<PortVariable, Term> mapRenamed = new HashMap<>();
		for(PortVariable pv : map.keySet())
			mapRenamed.put(pv.rename(m),map.get(pv).rename(m));
		return mapRenamed;
	}
	
	@Override
	public Formula getGuard() {
		return super.getGuard().rename(m);
	}
	
	@Override
	public Map<MemoryVariable, Term> getMemory() {
		Map<MemoryVariable, Term> map = super.getMemory();
		Map<MemoryVariable, Term> mapRenamed = new HashMap<>();
		for(MemoryVariable pv : map.keySet())
			mapRenamed.put(pv,map.get(pv).rename(m));
		return mapRenamed;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.getGuard(), this.getOutput(), this.getMemory());
	}

}
