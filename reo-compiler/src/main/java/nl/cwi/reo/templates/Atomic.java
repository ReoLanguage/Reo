/**
 * 
 */
package nl.cwi.reo.templates;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.cwi.reo.interpret.ports.Port;

// TODO: Auto-generated Javadoc
/**
 * Compiled atomic component that is independent of the target language.
 */
public final class Atomic implements Component {

	/**
	 * Flag for string template.
	 */
	public final boolean atomic = true;

	/** The name. */
	private final String name;

	/** The params. */
	private final List<String> params;

	/** The ports. */
	private final Set<Port> ports;

	/** The list port. */
	private final Map<Port, Integer> listPort = new HashMap<>();

	/** The call. */
	private final String call;

	/**
	 * Instantiates a new atomic.
	 *
	 * @param name
	 *            the name
	 * @param params
	 *            the params
	 * @param ports
	 *            the ports
	 * @param call
	 *            the call
	 */
	public Atomic(String name, List<String> params, Set<Port> ports, String call) {
		this.name = name;
		this.params = params;
		this.ports = ports;
		this.call = call;
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
	 * Gets the parameters.
	 *
	 * @return the parameters
	 */
	public List<String> getParameters() {
		return params;
	}

	/**
	 * Gets the list port.
	 *
	 * @return the list port
	 */
	public Map<Port, Integer> getListPort() {
		int i = 0;
		for (Port p : ports) {
			listPort.put(p, i);
			i++;
		}
		return listPort;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.compile.components.Component#getPorts()
	 */
	public Set<Port> getPorts() {
		return ports;
	}

	/**
	 * Gets the call.
	 *
	 * @return the call
	 */
	public String getCall() {
		return call;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "atomic: " + call + params + ports;
	}
}
