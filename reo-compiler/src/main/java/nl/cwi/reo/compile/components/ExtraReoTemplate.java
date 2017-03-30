package nl.cwi.reo.compile.components;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import nl.cwi.reo.interpret.connectors.Language;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.symbolicautomata.MemoryCell;

public final class ExtraReoTemplate {

	private final String reofile;

	private final String packagename;

	private final String name;

	private final Set<Port> ports;

	private final List<TransitionRule> transitions;

	private final Set<MemoryCell> mem;

	
	public ExtraReoTemplate(String reofile, String packagename, String name, Set<Port> ports, List<TransitionRule> transitions, Set<MemoryCell> memoryCells) {
		this.reofile = reofile;
		this.packagename = packagename;
		this.name = name;
//		this.ports = Collections.unmodifiableList(ports);
		this.ports=ports;
		this.transitions=transitions;
		this.mem=memoryCells;
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
	public List<TransitionRule> getTransitions(){
		return transitions;
	}
	public Set<MemoryCell> getMem(){
		return mem;
	}


	public String getCode(Language L) {
		STGroup group = null;
		switch (L) {
		case JAVA:
			group = new STGroupFile("JavaST.stg");
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

		ST temp1 = group.getInstanceOf("main");
		ST temp2 = group.getInstanceOf("Component");
		temp1.add("S", this);
		temp2.add("S", this);
		
		String path = "/home/e-spin/workspace/Reo/reo-runtime-java/src/main/java";
		
		try {
			Files.write(Paths.get(path, "test.java"), Arrays.asList(temp1.render()),
					Charset.defaultCharset());
			Files.write(Paths.get(path, "Component1.java"), Arrays.asList(temp2.render()),
					Charset.defaultCharset());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		temp1.add("S", this);
		return temp1.render();
	}
}
