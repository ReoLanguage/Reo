package nl.cwi.reo.semantics;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Semantics<T> {
	
	public Set<String> getInterface();
	
	public SemanticsType getType();
		
	public T getNode(List<Port> node);
	
	public T rename(Map<String, Port> links);

	public T evaluate(Map<String, String> params);
	
	public T compose(List<T> automata);
	
	public T restrict(Set<String> intface);
	
}
