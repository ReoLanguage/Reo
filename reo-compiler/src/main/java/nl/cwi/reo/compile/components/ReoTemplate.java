package nl.cwi.reo.compile.components;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.cwi.reo.interpret.ports.Port;

public final class ReoTemplate {

	private final String reofile;

	private final String packagename;

	private final String name;

	private final Set<Port> ports;

	private final List<Component> components;

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

	public String getFile() {
		return reofile;
	}

	public String getPackage() {
		return packagename;
	}

	public String getName() {
		return name;
	}

	public Set<Port> getPorts() {
		return ports;
	}

	public List<Component> getComponents() {
		return components;
	}
}
