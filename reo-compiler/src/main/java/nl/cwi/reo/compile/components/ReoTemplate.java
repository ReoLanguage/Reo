package nl.cwi.reo.compile.components;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import nl.cwi.reo.interpret.connectors.Language;
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

	public String generateCode(Language L) {
		STGroup group = null;
		
		switch (L) {
		case JAVA:
			group = new STGroupFile("Java.stg");
			break;
		default:
			return "";
		}

		ST temp = group.getInstanceOf("main");
		temp.add("S", this);
		
		String path = "../reo-runtime-java/src/main/java";
		
		try {
			Files.write(Paths.get(path, name+".java"), Arrays.asList(temp.render()),
					Charset.defaultCharset());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return temp.render(72);
	}
}
