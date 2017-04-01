package nl.cwi.reo.semantics.symbolicautomata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.util.Monitor;

public class Disjunction implements Formula {
	
	/**
	 * Flag for string template.
	 */
	public static final boolean disjunction = true;
	
	private final List<Formula> g;
	
	public Disjunction(List<Formula> g) {
		this.g = g;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Formula getGuard() {
		List<Formula> h = new ArrayList<Formula>();
		for (Formula f : g){
			if(f.getGuard() instanceof BooleanValue)
				if(((BooleanValue) f.getGuard()).getValue()){
					return new BooleanValue(true);
				}
//					return new BooleanValue(true);
			else
				h.add(f.getGuard());
		}
		if(h.size()==1)
			return h.get(0);
		return new Disjunction(h);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<Variable, Term> getAssignment() {
		Map<Variable, Term> assignment = new HashMap<Variable, Term>();
		for (Formula f : g) {
			Map<Variable, Term> assignment1 = f.getAssignment();
			if (assignment1 == null)
				return null;
			for (Map.Entry<Variable, Term> pair : assignment1.entrySet())
				if (assignment.put(pair.getKey(), pair.getValue()) != null)
					return null;
		}
		return assignment;
	}

	@Override
	public Formula rename(Map<Port, Port> links) {
		List<Formula> h = new ArrayList<Formula>();
		for (Formula f : g)
			h.add(f.rename(links));
		return new Disjunction(h);
	}

	@Override
	public Set<Port> getInterface() {
		Set<Port> P = new HashSet<Port>();
		for (Formula f : g)
			if(f instanceof Disjunction || f instanceof Conjunction || f instanceof Existential)
				P.addAll(f.getInterface());
		return P;
	}

	@Override
	public @Nullable Formula evaluate(Scope s, Monitor m) {
		return this;
	}
	
	public String toString(){
		String s = "[" + g.get(0).toString() +"]";
		for(int i=1;i<g.size(); i++){
			s = s + "OR" + "[" + g.get(i).toString() + "]";
		}
		return s;
	}

	public List<Formula> getClauses(){
		return g;
	}
	
	@Override
	public Disjunction DNF() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Formula NNF(boolean isNegative) {
		List<Formula> h = new ArrayList<Formula>();
		for (Formula f : g)
			h.add(f.NNF(isNegative));
		if(isNegative){
			return new Conjunction(h);
		}
		else{
			return new Disjunction(h);			
		}
	}

	@Override
	public Formula QE(Set<Term> quantifiers) {
		List<Formula> existentialList = new ArrayList<Formula>();
		for(Formula f : g){
			existentialList.add(f.QE(quantifiers));
		}
		return new Disjunction(existentialList);
		
	}

}
