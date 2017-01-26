package nl.cwi.reo.interpret.expressions;

import java.util.Map;

import nl.cwi.reo.semantics.api.Expression;

/**
 * A ValueExpression is an expression that cannot evaluate to a list of expressions.
 * For example, a variable range a[1..3] evaluates to a list &lt;a[1],a[2],a[3]&gt;.
 */
public interface ValueExpression extends Expression {
	
	public ValueExpression evaluate(Map<String, Expression> params);

}
