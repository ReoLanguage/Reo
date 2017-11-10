/**
 * 
 */
package nl.cwi.reo.templates;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.predicates.MemoryVariable;

/**
 * Maude Protocol template
 */
public final class MaudeProtocol extends Protocol {

	/** The initial state. */
	private Set<String> state = new HashSet<>(); 

	/** Set of variables involved in rew system. */
	private Set<String> variables = new HashSet<>(); 

	/** Port renaming */
	public Map<Port,String> renaming = new HashMap<>();
	
	/**
	 * Instantiates a new protocol.
	 *
	 * @param name
	 *            the name
	 * @param ports
	 *            the ports
	 * @param transitions
	 *            the transitions
	 * @param initial
	 *            the initial
	 */
	public MaudeProtocol(String name, Set<Port> ports, Set<Transition> transitions, Map<MemoryVariable, Object> initial) {	
		super(name,ports,transitions,initial);
		getState();
	}

	/**
	 * Gets initial state.
	 *
	 * @return the state
	 */
	public Set<String> getState() {
		String s = "";
		for (MemoryVariable m : getInitial().keySet()) {
			variables.add("d_m"+m.getName().substring(1));
			if (getInitial().get(m) != null){
				s = s + "m(" + m.getName().substring(1) + "," + getInitial().get(m).toString() + ") ";
			}
			else{
				s = s + "m(" + m.getName().substring(1) + "," + "*) ";
			}
		}
		s = s + "\n";
		for (Port p : getPorts()) {
			variables.add("d_"+p.getName().substring(1));
			if (p.isInput()) {
				s = s + "in(\"p" + p.getName().substring(1) + "\")" + " p(\"p" + p.getName().substring(1) + "\",*) ";
				s = s + "link(\"p" + p.getName().substring(1) + "\",\"q" + p.getName().substring(1) + "\") q(\"q"
						+ p.getName().substring(1) + "\",0,*) \n";
			} else {
				s = s + "out(\"p" + p.getName().substring(1) + "\")" + " p(\"p" + p.getName().substring(1) + "\",*) ";
				s = s + "link(\"q" + p.getName().substring(1) + "\",\"p" + p.getName().substring(1) + "\") q(\"q"
						+ p.getName().substring(1) + "\",0,*) \n";
			}
		}
		state.add(s);
		return state;
	}

	
	/**
	 * Get rewrite system variables
	 */
	
	public Set<String> getVariables(){
		
		return variables;
	}

	
}
