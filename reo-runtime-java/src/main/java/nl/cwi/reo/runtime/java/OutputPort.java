package nl.cwi.reo.runtime.java;

import java.util.concurrent.TimeoutException;

public interface OutputPort<T> extends Port<T> {
	
//	public void put(Object datum) throws InterruptedException;

	public void put(Object datum, long timeout) throws TimeoutException;

	public void putUninterruptibly(Object datum);

	public void resume() throws InterruptedException;
}