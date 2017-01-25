package nl.cwi.reo.semantics.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

public interface Semantics<T> {
	
	public SortedSet<Port> getInterface();
	
	public SemanticsType getType();
		
	public T getNode(SortedSet<Port> node);
	
	public T rename(Map<Port, Port> links);

	public T evaluate(Map<String, String> params);
	
	public T compose(List<T> automata);
	
	public T restrict(Collection<? extends Port> intface);
	
}
