package nl.cwi.pr.runtime;

import nl.cwi.pr.runtime.api.Port;

public class PortImpl implements Port {
	public volatile Object buffer;
}