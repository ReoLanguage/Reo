/**
 * 
 */
package nl.cwi.reo.templates.maude;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.predicates.Constant;
import nl.cwi.reo.semantics.predicates.Function;
import nl.cwi.reo.semantics.predicates.MemoryVariable;
import nl.cwi.reo.semantics.predicates.Term;
import nl.cwi.reo.templates.Protocol;
import nl.cwi.reo.templates.Transition;

/**
 * Maude Protocol template
 */
public final class MaudeProtocol extends Protocol {

	/** The initial state. */
	private Set<String> state = new HashSet<>(); 

	/** Set of variables involved in rew system. */
	private Set<String> variables = new HashSet<>(); 

	/** Set of threshold variables involved in rew system. */
	private Set<String> thVar = new HashSet<>(); 

	/** Port renaming */
	public Map<Port,String> renaming = new HashMap<>();

	/** Counter */
	int nb =0;

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
			variables.add("d_"+m.getName());
			if (getInitial().get(m) != null){
				s = s + "m(" + m.getName().substring(1) + "," + getInitial().get(m).toString() + ") ";
			}
			else{
				s = s + "m(" + m.getName().substring(1) + "," + "*) ";
			}
		}
		s = s + "\n";
		for (Port p : getPorts()) {
			variables.add("d_"+p.getName());
			if (p.isInput()) {
				s = s + "in(\"" + p.getName() + "\")" + " p(\"" + p.getName() + "\",*) ";
				s = s + "link(\"" + p.getName() + "\",\"q" + p.getName() + "\") q(\"q"
						+ p.getName() + "\",0,*) \n";
			} else {
				s = s + "out(\"" + p.getName() + "\")" + " p(\"" + p.getName() + "\",*) ";
				s = s + "link(\"q" + p.getName() + "\",\"" + p.getName() + "\") q(\"q"
						+ p.getName() + "\",0,*) \n";
			}
		}
		
		Transition t = getTransitions().iterator().next();
		if(t instanceof MaudeTransition){
			s=s+thresholdState(((MaudeTransition) t).getTh())+"trace(nil)";
		}
		nb=0;
		state.add(s);
		return state;
	}
	
	public String thresholdState(Term t){
		String s = "";
		
		if(t instanceof Function){
			for(Term _t : ((Function) t).getArgs()){
				s=s+thresholdState(_t)+" ";
			}
		}
		
		else if(t instanceof Constant){
			s= s+ "th(\"t"+nb +"\", ws("+(((nb&1)==0)?"i":"j")+"))";
			thVar.add("t"+nb);
			nb++;
		}
		
		return s;
	}
	
	/** Set of threshold */
	
	public Set<String> getThVar(){
		return thVar;
	}
	
	
	/**
	 * Get rewrite system variables
	 */
	
	public Set<String> getVariables(){
		
		return variables;
	}

	
}
