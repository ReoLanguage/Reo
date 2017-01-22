package nl.cwi.reo.semantics;

import java.util.Set;

import nl.cwi.reo.interpret.Evaluable;
import nl.cwi.reo.semantics.Semantics;

public interface Component1<T extends Semantics<T>> extends Evaluable<Component1<T>> {
	
	public Set<Port<T>> getInterface();

}
