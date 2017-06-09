package nl.cwi.reo.runtime;

public class MemCell<T> {
	private T m;
	
	public MemCell(T mem){
		this.m=mem;
	}
	
	public T getValue(){
		return m;
	}
	
	public void setValue(T mem){
		this.m = mem;
	}
}
