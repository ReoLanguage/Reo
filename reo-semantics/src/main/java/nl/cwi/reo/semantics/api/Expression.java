package nl.cwi.reo.semantics.api;

/**
 * An abstract representation of a expression in the Reo language. Examples of expressions include 
 * strings expressions, boolean expressions, integer expressions, floating point expressions, 
 * component expressions, and lists of expressions.
 * 
 * An expressions can be evaluated to obtain a new expression wherein values are substituted.
 */
public interface Expression extends Evaluable<Expression> {

}
