package nl.cwi.reo.interpret.instances;

import nl.cwi.reo.interpret.Expression;
import nl.cwi.reo.semantics.Semantics;

/**
 * Interpretation of a (set of) instances.
 * 
 * @param <T>
 *            Reo semantics type
 */
public interface InstanceExpression<T extends Semantics<T>> extends Expression<Instance<T>> {

}
