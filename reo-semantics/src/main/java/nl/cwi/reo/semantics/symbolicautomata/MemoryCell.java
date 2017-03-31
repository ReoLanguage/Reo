package nl.cwi.reo.semantics.symbolicautomata;

import java.util.Map;
import java.util.Objects;

import nl.cwi.reo.interpret.ports.Port;

public class MemoryCell implements Variable {
	
	private final int k;
	
	private final String type;
	
	private final boolean prime;
	
	public MemoryCell(int k, boolean prime) {
		this.k = k;
		this.prime = prime;
		this.type="";
	}

	public MemoryCell(int k, boolean prime, String type) {
		this.k = k;
		this.prime = prime;
		this.type=type;
	}
	
	public MemoryCell setType(String type){
		return new MemoryCell(k,prime,type);
	}
	
	public String getName() {
		return "q" + k;
	}

	public int getIndice(){
		return k;
	}
	
	public String getType(){
		return type;
	}
	
	public boolean hasPrime() {
		return prime;
	}
	
	@Override 
	public boolean equals(Object m){
		if(m instanceof MemoryCell){
			if(k==((MemoryCell) m).getIndice()&&type.equals(((MemoryCell) m).getType()))
				return true;
		}
		return false;
	}
	@Override
	public int hashCode() {
		return Objects.hash(this.k);
	}
	
	@Override
	public boolean hadOutputs() {
		return false;
	}
	
	public String toString(){
		if(prime)
			return getName()+"'";
		return getName();
	}

	@Override
	public Term rename(Map<Port, Port> links) {
		return this;
	}
}
