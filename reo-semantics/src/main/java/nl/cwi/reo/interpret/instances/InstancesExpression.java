package nl.cwi.reo.interpret.instances;

import nl.cwi.reo.interpret.Expression;
import nl.cwi.reo.interpret.connectors.Semantics;
import nl.cwi.reo.interpret.terms.Term;

/**
 * Interpretation of a (set of) instances.
 * @param <T> Reo semantics type
 */
public interface InstancesExpression<T extends Semantics<T>> extends Expression<Instances<T>>,Term {

}
