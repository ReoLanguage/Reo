package nl.cwi.reo.semantics.rbautomaton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.predicates.Formula;
import nl.cwi.reo.util.Monitor;

public class Rules {

	private Map<Port,Role> sync;
	private Formula f;
	
	public Rules(Map<Port,Role> sync, Formula f){
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
	
	public Rules rename(Map<Port, Port> links) {
		Map<Port,Role> map = new HashMap<Port,Role>();
		for (Port p : sync.keySet()){
			if(links.containsKey(p)){
				map.put(links.get(p),sync.get(p));
			}
			else
				map.put(p, sync.get(p));
		}
			
		return new Rules(map,f.rename(links));
	}
	
	public boolean hasEdge(Rules r){
		Map<Port,Role> map = r.getSync();
		boolean hasEdge = false;
		for(Port p : sync.keySet()){
			if(sync.get(p).getValue()==1 && map.get(p).getValue()==0){
				return false;
			}
			if(sync.get(p).getValue()==2 && map.get(p).getValue()==0){
				return false;
			}
			if(sync.get(p).getValue()==1 && map.get(p).getValue()==1){
				return false;
			}
		}
		return true;
	}
	
	public String toString(){
		String s = "{ (";
		int i = 0;
		for(Port p : sync.keySet()){
			i++;
			s=s+p.getName()+":"+sync.get(p).toString()+(i==(sync.size())?":":",")+ " ";
		}
		s=s+") , ";
		s=s+f.toString() + " }";
		return s;
	}
	
	public @Nullable Rules evaluate(Scope s, Monitor m){
		
		return new Rules(sync,f.evaluate(s, m));  
	}
}
