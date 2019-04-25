package nl.cwi.reo.templates.maude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.values.StringValue;
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
	
	/** Print trace */
	static boolean printTrace = true;
	
	/** Rewrite rule number */
	private int nb;
	
	/** Left Hand Side of the rewrite rule */
	private Map<Variable, Term> lstate = new HashMap<>();
	
	/** Right Hand Side of the rewrite rule */	
	private Map<Variable, Term> rstate = new HashMap<>();

	/** Condition of the rewrite rule */	
	private Set<Formula> condition = new HashSet<>();

	/** Semiring value of the rewrite rule */	
	private Term semiring;

	/** Threshold of the rewrite rule */	
	private Term threshold;

	/** List of user defined functions */	
	private List<Function> functions = new ArrayList<>();

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
		for(PortVariable p : getOutput().keySet()){
			lstate.put(p, new NullValue());
		}
		rstate.putAll(getMemory());
		setFunctions();
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
				if(p.isInput()){
					f = new Equality(p,new NonNullValue());
					rstate.put(p, new NullValue());
				}
				else
					f = new Equality(p,new NullValue());
				formulaToString(f);
			}
			else if (e.getLHS() instanceof MemoryVariable && e.getRHS() instanceof NullValue) {
				MemoryVariable m = (MemoryVariable) e.getLHS();
				f = new Equality(m,new NonNullValue());
				formulaToString(f);
				MemoryVariable mP = new MemoryVariable( m.getName(), true, m.getTypeTag());
				if(! getMemory().keySet().stream().anyMatch(o -> o.getName().equals(m.getName()))) {
					rstate.put(mP, m);
				}
			}
			else if (e.getLHS() instanceof Function) {
					condition.add(f);
			}
		}
		else if(f instanceof Equality){
			Term lhs = ((Equality) f).getLHS();
			Term rhs = ((Equality) f).getRHS();
			if(lhs instanceof Variable) {
				if(lhs instanceof PortVariable && ((PortVariable) lhs).isInput() ||
				   lhs instanceof MemoryVariable && !((MemoryVariable) lhs).hasPrime()) {
					lstate.put((Variable)lhs, lhs);
					if(rhs instanceof MemoryVariable && !((MemoryVariable) rhs).hasPrime() ||
					   rhs instanceof PortVariable && ((PortVariable) rhs).isInput() ||
					   rhs instanceof Function) {
						condition.add(f);
						lstate.put((Variable)rhs, rhs);
					}
				}
				else
					lstate.put((Variable)lhs, rhs);
			}
			if(lhs instanceof Function)
				functions.add((Function)lhs);
			if(rhs instanceof Function)
				functions.add((Function)rhs);	
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
		for(Variable key : lstate.keySet()){
			LHS = LHS + getVarString(key,lstate.get(key));
		}		
				
		for(Variable key : rstate.keySet()){
			RHS  = RHS + getVarString(key, rstate.get(key));
			if(key instanceof PortVariable)
				trace = trace + " a(\""+ key.getName() + "\") ";
		}
		
/*		if(semiring!=null && threshold !=null){
			
			String sem = semiringToStr(semiring);
			semCounter=0;
			String th = thresholdToStr(threshold);
			semCounter=0;
			String thState= thresholdState(threshold);
			trace=trace + "sv("+sem+")}) ";
			
			return "crl["+nb+"] : " + LHS + thState + " trace(sl) " + " => " + trace + RHS + thState + " if( "+ th +" <= "+ sem + ") .";			
		}
		semCounter=0;*/
		
		if(LHS.contentEquals(RHS)) {
			return null;
		}
		
		if(condition.isEmpty())
			return "rl["+nb+"] : " + LHS + " => " + RHS + " .";
		else
			return "crl["+nb+"] : " + LHS + " => " + RHS + " if(" + getCondition() + ") .";
	}
	
	
	public String getCondition() {
		String s = "";
		
		for(Formula f : condition) {
			if (f instanceof Negation && ((Negation) f).getFormula() instanceof Equality) {
				Equality e = (Equality) ((Negation) f).getFormula();
				s = s + getDataString(e.getLHS()) + " =/= " + getDataString(e.getRHS()) + " and " ;	
			}
			else if(f instanceof Equality) {
				Equality e = ((Equality) f);
				s = s + getDataString(e.getLHS()) + " == " + getDataString(e.getRHS()) + " and " ;	
			}
		}
		s = s.substring(0, s.length()-5);
		
		return s;
	}
	
	public String getDataPortString(PortVariable p){
		String s = "d_" + p.getName();	
		return s;
	}
	
	public String getDataMemString(MemoryVariable m){
		String s = "d_m" + m.getName().substring(1);	
		return s;
	}
	
	public String getDataFunctString(Function f){
		String s = f.getName().replace("\"", "") + "(";
		for(Term t : f.getArgs()){
			s = s + getDataString(t) + ", ";
		}
		return s.substring(0, s.length()-2)+")";
	}
	
	public String getDataString(Term t){
		String s = "";
		if(t instanceof PortVariable)
			s = s + getDataPortString((PortVariable)t);
		if(t instanceof MemoryVariable)
			s = s + getDataMemString((MemoryVariable)t);
		if(t instanceof Function)
			s = s + getDataFunctString((Function)t);
		if(t instanceof NullValue)
			s = s + "*";
		if(t instanceof Constant)
			s = s + getConstantString((Constant) t);
			
		return s;
	}
	
	public String getConstantString(Constant c) {
		String s ="";
/*		if(c.getValue() instanceof StringValue) {
			s = c.getValue().toString().replace("\"", "");
					
		}
		else*/
			s = c.toString();
			
		return s;
		
	}
	
	
	public String getMemString(MemoryVariable m, Term t){
		String s = "";
		if(t instanceof NonNullValue)
			s = " m("+m.getName().substring(1)+", d_m"+ m.getName().substring(1) + ")";
		else
			s = " m("+m.getName().substring(1)+", "+ getDataString(t) + ")";
		return s;
	}
	
	public String getVarString( Variable t1, Term t2 ){
		if(t1 instanceof PortVariable)
			return getPortString((PortVariable) t1, t2);
		if(t1 instanceof MemoryVariable)
			return getMemString((MemoryVariable) t1, t2);
		return "";
	}
	
	public String getPortString(PortVariable p, Term t){
		String s = "";
		if(t instanceof NonNullValue)
			s = " p(\""+p.getName()+"\", "+ "d_"+p.getName() + ")";
		else
			s = " p(\""+p.getName()+"\", "+ getDataString(t) + ")";
		return s;
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
	
	public void setFunctions(){
		for(Term t : lstate.values()){
			if(t instanceof Function)
				functions.add((Function) t);
		}
		for(Term t : rstate.values()){
			if(t instanceof Function)
				functions.add((Function) t);
		}
	}
	
	public List<Function> getFunction(){
		
		return functions;
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
