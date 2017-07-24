package nl.cwi.reo.compile.components;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.cwi.reo.interpret.ports.Port;

// TODO: Auto-generated Javadoc
/**
 * The Class ReoTemplate.
 */
public final class ReoTemplate {

	/** The reofile. */
	private final String reofile;

	/** The packagename. */
	private final String packagename;

	/** The name. */
	private final String name;

	/** The ports. */
	private final Set<Port> ports;

	/** The components. */
	private final List<Component> components;

	/**
	 * Instantiates a new reo template.
	 *
	 * @param reofile
	 *            the reofile
	 * @param packagename
	 *            the packagename
	 * @param name
	 *            the name
	 * @param components
	 *            the components
	 */
	public ReoTemplate(String reofile, String packagename, String name, List<Component> components) {
		this.reofile = reofile;
		this.packagename = packagename;
		this.name = name;
		this.components = Collections.unmodifiableList(components);
		Set<Port> P = new HashSet<Port>();
		for (Component c : components)
			P.addAll(c.getPorts());
		this.ports = Collections.unmodifiableSet(P);
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
	 * Gets the package.
	 *
	 * @return the package
	 */
	public String getPackage() {
		return packagename;
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
	 * Gets the ports.
	 *
	 * @return the ports
	 */
	public Set<Port> getPorts() {
		return ports;
	}

	/**
	 * Gets the components.
	 *
	 * @return the components
	 */
	public List<Component> getComponents() {
		return components;
	}
}
