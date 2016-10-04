package nl.cwi.reo.runtime.java;

import java.util.ArrayList;
import java.util.List;

/**
 * @deprecated
 */
public class PortBuffer<T> implements Port<T> {
	private Component prod;
	private Component cons;
	private volatile List<T> put;
	private volatile boolean get;
	private final int k;
	public PortBuffer(int c) { 
		if (c < 0) throw new IllegalArgumentException();
		k = c + 1; 
		put = new ArrayList<T>(); 
	}
	public void setProducer(Component p) { prod = p; }
	public void setConsumer(Component c) { cons = c; }	
	public void setGet() { get = true; }
	public boolean hasPut() { return !put.isEmpty(); }
	public T take() { synchronized (this) { return put.remove(0); } }
	public void setPut(T datum) { synchronized (this) { put.add(datum); } }
	public boolean hasGet() { return put.size() < k; }
	public void activateProducer() { prod.activate(); }
	public void activateConsumer() { cons.activate(); }
	public void put(T datum) {
		if (datum == null) throw new NullPointerException();
		while (put.size() == k) { }
		synchronized (this) { put.add(datum); }
		cons.activate();
		while (!get && put.size() == k) {}
	}
	public T get() {
		get = true;
		prod.activate();
		while (put.isEmpty()) { }
		T datum;
		synchronized (this) { datum = put.remove(0); }
		get = false;
		prod.activate();
		return datum;
	}
}