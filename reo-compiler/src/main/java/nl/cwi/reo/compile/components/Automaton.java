/**
 * 
 */
package nl.cwi.reo.compile.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import nl.cwi.reo.interpret.ports.Port;

/**
 *
 */
public final class Automaton implements Definition {

	private final String name;

	private final String reofile;

	private final String packagename;

	private final List<Port> ports;

	private final Behavior behavior;

	public final Map<String, Set<Transition>> out;

	private final String initial;

	public Automaton(String name, List<Port> ports, Behavior behavior, String reofile, Map<String, Set<Transition>> out,
			String initial, String packagename) {
		this.name = name;
		this.ports = ports;
		this.behavior = behavior;
		this.reofile = reofile;
		this.out = out;
		this.initial = initial;
		this.packagename = packagename;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<Port> getInterface() {
		return ports;
	}

	public Map<String, Set<Transition>> getTransitions() {
		return out;
	}

	public String getPackage() {
		return packagename;
	}

	public String getFile() {
		return reofile;
	}

	public String getInitial() {
		return initial;
	}

	@Override
	public String getRun() {
		return "the whole automaton here";
	}

	@Override
	public String getActivate() {
		if (behavior == Behavior.PASSIVE) {
			STGroup group = new STGroupFile("Automaton.stg", '$', '$');
			ST component = group.getInstanceOf("automaton");

			component.add("name", name);

			component.add("originalfile", reofile);

			List<Map<String, String>> ports = new ArrayList<Map<String, String>>();
			Map<String, String> port = new HashMap<String, String>();
			port.put("name", "a");
			port.put("type", "String");
			port.put("inpt", "");
			ports.add(port);
			Map<String, String> port2 = new HashMap<String, String>();
			port2.put("name", "b");
			port2.put("type", "String");
			ports.add(port2);

			component.add("ports", ports);

			return component.render();
		}
		return "notify();";
	}

	@Override
	public Behavior getBehavior() {
		return behavior;
	}

	@Override
	public String getReoFile() {
		return reofile;
	}

}
