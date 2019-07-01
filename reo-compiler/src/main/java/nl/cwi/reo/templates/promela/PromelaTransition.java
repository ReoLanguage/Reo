 package nl.cwi.reo.templates.promela;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

public final class PromelaTransition extends Transition{

	String RHS = "";
	String LHS = "";
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
	public PromelaTransition(Formula guard,  Map<Variable, Term> update, Formula constraint) {
		super(guard, update, constraint);
		for(Port p : getInterface())
			if(p.getName().substring(0, 1).contains("$")) {
				m.put(p, p.rename("np"+p.getName().substring(1,p.getName().length())));
			}
	}
	

	@Override
	public Formula getGuard() {	
		Formula guard = super.getGuard();
		guard = guard.rename(m);
		return guard;
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
			if ((e.getLHS() instanceof PortVariable || e.getLHS() instanceof MemoryVariable) && e.getRHS() instanceof NullValue) {
				f = new Equality(e.getLHS(),new NonNullValue());
				formulaToString(f);
			}
		}
		else if(f instanceof Equality){
			Term lhs = ((Equality) f).getLHS();
			Term rhs = ((Equality) f).getRHS();
			if(lhs instanceof PortVariable && rhs instanceof NonNullValue) {
				if(((PortVariable)lhs).isInput()) {
					LHS =LHS + "&& full("+((PortVariable)lhs).getName()+".data) ";
					RHS = "take("+((PortVariable)lhs).getName()+",_"+((PortVariable)lhs).getName()+"); " + RHS;
				}
			}
			if(lhs instanceof MemoryVariable) {
				if( rhs instanceof NonNullValue)
					LHS =LHS + "&& full("+((MemoryVariable)lhs).getName()+") ";
				if(rhs instanceof NullValue)
					LHS =LHS + "&& empty("+((MemoryVariable)lhs).getName()+") ";
			}
		}
	}
	
	public String getRenamedPort(PortVariable p) {
		return m.get(p.getPort()).getName();
	}
	
	/**
	 * Write the rewrite rule as a string
	 */
	public String getRewString() {

		for(PortVariable port : getOutput().keySet()){
			Term t = getOutput().get(port);
			LHS = LHS + "&& full("+getRenamedPort(port)+".trig) ";
			if(t instanceof PortVariable) {
				RHS = RHS + "put("+ getRenamedPort(port) + ",_" + getRenamedPort((PortVariable) t) + "); ";
			}
			else if (t instanceof MemoryVariable) {
				RHS = RHS + "put("+getRenamedPort(port) + ",_" + ((MemoryVariable) t).getName() + "); ";
			}
			else if(t instanceof Function) {
				String s = getDataFunctString((Function)t);
				RHS = RHS + s.substring(0,s.length()-1)+ ", _" + getRenamedPort(port) +"); put("+ getRenamedPort(port)+", _"+getRenamedPort(port)+") ;";
			}
		}		
		for(MemoryVariable memory : getMemory().keySet()){
			Term t = getMemory().get(memory);
			if(t instanceof PortVariable)
				RHS = RHS + memory.getName() + "!_" + getRenamedPort((PortVariable) t) + "; ";
			else if (t instanceof NullValue)
				RHS = memory.getName() + "?_" + memory.getName()+ "; " + RHS ;
			else if (t instanceof MemoryVariable)
				RHS = RHS + memory.getName() + "!_" + ((MemoryVariable) t).getName()+ "; ";
			else if(t instanceof Function) {
				String s = getDataFunctString((Function)t);
				RHS = RHS + s.substring(0,s.length()-1)+ ", _" + memory.getName() +"); "+memory.getName()+"!_"+memory.getName()+"; ";
			}

		}		
		formulaToString(getGuard());
		
		if(LHS.contentEquals(RHS)) {
			return null;
		}
		
		return  LHS.substring(2,LHS.length()) + " -> " + RHS + " ";
	}
	
	public String getDataPortString(PortVariable p){
		String s = "_" + getRenamedPort(p);	
		return s;
	}
	
	public String getDataMemString(MemoryVariable m){
		String s = "_" + m.getName();	
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
		s = c.toString();
			
		return s;
		
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
