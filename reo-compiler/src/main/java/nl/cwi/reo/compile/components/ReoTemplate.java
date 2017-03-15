package nl.cwi.reo.compile.components;

import java.util.Collections;
import java.util.List;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import nl.cwi.reo.interpret.connectors.Language;
import nl.cwi.reo.interpret.ports.Port;

public final class ReoTemplate {

	private final String reofile;

	private final String packagename;

	private final String name;

	private final List<Port> ports;

	private final List<Instance> instances;

	private final List<Definition> definitions;

	public ReoTemplate(String reofile, String packagename, String name, List<Port> ports, List<Instance> instances, List<Definition> definitions) {
		this.reofile = reofile;
		this.packagename = packagename;
		this.name = name;
		this.ports = Collections.unmodifiableList(ports);
		this.instances = Collections.unmodifiableList(instances);
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

	public List<Definition> getComponents() {
		return definitions;
	}

	public String getCode(Language L) {
		STGroup group = null;
		switch (L) {
		case JAVA:
			group = new STGroupFile("Java.stg");
			break;
		case C11:
			break;
		case PRT:
			break;
		case TEXT:
			break;
		default:
			break;
		}

		ST temp = group.getInstanceOf("main");
		temp.add("S", this);
		return temp.render();
	}
}
