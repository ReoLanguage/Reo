package nl.cwi.reo.runtime.java;

import java.util.ArrayList;

public class PortFIFOk<T> implements Port<T> {
	private Component prod;
	private Component cons;
	private volatile ArrayList<T> put;
	private int k;
	public int empty = 0;
	public PortFIFOk(int c) { put = new ArrayList<T>(c); k = c; }
	public void setProducer(Component p) { prod = p; }
	public void setConsumer(Component c) { cons = c; }	
	public void setGet() { }
	public void setPut(T datum) { put.add(datum); }
	public boolean canPut() { return put.size() < k; }
	public boolean canGet() { return !put.isEmpty(); }
	public void activateProducer() { prod.activate(); }
	public void activateConsumer() { cons.activate(); }
	public void put(T datum) {
		if (datum == null) throw new NullPointerException();
		while (put.size() >= k) { }
		synchronized (this) { put.add(datum); }
		cons.activate();
	}
	public T get() {
		prod.activate();
		while (put.isEmpty()) { empty++; }
		T datum;
		synchronized (this) { datum = put.remove(0); }
		prod.activate();
		return datum;
	}
}