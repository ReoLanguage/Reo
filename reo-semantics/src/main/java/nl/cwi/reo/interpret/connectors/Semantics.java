package nl.cwi.reo.interpret.connectors;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.cwi.reo.interpret.Expression;
import nl.cwi.reo.interpret.ports.Port;

public interface Semantics<T> extends Expression<T> {
	
	public Set<Port> getInterface();
	
	public SemanticsType getType();
		
	public T getNode(Set<Port> node);
	
	public T rename(Map<Port, Port> links);
	
	public T compose(List<T> automata);
	
	public T restrict(Collection<? extends Port> intface);
	
}