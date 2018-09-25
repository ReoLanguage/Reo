package nl.cwi.reo.templates.maude;

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

	/** Semiring value of the rewrite rule */	
	private Term semiring;

	/** Threshold of the rewrite rule */	
	private Term threshold;

	/** Rewrite rule counter */
	int semCounter=0;
	
	/** Trace */
	private String trace=" trace(sl ; {";
	
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
	public MaudeTransition(Formula guard, Map<PortVariable, Term> output, Map<MemoryVariable, Term> memory) {
		super(guard, output, memory);
		nb=++counter;
		formulaToString(getGuard());
		rstate.putAll(getOutput());
		rstate.putAll(getMemory());
		
	}
	
	/**
	 * From a guard to a rewrite rule
	 */
	private void formulaToString(Formula f){
		if(f instanceof Conjunction){
			for(Formula g : ((Conjunction) f).getClauses()){
				formulaToString(g);
			}
		}
		else if (f instanceof Negation && ((Negation) f).getFormula() instanceof Equality) {
			Equality e = (Equality) ((Negation) f).getFormula();
			if (e.getLHS() instanceof PortVariable && e.getRHS() instanceof NullValue) {
				PortVariable p = (PortVariable) e.getLHS();
				if(p.isInput())
					f = new Equality(p,new NonNullValue());
				else
					f = new Equality(p,new NullValue());
				formulaToString(f);
			}
			else if (e.getLHS() instanceof MemoryVariable && e.getRHS() instanceof NullValue) {
				MemoryVariable m = (MemoryVariable) e.getLHS();
				f = new Equality(m,new NonNullValue());
				formulaToString(f);
			}
		}
		else if(f instanceof Equality){
			Term lhs = ((Equality) f).getLHS();
			Term rhs = ((Equality) f).getRHS();
			if(lhs instanceof PortVariable)
				lstate.put((PortVariable)lhs, rhs);
			if(lhs instanceof MemoryVariable)
				lstate.put((MemoryVariable)lhs, rhs);
		}
		else  if(f instanceof Relation){
			if(((Relation) f).getName().contains("Semiring")){
				semiring = ((Relation) f).getArgs().get(0);
				threshold = ((Relation) f).getArgs().get(1);
			}
		}
	}

	

	/**
	 * Write the rewrite rule as a string
	 */
	public String getRewString() {
		String RHS = "";
		String LHS = "";
		
		//Convert lstate to string :
		for(Variable v : lstate.keySet()){
			if(v instanceof PortVariable){
				LHS = LHS + " p(\"" + v.getName() + "\",";
				if(lstate.get(v) instanceof NullValue){
					LHS = LHS + " *) ";		
					if(!rstate.containsKey(v)){
						RHS = RHS + "p(\"" + v.getName() + "\", *) ";
					}
				}
				else if(lstate.get(v) instanceof NonNullValue){
					LHS = LHS + "d_" + v.getName()+")";					
				}
			}
			if(v instanceof MemoryVariable){
				LHS = LHS + " m("+ v.getName().substring(1) + ",";
				if(lstate.get(v) instanceof NullValue){
					LHS = LHS + " *) ";
					MemoryVariable v_prime = new MemoryVariable(v.getName(),!((MemoryVariable) v).hasPrime(),v.getTypeTag());
					if(!rstate.containsKey(v) && !rstate.containsKey(v_prime)){
						RHS = RHS + "m("+ v.getName().substring(1) + ", *) ";
					}
				}
				else if(lstate.get(v) instanceof NonNullValue){
					LHS = LHS + "d_" + v.getName()+")";					
				}
			}
		}
		
		//Convert rstate to string :
		String update = "";
		
		Map<Variable,Term> endRule = new HashMap<>();
		for(Variable key : rstate.keySet()){
			if(key instanceof PortVariable){
//				MemoryVariable v_prime = new MemoryVariable(v.getName(),!((MemoryVariable) v).hasPrime(),v.getTypeTag());
				if(!lstate.containsKey(key)){
					LHS = LHS + " p(\"" + key.getName() + "\", *) ";
				}
				if(rstate.get(key) instanceof PortVariable){
					PortVariable value = (PortVariable)rstate.get(key);
					update = update + " p(\"" + key.getName() + "\", d_"+ value.getName()+")";
//					update = update + " p(\"" + value.getName() + "\", * )";
					endRule.put((PortVariable)rstate.get(key), new NullValue());
					trace = trace + " a(\""+ key.getName() + "\") ";
				}
				if(rstate.get(key) instanceof MemoryVariable){
					MemoryVariable value = (MemoryVariable)rstate.get(key);
					update = update + " p(\"" + key.getName() + "\", d_"+ value.getName()+")";
//					update = update + " m( "+ value.getName() + ", * )";
					MemoryVariable m_prime = new MemoryVariable(((MemoryVariable)rstate.get(key)).getName(),!((MemoryVariable)rstate.get(key)).hasPrime(),((MemoryVariable)rstate.get(key)).getTypeTag());
					if(!rstate.containsKey(m_prime))
						endRule.put((MemoryVariable)rstate.get(key), new NullValue());
					trace = trace + " a(\""+ key.getName() + "\") ";

				}
			}
			if(key instanceof MemoryVariable){			
				MemoryVariable m_prime = new MemoryVariable(key.getName(),!((MemoryVariable) key).hasPrime(),key.getTypeTag());
				if(!rstate.containsKey(key) && !rstate.containsKey(m_prime)){
					RHS = RHS + "m("+ key.getName().substring(1) + ", *) ";
				}
				if(rstate.get(key) instanceof PortVariable){
					PortVariable value = (PortVariable)rstate.get(key);
					update = update + " m("+ key.getName().substring(1) + ", d_"+ value.getName()+")";
//					update = update + " p(\"" + value.getName() + "\", * )";
					endRule.put((PortVariable)rstate.get(key), new NullValue());
				}
				if(rstate.get(key) instanceof MemoryVariable){
					MemoryVariable value = (MemoryVariable)rstate.get(key);
					update = update + " m("+ key.getName().substring(1) + ", d_"+ value.getName()+")";
					MemoryVariable _m = new MemoryVariable(((MemoryVariable)rstate.get(key)).getName(),!((MemoryVariable)rstate.get(key)).hasPrime(),((MemoryVariable)rstate.get(key)).getTypeTag());
					if(!rstate.containsKey(_m))
						endRule.put((MemoryVariable)rstate.get(key), new NullValue());
//					endRule.put((MemoryVariable)rstate.get(key), new NullValue());
//					update = update + " m("+ value.getName() + ", * )";
				}
				if(key instanceof MemoryVariable && rstate.get(key) instanceof NullValue){
					update = update + " m("+ key.getName().substring(1) + ", * )";
				}
			}
			
		}
		
		for(Variable var : endRule.keySet()){
			if(var instanceof MemoryVariable){
				update = update + " m("+ var.getName().substring(1) + ", * )";
			}
			if(var instanceof PortVariable){
				update = update + " p( "+ var.getName() + ", * )";
			}
		}
		RHS = RHS + update;
		
		if(semiring!=null && threshold !=null){
			
			String sem = semiringToStr(semiring);
			semCounter=0;
			String th = thresholdToStr(threshold);
			semCounter=0;
			String thState= thresholdState(threshold);
			trace=trace + "sv("+sem+")}) ";
			
			return "crl["+nb+"] : " + LHS + thState + " trace(sl) " + " => " + trace + RHS + thState + " if( "+ th +" <= "+ sem + ") .";			
		}
		semCounter=0;
		
		return "rl["+nb+"] : " + LHS + " => " + RHS + " .";
	}
	
	public String thresholdToStr(Term t){
		String s = "";
		
		if(t instanceof Function){
			if(((Function) t).getName().contains("lex")){
				s = s + "pairLex(";
				for(Term _t : ((Function) t).getArgs()){
					s=s+thresholdToStr(_t)+" , ";
				}
				s=s.substring(0, s.length()-3)+")";
			}
			else{
				s = s + "(";
				for(Term _t : ((Function) t).getArgs()){
					s=s+thresholdToStr(_t)+" * ";
				}
				s=s.substring(0, s.length()-3)+")";
			}
		}
		
		else if(t instanceof Constant){
			s= s+ "t"+semCounter++;
		}
		
		return s;
	}
	
	public String thresholdState(Term t){
		String s = "";
		
		if(t instanceof Function){
			for(Term _t : ((Function) t).getArgs()){
				s=s+thresholdState(_t)+" ";
			}
		}
		
		else if(t instanceof Constant){
			s= s+ "th(\"t"+semCounter +"\",t"+semCounter +")";
			semCounter++;
		}
		return s;
	}
	
	public String semiringToStr(Term t){
		String s = "";
		
		if(t instanceof Function){
			if(((Function) t).getName().contains("lex")){
				s = s + "pairLex(";
				for(Term _t : ((Function) t).getArgs()){
					s=s+semiringToStr(_t)+
							" , ";
				}
				s=s.substring(0, s.length()-3)+")";
			}
			else{
				s = s + "(";
				for(Term _t : ((Function) t).getArgs()){
					s=s+semiringToStr(_t)+
							" * ";
				}
				s=s.substring(0, s.length()-3)+")";
			}
			
		}
		
		else if(t instanceof Constant){
			s= s+ "ws("+t.toString()+")";
		}
		
		return s;
	}
	
	/**
	 * Get rewrite rule number
	 */
	
	public int getNb(){
		return nb;
	}
	
	/**
	 * Get threshold value
	 */
	
	public Term getTh(){
		return threshold;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.getGuard(), this.getOutput(), this.getMemory());
	}

}
