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
	
	/**
	 * Flag for string template.
	 */
	public static final boolean conjunction = true;
	
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
		boolean value = true;
		for (Formula f : g){
			if(f.getGuard() instanceof BooleanValue && value){
				if(!((BooleanValue) f.getGuard()).getValue())
					value=false;
			}
			else
				h.add(f.getGuard());
		}
		if(value && h.isEmpty())
			return new BooleanValue(true);
		else{
			if(h.size()==1)
				return h.get(0);
			return new Conjunction(h);
		}
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

	public List<Formula> getClauses(){
		return g;
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
	public Formula QE(Set<Term> quantifiers) {
		List<Formula> formulaList = new ArrayList<Formula>(Arrays.asList(this.getGuard()));
		Set<Term> variable = new HashSet<Term>();
		Map<Variable,Term> map = this.getAssignment();
		
		for(Port p : this.getInterface()){
			if(quantifiers.contains(new Node(p))){
				if(map.containsKey(new Node(p))){
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
				if(map.containsValue(new Node(p))){
					Set<Variable> variableToRemove = new HashSet<Variable>();
					for(Variable v :map.keySet()){
						if(map.get(v).equals(new Node(p))){
							variableToRemove.add(v);
							variable.add(new Node(p));
						}
					}
					for(Variable v : variableToRemove){
						map.remove(v);
					}
				}
			}
		}

		for(Variable p : map.keySet()){
			formulaList.add(new Equality(p,map.get(p)));
			if(p instanceof Node)
				formulaList.add(new Synchron(((Node) p).getPort()));
			if(map.get(p) instanceof Node)
				formulaList.add(new Synchron(((Node) map.get(p)).getPort()));
		}

		if(formulaList.size()==1)
			return formulaList.get(0);
		return new Conjunction(formulaList);
		
	}

}
