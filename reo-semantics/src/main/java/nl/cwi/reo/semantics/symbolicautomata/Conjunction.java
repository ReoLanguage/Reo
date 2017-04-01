package nl.cwi.reo.semantics.symbolicautomata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.util.Monitor;

public class Conjunction implements Formula {
	
	/**
	 * Flag for string template.
	 */
	public static final boolean conjunction = true;
	
	private final List<Formula> clauses;
	
	public Conjunction(List<Formula> clauses) {
		this.clauses = clauses;
	}
	
	public List<Formula> getFormula() {
		return clauses;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Formula getGuard() {
		List<Formula> h = new ArrayList<Formula>();
		for (Formula f : clauses)
			h.add(f.getGuard());
		return new Conjunction(h);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<Port, Term> getAssignment() {
		Map<Port, Term> assignment = new HashMap<Port, Term>();
		for (Formula f : clauses) {
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
		for (Formula f : clauses)
			h.add(f.rename(links));
		return new Conjunction(h);
	}

	@Override
	public Set<Port> getInterface() {
		Set<Port> P = new HashSet<Port>();
		for (Formula f : clauses)
			if(!(f instanceof Equality)&&(f.getInterface()!=null))
				P.addAll(f.getInterface());
		return P;
	}

	public String toString(){
		String s = clauses.get(0).toString();
		for(int i=1;i<clauses.size(); i++){
			s = s + " & " + clauses.get(i).toString();
		}
		return s;
	}
	
	@Override
	public @Nullable Formula evaluate(Scope s, Monitor m) {
		return this;
	}

	@Override
	public Disjunction DNF() {
		List<List<Formula>> c = new ArrayList<List<Formula>>();
		for (Formula f : clauses) {
			Formula g = f.DNF();
			if (g instanceof Disjunction) {
				c.add(((Disjunction) g).getClauses());
			} else {
				c.add(Arrays.asList(g));
			}
		}
		ClausesIterator iter = new ClausesIterator(c);
		List<Formula> clauses = new ArrayList<Formula>();
		while (iter.hasNext()) {
			List<Formula> list = new ArrayList<Formula>();
			List<Formula> tuple = iter.next();
			for (Formula f : tuple) {
				if (f instanceof Conjunction) {
					list.addAll(((Conjunction) f).getClauses());
				} else {
					list.add(f);
				}
			}
			clauses.add(new Conjunction(list));
		}
		return new Disjunction(clauses);
//		Queue<Formula> queue = new LinkedList<Formula>(clauses);
//
//		Formula dnf = queue.poll();
//		
//		while(!queue.isEmpty()){
//			if(dnf instanceof Conjunction){
//				List<Formula> newConjunction = new ArrayList<Formula>();
//				for(Formula g : queue){
//					if(g instanceof Conjunction){
//						queue.remove(g);
//						newConjunction.add(g);
//					}
//				}
//				queue.add(new Conjunction(newConjunction));
//				dnf=queue.poll();
//			}
//			if(dnf instanceof Disjunction){
//				Formula f = queue.poll();
//				List<Formula> disjunctFormula = new ArrayList<Formula>();
//				if(f instanceof Disjunction){
//					// TODO : optimize this part
//					for(Formula formulas : ((Disjunction) dnf).getClauses()){
//						if(formulas instanceof Conjunction){
//							for(Formula form : ((Disjunction) f).getClauses()){
//								if(form instanceof Conjunction){
//									List<Formula> c = new ArrayList<Formula>();
//									c.addAll(((Conjunction) formulas).getFormula());
//									c.addAll(((Conjunction) form).getFormula());
//									Set<Synchron> p = new HashSet<Synchron>();
//									if(new Conjunction(c).canSynchronize(p)!=null)
//										disjunctFormula.add(new Conjunction(c));
//								}
//							}
//						}
//						else
//							throw new UnsupportedOperationException();
//						
//					}
//				}
//				dnf=new Disjunction(disjunctFormula);
//			}
//		}
		
//		for(Formula f : g){
//			if(f instanceof Disjunction){
//				
//			}
//		}
//		return dnf;
	}
	
//	public Set<Synchron> canSynchronize(Set<Synchron> p){
//		boolean canSync=true;
//		for(Formula f : clauses){
//			if(f instanceof Synchron){
//				for(Synchron port : p){
//					if((port.getPort().getName().equals(((Synchron) f).getPort().getName()))){
//						if((port.isSync())&&!((Synchron)f).isSync()||!(port.isSync())&&((Synchron)f).isSync()){
//							return null;
//						}
//					}
//				}
//				p.add(((Synchron) f));
//			}
//			if(f instanceof Conjunction){
//				if(((Conjunction) f).canSynchronize(p)!=null)
//					p.addAll(((Conjunction) f).canSynchronize(p));
//				else
//					return null;
//			}
//			
//		}
//		if(canSync)
//			return p;
//		else
//			return null;
//	}

	public List<Formula> getClauses(){
		return clauses;
	}

	@Override
	public Formula NNF() {
		List<Formula> list = new ArrayList<Formula>();
		for (Formula f : clauses)
			list.add(f.NNF());
		return new Conjunction(list);
	}

	@Override
	public Formula QE() {
		List<Formula> list = new ArrayList<Formula>();
		for (Formula f : clauses)
			list.add(f.QE());
		return new Conjunction(list);
	}

	@Override
	public Formula Substitute(Term t, Variable x) {
		List<Formula> list = new ArrayList<Formula>();
		for (Formula f : clauses)
			list.add(f.Substitute(t, x));
		return new Conjunction(list);
	}

	@Override
	public Set<Variable> getFreeVariables() {
		Set<Variable> vars = new HashSet<Variable>();
		for (Formula f : clauses)
			vars.addAll(f.getFreeVariables());
		return vars;
	}

	@Override
	public Map<Variable, Integer> getEvaluation() {
		Map<Variable, Integer> map = new HashMap<Variable, Integer>();
		for (Formula f : clauses) {
			map.putAll(f.getEvaluation());
			if (f instanceof Equality) {
				Equality e = (Equality) f;
				if (e.getLHS() instanceof Variable && e.getRHS() instanceof Variable) {
					Integer p = map.get((Variable)e.getLHS());
					Integer q = map.get((Variable)e.getRHS());
					if (p != null && q == null) {
						map.put((Variable)e.getRHS(), p);
					} else if (q != null && p == null) {
						map.put((Variable)e.getLHS(), p);
					}
				}
			}
		}
		return map;
	}

}
