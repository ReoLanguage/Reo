package nl.cwi.pr.runtime.api;

import java.util.concurrent.TimeoutException;

public interface OutputPort extends Port {
	
	public void put(Object datum) throws InterruptedException;

	public void put(Object datum, long timeout) throws TimeoutException;

	public void putUninterruptibly(Object datum);

	public void resume() throws InterruptedException;
}