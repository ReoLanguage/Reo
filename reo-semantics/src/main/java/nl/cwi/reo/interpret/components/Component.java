package nl.cwi.reo.interpret.components;

import nl.cwi.reo.interpret.connectors.Semantics;
import nl.cwi.reo.interpret.instances.Set;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.terms.TermList;
import nl.cwi.reo.interpret.values.Value;
import nl.cwi.reo.interpret.variables.VariableListExpression;

public interface Component<T extends Semantics<T>> extends Value {

	public Set<T> instantiate(TermList values, VariableListExpression<Port> ports);
	
}
