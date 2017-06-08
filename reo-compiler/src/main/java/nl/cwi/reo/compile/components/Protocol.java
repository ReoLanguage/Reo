/**
 * 
 */
package nl.cwi.reo.compile.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.predicates.MemCell;

/**
 * Compiled automaton that is independent of the target language.
 */
public final class Protocol implements Component {
	
	public final boolean protocol = true;

	private final String name;

	private final Set<Port> ports;

	public final Set<Transition> transitions;

	private final Map<MemCell, Object> initial;

	private final Set<String> state = new HashSet<>();

	private final Map<Port,Integer> listPort = new HashMap<>();
	
	public Protocol(String name, Set<Port> ports, Set<Transition> transitions, Map<MemCell, Object> initial) {
		this.name = name;
		this.ports = ports;
		this.transitions = transitions;
		this.initial = initial;
	}

	public Set<Transition> getTransitions() {
		return transitions;
	}

	public Map<MemCell, Object> getInitial() {
		return initial;
	}

	public String getName() {
		return name;
	}

	public Set<Port> getPorts() {
		return ports;
	}
	
	public Map<Port,Integer> getListPort() {
		int i=0;
		for(Port p : ports){
			listPort.put(p,i);
			i++;
		}
		return listPort;
	}
	
	public Set<MemCell> getMem() {
		return initial.keySet();
	}
	
	public Set<String> getState(){
		for(MemCell m : initial.keySet()){
			if(initial.get(m)!=null)
				state.add("m(" + m.getName().substring(1) + "," + initial.get(m).toString()+")");
			else
				state.add("m(" + m.getName().substring(1) + "," +  "*)");
		}
		for(Port p : ports){
			if(p.isInput()){
				state.add("in(" + p.getName().substring(1) + ")");
				state.add("p(" + p.getName().substring(1) + ",*)");
			}
			else{
				state.add("out("+p.getName().substring(1)+")");
				state.add("p("+p.getName().substring(1)+ ",*)");
			}
		}
		return state;
	}
}
