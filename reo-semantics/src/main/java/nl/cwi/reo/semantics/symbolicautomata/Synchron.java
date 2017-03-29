package nl.cwi.reo.semantics.symbolicautomata;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.util.Monitor;

public class Synchron implements Formula {
	
	private final Port p;
	
	private final boolean isSync; 
	
	public Synchron(Port p) {
		this.p = p;
		this.isSync = true;
	}

	public Synchron(Port p, boolean isSync) {
		this.p = p;
		this.isSync = isSync;
	}
	
	public Port getPort() {
		return p;
	}

	@Override
	public Formula getGuard() {
		return this;
	}

	@Override
	public Map<Port, Term> getAssignment() {
		return new HashMap<Port, Term>();
	}

	@Override
	public Formula rename(Map<Port, Port> links) {
		Port b = links.get(p);
		if (b != null)
			return new Synchron(b);
		return this;
	}

	@Override
	public Set<Port> getInterface() {
		if(isSync)
			return new HashSet<Port>(Arrays.asList(p));
		return new HashSet<Port>();
	}

	public String toString(){
		if(isSync)
			return  p.toString();
		return "~" + p.toString();
	}
	
	@Override
	public @Nullable Formula evaluate(Scope s, Monitor m) {
		return this;
	}

	public boolean isSync(){
		return isSync;
	}
	
	@Override
	public Formula DNF() {
		return this;
	}

	@Override
	public Formula propNegation(boolean isNegative) {
		return new Synchron(this.p,!isNegative);
	}
	
}
