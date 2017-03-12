package nl.cwi.reo.interpret;

import nl.cwi.reo.interpret.connectors.ReoConnector;
import nl.cwi.reo.semantics.Semantics;

public final class ReoProgram<T extends Semantics<T>> {
	
	private final String name;
	
	private final String reofile;

	private final ReoConnector<T> connector;
	
	public ReoProgram(String name, String reofile, ReoConnector<T> connector) {
		this.name = name;
		this.reofile = reofile;
		this.connector = connector;
	}
	
	public String getName() {
		return name;
	}
	
	public String getFile() {
		return reofile;
	}
	
	public ReoConnector<T> getConnector() {
		return connector;
	}
}
