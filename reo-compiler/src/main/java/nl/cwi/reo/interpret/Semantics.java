package nl.cwi.reo.interpret;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Semantics<T> extends Evaluable<Semantics<T>> {
	
	public Set<String> getInterface();
	
	public T getNode(List<NodeName> node);
	
	//public T compose(List<T> automata);
	
	//public T restrict(Set<String> intface);
	
	public T rename(Map<String, String> links);
	
}
