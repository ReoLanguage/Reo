package nl.cwi.reo.automata;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Semantics<T> {
	
	public T compose(List<T> automata);
	
	public T restrict(Set<String> intface);
	
	public T rename(Map<String, String> links);
	
}
