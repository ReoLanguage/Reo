 package nl.cwi.reo.templates.promela;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.typetags.TypeTags;
import nl.cwi.reo.interpret.values.IntegerValue;
import nl.cwi.reo.semantics.prba.Distribution;
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
import nl.cwi.reo.semantics.predicates.Term;
import nl.cwi.reo.semantics.predicates.Variable;
import nl.cwi.reo.templates.Transition;

/**
 * The Class Transition.
 */

public final class PromelaTransition extends Transition{

	/** Guard map */
	private Map<Variable, Term> lstate = new HashMap<>();
	
	/** Update map */	
	private Map<Variable, Term> rstate = new HashMap<>();
	
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
	public PromelaTransition(Formula guard, Map<PortVariable, Term> output, Map<MemoryVariable, Term> memory,
			Set<Port> input) {
		super(guard, output, memory);
		Set<Port> s = guard.getPorts();
		for(Port p : s)
			m.put(p, p.rename("p"+p.getName().substring(1)));
		for(PortVariable pv : output.keySet())
			m.put(pv.getPort(), pv.getPort().rename("p"+pv.getPort().getName().substring(1)));
	}

	@Override
	public Formula getGuard() {	
		Formula guard = super.getGuard();
		guard = guard.rename(m);
		return guard;
	}
	
	public void guardToString(){
		Formula g = getGuard();
		
		if(g instanceof Conjunction){
			for(Formula f : ((Conjunction) g).getClauses()){
				if (f instanceof Negation && ((Negation) f).getFormula() instanceof Equality) {
					Equality e = (Equality) ((Negation) f).getFormula();
					if (e.getLHS() instanceof PortVariable && e.getRHS() instanceof NullValue) {
						PortVariable p = (PortVariable) e.getLHS();
						if(p.isInput())
							f = new Equality(p,new NonNullValue());
						else
							f = new Equality(p,new NullValue());
					}
					else if (e.getLHS() instanceof MemoryVariable && e.getRHS() instanceof NullValue) {
						MemoryVariable m = (MemoryVariable) e.getLHS();
						f = new Equality(m,new NonNullValue());
					}
				}
				if(f instanceof Equality){
					Term lhs = ((Equality) f).getLHS();
					Term rhs = ((Equality) f).getRHS();
					if(lhs instanceof PortVariable)
						lstate.put((PortVariable)lhs, rhs);
					if(lhs instanceof MemoryVariable)
						lstate.put((MemoryVariable)lhs, rhs);
				}
			}
		}
		
		if (g instanceof Negation && ((Negation) g).getFormula() instanceof Equality) {
			Equality e = (Equality) ((Negation) g).getFormula();
			if (e.getLHS() instanceof PortVariable && e.getRHS() instanceof NullValue) {
				PortVariable p = (PortVariable) e.getLHS();
				if(p.isInput())
					g = new Equality(p,new NonNullValue());
				else
					g = new Equality(p,new NullValue());
			}
			else if (e.getLHS() instanceof MemoryVariable && e.getRHS() instanceof NullValue) {
				MemoryVariable m = (MemoryVariable) e.getLHS();
				g = new Equality(m,new NonNullValue());
			}
		}
		
		if(g instanceof Equality){
			Term lhs = ((Equality) g).getLHS();
			Term rhs = ((Equality) g).getRHS();
			if(lhs instanceof PortVariable)
				lstate.put((PortVariable)lhs, rhs);
			if(lhs instanceof MemoryVariable)
				lstate.put((MemoryVariable)lhs, rhs);
		}
		
	}
	/**
	 * Write the rewrite rule as a string
	 */
	public String getTransitionString() {
		String RHS = "";
		String LHS = "";
		guardToString();
		//Convert lstate to string :
		int o=0;
		for(Variable v : lstate.keySet()){
			if(v instanceof PortVariable){
				LHS = LHS + " full(" + v.getName() + ".";
				if(lstate.get(v) instanceof NullValue){
					LHS = LHS + "sync) ";		
				}
				else if(lstate.get(v) instanceof NonNullValue){
					LHS = LHS + "data)";					
				}
			}
			if(v instanceof MemoryVariable){
				if(lstate.get(v) instanceof NullValue){
					LHS = LHS + "empty("+v.getName()+ ")";
				}
				else if(lstate.get(v) instanceof NonNullValue){
					LHS = LHS + "full("+v.getName()+ ")";
				}
			}
			if(o!=lstate.size()-1)
				LHS = LHS + " && ";
			o++;
		}
		
		//Convert rstate to string :
		String update = "";
		rstate.putAll(getOutput());
		rstate.putAll(getMemory());
		
		for(Variable key : rstate.keySet()){
			if(key instanceof PortVariable){
				if(!lstate.containsKey(key)){
					LHS = LHS + " && full(" + key.getName() + ".sync)";
				}
				if(rstate.get(key) instanceof PortVariable){
					PortVariable value = (PortVariable)rstate.get(key);
					update = update + "take("+value.getName() + "," + value.getName()+"); put("+ key.getName()+",_" +value.getName()+");";
				}
				if(rstate.get(key) instanceof MemoryVariable){
					MemoryVariable value = (MemoryVariable)rstate.get(key);
					update = update + value.getName() + "?_" + value.getName()+"; put("+ key.getName()+",_" +value.getName()+");";
				}
				if(rstate.get(key) instanceof Function){
					Function value = (Function)rstate.get(key);
					value.toString();
					update = update + getFunctionString(value) + key.getName() + ");";
				}
			}
			if(key instanceof MemoryVariable){			
				MemoryVariable m_prime = new MemoryVariable(key.getName(),!((MemoryVariable) key).hasPrime(),key.getTypeTag());
				if(!rstate.containsKey(key) && !rstate.containsKey(m_prime)){
//					RHS = RHS + "m(" + key.getName().substring(1) + ", *) ";
				}
				if(rstate.get(key) instanceof PortVariable){
					PortVariable value = (PortVariable)rstate.get(key);
					update = update + "take("+ value.getName() + ",_" + value.getName()+");"+ key.getName()+"!_" +value.getName()+";";
				}
				if(rstate.get(key) instanceof MemoryVariable){
					MemoryVariable value = (MemoryVariable)rstate.get(key);
					update = update + value.getName() + "?_" + value.getName()+";"+ key.getName()+"!_" +value.getName()+";";
				}
				if(rstate.get(key) instanceof Function){
					Function value = (Function)rstate.get(key);
					update = update +  getFunctionString(value) + key.getName() + ");";
				}
			}
		}
		RHS = RHS + update;
		
		return "("+ LHS + ") -> " + "atomic{"+ RHS +"}" ;
	}
	
	/**
	 * Gets the set of input ports that participate in this transition.
	 * 
	 * @return set of input ports
	 */
	public Set<Port> getInput() {
		Set<Port> in = new HashSet<>();
		Set<Port> inRenamed = new HashSet<>();
		for(Port p : in)
			inRenamed.add(p.rename("p"+p.getName().substring(1)));
		return inRenamed;
	}

	public String getFunctionString(Function value){
		String f = value.getName()+"(";
		for(Term t : value.getArgs()){
			if(t instanceof MemoryVariable)
				f = f + ((MemoryVariable)t).getName()+",";
			if(t instanceof PortVariable)
				f = f + ((PortVariable)t).getName()+",";
			if(t instanceof Function)
				f = f + getFunctionString((Function)t)+","; 
		}
		return f;
	}
	/**
	 * Gets the values assigned to the output ports.
	 * 
	 * @return assignment of terms to output ports.
	 */
	@Override
	public Map<PortVariable, Term> getOutput() {
		Map<PortVariable, Term> map = super.getOutput();
		Map<PortVariable, Term> mapRenamed = new HashMap<>();
		for(PortVariable pv : map.keySet())
			mapRenamed.put(pv.rename(m),map.get(pv).rename(m));
		return mapRenamed;
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
		return Objects.hash(this.getGuard(), this.getOutput(), this.getMemory(), this.getInput());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.getInput() + " " + this.getGuard() + " -> " + this.getOutput() + ", " + this.getMemory();
	}
}
