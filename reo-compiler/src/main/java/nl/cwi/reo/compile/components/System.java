package nl.cwi.reo.compile.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.cwi.reo.interpret.ports.Port;

public final class System {

	private final String reofile;

	private final String packagename;

	private final String name;

	private final List<Port> ports;

	private final List<Instance> instances;

	private final List<Definition> definitions;

	public System(String reofile, String packagename, String name, List<Port> ports, List<Instance> instances,
			List<Definition> definitions) {
		this.reofile = reofile;
		this.packagename = packagename;
		this.name = name;
		this.ports = ports;
		this.instances = instances;
		List<Definition> defs = new ArrayList<Definition>();
		for (Instance I : instances) 
			if (!defs.contains(I.getDefinition()))
				defs.add(I.getDefinition());
		this.definitions = Collections.unmodifiableList(definitions);
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

	public List<Port> getPorts() {
		return ports;
	}

	public List<Instance> getInstances() {
		return instances;
	}

	public List<Instance> getProactives() {
		List<Instance> active = new ArrayList<Instance>();
		for (Instance I : instances) 
			if (I.getDefinition().getBehavior() == Behavior.PROACTIVE)
				active.add(I);
		return active;
	}

	public List<Definition> getComponents() {
		return definitions;
	}
}
