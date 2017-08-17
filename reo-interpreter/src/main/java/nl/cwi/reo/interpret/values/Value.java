package nl.cwi.reo.interpret.values;

import nl.cwi.reo.interpret.terms.Term;

/**
 * A term, but not an identifier. That is, a component definition, component
 * instance, or a data item, such as a string, integer, boolean, etc.
 * 
 * <p>
 * Every implementing class is immutable.
 */
public interface Value extends Term {

}
