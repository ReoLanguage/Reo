package nl.cwi.reo.runtime.java;

import java.util.concurrent.TimeoutException;

public interface InputPort<T> extends Port<T> {
	
//	public Object get() throws InterruptedException;

	public Object get(long timeout) throws TimeoutException;

	public Object getUninterruptibly();

	public Object resume() throws InterruptedException;
}