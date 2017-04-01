package nl.cwi.reo.semantics.symbolicautomata;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.ports.PortType;
import nl.cwi.reo.util.Monitor;

public class Equality implements Formula {
	
	/**
	 * Flag for string template.
	 */
	public static final boolean equality = true;

	private final Term t1;

	private final Term t2;

	public Equality(Term t1, Term t2) {
		this.t1 = t1;
		this.t2 = t2;
	}

	@Override
	public Formula getGuard() {
		if (t1 instanceof Node && t2 instanceof Node) {
			if (((Node) t1).getPort().getType() != PortType.OUT)
				if (((Node) t2).getPort().getType() != PortType.OUT)
					return this;
		}
		if (t1 instanceof MemoryCell && t2 instanceof Constant) {
			return this;
		}
		return new BooleanValue(true);
	}

	@Override
	public Map<Variable, Term> getAssignment() {
		/*
		 * MemoryCell 
		 */
		Map<Variable, Term> map = new HashMap<Variable, Term>();
//		Port p=(t1 instanceof Node && !t2.hadOutputs())?((Node) t1).getPort():(((t2 instanceof Node && !t1.hadOutputs()))?null:((Node) t2).getPort());
		Term t = null;
		if(t1 instanceof Node){
			Port p = ((Node) t1).getPort();
			if(p.getType() == PortType.OUT){
				if(t2 instanceof MemoryCell && ((MemoryCell) t2).hasPrime()){
					//Look into a value that satisfy the formula
				}
				if(t2 instanceof MemoryCell && !((MemoryCell) t2).hasPrime()){
					map.put(new Node(p), ((MemoryCell) t2).setType(p.getTypeTag().toString()));
				}
				if(t2 instanceof Constant){
					map.put(new Node(p), t2);
				}
				if(t2 instanceof Node && ((Node) t2).getPort().getType() == PortType.IN){
					map.put(new Node(p), t2);
				}
				if(t2 instanceof Node && ((Node) t2).getPort().getType() == PortType.OUT){
					//Look into a value that satisfy the formula
				}
				return map;
			}
			if(p.getType() == PortType.IN){
				if(t2 instanceof MemoryCell && ((MemoryCell) t2).hasPrime()){
					map.put(((MemoryCell) t2).setType(p.getTypeTag().toString()), new Node(p));
				}
				if(t2 instanceof MemoryCell && !((MemoryCell) t2).hasPrime()){	
					//Look into a value that satisfy the formula
				}
				if(t2 instanceof Constant){
					map.put(new Node(p), t2);
				}
				if(t2 instanceof Node && ((Node) t2).getPort().getType() == PortType.IN){
					//Look into a value that satisfy the formula
				}
				if(t2 instanceof Node && ((Node) t2).getPort().getType() == PortType.OUT){
					map.put((Node)t2, new Node(p));
				}
				return map;
			}
			if(p.getType() == PortType.NONE){
				throw new UnsupportedOperationException();
			}
			return map;
		}
		else if(t1 instanceof MemoryCell){
			if(t2 instanceof Node){
				return (new Equality(t2,t1)).getAssignment();
			}
			if(t2 instanceof Constant){
//				map.put((MemoryCell)t1, t2);
			}
			return map;
		}
		else if(t1 instanceof Constant){
			if(t2 instanceof Node){
				return (new Equality(t2,t1)).getAssignment();
			}
			return map;
		}
		return map;
		
//		return new Equality(t2,t1).getAssignment();
//		if (t1 instanceof Node && !t2.hadOutputs()) {
//			p = ((Node) t1).getPort();
//			t = t2;
//		}
//		if(t2 instanceof Node && !t1.hadOutputs()) {
//			p = ((Node) t2).getPort();
//			t = t1;
//		}
//		
//		if(p!=null && t!=null){
//			if (p.getType() != PortType.OUT)
//				if(t2 instanceof MemoryCell && ((MemoryCell) t2).hasPrime())
//					map.put(p, ((MemoryCell) t2).setType(p.getTypeTag().toString()));
//			else{
//				if(t2 instanceof MemoryCell && ((MemoryCell) t2).hasPrime())
//					map.put(p, ((MemoryCell) t2).setType(p.getTypeTag().toString()));	
//			}
//		}
//			if (p.getType() != PortType.OUT)
//				if(t1 instanceof MemoryCell)
//					map.put(p, ((MemoryCell) t1).setType(p.getTypeTag().toString()));
//			if (p.getType() == PortType.OUT)
//				if(t1 instanceof MemoryCell && ((MemoryCell) t1).hasPrime())
//					map.put(p, ((MemoryCell) t1).setType(p.getTypeTag().toString()));
	}

	@Override
	public Formula rename(Map<Port, Port> links) {
		Term s1 = t1;
		if (t1 instanceof Node) {
			Port b = links.get(((Node) t1).getPort());
			if (b != null)
				s1 = new Node(b);
		}

		Term s2 = t2;
		if (t2 instanceof Node) {
			Port b = links.get(((Node) t2).getPort());
			if (b != null)
				s2 = new Node(b);
		}
		return new Equality(s1, s2);
	}

	@Override
	public Set<Port> getInterface() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String toString(){
		return "(" + t1.toString() + "=" + t2.toString() + ")";
	}

	@Override
	public @Nullable Formula evaluate(Scope s, Monitor m) {
		return this;
	}

	public Term getT1(){
		return t1;
	}
	public Term getT2(){
		return t2;
	}
	
	@Override
	public Formula DNF() {
		return this;
	}

	@Override
	public Formula NNF(boolean isNegative) {
		if(isNegative)
			return new Negation(this);
		else
			return this;
	}

	@Override
	public Formula QE(Set<Term> quantifiers) {
		// TODO Auto-generated method stub
		return null;
	}

}
