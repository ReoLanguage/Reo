package nl.cwi.reo.interpret.sets;

import nl.cwi.reo.interpret.instances.InstanceExpression;
import nl.cwi.reo.semantics.Semantics;

/**
 * Interpretation of an atomic/composite set definition.
 * @param <T> Reo semantics type
 */
public interface SetExpression<T extends Semantics<T>> extends InstanceExpression<T> {

}
