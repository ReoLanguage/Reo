package nl.cwi.reo.templates;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.predicates.Conjunction;
import nl.cwi.reo.semantics.predicates.Equality;
import nl.cwi.reo.semantics.predicates.Formula;
import nl.cwi.reo.semantics.predicates.MemoryVariable;
import nl.cwi.reo.semantics.predicates.Negation;
import nl.cwi.reo.semantics.predicates.NonNullValue;
import nl.cwi.reo.semantics.predicates.NullValue;
import nl.cwi.reo.semantics.predicates.PortVariable;
import nl.cwi.reo.semantics.predicates.Term;
import nl.cwi.reo.semantics.predicates.Variable;

/**
 * The Class Transition.
 */

public final class MaudeTransition extends Transition{


	/**
	 * Convention for Maude code generation : 
	 * 
	 * 		- a port : p(<it>name</it>, value)
 	 * 		- a memory cell : m(<it>name</it>, value)
 	 * 		- string port name : "p" + current_port_name.substring(1)  (remove the dollar and add a p)
 	 * 		- int mem name : mem_name.substring(1)
 	 * 		- empty data : *
 	 * 		- data from port: d_ + port_name.substring(1)
 	 * 		- data from mem: d_ + mem_name
	 */
	
	/** Rewrite rule counter */
	static int counter=0;
	
	/** Rewrite rule number */
	private int nb;
	
	/** Left Hand Side of the rewrite rule */
	private Map<Variable, Term> lstate = new HashMap<>();
	
	/** Right Hand Side of the rewrite rule */	
	private Map<Variable, Term> rstate = new HashMap<>();
	
	
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
	public MaudeTransition(Formula guard, Map<PortVariable, Term> output, Map<MemoryVariable, Term> memory,
			Set<Port> input) {
		super(guard, output, memory, input);
		nb=++counter;
		guardToRew();
		rstate.putAll(getOutput());
		rstate.putAll(getMemory());
		
	}
	
	private void guardToRew(){
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
	 * {@inheritDoc}
	 */
	public String getRewString() {
		String RHS = "";
		String LHS = "";
		
		//Convert lstate to string :
		for(Variable v : lstate.keySet()){
			if(v instanceof PortVariable){
				LHS = LHS + " p(\"p" + v.getName().substring(1) + "\",";
				if(lstate.get(v) instanceof NullValue){
					LHS = LHS + " *) ";		
					if(!rstate.containsKey(v)){
						RHS = RHS + "p(\"p" + v.getName().substring(1) + "\", *) ";
					}
				}
				else if(lstate.get(v) instanceof NonNullValue){
					LHS = LHS + "d_" + v.getName().substring(1)+")";					
				}
			}
			if(v instanceof MemoryVariable){
				LHS = LHS + " m(" + v.getName().substring(1) + ",";
				if(lstate.get(v) instanceof NullValue){
					LHS = LHS + " *) ";
					MemoryVariable v_prime = new MemoryVariable(v.getName(),!((MemoryVariable) v).hasPrime(),v.getTypeTag());
					if(!rstate.containsKey(v) && !rstate.containsKey(v_prime)){
						RHS = RHS + "m(" + v.getName().substring(1) + ", *) ";
					}
				}
				else if(lstate.get(v) instanceof NonNullValue){
					LHS = LHS + "d_" + v.getName()+")";					
				}
			}
		}
		
		//Convert rstate to string :
		String update = "";
		
		for(Variable key : rstate.keySet()){
			if(key instanceof PortVariable){
//				MemoryVariable v_prime = new MemoryVariable(v.getName(),!((MemoryVariable) v).hasPrime(),v.getTypeTag());
				if(!lstate.containsKey(key)){
					LHS = LHS + " p(\"p" + key.getName().substring(1) + "\", *) ";
				}
				if(rstate.get(key) instanceof PortVariable){
					PortVariable value = (PortVariable)rstate.get(key);
					update = update + " p(\"p" + key.getName().substring(1) + "\", d_"+ value.getName().substring(1)+")";
					update = update + " p(\"p" + value.getName().substring(1) + "\", * )";
				}
				if(rstate.get(key) instanceof MemoryVariable){
					MemoryVariable value = (MemoryVariable)rstate.get(key);
					update = update + " p(\"p" + key.getName().substring(1) + "\", d_"+ value.getName()+")";
					update = update + " m( "+ value.getName().substring(1) + ", * )";
				}
			}
			if(key instanceof MemoryVariable){			
				MemoryVariable m_prime = new MemoryVariable(key.getName(),!((MemoryVariable) key).hasPrime(),key.getTypeTag());
				if(!rstate.containsKey(key) && !rstate.containsKey(m_prime)){
					RHS = RHS + "m(" + key.getName().substring(1) + ", *) ";
				}
				if(rstate.get(key) instanceof PortVariable){
					PortVariable value = (PortVariable)rstate.get(key);
					update = update + " m(" + key.getName().substring(1) + ", d_"+ value.getName().substring(1)+")";
					update = update + " p(\"p" + value.getName().substring(1) + "\", * )";
				}
				if(rstate.get(key) instanceof MemoryVariable){
					MemoryVariable value = (MemoryVariable)rstate.get(key);
					update = update + " m(" + key.getName().substring(1) + ", d_"+ value.getName()+")";
					update = update + " m("+ value.getName().substring(1) + ", * )";
				}
			}
		}
		RHS = RHS + update;
		
		return "rl["+nb+"] : " + LHS + " => " + RHS + " .";
	}
	
	/**
	 * Get rewrite rule number
	 */
	
	public int getNb(){
		return nb;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.getGuard(), this.getOutput(), this.getMemory(), this.getInput());
	}

}
