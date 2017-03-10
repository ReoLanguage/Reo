/**
 * 
 */
package nl.cwi.reo.compile.targetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import nl.cwi.reo.interpret.ports.Port;

/**
 *
 */
public final class Automaton implements Component {
	
	private final String name;
	
	private final String reofile;
	
	private final List<Port> ports;
	
	private final Behavior behavior;
	
	private final SortedSet<String> states;
	
	public Automaton(String name, List<Port> ports, Behavior behavior, SortedSet<String> states, String reofile) {
		this.name = name;
		this.ports = ports;
		this.behavior = behavior;
		this.states = states;
		this.reofile = reofile;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<Port> getInterface() {
		return ports;
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
