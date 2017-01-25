package nl.cwi.pr.runtime;

import java.util.concurrent.Semaphore;

public abstract class PublicPort extends PortImpl {
	public final Semaphore semaphore = new Semaphore(0);
	public volatile Handler handler;
	public volatile IO status;
}