package nl.cwi.reo.semantics.symbolicautomata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.util.Monitor;

public class Conjunction implements Formula {
	
	private final List<Formula> g;
	
	public Conjunction(List<Formula> g) {
		this.g = g;
	}
	
	public List<Formula> getFormula(){
		return g;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Formula getGuard() {
		List<Formula> h = new ArrayList<Formula>();
		for (Formula f : g)
			h.add(f.getGuard());
		return new Conjunction(h);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<Port, Term> getAssignment() {
		Map<Port, Term> assignment = new HashMap<Port, Term>();
		for (Formula f : g) {
			Map<Port, Term> assignment1 = f.getAssignment();
			if (assignment1 == null)
				return null;
			for (Map.Entry<Port, Term> pair : assignment1.entrySet())
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
		return new Conjunction(h);
	}

	@Override
	public Set<Port> getInterface() {
		Set<Port> P = new HashSet<Port>();
		for (Formula f : g)
			if(!(f instanceof Equality)&&(f.getInterface()!=null))
				P.addAll(f.getInterface());
		return P;
	}

	public String toString(){
		String s = "(" + g.get(0).toString() +")";
		for(int i=1;i<g.size(); i++){
			s = s + "AND" + "(" + g.get(i).toString() + ")";
		}
		return s;
	}
	
	@Override
	public @Nullable Formula evaluate(Scope s, Monitor m) {
		return this;
	}

	@Override
	public Formula DNF() {
		Queue<Formula> queue = new LinkedList<Formula>(g);

		Formula dnf = queue.poll();
		
		while(!queue.isEmpty()){
			if(dnf instanceof Conjunction){
				List<Formula> newConjunction = new ArrayList<Formula>();
				for(Formula g : queue){
					if(g instanceof Conjunction){
						queue.remove(g);
						newConjunction.add(g);
					}
				}
				queue.add(new Conjunction(newConjunction));
				dnf=queue.poll();
			}
			if(dnf instanceof Disjunction){
				Formula f = queue.poll();
				List<Formula> disjunctFormula = new ArrayList<Formula>();
				if(f instanceof Disjunction){
					// TODO : optimize this part
					for(Formula formulas : ((Disjunction) dnf).getClauses()){
						if(formulas instanceof Conjunction){
							for(Formula form : ((Disjunction) f).getClauses()){
								if(form instanceof Conjunction){
									List<Formula> c = new ArrayList<Formula>();
									c.addAll(((Conjunction) formulas).getFormula());
									c.addAll(((Conjunction) form).getFormula());
									Set<Synchron> p = new HashSet<Synchron>();
									if(new Conjunction(c).canSynchronize(p)!=null)
										disjunctFormula.add(new Conjunction(c));
								}
							}
						}
						else
							throw new UnsupportedOperationException();
						
					}
				}
				dnf=new Disjunction(disjunctFormula);
			}
		}
		
//		for(Formula f : g){
//			if(f instanceof Disjunction){
//				
//			}
//		}
		return dnf;
	}
	
	public Set<Synchron> canSynchronize(Set<Synchron> p){
		boolean canSync=true;
		for(Formula f : g){
			if(f instanceof Synchron){
				for(Synchron port : p){
					if((port.getPort().getName().equals(((Synchron) f).getPort().getName()))){
						if((port.isSync())&&!((Synchron)f).isSync()||!(port.isSync())&&((Synchron)f).isSync()){
							return null;
						}
					}
				}
				p.add(((Synchron) f));
			}
			if(f instanceof Conjunction){
				if(((Conjunction) f).canSynchronize(p)!=null)
					p.addAll(((Conjunction) f).canSynchronize(p));
				else
					return null;
			}
			
		}
		if(canSync)
			return p;
		else
			return null;
	}

	@Override
	public Formula NNF(boolean isNegative) {
		List<Formula> h = new ArrayList<Formula>();
		for (Formula f : g)
			h.add(f.NNF(isNegative));
		if(isNegative){
			return new Disjunction(h);
		}
		else{
			return new Conjunction(h);			
		}
	
	}

	@Override
	public Formula QE() {
		// TODO Auto-generated method stub
		return null;
	}

}
