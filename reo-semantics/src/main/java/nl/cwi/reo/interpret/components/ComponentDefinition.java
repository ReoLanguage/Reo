package nl.cwi.reo.interpret.components;

import nl.cwi.reo.interpret.connectors.ReoConnector;
import nl.cwi.reo.interpret.connectors.Semantics;
import nl.cwi.reo.interpret.terms.Terms;
import nl.cwi.reo.interpret.values.Value;
import nl.cwi.reo.interpret.variables.VariableList;

public interface ComponentDefinition<T extends Semantics<T>> extends Value {

	public ReoConnector<T> instantiate(Terms values, VariableList ports);
	
}
