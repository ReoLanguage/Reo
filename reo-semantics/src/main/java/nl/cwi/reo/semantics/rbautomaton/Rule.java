package nl.cwi.reo.semantics.rbautomaton;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.predicates.Formula;
import nl.cwi.reo.util.Monitor;

public class Rule {

	private Map<Port,Role> sync;
	private Formula f;
	
	public Rule(Map<Port,Role> sync, Formula f){
		this.sync=sync;
		this.f=f;
	}
	
	public Map<Port,Role> getSync(){
		return sync;
	}
	
	public Formula getFormula(){
		return f;
	}
	
	public Set<Port> getFiringPorts(){
		Set<Port> setPort = new HashSet<Port>();
		for(Port p : sync.keySet()){
			if(sync.get(p).getValue()!=0){
				setPort.add(p);
			}
		}
		return setPort;
	}
	
	public Set<Port> getAllPorts(){
		Set<Port> setPort = new HashSet<Port>();
		for(Port p : sync.keySet()){
			setPort.add(p);
		}
		return setPort;
	}
	
	public Rule rename(Map<Port, Port> links) {
		Map<Port,Role> map = new HashMap<Port,Role>();
		for (Port p : sync.keySet()){
			if(links.containsKey(p)){
				map.put(links.get(p),sync.get(p));
			}	
			else
				map.put(p, sync.get(p));
		}
			
		return new Rule(map,f.rename(links));
	}
	
	/**
	 * 
	 * @param r
	 * @return
	 */
	public Rule merge(Rule r){
		/*
		 * This method assumes that both rules are in Conjunctive Normal Form
		 */
		
//		List<Formula> f = new ArrayList<Formula>();
//		Map<Port,Role> map = new HashMap<Port,Role>();
//		
//		f.add(r.getFormula());
//		for(Port p :  )
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString(){		
		String s = "{";
		Iterator<Map.Entry<Port, Role>> iter = this.sync.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<Port, Role> pair = iter.next();
			switch (pair.getValue()) {
			case BLOCK:
				s += "~";
				break;
			case FIRE:
				s += "?";
				break;
			case TRIGGER:
				s += "";
				break;
			default:
				break;
			}
			s += pair.getKey() + (iter.hasNext() ? ", " : "");
		}
		s += "} ";
		if(f!=null)
			s += f.toString();
		return s;
	}
	
	public @Nullable Rule evaluate(Scope s, Monitor m){
		
		return new Rule(sync,f.evaluate(s, m));  
	}
}
