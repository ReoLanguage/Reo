package nl.cwi.pr.runtime.api;

import java.util.concurrent.TimeoutException;

public interface InputPort extends Port {
	
	public Object get() throws InterruptedException;

	public Object get(long timeout) throws TimeoutException;

	public Object getUninterruptibly();

	public Object resume() throws InterruptedException;
}