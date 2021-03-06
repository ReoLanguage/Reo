package nl.cwi.reo.interpret;

import java.io.Serializable;

import nl.cwi.reo.interpret.connectors.ReoConnector;

// TODO: Auto-generated Javadoc
/**
 * The Class ReoProgram.
 *
 * @param <T>
 *            the generic type
 */
public final class ReoProgram {


	/** The name. */
	private final String name;

	/** The reofile. */
	private final String reofile;

	/** The connector. */
	private final ReoConnector connector;

	/**
	 * Instantiates a new reo program.
	 *
	 * @param name
	 *            the name
	 * @param reofile
	 *            the reofile
	 * @param connector
	 *            the connector
	 */
	public ReoProgram(String name, String reofile, ReoConnector connector) {
		this.name = name;
		this.reofile = reofile;
		this.connector = connector;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the file.
	 *
	 * @return the file
	 */
	public String getFile() {
		return reofile;
	}

	/**
	 * Gets the connector.
	 *
	 * @return the connector
	 */
	public ReoConnector getConnector() {
		return connector;
	}
}
