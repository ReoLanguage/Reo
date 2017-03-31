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
		for (Formula f : g)
			h.add(f.getGuard());
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
	public Formula QE() {
		List<Formula> existentialList = new ArrayList<Formula>();
		for(Formula f : g){
			Set<Term> variable = new HashSet<Term>();
			Map<Variable,Term> map = f.getAssignment();
			for(Port p : f.getInterface()){
				if(p.isHidden() && map.containsKey(new Node(p))){
					Term t = map.get(new Node(p));
					map.remove(new Node(p));
					for(Variable v : map.keySet()){
						if(map.get(v) instanceof Node && map.get(v).equals(new Node(p))){
							map.replace(v, t);
						}
					}
					map.remove(p);
					variable.add(new Node(p));
				}
			}
			List<Formula> formulaList = new ArrayList<Formula>();
			for(Variable p : map.keySet()){
				formulaList.add(new Equality(p,map.get(p)));
				if(p instanceof Node)
					formulaList.add(new Synchron(((Node) p).getPort()));
				if(map.get(p) instanceof Node)
					formulaList.add(new Synchron(((Node) map.get(p)).getPort()));
			}
			existentialList.add(new Existential(variable,new Conjunction(formulaList)));
			
		}
		return new Disjunction(existentialList);
	}

}
