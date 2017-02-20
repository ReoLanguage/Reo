package nl.cwi.reo.interpret.components;

import nl.cwi.reo.interpret.Expression;
import nl.cwi.reo.interpret.connectors.Semantics;

/**
 * Interpretation of a component definition.
 * @param <T> Reo semantics type
 */
public interface ComponentExpression<T extends Semantics<T>> extends Expression<ComponentDefinition<T>> {

}
